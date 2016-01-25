package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CEEmployeurMasseSalarialeViewBean;
import globaz.hercule.process.CEEmployeurMasseSalarialeProcess;
import globaz.jade.log.JadeLogger;

/**
 * @author MMO
 * @since 23 juillet 2010
 */
public class CEEmployeurMasseSalarialeHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEEmployeurMasseSalarialeViewBean vb = (CEEmployeurMasseSalarialeViewBean) viewBean;

        try {
            CEEmployeurMasseSalarialeProcess process = new CEEmployeurMasseSalarialeProcess();
            process.setSession((BSession) session);
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setToNumAffilie(vb.getToNumAffilie());
            process.setForAnnee(vb.getForAnnee());
            process.setTypeAdresse(vb.getTypeAdresse());
            process.setEMailAddress(vb.getEmail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
    }

}
