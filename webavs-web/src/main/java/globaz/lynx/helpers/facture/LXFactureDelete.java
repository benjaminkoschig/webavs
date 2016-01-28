package globaz.lynx.helpers.facture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXFactureDelete {

    /**
     * Effacer une factures depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void deleteFacture(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXFactureViewBean facture = (LXFactureViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, facture.getIdOperation());

            LXHelperUtils.deleteVentilations(session, transaction, facture);

            LXHelperUtils.deleteOperation(session, transaction, facture.getIdOperation());

            LXHelperUtils.deleteSection(session, transaction, facture.getIdSection());

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
