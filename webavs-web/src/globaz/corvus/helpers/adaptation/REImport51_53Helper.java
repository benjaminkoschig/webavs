package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REImportAnnonces51_53Process;
import globaz.corvus.vb.adaptation.REImport51_53ViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author HPE
 * 
 */
public class REImport51_53Helper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REImport51_53ViewBean vb = (REImport51_53ViewBean) viewBean;

        REImportAnnonces51_53Process process = new REImportAnnonces51_53Process();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setIdLot(vb.getIdLot());
        process.start();

    }

}
