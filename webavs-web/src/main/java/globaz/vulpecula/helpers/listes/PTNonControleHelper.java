package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTNonControleViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.process.revision.ListNonControleExcelProcess;

public class PTNonControleHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTNonControleViewBean vb = (PTNonControleViewBean) viewBean;

            ListNonControleExcelProcess process = new ListNonControleExcelProcess();
            process.setNombreAnnee(vb.getNombreAnnee());
            process.setDateDebut(new Date(vb.getDateDebut()));
            process.setEMailAddress(vb.getEmail());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            if (vb.getUniquementAVS().booleanValue()) {
                process.setUniquementAVS("true");
            }
            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
