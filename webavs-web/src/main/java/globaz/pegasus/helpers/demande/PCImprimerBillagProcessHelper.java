package globaz.pegasus.helpers.demande;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.demande.PCImprimerBillagProcess;
import globaz.pegasus.vb.demande.PCImprimerBillagViewBean;

public class PCImprimerBillagProcessHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof PCImprimerBillagViewBean) {
            try {
                PCImprimerBillagProcess process = new PCImprimerBillagProcess();
                process.setSession((BSession) session);
                process.setIdDemande(((PCImprimerBillagViewBean) viewBean).getIdDemandePc());
                process.setDateDebut(((PCImprimerBillagViewBean) viewBean).getDateDebut());
                process.setDateDoc(((PCImprimerBillagViewBean) viewBean).getDateDoc());
                process.setEmailGest(((PCImprimerBillagViewBean) viewBean).getMailGest());
                process.setNss(((PCImprimerBillagViewBean) viewBean).getNSS());
                process.setGestionnaire(((PCImprimerBillagViewBean) viewBean).getGestionnaire());
                process.setIdTiers(((PCImprimerBillagViewBean) viewBean).getIdTiers());

                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

}
