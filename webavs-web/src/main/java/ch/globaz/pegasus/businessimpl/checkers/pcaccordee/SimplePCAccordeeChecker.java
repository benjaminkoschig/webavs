package ch.globaz.pegasus.businessimpl.checkers.pcaccordee;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimplePCAccordeeChecker extends PegasusAbstractChecker {
    /**
     * @param simplePCAccordee
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public static void checkForCreate(SimplePCAccordee simplePCAccordee) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePCAccordeeChecker.checkMandatory(simplePCAccordee);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePCAccordeeChecker.checkIntegrity(simplePCAccordee);
        }
    }

    /**
     * @param simplePCAccordee
     */
    public static void checkForDelete(SimplePCAccordee simplePCAccordee) {
    }

    /**
     * @param simplePCAccordee
     */
    public static void checkForUpdate(SimplePCAccordee simplePCAccordee) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simplePCAccordee
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePCAccordee simplePCAccordee) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * V�rifie que le simplePCAccordee ait un idPlanDeCalcul. V�rifie que le simplePCAccordee ait un csKeyPCAccordee.
     * 
     * @param simplePCAccordee
     */
    private static void checkMandatory(SimplePCAccordee simplePCAccordee) {

    }
}
