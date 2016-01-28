package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pavo.db.process.CIControleCIInactifInvalideViewBean;
import globaz.pavo.process.CIControleCIInactifInvalideProcess;

/**
 * 
 * @author: mmo
 */
public class CIControleCIInactifInvalideHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {

            CIControleCIInactifInvalideViewBean vb = (CIControleCIInactifInvalideViewBean) viewBean;

            CIControleCIInactifInvalideProcess controleCIInactifInvalide = new CIControleCIInactifInvalideProcess();
            controleCIInactifInvalide.setSession((BSession) session);
            controleCIInactifInvalide.setEMailAddress(vb.getEmailAddress());

            BProcessLauncher.startJob(controleCIInactifInvalide);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
