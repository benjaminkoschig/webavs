/*
 * Créé le 30 juil. 07
 */
package globaz.cygnus.helpers.ordresversements;

import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.ordresversements.RFOrdresVersementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author JJE
 * 
 */
public class RFOrdresVersementsHelper extends PRAbstractHelper {

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        // ((RFOrdresVersementsViewBean) viewBean).retrieveMontantTotalPrestation();
        super._chercher(viewBean, action, session);
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFOrdresVersementsViewBean rfOrdreVersementViewBean = (RFOrdresVersementsViewBean) viewBean;

            RFOrdresVersements rfOrdresVersement = new RFOrdresVersements();
            rfOrdresVersement.setSession((BSession) session);
            rfOrdresVersement.setIdOrdreVersement(rfOrdreVersementViewBean.getIdOrdreVersement());
            rfOrdresVersement.retrieve(transaction);

            RFPrestation prestation = new RFPrestation();
            prestation.setSession((BSession) session);
            prestation.setIdPrestation(rfOrdresVersement.getIdPrestation());
            prestation.retrieve(transaction);

            if (!prestation.isNew()) {

                if (!prestation.getCsEtatPrestation().equals(IRFPrestations.CS_ETAT_PRESTATION_VALIDE)) {

                    if (!rfOrdresVersement.isNew()) {
                        rfOrdresVersement.setIsCompense(rfOrdreVersementViewBean.getIsCompense());
                        rfOrdresVersement.update(transaction);

                        // Màj du montant de la prestation
                        rfOrdreVersementViewBean.retrieveMontantTotalPrestation();

                        prestation.setMontantTotal(rfOrdreVersementViewBean.getMontantTotalPrestation());
                        prestation.update(transaction);

                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(rfOrdreVersementViewBean, "_update()",
                                "RFOrdresVersementsHelper");
                    }
                } else {
                    RFUtils.setMsgExceptionErreurViewBean(rfOrdreVersementViewBean,
                            "RFOrdresVersementsHelper._update(): La prestation est validée");
                }
            } else {
                RFUtils.setMsgErreurInattendueViewBean(rfOrdreVersementViewBean, "_update()", "RFPrestationHelper");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) || transaction.hasErrors()
                            || transaction.isRollbackOnly()) {
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
