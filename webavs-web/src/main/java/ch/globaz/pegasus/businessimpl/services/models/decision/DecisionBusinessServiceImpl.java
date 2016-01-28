/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionDate;
import ch.globaz.pegasus.business.models.decision.DecisionDateSearch;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.services.models.decision.DecisionBusinessService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         24 sept. 2010
 */
public class DecisionBusinessServiceImpl extends PegasusAbstractServiceImpl implements DecisionBusinessService {

    @Override
    public String findDateDecision(String idVersionDroit) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idVersionDroit)) {
            throw new DecisionException("Unable to findDateDecision idVersionDroit, the id passed is null!");
        }

        DecisionDateSearch search = new DecisionDateSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search = (DecisionDateSearch) JadePersistenceManager.search(search);

        if (search.getSize() == 0) {
            throw new DecisionException("No decisions found with this idVersionDroit: " + idVersionDroit);
        }

        return ((DecisionDate) search.getSearchResults()[0]).getDateDecision();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionBusinessService #read(java.lang.String)
     */
    @Override
    public ListDecisions read(String idDecision) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to read decision, the id passed is null!");
        }
        ListDecisions decision = new ListDecisions();
        decision.setId(idDecision);
        return (ListDecisions) JadePersistenceManager.read(decision);

    }

}
