package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFListeNouvelleAffiliationProcess;
import globaz.naos.db.affiliation.AFListeNouvelleAffiliationViewBean;

/**
 * @author MMO
 * @since 15 mars 2011
 */
public class AFListeNouvelleAffiliationHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFListeNouvelleAffiliationViewBean vb = (AFListeNouvelleAffiliationViewBean) viewBean;

        try {
            AFListeNouvelleAffiliationProcess process = new AFListeNouvelleAffiliationProcess();
            process.setSession((BSession) session);
            process.setFromDateCreation(vb.getFromDateCreation());
            process.setToDateCreation(vb.getToDateCreation());
            process.setCritereSelection(vb.getCritereSelection());
            process.setEMailAddress(vb.getEmail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
    }

}
