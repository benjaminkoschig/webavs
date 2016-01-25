package globaz.corvus.helpers.process;

import globaz.corvus.process.RETerminerDemandeRentePrevBilProcess;
import globaz.corvus.vb.process.RETerminerDemandeRentePrevBilViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class RETerminerDemandeRentePrevBilHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        RETerminerDemandeRentePrevBilViewBean gldaViewBean = (RETerminerDemandeRentePrevBilViewBean) viewBean;

        try {

            RETerminerDemandeRentePrevBilProcess process = new RETerminerDemandeRentePrevBilProcess((BSession) session);
            process.setSendCompletionMail(false);
            process.setSendMailOnError(true);
            process.setIdDemandeRente(gldaViewBean.getIdDemandeRente());
            process.setDateEnvoiCalcul(gldaViewBean.getDateEnvoiCalcul());

            // Conformément au BZ 7342, le process est maintenant lancé de manière synchrone
            process.executeProcess();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
