package globaz.hercule.helpers.statOfas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.statOfas.CEStatOfasViewBean;
import globaz.hercule.process.statOfas.CEStatOfasProcess;

/**
 * @author bjo
 * 
 */
public class CEStatOfasHelper extends FWHelper {

    public CEStatOfasHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CEStatOfasViewBean statOfasViewBean = (CEStatOfasViewBean) viewBean;
        CEStatOfasProcess process = new CEStatOfasProcess();

        process.setISession(session);
        process.setAnnee(statOfasViewBean.getAnnee());
        process.setEMailAddress(statOfasViewBean.getEmail());

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            statOfasViewBean.setMessage(e.toString());
            statOfasViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
