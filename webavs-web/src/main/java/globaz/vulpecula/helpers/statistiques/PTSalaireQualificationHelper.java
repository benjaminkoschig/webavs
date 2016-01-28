package globaz.vulpecula.helpers.statistiques;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.statistiques.PTSalaireQualificationViewBean;
import ch.globaz.vulpecula.process.statistiques.SalaireQualificationProcess;

public class PTSalaireQualificationHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTSalaireQualificationViewBean vb = (PTSalaireQualificationViewBean) viewBean;

            SalaireQualificationProcess process = new SalaireQualificationProcess();

            process.setEMailAddress(vb.getEmail());
            process.setPeriodeDebut(vb.getPeriodeDebut());
            process.setPeriodeFin(vb.getPeriodeFin());
            process.setIdConvention(vb.getIdConvention());
            process.setCodesQualifications(vb.getCodesQualifications());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
