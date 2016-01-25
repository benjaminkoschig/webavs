package globaz.al.helpers.prestation;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.prestation.ALGenerationAffilieViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper dédié au viewBean ALGenerationAffilieViewBean
 * 
 * @author GMO
 * 
 */
public class ALGenerationAffilieHelper extends ALAbstractHelper {
    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO (lot 2) : réinitialiser la période en cours, avec type direct /
        // indirect
        if (viewBean instanceof ALGenerationAffilieViewBean) {
            ((ALGenerationAffilieViewBean) viewBean).setPeriodeAGenerer(ALServiceLocator.getPeriodeAFBusinessService()
                    .getPeriodeEnCours(ALCSPrestation.BONI_INDIRECT, true).getDatePeriode());
        }
        super._init(viewBean, action, session);
    }
}
