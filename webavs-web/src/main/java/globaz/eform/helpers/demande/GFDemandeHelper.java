package globaz.eform.helpers.demande;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.web.servlet.GFDemandeServletAction;
import globaz.eform.vb.demande.GFDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class GFDemandeHelper extends FWHelper {
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        String actionPart = action.getActionPart();
        if (viewBean instanceof GFDemandeViewBean && GFDemandeServletAction.ACTION_ENVOYER.equals(actionPart)) {
            try {
                GFDaDossierSedexService sedexService = GFEFormServiceLocator.getGFDaDossierSedexService();
                sedexService.envoyerDemande(((GFDemandeViewBean) viewBean).getDaDossier(), ((GFDemandeViewBean) viewBean).getSession());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }
}
