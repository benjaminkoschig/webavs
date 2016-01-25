/*
 * Créé le 29 juin 05
 */
package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.process.IJGenererLotProcess;
import globaz.ij.vb.process.IJGenererLotViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererLotHelper extends FWHelper {

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
        IJGenererLotViewBean glViewBean = (IJGenererLotViewBean) viewBean;

        IJGenererLotProcess process = new IJGenererLotProcess((BSession) session);

        process.setEMailAddress(glViewBean.getEMailAddress());
        process.setDescription(glViewBean.getDescription());

        process.start();
    }
}
