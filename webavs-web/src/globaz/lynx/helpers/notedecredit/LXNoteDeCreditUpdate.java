package globaz.lynx.helpers.notedecredit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.notedecredit.LXNoteDeCreditViewBean;
import globaz.lynx.db.operation.LXOperationViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.helpers.utils.LXNoteDeCreditUtils;

public class LXNoteDeCreditUpdate {

    /**
     * Modification d'une factures depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void updateNoteDeCredit(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXNoteDeCreditViewBean noteDeCredit = (LXNoteDeCreditViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXNoteDeCreditUtils.validate(session, transaction, noteDeCredit);

            LXHelperUtils.updateSection(session, transaction, noteDeCredit);

            LXOperationViewBean operation = LXHelperUtils.updateOperation(session, transaction, noteDeCredit);

            LXHelperUtils.updateVentilations(session, transaction, noteDeCredit, operation);

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
