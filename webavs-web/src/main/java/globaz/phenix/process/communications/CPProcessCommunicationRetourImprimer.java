package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSManager;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscGE_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscJU_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscNE_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscSEDEX_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscVD_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscVS_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFisc_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetour_Doc;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.listes.excel.CPListeCommunicationRetour;
import globaz.pyxis.constantes.IConstantes;

/**
 * Dévalide une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessCommunicationRetourImprimer extends BProcess {

    private static final String NUMERO_INFOROM = "0078CCP";
    private static final long serialVersionUID = 1L;
    private java.lang.String forGenreAffilie = "";
    private String forIdPlausibilite = "";
    private String forStatus = "";
    private java.lang.String fromNumAffilie = "";
    private String idJournalRetour = "";
    private String idRetour = "";
    private String impression = "";
    private String orderBy = "";
    private java.lang.String tillNumAffilie = "";
    private Boolean wantDetail = Boolean.FALSE;
    private Boolean isTraitementUnitaire = Boolean.FALSE;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessCommunicationRetourImprimer() {
        super();
    }

    public CPProcessCommunicationRetourImprimer(BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        setSendCompletionMail(false);
        ICommunicationrRetourManager comManager = null;
        // Sous controle d'exceptions
        try {
            // Test du canton pour savoir quel manager utiliser.
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(getIdJournalRetour());
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                comManager = jrn.determinationManager();
            }
            if (comManager != null) {
                // Rechercher les données de la décision
                comManager.setSession(getSession());
                comManager.setForIdJournalRetour(getIdJournalRetour());
                comManager.setFromNumAffilie(getFromNumAffilie());
                comManager.setTillNumAffilie(getTillNumAffilie());
                comManager.setForIdRetour(getIdRetour());
                comManager.setForStatus(getForStatus());
                comManager.setForGenreAffilie(getForGenreAffilie());
                comManager.setWhitPavsAffilie(true);
                comManager.setWhitPersAffilie(true);
                comManager.setWhitAffiliation(true);
                comManager.setTri(orderBy);
                comManager.setForIdPlausibilite(getForIdPlausibilite());

                // Applique le tri sur le manager
                determineOrderBy(comManager);

                // Impression liste excel
                return printList(comManager, jrn);
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        return false;
    }

    private boolean printList(ICommunicationrRetourManager comManager, CPJournalRetour jrn) throws Exception {
        if ("LISTE_EXCEL".equalsIgnoreCase(getImpression())) {
            // Création du document PHENIX
            return printListExcel(comManager);
        } else if ("LISTE_PDF".equalsIgnoreCase(getImpression())) {
            // Impression individuel
            return printListPdf(comManager);

        } else if ("LISTE_PDF_DETAIL".equalsIgnoreCase(getImpression())) {
            // Impression détaillée suivant le FISC
            return printListPdfDetail(comManager, jrn);
        }

        return false;
    }

    private void determineOrderBy(ICommunicationrRetourManager comManager) {
        if ("ORDER_BY_CONTRIBUABLE".equals(orderBy)) {
            comManager.orderByErreur();
            comManager.orderByNumContribuable();
            comManager.orderByNumIFD();
        } else if ("ORDER_BY_AFFILIE".equals(getOrderBy())) {
            comManager.orderByErreur();
            comManager.orderByNumAffilie();
            comManager.orderByNumIFD();
        } else if ("ORDER_BY_IFD".equals(orderBy)) {
            comManager.orderByErreur();
            comManager.orderByNumIFD();
        } else if ("ORDER_BY_AVS".equals(orderBy)) {
            comManager.orderByErreur();
            comManager.orderByNumAvs();
            comManager.orderByNumIFD();
        } else { // Défaut
            comManager.orderByErreur();
            comManager.orderByNumContribuable();
            comManager.orderByNumIFD();
        }
    }

    private boolean printListExcel(ICommunicationrRetourManager comManager) throws Exception {
        CPListeCommunicationRetour excelDoc = new CPListeCommunicationRetour(getSession());
        excelDoc.setProcessAppelant(this);
        excelDoc.setIdJournalRetour(getIdJournalRetour());
        excelDoc.setForIdRetour(getIdRetour());
        excelDoc.setFromNumAffilie(getFromNumAffilie());
        excelDoc.setTillNumAffilie(getTillNumAffilie());
        excelDoc.setForStatus(getForStatus());
        excelDoc.populateSheet(comManager, getTransaction());
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CPApplication.DEFAULT_APPLICATION_PHENIX);
        docInfo.setDocumentType(NUMERO_INFOROM);
        docInfo.setDocumentTypeNumber(NUMERO_INFOROM);
        docInfo.setDocumentTitle("");
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);

        this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());

        return hasAttachedDocuments();
    }

    private boolean printListPdfDetail(ICommunicationrRetourManager comManager, CPJournalRetour journal) {

        CPImpressionCommunicationRetourDetailFisc_Doc decision;

        try {
            if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
                decision = new CPImpressionCommunicationRetourDetailFiscNE_Doc(this);
                ((CPImpressionCommunicationRetourDetailFiscNE_Doc) decision)
                        .setManager((CPCommunicationFiscaleRetourNEManager) comManager);

            } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_JURA)) {
                decision = new CPImpressionCommunicationRetourDetailFiscJU_Doc(this);
                ((CPImpressionCommunicationRetourDetailFiscJU_Doc) decision)
                        .setManager((CPCommunicationFiscaleRetourJUManager) comManager);

            } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
                decision = new CPImpressionCommunicationRetourDetailFiscVD_Doc(this);
                ((CPImpressionCommunicationRetourDetailFiscVD_Doc) decision)
                        .setManager((CPCommunicationFiscaleRetourVDManager) comManager);

            } else if (journal.getCanton().equalsIgnoreCase(CPJournalRetour.CS_CANTON_SEDEX)) {
                decision = new CPImpressionCommunicationRetourDetailFiscSEDEX_Doc(this);
                ((CPImpressionCommunicationRetourDetailFiscSEDEX_Doc) decision)
                        .setManager((CPCommunicationFiscaleRetourSEDEXManager) comManager);
                ((CPImpressionCommunicationRetourDetailFiscSEDEX_Doc) decision).setWantDetail(getWantDetail());

            } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_GENEVE)) {
                decision = new CPImpressionCommunicationRetourDetailFiscGE_Doc(this);
                ((CPImpressionCommunicationRetourDetailFiscGE_Doc) decision)
                        .setManager((CPCommunicationFiscaleRetourGEManager) comManager);

            } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VALAIS)) {
                decision = new CPImpressionCommunicationRetourDetailFiscVS_Doc(this);
                ((CPImpressionCommunicationRetourDetailFiscVS_Doc) decision)
                        .setManager((CPCommunicationFiscaleRetourVSManager) comManager);
            } else {
                return false;
            }

            decision.setParentWithCopy(this);
            decision.setSession(getSession());
            decision.setDeleteOnExit(true);
            decision.setForIdPlausibilite(getForIdPlausibilite());

            decision.start();

            return hasAttachedDocuments();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "Liste des décisions");
            return false;
        }
    }

    private boolean printListPdf(ICommunicationrRetourManager comManager) {
        try {
            CPImpressionCommunicationRetour_Doc decision = new CPImpressionCommunicationRetour_Doc(this);
            decision.setParentWithCopy(this);
            decision.setSession(getSession());
            decision.setIdJournalRetour(getIdJournalRetour());
            decision.setIdRetour(getIdRetour());
            decision.setFromNumAffilie(getFromNumAffilie());
            decision.setTillNumAffilie(getTillNumAffilie());
            decision.setForStatus(getForStatus());
            decision.setTri(comManager.getTri());
            decision.setDeleteOnExit(true);
            decision.setForIdPlausibilite(getForIdPlausibilite());
            decision.setTraitementUnitaire(isTraitementUnitaire().toString());
            decision.start();
            return hasAttachedDocuments();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "Liste des décisions");
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("PROCIMPRIMERRETOUR_ERROR") + getIdJournalRetour();
        } else {
            obj = getSession().getLabel("PROCIMPRIMERRETOUR_SUCCES") + getIdJournalRetour();
        }
        // Restituer l'objet
        return obj;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public java.lang.String getForStatus() {
        return forStatus;
    }

    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public java.lang.String getIdJournalRetour() {
        return idJournalRetour;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getImpression() {
        return impression;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    public Boolean getWantDetail() {
        return wantDetail;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    public void setForStatus(java.lang.String string) {
        forStatus = string;
    }

    public void setFromNumAffilie(java.lang.String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setIdJournalRetour(java.lang.String string) {
        idJournalRetour = string;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setTillNumAffilie(java.lang.String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

    public void setWantDetail(Boolean wantDetail) {
        this.wantDetail = wantDetail;
    }

    public Boolean isTraitementUnitaire() {
        return isTraitementUnitaire;
    }

    public void setIsTraitementUnitaire(boolean isTraitementUnitaire) {
        this.isTraitementUnitaire = isTraitementUnitaire;
    }

}
