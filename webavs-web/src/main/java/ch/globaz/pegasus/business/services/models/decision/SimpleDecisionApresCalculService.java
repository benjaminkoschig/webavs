/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalculSearch;

/**
 * @author SCE
 * 
 *         26 juil. 2010
 */
public interface SimpleDecisionApresCalculService extends JadeApplicationService {
    /**
     * Compte le nombre d'occurence en base de données
     * 
     * @param search
     * @return int, le nombre d'occurence
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleDecisionApresCalculSearch search) throws DecisionException, JadePersistenceException;

    /**
     * @param decision
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleDecisionApresCalcul create(SimpleDecisionApresCalcul simpleDecision) throws DecisionException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Attention aucun check n'est fait sur la liste idsDecisionHeader.
     * 
     * @param list
     *            des id de la simpleDesionHeader
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public void delete(List<String> idsDecisionHeader) throws DecisionException, JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionApresCalcul delete(SimpleDecisionApresCalcul decision) throws JadePersistenceException,
            DecisionException;

    /**
     * Charge l'entité en base de données
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionApresCalcul read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Recherche de l'entité en base de données
     * 
     * @param simpleDecisionSearch
     * @return serach, modele de recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionApresCalculSearch search(SimpleDecisionApresCalculSearch decisionSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * @param decision
     * @return
     */
    public SimpleDecisionApresCalcul update(SimpleDecisionApresCalcul decision) throws JadePersistenceException,
            DecisionException;

    /**
     * Permet de trouver la simpleDecisionApresCalcul avec un idDecisionHeader.
     * Si rien n'est retrouvé NULL sera renvoyé.
     * Si plusieurs décisions après calcul sont retrouve une exception sera levé.
     * 
     * @param idDecisionHeader
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionApresCalcul readByIdDecisionHeader(String idDecisionHeader) throws DecisionException,
            JadePersistenceException;

}
