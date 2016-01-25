/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.lots;

import globaz.apg.vb.lots.APLotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureJointCompensationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_chercher(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession bSession = (BSession) session;
        APLotViewBean lot = (APLotViewBean) viewBean;

        lot.setSession(bSession);
        lot.retrieve(bSession.getCurrentThreadTransaction());
    }
}
