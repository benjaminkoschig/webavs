package globaz.al.helpers.prestation;

import globaz.al.helpers.ALAbstractHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;

/**
 * Helper dédié au viewBean ALGenerationGlobaleViewBean
 * 
 * @author GMO
 * 
 */
public class ALGenerationGlobaleHelper extends ALAbstractHelper {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // if (viewBean instanceof ALGenerationGlobaleViewBean) {
        // ((ALGenerationGlobaleViewBean) viewBean)
        // .setPeriodeAGenerer(ALServiceLocator
        // .getPeriodeAFBusinessService().getPeriodeEnCours()
        // .getDatePeriode());
        // }
        super._init(viewBean, action, session);
    }

}