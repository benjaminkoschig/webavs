/*
 * Globaz SA
 */
package globaz.corvus.helpers.process;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.itext.REListeOrdresVersements;
import globaz.corvus.process.deblocage.REGenererListeOVDeblocageProcess;
import globaz.corvus.vb.process.REGenererListeOrdresVersementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;

public class REGenererListeOrdresVersementsHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // nothing
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REGenererListeOrdresVersementsViewBean glOVViewBean = (REGenererListeOrdresVersementsViewBean) viewBean;

        if (IRELot.CS_TYP_LOT_DEBLOCAGE_RA.equals(glOVViewBean.getCsTypeLot())) {

            REGenererListeOVDeblocageProcess process = new REGenererListeOVDeblocageProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(glOVViewBean.getEMailAddress());
            process.setForIdLot(glOVViewBean.getIdLot());

            try {
                BProcessLauncher.start(process);

            } catch (Exception e) {
                glOVViewBean.setMessage(e.toString());
                glOVViewBean.setMsgType(FWViewBeanInterface.ERROR);
                JadeLogger.error(this, e.getMessage());
            }

        } else {

            REListeOrdresVersements process = new REListeOrdresVersements((BSession) session);

            process.setEMailAddress(glOVViewBean.getEMailAddress());
            process.setForIdLot(glOVViewBean.getIdLot());
            try {
                process.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
            } catch (PropertiesException e) {
                throw new RETechnicalException(e);
            }

            process.start();
        }
    }
}
