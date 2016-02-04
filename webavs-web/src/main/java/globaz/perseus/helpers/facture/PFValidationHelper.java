package globaz.perseus.helpers.facture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.facture.PFValidationProcess;
import globaz.perseus.vb.facture.PFValidationViewBean;
import java.util.Arrays;
import java.util.List;

public class PFValidationHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("actionValider".equals(action.getActionPart())) {
            if (viewBean instanceof PFValidationViewBean) {
                PFValidationProcess process = new PFValidationProcess();
                process.setSession((BSession) session);
                process.setAdresseMail(((PFValidationViewBean) viewBean).getAdresseMail());
                String stringIds = ((PFValidationViewBean) viewBean).getFactureSelected();
                if (stringIds != null && !stringIds.isEmpty()) {
                    List<String> ids = Arrays.asList(stringIds.split(","));
                    process.setListIdFacture(ids);
                    try {
                        BProcessLauncher.startJob(process);
                    } catch (Exception e) {
                        e.printStackTrace();
                        viewBean.setMessage("Unable to start........");
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                } else {
                    viewBean.setMessage("selection is empty");
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

            } else {
                super._start(viewBean, action, session);
            }
        }
        return viewBean;
    }
}
