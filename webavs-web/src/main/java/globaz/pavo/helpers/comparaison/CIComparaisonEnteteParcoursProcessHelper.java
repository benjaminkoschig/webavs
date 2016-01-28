package globaz.pavo.helpers.comparaison;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.comparaison.CIComparaisonEnteteParcoursProcessViewBean;
import globaz.pavo.process.CIComparaisonEnteteParcoursProcess;

public class CIComparaisonEnteteParcoursProcessHelper extends FWHelper {

    public CIComparaisonEnteteParcoursProcessHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIComparaisonEnteteParcoursProcessViewBean vb = (CIComparaisonEnteteParcoursProcessViewBean) viewBean;
        try {
            CIComparaisonEnteteParcoursProcess process = new CIComparaisonEnteteParcoursProcess();

            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
            e.printStackTrace();
        }
    }
}
