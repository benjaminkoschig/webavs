package ch.globaz.perseus.businessimpl.checkers.pcfaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalcul;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author vyj
 */
public class SimpleDetailsCalculChecker extends PerseusAbstractChecker {

    /**
     * @param simpleDetailsCalcul
     */
    public static void checkForCreate(SimpleDetailsCalcul simpleDetailsCalcul) {
        SimpleDetailsCalculChecker.checkMandatory(simpleDetailsCalcul);
    }

    /**
     * @param simpleDetailsCalcul
     */
    public static void checkForDelete(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException {
    }

    /**
     * @param simpleDetailsCalcul
     */
    public static void checkForUpdate(SimpleDetailsCalcul simpleDetailsCalcul) {
        SimpleDetailsCalculChecker.checkMandatory(simpleDetailsCalcul);
    }

    /**
     * @param simpleDetailsCalcul
     */
    private static void checkMandatory(SimpleDetailsCalcul simpleDetailsCalcul) {

        if (JadeStringUtil.isEmpty(simpleDetailsCalcul.getIdPCFAccordee())) {
            JadeThread.logError(SimpleDetailsCalculChecker.class.getName(),
                    "perseus.pcfaccordee.simpledetailscalcul.idPcfAccordee.mandatory");
        }

        if (JadeStringUtil.isEmpty(simpleDetailsCalcul.getTypeData())) {
            JadeThread.logError(SimpleDetailsCalculChecker.class.getName(),
                    "perseus.pcfaccordee.simpledetailscalcul.typeData.mandatory");
        }
    }
}
