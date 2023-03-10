package globaz.perseus.helpers.paiements;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.paiements.PFPaiementMensuelProcess;
import globaz.perseus.vb.paiements.PFPaiementMensuelViewBean;

public class PFPaiementMensuelHelper extends FWHelper {

	@Override
	protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

		// Si decision processviewbean, pour document
		if (viewBean instanceof PFPaiementMensuelViewBean) {
			PFPaiementMensuelProcess process = new PFPaiementMensuelProcess();
			process.setSession((BSession) session);
			/**
			 * La variable de l'adresse email est automatiquement setter ? NULL si elle est nomm?e (eMailAddress) et
			 * doit donc ?tre renomm?e diff?rement (mailAd) pour fonctionner correctement.
			 */
			process.setAdresseMail(((PFPaiementMensuelViewBean) viewBean).getAdresseMail());

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
