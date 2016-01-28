package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class DroitMembreFamilleChecker extends PegasusAbstractChecker {
    /**
     * @param droitMembreFamille
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public static void checkForCreate(DroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        DroitMembreFamilleChecker.checkMandatory(droitMembreFamille);
        if (!PegasusAbstractChecker.threadOnError()) {
            DroitMembreFamilleChecker.checkIntegrity(droitMembreFamille);
        }
    }

    /**
     * @param droitMembreFamille
     */
    public static void checkForDelete(DroitMembreFamille droitMembreFamille) {
    }

    /**
     * @param droitMembreFamille
     */
    public static void checkForUpdate(DroitMembreFamille droitMembreFamille) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param droitMembreFamille
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(DroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * @param droitMembreFamille
     */
    private static void checkMandatory(DroitMembreFamille droitMembreFamille) {
    }
}
