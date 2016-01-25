package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REMiseAJourPrestProcess;
import globaz.corvus.vb.adaptation.REMiseAJourPrestViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REMiseAJourPrestHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REMiseAJourPrestViewBean vb = (REMiseAJourPrestViewBean) viewBean;

        REMiseAJourPrestProcess process = new REMiseAJourPrestProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
