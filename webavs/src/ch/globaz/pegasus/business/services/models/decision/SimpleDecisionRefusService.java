/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionRefusSearch;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public interface SimpleDecisionRefusService extends JadeApplicationService {
    /**
     * Compte le nombre d'occurence en base de données
     * 
     * @param search
     * @return int, le nombre d'occurence
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleDecisionRefusSearch search) throws DecisionException, JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionRefus create(SimpleDecisionRefus simpleDecision) throws DecisionException,
            JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionRefus delete(SimpleDecisionRefus decision) throws JadePersistenceException, DecisionException;

    /**
     * Charge l'entité en base de données
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionRefus read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Recherche de l'entité en base de données
     * 
     * @param simpleDecisionSearch
     * @return serach, modele de recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionRefusSearch search(SimpleDecisionRefusSearch decisionRefusSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionRefus update(SimpleDecisionRefus decision) throws JadePersistenceException, DecisionException;

}
