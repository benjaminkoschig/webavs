package globaz.perseus.helpers.traitements;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.traitements.PFAttributionNumeroOFSProcess;
import globaz.perseus.vb.traitements.PFAttributionNumeroOFSViewBean;

public class PFAttributionNumeroOFSHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PFAttributionNumeroOFSProcess process = new PFAttributionNumeroOFSProcess();
        process.setSession((BSession) session);
        process.setAdresseMail(((PFAttributionNumeroOFSViewBean) viewBean).getAdresseMail());
        try {
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage("Unable to start........");
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
