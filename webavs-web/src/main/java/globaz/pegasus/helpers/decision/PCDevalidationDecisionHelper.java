package globaz.pegasus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.decision.PCDevalidationDecisionViewBean;

public class PCDevalidationDecisionHelper extends PegasusHelper {

    private FWViewBeanInterface _devaliderDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDevalidationDecisionViewBean) viewBean).devalideDecision();
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("devalider")) {
                viewBean = _devaliderDecision(viewBean, action, session);
            } else {
                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            putTransactionInError(viewBean, e);
        }
        return viewBean;
    }

}
