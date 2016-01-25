package globaz.musca.helpers.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.process.FAInfoRom200GenerationComptabilisationPassageProcess;

/**
 * @author MMO
 * @since 27 juillet 2010
 */
public class FATestFacturationJournaliereHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            FAInfoRom200GenerationComptabilisationPassageProcess process = new FAInfoRom200GenerationComptabilisationPassageProcess();
            process.setSession((BSession) session);
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
