package globaz.pegasus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.decision.PCDecisionRefusViewBean;

public class PCDecisionRefusHelper extends PegasusHelper {

    private FWViewBeanInterface _detaillerDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDecisionRefusViewBean) viewBean).detail();
        return viewBean;
    }

    private FWViewBeanInterface _prevaliderDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDecisionRefusViewBean) viewBean).prevalider();
        return viewBean;
    };

    private FWViewBeanInterface _validerDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDecisionRefusViewBean) viewBean).valider();
        return viewBean;
    };

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("detail")) {
                viewBean = _detaillerDecision(viewBean, action, session);
            } else if (actionPart.equals("prevalider")) {
                viewBean = _prevaliderDecision(viewBean, action, session);
            } else if (actionPart.equals("valider")) {
                viewBean = _validerDecision(viewBean, action, session);
            } else {
                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            putTransactionInError(viewBean, e);
        }

        return viewBean;
    }
}
