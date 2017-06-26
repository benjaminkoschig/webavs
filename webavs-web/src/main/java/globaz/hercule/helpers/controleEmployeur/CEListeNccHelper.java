/*
 * Globaz SA.
 */
package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.process.controleEmployeur.CEListeNccProcess;
import globaz.hercule.vb.controleEmployeur.CEListeNccViewBean;

public class CEListeNccHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof CEListeNccViewBean) {
            CEListeNccProcess process = new CEListeNccProcess();
            CEListeNccViewBean listeBean = (CEListeNccViewBean) viewBean;
            process.setISession(session);
            process.setAnnee(listeBean.getAnnee());
            process.setEMailAddress(listeBean.geteMailAddress());

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
