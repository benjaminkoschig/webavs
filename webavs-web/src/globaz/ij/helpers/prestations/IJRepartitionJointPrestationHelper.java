package globaz.ij.helpers.prestations;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJCompensation;
import globaz.ij.db.lots.IJCompensationManager;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJCotisationManager;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestationManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.module.IJRepartitionPaiementBuilder;
import globaz.ij.vb.prestations.IJRepartitionJointPrestationViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJRepartitionJointPrestationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * 
     * Appelé lors de l'ajout d'un nouveau bénéficiaire depuis le GUI. Les bénéficiaires ajouté sont considéré comme des
     * employeurs, cad que les cotisations seront ajouté au montant net.
     * 
     * Principe : Si montant brut > montantVersé à l'assuré : erreur Sinon : maj montant versé assuré (montantAssuré =
     * montantAssuré - Montant nouveau bénéficiaire)
     * 
     * Ceci garanti que la somme total répartie ne sera jamais supérieur au montant total de la prestation (par défaut,
     * toute la prestations est versée à l'assuré, et il n'est pas possible de supprimer manuellement l'assuré).
     * 
     * Met également à jours les cotisations en fonction des nouveaux montants.
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        IJRepartitionJointPrestationViewBean vb = (IJRepartitionJointPrestationViewBean) viewBean;
        // Il s'agit d'une ventilation
        if (!JadeStringUtil.isEmpty(vb.getIdParent())) {
            super._add(viewBean, action, session);
            return;
        }

        // On contrôle que la prestation ne se trouve pas dans l'état définitif
        // ou annulé
        IJPrestation prst = new IJPrestation();
        prst.setSession((BSession) session);
        prst.setIdPrestation(vb.getIdPrestation());
        prst.retrieve();
        if (IIJPrestation.CS_ANNULE.equals(prst.getCsEtat()) || IIJPrestation.CS_DEFINITIF.equals(prst.getCsEtat())) {

            throw new Exception(((BSession) session).getLabel("AJOUT_REPARTITION_ERREUR"));
        }

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            FWCurrency montantBrutBeneficiaire = new FWCurrency(vb.getMontantBrut());

            // Récupération du montant de l'assuré
            IJPrononce prononce = new IJPrononce();
            prononce.setSession((BSession) session);
            prononce.setIdPrononce(vb.getIdPrononce());
            prononce.retrieve(transaction);
            if (prononce.isNew()) {
                throw new Exception(((BSession) session).getLabel("PRONONCE_ASSOCIE_REPART_ERREUR")
                        + ". idRepartition = " + vb.getIdRepartitionPaiement());
            }

            PRTiersWrapper assure = prononce.loadDemande(transaction).loadTiers();

            IJRepartitionJointPrestationManager mgr = new IJRepartitionJointPrestationManager();
            mgr.setSession((BSession) session);
            mgr.setForIdPrestation(vb.getIdPrestation());
            mgr.setForIdTiers(assure.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            mgr.find(transaction, 1);
            IJRepartitionJointPrestation repartAssure = null;

            if (mgr.size() == 1) {
                repartAssure = (IJRepartitionJointPrestation) mgr.getEntity(0);
            }

            // reprendre toutes les répartitions de pour l'id de "repartAssure"
            // et additionner les montant ventilés
            IJRepartitionPaiementsManager mgr2 = new IJRepartitionPaiementsManager();
            mgr2.setSession((BSession) session);
            mgr2.setForIdPrestation(repartAssure.getIdPrestation());
            mgr2.setForIdParent(repartAssure.getIdRepartitionPaiement());
            mgr2.find();

            FWCurrency montantVentilations = new FWCurrency();

            for (int i = 0; i < mgr2.size(); i++) {
                IJRepartitionPaiements repart = (IJRepartitionPaiements) mgr2.getEntity(i);
                if (!JadeStringUtil.isIntegerEmpty(repart.getMontantVentile())) {
                    montantVentilations.add(repart.getMontantVentile());
                }
            }

            FWCurrency montantBrutAssure = new FWCurrency("0");
            if (repartAssure != null) {
                montantBrutAssure.add(repartAssure.getMontantBrut());
                montantBrutAssure.sub(montantVentilations);

            }

            if (montantBrutBeneficiaire.compareTo(montantBrutAssure) == 1) {
                throw new Exception(((BSession) session).getLabel("MONTANT_SAISI_ERREUR"));
            } else {
                montantBrutAssure.add(montantVentilations);

                if (repartAssure != null) {
                    // Mise a jour de la répartition du bénéficiaire
                    montantBrutAssure.sub(montantBrutBeneficiaire);

                    IJRepartitionPaiements repartition = new IJRepartitionPaiements();
                    repartition.setSession((BSession) session);
                    repartition.setIdRepartitionPaiement(repartAssure.getIdRepartitionPaiement());
                    repartition.retrieve(transaction);

                    repartition.setMontantBrut(montantBrutAssure.toString());
                    repartition.update(transaction);
                    // Mise à jours des cotisations

                    // On supprime tout puis on recréé.
                    deleteAssurances((BSession) session, (BTransaction) transaction, vb.getIdPrestation(),
                            repartition.getIdRepartitionPaiement());

                    // calculer les cotisations d'assurances et impots
                    IJBaseIndemnisation bi = new IJBaseIndemnisation();
                    bi.setSession((BSession) session);
                    bi.setIdBaseIndemisation(vb.getIdBaseIndemnisation());
                    bi.retrieve(transaction);

                    // La nouvelle version de ACOR n'importe plus le montant jrn
                    // ext si la base ne contient que des code interne.
                    // Il faut donc aller le rechercher dans
                    // IJIndemniteJournaliere.
                    String montantJrnExt = prst.getMontantJournalierExterne();

                    if (JadeStringUtil.isBlankOrZero(montantJrnExt)) {
                        IJIJCalculee ijc = new IJIJCalculee();
                        ijc.setSession((BSession) session);
                        ijc.setIdIJCalculee(prst.getIdIJCalculee());
                        ijc.retrieve(transaction);

                        IJIndemniteJournaliereManager ijMgr = new IJIndemniteJournaliereManager();
                        ijMgr.setSession((BSession) session);
                        ijMgr.setForIdIJCalculee(ijc.getIdIJCalculee());
                        ijMgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        ijMgr.find(transaction);
                        if (!ijMgr.isEmpty()) {
                            montantJrnExt = ((IJIndemniteJournaliere) ijMgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                        }
                    }

                    double somme = IJRepartitionPaiementBuilder.getInstance().buildCotisationsAssure(
                            (BSession) session, transaction, prononce, bi, repartition, prst.getMontantBrut(),
                            montantJrnExt);
                    repartition.setMontantNet(JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition
                            .getMontantBrut()) + somme));
                    repartition.update(transaction);

                    String montantNet = JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition
                            .getMontantBrut()) + somme);

                    // Mise à jour du montant net de la (des) ventilation(s)
                    for (int j = 0; j < mgr2.size(); j++) {
                        IJRepartitionPaiements ventilations = (IJRepartitionPaiements) mgr2.getEntity(j);
                        ventilations.setMontantNet(montantNet);
                        ventilations.update();
                    }

                }

                // //////////////////////////////////////////////////////////////////////////////
                // ajout du nouveau bénéficaire.
                // //////////////////////////////////////////////////////////////////////////////

                vb.setTypePaiement(IIJRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);

                // //On met à jour le no d'affilié de la répartition, s'il
                // s'agit du même tiers que celui
                // //de la situation professionnelle.
                // if (JadeStringUtil.isBlankOrZero(vb.getIdAffilie()) &&
                // !JadeStringUtil.isBlankOrZero(vb.getIdPrononce())) {
                //
                // IJSituationProfessionnelleManager mgrrr = new
                // IJSituationProfessionnelleManager();
                // mgrrr.setSession((BSession)session);
                // mgrrr.setForIdPrononce(vb.getIdPrononce());
                // mgrrr.find();
                // for (int i = 0; i < mgrrr.size(); i++) {
                // IJSituationProfessionnelle sp =
                // (IJSituationProfessionnelle)mgrrr.getEntity(i);
                // IJEmployeur emp = new IJEmployeur();
                // emp.setSession((BSession)session);
                // emp.setIdEmployeur(sp.getIdEmployeur());
                // emp.retrieve();
                //
                // if (!emp.isNew()) {
                // if (emp.getIdTiers()!=null &&
                // emp.getIdTiers().equals(vb.getIdTiers())) {
                // vb.setIdAffilie(emp.getIdAffilie());
                // break;
                // }
                // }
                // }
                // }
                vb.add(transaction);

                IJRepartitionPaiements rp = new IJRepartitionPaiements();
                rp.setSession((BSession) session);
                rp.setIdRepartitionPaiement(vb.getIdRepartitionPaiement());
                rp.retrieve(transaction);

                IJBaseIndemnisation bi = new IJBaseIndemnisation();
                bi.setSession((BSession) session);
                bi.setIdBaseIndemisation(vb.getIdBaseIndemnisation());
                bi.retrieve(transaction);

                double somme = IJRepartitionPaiementBuilder.getInstance().buildCotisationsEmployeur((BSession) session,
                        transaction, prononce, bi, rp);

                rp.setMontantNet(JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(rp.getMontantBrut()) + somme));

                // On va essayser de lier le nouveau bénéficiaire avec un des
                // employeurs de la situation professionnel
                // s'il à été saisi.

                // Est utilisé pour la génération du décompe.
                // Si le lien n'est pas réalisé, il ne sera pas possible de
                // récupérer le département de l'employeur.

                IJSituationProfessionnelleManager spMgr = new IJSituationProfessionnelleManager();
                spMgr.setSession((BSession) session);
                spMgr.setForIdPrononce(bi.getIdPrononce());
                spMgr.find(transaction);
                for (Iterator iter = spMgr.iterator(); iter.hasNext();) {
                    IJSituationProfessionnelle sp = (IJSituationProfessionnelle) iter.next();
                    IJEmployeur emp = sp.loadEmployeur();

                    if (emp != null && emp.getIdAffilie() != null && emp.getIdAffilie().equals(rp.getIdAffilie())
                            && emp.getIdTiers() != null && emp.getIdTiers().equals(rp.getIdTiers())) {
                        rp.setIdSituationProfessionnelle(sp.getIdSituationProfessionnelle());
                        break;
                    }
                }
                rp.update(transaction);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
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
     * redefini pour renseigner les champs du viewbean qui sera affiche dans l'ecran rc.
     * 
     * <p>
     * Charge le droit et les prestations du droit. Positionne les champs du list viewBean en fonction, obtient une
     * adresse de paiement valide.
     * </p>
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
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        actionPreparerChercher(viewBean, action, (BSession) session);
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            IJRepartitionJointPrestationViewBean vb = (IJRepartitionJointPrestationViewBean) viewBean;
            IJPrononce prononce = new IJPrononce();
            prononce.setSession((BSession) session);
            prononce.setIdPrononce(vb.getIdPrononce());
            prononce.retrieve(transaction);
            if (prononce.isNew()) {
                throw new Exception(((BSession) session).getLabel("PRONONCE_ASSOCIE_REPART_ERREUR")
                        + ". idRepartition = " + vb.getIdRepartitionPaiement());
            }

            PRTiersWrapper assure = prononce.loadDemande(transaction).loadTiers();
            // On essaye de supprimer l'assuré -> interdit, sauf pour une
            // ventilation
            if (JadeStringUtil.isBlankOrZero(vb.getIdParent())
                    && assure.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS).equals(vb.getIdTiers())) {
                throw new Exception(((BSession) session).getLabel("SUPPRESSION_ASSURE_ERREUR"));
            } else {

                FWCurrency montantBrutEmployeurASupprimer = new FWCurrency(vb.getMontantBrut());

                // On récupère la répartition de l'assuré
                IJRepartitionJointPrestationManager mgr = new IJRepartitionJointPrestationManager();
                mgr.setSession((BSession) session);
                mgr.setForIdPrestation(vb.getIdPrestation());
                mgr.setForIdTiers(assure.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                mgr.find(transaction, 1);
                IJRepartitionJointPrestation repartAssure = null;
                if (mgr.size() == 1) {
                    repartAssure = (IJRepartitionJointPrestation) mgr.getEntity(0);
                }

                IJRepartitionPaiements repartition = new IJRepartitionPaiements();
                repartition.setSession((BSession) session);
                repartition.setIdRepartitionPaiement(repartAssure.getIdRepartitionPaiement());
                repartition.retrieve(transaction);

                montantBrutEmployeurASupprimer.add(new FWCurrency(repartition.getMontantBrut()));
                repartition.setMontantBrut(montantBrutEmployeurASupprimer.toString());
                repartition.update(transaction);
                // Mise à jours des cotisations

                // On supprime tout puis on recréé.
                deleteAssurances((BSession) session, (BTransaction) transaction, vb.getIdPrestation(),
                        repartition.getIdRepartitionPaiement());

                if (!vb.isVentilation()) {
                    // Suppression des ventilations qui correspondent de la
                    // répartition
                    IJRepartitionPaiementsManager man = new IJRepartitionPaiementsManager();
                    man.setForIdParent(vb.getIdRepartitionPaiement());
                    man.setSession((BSession) session);
                    man.find();

                    for (int j = 0; j < man.size(); j++) {
                        IJRepartitionPaiements ventilations = (IJRepartitionPaiements) man.getEntity(j);
                        ventilations.delete();
                    }

                }

                // calculer les cotisations d'assurances et impots
                IJBaseIndemnisation bi = new IJBaseIndemnisation();
                bi.setSession((BSession) session);
                bi.setIdBaseIndemisation(vb.getIdBaseIndemnisation());
                bi.retrieve(transaction);

                IJPrestation prst = new IJPrestation();
                prst.setSession((BSession) session);
                prst.setIdPrestation(vb.getIdPrestation());
                prst.retrieve(transaction);

                // La nouvelle version de ACOR n'importe plus le montant jrn ext
                // si la base ne contient que des code interne.
                // Il faut donc aller le rechercher dans IJIndemniteJournaliere.
                String montantJrnExt = prst.getMontantJournalierExterne();

                if (JadeStringUtil.isBlankOrZero(montantJrnExt)) {
                    IJIJCalculee ijc = new IJIJCalculee();
                    ijc.setSession((BSession) session);
                    ijc.setIdIJCalculee(prst.getIdIJCalculee());
                    ijc.retrieve(transaction);

                    IJIndemniteJournaliereManager ijMgr = new IJIndemniteJournaliereManager();
                    ijMgr.setSession((BSession) session);
                    ijMgr.setForIdIJCalculee(ijc.getIdIJCalculee());
                    ijMgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                    ijMgr.find(transaction);
                    if (!ijMgr.isEmpty()) {
                        montantJrnExt = ((IJIndemniteJournaliere) ijMgr.getFirstEntity())
                                .getMontantJournalierIndemnite();
                    }
                }

                double somme = IJRepartitionPaiementBuilder.getInstance().buildCotisationsAssure((BSession) session,
                        transaction, prononce, bi, repartition, prst.getMontantBrut(), montantJrnExt);
                repartition.setMontantNet(JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition
                        .getMontantBrut()) + somme));
                repartition.update(transaction);

                // Corrections à faire : si c'est une ventilation qui est
                // supprimée, ne pas mettre à jour
                // le montant ventilé.... !!!

                if (!vb.isVentilation()) {

                    // reprendre toutes les répartitions de pour l'id de
                    // "repartAssure" et additionner les montant ventilés
                    IJRepartitionPaiementsManager mgr2 = new IJRepartitionPaiementsManager();
                    mgr2.setSession((BSession) session);
                    mgr2.setForIdPrestation(repartAssure.getIdPrestation());
                    mgr2.setForIdParent(repartAssure.getIdRepartitionPaiement());
                    mgr2.find();

                    String montantNet = JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition
                            .getMontantBrut()) + somme);

                    // Mise à jour du montant net de la (des) ventilation(s)
                    for (int j = 0; j < mgr2.size(); j++) {
                        IJRepartitionPaiements ventilations = (IJRepartitionPaiements) mgr2.getEntity(j);
                        ventilations.setMontantNet(montantNet);
                        ventilations.update();
                    }

                }

                // On supprime l'employeur
                super._delete(viewBean, action, session);
            }
            // On rajoute le montant à l'assuré

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
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
     * redefini pour charger l'adresse de paiement.
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
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        // charger l'adresse de paiement
        rechargerAdressePaiement((BSession) session, (IJRepartitionJointPrestationViewBean) viewBean);

        IJRepartitionJointPrestationViewBean rpViewBean = (IJRepartitionJointPrestationViewBean) viewBean;

        if (!(rpViewBean.getIdPrestation() == "") && !(rpViewBean.getIdPrestation() == null)) {
            reChargeEtatPrestation((BSession) session, rpViewBean);
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
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
        IJRepartitionJointPrestationViewBean repPrest = (IJRepartitionJointPrestationViewBean) viewBean;

        BTransaction transaction = (BTransaction) ((BSession) session).newTransaction();
        BStatement statement = null;
        BStatement statInt = null;
        IJPrestationManager prestManager = null;
        IJRepartitionJointPrestationManager repPrestManager = null;

        try {
            transaction.openTransaction();

            repPrest.wantMiseAJourLot(true);
            repPrest.update(transaction);

            // si adresse de paiement modifiee, on effectue la mise a jours des
            // adresses de paiement
            // vide du beneficiaire dans les prestations de la meme base
            // d'indémnisation
            if ("true".equalsIgnoreCase(repPrest.isAdresseModifiee())
                    && !JadeStringUtil.isEmpty(repPrest.getIdDomaineAdressePaiement())) {

                prestManager = new IJPrestationManager();
                prestManager.setForIdBaseIndemnisation(repPrest.getIdBaseIndemnisation());
                prestManager.setSession((BSession) session);
                IJPrestation prestation = null;

                statement = prestManager.cursorOpen(transaction);

                // pour toutes les prestation de la base d'indemnisation
                while ((prestation = (IJPrestation) prestManager.cursorReadNext(statement)) != null) {

                    repPrestManager = new IJRepartitionJointPrestationManager();
                    repPrestManager.setSession((BSession) session);
                    repPrestManager.setForIdPrestation(prestation.getIdPrestation());
                    repPrestManager.setForIdAffilie(repPrest.getIdAffilie());
                    repPrestManager.setForIdTiers(repPrest.getIdTiers());

                    IJRepartitionJointPrestation repartPrest = null;
                    statInt = repPrestManager.cursorOpen(transaction);

                    while ((repartPrest = (IJRepartitionJointPrestation) repPrestManager.cursorReadNext(statInt)) != null) {

                        // si l'adresse est vide (0) on maj avec la nouvelle
                        // adresse
                        if ("0".equals(repartPrest.getIdTiersAdressePaiement())) {

                            IJRepartitionPaiements repPay = new IJRepartitionPaiements();

                            repPay.setSession((BSession) session);
                            repPay.setIdRepartitionPaiement(repartPrest.getIdRepartitionPaiement());
                            repPay.retrieve(transaction);

                            repPay.setIdDomaineAdressePaiement(repPrest.getIdDomaineAdressePaiement());
                            repPay.setIdTiersAdressePaiement(repPrest.getIdTiersAdressePaiement());
                            repPay.update(transaction);
                        }
                    }
                }

            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            try {
                if (statement != null) {
                    try {
                        prestManager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                try {
                    if (statInt != null) {
                        try {
                            repPrestManager.cursorClose(statement);
                        } finally {
                            statInt.closeStatement();
                        }
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * prepare un viewBean pour l'affichage d'informations dans la page rc de la ca page.
     * 
     * <p>
     * Charge le droit et les prestations du droit. Positionne les champs du list viewBean en fonction, obtient une
     * adresse de paiement valide.
     * </p>
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
    public FWViewBeanInterface actionPreparerChercher(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // Si l'employeur est le même tiers que celui saisi dans la situation
        // professionnelle,
        // On set l'id affilié de la sit. prof dans celui du bénéficiaire de
        // pmt.
        IJRepartitionJointPrestationViewBean rpViewBean = (IJRepartitionJointPrestationViewBean) viewBean;

        chargerInfosBaseIndemnisation(session, rpViewBean);
        chargerInfosPrononce(session, rpViewBean);

        // recharger l'adresse de paiement pour les cas ou l'adresse est
        // invalide
        rechargerAdressePaiement(session, rpViewBean);

        return rpViewBean;
    }

    /**
     * charger toutes les infos sur la base d'indemnisation et sa prestation.
     * 
     * @param session
     * @param viewBean
     * 
     * @throws Exception
     */
    private void chargerInfosBaseIndemnisation(BSession session, IJRepartitionJointPrestationViewBean viewBean)
            throws Exception {
        // charger la prestation courante
        IJPrestation prestation = new IJPrestation();

        prestation.setIdPrestation(viewBean.getIdPrestation());
        prestation.setSession(session);
        prestation.retrieve();

        viewBean.setMontantBrutPrestation(prestation.getMontantBrut());
        viewBean.setIdBaseIndemnisation(prestation.getIdBaseIndemnisation());

        // charger la base d'indemnisation
        IJBaseIndemnisation baseIndemnisation = new IJBaseIndemnisation();

        baseIndemnisation.setIdBaseIndemisation(viewBean.getIdBaseIndemnisation());
        baseIndemnisation.setSession(session);
        baseIndemnisation.retrieve();

        // renseigne les champs qui s'affichent dans l'ecran rc.
        viewBean.setDateDebutBaseIndemnisation(baseIndemnisation.getDateDebutPeriode());
        viewBean.setDateFinBaseIndemnisation(baseIndemnisation.getDateFinPeriode());
        viewBean.setIdPrononce(baseIndemnisation.getIdPrononce());
        viewBean.setCsTypeIJ(baseIndemnisation.getCsTypeIJ());
        viewBean.setEtatPrestation(prestation.getCsEtat());

        // retrouver les ids des prestations du droit
        LinkedList idsPrestations = new LinkedList();
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(viewBean.getIdBaseIndemnisation());
        prestations.setSession(session);
        prestations.find();

        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            idsPrestations.add(((IJPrestation) prestations.get(idPrestation)).getIdPrestation());
        }

        viewBean.setIdsPrestations(idsPrestations);

        // recupere l'id de la prestation dont on veut forcer l'affichage (on
        // vient de l'ecran des prestations)
        if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdPrestation())) {
            viewBean.setIdOfIdPrestationCourante(idsPrestations.indexOf(viewBean.getIdPrestation()));
        }

        // plausi
        if (viewBean.getIdOfIdPrestationCourante() < 0) {
            viewBean.setIdOfIdPrestationCourante(0);
        }
    }

    /*
     * Appelé lors de la suppresion d'un bénéficiaire. On interdit la suppression de l'assuré.
     */

    /**
     * charge toutes les infos sur le prononce
     * 
     * @param session
     * @param viewBean
     * 
     * @throws Exception
     */
    private void chargerInfosPrononce(BSession session, IJRepartitionJointPrestationViewBean viewBean) throws Exception {
        IJPrononce prononce = IJPrononce.loadPrononce(session, null, viewBean.getIdPrononce(), viewBean.getCsTypeIJ());
        PRTiersWrapper tiers = prononce.loadDemande(null).loadTiers();

        viewBean.setNoAVSAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        viewBean.setNomPrenomAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        viewBean.setDatePrononce(prononce.getDatePrononce());

    }

    private void deleteAssurances(BSession session, BTransaction transaction, String idPrestation,
            String idRepartitionPaiement) throws Exception {

        // effacement des assurances
        IJCotisationManager assuranceManager = new IJCotisationManager();

        assuranceManager.setSession(session);
        assuranceManager.setForIdRepartitionPaiements(idRepartitionPaiement);
        assuranceManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < assuranceManager.size(); i++) {
            IJCotisation assurance = (IJCotisation) assuranceManager.getEntity(i);

            assurance.wantMiseAJourMontantRepartition(false);
            assurance.delete(transaction);
        }
        remetLotEnOuvertEtEffaceCompensations(session, transaction, idPrestation);

    }

    /**
     * recherche par introspection la methode a appeller pour executer l'action courante.
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

    /**
     * 
     * Charge l'etat de la prestation courante
     * 
     * @param rpViewBean
     * @param session
     * @return rpViewBean
     * @throws Exception
     */
    public IJRepartitionJointPrestationViewBean reChargeEtatPrestation(BSession session,
            IJRepartitionJointPrestationViewBean rpViewBean) throws Exception {

        IJPrestation prestation = new IJPrestation();

        prestation.setIdPrestation(rpViewBean.getIdPrestation());
        prestation.setSession(session);
        prestation.retrieve();

        rpViewBean.setEtatPrestation(prestation.getCsEtat());

        return rpViewBean;
    }

    /**
     * charge une adresse de paiement valide.
     * 
     * <p>
     * si les id adresse de paiment et domaine d'adresses sont renseignes, charge et formatte l'adresse correspondante.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param rpViewBean
     * 
     * @throws Exception
     */
    private void rechargerAdressePaiement(BSession session, IJRepartitionJointPrestationViewBean rpViewBean)
            throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse
        // paiement
        if (rpViewBean.isTiersBeneficiaireChange()) {
            rpViewBean.setIdTiersAdressePaiement(rpViewBean.getIdTiers());

            // //On met à jour le no d'affilié de la répartition, s'il s'agit du
            // même tiers que celui
            // //de la situation professionnelle.
            // if (JadeStringUtil.isBlankOrZero(rpViewBean.getIdAffilie()) &&
            // !JadeStringUtil.isBlankOrZero(rpViewBean.getIdPrononce())) {
            //
            // IJSituationProfessionnelleManager mgr = new
            // IJSituationProfessionnelleManager();
            // mgr.setSession((BSession)session);
            // mgr.setForIdPrononce(rpViewBean.getIdPrononce());
            // mgr.find();
            // for (int i = 0; i < mgr.size(); i++) {
            // IJSituationProfessionnelle sp =
            // (IJSituationProfessionnelle)mgr.getEntity(i);
            // IJEmployeur emp = new IJEmployeur();
            // emp.setSession((BSession)session);
            // emp.setIdEmployeur(sp.getIdEmployeur());
            // emp.retrieve();
            //
            // if (!emp.isNew()) {
            // if (emp.getIdTiers()!=null &&
            // emp.getIdTiers().equals(rpViewBean.getIdTiers())) {
            // rpViewBean.setIdAffilie(emp.getIdAffilie());
            // break;
            // }
            // }
            // }
            // }

            // si le tiers beneficiaire a change, on set le domaine a IJAI
            rpViewBean.setIdDomaineAdressePaiement(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI);

            rpViewBean.setIdAffilieAdrPmt(rpViewBean.getIdAffilie());
        }

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise
        // dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(rpViewBean.getIdTiersAdressePaiement())) {
            return;
        }

        TIAdressePaiementData adresse = PRTiersHelper
                .getAdressePaiementData(session, session.getCurrentThreadTransaction(),
                        rpViewBean.getIdTiersAdressePaiement(), rpViewBean.getIdDomaineAdressePaiement(),
                        rpViewBean.getIdAffilieAdrPmt(), JACalendar.todayJJsMMsAAAA());

        rpViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                rpViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                rpViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            rpViewBean.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            rpViewBean.setCcpOuBanqueFormatte("");
            rpViewBean.setAdresseFormattee("");

            // si le tiers beneficiaire a change et que l'on a pas trouve
            // d'adresse
            // on enleve l'idTiersAdresseDePaiement
            if (rpViewBean.isTiersBeneficiaireChange()) {
                rpViewBean.setIdTiersAdressePaiement("0");
            }
        }
    }

    public void rechercherAffilie(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        IJRepartitionJointPrestationViewBean spviewBean = (IJRepartitionJointPrestationViewBean) viewBean;

        spviewBean.setIsCreationNouvelleRepartition(true);

        IPRAffilie affilie;
        affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session, spviewBean.getNumAffilieEmployeur(),
                spviewBean.getDateDebutPrestation());

        if (affilie == null) {
            spviewBean.setIdAffilie("");
            spviewBean.setNumAffilieEmployeur("");
            spviewBean.setNom("");

            spviewBean.setMessage(session.getLabel("AFFILIE_NON_TROUVE"));
            spviewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        else {
            spviewBean.setIdAffilie(affilie.getIdAffilie());
            spviewBean.setNumAffilieEmployeur(affilie.getNumAffilie());
            spviewBean.setNom(affilie.getNom());
            spviewBean.setIdTiersAdressePaiement(affilie.getIdTiers());
            spviewBean.setTiersBeneficiaireChange(true);
            spviewBean.setIdTiers(affilie.getIdTiers());

            rechargerAdressePaiement(session, spviewBean);

            chargerInfosBaseIndemnisation(session, spviewBean);
            chargerInfosPrononce(session, spviewBean);

        }
    }

    private void remetLotEnOuvertEtEffaceCompensations(BSession session, BTransaction transaction, String idPrestation)
            throws Exception {
        // remise en état ouvert du lot et effacement des compensations(S'il est
        // compensé, les compensations sont
        // maintenant incohérentes)

        IJPrestation prestation = new IJPrestation();

        prestation.setSession(session);
        prestation.setIdPrestation(idPrestation);
        prestation.retrieve(transaction);

        IJLot lot = new IJLot();

        lot.setSession(session);
        lot.setIdLot(prestation.getIdLot());
        lot.retrieve(transaction);

        if (lot.getCsEtat().equals(IIJLot.CS_COMPENSE)) {
            lot.setCsEtat(IIJLot.CS_OUVERT);
            lot.update(transaction);

            IJCompensationManager compensationManager = new IJCompensationManager();

            compensationManager.setSession(session);
            compensationManager.setForIdLot(lot.getIdLot());
            compensationManager.find(transaction);

            for (int i = 0; i < compensationManager.size(); i++) {
                ((IJCompensation) (compensationManager.getEntity(i))).delete(transaction);
            }
        }
    }

}
