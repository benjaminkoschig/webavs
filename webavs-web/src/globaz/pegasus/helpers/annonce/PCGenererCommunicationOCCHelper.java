package globaz.pegasus.helpers.annonce;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.annonce.communicationocc.PCGenererCommunicationOCCProcess;
import globaz.pegasus.vb.annonce.PCGenererCommunicationOCCViewBean;

public class PCGenererCommunicationOCCHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof PCGenererCommunicationOCCViewBean) {
            PCGenererCommunicationOCCViewBean vb = (PCGenererCommunicationOCCViewBean) viewBean;
            PCGenererCommunicationOCCProcess process = new PCGenererCommunicationOCCProcess();
            process.setSession((BSession) session);
            process.setMailGest(vb.getMailAddress());
            process.setNoSemaine(vb.getNoSemaine());
            process.setAnnee(vb.getAnnee());
            try {
                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

}
