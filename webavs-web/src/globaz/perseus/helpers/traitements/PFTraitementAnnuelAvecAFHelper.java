package globaz.perseus.helpers.traitements;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.traitements.PFTraitementAnnuelAvecAFProcess;
import globaz.perseus.vb.traitements.PFTraitementAnnuelAvecAFViewBean;

public class PFTraitementAnnuelAvecAFHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // Si decision processviewbean, pour document
        if (viewBean instanceof PFTraitementAnnuelAvecAFViewBean) {
            PFTraitementAnnuelAvecAFProcess process = new PFTraitementAnnuelAvecAFProcess();
            process.setSession((BSession) session);
            /**
             * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et
             * doit donc être renommée différement (mailAd) pour fonctionner correctement.
             */
            process.setAdresseMailCCVD(((PFTraitementAnnuelAvecAFViewBean) viewBean).getAdresseMailCCVD());
            process.setAdresseMailAGLAU(((PFTraitementAnnuelAvecAFViewBean) viewBean).getAdresseMailAGLAU());
            process.setTexteDecision(((PFTraitementAnnuelAvecAFViewBean) viewBean).getTexteDecision());

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
