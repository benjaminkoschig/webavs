package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleTypeFraisObtentionRevenuChecker extends PegasusAbstractChecker {
    /**
     * @param simpleTypeFraisObtentionRevenu
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws SimpleTypeFraisObtentionRevenuException
     */
    public static void checkForCreate(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleTypeFraisObtentionRevenuChecker.checkMandatory(simpleTypeFraisObtentionRevenu);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleTypeFraisObtentionRevenuChecker.checkIntegrity(simpleTypeFraisObtentionRevenu);
        }
    }

    /**
     * @param simpleTypeFraisObtentionRevenu
     */
    public static void checkForDelete(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu) {
    }

    /**
     * @param simpleTypeFraisObtentionRevenu
     */
    public static void checkForUpdate(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleTypeFraisObtentionRevenu
     * @throws SimpleTypeFraisObtentionRevenuException
     * @throws SimpleTypeFraisObtentionRevenuException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires
     * 
     * @param simpleTypeFraisObtentionRevenu
     */
    private static void checkMandatory(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu) {

    }
}
