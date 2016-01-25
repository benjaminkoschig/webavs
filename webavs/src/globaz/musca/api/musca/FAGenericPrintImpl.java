package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.IFAPassage;
import globaz.musca.process.FAImpressionFactureProcess;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class FAGenericPrintImpl {

    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public FAGenericPrintImpl() {
        super();
    }

    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return imprimer(passage, context);
    }

    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        try {

            FAImpressionFactureProcess process = new FAImpressionFactureProcess();
            process.setParentWithCopy(context);
            process.setIdPassage(passage.getIdPassage());
            process._executeImpressionProcess(null);
            // S'il y a un process parent et qu'il y a un fichier joint, on
            // remonte ce fichier vers
            // le process parent
            if ((process.getParent() != null) && process.hasAttachedDocuments()) {
                List<?> e = process.getAttachedDocuments();
                for (Iterator<?> iter = e.iterator(); iter.hasNext();) {
                    JadePublishDocumentInfo docInfo = process.getParent().createDocumentInfo();
                    process.getParent().registerAttachedDocument(docInfo,
                            ((JadePublishDocument) iter.next()).getDocumentLocation());
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

    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
