package globaz.pavo.helpers.bta;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.bta.CIDecisionBtaViewBean;
import globaz.pavo.print.itext.CIDecisionBta_Doc;

public class CIDecisionBtaHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIDecisionBtaViewBean vb = (CIDecisionBtaViewBean) viewBean;
        try {
            CIDecisionBta_Doc process = new CIDecisionBta_Doc();
            process.setIdDossierBta(vb.getId());
            process.setEMailAddress(vb.getEmailAddress());
            process.setAnnee(vb.getAnnee());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
