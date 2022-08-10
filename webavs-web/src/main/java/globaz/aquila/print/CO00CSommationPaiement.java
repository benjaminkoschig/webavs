/*
 * Cr�� le 10 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print;

import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.service.taxes.COTaxe;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.pdf.JadePdfUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.process.ebill.EBillHelper;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import globaz.osiris.process.ebill.EBillTypeDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1> Document : Sommation <br>
 * Niveaux et param�tres utilis�s : <br>
 * <ul>
 * <li>niveau 1 : Dump du niveau 1. <br>
 * {0} = nom section ; <br>
 * {1} = date de la section
 * <li>niveau 2 : Dump du niveau 2. <br>
 * {0} = formule de politesse <br>
 * {1} = niveau 9 avec la date rappel. <br>
 * {2} = date de la section
 * <li>niveau 3 : Position pris s�par�ments. Pas de param�tre possible.
 * <li>niveau 4 : Dump du niveau 4. <br>
 * {0} = formule de politesse <br>
 * {1} = date de d�lai du paiement
 * <li>niveau 5 : Dump du niveau. Ce niveau s'affiche uniquement pour les comptes annexes de cat�gorie employeur
 * (804002).
 * <li>niveau 9 : Si Premier rappel envoy�, dump du niveau 9. Param�tre possible : date d'execution.
 * </ul>
 * Document : Voies de droits <br>
 * Prend tous les niveaux et les concat�nes. Aucun param�tre possible.
 *
 * @author vre, sel
 */
public class CO00CSommationPaiement extends CODocumentManager {

    public static final String NOM_DOCUMENT_SOMMATION_CAP_CGAS = "Sommation CAP/CGAS";
    /**
     * Num�ro Inforom pour la sommation LTN
     */
    public static final String NUM_REF_SOMMATION_LTN = "0197GCO";
    /**
     * Num�ro Inforom
     */
    public static final String NUMERO_REFERENCE_INFOROM = "0022GCO";

    public static final String NUMERO_REFERENCE_INFOROM_SOMMATION_CAP_CGAS = "0297GCO";

    private static final long serialVersionUID = -3645938861761414428L;

    /**
     * Nom du document du catalogue de textes � utiliser pour les sommation LTN
     */
    private static final String SOMMATION_LTN = "sommation LTN";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final int STATE_IDLE = 0;
    private static final int STATE_LETTRE = 1;
    private static final int STATE_VD = 2;

    /**
     * Modele Jasper
     */
    private static final String TEMPLATE_NAME = "CO_00C_SOMMATION_AF_QR";

    /**
     * Le nom du mod�le Jasper pour les voies de droits
     */
    private static final String TEMPLATE_NAME_VD = "CO_00C_SOMMATION_VOIES_DROIT";

    private static final String TITLE_VOIES_DE_DROIT = "sommation voies de droit";
    private static final String TYPE_AFFILI_EMPLOY = "804002";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private ReferenceBVR bvr = null;
    private String dateDelaiPaiement = null;
    private int state = CO00CSommationPaiement.STATE_IDLE;

    private static final Logger LOGGER = LoggerFactory.getLogger(CO00CSommationPaiement.class);

    /* eBill fields */
    public Map<PaireIdExterneEBill, List<Map>> lignesSommation = new LinkedHashMap();
    public Map<PaireIdExterneEBill, String> referencesSommation = new LinkedHashMap();
    private EBillHelper eBillHelper = new EBillHelper();
    private int factureEBill = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe CO00CSommationPaiement.
     *
     * @throws Exception
     */
    public CO00CSommationPaiement() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe CO00SommationPaiement.
     *
     * @param session
     * @throws FWIException
     */
    public CO00CSommationPaiement(BSession session) throws FWIException {
        super(session, session.getLabel("AQUILA_SOMMATION"));
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see globaz.aquila.print.CODocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        super.getDocumentInfo().setDocumentTypeNumber(getNumeroReferenceInforom());
        try {
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());

            // Imprime les documents dans un fichier s�par� selon la propri�t� de la section
            if (curContentieux.getSection().getNonImprimable().booleanValue()) {
                getDocumentInfo().setSeparateDocument(true);
            }
        } catch (Exception e) {
            this._addError(e.toString());
        }
    }

    /**
     * Apr�s la cr�ation de tous les documents
     */
    @Override
    public void afterExecuteReport() {
        try {
            if (curContentieux.getSection() != null && curContentieux.getSection().getCompteAnnexe() != null) {

                // Effectue le traitement eBill pour les documents concern�s et les envoient sur le ftp
                boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(getSession());

                // On imprime eBill si :
                //  - eBillAquila est actif
                //  - le compte annexe poss�de un eBillAccountID
                //  - eBillPrintable est s�lection� sur l'�cran d'impression
                //  - l'impression pr�visionel n'est pas activ�e
                if (eBillAquilaActif && curContentieux.getEBillPrintable() && !curContentieux.getPrevisionnel()) {
                    if(curContentieux.getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(curContentieux.getCompteAnnexe().getEBillAccountID())) {
                        try {
                            EBillSftpProcessor.getInstance();
                            traiterSommationEBillAquila(curContentieux.getCompteAnnexe());
                            ajouteInfoEBillToEmail();
                        } catch (Exception exception) {
                            LOGGER.error("Impossible de cr�er les fichiers eBill : " + exception.getMessage(), exception);
                            getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_FAILED") + exception.getCause().getMessage(), FWMessage.ERREUR, this.getClass().getName());
                        } finally {
                            EBillSftpProcessor.closeServiceFtp();
                        }
                    }
                }
            }

            if ((getSession().getApplication().getProperty(CODocumentManager.GESTION_VERSO_AQUILA) == null)
                    || getSession().getApplication().getProperty(CODocumentManager.GESTION_VERSO_AQUILA)
                    .equals(CODocumentManager.AVEC_VERSO)) {
                this.mergePDF(getDocumentInfo(), true, 500, false, null, JadePdfUtil.DUPLEX_ON_FIRST);
            } else {
                this.mergePDF(getDocumentInfo(), true, 500, false, null);
            }

        } catch (Exception e) {
            this._addError(e.toString());
        }
        super.afterExecuteReport();
    }

    /**
     * M�thode permettant de traiter les sommations eBill
     * en attente d'�tre envoy� dans le processus actuel.
     */
    public void traiterSommationEBillAquila(CACompteAnnexe compteAnnexe) throws Exception {

        for (Map.Entry<PaireIdExterneEBill, List<Map>> lignes : lignesSommation.entrySet()) {
            if (curContentieux.getSection().getCompteAnnexe().getIdExterneRole().equals(lignes.getKey().getIdExterneRole())
                    && curContentieux.getSection().getIdExterne().equals(lignes.getKey().getIdExterneFactureCompensation())) {

                FAEnteteFacture entete = eBillHelper.generateEnteteFactureFictive(curContentieux.getSection(), getSession());
                String reference = referencesSommation.get(lignes.getKey());
                List<JadePublishDocument> attachedDocuments = eBillHelper.findReturnOrRemoveAttachedDocuments(entete, getAttachedDocuments(), CO00CSommationPaiement.class.getSimpleName(), false);

                if (!attachedDocuments.isEmpty()) {
                    creerFichierEBill(compteAnnexe, entete, lignes.getKey().getMontant(), lignes.getValue(), reference, attachedDocuments, curContentieux.getDateExecution(), curContentieux.getSection(), EBillTypeDocument.SOMMATION);
                }
            }
        }
    }

    private void ajouteInfoEBillToEmail() {
        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill, FWMessage.INFORMATION, this.getClass().getName());
        getDocumentInfo().setDocumentNotes(getDocumentInfo().getDocumentNotes() + getMemoryLog().getMessagesInString());
    }

    /**
     * M�thode permettant de cr�er la sommation eBill,
     * de g�n�rer et remplir le fichier puis de l'envoyer sur le ftp.
     *
     * @param compteAnnexe            : le compte annexe
     * @param entete                  : l'ent�te de la facture
     * @param montantFacture          : contient le montant total de la factures
     * @param lignes                  : contient les lignes
     * @param reference               : la r�f�rence BVR ou QR.
     * @param attachedDocuments       : la liste des fichiers cr�e par l'impression classique � joindre en base64 dans le fichier eBill
     * @param dateImprOuFactu         : la date d'execution ou de facturation du document
     * @param section                 : la section
     * @param typeDocument            : le type du document eBill
     * @throws Exception
     */
    private void creerFichierEBill(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, String montantFacture, List<Map> lignes, String reference, List<JadePublishDocument> attachedDocuments, String dateImprOuFactu, CASection section, EBillTypeDocument typeDocument) throws Exception {

        // G�n�re et ajoute un eBillTransactionId dans l'ent�te de facture eBill
        entete.addEBillTransactionID(getTransaction());

        // Met � jour le flag eBillPrinted dans l'ent�te de facture eBill
        entete.setEBillPrinted(true);

        // Met � jour le status eBill de la section
        eBillHelper.updateSectionEtatEtTransactionID(section, entete.getEBillTransactionID(), getMemoryLog());

        // Met � jour l'historique eBill du contentieux
        eBillHelper.updateHistoriqueEBillPrintedEtTransactionID(curContentieux, entete.getEBillTransactionID(), getMemoryLog());

        String dateEcheance = getDateDelaiPaiement();
        eBillHelper.creerFichierEBill(compteAnnexe, entete, null, montantFacture, lignes, null, reference, attachedDocuments, dateImprOuFactu, dateEcheance, null, getSession(), null, typeDocument);

        factureEBill++;
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        computeTotalPage();
    }

    /**
     * @throws FWIException
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();

        // Si l'on veut une QR Facture, dans ce cas, on va utiliser le deuxi�me template.
        setTemplateFile(CO00CSommationPaiement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_SOMMATION"));
        setNumeroReferenceInforom(CO00CSommationPaiement.NUMERO_REFERENCE_INFOROM);

    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            if (state == CO00CSommationPaiement.STATE_LETTRE) {
                createDataSourceLettre();
            } else {
                createDataSourceVoiesDroit();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * DataSource pour la lettre de sommation
     *
     * @throws Exception
     */
    private void createDataSourceLettre() throws Exception {

        if (APISection.ID_CATEGORIE_SECTION_LTN.equals(curContentieux.getSection().getCategorieSection()) || APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE.equals(curContentieux.getSection().getCategorieSection())) {
            // Setter pour un CT diff�rent
            getCatalogueTextesUtil().setNomDocument(CO00CSommationPaiement.SOMMATION_LTN);
            setNumeroReferenceInforom(CO00CSommationPaiement.NUM_REF_SOMMATION_LTN);
        }
        String adresse = getAdresseDestinataire();

        // Bug 7823
        setDocumentConfidentiel(true);
        // Gestion de l'en-t�te/pied de page/signature
        this._handleHeaders(curContentieux, true, false, true, adresse);

        // -- titre du doc
        initTitreDoc(getParent());

        // -- corps du doc
        initCorpsDoc(getParent());

        // -- boucle de detail
        FWCurrency montantTotal = initDetail(getParent());

        // -- texte en dessous du detail
        initTexteDetail(getParent());

        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new ReferenceQR();
            qrFacture.setSession(getSession());
            // Initialisation des variables du document
            initVariableQR(montantTotal);

            // G�n�ration du document QR
            qrFacture.initQR(this, qrFactures);
            referencesSommation.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), qrFacture.getReference());
        } else {
            // -- BVR
            initBVR(montantTotal);
            referencesSommation.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), getBvr().getRefNoSpace());
        }

    }

    /**
     * DataSource pour les voies de droits
     */
    private void createDataSourceVoiesDroit() {
        getCatalogueTextesUtil().setNomDocument(CO00CSommationPaiement.TITLE_VOIES_DE_DROIT);

        if (getCatalogueTextesUtil().isExistDocument(getParent(), CO00CSommationPaiement.TITLE_VOIES_DE_DROIT)) {
            setTemplateFile(CO00CSommationPaiement.TEMPLATE_NAME_VD);
            setDocumentTitle(getSession().getLabel("AQUILA_SOMMATION"));

            StringBuilder body = new StringBuilder("");
            // rechercher tous les paragraphes du corps du document
            for (int i = 1; i <= 9; i++) {
                try {
                    getCatalogueTextesUtil().dumpNiveau(getParent(), i, body, "\n");
                } catch (Exception e) {
                    this.log("exception: " + e.getMessage());
                }
            }

            this.setParametres(COParameter.P_TEXTVD, JadeStringUtil.isBlank(body.toString()) ? " " : body.toString());
        }
    }



    /**
     * Renvoie la r�f�rence BVR.
     *
     * @return la r�f�rence BVR.
     */
    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
    }

    /**
     * getter pour l'attribut date delai paiement.
     *
     * @return la valeur courante de l'attribut date delai paiement
     */
    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    /**
     * BVR
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

        // Informations pour la r�f�rence et l'OCRB
        getBvr().setSession(getSession());
        getBvr().setBVR(curContentieux.getSection(), cMontant.toString());
        // commencer � �crire les param�tres
        String adresseDebiteur = "";

        try {
            adresseDebiteur = getAdresseDestinataire();
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la recherche de l'adresse destinataire : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        try {
            // Modification suite � QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");

            super.setParametres(COParameter.P_ADRESSE, getBvr().getAdresse());
            super.setParametres(COParameter.P_ADRESSECOPY, getBvr().getAdresse());
            super.setParametres(COParameter.P_COMPTE, getBvr().getNumeroCC());// num�ro CC
            super.setParametres(COParameter.P_VERSE, getBvr().getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(COParameter.P_OCR, getBvr().getOcrb());
            super.setParametres(COParameter.P_FRANC, JANumberFormatter.deQuote(montantSansCentime));
            super.setParametres(COParameter.P_CENTIME, centimes);
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la sommation : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * corps du doc
     *
     * @param key
     * @throws Exception
     */
    private void initCorpsDoc(Object key) throws Exception {
        // -- corps du doc
        StringBuilder body = new StringBuilder();
        // rechercher tous les paragraphes du corps du document
        getCatalogueTextesUtil().dumpNiveau(key, 2, body, "\n\n");

        StringBuilder optionnel = new StringBuilder("");

        if (getTransition().getEtape().getLibEtape().equals(ICOEtape.CS_PREMIER_RAPPEL_ENVOYE)) {
            getCatalogueTextesUtil().dumpNiveau(key, 9, optionnel, " ");
        }

        /**
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <br/>
         * {0} = formule de politesse <br/>
         * {1} = date rappel <br/>
         * {2} = date de la section
         */
        this.setParametres(
                COParameter.T5,
                formatMessage(
                        body,
                        new Object[]{
                                getFormulePolitesse(destinataireDocument),
                                formatMessage(optionnel, new Object[]{formatDate(curContentieux.getDateExecution())}),
                                formatDate(curContentieux.getSection().getDateSection())}));
    }

    /**
     * boucle de detail
     *
     * @return
     * @throws Exception
     */
    private FWCurrency initDetail(Object key) throws Exception {
        // -- boucle de detail
        // si on a des taxes, on les affichent avec un total, sinon, on affiche juste le solde de la section
        LinkedList<HashMap<String, String>> lignes = new LinkedList<>();
        HashMap<String, String> fields = new HashMap<>();

        fields.put(COParameter.F1, getCatalogueTextesUtil().texte(key, 3, 1));
        fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
        fields.put(COParameter.F3, formatMontant(curContentieux.getSection().getSolde()));
        lignes.add(fields);

        // ajout des taxes si necessaire
        FWCurrency montantTotal = curContentieux.getSection().getSoldeToCurrency();

        // Ajoute des lignes de d�tail sur les sommations
        for (Iterator taxesIter = getTaxes().iterator(); taxesIter.hasNext(); ) {
            COTaxe taxe = (COTaxe) taxesIter.next();

            montantTotal.add(taxe.getMontantTaxe());

            fields = new HashMap<>();
            fields.put(COParameter.F1, taxe.loadCalculTaxe(getSession()).getRubrique().getDescription(getLangue()));
            fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
            fields.put(COParameter.F3, formatMontant(taxe.getMontantTaxe()));
            lignes.add(fields);
        }

        // Ajoute une ligne de TOTAL sur les sommations apr�s les lignes de d�tail
        if (montantTotal != null) {
            fields = new HashMap<>();
            fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
            fields.put(COParameter.F3, formatMontant(montantTotal.toString()));
            fields.put(COParameter.F4, getCatalogueTextesUtil().texte(key, 3, 3));
            lignes.add(fields);
        }

        // Prepare la map des lignes de sommations eBill si propri�t� eBillAquila est active et si compte annexe de la facture inscrit � eBill et si eBillPrintable est s�lection� sur l'�cran d'impression
        boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(getSession());
        if (eBillAquilaActif && curContentieux.getEBillPrintable() && curContentieux.getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(curContentieux.getCompteAnnexe().getEBillAccountID())) {
            lignesSommation.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), (List) lignes);
        }

        this.setDataSource(lignes);
        return montantTotal;
    }

    /**
     * texte en dessous du detail
     *
     * @throws Exception
     */
    private void initTexteDetail(Object key) throws Exception {
        StringBuilder body = new StringBuilder();

        // Si l'affiliation est de type employeur on affiche la phrase de niveau 5
        if (curContentieux.getSection().getCompteAnnexe().getIdCategorie()
                .equals(CO00CSommationPaiement.TYPE_AFFILI_EMPLOY)) {
            getCatalogueTextesUtil().dumpNiveau(key, 5, body, "\n\n");
        }

        // rechercher tous les paragraphes du corps du document
        getCatalogueTextesUtil().dumpNiveau(key, 4, body, "\n\n");

        /**
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <br/>
         * {0} = formule de politesse <br/>
         * {1} = date de d�lai du paiement
         */
        this.setParametres(
                COParameter.T6,
                formatMessage(body, new Object[]{getFormulePolitesse(destinataireDocument),
                        formatDate(dateDelaiPaiement)}));
    }

    /**
     * titre du doc
     *
     * @return
     */
    private StringBuilder initTitreDoc(Object key) {
        // -- titre du doc
        // rechercher tous les paragraphes du titre du document
        StringBuilder body = new StringBuilder();

        getCatalogueTextesUtil().dumpNiveau(key, 1, body, "\n");

        /**
         * formater le titre, les conventions de remplacement pour les paragraphes du titre sont: <br/>
         * {0} = nom section ; <br/>
         * {1} = date de la section
         */
        this.setParametres(
                COParameter.T1,
                formatMessage(body, new Object[]{curContentieux.getSection().getDescription(getLangue()),
                        curContentieux.getSection().getDateSection()}));
        return body;
    }

    /**
     * On surcharge la m�thode next() afin de g�rer les voies de droits au verso de la sommation.
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:
                if (super.next()) {
                    state = CO00CSommationPaiement.STATE_LETTRE;
                    // on va cr�er la lettre
                    return true;
                } else {
                    // il n'y a plus de documents � cr�er
                    return false;
                }
            case STATE_LETTRE:
                // on vient de cr�er la lettre, on va cr�er les voies de droits
                state = CO00CSommationPaiement.STATE_VD;
                return getCatalogueTextesUtil().isExistDocument(getParent(), CO00CSommationPaiement.TITLE_VOIES_DE_DROIT);
            default:
                // on regarder s'il y a encore des contentieux � traiter.
                state = CO00CSommationPaiement.STATE_IDLE;
                return next();
        }
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    /**
     * setter pour l'attribut date delai paiement.
     *
     * @param dateDelaiPaiement une nouvelle valeur pour cet attribut
     */
    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }

    /**
     * getter
     *
     * @return
     */
    public ReferenceQR getQrFacture() {
        return qrFacture;
    }

    /**
     * setter
     *
     * @param qrFacture
     */
    public void setQrFacture(ReferenceQR qrFacture) {
        this.qrFacture = qrFacture;
    }
}
