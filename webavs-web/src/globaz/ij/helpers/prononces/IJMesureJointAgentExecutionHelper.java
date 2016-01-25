package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJMesureJointAgentExecutionHelper extends PRAbstractHelper {

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
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJMesureJointAgentExecutionViewBean aeViewBean = (IJMesureJointAgentExecutionViewBean) viewBean;
        aeViewBean.add();

        // on doit pouvoir modifier les agent d'execution dans tous les etats du
        // prononce sans aucun autre impact
        // if (reinitialiserPrononce((BSession) session,
        // IJPrononce.loadPrononce((BSession) session, null,
        // aeViewBean.getIdPrononce(),
        // aeViewBean.getCsTypeIJ()))) {
        // aeViewBean.add();
        // }
    }

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
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        setPermissions((BSession) session, (IJMesureJointAgentExecutionViewBean) viewBean);
    }

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
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJMesureJointAgentExecutionViewBean aeViewBean = (IJMesureJointAgentExecutionViewBean) viewBean;
        aeViewBean.delete();

        // on doit pouvoir modifier les agent d'execution dans tous les etats du
        // prononce sans aucun autre impact
        // if (reinitialiserPrononce((BSession) session,
        // IJPrononce.loadPrononce((BSession) session, null,
        // aeViewBean.getIdPrononce(),
        // aeViewBean.getCsTypeIJ()))) {
        // aeViewBean.delete();
        // }
    }

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
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        setPermissions((BSession) session, (IJMesureJointAgentExecutionViewBean) viewBean);
    }

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
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);
        setPermissions((BSession) session, (IJMesureJointAgentExecutionViewBean) viewBean);
    }

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
        IJMesureJointAgentExecutionViewBean aeViewBean = (IJMesureJointAgentExecutionViewBean) viewBean;
        aeViewBean.update();

        // on doit pouvoir modifier les agent d'execution dans tous les etats du
        // prononce sans aucun autre impact
        // if (reinitialiserPrononce((BSession) session,
        // IJPrononce.loadPrononce((BSession) session, null,
        // aeViewBean.getIdPrononce(),
        // aeViewBean.getCsTypeIJ()))) {
        // aeViewBean.update();
        // }
    }

    private boolean reinitialiserPrononce(BSession session, IJPrononce prononce) throws Exception {
        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            // reinitialiser le prononce
            IJPrononceRegles.reinitialiser(session, transaction, prononce);
            prononce.update(transaction);

            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
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

    private void setPermissions(BSession session, IJMesureJointAgentExecutionViewBean viewBean) throws Exception {
        IJPrononce prononce = viewBean.loadPrononce(null);

        if (prononce != null) {
            viewBean.setModifierPermis(IJPrononceRegles.isModifierPermis(prononce));
        } else {
            viewBean.setModifierPermis(false);
        }
    }
}
