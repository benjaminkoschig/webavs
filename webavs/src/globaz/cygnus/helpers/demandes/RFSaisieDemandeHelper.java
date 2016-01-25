/*
 * Créé le 11 février 2010
 */
package globaz.cygnus.helpers.demandes;

import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author jje
 */
public class RFSaisieDemandeHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(session)) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_PREPARER_DECISION_DEMARRE");
        }
        super._init(viewBean, action, session);
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