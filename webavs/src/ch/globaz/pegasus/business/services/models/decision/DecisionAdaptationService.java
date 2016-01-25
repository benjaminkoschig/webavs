package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;

public interface DecisionAdaptationService extends JadeApplicationService {
    public List<SimpleDecisionHeader> preparerValiderDecision(String idDemande, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;
}
