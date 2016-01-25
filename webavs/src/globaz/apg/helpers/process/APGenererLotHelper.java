/*
 * Créé le 29 juin 05
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APGenererLotProcess;
import globaz.apg.vb.process.APGenererLotViewBean;
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
public class APGenererLotHelper extends FWHelper {

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
        APGenererLotViewBean glViewBean = (APGenererLotViewBean) viewBean;

        APGenererLotProcess process = new APGenererLotProcess((BSession) session);

        process.setEMailAddress(glViewBean.getEMailAddress());
        process.setDescription(glViewBean.getDescription());
        process.setTypePrestation(glViewBean.getTypePrestation());
        process.setPrestationDateFin(glViewBean.getPrestationDateFin());

        process.start();
    }
}
