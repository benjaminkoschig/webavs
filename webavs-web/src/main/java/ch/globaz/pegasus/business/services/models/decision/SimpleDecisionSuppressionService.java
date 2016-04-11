/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppressionSearch;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public interface SimpleDecisionSuppressionService extends JadeApplicationService {
    /**
     * Compte le nombre d'occurence de l'entit�
     * 
     * @param search
     * @return le nombre d'occurence de l'entit�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleDecisionSuppressionSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Cr�ation d'une entit� SinpleDecisionSuppression
     * 
     * @param decision
     * @return l'entit� cr�e
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression create(SimpleDecisionSuppression decision) throws JadePersistenceException,
            DecisionException, PmtMensuelException, JadeApplicationServiceNotAvailableException,
            JadeNoBusinessLogSessionError;

    /**
     * Suppression d'une entit�
     * 
     * @param decision
     * @return l'entit� supprim�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleDecisionSuppression delete(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException;

    /**
     * Chargement d'une entit�
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * @param idDecisionHeader
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression readByIdDecisionHeader(String idDecisionHeader) throws JadePersistenceException,
            DecisionException;

    /**
     * Recherche en fonction des param�tres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectu�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppressionSearch search(SimpleDecisionSuppressionSearch decisionSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * Mise � jour d'une entit�
     * 
     * @param decision
     * @return l'entit� mise � jour
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression update(SimpleDecisionSuppression decision) throws JadePersistenceException,
            DecisionException, PmtMensuelException, JadeApplicationServiceNotAvailableException,
            JadeNoBusinessLogSessionError;
}
