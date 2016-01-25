package globaz.perseus.helpers.paiements;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFValidationDecisionHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            if ("activer".equals(action.getActionPart())) {
                PerseusServiceLocator.getPmtMensuelService().activerValidationDecision();
            } else if ("desactiver".equals(action.getActionPart())) {
                PerseusServiceLocator.getPmtMensuelService().desactiverValidationDecision();
            } else {
                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            JadeThread.logError(e.getClass().getName(),
                    "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFValidationDecisionHelper.execute) : "
                            + e.toString());
        }

        return viewBean;
    }
}
