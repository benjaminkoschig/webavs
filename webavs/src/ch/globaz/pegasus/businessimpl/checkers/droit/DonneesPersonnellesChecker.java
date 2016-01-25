package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class DonneesPersonnellesChecker extends PegasusAbstractChecker {
    /**
     * @param donneesPersonnelles
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DonneesPersonnellesException
     */
    public static void checkForCreate(DonneesPersonnelles donneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        DonneesPersonnellesChecker.checkMandatory(donneesPersonnelles);
        if (!PegasusAbstractChecker.threadOnError()) {
            DonneesPersonnellesChecker.checkIntegrity(donneesPersonnelles);
        }
    }

    /**
     * @param donneesPersonnelles
     */
    public static void checkForDelete(DonneesPersonnelles donneesPersonnelles) {
    }

    /**
     * @param donneesPersonnelles
     */
    public static void checkForUpdate(DonneesPersonnelles donneesPersonnelles) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param donneesPersonnelles
     * @throws DonneesPersonnellesException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(DonneesPersonnelles donneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * @param donneesPersonnelles
     */
    private static void checkMandatory(DonneesPersonnelles donneesPersonnelles) {

    }
}
