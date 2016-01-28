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
import globaz.corvus.process.REDebloquerMontantRenteAccordeeProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.REPostItsFilteringUtils;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.vb.process.REDebloquerMontantRAViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteListViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
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
import java.util.HashSet;
import java.util.Set;
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

    private void doMajInfoCompta(final BSession session, final BTransaction transaction, final String idRA,
            final String idt) throws Exception {

        // Récupération de la rente accordée
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRA);
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);

        // Récupération des informations du tiers en cours (TEC)
        REInformationsComptabilite icTEC = ra.loadInformationsComptabilite();
        String idTEC = ra.getIdTiersBeneficiaire();

        REDemandeRente demande = getDemandeRente(session, transaction, ra.getIdPrestationAccordee());

        String idTiersRequerant = demande.loadDemandePrestation(transaction).getIdTiers();

        // Récupération des membres de la famille du tiers requérant
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        // Récupération des membres de la famille du requérant
        ISFMembreFamilleRequerant[] membres = sf.getMembresFamilleRequerant(idTiersRequerant);

        if (membres == null) {
            throw new Exception(session.getLabel("ERREUR_ASSURE_EXISTE_PAS_DS_FAMILLE"));
        }

        /*
         * 
         * Identification du cas à traiter....
         */
        boolean isMembreFamilleReferenced = false;
        boolean isReferencedButNotAFamilyMember = false;

        for (int i = 0; i < membres.length; i++) {
            ISFMembreFamilleRequerant mbr = membres[i];

            if (JadeStringUtil.isBlankOrZero(mbr.getIdTiers())) {
                continue;
            }
            // skip du requérant...
            if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mbr.getRelationAuRequerant())) {
                continue;
            }

            // Récupération des infoCompta des membre de famille...
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
            // mgr.setForNoDemandeRente(demande.getIdDemandeRente());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            mgr.find(transaction);

            for (int j = 0; j < mgr.size(); j++) {
                RERenteAccJoinTblTiersJoinDemandeRente raMbrF = (RERenteAccJoinTblTiersJoinDemandeRente) mgr
                        .getEntity(j);
                REInformationsComptabilite icMbrF = raMbrF.loadInformationsComptabilite();
                if (idt.equals(raMbrF.getIdTiersBeneficiaire())) {
                    isMembreFamilleReferenced = true;
                }
                if (idt.equals(icMbrF.getIdTiersAdressePmt())) {
                    isReferencedButNotAFamilyMember = true;
                }
            }
        }

        /*
         * Cas a) idt ne correspond à aucun des membres de la famille et n'est référencé par aucun des mbr. de la
         * famille...
         */
        if (!isMembreFamilleReferenced && !isReferencedButNotAFamilyMember) {

            doTraitementCasA(session, transaction, idt, idTEC, icTEC, membres);

        }
        /*
         * Cas b) idt correspond à un ou plusieurs des mbres de famille
         */
        else if (isMembreFamilleReferenced) {

            doTraitementCasB(session, transaction, idt, idTEC, icTEC, membres);

        }
        /*
         * Cas c) idt ne correspond à aucun membre de famille, mais est référencé
         */
        else if (!isMembreFamilleReferenced && isReferencedButNotAFamilyMember) {

            doTraitementCasC(session, transaction, idt, idTEC, icTEC, membres);

        } else {
            throw new Exception(session.getLabel("ERREUR_CAS_NON_PRISENCOMPTE_TRAITEMENT"));
        }

        if ((IREPrestationAccordee.CS_ETAT_VALIDE.equals(ra.getCsEtat()) || IREPrestationAccordee.CS_ETAT_PARTIEL
                .equals(ra.getCsEtat()))

        &&

        JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())) {

            icTEC.retrieve(transaction);

            if (JadeStringUtil.isBlankOrZero(icTEC.getIdCompteAnnexe())) {
                throw new Exception("Aucun compte annexe pour la RA # " + ra.getIdPrestationAccordee()
                        + ". Veuillez informer GLOBAZ immédiatement de ce problème, merci.");
            }
        }

    }

    private void doTraitementCasA(final BSession session, final BTransaction transaction, final String idt,
            final String idTEC, final REInformationsComptabilite icTEC, final ISFMembreFamilleRequerant[] membresFamille)
            throws Exception {

        // Création du CA pour le TEC si non existant
        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, icTEC,
                IREValidationLevel.VALIDATION_LEVEL_ALL);

        icTEC.retrieve(transaction);

        icTEC.setIdTiersAdressePmt(idt);
        icTEC.update(transaction);

        int groupLevelTEC = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                getIdsRaTiers(session, transaction, idTEC));

        // Parcous de tous les membres de la famille...
        for (int i = 0; i < membresFamille.length; i++) {
            ISFMembreFamilleRequerant mbr = membresFamille[i];
            // skip du requérant...
            if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mbr.getRelationAuRequerant())) {
                continue;
            }

            if (JadeStringUtil.isBlankOrZero(mbr.getIdTiers())) {
                continue;
            }

            // Récupération des infoCompta des membre de famille...
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
            // mgr.setForNoDemandeRente(demande.getIdDemandeRente());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            mgr.find(transaction);

            for (int j = 0; j < mgr.size(); j++) {
                RERenteAccJoinTblTiersJoinDemandeRente raMbrF = (RERenteAccJoinTblTiersJoinDemandeRente) mgr
                        .getEntity(j);
                REInformationsComptabilite icMbrF = raMbrF.loadInformationsComptabilite();
                if (idTEC.equals(icMbrF.getIdTiersAdressePmt())) {

                    int groupLevelMbrFamille = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                            getIdsRaTiers(session, transaction, raMbrF.getIdTiersBeneficiaire()));

                    boolean isMbrFamillePlusVieux = false;
                    if (groupLevelMbrFamille == groupLevelTEC) {
                        PRTiersWrapper mbrFamille = PRTiersHelper.getTiersParId(session,
                                raMbrF.getIdTiersBeneficiaire());
                        JADate dnMF = new JADate(mbrFamille.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTEC);
                        JADate dnTEC = new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                        JACalendar cal = new JACalendarGregorian();
                        if (cal.compare(dnMF, dnTEC) == JACalendar.COMPARE_FIRSTLOWER) {
                            isMbrFamillePlusVieux = true;
                        }

                    }

                    // Les deux mbr de famille sont de même niveau et ont les deux une rente principale
                    if ((groupLevelMbrFamille == groupLevelTEC) && (groupLevelTEC == 1)) {

                        // Création du CA pour le TEC si non existant
                        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, icTEC,
                                IREValidationLevel.VALIDATION_LEVEL_ALL);
                        icTEC.retrieve(transaction);
                        icTEC.setIdTiersAdressePmt(idt);
                        icTEC.update(transaction);
                    }

                    // Le membre de famille est de niveau inférieur ou de même
                    // niveau, mais plus vieux...
                    else if ((groupLevelMbrFamille < groupLevelTEC)
                            || ((groupLevelMbrFamille == groupLevelTEC) && isMbrFamillePlusVieux)) {

                        icMbrF.setIdTiersAdressePmt(idt);
                        icMbrF.setIdCompteAnnexe(icTEC.getIdCompteAnnexe());
                        icMbrF.update(transaction);
                    } else {
                        icMbrF.setIdTiersAdressePmt(idt);
                        icMbrF.update(transaction);
                    }

                }
            }
        }

    }

    private void doTraitementCasB(final BSession session, final BTransaction transaction, final String idt,
            final String idTEC, final REInformationsComptabilite icTEC, final ISFMembreFamilleRequerant[] membresFamille)
            throws Exception {

        icTEC.retrieve(transaction);
        icTEC.setIdTiersAdressePmt(idt);
        icTEC.update(transaction);

        int groupLevelTEC = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                getIdsRaTiers(session, transaction, idTEC));

        // Parcous de tous les membres de la famille
        for (int i = 0; i < membresFamille.length; i++) {
            ISFMembreFamilleRequerant mbr = membresFamille[i];

            if (JadeStringUtil.isBlankOrZero(mbr.getIdTiers())) {
                continue;
            }

            // skip du requérant...
            if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mbr.getRelationAuRequerant())) {
                continue;
            }

            // Récupération des infoCompta des membre de famille...
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
            // mgr.setForNoDemandeRente(demande.getIdDemandeRente());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            mgr.find(transaction);

            // Parcous de tous les membres référencés...
            for (int j = 0; j < mgr.size(); j++) {
                RERenteAccJoinTblTiersJoinDemandeRente raMbrF = (RERenteAccJoinTblTiersJoinDemandeRente) mgr
                        .getEntity(j);
                REInformationsComptabilite icMbrF = raMbrF.loadInformationsComptabilite();

                if (idTEC.equals(raMbrF.getIdTiersBeneficiaire()) || idt.equals(raMbrF.getIdTiersBeneficiaire())) {

                    int groupLevelMbrFamille = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                            getIdsRaTiers(session, transaction, raMbrF.getIdTiersBeneficiaire()));

                    // Le membre de famille est de meme niveau que le
                    // tiersEnCours, et sont tous les deux de niveau 1 (rente
                    // principale)
                    // Dans ce cas, on ne met pas à jours le compte annexe,
                    // chacun garde son propre compte.
                    if ((groupLevelMbrFamille == groupLevelTEC) && (groupLevelMbrFamille == 1)) {
                        continue;
                    }
                    boolean isMbrFamillePlusVieux = false;
                    if (groupLevelMbrFamille == groupLevelTEC) {
                        PRTiersWrapper mbrFamille = PRTiersHelper.getTiersParId(session,
                                raMbrF.getIdTiersBeneficiaire());
                        JADate dnMF = new JADate(mbrFamille.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTEC);
                        JADate dnTEC = new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                        JACalendar cal = new JACalendarGregorian();
                        if (cal.compare(dnMF, dnTEC) == JACalendar.COMPARE_FIRSTLOWER) {
                            isMbrFamillePlusVieux = true;
                        }

                    }

                    // Le membre de famille est de niveau inférieur ou de même
                    // niveau, mais plus vieux...
                    if ((groupLevelMbrFamille < groupLevelTEC)
                            || ((groupLevelMbrFamille == groupLevelTEC) && isMbrFamillePlusVieux)) {

                        // Création du CA pour le TEC si non existant
                        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, icTEC,
                                IREValidationLevel.VALIDATION_LEVEL_ALL);
                        icTEC.retrieve(transaction);
                        icTEC.setIdTiersAdressePmt(idt);
                        icTEC.wantCallMethodBefore(false);
                        icTEC.update(transaction);

                        icMbrF.setIdTiersAdressePmt(idt);
                        icMbrF.wantCallMethodBefore(false);
                        icMbrF.update(transaction);

                    } else {
                        String idCptAnnexe = null;
                        if (icMbrF.getIdTiersAdressePmt().equals(idt)) {
                            idCptAnnexe = icMbrF.getIdCompteAnnexe();
                        } else {
                            // Création du CA pour le TEC si non existant
                            idCptAnnexe = REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, icTEC,
                                    IREValidationLevel.VALIDATION_LEVEL_ALL);
                        }
                        icTEC.retrieve(transaction);
                        icTEC.setIdCompteAnnexe(idCptAnnexe);
                        icTEC.setIdTiersAdressePmt(idt);
                        icTEC.update(transaction);
                    }
                }
            }

        }
    }

    private void doTraitementCasC(final BSession session, final BTransaction transaction, final String idt,
            final String idTEC, final REInformationsComptabilite icTEC, final ISFMembreFamilleRequerant[] membresFamille)
            throws Exception {
        icTEC.retrieve(transaction);
        icTEC.setIdTiersAdressePmt(idt);
        icTEC.update(transaction);

        int groupLevelTEC = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                getIdsRaTiers(session, transaction, idTEC));

        // Parcous de tous les membres de la famille
        for (int i = 0; i < membresFamille.length; i++) {
            ISFMembreFamilleRequerant mbr = membresFamille[i];
            // skip du requérant...
            if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mbr.getRelationAuRequerant())) {
                continue;
            }

            if (JadeStringUtil.isBlankOrZero(mbr.getIdTiers())) {
                continue;
            }

            // Récupération des infoCompta des membre de famille...
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
            // mgr.setForNoDemandeRente(demande.getIdDemandeRente());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            mgr.find(transaction);

            // Parcous de tous les membres référencés...
            for (int j = 0; j < mgr.size(); j++) {
                RERenteAccJoinTblTiersJoinDemandeRente raMbrF = (RERenteAccJoinTblTiersJoinDemandeRente) mgr
                        .getEntity(j);
                REInformationsComptabilite icMbrF = raMbrF.loadInformationsComptabilite();

                if (idt.equals(icMbrF.getIdTiersAdressePmt())) {

                    int groupLevelMbrFamille = REBeneficiairePrincipal.getGroupLevel(session, transaction,
                            getIdsRaTiers(session, transaction, raMbrF.getIdTiersBeneficiaire()));

                    boolean isMbrFamillePlusVieux = false;
                    if (groupLevelMbrFamille == groupLevelTEC) {
                        PRTiersWrapper mbrFamille = PRTiersHelper.getTiersParId(session,
                                raMbrF.getIdTiersBeneficiaire());
                        JADate dnMF = new JADate(mbrFamille.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTEC);
                        JADate dnTEC = new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                        JACalendar cal = new JACalendarGregorian();
                        if (cal.compare(dnMF, dnTEC) == JACalendar.COMPARE_FIRSTLOWER) {
                            isMbrFamillePlusVieux = true;
                        }

                    }

                    // Les deux mbr de famille sont de même niveau et ont les deux une rente principale
                    if ((groupLevelMbrFamille == groupLevelTEC) && (groupLevelTEC == 1)) {

                        // Création du CA pour le TEC si non existant
                        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, icTEC,
                                IREValidationLevel.VALIDATION_LEVEL_ALL);

                        icTEC.retrieve(transaction);
                        icTEC.setIdTiersAdressePmt(idt);
                        icTEC.update(transaction);
                    }

                    // Le membre de famille est de niveau inférieur ou de même
                    // niveau, mais plus vieux...
                    else if ((groupLevelMbrFamille < groupLevelTEC)
                            || ((groupLevelMbrFamille == groupLevelTEC) && isMbrFamillePlusVieux)) {

                        // Création du CA pour le TEC si non existant

                        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, icTEC,
                                IREValidationLevel.VALIDATION_LEVEL_ALL);

                        icTEC.retrieve(transaction);
                        icTEC.setIdTiersAdressePmt(idt);
                        icTEC.update(transaction);

                        icMbrF.setIdTiersAdressePmt(idt);
                        icMbrF.setIdCompteAnnexe(icTEC.getIdCompteAnnexe());
                        icMbrF.update(transaction);

                    }
                    // De niveau supérieur
                    else {
                        String idCptAnnexe = null;
                        idCptAnnexe = icMbrF.getIdCompteAnnexe();
                        icTEC.retrieve(transaction);
                        icTEC.setIdCompteAnnexe(idCptAnnexe);
                        icTEC.setIdTiersAdressePmt(idt);
                        icTEC.update(transaction);
                    }
                }
            }
        }
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
