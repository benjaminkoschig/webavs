package globaz.hercule.helpers.couverture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.hercule.db.couverture.CECouvertureEcranViewBean;
import globaz.hercule.service.CECouvertureService;

/**
 * Helpers permettant la gestion des couvertures
 * 
 * @author SCO
 * @since 1 sept. 2010
 */
public class CECouvertureEcranHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CECouvertureEcranViewBean couvertureViewBean = (CECouvertureEcranViewBean) viewBean;
        CECouvertureService.createCouverture((BSession) session, couvertureViewBean.getIdAffilie(),
                couvertureViewBean.getNumAffilie(), couvertureViewBean.getAnnee(),
                couvertureViewBean.isCouvertureActive());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_delete(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CECouvertureEcranViewBean vb = (CECouvertureEcranViewBean) viewBean;
        CECouvertureService.deleteCouverture((BSession) session, vb.getIdCouverture());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CECouvertureEcranViewBean vb = (CECouvertureEcranViewBean) viewBean;
        CECouvertureService.updateCouverture((BSession) session, vb.getIdCouverture(), vb.getAnnee(),
                vb.isCouvertureActive());
    }

}
