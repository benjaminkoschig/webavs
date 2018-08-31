package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTListeCPsoumisLPPViewBean;
import ch.globaz.vulpecula.process.listeslpp.ListeCPSoumisLPPProcess;

public class PTListeCPsoumisLPPHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTListeCPsoumisLPPViewBean vb = (PTListeCPsoumisLPPViewBean) viewBean;
        ListeCPSoumisLPPProcess process = new ListeCPSoumisLPPProcess();
        process.setEmail(vb.getEmail());
        process.setAnnee(vb.getAnnee());
        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
