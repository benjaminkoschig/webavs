/*
 * Créé le 22 août 07
 */
package globaz.corvus.helpers.paiementMensuel;

import globaz.corvus.process.REGenererListesVerificationProcess;
import globaz.corvus.vb.paiementMensuel.REGenererListesVerificationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */
public class REGenererListesVerificationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REGenererListesVerificationViewBean glvViewBean = (REGenererListesVerificationViewBean) viewBean;

        REGenererListesVerificationProcess process = new REGenererListesVerificationProcess((BSession) session);

        process.setEMailAddress(glvViewBean.getEMailAddress());
        process.setMoisAnnee(glvViewBean.getMoisProchainPmt());
        process.setSendCompletionMail(true);
        process.setSendMailOnError(true);

        process.start();
    }

}
