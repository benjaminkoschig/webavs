package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.AnnexeDecision;
import ch.globaz.perseus.business.models.decision.AnnexeDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecisionSearchModel;

/**
 * 
 * @author MBO
 * 
 */

public interface AnnexeDecisionService extends JadeApplicationService {

    public int count(AnnexeDecisionSearchModel search) throws DecisionException, JadePersistenceException;

    public AnnexeDecision create(AnnexeDecision annexeDecision) throws JadePersistenceException, DecisionException;

    public AnnexeDecision delete(AnnexeDecision annexeDecision) throws JadePersistenceException, DecisionException;

    public int delete(SimpleAnnexeDecisionSearchModel searchModel) throws JadePersistenceException, DecisionException;

    public void deleteByLots(JadeAbstractModel[] searchResults) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public AnnexeDecision read(String idAnnexeDecision) throws JadePersistenceException, DecisionException;

    public AnnexeDecisionSearchModel search(AnnexeDecisionSearchModel searchModel) throws JadePersistenceException,
            DecisionException;

    public AnnexeDecision update(AnnexeDecision annexeDecision) throws JadePersistenceException, DecisionException;

}
