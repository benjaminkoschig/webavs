package ch.globaz.perseus.businessimpl.checkers.decision;

import ch.globaz.perseus.business.models.decision.SimpleCopieDecision;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleCopieDecisionChecker extends PerseusAbstractChecker {

    /**
     * @param simpleMotif
     */
    public static void checkForCreate(SimpleCopieDecision simpleCopieDecision) {
        SimpleCopieDecisionChecker.checkMandatory(simpleCopieDecision);
    }

    /**
     * @param simpleMotif
     */
    public static void checkForDelete(SimpleCopieDecision simpleCopieDecision) {

    }

    /**
     * @param simpleMotif
     */
    public static void checkForUpdate(SimpleCopieDecision simpleCopieDecision) {
        SimpleCopieDecisionChecker.checkMandatory(simpleCopieDecision);
    }

    /**
     * @param simpleMotif
     */
    private static void checkMandatory(SimpleCopieDecision simpleCopieDecision) {

        /*
         * Aucuns message d'erreur
         */
    }

}
