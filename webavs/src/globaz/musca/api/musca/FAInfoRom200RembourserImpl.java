package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.process.FAInfoRom200PassageRemboursementProcess;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class FAInfoRom200RembourserImpl extends FARembourserImpl {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public FAInfoRom200RembourserImpl() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        FAInfoRom200PassageRemboursementProcess cptProc = new FAInfoRom200PassageRemboursementProcess();
        // copier le process parent
        cptProc.setParentWithCopy(context);
        //
        cptProc.setIdPassage(passage.getIdPassage());
        cptProc.setEMailAddress(context.getEMailAddress());
        return cptProc._executeRemboursementProcess(passage);
    }
}
