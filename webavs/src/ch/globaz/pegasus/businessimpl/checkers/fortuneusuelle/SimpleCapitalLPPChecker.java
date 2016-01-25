package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPP;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleCapitalLPPChecker extends PegasusAbstractChecker {
    /**
     * @param simpleCapitalLPP
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws CapitalLPPException
     */
    public static void checkForCreate(SimpleCapitalLPP simpleCapitalLPP) throws CapitalLPPException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleCapitalLPPChecker.checkMandatory(simpleCapitalLPP);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleCapitalLPPChecker.checkIntegrity(simpleCapitalLPP);
        }
    }

    /**
     * @param simpleCapitalLPP
     */
    public static void checkForDelete(SimpleCapitalLPP simpleCapitalLPP) {
    }

    /**
     * @param simpleCapitalLPP
     */
    public static void checkForUpdate(SimpleCapitalLPP simpleCapitalLPP) {
        SimpleCapitalLPPChecker.checkMandatory(simpleCapitalLPP);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleCapitalLPP
     * @throws CapitalLPPException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleCapitalLPP simpleCapitalLPP) throws CapitalLPPException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleCapitalLPP ait un csTypePropriete Vérifie que le simpleCapitalLPP ait un montantCapitalLPP
     * Vérifie que le simpleCapitalLPP ait un noPoliceNoCompte Vérifie que le simpleCapitalLPP ait un
     * idInstitutionPrevoyance
     * 
     * @param simpleCapitalLPP
     */
    private static void checkMandatory(SimpleCapitalLPP simpleCapitalLPP) {

        // Vérifie que le simpleCapitalLPP ait un csTypePropriete

        if (JadeStringUtil.isEmpty(simpleCapitalLPP.getCsTypePropriete())) {
            JadeThread.logError(simpleCapitalLPP.getClass().getName(),
                    "pegasus.simpleCapitalLPP.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleCapitalLPP ait un montantCapitalLPP if
        if (JadeStringUtil.isEmpty(simpleCapitalLPP.getMontantCapitalLPP())) {
            JadeThread.logError(simpleCapitalLPP.getClass().getName(),
                    "pegasus.simpleCapitalLPP.montantCapitalLPP.mandatory");
        }

        // Vérifie que le simpleCapitalLPP ait un noPoliceNoCompte if
        if (JadeStringUtil.isEmpty(simpleCapitalLPP.getNoPoliceNoCompte())) {
            JadeThread.logError(simpleCapitalLPP.getClass().getName(),
                    "pegasus.simpleCapitalLPP.noPoliceNoCompte.mandatory");
        }

        // Vérifie que le simpleCapitalLPP ait un idInstitutionPrevoyance if
        // if (JadeStringUtil.isEmpty(simpleCapitalLPP.getIdInstitutionPrevoyance())) {
        // JadeThread.logError(simpleCapitalLPP.getClass().getName(),
        // "pegasus.simpleCapitalLPP.idInstitutionPrevoyance.mandatory");
        // }

        // Vérifie que le simpleCapitalLPP ait une part de propriété
        if (JadeStringUtil.isEmpty(simpleCapitalLPP.getPartProprieteNumerateur())
                || JadeStringUtil.isEmpty(simpleCapitalLPP.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleCapitalLPP.getClass().getName(),
                    "pegasus.simpleCapitalLPP.partPropriete.mandatory");
        }

    }
}
