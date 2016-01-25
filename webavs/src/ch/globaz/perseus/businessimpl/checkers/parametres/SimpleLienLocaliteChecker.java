package ch.globaz.perseus.businessimpl.checkers.parametres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.parametres.SimpleLienLocalite;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleLienLocaliteChecker extends PerseusAbstractChecker {

    /**
     * @param simpleLienLocalite
     */
    public static void checkForCreate(SimpleLienLocalite simpleLienLocalite) {
        SimpleLienLocaliteChecker.checkMandatory(simpleLienLocalite);
    }

    /**
     * @param simpleLienLocalite
     */
    public static void checkForDelete(SimpleLienLocalite simpleLienLocalite) {

    }

    /**
     * @param simpleLienLocalite
     */
    public static void checkForUpdate(SimpleLienLocalite simpleLienLocalite) {
        SimpleLienLocaliteChecker.checkMandatory(simpleLienLocalite);
    }

    /**
     * @param simpleLienLocalite
     */
    private static void checkMandatory(SimpleLienLocalite simpleLienLocalite) {
        if (JadeStringUtil.isEmpty(simpleLienLocalite.getIdLocalite())) {
            JadeThread.logError(SimpleLienLocaliteChecker.class.getName(),
                    "perseus.parametres.simpleLoyer.idLocalite.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLienLocalite.getIdZone())) {
            JadeThread.logError(SimpleLienLocaliteChecker.class.getName(),
                    "perseus.parametres.simpleLienLocalite.idZone.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLienLocalite.getDateDebut())) {
            JadeThread.logError(SimpleLienLocaliteChecker.class.getName(),
                    "perseus.parametres.simpleLoyer.dateDebut.mandatory");
        }

    }

}
