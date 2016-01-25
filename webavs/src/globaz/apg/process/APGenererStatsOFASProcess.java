/*
 * Créé le 15 juin 05
 */
package globaz.apg.process;

import globaz.apg.itext.APListeStatsOFAS;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererStatsOFASProcess extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     */
    public APGenererStatsOFASProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APGenererStatsOFASProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APGenererStatsOFASProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {

        try {
            setSendMailOnError(true);

            APListeStatsOFAS liste = new APListeStatsOFAS();
            liste.setEMailAddress(getEMailAddress());
            liste.setForAnnee(getForAnnee());
            liste.setSession(getSession());

            liste.setParent(this);
            liste.executeProcess();

        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(), e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("LISTE_STATS_OFAS"));
            return false;
        }

        return true;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return "";
        // ce process n'envoi pas de mails (c'est un fils de générer
        // communications)
    }

    /**
     * getter pour l'attribut for annee
     * 
     * @return la valeur courante de l'attribut for annee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * setter pour l'attribut for annee
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }
}
