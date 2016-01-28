package globaz.corvus.helpers.process;

import globaz.corvus.process.REGenererRecapitulationRentesProcess;
import globaz.corvus.vb.process.REgenererRecapitulationRentesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererRecapitulationRentesHelper extends PRAbstractHelper {

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

        REgenererRecapitulationRentesViewBean gRecRenViewBean = (REgenererRecapitulationRentesViewBean) viewBean;

        REGenererRecapitulationRentesProcess process;
        try {
            process = new REGenererRecapitulationRentesProcess((BSession) session);

            process.setEMailAddress(gRecRenViewBean.getEMailAddress());
            process.setReDetRecMenViewBean(gRecRenViewBean.getReDetRecMenViewBean());

            process.start();

        } catch (Exception e) {
            ((BSession) session).addError(e.getMessage());
        }
    }

}
