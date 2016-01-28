package ch.globaz.pegasus.businessimpl.checkers.habitat;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.constantes.IPCHabitat;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyer;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleLoyerChecker extends PegasusAbstractChecker {

    public static void checkForCreate(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        SimpleLoyerChecker.checkMandatory(simpleLoyer);
    }

    /**
     * @param simpleLoyer
     */
    public static void checkForDelete(SimpleLoyer simpleLoyer) {

    }

    /**
     * @param simpleLoyer
     * @throws JadePersistenceException
     * @throws LoyerException
     */
    public static void checkForUpdate(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException {
        SimpleLoyerChecker.checkMandatory(simpleLoyer);
        SimpleLoyerChecker.checkIntegrity(simpleLoyer);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleLoyer
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException {
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleLoyerChecker.checkIntegrityIDBaileur(simpleLoyer);
        }
    }

    private static void checkIntegrityIDBaileur(SimpleLoyer simpleLoyer) {
        // TODO
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * 
     * @param simpleLoyer
     */

    private static void checkMandatory(SimpleLoyer simpleLoyer) {
        if (JadeStringUtil.isEmpty(simpleLoyer.getCsTypeLoyer())) {
            JadeThread.logError(simpleLoyer.getClass().getName(), "pegasus.simpleLoyer.typeLoyer.mandatory");
        }

        if (JadeStringUtil.isIntegerEmpty(simpleLoyer.getNbPersonnes())) {
            JadeThread.logError(simpleLoyer.getClass().getName(), "pegasus.simpleLoyer.nombrepersonnes.mandatory");
        }

        // if (JadeStringUtil.isEmpty( JadeStringUtil.isBlankOrZero IPCHabitat

        if (simpleLoyer.getCsTypeLoyer().equalsIgnoreCase(IPCHabitat.CS_LOYER_NET_AVEC_CHARGE)) {

            if (JadeStringUtil.isBlank(simpleLoyer.getMontantCharges())) {
                JadeThread.logError(simpleLoyer.getClass().getName(), "pegasus.simpleLoyer.charges.mandatory");
            }

            if (JadeStringUtil.isBlank(simpleLoyer.getMontantLoyerNet())) {
                JadeThread.logError(simpleLoyer.getClass().getName(), "pegasus.simpleLoyer.montantloyer.mandatory");
            }

        }

        else if (simpleLoyer.getCsTypeLoyer().equalsIgnoreCase(IPCHabitat.CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES)
                || simpleLoyer.getCsTypeLoyer().equalsIgnoreCase(IPCHabitat.CS_LOYER_NET_SANS_CHARGE)
                || simpleLoyer.getCsTypeLoyer().equalsIgnoreCase(IPCHabitat.CS_LOYER_BRUT_CHARGES_COMPRISES)
                || simpleLoyer.getCsTypeLoyer().equalsIgnoreCase(IPCHabitat.CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE)) {

            if (JadeStringUtil.isBlank(simpleLoyer.getMontantLoyerNet())) {
                JadeThread.logError(simpleLoyer.getClass().getName(), "pegasus.simpleLoyer.montantloyer.mandatory");
            }

        }

        else if (simpleLoyer.getCsTypeLoyer().equalsIgnoreCase(IPCHabitat.CS_PENSION_NON_RECONNUE)) {

            if (JadeStringUtil.isBlank(simpleLoyer.getPensionNonReconnue())) {
                JadeThread.logError(simpleLoyer.getClass().getName(),
                        "pegasus.simpleLoyer.pensionNonReconnue.mandatory");
            }

            if (JadeStringUtil.isBlank(simpleLoyer.getTaxeJournalierePensionNonReconnue())) {
                JadeThread.logError(simpleLoyer.getClass().getName(),
                        "pegasus.simpleLoyer.TaxeJournalierePensionNonReconnue.mandatory");
            }

        }

    }
}
