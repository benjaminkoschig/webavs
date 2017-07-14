/**
 * 
 */
package globaz.naos.helpers.noga;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.naos.db.noga.AFListeAffiliesCodeNogaViewBean;
import ch.globaz.naos.liste.process.AFAffilieCodeNOGAProcess;

/**
 * @author est
 * 
 */
public class AFListeAffiliesCodeNogaHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {

            AFListeAffiliesCodeNogaViewBean vb = (AFListeAffiliesCodeNogaViewBean) viewBean;

            AFAffilieCodeNOGAProcess process = new AFAffilieCodeNOGAProcess();

            process.setSession((BSession) session);
            process.setIdCScodeNoga(vb.getCodeNoga());
            process.setIsOnlyAffiliesActifs(vb.getIsOnlyAffiliesActifs());
            process.setEMailAddress(vb.getEmail());
            process.start();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
