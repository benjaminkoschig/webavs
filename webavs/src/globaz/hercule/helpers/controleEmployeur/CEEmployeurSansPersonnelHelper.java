package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CEEmployeurSansPersonnelViewBean;
import globaz.hercule.process.CEEmployeurSansPersonnelProcess;
import globaz.jade.log.JadeLogger;

/**
 * @author MMO
 * @since 30 juillet 2010
 */
public class CEEmployeurSansPersonnelHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEEmployeurSansPersonnelViewBean vb = (CEEmployeurSansPersonnelViewBean) viewBean;

        try {
            CEEmployeurSansPersonnelProcess process = new CEEmployeurSansPersonnelProcess();
            process.setSession((BSession) session);
            process.setForAnnee(vb.getForAnnee());
            process.setTypeAdresse(vb.getTypeAdresse());
            process.setEMailAddress(vb.getEmail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
