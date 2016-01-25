package globaz.naos.db.tent;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliationManager;

public class AFExport extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //
    // protected final static String MSG_EMAIL_SUBJECT_OK = "8001";
    protected final static String MSG_EMAIL_SUBJECT_ERROR = "8002";
    //
    // protected final static String CS_NON_ACTIF = "804004";
    //
    protected String dateDebut = "";
    protected String dateFin = "";

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public AFExport() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFExport(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public AFExport(BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            /*
             * TIPersonneAvsManager tM = new TIPersonneAvsManager(); tM.setSession(getSession()); //
             * tM.setForTypeTiers(TITiers.CS_AFFILIE); tM.setForGenreAffilie(CS_NON_ACTIF);
             * tM.setBetweenDatesDebutActivite(dateDebut, dateFin); tM.find(BManager.SIZE_NOLIMIT);
             * System.out.println("taille " + tM.size());
             */
            // TODO : Utiliser manager AFAffiliation
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForDateDebutGreaterOrEqualTo(dateDebut);
            manager.setForDateDebutLowerOrEqualTo(dateFin);
            manager.forIsTraitement(false);
            // manager.setForChamp(TITiers.FIELD_DESIGNATION1);
            // manager.setForMotif("506001"); // Création
            // manager.setForChamp(ITIHistoriqueTiers.FIELD_DESIGNATION1);
            manager.find(BManager.SIZE_NOLIMIT);
            TentDocument excelDoc = new TentDocument("Tent " + dateDebut + "-" + dateFin);
            excelDoc.populateSheet(manager);
            this.registerAttachedDocument(excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return false;
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        String obj = "";

        if (isOnError()) {
            obj = getSession().getLabel("IMPRESSION_AFF_NON_PROV_ERREUR");
        } else {
            obj = getSession().getLabel("MSG_EMAIL_SUBJECT_OK");
        }

        return obj;
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}
