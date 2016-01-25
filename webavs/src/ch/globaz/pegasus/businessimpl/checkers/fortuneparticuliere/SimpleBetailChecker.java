package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleBetail;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleBetailChecker extends PegasusAbstractChecker {
    /**
     * @param simpleBetail
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws BetailException
     */
    public static void checkForCreate(SimpleBetail simpleBetail) throws BetailException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleBetailChecker.checkMandatory(simpleBetail);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleBetailChecker.checkIntegrity(simpleBetail);
        }
    }

    /**
     * @param simpleBetail
     */
    public static void checkForDelete(SimpleBetail simpleBetail) {
    }

    /**
     * @param simpleBetail
     */
    public static void checkForUpdate(SimpleBetail simpleBetail) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleBetail
     * @throws BetailException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleBetail simpleBetail) throws BetailException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleBetail ait un type de propriete</li> <li>
     * Vérifie que le simpleBetail ait une part de propriete</li> <li>Vérifie que le simpleBetail ait un montant</li>
     * 
     * @param simpleBetail
     */
    private static void checkMandatory(SimpleBetail simpleBetail) {

        // Vérifie que le simpleBetail ait un type de propriete
        if (JadeStringUtil.isEmpty(simpleBetail.getCsTypePropriete())) {
            JadeThread.logError(simpleBetail.getClass().getName(), "pegasus.simpleBetail.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleBetail ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleBetail.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleBetail.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleBetail.getClass().getName(), "pegasus.simpleBetail.partPropriete.mandatory");
        }

        // Vérifie que le simpleBetail ait un montant
        if (JadeStringUtil.isBlankOrZero(simpleBetail.getMontant())) {
            JadeThread.logError(simpleBetail.getClass().getName(), "pegasus.simpleBetail.montant.mandatory");
        }

    }
}
