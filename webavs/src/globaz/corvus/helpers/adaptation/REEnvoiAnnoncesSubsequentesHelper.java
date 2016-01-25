package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REEnvoiAnnoncesSubsequentesProcess;
import globaz.corvus.vb.adaptation.REEnvoiAnnoncesSubsequentesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REEnvoiAnnoncesSubsequentesHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REEnvoiAnnoncesSubsequentesViewBean vb = (REEnvoiAnnoncesSubsequentesViewBean) viewBean;

        REEnvoiAnnoncesSubsequentesProcess process = new REEnvoiAnnoncesSubsequentesProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
