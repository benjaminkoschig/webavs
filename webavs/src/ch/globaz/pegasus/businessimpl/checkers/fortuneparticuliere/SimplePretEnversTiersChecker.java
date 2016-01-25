package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimplePretEnversTiersChecker extends PegasusAbstractChecker {
    /**
     * @param simplePretEnversTiers
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PretEnversTiersException
     */
    public static void checkForCreate(SimplePretEnversTiers simplePretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePretEnversTiersChecker.checkMandatory(simplePretEnversTiers);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePretEnversTiersChecker.checkIntegrity(simplePretEnversTiers);
        }
    }

    /**
     * @param simplePretEnversTiers
     */
    public static void checkForDelete(SimplePretEnversTiers simplePretEnversTiers) {
        // TODO implement checker
    }

    /**
     * @param simplePretEnversTiers
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PretEnversTiersException
     */
    public static void checkForUpdate(SimplePretEnversTiers simplePretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePretEnversTiersChecker.checkMandatory(simplePretEnversTiers);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePretEnversTiersChecker.checkIntegrity(simplePretEnversTiers);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simplePretEnversTiers
     * @throws PretEnversTiersException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePretEnversTiers simplePretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simplePretEnversTiers ait un type de propriete</li> <li>Vérifie que le simplePretEnversTiers
     * ait une part de propriete</li> <li>Vérifie que le simplePretEnversTiers ait un montant</li>
     * 
     * @param simplePretEnversTiers
     */
    private static void checkMandatory(SimplePretEnversTiers simplePretEnversTiers) {

        // Vérifie que le simplePretEnversTiers ait un type de propriete
        if (JadeStringUtil.isEmpty(simplePretEnversTiers.getCsTypePropriete())) {
            JadeThread.logError(simplePretEnversTiers.getClass().getName(),
                    "pegasus.simplePretEnversTiers.csTypePropriete.mandatory");
        }

        // Vérifie que le simplePretEnversTiers ait une part de propriété
        if (JadeStringUtil.isEmpty(simplePretEnversTiers.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simplePretEnversTiers.getPartProprieteDenominateur())) {
            JadeThread.logError(simplePretEnversTiers.getClass().getName(),
                    "pegasus.simplePretEnversTiers.partPropriete.mandatory");
        }

        // Vérifie que le simplePretEnversTiers ait une reference sur un
        // droitMembreFamille
        if (JadeStringUtil.isEmpty(simplePretEnversTiers.getMontantPret())) {
            JadeThread.logError(simplePretEnversTiers.getClass().getName(),
                    "pegasus.simplePretEnversTiers.montantPret.mandatory");
        }

    }
}
