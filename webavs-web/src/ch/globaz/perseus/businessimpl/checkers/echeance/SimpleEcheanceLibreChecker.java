package ch.globaz.perseus.businessimpl.checkers.echeance;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.echeance.SimpleEcheanceLibre;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;
import ch.globaz.perseus.businessimpl.checkers.decision.SimpleDecisionChecker;

public class SimpleEcheanceLibreChecker extends PerseusAbstractChecker {

    public static void checkForCreate(SimpleEcheanceLibre echeanceLibre) {
        SimpleEcheanceLibreChecker.checkMandatory(echeanceLibre);
    }

    public static void checkForUpdate(SimpleEcheanceLibre echeanceLibre) {
        SimpleEcheanceLibreChecker.checkMandatory(echeanceLibre);
    }

    private static void checkMandatory(SimpleEcheanceLibre echeanceLibre) {

        if (JadeStringUtil.isIntegerEmpty(echeanceLibre.getDateButoire())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.echeance.simpleecheancelibre.datebutoire.mandatory");
        }
        if (JadeStringUtil.isEmpty(echeanceLibre.getDescription())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.echeance.simpleecheancelibre.description.mandatory");
        }
        if (JadeStringUtil.isEmpty(echeanceLibre.getMotif())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.echeance.simpleecheancelibre.motif.mandatory");
        }
    }

}
