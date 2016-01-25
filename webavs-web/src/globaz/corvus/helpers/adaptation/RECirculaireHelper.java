package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.RECirculaireRentiersProcess;
import globaz.corvus.vb.adaptation.RECirculaireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class RECirculaireHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        RECirculaireViewBean vb = (RECirculaireViewBean) viewBean;

        RECirculaireRentiersProcess process = new RECirculaireRentiersProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
