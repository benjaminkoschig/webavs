package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIEcritureViewBean;
import globaz.pavo.process.CIConversionGenre6Process;

public class CIConversionGenre6Helper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIEcritureViewBean vb = (CIEcritureViewBean) viewBean;
        try {
            CIConversionGenre6Process process = new CIConversionGenre6Process(vb.getSession());
            process.setSelectedIdValue(vb.getEcritureId());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
