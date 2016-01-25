/*
 * Créé le 03 sept. 07
 */
package globaz.corvus.helpers.process;

import globaz.corvus.process.REGenererListeEtatRentesProcess;
import globaz.corvus.vb.process.REGenererListeEtatRentesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */
public class REGenererListeEtatRentesHelper extends PRAbstractHelper {

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
        REGenererListeEtatRentesViewBean glerViewBean = (REGenererListeEtatRentesViewBean) viewBean;

        REGenererListeEtatRentesProcess process;
        try {
            process = new REGenererListeEtatRentesProcess((BSession) session);

            process.setEMailAddress(glerViewBean.getEMailAddress());
            process.setForMoisAnnee(glerViewBean.getMois());

            process.start();

        } catch (FWIException e) {
            ((BSession) session).addError(e.getMessage());
        } catch (Exception e) {
            ((BSession) session).addError(e.getMessage());
        }
    }
}
