package globaz.perseus.helpers.qd;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.perseus.vb.qd.PFDetailfactureViewBean;

public class PFDetailfactureHelper extends FWHelper {

    private FWViewBeanInterface _actionValider(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PFDetailfactureViewBean) viewBean).actionValider();
        return viewBean;
    }

    private FWViewBeanInterface _actionRestituer(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PFDetailfactureViewBean) viewBean).actionRestituer();
        return viewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("actionValider")) {
                viewBean = _actionValider(viewBean, action, session);
            }
            if (actionPart.equals("actionRestituer")) {
                viewBean = _actionRestituer(viewBean, action, session);
            }
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            e.printStackTrace();
            // viewBean.setMessage(e.toString() + " : " + ((JadeApplicationException) e).getCause().getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
