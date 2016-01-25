package globaz.naos.helpers.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.taxeCo2.AFLettreTaxeCo2ViewBean;
import globaz.naos.process.taxeCo2.AFImprimerLettreTaxeCo2Process;

public class AFLettreTaxeCo2Helper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFLettreTaxeCo2ViewBean vb = (AFLettreTaxeCo2ViewBean) viewBean;

        try {
            AFImprimerLettreTaxeCo2Process process = new AFImprimerLettreTaxeCo2Process();
            process.setSession((BSession) session);
            process.setForAnnee(vb.getAnnee());
            process.setForIdTaxeCo2(vb.getIdTaxeCo2());
            process.setEMailAddress(vb.getEmail());
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
