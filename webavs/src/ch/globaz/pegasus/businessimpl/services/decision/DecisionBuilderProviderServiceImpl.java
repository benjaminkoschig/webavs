package ch.globaz.pegasus.businessimpl.services.decision;

import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.business.services.decision.DecisionBuilder;
import ch.globaz.pegasus.business.services.decision.DecisionBuilderProviderService;
import ch.globaz.pegasus.business.services.demande.DemandeBuilder;
import ch.globaz.pegasus.businessimpl.services.demande.SingleBillagBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions.DecisionApresCalculBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions.DecisionRefusBuilder;

public class DecisionBuilderProviderServiceImpl implements DecisionBuilderProviderService {

    public DemandeBuilder getBillagBuilder() {
        return new SingleBillagBuilder();
    }

    @Override
    public DecisionBuilder getBuilderFor(DecisionTypes typeDecision) {
        switch (typeDecision) {
            case DECISION_APRES_CALCUL:
                return new DecisionApresCalculBuilder();

            case DECISION_REFUS:
                return new DecisionRefusBuilder();

        }
        return null;
    }

}
