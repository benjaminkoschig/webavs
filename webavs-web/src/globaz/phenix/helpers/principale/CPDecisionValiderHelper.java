package globaz.phenix.helpers.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.db.principale.CPDecisionValiderViewBean;
import globaz.phenix.db.principale.CPEnteteViewBean;

public class CPDecisionValiderHelper extends FWHelper {

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        if (viewBean instanceof CPEnteteViewBean) {
            ((CPEnteteViewBean) viewBean).retrieve();
        } else {
            super._chercher(viewBean, action, session);
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof CPDecisionValiderViewBean) {
            ((CPDecisionValiderViewBean) viewBean).update();
        } else {
            super._update(viewBean, action, session);
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("modifierProvenanceJournalRetour".equals(action.getActionPart())
                && viewBean instanceof CPDecisionValiderViewBean) {
            try {
                ((CPDecisionValiderViewBean) viewBean).update();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return super.execute(viewBean, action, session);
    }

}
