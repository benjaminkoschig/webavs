/*
 * Cr�� le 25 juin 07
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
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
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
import globaz.pyxis.db.tiers.TIReferencePaiementManager;
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

        // Cr�ation de la transaction
        BSession session1 = (BSession) session;

        BTransaction transaction = null;
        transaction = (BTransaction) (session1).newTransaction();

        try {
            transaction.openTransaction();

            // r�cup�ration du viewBean et ajout de la rente accord�e
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
            renteAccordee.setIdReferenceQR(renteAccordeeVb.getIdReferenceQR());

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

            // mise � jour de la p�riode de la demande
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
     * oca Surcharge pour permettre de rechercher les notes (FWNOTEP) avec une requ�te s�par�e (plus efficace que la
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
     * @param viewBean DOCUMENT ME!
     * @param action   DOCUMENT ME!
     * @param session  DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
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

        // Cr�ation de la transaction

        BSession session1 = (BSession) session;

        BTransaction transaction = null;
        transaction = (BTransaction) (session1).newTransaction();

        boolean updateCodeCasSpeciauxAnnonce = false;
        String idRenteCalculee = "";

        try {
            transaction.openTransaction();

            // r�cup�ration du viewBean et retrieve de la rente accord�e, puis
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
            renteAccordee.setIdReferenceQR(renteAccordeeVb.getIdReferenceQR());
            renteAccordee.setIdInfoCompta(renteAccordeeVb.getIdInfoCompta());

            // Mise � jour de l'info compta
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
        // mise � jour de la p�riode de la demande
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
     * @param viewBean DOCUMENT ME!
     * @param action   DOCUMENT ME!
     * @param session  DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
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
     * Affiche la page de d�blocage du montant de la rente accodr�e
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
     * M�thode permettant de rechercher la demande de rente dont la rente accord�e vient d'�tre calcul�e.
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
        if (!StringUtils.equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE, demandeRente.getCsEtat())) {
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

        // r�cup�ration des ID Tiers des membres de la famille (�tendue)
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
     * Le but de cette m�thode est de permettre la mise � jour des informations comptables li�es � une rente accord�e.
     * Elle va progressivement contr�ler les compte annexes pour chaque rentes li�s � un rentier. Pour les rentes de
     * niveau 5 (enfants) plusieurs tests seront fait (d�taill�s dans les m�thodes adequates) pour mettre � jour le
     * compte annexe d'une rente.
     *
     * @throws Exception
     */
    private void updateInfoComptaPourMembreFamillesRenteAccordee(final BSession session,
                                                                 final BTransaction transaction, final String idRA, final String newIDAdressePaiement) throws Exception {

        // R�cup�rer la rente accord�e du tiers b�n�ficiaire
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRA);
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);

        // R�cup�rer les informations comptables du tiers en cours (TEC)
        REInformationsComptabilite infoComptaTEC = ra.loadInformationsComptabilite();
        String idTEC = ra.getIdTiersBeneficiaire();

        REDemandeRente demande = getDemandeRente(session, transaction, ra.getIdPrestationAccordee());

        // R�cup�rer l'id du tiers requ�rant de la demande
        String idTiersRequerant = demande.loadDemandePrestation(transaction).getIdTiers();

        // R�cup�ration des membres de la famille du tiers requ�rant
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        // R�cup�ration des membres de la famille du requ�rant
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
     * Cette m�thode permet de v�rifier dans quel Group level on se trouve et d'appeler le bon traitement en fonction de
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
                    new Object[]{groupLevelTEC, idRA});
            throw new RETechnicalException(message);
        }
    }

    /**
     * M�thode de traitement de groupe de niveau 1 dans le rentes.
     * Cette m�thode effectue le travail suivant -> mise � jour de l'adresse de paiement niveau 1 (+ �ventuellement le
     * CA)
     * V�rification des rentes li�es -> Si niveau 5 dans la situation familiale ALORS v�rification pour mise � jour du
     * CA
     *
     * @param session
     * @param transaction
     * @param icTEC                : info comptable du tiers en cours
     * @param idTEC                : identifiant unique du tiers b�n�ficiaire en cours
     * @param newIDAdressePaiement : nouvel id adresse paiement
     * @param membres              : membres de la situation familiale li�s au tiers requ�rant
     * @throws Exception
     */
    private void treatGroupLevels(final BSession session, final BTransaction transaction,
                                  REInformationsComptabilite infoComptaTEC, String idTEC, String newIDAdressePaiement,
                                  ISFMembreFamilleRequerant[] membres) throws Exception {

        updateAdressePaiementForTEC(session, transaction, infoComptaTEC, idTEC, newIDAdressePaiement);
        /**
         *
         * 2. V�rifier les niveaux inf�rieurs de la situation familiale
         *
         */
        // Pr�paration d'une MAP tri�e par groupe level
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
     * M�thode qui retourne l'id du compte annexe pour un rentier selon son idTiers
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
     * M�thode qui permet de changer l'id du compte annexe des info comptable d'une rente accord�e en fonction de son
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

        // Permet de d�finir si l'idTiers de l'adresse de payement fait parti d'un id tiers des membres de la famille
        String idTiersMembreFamilleSameAsIdTiersAdressePmt = getIdTiersSameAsTiersAdressePmt(ra, membres);

        // Permet de d�finir l'idTiers du membre de la famille le plus jeune
        String idTiersYoungestMembreFamilleSameIdTiersAdressePmt = getIdTiersYoungestWithSameAdressPmt(session, ra,
                rentesLvl5);

        // r�cup�rer la base de calcul
        REBasesCalcul baseCalcul = new REBasesCalcul();
        baseCalcul.setSession(session);
        baseCalcul.setIdBasesCalcul(ra.getIdBaseCalcul());
        baseCalcul.retrieve(transaction);

        // Si l'id tiers de l'adresse de payement est �gale � l'idTiers de la base de calcul
        if (ra.getIdTierAdressePmt().equals(baseCalcul.getIdTiersBaseCalcul())) {
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(baseCalcul.getIdTiersBaseCalcul());
            mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
            // Ajout d'une clause OR pour contr�ler l'�tat, n�cessite setForMoisFinRANotEmptyAndHigherOrEgal
            mgr.setEtatCalculeForDateFinDroitNotEmpty(true);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            Map<Integer, String> mapIdCASortedByGroupLevel = new HashMap<Integer, String>();

            // pour chaque rente on v�rifie le level. On prend la r�f�rence compte annexe de la rente de
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

            // on r�cup�re le meilleur niveau de la map
            if (mapIdCASortedByGroupLevel.containsKey(bestLevel)) {
                idCAToChange = mapIdCASortedByGroupLevel.get(bestLevel);

                // Si on a pas d'idCompteAnnexe -> cr�er un compte annexe
                if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                    idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra, ra.getIdTierAdressePmt());
                }
            }
        }

        // Si TiersAdressePmt parmi les idTiersFamille
        else if (!JadeStringUtil.isBlankOrZero(idTiersMembreFamilleSameAsIdTiersAdressePmt)) {
            idCAToChange = getIdCompteAnnexe(session, idTiersMembreFamilleSameAsIdTiersAdressePmt);

            // Si on a pas d'idCompteAnnexe -> cr�er un compte annexe
            if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra,
                        idTiersMembreFamilleSameAsIdTiersAdressePmt);
            }
        }
        // Si il y a un plus jeune dans la famille
        else if (!JadeStringUtil.isBlankOrZero(idTiersYoungestMembreFamilleSameIdTiersAdressePmt)) {
            idCAToChange = getIdCompteAnnexe(session, idTiersYoungestMembreFamilleSameIdTiersAdressePmt);

            // Si on a pas d'idCompteAnnexe -> cr�er un compte annexe
            if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra,
                        idTiersYoungestMembreFamilleSameIdTiersAdressePmt);
            }
        }
        // Enfin si aucun cas ne correspond on r�cup�re l'id du CA de la personne a qui la rente est accord�e
        else {
            idCAToChange = getIdCompteAnnexe(session, ra.getIdTiersBeneficiaire());

            // Si on a pas d'idCompteAnnexe -> cr�er un compte annexe
            if (JadeStringUtil.isBlankOrZero(idCAToChange)) {
                idCAToChange = createCompteAnnexeAndReturnId(session, transaction, ra,
                        idTiersYoungestMembreFamilleSameIdTiersAdressePmt);
            }
        }

        // On charge les info comptables de la rente accord�e du tiers membre famille en cours
        REInformationsComptabilite infoComptaRenteMembreFamille = ra.loadInformationsComptabilite();
        if (!infoComptaRenteMembreFamille.getIdCompteAnnexe().equals(idCAToChange)) {
            infoComptaRenteMembreFamille.setIdCompteAnnexe(idCAToChange);
            infoComptaRenteMembreFamille.setSession(session);
            infoComptaRenteMembreFamille.update(transaction);
        }
    }

    /**
     * Cr�er un compte annexe pour le tiers pass� en argument et retourne l'id du compte
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
     * M�thode qui retourne l'idTiers du plus jeune parmi les rentes � avoir la m�me adresse de payement
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
            // it�rer sur toutes les rentes :
            for (int i = 0; i < rentesEnfants.size(); i++) {
                // Si adresse payement est �gal � un enfant, si elle est �gal � elle m�me c'est plus un soucis, on
                // r�cup�re le plus jeune -> lui m�me
                if (ra.getIdTierAdressePmt().equals(rentesEnfants.get(i).getIdTierAdressePmt())) {

                    // R�cup�rer le tiers rentier
                    PRTiersWrapper tiersFamille = PRTiersHelper.getTiersParId(session, rentesEnfants.get(i)
                            .getIdTiersBeneficiaire());
                    // On prend le tiers le plus jeune
                    Date dateMbrfm = new Date(tiersFamille.getDateNaissance());

                    if (dateMbrfm.compareTo(tempDateNaissance) > 0) {
                        tempDateNaissance = dateMbrfm;

                        // Si la rente enfant trouv�e n'as pas de date de fin
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
            // Si la rente de ref est nulle �a veut dire toutes les rentes enfants ont une date de fin, on prend donc le
            // plus jeune avec une date de fin quand m�me
            else if (tempRefRenteAvecDateDeFinDroit != null) {
                idTiersYoungestMembreFamilleSameIdTiersAdressePmt = tempRefRenteAvecDateDeFinDroit
                        .getIdTiersBeneficiaire();
            }
        }
        return idTiersYoungestMembreFamilleSameIdTiersAdressePmt;
    }

    /***
     * M�thode qui retourne l'idTiers du membre � qui correspond l'idTiers de l'adresse de payement de la rente accord�e
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
         * 1. Mettre � jour l'adresse de paiement du tiers en cours (TEC) + CA
         */
        infoComptaTEC.setIdTiersAdressePmt(newIDAdressePaiement);
        // v�rifier si le compte annexe est identique au CA li� au NSS du tiers courant -> sinon, mise � jour:
        // Cr�ation du CA pour le TEC si non existant -> l'update de l'info compta est d�l�gu� � cette m�thode.
        REInfoCompta.initCompteAnnexe_noCommit(session, transaction, idTEC, infoComptaTEC,
                IREValidationLevel.VALIDATION_LEVEL_ALL);
    }

    /**
     * Cette m�thode permet de cr�er une structure de donn�es mapp�e sur les niveaux de rentes.
     * Chaque rente se trouve dans un certain niveau. On d�termine pour chaque rente le niveau et on cr�er une map en
     * fonction du niveau :
     * <p>
     * EX: nom objet = rente1 niveau = 1 -> ajout de l'objet � la liste listLevel1.add(rente1) -> ajout de la liste � la
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

        // Instantiation d'une map afin de m�moriser les diff�rents cas de situation familiale par groupe
        Map<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>> mapSFByGroupLevel = new HashMap<Integer, List<RERenteAccJoinTblTiersJoinDemandeRente>>();
        // On pr�pare une nouvelle liste par groupe level
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel1 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel2 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel4 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        List<RERenteAccJoinTblTiersJoinDemandeRente> listRAGroupLevel5 = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        // Ajout des niveaux accept�s et des listes associ�s
        mapSFByGroupLevel.put(1, listRAGroupLevel1);
        mapSFByGroupLevel.put(2, listRAGroupLevel2);
        mapSFByGroupLevel.put(4, listRAGroupLevel4);
        mapSFByGroupLevel.put(5, listRAGroupLevel5);

        // Marqueur permettant de sp�cifier si un groupe level 5 est existant
        boolean hasGroupLevel5 = false;
        // On parcourt tous les membres de la famille et on r�cup�re la liste des rentes de chaque membre.
        for (int i = 0; i < membres.length; i++) {
            ISFMembreFamilleRequerant mbr = membres[i];
            // Pour chaque membre famille, on r�cup�re toutes les rentes en cours, sauf pour un "conjoint inconnu"
            // (idTiers = 0)
            if (!"0".equals(mbr.getIdTiers())) {
                RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
                mgr.setSession(session);
                mgr.setForIdTiersBeneficiaire(mbr.getIdTiers());
                mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
                // Ajout d'une clause OR pour contr�ler l'�tat, n�cessite setForMoisFinRANotEmptyAndHigherOrEgal
                mgr.setEtatCalculeForDateFinDroitNotEmpty(true);
                mgr.find(transaction, BManager.SIZE_NOLIMIT);

                // On parcourt chaque rente trouv�e afin d'en d�terminer le groupe level
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
            // Si on n'a pas de groupe level 5, on supprime l'entr�e dans la map car un test est fait plus loin sur
            // cette entr�e
            mapSFByGroupLevel.remove(5);
        }

        return mapSFByGroupLevel;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
                                          final BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private REDemandeRente getDemandeRente(final BSession session, final BTransaction transaction, final String idRA)
            throws Exception {
        // R�cup�ration de la rente accord�e
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRA);
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);

        // Recherche du tiers requ�rant de la demande

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

        // R�cup�ration des infoCompta des membre de famille...
        RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
        mgr.setSession(session);
        mgr.setForIdTiersBeneficiaire(idTiers);
        mgr.setForMoisFinRANotEmptyAndHigherOrEgal(REPmtMensuel.getDateDernierPmt(session));
        // Ajout d'une clause OR pour contr�ler l'�tat, n�cessite setForMoisFinRANotEmptyAndHigherOrEgal
        mgr.setEtatCalculeForDateFinDroitNotEmpty(true);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) mgr.getEntity(i);
            result.add(Long.parseLong(elm.getIdPrestationAccordee()));
        }
        return result;
    }

    /**
     * M�thode qui retourne le libell� de la nationalit� par rapport au csNationalit� qui est dans le vb
     *
     * @return le libell� du pays (retourne une cha�ne vide si pays inconnu)
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
     * @param session    DOCUMENT ME!
     * @param raViewBean
     * @throws Exception
     */
    private void rechargerAdressePaiement(final BSession session,
                                          final RERenteAccordeeJointDemandeRenteViewBean raViewBean) throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse
        // paiement
        if (raViewBean.isTiersBeneficiaireChange()) {
            raViewBean.setIdTiersAdressePmtIC(raViewBean.getIdTiersAdressePmtICDepuisPyxis());
            raViewBean.setIdReferenceQR(raViewBean.getIdReferenceQRDepuisPyxis());
        }

        if (raViewBean.isRetourReferenceQrDepuisPyxis()) {
            raViewBean.setIdReferenceQR(raViewBean.getIdReferenceQRDepuisPyxis());
            raViewBean.setRetourReferenceQrDepuisPyxis(false);
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

            if (TIAdressePaiement.isQRIban(adresse.getCompte())) {
                raViewBean.setReferenceQRFormattee(TIReferencePaiementManager.getReferencePaiementPourAffichage(session, raViewBean.getIdReferenceQR()));
            } else {
                raViewBean.setReferenceQRFormattee("");
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
