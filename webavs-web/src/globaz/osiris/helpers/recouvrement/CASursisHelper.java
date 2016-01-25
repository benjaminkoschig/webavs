/**
 *
 */
package globaz.osiris.helpers.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.osiris.helpers.recouvrement.process.CAProcessSursis;
import globaz.osiris.vb.recouvrement.CASursisEcheanceViewBean;

/**
 * @author SEL
 */
public class CASursisHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CASursisEcheanceViewBean vb = (CASursisEcheanceViewBean) viewBean;

        if (viewBean instanceof CASursisEcheanceViewBean) {
            CAProcessSursis process = new CAProcessSursis();
            process.setSession(vb.getSession());
            process.setIdEcheance(vb.getIdEcheance());
            process.setIdPlanRecouvrement(vb.getIdPlanRecouvrement());

            if ("supprimerSuivants".equals(action.getActionPart())) {
                process.setForDeleteEcheance(true);
            }
            if ("calculSuivants".equals(action.getActionPart())) {
                process.setForComputeEcheance(true);
            }

            try {
                process.executeProcess();
            } catch (Exception e) {
                vb.setMessage("Unable to start process!!! Reason : " + e.toString());
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }

        return vb;
    }

}
