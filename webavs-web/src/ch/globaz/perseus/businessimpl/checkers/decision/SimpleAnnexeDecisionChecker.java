package ch.globaz.perseus.businessimpl.checkers.decision;

import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecision;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleAnnexeDecisionChecker extends PerseusAbstractChecker {

    /**
     * @param simpleDecision
     */
    public static void checkForCreate(SimpleAnnexeDecision simpleAnnexeDecision) {
        SimpleAnnexeDecisionChecker.checkMandatory(simpleAnnexeDecision);
    }

    /**
     * @param simpleDecision
     */
    public static void checkForDelete(SimpleAnnexeDecision simpleAnnexeDecision) {

    }

    /**
     * @param simpleDecision
     */
    public static void checkForUpdate(SimpleAnnexeDecision simpleAnnexeDecision) {
        SimpleAnnexeDecisionChecker.checkMandatory(simpleAnnexeDecision);
    }

    /**
     * @param simpleDecision
     */
    private static void checkMandatory(SimpleAnnexeDecision simpleAnnexeDecision) {

        /*
         * Aucuns message d'erreur
         */
    }

}
