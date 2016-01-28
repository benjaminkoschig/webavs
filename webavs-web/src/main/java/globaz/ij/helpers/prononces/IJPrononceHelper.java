package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.IJRecapitulatifPrononceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPrononceHelper extends PRAbstractHelper {

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
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        IJRecapitulatifPrononceViewBean rpViewBean = (IJRecapitulatifPrononceViewBean) viewBean;
        IJPrononce prononce = IJPrononce.loadPrononce((BSession) session, null, rpViewBean.getIdPrononce(),
                rpViewBean.getCsTypeIJ());

        BITransaction transaction = null;

        if (IIJPrononce.CS_COMMUNIQUE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {

            throw new Exception(((BSession) session).getLabel("SUPPR_PRONONCE_ERR"));
        }

        if (JadeStringUtil.isIntegerEmpty(prononce.getIdParent())) {
            prononce.delete();
        }
        // Suppression d'un prononcé enfant
        else {
            try {
                // HACK: on cree une transaction pour etre sur que tous les
                // ajouts peuvent etre rollbackes
                // note: la transaction est enregistree dans la session est sera
                // utilisee dans tous les entity qui l'utilise
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                IJPrononce prononceOrigine = null;
                if (!JadeStringUtil.isIntegerEmpty(prononce.getIdCorrection())) {
                    prononceOrigine = IJPrononce.loadPrononce((BSession) session, transaction,
                            prononce.getIdCorrection(), prononce.getCsTypeIJ());
                }
                IJPrononceRegles.supprimerPrononceEnfant((BSession) session, (BTransaction) transaction,
                        prononceOrigine, prononce);

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
