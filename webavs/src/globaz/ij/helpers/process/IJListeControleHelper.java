/*
 * Créé le 7 oct. 05
 */
package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.itext.IJListePrestationsLot;
import globaz.ij.vb.process.IJListeControleViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJListeControleHelper extends FWHelper {

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
        IJListeControleViewBean lcViewBean = (IJListeControleViewBean) viewBean;

        IJListePrestationsLot process = new IJListePrestationsLot((BSession) session);

        process.setEMailAddress(lcViewBean.getEMailAddress());
        process.setIdLot(lcViewBean.getIdLot());

        process.start();
    }
}
