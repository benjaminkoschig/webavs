package globaz.orion.helpers.partnerWeb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.orion.process.EBGenererListContact;
import globaz.orion.vb.partnerWeb.EBListeDesContactsViewBean;

public class EBListeDesContactsHelper extends FWHelper {

    public EBListeDesContactsHelper() {
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBListeDesContactsViewBean vb = (EBListeDesContactsViewBean) viewBean;

        try {
            EBGenererListContact process = new EBGenererListContact();
            process.setSession((BSession) session);
            process.setEmail(vb.getEmail());
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}