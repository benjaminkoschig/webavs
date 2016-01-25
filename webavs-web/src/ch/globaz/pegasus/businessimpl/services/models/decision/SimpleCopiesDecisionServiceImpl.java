/**
 * 
 /**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecisionSearch;
import ch.globaz.pegasus.business.services.models.decision.SimpleCopiesDecisionService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         26 août 2010
 */
public class SimpleCopiesDecisionServiceImpl extends PegasusAbstractServiceImpl implements SimpleCopiesDecisionService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService
     * #create(ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision)
     */
    @Override
    public SimpleCopiesDecision create(SimpleCopiesDecision simpleCopiesDecision) throws DecisionException,
            JadePersistenceException {
        if (simpleCopiesDecision == null) {
            throw new DecisionException("Unable to create copiesDecisions, the model passed is null!");
        }
        return (SimpleCopiesDecision) JadePersistenceManager.add(simpleCopiesDecision);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService
     * #delete(ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision)
     */
    @Override
    public SimpleCopiesDecision delete(SimpleCopiesDecision simpleCopiesDecision) throws JadePersistenceException,
            DecisionException {
        // TODO Auto-generated method stub
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
            JadePersistenceManager.delete((SimpleCopiesDecision) copies);
        }

    }

    @Override
    public void deleteForDecision(List<String> idsDecisionHeader) throws JadePersistenceException, DecisionException {

        if (idsDecisionHeader == null) {
            throw new DecisionException("Unable to deleteForDecision idsDecisionHeader, the model passed is null!");
        }
        SimpleCopiesDecisionSearch searchModel = new SimpleCopiesDecisionSearch();
        searchModel.setForInIdDecisionHeader(idsDecisionHeader);
        JadePersistenceManager.delete(searchModel);
    }

    /**
     * Suppression des copies pour une decision
     */
    @Override
    public void deleteForDecision(String idDecisionHeader) throws JadePersistenceException, DecisionException {

        if (idDecisionHeader == null) {
            throw new DecisionException("Unable to delete copies for decision, the idDecisionHeader passed is null");
        }

        SimpleCopiesDecisionSearch searchModel = new SimpleCopiesDecisionSearch();
        searchModel.setForIdDecisionHeader(idDecisionHeader);
        searchModel = (SimpleCopiesDecisionSearch) JadePersistenceManager.search(searchModel);
        if (searchModel.getSize() > 0) {
            deleteByLots(searchModel.getSearchResults());
            // JadePersistenceManager.delete(searchModel);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleCopiesDecisionService#read(java.lang.String)
     */
    @Override
    public SimpleCopiesDecision read(String idCopiesDecision) throws JadePersistenceException, DecisionException {
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
    public SimpleCopiesDecisionSearch search(SimpleCopiesDecisionSearch copiesDecisionSearch)
            throws JadePersistenceException, DecisionException {
        if (copiesDecisionSearch == null) {
            throw new DecisionException("Unable to search copiesDecision, the search model passed is null!");
        }
        return (SimpleCopiesDecisionSearch) JadePersistenceManager.search(copiesDecisionSearch);

    }

}
