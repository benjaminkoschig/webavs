package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleFraisGarde;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;

public class SimpleFraisGardeChecker extends PegasusAbstractChecker {
    /**
     * @param simpleFraisGarde
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws FraisGardeException
     */
    public static void checkForCreate(SimpleFraisGarde simpleFraisGarde) throws FraisGardeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleFraisGardeChecker.checkMandatory(simpleFraisGarde);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleFraisGardeChecker.checkIntegrity(simpleFraisGarde);
        }
    }

    /**
     * @param simpleFraisGarde
     */
    public static void checkForDelete(SimpleFraisGarde simpleFraisGarde) {
    }

    /**
     * @param simpleFraisGarde
     */
    public static void checkForUpdate(SimpleFraisGarde simpleFraisGarde) {
        SimpleFraisGardeChecker.checkMandatory(simpleFraisGarde);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleFraisGarde
     * @throws FraisGardeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    @SuppressWarnings({"java:S1172","java:S1130"})
    private static void checkIntegrity(SimpleFraisGarde simpleFraisGarde) throws FraisGardeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleFraisGarde ait un libelle Vérifie que le simpleFraisGarde ait un montant
     * 
     * @param simpleFraisGarde
     */
    private static void checkMandatory(SimpleFraisGarde simpleFraisGarde) {

        // Vérifie que le simpleFraisGarde ait un montant mensuel
//        if (JadeStringUtil.isEmpty(simpleFraisGarde.getLibelle())) {
//            JadeThread.logError(simpleFraisGarde.getClass().getName(),
//                    "pegasus.simpleFraisGarde.libelle.mandatory");
//        }

        // Vérifie que le simpleFraisGarde ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleFraisGarde.getMontant())) {
            JadeThread.logError(simpleFraisGarde.getClass().getName(),
                    "pegasus.simpleFraisGarde.montant.mandatory");
        }
    }
}
