/*
 *
 */
package globaz.apg.helpers.droits;

import globaz.apg.process.APGenererDroitPandemieMensuelProcess;
import globaz.apg.vb.process.APGenererDroitPandemieFinDroitViewBean;
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
public class APFinDroitHelper extends FWHelper {

    /**
     * (non javadoc)
     *
     * @see FWHelper#_start(FWViewBeanInterface,
     *      FWAction, BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface bean, FWAction action, BISession session) {
        APGenererDroitPandemieFinDroitViewBean viewBean = (APGenererDroitPandemieFinDroitViewBean) bean;
        APGenererDroitPandemieMensuelProcess process = new APGenererDroitPandemieMensuelProcess((BSession) session);
        process.setEMailAddress(viewBean.getEMailAddress());
        process.setDateFin(viewBean.getDateFin());
        process.setIdDroit(viewBean.getIdDroit());

        process.start();
    }
}
