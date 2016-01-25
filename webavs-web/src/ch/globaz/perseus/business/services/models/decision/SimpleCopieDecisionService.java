package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.SimpleCopieDecision;

/**
 * 
 * @author MBO
 * 
 */

public interface SimpleCopieDecisionService extends JadeApplicationService {

    public SimpleCopieDecision create(SimpleCopieDecision simpleCopieDecision) throws JadePersistenceException,
            DecisionException;

    public SimpleCopieDecision delete(SimpleCopieDecision simpleCopieDecision) throws JadePersistenceException,
            DecisionException;

    public SimpleCopieDecision read(String idCopie) throws JadePersistenceException, DecisionException;

    public SimpleCopieDecision update(SimpleCopieDecision simpleCopieDecision) throws JadePersistenceException,
            DecisionException;

}
