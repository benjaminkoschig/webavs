package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSAnnulerValidationViewBean;
import globaz.draco.process.DSProcessAnnulerValidation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;

public class DSAnnulerValidationHelper extends FWHelper {

    public DSAnnulerValidationHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        DSAnnulerValidationViewBean vb = (DSAnnulerValidationViewBean) viewBean;
        try {
            DSProcessAnnulerValidation process = new DSProcessAnnulerValidation();
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
