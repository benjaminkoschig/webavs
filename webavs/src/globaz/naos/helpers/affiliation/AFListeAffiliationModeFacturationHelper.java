package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFListeAffiliationModeFacturationProcess;
import globaz.naos.db.affiliation.AFListeAffiliationModeFacturationViewBean;

/**
 * @author MMO
 * @since 15 mars 2011
 */
public class AFListeAffiliationModeFacturationHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFListeAffiliationModeFacturationViewBean vb = (AFListeAffiliationModeFacturationViewBean) viewBean;

        try {
            AFListeAffiliationModeFacturationProcess process = new AFListeAffiliationModeFacturationProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
    }

}
