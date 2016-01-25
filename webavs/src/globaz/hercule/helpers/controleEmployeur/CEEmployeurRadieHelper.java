package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CEEmployeurRadieViewBean;
import globaz.hercule.process.CEEmployeurRadieProcess;
import globaz.jade.log.JadeLogger;

/**
 * @author MMO
 * @since 3 août 2010
 */
public class CEEmployeurRadieHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEEmployeurRadieViewBean vb = (CEEmployeurRadieViewBean) viewBean;

        try {
            CEEmployeurRadieProcess process = new CEEmployeurRadieProcess();
            process.setSession((BSession) session);
            process.setFromDateRadiation(vb.getFromDateRadiation());
            process.setToDateRadiation(vb.getToDateRadiation());
            process.setFromMasseSalariale(vb.getFromMasseSalariale());
            process.setToMasseSalariale(vb.getToMasseSalariale());
            process.setTypeAdresse(vb.getTypeAdresse());
            process.setForMotifRadiation(vb.getForMotifRadiation());
            process.setEMailAddress(vb.getEmail());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
