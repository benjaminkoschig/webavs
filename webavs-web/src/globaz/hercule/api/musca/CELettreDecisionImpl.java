package globaz.hercule.api.musca;

import globaz.globall.db.BProcess;
import globaz.hercule.itext.controleEmployeur.CELettreDecision;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAPassage;
import globaz.musca.api.musca.FAGenericPrintImpl;
import globaz.musca.external.IntModuleFacturation;
import java.util.Iterator;
import java.util.List;

/**
 * @author SCO
 * @since 1 déc. 2010
 */
public class CELettreDecisionImpl extends FAGenericPrintImpl implements IntModuleFacturation {

    /**
     * Constructeur de CELettreDecisionImpl
     */
    public CELettreDecisionImpl() {
        super();
    }

    /**
     * @see globaz.musca.api.musca.FAGenericPrintImpl#comptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        CELettreDecision doc = new CELettreDecision();

        // copier le process parent
        doc.setParentWithCopy(context);
        doc.setIdPassage(passage.getIdPassage());
        doc.setEMailAddress(context.getEMailAddress());
        doc.executeProcess();

        if (doc.getParent() != null && doc.hasAttachedDocuments()) {
            List<JadePublishDocument> e = doc.getAttachedDocuments();
            for (Iterator<JadePublishDocument> iter = e.iterator(); iter.hasNext();) {
                JadePublishDocument document = iter.next();
                doc.getParent().registerAttachedDocument(document.getPublishJobDefinition().getDocumentInfo(),
                        document.getDocumentLocation());
            }
        }

        // contrôler si le process a fonctionné
        if (context.isAborted()) {
            return false;
        }

        return true;
    }

    /**
     * @see globaz.musca.api.musca.FAGenericPrintImpl#imprimer(globaz.musca.api.IFAPassage, globaz.globall.db.BProcess)
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {

        return false;
    }

}
