package globaz.perseus.helpers.impotsource;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.impotsource.PFGenererListeImpotSourceProcess;
import globaz.perseus.vb.impotsource.PFListeImpotSourceViewBean;

public class PFListeImpotSourceHelper extends FWHelper {
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        if (viewBean instanceof PFListeImpotSourceViewBean) {
            ((PFListeImpotSourceViewBean) viewBean).init();
        }
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof PFListeImpotSourceViewBean) {
            try {

                PFGenererListeImpotSourceProcess process = new PFGenererListeImpotSourceProcess();
                process.setSession((BSession) session);
                process.setAdMail(((PFListeImpotSourceViewBean) viewBean).geteMailAdresse(session.getUserEMail()));
                process.setAnneeLC(((PFListeImpotSourceViewBean) viewBean).getAnneeGenerationListeCorrective());
                process.setCsTypeListe(((PFListeImpotSourceViewBean) viewBean).getTypeListe());
                process.setIdPeriode(((PFListeImpotSourceViewBean) viewBean).getIdPeriode());
                process.setNumeroDebiteur(((PFListeImpotSourceViewBean) viewBean).getNumeroDebiteur());

                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMessage("Unable to start........");
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }
    }
}
