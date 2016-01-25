package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.DecisionMotif;
import ch.globaz.perseus.business.models.decision.DecisionMotifSearchModel;

/**
 * 
 * @author MBO
 * 
 */

public interface DecisionMotifService extends JadeApplicationService {

    public int count(DecisionMotifSearchModel search) throws DecisionException, JadePersistenceException;

    public DecisionMotif create(DecisionMotif decisionMotif) throws JadePersistenceException, DecisionException;

    public DecisionMotif delete(DecisionMotif decisionMotif) throws JadePersistenceException, DecisionException;

    public DecisionMotif read(String idDecisionMotif) throws JadePersistenceException, DecisionException;

    public DecisionMotifSearchModel search(DecisionMotifSearchModel searchModel) throws JadePersistenceException,
            DecisionException;

    public DecisionMotif update(DecisionMotif decisionMotif) throws JadePersistenceException, DecisionException;

}
