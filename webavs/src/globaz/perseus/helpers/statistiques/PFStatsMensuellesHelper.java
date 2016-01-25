package globaz.perseus.helpers.statistiques;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.statistiques.PFStatsMensuellesProcess;
import globaz.perseus.vb.statistiques.PFStatsMensuellesViewBean;

public class PFStatsMensuellesHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // TODO Auto-generated method stub

        try {
            PFStatsMensuellesViewBean vb = (PFStatsMensuellesViewBean) viewBean;

            PFStatsMensuellesProcess process = new PFStatsMensuellesProcess();
            process.setDateDebut(vb.getDateDebut());
            process.setDateFin(vb.getDateFin());
            process.setMailAd(vb.geteMailAdresse());
            process.setSession((BSession) session);
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage("Unable to start : " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
