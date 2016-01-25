package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;
import java.util.Iterator;
import java.util.List;

/**
 * Process de facturation pour l'impression des listes de décomptes. Seul la méthode <i>imprimer</i> est appelé, car les
 * traitements sur les listes sont pour l'impression et ne change pas la nature des documents. Date de création :
 * (07.04.2003 11:23:57)
 * 
 * @author: btc
 */
public class FALettreRentierNAImpl extends FAGenericPrintImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListDecomptesMUSCA.
     */
    public FALettreRentierNAImpl() {
        super();
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        globaz.musca.itext.FALettreRentierNA_Doc doc = new globaz.musca.itext.FALettreRentierNA_Doc();

        // copier le process parent
        doc.setParentWithCopy(context);
        //
        doc.setIdPassage(passage.getIdPassage());
        doc.setEMailAddress(context.getEMailAddress());
        doc.setPassage((FAPassage) passage);
        // tous les décomptes
        // doc.setIdTriDecompte(null);
        // tous les sous-types
        // doc.setIdSousType(null);
        // trier par numéro de débiteur
        // commencer l'impression
        doc.executeProcess();

        if ((doc.getParent() != null) && doc.hasAttachedDocuments()) {
            List<?> e = doc.getAttachedDocuments();
            for (Iterator<?> iter = e.iterator(); iter.hasNext();) {
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

        // globaz.musca.itext.FALettreRentierNA_Doc doc =
        // new globaz.musca.itext.FALettreRentierNA_Doc();
        //
        // //copier le process parent
        // doc.setParentWithCopy(context);
        // //
        // doc.setIdPassage(passage.getIdPassage());
        // doc.setEMailAddress(context.getEMailAddress());
        // //tous les décomptes
        // //doc.setIdTriDecompte(null);
        // //tous les sous-types
        // //doc.setIdSousType(null);
        // //trier par numéro de débiteur
        // //commencer l'impression
        // doc.executeProcess();
        //
        // //contrôler si le process a fonctionné
        // if (!context.isAborted()){
        // return true;
        // } else
        return false;
    }

}
