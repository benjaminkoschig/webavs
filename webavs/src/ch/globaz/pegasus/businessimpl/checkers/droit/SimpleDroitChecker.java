package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleDroitChecker extends PegasusAbstractChecker {
    /**
     * @param simpleDroit
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public static void checkForCreate(SimpleDroit simpleDroit) throws DroitException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleDroitChecker.checkMandatory(simpleDroit);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDroitChecker.checkIntegrity(simpleDroit);
        }
    }

    /**
     * @param simpleDroit
     */
    public static void checkForDelete(SimpleDroit simpleDroit) {
    }

    /**
     * @param simpleDroit
     */
    public static void checkForUpdate(SimpleDroit simpleDroit) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleDroit
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDroit simpleDroit) throws DroitException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * @param simpleDroit
     */
    private static void checkMandatory(SimpleDroit simpleDroit) {

    }
}
