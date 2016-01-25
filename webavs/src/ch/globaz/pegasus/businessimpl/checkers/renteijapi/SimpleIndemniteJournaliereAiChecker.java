package ch.globaz.pegasus.businessimpl.checkers.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIndemniteJournaliereAi;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * Checker pour le modele simple IndemniteJournaliereAi 6.2010
 * 
 * @author SCE
 * 
 */
public abstract class SimpleIndemniteJournaliereAiChecker extends PegasusAbstractChecker {

    /**
     * Validation d'une indemnitejournalieresAi lors de la creation
     * 
     * @param simpleIndemniteJournaliereAi
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereAiException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleIndemniteJournaliereAiChecker.checkMandatory(simpleIndemniteJournaliereAi);

        if (!PegasusAbstractChecker.threadOnError()) {
            // Check integrit�
            SimpleIndemniteJournaliereAiChecker.checkIntegrity(simpleIndemniteJournaliereAi);
        }
    }

    /**
     * Validation lors de l'effacement d'une indemnite journaliere A prori inutile, car on ne supprime pas une
     * indemniteJournaliereAi, mais on la marque comme supprim�
     * 
     * @param simpleIndemniteJournaliereAi
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereAiException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForDelete(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Validation d'une indemniteJournaliere lors d'une mise a jour
     * 
     * @param simpleIndemniteJournaliereAi
     * @throws IndemniteJournaliereAiException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleIndemniteJournaliereAiChecker.checkMandatory(simpleIndemniteJournaliereAi);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * 
     * @param simpleIndemniteJournaliereAi
     * @throws IndemniteJournaliereAiException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * V�rification des donn�es obligatoires
     * 
     * @param simpleIndemniteJournaliereAi
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws IndemniteJournaliereAiException
     */
    private static void checkMandatory(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException, JadeNoBusinessLogSessionError {

        // V�rifie que le nombre de jours soit sp�cifi�
        // if (JadeStringUtil.isEmpty(simpleIndemniteJournaliereAi.getNbreJours())) {
        // JadeThread.logError(simpleIndemniteJournaliereAi.getClass().getName(),
        // "pegasus.simpleIndemniteJournaliereAi.nbreJours.mandatory");
        // }

        // V�rifie que le montant soit sp�cifi�
        if (JadeStringUtil.isEmpty(simpleIndemniteJournaliereAi.getMontant())) {
            JadeThread.logError(simpleIndemniteJournaliereAi.getClass().getName(),
                    "pegasus.simpleIndemniteJournaliereAi.montantIJ.mandatory");
        }

        // V�rifie que le type IJAI est sp�cifi�
        if (JadeStringUtil.isEmpty(simpleIndemniteJournaliereAi.getCsTypeIjai())) {
            JadeThread.logError(simpleIndemniteJournaliereAi.getClass().getName(),
                    "pegasus.simpleIndemniteJournaliereAi.csTypeIJAI.mandatory");
        }

    }
}
