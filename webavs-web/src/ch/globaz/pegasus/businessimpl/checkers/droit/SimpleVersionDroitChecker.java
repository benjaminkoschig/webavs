package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleVersionDroitChecker extends PegasusAbstractChecker {
    /**
     * @param simpleVersionDroit
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public static void checkForCreate(SimpleVersionDroit simpleVersionDroit) throws DroitException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleVersionDroitChecker.checkMandatory(simpleVersionDroit);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleVersionDroitChecker.checkIntegrity(simpleVersionDroit);
        }
    }

    /**
     * @param simpleVersionDroit
     */
    public static void checkForDelete(SimpleVersionDroit simpleVersionDroit) {
    }

    /**
     * @param simpleVersionDroit
     */
    public static void checkForUpdate(SimpleVersionDroit simpleVersionDroit) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleVersionDroit
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleVersionDroit simpleVersionDroit) throws DroitException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * @param simpleVersionDroit
     */
    private static void checkMandatory(SimpleVersionDroit simpleVersionDroit) {
        if (JadeStringUtil.isEmpty(simpleVersionDroit.getDateAnnonce())) {
            JadeThread
                    .logError(simpleVersionDroit.getClass().getName(), "pegasus.droit.corriger.dateAnnonce.mandatory");
        }
    }
}
