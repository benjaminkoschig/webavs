package ch.globaz.perseus.businessimpl.checkers.parametres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.parametres.SimpleLoyer;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author mbo
 */
public class SimpleLoyerChecker extends PerseusAbstractChecker {

    /**
     * @param simpleLoyer
     */
    public static void checkForCreate(SimpleLoyer simpleLoyer) {
        SimpleLoyerChecker.checkMandatory(simpleLoyer);
    }

    /**
     * @param simpleLoyer
     */
    public static void checkForDelete(SimpleLoyer simpleLoyer) {

    }

    /**
     * @param simpleLoyer
     */
    public static void checkForUpdate(SimpleLoyer simpleLoyer) {
        SimpleLoyerChecker.checkMandatory(simpleLoyer);
    }

    /**
     * @param simpleLoyer
     */
    private static void checkMandatory(SimpleLoyer simpleLoyer) {

        if (JadeStringUtil.isEmpty(simpleLoyer.getIdZone())) {
            JadeThread.logError(SimpleLoyerChecker.class.getName(), "perseus.parametres.simpleLoyer.idZone.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLoyer.getCsTypeLoyer())) {
            JadeThread.logError(SimpleLoyerChecker.class.getName(),
                    "perseus.parametres.simpleLoyer.csTypeLoyer.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLoyer.getMontant())) {
            JadeThread.logError(SimpleLoyerChecker.class.getName(),
                    "perseus.parametres.simpleLoyer.montantLoyer.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLoyer.getDateDebut())) {
            JadeThread.logError(SimpleLoyerChecker.class.getName(),
                    "perseus.parametres.simpleLoyer.dateDebutLoyer.mandatory");
        }
    }
}
