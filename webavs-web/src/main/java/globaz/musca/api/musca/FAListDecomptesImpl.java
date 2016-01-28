package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.itext.list.FAListDecompteNew_Doc;

/**
 * Process de facturation pour l'impression des listes de d�comptes. Seul la m�thode <i>imprimer</i> est appel�, car les
 * traitements sur les listes sont pour l'impression et ne change pas la nature des documents. Date de cr�ation :
 * (07.04.2003 11:23:57)
 * 
 * @author: btc
 */
public class FAListDecomptesImpl extends FAGenericPrintImpl implements IntModuleFacturation {

    public final static String CS_TRI_LISTEDECOMPTE = "909001";

    /**
     * Commentaire relatif au constructeur FAListDecomptesMUSCA.
     */
    public FAListDecomptesImpl() {
        super();
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {

        FAListDecompteNew_Doc doc = new FAListDecompteNew_Doc();

        // copier le process parent
        doc.setParentWithCopy(context);
        //
        doc.setIdPassage(passage.getIdPassage());
        doc.setEMailAddress(context.getEMailAddress());
        doc.setIdTri(FAListDecomptesImpl.CS_TRI_LISTEDECOMPTE);
        // tous les d�comptes
        // doc.setIdTriDecompte(null);
        // tous les sous-types
        // doc.setIdSousType(null);
        // trier par num�ro de d�biteur
        // commencer l'impression
        doc.executeProcess();

        // contr�ler si le process a fonctionn�
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }

    }
}
