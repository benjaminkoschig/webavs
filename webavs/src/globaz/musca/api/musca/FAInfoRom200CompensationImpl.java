package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.process.FAInfoRom200PassageCompensationProcess;

public class FAInfoRom200CompensationImpl extends FANewCompenserImpl {

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        FAInfoRom200PassageCompensationProcess cptProc = new FAInfoRom200PassageCompensationProcess();
        // copier le process parent
        cptProc.setParentWithCopy(context);
        cptProc.setIdModuleFacturation(idModuleFacturation);
        cptProc.setIdPassage(passage.getIdPassage());
        cptProc.setEMailAddress(context.getEMailAddress());
        return cptProc._executeCompenserProcess(passage);
    }

}
