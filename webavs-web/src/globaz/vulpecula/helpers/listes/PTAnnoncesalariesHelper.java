package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTAnnoncesalariesViewBean;
import ch.globaz.vulpecula.process.annoncesalaries.AnnonceSalarieProcess;

public class PTAnnoncesalariesHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTAnnoncesalariesViewBean vb = (PTAnnoncesalariesViewBean) viewBean;
            AnnonceSalarieProcess process = new AnnonceSalarieProcess();
            process.setEMailAddress(vb.getEmail());
            process.setMiseAJour(vb.isMiseAJour());
            process.setDate(vb.getDate());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
