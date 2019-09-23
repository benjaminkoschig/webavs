package globaz.orion.helpers.swissdec;

import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.xmlns.eb.dan.DanStatutEnum;
import ch.globaz.xmlns.eb.pucs.PucsStatusEnum;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.orion.vb.swissdec.EBPucsValidationDetailViewBean;
import ch.globaz.orion.service.EBPucsFileService;
import com.sun.star.uno.RuntimeException;

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
        try {
            vb.retrieve();
            EBPucsFileService.aTraiter(vb.getCurrentId(), (BSession) session);
        } catch (Exception e) {
            throw new RuntimeException("Unknown error", e);
        }
    }

    private void executeRefuser(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;
        try {
            vb.retrieve();
            EBPucsFileService.rejeter(vb.getCurrentId(), (BSession) session);
            if (vb.getPucsFile().getProvenance().isPucs()) {
                PucsServiceImpl.updateStatusPucs(vb.getPucsFile().getFilename(), PucsStatusEnum.REJECTED, (BSession) session);
            } else if (vb.getPucsFile().getProvenance().isDan()) {
                DanServiceImpl.updateStatusDan(vb.getPucsFile().getFilename(), DanStatutEnum.REJECTED, (BSession) session);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unknown error", e);
        }
    }

    private void executeAnnulerRefuser(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;
        try {
            vb.retrieve();
            EBPucsFileService.annulerRejeter(vb.getCurrentId(), (BSession) session);
            if (vb.getPucsFile().getProvenance().isPucs()) {
                EBPucsFileService.annulerRejeterPucsDAN(vb.getCurrentId(), (BSession) session);
                PucsServiceImpl.updateStatusPucs(vb.getPucsFile().getFilename(), PucsStatusEnum.TO_HANDLE, (BSession) session);
            } else if (vb.getPucsFile().getProvenance().isDan()) {
                EBPucsFileService.annulerRejeterPucsDAN(vb.getCurrentId(), (BSession) session);
                DanServiceImpl.updateStatusDan(vb.getPucsFile().getFilename(), DanStatutEnum.EN_TRAITEMENT, (BSession) session);
            } else {
                EBPucsFileService.annulerRejeter(vb.getCurrentId(), (BSession) session);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unknown error", e);
        }
    }
}
