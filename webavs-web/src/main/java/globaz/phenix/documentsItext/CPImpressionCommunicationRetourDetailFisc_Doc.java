package globaz.phenix.documentsItext;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.util.Constante;

public class CPImpressionCommunicationRetourDetailFisc_Doc extends FWIDocumentManager {

    private static final long serialVersionUID = 1L;

    private String forIdPlausibilite = "";

    public int nbCommunication = 0;

    // initialisation pour CS langue tiers (TITIERS)

    public CPImpressionCommunicationRetourDetailFisc_Doc() throws Exception {
        this(new BSession(CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    public CPImpressionCommunicationRetourDetailFisc_Doc(BProcess parent) throws FWIException {
        super(parent, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION_FISCALE");
        super.setFileTitle(getSession().getLabel("CP_MSG_0192"));
    }

    public CPImpressionCommunicationRetourDetailFisc_Doc(BSession session) throws FWIException {
        super(session, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION_FISCALE");
        super.setFileTitle(getSession().getLabel("CP_MSG_0192"));
    }

    @Override
    protected void _executeCleanUp() {
        if ((getTransaction() != null) && (getTransaction().isOpened())) {
            try {
                getTransaction().closeTransaction();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        // Permet l'affichage des données du processus
        setState(Constante.FWPROCESS_MGS_220);
        super._executeCleanUp();
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (!JadeStringUtil.isEmpty(getMessage())) {
            abort();
        }
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
    }

    @Override
    public void afterExecuteReport() {
        try {
            if (nbCommunication > 0) {
                getMemoryLog().logMessage(
                        "\n" + getSession().getLabel("CP_MSG_0149") + " " + nbCommunication + " "
                                + getSession().getLabel("CP_MSG_0179"), globaz.framework.util.FWMessage.INFORMATION,
                        this.getClass().getName());
                getDocumentInfo().setPublishDocument(true);
                getDocumentInfo().setArchiveDocument(false);
                // on remplace les fichiers sauf si on fait un envoit ged auqeul cas
                // on a besoin des fichiers unitaire.
                boolean replaceFiles = true;
                this.mergePDF(getDocumentInfo(), replaceFiles, 500, true, null, null);
            } else {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0151"),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.afterExecuteReport();
    }

    @Override
    public void beforeBuildReport() {
        // Nothing
    }

    @Override
    public void beforeExecuteReport() {
        // Nothing
    }

    @Override
    public boolean beforePrintDocument() {
        if ((size() == 0) || isAborted()) {

            // Permet l'affichage des données du processus
            setState(Constante.FWPROCESS_MGS_220);
            return false;
        } else { // On met la liste de document dans l'ordre
            super.DocumentSort();
            return true;
        }
    }

    @Override
    public void createDataSource() {
        // Nothing
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        return false;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

}
