package globaz.lynx.helpers.paiement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.paiement.LXPaiementViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.utils.LXUtils;

public class LXPaiementRetrieve {

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrievePaiement(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXPaiementViewBean paiement = (LXPaiementViewBean) viewBean;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, paiement.getIdOperation());

            LXHelperUtils.fillWithOperation(session, paiement);

            paiement.setMontant(LXUtils.getMontantPositif(paiement.getMontant()));

            LXHelperUtils.fillWithSection(session, paiement);

            LXHelperUtils.fillForLayoutOnly(session, paiement);

            LXHelperUtils.fillWithVentilations(session, paiement);

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
