package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleCompteBancaireCCPChecker extends PegasusAbstractChecker {
    /**
     * @param simpleCompteBancaireCCP
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws CompteBancaireCCPException
     */
    public static void checkForCreate(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleCompteBancaireCCPChecker.checkMandatory(simpleCompteBancaireCCP);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleCompteBancaireCCPChecker.checkIntegrity(simpleCompteBancaireCCP);
        }
    }

    /**
     * @param simpleCompteBancaireCCP
     */
    public static void checkForDelete(SimpleCompteBancaireCCP simpleCompteBancaireCCP) {
    }

    /**
     * @param simpleCompteBancaireCCP
     */
    public static void checkForUpdate(SimpleCompteBancaireCCP simpleCompteBancaireCCP) {
        SimpleCompteBancaireCCPChecker.checkMandatory(simpleCompteBancaireCCP);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleCompteBancaireCCP
     * @throws CompteBancaireCCPException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleCompteBancaireCCP ait un csTypePropriete Vérifie que le simpleCompteBancaireCCP ait un iban
     * Vérifie que le simpleCompteBancaireCCP ait un montant
     * 
     * @param simpleCompteBancaireCCP
     */
    private static void checkMandatory(SimpleCompteBancaireCCP simpleCompteBancaireCCP) {

        // Vérifie que le simpleCompteBancaireCCP ait un type ppt
        if (JadeStringUtil.isEmpty(simpleCompteBancaireCCP.getCsTypePropriete())) {
            JadeThread.logError(simpleCompteBancaireCCP.getClass().getName(),
                    "pegasus.simpleCompteBancaireCCP.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleCompteBancaireCCP ait un iban
        if (JadeStringUtil.isEmpty(simpleCompteBancaireCCP.getIban())) {
            JadeThread.logError(simpleCompteBancaireCCP.getClass().getName(),
                    "pegasus.simpleCompteBancaireCCP.iban.mandatory");
        }

        // Vérifie que le simpleCompteBancaireCCP ait un montant
        if (JadeStringUtil.isEmpty(simpleCompteBancaireCCP.getMontant())) {
            JadeThread.logError(simpleCompteBancaireCCP.getClass().getName(),
                    "pegasus.simpleCompteBancaireCCP.montant.mandatory");
        }

        // Vérifie que le simpleCompteBancaireCCP ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleCompteBancaireCCP.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleCompteBancaireCCP.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleCompteBancaireCCP.getClass().getName(),
                    "pegasus.simpleCompteBancaireCCP.partPropriete.mandatory");
        }

        // Vérifie que le simpleCompteBancaireCCP ait un type compte
        /*
         * if (JadeStringUtil.isEmpty(simpleCompteBancaireCCP.getCsTypeCompte())) {
         * JadeThread.logError(simpleCompteBancaireCCP.getClass().getName(),
         * "pegasus.simpleCompteBancaireCCP.csTypeCompte.mandatory"); }
         */

        // Vérifie que l'iban ai un max de 34 char
        /*
         * if (simpleCompteBancaireCCP.getIban().length() <= 34) {
         * JadeThread.logError(simpleCompteBancaireCCP.getClass().getName(),
         * "pegasus.simpleCompteBancaireCCP.iban.size"); }
         */
    }
}
