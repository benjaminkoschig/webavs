package globaz.naos.helpers.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2ViewBean;
import globaz.naos.process.taxeCo2.AFListeExcelTaxeCo2Process;

public class AFListeExcelTaxeCo2Helper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFListeExcelTaxeCo2ViewBean vb = (AFListeExcelTaxeCo2ViewBean) viewBean;

        try {
            AFListeExcelTaxeCo2Process process = new AFListeExcelTaxeCo2Process();
            process.setSession((BSession) session);
            process.setForAnnee(vb.getAnneeMasse());
            process.setEMailAddress(vb.getEmail());
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
