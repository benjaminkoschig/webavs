/*
 * Créé le 31 mai 05
 */
package globaz.apg.helpers.droits;

import globaz.apg.vb.droits.APEnfantAPGViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Helper pour EnfantAPG</H1>
 * 
 * @author dvh
 */
public class APEnfantAPGHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfinie pour permettre d'ajouter plusieurs enfants à la fois
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        APEnfantAPGViewBean enfantAPG = (APEnfantAPGViewBean) viewBean;

        for (int i = 0; i < Integer.parseInt(enfantAPG.getNombreEnfants()); i++) {
            enfantAPG.add();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
