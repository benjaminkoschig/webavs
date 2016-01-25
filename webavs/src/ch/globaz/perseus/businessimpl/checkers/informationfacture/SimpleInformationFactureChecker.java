package ch.globaz.perseus.businessimpl.checkers.informationfacture;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.informationfacture.SimpleInformationFacture;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;
import ch.globaz.perseus.businessimpl.checkers.decision.SimpleDecisionChecker;

public class SimpleInformationFactureChecker extends PerseusAbstractChecker {

    public static void checkForCreate(SimpleInformationFacture informationFacture) {
        SimpleInformationFactureChecker.checkMandatory(informationFacture);
    }

    public static void checkForUpdate(SimpleInformationFacture informationFacture) {
        SimpleInformationFactureChecker.checkMandatory(informationFacture);
    }

    private static void checkMandatory(SimpleInformationFacture informatinFacture) {

        if (JadeStringUtil.isIntegerEmpty(informatinFacture.getDate())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.informationfacture.informationFacture.date.mandatory");
        }
        if (JadeStringUtil.isEmpty(informatinFacture.getDescription())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.informationfacture.informationFacture.description.mandatory");
        }
    }

}
