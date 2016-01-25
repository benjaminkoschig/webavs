package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleVehicule;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleVehiculeChecker extends PegasusAbstractChecker {
    /**
     * @param simpleVehicule
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws VehiculeException
     */
    public static void checkForCreate(SimpleVehicule simpleVehicule) throws VehiculeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleVehiculeChecker.checkMandatory(simpleVehicule);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleVehiculeChecker.checkIntegrity(simpleVehicule);
        }
    }

    /**
     * @param simpleVehicule
     */
    public static void checkForDelete(SimpleVehicule simpleVehicule) {
    }

    /**
     * @param simpleVehicule
     */
    public static void checkForUpdate(SimpleVehicule simpleVehicule) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleVehicule
     * @throws VehiculeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleVehicule simpleVehicule) throws VehiculeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleVehicule ait un type de propriete</li> <li>
     * Vérifie que le simpleVehicule ait une part de propriete</li> <li>Vérifie que le simpleVehicule ait un montant</li>
     * 
     * @param simpleVehicule
     */
    private static void checkMandatory(SimpleVehicule simpleVehicule) {

        // Vérifie que le simpleVehicule ait un type de propriete
        if (JadeStringUtil.isEmpty(simpleVehicule.getCsTypePropriete())) {
            JadeThread
                    .logError(simpleVehicule.getClass().getName(), "pegasus.simpleVehicule.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleVehicule ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleVehicule.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleVehicule.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleVehicule.getClass().getName(), "pegasus.simpleVehicule.partPropriete.mandatory");
        }

        // Vérifie que le simpleVehicule ait un montant
        if (JadeStringUtil.isBlankOrZero(simpleVehicule.getMontant())) {
            JadeThread.logError(simpleVehicule.getClass().getName(), "pegasus.simpleVehicule.montant.mandatory");
        }
    }
}
