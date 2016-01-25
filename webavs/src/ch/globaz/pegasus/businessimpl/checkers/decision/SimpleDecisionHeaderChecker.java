/**
 * 
 */
package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionHeaderChecker extends PegasusAbstractChecker {
    /**
     * Verification des dates de la decision après le dernier paiement, et avant le prochain paiement Uniquement appelé
     * pour la préparation et la validation des décision après calcul et des décision de suppression
     * 
     * @param decision
     * @throws PmtMensuelException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    public static void checkForCoherenceDateDecision(SimpleDecisionHeader decision) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {

        // Check du format de la date
        SimpleDecisionHeaderChecker.checkForDateDecisionFormat(decision.getDateDecision());

        // vérifie que la date de décision soit après le dernier paiement
        String dateDernierPmt = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        String dateProchainPmt = "01." + PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if (JadeDateUtil.isDateBefore(decision.getDateDecision(), dateDernierPmt)) {
            String[] params = new String[2];
            params[0] = decision.getDateDecision();
            params[1] = dateDernierPmt;
            JadeThread.logError(decision.getClass().getName(),
                    "pegasus.simpleDecisionHeader.dateDecision.integrity.dateDernierPaiement", params);
        }
        if (!JadeDateUtil.isDateBefore(decision.getDateDecision(), dateProchainPmt)) {
            String[] params = new String[2];
            params[0] = decision.getDateDecision();
            params[1] = dateProchainPmt;
            JadeThread.logError(decision.getClass().getName(),
                    "pegasus.simpleDecisionHeader.dateDecision.integrity.dateProchainPaiement", params);
        }
    }

    /**
     * Validation d'un header de decision lors de l'insertion
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForCreate(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Check mandatory
        SimpleDecisionHeaderChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionHeaderChecker.checkIntegrity(decision);
        }
    }

    public static void checkForDateDecisionFormat(String date) {
        // format de date incorrect
        if (!JadeDateUtil.isGlobazDate(date)) {
            JadeThread.logError(SimpleDecisionHeader.class.getName(),
                    "pegasus.simpleDecisionHeader.formatdate.integrity");

        }
    }

    /**
     * Validation lors de l'effacement d'un header de decision
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForDelete(SimpleDecisionHeaderChecker decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    public static void checkForPrevalidation(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleDecisionHeaderChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionHeaderChecker.checkIntegrity(decision);
        }

    }

    /**
     * Validation lors de la mise à jour d'un header de decision
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleDecisionHeaderChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionHeaderChecker.checkIntegrity(decision);
        }
    }

    /**
     * Verification de l'integrite des donness
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Le decision header doit avoir un type
        if (JadeStringUtil.isEmpty(decision.getCsTypeDecision())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.type.integrity");
        }
        // L'id du tiers doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getIdTiersBeneficiaire())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.tiers.integrity");
        }
        // L'id du tier courrier doit etre spécifié
        // if (JadeStringUtil.isEmpty(decision.getIdTiersCourrier())) {
        // JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.tiersCourrier.integrity");
        // }
        // Le no de decision doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getNoDecision())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.noDecision.integrity");
        }
        // La date de preparation doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getDatePreparation())) {
            JadeThread
                    .logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.datePreparation.integrity");
        }
        // La personne qui a préparé la decision doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getPreparationPar())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.preparationPar.integrity");
        }
    }

    /**
     * Validation des champs obligatoires
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkMandatory(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // La date de decision doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getDateDecision())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.dateDecision.mandatory");
        }

    }
}
