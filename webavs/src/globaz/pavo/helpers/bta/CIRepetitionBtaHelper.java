package globaz.pavo.helpers.bta;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.bta.CIRepetitionBtaViewBean;
import globaz.pavo.print.itext.CIRepetitionBta_Doc;

public class CIRepetitionBtaHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIRepetitionBtaViewBean vb = (CIRepetitionBtaViewBean) viewBean;
        try {
            CIRepetitionBta_Doc process = new CIRepetitionBta_Doc();
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
