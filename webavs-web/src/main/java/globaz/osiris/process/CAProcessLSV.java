package globaz.osiris.process;

import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.api.process.APIProcessUpload;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.sepa.CACamt054GroupsMessage;

/**
 * Traitement d'un fichier de recouvrement direct. Date de création : (18.11.2002 10:35:46)
 * 
 * @author: Administrator
 */
public class CAProcessLSV extends BProcess implements APIProcessUpload {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValeur = new String();
    private boolean echoToConsole = false;
    private String fileName = new String();
    private String idOrganeExecution = new String();
    private String libelle = new String();
    private Boolean simulation = new Boolean(false);
    private String idYellowReportFile = new String();
    private CACamt054GroupsMessage groupesMessage = new CACamt054GroupsMessage();

    /**
     * Commentaire relatif au constructeur CAProcessLSV.
     */
    public CAProcessLSV() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 11:12:27)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessLSV(BProcess parent) {
        super(parent);
    }

    @Override
    public String getSubjectDetail() {
        StringBuilder builder = new StringBuilder();
        builder.append(groupesMessage.getMessage());
        builder.append("\r\n\r\n");
        builder.append(super.getSubjectDetail());
        return builder.toString();
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

        // Vérifier l'ordre groupé
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            getMemoryLog().logMessage("5320", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Vérifier le nom du fichier
        if (JadeStringUtil.isBlank(getFileName()) && JadeStringUtil.isBlank(getIdYellowReportFile())) {
            getMemoryLog().logMessage("5324", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Date valeur = date du jour si vide
        if (JadeStringUtil.isBlank(getDateValeur()) || JadeStringUtil.isIntegerEmpty(getDateValeur())) {
            getMemoryLog().logMessage("5327", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        } else {

            // Vérifier la date de valeur
            try {
                globaz.globall.db.BSessionUtil.checkDateGregorian(
                        ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                        getDateValeur());
            } catch (Exception e) {

            }
        }

        // Libellé par défaut si vide
        if (JadeStringUtil.isBlank(getLibelle())) {
            setLibelle(getSession().getLabel("53281"));
        }

        // Sous controle d'exceptions
        try {
            // Instancier un ordre groupé
            CAOrganeExecution organeExecution = new CAOrganeExecution();

            // Charger l'ordre
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            organeExecution.retrieve(getTransaction());
            if (organeExecution.isNew() || organeExecution.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), organeExecution.getClass().getName());
                getMemoryLog().logMessage("5159", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Demander le traitement du BVR
            organeExecution.setMemoryLog(getMemoryLog());
            organeExecution.executeLSV(this);

            groupesMessage = organeExecution.getGroupesMessage();

            // Tester si abort
            if (isAborted()) {
                return false;
            }

            // S'il y a des erreurs fatales
            if (organeExecution.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), organeExecution.getClass().getName());
                getMemoryLog().logMessage("5329", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // s'il n'y a pas d'erreurs fatales et qu'on n'est pas en simulation
            if (!getMemoryLog().isOnFatalLevel() && !getSimulation().booleanValue()) {
                organeExecution.update(getTransaction());
                if (organeExecution.hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), organeExecution.getClass().getName());
                    getMemoryLog().logMessage("5329", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return getMemoryLog().hasErrors();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:54:30)
     * 
     * @return String
     */
    @Override
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:57:24)
     * 
     * @return boolean
     */
    @Override
    public boolean getEchoToConsole() {
        return echoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String title;

        if (getMemoryLog().hasErrors()) {
            title = getSession().getLabel("5285");
        } else {
            title = getSession().getLabel("5284");
        }

        if (simulation) {
            title += " - " + getSession().getLabel("SIMULATION");
        }

        // Restituer l'objet
        return title;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:59:41)
     * 
     * @return String
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 16:10:31)
     * 
     * @return String
     */
    @Override
    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:51:56)
     * 
     * @return String
     */
    @Override
    public String getLibelle() {
        return libelle;
    }

    @Override
    public Boolean getSimulation() {
        return simulation;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:54:30)
     * 
     * @param newDateValeur
     *            String
     */
    @Override
    public void setDateValeur(String newDateValeur) {
        dateValeur = newDateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:57:24)
     * 
     * @param newEchoToConsole
     *            boolean
     */
    @Override
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:59:41)
     * 
     * @param newFileName
     *            String
     */
    @Override
    public void setFileName(String newFileName) {
        fileName = newFileName;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 16:10:31)
     * 
     * @param newOrganeExecution
     *            String
     */
    @Override
    public void setIdOrganeExecution(String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:51:56)
     * 
     * @param newLibelle
     *            String
     */
    @Override
    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    @Override
    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    @Override
    public void setIdYellowReportFile(String id) {
        idYellowReportFile = id;
    }

    @Override
    public String getIdYellowReportFile() {
        return idYellowReportFile;
    }

    @Override
    public BTransaction getTransactionProcess() {
        return getTransaction();
    }

    @Override
    public void setProgressScaleValueProcess(long value) {
        setProgressScaleValue(value);
    }

    @Override
    public void setMemoryLogProcess(FWMemoryLog newMemoryLog) {
        setMemoryLog(newMemoryLog);
    }

    @Override
    public FWMemoryLog getMemoryLogProcess() {
        return getMemoryLog();
    }

    @Override
    public boolean isAbortedProcess() {
        return isAborted();
    }

}
