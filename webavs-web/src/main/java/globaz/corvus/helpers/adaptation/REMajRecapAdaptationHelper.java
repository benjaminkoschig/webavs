package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REMajRecapAdaptationProcess;
import globaz.corvus.vb.adaptation.REMajRecapAdaptationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REMajRecapAdaptationHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REMajRecapAdaptationViewBean vb = (REMajRecapAdaptationViewBean) viewBean;

        REMajRecapAdaptationProcess process = new REMajRecapAdaptationProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
