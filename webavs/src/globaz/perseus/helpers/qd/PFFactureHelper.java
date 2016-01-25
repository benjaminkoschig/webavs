package globaz.perseus.helpers.qd;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.perseus.vb.qd.PFFactureViewBean;

public class PFFactureHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        if (viewBean instanceof PFFactureViewBean) {
            ((PFFactureViewBean) viewBean).init(session);
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

    }

    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        super.beforeExecute(viewBean, action, session);
        if (viewBean instanceof PFFactureViewBean) {

            if (((PFFactureViewBean) viewBean).getModificationFacture()) {
                ((PFFactureViewBean) viewBean).deleteFactureAModifier();
                ((PFFactureViewBean) viewBean).add();
            }
        }
    }

}
