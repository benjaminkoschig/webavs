/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeaderSearch;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public interface SimpleDecisionHeaderService extends JadeApplicationService {

    /**
     * Compte le nombre d'occurence en base de donn�es
     * 
     * @param search
     * @return int, le nombre d'occurence
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleDecisionHeaderSearch search) throws DecisionException, JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionHeader create(SimpleDecisionHeader simpleDecision) throws DecisionException,
            JadePersistenceException;

    /**
     * Attention aucun check n'est fait sur la liste idsDecisionHeader.
     * 
     * @param list
     *            des id de la simpleDesionHeade
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void delete(List<String> idsDecisionHeader) throws JadePersistenceException, DecisionException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionHeader delete(SimpleDecisionHeader decision) throws JadePersistenceException,
            DecisionException;

    /**
     * Charge l'entit� en base de donn�es
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionHeader read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Recherche de l'entit� en base de donn�es
     * 
     * @param simpleDecisionSearch
     * @return serach, modele de recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionHeaderSearch search(SimpleDecisionHeaderSearch decisionSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionHeader update(SimpleDecisionHeader decision) throws JadePersistenceException,
            DecisionException;

    /**
     * Mise � jour, pour la pr�validation, gestion de l'�tat pr�-valider en dernier recours
     * 
     * @param decision
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleDecisionHeader updateForPrevalidation(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException;

    public SimpleDecisionHeader updateForValidation(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException;

}
