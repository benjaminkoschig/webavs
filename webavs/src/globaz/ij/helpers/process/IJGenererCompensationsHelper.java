/*
 * Créé le 12 juil. 05
 */
package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.ij.api.process.IIJGenererCompensationProcess;
import globaz.ij.vb.process.IJGenererCompensationsViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererCompensationsHelper extends FWHelper {

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

        IJGenererCompensationsViewBean gcViewBean = (IJGenererCompensationsViewBean) viewBean;

        try {

            IIJGenererCompensationProcess process = (IIJGenererCompensationProcess) GlobazSystem.getApplication(
                    session.getApplicationId()).getImplementationFor(session, IIJGenererCompensationProcess.class);

            if (!(process instanceof BProcess)) {
                throw new Exception("Class cast Exception : " + process.getClass().getName()
                        + "Must extend BProcess !!!");
            }

            ((BProcess) process).setEMailAddress(gcViewBean.getEMailAddress());
            process.setForIdLot(gcViewBean.getForIdLot());
            process.setMoisPeriodeFacturation(gcViewBean.getMoisPeriodeFacturation());
            ((BProcess) process).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
