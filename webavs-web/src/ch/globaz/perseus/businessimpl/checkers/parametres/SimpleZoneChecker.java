package ch.globaz.perseus.businessimpl.checkers.parametres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.parametres.SimpleZone;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author vyj
 */
public class SimpleZoneChecker extends PerseusAbstractChecker {

    /**
     * @param simpleZone
     */
    public static void checkForCreate(SimpleZone simpleZone) {
        SimpleZoneChecker.checkMandatory(simpleZone);
    }

    /**
     * @param simpleZone
     */
    public static void checkForDelete(SimpleZone simpleZone) {

    }

    /**
     * @param simpleZone
     */
    public static void checkForUpdate(SimpleZone simpleZone) {
        SimpleZoneChecker.checkMandatory(simpleZone);
    }

    /**
     * @param simpleZone
     */
    private static void checkMandatory(SimpleZone simpleZone) {
        if (JadeStringUtil.isEmpty(simpleZone.getDesignation())) {
            JadeThread.logError(SimpleZoneChecker.class.getName(),
                    "perseus.parametres.simpleZone.designation.mandatory");
        }
    }
}
