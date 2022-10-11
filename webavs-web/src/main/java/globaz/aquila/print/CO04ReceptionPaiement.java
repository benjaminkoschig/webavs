package globaz.aquila.print;

import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.process.ebill.EBillHelper;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import globaz.osiris.process.ebill.EBillTypeDocument;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Formule 44 ou Reclamer frais et interets
 * </p>
 * 
 * @author Alexandre Cuva, 19-aug-2004
 */
public class CO04ReceptionPaiement extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CENT_DEFAUT = "XX";
    public static final String MONTANT_DEFAUT = "XXXX";
    public static final String NUMERO_REFERENCE_INFOROM = "0040GCO";
    public static final String OCRB_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static final String REFERENCE_NON_FACTURABLE_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final long serialVersionUID = -1361271953197021651L;
    private static final String TEMPLATE_NAME = "CO_04_RECEPTION_PAIEMENT_AF_BVR_QR";

    private List<CAInteretManuelVisualComponent> interetCalcule = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(CO04ReceptionPaiement.class);

    /* eBill fields */
    public Map<PaireIdExterneEBill, List<Map>> lignesReclamation = new LinkedHashMap();
    public Map<PaireIdExterneEBill, String> referencesReclamation = new LinkedHashMap();
    private EBillHelper eBillHelper = new EBillHelper();
    private int factureEBill = 0;
    private String eBillTransactionID = "";
    private Boolean eBillPrintable = Boolean.FALSE;

    /**
     * Crée une nouvelle instance de la classe CO04ReceptionPaiement.
     * 
     * @throws Exception
     */
    public CO04ReceptionPaiement() {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO04ReceptionPaiement(BSession parent) throws FWIException {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getEBillTransactionID() {
        return eBillTransactionID;
    }

    public void setEBillTransactionID(String eBillTransactionID) {
        this.eBillTransactionID = eBillTransactionID;
    }

    public Boolean getEBillPrintable() {
        return eBillPrintable;
    }

    public void setEBillPrintable(Boolean eBillPrintable) {
        this.eBillPrintable = eBillPrintable;
    }

    /**
     * Ajoute les IM facturé à cette étape au dataSource
     * 
     * @param dataSource
     * @return montant total des IM
     */
    private FWCurrency addMontantIM(List<Map<String, String>> dataSource) {
        FWCurrency totalIM = new FWCurrency("0");
        if (getInteretCalcule() == null) {
            return totalIM;
        }

        for (CAInteretManuelVisualComponent im : getInteretCalcule()) {
            Map<String, String> f = new HashMap<>();
            f.put(COParameter.F1, im.getInteretMoratoire().getRubrique().getDescription(getLangue()));
            f.put(COParameter.F2, formatMontant(im.montantInteretTotalCalcule()));
            f.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));

            dataSource.add(f);
            totalIM.add(new FWCurrency(im.montantInteretTotalCalcule()));
        }
        return totalIM;
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        computeTotalPage();
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());
    }

    @Override
    public void afterExecuteReport() {
        if (curContentieux.getSection() != null && curContentieux.getSection().getCompteAnnexe() != null) {

            // Effectue le traitement eBill pour les documents concernés et les envoient sur le ftp
            boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(getSession());

            // On imprime eBill si :
            //  - eBillAquila est actif
            //  - le compte annexe possède un eBillAccountID
            //  - eBillPrintable est sélectioné sur l'écran d'impression
            //  - l'impression prévisionel n'est pas activée
            if (eBillAquilaActif && getEBillPrintable() && !getPrevisionnel()) {
                if(curContentieux.getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(curContentieux.getCompteAnnexe().getEBillAccountID())) {
                    try {
                        EBillSftpProcessor.getInstance();
                        traiterReclamationEBillAquila(curContentieux.getCompteAnnexe());
                        eBillHelper.ajouteInfoEBillToDocumentNotes(factureEBill, getDocumentInfo(), getSession());
                    } catch (Exception exception) {
                        LOGGER.error("Impossible de créer les fichiers eBill : " + exception.getMessage(), exception);
                        getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_FAILED") + exception.getCause().getMessage(), FWMessage.ERREUR, this.getClass().getName());
                    } finally {
                        EBillSftpProcessor.closeServiceFtp();
                    }
                }
            }
        }
        super.afterExecuteReport();
    }

    /**
     * Méthode permettant de traiter les Réclamations de frais et intérêts eBill
     * en attente d'être envoyé dans le processus actuel.
     */
    public void traiterReclamationEBillAquila(CACompteAnnexe compteAnnexe) throws Exception {
        for (Map.Entry<PaireIdExterneEBill, List<Map>> lignes : lignesReclamation.entrySet()) {
            if (curContentieux.getSection().getCompteAnnexe().getIdExterneRole().equals(lignes.getKey().getIdExterneRole())
                    && curContentieux.getSection().getIdExterne().equals(lignes.getKey().getIdExterneFactureCompensation())) {

                FAEnteteFacture entete = eBillHelper.generateEnteteFactureFictive(curContentieux.getSection(), getSession());
                String reference = referencesReclamation.get(lignes.getKey());
                List<JadePublishDocument> attachedDocuments = eBillHelper.findReturnOrRemoveAttachedDocuments(entete, getAttachedDocuments(), CO04ReceptionPaiement.class.getSimpleName(), false);

                if (!attachedDocuments.isEmpty()) {
                    creerFichierEBill(compteAnnexe, entete, lignes.getKey().getMontant(), lignes.getValue(), reference, attachedDocuments, curContentieux.getSection(), EBillTypeDocument.RECLAMATION_FRAIS_ET_INTERET);
                }
            }
        }
    }

    /**
     * Méthode permettant de créer la Réclamation de frais et intérêts eBill,
     * de générer et remplir le fichier puis de l'envoyer sur le ftp.
     *
     * @param compteAnnexe            : le compte annexe
     * @param entete                  : l'entête de la facture
     * @param montantFacture          : contient le montant total de la factures (seulement rempli dans le cas d'un bulletin de soldes ou d'un sursis au paiement)
     * @param lignes                  : contient les lignes
     * @param reference               : la référence BVR ou QR.
     * @param attachedDocuments       : la liste des fichiers crée par l'impression classique à joindre en base64 dans le fichier eBill
     * @param section                 : la section
     * @param typeDocument            : le type du document eBill
     * @throws Exception
     */
    private void creerFichierEBill(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, String montantFacture, List<Map> lignes, String reference, List<JadePublishDocument> attachedDocuments, CASection section, EBillTypeDocument typeDocument) throws Exception {

        // Génère et ajoute un eBillTransactionId dans l'entête de facture eBill
        entete.setEBillTransactionID(getEBillTransactionID());

        eBillHelper.creerFichierEBill(compteAnnexe, entete, null, montantFacture, lignes, null, reference, attachedDocuments, curContentieux.getDateExecution(), curContentieux.getProchaineDateDeclenchement(), null, getSession(), null, typeDocument);

        // Met à jour le status eBill de la section
        eBillHelper.updateSectionEtatEtTransactionID(section, entete.getEBillTransactionID(), getMemoryLog());

        factureEBill++;
    }

    /**
     * @throws FWIException
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO04ReceptionPaiement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RECEPTION_PAIEMENT"));
        setNumeroReferenceInforom(CO04ReceptionPaiement.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // destinataire est l'affilie
            destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
            _setLangueFromTiers(destinataireDocument);

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(curContentieux, true, false, true, getAdressePrincipale(destinataireDocument));
            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher toutes les lignes du titre du document
            StringBuilder body = new StringBuilder();

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            // formater le titre, les conventions de remplacement pour les lignes du titre sont:
            // {0} = numéro de poursuite
            // {1} = période
            // {2} = date de la section
            this.setParametres(
                    COParameter.T1,
                    formatMessage(body,
                            new Object[] { curContentieux.getNumPoursuite(), curContentieux.getPeriodeSection(),
                                    curContentieux.getSection().getDescription(getLangue()),
                                    formatDate(curContentieux.getSection().getDateSection()) }));

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
            // {0} = formule de politesse (ex: Madame, Monsieur)
            // {1} = date de la section
            this.setParametres(
                    COParameter.T2,
                    formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument),
                            formatDate(curContentieux.getSection().getDateSection()) }));

            // -- lignes détail
            // ------------------------------------------------------------------------------
            String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                    curContentieux.getIdSection());

            this.setParametres(COParameter.P_BASE, formatMontant(infoSection[0]));
            this.setParametres(COParameter.P_DEVISE, getCatalogueTextesUtil().texte(getParent(), 3, 2));

            List<Map<String, String>> dataSource = new ArrayList<>();

            dataSource.add(montantFactureInitiale(infoSection));

            List<Map<String, String>> situation = createSituationCompteDS(infoSection[2], COParameter.F1,
                    COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2),
                    ICOEtape.CS_FRAIS_ET_INTERETS_RECLAMES, infoSection[1]);
            dataSource.addAll(situation);

            FWCurrency montantTotal = curContentieux.getSection().getSoldeToCurrency();

            // Prepare la map des lignes de frais et intérêts réclamé eBill si propriété eBillAquila est active et si compte annexe de la facture inscrit à eBill et si eBillPrintable est sélectioné sur l'écran d'impression
            boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(getSession());
            if (eBillAquilaActif && getEBillPrintable() && curContentieux.getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(curContentieux.getCompteAnnexe().getEBillAccountID())) {
                lignesReclamation.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), (List) situation);
            }

            FWCurrency totalIM = addMontantIM(dataSource);

            // -- lignes pour nouveaux frais
            FWCurrency totalNouvellesTaxes = addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
                    getCatalogueTextesUtil().texte(getParent(), 3, 2));

            this.setDataSource(dataSource);
            FWCurrency bvr;

            // -- pied après détail
            // ---------------------------------------------------------------------
            this.setParametres(COParameter.T7, getCatalogueTextesUtil().texte(getParent(), 3, 8));
            this.setParametres(COParameter.T6, getCatalogueTextesUtil().texte(getParent(), 3, 2));

            if (totalNouvellesTaxes != null) {
                totalNouvellesTaxes.add(curContentieux.getSolde());
                totalNouvellesTaxes.add(totalIM);
                this.setParametres(COParameter.M6, formatMontant(totalNouvellesTaxes.toString()));
                bvr = totalNouvellesTaxes;
            } else {
                totalIM.add(curContentieux.getSolde());
                this.setParametres(COParameter.M6, formatMontant(totalIM.toString()));
                bvr = totalIM;
            }

            // rechercher tous les paragraphes du pied après détail
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n\n");

            if (CommonProperties.QR_FACTURE.getBooleanValue()) {
                // -- QR
                qrFacture = new ReferenceQR();
                qrFacture.setSession(getSession());
                // Initialisation des variables du document
                initVariableQR(bvr);
                // Génération du document QR
                qrFacture.initQR(this, qrFactures);
                referencesReclamation.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), qrFacture.getReference());
            } else {
                initBVR(bvr);
            }

            // formater le pied après détail, les conventions de remplacement pour les paragraphes sont:
            // {0} = délai
            // {1} = CCP de la caisse
            // {2} = formule de politesse (ex: Madame, Monsieur,)
            this.setParametres(
                    COParameter.T9,
                    formatMessage(body, new Object[] { getTransition().getDuree(), getNumeroCCP(),
                            getFormulePolitesse(destinataireDocument) }));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @return the interetCalcule
     */
    public List<CAInteretManuelVisualComponent> getInteretCalcule() {
        return interetCalcule;
    }

    /**
     * Initialise la partie du bulletin de versement du document
     * 
     * @param montantTotal
     * @throws Exception
     */
    private void initBVR(FWCurrency montantTotal) throws Exception {
        // -- BVR
        FWCurrency cMontant = new FWCurrency(0.00);
        cMontant.add(montantTotal);

        // Recherche les informations pour remplir le bordereau
        String montantSansCentime = JAUtil.createBigDecimal(cMontant.toString()).toBigInteger().toString();
        BigDecimal montantSansCentimeBigDecimal = JAUtil.createBigDecimal(montantSansCentime);
        BigDecimal montantAvecCentimeBigDecimal = JAUtil.createBigDecimal(cMontant.toString());
        String centimes = montantAvecCentimeBigDecimal.subtract(montantSansCentimeBigDecimal).toString()
                .substring(2, 4);
        montantSansCentime = globaz.globall.util.JANumberFormatter.formatNoRound(montantSansCentime);

        // commencer à écrire les paramètres
        String adresseDebiteur = "";
        try {
            adresseDebiteur = getAdressePrincipale(curContentieux.getCompteAnnexe().getTiers());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Informations pour la référence et l'OCRB
        ReferenceBVR bvr = new ReferenceBVR();
        bvr.setSession(getSession());
        bvr.setBVR(curContentieux.getSection(), cMontant.toString());
        try {
            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");
            super.setParametres(COParameter.P_SUBREPORT_QR_CURRENT_PAGE, getImporter().getImportPath() + "BVR_TEMPLATE_CURRENT_PAGE.jasper");

            super.setParametres(FWIImportParametre.PARAM_REFERENCE + "_X",
                    CO04ReceptionPaiement.REFERENCE_NON_FACTURABLE_DEFAUT);
            super.setParametres(COParameter.P_OCR + "_X", CO04ReceptionPaiement.OCRB_DEFAUT);
            super.setParametres(COParameter.P_FRANC + "_X", CO04ReceptionPaiement.MONTANT_DEFAUT);
            super.setParametres(COParameter.P_CENTIME + "_X", CO04ReceptionPaiement.CENT_DEFAUT);
            super.setParametres(COParameter.P_ADRESSE, bvr.getAdresse());
            super.setParametres(COParameter.P_ADRESSECOPY, bvr.getAdresse());
            super.setParametres(COParameter.P_COMPTE, bvr.getNumeroCC());// numéro
            // CC
            super.setParametres(COParameter.P_VERSE, bvr.getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());
            super.setParametres(COParameter.P_OCR, bvr.getOcrb());
            super.setParametres(COParameter.P_FRANC, JANumberFormatter.deQuote(montantSansCentime));
            super.setParametres(COParameter.P_CENTIME, centimes);
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la sommation : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * @param infoSection
     * @return
     */
    private HashMap<String, String> montantFactureInitiale(String[] infoSection) {
        HashMap<String, String> fields = new HashMap<>();
        fields.put(COParameter.F1, curContentieux.getSection().getDescription(getLangue()));
        fields.put(COParameter.F2, formatMontant(infoSection[0]));
        fields.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));
        return fields;
    }

    /**
     * @param interetCalcule
     *            the interetCalcule to set
     */
    public void setInteretCalcule(List<CAInteretManuelVisualComponent> interetCalcule) {
        this.interetCalcule = interetCalcule;
    }

}
