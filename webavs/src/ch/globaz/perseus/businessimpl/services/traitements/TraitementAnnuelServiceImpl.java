/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.traitements;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.models.parametres.LienLocaliteSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.traitements.TraitementAnnuelService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author dde
 * 
 */
public class TraitementAnnuelServiceImpl extends PerseusAbstractServiceImpl implements TraitementAnnuelService {

    private Map<String, Decision> anciennesDecisions = new HashMap<String, Decision>();
    private StringBuilder casAvecRetenu = null;
    private StringBuilder casNonCalculable = null;

    private int demandesFermees = 0;

    private Collection<String> dossiersOuverts = new ArrayList<String>();

    List<Decision> listDecisionsReturn = new ArrayList<Decision>();

    private StringBuilder listeCasAvecMontantAFDifferent = null;
    private StringBuilder listeCasLocaliteHorsVaud = null;
    private ArrayList<String> listeLocalite = null;

    private Map<String, String> listMontantAFAvecChangement = null;

    private List<String> listMontantAFSansChangement = null;

    private String moisActuel = "";

    private List<String> nouvellesDemandes = new ArrayList<String>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.traitements.TraitementAnnuelService#executerTraitementsAnnuels(globaz.globall
     * .db.BSession, globaz.jade.log.business.JadeBusinessLogSession)
     */

    private PCFAccordee calculerNouvelleDemande(Demande nouvelleDemande) throws Exception {
        // On fait le calcul
        return PerseusServiceLocator.getPCFAccordeeService().calculer(nouvelleDemande.getId());
    }

    private boolean checkConditionsForPriseEnCompteDemandeForTraitement(Decision decision)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        boolean isOkForTraitement = false;
        if (isDecisionOctroiCompletOrPartiel(decision) && isDemandeNoRenouvelle(decision)) {
            isOkForTraitement = true;
        }
        return isOkForTraitement;
    }

    private boolean checkParameter(BSession session, JadeBusinessLogSession logSession) throws ParametresException {
        if ((null == session) || (null == logSession)) {
            throw new ParametresException("Parameter is null");
        }
        return true;
    }

    private Demande copierDemandePourTraitementAnneulAvecAF(Demande demande) throws Exception {
        Demande newDemande = (Demande) JadePersistenceUtil.clone(demande);
        newDemande.setDossier(demande.getDossier());

        try {
            // Copie de la situation familiale
            SituationFamiliale newSituationFamiliale = (SituationFamiliale) JadePersistenceUtil.clone(demande
                    .getSituationFamiliale());
            newSituationFamiliale.setRequerant(demande.getSituationFamiliale().getRequerant());
            newSituationFamiliale.setConjoint(demande.getSituationFamiliale().getConjoint());
            newSituationFamiliale = PerseusServiceLocator.getSituationFamilialeService().create(newSituationFamiliale);
            newDemande.setSituationFamiliale(newSituationFamiliale);
            // Insertion de la demande
            SimpleDemande simpleDemande = newDemande.getSimpleDemande();
            simpleDemande.setIdDossier(newDemande.getDossier().getId());
            simpleDemande.setIdSituationFamiliale(newSituationFamiliale.getId());
            simpleDemande.setCsEtatDemande(CSEtatDemande.ENREGISTRE.getCodeSystem());
            simpleDemande.setDateTimeDecisionValidation("");
            simpleDemande = PerseusImplServiceLocator.getSimpleDemandeService().create(simpleDemande);
            newDemande.setSimpleDemande(simpleDemande);

            // Copier les enfants de la demande
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);
            for (JadeAbstractModel model : enfantFamilleSearchModel.getSearchResults()) {
                EnfantFamille enfantFamille = (EnfantFamille) model;
                EnfantFamille newEnfantFamille = (EnfantFamille) JadePersistenceUtil.clone(enfantFamille);
                newEnfantFamille.getSimpleEnfantFamille().setIdSituationFamiliale(newSituationFamiliale.getId());
                newEnfantFamille.setEnfant(enfantFamille.getEnfant());

                PerseusImplServiceLocator.getEnfantFamilleService().create(newEnfantFamille);
            }

            // Copier les données financières de la demande
            // Revenus
            RevenuSearchModel revenuSearchModel = new RevenuSearchModel();
            revenuSearchModel.setForIdDemande(demande.getId());
            revenuSearchModel = PerseusServiceLocator.getRevenuService().search(revenuSearchModel);
            for (JadeAbstractModel model : revenuSearchModel.getSearchResults()) {
                Revenu revenu = (Revenu) model;
                Revenu newRevenu = (Revenu) JadePersistenceUtil.clone(revenu);
                if (RevenuType.ALLOCATIONS_FAMILIALES.getId().toString()
                        .equals(newRevenu.getSimpleDonneeFinanciere().getType())
                        && !revenu.getSimpleDonneeFinanciere().getValeur().equals("0")) {
                    newRevenu = traitementRevenuPourAdaptationAF(revenu, demande);
                }
                newRevenu.setDemande(newDemande);
                newRevenu.setMembreFamille(revenu.getMembreFamille());
                PerseusServiceLocator.getRevenuService().create(newRevenu);
            }
            // Fortune
            FortuneSearchModel fortuneSearchModel = new FortuneSearchModel();
            fortuneSearchModel.setForIdDemande(demande.getId());
            fortuneSearchModel = PerseusServiceLocator.getFortuneService().search(fortuneSearchModel);
            for (JadeAbstractModel model : fortuneSearchModel.getSearchResults()) {
                Fortune fortune = (Fortune) model;
                Fortune newFortune = (Fortune) JadePersistenceUtil.clone(fortune);
                newFortune.setDemande(newDemande);
                newFortune.setMembreFamille(fortune.getMembreFamille());
                PerseusServiceLocator.getFortuneService().create(newFortune);
            }
            // Dettes
            DetteSearchModel detteSearchModel = new DetteSearchModel();
            detteSearchModel.setForIdDemande(demande.getId());
            detteSearchModel = PerseusServiceLocator.getDetteService().search(detteSearchModel);
            for (JadeAbstractModel model : detteSearchModel.getSearchResults()) {
                Dette dette = (Dette) model;
                Dette newDette = (Dette) JadePersistenceUtil.clone(dette);
                newDette.setDemande(newDemande);
                newDette.setMembreFamille(dette.getMembreFamille());
                PerseusServiceLocator.getDetteService().create(newDette);
            }
            // Dépenses reconnues
            DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
            depenseReconnueSearchModel.setForIdDemande(demande.getId());
            depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                    depenseReconnueSearchModel);
            for (JadeAbstractModel model : depenseReconnueSearchModel.getSearchResults()) {
                DepenseReconnue depenseReconnue = (DepenseReconnue) model;
                DepenseReconnue newDepenseReconnue = (DepenseReconnue) JadePersistenceUtil.clone(depenseReconnue);
                newDepenseReconnue.setDemande(newDemande);
                newDepenseReconnue.setMembreFamille(depenseReconnue.getMembreFamille());
                PerseusServiceLocator.getDepenseReconnueService().create(newDepenseReconnue);
            }

        } catch (SituationFamilleException e) {
            throw new DemandeException("SituationFamilleException during demande copy : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during demande copy : " + e.toString(), e);
        } catch (DonneesFinancieresException e) {
            throw new DemandeException("DonneesFinancieresException during demande copy : " + e.toString(), e);
        }

        return newDemande;
    }

    private Decision createDecisionForNewDemande(Demande nouvelleDemande, Decision oldDecision, PCFAccordee pcfa,
            String texteDecision) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DecisionException {
        Decision decision = new Decision();
        decision.setDemande(nouvelleDemande);
        // Service devrait faire ca mais bon je le fais
        decision.getSimpleDecision().setIdDemande(nouvelleDemande.getId());
        decision.setListCopies(oldDecision.getListCopies());
        if (Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant()) > 0) {
            decision.getSimpleDecision().setCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        } else {
            decision.getSimpleDecision().setCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
        }
        decision.getSimpleDecision().setDateDocument(JadeDateUtil.getGlobazFormattedDate(new Date()));
        decision.getSimpleDecision().setCsEtat(CSEtatDecision.PRE_VALIDE.getCodeSystem());
        decision.getSimpleDecision().setDatePreparation(nouvelleDemande.getSimpleDemande().getDateDebut());
        decision.getSimpleDecision().setUtilisateurPreparation(
                oldDecision.getSimpleDecision().getUtilisateurPreparation());
        decision.getSimpleDecision().setIdDomaineApplicatifAdresseCourrier(
                oldDecision.getSimpleDecision().getIdDomaineApplicatifAdresseCourrier());
        decision.getSimpleDecision().setIdDomaineApplicatifAdressePaiement(
                oldDecision.getSimpleDecision().getIdDomaineApplicatifAdressePaiement());
        decision.getSimpleDecision().setIdTiersAdresseCourrier(
                oldDecision.getSimpleDecision().getIdTiersAdresseCourrier());
        decision.getSimpleDecision().setIdTiersAdressePaiement(
                oldDecision.getSimpleDecision().getIdTiersAdressePaiement());
        decision.getSimpleDecision().setMontantToucheAuRI(oldDecision.getSimpleDecision().getMontantToucheAuRI());
        decision.getSimpleDecision().setNumeroDecision(
                PerseusServiceLocator.getDecisionService().getNumeroDemandeCalculee(
                        nouvelleDemande.getSimpleDemande().getDateDebut().substring(6)));
        decision.getSimpleDecision().setRemarquesGenerales(oldDecision.getSimpleDecision().getRemarquesGenerales());
        decision.getSimpleDecision().setRemarqueUtilisateur(texteDecision);

        return PerseusServiceLocator.getDecisionService().create(decision);
    }

    /**
     * Traitement des demandes - Check si calculable - Si calculable, copie de la demande - Si non calculable,
     * indication de la demande dans l'e-mail de fin de processus - Ferme les demandes - Indication si la demande a une
     * retenue dans l'e-amil de fin de processus
     * 
     * @throws Exception
     */
    private void doTraitementDemandeEnCours(Decision decision) throws Exception {
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            if (isLocaliteDansCantonVaud(decision.getDemande(), moisActuel)) {
                traitementDemandeOctroiComplet(decision);
            }
        }
        // Toute les demandes sont fermées
        fermerAncienneDemande(decision);
    }

    private void doTraitementDemandeEnCoursPourTraitementAF(Decision decision) throws Exception {
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            if (isLocaliteDansCantonVaud(decision.getDemande(), moisActuel)) {
                traitementDemandeOctroiCompletPourTraitementAF(decision);
            }
        }
        // Toute les demandes sont fermées
        fermerAncienneDemande(decision);
    }

    /**
     * Insere la date de fin et le type dans la nouvelle demande Calcul la nouvelle demande Reprise de la mesure de
     * coaching Création de la décision pour la nouvelle demande Valide la décision Détermine si la décision doit être
     * imprimée
     * 
     * @throws Exception
     */
    private void doTraitementReouvertureDemande(Demande demande, String texteDecision, BSession session)
            throws Exception {
        Decision oldDecision = anciennesDecisions.get(demande.getId());
        demande = updateDateEtTypeNouvelleDemande(demande);
        PCFAccordee pcfa = calculerNouvelleDemande(demande);
        demande = pcfa.getDemande();
        PCFAccordee oldPCFA = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                oldDecision.getDemande().getId());
        pcfa = repriseMesureCoaching(oldPCFA, pcfa);
        Decision decision = createDecisionForNewDemande(demande, oldDecision, pcfa, texteDecision);
        decision = PerseusServiceLocator.getDecisionService().valider(decision, session.getUserId());
        putDecisionInListIfImpression(oldDecision, decision, oldPCFA, pcfa);
    }

    @Override
    public List<Decision> executerTraitementsAnnuels(BSession session, JadeBusinessLogSession logSession,
            String texteDecision) throws Exception {

        try {

            if (checkParameter(session, logSession) && isValidationDecisionNoAuthorise(session) && isJanvier()) {

                DecisionSearchModel decisionSearchModel = loadDecisionValidate();
                initialiseStringBuilderAndList();

                // Traitement des demandes en cours
                for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                    Decision decision = (Decision) model;

                    if (checkConditionsForPriseEnCompteDemandeForTraitement(decision)) {
                        dossiersOuverts.add(decision.getDemande().getDossier().getId());
                        doTraitementDemandeEnCours(decision);
                    }

                    logSiError(decision.getDemande());

                }

                PerseusServiceLocator.getPmtMensuelService().activerValidationDecision();

                // Traitement de réouverture des demandes d'octroi
                for (String idDemande : nouvellesDemandes) {
                    Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);

                    if (isLocaliteDansCantonVaud(demande, moisActuel)) {
                        doTraitementReouvertureDemande(demande, texteDecision, session);

                        logSiError(demande);
                    }

                }
                renseignerInfoMailSuiteTraitements(logSession);

            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PerseusException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_SERVICE_NON_DISPONIBLE")
                    + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PerseusException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_JADEAPPLICATIONEXCEPTION")
                    + e.toString(), e);
        }
        return listDecisionsReturn;
    }

    @Override
    public List<Decision> executerTraitementsAnnuelsAvecAF(BSession session, JadeBusinessLogSession logSession,
            String texteDecision) throws Exception {
        try {
            if (checkParameter(session, logSession) && isValidationDecisionNoAuthorisePourTraitementAF(session)
                    && isJanvier()) {

                DecisionSearchModel decisionSearchModel = loadDecisionValidate();
                initiliseStringBuilderAndListForTraitementAvecAF();

                // Traitement des demandes en cours
                for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                    Decision decision = (Decision) model;

                    if (checkConditionsForPriseEnCompteDemandeForTraitement(decision)) {
                        dossiersOuverts.add(decision.getDemande().getDossier().getId());
                        doTraitementDemandeEnCoursPourTraitementAF(decision);
                    }

                    logSiError(decision.getDemande());

                }

                PerseusServiceLocator.getPmtMensuelService().activerValidationDecision();

                // Traitement de réouverture des demandes d'octroi
                for (String idDemande : nouvellesDemandes) {
                    Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);

                    if (isLocaliteDansCantonVaud(demande, moisActuel)) {
                        doTraitementReouvertureDemande(demande, texteDecision, session);

                        logSiError(demande);
                    }

                }

                renseignerInfoMailSuiteTraitements(logSession);
                renseignerInfoMailSuiteTraitementAvecAF(logSession);

            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PerseusException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_SERVICE_NON_DISPONIBLE")
                    + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PerseusException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_JADEAPPLICATIONEXCEPTION")
                    + e.toString(), e);
        }
        return listDecisionsReturn;
    }

    private void fermerAncienneDemande(Decision decision) throws Exception {
        // Fermeture des demandes le dernier jour du mois précédent le lancement du traitement
        String dateFin = PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        dateFin = "31." + dateFin;
        decision.getDemande().getSimpleDemande().setDateFin(dateFin);
        Demande demande = PerseusServiceLocator.getDemandeService().update(decision.getDemande());
        demandesFermees++;
    }

    private String getIdLocalite(Demande demande, String mois) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail adresseTiersDetail = null;
        adresseTiersDetail = PFUserHelper.getAdresseAssure(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "01." + mois);

        if (adresseTiersDetail.getFields() == null) {
            adresseTiersDetail = PFUserHelper.getAdresseAssure(demande.getDossier().getDemandePrestation()
                    .getPersonneEtendue().getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_COURRIER, "01." + mois);
            if (adresseTiersDetail.getFields() == null) {
                return "";
            }
        }
        String idLocalite = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
        return idLocalite;
    }

    private ArrayList<String> getListLocalite(String mois) throws ParametresException, JadePersistenceException {
        // Recherche des localite
        ArrayList<String> listeLocalite = new ArrayList<String>();
        LienLocaliteSearchModel lienLocaliteSearchModel = new LienLocaliteSearchModel();
        lienLocaliteSearchModel.setForDateValable("01." + mois);
        lienLocaliteSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        try {
            lienLocaliteSearchModel = PerseusServiceLocator.getLienLocaliteService().search(lienLocaliteSearchModel);
            for (JadeAbstractModel model : lienLocaliteSearchModel.getSearchResults()) {
                LienLocalite lien = (LienLocalite) model;
                listeLocalite.add(lien.getSimpleLienLocalite().getIdLocalite());
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available : " + e.getMessage());
        }

        return listeLocalite;
    }

    private boolean hasDifferenceBetweenOldEtNewRetenuImpotSource(PCFAccordee oldPCFA, PCFAccordee newPCFA)
            throws RetenueException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean hasDifferenceIS = false;
        if (oldPCFA.getDemande().getSimpleDemande().getPermisB()) {
            // Recuperation de la retenue IS pour l'ancienne situatione et la nouvelle
            SimpleRetenue simpleRetenueOld = loadRetenueIS(oldPCFA.getSimplePCFAccordee().getIdPCFAccordee());
            SimpleRetenue simpleRetenueNew = loadRetenueIS(newPCFA.getSimplePCFAccordee().getIdPCFAccordee());
            if ((null != simpleRetenueOld) && (null != simpleRetenueNew)
                    && !simpleRetenueNew.getMontantRetenuMensuel().equals(simpleRetenueOld.getMontantRetenuMensuel())) {
                hasDifferenceIS = true;
            } else if (((null != simpleRetenueOld) && (null == simpleRetenueNew))
                    || ((null == simpleRetenueOld) && (null != simpleRetenueNew))) {
                hasDifferenceIS = true;
            }
        }

        return hasDifferenceIS;

    }

    private boolean hasRetenuNonIS(Decision decision) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, RetenueException {
        // indique si le cas est concerné par une retenue
        boolean isCasAvecRetenu = false;
        PCFAccordee pcf = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                decision.getDemande().getSimpleDemande().getId());

        if (null != pcf) {
            SimpleRetenueSearchModel retenueSearch = new SimpleRetenueSearchModel();
            retenueSearch.setForIdPcfAccordee(pcf.getSimplePCFAccordee().getIdPCFAccordee());
            retenueSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retenueSearch);

            for (JadeAbstractModel model : retenueSearch.getSearchResults()) {
                SimpleRetenue retenu = (SimpleRetenue) model;
                if (!CSTypeRetenue.IMPOT_SOURCE.getCodeSystem().equals(retenu.getCsTypeRetenue())) {
                    isCasAvecRetenu = true;
                }
            }
        }
        return isCasAvecRetenu;
    }

    private StringBuilder initialiseListCasAFAvecMontantDifferent() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ANNUEL_CAS_AF_NON_IDENTIFIE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private StringBuilder initialiseListCasLocaliteHorsVaud() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_CAS_LOCALITE_HORS_VAUD"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private StringBuilder initialiseListCasNonCalculable() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_DEMANDE_NON_CALCULABLE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private StringBuilder initialiseListCasRetenu() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_CAS_AVEC_RETENU"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private List<String> initialiseListMontantAFSansChangement() {
        listMontantAFSansChangement = new ArrayList<String>();
        listMontantAFSansChangement.add("3600.00");
        listMontantAFSansChangement.add("7200.00");
        listMontantAFSansChangement.add("11640.00");
        listMontantAFSansChangement.add("16080.00");
        listMontantAFSansChangement.add("20520.00");
        listMontantAFSansChangement.add("24960.00");
        listMontantAFSansChangement.add("29400.00");
        listMontantAFSansChangement.add("33840.00");
        listMontantAFSansChangement.add("12840.00");
        listMontantAFSansChangement.add("17280.00");
        listMontantAFSansChangement.add("21720.00");
        listMontantAFSansChangement.add("26160.00");
        listMontantAFSansChangement.add("30600.00");
        listMontantAFSansChangement.add("35040.00");
        listMontantAFSansChangement.add("18480.00");
        listMontantAFSansChangement.add("22920.00");
        listMontantAFSansChangement.add("27360.00");
        listMontantAFSansChangement.add("31800.00");
        listMontantAFSansChangement.add("36240.00");
        listMontantAFSansChangement.add("24120.00");
        listMontantAFSansChangement.add("28560.00");
        listMontantAFSansChangement.add("33000.00");
        listMontantAFSansChangement.add("37440.00");
        listMontantAFSansChangement.add("29760.00");
        listMontantAFSansChangement.add("34200.00");
        listMontantAFSansChangement.add("38640.00");
        listMontantAFSansChangement.add("35400.00");
        listMontantAFSansChangement.add("39840.00");
        listMontantAFSansChangement.add("41040.00");
        return listMontantAFSansChangement;

    }

    private Map<String, String> initialiseMapListMontantAFAvecChangement() {
        listMontantAFAvecChangement = new HashMap<String, String>();
        listMontantAFAvecChangement.put("2400.00", "2760.00");
        listMontantAFAvecChangement.put("4800.00", "5520.00");
        listMontantAFAvecChangement.put("9240.00", "9960.00");
        listMontantAFAvecChangement.put("13680.00", "14400.00");
        listMontantAFAvecChangement.put("18120.00", "18840.00");
        listMontantAFAvecChangement.put("22560.00", "23280.00");
        listMontantAFAvecChangement.put("27000.00", "27720.00");
        listMontantAFAvecChangement.put("31440.00", "32160.00");
        listMontantAFAvecChangement.put("6000.00", "6360.00");
        listMontantAFAvecChangement.put("10440.00", "10800.00");
        listMontantAFAvecChangement.put("14880.00", "15240.00");
        listMontantAFAvecChangement.put("19320.00", "19680.00");
        listMontantAFAvecChangement.put("23760.00", "24120.00");
        listMontantAFAvecChangement.put("28200.00", "28560.00");
        listMontantAFAvecChangement.put("32640.00", "33000.00");
        return listMontantAFAvecChangement;
    }

    private void initialiseStringBuilderAndList() throws ParametresException, JadePersistenceException {
        listeLocalite = getListLocalite(moisActuel);
        casNonCalculable = initialiseListCasNonCalculable();
        casAvecRetenu = initialiseListCasRetenu();
        listeCasLocaliteHorsVaud = initialiseListCasLocaliteHorsVaud();
    }

    private void initiliseStringBuilderAndListForTraitementAvecAF() throws ParametresException,
            JadePersistenceException {
        initialiseStringBuilderAndList();
        listeCasAvecMontantAFDifferent = initialiseListCasAFAvecMontantDifferent();
        listMontantAFAvecChangement = initialiseMapListMontantAFAvecChangement();
        listMontantAFSansChangement = initialiseListMontantAFSansChangement();

    }

    private boolean isDecisionOctroiCompletOrPartiel(Decision decision) {
        boolean isOctroiCompletOrPartiel = false;
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
                || CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                        decision.getSimpleDecision().getCsTypeDecision())) {
            isOctroiCompletOrPartiel = true;
        }
        return isOctroiCompletOrPartiel;
    }

    private boolean isDemandeNoRenouvelle(Decision decision) {
        if (!moisActuel.equals(decision.getDemande().getSimpleDemande().getDateDebut().substring(3))) {
            return true;
        } else {
            // J'ajoute le dossier dans la liste des dossiers déjà traités car la demande est déjà renouvellée
            dossiersOuverts.add(decision.getDemande().getDossier().getDossier().getIdDossier());
            return false;
        }
    }

    private boolean isJanvier() throws PaiementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean isJanvier = true;
        moisActuel = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if (!moisActuel.substring(0, 2).equals("01")) {
            isJanvier = false;
            JadeThread
                    .logError(
                            this.getClass().getName(),
                            BSessionUtil.getSessionFromThreadContext().getLabel(
                                    "SERVICE_TRAITEMENT_ANNUEL_ERREUR_PMT_MENSUEL"));
        }
        return isJanvier;
    }

    /**
     * Détermine si la localité est dans le canton de Vaud, si ce n'est pas le cas, indique le cas dans l'e-mail de fin
     * de processus
     */
    private boolean isLocaliteDansCantonVaud(Demande demande, String mois)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        String idLocalite = getIdLocalite(demande, mois);
        boolean isLocaliteDansCantonVaud = false;
        if (!JadeStringUtil.isEmpty(idLocalite)) {
            isLocaliteDansCantonVaud = listeLocalite.contains(idLocalite);
        }

        if (!isLocaliteDansCantonVaud) {
            listInfoCas(listeCasLocaliteHorsVaud, demande, false, "", "");
        }

        return isLocaliteDansCantonVaud;

    }

    private boolean isValidationDecisionNoAuthorise(BSession session) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {
        if (PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise(session)) {
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "SERVICE_TRAITEMENT_ANNUEL_ERREUR_INTERDICTION_VALIDATION_DECISION"));
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidationDecisionNoAuthorisePourTraitementAF(BSession session) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {
        if (PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise(session)) {
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "SERVICE_TRAITEMENT_ANNUEL_AVEC_AF_ERREUR_INTERDICTION_VALIDATION_DECISION"));
            return false;
        } else {
            return true;
        }
    }

    private void listerCasSiRetenu(Decision decision, Demande demandeCopie) throws PCFAccordeeException,
            RetenueException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (hasRetenuNonIS(decision)) {

            listInfoCas(casAvecRetenu, decision.getDemande(), true, demandeCopie.getSimpleDemande().getIdDemande(),
                    decision.getDemande().getSimpleDemande().getIdDemande());
        }
    }

    private void listIdNewDemandeEtOldDecisionPourReouverture(String idNewDemande, Decision decision) {
        nouvellesDemandes.add(idNewDemande);
        anciennesDecisions.put(idNewDemande, decision);
    }

    private StringBuilder listInfoCas(StringBuilder listCas, Demande demande, boolean casRetenu,
            String idNouvelleDemande, String idAncienneDemande) {
        listCas.append(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                + " "
                + demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2()
                + ", "
                + demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel() + ", ");
        if (casRetenu) {
            listCas.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_NUMERO_ANCIENNE_DEMANDE")
                    + idAncienneDemande
                    + ", "
                    + BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_NUMERO_DEMANDE")
                    + idNouvelleDemande + ", ");
        }

        return renseigneCaisse(listCas, demande.getSimpleDemande().getCsCaisse());

    }

    private StringBuilder listInfoCasPourAF(StringBuilder listCas, Demande demande, Revenu revenu, boolean isRequerant) {
        if (CSCaisse.CCVD.getCodeSystem().equals(demande.getSimpleDemande().getCsCaisse())) {
            listCas.append(CSCaisse.CCVD);
        } else {
            listCas.append(CSCaisse.AGENCE_LAUSANNE);
        }
        listCas.append(", ");
        if (isRequerant) {
            listCas.append(demande.getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue()
                    .getTiers().getDesignation1()
                    + " "
                    + demande.getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue().getTiers()
                            .getDesignation2()
                    + ", "
                    + demande.getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()
                    + ", "
                    + BSessionUtil.getSessionFromThreadContext()
                            .getLabel("SERVICE_TRAITEMENT_ANNUEL_AVEC_AF_REQUERANT") + ", ");

        } else {
            listCas.append(demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                    .getTiers().getDesignation1()
                    + " "
                    + demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue().getTiers()
                            .getDesignation2()
                    + ", "
                    + demande.getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()
                    + ", "
                    + BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ANNUEL_AVEC_AF_CONJOINT")
                    + ", ");
        }
        listCas.append(revenu.getSimpleDonneeFinanciere().getValeur());
        listCas.append(BSessionUtil.getSessionFromThreadContext()
                .getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return listCas;

    }

    private DecisionSearchModel loadDecisionValidate() throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionSearchModel.setWhereKey(DecisionSearchModel.WITHOUT_DATEFIN);
        ArrayList<String> typesDecision = new ArrayList<String>(1);
        typesDecision.add(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        typesDecision.add(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
        decisionSearchModel.setForListCsTypes(typesDecision);
        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
        return decisionSearchModel;
    }

    private SimpleRetenue loadRetenueIS(String idPCFAccordee) throws RetenueException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleRetenue simpleretenue = null;
        SimpleRetenueSearchModel retSearch = new SimpleRetenueSearchModel();
        retSearch.setForIdPcfAccordee(idPCFAccordee);
        retSearch.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
        retSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retSearch);
        for (JadeAbstractModel model : retSearch.getSearchResults()) {
            simpleretenue = (SimpleRetenue) model;
        }
        return simpleretenue;
    }

    private void logError(String nss) throws Exception {
        StringBuilder messageError = new StringBuilder();
        messageError.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_ERREUR")
                + "\n");
        messageError.append(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PF_RENTEPONT_R_NSS") + " : ");
        messageError.append(nss + "\n");
        for (int iMessage = 0; iMessage < JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
            messageError.append(JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                    .getContents(JadeThread.currentLanguage()));
        }
        throw new Exception(messageError.toString());
    }

    private void logSiError(Demande demande) throws Exception {
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            logError(demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel());
        }
    }

    /**
     * Liste la demande pour le phase de réouverture des demandes Indique dans l'e-mail de fin de processus si le cas
     * est concerné par une retenue
     */
    private void priseEnCompteDemandePourReouvertureEtListeCasSiRetenu(Demande demandeCopie, Decision decision)
            throws PCFAccordeeException, RetenueException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        listIdNewDemandeEtOldDecisionPourReouverture(demandeCopie.getSimpleDemande().getIdDemande(), decision);
        listerCasSiRetenu(decision, demandeCopie);
    }

    private List<Decision> putDecisionInListIfImpression(Decision oldDecision, Decision decision, PCFAccordee oldPCFA,
            PCFAccordee pcfa) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RetenueException {
        Float newExcedant = Float.parseFloat(pcfa.getSimplePCFAccordee().getExcedantRevenu());
        Float oldExcedant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getExcedantRevenu());
        Float newMontant = Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        Float oldMontant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getMontant());
        if (!newMontant.equals(oldMontant) || !newExcedant.equals(oldExcedant)) {
            listDecisionsReturn.add(decision);
        } else if (hasDifferenceBetweenOldEtNewRetenuImpotSource(oldPCFA, pcfa)) {
            listDecisionsReturn.add(decision);
        }
        return listDecisionsReturn;
    }

    private StringBuilder renseigneCaisse(StringBuilder stringBuilder, String csCaisse) {
        if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
            stringBuilder.append(CSCaisse.CCVD);
        } else {
            stringBuilder.append(CSCaisse.AGENCE_LAUSANNE);
        }
        stringBuilder.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return stringBuilder;
    }

    private void renseignerInfoMailSuiteTraitementAvecAF(JadeBusinessLogSession logSession) {
        logSession.info(this.getClass().getName(), listeCasAvecMontantAFDifferent.toString());
    }

    private void renseignerInfoMailSuiteTraitements(JadeBusinessLogSession logSession) {
        logSession.info(this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_DEMANDES_FERMEES")
                        + demandesFermees);
        logSession.info(this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ANNUEL_DEMANDES_REOUVERTES")
                        + nouvellesDemandes.size());
        logSession.info(this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_DECISION_IMPRIMEES")
                        + listDecisionsReturn.size());
        logSession.info(this.getClass().getName(), casNonCalculable.toString());
        logSession.info(this.getClass().getName(), listeCasLocaliteHorsVaud.toString());
        logSession.info(this.getClass().getName(), casAvecRetenu.toString());

    }

    private PCFAccordee repriseMesureCoaching(PCFAccordee oldPCFA, PCFAccordee pcfa) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PerseusServiceLocator.getPCFAccordeeService().update(pcfa, "0", "0",
                oldPCFA.getCalcul().getDonneeString(OutputData.MESURE_COACHING));
    }

    private void traitementDemandeOctroiComplet(Decision decision) throws Exception {
        String dateDebutDemande = decision.getDemande().getSimpleDemande().getDateDebut();
        decision.getDemande().getSimpleDemande().setDateDebut("01." + moisActuel);
        if (PerseusServiceLocator.getDemandeService().checkCalculable(decision.getDemande())) {
            decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
            Demande demandeCopie = PerseusServiceLocator.getDemandeService().copier(decision.getDemande());
            priseEnCompteDemandePourReouvertureEtListeCasSiRetenu(demandeCopie, decision);
        } else {
            // Je liste le cas pour dire qu'il n'est pas calculable, ces cas seront fermées mais pas réouverts !
            listInfoCas(casNonCalculable, decision.getDemande(), false, "", "");
        }
        decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
    }

    private void traitementDemandeOctroiCompletPourTraitementAF(Decision decision) throws Exception {
        String dateDebutDemande = decision.getDemande().getSimpleDemande().getDateDebut();
        decision.getDemande().getSimpleDemande().setDateDebut("01." + moisActuel);
        if (PerseusServiceLocator.getDemandeService().checkCalculable(decision.getDemande())) {
            decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
            Demande demandeCopie = copierDemandePourTraitementAnneulAvecAF(decision.getDemande());
            priseEnCompteDemandePourReouvertureEtListeCasSiRetenu(demandeCopie, decision);
        } else {
            // Je liste le cas pour dire qu'il n'est pas calculable, ces cas seront fermées mais pas réouverts !
            listInfoCas(casNonCalculable, decision.getDemande(), false, "", "");
        }
        decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
    }

    private Revenu traitementRevenuPourAdaptationAF(Revenu revenu, Demande demande) {
        if (listMontantAFAvecChangement.containsKey(revenu.getSimpleDonneeFinanciere().getValeur())) {
            revenu.getSimpleDonneeFinanciere().setValeur(
                    listMontantAFAvecChangement.get(revenu.getSimpleDonneeFinanciere().getValeur()));
        } else if (!listMontantAFSansChangement.contains(revenu.getSimpleDonneeFinanciere().getValeur())) {
            if (demande.getSituationFamiliale().getRequerant().getSimpleRequerant().getIdMembreFamille()
                    .equals(revenu.getMembreFamille().getSimpleMembreFamille().getIdMembreFamille())) {
                listInfoCasPourAF(listeCasAvecMontantAFDifferent, demande, revenu, true);
            } else {
                listInfoCasPourAF(listeCasAvecMontantAFDifferent, demande, revenu, false);
            }

        }
        return revenu;
    }

    private Demande updateDateEtTypeNouvelleDemande(Demande nouvelleDemande) throws Exception {
        String dateDebut = "01." + moisActuel;
        nouvelleDemande.getSimpleDemande().setDateDebut(dateDebut);
        nouvelleDemande.getSimpleDemande().setDateFin("");
        nouvelleDemande.getSimpleDemande().setDateDepot(dateDebut);
        nouvelleDemande.getSimpleDemande().setTypeDemande(CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem());

        return PerseusServiceLocator.getDemandeService().update(nouvelleDemande);
    }
}
