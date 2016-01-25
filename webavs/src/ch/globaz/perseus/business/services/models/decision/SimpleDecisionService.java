package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.SimpleDecision;
import ch.globaz.perseus.business.models.decision.SimpleDecisionSearchModel;

/**
 * 
 * @author MBO
 * 
 */

public interface SimpleDecisionService extends JadeApplicationService {

    public SimpleDecision create(SimpleDecision simpleDecision) throws JadePersistenceException, DecisionException;

    public SimpleDecision delete(SimpleDecision simpleDecision) throws JadePersistenceException, DecisionException;

    public int delete(SimpleDecisionSearchModel simpleDecisionSearchModel) throws JadePersistenceException,
            DecisionException;

    public SimpleDecision read(String idDecision) throws JadePersistenceException, DecisionException;

    public SimpleDecision update(SimpleDecision simpleDecision) throws JadePersistenceException, DecisionException;

}
