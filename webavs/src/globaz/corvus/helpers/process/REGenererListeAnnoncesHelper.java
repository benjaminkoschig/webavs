/*
 * Créé le 05 sept. 07
 */
package globaz.corvus.helpers.process;

import globaz.corvus.itext.REListeAnnonces;
import globaz.corvus.process.REGenererListeExcelAnnoncesProcess;
import globaz.corvus.vb.process.REGenererListeAnnoncesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */
public class REGenererListeAnnoncesHelper extends PRAbstractHelper {

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
        REGenererListeAnnoncesViewBean glaViewBean = (REGenererListeAnnoncesViewBean) viewBean;

        if (REGenererListeAnnoncesViewBean.PDF_OUTPUT.equals(glaViewBean.getOutputType())) {

            REListeAnnonces process = new REListeAnnonces((BSession) session);

            process.setEMailAddress(glaViewBean.getEMailAddress());
            process.setMois(glaViewBean.getMois());
            process.setIsAnnoncesSubsequentes(false);
            process.start();

        } else {

            REGenererListeExcelAnnoncesProcess process = new REGenererListeExcelAnnoncesProcess();

            process.setSession((BSession) session);
            process.setEMailAddress(glaViewBean.getEMailAddress());
            process.setMois(glaViewBean.getMois());

            process.start();
        }

    }
}
