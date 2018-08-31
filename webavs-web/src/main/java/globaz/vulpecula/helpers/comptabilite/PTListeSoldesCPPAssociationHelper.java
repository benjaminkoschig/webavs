package globaz.vulpecula.helpers.comptabilite;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.comptabilite.PTListeSoldesCPPAssociationViewBean;
import ch.globaz.vulpecula.process.comptabilite.ListeSoldesCPPAssociationProcess;

public class PTListeSoldesCPPAssociationHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTListeSoldesCPPAssociationViewBean vb = (PTListeSoldesCPPAssociationViewBean) viewBean;
        try {
            ListeSoldesCPPAssociationProcess process = new ListeSoldesCPPAssociationProcess();
            process.setEMailAddress(vb.getEmail());
            process.setOrderBy(vb.getOrderBy());
            process.setDateUntil(vb.getDateUntil());
            BProcessLauncher.start(process);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
