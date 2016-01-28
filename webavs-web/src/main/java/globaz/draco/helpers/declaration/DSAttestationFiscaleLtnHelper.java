package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSAttestationFiscaleLtnViewBean;
import globaz.draco.print.itext.DSAttestationFiscaleLtn_Doc;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class DSAttestationFiscaleLtnHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof DSAttestationFiscaleLtnViewBean) {
            DSAttestationFiscaleLtnViewBean vb = (DSAttestationFiscaleLtnViewBean) viewBean;
            try {
                DSAttestationFiscaleLtn_Doc process = new DSAttestationFiscaleLtn_Doc();

                process.setSession(vb.getSession());
                process.setSimulation(vb.isSimulation());
                process.setDateValeur(vb.getDateValeur());
                process.setEMailAddress(vb.getEmailAddress());
                process.setIdDeclaration(vb.getId());
                process.start();
            } catch (Exception e) {
                vb.setMessage("Unable to start process!!! Reason : " + e.toString());
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

}
