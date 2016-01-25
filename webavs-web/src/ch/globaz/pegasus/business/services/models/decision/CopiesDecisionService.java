/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.CopiesDecisionSearch;

/**
 * @author SCE
 * 
 *         26 ao�t 2010
 */
public interface CopiesDecisionService extends JadeApplicationService {

    /**
     * Retourne le service de creation d'une entit� copies de decision
     * 
     * @param simpleCopiesDecision
     * @return l'entit� cr�er
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public CopiesDecision create(CopiesDecision simpleCopiesDecision) throws DecisionException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� copies de decision
     * 
     * @param simpleCopiesDecision
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public CopiesDecision delete(CopiesDecision copiesDecision) throws JadePersistenceException, DecisionException;

    /**
     * Suppression des annexes par lots
     * 
     * @param lotAnnexes
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteByLots(JadeAbstractModel[] lotCopies) throws JadePersistenceException, DecisionException;

    /**
     * Charge une entit�
     * 
     * @param idCopiesDecision
     * @return l'entit� charg�e
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public CopiesDecision read(String idCopiesDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet la recherche d'entit� sur la base de crit�es definis
     * 
     * @param copiesDecisionSearch
     * @return l'entit�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public CopiesDecisionSearch search(CopiesDecisionSearch copiesDecisionSearch) throws JadePersistenceException,
            DecisionException;

}
