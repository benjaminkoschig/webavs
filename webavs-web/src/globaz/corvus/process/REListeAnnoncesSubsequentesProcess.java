package globaz.corvus.process;

import globaz.corvus.itext.REListeAnnonces;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * 
 * @author HPE
 * 
 */
public class REListeAnnoncesSubsequentesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REListeAnnoncesSubsequentesProcess() {
        super();
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            // Liste des annonces subséquentes
            REListeAnnonces process = new REListeAnnonces(getSession());

            process.setEMailAddress(getEMailAddress());
            process.setMois(getMoisAnnee());
            process.setIsAnnoncesSubsequentes(true);
            process.setTransaction(transaction);
            process.setParent(this);
            process.executeProcess();

            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            mergePDF(info, true, 500, false, null);

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REListeAnnoncesSubsequentesProcess");
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_LISTE_ANN_SUB_OBJET_MAIL");
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
