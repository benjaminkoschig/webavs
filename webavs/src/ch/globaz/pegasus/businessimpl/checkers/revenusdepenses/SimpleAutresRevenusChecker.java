package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAutresRevenus;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAutresRevenusChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAutresRevenus
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AutresRevenusException
     */
    public static void checkForCreate(SimpleAutresRevenus simpleAutresRevenus) throws AutresRevenusException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleAutresRevenusChecker.checkMandatory(simpleAutresRevenus);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAutresRevenusChecker.checkIntegrity(simpleAutresRevenus);
        }
    }

    /**
     * @param simpleAutresRevenus
     */
    public static void checkForDelete(SimpleAutresRevenus simpleAutresRevenus) {
    }

    /**
     * @param simpleAutresRevenus
     */
    public static void checkForUpdate(SimpleAutresRevenus simpleAutresRevenus) {
        SimpleAutresRevenusChecker.checkMandatory(simpleAutresRevenus);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAutresRevenus
     * @throws AutresRevenusException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAutresRevenus simpleAutresRevenus) throws AutresRevenusException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleAutresRevenus ait un libelle Vérifie que le simpleAutresRevenus ait un montant
     * 
     * @param simpleAutresRevenus
     */
    private static void checkMandatory(SimpleAutresRevenus simpleAutresRevenus) {

        // Vérifie que le simpleAutresRevenus ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleAutresRevenus.getLibelle())) {
            JadeThread.logError(simpleAutresRevenus.getClass().getName(),
                    "pegasus.simpleAutresRevenus.libelle.mandatory");
        }

        // Vérifie que le simpleAutresRevenus ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleAutresRevenus.getMontant())) {
            JadeThread.logError(simpleAutresRevenus.getClass().getName(),
                    "pegasus.simpleAutresRevenus.montant.mandatory");
        }
    }
}
