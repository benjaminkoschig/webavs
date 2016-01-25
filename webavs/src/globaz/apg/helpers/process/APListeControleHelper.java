/*
 * Créé le 30 nov. 06
 */
package globaz.apg.helpers.process;

import globaz.apg.itext.APListePrestationsLot;
import globaz.apg.vb.process.APListeControleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class APListeControleHelper extends FWHelper {

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
        APListeControleViewBean lcViewBean = (APListeControleViewBean) viewBean;

        APListePrestationsLot process = new APListePrestationsLot((BSession) session);

        process.setEMailAddress(lcViewBean.getEMailAddress());
        process.setIdLot(lcViewBean.getIdLot());

        process.start();
    }
}
