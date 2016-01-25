package ch.globaz.pegasus.businessimpl.checkers.pcaccordee;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * 
 * @author sce
 * 
 */

public class SimpleAllocationNoelChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAllocationNoel
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public static void checkForCreate(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleAllocationNoelChecker.checkMandatory(simpleAllocationNoel);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAllocationNoelChecker.checkIntegrity(simpleAllocationNoel);
        }
    }

    /**
     * @param simpleAllocationNoel
     */
    public static void checkForDelete(SimpleAllocationNoel simpleAllocationNoel) {
    }

    /**
     * @param simpleAllocationNoel
     */
    public static void checkForUpdate(SimpleAllocationNoel simpleAllocationNoel) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAllocationNoel
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            JadeNoBusinessLogSessionError {
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * 
     * 
     * @param simpleAllocationNoel
     */
    private static void checkMandatory(SimpleAllocationNoel simpleAllocationNoel) {

    }
}
