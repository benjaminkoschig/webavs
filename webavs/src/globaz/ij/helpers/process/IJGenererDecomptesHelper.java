/*
 * Créé le 7 oct. 05
 */
package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.process.IJGenererDecomptesProcess;
import globaz.ij.vb.process.IJGenererDecomptesViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererDecomptesHelper extends FWHelper {

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
        IJGenererDecomptesViewBean gcViewBean = (IJGenererDecomptesViewBean) viewBean;

        IJGenererDecomptesProcess process = new IJGenererDecomptesProcess((BSession) session);

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
