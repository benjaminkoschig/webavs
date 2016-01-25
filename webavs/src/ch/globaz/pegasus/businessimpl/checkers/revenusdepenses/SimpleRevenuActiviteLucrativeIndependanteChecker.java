package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleRevenuActiviteLucrativeIndependanteChecker extends PegasusAbstractChecker {
    /**
     * @param simpleRevenuActiviteLucrativeIndependante
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeIndependanteException
     */
    public static void checkForCreate(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleRevenuActiviteLucrativeIndependanteChecker.checkMandatory(simpleRevenuActiviteLucrativeIndependante);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRevenuActiviteLucrativeIndependanteChecker.checkIntegrity(simpleRevenuActiviteLucrativeIndependante);
        }
    }

    /**
     * @param simpleRevenuActiviteLucrativeIndependante
     */
    public static void checkForDelete(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante) {
    }

    /**
     * @param simpleRevenuActiviteLucrativeIndependante
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeIndependanteException
     */
    public static void checkForUpdate(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        SimpleRevenuActiviteLucrativeIndependanteChecker.checkMandatory(simpleRevenuActiviteLucrativeIndependante);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRevenuActiviteLucrativeIndependanteChecker.checkIntegrity(simpleRevenuActiviteLucrativeIndependante);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleRevenuActiviteLucrativeIndependante
     * @throws RevenuActiviteLucrativeIndependanteException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleRevenuActiviteLucrativeIndependante ait un csDeterminationRevenu Vérifie que le
     * simpleRevenuActiviteLucrativeIndependante ait un csGenreRevenu Vérifie que le
     * simpleRevenuActiviteLucrativeIndependante ait un montantRevenu
     * 
     * @param simpleRevenuActiviteLucrativeIndependante
     */
    private static void checkMandatory(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante) {

        // Vérifie que le simpleRevenuActiviteLucrativeIndependante ait un
        // csDeterminationRevenu
        if (JadeStringUtil.isEmpty(simpleRevenuActiviteLucrativeIndependante.getCsDeterminationRevenu())) {
            JadeThread.logError(simpleRevenuActiviteLucrativeIndependante.getClass().getName(),
                    "pegasus.simpleRevenuActiviteLucrativeIndependante.csDeterminationRevenu.mandatory");
        }

        // Vérifie que le simpleRevenuActiviteLucrativeIndependante ait un
        // csGenreRevenu
        if (JadeStringUtil.isEmpty(simpleRevenuActiviteLucrativeIndependante.getCsGenreRevenu())) {
            JadeThread.logError(simpleRevenuActiviteLucrativeIndependante.getClass().getName(),
                    "pegasus.simpleRevenuActiviteLucrativeIndependante.csGenreRevenu.mandatory");
        }

        // Vérifie que le simpleRevenuActiviteLucrativeIndependante ait un
        // montantRevenu
        if (JadeStringUtil.isBlank(simpleRevenuActiviteLucrativeIndependante.getMontantRevenu())) {
            JadeThread.logError(simpleRevenuActiviteLucrativeIndependante.getClass().getName(),
                    "pegasus.simpleRevenuActiviteLucrativeIndependante.montantRevenu.mandatory");
        }

    }
}
