package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSImprimerDeclarationViewBean;
import globaz.draco.print.itext.DSImpressionDeclaration;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;

public class DSImprimerDeclarationHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        DSImprimerDeclarationViewBean vb = (DSImprimerDeclarationViewBean) viewBean;
        try {
            DSImpressionDeclaration process = new DSImpressionDeclaration();
            process.setEMailAddress(vb.getEmailAddress());
            process.setIdDeclaration(vb.getId());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
            e.printStackTrace();
        }
    }
}
