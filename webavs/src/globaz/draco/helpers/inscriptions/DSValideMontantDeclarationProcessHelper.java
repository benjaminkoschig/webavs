package globaz.draco.helpers.inscriptions;

import globaz.draco.db.inscriptions.DSValideMontantDeclarationProcess;
import globaz.draco.db.inscriptions.DSValideMontantDeclarationProcessViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;

public class DSValideMontantDeclarationProcessHelper extends FWHelper {

    public DSValideMontantDeclarationProcessHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        DSValideMontantDeclarationProcessViewBean vb = (DSValideMontantDeclarationProcessViewBean) viewBean;
        try {
            DSValideMontantDeclarationProcess process = new DSValideMontantDeclarationProcess();
            process.setEMailAddress(vb.getEMailAddress());
            process.setSession((BSession) session);
            process.setIdDeclaration(vb.getIdDeclaration());
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
