package globaz.perseus.helpers.revisiondossier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.revisiondossier.PFRevisionDossierProcess;
import globaz.perseus.vb.revisiondossier.PFRevisionDossierViewBean;

public class PFRevisionDossierHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PFRevisionDossierViewBean) {

            PFRevisionDossierViewBean vb = (PFRevisionDossierViewBean) viewBean;
            PFRevisionDossierProcess process = new PFRevisionDossierProcess();
            process.setSession((BSession) session);
            process.setAdresseMail(vb.getAdresseMail());
            process.setCsCaisse(vb.getCsCaisse());
            process.setDateRevision(vb.getDateRevision());
            process.setIsSendToGed(vb.getIsSendToGed());

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
