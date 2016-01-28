package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleMarchandisesStock;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleMarchandisesStockChecker extends PegasusAbstractChecker {
    /**
     * @param simpleMarchandisesStock
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws MarchandisesStockException
     */
    public static void checkForCreate(SimpleMarchandisesStock simpleMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleMarchandisesStockChecker.checkMandatory(simpleMarchandisesStock);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleMarchandisesStockChecker.checkIntegrity(simpleMarchandisesStock);
        }
    }

    /**
     * @param simpleMarchandisesStock
     */
    public static void checkForDelete(SimpleMarchandisesStock simpleMarchandisesStock) {
    }

    /**
     * @param simpleMarchandisesStock
     */
    public static void checkForUpdate(SimpleMarchandisesStock simpleMarchandisesStock) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleMarchandisesStock
     * @throws MarchandisesStockException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleMarchandisesStock simpleMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleMarchandisesStock ait un type de propriete</li> <li>
     * Vérifie que le simpleMarchandisesStock ait une part de propriete</li> <li>
     * Vérifie que le simpleMarchandisesStock ait un montant</li>
     * 
     * @param simpleMarchandisesStock
     */
    private static void checkMandatory(SimpleMarchandisesStock simpleMarchandisesStock) {

        // Vérifie que le simpleMarchandisesStock ait un type de propriete
        if (JadeStringUtil.isEmpty(simpleMarchandisesStock.getCsTypePropriete())) {
            JadeThread.logError(simpleMarchandisesStock.getClass().getName(),
                    "pegasus.simpleMarchandisesStock.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleMarchandisesStock ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleMarchandisesStock.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleMarchandisesStock.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleMarchandisesStock.getClass().getName(),
                    "pegasus.simpleMarchandisesStock.partPropriete.mandatory");
        }

        // Vérifie que le simpleMarchandisesStock ait un montant
        if (JadeStringUtil.isBlankOrZero(simpleMarchandisesStock.getMontantStock())) {
            JadeThread.logError(simpleMarchandisesStock.getClass().getName(),
                    "pegasus.simpleMarchandisesStock.montantStock.mandatory");
        }

    }
}
