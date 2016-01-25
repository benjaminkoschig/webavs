/*
 * Créé le 6 oct. 05
 */
package globaz.ij.helpers.lots;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.vb.lots.IJLotViewBean;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureJointCompensationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_chercher(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession bSession = (BSession) session;
        IJLotViewBean lot = (IJLotViewBean) viewBean;

        lot.setSession(bSession);
        lot.retrieve(bSession.getCurrentThreadTransaction());
    }
}
