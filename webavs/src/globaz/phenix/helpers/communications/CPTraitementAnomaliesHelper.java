package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.db.communications.CPTraitementAnomaliesViewBean;
import globaz.phenix.process.communications.CPProcessTraitementAnomalies;

public class CPTraitementAnomaliesHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("executerReceptionner".equals(action.getActionPart()) && viewBean instanceof CPTraitementAnomaliesViewBean) {
            CPTraitementAnomaliesViewBean vb = (CPTraitementAnomaliesViewBean) viewBean;

            CPProcessTraitementAnomalies process = new CPProcessTraitementAnomalies();
            process.setSession(vb.getSession());
            process.setEMailAddress(vb.getEMailAdress());
            process.setInputFileName(vb.getReceptionFileName());
            process.setCsCanton(vb.getCsCanton());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }
        return super.execute(viewBean, action, session);
    }
}
