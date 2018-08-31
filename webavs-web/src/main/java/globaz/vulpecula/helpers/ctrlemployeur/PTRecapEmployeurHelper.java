package globaz.vulpecula.helpers.ctrlemployeur;

import ch.globaz.vulpecula.documents.ctrlemployeur.DocumentRecapEmployeurPrinter;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.ctrlemployeur.PTRecapEmployeurViewBean;

public class PTRecapEmployeurHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTRecapEmployeurViewBean vb = (PTRecapEmployeurViewBean) viewBean;

            DocumentRecapEmployeurPrinter process = new DocumentRecapEmployeurPrinter();

            process.setEMailAddress(vb.getEmail());
            process.setIdEmployeur(vb.getIdEmployeur());
            process.setDateReference(vb.getDateReference());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
