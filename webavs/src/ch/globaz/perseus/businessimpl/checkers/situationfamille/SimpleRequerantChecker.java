package ch.globaz.perseus.businessimpl.checkers.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleRequerant;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleRequerantChecker extends PerseusAbstractChecker {
    /**
     * @param requerant
     */
    public static void checkForCreate(SimpleRequerant requerant) {
        SimpleRequerantChecker.checkMandatory(requerant);
    }

    /**
     * @param requerant
     */
    public static void checkForDelete(SimpleRequerant requerant) {

    }

    /**
     * @param requerant
     */
    public static void checkForUpdate(SimpleRequerant requerant) {
        SimpleRequerantChecker.checkMandatory(requerant);
    }

    /**
     * @param requerant
     */
    private static void checkMandatory(SimpleRequerant requerant) {
        if (JadeStringUtil.isEmpty(requerant.getIdMembreFamille())) {
            JadeThread.logError(SimpleMembreFamille.class.getName(),
                    "perseus.simplerequerant.idMembreFamille.mandatory");
        }
    }

}
