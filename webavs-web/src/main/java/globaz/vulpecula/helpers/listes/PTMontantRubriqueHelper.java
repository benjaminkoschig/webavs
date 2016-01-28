package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTMontantRubriqueViewBean;
import ch.globaz.vulpecula.process.comptabilite.MontantRubriqueProcess;

public class PTMontantRubriqueHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTMontantRubriqueViewBean vb = (PTMontantRubriqueViewBean) viewBean;

            MontantRubriqueProcess process = new MontantRubriqueProcess();
            process.setEMailAddress(vb.getEmail());
            process.setDateSaisie(vb.getDate());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
