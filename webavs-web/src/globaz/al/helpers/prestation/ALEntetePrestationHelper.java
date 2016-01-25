package globaz.al.helpers.prestation;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.prestation.ALEntetePrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;

/**
 * Helper dédié au viewBean ALEntetePrestationViewBean
 * 
 * @author GMO
 * 
 */
public class ALEntetePrestationHelper extends ALAbstractHelper {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("supprimerPrestation".equals(action.getActionPart()) && (viewBean instanceof ALEntetePrestationViewBean)) {
            try {
                if (((ALEntetePrestationViewBean) viewBean).getEntetePrestationModel().isNew()) {
                    ((ALEntetePrestationViewBean) viewBean).retrieve();
                }
                ((ALEntetePrestationViewBean) viewBean).delete();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }
        return super.execute(viewBean, action, session);
    }

}
