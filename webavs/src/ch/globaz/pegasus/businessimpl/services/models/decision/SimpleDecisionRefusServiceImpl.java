/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionRefusSearch;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionRefusService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionRefusChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         22 juil. 2010
 */
public class SimpleDecisionRefusServiceImpl extends PegasusAbstractServiceImpl implements SimpleDecisionRefusService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionRefusService
     * #count(ch.globaz.pegasus.business.models.decision .SimpleDecisionRefusSearch)
     */
    @Override
    public int count(SimpleDecisionRefusSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count simpleDecisionsRefus, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionRefusService
     * #create(ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus)
     */
    @Override
    public SimpleDecisionRefus create(SimpleDecisionRefus simpleDecision) throws DecisionException,
            JadePersistenceException {
        if (simpleDecision == null) {
            throw new DecisionException("Unable to create simpleDecisionsRefus, the search model passed is null!");
        }
        SimpleDecisionRefusChecker.checkForCreate(simpleDecision);

        return (SimpleDecisionRefus) JadePersistenceManager.add(simpleDecision);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionRefusService
     * #delete(ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus)
     */
    @Override
    public SimpleDecisionRefus delete(SimpleDecisionRefus decision) throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to delete simpleDecisionsRefus, the search model passed is null!");
        }
        SimpleDecisionRefusChecker.checkForDelete(decision);
        return (SimpleDecisionRefus) JadePersistenceManager.delete(decision);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionRefusService#read(java.lang.String)
     */
    @Override
    public SimpleDecisionRefus read(String idDecision) throws JadePersistenceException, DecisionException {

        SimpleDecisionRefus simpleDecisionRefus = new SimpleDecisionRefus();
        simpleDecisionRefus.setId(idDecision);
        return (SimpleDecisionRefus) JadePersistenceManager.read(simpleDecisionRefus);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionRefusService
     * #search(ch.globaz.pegasus.business.models.decision .SimpleDecisionRefusSearch)
     */
    @Override
    public SimpleDecisionRefusSearch search(SimpleDecisionRefusSearch decisionRefusSearch)
            throws JadePersistenceException, DecisionException {
        if (decisionRefusSearch == null) {
            throw new DecisionException("Unable to search simpleDecisionRefus, the search model passed is null!");
        }
        return (SimpleDecisionRefusSearch) JadePersistenceManager.search(decisionRefusSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionRefusService
     * #update(ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus)
     */
    @Override
    public SimpleDecisionRefus update(SimpleDecisionRefus decision) throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to update simpleDecisionRefus, the search model passed is null!");
        }

        SimpleDecisionRefusChecker.checkForUpdate(decision);

        return (SimpleDecisionRefus) JadePersistenceManager.update(decision);
    }

}
