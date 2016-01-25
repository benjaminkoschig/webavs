package globaz.ij.helpers.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.module.IJRepartitionPaiementBuilder;
import globaz.ij.regles.IJPrestationRegles;
import globaz.ij.vb.lots.IJLotViewBean;
import globaz.ij.vb.prestations.IJPrestationJointLotPrononceListViewBean;
import globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPrestationJointLotPrononceHelper extends PRAbstractHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrestationJointLotPrononceHelper.
     */
    public IJPrestationJointLotPrononceHelper() {
        super();
    }

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
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJPrestationJointLotPrononceListViewBean pViewBean = (IJPrestationJointLotPrononceListViewBean) viewBean;

        if (!JadeStringUtil.isIntegerEmpty(pViewBean.getForIdLot())) {
            // recherche du numero du lot
            IJLotViewBean lot = new IJLotViewBean();

            lot.setIdLot(pViewBean.getForIdLot());
            lot.setISession(session);
            lot.retrieve();

            pViewBean.setForNoLot(lot.getNoLot());
        }

        if (!JadeStringUtil.isIntegerEmpty(pViewBean.getForNoBaseIndemnisation())) {
            IJBaseIndemnisation base = new IJBaseIndemnisation();

            base.setIdBaseIndemisation(pViewBean.getForNoBaseIndemnisation());
            base.setISession(session);
            base.retrieve();

            pViewBean.setIdPrononce(base.getIdPrononce());
        }
    }

    /**
     * Efface la répartition de paiement de la prestation qui vient d'etre supprimée.
     * 
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
        IJPrestationJointLotPrononceViewBean spViewBean = (IJPrestationJointLotPrononceViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            // effacer la prestation
            IJPrestation prestation = new IJPrestation();

            prestation.setIdPrestation(spViewBean.getIdPrestation());
            prestation.setISession(session);
            prestation.retrieve();

            prestation.delete(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
        } finally {
            if (transaction != null) {
                try {
                    if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                        transaction.commit();
                    } else {
                        transaction.rollback();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * Efface et reconstruit la répartition de paiement de la prestation qui vient d'etre modifiée.
     * 
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
        IJPrestationJointLotPrononceViewBean spViewBean = (IJPrestationJointLotPrononceViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            // mettre ajour les lots si necessaire
            if (!JadeStringUtil.isIntegerEmpty(spViewBean.getIdLot())
                    || IIJPrestation.CS_MIS_EN_LOT.equals(spViewBean.getCsEtat())) {

                // On stock l'état, car l'annulation de la mise en lot va focrer
                // la maj de l'état de la prestation
                // à 'validé'
                String csEtat = spViewBean.getCsEtat();

                IJPrestationRegles.annulerMiseEnLot((BSession) session, transaction, spViewBean);
                spViewBean.setCsEtat(csEtat);
            }

            // mettre a jour la prestation avec les nouvelles valeurs
            spViewBean.update(transaction);

            // S'il s'agit d'une prestation de restitution, seul son état peut
            // être modifié.
            // On ne va donc pas effacer et recalculer les répartitions de
            // paiement.
            if (!IIJPrestation.CS_RESTITUTION.equals(spViewBean.getCsType())) {

                // effacer les repartitions de paiements
                IJRepartitionPaiementsManager repartitions = new IJRepartitionPaiementsManager();

                repartitions.setForIdPrestation(spViewBean.getIdPrestation());
                repartitions.setISession(session);
                repartitions.find();

                for (int idRepartition = 0; idRepartition < repartitions.size(); ++idRepartition) {
                    IJRepartitionPaiements repartition = (IJRepartitionPaiements) repartitions.get(idRepartition);

                    repartition.wantMiseAJourLot(false);
                    repartition.delete(transaction);
                }

                // creer une nouvelle repartition de paiement
                IJBaseIndemnisation base = new IJBaseIndemnisation();

                base.setIdBaseIndemisation(spViewBean.getIdBaseIndemnisation());
                base.setISession(session);
                base.retrieve();

                IJPrononce prononce = IJPrononce.loadPrononce((BSession) session, null, base.getIdPrononce(),
                        base.getCsTypeIJ());

                // IJRepartitionPaiementBuilder.getInstance().buildRepartitionPaiements((BSession) session, transaction,
                // prononce, base);

                IJRepartitionPaiementBuilder.getInstance().buildRepartitionPaiementsPourUnePrestation(
                        (BSession) session, transaction, prononce, base, spViewBean.getIdPrestation());

            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
        } finally {
            if (transaction != null) {
                try {
                    if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                        transaction.commit();
                    } else {
                        transaction.rollback();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

}
