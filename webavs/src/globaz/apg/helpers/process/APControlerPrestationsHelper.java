/*
 * Créé le 29 juin 05
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APControlerPrestationsProcess;
import globaz.apg.vb.process.APControlerPrestationsViewBean;
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
public class APControlerPrestationsHelper extends FWHelper {

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
        APControlerPrestationsViewBean cpViewBean = (APControlerPrestationsViewBean) viewBean;

        APControlerPrestationsProcess process = new APControlerPrestationsProcess((BSession) session);

        process.setEMailAddress(cpViewBean.getEMailAddress());
        process.setImprimerListeDroits(cpViewBean.getImprimerListeDroits());
        process.setControlerPrestations(cpViewBean.getControlerPrestations());

        process.start();
    }
}
