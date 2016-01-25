/*
 * Créé le 5 octobre 05
 */
package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.process.IJEnvoyerCIProcess;
import globaz.ij.vb.process.IJEnvoyerCIViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJEnvoyerCIHelper extends FWHelper {

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
        IJEnvoyerCIViewBean cpViewBean = (IJEnvoyerCIViewBean) viewBean;

        IJEnvoyerCIProcess process = new IJEnvoyerCIProcess((BSession) session);

        process.setEMailAddress(cpViewBean.getEMailAddress());

        process.start();
    }
}
