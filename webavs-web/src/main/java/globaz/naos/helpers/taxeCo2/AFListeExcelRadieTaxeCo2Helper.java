package globaz.naos.helpers.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.taxeCo2.AFListeExcelRadieTaxeCo2ViewBean;
import globaz.naos.process.taxeCo2.AFListeExcelRadieTaxeCo2Process;

public class AFListeExcelRadieTaxeCo2Helper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFListeExcelRadieTaxeCo2ViewBean vb = (AFListeExcelRadieTaxeCo2ViewBean) viewBean;

        try {
            AFListeExcelRadieTaxeCo2Process process = new AFListeExcelRadieTaxeCo2Process();
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
