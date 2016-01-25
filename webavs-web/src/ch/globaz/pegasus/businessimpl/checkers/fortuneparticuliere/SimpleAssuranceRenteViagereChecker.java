package ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagere;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleAssuranceRenteViagereChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAssuranceRenteViagere
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AssuranceRenteViagereException
     */
    public static void checkForCreate(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleAssuranceRenteViagereChecker.checkMandatory(simpleAssuranceRenteViagere);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAssuranceRenteViagereChecker.checkIntegrity(simpleAssuranceRenteViagere);
        }
    }

    /**
     * @param simpleAssuranceRenteViagere
     */
    public static void checkForDelete(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere) {
    }

    /**
     * @param simpleAssuranceRenteViagere
     */
    public static void checkForUpdate(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAssuranceRenteViagere
     * @throws AssuranceRenteViagereException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * @param simpleAssuranceRenteViagere
     */
    private static void checkMandatory(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere) {

        // Vérifie que le simpleAssuranceRenteViagere ait une valeur de rachat
        if (JadeStringUtil.isBlankOrZero(simpleAssuranceRenteViagere.getMontantValeurRachat())) {
            JadeThread.logError(simpleAssuranceRenteViagere.getClass().getName(),
                    "pegasus.simpleAssuranceRenteViagere.montantValeurRachat.mandatory");
        }

        // Vérifie que le simpleAssuranceRenteViagere ait un numero de police
        if (JadeStringUtil.isEmpty(simpleAssuranceRenteViagere.getNumeroPolice())) {
            JadeThread.logError(simpleAssuranceRenteViagere.getClass().getName(),
                    "pegasus.simpleAssuranceRenteViagere.numeroPolice.mandatory");
        }

        // Vérifie que le simpleAssuranceRenteViagere soit liee a une compagnie
        if (JadeStringUtil.isEmpty(simpleAssuranceRenteViagere.getIdCompagnie())) {
            // Cette valeur n'est plus obligatoire
            // JadeThread.logError(simpleAssuranceRenteViagere.getClass().getName(),
            // "pegasus.simpleAssuranceRenteViagere.idCompagnie.mandatory");
        }

        // Vérifie que le simpleAssuranceRenteViagere ait un montant de rente
        if (JadeStringUtil.isBlankOrZero(simpleAssuranceRenteViagere.getMontantRenteViagere())) {
            JadeThread.logError(simpleAssuranceRenteViagere.getClass().getName(),
                    "pegasus.simpleAssuranceRenteViagere.montantRenteViagere.mandatory");
        }

    }
}
