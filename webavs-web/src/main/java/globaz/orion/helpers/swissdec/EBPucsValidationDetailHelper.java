package globaz.orion.helpers.swissdec;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.orion.vb.swissdec.EBPucsValidationDetailViewBean;
import ch.globaz.orion.service.EBPucsFileService;

public class EBPucsValidationDetailHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (action.getActionPart().equals("accepter")) {
            executeAccepter(viewBean, action, session);
        } else if (action.getActionPart().equals("refuser")) {
            executeRefuser(viewBean, action, session);
        } else if (action.getActionPart().equals("annulerRefus")) {
            executeAnnulerRefuser(viewBean, action, session);
        }

        return super.execute(viewBean, action, session);
    }

    private void executeAccepter(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;
        EBPucsFileService.accepter(vb.getCurrentId(), (BSession) session);
    }

    private void executeRefuser(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;
        EBPucsFileService.rejeter(vb.getCurrentId(), (BSession) session);
    }

    private void executeAnnulerRefuser(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;
        EBPucsFileService.annulerRejeter(vb.getCurrentId(), (BSession) session);
    }
}
