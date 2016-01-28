package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFListeAffilieFraisGestionReduitProcess;
import globaz.naos.db.affiliation.AFListeAffilieFraisGestionReduitViewBean;

/**
 * @author MMO
 * @since 23 août 2011
 */
public class AFListeAffilieFraisGestionReduitHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFListeAffilieFraisGestionReduitViewBean vb = (AFListeAffilieFraisGestionReduitViewBean) viewBean;

        try {
            AFListeAffilieFraisGestionReduitProcess process = new AFListeAffilieFraisGestionReduitProcess();
            process.setSession((BSession) session);
            process.setTauxFraisGestion(vb.getTauxFraisGestion());
            process.setEMailAddress(vb.getEmail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
    }
}
