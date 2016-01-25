package globaz.pegasus.helpers.lot;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.lot.PCGenererListeOrdresVersementViewBean;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCGenererListeOrdresVersementHelper extends PegasusHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof PCGenererListeOrdresVersementViewBean) {

            try {
                PegasusServiceLocator.getListeDeControleService().createListeOrdreDeVersement(
                        ((PCGenererListeOrdresVersementViewBean) viewBean).getSimpleLot().getId());

            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            super._start(viewBean, action, session);
        }

    }

}
