package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class ValidationDecisionChecker extends PegasusAbstractChecker {

    public static boolean isPaymentDoneBetweenTheValidation(DecisionApresCalcul decisionApresCalcul)
            throws JadeApplicationServiceNotAvailableException, DecisionException {
        try {
            if (PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt()
                    .equals(decisionApresCalcul.getSimpleDecisionApresCalcul().getDateProchainPaiement())) {
                return false;
            }
        } catch (PmtMensuelException e) {
            throw new DecisionException("Unable to get the next payment", e);
        }
        return true;
    }

}
