package globaz.corvus.helpers.process;

import globaz.corvus.process.liste.restitution.REGenererListeRestitutionsSoldesProcess;
import globaz.corvus.vb.process.REGenererListeRestitutionsSoldesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererListeRestitutionsSoldesHelper extends PRAbstractHelper {

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
        try {
            REGenererListeRestitutionsSoldesViewBean glabViewBean = (REGenererListeRestitutionsSoldesViewBean) viewBean;

            if (!((PRAbstractViewBeanSupport) viewBean).validate()) {
                return;
            }

            REGenererListeRestitutionsSoldesProcess process = new REGenererListeRestitutionsSoldesProcess();
            process.setSession((BSession) session);
            process.setForRole(glabViewBean.getForRole());
            process.setDateValeur(glabViewBean.getDateValeur());
            process.setEMailAddress(glabViewBean.getEMailAddress());

            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}