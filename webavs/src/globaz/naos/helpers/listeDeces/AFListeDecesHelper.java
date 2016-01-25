package globaz.naos.helpers.listeDeces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.listeDeces.AFListeDecesViewBean;
import globaz.naos.process.AFListeDecesProcess;

public class AFListeDecesHelper extends FWHelper {

    public AFListeDecesHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFListeDecesViewBean asViewBean = (AFListeDecesViewBean) viewBean;
        AFListeDecesProcess process = new AFListeDecesProcess();

        process.setDateDeces(asViewBean.getDateDeces());
        process.setEMailAddress(asViewBean.getEmail());
        process.setISession(session);

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
