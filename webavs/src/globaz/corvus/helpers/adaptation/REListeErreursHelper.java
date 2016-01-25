package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REListeErreursProcess;
import globaz.corvus.vb.adaptation.REListeErreursViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REListeErreursHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REListeErreursViewBean vb = (REListeErreursViewBean) viewBean;

        REListeErreursProcess process = new REListeErreursProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.setPourcentDe(vb.getPourcentDe());
        process.setPourcentA(vb.getPourcentA());

        if (JadeStringUtil.isBlankOrZero(vb.getPourcentDe()) && JadeStringUtil.isBlankOrZero(vb.getPourcentA())) {
            process.setWantListePourcent(false);
        } else {
            process.setWantListePourcent(true);
        }

        process.start();

    }

}
