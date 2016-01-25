package ch.globaz.perseus.businessimpl.checkers.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.dossier.SimpleDossier;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author vyj
 */
public class SimpleDossierChecker extends PerseusAbstractChecker {

    /**
     * @param dossier
     */
    public static void checkForCreate(SimpleDossier dossier) {
        SimpleDossierChecker.checkMandatory(dossier);
    }

    /**
     * @param dossier
     */
    public static void checkForDelete(SimpleDossier dossier) {

    }

    /**
     * @param dossier
     */
    public static void checkForUpdate(SimpleDossier dossier) {
        SimpleDossierChecker.checkMandatory(dossier);
    }

    /**
     * @param dossier
     */
    private static void checkMandatory(SimpleDossier dossier) {
        if (JadeStringUtil.isEmpty(dossier.getIdDemandePrestation())) {
            JadeThread.logError(SimpleDossierChecker.class.getName(),
                    "perseus.dossier.simpleDossier.idDemandePrestation.mandatory");
        }
    }
}
