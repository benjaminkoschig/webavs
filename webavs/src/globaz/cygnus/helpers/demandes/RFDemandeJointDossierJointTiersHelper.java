package globaz.cygnus.helpers.demandes;

import globaz.cygnus.services.RFCorrectionDemandeService;
import globaz.cygnus.vb.demandes.RFDemandeJointDossierJointTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author jje
 */
public class RFDemandeJointDossierJointTiersHelper extends PRAbstractHelper {

    public FWViewBeanInterface actionCreerCorrection(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        RFDemandeJointDossierJointTiersViewBean demandeViewBean = (RFDemandeJointDossierJointTiersViewBean) viewBean;

        RFCorrectionDemandeService.correctionDemande(session, demandeViewBean.getIdDemande(),
                demandeViewBean.getCodeTypeDeSoin());

        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

}
