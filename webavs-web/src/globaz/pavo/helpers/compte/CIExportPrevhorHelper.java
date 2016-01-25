package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIExportPrevhorViewBean;
import globaz.pavo.process.CIExportPrevorProcess;

public class CIExportPrevhorHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            CIExportPrevhorViewBean vb = (CIExportPrevhorViewBean) viewBean;
            CIExportPrevorProcess process = new CIExportPrevorProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmailAddress());
            process.setAnnee(vb.getAnnee());
            process.setMontantSeuilAnnuel(vb.getMontantSeuilAnnuel());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
