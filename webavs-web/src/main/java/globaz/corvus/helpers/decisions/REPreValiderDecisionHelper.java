package globaz.corvus.helpers.decisions;

import globaz.babel.db.copies.CTCopies;
import globaz.babel.db.copies.CTCopiesManager;
import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.creances.IRECreancier;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.decisions.IREPreparationDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.creances.RECreancierManager;
import globaz.corvus.db.decisions.REAnnexeDecision;
import globaz.corvus.db.decisions.REAnnexeDecisionManager;
import globaz.corvus.db.decisions.RECopieDecision;
import globaz.corvus.db.decisions.RECopieDecisionManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.db.rentesverseesatort.RERenteVerseeATortJointRenteAccordee;
import globaz.corvus.db.rentesverseesatort.RERenteVerseeATortJointRenteAccordeeManager;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortConverter;
import globaz.corvus.db.rentesverseesatort.wrapper.RERenteVerseeATortWrapper;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REImprimerDecisionProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipalVO;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.corvus.utils.decisions.REDecisionsUtil;
import globaz.corvus.vb.creances.RECreancierListViewBean;
import globaz.corvus.vb.creances.RECreancierViewBean;
import globaz.corvus.vb.decisions.REAnnexeDecisionViewBean;
import globaz.corvus.vb.decisions.RECopieDecisionViewBean;
import globaz.corvus.vb.decisions.REDecisionInfoContainer;
import globaz.corvus.vb.decisions.REDecisionsContainer;
import globaz.corvus.vb.decisions.REPreValiderDecisionViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APISection;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationAdresse;
import globaz.pyxis.db.tiers.TIAdministrationAdresseManager;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.decisions.DecisionService;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.PrestationDue;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;
import ch.globaz.corvus.domaine.constantes.TypeTraitementDecisionRente;
import ch.globaz.corvus.utils.rentesverseesatort.RECalculRentesVerseesATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * @author SCR
 */
public class REPreValiderDecisionHelper extends PRHybridHelper {

    class OrdreVersementWrapper {
        public String csType = "";
        public String idCreanceAccordee = "";
        public String idDomaine = "";
        public String idTiers = "";
        public String idTiersAdrPmt = "";
        public FWCurrency montant = new FWCurrency(0);
    }

    class PrestationWrapper {
        public Collection<Long> idsRATraitee = new ArrayList<Long>();
        public FWCurrency montantPrestation = new FWCurrency();

        // Map des ordres de versements.
        // Les OV sont groupés par bénéficiaire et par type d'OV
        private final Map<String, OrdreVersementWrapper> ordresVersement = new HashMap<String, REPreValiderDecisionHelper.OrdreVersementWrapper>();

        public void addOV(final OrdreVersementWrapper ovw) {
            String key = ovw.idTiers + "-" + ovw.idTiersAdrPmt + "-" + ovw.idDomaine + "-" + ovw.csType;

            if (ordresVersement.containsKey(key)) {
                OrdreVersementWrapper lOVW = ordresVersement.get(key);
                lOVW.montant.add(ovw.montant);
                ordresVersement.put(key, lOVW);
            } else {
                ordresVersement.put(key, ovw);
            }
        }

        public OrdreVersementWrapper getOV(final String key) {
            if (ordresVersement.containsKey(key)) {
                return ordresVersement.get(key);
            } else {
                return null;
            }

        }

        public Set<String> ordresVersementKeys() {
            return ordresVersement.keySet();
        }
    }

    class RenteAccordeeADiminuer {
        public String codeMutation = "";
        public String dateFinDroit = "";
        public String fractionRente = "";
        public String genreRente = "";
        public String idRA = "";
        public String idTiersBeneficiaire = "";
        public boolean isCompenser = false;
        public boolean isDiminuer = false;
    }

    /**
     * Retourne la période rétroactive de la demande en fonction du type de décision
     */
    static Periode retrievePeriodeRetro(final DemandeRente demande, final String borneMaxSuperieure) throws Exception {

        Periode periodePrestationsAccordees = demande.getPeriodeDuDroitDesRentesAccordees();
        Periode periodeBorneMax = new Periode("", borneMaxSuperieure);

        return periodePrestationsAccordees.intersectionMois(periodeBorneMax);
    }

    private final REDecisionsUtil decisionUtil = new REDecisionsUtil();

    /**
     * Lors de la validation de la décision, il peut y avoir des retenues sur certaines rentes accordées, lorsqu'il y a
     * des retenues, on affiche un message non bloquant sur à l'arrivée sur le nouvel écran PRE2002
     */
    private String messageRetenueSurRente;

    public FWViewBeanInterface actionDecisionPrecedente(final FWViewBeanInterface vb, final FWAction action,
            final BSession session) throws Exception {

        REPreValiderDecisionViewBean viewBean = (REPreValiderDecisionViewBean) vb;

        viewBean.getDecisionContainer().decisionPrecedente();

        // mise a jour du bénéficiaire principal
        String idDecision = viewBean.getDecisionContainer().getDecisionIC().getIdDecision();
        REDecisionEntity dec = new REDecisionEntity();
        dec.setSession(session);
        dec.setIdDecision(idDecision);
        dec.retrieve();

        viewBean.setIdTiersBeneficiairePrincipal(dec.getIdTiersBeneficiairePrincipal());
        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, dec.getIdTiersBeneficiairePrincipal());
        if (tw != null) {
            viewBean.setTiersBeneficiairePrincipalInfo(tw.getDescription(session));
        }

        viewBean.setIdTierAdresseCourrier(dec.getIdTiersAdrCourrier());
        viewBean.setIdDecision(dec.getIdDecision());

        // on vide le pdf du vb
        viewBean.setDocumentsPreview(null);

        return viewBean;

    }

    public FWViewBeanInterface actionDecisionSuivante(final FWViewBeanInterface vb, final FWAction action,
            final BSession session) throws Exception {

        REPreValiderDecisionViewBean viewBean = (REPreValiderDecisionViewBean) vb;

        viewBean.getDecisionContainer().decisionSuivante();

        // mise a jour du bénéficiaire principal
        String idDecision = viewBean.getDecisionContainer().getDecisionIC().getIdDecision();
        REDecisionEntity dec = new REDecisionEntity();
        dec.setSession(session);
        dec.setIdDecision(idDecision);
        dec.retrieve();

        viewBean.setIdTiersBeneficiairePrincipal(dec.getIdTiersBeneficiairePrincipal());
        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, dec.getIdTiersBeneficiairePrincipal());
        if (tw != null) {
            viewBean.setTiersBeneficiairePrincipalInfo(tw.getDescription(session));
        }

        viewBean.setIdDecision(dec.getIdDecision());
        viewBean.setIdTierAdresseCourrier(dec.getIdTiersAdrCourrier());

        // on vide le pdf du vb
        viewBean.setDocumentsPreview(null);

        return viewBean;
    }

    public FWViewBeanInterface actionPrevalider(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

            decisionUtil.enregistrerModifs(vb, action, session, transaction);

            REDecisionsContainer decisionContainer = vb.getDecisionContainer();
            globaz.corvus.vb.decisions.REDecisionInfoContainer decision = null;

            if (decisionContainer != null) {
                decision = decisionContainer.getDecisionIC();
            }

            REDecisionEntity decisionAValider = new REDecisionEntity();

            decisionAValider.setSession(session);
            decisionAValider.setIdDecision(decision.getIdDecision());
            decisionAValider.retrieve(transaction);

            if (decisionAValider.isNew()) {
                throw new Exception("Decision supposed to be already existent, but not found in DB. idDecision = "
                        + decision.getIdDecision());
            }

            decisionAValider.setCsEtat(IREDecision.CS_ETAT_PREVALIDE);
            decisionAValider.setIsRemInteretMoratoires(vb.getIsInteretMoratoire());
            decisionAValider.update(transaction);

            Set<Long> keys = null;

            if ((decisionContainer != null) && (decision != null)) {
                keys = decision.getIdsPrstDuesParIdsRA().keySet();
            }

            if (keys != null) {
                for (Long idRA : keys) {
                    // Essai de récupération de la la RA.
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(session);
                    ra.setIdPrestationAccordee(idRA.toString());
                    ra.retrieve(transaction);

                    PRAssert.notIsNew(ra, null);
                    // maj de la remarque de la rente accordée
                }
            }

            if (vb.getDecisionContainer().hasDecisionSuivante()) {
                // mise a jour du bénéficiaire principal
                String idDecision = vb.getDecisionContainer().getDecisionIC().getIdDecision();
                REDecisionEntity dec = new REDecisionEntity();
                dec.setSession(session);
                dec.setIdDecision(idDecision);
                dec.retrieve(transaction);

                vb.setIdTiersBeneficiairePrincipal(dec.getIdTiersBeneficiairePrincipal());
                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, dec.getIdTiersBeneficiairePrincipal());
                if (tw != null) {
                    vb.setTiersBeneficiairePrincipalInfo(tw.getDescription(session));
                }
            }

            if (!transaction.hasErrors()) {
                transaction.commit();
            }

            // Lancer l'impression de la décision
            REImprimerDecisionProcess imprimerDecision = new REImprimerDecisionProcess();
            imprimerDecision.setSession(session);
            imprimerDecision.setIdDecision(decisionAValider.getIdDecision());
            imprimerDecision.setIdDemandeRente(decisionAValider.getIdDemandeRente());
            imprimerDecision.setDateDocument(decisionAValider.getDateDecision());
            imprimerDecision.setEMailAddress(decisionAValider.getAdresseEMail());

            BProcessLauncher.start(imprimerDecision, false);

            return viewBean;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
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
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

    private void addDetteRenteEnCours(final BSession session, final BITransaction transaction,
            final REDecisionEntity decisionEntity, final String idTiersRequerant, final REPrestations prestation,
            final Collection<Long> idsRA, final Collection<REOrdresVersements> ovsCrees) throws Exception {

        messageRetenueSurRente = "";

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);
        ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTiersRequerant);

        for (ISFMembreFamilleRequerant membre : mf) {
            if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(membre.getRelationAuRequerant())
                    || ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(membre.getRelationAuRequerant())
                    || ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(membre.getRelationAuRequerant())) {

                if (JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
                    // On passe au membre suivant, s'il le membre de la famille n'existe pas dans les tiers.
                    // car si n'existe pas dans les tiers, il ne peut pas avoir de rente en cours.
                    continue;
                }

                // chargement des rentes versées à tort de la demande depuis la base de données
                RERenteVerseeATortJointRenteAccordeeManager renteVerseeATortJointRenteAccordeeManager = new RERenteVerseeATortJointRenteAccordeeManager();
                renteVerseeATortJointRenteAccordeeManager.setSession(session);
                renteVerseeATortJointRenteAccordeeManager.setForIdDemandeRente(Long.parseLong(decisionEntity
                        .getIdDemandeRente()));
                renteVerseeATortJointRenteAccordeeManager.setForIdTiers(Long.parseLong(membre.getIdTiers()));
                renteVerseeATortJointRenteAccordeeManager.setForIdsRentesNouveauDroitIn(idsRA);
                renteVerseeATortJointRenteAccordeeManager.setSansLesSaisiesManuelles(true);
                renteVerseeATortJointRenteAccordeeManager.find(BManager.SIZE_NOLIMIT);

                // regroupement des résultats de la requête par tiers et par rente versée à tort
                class IdRenteVerseeATortIdTiers {
                    public Long idRenteAncienDroit;
                    public Long idRenteVerseeATort;
                    public Long idTiers;
                    public BigDecimal montant;

                    @Override
                    public boolean equals(final Object obj) {
                        if (obj instanceof IdRenteVerseeATortIdTiers) {
                            IdRenteVerseeATortIdTiers uneAutreCle = (IdRenteVerseeATortIdTiers) obj;
                            return idTiers.equals(uneAutreCle.idTiers)
                                    && idRenteVerseeATort.equals(uneAutreCle.idRenteVerseeATort);
                        }
                        return false;
                    }

                    @Override
                    public int hashCode() {
                        StringBuilder hashCodeBuilder = new StringBuilder();
                        hashCodeBuilder.append(this.getClass().getName()).append("(").append(idRenteVerseeATort)
                                .append(" - ").append(idTiers).append(")");
                        return hashCodeBuilder.toString().hashCode();
                    }
                }
                Map<IdRenteVerseeATortIdTiers, Collection<RERenteVerseeATortJointRenteAccordee>> rentesVerseesATortParIdEtParTiers = new HashMap<IdRenteVerseeATortIdTiers, Collection<RERenteVerseeATortJointRenteAccordee>>();
                for (RERenteVerseeATortJointRenteAccordee uneRenteVerseeATort : renteVerseeATortJointRenteAccordeeManager
                        .getContainerAsList()) {

                    IdRenteVerseeATortIdTiers cle = new IdRenteVerseeATortIdTiers();
                    cle.idTiers = uneRenteVerseeATort.getIdTiersBeneficiaire();
                    cle.idRenteVerseeATort = uneRenteVerseeATort.getIdRenteVerseeATort();
                    cle.idRenteAncienDroit = uneRenteVerseeATort.getIdRenteAncienDroit();
                    cle.montant = uneRenteVerseeATort.getMontant();

                    if (rentesVerseesATortParIdEtParTiers.containsKey(cle)) {
                        rentesVerseesATortParIdEtParTiers.get(cle).add(uneRenteVerseeATort);
                    } else {
                        rentesVerseesATortParIdEtParTiers.put(cle, new ArrayList<RERenteVerseeATortJointRenteAccordee>(
                                Arrays.asList(uneRenteVerseeATort)));
                    }
                }

                // transformation des résultats de la requête en entité utilisable pour le calcul du montant total
                // versé à tort
                for (Entry<IdRenteVerseeATortIdTiers, Collection<RERenteVerseeATortJointRenteAccordee>> uneRenteVerseeATortPourUnTiers : rentesVerseesATortParIdEtParTiers
                        .entrySet()) {
                    RERenteVerseeATortWrapper renteVerseeATortWrapper = RECalculRentesVerseesATortConverter
                            .convertRenteVerseeATortEntityToWrapper(uneRenteVerseeATortPourUnTiers.getValue());
                    REDetailCalculRenteVerseeATort detailCalcul = RECalculRentesVerseesATort
                            .chargerDetailRenteVerseeATort(renteVerseeATortWrapper);

                    IdRenteVerseeATortIdTiers cle = uneRenteVerseeATortPourUnTiers.getKey();

                    REOrdresVersements ov = new REOrdresVersements();
                    ov.setSession(session);
                    ov.setIdPrestation(prestation.getIdPrestation());
                    ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE);
                    ov.setMontant(uneRenteVerseeATortPourUnTiers.getKey().montant.toString());
                    ov.setMontantDette(detailCalcul.getMontantTotalVerseeATort().toString());
                    ov.setIdRenteAccordeeDiminueeParOV(null);
                    if (cle.idRenteAncienDroit != null) {
                        ov.setIdRenteAccordeeACompenserParOV(cle.idRenteAncienDroit.toString());
                    }
                    ov.setIdRenteVerseeATort(detailCalcul.getIdRenteVerseeATort().toString());
                    ov.setIdSection(null);
                    ov.setIsCompense(Boolean.TRUE);
                    ov.setIdTiers(uneRenteVerseeATortPourUnTiers.getKey().idTiers.toString());
                    ov.setIdTiersAdressePmt(null);
                    ov.setIdDomaineApplication(null);
                    ov.setIsValide(Boolean.TRUE);

                    ov.add(transaction);
                    ovsCrees.add(ov);
                }

                // Gestion des rentes versée à tort saisies manuellement
                RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
                renteVerseeATortManager.setSession(session);
                renteVerseeATortManager.setForIdDemandeRente(Long.parseLong(decisionEntity.getIdDemandeRente()));
                renteVerseeATortManager.setForIdTiers(Long.parseLong(membre.getIdTiers()));
                renteVerseeATortManager.setForIdsRentesNouveauDroitIn(idsRA);
                renteVerseeATortManager.setSeulementLesSaisiesManuelles(true);
                renteVerseeATortManager.find();

                for (RERenteVerseeATort uneSaisieManuelle : renteVerseeATortManager.getContainerAsList()) {

                    REOrdresVersements ov = new REOrdresVersements();
                    ov.setSession(session);
                    ov.setIdPrestation(prestation.getIdPrestation());
                    ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE);
                    ov.setMontant(uneSaisieManuelle.getMontant().toString());
                    ov.setMontantDette(uneSaisieManuelle.getMontant().toString());
                    ov.setIdRenteAccordeeDiminueeParOV(null);
                    ov.setIdRenteAccordeeACompenserParOV(uneSaisieManuelle.getIdRenteAccordeeNouveauDroit().toString());
                    ov.setIdRenteVerseeATort(uneSaisieManuelle.getIdRenteVerseeATort().toString());
                    ov.setIdSection(null);
                    ov.setIsCompense(Boolean.TRUE);
                    ov.setIdTiers(uneSaisieManuelle.getIdTiers().toString());
                    ov.setIdTiersAdressePmt(null);
                    ov.setIdDomaineApplication(null);
                    ov.setIsValide(Boolean.TRUE);

                    ov.add(transaction);
                    ovsCrees.add(ov);
                }

                /*
                 * Gestion des rentes versées à tort sans lien vers une rente du nouveau droit (trou dans la période du
                 * nouveau droit)
                 * Ces rentes seront rattachées à la première décision (s'il y en a plusieurs), on vérifie donc qu'elle
                 * ne soit pas déjà lié à un ordre de versement
                 */
                renteVerseeATortManager = new RERenteVerseeATortManager();
                renteVerseeATortManager.setSession(session);
                renteVerseeATortManager.setForIdDemandeRente(Long.parseLong(decisionEntity.getIdDemandeRente()));
                renteVerseeATortManager.setForIdTiers(Long.parseLong(membre.getIdTiers()));
                renteVerseeATortManager.setSansRenteDuNouveauDroit(true);
                renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

                for (RERenteVerseeATort uneRenteVerseeATortSansRenteNouveauDroit : renteVerseeATortManager
                        .getContainerAsList()) {

                    // recherche d'un éventuel ordre de versement déjà lié à cette rente versée à tort
                    REOrdresVersementsManager ordresVersementsManager = new REOrdresVersementsManager();
                    ordresVersementsManager.setSession(session);
                    ordresVersementsManager.setForIdRenteVerseeATort(uneRenteVerseeATortSansRenteNouveauDroit
                            .getIdRenteVerseeATort());
                    ordresVersementsManager.find(transaction, BManager.SIZE_NOLIMIT);

                    if (ordresVersementsManager.size() == 0) {

                        REOrdresVersements ov = new REOrdresVersements();
                        ov.setSession(session);
                        ov.setIdPrestation(prestation.getIdPrestation());
                        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE);
                        ov.setMontant(uneRenteVerseeATortSansRenteNouveauDroit.getMontant().toString());
                        ov.setMontantDette(uneRenteVerseeATortSansRenteNouveauDroit.getMontant().toString());
                        ov.setIdRenteAccordeeDiminueeParOV(null);
                        ov.setIdRenteAccordeeACompenserParOV(null);
                        ov.setIdRenteVerseeATort(uneRenteVerseeATortSansRenteNouveauDroit.getIdRenteVerseeATort()
                                .toString());
                        ov.setIdSection(null);
                        ov.setIsCompense(Boolean.TRUE);
                        ov.setIdTiers(uneRenteVerseeATortSansRenteNouveauDroit.getIdTiers().toString());
                        ov.setIdTiersAdressePmt(null);
                        ov.setIdDomaineApplication(null);
                        ov.setIsValide(Boolean.TRUE);

                        ov.add(transaction);
                        ovsCrees.add(ov);
                    }
                }
            }
        }
    }

    private void addDettesCompensations(final BSession session, final BITransaction transaction,
            final String idTiersRequerant, final String idPrestation, final FWCurrency montantPrestation,
            final Collection<REOrdresVersements> ovsCrees) throws Exception {

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        // instanciation du processus de compta
        // BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);
        // APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne)
        // sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);

        // On récupère les membres de la famille du requérant
        ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTiersRequerant);
        for (ISFMembreFamilleRequerant membre : mf) {

            if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(membre.getRelationAuRequerant())
                    || ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(membre.getRelationAuRequerant())
                    || ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(membre.getRelationAuRequerant())) {

                Collection<APISection> propositionsCompensationsPourTiersEnCours = null;
                Iterator<APISection> propositionsCompensationsIterator = null;
                APISection sectionEnCours = null;

                try {
                    // Récupérer les dettes du conjoint.
                    if (montantPrestation.isPositive()) {
                        propositionsCompensationsPourTiersEnCours = getCollectionSectionsACompenser(session,
                                membre.getIdTiers(), new FWCurrency(999999999));
                    } else {
                        propositionsCompensationsPourTiersEnCours = getCollectionSectionsACompenser(session,
                                membre.getIdTiers(), new FWCurrency(-999999999));
                    }

                    propositionsCompensationsIterator = propositionsCompensationsPourTiersEnCours.iterator();

                } catch (Exception e) {
                    // Catch les exception de la compta.
                    // Au lieu de retourné un élément vide ou null, la CA renvoie un exception si rien trouvé.
                    propositionsCompensationsIterator = null;
                }

                while ((propositionsCompensationsIterator != null) && propositionsCompensationsIterator.hasNext()) {
                    sectionEnCours = propositionsCompensationsIterator.next();

                    // On crée un OV de type detteXXX pour chaque facture à compenser
                    REOrdresVersements ov = new REOrdresVersements();
                    ov.setSession(session);
                    ov.setIdPrestation(idPrestation);

                    APICompteAnnexe ca = sectionEnCours.getCompteAnnexe();
                    ov.setIdRoleComptaAux(ca.getIdRole());

                    if (APISection.ID_CATEGORIE_SECTION_AVANCE.equals(sectionEnCours.getCategorieSection())) {
                        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_AVANCES);
                    } else if (APISection.ID_CATEGORIE_SECTION_RESTITUTIONS
                            .equals(sectionEnCours.getCategorieSection())) {
                        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
                    } else if (APISection.ID_CATEGORIE_SECTION_DECISION.equals(sectionEnCours.getCategorieSection())) {
                        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_DECISION);
                    } else if (APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES.equals(sectionEnCours
                            .getCategorieSection())) {
                        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_PRST_BLOQUE);
                    } else if (APISection.ID_CATEGORIE_SECTION_RETOUR.equals(sectionEnCours.getCategorieSection())) {
                        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RETOUR);
                    } else {
                        // Ne nous concerne pas.
                        continue;
                    }
                    ov.setMontant("0.0");
                    ov.setMontantDette(sectionEnCours.getSolde());

                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, membre.getIdTiers());
                    if (tw == null) {
                        throw new Exception(session.getLabel("ERREUR_CONJOINT_NON_REPERTORIE") + membre.getIdTiers());
                    }

                    ov.setNoFacture(ca.getIdExterneRole() + " " + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " - " + sectionEnCours.getIdExterne());

                    ov.setIdSection(sectionEnCours.getIdSection());
                    ov.setIsCompense(Boolean.FALSE);
                    ov.setIdTiers(membre.getIdTiers());
                    ov.setIdTiersAdressePmt(null);
                    ov.setIdDomaineApplication(null);
                    ov.setIsValide(Boolean.TRUE);
                    ov.add(transaction);

                    ovsCrees.add(ov);
                }
            }
        }
    }

    /**
     * Crée la prestation avec les ordres de versements liés. La prestation est le cummul des $p pour la décision. De
     * plus, créé les OV à partir des créanciers, ainsi que les IM Récupérer les dettes de la famille, le cas échéants
     * et les ajouter en tant que OV.
     * 
     * @param session
     * @param transaction
     * @param idDemandeRente
     * @param ra
     * @throws Exception
     */
    private PrestationWrapper addPrestation(final BSession session, final BTransaction transaction,
            final PrestationWrapper pw, final JADate dateDebutRetroDecision, final JADate dateFinRetroDecision,
            final String idDemandeRente, final RERenteAccordee ra, final REPrestationDue pd) throws Exception {

        /*
         * 
         * Exemples :
         * 
         * Décision RETRO [ période RETRO décision ] [ pd ] -1-----2-----------3------------4----------->t
         * 
         * 
         * Le nombre de mois à prendre en compte est : 3-2
         * 
         * 
         * 
         * Décision courant (le rétro est sur le mois en cours, car déjà payé) période RETRO décision ][ [ pd
         * -1---------------------------2--------------->t
         * 
         * 
         * Le nombre de mois à prendre en compte est : 1
         * 
         * 
         * 
         * Décision STANDARD
         * 
         * [ période RETRO décision ] [ pd ][ pd ][ pd-1----------23----------45------6------------------->t
         * 
         * 
         * Le nombre de mois à prendre en compte est : 2-1 + 4-3 + 6-5. Ce la dit, cette méthode sera appelé 3 fois, cad
         * 1 fois pour chaque pd.
         */

        JACalendar cal = new JACalendarGregorian();
        JADate dd = new JADate(pd.getDateDebutPaiement());

        // La date de début de la prestation dues doit être comprise entre date debut/fin de la période
        // Rétroactive.
        boolean isRetroCourantPourMoisEnCours = false;
        if ((dateDebutRetroDecision != null) && (dateFinRetroDecision != null)
                && (cal.compare(dateDebutRetroDecision, dateFinRetroDecision) == JACalendar.COMPARE_EQUALS)) {
            // Validation du courant, on ne prend que le mois en cours.
            isRetroCourantPourMoisEnCours = true;
            ;
        }

        else if ((cal.compare(dd, dateDebutRetroDecision) == JACalendar.COMPARE_FIRSTUPPER)
                || (cal.compare(dd, dateDebutRetroDecision) == JACalendar.COMPARE_EQUALS)) {

            // Cas possible, après l'adaptation des rentes !!!!
            // Une date de fin est mise au $p pour le mois de décembre. Si la décision courante à début en octobre, par
            // exemple
            // la dateFinDecision Retro == septembre et après l'augmenation, la df du $p passe de vide à décembre ->
            // passe dans le test et plante.

            // if (!JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
            // JADate df = new JADate(pd.getDateFinPaiement());
            //
            // if ((cal.compare(df, dateFinRetroDecision) == JACalendar.COMPARE_FIRSTLOWER)
            // || (cal.compare(df, dateFinRetroDecision) == JACalendar.COMPARE_EQUALS)) {
            // ;// OK
            // } else {
            // throw new Exception(session.getLabel("ERREUR_DATE_FIN_PREST_SUP_DATE_FIN_RETRO"));
            // }
            // }
        } else {
            // Pas de rétro pour cette prestation due à calculer.
            return pw;
        }

        JADate df = null;

        if (!JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
            df = new JADate(pd.getDateFinPaiement());
        } else {
            df = dateFinRetroDecision;
        }
        // On prend la plus petite des dates de fins.
        if (cal.compare(df, dateFinRetroDecision) == JACalendar.COMPARE_FIRSTUPPER) {
            df = dateFinRetroDecision;
        }
        int nbrMois = 0;
        if (isRetroCourantPourMoisEnCours) {
            nbrMois = 1;
        } else {
            nbrMois = PRDateFormater.nbrMoisEntreDates(dd, df);
        }

        BigDecimal montant = new BigDecimal(pd.getMontant());
        montant = montant.multiply(new BigDecimal(nbrMois));

        pw.montantPrestation.add(montant.toString());

        // Les créances sont par rente accordée. Ce test est nécessaire pour
        // ne pas récupérer plusieurs fois la même créance.
        // Il en va de même pour les intérets moratoires.

        if (!pw.idsRATraitee.contains(Long.parseLong(ra.getIdPrestationAccordee()))) {

            pw.idsRATraitee.add(Long.parseLong(ra.getIdPrestationAccordee()));

            /*
             * Traitement des créances
             */

            // Ajouter ordres de versement a partir des creances accordees.
            RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
            caManager.setSession(session);
            caManager.setForIdRenteAccordee(ra.getIdPrestationAccordee());
            caManager.find(transaction);

            // un ordre de versement pour chaque creance accordee
            // Les OV sont regroupées par bénéficiaire(adr. pmt) et type d'ordre.
            for (int i = 0; i < caManager.getSize(); i++) {
                RECreanceAccordee ca = (RECreanceAccordee) caManager.getEntity(i);

                RECreancier creancier = new RECreancier();
                creancier.setSession(session);
                creancier.setIdCreancier(ca.getIdCreancier());
                creancier.retrieve(transaction);
                PRAssert.notIsNew(
                        creancier,
                        "creancier not found for idCreancier/idCreanceAccordee : " + ca.getIdCreancier() + "/"
                                + ca.getIdCreanceAccordee());

                OrdreVersementWrapper ovw = new OrdreVersementWrapper();
                // convertir le type de creancier pour obtenir le type d'ordre de versement
                ovw.csType = getOvTypeAccordingToCType(creancier.getCsType());
                ovw.idDomaine = creancier.getIdDomaineApplicatif();
                ovw.idTiers = creancier.getIdTiers();
                ovw.idTiersAdrPmt = creancier.getIdTiersAdressePmt();
                ovw.montant = new FWCurrency(ca.getMontant());
                ovw.idCreanceAccordee = ca.getIdCreanceAccordee();
                pw.addOV(ovw);
            }

            /*
             * 
             * Traitement des Intérêts Moratoires
             */

            // si il y a des IM, on cree un odre de versement de type IM
            if (!JadeStringUtil.isIntegerEmpty(ra.getIdCalculInteretMoratoire())) {
                // on cherche les interets moratoires de la rente accordee

                RECalculInteretMoratoire cim = new RECalculInteretMoratoire();
                cim.setSession(session);
                cim.setIdCalculInteretMoratoire(ra.getIdCalculInteretMoratoire());
                cim.retrieve(transaction);

                REInteretMoratoire im = new REInteretMoratoire();
                im.setSession(session);
                im.setIdInteretMoratoire(cim.getIdInteretMoratoire());
                im.retrieve(transaction);

                if (im.isNew()) {
                    throw new REBusinessException(
                            session.getLabel("ERREUR_PREPARATION_DECISION_CALCUL_INTERETS_MORATOIRES_INACHEVE"));
                }

                // Ajouter l'ov dans la liste.

                // on ne crée un OV que si le montant des intérêts moratoires est supérieur à zéro
                if (!JadeStringUtil.isBlankOrZero(im.getMontantInteret())) {
                    OrdreVersementWrapper ovw = new OrdreVersementWrapper();
                    ovw.csType = IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE;
                    ovw.idDomaine = im.getCsDomaineAdrPmt();
                    ovw.idTiers = cim.getIdTiers();
                    ovw.idTiersAdrPmt = im.getIdTiersAdrPmt();
                    ovw.montant = new FWCurrency(im.getMontantInteret());
                    pw.addOV(ovw);
                }
            }
        }
        return pw;
    }

    /**
     * Ajoute une prestation dans l'état attente. Le montant de la prestation = Somme(des montants des RA) Utilisée
     * uniquement pour les décisions sans rétroactif.
     * 
     * @param session
     * @param transaction
     * @param decision
     * @param idsRA
     * @param idsRADejaDiminuees
     * @return
     * @throws Exception
     */
    private REPrestations addPrestationSansOV(final BSession session, final BTransaction transaction,
            final REDecisionEntity decision, final Collection<Long> idsRA, final DemandeRente demande,
            final String dateDernierPaiement) throws Exception {

        // Pas de rétro pour la décision
        // On créé cependant une prestation, sans aucun OV.
        // Création de la prestation...
        REPrestations prestation = new REPrestations();
        prestation.setSession(session);
        prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
        prestation.setIdDemandeRente(decision.getIdDemandeRente());
        prestation.setMoisAnnee(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(JACalendar.todayJJsMMsAAAA()));
        prestation.setIdDecision(decision.getId().toString());

        prestation.setMontantPrestation("0");
        prestation.add(transaction);

        // on ne traite pas les dettes dans le cas d'une décision courante
        if (!IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {
            addDetteRenteEnCours(session, transaction, decision, demande.getRequerant().getId().toString(), prestation,
                    idsRA, new ArrayList<REOrdresVersements>());
        }

        REOrdresVersements ov = new REOrdresVersements();
        ov.setSession(session);
        ov.setCsType(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
        // pour les décisions courantes, si la date de décision depuis est la même que le mois de paiement, on doit un
        // mois à l'assuré
        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())
                && dateDernierPaiement.equals(JadeDateUtil.convertDateMonthYear(decision.getDecisionDepuis()))) {

            BigDecimal montantRetroPourUnMois = BigDecimal.ZERO;

            Periode periodeDuMoisCourant = new Periode(decision.getDecisionDepuis(), decision.getDecisionDepuis());

            for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
                if (idsRA.contains(uneRenteDeLaDemande.getId())
                        && (periodeDuMoisCourant.intersection(uneRenteDeLaDemande.getPeriodeDuDroit()) != null)) {
                    montantRetroPourUnMois = montantRetroPourUnMois.add(uneRenteDeLaDemande.getMontant());
                }
            }

            ov.setMontant(montantRetroPourUnMois.toString());
            ov.setMontantDette("0.00");

            prestation.setMontantPrestation(ov.getMontant());
            prestation.update(transaction);

            REBeneficiairePrincipalVO bp = REBeneficiairePrincipal.retrieveBeneficiairePrincipal(session, transaction,
                    idsRA);

            TIAdressePaiementData adr = PRTiersHelper.getAdressePaiementData(session, transaction, bp.ra
                    .loadInformationsComptabilite().getIdTiersAdressePmt(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            if ((adr == null) || adr.isNew()) {
                throw new Exception(session.getLabel("ERREUR_AUCUNE_ADRESSE_PMT_TROUVE") + prestation.getIdPrestation()
                        + "/" + bp.ra.loadInformationsComptabilite().getIdTiersAdressePmt());
            }

            ov.setIdTiers(bp.idTiersBeneficiairePrincipal);
            ov.setIdTiersAdressePmt(bp.ra.loadInformationsComptabilite().getIdTiersAdressePmt());
            ov.setIdDomaineApplication(adr.getIdApplication());

        } else {
            ov.setIdTiers(decision.getIdTiersBeneficiairePrincipal());
            ov.setMontant(new FWCurrency("0").toString());
            ov.setMontantDette(new FWCurrency("0").toString());
        }

        ov.setIdPrestation(prestation.getIdPrestation());
        ov.add(transaction);

        return prestation;
    }

    protected void assertNotIsNew(final BEntity entity, final String errorMsg) throws Exception {
        if (entity.isNew()) {
            throw new Exception(errorMsg == null ? "" : errorMsg + " " + entity.getClass().getName()
                    + " not Found. id=" + entity.getId());
        }
    }

    /**
     * @param session
     * @param transaction
     * @param decision
     * @param tiersBeneficiaire
     * @throws Exception
     */
    private void creerCopiePourAffaireMilitaire(final BSession session, final BITransaction transaction,
            final REDecisionEntity decision, final PRTiersWrapper tiersBeneficiaire) throws Exception {
        // calcul du nombre de mois de vie du tiers jusqu'à la date de la décision
        int nombreMoisEntreNaissanceEtDecision = JadeDateUtil.getNbMonthsBetween(
                tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE), decision.getDateDecision());

        // Recherche du service des affaires militaires cantonal
        // par rapport au canton de domicile du tiers
        String cantonDomicileTiers = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);

        // si le tiers a entre 20 et 34 ans au moment de la décision, création d'une copie
        // pour les affaires militaires
        if (!JadeStringUtil.isBlank(cantonDomicileTiers) && (nombreMoisEntreNaissanceEtDecision >= (20 * 12))
                && (nombreMoisEntreNaissanceEtDecision < (34 * 12))) {
            // recherche du service des affaires militaires pour le canton défini plus haut
            TIAdministrationAdresseManager manager = new TIAdministrationAdresseManager();
            manager.setSession(session);
            manager.setForGenreAdministration(IPRConstantesExternes.TIERS_CS_ADMINISTRATION_GENRE_AFFAIRES_MILITAIRES);
            manager.setForCantonAdministration(cantonDomicileTiers);
            manager.find(BManager.SIZE_NOLIMIT);

            String idAdministrationAffairesMilitairesCantonale = null;
            if (manager.size() == 1) {
                idAdministrationAffairesMilitairesCantonale = ((TIAdministrationAdresse) manager.get(0))
                        .getIdTiersAdministration();
            }

            if (!JadeStringUtil.isBlankOrZero(idAdministrationAffairesMilitairesCantonale)) {
                RECopieDecision copie = new RECopieDecision();
                copie.setSession(session);
                copie.setIdDecision(decision.getIdDecision());
                copie.setIdTiersCopie(idAdministrationAffairesMilitairesCantonale);

                // selon le document du mandat InfoRom 329 (document N°32004T)
                copie.setIsPageGarde(Boolean.TRUE);
                copie.setIsVersementA(Boolean.FALSE);
                copie.setIsBaseCalcul(Boolean.FALSE);
                copie.setIsDecompte(Boolean.TRUE);
                copie.setIsRemarques(Boolean.TRUE);
                copie.setIsMoyensDroit(Boolean.FALSE);
                copie.setIsSignature(Boolean.TRUE);
                copie.setIsAnnexes(Boolean.FALSE);
                copie.setIsCopies(Boolean.TRUE);

                copie.add(transaction);
            }
        }
    }

    /**
     * @param session
     * @param transaction
     * @param dem
     * @param decision
     * @param isRetro
     * @param isDecisionRetro
     * @param typeDecision
     * @param warningCopy
     * @throws Exception
     */
    private void creerCopiePourAgenceCommunale(final BSession session, final BITransaction transaction,
            final DemandeRente demande, final REDecisionEntity decision, final boolean isRetro,
            final boolean isDecisionRetro, final String typeDecision, final StringBuffer warningCopy) throws Exception {
        TICompositionTiersManager compTiersMgr = new TICompositionTiersManager();
        compTiersMgr.setSession(session);

        // Retrieve du destinataire de la décision
        String idTiersPrincipal = "";
        idTiersPrincipal = decision.getIdTiersBeneficiairePrincipal();

        compTiersMgr.setForIdTiersParent(idTiersPrincipal);
        compTiersMgr.setForTypeLien("507007");

        compTiersMgr.find();

        if (compTiersMgr.isEmpty()) {
            if (!JadeStringUtil.contains(warningCopy.toString(), session.getLabel("WARNING_AGENCE_COMM"))) {
                warningCopy.append(session.getLabel("WARNING_AGENCE_COMM") + "\n");
            }
        }

        for (int i = 0; i < compTiersMgr.size(); i++) {
            TICompositionTiers entity = (TICompositionTiers) compTiersMgr.get(i);

            TIAdministrationViewBean administrationCommunale = new TIAdministrationViewBean();
            administrationCommunale.setSession(session);
            administrationCommunale.setIdTiersAdministration(entity.getIdTiersEnfant());
            administrationCommunale.retrieve(transaction);

            if (administrationCommunale.isNew() || "9998".equals(administrationCommunale.getCodeAdministration())
                    || "9999".equals(administrationCommunale.getCodeAdministration())) {
                continue;
            }

            RECopieDecisionViewBean copie = new RECopieDecisionViewBean();
            copie.setSession(session);
            copie.setIdDecision(decision.getIdDecision());
            copie.setIdTiersCopie(entity.getIdTiersEnfant());
            copie.setIsVersementA(Boolean.TRUE);

            if (isDecisionRetro) {
                copie.setIsBaseCalcul(Boolean.FALSE);
            } else {
                copie.setIsBaseCalcul(Boolean.TRUE);
            }

            if (typeDecision.startsWith("INV")) {
                REDemandeRenteInvalidite demAI = new REDemandeRenteInvalidite();
                demAI.setSession(session);
                demAI.setIdDemandeRente(demande.getId().toString());
                demAI.retrieve();

                if (!isRetro
                        && demAI.getCsGenrePrononceAI().equals(
                                IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {
                    copie.setIsMoyensDroit(Boolean.FALSE);
                    copie.setIsSignature(Boolean.FALSE);
                } else {
                    copie.setIsMoyensDroit(Boolean.TRUE);
                    copie.setIsSignature(Boolean.TRUE);
                }

            } else {
                REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
                demAPI.setSession(session);
                demAPI.setIdDemandeRente(demande.getId().toString());
                demAPI.retrieve();

                if (!isRetro
                        && demAPI.getCsGenrePrononceAI().equals(
                                IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {
                    copie.setIsMoyensDroit(Boolean.FALSE);
                    copie.setIsSignature(Boolean.FALSE);
                } else {
                    copie.setIsMoyensDroit(Boolean.TRUE);
                    copie.setIsSignature(Boolean.TRUE);
                }
            }

            copie.setIsDecompte(Boolean.TRUE);
            copie.setIsRemarques(Boolean.TRUE);
            copie.setIsAnnexes(Boolean.TRUE);
            copie.setIsCopies(Boolean.TRUE);
            copie.setIsPageGarde(Boolean.FALSE);
            copie.add(transaction);
        }
    }

    /**
     * @param session
     * @param transaction
     * @param dem
     * @param decision
     * @param isRetro
     * @param isDecisionRetro
     * @param isIdTiersBCEqualsIdTiersReqDemande
     * @param typeDecision
     * @throws Exception
     */
    private void creerCopiePourLaCaisse(final BSession session, final BITransaction transaction,
            final DemandeRente demande, final REDecisionEntity decision, final boolean isRetro,
            final boolean isDecisionRetro, final boolean isIdTiersBCEqualsIdTiersReqDemande, final String typeDecision)
            throws Exception {
        TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
        tiAdminCaisseMgr.setSession(session);
        tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                session.getApplication()));
        tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
        tiAdminCaisseMgr.find();

        // on récupère le tiers bénéficiaire pour en connaître sa langue
        PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersById(session,
                decision.getIdTiersBeneficiairePrincipal());

        TIAdministrationViewBean tiAdminCaisse = PRTiersHelper.resolveAdminFromTiersLanguage(tiersBeneficiaire,
                tiAdminCaisseMgr);

        String idTiersCaisse = "";
        if (tiAdminCaisse != null) {
            idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
        }

        RECopieDecisionViewBean copie = new RECopieDecisionViewBean();
        copie.setSession(session);
        copie.setIdDecision(decision.getIdDecision());
        copie.setIdTiersCopie(idTiersCaisse);
        copie.setIsPageGarde(Boolean.FALSE);
        copie.setIsVersementA(Boolean.TRUE);

        if (isDecisionRetro) {
            copie.setIsBaseCalcul(Boolean.FALSE);
        } else {
            copie.setIsBaseCalcul(Boolean.TRUE);
        }

        copie.setIsDecompte(Boolean.TRUE);
        copie.setIsRemarques(Boolean.TRUE);

        if (isIdTiersBCEqualsIdTiersReqDemande) {

            if (typeDecision.startsWith("INV")) {
                REDemandeRenteInvalidite demAI = new REDemandeRenteInvalidite();
                demAI.setSession(session);
                demAI.setIdDemandeRente(demande.getId().toString());
                demAI.retrieve();

                if (!isRetro
                        && demAI.getCsGenrePrononceAI().equals(
                                IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {
                    copie.setIsMoyensDroit(Boolean.FALSE);
                    copie.setIsSignature(Boolean.FALSE);
                } else {
                    copie.setIsMoyensDroit(Boolean.TRUE);
                    copie.setIsSignature(Boolean.TRUE);
                }

            } else {
                REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
                demAPI.setSession(session);
                demAPI.setIdDemandeRente(demande.getId().toString());
                demAPI.retrieve();

                if (!isRetro
                        && demAPI.getCsGenrePrononceAI().equals(
                                IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {
                    copie.setIsMoyensDroit(Boolean.FALSE);
                    copie.setIsSignature(Boolean.FALSE);
                } else {
                    copie.setIsMoyensDroit(Boolean.TRUE);
                    copie.setIsSignature(Boolean.TRUE);
                }
            }

        } else {
            copie.setIsMoyensDroit(Boolean.TRUE);
            copie.setIsSignature(Boolean.TRUE);
        }

        copie.setIsAnnexes(Boolean.TRUE);
        copie.setIsCopies(Boolean.TRUE);

        copie.add(transaction);
    }

    /**
     * @param session
     * @param transaction
     * @param decision
     * @param idRaPourRequ
     * @param warningCopy
     * @throws Exception
     */
    private void creerCopiePourLeFisc(final BSession session, final BITransaction transaction,
            final REDecisionEntity decision, final String idRaPourRequ, final StringBuffer warningCopy)
            throws Exception {
        TIAdministrationAdresseManager admAdrMgr = new TIAdministrationAdresseManager();
        admAdrMgr.setSession(session);

        // Trouver le canton de domicile du bénéfiaire principal
        // Retrieve du destinataire de la décision
        String idTiersPrincipal = "";
        idTiersPrincipal = decision.getIdTiersBeneficiairePrincipal();
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseDomicileParId(session, idTiersPrincipal,
                JACalendar.todayJJsMMsAAAA());

        if (null == tier) {

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(idRaPourRequ);
            ra.retrieve();

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve();

            tier = PRTiersHelper.getTiersAdresseDomicileParId(session, bc.getIdTiersBaseCalcul(),
                    JACalendar.todayJJsMMsAAAA());

        }

        if (tier == null) {
            throw new Exception(session.getLabel("PROCESS_PREP_DECISION_PAS_ADR_DOM"));
        }

        String cantonDomicile = tier.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
        String langueTier = tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE);

        admAdrMgr.setForCantonAdministration(cantonDomicile);
        admAdrMgr.setForGenreAdministration("509011");// Administration fiscale cantonale
        admAdrMgr.find();

        if (admAdrMgr.isEmpty()) {
            if (!JadeStringUtil.contains(warningCopy.toString(), session.getLabel("WARNING_ADM_FISCALE"))) {
                warningCopy.append(session.getLabel("WARNING_ADM_FISCALE") + "\n");
            }
        }

        String idAdmFiscale = "";
        String idAdmFiscaleFR = "";
        String idAdmFiscaleDE = "";
        String idAdmFiscaleAutre = "";
        String idAdmFiscaleNonBilingueAutre = "";

        for (int i = 0; i < admAdrMgr.size(); i++) {

            TIAdministrationAdresse entity = (TIAdministrationAdresse) admAdrMgr.get(i);

            if (entity.getCantonAdministration().equals(IConstantes.CS_LOCALITE_CANTON_BERNE)
                    || entity.getCantonAdministration().equals(IConstantes.CS_LOCALITE_CANTON_FRIBOURG)
                    || entity.getCantonAdministration().equals(IConstantes.CS_LOCALITE_CANTON_VALAIS)) {
                // si canton de Berne,Fribourg ou Valais choisir le service dans la langue du
                // bénéficiaire principal
                // sinon langue adm = langue tier

                if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(langueTier)) {

                    if (JadeStringUtil.isBlank(idAdmFiscaleFR)) {

                        if (entity.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                            idAdmFiscaleFR = entity.getIdTiers();
                        } else if (entity.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                            idAdmFiscaleDE = entity.getIdTiers();
                        } else {
                            idAdmFiscaleAutre = entity.getIdTiers();
                        }
                    }
                } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(langueTier)
                        || IConstantes.CS_TIERS_LANGUE_ROMANCHE.equals(langueTier)) {

                    if (JadeStringUtil.isBlank(idAdmFiscaleDE)) {

                        if (entity.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                            idAdmFiscaleFR = entity.getIdTiers();
                        } else if (entity.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                            idAdmFiscaleDE = entity.getIdTiers();
                        } else {
                            idAdmFiscaleAutre = entity.getIdTiers();
                        }
                    }
                } else {

                    if (JadeStringUtil.isBlank(idAdmFiscaleAutre)) {

                        if (entity.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                            idAdmFiscaleFR = entity.getIdTiers();
                        } else if (entity.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                            idAdmFiscaleDE = entity.getIdTiers();
                        } else {
                            idAdmFiscaleAutre = entity.getIdTiers();
                        }
                    }
                }
            } else {
                if (entity.getLangue().equals(langueTier)) {
                    idAdmFiscale = entity.getIdTiers();
                } else {
                    idAdmFiscaleNonBilingueAutre = entity.getIdTiers();
                }
            }
        }// Fin boucle for

        if (JadeStringUtil.isBlank(idAdmFiscale) && JadeStringUtil.isBlank(idAdmFiscaleNonBilingueAutre)) {

            // Si assuré FR, recours FR sinon DE sinon Autre
            if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(langueTier)) {

                if (!JadeStringUtil.isBlank(idAdmFiscaleFR)) {
                    idAdmFiscale = idAdmFiscaleFR;
                } else if (!JadeStringUtil.isBlank(idAdmFiscaleDE)) {
                    idAdmFiscale = idAdmFiscaleDE;
                } else if (!JadeStringUtil.isBlank(idAdmFiscaleAutre)) {
                    idAdmFiscale = idAdmFiscaleAutre;
                } else {
                    idAdmFiscale = "";
                }

                // Si assuré DE ou RO, recours DE sinon FR sinon Autre
            } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(langueTier)
                    || IConstantes.CS_TIERS_LANGUE_ROMANCHE.equals(langueTier)) {

                if (!JadeStringUtil.isBlank(idAdmFiscaleDE)) {
                    idAdmFiscale = idAdmFiscaleDE;
                } else if (!JadeStringUtil.isBlank(idAdmFiscaleFR)) {
                    idAdmFiscale = idAdmFiscaleFR;
                } else if (!JadeStringUtil.isBlank(idAdmFiscaleAutre)) {
                    idAdmFiscale = idAdmFiscaleAutre;
                } else {
                    idAdmFiscale = "";
                }

                // Si assuré autre, recours FR sinon DE sinon Autre
            } else {
                if (!JadeStringUtil.isBlank(idAdmFiscaleAutre)) {
                    idAdmFiscale = idAdmFiscaleAutre;
                } else if (!JadeStringUtil.isBlank(idAdmFiscaleFR)) {
                    idAdmFiscale = idAdmFiscaleFR;
                } else if (!JadeStringUtil.isBlank(idAdmFiscaleDE)) {
                    idAdmFiscale = idAdmFiscaleDE;
                } else {
                    idAdmFiscale = "";
                }
            }
        }

        if (JadeStringUtil.isBlank(idAdmFiscale)) {
            if (!JadeStringUtil.isBlank(idAdmFiscaleNonBilingueAutre)) {
                idAdmFiscale = idAdmFiscaleNonBilingueAutre;
            } else if (!JadeStringUtil.contains(warningCopy.toString(), session.getLabel("WARNING_ADM_FISCALE"))) {
                warningCopy.append(session.getLabel("WARNING_ADM_FISCALE") + "\n");
            }
        }

        if (!JadeStringUtil.isBlank(idAdmFiscale)) {
            RECopieDecisionViewBean copie = new RECopieDecisionViewBean();
            copie.setSession(session);
            copie.setIdDecision(decision.getIdDecision());
            copie.setIdTiersCopie(idAdmFiscale);
            copie.setIsVersementA(Boolean.FALSE);
            copie.setIsBaseCalcul(Boolean.FALSE);
            copie.setIsDecompte(Boolean.TRUE);
            copie.setIsRemarques(Boolean.TRUE);
            copie.setIsMoyensDroit(Boolean.FALSE);
            copie.setIsSignature(Boolean.TRUE);
            copie.setIsAnnexes(Boolean.FALSE);
            copie.setIsCopies(Boolean.TRUE);

            if (session.getApplication().getProperty("isLettreEnTeteCopieFisc").equals("true")) {
                copie.setIsPageGarde(Boolean.TRUE);
            }

            copie.add(transaction);
        }
    }

    /**
     * @param session
     * @param transaction
     * @param dem
     * @param decision
     * @param isRetro
     * @param isDecisionRetro
     * @param typeDecision
     * @throws Exception
     */
    private void creerCopiePourOfficeAI(final BSession session, final BITransaction transaction,
            final DemandeRente demande, final REDecisionEntity decision, final boolean isRetro,
            final boolean isDecisionRetro, final String typeDecision) throws Exception {
        String noOfficeAI = "";

        REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
        demAPI.setSession(session);
        demAPI.setIdDemandeRente(demande.getId().toString());
        demAPI.retrieve();

        noOfficeAI = demAPI.getCodeOfficeAI();

        TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
        tiAdministrationMgr.setSession(session);
        tiAdministrationMgr.setForCodeAdministration(noOfficeAI);
        tiAdministrationMgr.setForGenreAdministration("509004");
        tiAdministrationMgr.find();
        // on récupère le tiers bénéficiaire pour en connaître sa langue
        PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersById(session,
                decision.getIdTiersBeneficiairePrincipal());

        TIAdministrationViewBean tiAdministration = PRTiersHelper.resolveAdminFromTiersLanguage(tiersBeneficiaire,
                tiAdministrationMgr);

        if (null != tiAdministration) {

            RECopieDecisionViewBean copie = new RECopieDecisionViewBean();
            copie.setSession(session);
            copie.setIdDecision(decision.getIdDecision());
            copie.setIdTiersCopie(tiAdministration.getIdTiers());
            copie.setIdAffilie(tiAdministration.getIdTiersExterne());
            copie.setIsVersementA(Boolean.TRUE);

            if (isDecisionRetro) {
                copie.setIsBaseCalcul(Boolean.FALSE);
            } else {
                copie.setIsBaseCalcul(Boolean.TRUE);
            }

            copie.setIsDecompte(Boolean.TRUE);
            copie.setIsRemarques(Boolean.TRUE);

            if (session.getApplication().getProperty("isLettreEnTeteCopieOAI").equals("true")) {
                copie.setIsPageGarde(Boolean.TRUE);
            }

            if (!isRetro
                    && demAPI.getCsGenrePrononceAI().equals(
                            IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {

                if (typeDecision.equals("API-AVS")) {
                    copie.setIsMoyensDroit(Boolean.FALSE);
                    copie.setIsSignature(Boolean.TRUE);
                } else {
                    copie.setIsMoyensDroit(Boolean.FALSE);
                    copie.setIsSignature(Boolean.FALSE);
                }

            } else {
                copie.setIsMoyensDroit(Boolean.TRUE);
                copie.setIsSignature(Boolean.TRUE);
            }

            copie.setIsAnnexes(Boolean.TRUE);
            copie.setIsCopies(Boolean.TRUE);

            copie.add(transaction);
        }
    }

    private void creerOrdresVersementDiminutionRenteFamille(final BITransaction transaction,
            final DemandeRente demande, final Long idPrestation, final REDecisionEntity decision,
            final String dateDernierPaiement, final Set<Long> idsRenteDecision, final Set<Long> idsRADejaDiminuees,
            final Set<Long> beneficiairesDeCetteDecision) throws Exception {

        Set<RenteAccordee> rentesNecessiantUneDiminution = CorvusServiceLocator.getDemandeRenteService()
                .rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(demande);
        Set<RenteAccordee> rentesDeLaDecision = chargerRentesAccordees(idsRenteDecision);

        for (RenteAccordee uneRenteAccordeeADiminuer : rentesNecessiantUneDiminution) {
            boolean estDeLaMemeFamilleQueRenteDecision = false;

            /*
             * On ne prend en compte, comme rente complémentaire à diminuer, que les rentes accordées dont le donneur de
             * droit est le même (père ou mère) afin de ne pas diminuer une rente qui n'est pas impactée par cette
             * décision.
             */
            for (RenteAccordee uneRenteDeLaDecision : rentesDeLaDecision) {
                estDeLaMemeFamilleQueRenteDecision |= uneRenteDeLaDecision
                        .estDeLaMemeFamilleDePrestationQue(uneRenteAccordeeADiminuer);
            }

            if (estDeLaMemeFamilleQueRenteDecision
                    && beneficiairesDeCetteDecision.contains(uneRenteAccordeeADiminuer.getBeneficiaire().getId())
                    && !idsRADejaDiminuees.contains(uneRenteAccordeeADiminuer.getId())) {

                REOrdresVersements ov = new REOrdresVersements();
                ov.setSession(BSessionUtil.getSessionFromThreadContext());
                ov.setCsTypeOrdreVersement(TypeOrdreVersement.DIMINUTION_DE_RENTE);
                ov.setIdDomaineApplication(null);
                ov.setIdPrestation(idPrestation.toString());
                ov.setIdRenteAccordeeACompenserParOV(uneRenteAccordeeADiminuer.getId().toString());
                ov.setIdRenteAccordeeDiminueeParOV(uneRenteAccordeeADiminuer.getId().toString());
                ov.setIdSection(null);
                ov.setIdTiers(uneRenteAccordeeADiminuer.getBeneficiaire().getId().toString());
                ov.setIdTiersAdressePmt(null);
                ov.setIsCompense(Boolean.TRUE);
                ov.setIsValide(Boolean.TRUE);

                // dans le cas des courants validées, on regarde si un montant doit être restitué (si le courant démarre
                // dans le mois de paiement)
                if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())
                        && dateDernierPaiement.equals(decision.getDecisionDepuis())) {
                    Periode periodeDecision = new Periode(decision.getDecisionDepuis(), decision.getDecisionDepuis());
                    Periode periodePrestationAccordee = uneRenteAccordeeADiminuer.getPeriodeDuDroit();

                    Periode periodePayeeATort = periodeDecision.intersectionMois(periodePrestationAccordee);
                    if (periodePayeeATort != null) {
                        BigDecimal montantDette = uneRenteAccordeeADiminuer.getMontant();
                        montantDette = montantDette.multiply(new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                                JadeDateUtil.getFirstDateOfMonth(periodePayeeATort.getDateDebut()),
                                JadeDateUtil.getLastDateOfMonth(periodePayeeATort.getDateFin()))));

                        ov.setMontant(montantDette.toString());
                        ov.setMontantDette(montantDette.toString());
                    }
                } else {
                    ov.setMontant("0.0");
                    ov.setMontantDette("0.0");
                }

                ov.add(transaction);

                idsRADejaDiminuees.add(uneRenteAccordeeADiminuer.getId());
            }
        }
    }

    private Set<RenteAccordee> chargerRentesAccordees(Set<Long> idsRenteDecision) {
        Set<RenteAccordee> rentes = new HashSet<RenteAccordee>();
        for (Long idRenteAccordee : idsRenteDecision) {
            rentes.add(CorvusCrudServiceLocator.getRenteAccordeeCrudService().read(idRenteAccordee));
        }
        return rentes;
    }

    /**
     * Méthode qui permet de dévalider toutes les autres décisions de la demande de rente. Est utilisé lors de la
     * dévalidation d'une décision.
     * 
     * @param REDecisionEntity
     * @param BSession
     * @param BITransaction
     * @throws Exception
     */
    private void devalideAutresDecisionsDemande(final REDecisionEntity decision, final BSession session,
            final BITransaction transaction) throws Exception {

        decisionUtil.devalideAutresDecisionsDemande(decision, session, transaction);
    }

    /**
     * Regroupe toutes les RA pour la demande concernée, par adr. pmt
     * 
     * @throws Exception
     */
    protected void doTraitement(final BSession session, final BTransaction transaction, final DemandeRente demande,
            final REPreValiderDecisionViewBean viewBean, final String csTypeDecision) throws Exception {

        REDemandeRente demandeRenteEntitee = new REDemandeRente();
        demandeRenteEntitee.setSession(session);
        demandeRenteEntitee.setId(demande.getId().toString());
        demandeRenteEntitee.retrieve(transaction);

        RERenteCalculee rc = new RERenteCalculee();
        rc.setSession(session);
        rc.setIdRenteCalculee(demandeRenteEntitee.getIdRenteCalculee());
        rc.retrieve(transaction);

        JADate dfRetroPure = null;
        JACalendar cal = new JACalendarGregorian();

        // Si décision de type Retro Pure, on recherche la date de fin du RETRO
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO.equals(csTypeDecision)) {
            REDecisionEntity dd = retrieveDecisionCourante(session, transaction, demande);

            JADate ddR = new JADate(dd.getDecisionDepuis());
            // La date de fin du rétro = date début rétro moins 1 mois.
            dfRetroPure = cal.addMonths(ddR, -1);
        }

        if (rc.isNew()) {
            throw new Exception("Rente Calculée not found for idDem/idRC : " + demande.getId() + "/"
                    + demandeRenteEntitee.getIdRenteCalculee());
        }

        // Récupération des bases de calculs.
        REBasesCalculManager mgrBC = new REBasesCalculManager();
        mgrBC.setSession(session);
        mgrBC.setForIdRenteCalculee(rc.getIdRenteCalculee());
        mgrBC.setForCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
        mgrBC.find(transaction);

        // Pour chaque BC, récupération des RA, Info Compta et Prst. dues
        REDecisionsContainer decisionContainer = new REDecisionsContainer();
        decisionContainer.setIdDemandeRente(demande.getId().toString());

        JADate dateDecisionCourant = null;
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO.equals(csTypeDecision)) {

            // On récupère la décision de type courant
            REDecisionsManager m = new REDecisionsManager();
            m.setSession(session);
            m.setForIdDemandeRente(demande.getId().toString());
            m.setForCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
            m.find(transaction);
            if (m.isEmpty()) {
                throw new Exception("Aucune décision de type courant trouvée !!! Incohérence dans les données");
            }

            // Récupération de la date de départ de la décision du courant
            REDecisionEntity decCourant = (REDecisionEntity) m.getFirstEntity();
            dateDecisionCourant = new JADate(decCourant.getDecisionDepuis());

        }

        for (int i = 0; i < mgrBC.size(); i++) {
            REBasesCalcul bc = (REBasesCalcul) mgrBC.getEntity(i);

            RERenteAccordeeJoinInfoComptaJoinPrstDuesManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesManager();

            mgr.setSession(session);
            mgr.setForIdBaseCalcul(bc.getIdBasesCalcul());

            // Au moment de la préparation à la validation des décisions, l'état
            // des presations dues n'est pas encore activé.

            mgr.setForCsTypePrstDue(IREPrestationDue.CS_TYPE_PMT_MENS);

            if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(csTypeDecision)) {
                mgr.setForCsEtatPrstDue(IREPrestationDue.CS_ETAT_ATTENTE);
                mgr.setForCsEtatRA(IREPrestationAccordee.CS_ETAT_CALCULE);
                mgr.setUntilDateDebutPmt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(viewBean.getDecisionDepuis()));
            } else if (IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD.equals(csTypeDecision)) {
                mgr.setForCsEtatRA(IREPrestationAccordee.CS_ETAT_CALCULE);
            }

            // On ne prend que les $p jusqu'à la date de fin = date début Retro du courant - 1 mois.
            else if (IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO.equals(csTypeDecision)) {
                String dateDecisionCourantFormat = PRDateFormater
                        .convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDecisionCourant.toStrAMJ());
                String AAAAMM = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(cal.addMonths(
                        dateDecisionCourantFormat, -1));
                mgr.setUntilDateDebutPmt(AAAAMM);
            }

            // //Hack pour récupération du schéma de la DB...
            RERenteAccordeeJoinInfoComptaJoinPrstDues dummy = new RERenteAccordeeJoinInfoComptaJoinPrstDues();
            dummy.setSession(session);
            String schema = dummy.getCollection();
            dummy = null;

            // Le tri s'effectue par genre, idTiersAdressePmt + idDomaineApp, ref. interne
            mgr.setOrderBy(" " + schema + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES + "."
                    + REPrestationDue.FIELDNAME_CS_TYPE + ", " + schema
                    + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ", " + schema
                    + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA + "."
                    + REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT + ", " + schema
                    + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA + "."
                    + REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION + ", " + schema
                    + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_REFERENCE_PMT + " ");

            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            for (int j = 0; j < mgr.size(); j++) {
                RERenteAccordeeJoinInfoComptaJoinPrstDues elem = (RERenteAccordeeJoinInfoComptaJoinPrstDues) mgr
                        .getEntity(j);

                // En cas de RETRO pure, la fin de paiement du $p peut être erronée si entre la prise de décision du
                // courant
                // et la prise de décision du RETRO, il y a eu l'adaptation bis-anuelle.
                // Dans ce cas, la date finPaiement vaudra par exemple. 12.2010 alors que si la décision courante a été
                // validée
                // en 10.2010, cette date de fin de paiement doit valoir 09.2010 au lieu de 12.2010.
                if (dfRetroPure != null) {
                    JADate dfPmt = new JADate(elem.getFinPaiement());
                    if (cal.compare(dfRetroPure, dfPmt) == JACalendar.COMPARE_FIRSTLOWER) {
                        elem.setFinPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dfRetroPure.toStrAMJ()));
                    }
                }

                decisionContainer.addElement(session, elem, csTypeDecision, viewBean.getDecisionDepuis(),
                        bc.getIdBasesCalcul());
            }
        }
        viewBean.setDecisionContainer(decisionContainer);
    }

    /**
     * Cette méthode crée la prestation et les ordres de versements liés.
     * 
     * @param session
     * @param transaction
     * @param decisionEntity
     * @param pw
     * @param idsRADejaDiminuees
     * @param idTiersRequerant
     * @return idPrestation
     * @throws Exception
     */
    private REPrestations doTraitementOV(final BSession session, final BITransaction transaction,
            final REDecisionEntity decisionEntity, final PrestationWrapper pw, final String testRetenue,
            final DemandeRente demande, final String dateDernierPaiement) throws Exception {

        List<Long> idsRA = new ArrayList<Long>(pw.idsRATraitee);
        REBeneficiairePrincipalVO bp = REBeneficiairePrincipal.retrieveBeneficiairePrincipal(session, transaction,
                idsRA);

        // Création de la prestation...
        REPrestations prestation = new REPrestations();
        prestation.setSession(session);
        prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
        prestation.setIdDemandeRente(decisionEntity.getIdDemandeRente());
        prestation.setMoisAnnee(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(JACalendar.todayJJsMMsAAAA()));
        prestation.setIdDecision(decisionEntity.getId().toString());
        prestation.setMontantPrestation(pw.montantPrestation.toString());

        prestation.add(transaction);

        String idTiersAdresssePmt = bp.ra.loadInformationsComptabilite().getIdTiersAdressePmt();
        TIAdressePaiementData adr = PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                idTiersAdresssePmt, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                JACalendar.todayJJsMMsAAAA());

        if ((adr == null) || adr.isNew()) {
            throw new Exception(session.getLabel("ERREUR_AUCUNE_ADRESSE_PMT_TROUVE") + ""
                    + bp.ra.getIdPrestationAccordee() + "/" + idTiersAdresssePmt);
        }

        // Solde à verser au bénéficiaire principal :
        FWCurrency solde = new FWCurrency(pw.montantPrestation.toString());

        REOrdresVersements ovBeneficiairePrincipal = new REOrdresVersements();
        ovBeneficiairePrincipal.setSession(session);
        ovBeneficiairePrincipal.setIdPrestation(prestation.getIdPrestation());
        ovBeneficiairePrincipal.setCsType(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
        ovBeneficiairePrincipal.setIsCompense(Boolean.TRUE);
        ovBeneficiairePrincipal.setIsValide(Boolean.TRUE);
        ovBeneficiairePrincipal.setIdTiers(bp.idTiersBeneficiairePrincipal);
        ovBeneficiairePrincipal.setIdTiersAdressePmt(bp.ra.loadInformationsComptabilite().getIdTiersAdressePmt());
        ovBeneficiairePrincipal.setIdDomaineApplication(adr.getIdApplication());

        List<REOrdresVersements> ovsCrees = new ArrayList<REOrdresVersements>();

        // on ajoute les OV sont uniquement pour les décisions de type RETRO ou Standard (avec du rétro)
        if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decisionEntity.getCsTypeDecision())
                || IREDecision.CS_TYPE_DECISION_STANDARD.equals(decisionEntity.getCsTypeDecision())) {

            for (String key : pw.ordresVersementKeys()) {
                OrdreVersementWrapper ovw = pw.getOV(key);

                REOrdresVersements unOvDeLaPrestation = new REOrdresVersements();

                unOvDeLaPrestation.setCsType(ovw.csType);
                unOvDeLaPrestation.setMontant(ovw.montant.toString());
                unOvDeLaPrestation.setMontantDette(ovw.montant.toString());
                unOvDeLaPrestation.setIsCompense(Boolean.TRUE);
                unOvDeLaPrestation.setIdTiers(ovw.idTiers);
                unOvDeLaPrestation.setIdTiersAdressePmt(ovw.idTiersAdrPmt);
                unOvDeLaPrestation.setIdDomaineApplication(ovw.idDomaine);
                unOvDeLaPrestation.setIsValide(Boolean.TRUE);
                unOvDeLaPrestation.setIdPrestation(prestation.getIdPrestation());
                unOvDeLaPrestation.add(transaction);

                // Il faut mettre à jours la clé externe idOV dans la table des créances accordées.
                // Ceci uniquement pour les ov de type TIERS, IS et ASSURANCE SOCIALE
                if (!JadeStringUtil.isBlankOrZero(ovw.idCreanceAccordee)) {
                    RECreanceAccordee ca = new RECreanceAccordee();
                    ca.setSession(session);
                    ca.setIdCreanceAccordee(ovw.idCreanceAccordee);
                    ca.retrieve(transaction);
                    PRAssert.notIsNew(ca, null);

                    ca.setIdOrdreVersement(unOvDeLaPrestation.getIdOrdreVersement());
                    ca.update(transaction);
                }
                ovsCrees.add(unOvDeLaPrestation);
            }
            if (solde.isPositive()) {
                ovBeneficiairePrincipal.setMontant(pw.montantPrestation.toString());
            } else {
                ovBeneficiairePrincipal.setMontant("0.0");
            }

            // Ajouter OV de type compensation
            addDettesCompensations(session, transaction, demande.getRequerant().getId().toString(),
                    prestation.getIdPrestation(), new FWCurrency(prestation.getMontantPrestation()), ovsCrees);

            // Ajouter OV de type Dette interne, suite diminution de rente...
            addDetteRenteEnCours(session, transaction, decisionEntity, demande.getRequerant().getId().toString(),
                    prestation, pw.idsRATraitee, ovsCrees);

            FWCurrency cumulOV = new FWCurrency(ovBeneficiairePrincipal.getMontant());
            boolean creanciersPresents = false;

            for (REOrdresVersements unOv : ovsCrees) {
                switch (unOv.getCsTypeOrdreVersement()) {
                    case CREANCIER:
                    case ASSURANCE_SOCIALE:
                    case IMPOT_A_LA_SOURCE:
                        if (!new FWCurrency(unOv.getMontant()).isZero()) {
                            cumulOV.sub(unOv.getMontant());
                            creanciersPresents = true;
                        }
                        break;

                    case BENEFICIAIRE_PRINCIPAL:
                    case INTERET_MORATOIRE:
                        cumulOV.add(unOv.getMontant());
                        break;

                    case DETTE:
                    case DETTE_RENTE_AVANCES:
                    case DETTE_RENTE_DECISION:
                    case DETTE_RENTE_PRST_BLOQUE:
                    case DETTE_RENTE_RESTITUTION:
                    case DETTE_RENTE_RETOUR:
                    case DIMINUTION_DE_RENTE:
                        if ((unOv.getIsCompense() != null) && unOv.getIsCompense()) {
                            cumulOV.sub(unOv.getMontant());
                        }
                        break;

                    default:
                        break;
                }
            }

            // Le montant de la prestation est < que le total des OV (Créanciers)
            if ((cumulOV.compareTo(new FWCurrency("0.00")) < 0) && creanciersPresents) {
                throw new Exception(session.getLabel("ERREUR_MNT_TOT_CREANCIER_SUP_MNT_PREST"));
            }

        } else if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decisionEntity.getCsTypeDecision())) {
            if (solde.isPositive()) {
                ovBeneficiairePrincipal.setMontant(solde.toString());
            } else {
                ovBeneficiairePrincipal.setMontant("0.0");
            }
        }
        ovBeneficiairePrincipal.add(transaction);

        return prestation;
    }

    public FWViewBeanInterface enregistrerModifications(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            vb = decisionUtil.enregistrerModifs(vb, action, session, transaction);

            // --> Pour réaffichage

            // Si on est venu dans l'écran depuis la rcList des décisions (détail)

            // BZ 4947 - Suppression temporaire de la navigation entre décisions
            // if (vb.getIsDepuisRcListDecision().booleanValue()) {

            // chargement de la décision d'arrivée
            REDecisionEntity deci = new REDecisionEntity();
            deci.setIdDecision(vb.getIdDecision());
            deci.setSession(session);
            deci.retrieve();

            // Sauvegarde des remarques des écisions
            if (!deci.isNew()) {
                deci.setIsRemInteretMoratoires(vb.getIsInteretMoratoire());
                deci.setIsRemarqueRenteDeVeufLimitee(vb.getHasRemarqueRenteDeVeufLimitee());
                deci.setIsRemarqueRenteDeVeuveLimitee(vb.getHasRemarqueRenteDeVeuveLimitee());
                deci.setIsRemarqueRemariageRenteDeSurvivant(vb.getHasRemarqueRemariageRenteDeSurvivant());
                deci.setIsRemarqueRentePourEnfant(vb.getHasRemarqueRentesPourEnfants());
                deci.setIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande(vb
                        .getHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot());
                deci.setIsRemSuppVeuf(vb.getIsRemSuppVeuf());
                deci.setIsRemarqueRenteAvecMontantMinimumMajoreInvalidite(vb
                        .getHasRemarqueRenteAvecMontantMinimumMajoreInvalidite());
                deci.setIsRemarqueRenteReduitePourSurassurance(vb.getHasRemarqueRenteReduitePourSurassurance());
                deci.update(transaction);
            }

            REDecisionsContainer decisionContainer = new REDecisionsContainer();

            String idDemandeRente = deci.getIdDemandeRente();
            REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
            dem.setSession(session);
            dem.setIdDemandeRente(idDemandeRente);
            dem.retrieve(transaction);

            vb.setIdTiersRequerant(dem.getIdTiersRequerant());
            vb.setIdDemandeRente(dem.getIdDemandeRente());

            decisionContainer.loadDecision(session, deci);
            decisionContainer.parcourDecisionsIC(session);
            decisionContainer.setIdDemandeRente(deci.getIdDemandeRente());
            vb.setDecisionContainer(decisionContainer);

            if (!deci.getIdDecision().equals(vb.getIdDecision())) {
                decisionContainer.init();
            }

            vb.setDecisionContainer(vb.getDecisionContainer());

            vb.getDecisionContainer().getDecisionIC().setCopiesListDIC(vb.getLstCopie());
            vb.getDecisionContainer().getDecisionIC().setAnnexesListDIC(vb.getLstAnnexe());
            vb.getDecisionContainer().getDecisionIC().setRemarqueDecisionDIC(vb.getRemarqueDecision());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
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
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return vb;
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

    private boolean isRenteVerseeATortPlusGrandEgalMontantRetroactifAPayer(DemandeRente demande, BSession session)
            throws Exception {

        BigDecimal montantRetroactif = demande.getMontantRetroactif();

        RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
        renteVerseeATortManager.setSession(session);
        renteVerseeATortManager.setForIdDemandeRente(demande.getId());
        renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

        BigDecimal montantRenteVerseeATort = BigDecimal.ZERO;
        for (int i = 0; i < renteVerseeATortManager.size(); i++) {
            RERenteVerseeATort renteVerseeATort = (RERenteVerseeATort) renteVerseeATortManager.getEntity(i);
            montantRenteVerseeATort = montantRenteVerseeATort.add(renteVerseeATort.getMontant());
        }

        return BigDecimal.ZERO.compareTo(montantRenteVerseeATort) != 0
                && montantRenteVerseeATort.compareTo(montantRetroactif) >= 0;

    }

    /**
     * Création des décisions dans l'état ATTENTE. Les décisions sont regroupées par RA et adr. pmt
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface genererDecision(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        BTransaction transaction = null;
        Set<Long> idsDecision = new HashSet<Long>();

        try {

            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;
            validate(vb, session, transaction);

            String idDemandeRente = vb.getIdDemandeRente();

            DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                    Long.parseLong(idDemandeRente));

            REPreparerDecisionSpecifiqueHelper.verifierQuAucuneRenteDesBeneficiairesDeLaDemandeNeSoitBloquee(demande);

            if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equalsIgnoreCase(vb.getCsTypePreparationDecision())) {
                if (isRenteVerseeATortPlusGrandEgalMontantRetroactifAPayer(demande, session)) {
                    throw new REBusinessException(
                            session.getLabel("PREVALIDATION_DECISION_COURANT_ERREUR_RENTE_VERSEE_A_TORT_PLUS_GRAND_EGAL_MONTANT_RETROACTIF"));
                }

            }

            String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(session);

            boolean comporteDesRentesAccordeesFutures = demande
                    .comporteDesRentesAccordeesCommencantApresCeMois(dateDernierPaiement);
            boolean comporteDesRentesAccordeesRetro = demande
                    .comporteDesRentesAccordeesCommencantAvantCeMois(dateDernierPaiement);
            boolean comporteDesRentesAccordeesCommencantDansLeMois = demande
                    .comporteDesRentesAccordeesCommencantDansCeMois(dateDernierPaiement);

            /*
             * S'il y a mélange de rentes rétro (rentes commençant avant ou pendant le mois) et de rentes futures
             * (commençant après le mois), on doit lever une exception pour empêcher de poursuivre
             */
            if ((comporteDesRentesAccordeesCommencantDansLeMois || comporteDesRentesAccordeesRetro)
                    && comporteDesRentesAccordeesFutures) {
                throw new REBusinessException(
                        session.getLabel("ERREUR_PREPARATION_DECISION_COMPORTANT_RENTES_FUTURES_ET_ANCIENNES"));
            }

            /*
             * Si on veut préparer une décision courante, il ne peut pas y avoir de rentes commençant dans le futur
             */
            if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(vb.getCsTypePreparationDecision())
                    && comporteDesRentesAccordeesFutures) {
                throw new REBusinessException(
                        session.getLabel("ERREUR_PREPARATION_DECISION_COURANTE_COMPORTANT_RENTES_FUTURES"));
            }

            /*
             * Test si :
             * - La préparation de la décision est de type COURANT ()
             * - Si c'est le cas :
             * -- On additionne le montant de toutes les rentes accordées de la demande
             * -- On additionne le montant de toutes les rentes versées à tort de la demande
             * Si le montant totale des rentes versées à tort est égal ou supérieur au montant des rentes accordées -->
             * Erreur !
             */
            TypeTraitementDecisionRente typePreparationDecision = TypeTraitementDecisionRente.parse(vb
                    .getCsTypePreparationDecision());
            if (typePreparationDecision == null) {
                String message = session
                        .getLabel("PREPARER_DECISION_ERREUR_CODE_SYSTEM_TYPE_PREPARATION_DECISION_INCONNU");
                message = message.replace("{0}", vb.getCsTypePreparationDecision());
                throw new REBusinessException(message);
            }
            controleMontantRentesVerseesATort(session, typePreparationDecision, demande);

            /*
             * Si une ou plusieurs rentes en cours pour la famille du requérant, et qu'on veut prendre un décision
             * courant-validée avec comme mois de début le mois courant (donc on veut payer un mois par cette décision)
             * on arrête le processus car les utilisateurs doivent d'abord diminuer les rentes en cours à la main avant
             * de pouvoir prendre cette décision courant-validée
             */
            Set<RenteAccordee> renteNecessitantUneDiminutionApresValidation = CorvusServiceLocator
                    .getRenteAccordeeService().rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(demande);
            if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(vb.getCsTypePreparationDecision())
                    && dateDernierPaiement.equals(vb.getDecisionDepuis())
                    && (renteNecessitantUneDiminutionApresValidation.size() > 0)) {
                throw new REBusinessException(
                        session.getLabel("ERREUR_PREPARATION_DECISION_COURANT_VALIDE_AVEC_RENTES_NECESSITANT_DIMINUTION"));
            }

            vb.setRequerantInfo(getRequerantInfo(session, demande));
            vb.setIdTiersRequerant(demande.getRequerant().getId().toString());

            doTraitement(session, transaction, demande, vb, vb.getCsTypePreparationDecision());

            // Si aucun dic trouvé pour du courant, signifie que les RA de la demande courante partent dans le futur !!!
            // On interdi donc ce choix.
            if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(vb.getCsTypePreparationDecision())
                    && (vb.getDecisionContainer().getDecisionIC() == null)) {
                throw new Exception(session.getLabel("ERREUR_PAS_RETRO_DEM"));
            }

            // Suppression de toutes les décisions en état non validé de la demande
            Set<String> etat = new HashSet<String>();
            etat.add(IREDecision.CS_ETAT_ATTENTE);
            etat.add(IREDecision.CS_ETAT_PREVALIDE);

            REDecisionsManager decisionsManager = new REDecisionsManager();
            decisionsManager.setSession(session);
            decisionsManager.setForIdDemandeRente(idDemandeRente);
            decisionsManager.setForCsEtatIn(etat);
            decisionsManager.find(transaction, BManager.SIZE_NOLIMIT);
            for (Object o : decisionsManager.getContainer()) {
                REDecisionEntity decision = (REDecisionEntity) o;
                REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit(session, transaction,
                        decision, IREValidationLevel.VALIDATION_LEVEL_NONE);
            }

            REDecisionsContainer decisionsContainer = vb.getDecisionContainer();
            decisionsContainer.init();

            List<String> idsPrestationsAvecOV = new ArrayList<String>();
            Set<Long> idsRADejaDiminuees = new HashSet<Long>();

            while (decisionsContainer.getDecisionIC() != null) {

                boolean hasInteretMoratoires = false;
                REDecisionInfoContainer dic = decisionsContainer.getDecisionIC();

                REDecisionEntity decisionEntity = new REDecisionEntity();
                decisionEntity.setSession(session);
                decisionEntity.setAlternateKey(REDecisionEntity.ALTERNATE_KEY_UID);
                decisionEntity.setUid(dic.getUID());
                decisionEntity.retrieve(transaction);

                // On a supprimer toutes les décisions avant d'arriver ici, les seules qu'on à laissée
                // sont les décision en état validées qu'on ne veut surtout pas toucher.
                if (IREDecision.CS_ETAT_VALIDE.equals(decisionEntity.getCsEtat())) {
                    continue;
                }

                Set<Long> idsRA = dic.getIdsPrstDuesParIdsRA().keySet();

                decisionEntity = new REDecisionEntity();
                decisionEntity.setSession(session);
                decisionEntity.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
                decisionEntity.setCsTypeDecision(vb.getCsTypePreparationDecision());
                decisionEntity.setIdDemandeRente(vb.getIdDemandeRente());
                decisionEntity.setDateValidation(null);
                decisionEntity.setUid(dic.getUID());
                decisionEntity.setAdresseEMail(vb.getEMailAddress());
                decisionEntity.setDateDecision(vb.getDateDecision());
                decisionEntity.setDatePreparation(JACalendar.todayJJsMMsAAAA());
                decisionEntity.setValidePar(null);
                decisionEntity.setPreparePar(session.getUserId());
                decisionEntity.setTraitePar(session.getUserId());
                decisionEntity.setCsGenreDecision(IREDecision.CS_GENRE_DECISION_DECISION);

                // On met a jour la décision avec l'id tiers bénéficiaire principal !!!
                REBeneficiairePrincipalVO bp = REBeneficiairePrincipal.retrieveBeneficiairePrincipal(session,
                        transaction, idsRA);
                decisionEntity.setIdTiersBeneficiairePrincipal(bp.getIdTiersBeneficiairePrincipal());
                decisionEntity.setIdTiersAdrCourrier(bp.getIdTiersBeneficiairePrincipal());

                // Gestion des périodes rétro
                Periode periodeRetro = null;
                switch (TypeTraitementDecisionRente.parse(vb.getCsTypePreparationDecision())) {

                    case RETRO:
                        // la borne max est le mois précédant le mois à laquelle la décision courante a démarré (date
                        // décision depuis)

                        REDecisionsManager decisionsCourantValideeManager = new REDecisionsManager();
                        decisionsCourantValideeManager.setSession(session);
                        decisionsCourantValideeManager.setForIdDemandeRente(idDemandeRente);
                        decisionsCourantValideeManager.setForCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
                        decisionsCourantValideeManager.setForCsEtat(IREDecision.CS_ETAT_VALIDE);
                        decisionsCourantValideeManager.find(transaction);

                        if (decisionsCourantValideeManager.size() == 0) {
                            throw new RETechnicalException(
                                    session.getLabel("ERREUR_DECISION_COURANT_VALIDEE_INTROUVABLE"));
                        }

                        REDecisionEntity decisionCourantValidee = (REDecisionEntity) decisionsCourantValideeManager
                                .getFirstEntity();

                        periodeRetro = REPreValiderDecisionHelper.retrievePeriodeRetro(
                                demande,
                                JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                                        "01." + decisionCourantValidee.getDecisionDepuis(), -1)));
                        break;

                    case STANDARD:
                        periodeRetro = REPreValiderDecisionHelper.retrievePeriodeRetro(demande, dateDernierPaiement);
                        break;

                    case COURANT:
                        periodeRetro = null;

                        decisionEntity.setDecisionDepuis(vb.getDecisionDepuis());
                        break;
                }

                if (periodeRetro != null) {
                    decisionEntity.setDateDebutRetro(periodeRetro.getDateDebut());
                    decisionEntity.setDateFinRetro(periodeRetro.getDateFin());
                }

                // gestion des cotisations
                switch (demande.getTypeDemandeRente()) {
                    case DEMANDE_SURVIVANT:
                    case DEMANDE_INVALIDITE:
                    case DEMANDE_VIEILLESSE:
                        decisionEntity.setIsObliPayerCoti(Boolean.TRUE);
                        break;
                    case DEMANDE_API:
                        decisionEntity.setIsObliPayerCoti(Boolean.FALSE);
                        break;
                }

                String cuid = getCuid(session, transaction, decisionEntity.getIdTiersBeneficiairePrincipal());
                decisionEntity.setCuid(cuid);
                decisionEntity.add(transaction);

                idsDecision.add(Long.parseLong(decisionEntity.getIdDecision()));

                /*
                 * Si une décision est prise pour une ou plusieurs rentes complémentaires sans aucune rente principale
                 * dans cette décision, il faut vérifier si la rente principale est ou a été envoyée à la même adresse,
                 * si ce n'est pas le cas, mettre l'adresse de la rente principale automatiquement dans les copies avant
                 * d'afficher l'écran "PRE2002 Décision"
                 */
                String idRaPourRequ = "";
                boolean isRenteComplementaire = false;
                boolean isRentePrincipale = false;
                boolean isRetro = IREDecision.CS_TYPE_DECISION_RETRO.equals(decisionEntity.getCsTypeDecision());
                boolean isRenteAIPrincipale = false;
                for (Long idRA : idsRA) {
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(session);
                    ra.setIdPrestationAccordee(idRA.toString());
                    ra.retrieve();

                    String codePrestationRA = ra.getCodePrestation();

                    // Si une décision est prise pour une ou plusieurs rentes complémentaires
                    if (codePrestationRA.equals("14") || codePrestationRA.equals("15") || codePrestationRA.equals("16")
                            || codePrestationRA.equals("24") || codePrestationRA.equals("25")
                            || codePrestationRA.equals("26") || codePrestationRA.equals("33")
                            || codePrestationRA.equals("34") || codePrestationRA.equals("35")
                            || codePrestationRA.equals("36") || codePrestationRA.equals("45")
                            || codePrestationRA.equals("53") || codePrestationRA.equals("54")
                            || codePrestationRA.equals("55") || codePrestationRA.equals("73")
                            || codePrestationRA.equals("74") || codePrestationRA.equals("75")) {

                        isRenteComplementaire = true;
                        idRaPourRequ = ra.getIdPrestationAccordee();

                        // Si il y a une rente principale
                    } else if (codePrestationRA.equals("10") || codePrestationRA.equals("13")
                            || codePrestationRA.equals("20") || codePrestationRA.equals("23")
                            || codePrestationRA.equals("50") || codePrestationRA.equals("40")
                            || codePrestationRA.equals("70")) {

                        isRentePrincipale = true;
                    }
                    if (!JadeStringUtil.isBlank(codePrestationRA)) {
                        switch (Integer.parseInt(codePrestationRA)) {
                            case 50:
                            case 70:
                                isRenteAIPrincipale = true;
                                break;
                        }
                    }

                    // Inforom 500 : recherche s'il y à des intérêts à payer au tiers. Le but est de
                    if (!hasInteretMoratoires) {
                        hasInteretMoratoires = gererInteretMoratoire(session, transaction, hasInteretMoratoires, ra);
                    }
                }

                // Est-ce la décision est du rétro ? (ne pas afficher les bases de calcul dans les cas de rétro)
                boolean isDecisionRetro = IREDecision.CS_TYPE_DECISION_RETRO.equals(decisionEntity.getCsTypeDecision());

                // On ajoute les copies par défaut pour le tiers considéré.....
                CTCopiesManager mgr = new CTCopiesManager();
                mgr.setSession(session);
                mgr.setForIdTiersRequerant(decisionEntity.getIdTiersBeneficiairePrincipal());
                mgr.setFromDateDebut(JACalendar.todayJJsMMsAAAA());
                mgr.setToDateFin(JACalendar.todayJJsMMsAAAA());
                mgr.find();

                for (int i = 0; i < mgr.size(); i++) {
                    CTCopies cop = (CTCopies) mgr.getEntity(i);

                    RECopieDecision copieDec = new RECopieDecision();
                    copieDec.setSession(session);
                    copieDec.setIdDecision(decisionEntity.getIdDecision());
                    copieDec.setIsDecompte(Boolean.TRUE);
                    copieDec.setIsRemarques(Boolean.TRUE);
                    copieDec.setIsSignature(Boolean.TRUE);
                    copieDec.setIsCopies(Boolean.TRUE);
                    copieDec.setIsVersementA(Boolean.TRUE);
                    copieDec.setIsBaseCalcul(!isDecisionRetro);
                    copieDec.setIsMoyensDroit(Boolean.TRUE);
                    copieDec.setIsAnnexes(Boolean.TRUE);
                    copieDec.setIsPageGarde(Boolean.TRUE);
                    copieDec.setIdTiersCopie(cop.getIdTiersCopieA());

                    // BZ 5536, ajout de l'ID de la copie (procédure de communication) dans la copie de décision
                    // pour pouvoir récupérer ultérieurement la remarque
                    copieDec.setIdProcedureCommunication(cop.getIdCopie());

                    copieDec.add(transaction);
                }

                // Si rentes complémentaires et aucune rente principale
                if (isRenteComplementaire && !isRentePrincipale) {

                    // Retrouver la rente principale (idTierRequerant de la bc)
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(session);
                    ra.setIdPrestationAccordee(idRaPourRequ);
                    ra.retrieve();

                    // Si idTiersBaseCalcul différent de idTiersAdressePmt, ajouter la copie pour idTiersBaseCalcul
                    REInformationsComptabilite infoCompta = new REInformationsComptabilite();
                    infoCompta.setSession(session);
                    infoCompta.setIdInfoCompta(ra.getIdInfoCompta());
                    infoCompta.retrieve();

                    if (!ra.getIdTiersBaseCalcul().equals(infoCompta.getIdTiersAdressePmt())) {

                        RECopieDecision copieDec = new RECopieDecision();
                        copieDec.setSession(session);
                        copieDec.setIdDecision(decisionEntity.getIdDecision());
                        copieDec.setIdTiersCopie(ra.getIdTiersBaseCalcul());
                        copieDec.setIsVersementA(Boolean.TRUE);
                        copieDec.setIsBaseCalcul(!isDecisionRetro);
                        copieDec.setIsDecompte(Boolean.TRUE);
                        copieDec.setIsRemarques(Boolean.TRUE);
                        copieDec.setIsMoyensDroit(Boolean.TRUE);
                        copieDec.setIsSignature(Boolean.TRUE);
                        copieDec.setIsAnnexes(Boolean.TRUE);
                        copieDec.setIsCopies(Boolean.TRUE);
                        copieDec.setIsPageGarde(Boolean.TRUE);
                        copieDec.add(transaction);
                    }
                }
                // Lors de la création de la décision, il faut ajouter tous les créanciers dans les copies,
                RECreancierManager creMgr = new RECreancierManager();
                creMgr.setSession(session);
                creMgr.setForIdDemandeRente(demande.getId().toString());
                creMgr.find(transaction);

                for (RECreancier cre : creMgr.getContainerAsList()) {

                    // sauf les types impôts à la source
                    if (!cre.getCsType().equals(IRECreancier.CS_IMPOT_SOURCE)) {

                        // Si le montant revendiqué est à zéro, ne pas mettre dans les copies
                        if (!(new FWCurrency(cre.getMontantRevandique()).isZero())) {

                            // si l'idtiers regroupement n'est pas vide, contrôler qui s'agisse du même que celui de
                            // la décision
                            // s'il est vide, pas de pb
                            if ((!JadeStringUtil.isBlankOrZero(cre.getIdTiersRegroupement()) && cre
                                    .getIdTiersRegroupement().equals(decisionEntity.getIdTiersBeneficiairePrincipal()))
                                    || JadeStringUtil.isBlankOrZero(cre.getIdTiersRegroupement())) {

                                RECopieDecision copieDec = new RECopieDecision();
                                copieDec.setSession(session);
                                copieDec.setIdDecision(decisionEntity.getIdDecision());
                                copieDec.setIdAffilie(cre.getIdAffilieAdressePmt());
                                copieDec.setIsDecompte(Boolean.TRUE);
                                copieDec.setIsRemarques(Boolean.TRUE);
                                copieDec.setIsSignature(Boolean.TRUE);
                                copieDec.setIsCopies(Boolean.TRUE);

                                if (cre.getCsType().equals(IRECreancier.CS_TIERS)) {
                                    copieDec.setIsVersementA(Boolean.FALSE);
                                    copieDec.setIsBaseCalcul(Boolean.FALSE);
                                    copieDec.setIsMoyensDroit(Boolean.FALSE);
                                    copieDec.setIsAnnexes(Boolean.FALSE);
                                } else {
                                    copieDec.setIsVersementA(Boolean.TRUE);
                                    copieDec.setIsBaseCalcul(!isDecisionRetro);
                                    copieDec.setIsMoyensDroit(Boolean.TRUE);
                                    copieDec.setIsAnnexes(Boolean.TRUE);
                                }
                                copieDec.setIsPageGarde(Boolean.TRUE);
                                copieDec.setIdTiersCopie(cre.getIdTiers());
                                copieDec.add(transaction);
                            }
                        }
                    }
                }

                boolean isIdTiersBCEqualsIdTiersReqDemande = false;

                // Ajouter les copies par défaut selon properties et autres....
                String typeDecision = "";

                for (Long idRa : idsRA) {

                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(session);
                    ra.setIdPrestationAccordee(idRa.toString());
                    ra.retrieve(transaction);
                    int genrePrestation = Integer.parseInt(ra.getCodePrestation());

                    // AI
                    if ((genrePrestation >= 50) && (genrePrestation <= 59)) {
                        typeDecision = "INV-ORD";
                    } else if ((genrePrestation >= 70) && (genrePrestation <= 79)) {
                        typeDecision = "INV-EXT";
                        // AVS
                    } else if (((genrePrestation >= 10) && (genrePrestation <= 19))
                            || ((genrePrestation >= 30) && (genrePrestation <= 39))) {
                        typeDecision = "AVS-ORD";
                    } else if (((genrePrestation >= 20) && (genrePrestation <= 29))
                            || ((genrePrestation >= 40) && (genrePrestation <= 49))) {
                        typeDecision = "AVS-EXT";
                        // API
                    } else if (((genrePrestation >= 85) && (genrePrestation <= 87))
                            || ((genrePrestation >= 94) && (genrePrestation <= 97)) || (genrePrestation == 89)) {
                        typeDecision = "API-AVS";
                    } else {
                        typeDecision = "API-INV";
                    }

                    if (ra.getIdTiersBaseCalcul().equals(demande.getRequerant().getId().toString())) {
                        isIdTiersBCEqualsIdTiersReqDemande = true;
                    }

                }

                if (typeDecision.equals("API-AVS")) {
                    creerCopiePourOfficeAI(session, transaction, demande, decisionEntity, isRetro, isDecisionRetro,
                            typeDecision);
                }

                // Copie caisse
                // Chargement des nouvelles copies selon décision originale (la propriété permet d'imprimer ou non
                // la copie
                // sera utile pour l'envoi à la ged... a voir plus tard
                if (typeDecision.startsWith("INV") || (typeDecision.startsWith("API") && typeDecision.endsWith("INV"))) {
                    creerCopiePourLaCaisse(session, transaction, demande, decisionEntity, isRetro, isDecisionRetro,
                            isIdTiersBCEqualsIdTiersReqDemande, typeDecision);
                }

                StringBuffer warningCopy = vb.getAvertissementCopie();

                // Copie agence communale
                String propertiesCopieAgenceComm = session.getApplication().getProperty("isCopieAgenceCommunale");
                if ("true".equals(propertiesCopieAgenceComm)) {
                    // Trouver l'idTiers de l'agence communale par rapport au bénéficiaire principal
                    creerCopiePourAgenceCommunale(session, transaction, demande, decisionEntity, isRetro,
                            isDecisionRetro, typeDecision, warningCopy);
                }

                // Copie FISC
                if (typeDecision.startsWith("INV")) {
                    creerCopiePourLeFisc(session, transaction, decisionEntity, idRaPourRequ, warningCopy);
                }

                // /////////////////
                // BZ6836
                PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersAdresseDomicileParId(session,
                        bp.getIdTiersBeneficiairePrincipal());
                // Pas de bol onl'a pas trouvé. On va voir dans les rentes accordées de la décision courant
                // si il y a une rente principale
                if (tiersBeneficiaire == null) {
                    Set<Long> idRAs = dic.getIdsPrstDuesParIdsRA().keySet();

                    if (idRAs.size() > 0) {
                        RERenteAccordeeManager renteAccordeeManager = new RERenteAccordeeManager();
                        renteAccordeeManager.setSession(session);
                        String listeIdRas = "";
                        Iterator<Long> itor = idRAs.iterator();
                        while (itor.hasNext()) {
                            listeIdRas += itor.next();
                            if (itor.hasNext()) {
                                listeIdRas += ", ";
                            }
                        }
                        renteAccordeeManager.setForIdsRentesAccordees(listeIdRas);
                        renteAccordeeManager.setSession(session);
                        renteAccordeeManager.setForIdTiersBeneficiaire(bp.getIdTiersBeneficiairePrincipal());
                        renteAccordeeManager.find();

                        boolean hasRentePrincipale = false;
                        for (RERenteAccordee renteAcc : renteAccordeeManager.getContainerAsList()) {
                            if ("10".equals(renteAcc.getCodePrestation()) || "13".equals(renteAcc.getCodePrestation())
                                    || "20".equals(renteAcc.getCodePrestation())
                                    || "23".equals(renteAcc.getCodePrestation())
                                    || "10".equals(renteAcc.getCodePrestation())
                                    || "50".equals(renteAcc.getCodePrestation())
                                    || "70".equals(renteAcc.getCodePrestation())) {
                                hasRentePrincipale = true;
                            }
                        }

                        // Si il y a une rente principale accordée on s'arrête la..500
                        if (hasRentePrincipale) {
                            throw new Exception(session.getLabel("AUCUNE_ADRESSE_TROUVE_BENEFICIAIRE_PRINCIPALE"));
                        } else {
                            Iterator<RERenteAccordee> iterator = renteAccordeeManager.getContainerAsList().iterator();
                            while (iterator.hasNext() && (tiersBeneficiaire == null)) {
                                RERenteAccordee renteAcc = iterator.next();
                                String idTiersAdressePmt = renteAcc.loadInformationsComptabilite()
                                        .getIdTiersAdressePmt();
                                tiersBeneficiaire = PRTiersHelper.getTiersAdresseDomicileParId(session,
                                        idTiersAdressePmt);
                            }

                            if (tiersBeneficiaire == null) {
                                throw new Exception(session.getLabel("AUCUNE_ADRESSE_TROUVE_BENEFICIAIRE_PRINCIPALE"));
                            }

                        }
                    } else {
                        throw new Exception(session.getLabel("AUCUNE_ADRESSE_TROUVE_BENEFICIAIRE_PRINCIPALE"));
                    }
                }
                // /////////////////

                // InfoRom 329 : copie aux affaires militaires si le tiers (masculin, de nationalité suisse) va
                // bénéficier d'une rente AI principale et a entre 20 et 34 ans
                // PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersAdresseDomicileParId(session,
                // vb.getIdTiersRequerant(), JACalendar.todayJJsMMsAAAA());
                if ((tiersBeneficiaire != null)
                        && ITIPersonne.CS_HOMME.equals(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_SEXE))
                        && IConstantes.ID_PAYS_SUISSE.equals(tiersBeneficiaire
                                .getProperty(PRTiersWrapper.PROPERTY_ID_PAYS)) && isRenteAIPrincipale) {
                    creerCopiePourAffaireMilitaire(session, transaction, decisionEntity, tiersBeneficiaire);
                }

                vb.setAvertissementCopie(warningCopy);

                dic.setIdDecision(decisionEntity.getIdDecision());
                Set<Long> keys = null;

                if ((dic != null) && (decisionEntity != null)) {
                    keys = dic.getIdsPrstDuesParIdsRA().keySet();
                }

                if (keys != null) {
                    PrestationWrapper pw = new PrestationWrapper();

                    Set<Long> beneficiairesDeCetteDecision = new HashSet<Long>();
                    boolean isRetroPur = true;

                    for (Long idRA : keys) {
                        // Essai de récupération de la la RA.
                        RERenteAccordee ra = new RERenteAccordee();
                        ra.setSession(session);
                        ra.setIdPrestationAccordee(idRA.toString());
                        ra.retrieve(transaction);

                        PRAssert.notIsNew(ra, null);

                        Long idTiersBeneficaire = Long.parseLong(ra.getIdTiersBeneficiaire());
                        beneficiairesDeCetteDecision.add(idTiersBeneficaire);

                        isRetroPur &= !JadeStringUtil.isBlankOrZero(ra.getDateFinDroit());

                        for (Long idPrstDue : dic.getListIdsPrst(idRA)) {

                            REPrestationDue prstD = new REPrestationDue();
                            prstD.setSession(session);
                            prstD.setIdPrestationDue(idPrstDue.toString());
                            prstD.retrieve(transaction);
                            PRAssert.notIsNew(prstD, "Prestation due not found");

                            // Il y a du rétro, On créé la prestation
                            if (isRetro(decisionEntity)) {
                                // Création de l'objet prestation wrapper

                                // Le container de decisions ne contient que les infos des RA et PD de type $p.
                                pw = addPrestation(session, transaction, pw, new JADate(periodeRetro.getDateDebut()),
                                        new JADate(periodeRetro.getDateFin()), vb.getIdDemandeRente(), ra, prstD);
                            }

                            REValidationDecisions validR = new REValidationDecisions();
                            validR.setSession(session);
                            validR.setIdPrestationDue(prstD.getIdPrestationDue());
                            validR.setIdDecision(decisionEntity.getIdDecision());
                            validR.add(transaction);
                        }
                    }
                    REPrestations nouvellePrestation = null;
                    if (isRetro(decisionEntity)) {
                        nouvellePrestation = doTraitementOV(session, transaction, decisionEntity, pw,
                                vb.getTestRetenue(), demande, dateDernierPaiement);
                        idsPrestationsAvecOV.add(nouvellePrestation.getIdPrestation());
                    } else {
                        // Pas de rétro pour la décision
                        // On créé cependant une prestation, sans aucun OV.
                        if ((dic != null) && (decisionEntity != null)) {
                            nouvellePrestation = addPrestationSansOV(session, transaction, decisionEntity, dic
                                    .getIdsPrstDuesParIdsRA().keySet(), demande, dateDernierPaiement);
                        }
                    }

                    if (!IREDecision.CS_TYPE_DECISION_RETRO.equals(decisionEntity.getCsTypeDecision())
                            && (nouvellePrestation != null) && !isRetroPur) {
                        creerOrdresVersementDiminutionRenteFamille(transaction, demande,
                                Long.parseLong(nouvellePrestation.getIdPrestation()), decisionEntity,
                                dateDernierPaiement, keys, idsRADejaDiminuees, beneficiairesDeCetteDecision);
                    }
                }

                if (tiersBeneficiaire == null) {
                    tiersBeneficiaire = PRTiersHelper.getTiersParId(session, vb.getIdTiersRequerant());
                }

                CatalogueText catalogueTexteDecision = new CatalogueText();
                // BZ 6480
                if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(tiersBeneficiaire
                        .getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {
                    catalogueTexteDecision.setCodeIsoLangue("fr");
                } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(tiersBeneficiaire
                        .getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {
                    catalogueTexteDecision.setCodeIsoLangue("de");
                } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(tiersBeneficiaire
                        .getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {
                    catalogueTexteDecision.setCodeIsoLangue("it");
                } else if (IConstantes.CS_TIERS_LANGUE_ROMANCHE.equals(tiersBeneficiaire
                        .getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {
                    catalogueTexteDecision.setCodeIsoLangue("rm");
                }
                catalogueTexteDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
                catalogueTexteDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
                catalogueTexteDecision.setNomCatalogue("openOffice");

                BabelContainer conteneurCatalogues = new BabelContainer();
                conteneurCatalogues.setSession(session);
                conteneurCatalogues.addCatalogueText(catalogueTexteDecision);
                conteneurCatalogues.load();

                // BZ 5987, ajout d'un annexe par défaut
                REAnnexeDecision annexeMementoPC = new REAnnexeDecision();
                annexeMementoPC.setSession(session);
                annexeMementoPC.setIdDecision(decisionEntity.getIdDecision());
                annexeMementoPC.setLibelle(conteneurCatalogues.getTexte(catalogueTexteDecision, 8, 6));
                annexeMementoPC.add(transaction);

                decisionEntity.setIsRemInteretMoratoires(hasInteretMoratoires);
                decisionEntity.update(transaction);

                // Gestion des remarques de la décision. La décision doit déjà exister en base de données
                gererRemarqueDeLaDecision(session, transaction, demande, decisionEntity,
                        bp.getIdTiersBeneficiairePrincipal());

                if (decisionsContainer.hasDecisionSuivante()) {
                    decisionsContainer.decisionSuivante();
                } else {
                    // On sort de la boucle, il n'y a plus de décision.
                    break;
                }

            }

            // Compensation des Dettes de type Rentes en cours, inter décisions
            decisionUtil.doCompensationRECInterDecision(session, transaction, idsPrestationsAvecOV);
            // MAJ du montant a restituer, le cas échéant...

            decisionUtil.doMAJSoldePourRestitution(session, transaction, idsPrestationsAvecOV);

            decisionsContainer.init();
            decisionsContainer.parcourDecisionsIC(session);

            decisionsContainer.init();
            vb.setDecisionContainer(decisionsContainer);
            vb.setMessageRetenueSurRente(messageRetenueSurRente);
            // Inforom 500
            REDecisionEntity dd = new REDecisionEntity();
            dd.setSession(session);
            dd.setIdDecision(decisionsContainer.getDecisionIC().getIdDecision());
            dd.retrieve(transaction);

            vb.setIsInteretMoratoire(dd.getIsRemInteretMoratoires());
            vb.setHasRemarqueIncarceration(dd.getIsRemarqueIncarceration());
            vb.setHasRemarqueRenteDeVeufLimitee(dd.getIsRemarqueRenteDeVeufLimitee());
            vb.setHasRemarqueRenteDeVeuveLimitee(dd.getIsRemarqueRenteDeVeuveLimitee());
            vb.setHasRemarqueRemariageRenteDeSurvivant(dd.getIsRemarqueRemariageRenteDeSurvivant());
            vb.setHasRemarqueRentesPourEnfants(dd.getIsRemarqueRentePourEnfant());
            vb.setHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot(dd
                    .getIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande());
            vb.setHasRemarqueRenteAvecSupplementPourPersonneVeuve(dd.getIsRemSuppVeuf());
            vb.setHasRemarqueRenteAvecMontantMinimumMajoreInvalidite(dd
                    .getIsRemarqueRenteAvecMontantMinimumMajoreInvalidite());
            vb.setHasRemarqueRenteReduitePourSurassurance(dd.getIsRemarqueRenteReduitePourSurassurance());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            e.printStackTrace();
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || session.hasWarnings() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors() || transaction.hasWarnings()) {
                            viewBean.setMessage(transaction.getErrors().toString());
                            viewBean.setMsgType(transaction.hasErrors() ? FWViewBeanInterface.ERROR
                                    : FWViewBeanInterface.WARNING);
                        } else if (session.hasWarnings()) {
                            viewBean.setMessage(session.getWarnings().toString());
                            viewBean.setMsgType(FWViewBeanInterface.WARNING);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        if (FWViewBeanInterface.OK.equals(viewBean.getMsgType())) {
            DecisionService decisionService = CorvusServiceLocator.getDecisionService();
            for (Long unIdDecision : idsDecision) {
                decisionService.recalculerSoldePourRestitution(unIdDecision);
            }
        }

        return viewBean;
    }

    /**
     * 
     * Test si :
     * - La préparation de la décision est de type COURANT ()
     * - Si c'est le cas :
     * -- On additionne le montant de toutes les rentes accordées de la demande
     * -- On additionne le montant de toutes les rentes versées à tort de la demande
     * Si le montant total des rentes versées à tort est égal ou supérieur au montant des rentes accordées --> Erreur !
     * 
     * 
     * @param session La session courante
     * @param typeTraitementDecisionRente Le type de traitement de la décision
     * @param demande La demande de rente
     * @throws Exception En cas d'erreur d'accès à la persistence
     * @throws REBusinessException Si le montant totale des rentes versées à tort est égal ou supérieur au montant des
     *             rentes accordées
     */
    private void controleMontantRentesVerseesATort(final BSession session,
            TypeTraitementDecisionRente typeTraitementDecisionRente, DemandeRente demande) throws Exception,
            REBusinessException {
        if (TypeTraitementDecisionRente.COURANT.equals(typeTraitementDecisionRente)) {

            // Montant total des rentes accordées
            BigDecimal totalPrestationDue = new BigDecimal("0");
            for (RenteAccordee ra : demande.getRentesAccordees()) {
                for (PrestationDue pd : ra.getPrestationsDues()) {
                    if (pd.getType().equals(TypePrestationDue.MONTANT_RETROACTIF_TOTAL)) {
                        totalPrestationDue = totalPrestationDue.add(pd.getMontant());
                    }
                }
            }

            // Recherche des rentes versées à tort
            RERenteVerseeATortManager manager = new RERenteVerseeATortManager();
            manager.setSession(session);
            manager.setForIdDemandeRente(demande.getId());
            manager.find(BManager.SIZE_NOLIMIT);

            // Montant total des rentes versées à tort
            BigDecimal totalRentesVerseesATort = new BigDecimal("0");
            for (Object o : manager.getContainer()) {
                RERenteVerseeATort renteVerseeATort = (RERenteVerseeATort) o;
                if (renteVerseeATort.getMontant() != null) {
                    totalRentesVerseesATort = totalRentesVerseesATort.add(renteVerseeATort.getMontant());
                }
            }

            if (totalRentesVerseesATort.intValue() >= totalPrestationDue.intValue()) {
                throw new REBusinessException(
                        session.getLabel("PREPARER_DECISION_COURANT_IMPOSSIBLE_MONTANT_RVAT_SUPERIEUR_MONTANT_RA"));
            }
        }
    }

    /**
     * Inforom 500 : si il y à des intérêts moratoires il faut remonter l'info dans le viewBean afin de cocher la
     * checkBox 'Intérêts moratoire' On récupère quand même l'info depuis l'entité en db vu que l'utilisateur est
     * peut-être déjà venu sur cet écran
     * 
     * @param session
     * @param transaction
     * @param hasInteretMoratoires
     * @param ra
     * @return
     * @throws Exception
     */
    private boolean gererInteretMoratoire(final BSession session, final BITransaction transaction,
            boolean hasInteretMoratoires, final RERenteAccordee ra) throws Exception {
        String idCalculInteretMoratoire = ra.getIdCalculInteretMoratoire();
        if (!JadeStringUtil.isBlankOrZero(idCalculInteretMoratoire)) {
            RECalculInteretMoratoire calculIM = new RECalculInteretMoratoire();
            calculIM.setSession(session);
            calculIM.setIdCalculInteretMoratoire(idCalculInteretMoratoire);
            calculIM.retrieve(transaction);
            if (!calculIM.isNew() && !JadeStringUtil.isBlankOrZero(calculIM.getIdInteretMoratoire())) {
                REInteretMoratoire im = new REInteretMoratoire();
                im.setSession(session);
                im.setIdInteretMoratoire(calculIM.getIdInteretMoratoire());
                im.retrieve(transaction);
                if (!im.isNew() && !JadeStringUtil.isBlankOrZero(im.getMontantInteret())) {
                    hasInteretMoratoires = true;
                }
            }
        }
        return hasInteretMoratoires;
    }

    private void gererRemarqueAnnuleEtRemplaceDecisionPrecedente(final BSession session, final DemandeRente demande,
            final REDecisionEntity decision) throws Exception {

        REDemandeRenteJointDemandeManager demandeMgr = new REDemandeRenteJointDemandeManager();
        demandeMgr.setSession(session);
        demandeMgr.setForIdTiersRequ(decision.getIdTiersBeneficiairePrincipal());
        demandeMgr.find();

        if (!demandeMgr.isEmpty()) {
            for (int i = 0; i < demandeMgr.size(); i++) {
                REDemandeRenteJointDemande demandeEntity = (REDemandeRenteJointDemande) demandeMgr.get(i);

                // Voir si c'est la demande en cours
                if (!demandeEntity.getIdDemandeRente().equals(decision.getIdDemandeRente())) {
                    if (demandeEntity.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                        // Si demande en cours = API, ne traiter que des demandes validées API
                        if (demandeEntity.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)
                                && demandeEntity.getCsEtatDemande()
                                        .equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
                            decision.setIsRemAnnDeci(Boolean.TRUE);
                        }
                    } else {
                        if (!demandeEntity.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)
                                && demandeEntity.getCsEtatDemande()
                                        .equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
                            decision.setIsRemAnnDeci(Boolean.TRUE);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param session
     * @param transaction
     * @param decision
     * @throws Exception
     */
    private void gererRemarqueDeLaDecision(final BSession session, final BITransaction transaction,
            final DemandeRente demande, final REDecisionEntity decision, final String idTiersBeneficiairePrincipal)
            throws Exception {

        List<RERenteAccordee> renteAccordees = getRenteAccordeeDeLaDecision(session, transaction, decision);
        List<String> codePrestationDecision = new ArrayList<String>();
        if (renteAccordees != null) {
            for (REPrestationsAccordees prestation : getRenteAccordeeDeLaDecision(session, transaction, decision)) {
                if (!codePrestationDecision.contains(prestation.getCodePrestation())) {
                    codePrestationDecision.add(prestation.getCodePrestation());
                }
            }
        }

        decision.setRemarqueDecision("");
        decision.setIsRemRedPlaf(isCodeCasSpecial(renteAccordees, "05"));
        decision.setIsRemarqueRenteDeVeufLimitee(false);
        decision.setIsRemarqueRenteDeVeuveLimitee(false);
        decision.setIsRemarqueRemariageRenteDeSurvivant(false);
        decision.setIsRemarqueRentePourEnfant(false);
        decision.setIsRemarqueIncarceration(demande
                .comporteDesRentesAccordeesAvecCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_07));

        gererRemarqueAnnuleEtRemplaceDecisionPrecedente(session, demande, decision);

        /*
         * Lors de la remontée du calcul ACOR, la feuille de calcul a étée analysée pour voir si des remarques
         * particulières étaient présente. Si oui -> - création de l'entitée infoComplémentaire - renseignement des
         * champs concerné : -- WCBVLI (Demande possède des rentes limitées) -- WCBRSV (Montant comprenant un supplément
         * pour personne veuve) -- WCBDDA (Début de droit fixé à 5 ans avant la date de dépôt de la demande)
         */
        boolean isRenteLimitee = false;
        boolean isRemarqueRenteAvecSupplementPourPersonneVeuve = false;
        boolean isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande = false;
        boolean isRemarqueRenteAvecMontantMinimumMajoreInvalidite = false;
        boolean isRemarqueRenteReduitePourSurassurance = false;

        /**
         * les info compl peuvent ne pas exister en cas de saisie manuelle !!!
         */
        String idInfoComplementaire = demande.getInformationsComplementaires().getId().toString();
        if (!JadeStringUtil.isBlankOrZero(idInfoComplementaire)) {
            PRInfoCompl infoComplementaire = new PRInfoCompl();
            infoComplementaire.setSession(session);
            infoComplementaire.setIdInfoCompl(idInfoComplementaire);
            infoComplementaire.retrieve(transaction);

            if (!infoComplementaire.isNew()) {
                isRemarqueRenteAvecSupplementPourPersonneVeuve = Boolean.TRUE.equals(infoComplementaire
                        .getIsRenteAvecSupplementPourPersonneVeuve());

                isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande = Boolean.TRUE.equals(infoComplementaire
                        .getIsRenteAvecDebutDroit5AnsAvantDepotDemande());

                isRemarqueRenteAvecMontantMinimumMajoreInvalidite = Boolean.TRUE.equals(infoComplementaire
                        .getIsRenteAvecMontantMinimumMajoreInvalidite());

                isRemarqueRenteReduitePourSurassurance = Boolean.TRUE.equals(infoComplementaire
                        .getIsRenteReduitePourSurassurance());

                isRenteLimitee = Boolean.TRUE.equals(infoComplementaire.getIsRenteLimitee());
            }
        }

        decision.setIsRemSuppVeuf(isRemarqueRenteAvecSupplementPourPersonneVeuve);
        decision.setIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande(isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande);
        decision.setIsRemarqueRenteAvecMontantMinimumMajoreInvalidite(isRemarqueRenteAvecMontantMinimumMajoreInvalidite);
        decision.setIsRemarqueRenteReduitePourSurassurance(isRemarqueRenteReduitePourSurassurance);

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal);
        if (tiers == null) {
            String message = session.getLabel("CALCULER_DECISION_REMARQUE_ERROR_IMPOSSIBLE_TROUVER_TIERS");
            throw new Exception(message);
        }

        boolean isHomme = IConstantes.CS_PERSONNE_SEXE_HOMME.equals(tiers.getSexe());
        boolean isFemme = IConstantes.CS_PERSONNE_SEXE_FEMME.equals(tiers.getSexe());
        if (!isHomme && !isFemme) {
            String message = session.getLabel("CALCULER_DECISION_REMARQUE_ERROR_IMPOSSIBLE_TROUVER_SEXE");
            throw new Exception(message);
        }

        if (codePrestationDecision.contains(PRCodePrestationSurvivant._13.getCodePrestationAsString())) {
            // Remarque : rente de veuf (ve) s'éteint en cas de remariage
            decision.setIsRemarqueRemariageRenteDeSurvivant(true);

            // Rente de veuf limitée
            decision.setIsRemarqueRenteDeVeufLimitee(isHomme);

            // Rente de veuve limitée
            if (isRenteLimitee) {
                decision.setIsRemarqueRenteDeVeuveLimitee(isFemme);
            }
        }

        // Rentes pour enfants
        boolean isRentePourEnfant = false;
        int ctr = 0;

        while (!isRentePourEnfant && (ctr < codePrestationDecision.size())) {
            String codePrestation = codePrestationDecision.get(ctr);
            isRentePourEnfant = RECodePrestationResolver.isPrestationPourEnfant(codePrestation);
            ctr++;
        }
        decision.setIsRemarqueRentePourEnfant(isRentePourEnfant);

        decision.update(transaction);
    }

    private Boolean isCodeCasSpecial(List<RERenteAccordee> renteAccordees, String codeCasSpecial)
            throws IllegalArgumentException {
        if (JadeStringUtil.isBlankOrZero(codeCasSpecial)) {
            throw new IllegalArgumentException(
                    "REPreValiderDecisionHelper.isCodeCasSpecial(List<RERenteAccordee> renteAccordees, String codeCasSpecial) : The provided codeCasSpecial to search must not be empty !");
        }
        if (renteAccordees != null) {
            for (RERenteAccordee rente : renteAccordees) {
                if (codeCasSpecial.equals(rente.getCodeCasSpeciaux1())) {
                    return true;
                }
                if (codeCasSpecial.equals(rente.getCodeCasSpeciaux2())) {
                    return true;
                }
                if (codeCasSpecial.equals(rente.getCodeCasSpeciaux3())) {
                    return true;
                }
                if (codeCasSpecial.equals(rente.getCodeCasSpeciaux4())) {
                    return true;
                }
                if (codeCasSpecial.equals(rente.getCodeCasSpeciaux5())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param idTiers
     *            l'idTiers de la personne susceptible de devoir compenser des trucs
     * @param montant
     *            montant qu'on peut compenser
     * @return une Collection de CAPropositionCompensation
     * @throws Exception
     *             DOCUMENT ME!
     */
    private Collection<APISection> getCollectionSectionsACompenser(final BSession session, final String idTiers,
            final FWCurrency montant) throws Exception {
        BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");

        APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) sessionOsiris
                .getAPIFor(APIPropositionCompensation.class);
        FWCurrency montantTotalNegate = new FWCurrency(montant.toString());
        montantTotalNegate.negate();

        Collection<APISection> compensations = propositionCompensation.propositionCompensation(
                Integer.parseInt(idTiers), montantTotalNegate, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

        return compensations;
    }

    /**
     * Utilisé pour contrôlé si des changements au niveau des copies ont été opéré entre 2 génération de décision. Dans
     * l'affirmative, la nouvelle décision devra être entièrement recréée.
     * 
     * @param session
     * @param transaction
     * @param decision
     * @return liste des id de copie d'un tiers donnée. Format : idTiersCopieA/DOMAINE-idTiersCopieA/DOMAINE-... tris
     *         croissant
     * @throws Exception
     */
    public String getCuid(final BSession session, final BITransaction transaction, final String idTiersRequerant)
            throws Exception {
        CTCopiesManager mgr = new CTCopiesManager();
        mgr.setSession(session);
        mgr.setForIdTiersRequerant(idTiersRequerant);
        mgr.setFromDateDebut(JACalendar.todayJJsMMsAAAA());
        mgr.setToDateFin(JACalendar.todayJJsMMsAAAA());
        mgr.setOrderBy(CTCopies.FIELDNAME_ID_TIERS_COPIE_A + ", " + CTCopies.FIELDNAME_DOMAINE_APP_ADR_COPIE);
        mgr.find(transaction);

        String result = "";
        for (int i = 0; i < mgr.size(); i++) {
            CTCopies cop = (CTCopies) mgr.getEntity(i);
            if (i < (mgr.size() - 1)) {
                result += cop.getIdTiersCopieA() + "/" + cop.getDomaineAppAdrCopie() + "-";
            } else {
                result += cop.getIdTiersCopieA() + "/" + cop.getDomaineAppAdrCopie();
            }
        }
        return result;
    }

    /**
     * @param session
     * @param transaction
     * @param idTiers
     * @return Le montant mensuel de la RA du tiers passé en paramètre non encore validée. C'est à dire, la rente
     *         accordée dans l'état calculé ou partiel. Si plusieurs RA, on retournera en priorité celle n'étant pas de
     *         type API
     * @throws Exception
     */
    public FWCurrency getMontantRA(final BSession session, final BITransaction transaction, final String idTiers)
            throws Exception {
        return decisionUtil.getMontantRA(session, transaction, idTiers);
    }

    /**
     * Donne le type d'ordre de versement en fonction du type de creancier
     * 
     * @param string
     * @return
     */
    private String getOvTypeAccordingToCType(final String csCreanciertype) {

        if (IRECreancier.CS_ASSURANCE_SOCIALE.equals(csCreanciertype)) {
            return IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE;
        } else if (IRECreancier.CS_IMPOT_SOURCE.equals(csCreanciertype)) {
            return IREOrdresVersements.CS_TYPE_IMPOT_SOURCE;
        } else {
            return IREOrdresVersements.CS_TYPE_TIERS;
        }
    }

    private String getRequerantInfo(final BSession session, final DemandeRente demande) {
        PersonneAVS requerant = demande.getRequerant();

        StringBuilder detailRequerant = new StringBuilder();

        detailRequerant.append(requerant.getNss()).append(" - ");
        detailRequerant.append(requerant.getNom()).append(" ").append(requerant.getPrenom()).append(" ");
        detailRequerant.append("(").append(requerant.getDateNaissance()).append(" / ")
                .append(session.getCodeLibelle(requerant.getSexe().getCodeSysteme().toString())).append(")");

        return detailRequerant.toString();
    }

    /**
     * Retourne les rentes accordées liées à la décision
     * 
     * @param session
     * @param transaction
     * @param decision
     * @return
     * @throws Exception
     */
    private List<RERenteAccordee> getRenteAccordeeDeLaDecision(final BSession session, final BITransaction transaction,
            final REDecisionEntity decision) throws Exception {

        List<RERenteAccordee> renteAccordeesDecision = new ArrayList<RERenteAccordee>();
        REValidationDecisionsManager renteValideeManager = new REValidationDecisionsManager();
        renteValideeManager.setSession(session);

        REPrestationsDuesManager prestationsDuesManager = new REPrestationsDuesManager();
        prestationsDuesManager.setSession(session);

        // On récupère toutes les rentes validées liées à la décision
        renteValideeManager.setForIdDecision(decision.getIdDecision());

        renteValideeManager.find(transaction, BManager.SIZE_NOLIMIT);
        for (Object o1 : renteValideeManager.getContainer()) {
            REValidationDecisions renteValidee = (REValidationDecisions) o1;

            // On récupère toutes les prestations dues de chaque rente validée
            prestationsDuesManager.setForIdsPrestDues(renteValidee.getIdPrestationDue());
            prestationsDuesManager.find(transaction, BManager.SIZE_NOLIMIT);
            for (Object o2 : prestationsDuesManager.getContainer()) {
                REPrestationDue prestationDue = (REPrestationDue) o2;

                // Pour chaque prestation due, on récupère le code prestation de la prestation accordée
                String idPrestationAccordee = prestationDue.getIdRenteAccordee();
                if (JadeStringUtil.isBlankOrZero(idPrestationAccordee)) {
                    throw new Exception(
                            "Can not retrieve the REPrestationsAccordees from the the REPrestationDue with id ["
                                    + prestationDue.getIdPrestationDue() + "]");
                }
                RERenteAccordee renteAccordee = new RERenteAccordee();
                renteAccordee.setSession(session);
                renteAccordee.setIdPrestationAccordee(idPrestationAccordee);
                renteAccordee.retrieve(transaction);
                if (renteAccordee.isNew()) {
                    throw new Exception("Can not retrieve the REPrestationsAccordees  with id [" + idPrestationAccordee
                            + "]");
                }
                renteAccordeesDecision.add(renteAccordee);
            }
        }
        return renteAccordeesDecision;
    }

    private boolean isRetro(final REDecisionEntity decision) {
        if (JadeStringUtil.isBlankOrZero(decision.getDateDebutRetro())
                && JadeStringUtil.isBlankOrZero(decision.getDateFinRetro())) {
            return false;
        } else {
            return true;
        }
    }

    public FWCurrency min(final FWCurrency mnt1, final FWCurrency mnt2) {
        return decisionUtil.min(mnt1, mnt2);
    }

    private void miseAJourRemarqueDecision(final REPreValiderDecisionViewBean vb, final REDecisionEntity decision) {
        decision.setIsRemAnnDeci(vb.getIsRemAnnDeci());
        decision.setIsObliPayerCoti(vb.getIsObligPayerCoti());
        decision.setRemarqueDecision(vb.getRemarqueDecision());
        decision.setIsRemSuppVeuf(vb.getIsRemSuppVeuf());
        decision.setIsRemRedPlaf(vb.getIsRemRedPlaf());
        decision.setIsAvecBonneFoi(vb.getIsAvecBonneFoi());
        decision.setIsSansBonneFoi(vb.getIsSansBonneFoi());
        decision.setIsRemInteretMoratoires(vb.getIsInteretMoratoire());
        decision.setIsRemarqueRenteDeVeufLimitee(vb.getHasRemarqueRenteDeVeufLimitee());
        decision.setIsRemarqueRenteDeVeuveLimitee(vb.getHasRemarqueRenteDeVeuveLimitee());
        decision.setIsRemarqueRemariageRenteDeSurvivant(vb.getHasRemarqueRemariageRenteDeSurvivant());
        decision.setIsRemarqueRentePourEnfant(vb.getHasRemarqueRentesPourEnfants());
        decision.setIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande(vb
                .getHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot());
        decision.setIsRemarqueRenteAvecMontantMinimumMajoreInvalidite(vb
                .getHasRemarqueRenteAvecMontantMinimumMajoreInvalidite());
        decision.setIsRemarqueRenteReduitePourSurassurance(vb.getHasRemarqueRenteReduitePourSurassurance());
        decision.setCsGenreDecision(vb.getCsGenreDecision());
        decision.setTraitePar(vb.getTraiterParDecision());
    }

    public FWViewBeanInterface reAfficher(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            // Modifications possibles uniquement si la prestation de la décision se trouve dans un lot définitif

            // Retrieve de la décision
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(session);
            decision.setIdDecision(vb.getIdDecision());
            decision.setId(vb.getIdDecision());
            decision.retrieve(transaction);

            if (!decision.isNew()) {
                if (decision.getCsEtat().equals(IREDecision.CS_ETAT_ATTENTE)) {

                    // --> Mise à jour de la remarque de chaque "key"
                    decisionUtil.updateRemarqueKey(vb, session, transaction);

                    // update de la demande (remarque uniquement)
                    miseAJourRemarqueDecision(vb, decision);
                    decision.update(transaction);

                } else if (decision.getCsEtat().equals(IREDecision.CS_ETAT_PREVALIDE)) {

                    // --> Mise à jour de la remarque de chaque "key"
                    decisionUtil.updateRemarqueKey(vb, session, transaction);

                    miseAJourRemarqueDecision(vb, decision);
                    decision.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
                    decision.update(transaction);

                } else if (decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {

                    // Récupération de la prestation
                    REPrestationsManager prestationMgr = new REPrestationsManager();
                    prestationMgr.setSession(session);
                    prestationMgr.setForIdDecision(decision.getIdDecision());
                    prestationMgr.find(1);

                    REPrestations prestation = null;
                    if (!prestationMgr.isEmpty()) {
                        prestation = (REPrestations) prestationMgr.getFirstEntity();
                    }

                    // Si la prestation de la décision n'est pas dans un lot définitif
                    if ((prestation == null) || prestation.isNew()) {

                        miseAJourRemarqueDecision(vb, decision);
                        decision.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
                        decision.update(transaction);

                        REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction, session,
                                decision);

                        devalideAutresDecisionsDemande(decision, session, transaction);

                    } else {

                        if (JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {

                            miseAJourRemarqueDecision(vb, decision);
                            decision.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
                            decision.update(transaction);

                            REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction,
                                    session, decision);

                            devalideAutresDecisionsDemande(decision, session, transaction);

                        } else {

                            // Retrieve du lot
                            RELot lot = new RELot();
                            lot.setSession(session);
                            lot.setIdLot(prestation.getIdLot());
                            lot.retrieve(transaction);

                            if (!lot.isNew()) {

                                // si le lot est définitif
                                if (lot.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {

                                    // On peut pas modifier la décision
                                    String message = session.getLabel("ERREUR_IMP_MOD_DEC_PREST_VALIDE");
                                    message = message.replace("{0}", decision.getIdDecision());
                                    throw new Exception(message);

                                } else {

                                    // On peut modifier (état + remarque)
                                    miseAJourRemarqueDecision(vb, decision);
                                    decision.setTraitePar(vb.getTraiterParDecision());
                                    decision.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
                                    decision.update(transaction);

                                    REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(
                                            transaction, session, decision);

                                    devalideAutresDecisionsDemande(decision, session, transaction);

                                }
                            }
                        }
                    }
                }
            }

            vb.setIdDecision(vb.getDecision().getIdDecision());

            // --> Mise à jour des annexes

            // 1. Effacer tout ce qu'il y a dans la base
            REAnnexeDecisionManager annexeMgr = new REAnnexeDecisionManager();
            annexeMgr.setSession(session);
            annexeMgr.setForIdDecision(vb.getIdDecision());
            annexeMgr.find(transaction);

            for (REAnnexeDecision annexeDb : annexeMgr.getContainerAsList()) {
                annexeDb.delete(transaction);
            }

            // 2. Ajouter tout ce qui se trouve dans la liste
            List<REAnnexeDecisionViewBean> lstAnnexe = vb.getAnnexesList();

            for (REAnnexeDecisionViewBean annexe : lstAnnexe) {
                annexe.setIdDecision(vb.getIdDecision());
                annexe.add(transaction);
                annexe.setIdProvisoire(annexe.getIdDecisionAnnexe());

            }

            // --> Mise à jour des copie

            // 1. Effacer tout ce qu'il y a dans la base
            RECopieDecisionManager copieMgr = new RECopieDecisionManager();
            copieMgr.setSession(session);
            copieMgr.setForIdDecision(vb.getIdDecision());
            copieMgr.find(transaction);

            for (RECopieDecision copieDb : copieMgr.getContainerAsList()) {
                copieDb.delete(transaction);
            }

            // 2. Ajouter tout ce qui se trouve dans la liste
            List<RECopieDecisionViewBean> lstCopie = vb.getCopiesList();

            for (RECopieDecisionViewBean copie : lstCopie) {
                copie.setIdDecision(vb.getIdDecision());
                copie.add(transaction);
                copie.setIdProvisoire(copie.getIdDecisionCopie());
            }

            // --> Pour réaffichage

            String idTierBenefPrinc = vb.getIdTiersBeneficiairePrincipal();

            // Si on est venu dans l'écran depuis la rcList des décisions (détail)
            if (vb.getIsDepuisRcListDecision().booleanValue()) {

                // chargement de la décision d'arrivée
                REDecisionEntity deci = new REDecisionEntity();
                deci.setIdDecision(vb.getIdDecision());
                deci.setSession(session);
                deci.retrieve();

                REDecisionsContainer decisionContainer = new REDecisionsContainer();

                String idDemandeRente = deci.getIdDemandeRente();
                REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
                dem.setSession(session);
                dem.setIdDemandeRente(idDemandeRente);
                dem.retrieve(transaction);

                vb.setIdTiersRequerant(dem.getIdTiersRequerant());
                vb.setIdDemandeRente(dem.getIdDemandeRente());

                decisionContainer.loadDecision(session, deci);
                decisionContainer.parcourDecisionsIC(session);
                decisionContainer.setIdDemandeRente(deci.getIdDemandeRente());
                vb.setDecisionContainer(decisionContainer);

                if (!deci.getIdDecision().equals(vb.getIdDecision())) {
                    decisionContainer.init();
                }

                vb.setDecisionContainer(vb.getDecisionContainer());

                vb.getDecisionContainer().getDecisionIC().setCopiesListDIC(lstCopie);
                vb.getDecisionContainer().getDecisionIC().setAnnexesListDIC(lstAnnexe);
                vb.getDecisionContainer().getDecisionIC().setRemarqueDecisionDIC(vb.getRemarqueDecision());

                // Si on venu dans l'écran de préparation depuis la demande (préparation de la décision)
            } else {

                // chargement de toutes les décision pour la demande
                REDecisionsManager decMgr = new REDecisionsManager();
                decMgr.setForIdDemandeRente(vb.getIdDemandeRente());
                decMgr.setSession(session);
                decMgr.find();

                REDecisionsContainer decisionContainer = new REDecisionsContainer();

                for (REDecisionEntity dec : decMgr.getContainerAsList()) {

                    String idDemandeRente = dec.getIdDemandeRente();
                    REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
                    dem.setSession(session);
                    dem.setIdDemandeRente(idDemandeRente);
                    dem.retrieve(transaction);

                    vb.setIdTiersRequerant(dem.getIdTiersRequerant());

                    vb.setIdDemandeRente(dem.getIdDemandeRente());

                    decisionContainer.loadDecision(session, dec);
                    decisionContainer.parcourDecisionsIC(session);
                    decisionContainer.setIdDemandeRente(dec.getIdDemandeRente());
                    vb.setDecisionContainer(decisionContainer);

                    if (!dec.getIdDecision().equals(vb.getIdDecision())) {
                        decisionContainer.init();
                    }

                }

                vb.setDecisionContainer(vb.getDecisionContainer());
                vb.getDecisionContainer().getDecisionIC().setRemarqueDecisionDIC(vb.getRemarqueDecision());

            }

            vb.setIdTiersBeneficiairePrincipal(idTierBenefPrinc);
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTierBenefPrinc);
            if (tw != null) {
                vb.setTiersBeneficiairePrincipalInfo(tw.getDescription(session));
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
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
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return vb;
    }

    /**
     * Récupère la décision de type Courant associée à la demande.
     * 
     * @param session
     * @param transaction
     * @param idDemandeRente
     * @return
     * @throws Exception
     */
    private REDecisionEntity retrieveDecisionCourante(final BSession session, final BITransaction transaction,
            final DemandeRente demande) throws Exception {

        // Toutes les décisions de type courant pour une même demande
        // on obligatoirement la même date de début. On ne récupère donc que la première.

        REDecisionsManager mgr = new REDecisionsManager();
        mgr.setSession(session);
        mgr.setForCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
        mgr.setForIdDemandeRente(demande.getId().toString());
        mgr.find(transaction, 1);

        if (!mgr.isEmpty()) {
            return (REDecisionEntity) mgr.get(0);
        } else {
            throw new Exception("Inconsistent data, decision 'COURANT' type not found");
        }
    }

    public FWViewBeanInterface supprimerDecision(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {

        BITransaction transaction = null;
        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            // Retrieve de la décision
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(session);
            decision.setIdDecision(vb.getIdDecision());
            decision.setId(vb.getIdDecision());
            decision.retrieve(transaction);

            REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit(session, transaction, decision,
                    IREValidationLevel.VALIDATION_LEVEL_NONE);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
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
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return vb;

    }

    private void validate(final REPreValiderDecisionViewBean vb, final BSession session, final BITransaction transaction)
            throws Exception {

        REDemandeRente dem = new REDemandeRente();
        dem.setSession(session);
        dem.setIdDemandeRente(vb.getIdDemandeRente());
        dem.retrieve();
        PRAssert.notIsNew(dem, "Entity not found");

        // Si validation du courant, la demande doit être dans l'état calculé.
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(vb.getCsTypePreparationDecision())) {
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(dem.getCsEtat())) {
                throw new Exception(session.getLabel("ERREUR_DEMANDE_ETAT_CALCULE"));
            }
        }

        // Si validation du rétro, la demande doit être dans l'état PARTIEL, cad que le courant doit avoir été
        // préalablement validé.
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO.equals(vb.getCsTypePreparationDecision())) {
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(dem.getCsEtat())) {
                throw new Exception(session.getLabel("ERREUR_DEMANDE_ETAT_PARTIEL"));
            }
        }

        // Si validation standard, la demande doit être dans l'état calculé.
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD.equals(vb.getCsTypePreparationDecision())) {
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(dem.getCsEtat())) {
                throw new Exception(session.getLabel("ERREUR_DEMANDE_ETAT_CALCULE"));
            }
        }

        if (JadeStringUtil.isBlankOrZero(vb.getEMailAddress())) {
            throw new Exception(session.getLabel("ERREUR_EMAIL_NON_RENSEIGNE"));
        }
        if (JadeStringUtil.isBlankOrZero(vb.getDateDecision())) {
            throw new Exception(session.getLabel("ERREUR_DATE_DECISION_NON_RENSEIGNE"));
        }
        // if (JadeStringUtil.isBlankOrZero(vb.getDecisionDu())) {
        // throw new Exception("Le champ 'Décision du' doit être renseigné!");
        // }

        if (JadeStringUtil.isBlankOrZero(vb.getCsTypePreparationDecision())) {
            throw new Exception(session.getLabel("ERREUR_GENRE_NON_RENSEIGNE"));
        }

        // Vérification des dates de traitements, début du droit Pmt. Mensuel

        JACalendar cal = new JACalendarGregorian();
        JADate datePmtMensuel = null;

        if (!JadeStringUtil.isBlankOrZero(REPmtMensuel.getDateDernierPmt(session))) {
            datePmtMensuel = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(REPmtMensuel
                    .getDateDernierPmt(session)));
        }

        JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateDebut()));
        JADate dateDecision = new JADate(vb.getDateDecision());
        dateDecision.setDay(1);
        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(session);
        decision.setIdDecision(vb.getIdDecision());
        decision.retrieve(transaction);

        if (!IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {
            if (datePmtMensuel != null) {
                if (vb.getCsTypePreparationDecision().equals(IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD)) {
                    if (cal.compare(dateDecision, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER) {
                        vb._addError("JSP_PRP_D_DDECMOICON");
                    }
                    if (((cal.compare(dateDecision, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER) && ((cal.compare(
                            dateDecision, dateDebutDroit) == JACalendar.COMPARE_FIRSTUPPER) || (cal.compare(
                            dateDecision, dateDebutDroit) == JACalendar.COMPARE_EQUALS)))) {
                        vb._addError("JSP_PRP_D_RECDEM");
                    }
                } else {
                    if (cal.compare(dateDecision, datePmtMensuel) != JACalendar.COMPARE_EQUALS) {
                        vb._addError("JSP_PRP_D_DDECMOICON");
                    }
                }
            } else {
                throw new Exception("JSP_PRP_D_RECDEM");
            }
        }

        // vérifier que toutes les rentes accordées figurant dans cette demande aient une adresse de paiement
        // verifier que tous les creancier de type "Tiers" et "Assurance sociale" avec un montant repartit
        // different de 0 aient une adresse de paiement

        RERenteAccJoinTblTiersJoinDemRenteManager rajtdrManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
        rajtdrManager.setSession(session);
        rajtdrManager.setForNoDemandeRente(dem.getIdDemandeRente());
        rajtdrManager.find(transaction);

        for (RERenteAccJoinTblTiersJoinDemandeRente ra : rajtdrManager.getContainerAsList()) {
            REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();

            if ((infoCompt == null) || infoCompt.isNew()) {
                throw new Exception(session.getLabel("ERREUR_ADRESSE_PAIEMENT_RA"));
            }

            TIAdressePaiementData adr = null;
            if (!JadeStringUtil.isBlankOrZero(infoCompt.getIdTiersAdressePmt())) {
                // Si le domaine rente n'existe pas, on récupère le domaine standard.
                adr = PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                        infoCompt.getIdTiersAdressePmt(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                        vb.getDateDecision());
            }

            if ((adr == null) || adr.isNew()) {
                vb.setMessage(session.getLabel("ERREUR_ADRESSE_PAIEMENT_RA"));
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        RECreancierListViewBean crManager = new RECreancierListViewBean();
        crManager.setSession(session);
        crManager.setForIdDemandeRente(dem.getIdDemandeRente());
        crManager.find(transaction);

        for (int i = 0; i < crManager.size(); i++) {
            RECreancierViewBean cr = (RECreancierViewBean) crManager.get(i);

            if ((IRECreancier.CS_TIERS.equals(cr.getCsType()) || IRECreancier.CS_ASSURANCE_SOCIALE.equals(cr
                    .getCsType())) && !new FWCurrency(cr.getMontantReparti()).isZero()) {

                // Si le domaine rente n'existe pas, on récupère le domaine standard.
                TIAdressePaiementData adr = PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                        cr.getIdTiersAdressePmt(), cr.getIdDomaineApplicatif(), "", vb.getDateDecision());

                if ((adr == null) || adr.isNew()) {
                    vb._addError("ERREUR_ADRESSE_PAIEMENT_CR");
                    throw new Exception(session.getLabel("ERREUR_ADRESSE_PAIEMENT_CR"));
                }
            }
        }
    }
}
