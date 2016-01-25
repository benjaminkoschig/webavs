package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleRevenuActiviteLucrativeDependanteChecker extends PegasusAbstractChecker {
    /**
     * @param simpleRevenuActiviteLucrativeDependante
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeDependanteException
     */
    public static void checkForCreate(SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleRevenuActiviteLucrativeDependanteChecker.checkMandatory(simpleRevenuActiviteLucrativeDependante);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRevenuActiviteLucrativeDependanteChecker.checkIntegrity(simpleRevenuActiviteLucrativeDependante);
        }
    }

    /**
     * @param simpleRevenuActiviteLucrativeDependante
     */
    public static void checkForDelete(SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante) {
    }

    /**
     * @param simpleRevenuActiviteLucrativeDependante
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeDependanteException
     */
    public static void checkForUpdate(SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleRevenuActiviteLucrativeDependanteChecker.checkMandatory(simpleRevenuActiviteLucrativeDependante);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRevenuActiviteLucrativeDependanteChecker.checkIntegrity(simpleRevenuActiviteLucrativeDependante);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleRevenuActiviteLucrativeDependante
     * @throws RevenuActiviteLucrativeDependanteException
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeDependanteException
     */
    private static void checkIntegrity(SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleRevenuActiviteLucrativeDependante ait un idEmployeur Vérifie que le
     * simpleRevenuActiviteLucrativeDependante ait un csGenreRevenu Vérifie que le
     * simpleRevenuActiviteLucrativeDependante ait un montantActiviteLucrative
     * 
     * @param simpleRevenuActiviteLucrativeIndependante
     */
    private static void checkMandatory(SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante) {

        // Vérifie que le simpleRevenuActiviteLucrativeDependante ait un
        // idEmployeur
        // if (JadeStringUtil.isEmpty(simpleRevenuActiviteLucrativeDependante.getIdEmployeur())) {
        // JadeThread.logError(simpleRevenuActiviteLucrativeDependante.getClass().getName(),
        // "pegasus.simpleRevenuActiviteLucrativeDependante.idEmployeur.mandatory");
        // }

        // Vérifie que le simpleRevenuActiviteLucrativeDependante ait un
        // csGenreRevenu
        if (JadeStringUtil.isEmpty(simpleRevenuActiviteLucrativeDependante.getCsGenreRevenu())) {
            JadeThread.logError(simpleRevenuActiviteLucrativeDependante.getClass().getName(),
                    "pegasus.simpleRevenuActiviteLucrativeDependante.csGenreRevenu.mandatory");
        }

        // Vérifie que le simpleRevenuActiviteLucrativeDependante ait un
        // montantActiviteLucrative
        if (JadeStringUtil.isBlank(simpleRevenuActiviteLucrativeDependante.getMontantActiviteLucrative())) {
            JadeThread.logError(simpleRevenuActiviteLucrativeDependante.getClass().getName(),
                    "pegasus.simpleRevenuActiviteLucrativeDependante.montantActiviteLucrative.mandatory");
        }

        if ((simpleRevenuActiviteLucrativeDependante.getCsFraisObtentionRevenu() != null)
                && simpleRevenuActiviteLucrativeDependante.getCsFraisObtentionRevenu().equals("64035005")
                && JadeStringUtil.isBlankOrZero(simpleRevenuActiviteLucrativeDependante.getAutreFraisObtentionRevenu())) {
            JadeThread.logError(simpleRevenuActiviteLucrativeDependante.getClass().getName(),
                    "pegasus.simpleRevenuActiviteLucrativeDependante.autresFrais.mandatory");
        }

    }
}
