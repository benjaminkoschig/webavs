package ch.globaz.pegasus.business.services.decision;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;

public interface DecisionBuilderProviderService extends JadeApplicationService {

    public DecisionBuilder getBuilderFor(DecisionTypes typeDecision);
}
