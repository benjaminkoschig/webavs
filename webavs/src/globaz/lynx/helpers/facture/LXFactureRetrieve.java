package globaz.lynx.helpers.facture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXFactureRetrieve {

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveFacture(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXFactureViewBean facture = (LXFactureViewBean) viewBean;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, facture.getIdOperation());

            LXHelperUtils.fillWithOperation(session, facture);

            LXHelperUtils.fillWithSection(session, facture);

            LXHelperUtils.fillForLayoutOnly(session, facture);

            LXHelperUtils.fillWithVentilations(session, facture);

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
