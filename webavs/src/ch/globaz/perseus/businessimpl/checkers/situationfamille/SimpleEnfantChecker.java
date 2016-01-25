package ch.globaz.perseus.businessimpl.checkers.situationfamille;

import ch.globaz.perseus.business.models.situationfamille.SimpleEnfant;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleEnfantChecker extends PerseusAbstractChecker {
    /**
     * @param enfant
     */
    public static void checkForCreate(SimpleEnfant enfant) {
        SimpleEnfantChecker.checkMandatory(enfant);
    }

    /**
     * @param enfant
     */
    public static void checkForDelete(SimpleEnfant enfant) {

    }

    /**
     * @param enfant
     */
    public static void checkForUpdate(SimpleEnfant enfant) {
        SimpleEnfantChecker.checkMandatory(enfant);
    }

    /**
     * @param enfant
     */
    private static void checkMandatory(SimpleEnfant enfant) {

    }

}
