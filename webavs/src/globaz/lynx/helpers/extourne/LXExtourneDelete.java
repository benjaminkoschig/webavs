package globaz.lynx.helpers.extourne;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.extourne.LXExtourneViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXExtourneDelete {

    /**
     * Effacer une extourne depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void deleteExtourne(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXExtourneViewBean extourne = (LXExtourneViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, extourne.getIdOperation());

            LXHelperUtils.deleteVentilations(session, transaction, extourne);

            LXHelperUtils.deleteOperation(session, transaction, extourne.getIdOperation());

            LXHelperUtils.deleteSection(session, transaction, extourne.getIdSection());

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
