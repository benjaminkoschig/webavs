package globaz.corvus.helpers.process;

import globaz.corvus.process.REEnvoyerRecapARC8DProcess;
import globaz.corvus.vb.process.REgenererRecapitulationRentesARC8DViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererRecapitulationRentesARC8DHelper extends PRAbstractHelper {

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

        REgenererRecapitulationRentesARC8DViewBean gRecRenARC8DViewBean = (REgenererRecapitulationRentesARC8DViewBean) viewBean;

        REEnvoyerRecapARC8DProcess process;
        try {
            process = new REEnvoyerRecapARC8DProcess((BSession) session);

            process.setEMailAddress(gRecRenARC8DViewBean.getEMailAddress());
            process.setReDetRecMenViewBean(gRecRenARC8DViewBean.getReDetRecMenViewBean());

            process.start();

        } catch (Exception e) {
            ((BSession) session).addError(e.getMessage());
        }
    }

}
