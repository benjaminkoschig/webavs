package globaz.phenix.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.phenix.process.decision.CPProcessDecisionRecomptabiliser;
import globaz.phenix.process.decision.CPProcessReporterDecisionPreEncodee;
import globaz.phenix.vb.decision.CPDecisionRecomptabiliserViewBean;
import globaz.phenix.vb.decision.CPDecisionReporterViewBean;

public class CPDecisionHelper extends FWHelper {

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        if ("executerReporter".equals(action.getActionPart()) && (viewBean instanceof CPDecisionReporterViewBean)) {
            CPDecisionReporterViewBean vb = (CPDecisionReporterViewBean) viewBean;

            CPProcessReporterDecisionPreEncodee process = new CPProcessReporterDecisionPreEncodee();
            process.setIdPassage(vb.getIdPassage());
            process.setEMailAddress(vb.getEMailAddress());
            process.setSession(vb.getSession());
            process.start();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        if ("executerRecomptabiliser".startsWith(action.getActionPart())
                && (viewBean instanceof CPDecisionRecomptabiliserViewBean)) {
            CPDecisionRecomptabiliserViewBean vb = (CPDecisionRecomptabiliserViewBean) viewBean;

            CPProcessDecisionRecomptabiliser process = new CPProcessDecisionRecomptabiliser();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdDecision(vb.getIdDecision());
            process.setWantMajCI(vb.getWantMajCI());
            process.setSession(vb.getSession());
            process.start();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }
        return super.execute(viewBean, action, session);
    }

}
