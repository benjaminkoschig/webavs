package globaz.al.helpers.traitement;

import globaz.al.helpers.ALAbstractHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;

/**
 * Helper dédié au viewBean ALRecapImpressionViewBean
 * 
 * @author GMO
 * 
 */
public class ALRecapImpressionHelper extends ALAbstractHelper {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // if (viewBean instanceof ALRecapImpressionViewBean) {
        // (
        //
        // (ALRecapImpressionViewBean) viewBean).setDate(ALServiceLocator
        // .getPeriodeAFBusinessService().getPeriodeEnCours()
        // .getDatePeriode());
        //
        // ((ALRecapImpressionViewBean) viewBean)
        // .setDateImpression(JadeDateUtil
        // .getGlobazFormattedDate(new Date()));
        // }
        super._init(viewBean, action, session);
    }

}
