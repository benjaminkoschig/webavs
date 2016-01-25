package globaz.hercule.helpers.groupement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.hercule.db.groupement.CEGroupeViewBean;

/**
 * @author JMC
 * @since 08 juin 2010
 */
public class CEMembreHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_chercher(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._chercher(viewBean, action, session);
        ((CEGroupeViewBean) viewBean).retrieve();
    }
}
