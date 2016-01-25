package globaz.pegasus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.decision.PCPrepDecisionRefusViewBean;

public class PCPrepDecisionRefusHelper extends PegasusHelper {

    private FWViewBeanInterface _preparerDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCPrepDecisionRefusViewBean) viewBean).preparer();
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("preparer")) {
                viewBean = _preparerDecision(viewBean, action, session);
            } else {
                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            putTransactionInError(viewBean, e);
        }
        return viewBean;
    }
}
