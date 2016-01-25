package globaz.perseus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.decision.PFDecisionProcess;
import globaz.perseus.vb.decision.PFDecisionProcessViewBean;

public class PFDecisionProcessHelper extends FWHelper {

    /**
     * Méthode de démarrage du process
     * 
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // Si decision processviewbean, pour document
        if (viewBean instanceof PFDecisionProcessViewBean) {
            PFDecisionProcess process = new PFDecisionProcess();
            process.setSession((BSession) session);
            process.setDecisionId(((PFDecisionProcessViewBean) viewBean).getDecisionId());
            // if ("on".equals(((PFDecisionProcessViewBean) viewBean).getIsSendToGed())) {
            // process.setSendToGed(true);
            // }
            process.setIsSendToGed(((PFDecisionProcessViewBean) viewBean).getIsSendToGed());
            /**
             * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et
             * doit donc être renommée différement (mailAd) pour fonctionner correctement.
             */
            process.seteMailAddress(((PFDecisionProcessViewBean) viewBean).geteMailAddress());
            process.setMailAd(((PFDecisionProcessViewBean) viewBean).geteMailAddress());
            process.setDateDocument(((PFDecisionProcessViewBean) viewBean).getDecision().getSimpleDecision()
                    .getDateDocument());

            try {
                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMessage("Unable to start........");
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        } else {
            super._start(viewBean, action, session);
        }
    }
}
