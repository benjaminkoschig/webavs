package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.process.controleEmployeur.CEControles5PourCentProcess;
import globaz.hercule.vb.controleEmployeur.CEControles5PourCentViewBean;

/**
 * @author JPA
 * @since 3 août 2010
 */
public class CEControles5PourCentHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof CEControles5PourCentViewBean) {
            CEControles5PourCentProcess process = new CEControles5PourCentProcess();
            CEControles5PourCentViewBean listeBean = (CEControles5PourCentViewBean) viewBean;
            process.setISession(session);
            process.setAnnee(listeBean.getAnnee());
            process.setEMailAddress(listeBean.geteMailAddress());
            process.setTypeAdresse(listeBean.getTypeAdresse());
            try {
                BProcessLauncher.start(process);
            } catch (Exception e) {
                listeBean.setMessage(e.toString());
                listeBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }

    }

}
