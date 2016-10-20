package globaz.orion.helpers.sdd;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.orion.process.EBTreatSaisieDecompte;
import globaz.orion.vb.sdd.EBSaisieDecompteViewBean;

public class EBSaisieDecompteHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBSaisieDecompteViewBean vb = (EBSaisieDecompteViewBean) viewBean;

        try {

            EBTreatSaisieDecompte process = new EBTreatSaisieDecompte();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
