/*
 * Créé le 28 juin 05
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APGenererDecomptesProcess;
import globaz.apg.vb.process.APGenererDecomptesViewBean;
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
public class APGenererDecomptesHelper extends FWHelper {

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
        APGenererDecomptesViewBean gcViewBean = (APGenererDecomptesViewBean) viewBean;

        APGenererDecomptesProcess process = new APGenererDecomptesProcess((BSession) session);

        process.setEMailAddress(gcViewBean.getEMailAddress());
        process.setDateSurDocument(gcViewBean.getDateSurDocument());
        process.setDateValeurComptable(gcViewBean.getDateValeurComptable());
        process.setDescriptionLot(gcViewBean.getDescriptionLot());
        process.setIdLot(gcViewBean.getIdLot());
        process.setIsDefinitif(gcViewBean.getIsDefinitif());
        process.setIsSendToGed(gcViewBean.getIsSendToGed());
        process.start();
    }
}
