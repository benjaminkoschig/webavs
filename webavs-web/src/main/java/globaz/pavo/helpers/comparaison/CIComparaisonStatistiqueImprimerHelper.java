package globaz.pavo.helpers.comparaison;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.comparaison.CIComparaisonStatistiqueImprimerViewBean;
import globaz.pavo.print.list.CIComparaisonStatistique_Doc;

public class CIComparaisonStatistiqueImprimerHelper extends FWHelper {

    public CIComparaisonStatistiqueImprimerHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CIComparaisonStatistiqueImprimerViewBean vb = (CIComparaisonStatistiqueImprimerViewBean) viewBean;
        try {
            CIComparaisonStatistique_Doc process = new CIComparaisonStatistique_Doc();
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
