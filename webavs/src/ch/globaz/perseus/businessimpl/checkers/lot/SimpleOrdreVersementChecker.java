package ch.globaz.perseus.businessimpl.checkers.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleOrdreVersementChecker extends PerseusAbstractChecker {

    /**
     * @param simpleLot
     */
    public static void checkForCreate(SimpleOrdreVersement simpleOrdreVersement) {
        SimpleOrdreVersementChecker.checkMandatory(simpleOrdreVersement);
    }

    /**
     * @param simpleLot
     */
    public static void checkForDelete(SimpleOrdreVersement simpleOrdreVersement) {

    }

    /**
     * @param simpleLot
     */
    public static void checkForUpdate(SimpleOrdreVersement simpleOrdreVersement) {
        SimpleOrdreVersementChecker.checkMandatory(simpleOrdreVersement);
    }

    /**
     * @param simpleLot
     */
    private static void checkMandatory(SimpleOrdreVersement simpleOrdreVersement) {
        if (JadeStringUtil.isEmpty(simpleOrdreVersement.getMontantVersement())) {
            JadeThread.logError(SimpleLotChecker.class.getName(), "perseus.ordreVersement.montant.mandatory");
        }

    }

}
