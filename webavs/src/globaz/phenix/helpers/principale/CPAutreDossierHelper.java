package globaz.phenix.helpers.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.db.principale.CPAutreDossierViewBean;

public class CPAutreDossierHelper extends FWHelper {

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (viewBean instanceof CPAutreDossierViewBean) {
            ((CPAutreDossierViewBean) viewBean)._controle();
        } else {
            super._update(viewBean, action, session);
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("afficherDecisions".equals(action.getActionPart()) && viewBean instanceof CPAutreDossierViewBean) {
            try {
                ((CPAutreDossierViewBean) viewBean)._controle();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }
        return super.execute(viewBean, action, session);
    }

}
