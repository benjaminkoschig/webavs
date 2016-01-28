package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAPassage;
import globaz.musca.api.musca.FAGenericPrintImpl;
import globaz.musca.external.IntModuleFacturation;
import java.util.Iterator;
import java.util.List;

public class AFLettreDecisionImpl extends FAGenericPrintImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListDecomptesMUSCA.
     */
    public AFLettreDecisionImpl() {
        super();
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        globaz.naos.itext.controleEmployeur.AFLettreDecision doc = new globaz.naos.itext.controleEmployeur.AFLettreDecision();

        // copier le process parent
        doc.setParentWithCopy(context);
        //
        doc.setIdPassage(passage.getIdPassage());
        doc.setEMailAddress(context.getEMailAddress());
        // doc.setPassage((FAPassage)passage);
        // tous les décomptes
        // doc.setIdTriDecompte(null);
        // tous les sous-types
        // doc.setIdSousType(null);
        // trier par numéro de débiteur
        // commencer l'impression
        // doc.setTailleLot(1);
        // doc.setImpressionParLot(true);
        doc.executeProcess();

        if ((doc.getParent() != null) && doc.hasAttachedDocuments()) {
            List e = doc.getAttachedDocuments();
            for (Iterator iter = e.iterator(); iter.hasNext();) {
                JadePublishDocument document = (JadePublishDocument) iter.next();
                doc.getParent().registerAttachedDocument(document.getPublishJobDefinition().getDocumentInfo(),
                        document.getDocumentLocation());
            }
        }

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {

        return false;
    }

}
