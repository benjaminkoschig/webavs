package globaz.pavo.helpers.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.inscriptions.CIJournalImprimerViewBean;

public class CIJournalImprimerHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CIJournalImprimerViewBean vb = (CIJournalImprimerViewBean) viewBean;
        try {
            if (vb.getUnMailPasAnnee().booleanValue()) {
                // Description année de cotisation
                if ((!JadeStringUtil.isBlankOrZero(vb.getFromAnnee()))
                        && (!JadeStringUtil.isBlankOrZero(vb.getUntilAnnee()))) {
                    int anneeDeb = JadeStringUtil.toInt(vb.getFromAnnee());
                    int anneeFin = JadeStringUtil.toInt(vb.getUntilAnnee());
                    for (int i = anneeDeb; i <= anneeFin; i++) {
                        vb.setFromAnnee("" + i);
                        vb.setUntilAnnee("" + i);
                        BProcessLauncher.start(vb);
                    }
                } else {
                    BProcessLauncher.start(vb);
                }
            } else {
                BProcessLauncher.start(vb);
            }

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
