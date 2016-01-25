package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSAttestationFiscaleLtnGenViewBean;
import globaz.draco.process.DSAttestationFiscaleLtnGenProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class DSAttestationFiscaleLtnGenHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof DSAttestationFiscaleLtnGenViewBean) {
            DSAttestationFiscaleLtnGenViewBean vb = (DSAttestationFiscaleLtnGenViewBean) viewBean;
            DSAttestationFiscaleLtnGenProcess process = new DSAttestationFiscaleLtnGenProcess();
            process.setSession(vb.getSession());
            process.setSimulation(vb.isSimulation());
            process.setAffilieTous(vb.isAffilieTous());
            process.setFromAffilies(vb.getFromAffilies());
            process.setUntilAffilies(vb.getUntilAffilies());
            process.setAnnee(vb.getAnnee());
            process.setDateImpression(vb.getDateImpression());
            process.setDateValeur(vb.getDateValeur());
            process.setEMailAddress(vb.getEmailAddress());

            try {
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
