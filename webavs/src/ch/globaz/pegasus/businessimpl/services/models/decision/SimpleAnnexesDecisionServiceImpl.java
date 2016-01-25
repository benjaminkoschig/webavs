/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecisionSearch;
import ch.globaz.pegasus.business.services.models.decision.SimpleAnnexesDecisionService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         26 août 2010
 */
public class SimpleAnnexesDecisionServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleAnnexesDecisionService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleAnnexesDecisionService
     * #create(ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision)
     */
    @Override
    public SimpleAnnexesDecision create(SimpleAnnexesDecision simpleAnnexesDecision) throws DecisionException,
            JadePersistenceException {
        if (simpleAnnexesDecision == null) {
            throw new DecisionException("Unable to create annexesDecisions, the model passed is null!");
        }
        return (SimpleAnnexesDecision) JadePersistenceManager.add(simpleAnnexesDecision);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleAnnexesDecisionService
     * #delete(ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision)
     */
    @Override
    public SimpleAnnexesDecision delete(SimpleAnnexesDecision simpleAnnexesDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleAnnexesDecision == null) {
            throw new DecisionException("Unable to delete simple annexe decision the model passed is null!");
        }
        return (SimpleAnnexesDecision) JadePersistenceManager.delete(simpleAnnexesDecision);
    }

    @Override
    public void deleteByLots(JadeAbstractModel[] lotAnnexes) throws JadePersistenceException, DecisionException {
        if ((lotAnnexes == null) || (lotAnnexes.length == 0)) {
            throw new DecisionException("Unable to delete lot annexe, the list is null or empty!");
        }
        // Suppression des annexes du lot
        for (JadeAbstractModel annexe : lotAnnexes) {
            JadePersistenceManager.delete((SimpleAnnexesDecision) annexe);
        }
    }

    @Override
    public void deleteForDecision(List<String> idsDecisionHeader) throws DecisionException, JadePersistenceException {
        if (idsDecisionHeader == null) {
            throw new DecisionException("Unable to deleteForDecision idsDecisionHeader, the model passed is null!");
        }
        SimpleAnnexesDecisionSearch searchModel = new SimpleAnnexesDecisionSearch();
        searchModel.setForInIdDecisionHeader(idsDecisionHeader);
        JadePersistenceManager.delete(searchModel);

    }

    /**
     * Suppression des copies pour une decision
     */
    @Override
    public void deleteForDecision(String idDecisionHeader) throws JadePersistenceException, DecisionException {

        if (idDecisionHeader == null) {
            throw new DecisionException("Unable to delete annexes for decision, the idDecisionHeader passed is null");
        }

        SimpleAnnexesDecisionSearch searchModel = new SimpleAnnexesDecisionSearch();
        searchModel.setForIdDecisionHeader(idDecisionHeader);
        searchModel = (SimpleAnnexesDecisionSearch) JadePersistenceManager.search(searchModel);
        // JadePersistenceManager.delete(searchModel);
        if (searchModel.getSize() > 0) {
            deleteByLots(searchModel.getSearchResults());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleAnnexesDecisionService#read(java.lang.String)
     */
    @Override
    public SimpleAnnexesDecision read(String idAnnexesDecision) throws JadePersistenceException, DecisionException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleAnnexesDecisionService
     * #search(ch.globaz.pegasus.business.models.decision .SimpleAnnexesDecisionSearch)
     */
    @Override
    public SimpleAnnexesDecisionSearch search(SimpleAnnexesDecisionSearch annexesDecisionSearch)
            throws JadePersistenceException, DecisionException {
        if (annexesDecisionSearch == null) {
            throw new DecisionException("Unable to search annexesDecision, the search model passed is null!");
        }
        return (SimpleAnnexesDecisionSearch) JadePersistenceManager.search(annexesDecisionSearch);
    }
}
