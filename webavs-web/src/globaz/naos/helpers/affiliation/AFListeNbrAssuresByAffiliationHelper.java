package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFListeNbrAssuresByAffiliationViewBean;
import globaz.naos.process.nbrAssures.AFImpressionListeNbrAssuresByAffiliationProcess;

/**
 * Helper pour le lancement du processus de gönöration de la liste
 * des assurés par affiliation
 */
public class AFListeNbrAssuresByAffiliationHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFListeNbrAssuresByAffiliationViewBean vb = (AFListeNbrAssuresByAffiliationViewBean) viewBean;

        try {
            AFImpressionListeNbrAssuresByAffiliationProcess process = new AFImpressionListeNbrAssuresByAffiliationProcess();
            process.setSession((BSession) session);
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setToNumAffilie(vb.getToNumAffilie());
            process.setForIdAssurance(vb.getIdAssurance());
            process.setForAnnee(vb.getForAnnee());
            process.setEMailAddress(vb.getMail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFListeNbrAssuresByAffiliationViewBean vb = (AFListeNbrAssuresByAffiliationViewBean) viewBean;
        return super.execute(viewBean, action, session);
    }

}
