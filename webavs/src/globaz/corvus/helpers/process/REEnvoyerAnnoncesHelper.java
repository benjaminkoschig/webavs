/*
 * Créé le 17 juillet 2008
 */
package globaz.corvus.helpers.process;

import globaz.corvus.process.REEnvoyerAnnoncesProcess;
import globaz.corvus.vb.process.REEnvoyerAnnoncesViewBean;
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
public class REEnvoyerAnnoncesHelper extends FWHelper {

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
        REEnvoyerAnnoncesViewBean eaViewBean = (REEnvoyerAnnoncesViewBean) viewBean;

        REEnvoyerAnnoncesProcess process = new REEnvoyerAnnoncesProcess((BSession) session);

        process.setEMailAddress(eaViewBean.getEMailAddress());
        process.setForDateEnvoi(eaViewBean.getForDateEnvoi());
        process.setForMoisAnneeComptable(eaViewBean.getForMoisAnneeComptable());
        process.setIsForAnnoncesSubsequentes(false);
        process.start();
    }
}
