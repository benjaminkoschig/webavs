package ch.globaz.vulpecula.documents.rectificatif;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * 
 * @author jpa
 *         Process de lancement de l'impression des documents rectificatifs
 */
public class PTImpressionRectificatifProcess extends BProcess {

    private String numeroPassageFacturation = null;

    public PTImpressionRectificatifProcess() {
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

            List<Decompte> listDecomptesRectificatif = rechercheDecomptesRectificatifs();

            if (!listDecomptesRectificatif.isEmpty()) {
                // Mise à jour de l'état recitifié sur le décompte
                for (Decompte decompte : listDecomptesRectificatif) {
                    decompte.setRectifie(false);
                    VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);
                }

                DocumentRectificatifPrinter docs = new DocumentRectificatifPrinter(
                        DocumentPrinter.getIds(listDecomptesRectificatif));
                docs.setEMailAddress(getEMailAddress());
                BProcessLauncher.start(docs);

                JadePublishDocumentInfo docInfoMergedDocument = createDocumentInfo();

                this.mergePDF(docInfoMergedDocument, true, 500, false, null);
            }

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

    private List<Decompte> rechercheDecomptesRectificatifs() {
        return VulpeculaServiceLocator.getDecompteService().rechercheDecomptesRectificatif();
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
