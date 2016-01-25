/**
 * 
 */
package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionApresCalculChecker extends PegasusAbstractChecker {

    /**
     * Validation d'une decision apres calcul lors de l'insertion
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForCreate(SimpleDecisionApresCalcul decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Check mandatory
        SimpleDecisionApresCalculChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionApresCalculChecker.checkIntegrity(decision);
        }
    }

    /**
     * Validation d'une decision apres calcul lors de la suppression
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForDelete(SimpleDecisionApresCalcul decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Validation d'une decision apres calcul lors de la mise à jour
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(SimpleDecisionApresCalcul decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        // Check mandatory
        SimpleDecisionApresCalculChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionApresCalculChecker.checkIntegrity(decision);
        }
    }

    /**
     * Vérification de l'intégrité des données
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDecisionApresCalcul decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Le header doit etre defini
        if (JadeStringUtil.isEmpty(decision.getIdDecisionHeader())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionApresCalcul.header.mandatory");
        }
        // la taille des remarques ne doit pas dépasser 1024 caractères
        if ((decision.getRemarqueGenerale() != null) && (decision.getRemarqueGenerale().length() > 1024)) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionApresCalcul.remarquegen.size");
        }
        // la taille des remarques ne doit pas dépasser 1024 caractères
        if ((decision.getIntroduction() != null) && (decision.getIntroduction().length() > 1024)) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionApresCalcul.introduction.size");
        }
    }

    /**
     * Vérification des données obligatoires
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkMandatory(SimpleDecisionApresCalcul decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // La version du droit doit etre défini
        if (JadeStringUtil.isEmpty(decision.getIdVersionDroit())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionApresCalcul.idDroit.mandatory");
        }

    }
}
