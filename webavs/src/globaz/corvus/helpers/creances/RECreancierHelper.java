package globaz.corvus.helpers.creances;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.vb.creances.RECreancierViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;

/**
 * Helper pour la page de recherche des créanciers
 */
public class RECreancierHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = ((BSession) session).newTransaction();

        try {

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            REDecisionsManager decisionsMgr = new REDecisionsManager();
            decisionsMgr.setSession((BSession) session);
            decisionsMgr.setForIdDemandeRente(((RECreancierViewBean) viewBean).getIdDemandeRente());
            decisionsMgr.find();

            for (REDecisionEntity decision : decisionsMgr.getContainerAsList()) {
                REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit((BSession) session,
                        transaction, decision, IREValidationLevel.VALIDATION_LEVEL_NONE);
            }

        } catch (Exception e) {
            transaction.setRollbackOnly();

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    if (transaction.hasErrors()) {
                        viewBean.setMessage(transaction.getErrors().toString());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
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

        super._add(viewBean, action, session);
    }

    /**
     * redefini pour renseigner les champs du viewbean qui sera affiche dans l'ecran rc.
     * 
     * <p>
     * Cherche le montant retroactif du tiers beneficiaire et obtient une adresse de paiement valide.
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
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // on supprime les creances accordees pour ce creancier
        RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
        caManager.setSession((BSession) session);
        caManager.setForIdCreancier(((RECreancierViewBean) viewBean).getIdCreancier());
        caManager.find();

        for (int i = 0; i < caManager.getSize(); i++) {
            RECreanceAccordee ca = (RECreanceAccordee) caManager.getEntity(i);
            ca.delete();
        }

        BITransaction transaction = ((BSession) session).newTransaction();

        try {

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            REDecisionsManager decisionsMgr = new REDecisionsManager();
            decisionsMgr.setSession((BSession) session);
            decisionsMgr.setForIdDemandeRente(((RECreancierViewBean) viewBean).getIdDemandeRente());
            decisionsMgr.find();

            for (REDecisionEntity decision : decisionsMgr.getContainerAsList()) {
                REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit((BSession) session,
                        transaction, decision, IREValidationLevel.VALIDATION_LEVEL_NONE);
            }

        } catch (Exception e) {
            transaction.setRollbackOnly();

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    if (transaction.hasErrors()) {
                        viewBean.setMessage(transaction.getErrors().toString());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
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

        super._delete(viewBean, action, session);
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
        rechargerAdressePaiement((BSession) session, (RECreancierViewBean) viewBean);
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = ((BSession) session).newTransaction();

        try {

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            REDecisionsManager decisionsMgr = new REDecisionsManager();
            decisionsMgr.setSession((BSession) session);
            decisionsMgr.setForIdDemandeRente(((RECreancierViewBean) viewBean).getIdDemandeRente());
            decisionsMgr.find();

            for (REDecisionEntity decision : decisionsMgr.getContainerAsList()) {

                if (!IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {
                    REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit((BSession) session,
                            transaction, decision, IREValidationLevel.VALIDATION_LEVEL_NONE);

                }
            }

        } catch (Exception e) {
            transaction.setRollbackOnly();

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    if (transaction.hasErrors()) {
                        viewBean.setMessage(transaction.getErrors().toString());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
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

        super._update(viewBean, action, session);
    }

    /**
     * prepare un viewBean pour l'affichage d'informations dans la page rc de la ca page.
     * 
     * <p>
     * Cherche le montant retroactif du tiers beneficiaire et obtient une adresse de paiement valide.
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
        RECreancierViewBean crViewBean = (RECreancierViewBean) viewBean;

        // recharger l'adresse de paiement pour les cas ou l'adresse est
        // invalide
        rechargerAdressePaiement(session, crViewBean);

        chargerMontantRetroactif(session, crViewBean);

        chargerMontantPrestationsDejaVersees(session, crViewBean);

        return crViewBean;
    }

    /**
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionRepartirLesCreances(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // on calculera ici les propositions de repartitions

        return viewBean;
    }

    private void chargerMontantPrestationsDejaVersees(BSession session, RECreancierViewBean crViewBean)
            throws Exception {
        RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
        renteVerseeATortManager.setSession(session);
        renteVerseeATortManager.setForIdDemandeRente(Long.parseLong(crViewBean.getIdDemandeRente()));
        renteVerseeATortManager.find();

        BigDecimal montantPrestationsDejaVersees = BigDecimal.ZERO;

        for (RERenteVerseeATort uneRenteVerseeATort : renteVerseeATortManager.getContainerAsList()) {
            montantPrestationsDejaVersees = montantPrestationsDejaVersees.add(uneRenteVerseeATort.getMontant());
        }

        crViewBean.setMontantPrestationsDejaVersees(montantPrestationsDejaVersees.toString());
    }

    /**
     * 
     * @param session
     * @param rpViewBean
     * @throws Exception
     */
    private void chargerMontantRetroactif(BSession session, RECreancierViewBean rpViewBean) throws Exception {

        BigDecimal montantRetroactif = BigDecimal.ZERO;

        // cherche les prestations due pour la demande de rente
        // seulement le type montant total ($t)
        // seulement les prestations non traitees
        REPrestationsDuesJointDemandeRenteManager pdManager = new REPrestationsDuesJointDemandeRenteManager();
        pdManager.setSession(session);
        pdManager.setForNoDemandeRente(rpViewBean.getIdDemandeRente());
        pdManager.setForCsTypePrestationDue(IREPrestationDue.CS_TYPE_MNT_TOT);
        pdManager.setForCsEtatPrestationDueNotEqual(IREPrestationDue.CS_ETAT_TRAITE);
        pdManager.find();

        for (int i = 0; i < pdManager.size(); i++) {
            REPrestationDue pd = (REPrestationDue) pdManager.getEntity(i);

            montantRetroactif = montantRetroactif.add(new BigDecimal(pd.getMontant()));
        }

        rpViewBean.setMontantRetroactif(montantRetroactif.toString());
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface miseAJourCreancesAccordees(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {
        return viewBean;
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
    private void rechargerAdressePaiement(BSession session, RECreancierViewBean rpViewBean) throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse
        // paiement
        if (rpViewBean.isTiersBeneficiaireChange()) {
            rpViewBean.setIdTiersAdressePmt(rpViewBean.getIdTiers());

            // si le tiers beneficiaire a change, on set le domaine a IJAI
            rpViewBean.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        }

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise
        // dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(rpViewBean.getIdTiersAdressePmt())) {
            if (!rpViewBean.isRetourDepuisPyxis()) {
                return;
            } else {
                rpViewBean.setCcpOuBanqueFormatte(session.getLabel("JSP_CRE_D_ERREUR_CRANCIER"));
                return;
            }
        }

        // chercher une adresse de paiement pour ce beneficiaire

        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), rpViewBean.getIdTiersAdressePmt(),
                rpViewBean.getIdDomaineApplicatif(), rpViewBean.getIdAffilieAdressePmt(), JACalendar.todayJJsMMsAAAA());

        rpViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            if (!JadeStringUtil.isEmpty(adresse.getCcp())) {
                if (!JadeStringUtil.isEmpty(adresse.getDesignation1_adr())) {
                    rpViewBean.setCcpOuBanqueFormatte(adresse.getDesignation1_adr() + "<BR>" + adresse.getCcp());
                } else {
                    rpViewBean.setCcpOuBanqueFormatte(adresse.getDesignation1_tiers() + " "
                            + adresse.getDesignation2_tiers() + "<BR>" + adresse.getCcp());
                }
            } else if (!JadeStringUtil.isEmpty(adresse.getIdTiersBanque())) {
                if (!JadeStringUtil.isEmpty(adresse.getDesignation1_adr())) {
                    rpViewBean.setCcpOuBanqueFormatte(adresse.getDesignation1_adr() + "<BR>" + adresse.getCompte()
                            + "<BR>" + adresse.getDesignation1_banque());
                } else {
                    rpViewBean.setCcpOuBanqueFormatte(adresse.getDesignation1_tiers() + " "
                            + adresse.getDesignation2_tiers() + "<BR>" + adresse.getCompte() + "<BR>"
                            + adresse.getDesignation1_banque());
                }
            } else {
                if (!JadeStringUtil.isEmpty(adresse.getDesignation1_adr())) {
                    rpViewBean.setCcpOuBanqueFormatte(adresse.getDesignation1_adr());
                } else {
                    rpViewBean.setCcpOuBanqueFormatte(adresse.getDesignation1_tiers() + " "
                            + adresse.getDesignation2_tiers());
                }
            }
        } else {
            rpViewBean.setCcpOuBanqueFormatte("");

            // si le tiers beneficiaire a change et que l'on a pas trouve
            // d'adresse
            // on enleve l'idTiersAdresseDePaiement
            if (rpViewBean.isTiersBeneficiaireChange()) {
                rpViewBean.setIdTiersAdressePmt("0");
            }
        }
    }
}
