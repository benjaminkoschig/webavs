package globaz.orion.helpers.adi;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.orion.process.EBTreatADI;
import globaz.orion.vb.adi.EBRecapDemandesTransmisesViewBean;

public class EBRecapDemandesTransmisesHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBRecapDemandesTransmisesViewBean vb = (EBRecapDemandesTransmisesViewBean) viewBean;

        try {
            EBTreatADI process = new EBTreatADI();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
