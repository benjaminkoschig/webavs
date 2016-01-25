/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.models.decision.ListDecisionsValidees;
import ch.globaz.pegasus.business.models.decision.ListDecisionsValideesSearch;
import ch.globaz.pegasus.business.services.models.decision.ListDecisionsValideesService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         22 sept. 2010
 */
public class ListDecisionsValideesServiceImpl extends PegasusAbstractServiceImpl implements
        ListDecisionsValideesService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision.DecisionService# readDecision(java.lang.String)
     */
    @Override
    public ListDecisionsValidees readDecision(String idDecision) throws DecisionException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to read decision, the id passed is null!");
        }
        ListDecisions decision = new ListDecisions();
        decision.setId(idDecision);
        return (ListDecisionsValidees) JadePersistenceManager.read(decision);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision.DecisionService# searchDecisions
     * (ch.globaz.pegasus.business.models.decision.DecisionSearch)
     */
    @Override
    public ListDecisionsValideesSearch search(ListDecisionsValideesSearch listDecisionsSearch)
            throws DecisionException, JadePersistenceException {

        if (listDecisionsSearch == null) {
            throw new DecisionException("Unable to search decisions, the search model passed is null!");
        }
        return (ListDecisionsValideesSearch) JadePersistenceManager.search(listDecisionsSearch);
    }

}
