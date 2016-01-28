package globaz.hercule.helpers.reviseur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.reviseur.CEReviseurABlancViewBean;
import globaz.hercule.process.reviseur.CEReviseurABlancProcess;
import globaz.jade.log.JadeLogger;

/**
 * @author MMO
 * @since 9 août 2010
 */
public class CEReviseurABlancHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEReviseurABlancViewBean vb = (CEReviseurABlancViewBean) viewBean;

        try {
            CEReviseurABlancProcess process = new CEReviseurABlancProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmail());
            process.setIdReviseur(vb.getIdReviseur());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
