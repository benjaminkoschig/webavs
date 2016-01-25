package ch.globaz.perseus.businessimpl.checkers.impotsource;

import ch.globaz.perseus.business.models.impotsource.SimpleTaux;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleTauxChecker extends PerseusAbstractChecker {

    /**
     * @param simpleTaux
     */
    public static void checkForCreate(SimpleTaux simpleTaux) {
        SimpleTauxChecker.checkMandatory(simpleTaux);
    }

    /**
     * @param simpleTaux
     */
    public static void checkForDelete(SimpleTaux simpleTaux) {

    }

    /**
     * @param simpleTaux
     */
    public static void checkForUpdate(SimpleTaux simpleTaux) {
        SimpleTauxChecker.checkMandatory(simpleTaux);
    }

    /**
     * @param simpleTaux
     */
    private static void checkMandatory(SimpleTaux simpleTaux) {

        // if (JadeStringUtil.isEmpty(simpleTaux.getAnneeTaux())) {
        // JadeThread
        // .logError(SimpleTauxChecker.class.getName(), "perseus.impotsource.simpletaux.anneetaux.mandatory");
        // }
        // if (JadeStringUtil.isEmpty(simpleTaux.getValeurTaux())) {
        // JadeThread.logError(SimpleTauxChecker.class.getName(),
        // "perseus.impotsource.simpletaux.valeurtaux.mandatory");
        // }

    }
}
