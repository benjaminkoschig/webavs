package ch.globaz.pegasus.businessimpl.checkers.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleFraisGarde;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;

public class SimplePrimeAssuranceMaladieChecker extends PegasusAbstractChecker {
    /**
     * @param simplePrimeAssuranceMaladie
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws FraisGardeException
     */
    public static void checkForCreate(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws PrimeAssuranceMaladieException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePrimeAssuranceMaladieChecker.checkMandatory(simplePrimeAssuranceMaladie);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePrimeAssuranceMaladieChecker.checkIntegrity(simplePrimeAssuranceMaladie);
        }
    }

    /**
     * @param simplePrimeAssuranceMaladie
     */
    public static void checkForDelete(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) {
    }

    /**
     * @param simplePrimeAssuranceMaladie
     */
    public static void checkForUpdate(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) {
        SimplePrimeAssuranceMaladieChecker.checkMandatory(simplePrimeAssuranceMaladie);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simplePrimeAssuranceMaladie
     * @throws FraisGardeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    @SuppressWarnings({"java:S1172","java:S1130"})
    private static void checkIntegrity(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws PrimeAssuranceMaladieException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simplePrimeAssuranceMaladie ait un libelle Vérifie que le simplePrimeAssuranceMaladie ait un montant
     * 
     * @param simplePrimeAssuranceMaladie
     */
    private static void checkMandatory(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) {

        // Vérifie que le simpleFraisGarde ait un montant mensuel
//        if (JadeStringUtil.isEmpty(simpleFraisGarde.getLibelle())) {
//            JadeThread.logError(simpleFraisGarde.getClass().getName(),
//                    "pegasus.simpleFraisGarde.libelle.mandatory");
//        }

        // Vérifie que le simplePrimeAssuranceMaladie ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simplePrimeAssuranceMaladie.getMontant())) {
            JadeThread.logError(simplePrimeAssuranceMaladie.getClass().getName(),
                    "pegasus.simplePrimeAssuranceMaladie.montant.mandatory");
        }
    }
}
