package globaz.perseus.helpers.lot;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.lot.PFComptabiliserLotProcess;
import globaz.perseus.vb.lot.PFComptabiliserLotViewBean;

public class PFComptabiliserLotHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PFComptabiliserLotViewBean) {
            PFComptabiliserLotProcess process = new PFComptabiliserLotProcess();
            process.setSession((BSession) session);
            /**
             * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et
             * doit donc être renommée différement (mailAd) pour fonctionner correctement.
             */
            process.setAdresseMail(((PFComptabiliserLotViewBean) viewBean).getAdresseMail());
            process.setLot(((PFComptabiliserLotViewBean) viewBean).getLot());

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
