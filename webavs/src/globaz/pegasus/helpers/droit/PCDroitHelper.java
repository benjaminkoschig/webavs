package globaz.pegasus.helpers.droit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.droit.PCDroitViewBean;

public class PCDroitHelper extends PegasusHelper {

    private FWViewBeanInterface _modifierAnnonce(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDroitViewBean) viewBean).modifierDateAnnonce();
        return viewBean;
    }

    private FWViewBeanInterface _synchronize(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDroitViewBean) viewBean).synchronizeMembresFamille();
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("synchroniser")) {
                viewBean = _synchronize(viewBean, action, session);
            } else if (actionPart.equals("modifierDateAnnonce")) {
                viewBean = _modifierAnnonce(viewBean, action, session);
            } else {

                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            putTransactionInError(viewBean, e);
        }
        return viewBean;
    }

}
