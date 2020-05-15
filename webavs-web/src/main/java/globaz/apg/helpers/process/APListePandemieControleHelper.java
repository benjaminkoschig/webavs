package globaz.apg.helpers.process;

import globaz.apg.process.APListePandemieControleProcess;
import globaz.apg.vb.process.APListePandemieControleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

public class APListePandemieControleHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APListePandemieControleViewBean vb = (APListePandemieControleViewBean) viewBean;

        APListePandemieControleProcess process = new APListePandemieControleProcess((BSession) session);
        process.setEMailAddress(vb.getEMailAddress());
        process.start();
    }
}
