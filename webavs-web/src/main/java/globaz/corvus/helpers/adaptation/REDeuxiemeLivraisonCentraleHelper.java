package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REDeuxiemeLivraisonCentraleProcess;
import globaz.corvus.vb.adaptation.REDeuxiemeLivraisonCentraleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REDeuxiemeLivraisonCentraleHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REDeuxiemeLivraisonCentraleViewBean vb = (REDeuxiemeLivraisonCentraleViewBean) viewBean;

        REDeuxiemeLivraisonCentraleProcess process = new REDeuxiemeLivraisonCentraleProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.setIdLot(vb.getIdLot());
        process.start();

    }

}
