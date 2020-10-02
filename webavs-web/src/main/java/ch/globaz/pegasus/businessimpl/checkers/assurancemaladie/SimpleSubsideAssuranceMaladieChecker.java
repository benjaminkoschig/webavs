package ch.globaz.pegasus.businessimpl.checkers.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SimpleSubsideAssuranceMaladie;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;

public class SimpleSubsideAssuranceMaladieChecker extends PegasusAbstractChecker {
    /**
     * @param simpleSubsideAssuranceMaladie
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws FraisGardeException
     */
    public static void checkForCreate(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws SubsideAssuranceMaladieException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleSubsideAssuranceMaladieChecker.checkMandatory(simpleSubsideAssuranceMaladie);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleSubsideAssuranceMaladieChecker.checkIntegrity(simpleSubsideAssuranceMaladie);
        }
    }

    /**
     * @param simpleSubsideAssuranceMaladie
     */
    public static void checkForDelete(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) {
    }

    /**
     * @param simpleSubsideAssuranceMaladie
     */
    public static void checkForUpdate(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) {
        SimpleSubsideAssuranceMaladieChecker.checkMandatory(simpleSubsideAssuranceMaladie);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleSubsideAssuranceMaladie
     * @throws FraisGardeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    @SuppressWarnings({"java:S1172","java:S1130"})
    private static void checkIntegrity(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws SubsideAssuranceMaladieException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleSubsideAssuranceMaladie ait un libelle Vérifie que le simpleSubsideAssuranceMaladie ait un montant
     * 
     * @param simpleSubsideAssuranceMaladie
     */
    private static void checkMandatory(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) {
        // Vérifie que le simpleSubsideAssuranceMaladie ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleSubsideAssuranceMaladie.getMontant())) {
            JadeThread.logError(simpleSubsideAssuranceMaladie.getClass().getName(),
                    "pegasus.simpleSubsideAssuranceMaladie.montant.mandatory");
        }
    }
}
