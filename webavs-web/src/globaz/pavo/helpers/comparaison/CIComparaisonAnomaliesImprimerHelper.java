package globaz.pavo.helpers.comparaison;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.comparaison.CIComparaisonAnomaliesImprimerViewBean;
import globaz.pavo.print.list.CIComparaisonAnomaliesImprimer;

public class CIComparaisonAnomaliesImprimerHelper extends FWHelper {

    public CIComparaisonAnomaliesImprimerHelper() {
        super();

    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIComparaisonAnomaliesImprimerViewBean vb = (CIComparaisonAnomaliesImprimerViewBean) viewBean;
        try {
            CIComparaisonAnomaliesImprimer process = new CIComparaisonAnomaliesImprimer(vb.getSession());

            process.setForIdTypeAnomalie(vb.getForIdTypeAnomalie());
            process.setLikeNumeroAvs(vb.getLikeNumeroAvs());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
