package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CEEmployeurChangementMasseSalarialeViewBean;
import globaz.hercule.process.CEEmployeurChangementMasseSalarialeProcess;
import globaz.jade.log.JadeLogger;

/**
 * @author MMO
 * @since 27 juillet 2010
 */
public class CEEmployeurChangementMasseSalarialeHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEEmployeurChangementMasseSalarialeViewBean vb = (CEEmployeurChangementMasseSalarialeViewBean) viewBean;

        try {
            CEEmployeurChangementMasseSalarialeProcess process = new CEEmployeurChangementMasseSalarialeProcess();
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
