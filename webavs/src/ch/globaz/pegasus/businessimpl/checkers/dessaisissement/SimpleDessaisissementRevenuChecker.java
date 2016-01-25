package ch.globaz.pegasus.businessimpl.checkers.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenu;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleDessaisissementRevenuChecker extends PegasusAbstractChecker {
    /**
     * @param simpleDessaisissementRevenu
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DessaisissementRevenuException
     */
    public static void checkForCreate(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws DessaisissementRevenuException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleDessaisissementRevenuChecker.checkMandatory(simpleDessaisissementRevenu);

    }

    /**
     * @param simpleDessaisissementRevenu
     */
    public static void checkForDelete(SimpleDessaisissementRevenu simpleDessaisissementRevenu) {
    }

    /**
     * @param simpleDessaisissementRevenu
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DessaisissementRevenuException
     */
    public static void checkForUpdate(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws DessaisissementRevenuException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleDessaisissementRevenuChecker.checkMandatory(simpleDessaisissementRevenu);

    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleDessaisissementRevenu
     * @throws DessaisissementRevenuException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws DessaisissementRevenuException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // TODO implementer checker
    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleDessaisissementRevenu ait un type de propriete</li> <li>
     * Vérifie que le simpleDessaisissementRevenu ait une part de propriete</li> <li>Vérifie que le
     * simpleDessaisissementRevenu ait un montant</li>
     * 
     * @param simpleDessaisissementRevenu
     */
    private static void checkMandatory(SimpleDessaisissementRevenu simpleDessaisissementRevenu) {

        if (JadeStringUtil.isEmpty(simpleDessaisissementRevenu.getLibelleDessaisissement())) {
            JadeThread.logError(simpleDessaisissementRevenu.getClass().getName(),
                    "pegasus.simpledessaisissementRevenu.motifdessaisissement.mandatory");
        }

        if (JadeStringUtil.isEmpty(simpleDessaisissementRevenu.getMontantBrut())) {
            JadeThread.logError(simpleDessaisissementRevenu.getClass().getName(),
                    "pegasus.simpledessaisissementRevenu.montantbrutdessaisi.mandatory");
        }

    }
}
