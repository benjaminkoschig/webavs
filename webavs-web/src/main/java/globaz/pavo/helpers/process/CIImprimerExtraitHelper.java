package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hermes.application.HEApplication;
import globaz.hermes.print.itext.HEExtrait95Complet_Doc;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.process.CIImprimerExtraitViewBean;

public class CIImprimerExtraitHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIImprimerExtraitViewBean vb = (CIImprimerExtraitViewBean) viewBean;
        try {
            HEExtrait95Complet_Doc process = new HEExtrait95Complet_Doc(vb.getSession());
            process.setReferenceUniqueVector(vb.getRefUniques());
            process.setEMailAddress(vb.getEmailAddress());
            BSession sessionHermes = new BSession(HEApplication.DEFAULT_APPLICATION_HERMES);
            vb.getSession().connectSession(sessionHermes);
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
