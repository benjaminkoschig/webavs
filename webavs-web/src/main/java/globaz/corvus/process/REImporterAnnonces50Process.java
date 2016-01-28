/*
 * Créé le 31 août 09
 */
package globaz.corvus.process;

import globaz.corvus.api.arc.downloader.REDownloaderAnnonces50;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;

/**
 * 
 * @author BSC
 */
public class REImporterAnnonces50Process extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String emailObject = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     */
    public REImporterAnnonces50Process() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public REImporterAnnonces50Process(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public REImporterAnnonces50Process(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {

        boolean succes = false;
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        try {
            _validate();

            REDownloaderAnnonces50 da50 = new REDownloaderAnnonces50();
            da50.setSession(session);
            da50.setParentProcess(this);
            da50.setLog(getMemoryLog());

            da50.download(transaction);

            if (!transaction.hasErrors() && !session.hasErrors() && !getMemoryLog().hasErrors()) {
                transaction.commit();
                succes = true;
            }

        } catch (Exception e) {

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(session.getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
            }

            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {

            if (succes) {
                emailObject = getSession().getLabel("EMAIL_OBJECT_GENERATION_LISTE_A_B_SUCCES");
            } else {
                emailObject = getSession().getLabel("EMAIL_OBJECT_GENERATION_LISTE_A_B_ERREUR");
            }

            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

}
