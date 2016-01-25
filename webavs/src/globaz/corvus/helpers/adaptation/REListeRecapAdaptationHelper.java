package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REListeRecapAdaptationProcess;
import globaz.corvus.vb.adaptation.REListeRecapAdaptationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REListeRecapAdaptationHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REListeRecapAdaptationViewBean vb = (REListeRecapAdaptationViewBean) viewBean;

        REListeRecapAdaptationProcess process = new REListeRecapAdaptationProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
