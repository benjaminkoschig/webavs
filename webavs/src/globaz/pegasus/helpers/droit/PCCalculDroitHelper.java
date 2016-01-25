package globaz.pegasus.helpers.droit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.droit.PCCalculDroitViewBean;

public class PCCalculDroitHelper extends PegasusHelper {

    private FWViewBeanInterface _calculeDroit(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCCalculDroitViewBean) viewBean).calculeDroit();
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("calculer")) {
                viewBean = _calculeDroit(viewBean, action, session);
            } else {
                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            putTransactionInError(viewBean, e);
        }

        return viewBean;
    }

}
