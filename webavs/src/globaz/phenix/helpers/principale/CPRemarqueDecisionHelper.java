package globaz.phenix.helpers.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.phenix.db.principale.CPRemarqueDecisionViewBean;

public class CPRemarqueDecisionHelper extends FWHelper {

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        if (viewBean instanceof CPRemarqueDecisionViewBean) {
            ((CPRemarqueDecisionViewBean) viewBean)._chargerEntete();
        } else {
            super._chercher(viewBean, action, session);
        }
    }
}
