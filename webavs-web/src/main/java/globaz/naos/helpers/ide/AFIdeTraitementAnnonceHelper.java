package globaz.naos.helpers.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.naos.db.ide.AFIdeTraitementAnnonceViewBean;
import globaz.naos.process.ide.AFIdeTraitementAnnonceProcess;

public class AFIdeTraitementAnnonceHelper extends FWHelper {

    public AFIdeTraitementAnnonceHelper() {
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            AFIdeTraitementAnnonceViewBean vb = (AFIdeTraitementAnnonceViewBean) viewBean;

            AFIdeTraitementAnnonceProcess process = new AFIdeTraitementAnnonceProcess();

            process.setSession((BSession) session);
            process.setForTypeTraitement(vb.getForTypeTraitement());
            process.setEMailAddress(vb.getEmail());
            process.start();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
