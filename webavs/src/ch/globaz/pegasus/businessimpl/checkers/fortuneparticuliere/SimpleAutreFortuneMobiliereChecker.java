package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutreFortuneMobiliere;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliere;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleAutreFortuneMobiliereChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAutreFortuneMobiliere
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AutreFortuneMobiliereException
     */
    public static void checkForCreate(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleAutreFortuneMobiliereChecker.checkMandatory(simpleAutreFortuneMobiliere);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAutreFortuneMobiliereChecker.checkIntegrity(simpleAutreFortuneMobiliere);
        }
    }

    /**
     * @param simpleAutreFortuneMobiliere
     */
    public static void checkForDelete(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere) {
    }

    /**
     * @param simpleAutreFortuneMobiliere
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AutreFortuneMobiliereException
     */
    public static void checkForUpdate(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleAutreFortuneMobiliereChecker.checkMandatory(simpleAutreFortuneMobiliere);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAutreFortuneMobiliereChecker.checkIntegrity(simpleAutreFortuneMobiliere);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAutreFortuneMobiliere
     * @throws AutreFortuneMobiliereException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleAutreFortuneMobiliere ait un type de propriete</li> <li>Vérifie que le
     * simpleAutreFortuneMobiliere ait une part de propriete</li> <li>Vérifie que le simpleAutreFortuneMobiliere ait un
     * montant</li><li>
     * Vérifie que le simpleAutreFortuneMobiliere ait un type de fortune/li>
     * 
     * @param simpleAutreFortuneMobiliere
     */
    private static void checkMandatory(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere) {

        // Vérifie que le simpleAutreFortuneMobiliere ait un type de propriete
        if (JadeStringUtil.isEmpty(simpleAutreFortuneMobiliere.getCsTypePropriete())) {
            JadeThread.logError(simpleAutreFortuneMobiliere.getClass().getName(),
                    "pegasus.simpleAutreFortuneMobiliere.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleAutreFortuneMobiliere ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleAutreFortuneMobiliere.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleAutreFortuneMobiliere.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleAutreFortuneMobiliere.getClass().getName(),
                    "pegasus.simpleAutreFortuneMobiliere.partPropriete.mandatory");
        }

        // Vérifie que le simpleAutreFortuneMobiliere ait un type de fortune
        if (JadeStringUtil.isEmpty(simpleAutreFortuneMobiliere.getCsTypeFortune())) {
            JadeThread.logError(simpleAutreFortuneMobiliere.getClass().getName(),
                    "pegasus.simpleAutreFortuneMobiliere.csTypeFortune.mandatory");
        }

        // Vérifie que le simpleVehicule ait un montant
        if (JadeStringUtil.isBlankOrZero(simpleAutreFortuneMobiliere.getMontant())) {
            JadeThread.logError(simpleAutreFortuneMobiliere.getClass().getName(),
                    "pegasus.simpleAutreFortuneMobiliere.montant.mandatory");
        }

        // Vérifie que le simpleAutreFortuneMobiliere ait un type de fortune
        if (IPCAutreFortuneMobiliere.CS_TYPE_FORTUNE_AUTRES.equals(simpleAutreFortuneMobiliere.getCsTypeFortune())
                && JadeStringUtil.isEmpty(simpleAutreFortuneMobiliere.getAutre())) {
            JadeThread.logError(simpleAutreFortuneMobiliere.getClass().getName(),
                    "pegasus.simpleAutreFortuneMobiliere.autre.mandatory");
        }

    }
}
