package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.db.communications.CPParametrePlausibiliteManager;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.listes.csv.CPListeCommunicationARetournerCSV;
import globaz.phenix.listes.excel.CPListeCommunicationARetourner;
import java.io.IOException;

/**
 * Dévalide une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessCommunicationRetourner extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateFichier = "";

    private String forIdPlausibilite = "";
    private String forStatus = "";
    private java.lang.String fromNumAffilie = "";
    private String idJournalRetour = "";
    private Boolean listeExcel = new Boolean(true);
    private String orderBy = "";
    private Boolean simulation = new Boolean(false);
    private java.lang.String tillNumAffilie = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessCommunicationRetourner() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessCommunicationRetourner(BProcess parent) {
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
            comManager.setForStatus(getForStatus());
            comManager.setWhitPavsAffilie(true);
            comManager.setWhitPersAffilie(true);
            comManager.setWhitAffiliation(true);
            comManager.setTri(orderBy);
            comManager.setForIdPlausibilite(getForIdPlausibilite());
            if ("ORDER_BY_CONTRIBUABLE".equals(orderBy)) {
                comManager.orderByNumContribuable();
                comManager.orderByNumIFD();
            } else if ("ORDER_BY_AFFILIE".equals(getOrderBy())) {
                comManager.orderByNumAffilie();
                comManager.orderByNumIFD();
            } else if ("ORDER_BY_IFD".equals(orderBy)) {
                comManager.orderByNumIFD();
            } else if ("ORDER_BY_AVS".equals(orderBy)) {
                comManager.orderByNumAvs();
                comManager.orderByNumIFD();
            } else { // Défaut
                comManager.orderByNumContribuable();
                comManager.orderByNumIFD();
            }
            if (JadeStringUtil.isEmpty(getForIdPlausibilite())) {
                CPParametrePlausibiliteManager plausiManager = new CPParametrePlausibiliteManager();
                plausiManager.setSession(getSession());
                plausiManager.setForActif(new Boolean(true));
                plausiManager.setInTypeMessage(CPParametrePlausibilite.CS_MSG_ERREUR_CRITIQUE);
                plausiManager.find();
                if (plausiManager.size() > 0) {
                    for (int i = 0; i < plausiManager.size(); i++) {
                        CPParametrePlausibilite plausi = (CPParametrePlausibilite) plausiManager.get(i);
                        comManager.setForIdPlausibilite(plausi.getIdParametre());
                        imprimer(comManager, jrn);
                    }
                }
            } else {
                imprimer(comManager, jrn);
            }

            return !isOnError();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
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

    public String getDateFichier() {
        return dateFichier;
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

    public Boolean getListeExcel() {
        return listeExcel;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public Boolean getSimulation() {
        return simulation;
    }

    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    private void imprimer(ICommunicationrRetourManager comManager, CPJournalRetour jrn) throws Exception, IOException {
        if (comManager.getCount(getTransaction()) > 0) {
            if (Boolean.TRUE.equals(getListeExcel())) {
                // Impression liste excel
                CPListeCommunicationARetourner excelDoc = new CPListeCommunicationARetourner();
                excelDoc.setSession(getSession());
                excelDoc.setProcessAppelant(this);
                excelDoc.setCanton(jrn.getCanton());
                excelDoc.setSimulation(getSimulation());
                excelDoc.setDateFichier(getDateFichier());
                excelDoc.setIdPlausibilite(getForIdPlausibilite());
                excelDoc.populateSheet(comManager, getTransaction());
                registerAttachedDocument(excelDoc.getOutputFile());
            } else {
                // Génération fichier csv
                CPListeCommunicationARetournerCSV docCSV = new CPListeCommunicationARetournerCSV();
                docCSV.setSession(getSession());
                docCSV.setProcessAppelant(this);
                docCSV.setEmailObjet(getEMailObject());
                docCSV.setCanton(jrn.getCanton());
                docCSV.setSimulation(getSimulation());
                docCSV.setDateFichier(getDateFichier());
                docCSV.setTransaction(getTransaction());
                docCSV.setIdPlausibilite(getForIdPlausibilite());
                docCSV.setManager(comManager);
                docCSV.setParentWithCopy(this);
                docCSV.executeProcess();
            }
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDateFichier(String dateImpression) {
        dateFichier = dateImpression;
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

    public void setListeExcel(Boolean lisetExcel) {
        listeExcel = lisetExcel;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setSimulation(Boolean varBoolean) {
        simulation = varBoolean;
    }

    public void setTillNumAffilie(java.lang.String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

}
