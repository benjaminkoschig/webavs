package globaz.naos.helpers.masse;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.masse.AFTrimestrielleAvecMasseSuppViewBean;
import globaz.naos.process.AFTrimestrielleAvecMasseSuppProcess;

/**
 * Helper pour la liste des affiliés paritaires actifs avec masse AVS > 200'000.- et périodicité trimestrielle.
 * 
 * @author SEL <br>
 *         Date : 19 févr. 08
 */
public class AFTrimestrielleAvecMasseSuppHelper extends FWHelper {

    /**
     * Crée une nouvelle instance de la classe AFTrimestrielleAvecMasseSuppHelper.
     */
    public AFTrimestrielleAvecMasseSuppHelper() {
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFTrimestrielleAvecMasseSuppViewBean anViewBean = (AFTrimestrielleAvecMasseSuppViewBean) viewBean;

        try {
            AFTrimestrielleAvecMasseSuppProcess doc = new AFTrimestrielleAvecMasseSuppProcess();
            doc.setSession(anViewBean.getSession());
            doc.setEMailAddress(anViewBean.getEmail());

            BProcessLauncher.start(doc);
        } catch (Exception e) {
            anViewBean.setMessage(e.getMessage());
            anViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
