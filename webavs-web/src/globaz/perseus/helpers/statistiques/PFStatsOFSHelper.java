package globaz.perseus.helpers.statistiques;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.statistiques.PFStatsOFSProcess;
import globaz.perseus.vb.statistiques.PFStatsOFSViewBean;

public class PFStatsOFSHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // TODO Auto-generated method stub

        try {
            PFStatsOFSViewBean vb = (PFStatsOFSViewBean) viewBean;
            PFStatsOFSProcess process = new PFStatsOFSProcess();
            process.setAdMail(vb.geteMailAdresse());
            process.setAnneeStat(vb.getAnneeEnquete());
            process.setSession((BSession) session);
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage("Unable to start : " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
