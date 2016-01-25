package globaz.naos.helpers.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.naos.db.ide.AFIdeSyncRegistreViewBean;
import globaz.naos.process.ide.AFIdeSyncRegistreProcess;

public class AFIdeSyncRegistreHelper extends FWHelper {

    public AFIdeSyncRegistreHelper() {
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            AFIdeSyncRegistreViewBean vb = (AFIdeSyncRegistreViewBean) viewBean;

            AFIdeSyncRegistreProcess process = new AFIdeSyncRegistreProcess();

            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmail());
            process.setModeForceAllStatus(vb.getModeForceAllStatus());
            process.start();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
