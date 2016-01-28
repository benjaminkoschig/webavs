/**
 * 
 */
package globaz.amal.vb.reprise;

import globaz.amal.process.reprise.AMRepriseTiersProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.log.JadeLogger;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author DHI
 * 
 */
public class AMRepriseViewBean extends BJadePersistentObjectViewBean {

    private Boolean askToStop = true;
    private Boolean checkDataBeforeReprise = false;
    private Boolean isSimulation = true;
    private String nbEnregistrements = "0";
    private String nbEnregistrementsEnCours = "0";
    private String repriseStatus = "0";

    /**
	 * 
	 */
    public AMRepriseViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the askToStop
     */
    public Boolean getAskToStop() {
        return askToStop;
    }

    /**
     * @return the checkDataBeforeReprise
     */
    public Boolean getCheckDataBeforeReprise() {
        return checkDataBeforeReprise;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * @return the isSimulation
     */
    public Boolean getIsSimulation() {
        return isSimulation;
    }

    /**
     * @return the nbEnregistrements
     */
    public String getNbEnregistrements() {
        return nbEnregistrements;
    }

    /**
     * @return the nbEnregistrementsEnCours
     */
    public String getNbEnregistrementsEnCours() {
        return nbEnregistrementsEnCours;
    }

    /**
     * @return the repriseStatus
     */
    public String getRepriseStatus() {
        return repriseStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Exécution du job de contrôle des tables DB2
     */
    public void launchCheckDB2Tables() {

    }

    /**
     * Exécution du job de génération des tables de journalisation
     */
    public void launchGenerateLibra() {

    }

    /**
     * Exécution du job de reprise tiers
     */
    public void launchRepriseTiers() {
        AMRepriseTiersProcess processTiers = new AMRepriseTiersProcess();
        processTiers.setCheckDataBeforeReprise(checkDataBeforeReprise);
        processTiers.setIsSimulation(isSimulation);
        processTiers.setAskToStop(askToStop);
        processTiers.setSession(BSessionUtil.getSessionFromThreadContext());

        // Launch the process
        try {
            if (askToStop) {
                // ASK TO STOP
                JadeLogger.info(this, "ASKING TO STOP THE REPRISE");
                try {
                    processTiers.updateInnerMF("0", "-1");
                } catch (Exception ex) {
                    JadeLogger.error(this, "Error setting inner mf 0 -1 : " + ex.toString());
                }
            } else {
                // RUN
                try {
                    processTiers.updateInnerMF("0", "2");
                    BProcessLauncher.start(processTiers, false);
                } catch (Exception ex) {
                    JadeLogger.error(this, "Error setting inner mf 0 -1 : " + ex.toString());
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error launching process reprise Tiers : " + e.toString());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        try {
            nbEnregistrements = AmalServiceLocator.getContribuableRepriseService().getNbTotalEnregistrements();
            nbEnregistrementsEnCours = AmalServiceLocator.getContribuableRepriseService().getNbEnCoursEnregistrements();
            repriseStatus = AmalServiceLocator.getContribuableRepriseService().getRepriseStatus();
        } catch (Exception ex) {
            JadeLogger.error(this, "Error retrieving reprise counter : " + ex.toString());
        }
    }

    /**
     * @param askToStop
     *            the askToStop to set
     */
    public void setAskToStop(Boolean askToStop) {
        this.askToStop = askToStop;
    }

    /**
     * @param checkDataBeforeReprise
     *            the checkDataBeforeReprise to set
     */
    public void setCheckDataBeforeReprise(Boolean checkDataBeforeReprise) {
        this.checkDataBeforeReprise = checkDataBeforeReprise;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
    }

    /**
     * @param isSimulation
     *            the isSimulation to set
     */
    public void setIsSimulation(Boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

    /**
     * @param nbEnregistrements
     *            the nbEnregistrements to set
     */
    public void setNbEnregistrements(String nbEnregistrements) {
        this.nbEnregistrements = nbEnregistrements;
    }

    /**
     * @param nbEnregistrementsEnCours
     *            the nbEnregistrementsEnCours to set
     */
    public void setNbEnregistrementsEnCours(String nbEnregistrementsEnCours) {
        this.nbEnregistrementsEnCours = nbEnregistrementsEnCours;
    }

    /**
     * @param repriseStatus
     *            the repriseStatus to set
     */
    public void setRepriseStatus(String repriseStatus) {
        this.repriseStatus = repriseStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }

}
