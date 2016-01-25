package ch.globaz.pegasus.businessimpl.checkers.pcaccordee;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleJoursAppointChecker extends PegasusAbstractChecker {
    /**
     * @param simpleJoursAppoint
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public static void checkForCreate(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleJoursAppointChecker.checkMandatory(simpleJoursAppoint);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleJoursAppointChecker.checkIntegrity(simpleJoursAppoint);
        }
    }

    /**
     * @param simpleJoursAppoint
     */
    public static void checkForDelete(SimpleJoursAppoint simpleJoursAppoint) {
    }

    /**
     * @param simpleJoursAppoint
     */
    public static void checkForUpdate(SimpleJoursAppoint simpleJoursAppoint) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleJoursAppoint
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleJoursAppoint ait un idPlanDeCalcul. Vérifie que le simpleJoursAppoint ait un
     * csKeyPCAccordee.
     * 
     * @param simpleJoursAppoint
     */
    private static void checkMandatory(SimpleJoursAppoint simpleJoursAppoint) {

    }
}
