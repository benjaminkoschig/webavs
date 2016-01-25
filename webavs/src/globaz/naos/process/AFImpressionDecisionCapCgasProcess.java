package globaz.naos.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.auriga.business.services.AurigaServiceLocator;

public class AFImpressionDecisionCapCgasProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String numeroPassageFacturation = null;

    public AFImpressionDecisionCapCgasProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean success = true;
        String errorMessage = "";

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // appel du service DecisionCGASService
            registerDocuments(AriesServiceLocator.getDecisionCGASService().printDecisionPassage(
                    numeroPassageFacturation));

            // appel du service DecisionCAPService
            registerDocuments(AurigaServiceLocator.getDecisionCAPService().printDecisionPassage(
                    numeroPassageFacturation));

            JadePublishDocumentInfo docInfoMergedDocument = createDocumentInfo();

            docInfoMergedDocument.setArchiveDocument(false);
            docInfoMergedDocument.setPublishDocument(true);
            this.mergePDF(docInfoMergedDocument, false, 500, false, "numero.affilie.formatte");

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (!success || isAborted() || isOnError() || getSession().hasErrors()) {
            success = false;

            // permet de ne pas envoyer le mail contenant le pdf avec l'ensemble des décisions
            getAttachedDocuments().clear();

            getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), errorMessage);
        }

        return success;

    }

    @Override
    protected String getEMailObject() {

        // Le mail est envoyé uniquement si le process s'est bien passé.
        // Il n'y a donc pas besoin de gérer l'objet lorsque le processus s'est mal passé
        return getSession().getLabel("PROCESS_IMPRESSION_DECISION_CAP_CGAS_MAIL_OBJECT_SUCCESS");
    }

    public String getNumeroPassageFacturation() {
        return numeroPassageFacturation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setNumeroPassageFacturation(String numeroPassageFacturation) {
        this.numeroPassageFacturation = numeroPassageFacturation;
    }

}
