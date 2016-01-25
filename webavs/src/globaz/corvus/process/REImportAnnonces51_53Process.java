package globaz.corvus.process;

import globaz.corvus.api.arc.downloader.REDownloaderAnnonces51_53;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;

/**
 * 
 * @author HPE
 * 
 */
public class REImportAnnonces51_53Process extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idLot = "";
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REImportAnnonces51_53Process() {
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

            REDownloaderAnnonces51_53 da51_53 = new REDownloaderAnnonces51_53();
            da51_53.setSession(getSession());
            da51_53.setParentProcess(this);
            da51_53.setIdLot(getIdLot());
            da51_53.setLog(getMemoryLog());

            da51_53.download((BTransaction) transaction);

            getMemoryLog().logMessage("Import des annonces 51/53", FWMessage.INFORMATION,
                    "REImportAnnonces51_53Process");
            if (da51_53.getNbAnnoncesTotal() > 0) {
                getMemoryLog()
                        .logMessage(
                                "Annonces importées : " + da51_53.getNbAnnoncesTotal() + "/"
                                        + da51_53.getNbAnnoncesImportees(), FWMessage.INFORMATION,
                                "REImportAnnonces51_53Process");
            } else {
                getMemoryLog().logMessage("Aucune annonce importée !", FWMessage.INFORMATION,
                        "REImportAnnonces51_53Process");
            }

            if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnWarningLevel()) {
                getMemoryLog().logMessage(da51_53.getLog().getMessagesInString(), da51_53.getLog().getErrorLevel(),
                        "REImportAnnonces51_53Process");
            }

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REImportAnnonces51_53Process");
            }
            return false;
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
                + getSession().getLabel("PROCESS_IMP_51_53_OBJET_MAIL");
    }

    public String getIdLot() {
        return idLot;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
