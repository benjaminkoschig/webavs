package globaz.lynx.helpers.canevas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.canevas.LXCanevasOperation;
import globaz.lynx.db.canevas.LXCanevasSection;
import globaz.lynx.db.canevas.LXCanevasViewBean;
import globaz.lynx.helpers.utils.LXCanevasUtils;
import globaz.lynx.helpers.utils.LXHelperUtils;

/**
 * Permet d'ajouter des canevas.
 * 
 * @author DDA
 */
public class LXCanevasAdd {

    /**
     * Ajout de canevas depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void addCanevas(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXCanevasViewBean canevas = (LXCanevasViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXCanevasUtils.validate(session, transaction, canevas);

            LXCanevasSection canSect = LXHelperUtils.getCanevasSection(session, canevas, transaction);

            LXCanevasOperation canOpe = LXHelperUtils.createCanevasOperation(session, canevas, transaction, canSect);

            LXHelperUtils.addCanevasVentilations(session, transaction, canevas, canOpe);

            canevas.setIdSectionCanevas(canSect.getIdSectionCanevas());
            canevas.setIdOperationCanevas(canOpe.getIdOperationCanevas());

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
