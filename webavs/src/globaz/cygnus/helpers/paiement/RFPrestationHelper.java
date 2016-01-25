/*
 * Créé le 04 mars 2011
 */
package globaz.cygnus.helpers.paiement;

import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.paiement.RFPrestationDetailViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author fha
 */
public class RFPrestationHelper extends PRAbstractHelper {

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        RFPrestationDetailViewBean rfPrestationVB = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            rfPrestationVB = (RFPrestationDetailViewBean) viewBean;
            RFPrestation prestation = new RFPrestation();
            prestation.setSession(rfPrestationVB.getSession());
            prestation.setIdPrestation(rfPrestationVB.getIdPrestation());
            prestation.retrieve(transaction);

            if (!prestation.isNew()) {

                prestation.setReferencePaiement(rfPrestationVB.getReferencePaiement());

                prestation.update(transaction);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(rfPrestationVB, "_update()", "RFPrestationHelper");
            }

        } catch (Exception e) {
            RFUtils.setMsgErreurInattendueViewBean(rfPrestationVB, "_update()", "RFPrestationHelper");
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
