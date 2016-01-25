package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIEcritureGenre6ViewBean;
import globaz.pavo.process.CIEcritureGenre6Process;

public class CIEcritureGenre6Helper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // TODO Auto-generated method stub
        CIEcritureGenre6ViewBean vb = (CIEcritureGenre6ViewBean) viewBean;
        try {
            CIEcritureGenre6Process process = new CIEcritureGenre6Process();

            process.setEMailAddress(vb.getEmailAddress());
            process.setAvs(vb.getAvs());
            process.setSession((BSession) session);
            process.setIdEcriture(vb.getId());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
            e.printStackTrace();
        }
    }

}
