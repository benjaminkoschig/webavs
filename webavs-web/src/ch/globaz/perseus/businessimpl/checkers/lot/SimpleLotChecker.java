package ch.globaz.perseus.businessimpl.checkers.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.lot.SimpleLot;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleLotChecker extends PerseusAbstractChecker {

    /**
     * @param simpleLot
     */
    public static void checkForCreate(SimpleLot simpleLot) {
        SimpleLotChecker.checkMandatory(simpleLot);
    }

    /**
     * @param simpleLot
     */
    public static void checkForDelete(SimpleLot simpleLot) {

    }

    /**
     * @param simpleLot
     */
    public static void checkForUpdate(SimpleLot simpleLot) {
        SimpleLotChecker.checkMandatory(simpleLot);
    }

    /**
     * @param simpleLot
     */
    private static void checkMandatory(SimpleLot simpleLot) {
        if (JadeStringUtil.isEmpty(simpleLot.getDateCreation())) {
            JadeThread.logError(SimpleLotChecker.class.getName(), "perseus.lot.datecreation.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLot.getTypeLot())) {
            JadeThread.logError(SimpleLotChecker.class.getName(), "perseus.lot.typelot.mandatory");
        }

    }

}
