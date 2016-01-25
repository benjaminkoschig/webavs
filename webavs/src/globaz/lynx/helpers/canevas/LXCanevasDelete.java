package globaz.lynx.helpers.canevas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.canevas.LXCanevasViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXCanevasDelete {

    /**
     * Effacer un canevas depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void deleteCanevas(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXCanevasViewBean canevas = (LXCanevasViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXHelperUtils.validateIdOperationCanevas(session, transaction, canevas.getIdOperationCanevas());

            LXHelperUtils.deleteCanevasVentilations(session, transaction, canevas);

            LXHelperUtils.deleteCanevasOperation(session, transaction, canevas.getIdOperationCanevas());

            LXHelperUtils.deleteCanevasSection(session, transaction, canevas.getIdSectionCanevas());

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
