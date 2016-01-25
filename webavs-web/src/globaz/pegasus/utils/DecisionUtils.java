package globaz.pegasus.utils;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class DecisionUtils {

    /**
     * Check si la date de decision est comprise entre la date de dernier paiement (egal ou plus grand) et la date de
     * prochain paiement (egal ou plus petite)
     * 
     * @param dateDecision
     * @return l'état du test, comprise dans la période ddp -- date -- dpp
     * @throws DecisionException
     */
    public static void checkIfDateDecisionCompriseEntreDernierEtProchainPaiement(String dateDecision)
            throws DecisionException {

        String datePP;// prochain paiement
        String dateDP;// Dernier paiement
        try {
            datePP = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            dateDP = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        } catch (PmtMensuelException e) {
            throw new DecisionException("Unable to optain the date prochainPmt", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("The service for prochainPmt is not available", e);
        }

        // date après prochain paiement
        if (JadeDateUtil.isDateAfter(dateDecision, "01." + datePP)) {
            String[] date = { "01." + datePP };
            JadeThread.logError("DecisionSuppressionChecker", "pegasus.validationDecision.dateProchainPaimentAfter",
                    date);
        }
        if (JadeDateUtil.isDateBefore(dateDecision, "01." + dateDP)
                || JadeDateUtil.isDateBefore(dateDecision, "01." + dateDP)) {
            String[] date = { "01." + dateDP };
            JadeThread.logError("DecisionSuppressionChecker", "pegasus.validationDecision.dateDernierPaimentBefore",
                    date);
        }

    }
}
