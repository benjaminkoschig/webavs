package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.CopieDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleCopieDecisionSearchModel;

/**
 * 
 * @author MBO
 * 
 */

public interface CopieDecisionService extends JadeApplicationService {

    public int count(CopieDecisionSearchModel search) throws DecisionException, JadePersistenceException;

    public CopieDecision create(CopieDecision copieDecision) throws JadePersistenceException, DecisionException;

    public CopieDecision delete(CopieDecision copieDecision) throws JadePersistenceException, DecisionException;

    public int delete(SimpleCopieDecisionSearchModel searchModel) throws JadePersistenceException, DecisionException;

    public void deleteByLots(JadeAbstractModel[] searchResults) throws JadePersistenceException, DecisionException;

    public CopieDecision read(String idCopieDecision) throws JadePersistenceException, DecisionException;

    public CopieDecisionSearchModel search(CopieDecisionSearchModel searchModel) throws JadePersistenceException,
            DecisionException;

    public CopieDecision update(CopieDecision copieDecision) throws JadePersistenceException, DecisionException;

}
