package globaz.ij.helpers.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.module.IJRepartitionPaiementBuilder;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationAitAaViewBean;
import globaz.ij.vb.prononces.IJPrononceAitViewBean;
import globaz.ij.vb.prononces.IJPrononceAllocAssistanceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRAssert;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * helper le calcul des prestations des pronoces AIT et AA.
 * </p>
 * 
 * @author bsc
 */
public class IJCalculDecompteAitAaHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface calculerAa(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;

        BTransaction transaction = null;

        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();
            // on cherche le prononce
            IJPrononceAllocAssistanceViewBean prononce = new IJPrononceAllocAssistanceViewBean();
            prononce.setSession(session);
            prononce.setIdPrononce(biViewBean.getIdPrononce());
            prononce.retrieve(transaction);

            // on cherche la base d'indemnisation
            IJBaseIndemnisation bi = new IJBaseIndemnisation();
            bi.setSession(session);
            bi.setIdBaseIndemisation(biViewBean.getIdBaseIndemisation());
            bi.retrieve(transaction);

            // si la base d'indemnisation est une correction, on restitue les
            // prestations du parent
            if (!JadeStringUtil.isIntegerEmpty(bi.getIdParent())) {
                IJBaseIndemnisationRegles.correction(session, transaction, bi);
            }

            // creation de la prestation
            IJPrestation prestation = new IJPrestation();
            prestation.setSession(session);
            prestation.setIdBaseIndemnisation(biViewBean.getIdBaseIndemisation());
            prestation.setCsEtat(IIJPrestation.CS_VALIDE);
            prestation.setCsType(IIJPrestation.CS_NORMAL);
            prestation.setDateDebut(biViewBean.getDateDebutPeriodeEtendue());
            prestation.setDateFin(biViewBean.getDateFinPeriodeEtendue());
            prestation.setMontantBrut(prononce.getMontantTotal());
            prestation.setNombreJoursInt(biViewBean.getNombreJoursInterne());

            prestation.add(transaction);

            // creation de la repartition des paiements et des cotisations
            IJRepartitionPaiementBuilder repBuilder = IJRepartitionPaiementBuilder.getInstance();
            repBuilder.buildRepartitionPaiementsSansCotisations(session, transaction, prononce.getPrononce(),
                    biViewBean);

            // mise a jours de la base d'indemnisation
            bi.setCsEtat(IIJBaseIndemnisation.CS_VALIDE);
            bi.update(transaction);

            if (transaction.hasErrors()) {
                transaction.setRollbackOnly();
            }

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

        return biViewBean;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface calculerAit(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;

        BTransaction transaction = null;

        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();
            // on cherche le prononce
            IJPrononceAitViewBean prononce = new IJPrononceAitViewBean();
            prononce.setSession(session);
            prononce.setIdPrononce(biViewBean.getIdPrononce());
            prononce.retrieve(transaction);

            // on cherche l'ij calculee
            IJIJCalculeeManager ijcManager = new IJIJCalculeeManager();
            ijcManager.setSession(session);
            ijcManager.setForIdPrononce(prononce.getIdPrononce());
            ijcManager.find(transaction);

            if (ijcManager.getSize() == 0) {
                throw new Exception(session.getLabel("AUCUNE_IJ_CALCULEE") + " idPrononce : "
                        + prononce.getIdPrononce());
            }

            IJIJCalculee ijc = (IJIJCalculee) ijcManager.getFirstEntity();

            // on cherche la base d'indemnisation
            IJBaseIndemnisation bi = new IJBaseIndemnisation();
            bi.setSession(session);
            bi.setIdBaseIndemisation(biViewBean.getIdBaseIndemisation());
            bi.retrieve(transaction);
            PRAssert.notIsNew(bi, null);

            if (IIJBaseIndemnisation.CS_COMMUNIQUE.equals(bi.getCsEtat())
                    || IIJBaseIndemnisation.CS_ANNULE.equals(bi.getCsEtat())) {

                throw new Exception("Action non authorisée pour les bases communiquées/annulées");
            }

            // Arrive dans le cas d'un recalcul de prestations. On efface toutes
            // les prestations NORMAL pour éviter de les avoir à doubles.
            if (!JadeStringUtil.isIntegerEmpty(bi.getIdParent())) {
                if (IIJBaseIndemnisation.CS_VALIDE.equals(bi.getCsEtat())) {
                    IJPrestationManager mgr = new IJPrestationManager();
                    mgr.setSession(session);
                    mgr.setForIdBaseIndemnisation(bi.getIdBaseIndemisation());
                    mgr.setForCsType(IIJPrestation.CS_NORMAL);
                    mgr.find(transaction);

                    for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                        IJPrestation prest = (IJPrestation) iterator.next();

                        prest.delete(transaction);

                    }
                }
            }

            // si la base d'indemnisation est une correction, on restitue les
            // prestations du parent
            if (!JadeStringUtil.isIntegerEmpty(bi.getIdParent())) {
                IJBaseIndemnisationRegles.correction(session, transaction, bi);
            }

            // creation de la prestation
            IJPrestation prestation = new IJPrestation();
            prestation.setSession(session);
            prestation.setIdBaseIndemnisation(biViewBean.getIdBaseIndemisation());
            prestation.setIdIJCalculee(ijc.getIdIJCalculee());
            prestation.setCsEtat(IIJPrestation.CS_VALIDE);
            prestation.setCsType(IIJPrestation.CS_NORMAL);
            prestation.setDateDebut(biViewBean.getDateDebutPeriodeEtendue());
            prestation.setDateFin(biViewBean.getDateFinPeriodeEtendue());
            prestation.setNombreJoursInt(biViewBean.getNombreJoursInterne());
            prestation.setMontantJournalierInterne(prononce.getMontant());

            // calcul du montant brut interne
            BigDecimal montantBrutInterne = new BigDecimal(prononce.getMontant());
            montantBrutInterne = montantBrutInterne.multiply(new BigDecimal(biViewBean.getNombreJoursInterne()));

            prestation.setMontantBrutInterne(montantBrutInterne.toString());
            prestation.setMontantBrut(montantBrutInterne.toString());
            prestation.add(transaction);

            // creation de la repartition des paiements et des cotisations
            IJRepartitionPaiementBuilder repBuilder = IJRepartitionPaiementBuilder.getInstance();
            repBuilder.buildRepartitionPaiementsEmployeurSansCotisations(session, transaction, prononce.getPrononce(),
                    biViewBean);

            // mise a jours de la base d'indemnisation
            bi.retrieve(transaction);
            bi.setCsEtat(IIJBaseIndemnisation.CS_VALIDE);
            bi.update(transaction);

            if (transaction.hasErrors()) {
                transaction.setRollbackOnly();
            }

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

        return biViewBean;
    }

    /**
     * retrouve par introspection la methode a executer quand on arrive dans ce helper avec une action custom.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        return deleguerExecute(viewBean, action, session);
    }
}
