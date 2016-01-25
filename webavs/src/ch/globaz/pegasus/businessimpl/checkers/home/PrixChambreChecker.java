package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class PrixChambreChecker extends PegasusAbstractChecker {
    /**
     * @param prixChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws TypeChambreException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(PrixChambre prixChambre) throws PrixChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        PrixChambreChecker.checkMandatory(prixChambre);
        if (!PegasusAbstractChecker.threadOnError()) {
            PrixChambreChecker.checkIntegrity(prixChambre);
        }
    }

    /**
     * @param prixChambre
     */
    public static void checkForDelete(PrixChambre prixChambre) {
    }

    /**
     * @param prixChambre
     */
    public static void checkForUpdate(PrixChambre prixChambre) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param prixChambre
     * @throws PrixChambreException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(PrixChambre prixChambre) throws PrixChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * @param prixChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PrixChambreException
     */
    private static void checkMandatory(PrixChambre prixChambre) throws PrixChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
    }
}
