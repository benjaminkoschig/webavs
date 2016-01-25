/*
 * Créé le 16 juillet 08
 */

package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.basescalcul.IREFactureARestituer;
import globaz.corvus.vb.rentesaccordees.REFactureARestituerViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author HPE
 * 
 */

public class REFactureARestituerHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionChangeEtat(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        REFactureARestituerViewBean farViewBean = (REFactureARestituerViewBean) viewBean;

        if (IREFactureARestituer.CS_ATTENTE.equals(farViewBean.getCsEtat())) {
            farViewBean.setCsEtat(IREFactureARestituer.CS_A_FACTURER);
        } else {
            farViewBean.setCsEtat(IREFactureARestituer.CS_ATTENTE);
        }

        farViewBean.update();

        return farViewBean;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

}
