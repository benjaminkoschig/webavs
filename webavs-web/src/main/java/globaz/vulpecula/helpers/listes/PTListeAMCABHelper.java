package globaz.vulpecula.helpers.listes;

import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.process.caissemaladie.CaisseMaladieDecomptesProcess;
import ch.globaz.vulpecula.process.caissemaladie.CaisseMaladieProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.listes.PTListeAMCABViewBean;

public class PTListeAMCABHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            AffiliationCaisseMaladieService cmService = VulpeculaServiceLocator.getAffiliationCaisseMaladieService();

            PTListeAMCABViewBean vb = (PTListeAMCABViewBean) viewBean;
            CaisseMaladieProcess process = new CaisseMaladieDecomptesProcess();
            process.setEMailAddress(vb.getEmail());

            cmService.checkPeriodValidty(vb.getDateFrom(), vb.getDateTo(), process.getSession());

            if (!JadeStringUtil.isEmpty(vb.getDateFrom())) {
                process.setDateAnnonceFrom(new Date(vb.getDateFrom()).getValue());
            }

            if (!JadeStringUtil.isEmpty(vb.getDateTo())) {
                process.setDateAnnonceTo(new Date(vb.getDateTo()).getValue());
            } else {
                process.setDateAnnonceTo("0");
            }

            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

    }

}
