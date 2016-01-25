package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.api.process.APIProcessUpload;
import globaz.osiris.api.process.APIProcessUploadBVR;
import globaz.osiris.db.ordres.CAOrganeExecution;

/**
 * Traitement d'un fichier BVR
 * 
 * @author: Administrator
 */
public class CAProcessBVR extends BProcess implements APIProcessUpload, APIProcessUploadBVR {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValeur = new String();
    private boolean echoToConsole = false;
    private String fileName = new String();
    private String idJournalBvr;
    private String idOrganeExecution = new String();
    private String libelle = new String();

    private boolean retrieveBvrFromDataBase = true;
    private Boolean simulation = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CAProcessBVR.
     */
    public CAProcessBVR() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 11:12:27)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessBVR(BProcess parent) {
        super(parent);
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
        if (!checkParameter()) {
            return false;
        }

        // Libellé par défaut si vide
        if (JadeStringUtil.isBlank(getLibelle())) {
            setLibelle(getSession().getLabel("5328"));
        }

        try {
            // Instancier un ordre groupé
            CAOrganeExecution organeExecution = new CAOrganeExecution();

            // Charger l'ordre
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            organeExecution.retrieve(getTransaction());

            if (organeExecution.hasErrors() || organeExecution.isNew()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), organeExecution.getClass().getName());
                getMemoryLog().logMessage("5159", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Demander le traitement du BVR
            organeExecution.setRetrieveBvrFromDataBase(isRetrieveBvrFromDataBase());
            organeExecution.setMemoryLog(getMemoryLog());
            idJournalBvr = organeExecution.executeBVR(this);

            if (isAborted()) {
                return false;
            }

            if (organeExecution.hasErrors() || getTransaction().hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), organeExecution.getClass().getName());
                getMemoryLog().logMessage("5329", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            if (!getMemoryLog().isOnFatalLevel() && !getSimulation().booleanValue()) {
                organeExecution.update(getTransaction());
                if (organeExecution.hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), organeExecution.getClass().getName());
                    getMemoryLog().logMessage("5329", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        }

        return getMemoryLog().hasErrors();
    }

    /**
     * Contrôle des paramètres.
     * 
     * @return
     */
    private boolean checkParameter() {
        // Vérifier l'ordre groupé
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            getMemoryLog().logMessage("5320", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Vérifier le nom du fichier
        if (JadeStringUtil.isBlank(getFileName())) {
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
                getMemoryLog().logMessage("5327", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }
        }

        return true;
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
        if (getMemoryLog().hasErrors() || getSession().hasErrors() || isOnError()) {
            return getSession().getLabel("5301");
        } else {
            return getSession().getLabel("5300");
        }
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

    public String getIdJournalBvr() {
        return idJournalBvr;
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

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:50:45)
     * 
     * @return Boolean
     */
    @Override
    public Boolean getSimulation() {
        return simulation;
    }

    public boolean isRetrieveBvrFromDataBase() {
        return retrieveBvrFromDataBase;
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

    public void setIdJournalBvr(String idJournalBvr) {
        this.idJournalBvr = idJournalBvr;
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

    public void setRetrieveBvrFromDataBase(boolean retrieveBvrFromDataBase) {
        this.retrieveBvrFromDataBase = retrieveBvrFromDataBase;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:50:45)
     * 
     * @param newSimulation
     *            Boolean
     */
    @Override
    public void setSimulation(Boolean newSimulation) {
        simulation = newSimulation;
    }
}
