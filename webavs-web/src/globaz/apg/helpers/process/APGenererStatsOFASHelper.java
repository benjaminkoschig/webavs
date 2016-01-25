/*
 * Créé le 15.09.2006
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APGenererStatsOFASProcess;
import globaz.apg.vb.process.APGenererStatsOFASViewBean;
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
public class APGenererStatsOFASHelper extends FWHelper {

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
        APGenererStatsOFASViewBean eaViewBean = (APGenererStatsOFASViewBean) viewBean;

        APGenererStatsOFASProcess process = new APGenererStatsOFASProcess((BSession) session);

        process.setEMailAddress(eaViewBean.getEMailAddress());
        process.setForAnnee(eaViewBean.getForAnnee());

        process.start();
    }
}
