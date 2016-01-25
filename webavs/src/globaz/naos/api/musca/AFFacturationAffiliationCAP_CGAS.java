package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFProcessFacturationCAPCGAS;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class AFFacturationAffiliationCAP_CGAS extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public AFFacturationAffiliationCAP_CGAS() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        AFProcessFacturationCAPCGAS procFacturation = new AFProcessFacturationCAPCGAS();

        // copier le process parent
        procFacturation.setParentWithCopy(context);
        procFacturation.setPassage(passage);
        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setFacturerCAP_CGAS(true);
        // procFacturation._executeProcess(passage);
        procFacturation.setPassage(passage);
        procFacturation.setIdModuleFacturationCAPCGAS(idModuleFacturation);
        procFacturation.executeProcess();

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }
}
