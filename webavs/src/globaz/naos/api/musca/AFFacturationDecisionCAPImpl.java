package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFProcessFacturationDecisionCAP;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class AFFacturationDecisionCAPImpl extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public AFFacturationDecisionCAPImpl() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        AFProcessFacturationDecisionCAP processFacturationDecisionCap = new AFProcessFacturationDecisionCAP();
        processFacturationDecisionCap.setIdModuleFacturation(idModuleFacturation);
        processFacturationDecisionCap.setParentWithCopy(context);
        processFacturationDecisionCap.setEMailAddress(context.getEMailAddress());
        processFacturationDecisionCap.setPassageFacturation(passage);
        processFacturationDecisionCap.executeProcess();

        // return true si le process s'est terminé correctement
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }
}
