package globaz.orion.helpers.acompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.orion.process.EBTreatPac;
import globaz.orion.vb.acompte.EBPrevisionAcompteViewBean;

/**
 * @author BJO
 * 
 */
public class EBPrevisionAcompteHelper extends FWHelper {

    public EBPrevisionAcompteHelper() {
        // Do nothing
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBPrevisionAcompteViewBean anViewBean = (EBPrevisionAcompteViewBean) viewBean;

        try {
            EBTreatPac process = new EBTreatPac();
            process.setSession((BSession) session);
            process.setEMailAddress(anViewBean.getEmail());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            anViewBean.setMessage(e.getMessage());
            anViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
