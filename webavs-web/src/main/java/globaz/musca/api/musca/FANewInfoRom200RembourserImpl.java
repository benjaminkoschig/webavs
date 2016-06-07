package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.process.FANewInfoRom200PassageRemboursementProcess;

public class FANewInfoRom200RembourserImpl extends FARembourserImpl {

    public FANewInfoRom200RembourserImpl() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        FANewInfoRom200PassageRemboursementProcess cptProc = new FANewInfoRom200PassageRemboursementProcess();
        // copier le process parent
        cptProc.setParentWithCopy(context);
        //
        cptProc.setIdPassage(passage.getIdPassage());
        cptProc.setEMailAddress(context.getEMailAddress());
        return cptProc._executeRemboursementProcess(passage);
    }
}
