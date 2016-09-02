package globaz.naos.helpers.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.taxeCo2.AFFigerTaxeCo2ViewBean;
import globaz.naos.process.taxeCo2.AFProcessFigerTaxeCo2;

public class AFFigerTaxeCo2Helper extends FWHelper {

    public AFFigerTaxeCo2Helper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof AFFigerTaxeCo2ViewBean) {
            AFFigerTaxeCo2ViewBean asViewBean = (AFFigerTaxeCo2ViewBean) viewBean;
            asViewBean._validate();
            if (!FWViewBeanInterface.ERROR.equals(asViewBean.getMsgType())) {
                AFProcessFigerTaxeCo2 process = new AFProcessFigerTaxeCo2();
                process.setAnneeMasse(asViewBean.getAnneeMasse());
                process.setAnneeRedistribution(asViewBean.getAnneeRedistri());
                process.setEMailAddress(asViewBean.getEmail());
                process.setReinitialiser(asViewBean.getReinitialiser());
                process.setISession(session);
                try {
                    BProcessLauncher.start(process);
                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.toString());
                }
            }
        } else {
            super._start(viewBean, action, session);
        }
    }
}
