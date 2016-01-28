package globaz.naos.helpers.decisionCotisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.decisionCotisations.AFDecisionCotisationsViewBean;
import globaz.naos.process.AFDecisionCotisationsProcess;

public class AFDecisionCotisationsHelper extends FWHelper {

    public AFDecisionCotisationsHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFDecisionCotisationsViewBean asViewBean = (AFDecisionCotisationsViewBean) viewBean;
        AFDecisionCotisationsProcess process = new AFDecisionCotisationsProcess();

        process.setAffiliationId(asViewBean.getAffiliationId());
        process.setDateEnvoi(asViewBean.getDateEnvoi());
        process.setDateDebut(asViewBean.getDateDebut());
        process.setDateFin(asViewBean.getDateFin());
        process.setPlanAffiliationId(asViewBean.getPlanAffiliationId());
        process.setEMailAddress(asViewBean.getEmail());
        process.setFromIdExterneRole(asViewBean.getFromIdExterneRole());
        process.setTillIdExterneRole(asViewBean.getTillIdExterneRole());
        process.setDateImprime(asViewBean.getDateImprime());
        process.setISession(session);

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

}
