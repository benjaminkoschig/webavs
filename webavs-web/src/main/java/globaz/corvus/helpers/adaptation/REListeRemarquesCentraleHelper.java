package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REListeRemarquesCentraleProcess;
import globaz.corvus.vb.adaptation.REListeRemarquesCentraleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * Helper pour la recherche des remarques de la centrale
 *
 * @author ESVE | Créé le 26 août 2020
 *
 */
public class REListeRemarquesCentraleHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REListeRemarquesCentraleViewBean vb = (REListeRemarquesCentraleViewBean) viewBean;

        REListeRemarquesCentraleProcess process = new REListeRemarquesCentraleProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.start();

    }

}
