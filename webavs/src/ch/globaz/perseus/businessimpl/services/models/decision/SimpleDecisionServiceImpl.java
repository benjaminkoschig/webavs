package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.SimpleDecision;
import ch.globaz.perseus.business.models.decision.SimpleDecisionSearchModel;
import ch.globaz.perseus.business.services.models.decision.SimpleDecisionService;
import ch.globaz.perseus.businessimpl.checkers.decision.SimpleDecisionChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleDecisionServiceImpl extends PerseusAbstractServiceImpl implements SimpleDecisionService {

    @Override
    public SimpleDecision create(SimpleDecision simpleDecision) throws JadePersistenceException, DecisionException {
        if (simpleDecision == null) {
            throw new DecisionException("Unable to create a simple Decision, the model passed is null");
        }
        SimpleDecisionChecker.checkForCreate(simpleDecision);
        return (SimpleDecision) JadePersistenceManager.add(simpleDecision);
    }

    @Override
    public SimpleDecision delete(SimpleDecision simpleDecision) throws JadePersistenceException, DecisionException {
        if (simpleDecision == null) {
            throw new DecisionException("Unable to delete a simple Decision, the model passed is null");
        }
        if (simpleDecision.isNew()) {
            throw new DecisionException("Unable to delete a simple Decision, the model passed is new!");
        }
        SimpleDecisionChecker.checkForDelete(simpleDecision);
        return (SimpleDecision) JadePersistenceManager.delete(simpleDecision);
    }

    @Override
    public int delete(SimpleDecisionSearchModel simpleDecisionSearchModel) throws JadePersistenceException,
            DecisionException {
        if (simpleDecisionSearchModel == null) {
            throw new DecisionException("Unable to delete a simple Decision, the search model passed is null");
        }

        return JadePersistenceManager.delete(simpleDecisionSearchModel);
    }

    @Override
    public SimpleDecision read(String idDecision) throws JadePersistenceException, DecisionException {
        if (idDecision == null) {
            throw new DecisionException("Unable to read a simple Decision, the model passed is null!");
        }
        SimpleDecision simpleDecision = new SimpleDecision();
        simpleDecision.setId(idDecision);
        return (SimpleDecision) JadePersistenceManager.read(simpleDecision);
    }

    @Override
    public SimpleDecision update(SimpleDecision simpleDecision) throws JadePersistenceException, DecisionException {
        if (simpleDecision == null) {
            throw new DecisionException("Unable to update a simple Decision, the model passed is null !");
        }
        if (simpleDecision.isNew()) {
            throw new DecisionException("Unable to update a simple Decision, the model passed is new!");
        }
        SimpleDecisionChecker.checkForUpdate(simpleDecision);
        return (SimpleDecision) JadePersistenceManager.update(simpleDecision);
    }

}
