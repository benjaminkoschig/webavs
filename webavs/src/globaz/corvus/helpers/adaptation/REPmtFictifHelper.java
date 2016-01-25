package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REPmtFictifProcess;
import globaz.corvus.vb.adaptation.REPmtFictifViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REPmtFictifHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REPmtFictifViewBean vb = (REPmtFictifViewBean) viewBean;

        REPmtFictifProcess process = new REPmtFictifProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
