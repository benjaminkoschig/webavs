/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.qd;

import ch.globaz.perseus.business.models.qd.SimpleQDAnnuelle;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author DDE
 * 
 */
public class SimpleQDAnnuelleChecker extends PerseusAbstractChecker {

    /**
     * @param simpleQDAnnuelle
     */
    public static void checkForCreate(SimpleQDAnnuelle simpleQDAnnuelle) {
        SimpleQDAnnuelleChecker.checkMandatory(simpleQDAnnuelle);
    }

    /**
     * @param simpleQDAnnuelle
     */
    public static void checkForDelete(SimpleQDAnnuelle simpleQDAnnuelle) {

    }

    /**
     * @param simpleQDAnnuelle
     */
    public static void checkForUpdate(SimpleQDAnnuelle simpleQDAnnuelle) {
        SimpleQDAnnuelleChecker.checkMandatory(simpleQDAnnuelle);
    }

    /**
     * @param simpleQDAnnuelle
     */
    private static void checkMandatory(SimpleQDAnnuelle simpleQDAnnuelle) {

    }

}
