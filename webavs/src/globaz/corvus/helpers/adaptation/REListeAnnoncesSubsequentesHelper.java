package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REListeAnnoncesSubsequentesProcess;
import globaz.corvus.vb.adaptation.REListeAnnoncesSubsequentesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REListeAnnoncesSubsequentesHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REListeAnnoncesSubsequentesViewBean vb = (REListeAnnoncesSubsequentesViewBean) viewBean;

        REListeAnnoncesSubsequentesProcess process = new REListeAnnoncesSubsequentesProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
