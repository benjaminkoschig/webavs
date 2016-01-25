package globaz.corvus.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;

/**
 * 
 * @author HPE
 * 
 */
public class REEnvoiAnnoncesSubsequentesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REEnvoiAnnoncesSubsequentesProcess() {
        super();
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            // Envoi des annonces subséquentes

            // Envoi des annonces 43 et 46 stocker dans les tables
            // pour le bon mois rapport (décembre)
            REEnvoyerAnnoncesProcess process = new REEnvoyerAnnoncesProcess(getSession());

            process.setEMailAddress(getEMailAddress());
            process.setForDateEnvoi(JACalendar.todayJJsMMsAAAA());
            process.setForMoisAnneeComptable(getMoisAnnee());
            process.setIsForAnnoncesSubsequentes(true);
            process.setParent(this);
            process.executeProcess();

            return true;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REEnvoiAnnoncesSubsequentesProcess");
            return false;
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_ENVOI_ANN_SUB_OBJET_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
