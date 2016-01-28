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

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFisc_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forIdPlausibilite = "";

    public int nbCommunication = 0;

    // initialisation pour CS langue tiers (TITIERS)
    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFisc_Doc() throws Exception {
        this(new BSession(CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    public CPImpressionCommunicationRetourDetailFisc_Doc(BProcess parent) throws FWIException {
        super(parent, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION_FISCALE");
        super.setFileTitle(getSession().getLabel("CP_MSG_0192"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPImpressionCommunicationRetourDetailFisc_Doc(BSession session) throws FWIException {
        super(session, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION_FISCALE");
        super.setFileTitle(getSession().getLabel("CP_MSG_0192"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
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

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
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
                // JadePublishDocumentInfo docInfo = createDocumentInfo();
                getDocumentInfo().setPublishDocument(true);
                getDocumentInfo().setArchiveDocument(false);
                // on remplace les fichiers sauf si on fait un envoit ged auqeul cas
                // on a besoin des fichiers unitaire.
                boolean replaceFiles = true;// getEnvoiGed().equals(Boolean.FALSE);
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

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        if ((size() == 0) || isAborted()) {
            // getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0151"),
            // globaz.framework.util.FWMessage.INFORMATION,
            // getClass().getName());
            // Permet l'affichage des données du processus
            setState(Constante.FWPROCESS_MGS_220);
            return false;
        } else { // On met la liste de document dans l'ordre
            super.DocumentSort();
            return true;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() {

    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
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
