/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppressionSearch;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionSuppressionService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionSuppressionChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionSuppressionServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDecisionSuppressionService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #count(ch.globaz.pegasus.business.models.decision .SimpleDecisionSuppressionSearch)
     */
    @Override
    public int count(SimpleDecisionSuppressionSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count simpleDecisionsSuppression, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #create(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppression)
     */
    @Override
    public SimpleDecisionSuppression create(SimpleDecisionSuppression decision) throws JadePersistenceException,
            DecisionException, PmtMensuelException, JadeApplicationServiceNotAvailableException,
            JadeNoBusinessLogSessionError {
        if (decision == null) {
            throw new DecisionException("Unable to create simpleDecisionsSuppression, the search model passed is null!");
        }
        SimpleDecisionSuppressionChecker.checkForCreate(decision);
        return (SimpleDecisionSuppression) JadePersistenceManager.add(decision);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #delete(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppression)
     */
    @Override
    public SimpleDecisionSuppression delete(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException {
        if (decision == null) {
            throw new DecisionException("Unable to delete simpleDecisionsSuppression, the search model passed is null!");
        }
        SimpleDecisionSuppressionChecker.checkForDelete(decision);
        return (SimpleDecisionSuppression) JadePersistenceManager.delete(decision);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService#read(java.lang.String)
     */
    @Override
    public SimpleDecisionSuppression read(String idDecision) throws JadePersistenceException, DecisionException {
        SimpleDecisionSuppression simpleDecisionSuppression = new SimpleDecisionSuppression();
        simpleDecisionSuppression.setId(idDecision);
        return (SimpleDecisionSuppression) JadePersistenceManager.read(simpleDecisionSuppression);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #search(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppressionSearch)
     */
    @Override
    public SimpleDecisionSuppressionSearch search(SimpleDecisionSuppressionSearch decisionSearch)
            throws JadePersistenceException, DecisionException {
        if (decisionSearch == null) {
            throw new DecisionException("Unable to search simpleDecisionSuppression, the search model passed is null!");
        }
        return (SimpleDecisionSuppressionSearch) JadePersistenceManager.search(decisionSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #update(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppression)
     */
    @Override
    public SimpleDecisionSuppression update(SimpleDecisionSuppression decision) throws JadePersistenceException,
            DecisionException, PmtMensuelException, JadeApplicationServiceNotAvailableException,
            JadeNoBusinessLogSessionError {
        if (decision == null) {
            throw new DecisionException("Unable to update simpleDecisionSuppression, the search model passed is null!");
        }

        SimpleDecisionSuppressionChecker.checkForUpdate(decision);

        return (SimpleDecisionSuppression) JadePersistenceManager.update(decision);
    }

}
