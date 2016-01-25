/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.CopiesDecisionSearch;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;
import ch.globaz.pegasus.business.services.models.decision.CopiesDecisionService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author SCE
 * 
 *         26 août 2010
 */
public class CopiesDecisionServiceImpl extends PegasusAbstractServiceImpl implements CopiesDecisionService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService
     * #create(ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision)
     */
    @Override
    public CopiesDecision create(CopiesDecision copiesDecision) throws DecisionException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (copiesDecision == null) {
            throw new DecisionException("Unable to create copiesDecisions, the model passed is null!");
        }
        PegasusImplServiceLocator.getSimpleCopiesDecisionsService().create(copiesDecision.getSimpleCopiesDecision());
        return copiesDecision;

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService
     * #delete(ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision)
     */
    @Override
    public CopiesDecision delete(CopiesDecision copiesDecision) throws JadePersistenceException, DecisionException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService
     * #deleteByLots(globaz.jade.persistence.model.JadeAbstractModel[])
     */
    @Override
    public void deleteByLots(JadeAbstractModel[] lotCopies) throws JadePersistenceException, DecisionException {
        if ((lotCopies == null) || (lotCopies.length == 0)) {
            throw new DecisionException("Unable to delete lot copies, the list is null or empty!");
        }
        // Suppression des annexes du lot
        for (JadeAbstractModel copies : lotCopies) {
            SimpleCopiesDecision copiesDecision = ((CopiesDecision) copies).getSimpleCopiesDecision();
            JadePersistenceManager.delete(copiesDecision);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService#read(java.lang.String)
     */
    @Override
    public CopiesDecision read(String idCopiesDecision) throws JadePersistenceException, DecisionException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService
     * #search(ch.globaz.pegasus.business.models.decision .SimpleCopiesDecisionsSearch)
     */
    @Override
    public CopiesDecisionSearch search(CopiesDecisionSearch copiesDecisionSearch) throws JadePersistenceException,
            DecisionException {
        if (copiesDecisionSearch == null) {
            throw new DecisionException("Unable to search copiesDecision, the search model passed is null!");
        }
        return (CopiesDecisionSearch) JadePersistenceManager.search(copiesDecisionSearch);

    }

}
