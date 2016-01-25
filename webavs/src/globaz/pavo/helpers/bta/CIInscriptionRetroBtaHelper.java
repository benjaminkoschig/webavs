package globaz.pavo.helpers.bta;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.bta.CIInscriptionRetroBtaViewBean;
import globaz.pavo.process.CIInscriptionRetroBtaProcess;

public class CIInscriptionRetroBtaHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIInscriptionRetroBtaViewBean vb = (CIInscriptionRetroBtaViewBean) viewBean;
        try {
            CIInscriptionRetroBtaProcess process = new CIInscriptionRetroBtaProcess();
            process.setIdDossierBta(vb.getId());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
