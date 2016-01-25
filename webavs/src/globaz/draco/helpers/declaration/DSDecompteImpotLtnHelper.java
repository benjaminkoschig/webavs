package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSDecompteImpotLtnViewBean;
import globaz.draco.process.DSDecompteImpotLtnProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class DSDecompteImpotLtnHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof DSDecompteImpotLtnViewBean) {

            DSDecompteImpotLtnViewBean vb = null;
            try {
                vb = (DSDecompteImpotLtnViewBean) viewBean;
                DSDecompteImpotLtnProcess process = new DSDecompteImpotLtnProcess();
                process.setSession(vb.getSession());
                process.setSimulation(vb.isSimulation());
                process.setReImpression(vb.isReImpression());
                process.setCantonSelectionne(vb.getCantonSelectionne());
                process.setDateImpression(vb.getDateImpression());
                process.setAnnee(vb.getAnnee());
                process.setDateValeur(vb.getDateValeur());
                process.setTypeImpression(vb.getTypeImpression());
                process.setEMailAddress(vb.getEmailAddress());
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
