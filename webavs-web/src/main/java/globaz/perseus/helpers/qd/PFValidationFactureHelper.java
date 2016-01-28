package globaz.perseus.helpers.qd;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.qd.PFValidationFactureProcess;
import globaz.perseus.vb.qd.PFValidationFactureAjaxViewBean;

public class PFValidationFactureHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PFValidationFactureAjaxViewBean) {
            PFValidationFactureProcess process = new PFValidationFactureProcess();
            process.setSession((BSession) session);
            // process.setAdresseMail(((PFValidationFactureAjaxViewBean) viewBean).getMail());

            try {
                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMessage("Unable to start........");
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }
}
