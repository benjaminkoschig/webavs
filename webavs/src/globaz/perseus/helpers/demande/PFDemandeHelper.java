package globaz.perseus.helpers.demande;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.perseus.vb.demande.PFDemandeViewBean;

public class PFDemandeHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        if (viewBean instanceof PFDemandeViewBean) {
            ((PFDemandeViewBean) viewBean).init();
        }
    }

}
