/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.ListDecisionsValidees;
import ch.globaz.pegasus.business.models.decision.ListDecisionsValideesSearch;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public interface ListDecisionsValideesService extends JadeApplicationService {

    /**
     * Retourne la decision chargé
     * 
     * @param idDecision
     * @return decsion, la decision chargé
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDecisionsValidees readDecision(String idDecision) throws DecisionException, JadePersistenceException;

    /**
     * Retourne le resultat de recherches des décisions de tout types
     * 
     * @param decisionSearch
     * @return decisionSearch, le modèle de recherche
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDecisionsValideesSearch search(ListDecisionsValideesSearch listDecisionsSearch)
            throws DecisionException, JadePersistenceException;
}
