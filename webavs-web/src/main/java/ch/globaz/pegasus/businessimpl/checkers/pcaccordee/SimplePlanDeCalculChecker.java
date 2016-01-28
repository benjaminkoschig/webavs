package ch.globaz.pegasus.businessimpl.checkers.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimplePlanDeCalculChecker extends PegasusAbstractChecker {
    /**
     * @param simplePlanDeCalcul
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public static void checkForCreate(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePlanDeCalculChecker.checkMandatory(simplePlanDeCalcul);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePlanDeCalculChecker.checkIntegrity(simplePlanDeCalcul);
        }
    }

    /**
     * @param simplePlanDeCalcul
     */
    public static void checkForDelete(SimplePlanDeCalcul simplePlanDeCalcul) {
    }

    /**
     * @param simplePlanDeCalcul
     */
    public static void checkForUpdate(SimplePlanDeCalcul simplePlanDeCalcul) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simplePlanDeCalcul
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simplePlanCalcul ait un idPCAccordee. Vérifie que le simplePlanCalucl ait un isPlanRetenu non
     * null.
     * 
     * @param simplePlanDeCalcul
     */
    private static void checkMandatory(SimplePlanDeCalcul simplePlanDeCalcul) {

        // Vérifie que le simplePlanCalcul ait un IdPCAccordee
        if (JadeStringUtil.isIntegerEmpty(simplePlanDeCalcul.getIdPCAccordee())) {
            JadeThread.logError(simplePlanDeCalcul.getClass().getName(),
                    "pegasus.simplePlanDeCalcul.idPCAccordee.mandatory");
        }

        // Vérifie que le simplePlanCalcul ait un isPlanRetenu
        if (simplePlanDeCalcul.getIsPlanRetenu() == null) {
            JadeThread.logError(simplePlanDeCalcul.getClass().getName(),
                    "pegasus.simplePlanDeCalcul.isPlanRetenu.mandatory");
        }

    }
}
