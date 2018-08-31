package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTSalairesAVSViewBean;
import ch.globaz.vulpecula.process.salaires.ListSalairesAVSExcelProcess;

public class PTSalairesAVSHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTSalairesAVSViewBean vb = (PTSalairesAVSViewBean) viewBean;
            ListSalairesAVSExcelProcess process = new ListSalairesAVSExcelProcess();
            process.setEMailAddress(vb.getEmail());
            process.setIdEmployeur(vb.getIdEmployeur());
            process.setInTypeDecompte(vb.getInTypeDecompte());
            process.setAnnee(vb.getAnnee());

            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
