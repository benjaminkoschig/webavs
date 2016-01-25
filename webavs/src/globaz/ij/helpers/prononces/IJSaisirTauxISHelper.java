package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.vb.prononces.IJSaisirTauxISViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class IJSaisirTauxISHelper extends PRAbstractHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        return deleguerExecute(viewBean, action, session);
    }

    public FWViewBeanInterface modiferTauxIS(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        IJSaisirTauxISViewBean vb = (IJSaisirTauxISViewBean) viewBean;

        if (!JadeStringUtil.isBlankOrZero(vb.getCsCantonImpositionSource())
                || !JadeStringUtil.isBlankOrZero(vb.getTauxImpositionSource())) {

            vb.setSoumisImpotSource(Boolean.TRUE);
        } else {
            vb.setSoumisImpotSource(Boolean.FALSE);
        }

        vb.update();

        return viewBean;
    }

    public FWViewBeanInterface saisirTauxIS(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        return viewBean;
    }
}
