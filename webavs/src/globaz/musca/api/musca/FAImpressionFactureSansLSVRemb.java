package globaz.musca.api.musca;

import globaz.globall.api.BIContainer;
import globaz.globall.db.BProcess;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FAImpressionFacture_BVR_Doc;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.process.FAImpressionFactureProcessSansLSVRemb;
import java.util.Iterator;
import java.util.List;

/**
 * Process de d'impression pour l'impression des listes de décomptes. Seul la méthode <i>imprimer</i> est appelé, car
 * les traitements sur les listes sont pour l'impression et ne change pas la nature des documents. Date de création :
 * (07.04.2003 11:23:57)
 * 
 * @author: btc
 */
public class FAImpressionFactureSansLSVRemb extends FAImpressionGenerique {

    private FAEnteteFactureManager manager = null;

    /**
     * Commentaire relatif au constructeur FAListDecomptesMUSCA.
     */
    public FAImpressionFactureSansLSVRemb() {
        super();
    }

    @Override
    public boolean beginPrinting(IFAPassage intPassage, IFAImpressionFactureProcess c) throws Exception {
        FAPassage passage = (FAPassage) intPassage;
        FAImpressionFactureProcessSansLSVRemb context = (FAImpressionFactureProcessSansLSVRemb) c;

        if (_document == null) {
            _document = new FAImpressionFacture_BVR_Doc(context);
            _document.setEMailAddress(context.getEMailAddress());
        }
        _document.setSendCompletionMail(false);
        _document.setParentWithCopy(context);
        _document.setPassage(passage);
        _document.setJadeUser(context.getJadeUser());

        // _document.setDateImpression(aDate);
        return true;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        try {
            FAImpressionFactureProcessSansLSVRemb process = new FAImpressionFactureProcessSansLSVRemb();
            process.setParentWithCopy(context);
            process.getMemoryLog();
            process.setIdPassage(passage.getIdPassage());
            process.setEMailAddress(context.getEMailAddress());
            process.setIdTri(FAEnteteFacture.CS_TRI_DEBITEUR);
            process.setSendCompletionMail(false);
            process.setActionModulePassage(FAModulePassage.CS_ACTION_IMPRIMER);
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

    @Override
    public BIContainer get_documentManager(IFAImpressionFactureProcess c) {
        FAImpressionFactureProcess context = (FAImpressionFactureProcess) c;

        manager = new FAEnteteFactureManager();
        manager.setSession(context.getSession());
        manager.setForIdPassage(context.getIdPassage());
        return manager;
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 10:34:56)
     * 
     * @param new_document
     *            globaz.musca.itext.FAImpressionFacture_BVR_Doc
     */
    public void set_document(globaz.musca.itext.FAImpressionFacture_BVR_Doc new_document) {
        _document = new_document;
    }
}