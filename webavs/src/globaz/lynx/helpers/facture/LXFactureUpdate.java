package globaz.lynx.helpers.facture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.db.operation.LXOperationViewBean;
import globaz.lynx.helpers.utils.LXFactureUtils;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXFactureUpdate {

    /**
     * Modification d'une factures depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void updateFacture(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXFactureViewBean facture = (LXFactureViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXFactureUtils.validate(session, transaction, facture);

            LXHelperUtils.updateSection(session, transaction, facture);

            LXOperationViewBean operation = LXHelperUtils.updateOperation(session, transaction, facture);

            LXHelperUtils.updateVentilations(session, transaction, facture, operation);

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
