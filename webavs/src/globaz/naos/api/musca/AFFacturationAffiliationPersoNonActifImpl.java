package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazServer;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFNewProcessFacturation;
import globaz.naos.process.AFProcessFacturation;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class AFFacturationAffiliationPersoNonActifImpl extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public AFFacturationAffiliationPersoNonActifImpl() {
        super();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        FAApplication appAf = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                FAApplication.DEFAULT_APPLICATION_MUSCA);
        if (appAf.wantOldProcessFacturation()) {
            AFProcessFacturation procFacturation = new AFProcessFacturation();
            procFacturation.setParentWithCopy(context);
            procFacturation.setFacturerPersonnel(true);
            procFacturation.setTypeFacturationPersonnelle(AFProcessFacturation.PERIODIQUE_COT_PERS_NAC);
            procFacturation.setPassage(passage);
            procFacturation.setEMailAddress(context.getEMailAddress());
            procFacturation.executeProcess();
        } else {
            AFNewProcessFacturation procFacturation = new AFNewProcessFacturation();
            procFacturation.setParentWithCopy(context);
            procFacturation.setFacturerPersonnel(true);
            procFacturation.setTypeFacturationPersonnelle(AFProcessFacturation.PERIODIQUE_COT_PERS_NAC);
            procFacturation.setPassage(passage);
            procFacturation.setEMailAddress(context.getEMailAddress());
            procFacturation.setIdModuleFacturation(idModuleFacturation);
            procFacturation.executeProcess();
        }
        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }
}
