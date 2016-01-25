package globaz.perseus.helpers.creancier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.perseus.vb.creancier.PFCreancierViewBean;

public class PFCreancierHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        if (viewBean instanceof PFCreancierViewBean) {
            ((PFCreancierViewBean) viewBean).init();
        }
    }

}
