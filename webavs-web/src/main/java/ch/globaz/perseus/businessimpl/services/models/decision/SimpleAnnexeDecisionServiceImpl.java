package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecision;
import ch.globaz.perseus.business.services.models.decision.SimpleAnnexeDecisionService;
import ch.globaz.perseus.businessimpl.checkers.decision.SimpleAnnexeDecisionChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleAnnexeDecisionServiceImpl extends PerseusAbstractServiceImpl implements SimpleAnnexeDecisionService {

    @Override
    public SimpleAnnexeDecision create(SimpleAnnexeDecision simpleAnnexeDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleAnnexeDecision == null) {
            throw new DecisionException("Unable to create a simpleAnnexeDecision, the model passed is null");
        }
        SimpleAnnexeDecisionChecker.checkForCreate(simpleAnnexeDecision);
        return (SimpleAnnexeDecision) JadePersistenceManager.add(simpleAnnexeDecision);
    }

    @Override
    public SimpleAnnexeDecision delete(SimpleAnnexeDecision simpleAnnexeDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleAnnexeDecision == null) {
            throw new DecisionException("Unable to delete a simpleAnnexeDecision, the model passed is null");
        }
        if (simpleAnnexeDecision.isNew()) {
            throw new DecisionException("Unable to delete a simpleAnnexeDecision, the model passed is new!");
        }
        SimpleAnnexeDecisionChecker.checkForDelete(simpleAnnexeDecision);
        return (SimpleAnnexeDecision) JadePersistenceManager.delete(simpleAnnexeDecision);
    }

    @Override
    public SimpleAnnexeDecision read(String idAnnexeDecision) throws JadePersistenceException, DecisionException {
        if (idAnnexeDecision == null) {
            throw new DecisionException("Unable to read a simpleAnnexeDecision, the model passed is null!");
        }
        SimpleAnnexeDecision simpleAnnexeDecision = new SimpleAnnexeDecision();
        simpleAnnexeDecision.setId(idAnnexeDecision);
        return (SimpleAnnexeDecision) JadePersistenceManager.read(simpleAnnexeDecision);
    }

    @Override
    public SimpleAnnexeDecision update(SimpleAnnexeDecision simpleAnnexeDecision) throws JadePersistenceException,
            DecisionException {
        if (simpleAnnexeDecision == null) {
            throw new DecisionException("Unable to update a simpleAnnexeDecision, the model passed is null !");
        }
        if (simpleAnnexeDecision.isNew()) {
            throw new DecisionException("Unable to update a simpleAnnexeDecision, the model passed is new!");
        }
        SimpleAnnexeDecisionChecker.checkForUpdate(simpleAnnexeDecision);
        return (SimpleAnnexeDecision) JadePersistenceManager.update(simpleAnnexeDecision);
    }

}
