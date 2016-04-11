/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.Date;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeaderSearch;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionHeaderService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionHeaderChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.utils.decision.PCDecisionUIDGeneratorHandler;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public class SimpleDecisionHeaderServiceImpl extends PegasusAbstractServiceImpl implements SimpleDecisionHeaderService {

    /**
     * Compte le nombre d'occurence en base de données
     * 
     * @param search
     * @return int, le nombre d'occurence
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    @Override
    public int count(SimpleDecisionHeaderSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count decisions, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /**
     * @param decision
     * @return
     */
    @Override
    public SimpleDecisionHeader create(SimpleDecisionHeader simpleDecision) throws DecisionException,
            JadePersistenceException {
        if (simpleDecision == null) {
            throw new DecisionException("Unable to create decisions, the search model passed is null!");
        }

        SimpleDecisionHeaderChecker.checkForDateDecisionFormat(simpleDecision.getDateDecision());

        // Set the le no de decision, avec année en paramètre
        simpleDecision.setNoDecision(PCDecisionUIDGeneratorHandler.getNoDecisionForPC(simpleDecision.getDateDecision()
                .substring(6, 10)));

        // Set the preparation Date
        simpleDecision.setDatePreparation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        SimpleDecisionHeaderChecker.checkForCreate(simpleDecision);

        return (SimpleDecisionHeader) JadePersistenceManager.add(simpleDecision);
    }

    @Override
    public void delete(List<String> idsDecisionHeader) throws JadePersistenceException, DecisionException {
        if (idsDecisionHeader == null) {
            throw new DecisionException("Unable to delete idsDecisionHeader, the model passed is null!");
        }

        SimpleDecisionHeaderSearch searchModel = new SimpleDecisionHeaderSearch();
        searchModel.setForInIdDecisionHeader(idsDecisionHeader);
        searchModel = (SimpleDecisionHeaderSearch) JadePersistenceManager.search(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            SimpleDecisionHeaderChecker.checkForDelete((SimpleDecisionHeader) model);
        }
        JadePersistenceManager.delete(searchModel);
    }

    /**
     * @param decision
     * @return
     */
    @Override
    public SimpleDecisionHeader delete(SimpleDecisionHeader decision) throws JadePersistenceException,
            DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to delete decisions, the search model passed is null!");
        }
        SimpleDecisionHeaderChecker.checkForDelete(decision);
        return (SimpleDecisionHeader) JadePersistenceManager.delete(decision);
    }

    /**
     * Charge l'entité en base de données
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    @Override
    public SimpleDecisionHeader read(String idDecision) throws JadePersistenceException, DecisionException {

        SimpleDecisionHeader simpleDecisionHeader = new SimpleDecisionHeader();
        simpleDecisionHeader.setId(idDecision);
        return (SimpleDecisionHeader) JadePersistenceManager.read(simpleDecisionHeader);
    }

    /**
     * Recherche de l'entité en base de données
     * 
     * @param simpleDecisionSearch
     * @return serach, modele de recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    @Override
    public SimpleDecisionHeaderSearch search(SimpleDecisionHeaderSearch decisionSearch)
            throws JadePersistenceException, DecisionException {
        if (decisionSearch == null) {
            throw new DecisionException("Unable to search simpleDecisionHeader, the search model passed is null!");
        }

        return (SimpleDecisionHeaderSearch) JadePersistenceManager.search(decisionSearch);
    }

    /**
     * @param decision
     * @return
     */
    @Override
    public SimpleDecisionHeader update(SimpleDecisionHeader decision) throws JadePersistenceException,
            DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to update simpleDecisionHeader, the search model passed is null!");
        }
        SimpleDecisionHeaderChecker.checkForUpdate(decision);

        return (SimpleDecisionHeader) JadePersistenceManager.update(decision);
    }

    @Override
    public SimpleDecisionHeader updateForPrevalidation(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException {

        if (decision == null) {
            throw new DecisionException(
                    "Unable to update simpleDecisionHeader for prevalidation, the search model passed is null!");
        }
        SimpleDecisionHeaderChecker.checkForUpdate(decision);
        // on set l'état seulement si ok
        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            decision.setCsEtatDecision(IPCDecision.CS_ETAT_PRE_VALIDE);
        }
        return (SimpleDecisionHeader) JadePersistenceManager.update(decision);
    }

    @Override
    public SimpleDecisionHeader updateForValidation(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException {

        if (decision == null) {
            throw new DecisionException(
                    "Unable to update simpleDecisionHeader for prevalidation, the search model passed is null!");
        }
        SimpleDecisionHeaderChecker.checkForUpdate(decision);
        // on set l'état seulement si ok
        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            decision.setCsEtatDecision(IPCDecision.CS_ETAT_DECISION_VALIDE);
        }
        return (SimpleDecisionHeader) JadePersistenceManager.update(decision);
    }

}
