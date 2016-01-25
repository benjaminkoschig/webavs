package globaz.pavo.helpers.bta;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.bta.CIInscriptionBtaViewBean;
import globaz.pavo.process.CIInscriptionBtaProcess;

public class CIInscriptionBtaHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIInscriptionBtaViewBean vb = (CIInscriptionBtaViewBean) viewBean;
        try {
            CIInscriptionBtaProcess process = new CIInscriptionBtaProcess();
            process.setSimulation(vb.isSimulation());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
