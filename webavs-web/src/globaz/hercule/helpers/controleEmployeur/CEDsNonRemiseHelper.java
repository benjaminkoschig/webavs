package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.process.controleEmployeur.CEDsNonRemiseProcess;
import globaz.hercule.vb.controleEmployeur.CEDsNonRemiseViewBean;

/**
 * @author JPA
 * @since 3 août 2010
 */
public class CEDsNonRemiseHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof CEDsNonRemiseViewBean) {
            CEDsNonRemiseProcess process = new CEDsNonRemiseProcess();
            CEDsNonRemiseViewBean listeBean = (CEDsNonRemiseViewBean) viewBean;
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
