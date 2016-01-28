package ch.globaz.prestation.businessimpl.services.models.echeance;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.prestation.business.models.echance.SimpleEcheance;

public class SimpleEcheanceChecker {
    public static void checkForCreate(SimpleEcheance echeance) {
        SimpleEcheanceChecker.checkMandatory(echeance);
    }

    public static void checkForUpdate(SimpleEcheance echeance) {
        SimpleEcheanceChecker.checkMandatory(echeance);
    }

    private static void checkMandatory(SimpleEcheance echeance) {

        if (JadeStringUtil.isEmpty(echeance.getIdExterne())) {
            JadeThread.logError(echeance.getClass().getName(), "presation.echeance.idExterne.mandatory");
        }
        if (JadeStringUtil.isEmpty(echeance.getCsDomaine())) {
            JadeThread.logError(echeance.getClass().getName(), "presation.demande.csDomaine.mandatory");
        }
        if (JadeStringUtil.isEmpty(echeance.getCsEtat())) {
            JadeThread.logError(echeance.getClass().getName(), "presation.demande.csEtat.mandatory");
        }
        if (JadeStringUtil.isEmpty(echeance.getCsTypeEcheance())) {
            JadeThread.logError(echeance.getClass().getName(), "presation.demande.csType.mandatory");
        }
        if (JadeStringUtil.isEmpty(echeance.getDateEcheance())) {
            JadeThread.logError(echeance.getClass().getName(), "presation.demande.dateEcheance.mandatory");
        }
    }
}
