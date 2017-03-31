/*
 * Créé le 3 oct. 08
 */
package globaz.corvus.helpers.process;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.itext.REListeOrdresVersements;
import globaz.corvus.vb.process.REGenererListeOrdresVersementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * @author BSC
 * 
 */
public class REGenererListeOrdresVersementsHelper extends PRAbstractHelper {

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
        REGenererListeOrdresVersementsViewBean glOVViewBean = (REGenererListeOrdresVersementsViewBean) viewBean;

        if (IRELot.CS_TYP_LOT_DEBLOCAGE_RA.equals(glOVViewBean.getCsTypeLot())) {

        } else {

            REListeOrdresVersements process = new REListeOrdresVersements((BSession) session);

            process.setEMailAddress(glOVViewBean.getEMailAddress());
            process.setForIdLot(glOVViewBean.getIdLot());
            try {
                process.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
            } catch (PropertiesException e) {
                throw new RuntimeException(e);
            }

            process.start();
        }
    }
}
