package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.RERenteVerseeATortJointRenteAccordeeManager;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortConverter;
import globaz.corvus.db.rentesverseesatort.wrapper.RERenteVerseeATortWrapper;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.corvus.vb.rentesaccordees.RERenteVerseeATortAjaxViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.RECalculRentesVerseesATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * @author PBA
 */
public class RERenteVerseeATortAjaxHelper extends PRAbstractHelper {

    private static RERenteVerseeATortAjaxViewBean chargerCodesSystemesTypeRenteVerseeATort(
            RERenteVerseeATortAjaxViewBean viewBean, BSession session) {

        try {
            BSessionUtil.initContext(session, viewBean);

            JadeCodeSystemeService codeSystemService = JadeBusinessServiceLocator.getCodeSystemeService();
            List<JadeCodeSysteme> listeCodesSystemes = codeSystemService
                    .getFamilleCodeSysteme(RERenteVerseeATort.FAMILLE_CODE_SYSTEME_TYPE_RENTE_VERSEE_A_TROT);
            Collections.sort(listeCodesSystemes, new Comparator<JadeCodeSysteme>() {

                @Override
                public int compare(JadeCodeSysteme o1, JadeCodeSysteme o2) {
                    return o1.getOrdre().compareTo(o2.getOrdre());
                }
            });
            viewBean.setCodesSystemeTypeRenteVerseeATort(listeCodesSystemes);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        } finally {
            BSessionUtil.stopUsingContext(viewBean);
        }

        return viewBean;
    }

    private static RERenteVerseeATortAjaxViewBean chargerDetailDuTiers(RERenteVerseeATortAjaxViewBean viewBean,
            BSession session) throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, viewBean.getIdTiers().toString());

        viewBean.setNom(tiers.getNom());
        viewBean.setPrenom(tiers.getPrenom());
        viewBean.setNss(new NumeroSecuriteSociale(tiers.getNSS()));
        viewBean.setDateNaissance(tiers.getDateNaissance());
        viewBean.setDateDeces(tiers.getDateDeces());
        viewBean.setSexe(RETiersForJspUtils.getInstance(session).getLibelleCourtSexe(tiers.getSexe()));
        viewBean.setNationalite(RETiersForJspUtils.getInstance(session).getLibellePays(tiers.getIdPays()));

        return viewBean;
    }

    private static REDetailCalculRenteVerseeATort chargerDetailPourRenteVerseeATort(Long idRenteVerseeATort,
            BSession session) throws Exception {

        RERenteVerseeATortJointRenteAccordeeManager renteVerseeATortManager = new RERenteVerseeATortJointRenteAccordeeManager();
        renteVerseeATortManager.setSession(session);
        renteVerseeATortManager.setForIdRenteVerseeATort(idRenteVerseeATort);
        renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

        RERenteVerseeATortWrapper renteVerseeATortWrapper = RECalculRentesVerseesATortConverter
                .convertRenteVerseeATortEntityToWrapper(renteVerseeATortManager.getContainerAsList());

        return RECalculRentesVerseesATort.chargerDetailRenteVerseeATort(renteVerseeATortWrapper);
    }

    public static RERenteVerseeATortAjaxViewBean loadViewBeanWithEntity(BSession session,
            RERenteVerseeATortAjaxViewBean viewBean, RERenteVerseeATort entity) throws Exception {

        viewBean.setDescriptionSaisieManuelle(entity.getDescriptionSaisieManuelle());
        viewBean.setIdRenteVerseeATort(entity.getIdRenteVerseeATort().toString());
        viewBean.setIdTiers(entity.getIdTiers().toString());
        viewBean.setIdDemandeRente(entity.getIdDemandeRente().toString());
        viewBean.setTypeRenteVerseeATort(entity.getTypeRenteVerseeATort());
        viewBean.setMontant(entity.getMontant().toString());
        viewBean.setCreationSpy(entity.getCreationSpy());
        viewBean.setModificationSpy(entity.getSpy());

        if (entity.isSaisieManuelle()) {
            viewBean = RERenteVerseeATortAjaxHelper.chargerDetailDuTiers(viewBean, session);
        } else {
            viewBean.setDetailCalcul(RERenteVerseeATortAjaxHelper.chargerDetailPourRenteVerseeATort(
                    entity.getIdRenteVerseeATort(), session));
        }

        return RERenteVerseeATortAjaxHelper.chargerCodesSystemesTypeRenteVerseeATort(viewBean, session);
    }

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        RERenteVerseeATortAjaxViewBean ajaxViewBean = (RERenteVerseeATortAjaxViewBean) viewBean;

        ajaxViewBean = RERenteVerseeATortAjaxHelper
                .chargerDetailDuTiers(RERenteVerseeATortAjaxHelper.chargerCodesSystemesTypeRenteVerseeATort(
                        ajaxViewBean, (BSession) session), (BSession) session);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) BSessionUtil.getSessionFromThreadContext().newTransaction();
            transaction.openTransaction();

            RERenteVerseeATort nouvelleRenteVerseeATort = new RERenteVerseeATort();
            nouvelleRenteVerseeATort.setSession((BSession) session);
            nouvelleRenteVerseeATort.setDescriptionSaisieManuelle(ajaxViewBean.getDescriptionSaisieManuelle());
            nouvelleRenteVerseeATort.setIdDemandeRente(ajaxViewBean.getIdDemandeRente());
            nouvelleRenteVerseeATort.setIdRenteAccordeeNouveauDroit(ajaxViewBean.getIdRenteSelectionnee());
            nouvelleRenteVerseeATort.setIdTiers(ajaxViewBean.getIdTiers());
            nouvelleRenteVerseeATort.setMontant(ajaxViewBean.getMontant());
            nouvelleRenteVerseeATort.setSaisieManuelle(ajaxViewBean.isSaisieManuelle());
            nouvelleRenteVerseeATort.setTypeRenteVerseeATort(ajaxViewBean.getTypeRenteVerseeATort());

            nouvelleRenteVerseeATort.add(transaction);

            ajaxViewBean.setSuppressionDesDecisionsFaite(supprimerDecisionPourLaDemande(transaction,
                    ajaxViewBean.getIdDemandeRente()));
        } catch (Exception ex) {
            transaction.addErrors(ex.toString());
            transaction.setRollbackOnly();
            throw new RETechnicalException(ex);
        } finally {
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception ex) {
                    throw new RETechnicalException(ex);
                }
            }
        }
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        RERenteVerseeATortAjaxViewBean ajaxViewBean = (RERenteVerseeATortAjaxViewBean) viewBean;

        ajaxViewBean = RERenteVerseeATortAjaxHelper
                .chargerDetailDuTiers(RERenteVerseeATortAjaxHelper.chargerCodesSystemesTypeRenteVerseeATort(
                        ajaxViewBean, (BSession) session), (BSession) session);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) BSessionUtil.getSessionFromThreadContext().newTransaction();
            transaction.openTransaction();

            RERenteVerseeATort renteVerseeATortASupprimer = new RERenteVerseeATort();
            renteVerseeATortASupprimer.setSession((BSession) session);
            renteVerseeATortASupprimer.setIdRenteVerseeATort(ajaxViewBean.getIdRenteVerseeATort());
            renteVerseeATortASupprimer.retrieve(transaction);

            Long idDemandeRente = renteVerseeATortASupprimer.getIdDemandeRente();

            renteVerseeATortASupprimer.delete(transaction);

            ajaxViewBean.setSuppressionDesDecisionsFaite(supprimerDecisionPourLaDemande(transaction, idDemandeRente));
        } catch (Exception ex) {
            transaction.addErrors(ex.toString());
            transaction.setRollbackOnly();
            throw new RETechnicalException(ex);
        } finally {
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception ex) {
                    throw new RETechnicalException(ex);
                }
            }
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RERenteVerseeATortAjaxViewBean ajaxViewBean = (RERenteVerseeATortAjaxViewBean) viewBean;
        ajaxViewBean.setSession((BSession) session);

        RERenteVerseeATort renteVerseeATort = new RERenteVerseeATort();
        renteVerseeATort.setSession((BSession) session);
        renteVerseeATort.setIdRenteVerseeATort(ajaxViewBean.getIdRenteVerseeATort());
        renteVerseeATort.retrieve();

        DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                renteVerseeATort.getIdDemandeRente());
        ajaxViewBean.setRentesNouveauDroit(demande.getRentesAccordees());

        // il se peut qu'il n'y ait pas d'ID de rente du nouveau droit (cas de trou dans la période du nouveau droit)
        if (renteVerseeATort.getIdRenteAccordeeNouveauDroit() != null) {
            ajaxViewBean.setIdRenteSelectionnee(renteVerseeATort.getIdRenteAccordeeNouveauDroit().toString());
        }

        ajaxViewBean = RERenteVerseeATortAjaxHelper.loadViewBeanWithEntity((BSession) session, ajaxViewBean,
                renteVerseeATort);
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RERenteVerseeATortAjaxViewBean ajaxViewBean = (RERenteVerseeATortAjaxViewBean) viewBean;

        ajaxViewBean = RERenteVerseeATortAjaxHelper
                .chargerCodesSystemesTypeRenteVerseeATort(
                        RERenteVerseeATortAjaxHelper.chargerDetailDuTiers(ajaxViewBean, (BSession) session),
                        (BSession) session);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) BSessionUtil.getSessionFromThreadContext().newTransaction();
            transaction.openTransaction();

            RERenteVerseeATort renteVerseeATort = new RERenteVerseeATort();
            renteVerseeATort.setSession((BSession) session);
            renteVerseeATort.setIdRenteVerseeATort(ajaxViewBean.getIdRenteVerseeATort());
            renteVerseeATort.retrieve(transaction);

            if (renteVerseeATort.isNew()) {
                throw new RETechnicalException(renteVerseeATort.toString() + " wasn't found in the database");
            } else {
                if (!BigDecimal.ZERO.equals(ajaxViewBean.getMontant())) {
                    renteVerseeATort.setMontant(ajaxViewBean.getMontant());
                }
                renteVerseeATort.setTypeRenteVerseeATort(ajaxViewBean.getTypeRenteVerseeATort());
                renteVerseeATort.setDescriptionSaisieManuelle(ajaxViewBean.getDescriptionSaisieManuelle());

                renteVerseeATort.update(transaction);

                DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                        renteVerseeATort.getIdDemandeRente());

                // si les décisions sont déjà existantes et que des intérêts moratoires ou des créanciers sont présent,
                // il est nécessaire de re-préparer les décisions
                if (demande.comporteDesInteretsMoratoires() || demande.comporteDesCreances()) {
                    ajaxViewBean.setSuppressionDesDecisionsFaite(supprimerDecisionPourLaDemande(transaction,
                            demande.getId()));
                } else {
                    REOrdresVersements ordresVersements = new REOrdresVersements();
                    ordresVersements.setSession(BSessionUtil.getSessionFromThreadContext());
                    ordresVersements.setAlternateKey(REOrdresVersements.ALTERNATE_KEY_ID_RENTE_VERSEE_A_TORT);
                    ordresVersements.setIdRenteVerseeATort(renteVerseeATort.getIdRenteVerseeATort().toString());
                    ordresVersements.retrieve(transaction);

                    if (!ordresVersements.isNew()) {
                        ordresVersements.setMontant(renteVerseeATort.getMontant().toString());
                        ordresVersements.setMontantDette(renteVerseeATort.getMontant().toString());

                        ordresVersements.update(transaction);
                    }
                }
            }
        } catch (Exception ex) {
            transaction.addErrors(ex.toString());
            transaction.setRollbackOnly();
            throw new RETechnicalException(ex);
        } finally {
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception ex) {
                    throw new RETechnicalException(ex);
                }
            }
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("creerNouvelleEntiteeAJAX".equals(action.getActionPart())) {
            try {
                RERenteVerseeATortAjaxViewBean ajaxViewBean = (RERenteVerseeATortAjaxViewBean) viewBean;

                ajaxViewBean.setMontant("0.00");
                DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                        ajaxViewBean.getIdDemandeRente());
                ajaxViewBean.setRentesNouveauDroit(demande.getRentesAccordees());
                ajaxViewBean.setTypeRenteVerseeATort(TypeRenteVerseeATort.SAISIE_MANUELLE);

                return RERenteVerseeATortAjaxHelper.chargerCodesSystemesTypeRenteVerseeATort(ajaxViewBean,
                        (BSession) session);
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

    /**
     * @return vrai si des décisions ont été supprimées
     */
    private boolean supprimerDecisionPourLaDemande(BTransaction transaction, Long idDemandeRente) throws Exception {

        REDecisionsManager decisionsManager = new REDecisionsManager();
        decisionsManager.setSession(BSessionUtil.getSessionFromThreadContext());
        decisionsManager.setForIdDemandeRente(idDemandeRente.toString());
        decisionsManager.find(transaction);

        boolean desDecisionsOntEteSupprimees = false;
        for (REDecisionEntity uneDecisionASupprimer : decisionsManager.getContainerAsList()) {
            if (IREDecision.CS_ETAT_ATTENTE.equals(uneDecisionASupprimer.getCsEtat())
                    || IREDecision.CS_ETAT_PREVALIDE.equals(uneDecisionASupprimer.getCsEtat())
                    || laDecisionEstDansUnLotNonComptabilise(transaction, uneDecisionASupprimer)) {
                REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit(
                        BSessionUtil.getSessionFromThreadContext(), transaction, uneDecisionASupprimer,
                        IREValidationLevel.VALIDATION_LEVEL_NONE);
                desDecisionsOntEteSupprimees = true;
            }
        }
        return desDecisionsOntEteSupprimees;
    }

    private boolean laDecisionEstDansUnLotNonComptabilise(BTransaction transaction,
            REDecisionEntity uneDecisionASupprimer) throws Exception {
        REPrestations prestation = uneDecisionASupprimer.getPrestation(transaction);

        RELot lot = new RELot();
        lot.setIdLot(prestation.getIdLot());
        lot.retrieve(transaction);

        return IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtatLot());
    }
}
