package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.SimpleCopieDecision;
import ch.globaz.perseus.business.services.models.decision.SimpleCopieDecisionService;
import ch.globaz.perseus.businessimpl.checkers.decision.SimpleCopieDecisionChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleCopieDecisionServiceImpl extends PerseusAbstractServiceImpl implements SimpleCopieDecisionService {

    @Override
    public SimpleCopieDecision create(SimpleCopieDecision simpleCopieDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleCopieDecision == null) {
            throw new DecisionException("Unable to create a SimpleCopieDecision, the model passed is null");
        }
        SimpleCopieDecisionChecker.checkForCreate(simpleCopieDecision);
        return (SimpleCopieDecision) JadePersistenceManager.add(simpleCopieDecision);
    }

    @Override
    public SimpleCopieDecision delete(SimpleCopieDecision simpleCopieDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleCopieDecision == null) {
            throw new DecisionException("Unable to delete a SimpleCopieDecision, the model passed is null");
        }
        if (simpleCopieDecision.isNew()) {
            throw new DecisionException("Unable to delete a SimpleCopieDecision, the model passed is new!");
        }
        SimpleCopieDecisionChecker.checkForDelete(simpleCopieDecision);
        return (SimpleCopieDecision) JadePersistenceManager.delete(simpleCopieDecision);
    }

    @Override
    public SimpleCopieDecision read(String idCopieDecision) throws JadePersistenceException, DecisionException {
        if (idCopieDecision == null) {
            throw new DecisionException("Unable to read a SimpleCopieDecision, the model passed is null!");
        }
        SimpleCopieDecision simpleCopieDecision = new SimpleCopieDecision();
        simpleCopieDecision.setId(idCopieDecision);
        return (SimpleCopieDecision) JadePersistenceManager.read(simpleCopieDecision);
    }

    @Override
    public SimpleCopieDecision update(SimpleCopieDecision simpleCopieDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleCopieDecision == null) {
            throw new DecisionException("Unable to update a SimpleCopieDecision, the model passed is null !");
        }
        if (simpleCopieDecision.isNew()) {
            throw new DecisionException("Unable to update a SimpleCopieDecision, the model passed is new!");
        }
        SimpleCopieDecisionChecker.checkForUpdate(simpleCopieDecision);
        return (SimpleCopieDecision) JadePersistenceManager.update(simpleCopieDecision);
    }

}
