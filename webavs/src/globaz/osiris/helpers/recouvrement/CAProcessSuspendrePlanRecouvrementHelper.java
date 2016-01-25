package globaz.osiris.helpers.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.osiris.db.recouvrement.CAProcessSuspendrePlanRecouvrementViewBean;
import globaz.osiris.process.CAProcessSuspendrePlanRecouvrement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CAProcessSuspendrePlanRecouvrementHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CAProcessSuspendrePlanRecouvrementViewBean psViewBean = (CAProcessSuspendrePlanRecouvrementViewBean) viewBean;
        CAProcessSuspendrePlanRecouvrement process = new CAProcessSuspendrePlanRecouvrement();

        process.setEMailAddress(psViewBean.getEmail());
        process.setDateSuspension(psViewBean.getDateSuspension());
        process.setIdPlanRecouvrement(psViewBean.getIdPlanRecouvrement());
        process.setSendSommation(psViewBean.getSendSommation());
        process.setISession(session);

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMessage(viewBean.getMessage() + "\n" + e.getMessage());
            viewBean.setMessage(FWViewBeanInterface.ERROR);
        }
    }
}
