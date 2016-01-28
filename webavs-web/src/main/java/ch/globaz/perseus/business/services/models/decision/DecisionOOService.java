package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.decision.DecisionOO;
import ch.globaz.perseus.business.models.decision.DecisionOOSearchModel;

public interface DecisionOOService extends JadeApplicationService {

    public int count(DecisionOOSearchModel search) throws DecisionException, JadePersistenceException;

    public DecisionOO create(DecisionOO decisionOO) throws JadePersistenceException, DecisionException;

    public DecisionOO delete(DecisionOO decisionOO) throws JadePersistenceException, DecisionException;

    public DecisionOO read(String idDecision) throws JadePersistenceException, DecisionException,
            SituationFamilleException, JadeApplicationServiceNotAvailableException, CreancierException;

    public DecisionOOSearchModel search(DecisionOOSearchModel searchModel) throws JadePersistenceException,
            DecisionException;

    public DecisionOO update(DecisionOO decisionOO) throws JadePersistenceException, DecisionException;
}
