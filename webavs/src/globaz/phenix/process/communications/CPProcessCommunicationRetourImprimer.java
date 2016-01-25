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
import globaz.phenix.db.communications.CPJournalRetourViewBean;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscGE_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscJU_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscNE_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscSEDEX_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscVD_Doc;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetourDetailFiscVS_Doc;
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
    /**
     * 
     */
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

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessCommunicationRetourImprimer(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
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
            // Impression liste excel
            if (getImpression().equalsIgnoreCase("LISTE_EXCEL")) {
                // Création du document PHENIX
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
                docInfo.setDocumentType("0078CCP");
                docInfo.setDocumentTitle("");
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                // registerAttachedDocument(docInfo, excelDoc.getOutputFile());
                this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
                // super.publishDocuments();
                return hasAttachedDocuments();
            } else if (getImpression().equalsIgnoreCase("LISTE_PDF")) {
                // Impression individuel
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
            } else if (getImpression().equalsIgnoreCase("LISTE_PDF_DETAIL")) {
                // Impression détaillée suivant le FISC
                try {
                    // On cherche le canton
                    CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
                    journal.setSession(getSession());
                    journal.setIdJournalRetour(getIdJournalRetour());
                    journal.retrieve();
                    if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
                        CPImpressionCommunicationRetourDetailFiscNE_Doc decision = new CPImpressionCommunicationRetourDetailFiscNE_Doc(
                                this);
                        decision.setParentWithCopy(this);
                        decision.setSession(getSession());
                        decision.setManager((CPCommunicationFiscaleRetourNEManager) comManager);
                        decision.setDeleteOnExit(true);
                        decision.setForIdPlausibilite(getForIdPlausibilite());
                        decision.start();
                        return hasAttachedDocuments();
                    } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_JURA)) {
                        CPImpressionCommunicationRetourDetailFiscJU_Doc decision = new CPImpressionCommunicationRetourDetailFiscJU_Doc(
                                this);
                        decision.setParentWithCopy(this);
                        decision.setSession(getSession());
                        decision.setManager((CPCommunicationFiscaleRetourJUManager) comManager);
                        decision.setDeleteOnExit(true);
                        decision.setForIdPlausibilite(getForIdPlausibilite());
                        decision.start();
                        return hasAttachedDocuments();
                    } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
                        CPImpressionCommunicationRetourDetailFiscVD_Doc decision = new CPImpressionCommunicationRetourDetailFiscVD_Doc(
                                this);
                        decision.setParentWithCopy(this);
                        decision.setSession(getSession());
                        decision.setManager((CPCommunicationFiscaleRetourVDManager) comManager);
                        decision.setDeleteOnExit(true);
                        decision.setForIdPlausibilite(getForIdPlausibilite());
                        decision.start();
                        return hasAttachedDocuments();
                    } else if (journal.getCanton().equalsIgnoreCase(CPJournalRetour.CS_CANTON_SEDEX)) {
                        CPImpressionCommunicationRetourDetailFiscSEDEX_Doc decision = new CPImpressionCommunicationRetourDetailFiscSEDEX_Doc(
                                this);
                        decision.setWantDetail(getWantDetail());
                        decision.setParentWithCopy(this);
                        decision.setSession(getSession());
                        decision.setManager((CPCommunicationFiscaleRetourSEDEXManager) comManager);
                        decision.setDeleteOnExit(true);
                        decision.setForIdPlausibilite(getForIdPlausibilite());
                        decision.start();
                        return hasAttachedDocuments();
                    } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_GENEVE)) {
                        CPImpressionCommunicationRetourDetailFiscGE_Doc decision = new CPImpressionCommunicationRetourDetailFiscGE_Doc(
                                this);
                        decision.setParentWithCopy(this);
                        decision.setSession(getSession());
                        decision.setManager((CPCommunicationFiscaleRetourGEManager) comManager);
                        decision.setDeleteOnExit(true);
                        decision.setForIdPlausibilite(getForIdPlausibilite());
                        decision.start();
                        return hasAttachedDocuments();
                    } else if (journal.getCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VALAIS)) {
                        CPImpressionCommunicationRetourDetailFiscVS_Doc decision = new CPImpressionCommunicationRetourDetailFiscVS_Doc(
                                this);
                        decision.setParentWithCopy(this);
                        decision.setSession(getSession());
                        decision.setManager((CPCommunicationFiscaleRetourVSManager) comManager);
                        decision.setDeleteOnExit(true);
                        decision.setForIdPlausibilite(getForIdPlausibilite());
                        decision.start();
                        return hasAttachedDocuments();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "Liste des décisions");
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
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

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("PROCIMPRIMERRETOUR_ERROR") + getIdJournalRetour();
        } else {
            obj = getSession().getLabel("PROCIMPRIMERRETOUR_SUCCES") + getIdJournalRetour();
        }
        // Restituer l'objet
        return obj;

    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    /**
     * @return
     */
    public java.lang.String getForStatus() {
        return forStatus;
    }

    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @return
     */
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

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    /**
     * @param string
     */
    public void setForStatus(java.lang.String string) {
        forStatus = string;
    }

    public void setFromNumAffilie(java.lang.String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    /**
     * @param string
     */
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

    /**
     * @return the isTraitementUnitaire
     */
    public Boolean isTraitementUnitaire() {
        return isTraitementUnitaire;
    }

    /**
     * @param isTraitementUnitaire the isTraitementUnitaire to set
     */
    public void setIsTraitementUnitaire(boolean isTraitementUnitaire) {
        this.isTraitementUnitaire = isTraitementUnitaire;
    }

}
