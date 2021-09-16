/*
 * Créé le 25 juin 07
 */

package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
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
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.rentesaccordees.*;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REDebloquerMontantRenteAccordeeProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.REPostItsFilteringUtils;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
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
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;
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
import org.apache.commons.lang.StringUtils;

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
            renteAccordee.setQuotiteRente(renteAccordeeVb.getQuotiteRente());
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
            renteAccordee.setQuotiteRente(renteAccordeeVb.getQuotiteRente());
            renteAccordee.setIdBaseCalcul(renteAccordeeVb.getIdBaseCalcul());
            renteAccordee.setIdDemandePrincipaleAnnulante(renteAccordeeVb.getIdDemandePrincipaleAnnulante());
            renteAccordee.setReferencePmt(renteAccordeeVb.getReferencePmt());
            renteAccordee.setIdInfoCompta(renteAccordeeVb.getIdInfoCompta());

            // Mise à jour de l'info compta
            updateInfoComptaPourMembreFamillesRenteAccordee((BSession) session, transaction,
                    renteAccordee.getIdPrestationAccordee(), renteAccordeeVb.getIdTiersAdressePmtIC());

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

    /**
     * Méthode permettant de rechercher la demande de rente dont la rente accordée vient d'être calculée.
     *
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionAfficherCalcul(final FWViewBeanInterface viewBean, final FWAction action,
                                                    final BSession session) throws Exception {

        RECalculACORDemandeRenteViewBean raViewBean = (RECalculACORDemandeRenteViewBean) viewBean;
        RERenteAccordeeJointDemandeRenteViewBean rechercheViewBean = new RERenteAccordeeJointDemandeRenteViewBean();
        rechercheViewBean.setNoDemandeRente(raViewBean.getIdDemandeRente());

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(session);
        demandeRente.setIdDemandeRente(raViewBean.getIdDemandeRente());
        demandeRente.retrieve();
        if (!StringUtils.equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE,demandeRente.getCsEtat())) {
            REDemandeRenteJointDemandeManager managerDemande = new REDemandeRenteJointDemandeManager();
            managerDemande.setForCsEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
            managerDemande.setForIdTiersRequ(raViewBean.getIdTiers());
            managerDemande.find(BManager.SIZE_NOLIMIT);
            if (managerDemande.size() > 0) {
                REDemandeRenteJointDemande demandeRenteCalcule = (REDemandeRenteJointDemande) managerDemande.get(0);
                rechercheViewBean.setNoDemandeRente(demandeRenteCalcule.getId());
            }
        }

        return rechercheViewBean;
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
     * Le but de cette méthode est de permettre la mise à jour des informations comptables liées à une rente accordée.
     * Elle va progressivement contrôler les compte annexes pour chaque rentes liés à un rentier. Pour les rentes de
     * niveau 5 (enfants) plusieurs tests seront fait (détaillés dans les méthodes adequates) pour mettre à jour le
     * compte annexe d'une rente.
     * 
     * @throws Exception
     */
    private void updateInfoComptaPourMembreFamillesRenteAccordee(final BSession session,
            final BTransaction transaction, final String idRA, final String newIDAdressePaiement) throws Exception {

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
            // parcourt de toutes les rentes contenues dans cette liste et traitement
            for (RERenteAccJoinTblTiersJoinDemandeRente ra : mapSFSortedByGroupLevel.get(5)) {
                changementCASelonTiersAdressePmt(session, transaction, ra, membres, mapSFSortedByGroupLevel.get(5));
            }
        }
    }

    /***
     * Méthode qui retourne l'id du compte annexe pour un rentier selon son idTiers
     * 
     * @param bSession
     * @param idTiers
     * @return
     * @throws Exception
     */
    public String getIdCompteAnnexe(BSession bSession, String idTiers) throws Exception {

        String nss = getNSSTiers(idTiers, bSession);

        String idCompteAnnexe = "";

        CACompteAnnexeManager caManager = new CACompteAnnexeManager();
        caManager.setSession(bSession);
        caManager.setForIdRole(IntRole.ROLE_RENTIER);
        caManager.setForIdTiers(idTiers);
        caManager.setForIdExterneRole(nss);
        caManager.find(BManager.SIZE_NOLIMIT);

        if (!caManager.hasErrors()) {
            if (caManager.size() != 0) {
                CACompteAnnexe compte = (CACompteAnnexe) caManager.getFirstEntity();
                idCompteAnnexe = compte.getIdCompteAnnexe();
            }
        } else {
            String message = java.text.MessageFormat
                    .format(bSession.getLabel("GROUP_LEVEL_NOT_RECOGNISED"),
                            "Erreur dans RERenteAccordeeJointDemandeRenteHelper->getIdCompteAnnexe() => caManager.hasErrors()=true",
                            caManager.getErrors());
            throw new RETechnicalException(message);
        }

        return idCompteAnnexe;
    }

    private String getNSSTiers(String idTiers, BSession bSession) throws Exception {

        PRTiersWrapper tw = PRTiersHelper.getTiersParId(bSession, idTiers);
        if (tw == null || JadeStringUtil.isEmpty(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
            throw new Exception("Tiers not found, or NSS is blank. idTiers = " + idTiers);
        }
        return tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    }

    /***
     * Méthode qui permet de changer l'id du compte annexe des info comptable d'une rente accordée en fonction de son
     * idTiers de payement.
     * 
     * @param session
     * @param transaction
     * @param ra
     * @param membres
     * @throws Exception
     */
    private void changementCASelonTiersAdressePmt(final BSession session, final BTransaction transaction,
            RERenteAccJoinTblTiersJoinDemandeRente ra, ISFMembreFamilleRequerant[] membres,
            List<RERenteAccJoinTblTiersJoinDemandeRente> rentesLvl5) throws Exception {

        String idCAToChange = "";

        // Permet de définir si l'idTiers de l'adresse de payement fait parti d'un id tiers des membres de la famille
        String idTiersMembreFamilleSameAsIdTiersAdressePmt = getIdTiersSameAsTiersAdressePmt(ra, membres);

        // Permet de définir l'idTiers du membre de la famille le plus jeune
        String idTiersYoungestMembreFamilleSameIdTiersAdressePmt = getIdTiersYoungestWithSameAdressPmt(session, ra,
                rentesLvl5);

        // récupérer la base de calcul
        REBasesCalcul baseCalcul = new REBasesCalcul();
        baseCalcul.setSession(session);
        baseCalcul.setIdBasesCalcul(ra.getIdBaseCalcul());
        baseCalcul.retrieve(transaction);

        // Si l'id tiers de l'adresse de payement est égale à l'idTiers de la base de calcul
        if (ra.getIdTierAdressePmt().equals(baseCalcul.getIdTiersBaseCalcul())) {
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(baseCalcul.getIdTiersBaseCalcul());
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            // Ajout d'une clause OR pour contrôler l'état, nécessite setForMoisFinRANotEmptyAndHigherOrEgal
            mgr.setEtatCalculeForDateFinDroitNotEmpty(true);
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

            // on récupère le meilleur niveau de la map
            if (mapIdCASortedByGroupLevel.containsKey(bestLevel)) {
                idCAToChange = mapIdCASortedByGroupLevel.get(bestLevel);

                // Si on a pas d'idCompteAnnexe -> créer un compte annexe
                if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                    idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra, ra.getIdTierAdressePmt());
                }
            }
        }

        // Si TiersAdressePmt parmi les idTiersFamille
        else if (!JadeStringUtil.isBlankOrZero(idTiersMembreFamilleSameAsIdTiersAdressePmt)) {
            idCAToChange = getIdCompteAnnexe(session, idTiersMembreFamilleSameAsIdTiersAdressePmt);

            // Si on a pas d'idCompteAnnexe -> créer un compte annexe
            if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra,
                        idTiersMembreFamilleSameAsIdTiersAdressePmt);
            }
        }
        // Si il y a un plus jeune dans la famille
        else if (!JadeStringUtil.isBlankOrZero(idTiersYoungestMembreFamilleSameIdTiersAdressePmt)) {
            idCAToChange = getIdCompteAnnexe(session, idTiersYoungestMembreFamilleSameIdTiersAdressePmt);

            // Si on a pas d'idCompteAnnexe -> créer un compte annexe
            if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra,
                        idTiersYoungestMembreFamilleSameIdTiersAdressePmt);
            }
        }
        // Enfin si aucun cas ne correspond on récupère l'id du CA de la personne a qui la rente est accordée
        else {
            idCAToChange = getIdCompteAnnexe(session, ra.getIdTiersBeneficiaire());

            // Si on a pas d'idCompteAnnexe -> créer un compte annexe
            if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra,
                        idTiersYoungestMembreFamilleSameIdTiersAdressePmt);
            }
        }

        // On charge les info comptables de la rente accordée du tiers membre famille en cours
        REInformationsComptabilite infoComptaRenteMembreFamille = ra.loadInformationsComptabilite();
        if (!infoComptaRenteMembreFamille.getIdCompteAnnexe().equals(idCAToChange)) {
            infoComptaRenteMembreFamille.setIdCompteAnnexe(idCAToChange);
            infoComptaRenteMembreFamille.setSession(session);
            infoComptaRenteMembreFamille.update(transaction);
        }
    }

    /**
     * Créer un compte annexe pour le tiers passé en argument et retourne l'id du compte
     * 
     * @param session
     * @param transaction
     * @param ra
     * @param idTiers
     * @return
     * @throws Exception
     */
    private String createCompteAnnexeAndReturnId(final BSession session, final BTransaction transaction,
            RERenteAccJoinTblTiersJoinDemandeRente ra, String idTiers) throws Exception {
        String idCAToChange;
        idCAToChange = REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTiers,
                ra.loadInformationsComptabilite(), IREValidationLevel.VALIDATION_LEVEL_ALL);
        return idCAToChange;
    }

    /***
     * Méthode qui retourne l'idTiers du plus jeune parmi les rentes à avoir la même adresse de payement
     * 
     * @param session
     * @param ra
     * @param rentesEnfants
     * @return
     * @throws Exception
     */
    private String getIdTiersYoungestWithSameAdressPmt(final BSession session,
            RERenteAccJoinTblTiersJoinDemandeRente ra, List<RERenteAccJoinTblTiersJoinDemandeRente> rentesEnfants)
            throws Exception {
        String idTiersYoungestMembreFamilleSameIdTiersAdressePmt = "";
        if (!rentesEnfants.isEmpty()) {
            Date tempDateNaissance = new Date("01.01.1900");
            RERenteAccJoinTblTiersJoinDemandeRente tempRefRente = null;
            RERenteAccJoinTblTiersJoinDemandeRente tempRefRenteAvecDateDeFinDroit = null;
            // itérer sur toutes les rentes :
            for (int i = 0; i < rentesEnfants.size(); i++) {
                // Si adresse payement est égal à un enfant, si elle est égal à elle même c'est plus un soucis, on
                // récupère le plus jeune -> lui même
                if (ra.getIdTierAdressePmt().equals(rentesEnfants.get(i).getIdTierAdressePmt())) {

                    // Récupérer le tiers rentier
                    PRTiersWrapper tiersFamille = PRTiersHelper.getTiersParId(session, rentesEnfants.get(i)
                            .getIdTiersBeneficiaire());
                    // On prend le tiers le plus jeune
                    Date dateMbrfm = new Date(tiersFamille.getDateNaissance());

                    if (dateMbrfm.compareTo(tempDateNaissance) > 0) {
                        tempDateNaissance = dateMbrfm;

                        // Si la rente enfant trouvée n'as pas de date de fin
                        if (rentesEnfants.get(i).getDateFinDroit().isEmpty()) {
                            tempRefRente = rentesEnfants.get(i);
                        } else {
                            tempRefRenteAvecDateDeFinDroit = rentesEnfants.get(i);
                        }
                    }
                }
            }
            if (tempRefRente != null) {
                idTiersYoungestMembreFamilleSameIdTiersAdressePmt = tempRefRente.getIdTiersBeneficiaire();
            }
            // Si la rente de ref est nulle ça veut dire toutes les rentes enfants ont une date de fin, on prend donc le
            // plus jeune avec une date de fin quand même
            else if (tempRefRenteAvecDateDeFinDroit != null) {
                idTiersYoungestMembreFamilleSameIdTiersAdressePmt = tempRefRenteAvecDateDeFinDroit
                        .getIdTiersBeneficiaire();
            }
        }
        return idTiersYoungestMembreFamilleSameIdTiersAdressePmt;
    }

    /***
     * Méthode qui retourne l'idTiers du membre à qui correspond l'idTiers de l'adresse de payement de la rente accordée
     * 
     * @param ra
     * @param membres
     * @return
     */
    private String getIdTiersSameAsTiersAdressePmt(RERenteAccJoinTblTiersJoinDemandeRente ra,
            ISFMembreFamilleRequerant[] membres) {
        String idTiersMembreFamilleSameAsIdTiersAdressePmt = "";
        for (int i = 0; i < membres.length; i++) {
            if (ra.getIdTiersAdressePmt().equals(membres[i].getIdTiers())) {
                idTiersMembreFamilleSameAsIdTiersAdressePmt = membres[i].getIdTiers();
            }
        }
        return idTiersMembreFamilleSameAsIdTiersAdressePmt;
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
            // Pour chaque membre famille, on récupère toutes les rentes en cours, sauf pour un "conjoint inconnu"
            // (idTiers = 0)
            if (!"0".equals(mbr.getIdTiers())) {
                RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
                mgr.setSession(session);
                mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
                mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
                // Ajout d'une clause OR pour contrôler l'état, nécessite setForMoisFinRANotEmptyAndHigherOrEgal
                mgr.setEtatCalculeForDateFinDroitNotEmpty(true);
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
        mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
        // Ajout d'une clause OR pour contrôler l'état, nécessite setForMoisFinRANotEmptyAndHigherOrEgal
        mgr.setEtatCalculeForDateFinDroitNotEmpty(true);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);
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
