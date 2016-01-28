package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.vb.prononces.IJSaisirEcheanceViewBean;
import globaz.prestation.helpers.PRAbstractHelper;

public class IJSaisirEcheanceHelper extends PRAbstractHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        return deleguerExecute(viewBean, action, session);
    }

    public FWViewBeanInterface modiferEcheance(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        IJSaisirEcheanceViewBean vb = (IJSaisirEcheanceViewBean) viewBean;
        vb.update();

        return viewBean;
    }

    public FWViewBeanInterface saisirEcheance(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        return viewBean;
    }

}
