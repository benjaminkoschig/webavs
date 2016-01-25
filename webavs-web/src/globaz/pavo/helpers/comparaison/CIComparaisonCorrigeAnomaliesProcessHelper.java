package globaz.pavo.helpers.comparaison;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.comparaison.CIComparaisonCorrigeAnomaliesProcessViewBean;
import globaz.pavo.process.CIComparaisonCorrigeAnomalies;

public class CIComparaisonCorrigeAnomaliesProcessHelper extends FWHelper {

    public CIComparaisonCorrigeAnomaliesProcessHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIComparaisonCorrigeAnomaliesProcessViewBean vb = (CIComparaisonCorrigeAnomaliesProcessViewBean) viewBean;
        try {
            CIComparaisonCorrigeAnomalies process = new CIComparaisonCorrigeAnomalies();

            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
