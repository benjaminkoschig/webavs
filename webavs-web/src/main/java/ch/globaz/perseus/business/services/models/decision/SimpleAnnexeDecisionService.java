package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecision;

/**
 * 
 * @author MBO
 * 
 */

public interface SimpleAnnexeDecisionService extends JadeApplicationService {

    public SimpleAnnexeDecision create(SimpleAnnexeDecision simpleAnnexeDecision) throws JadePersistenceException,
            DecisionException;

    public SimpleAnnexeDecision delete(SimpleAnnexeDecision simpleAnnexeDecision) throws JadePersistenceException,
            DecisionException;

    public SimpleAnnexeDecision read(String idAnnexeDecision) throws JadePersistenceException, DecisionException;

    public SimpleAnnexeDecision update(SimpleAnnexeDecision simpleAnnexeDecision) throws JadePersistenceException,
            DecisionException;

}
