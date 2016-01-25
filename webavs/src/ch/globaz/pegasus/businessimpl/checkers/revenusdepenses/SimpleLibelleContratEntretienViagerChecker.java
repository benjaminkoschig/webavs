package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleLibelleContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleLibelleContratEntretienViagerChecker extends PegasusAbstractChecker {
    /**
     * @param simpleLibelleContratEntretienViager
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws SimpleLibelleContratEntretienViagerException
     */
    public static void checkForCreate(SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleLibelleContratEntretienViagerChecker.checkMandatory(simpleLibelleContratEntretienViager);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleLibelleContratEntretienViagerChecker.checkIntegrity(simpleLibelleContratEntretienViager);
        }
    }

    /**
     * @param simpleLibelleContratEntretienViager
     */
    public static void checkForDelete(SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) {
    }

    /**
     * @param simpleLibelleContratEntretienViager
     */
    public static void checkForUpdate(SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleLibelleContratEntretienViager
     * @throws SimpleLibelleContratEntretienViagerException
     * @throws SimpleLibelleContratEntretienViagerException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires
     * 
     * @param simpleLibelleContratEntretienViager
     */
    private static void checkMandatory(SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) {

    }
}
