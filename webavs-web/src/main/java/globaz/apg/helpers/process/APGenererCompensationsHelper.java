/*
 * Créé le 12 juil. 05
 */
package globaz.apg.helpers.process;

import globaz.apg.api.process.IAPGenererCompensationProcess;
import globaz.apg.impl.process.APGenererCompensationsProcessPandemie;
import globaz.apg.vb.process.APGenererCompensationsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.prestation.api.IPRDemande;

import java.rmi.RemoteException;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererCompensationsHelper extends FWHelper {

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
        APGenererCompensationsViewBean gcViewBean = (APGenererCompensationsViewBean) viewBean;

        try {

            IAPGenererCompensationProcess process;
            if(gcViewBean.getTypePrestation().equals(IPRDemande.CS_TYPE_PANDEMIE)) {
                process = new APGenererCompensationsProcessPandemie((BSession) session);
            } else {
                process = (IAPGenererCompensationProcess) GlobazSystem.getApplication(
                    session.getApplicationId()).getImplementationFor(session, IAPGenererCompensationProcess.class);
            }

            if (!(process instanceof BProcess)) {
                throw new Exception("Class cast Exception : " + process.getClass().getName()
                        + "Must extend BProcess !!!");
            }

            ((BProcess) process).setEMailAddress(gcViewBean.getEMailAddress());
            process.setForIdLot(gcViewBean.getForIdLot());
            process.setMoisPeriodeFacturation(gcViewBean.getMoisPeriodeFacturation());
            ((BProcess) process).start();

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
