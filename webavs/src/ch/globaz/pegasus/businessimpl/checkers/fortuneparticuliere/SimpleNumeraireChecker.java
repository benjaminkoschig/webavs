package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleNumeraire;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleNumeraireChecker extends PegasusAbstractChecker {
    /**
     * @param simpleNumeraire
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws NumeraireException
     */
    public static void checkForCreate(SimpleNumeraire simpleNumeraire) throws NumeraireException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleNumeraireChecker.checkMandatory(simpleNumeraire);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleNumeraireChecker.checkIntegrity(simpleNumeraire);
        }
    }

    /**
     * @param simpleNumeraire
     */
    public static void checkForDelete(SimpleNumeraire simpleNumeraire) {
        // TODO implement checker
    }

    /**
     * @param simpleNumeraire
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws NumeraireException
     */
    public static void checkForUpdate(SimpleNumeraire simpleNumeraire) throws NumeraireException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleNumeraireChecker.checkMandatory(simpleNumeraire);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleNumeraireChecker.checkIntegrity(simpleNumeraire);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleNumeraire
     * @throws NumeraireException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleNumeraire simpleNumeraire) throws NumeraireException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleNumeraire ait un type de propriete</li> <li>
     * Vérifie que le simpleNumeraire ait une part de propriete</li> <li>Vérifie que le simpleNumeraire ait un montant</li>
     * 
     * @param simpleNumeraire
     */
    private static void checkMandatory(SimpleNumeraire simpleNumeraire) {

        // Vérifie que le simpleNumeraire ait un type de propriete
        if (JadeStringUtil.isEmpty(simpleNumeraire.getCsTypePropriete())) {
            JadeThread.logError(simpleNumeraire.getClass().getName(),
                    "pegasus.simpleNumeraire.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleNumeraire ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleNumeraire.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleNumeraire.getPartProprieteDenominateur())) {
            JadeThread
                    .logError(simpleNumeraire.getClass().getName(), "pegasus.simpleNumeraire.partPropriete.mandatory");
        }

        // Vérifie que le simpleNumeraire ait une reference sur un
        // droitMembreFamille
        if (JadeStringUtil.isEmpty(simpleNumeraire.getMontant())) {
            JadeThread.logError(simpleNumeraire.getClass().getName(), "pegasus.simpleNumeraire.montant.mandatory");
        }

        // if (JadeStringUtil.isEmpty(simpleNumeraire.getMontantInteret()) && !simpleNumeraire.getIsSansInteret()) {
        // JadeThread.logError(simpleNumeraire.getClass().getName(),
        // "pegasus.simpleNumeraire.montantinteret.mandatory");
        // }
    }
}
