package globaz.osiris.helpers.interet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.osiris.db.process.CAInteretMoratoireManuelViewBean;

public class CASimulerInteretHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(FWViewBeanInterface viewBean, FWAction action, BISession
     *      session)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            ((CAInteretMoratoireManuelViewBean) viewBean).executeProcess();
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        return viewBean;
    }
}
