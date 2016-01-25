package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuvees;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAutresDettesProuveesChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAutresDettesProuvees
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AutresDettesProuveesException
     */
    public static void checkForCreate(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleAutresDettesProuveesChecker.checkMandatory(simpleAutresDettesProuvees);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAutresDettesProuveesChecker.checkIntegrity(simpleAutresDettesProuvees);
        }
    }

    /**
     * @param simpleAutresDettesProuvees
     */
    public static void checkForDelete(SimpleAutresDettesProuvees simpleAutresDettesProuvees) {
    }

    /**
     * @param simpleAutresDettesProuvees
     */
    public static void checkForUpdate(SimpleAutresDettesProuvees simpleAutresDettesProuvees) {
        SimpleAutresDettesProuveesChecker.checkMandatory(simpleAutresDettesProuvees);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAutresDettesProuvees
     * @throws AutresDettesProuveesException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleAutresDettesProuvees ait un montant</li> <li>
     * 
     * @param simpleAutresDettesProuvees
     */
    private static void checkMandatory(SimpleAutresDettesProuvees simpleAutresDettesProuvees) {

        // Vérifie que le simpleAutresDettesProuvees ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleAutresDettesProuvees.getMontantDette())) {
            JadeThread.logError(simpleAutresDettesProuvees.getClass().getName(),
                    "pegasus.simpleAutresDettesProuvees.montantDette.mandatory");
        }

    }
}
