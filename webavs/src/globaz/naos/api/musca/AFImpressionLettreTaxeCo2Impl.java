package globaz.naos.api.musca;

import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.globall.db.BProcess;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.api.musca.FAImpressionGenerique;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.process.FAGenericProcess;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.naos.db.taxeCo2.AFLettreTaxeCo2Manager;
import globaz.naos.itext.taxeCo2.AFLettreTaxeCo2_doc;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (20.08.2009 11:09:01)
 * 
 * @author: mar
 */
public class AFImpressionLettreTaxeCo2Impl extends FAImpressionGenerique implements IntModuleFacturation {
    /**  
 *
 */
    private AFLettreTaxeCo2Manager manager = null; // le manager

    public AFImpressionLettreTaxeCo2Impl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleImpression#add(BIEntity)
     */
    @Override
    public void add(BIEntity intEntity) throws Exception {
        entityList.add(intEntity);
    }

    @Override
    public boolean beginPrinting(IFAPassage intPassage, IFAImpressionFactureProcess c) throws Exception {

        FAPassage passage = (FAPassage) intPassage;
        FAImpressionFactureProcess context = (FAImpressionFactureProcess) c;

        if (_document == null) {
            _document = new AFLettreTaxeCo2_doc(context);
            _document.setEMailAddress(context.getEMailAddress());
        }
        _document.setSendCompletionMail(false);
        _document.setParentWithCopy(context);
        _document.setPassage(passage);

        return true;

    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        try {
            FAImpressionFactureProcess process = new FAImpressionFactureProcess();
            process.setParentWithCopy(context);

            process.setIdPassage(passage.getIdPassage());
            process.setIdEnteteFacture(((FAGenericProcess) context).getIdEnteteFacture());
            process.setEMailAddress(context.getEMailAddress());
            process.setSendCompletionMail(false);
            process.setDocumentType(FAImpressionFactureProcess.DOCTYPE_LETTER);
            process.setAImprimer(FAImpressionFactureProcess.IMPRESSION_LETTRE);
            process.setImpressionClassName(this.getClass().getName());
            process.setUnificationProcess(true);
            process._executeImpressionProcess(passage);

            // S'il y a un process parent et qu'il y a un fichier joint, on
            // remonte ce fichier vers
            // le process parent
            if ((process.getParent() != null) && process.hasAttachedDocuments()) {
                List e = process.getAttachedDocuments();
                for (Iterator iter = e.iterator(); iter.hasNext();) {
                    JadePublishDocument doc = (JadePublishDocument) iter.next();
                    process.getParent().registerAttachedDocument(doc.getPublishJobDefinition().getDocumentInfo(),
                            doc.getDocumentLocation());
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see globaz.musca.external.IntModuleImpression#get_documentManager(FAImpressionFactureProcess)
     */
    @Override
    public BIContainer get_documentManager(IFAImpressionFactureProcess c) {
        FAImpressionFactureProcess context = (FAImpressionFactureProcess) c;

        manager = new AFLettreTaxeCo2Manager();
        manager.setSession(context.getSession());
        manager.setForIdPassage(context.getIdPassage());
        manager.setForIdModuleTout(ServicesFacturation.getIdModFacturationByType(context.getSession(),
                context.getTransaction(), FAModuleFacturation.CS_MODULE_TAXE_CO2_TOUT));
        manager.setForIdModuleEnteteExiste(ServicesFacturation.getIdModFacturationByType(context.getSession(),
                context.getTransaction(), FAModuleFacturation.CS_MODULE_TAXE_CO2_ENTETE_EXISTE));

        return manager;
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    /**
     * Method set_document.
     * 
     * @param new_document
     */
    public void set_document(AFLettreTaxeCo2_doc new_document) {
        _document = new_document;
    }
}
