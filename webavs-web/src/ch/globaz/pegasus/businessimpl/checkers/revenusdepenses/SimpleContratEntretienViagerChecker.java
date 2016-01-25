package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleContratEntretienViagerChecker extends PegasusAbstractChecker {
    /**
     * @param simpleContratEntretienViager
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws ContratEntretienViagerException
     */
    public static void checkForCreate(SimpleContratEntretienViager simpleContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleContratEntretienViagerChecker.checkMandatory(simpleContratEntretienViager);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleContratEntretienViagerChecker.checkIntegrity(simpleContratEntretienViager);
        }
    }

    /**
     * @param simpleContratEntretienViager
     */
    public static void checkForDelete(SimpleContratEntretienViager simpleContratEntretienViager) {
    }

    /**
     * @param simpleContratEntretienViager
     */
    public static void checkForUpdate(SimpleContratEntretienViager simpleContratEntretienViager) {
        SimpleContratEntretienViagerChecker.checkMandatory(simpleContratEntretienViager);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleContratEntretienViager
     * @throws ContratEntretienViagerException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleContratEntretienViager simpleContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleContratEntretienViager ait un montantContrat
     * 
     * @param simpleContratEntretienViager
     */
    private static void checkMandatory(SimpleContratEntretienViager simpleContratEntretienViager) {

        // Vérifie que le simpleContratEntretienViager ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleContratEntretienViager.getMontantContrat())) {
            JadeThread.logError(simpleContratEntretienViager.getClass().getName(),
                    "pegasus.simpleContratEntretienViager.montantContrat.mandatory");
        }

    }
}
