package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSValidationViewBean;
import globaz.draco.process.DSProcessValidation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;

public class DSValidationHelper extends FWHelper {

    public DSValidationHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        DSValidationViewBean vb = (DSValidationViewBean) viewBean;
        try {
            DSProcessValidation process = new DSProcessValidation();
            process.setEMailAddress(vb.getEMailAddress());
            process.setNotImpressionDecFinalAZero(vb.getNotImpressionDecFinalAZero());
            process.setSession((BSession) session);
            process.setIdDeclaration(vb.getIdDeclaration());
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    };

}
