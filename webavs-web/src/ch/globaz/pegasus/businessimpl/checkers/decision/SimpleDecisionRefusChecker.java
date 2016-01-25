/**
 * 
 */
package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionRefusChecker extends PegasusAbstractChecker {

    /**
     * Validation lors de la creation d'une decision de refus
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForCreate(SimpleDecisionRefus decision) throws DecisionException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // Check mandatory
        SimpleDecisionRefusChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionRefusChecker.checkIntegrity(decision);
        }
    }

    /**
     * Validation lors de la suppression d'une decision de refus
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForDelete(SimpleDecisionRefus decision) throws DecisionException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
    }

    /**
     * Validation lors de la mise à jour d'une decision de refus
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(SimpleDecisionRefus decision) throws DecisionException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleDecisionRefusChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionRefusChecker.checkIntegrity(decision);
        }
    }

    /**
     * Verification de l'integrite des données
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDecisionRefus decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // L'id du header doit etre défini
        if (JadeStringUtil.isEmpty(decision.getIdDecisionHeader())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionRefus.header.integrity");
        }
        // L'id de la demande lié doit etre définie
        if (JadeStringUtil.isEmpty(decision.getIdDemandePc())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionRefus.idDemande.integrity");
        }

    }

    /**
     * Verification des donnees obligatoires
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkMandatory(SimpleDecisionRefus decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Le motif doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getCsMotif())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionRefus.motif.mandatory");
        }
        // La date de refus doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getDateRefus())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionRefus.dateRefus.mandatory");
        }
        // si le motif a un sous motifs et que celui ci est vide
        if (IPCDecision.CS_MOTIF_REFUS_WITH_SM.equals(decision.getCsMotif())) {
            if (JadeStringUtil.isEmpty(decision.getCsSousMotif())) {
                JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionRefus.sousMotifs.mandatory");
            }
        }

    }
}
