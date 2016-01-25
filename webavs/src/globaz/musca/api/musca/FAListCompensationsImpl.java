package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;

/**
 * Process de facturation pour l'impression des listes de décomptes. Seul la méthode <i>imprimer</i> est appelé, car les
 * traitements sur les listes sont pour l'impression et ne change pas la nature des documents. Date de création :
 * (07.04.2003 11:23:57)
 * 
 * @author: btc
 */
public class FAListCompensationsImpl extends FAGenericPrintImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListDecomptesMUSCA.
     */
    public FAListCompensationsImpl() {
        super();
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {

        globaz.musca.itext.FAListCompensation_Doc doc = new globaz.musca.itext.FAListCompensation_Doc();

        // copier le process parent
        doc.setParentWithCopy(context);
        //
        doc.setIdPassage(passage.getIdPassage());
        doc.setEMailAddress(context.getEMailAddress());
        // tous les décomptes
        // doc.setIdTriDecompte(null);
        // tous les sous-types
        // doc.setIdSousType(null);
        // trier par numéro de débiteur
        // commencer l'impression
        doc.executeProcess();

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }
}
