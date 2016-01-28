package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.db.communications.CPReglePlausibiliteViewBean;

public class CPReglePlausibiliteHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("configurer".equals(action.getActionPart()) && viewBean instanceof CPReglePlausibiliteViewBean) {
            try {
                ((CPReglePlausibiliteViewBean) viewBean).retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.execute(viewBean, action, session);
    }
}
