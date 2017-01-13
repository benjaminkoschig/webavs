/*
 * Créé le 25 juin 07
 */

package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.dao.REInfoCompta;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REDebloquerMontantRenteAccordeeProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.REPostItsFilteringUtils;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.process.REDebloquerMontantRAViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteListViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Date;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;

/**
 * @author HPE
 */

public class RERenteAccordeeJointDemandeRenteHelper extends PRHybridHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _add(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        // Création de la transaction
        BSession session1 = (BSession) session;

        BTransaction transaction = null;
        transaction = (BTransaction) (session1).newTransaction();

        try {
            transaction.openTransaction();

            // récupération du viewBean et ajout de la rente accordée
            RERenteAccordeeJointDemandeRenteViewBean renteAccordeeVb = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;

            RERenteAccordee renteAccordee = new RERenteAccordee();

            renteAccordee.setAnneeAnticipation(renteAccordeeVb.getAnneeAnticipation());
            renteAccordee.setAnneeMontantRAM(renteAccordeeVb.getAnneeMontantRAM());
            renteAccordee.setCodeAuxilliaire(renteAccordeeVb.getCodeAuxilliaire());
            renteAccordee.setCodeCasSpeciaux1(renteAccordeeVb.getCodeCasSpeciaux1());
            renteAccordee.setCodeCasSpeciaux2(renteAccordeeVb.getCodeCasSpeciaux2());
            renteAccordee.setCodeCasSpeciaux3(renteAccordeeVb.getCodeCasSpeciaux3());
            renteAccordee.setCodeCasSpeciaux4(renteAccordeeVb.getCodeCasSpeciaux4());
            renteAccordee.setCodeCasSpeciaux5(renteAccordeeVb.getCodeCasSpeciaux5());
            renteAccordee.setCodeMutation(renteAccordeeVb.getCodeMutation());
            renteAccordee.setCodePrestation(renteAccordeeVb.getCodePrestation());
            renteAccordee.setCodeSurvivantInvalide(renteAccordeeVb.getCodeSurvivantInvalide());
            renteAccordee.setCsEtat(renteAccordeeVb.getCsEtat());
            renteAccordee.setCsEtatCivil(renteAccordeeVb.getCsEtatCivil());
            renteAccordee.setCsRelationAuRequerant(renteAccordeeVb.getCsRelationAuRequerant());
            renteAccordee.setDateDebutAnticipation(renteAccordeeVb.getDateDebutAnticipation());
            renteAccordee.setDateDebutDroit(renteAccordeeVb.getDateDebutDroit());
            renteAccordee.setDateFinDroit(renteAccordeeVb.getDateFinDroit());
            renteAccordee.setDateFinDroitPrevueEcheance(renteAccordeeVb.getDateFinDroitPrevueEcheance());
            renteAccordee.setDateRevocationAjournement(renteAccordeeVb.getDateRevocationAjournement());
            renteAccordee.setDureeAjournement(renteAccordeeVb.getDureeAjournement());
            renteAccordee.setFractionRente(renteAccordeeVb.getFractionRente());
            renteAccordee.setIdBaseCalcul(renteAccordeeVb.getIdBaseCalcul());
            renteAccordee.setIdDemandePrincipaleAnnulante(renteAccordeeVb.getIdDemandePrincipaleAnnulante());
            renteAccordee.setIdInfoCompta(renteAccordeeVb.getIdInfoCompta());
            renteAccordee.setIdPrestationAccordee(renteAccordeeVb.getIdPrestationAccordee());
            renteAccordee.setIdTiersBeneficiaire(renteAccordeeVb.getIdTiersBeneficiaire());
            renteAccordee.setReferencePmt(renteAccordeeVb.getReferencePmt());

            // Retrouver l'idTiers par le NSS et le setter
            PRTiersWrapper tiers = PRTiersHelper.getTiers(session1, renteAccordeeVb.getNssTiersComplementaire1a());

            if (tiers != null) {
                renteAccordee.setIdTiersComplementaire1(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            } else {
                renteAccordee.setIdTiersComplementaire1("");
            }

            tiers = PRTiersHelper.getTiers(session1, renteAccordeeVb.getNssTiersComplementaire2a());

            if (tiers != null) {
                renteAccordee.setIdTiersComplementaire2(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            } else {
                renteAccordee.setIdTiersComplementaire2("");
            }

            renteAccordee.setCodeRefugie(renteAccordeeVb.getCodeRefugie());
            renteAccordee.setIsTraitementManuel(renteAccordeeVb.isTraitementManuel());
            renteAccordee.setMontantPrestation(renteAccordeeVb.getMontantPrestation());
            renteAccordee.setMontantReducationAnticipation(renteAccordeeVb.getMontantReducationAnticipation());
            renteAccordee.setMontantRenteOrdiRemplacee(renteAccordeeVb.getMontantRenteOrdiRemplacee());
            renteAccordee.setPrescriptionAppliquee(renteAccordeeVb.getPrescriptionAppliquee());
            renteAccordee.setReductionFauteGrave(renteAccordeeVb.getReductionFauteGrave());
            renteAccordee.setSupplementAjournement(renteAccordeeVb.getSupplementAjournement());
            renteAccordee.setSupplementVeuvage(renteAccordeeVb.getSupplementVeuvage());
            renteAccordee.setSession(session1);

            renteAccordee.add(transaction);

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
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        try {
            RERenteAccordeeJointDemandeRenteViewBean vb = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;

            RERenteAccordee ra = new RERenteAccordee();
            ra.setIdPrestationAccordee(vb.getIdPrestationAccordee());
            ra.retrieve();

            REBasesCalcul baseCalcul = new REBasesCalcul();
            baseCalcul.setIdBasesCalcul(ra.getIdBaseCalcul());
            baseCalcul.retrieve();

            // mise à jour de la période de la demande
            REDemandeRente demandeRenteEntity = new REDemandeRente();
            demandeRenteEntity.setIdRenteCalculee(baseCalcul.getIdRenteCalculee());
            demandeRenteEntity.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRenteEntity.retrieve();

            REDeleteCascadeDemandeAPrestationsDues.supprimerRenteAccordeeCascade_noCommit(BSessionUtil
                    .getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                    .getCurrentThreadTransaction(), ra, IREValidationLevel.VALIDATION_LEVEL_NONE);

            if (!demandeRenteEntity.isNew()) {
                DemandeRente demandeRente = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                        Long.parseLong(demandeRenteEntity.getIdDemandeRente()));
                CorvusServiceLocator.getDemandeRenteService().mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(
                        demandeRente);
            }
        } catch (Exception ex) {
            JadeThread.logError(this.getClass().getName(), ex.toString());
        }

    }

    /*
     * oca Surcharge pour permettre de rechercher les notes (FWNOTEP) avec une requête séparée (plus efficace que la
     * jointure...)
     */
    @Override
    protected void _find(final BIPersistentObjectList persistentList, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        RERenteAccordeeJointDemandeRenteListViewBean mgr = (RERenteAccordeeJointDemandeRenteListViewBean) persistentList;
        mgr.setScreenMode(true);
        mgr.find();
        REPostItsFilteringUtils.keepPostItsForLastRenteAccordeeOnly(mgr);
    }

    @Override
    protected void _findNext(final BIPersistentObjectList persistentList, final FWAction action, final BISession session)
            throws Exception {
        RERenteAccordeeJointDemandeRenteListViewBean mgr = (RERenteAccordeeJointDemandeRenteListViewBean) persistentList;
        mgr.setScreenMode(true);
        mgr.findNext();
        REPostItsFilteringUtils.keepPostItsForLastRenteAccordeeOnly(mgr);
    }

    @Override
    protected void _findPrevious(final BIPersistentObjectList persistentList, final FWAction action,
            final BISession session) throws Exception {
        RERenteAccordeeJointDemandeRenteListViewBean mgr = (RERenteAccordeeJointDemandeRenteListViewBean) persistentList;
        mgr.setScreenMode(true);
        mgr.findPrev();
        REPostItsFilteringUtils.keepPostItsForLastRenteAccordeeOnly(mgr);
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
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {
        super._retrieve(viewBean, action, session);

        // Charger les informations comptables
        REInformationsComptabilite ic = new REInformationsComptabilite();
        ic.setSession((BSession) session);
        ic.setIdInfoCompta(((RERenteAccordeeJointDemandeRenteViewBean) viewBean).getIdInfoCompta());
        ic.retrieve();

        if (!((RERenteAccordeeJointDemandeRenteViewBean) viewBean).isRetourDepuisPyxis()) {

            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setRetourDepuisPyxis(false);

            if (JadeStringUtil.isEmpty(((RERenteAccordeeJointDemandeRenteViewBean) viewBean)
                    .getIdTiersAdressePmtICDepuisPyxis())) {
                ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdInfoComptaIC(ic.getIdInfoCompta());
                ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdTiersAdressePmtIC(ic.getIdTiersAdressePmt());
                // ((RERenteAccordeeJointDemandeRenteViewBean)viewBean).setIdAffilieAdressePmtIC(ic.getIdAffilieAdressePmt());
                // ((RERenteAccordeeJointDemandeRenteViewBean)viewBean).setIdDomaineApplicationIC(ic.getIdDomaineApplication());
                ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdCompteAnnexeIC(ic.getIdCompteAnnexe());
            }
        }

        if (!JadeStringUtil.isEmpty(((RERenteAccordeeJointDemandeRenteViewBean) viewBean).getIsChangeBeneficiaire())) {
            ic.setSession((BSession) session);

            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean)
                    .setIdTiersBeneficiaire(((RERenteAccordeeJointDemandeRenteViewBean) viewBean)
                            .getIdTiersAdressePmtICDepuisPyxis());

            // Recherche de l'adresse de paiement
            ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData((BSession) session,
                    ((BSession) session).getCurrentThreadTransaction(),
                    ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).getIdTiersBeneficiaire(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA()));

            // Les setter dans le viewBean
            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdInfoComptaIC(ic.getIdInfoCompta());
            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdTiersAdressePmtIC(ic.getIdTiersAdressePmt());
            // ((RERenteAccordeeJointDemandeRenteViewBean)viewBean).setIdAffilieAdressePmtIC(ic.getIdAffilieAdressePmt());
            // ((RERenteAccordeeJointDemandeRenteViewBean)viewBean).setIdDomaineApplicationIC(ic.getIdDomaineApplication());
            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdCompteAnnexeIC(ic.getIdCompteAnnexe());

        }

        // charger l'adresse de paiement
        rechargerAdressePaiement((BSession) session, (RERenteAccordeeJointDemandeRenteViewBean) viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        // Création de la transaction

        BSession session1 = (BSession) session;

        BTransaction transaction = null;
        transaction = (BTransaction) (session1).newTransaction();

        boolean updateCodeCasSpeciauxAnnonce = false;
        String idRenteCalculee = "";

        try {
            transaction.openTransaction();

            // récupération du viewBean et retrieve de la rente accordée, puis
            // update
            RERenteAccordeeJointDemandeRenteViewBean renteAccordeeVb = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;

            RERenteAccordee renteAccordee = new RERenteAccordee();
            renteAccordee.setIdPrestationAccordee(renteAccordeeVb.getIdPrestationAccordee());
            renteAccordee.setSession((BSession) session);
            renteAccordee.retrieve(transaction);

            REBasesCalcul baseCalcul = new REBasesCalcul();
            baseCalcul.setSession(session1);
            baseCalcul.setIdBasesCalcul(renteAccordee.getIdBaseCalcul());
            baseCalcul.retrieve(transaction);

            idRenteCalculee = baseCalcul.getIdRenteCalculee();

            // on regarde si il faut metre a jours les codes cas speciaux de
            // l'annonce liee cette rente accordee
            if (IREPrestationAccordee.CS_ETAT_CALCULE.equals(renteAccordee.getCsEtat())
                    && (!renteAccordee.getCodeCasSpeciaux1().equals(renteAccordeeVb.getCodeCasSpeciaux1())
                            || !renteAccordee.getCodeCasSpeciaux2().equals(renteAccordeeVb.getCodeCasSpeciaux2())
                            || !renteAccordee.getCodeCasSpeciaux3().equals(renteAccordeeVb.getCodeCasSpeciaux3())
                            || !renteAccordee.getCodeCasSpeciaux4().equals(renteAccordeeVb.getCodeCasSpeciaux4()) || !renteAccordee
                            .getCodeCasSpeciaux5().equals(renteAccordeeVb.getCodeCasSpeciaux5()))) {

                updateCodeCasSpeciauxAnnonce = true;
            }

            renteAccordee.setAnneeAnticipation(renteAccordeeVb.getAnneeAnticipation());
            renteAccordee.setAnneeMontantRAM(renteAccordeeVb.getAnneeMontantRAM());
            renteAccordee.setCodeAuxilliaire(renteAccordeeVb.getCodeAuxilliaire());
            renteAccordee.setCodeCasSpeciaux1(renteAccordeeVb.getCodeCasSpeciaux1());
            renteAccordee.setCodeCasSpeciaux2(renteAccordeeVb.getCodeCasSpeciaux2());
            renteAccordee.setCodeCasSpeciaux3(renteAccordeeVb.getCodeCasSpeciaux3());
            renteAccordee.setCodeCasSpeciaux4(renteAccordeeVb.getCodeCasSpeciaux4());
            renteAccordee.setCodeCasSpeciaux5(renteAccordeeVb.getCodeCasSpeciaux5());
            renteAccordee.setCodeMutation(renteAccordeeVb.getCodeMutation());
            renteAccordee.setCodePrestation(renteAccordeeVb.getCodePrestation());
            renteAccordee.setCodeSurvivantInvalide(renteAccordeeVb.getCodeSurvivantInvalide());
            renteAccordee.setCsEtat(renteAccordeeVb.getCsEtat());
            renteAccordee.setCsEtatCivil(renteAccordeeVb.getCsEtatCivil());
            renteAccordee.setCsRelationAuRequerant(renteAccordeeVb.getCsRelationAuRequerant());
            renteAccordee.setDateDebutAnticipation(renteAccordeeVb.getDateDebutAnticipation());
            renteAccordee.setDateDebutDroit(renteAccordeeVb.getDateDebutDroit());
            renteAccordee.setDateEcheance(renteAccordeeVb.getDateEcheance());
            renteAccordee.setDateFinDroit(renteAccordeeVb.getDateFinDroit());
            renteAccordee.setDateFinDroitPrevueEcheance(renteAccordeeVb.getDateFinDroitPrevueEcheance());
            renteAccordee.setDateRevocationAjournement(renteAccordeeVb.getDateRevocationAjournement());
            renteAccordee.setDureeAjournement(renteAccordeeVb.getDureeAjournement());
            renteAccordee.setFractionRente(renteAccordeeVb.getFractionRente());
            renteAccordee.setIdBaseCalcul(renteAccordeeVb.getIdBaseCalcul());
            renteAccordee.setIdDemandePrincipaleAnnulante(renteAccordeeVb.getIdDemandePrincipaleAnnulante());
            renteAccordee.setReferencePmt(renteAccordeeVb.getReferencePmt());
            renteAccordee.setIdInfoCompta(renteAccordeeVb.getIdInfoCompta());

            // Mise à jour de l'info compta
            doMajInfoCompta((BSession) session, transaction, renteAccordee.getIdPrestationAccordee(),
                    renteAccordeeVb.getIdTiersAdressePmtIC());

            REInformationsComptabilite rc = new REInformationsComptabilite();
            rc.setSession((BSession) session);
            rc.setIdInfoCompta(renteAccordee.getIdInfoCompta());
            rc.retrieve(transaction);

            if (rc.isNew()) {
                rc.setIdTiersAdressePmt(renteAccordeeVb.getIdTiersAdressePmtIC());
                // rc.setIdAffilieAdressePmt(renteAccordeeVb.getIdAffilieAdressePmtIC());
                // rc.setIdDomaineApplication(renteAccordeeVb.getIdDomaineApplicationIC());
                rc.add(transaction);
            } else {
                rc.setIdTiersAdressePmt(renteAccordeeVb.getIdTiersAdressePmtIC());
                // rc.setIdAffilieAdressePmt(renteAccordeeVb.getIdAffilieAdressePmtIC());
                // rc.setIdDomaineApplication(renteAccordeeVb.getIdDomaineApplicationIC());
                rc.update(transaction);
            }

            renteAccordee.setIdPrestationAccordee(renteAccordeeVb.getIdPrestationAccordee());
            renteAccordee.setIdTiersBeneficiaire(renteAccordeeVb.getIdTiersBeneficiaire());

            // Retrouver l'idTiers par le NSS et le setter
            PRTiersWrapper tiers = PRTiersHelper.getTiers(session1, renteAccordeeVb.getNssTiersComplementaire1a());

            if (tiers != null) {
                renteAccordee.setIdTiersComplementaire1(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            } else {
                renteAccordee.setIdTiersComplementaire1("");
            }

            tiers = PRTiersHelper.getTiers(session1, renteAccordeeVb.getNssTiersComplementaire2a());

            if (tiers != null) {
                renteAccordee.setIdTiersComplementaire2(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            } else {
                renteAccordee.setIdTiersComplementaire2("");
            }

            renteAccordee.setCodeRefugie(renteAccordeeVb.getCodeRefugie());
            renteAccordee.setIsTraitementManuel(renteAccordeeVb.isTraitementManuel());
            renteAccordee.setMontantPrestation(renteAccordeeVb.getMontantPrestation());
            renteAccordee.setMontantReducationAnticipation(renteAccordeeVb.getMontantReducationAnticipation());
            renteAccordee.setMontantRenteOrdiRemplacee(renteAccordeeVb.getMontantRenteOrdiRemplacee());
            renteAccordee.setPrescriptionAppliquee(renteAccordeeVb.getPrescriptionAppliquee());
            renteAccordee.setReductionFauteGrave(renteAccordeeVb.getReductionFauteGrave());
            renteAccordee.setSupplementAjournement(renteAccordeeVb.getSupplementAjournement());
            renteAccordee.setSupplementVeuvage(renteAccordeeVb.getSupplementVeuvage());

            renteAccordee.update(transaction);

            if (updateCodeCasSpeciauxAnnonce) {
                REAnnonceRenteManager arMgr = new REAnnonceRenteManager();
                arMgr.setSession((BSession) session);
                arMgr.setForIdRenteAccordee(renteAccordee.getIdPrestationAccordee());
                arMgr.find(transaction);

                if (arMgr.getSize() > 0) {
                    REAnnonceRente ar = (REAnnonceRente) arMgr.getFirstEntity();

                    // le header pour retrouver le lien sur le code
                    // enregistrement 02
                    REAnnonceHeader ah = new REAnnonceHeader();
                    ah.setSession((BSession) session);
                    ah.setIdAnnonce(ar.getIdAnnonceHeader());
                    ah.retrieve(transaction);

                    // mise a jours de codes cas speciaux
                    REAnnoncesAbstractLevel2A aal2a = new REAnnoncesAbstractLevel2A();
                    aal2a.setSession((BSession) session);
                    aal2a.setIdAnnonce(ah.getIdLienAnnonce());
                    aal2a.retrieve(transaction);

                    if (!aal2a.isNew()) {
                        aal2a.setCasSpecial1(renteAccordeeVb.getCodeCasSpeciaux1());
                        aal2a.setCasSpecial2(renteAccordeeVb.getCodeCasSpeciaux2());
                        aal2a.setCasSpecial3(renteAccordeeVb.getCodeCasSpeciaux3());
                        aal2a.setCasSpecial4(renteAccordeeVb.getCodeCasSpeciaux4());
                        aal2a.setCasSpecial5(renteAccordeeVb.getCodeCasSpeciaux5());

                        aal2a.update(transaction);
                    }

                    // mise a jours de SPY a la dur
                    REAnnoncesAugmentationModification10Eme aa10 = new REAnnoncesAugmentationModification10Eme();
                    aa10.setSession((BSession) session);
                    aa10.setIdAnnonce(ar.getIdAnnonceHeader());
                    aa10.retrieve(transaction);

                    // si c'est pas une 10, c'est une 9
                    if (aa10.isNew()) {
                        REAnnoncesAugmentationModification9Eme aa9 = new REAnnoncesAugmentationModification9Eme();
                        aa9.setSession((BSession) session);
                        aa9.setIdAnnonce(ar.getIdAnnonceHeader());
                        aa9.retrieve(transaction);

                        aa9.update(transaction);

                    } else {
                        aa10.update(transaction);
                    }
                }
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
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
        // mise à jour de la période de la demande
        REDemandeRente demandeRenteEntity = new REDemandeRente();
        demandeRenteEntity.setSession((BSession) session);
        demandeRenteEntity.setIdRenteCalculee(idRenteCalculee);
        demandeRenteEntity.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demandeRenteEntity.retrieve();

        if (!demandeRenteEntity.isNew()) {
            DemandeRente demandeRente = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                    Long.parseLong(demandeRenteEntity.getIdDemandeRente()));
            CorvusServiceLocator.getDemandeRenteService().mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(
                    demandeRente);
        }
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionBloquerRA(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        RERenteAccordeeJointDemandeRenteViewBean raViewBean = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;

        RERenteAccordee ra = new RERenteAccordee();
        ra.setIdPrestationAccordee(raViewBean.getIdPrestationAccordee());
        ra.setSession(session);
        ra.retrieve();

        ra.setIsPrestationBloquee(Boolean.TRUE);
        ra.setTypeDeMiseAJours(IREPrestationAccordee.CS_MAJ_BLOCAGE_MANUELLE);
        ra.update();

        return raViewBean;
    }

    /**
     * Affiche la page de déblocage du montant de la rente accodrée
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionDebloquerMontantRA(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        REDebloquerMontantRAViewBean draViewBean = null;

        REPrestationsAccordees pracc = new REPrestationsAccordees();
        pracc.setSession(session);

        // Ce cas arrive lors du retour depuis pyxis !!!
        if ((viewBean instanceof REDebloquerMontantRAViewBean)
                && ((REDebloquerMontantRAViewBean) viewBean).isRetourDepuisPyxis()) {

            draViewBean = (REDebloquerMontantRAViewBean) viewBean;
        } else {

            RERenteAccordeeJointDemandeRenteViewBean raViewBean = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;
            draViewBean = new REDebloquerMontantRAViewBean();
            draViewBean.setISession(session);
            draViewBean.setIdRenteAccordee(raViewBean.getIdPrestationAccordee());
            draViewBean.setIdTiersBeneficiaire(raViewBean.getIdTiersBeneficiaire());

            // recherche de la description du tiers beneficiaire
            PRTiersWrapper beneficiaire = PRTiersHelper.getTiersParId(session, draViewBean.getIdTiersBeneficiaire());
            if (beneficiaire != null) {
                draViewBean.setTiersBeneficiaireInfo(PRNSSUtil.formatDetailRequerantDetail(
                        beneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                        beneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + beneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                        beneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                        session.getCodeLibelle(beneficiaire.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                        getLibellePays(beneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session)));
            }
            pracc.setIdPrestationAccordee(raViewBean.getIdPrestationAccordee());
            pracc.retrieve();

            // recherche du total bloque
            if (!JadeStringUtil.isBlankOrZero(pracc.getIdEnteteBlocage())) {
                REEnteteBlocage entete = new REEnteteBlocage();
                entete.setSession(session);
                entete.setIdEnteteBlocage(pracc.getIdEnteteBlocage());

                FWCurrency montantBlk = new FWCurrency(0);
                try {
                    entete.retrieve();
                    if (!entete.isNew()) {
                        montantBlk.add(entete.getMontantBloque());
                        montantBlk.sub(entete.getMontantDebloque());
                        draViewBean.setMontantADebloquer(montantBlk.toString());
                        draViewBean.setMontantDebloque(entete.getMontantDebloque());
                        draViewBean.setMontantBloque(entete.getMontantBloque());
                    }
                } catch (Exception e) {
                    ;
                }
            }

            // On charge l'adresse de pmt...
            REInformationsComptabilite ic = pracc.loadInformationsComptabilite();

            draViewBean.setIdTiersAdrPmt(ic.getIdTiersAdressePmt());
            draViewBean.setIdDomaineAdrPmt(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            draViewBean.setIdCompteAnnexe(ic.getIdCompteAnnexe());
        }

        chercherIdTiersFamille(draViewBean, action, session);

        draViewBean.loadAdressePaiement(JACalendar.todayJJsMMsAAAA());
        draViewBean.setRetourDepuisPyxis(false);
        return draViewBean;

    }

    public FWViewBeanInterface actionDesactiverBlocage(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        RERenteAccordeeJointDemandeRenteViewBean raViewBean = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;
        RERenteAccordee ra = new RERenteAccordee();
        ra.setIdPrestationAccordee(raViewBean.getIdPrestationAccordee());
        ra.setSession(session);
        ra.retrieve();
        ra.setIsPrestationBloquee(Boolean.FALSE);
        ra.setTypeDeMiseAJours("0");
        ra.setTypeDeMiseAJours("0");
        ra.update();
        return raViewBean;
    }

    public FWViewBeanInterface actionExecuterDeblocage(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        REDebloquerMontantRenteAccordeeProcess process = new REDebloquerMontantRenteAccordeeProcess(session);
        process.setIdDomaine(((REDebloquerMontantRAViewBean) viewBean).getIdDomaineAdrPmt());
        process.setIdRenteAccordee(((REDebloquerMontantRAViewBean) viewBean).getIdRenteAccordee());
        process.setIdSection(((REDebloquerMontantRAViewBean) viewBean).getIdSection());
        process.setIdTiersAdrPmt(((REDebloquerMontantRAViewBean) viewBean).getIdTiersAdrPmt());
        process.setMontantADebloque(((REDebloquerMontantRAViewBean) viewBean).getMontantADebloquer());
        process.setRefPaiement(((REDebloquerMontantRAViewBean) viewBean).getRefPmt());
        process.setControleTransaction(true);
        process.setEMailAddress(((REDebloquerMontantRAViewBean) viewBean).getEMailAdress());
        process.setSendCompletionMail(true);
        process.start();

        return viewBean;
    }

    private void chercherIdTiersFamille(final REDebloquerMontantRAViewBean viewBean, final FWAction action,
            final BISession session) throws Exception {

        // récupération des ID Tiers des membres de la famille (étendue)
        Set<String> idMembreFamille = new HashSet<String>();

        Set<PRTiersWrapper> famille = SFFamilleUtils.getTiersFamilleProche(viewBean.getSession(),
                viewBean.getIdTiersBeneficiaire());

        for (PRTiersWrapper unMembre : famille) {
            if (!JadeStringUtil.isBlank(unMembre.getIdTiers())) {
                idMembreFamille.add(unMembre.getIdTiers());
            }
        }

        viewBean.setIdTiersFamille(idMembreFamille);
    }

    /**
     * Le nom de cette méthode est provisoire.
     * Le but de cette méthode est de permettre la mise à jour des informations comptables liées à une rente accordée.
     * 
     * @throws Exception
     */
    private void doMajInfoCompta(final BSession session, final BTransaction transaction, final String idRA,
            final String newIDAdressePaiement) throws Exception {

        // Récupérer la rente accordée du tiers bénéficiaire
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRA);
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);

        // Récupérer les informations comptables du tiers en cours (TEC)
        REInformationsComptabilite infoComptaTEC = ra.loadInformationsComptabilite();
        String idTEC = ra.getIdTiersBeneficiaire();

        REDemandeRente demande = getDemandeRente(session, transaction, ra.getIdPrestationAccordee());

        // Récupérer l'id du tiers requérant de la demande
        String idTiersRequerant = demande.loadDemandePrestation(transaction).getIdTiers();

        // Récupération des membres de la famille du tiers requérant
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        // Récupération des membres de la famille du requérant
        ISFMembreFamilleRequerant[] membres = sf.getMembresFamilleRequerant(idTiersRequerant);

        if (membres == null) {
            throw new RETechnicalException(session.getLabel("ERREUR_ASSURE_EXISTE_PAS_DS_FAMILLE"));
        }

        /**
         * 1. VERIFIER LE GROUPE LEVEL DU TEC
         * 2. COMMUTER DANS LE BON LEVEL
         * 
         */

        int groupLevelTEC = REBeneficiairePrincipal.getGroupLevel(session, transaction, idRA);

        checkGroupLevelAndCommuteToLevelTreat(session, transaction, idRA, newIDAdressePaiement, infoComptaTEC, idTEC,
                membres, groupLevelTEC);

    }

    /**
     * Cette méthode permet de vérifier dans quel Group level on se trouve et d'appeler le bon traitement en fonction de
     * cela
     * 
     * @param session
     * @param idRA
     * @param idt
     * @param icTEC
     * @param membres
     * @param groupLevelTEC
     * @throws Exception
     */
    private void checkGroupLevelAndCommuteToLevelTreat(final BSession session, final BTransaction transaction,
            final String idRA, final String newIDAdressePaiement, REInformationsComptabilite infoComptaTEC,
            String idTEC, ISFMembreFamilleRequerant[] membres, int groupLevelTEC) throws Exception {
        if (groupLevelTEC == 1 || groupLevelTEC == 2 || groupLevelTEC == 4 || groupLevelTEC == 5) {
            treatGroupLevels(session, transaction, infoComptaTEC, idTEC, newIDAdressePaiement, membres);
        } else {
            String message = java.text.MessageFormat.format(session.getLabel("GROUP_LEVEL_NOT_RECOGNISED"),
                    new Object[] { groupLevelTEC, idRA });
            throw new RETechnicalException(message);
        }
    }

    /**
     * Méthode de traitement de groupe de niveau 1 dans le rentes.
     * Cette méthode effectue le travail suivant -> mise à jour de l'adresse de paiement niveau 1 (+ éventuellement le
     * CA)
     * Vérification des rentes liées -> Si niveau 5 dans la situation familiale ALORS vérification pour mise à jour du
     * CA
     * 
     * @param session
     * @param transaction
     * @param icTEC : info comptable du tiers en cours
     * @param idTEC : identifiant unique du tiers bénéficiaire en cours
     * @param newIDAdressePaiement : nouvel id adresse paiement
     * @param membres : membres de la situation familiale liés au tiers requérant
     * @throws Exception
     */
    private void treatGroupLevels(final BSession session, final BTransaction transaction,
            REInformationsComptabilite infoComptaTEC, String idTEC, String newIDAdressePaiement,
            ISFMembreFamilleRequerant[] membres) throws Exception {

        updateAdressePaiementForTEC(session, transaction, infoComptaTEC, idTEC, newIDAdressePaiement);
        /**
         * 
         * 2. Vérifier les niveaux inférieurs de la situation familiale
         * 
         */
        // Préparation d'une MAP triée par groupe level
        Map<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapSFSortedByGroupLevel = prepareMapSortedByGroupLevel(
                session, transaction, membres);

        // Si la map contient des niveaux 5, on traite les cas.
        if (mapSFSortedByGroupLevel.containsKey(5)) {
            List<RERenteAccJoinTblTiersJoinDemandeRente> listCAToDefine = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
            // parcourt de toutes les rentes contenues dans cette liste et traitement
            for (RERenteAccJoinTblTiersJoinDemandeRente ra : mapSFSortedByGroupLevel.get(5)) {
                boolean isInCAToDefineList = false;
                if (isSpecifiqueRenteType(ra)) {
                    // on vérifie si la rente accordée contient un NSS complémentaire 2
                    if (!JadeStringUtil.isBlankOrZero(ra.getIdTiersComplementaire2())) {

                        isInCAToDefineList = updateCAForNSSComplementaire2InEveryParentLevels(mapSFSortedByGroupLevel,
                                ra, session, transaction);

                    } else {
                        // stocker la RA dans une liste temporaire afin de la retraiter plus tard dans ce processus.
                        isInCAToDefineList = true;
                    }

                } else {
                    isInCAToDefineList = verifyRenteParentByIdTiersBaseCalcul(session, transaction, ra);
                }

                if (isInCAToDefineList) {
                    listCAToDefine.add(ra);
                }
            }
            // Si on a plus d'un cas dans la liste des cas temporaire, on compare les cas et on met à jour en fonction
            // des id adresse paiement semblables
            if (listCAToDefine.size() > 1) {
                treatTempCAToDefineListFromLevel5(session, transaction, listCAToDefine);
            }

        }

    }

    /**
     * @param session
     * @param transaction
     * @param infoComptaTEC
     * @param idTEC
     * @param newIDAdressePaiement
     * @throws Exception
     */
    private void updateAdressePaiementForTEC(final BSession session, final BTransaction transaction,
            REInformationsComptabilite infoComptaTEC, String idTEC, String newIDAdressePaiement) throws Exception {
        /**
         * 1. Mettre à jour l'adresse de paiement du tiers en cours (TEC) + CA
         */
        infoComptaTEC.setIdTiersAdressePmt(newIDAdressePaiement);
        // vérifier si le compte annexe est identique au CA lié au NSS du tiers courant -> sinon, mise à jour:
        // Création du CA pour le TEC si non existant -> l'update de l'info compta est délégué à cette méthode.
        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, infoComptaTEC,
                IREValidationLevel.VALIDATION_LEVEL_ALL);
    }

    /**
     * Cette méthode permet de vérifier si le tiers a un parent en récupérant l'id de base de calcul.
     * S'il y a bien un tiers parent de niveau supérieur, on prend le tiers au niveau le plus élevé et on compare les
     * comptes annexes.
     * Si le compte annexe est différent, on récupère celui du tiers base de calcul et on l'assigne au fils (niveau 5).
     * 
     * Cette méthode est prévue pour tous les types de rentes sauf les suivants : 13, 14, 15, 16, 23, 24, 25, 26
     * 
     * @param session
     * @param transaction
     * @param ra
     * @param isInCAToDefineList
     * @return
     * @throws Exception
     */
    private boolean verifyRenteParentByIdTiersBaseCalcul(final BSession session, final BTransaction transaction,
            RERenteAccJoinTblTiersJoinDemandeRente ra) throws Exception {
        // récupérer la base de calcul
        REBasesCalcul baseCalcul = new REBasesCalcul();
        baseCalcul.setSession(session);
        baseCalcul.setIdBasesCalcul(ra.getIdBaseCalcul());
        baseCalcul.retrieve(transaction);

        boolean isInCAToDefineList = false;

        // Si l'id tiers base de calcul est différent à l'id tiers bénéficiaire de la RA, il faut charger la
        // rente du tiers base calcul et comparer les info comptable
        if (!ra.getIdTiersBeneficiaire().equals(baseCalcul.getIdTiersBaseCalcul())) {

            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(baseCalcul.getIdTiersBaseCalcul());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            Map<Integer, String> mapIdCASortedByGroupLevel = new HashMap<Integer, String>();

            // pour chaque rente on vérifie le level. On prend la référence compte annexe de la rente de
            // niveau le plus fort
            Integer bestLevel = 4;
            for (int i = 0; i < mgr.size(); i++) {
                RERenteAccJoinTblTiersJoinDemandeRente raRefRente = (RERenteAccJoinTblTiersJoinDemandeRente) mgr
                        .getEntity(i);
                REInformationsComptabilite infoComptaRefRente = raRefRente.loadInformationsComptabilite();

                int groupLevelRefRente = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                        getIdsRaTiers(session, transaction, raRefRente.getIdTiersBeneficiaire()));

                mapIdCASortedByGroupLevel.put(groupLevelRefRente, infoComptaRefRente.getIdCompteAnnexe());

                if (groupLevelRefRente < bestLevel) {
                    bestLevel = groupLevelRefRente;
                }

            }

            String idCAToChange = null;
            // on récupère le meilleur niveau de la map
            if (mapIdCASortedByGroupLevel.containsKey(bestLevel)) {
                idCAToChange = mapIdCASortedByGroupLevel.get(bestLevel);
            }

            if (idCAToChange != null) {
                // On charge les informations comptables de la rente accordée du tiers membre famille en
                // cours
                REInformationsComptabilite infoComptaRenteMembreFamille = ra.loadInformationsComptabilite();

                if (!infoComptaRenteMembreFamille.getIdCompteAnnexe().equals(idCAToChange)) {

                    infoComptaRenteMembreFamille.setIdCompteAnnexe(idCAToChange);
                    infoComptaRenteMembreFamille.setSession(session);
                    infoComptaRenteMembreFamille.update(transaction);
                }

            } else {
                // on a trouvé aucune rente, dans ce cas on délègue la liste des définitions de niveau 5
                isInCAToDefineList = true;
            }
        }
        return isInCAToDefineList;
    }

    /**
     * 
     * Cette méthode vérifie les cas de figures pour lequels il est nécessaire de comparer les niveaux 5 entre eux.
     * 
     * On vérifie si des rentes de niveau 5 pointent sur le même id adresse paiement. Si c'est le cas, on va vérifier
     * quel est le tiers le plus jeune.
     * 
     * On met ensuite à jour tous les CA pointant sur la même adresse de paiement avec l'id du tiers le plus jeune.
     * 
     * @param session
     * @param transaction
     * @param listCAToDefine
     * @throws Exception
     */
    private void treatTempCAToDefineListFromLevel5(final BSession session, final BTransaction transaction,
            List<RERenteAccJoinTblTiersJoinDemandeRente> listCAToDefine) throws Exception {
        Map<String, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapRAByIDAdressePaiement = transformListToSortedMapByIdAdressePaiement(listCAToDefine);
        for (Map.Entry<String, List<RERenteAccJoinTblTiersJoinDemandeRente>> entry : mapRAByIDAdressePaiement
                .entrySet()) {
            // Si on a plus d'un élément qui pointe sur la même adresse de paiement, on doit effectuer un
            // traitement spécifique (vérification des âges)
            if (entry.getValue().size() > 1) {
                Date tempDateNaissance = new Date("01.01.1900");
                RERenteAccJoinTblTiersJoinDemandeRente tempRefRente = null;
                // itérer sur toutes les rentes :
                for (RERenteAccJoinTblTiersJoinDemandeRente ra : entry.getValue()) {

                    // Récupérer le tiers rentier
                    PRTiersWrapper tiersFamille = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
                    // On prend le tiers le plus jeune
                    Date dateMbrfm = new Date(tiersFamille.getDateNaissance());

                    if (dateMbrfm.compareTo(tempDateNaissance) > 0) {
                        tempDateNaissance = dateMbrfm;
                        tempRefRente = ra;
                    }
                }
                if (tempRefRente != null) {
                    // Une fois que l'on a la rente de référence, on l'enlève de la liste.
                    entry.getValue().remove(tempRefRente);
                    // On charge ensuite les informations comptable de la rente de référence
                    REInformationsComptabilite raInfoComptableRefRente = tempRefRente.loadInformationsComptabilite();
                    // re- parcourt de la liste des rentes pour mise à jour des info comptables
                    for (RERenteAccJoinTblTiersJoinDemandeRente ra : entry.getValue()) {

                        REInformationsComptabilite raInfoComptable = ra.loadInformationsComptabilite();

                        if (!raInfoComptable.getIdCompteAnnexe().equals(raInfoComptableRefRente.getIdCompteAnnexe())) {

                            raInfoComptable.setIdCompteAnnexe(raInfoComptableRefRente.getIdCompteAnnexe());
                            raInfoComptable.setSession(session);
                            raInfoComptable.update(transaction);
                        }

                    }

                }
            }
        }
    }

    /**
     * 
     * Cette méthode parcourt une liste de rentes accordées passés en paramètre et créer une map de type String - LIST
     * avec l'id tiers adresse de paiement comme clé.
     * 
     * Cela permet d'effectuer un premier tri et de voir quels tiers partagent la même adresse de paiement.
     * 
     * @return Map<String, List<RERenteAccJoinTblTiersJoinDemandeRente>>
     */
    private Map<String, List<RERenteAccJoinTblTiersJoinDemandeRente>> transformListToSortedMapByIdAdressePaiement(
            List<RERenteAccJoinTblTiersJoinDemandeRente> listCAToDefine) {

        Map<String, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapRAByIDAdressePaiement = new HashMap<String, List<RERenteAccJoinTblTiersJoinDemandeRente>>();

        for (RERenteAccJoinTblTiersJoinDemandeRente ra : listCAToDefine) {

            if (!mapRAByIDAdressePaiement.containsKey(ra.getIdTiersAdressePmt())) {
                List<RERenteAccJoinTblTiersJoinDemandeRente> raSortedList = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
                raSortedList.add(ra);
                mapRAByIDAdressePaiement.put(ra.getIdTiersAdressePmt(), raSortedList);

            } else {

                mapRAByIDAdressePaiement.get(ra.getIdTiersAdressePmt()).add(ra);
            }
        }
        return mapRAByIDAdressePaiement;
    }

    /**
     * Cette méthode s'occupe de vérifier si le tiers membres famille a un parent dans le niveau 1, 2 ou 4.
     * 
     * Si c'est le cas, une comparaison des idCompteAnnexe est effectuée afin de voir si le tiers pointe sur le même
     * compte annexe que son parent.
     * 
     * Si le tiers ne pointe pas sur le même compte annexe que son parent, le compte annexe est mis à jour en prenant
     * celui du parent.
     * 
     * Cette méthode retourne un booléen qui permet de tracer l'update réalisé en cas de mise à jour de compte annexe.
     * 
     * @param mapSFSortedByGroupLevel
     * @param raMbrf
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private boolean updateCAForNSSComplementaire2InEveryParentLevels(
            Map<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapSFSortedByGroupLevel,
            RERenteAccJoinTblTiersJoinDemandeRente raMbrf, final BSession session, final BTransaction transaction)
            throws Exception {
        // booléen permettant de dire si le cas doit être à nouveau traité dans la liste temporaire pour les niveaux 5.
        boolean isInCAToDefineList = true;
        // vérification level 1
        RERenteAccJoinTblTiersJoinDemandeRente raToCheck = findIdNSSComplementaire2InSpecifiedLowerLevel(
                mapSFSortedByGroupLevel, raMbrf, 1);
        // Si on a rien trouvé à ce niveau, on vérifie le level 2
        if (raToCheck == null) {
            raToCheck = findIdNSSComplementaire2InSpecifiedLowerLevel(mapSFSortedByGroupLevel, raMbrf, 2);
        }
        // Si le niveau 2 n'a rien donné on vérifie le niveau final 4
        if (raToCheck == null) {
            raToCheck = findIdNSSComplementaire2InSpecifiedLowerLevel(mapSFSortedByGroupLevel, raMbrf, 4);
        }
        // Si après tous les tests on a bien une rente accordée, on va la récupérer et vérifier si l'id compte annexe
        // est identique à celui du membre famille en cours
        if (raToCheck != null) {

            REInformationsComptabilite infoComptableTiersHigherLevel = raToCheck.loadInformationsComptabilite();
            REInformationsComptabilite infoComptableTiersMembreFamille = raMbrf.loadInformationsComptabilite();

            if (!infoComptableTiersHigherLevel.getIdCompteAnnexe().equals(
                    infoComptableTiersMembreFamille.getIdCompteAnnexe())) {
                // Si les idCompteAnnexe sont différents, on récupère celui du parent
                infoComptableTiersMembreFamille.setIdCompteAnnexe(infoComptableTiersHigherLevel.getIdCompteAnnexe());
                infoComptableTiersMembreFamille.setSession(session);
                infoComptableTiersMembreFamille.update(transaction);

                isInCAToDefineList = false;
            }

        }

        return isInCAToDefineList;

    }

    /**
     * Cette méthode permet de rechercher les cas de rentes égales au NSS complémentaire et qui se trouvent dans le
     * niveau spécifié.
     * 
     * @param mapSFSortedByGroupLevel
     * @param raMbrf
     * @param levelNumber
     * @return
     */
    private RERenteAccJoinTblTiersJoinDemandeRente findIdNSSComplementaire2InSpecifiedLowerLevel(
            Map<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapSFSortedByGroupLevel,
            RERenteAccJoinTblTiersJoinDemandeRente raMbrf, int levelNumber) {

        RERenteAccJoinTblTiersJoinDemandeRente raToReturn = null;

        if (mapSFSortedByGroupLevel.containsKey(levelNumber)) {

            for (RERenteAccJoinTblTiersJoinDemandeRente ra : mapSFSortedByGroupLevel.get(levelNumber)) {

                // Si le bénéficiaire de la rente de niveau 1 est égal à l'id NSS complémentaire 2 de la rente du membre
                // famille niveau 5, on va comparer les CA et mettre à jour si nécessaire.
                if (ra.getIdTiersBeneficiaire().equals(raMbrf.getIdTiersComplementaire2())) {

                    raToReturn = ra;
                    break;
                }
            }
        }
        return raToReturn;
    }

    /**
     * Méthode qui permet de vérifier si la rente se trouve dans les types 14, 15, 16, 24, 25, 26.
     * Si c'est le cas, le traitement des rentes de niveau 5 doit se faire d'une façon différente.
     * 
     * @param ra
     * @return isSpecifiqueType -> booléen
     */
    private boolean isSpecifiqueRenteType(RERenteAccJoinTblTiersJoinDemandeRente ra) {

        boolean isSpecifiqueType = false;
        Map<String, String> mapGenrePrestation = new HashMap<String, String>();

        mapGenrePrestation.put(REGenresPrestations.GENRE_14, REGenresPrestations.GENRE_14);
        mapGenrePrestation.put(REGenresPrestations.GENRE_15, REGenresPrestations.GENRE_15);
        mapGenrePrestation.put(REGenresPrestations.GENRE_16, REGenresPrestations.GENRE_16);
        mapGenrePrestation.put(REGenresPrestations.GENRE_24, REGenresPrestations.GENRE_24);
        mapGenrePrestation.put(REGenresPrestations.GENRE_25, REGenresPrestations.GENRE_25);
        mapGenrePrestation.put(REGenresPrestations.GENRE_26, REGenresPrestations.GENRE_26);

        if (mapGenrePrestation.containsKey(ra.getCodePrestation())) {

            isSpecifiqueType = true;
        }
        return isSpecifiqueType;
    }

    /**
     * Cette méthode permet de créer une structure de données mappée sur les niveaux de rentes.
     * Chaque rente se trouve dans un certain niveau. On détermine pour chaque rente le niveau et on créer une map en
     * fonction du niveau :
     * 
     * EX: nom objet = rente1 niveau = 1 -> ajout de l'objet à la liste listLevel1.add(rente1) -> ajout de la liste à la
     * map -> [1
     * : listLevel1]
     * 
     * @param session
     * @param transaction
     * @param membres
     * @throws Exception
     */
    private Map<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>> prepareMapSortedByGroupLevel(BSession session,
            final BTransaction transaction, ISFMembreFamilleRequerant[] membres) throws Exception {

        // Instantiation d'une map afin de mémoriser les différents cas de situation familiale par groupe
        Map<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapSFByGroupLevel = new HashMap<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>>();
        // On prépare une nouvelle liste par groupe level
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel1 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel2 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel4 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel5 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        // Ajout des niveaux acceptés et des listes associés
        mapSFByGroupLevel.put(1, listRAGroupLevel1);
        mapSFByGroupLevel.put(2, listRAGroupLevel2);
        mapSFByGroupLevel.put(4, listRAGroupLevel4);
        mapSFByGroupLevel.put(5, listRAGroupLevel5);

        // Marqueur permettant de spécifier si un groupe level 5 est existant
        boolean hasGroupLevel5 = false;
        // On parcourt tous les membres de la famille et on récupère la liste des rentes de chaque membre.
        for (int i = 0; i < membres.length; i++) {
            ISFMembreFamilleRequerant mbr = membres[i];
            // Pour chaque membre famille, on récupère toutes les rentes en cours
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            // On parcourt chaque rente trouvée afin d'en déterminer le groupe level
            for (int j = 0; j < mgr.size(); j++) {
                RERenteAccJoinTblTiersJoinDemandeRente raMbrF = (RERenteAccJoinTblTiersJoinDemandeRente) mgr
                        .getEntity(j);

                int groupLevelMbrFamille = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                        getIdsRaTiers(session, transaction, raMbrF.getIdTiersBeneficiaire()));

                if (mapSFByGroupLevel.containsKey(groupLevelMbrFamille)) {
                    mapSFByGroupLevel.get(groupLevelMbrFamille).add(raMbrF);
                }

                if (groupLevelMbrFamille == 5) {
                    hasGroupLevel5 = true;
                }
            }
        }

        if (!hasGroupLevel5) {
            // Si on n'a pas de groupe level 5, on supprime l'entrée dans la map car un test est fait plus loin sur
            // cette entrée
            mapSFByGroupLevel.remove(5);
        }

        return mapSFByGroupLevel;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
            final BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private REDemandeRente getDemandeRente(final BSession session, final BTransaction transaction, final String idRA)
            throws Exception {
        // Récupération de la rente accordée
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRA);
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);

        // Recherche du tiers requérant de la demande

        // Retrieve Base de calcul
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);
        PRAssert.notIsNew(bc, null);

        // Retrieve rente calculee
        RERenteCalculee rc = new RERenteCalculee();
        rc.setSession(session);
        rc.setIdRenteCalculee(bc.getIdRenteCalculee());
        rc.retrieve(transaction);
        PRAssert.notIsNew(rc, null);
        // Retrieve demande rente
        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setIdRenteCalculee(rc.getIdRenteCalculee());
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.retrieve(transaction);
        PRAssert.notIsNew(demande, null);

        return demande;

    }

    private Set<Long> getIdsRaTiers(final BSession session, final BTransaction transaction, final String idTiers)
            throws Exception {
        Set<Long> result = new HashSet<Long>();

        // Récupération des infoCompta des membre de famille...
        RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
        mgr.setSession(session);
        mgr.setForIdTiersBeneficiaire(idTiers);
        // mgr.setForNoDemandeRente(idDemande);
        mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
        mgr.find(transaction);

        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) mgr.getEntity(i);
            result.add(Long.parseLong(elm.getIdPrestationAccordee()));
        }
        return result;
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    private String getLibellePays(final String idPays, final BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", idPays)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", idPays));
        }
    }

    /**
     * charge une adresse de paiement valide.
     * <p>
     * si les id adresse de paiment et domaine d'adresses sont renseignes, charge et formatte l'adresse correspondante,
     * sinon recherche, charge et formatte une adresse pour le tiers courant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param raViewBean
     * @throws Exception
     */
    private void rechargerAdressePaiement(final BSession session,
            final RERenteAccordeeJointDemandeRenteViewBean raViewBean) throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse
        // paiement
        if (raViewBean.isTiersBeneficiaireChange()) {

            raViewBean.setIdTiersAdressePmtIC(raViewBean.getIdTiersAdressePmtICDepuisPyxis());

        }

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise
        // dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(raViewBean.getIdTiersAdressePmtIC())) {
            return;
        }

        // charcher une adresse de paiement pour ce beneficiaire
        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), raViewBean.getIdTiersAdressePmtIC(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

        raViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                raViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                raViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            raViewBean.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            raViewBean.setCcpOuBanqueFormatte("");
            raViewBean.setAdresseFormattee("");

            // si le tiers beneficiaire a change et que l'on a pas trouve
            // d'adresse
            // on enleve l'idTiersAdresseDePaiement
            if (raViewBean.isTiersBeneficiaireChange()) {
                raViewBean.setIdTiersAdressePmtIC("0");
            }
        }
    }

}
