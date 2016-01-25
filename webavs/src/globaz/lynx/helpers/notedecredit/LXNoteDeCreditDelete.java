package globaz.lynx.helpers.notedecredit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.notedecredit.LXNoteDeCreditViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXNoteDeCreditDelete {

    /**
     * Effacer une note de crédit depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void deleteNoteDeCredit(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXNoteDeCreditViewBean noteDeCredit = (LXNoteDeCreditViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, noteDeCredit.getIdOperation());

            LXHelperUtils.deleteVentilations(session, transaction, noteDeCredit);

            LXHelperUtils.deleteOperation(session, transaction, noteDeCredit.getIdOperation());

            LXHelperUtils.deleteSection(session, transaction, noteDeCredit.getIdSection());

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
