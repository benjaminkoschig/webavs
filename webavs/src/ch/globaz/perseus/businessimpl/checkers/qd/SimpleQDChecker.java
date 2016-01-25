/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.qd;

import ch.globaz.perseus.business.models.qd.SimpleQD;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author DDE
 * 
 */
public class SimpleQDChecker extends PerseusAbstractChecker {

    /**
     * @param simpleQD
     */
    public static void checkForCreate(SimpleQD simpleQD) {
        SimpleQDChecker.checkMandatory(simpleQD);
    }

    /**
     * @param simpleQD
     */
    public static void checkForDelete(SimpleQD simpleQD) {

    }

    /**
     * @param simpleQD
     */
    public static void checkForUpdate(SimpleQD simpleQD) {
        SimpleQDChecker.checkMandatory(simpleQD);
    }

    /**
     * @param simpleQD
     */
    private static void checkMandatory(SimpleQD simpleQD) {

    }

}
