package ch.globaz.pegasus.businessimpl.checkers.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAllocationImpotentChecker extends PegasusAbstractChecker {

    /**
     * Validation d'une allocation impotent lors de la creation
     * 
     * @param simpleAllocationImpotent
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AllocationImpotentException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleAllocationImpotent simpleAllocationImpotent)
            throws AllocationImpotentException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleAllocationImpotentChecker.checkMandatory(simpleAllocationImpotent);

        if (!PegasusAbstractChecker.threadOnError()) {
            // Check integrité
            SimpleAllocationImpotentChecker.checkIntegrity(simpleAllocationImpotent);
        }
    }

    /**
     * Validation lors de l'effacement d'une allocationImpotent A prori inutile, car on ne supprime pas une
     * allocationImpotent, mais on la marque comme supprimé
     * 
     * @param simpleAllocationImpotent
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AllocationImpotenetException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForDelete(SimpleAllocationImpotent simpleAllocationImpotent)
            throws AllocationImpotentException, JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Validation d'une allocationImpotent lors d'une mise a jour
     * 
     * @param allocationImpotent
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(SimpleAllocationImpotent simpleAllocationImpotent)
            throws AllocationImpotentException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleAllocationImpotentChecker.checkMandatory(simpleAllocationImpotent);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * 
     * @param simpleAllocationImpotent
     * @throws simpleAllocationImpotentException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAllocationImpotent simpleAllocationImpotent)
            throws AllocationImpotentException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Vérification des données obligatoires
     * 
     * @param simpleAllocationImpotent
     * @throws JadeNoBusinessLogSessionError
     * @throws RenteAvsAiException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws IndemniteJournaliereAiException
     */
    private static void checkMandatory(SimpleAllocationImpotent simpleAllocationImpotent)
            throws AllocationImpotentException, JadePersistenceException, JadeNoBusinessLogSessionError {

        // Vérifie que le montant soit spécifié
        if (JadeStringUtil.isEmpty(simpleAllocationImpotent.getMontant())) {
            JadeThread.logError(simpleAllocationImpotent.getClass().getName(),
                    "pegasus.simpleAllocationImpotent.montant.mandatory");
        }

        // Vérifie que le type de rente soit spécifié
        if (JadeStringUtil.isEmpty(simpleAllocationImpotent.getCsTypeRente())) {
            JadeThread.logError(simpleAllocationImpotent.getClass().getName(),
                    "pegasus.simpleAllocationImpotent.csTypeRente.mandatory");
        }

    }
}
