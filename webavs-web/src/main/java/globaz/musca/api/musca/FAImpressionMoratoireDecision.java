package globaz.musca.api.musca;

import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.globall.db.BProcess;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FADetailInteretMoratoire_Doc;
import globaz.musca.process.FAGenericProcess;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.osiris.db.interets.CAInteretMoratoireImpressionManager;
import java.util.Iterator;
import java.util.List;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Process de d'impression pour l'impression des listes de décomptes. Seul la méthode <i>imprimer</i> est appelé, car
 * les traitements sur les listes sont pour l'impression et ne change pas la nature des documents. Date de création :
 * (07.04.2003 11:23:57)
 * 
 * @author: btc
 */
public class FAImpressionMoratoireDecision extends FAImpressionGenerique {
    private CAInteretMoratoireImpressionManager manager = null; // le manager

    /**
     * Commentaire relatif au constructeur FAListDecomptesMUSCA.
     */
    public FAImpressionMoratoireDecision() {
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
            _document = new FADetailInteretMoratoire_Doc(context);
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
            process.setImpressionClassName(this.getClass().getName());
            process.setEnvoyerGed(((FAGenericProcess) context).getEnvoyerGed());
            process.setCallEcran(((FAGenericProcess) context).getCallEcran());
            process.setUnificationProcess(true);
            process._executeImpressionProcess(passage);

            // S'il y a un process parent et qu'il y a un fichier joint, on
            // remonte ce fichier vers
            // le process parent
            if ((process.getParent() != null) && process.hasAttachedDocuments()) {
                List<?> e = process.getAttachedDocuments();
                for (Iterator<?> iter = e.iterator(); iter.hasNext();) {
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

        manager = new CAInteretMoratoireImpressionManager();
        manager.setSession(context.getSession());
        manager.setForIdJournalFacturation(context.getIdPassage());
        manager.setForFacturable(true);

        return manager;
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Method set_document.
     * 
     * @param new_document
     */
    public void set_document(FADetailInteretMoratoire_Doc new_document) {
        _document = new_document;
    }

}
