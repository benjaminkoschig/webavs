package globaz.corvus.helpers.process;

import globaz.corvus.process.REImporterAnnonces50Process;
import globaz.corvus.vb.process.REGenererListesABViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererListesABHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REGenererListesABViewBean glabViewBean = (REGenererListesABViewBean) viewBean;

        REImporterAnnonces50Process process = new REImporterAnnonces50Process();
        process.setSession((BSession) session);
        process.setEMailAddress(glabViewBean.getEMailAddress());
        process.setSendCompletionMail(true);
        process.setSendMailOnError(true);
        process.start();

    }
}