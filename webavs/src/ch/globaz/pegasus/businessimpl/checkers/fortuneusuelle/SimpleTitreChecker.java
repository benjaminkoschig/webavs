package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleTitreChecker extends PegasusAbstractChecker {
    /**
     * @param simpleTitre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws TitreException
     */
    public static void checkForCreate(SimpleTitre simpleTitre) throws TitreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleTitreChecker.checkMandatory(simpleTitre);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleTitreChecker.checkIntegrity(simpleTitre);
        }
    }

    /**
     * @param simpleTitre
     */
    public static void checkForDelete(SimpleTitre simpleTitre) {
    }

    /**
     * @param simpleTitre
     */
    public static void checkForUpdate(SimpleTitre simpleTitre) {
        SimpleTitreChecker.checkMandatory(simpleTitre);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleTitre
     * @throws TitreException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleTitre simpleTitre) throws TitreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleTitre ait un type</li> <li>
     * Vérifie que le simpleTitre ait un genre</li>
     * 
     * @param simpleTitre
     */
    private static void checkMandatory(SimpleTitre simpleTitre) {

        // Vérifie que le simpleTitre ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleTitre.getCsTypePropriete())) {
            JadeThread.logError(simpleTitre.getClass().getName(), "pegasus.simpleTitre.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleTitre ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleTitre.getCsGenreTitre())) {
            JadeThread.logError(simpleTitre.getClass().getName(), "pegasus.simpleTitre.csGenreTitre.mandatory");
        }

        // La part de propriete est obligatoire
        if (JadeStringUtil.isBlankOrZero(simpleTitre.getPartProprieteNumerateur())
                || JadeStringUtil.isBlankOrZero(simpleTitre.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleTitre.getClass().getName(), "pegasus.simpleTitre.partPropriete.mandatory");
        }
    }
}
