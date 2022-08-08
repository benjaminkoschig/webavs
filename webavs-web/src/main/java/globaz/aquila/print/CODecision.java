/*
 * Cr�� le 10 janv. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print;

import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
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
import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.process.ebill.EBillHelper;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import globaz.osiris.process.ebill.EBillTypeDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author sch
 */
public class CODecision extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CENT_DEFAUT = "XX";
    public static final String MONTANT_DEFAUT = "XXXX";
    public static final String NUMERO_REFERENCE_INFOROM = "0126GCO";

    public static final String OCRB_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String REFERENCE_NON_FACTURABLE_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final long serialVersionUID = -3645938861761414428L;
    private static final int STATE_IDLE = 0;

    private static final int STATE_LETTRE = 1;
    private static final int STATE_VD = 2;
    private static final String TEMPLATE_NAME = "CO_DECISION_QR";
    /** Le nom du mod�le */
    private static final String TEMPLATE_NAME_VD = "CO_DECISION_VOIES_DROIT";

    private static final String TITLE_VOIES_DE_DROIT = "d�cision voies de droit";
    private static final String TYPE_AFFILI_EMPLOY = "804002";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private ReferenceBVR bvr = null;
    private int state = CODecision.STATE_IDLE;

    /* eBill fields */
    public Map<PaireIdExterneEBill, List<Map>> lignesDecision = new LinkedHashMap();
    public Map<PaireIdExterneEBill, String> referencesDecision = new LinkedHashMap();
    private static final Logger LOGGER = LoggerFactory.getLogger(CODecision.class);
    private EBillHelper eBillHelper = new EBillHelper();
    private int factureEBill = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe CODecision.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CODecision() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe CODecision.
     * 
     * @param session
     * @throws FWIException
     */
    public CODecision(BSession session) throws FWIException {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        computeTotalPage();
    }

    /**
     * Pour les voies de droit
     * 
     * @see globaz.aquila.print.CODocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        super.getDocumentInfo().setDocumentTypeNumber(getNumeroReferenceInforom());
        try {
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());

            // Imprime les documents dans un fichier s�par� selon la propri�t�
            // de la section
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
                            traiterDecisionEBillAquila(curContentieux.getCompteAnnexe());
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
     * M�thode permettant de traiter les d�cisions eBill
     * en attente d'�tre envoy� dans le processus actuel.
     */
    public void traiterDecisionEBillAquila(CACompteAnnexe compteAnnexe) throws Exception {

        for (Map.Entry<PaireIdExterneEBill, List<Map>> lignes : lignesDecision.entrySet()) {
            if (curContentieux.getSection().getCompteAnnexe().getIdExterneRole().equals(lignes.getKey().getIdExterneRole())
                    && curContentieux.getSection().getIdExterne().equals(lignes.getKey().getIdExterneFactureCompensation())) {

                FAEnteteFacture entete = eBillHelper.generateEnteteFacture(curContentieux.getSection(), getSession());
                String reference = referencesDecision.get(lignes.getKey());
                List<JadePublishDocument> attachedDocuments = eBillHelper.findReturnOrRemoveAttachedDocuments(entete, getAttachedDocuments(), CODecision.class.getSimpleName(), false);

                if (!attachedDocuments.isEmpty()) {
                    creerFichierEBill(compteAnnexe, entete, lignes.getKey().getMontant(), lignes.getValue(), reference, attachedDocuments, curContentieux.getDateExecution(), curContentieux.getSection(), EBillTypeDocument.DECISION);
                }
            }
        }
    }

    private void ajouteInfoEBillToEmail() {
        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill, FWMessage.INFORMATION, this.getClass().getName());
        getDocumentInfo().setDocumentNotes(getDocumentInfo().getDocumentNotes() + getMemoryLog().getMessagesInString());
    }

    /**
     * M�thode permettant de cr�er la d�cision eBill,
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

        String dateEcheance = dateImprOuFactu;
        eBillHelper.creerFichierEBill(compteAnnexe, entete, null, montantFacture, lignes, null, reference, attachedDocuments, dateImprOuFactu, dateEcheance, null, getSession(), null, typeDocument);

        factureEBill++;
    }

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CODecision.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_DECISION"));
        setNumeroReferenceInforom(CODecision.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            if (state == CODecision.STATE_LETTRE) {
                createDataSourceLettre();
            } else {
                createDataSourceVoiesDroit();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    private void createDataSourceLettre() throws Exception {
        String adresse = getAdresseDestinataire();
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
            referencesDecision.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), qrFacture.getReference());
        } else {
            // -- BVR
            initBVR(montantTotal);
            referencesDecision.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), getBvr().getRefNoSpace());
        }
    }


    /**
     * Cr�� les voies de droit s'il existe un catalogue de textes pour.
     */
    private void createDataSourceVoiesDroit() {
        getCatalogueTextesUtil().setNomDocument(CODecision.TITLE_VOIES_DE_DROIT);

        if (getCatalogueTextesUtil().isExistDocument(getParent(), CODecision.TITLE_VOIES_DE_DROIT)) {
            setTemplateFile(CODecision.TEMPLATE_NAME_VD);
            setDocumentTitle(getSession().getLabel("AQUILA_DECISION"));

            StringBuilder body = new StringBuilder();
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
            e.printStackTrace();
        }
        try {
            // Modification suite � QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");
            super.setParametres(COParameter.P_SUBREPORT_QR_CURRENT_PAGE, getImporter().getImportPath() + "BVR_TEMPLATE_CURRENT_PAGE.jasper");

            // Param�tre de footer si document sur plusieurs pages
            // N'est plus utilis� sur document CO_DECISION, car il est sur une seule page
            super.setParametres(FWIImportParametre.PARAM_REFERENCE + "_X", CODecision.REFERENCE_NON_FACTURABLE_DEFAUT);
            super.setParametres(COParameter.P_OCR + "_X", CODecision.OCRB_DEFAUT);
            super.setParametres(COParameter.P_FRANC + "_X", CODecision.MONTANT_DEFAUT);
            super.setParametres(COParameter.P_CENTIME + "_X", CODecision.CENT_DEFAUT);
            //

            super.setParametres(COParameter.P_ADRESSE, getBvr().getAdresse());
            super.setParametres(COParameter.P_ADRESSECOPY, getBvr().getAdresse());
            super.setParametres(COParameter.P_COMPTE, getBvr().getNumeroCC());// num�ro
            // CC
            super.setParametres(COParameter.P_VERSE, getBvr().getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(COParameter.P_OCR, getBvr().getOcrb());
            super.setParametres(COParameter.P_FRANC, JANumberFormatter.deQuote(montantSansCentime));
            super.setParametres(COParameter.P_CENTIME, centimes);
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la D�cision : " + e1.getMessage(),
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

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule de
         * politesse {1} = date rappel
         */
        this.setParametres(
                COParameter.T5,
                formatMessage(
                        body,
                        new Object[] {
                                getFormulePolitesse(destinataireDocument),
                                formatMessage(optionnel, new Object[] { formatDate(curContentieux.getDateExecution()) }) }));
    }

    /**
     * boucle de detail
     * 
     * @return
     * @throws Exception
     */
    private FWCurrency initDetail(Object key) throws Exception {
        // -- boucle de detail
        // si on a des taxes, on les affichent avec un total, sinon, on affiche
        // juste le solde de la section
        HashMap fields = new HashMap();

        String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                curContentieux.getIdSection());

        // D�tail de la section avec paiements
        List dataSource = new ArrayList();
        fields.put(COParameter.F1, curContentieux.getSection().getDescription(getLangue()));
        fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
        fields.put(COParameter.F3, formatMontant(infoSection[0]));
        dataSource.add(fields);

        List situation = createSituationCompteDS(infoSection[2], COParameter.F1, COParameter.F3, COParameter.F2,
                getCatalogueTextesUtil().texte(key, 3, 2), ICOEtape.CS_DECISION, infoSection[1]);
        dataSource.addAll(situation);
        // ajout des taxes si necessaire
        FWCurrency montantTotal = new FWCurrency(infoSection[0]);
        Iterator it = situation.iterator();
        while (it.hasNext()) {
            HashMap m = (HashMap) it.next();
            montantTotal.add(new FWCurrency((String) m.get(COParameter.F3)));
        }

        if (getTaxes() != null) {
            for (Iterator taxesIter = getTaxes().iterator(); taxesIter.hasNext();) {
                COTaxe taxe = (COTaxe) taxesIter.next();

                montantTotal.add(taxe.getMontantTaxe());

                fields = new HashMap();
                String libelle = "";
                if ((taxe.getLibelle(getSession()) != null) || JadeStringUtil.isBlank(taxe.getLibelle(getSession()))) {
                    libelle = taxe.getLibelle(getSession());
                } else {
                    libelle = taxe.loadCalculTaxe(getSession()).getRubrique().getDescription(getLangue());
                }
                fields.put(COParameter.F1, libelle);
                fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
                fields.put(COParameter.F3, formatMontant(taxe.getMontantTaxe()));
                dataSource.add(fields);
            }
        }

        // Prepare la map des lignes de d�cisions eBill si propri�t� eBillAquila est active et si compte annexe de la facture inscrit � eBill et si eBillPrintable est s�lection� sur l'�cran d'impression
        boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(getSession());
        if (eBillAquilaActif && curContentieux.getEBillPrintable() && curContentieux.getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(curContentieux.getCompteAnnexe().getEBillAccountID())) {
            lignesDecision.put(new PaireIdExterneEBill(curContentieux.getCompteAnnexe().getIdExterneRole(), curContentieux.getSection().getIdExterne(), montantTotal != null ? montantTotal.toString() : ""), dataSource);
        }

        this.setDataSource(dataSource);

        if (montantTotal != null) {
            this.setParametres(COParameter.P_TOT_DEVISE, getCatalogueTextesUtil().texte(key, 3, 2));
            this.setParametres(COParameter.P_TOT_MONTANT, formatMontant(montantTotal.toString()));
            this.setParametres(COParameter.P_TOT_LIBELLE, getCatalogueTextesUtil().texte(key, 3, 3));
        }

        return montantTotal;
    }

    /**
     * texte en dessous du detail
     * 
     * @throws Exception
     */
    private void initTexteDetail(Object key) throws Exception {
        // -- texte en dessous du detail
        StringBuilder body = new StringBuilder();

        // Si l'affiliation est de type employeur on affiche la phrase de niveau
        // 5
        if (curContentieux.getSection().getCompteAnnexe().getIdCategorie().equals(CODecision.TYPE_AFFILI_EMPLOY)) {
            getCatalogueTextesUtil().dumpNiveau(key, 5, body, "\n\n");
        }

        // rechercher tous les paragraphes du corps du document
        getCatalogueTextesUtil().dumpNiveau(key, 4, body, "\n\n");

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule de
         * politesse {1} = date de d�lai du paiement
         */
        this.setParametres(COParameter.T6,
                formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument) }));
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

        /*
         * formater le titre, les conventions de remplacement pour les paragraphes du titre sont: {0} = nom section ;
         * {1} = date de la section
         */
        this.setParametres(
                COParameter.T1,
                formatMessage(body, new Object[] { curContentieux.getSection().getDescription(getLangue()),
                        curContentieux.getSection().getDateSection() }));
        return body;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.print.CODocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:
                // on commence ou a deja trait� un contentieux, on regarde s'il
                // reste des contentieux a traiter
                if (super.next()) {
                    state = CODecision.STATE_LETTRE;
                    // on va cr�er la lettre
                    return true;
                } else {
                    // il n'y a plus de documents � cr�er
                    return false;
                }
            case STATE_LETTRE:
                // on vient de cr�er la lettre, on va cr�er les voies de droits
                state = CODecision.STATE_VD;
                return getCatalogueTextesUtil().isExistDocument(getParent(), CODecision.TITLE_VOIES_DE_DROIT);
            default:
                // on regarder s'il y a encore des contentieux � traiter.
                state = CODecision.STATE_IDLE;
                return next();
        }
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

}
