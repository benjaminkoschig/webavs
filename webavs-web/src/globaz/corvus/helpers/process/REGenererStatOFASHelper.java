package globaz.corvus.helpers.process;

import globaz.corvus.process.REGenererStatOFASProcess;
import globaz.corvus.vb.process.REGenererStatOFASViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererStatOFASHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            REGenererStatOFASViewBean glabViewBean = (REGenererStatOFASViewBean) viewBean;

            REGenererStatOFASProcess process = new REGenererStatOFASProcess();
            process.setSession((BSession) session);
            process.setEmailAdresse(glabViewBean.getEMailAddress());
            process.setAnneeStatistiqueOFAS(glabViewBean.getAnneeStatistique());

            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}