package globaz.corvus.helpers.process;

import globaz.corvus.process.REConcordanceCentraleProcess;
import globaz.corvus.vb.process.REConcordanceCentraleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REConcordanceCentraleHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {

            REConcordanceCentraleViewBean vBean = (REConcordanceCentraleViewBean) viewBean;

            REConcordanceCentraleProcess process = new REConcordanceCentraleProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(vBean.getEMailAddress());
            process.setMoisAnnee_MMxAAAA(vBean.getMoisAnnee());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);

            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}