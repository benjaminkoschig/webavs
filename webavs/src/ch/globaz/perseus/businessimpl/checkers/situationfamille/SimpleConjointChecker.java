package ch.globaz.perseus.businessimpl.checkers.situationfamille;

import ch.globaz.perseus.business.models.situationfamille.SimpleConjoint;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleConjointChecker extends PerseusAbstractChecker {
    /**
     * @param conjoint
     */
    public static void checkForCreate(SimpleConjoint conjoint) {
        SimpleConjointChecker.checkMandatory(conjoint);
    }

    /**
     * @param conjoint
     */
    public static void checkForDelete(SimpleConjoint conjoint) {

    }

    /**
     * @param conjoint
     */
    public static void checkForUpdate(SimpleConjoint conjoint) {
        SimpleConjointChecker.checkMandatory(conjoint);
    }

    /**
     * @param conjoint
     */
    private static void checkMandatory(SimpleConjoint conjoint) {

    }

}
