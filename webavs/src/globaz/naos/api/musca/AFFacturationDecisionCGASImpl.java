package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFProcessFacturationDecisionCGAS;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class AFFacturationDecisionCGASImpl extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public AFFacturationDecisionCGASImpl() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        AFProcessFacturationDecisionCGAS processFacturationDecisionCgas = new AFProcessFacturationDecisionCGAS();
        processFacturationDecisionCgas.setIdModuleFacturation(idModuleFacturation);
        processFacturationDecisionCgas.setParentWithCopy(context);
        processFacturationDecisionCgas.setEMailAddress(context.getEMailAddress());
        processFacturationDecisionCgas.setPassageFacturation(passage);
        processFacturationDecisionCgas.executeProcess();

        // return true si le process s'est terminé correctement
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }
}
