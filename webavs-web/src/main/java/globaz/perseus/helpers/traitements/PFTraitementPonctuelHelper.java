package globaz.perseus.helpers.traitements;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.traitements.PFTraitementPonctuelProcess;
import globaz.perseus.vb.traitements.PFTraitementPonctuelViewBean;

public class PFTraitementPonctuelHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // Si decision processviewbean, pour document
        if (viewBean instanceof PFTraitementPonctuelViewBean) {
            PFTraitementPonctuelProcess process = new PFTraitementPonctuelProcess();
            process.setSession((BSession) session);
            /**
             * La variable de l'adresse email est automatiquement setter ? NULL si elle est nomm?e (eMailAddress) et
             * doit donc ?tre renomm?e diff?rement (mailAd) pour fonctionner correctement.
             */
            process.setAdresseMailCCVD(((PFTraitementPonctuelViewBean) viewBean).getAdresseMailCCVD());
            process.setAdresseMailAGLAU(((PFTraitementPonctuelViewBean) viewBean).getAdresseMailAGLAU());
            process.setMois(((PFTraitementPonctuelViewBean) viewBean).getMois());
            process.setTexteDecision(((PFTraitementPonctuelViewBean) viewBean).getTexteDecision());

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
