package globaz.perseus.helpers.qd;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.qd.PFValidationFactureProcess;
import globaz.perseus.vb.qd.PFValidationFactureViewBean;
import java.util.Arrays;
import java.util.List;

public class PFValidationFactureHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("actionValider".equals(action.getActionPart())) {
            if (viewBean instanceof PFValidationFactureViewBean) {
                PFValidationFactureProcess process = new PFValidationFactureProcess();
                process.setSession((BSession) session);
                process.setAdresseMail(((PFValidationFactureViewBean) viewBean).getAdresseMail());
                String stringIds = ((PFValidationFactureViewBean) viewBean).getFactureSelected();
                if (stringIds != null && !stringIds.isEmpty()) {
                    List<String> ids = Arrays.asList(stringIds.split(","));
                    process.setListIdFacture(ids);
                }

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
        return viewBean;
    }
}
