/*
 * Cr?? le 18 juil. 05
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APEnvoyerCIProcess;
import globaz.apg.vb.process.APEnvoyerCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnvoyerCIHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APEnvoyerCIViewBean cpViewBean = (APEnvoyerCIViewBean) viewBean;

        APEnvoyerCIProcess process = new APEnvoyerCIProcess((BSession) session);

        process.setEMailAddress(cpViewBean.getEMailAddress());

        process.start();
    }
}
