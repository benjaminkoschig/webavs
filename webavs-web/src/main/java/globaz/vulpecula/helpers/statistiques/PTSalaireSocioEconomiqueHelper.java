package globaz.vulpecula.helpers.statistiques;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.statistiques.PTSalaireSocioEconomiqueViewBean;
import ch.globaz.vulpecula.process.statistiques.SalaireSocioEconomiqueProcess;

public class PTSalaireSocioEconomiqueHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTSalaireSocioEconomiqueViewBean vb = (PTSalaireSocioEconomiqueViewBean) viewBean;
            SalaireSocioEconomiqueProcess process = new SalaireSocioEconomiqueProcess();

            process.setEMailAddress(vb.getEmail());
            process.setPeriodeDebut(vb.getPeriodeDebut());
            process.setPeriodeFin(vb.getPeriodeFin());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
