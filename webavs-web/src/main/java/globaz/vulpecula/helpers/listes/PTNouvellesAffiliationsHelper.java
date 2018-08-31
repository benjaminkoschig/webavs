package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTNouvellesAffiliationsViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.process.nouvellesaffiliations.NouvellesAffiliationsProcess;

public class PTNouvellesAffiliationsHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTNouvellesAffiliationsViewBean vb = (PTNouvellesAffiliationsViewBean) viewBean;
            NouvellesAffiliationsProcess process = new NouvellesAffiliationsProcess();
            process.setDateDebut(new Date(vb.getDateDebut()));
            process.setDateFin(new Date(vb.getDateFin()));
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

}
