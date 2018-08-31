package globaz.vulpecula.helpers.ctrlemployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.ctrlemployeur.PTControleEmployeurParAnneeViewBean;
import ch.globaz.vulpecula.documents.ctrlemployeur.ListeControleEmployeurParAnneePrinter;

public class PTControleEmployeurParAnneeHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTControleEmployeurParAnneeViewBean vb = (PTControleEmployeurParAnneeViewBean) viewBean;

            ListeControleEmployeurParAnneePrinter process = new ListeControleEmployeurParAnneePrinter();

            process.setEMailAddress(vb.getEmail());
            process.setAnnee(vb.getAnnee());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
