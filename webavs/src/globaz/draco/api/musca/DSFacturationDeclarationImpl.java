package globaz.draco.api.musca;

import globaz.draco.process.DSProcessFacturationDeclarationSalaire;
import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class DSFacturationDeclarationImpl extends DSFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public DSFacturationDeclarationImpl() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        DSProcessFacturationDeclarationSalaire procFacturation = new DSProcessFacturationDeclarationSalaire();

        // copier le process parent
        procFacturation.setParentWithCopy(context);

        FAPassage myPassage = (FAPassage) passage;
        procFacturation.setPassage(myPassage);

        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setSendCompletionMail(false);
        procFacturation.setModeFacturation(DSProcessFacturationDeclarationSalaire.MODE_STANDARD);
        procFacturation.executeProcess();
        // contr�ler si le process a fonctionn�
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }
}
