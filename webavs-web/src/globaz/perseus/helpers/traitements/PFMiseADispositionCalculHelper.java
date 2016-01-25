package globaz.perseus.helpers.traitements;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.traitements.PFMiseADispositionCalculProcess;
import globaz.perseus.vb.traitements.PFMiseADispositionCalculViewBean;

public class PFMiseADispositionCalculHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PFMiseADispositionCalculProcess process = new PFMiseADispositionCalculProcess();
        process.setSession((BSession) session);
        process.setAdresseMail(((PFMiseADispositionCalculViewBean) viewBean).getAdresseMail());
        try {
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage("Unable to start........");
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
