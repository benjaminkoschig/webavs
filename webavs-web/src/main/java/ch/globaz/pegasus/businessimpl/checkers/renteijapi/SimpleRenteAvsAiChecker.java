package ch.globaz.pegasus.businessimpl.checkers.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCRenteijapi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAi;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleRenteAvsAiChecker extends PegasusAbstractChecker {

    /**
     * Validation d'une renteAvsAi lors de la creation
     * 
     * @param simpleRenteAvsAi
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleRenteAvsAi simpleRenteAvsAi) throws RenteAvsAiException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleRenteAvsAiChecker.checkMandatory(simpleRenteAvsAi);

        if (!PegasusAbstractChecker.threadOnError()) {
            // Check integrité
            SimpleRenteAvsAiChecker.checkIntegrity(simpleRenteAvsAi);
        }
    }

    /**
     * Validation lors de l'effacement d'une renteAvAi A prori inutile, car on ne supprime pas une renteAvAi, mais on la
     * marque comme supprimé
     * 
     * @param simpleRenteAvsAi
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForDelete(SimpleRenteAvsAi simpleRenteAvsAi) throws RenteAvsAiException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Validation d'une renteAvAi lors d'une mise a jour
     * 
     * @param renteAvAi
     * @throws RenteAvsAiException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(SimpleRenteAvsAi simpleRenteAvsAi) throws RenteAvsAiException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleRenteAvsAiChecker.checkMandatory(simpleRenteAvsAi);
        if (!PegasusAbstractChecker.threadOnError()) {
            // Check integrité
            SimpleRenteAvsAiChecker.checkIntegrity(simpleRenteAvsAi);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * 
     * @param simpleIndemniteJournaliereAi
     * @throws simpleRenteAvsAiException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleRenteAvsAi simpleRenteAvsAi) throws RenteAvsAiException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        if (!JadeStringUtil.isBlank(simpleRenteAvsAi.getDegreInvalidite())) {

            float pourcentage = new Float(simpleRenteAvsAi.getDegreInvalidite());

            if ((pourcentage < 0) || (pourcentage > 100)) {
                JadeThread.logError(simpleRenteAvsAi.getClass().getName(),
                        "pegasus.simpleRenteAvsAi.degreInvalidite.integrity");
            }
        }

    }

    /**
     * Vérification des données obligatoires
     * 
     * @param simpleRenteAvsAi
     * @throws JadeNoBusinessLogSessionError
     * @throws RenteAvsAiException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws IndemniteJournaliereAiException
     */
    private static void checkMandatory(SimpleRenteAvsAi simpleRenteAvsAi) throws RenteAvsAiException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Si le code cs du genre de rente est sans rente le type de pc est
        // obligatoire
        if (simpleRenteAvsAi.getCsTypeRente().equals(IPCRenteijapi.CS_SANS_RENTE_AVS)) {
            if (JadeStringUtil.isEmpty(simpleRenteAvsAi.getCsTypePc())) {
                JadeThread.logError(simpleRenteAvsAi.getClass().getName(), "pegasus.simpleRenteAvsAi.typepc.mandatory");
            }
        }
        // Vérifie que le montant soit spécifié
        if (JadeStringUtil.isEmpty(simpleRenteAvsAi.getMontant())) {
            JadeThread.logError(simpleRenteAvsAi.getClass().getName(), "pegasus.simpleRenteAvsAi.montant.mandatory");
        }

        // Vérifie que le type de rente soit spécifié
        if (JadeStringUtil.isEmpty(simpleRenteAvsAi.getCsTypeRente())) {
            JadeThread
                    .logError(simpleRenteAvsAi.getClass().getName(), "pegasus.simpleRenteAvsAi.csTypeRente.mandatory");
        }
         // Si le code cs du genre de rente est 3 le type de pc est
         // obligatoire         
         if (simpleRenteAvsAi.getCsTypeRente().equals(IPCRenteAvsAi.CS_TYPE_RENTE_13)) {
             if (JadeStringUtil.isEmpty(simpleRenteAvsAi.getImputationFortune())) {
                 JadeThread.logError(simpleRenteAvsAi.getClass().getName(), "pegasus.simpleRenteAvsAi.imputationFortune.mandatory");
             }
         }

    }
}
