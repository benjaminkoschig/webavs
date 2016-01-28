package globaz.lynx.helpers.escompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.escompte.LXEscompteViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.utils.LXUtils;

public class LXEscompteRetrieve {

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveEscompte(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXEscompteViewBean paiement = (LXEscompteViewBean) viewBean;
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
