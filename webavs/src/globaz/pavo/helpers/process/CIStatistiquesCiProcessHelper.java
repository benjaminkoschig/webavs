package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.process.CIStatistiquesCiProcessViewBean;
import globaz.pavo.process.CIStatistiquesProcess;

public class CIStatistiquesCiProcessHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIStatistiquesCiProcessViewBean vb = (CIStatistiquesCiProcessViewBean) viewBean;
        try {
            CIStatistiquesProcess process = new CIStatistiquesProcess(vb.getSession());
            process.setAnneeDebut(vb.getDateDebut());
            process.setAnneeFin(vb.getDateFin());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
