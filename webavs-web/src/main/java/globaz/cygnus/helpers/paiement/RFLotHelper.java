/*
 * Créé le 11 août 2011
 */
package globaz.cygnus.helpers.paiement;

import globaz.corvus.db.lots.RELot;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.paiement.RFLotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author fha
 */
public class RFLotHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        ((RFLotViewBean) viewBean)._retrieve();
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFLotViewBean rfLotVB = (RFLotViewBean) viewBean;

            RELot lot = new RELot();
            lot.setSession(rfLotVB.getSession());
            lot.setIdLot(rfLotVB.getIdLot());
            lot.retrieve(transaction);

            if (!lot.isNew()) {

                lot.setDescription(rfLotVB.getDescription());

                lot.update(transaction);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(rfLotVB, "_update()", "RFLotHelper");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

}
