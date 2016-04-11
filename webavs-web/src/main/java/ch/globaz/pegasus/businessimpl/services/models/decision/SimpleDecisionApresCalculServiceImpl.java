/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionApresCalculService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionApresCalculChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionApresCalculServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDecisionApresCalculService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionApresCalculService
     * #count(ch.globaz.pegasus.business.models.decision .SimpleDecisionApresCalculSearch)
     */
    @Override
    public int count(SimpleDecisionApresCalculSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count simpleDecisionsApresCalcul, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionApresCalculService
     * #create(ch.globaz.pegasus.business.models. decision.SimpleDecisionApresCalculSearch)
     */
    @Override
    public SimpleDecisionApresCalcul create(SimpleDecisionApresCalcul simpleDecision) throws DecisionException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simpleDecision == null) {
            throw new DecisionException("Unable to create simpleDecisionsApresCalcul, the search model passed is null!");
        }
        if (JadeStringUtil.isEmpty(simpleDecision.getDateProchainPaiement())) {
            try {
                simpleDecision.setDateProchainPaiement(PegasusServiceLocator.getPmtMensuelService()
                        .getDateProchainPmt());
            } catch (PmtMensuelException e) {
                throw new DecisionException("Unable to get the date for the next payment", e);
            }
        }

        SimpleDecisionApresCalculChecker.checkForCreate(simpleDecision);

        return (SimpleDecisionApresCalcul) JadePersistenceManager.add(simpleDecision);

    }

    @Override
    public void delete(List<String> idsDecisionHeader) throws DecisionException, JadePersistenceException {
        if (idsDecisionHeader == null) {
            throw new DecisionException("Unable to delete idsDecisionHeader, the model passed is null!");
        }
        SimpleDecisionApresCalculSearch searchModel = new SimpleDecisionApresCalculSearch();
        searchModel.setForInIdDecisionHeader(idsDecisionHeader);
        JadePersistenceManager.delete(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionApresCalculService
     * #delete(ch.globaz.pegasus.business.models. decision.SimpleDecisionApresCalculSearch)
     */
    @Override
    public SimpleDecisionApresCalcul delete(SimpleDecisionApresCalcul decision) throws JadePersistenceException,
            DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to create simpleDecisionsApresCalcul, the search model passed is null!");
        }
        SimpleDecisionApresCalculChecker.checkForDelete(decision);
        return (SimpleDecisionApresCalcul) JadePersistenceManager.delete(decision);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionApresCalculService#read(java.lang.String)
     */
    @Override
    public SimpleDecisionApresCalcul read(String idDecision) throws JadePersistenceException, DecisionException {
        SimpleDecisionApresCalcul simpleDecisionApresCalcul = new SimpleDecisionApresCalcul();
        simpleDecisionApresCalcul.setId(idDecision);
        return (SimpleDecisionApresCalcul) JadePersistenceManager.read(simpleDecisionApresCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionApresCalculService
     * #search(ch.globaz.pegasus.business.models. decision.SimpleDecisionApresCalculSearch)
     */
    @Override
    public SimpleDecisionApresCalculSearch search(SimpleDecisionApresCalculSearch decisionSearch)
            throws JadePersistenceException, DecisionException {
        if (decisionSearch == null) {
            throw new DecisionException("Unable to search simpleDecisionApresCalcul, the search model passed is null!");
        }
        return (SimpleDecisionApresCalculSearch) JadePersistenceManager.search(decisionSearch);
    }

    @Override
    public SimpleDecisionApresCalcul readByIdDecisionHeader(String idDecisionHeader) throws DecisionException,
            JadePersistenceException {
        SimpleDecisionApresCalculSearch search = new SimpleDecisionApresCalculSearch();
        search.setForIdDecisionHeader(idDecisionHeader);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = search(search);
        SimpleDecisionApresCalcul decisionApresCalcul = null;
        if (search.getSize() == 1) {
            decisionApresCalcul = (SimpleDecisionApresCalcul) search.getSearchResults()[0];
        } else if (search.getSize() > 1) {
            throw new DecisionException("Too many decisionApresCalcul found with this idDecisionHeader: "
                    + idDecisionHeader);
        }
        return decisionApresCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionApresCalculService
     * #update(ch.globaz.pegasus.business.models. decision.SimpleDecisionApresCalcul)
     */
    @Override
    public SimpleDecisionApresCalcul update(SimpleDecisionApresCalcul decision) throws JadePersistenceException,
            DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to update simpleDecisionApresCalcul, the search model passed is null!");
        }
        SimpleDecisionApresCalculChecker.checkForUpdate(decision);

        return (SimpleDecisionApresCalcul) JadePersistenceManager.update(decision);
    }

}
