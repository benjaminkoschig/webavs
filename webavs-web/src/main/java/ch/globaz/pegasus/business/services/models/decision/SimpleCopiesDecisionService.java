/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecisionSearch;

/**
 * @author SCE
 * 
 *         26 ao�t 2010
 */
public interface SimpleCopiesDecisionService extends JadeApplicationService {

    /**
     * Retourne le service de creation d'une entit� copies de decision
     * 
     * @param simpleCopiesDecision
     * @return l'entit� cr�er
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleCopiesDecision create(SimpleCopiesDecision simpleCopiesDecision) throws DecisionException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� copies de decision
     * 
     * @param simpleCopiesDecision
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleCopiesDecision delete(SimpleCopiesDecision simpleCopiesDecision) throws JadePersistenceException,
            DecisionException;

    /**
     * Suppression des annexes par lots
     * 
     * @param lotAnnexes
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteByLots(JadeAbstractModel[] lotCopies) throws JadePersistenceException, DecisionException;

    /**
     * 
     * Attention aucun check n'est fait sur la liste idsDecisionHeader. Suppression de toutes les copies d'une d�cision
     * 
     * @param idDecisionHeader
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteForDecision(List<String> idsDecisionHeader) throws JadePersistenceException, DecisionException;

    /**
     * Suppression de toutes les copies d'une d�cision
     * 
     * @param idDecisionHeader
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteForDecision(String idDecisionHeader) throws JadePersistenceException, DecisionException;

    /**
     * Charge une entit�
     * 
     * @param idCopiesDecision
     * @return l'entit� charg�e
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleCopiesDecision read(String idCopiesDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet la recherche d'entit� sur la base de crit�es definis
     * 
     * @param copiesDecisionSearch
     * @return l'entit�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleCopiesDecisionSearch search(SimpleCopiesDecisionSearch copiesDecisionSearch)
            throws JadePersistenceException, DecisionException;

}
