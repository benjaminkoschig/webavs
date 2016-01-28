package globaz.lynx.helpers.escompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.escompte.LXEscompteViewBean;
import globaz.lynx.db.operation.LXOperationViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.helpers.utils.LXPaiementUtils;
import globaz.lynx.utils.LXUtils;

public class LXEscompteUpdate {

    /**
     * Modification d'un escompte depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void updateEscompte(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXEscompteViewBean paiement = (LXEscompteViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXPaiementUtils.validate(session, transaction, paiement);

            LXHelperUtils.updateSection(session, transaction, paiement);

            paiement.setMontant(LXUtils.getMontantNegatif(paiement.getMontant()));

            LXOperationViewBean operation = LXHelperUtils.updateOperation(session, transaction, paiement);

            LXHelperUtils.updateVentilations(session, transaction, paiement, operation);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }

                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }
}
