package globaz.pavo.helpers.comparaison;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.comparaison.CIComparaisonEnteteProcessViewBean;
import globaz.pavo.process.CIComparaisonEnteteProcess;

public class CIComparaisonEnteteProcessHelper extends FWHelper {

    public CIComparaisonEnteteProcessHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIComparaisonEnteteProcessViewBean vb = (CIComparaisonEnteteProcessViewBean) viewBean;
        try {
            CIComparaisonEnteteProcess process = new CIComparaisonEnteteProcess();
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
