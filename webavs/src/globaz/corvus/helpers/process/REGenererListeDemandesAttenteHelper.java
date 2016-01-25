package globaz.corvus.helpers.process;

import globaz.corvus.itext.REListeDemandesAttente;
import globaz.corvus.vb.process.REGenererListeDemandesAttenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;

public class REGenererListeDemandesAttenteHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REGenererListeDemandesAttenteViewBean gldaViewBean = (REGenererListeDemandesAttenteViewBean) viewBean;

        REListeDemandesAttente process = new REListeDemandesAttente((BSession) session);
        try {
            process.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        } catch (PropertiesException e) {
            throw new RuntimeException(e.toString(), e);
        }
        process.setEMailAddress(gldaViewBean.getEMailAddress());
        process.start();

    }
}