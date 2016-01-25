package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.IJPetiteIJJointRevenuViewBean;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPetiteIJJointRevenuHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJPetiteIJJointRevenuViewBean prViewBean = (IJPetiteIJJointRevenuViewBean) viewBean;

        if (prViewBean.isModifie()) {
            BITransaction transaction = null;

            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                IJPrononceRegles.reinitialiser((BSession) session, transaction, prViewBean);
                prViewBean.update(transaction);
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
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
