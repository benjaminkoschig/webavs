/*
 * Créé le 7 oct. 05
 */
package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.process.IJEnvoyerAnnoncesProcess;
import globaz.ij.vb.process.IJEnvoyerAnnoncesViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJEnvoyerAnnoncesHelper extends FWHelper {

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
        IJEnvoyerAnnoncesViewBean eaViewBean = (IJEnvoyerAnnoncesViewBean) viewBean;

        IJEnvoyerAnnoncesProcess process = new IJEnvoyerAnnoncesProcess((BSession) session);

        process.setEMailAddress(eaViewBean.getEMailAddress());
        process.setForDateEnvoi(eaViewBean.getForDateEnvoi());
        process.setForMoisAnneeComptable(eaViewBean.getForMoisAnneeComptable());

        process.start();
    }
}
