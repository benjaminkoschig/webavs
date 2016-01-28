package globaz.ij.helpers.annonces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.vb.annonces.IJAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.webavs.common.CommonNSSFormater;

/**
 * @author DVH
 */
public class IJAnnonceHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // creation d'une transaction pour ajouter l'annonces et les périodes en même temps
        BTransaction transaction = null;
        IJAnnonceViewBean annonceViewBean = (IJAnnonceViewBean) viewBean;

        try {
            transaction = new BTransaction(annonceViewBean.getSession());
            transaction.openTransaction();
            annonceViewBean.add(transaction);
            annonceViewBean.setCsEtat(IIJAnnonce.CS_VALIDE);
            annonceViewBean.getPeriodeAnnonce1().setIdAnnonce(annonceViewBean.getIdAnnonce());
            annonceViewBean.getPeriodeAnnonce1().add(transaction);

            if (annonceViewBean.isDeuxiemePeriode()) {
                annonceViewBean.setIdAnnonce(annonceViewBean.getIdAnnonce());
                annonceViewBean.getPeriodeAnnonce2().save(transaction);
            }

            if (transaction.hasErrors()) {
                annonceViewBean.setMessage(transaction.getErrors().toString());
                annonceViewBean.setMsgType(FWViewBeanInterface.ERROR);
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            annonceViewBean.setMessage(e.getMessage());
            annonceViewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        IJAnnonceViewBean annonceViewBean = (IJAnnonceViewBean) viewBean;
        annonceViewBean.setDeuxiemePeriode(!annonceViewBean.getPeriodeAnnonce2().isNew());

        PRTiersWrapper tiers = PRTiersHelper.getTiers(session,
                new CommonNSSFormater().format(annonceViewBean.getNoAssure()));
        if (tiers != null) {
            annonceViewBean.setIdTiers(tiers.getIdTiers());
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // creation d'une transaction pour updater l'annonces et les périodes en même temps
        BTransaction transaction = null;

        try {
            IJAnnonceViewBean annonceViewBean = (IJAnnonceViewBean) viewBean;
            transaction = new BTransaction(annonceViewBean.getSession());
            transaction.openTransaction();

            if (annonceViewBean.getCsEtat().equals(IIJAnnonce.CS_ENVOYEE)) {
                // création d'une nouvelle annonce erronée avec les modifs
                IJAnnonce annonceErronee = annonceViewBean.createClone();
                annonceErronee.setCsEtat(IIJAnnonce.CS_ERRONEE);

                if (JadeStringUtil.isIntegerEmpty(annonceViewBean.getIdParent())) {
                    annonceErronee.setIdParent(annonceViewBean.getIdAnnonce());
                } else {
                    annonceErronee.setIdParent(annonceViewBean.getIdParent());
                }

                annonceErronee.add(transaction);
                annonceErronee.getPeriodeAnnonce1().setIdAnnonce(annonceErronee.getIdAnnonce());
                annonceErronee.getPeriodeAnnonce1().add(transaction);

                if (annonceViewBean.isDeuxiemePeriode()) {
                    annonceErronee.getPeriodeAnnonce2().setIdAnnonce(annonceErronee.getIdAnnonce());
                    annonceErronee.getPeriodeAnnonce2().add(transaction);
                }
            } else {
                // update de l'annonce et de ses périodes

                annonceViewBean.update(transaction);
                annonceViewBean.getPeriodeAnnonce1().setIdAnnonce(annonceViewBean.getIdAnnonce());
                annonceViewBean.getPeriodeAnnonce1().update(transaction);

                if (annonceViewBean.isDeuxiemePeriode()) {
                    annonceViewBean.getPeriodeAnnonce2().setIdAnnonce(annonceViewBean.getIdAnnonce());
                    annonceViewBean.getPeriodeAnnonce2().save(transaction);
                } else {
                    // effacement de la 2eme période
                    if (!annonceViewBean.getPeriodeAnnonce2().isNew()) {
                        annonceViewBean.getPeriodeAnnonce2().delete(transaction);
                    }
                }
            }

            if (transaction.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(transaction.getErrors().toString());
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }
}
