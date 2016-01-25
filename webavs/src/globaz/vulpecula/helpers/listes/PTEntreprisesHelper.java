package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTEntreprisesViewBean;
import ch.globaz.vulpecula.process.entreprises.ListEntreprisesExcelProcess;
import ch.globaz.vulpecula.process.prestations.AbstractPrestationExcelProcess;

public class PTEntreprisesHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTEntreprisesViewBean vb = (PTEntreprisesViewBean) viewBean;
            AbstractPrestationExcelProcess process = new ListEntreprisesExcelProcess();
            process.setEMailAddress(vb.getEmail());
            process.setIdConvention(vb.getIdConvention());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
