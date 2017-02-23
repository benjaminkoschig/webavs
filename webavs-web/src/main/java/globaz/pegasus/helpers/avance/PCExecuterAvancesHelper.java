package globaz.pegasus.helpers.avance;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.avance.PCExecuterAvancesProcess;
import globaz.pegasus.vb.avance.PCExecuterAvancesViewBean;

public class PCExecuterAvancesHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof PCExecuterAvancesViewBean) {
            PCExecuterAvancesProcess process = new PCExecuterAvancesProcess();
            process.setTypeTraitement(((PCExecuterAvancesViewBean) viewBean).getTypeTraitement());
            process.setEmail(((PCExecuterAvancesViewBean) viewBean).getEmail());
            process.setSession((BSession) session);
            process.setNoNog(((PCExecuterAvancesViewBean) viewBean).getNoOg());
            process.setIdOrganeExecution(((PCExecuterAvancesViewBean) viewBean).getIdOrganeExecution());
            process.setDateEchance(((PCExecuterAvancesViewBean) viewBean).getDateEcheance());
            process.setIsoGestionnaire(((PCExecuterAvancesViewBean) viewBean).getIsoGestionnaire());
            process.setIsoHighPriority(((PCExecuterAvancesViewBean) viewBean).getIsoHighPriority());
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
