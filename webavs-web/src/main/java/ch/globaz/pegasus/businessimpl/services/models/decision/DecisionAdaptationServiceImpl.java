package ch.globaz.pegasus.businessimpl.services.models.decision;

import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.external.IntRole;
import globaz.pegasus.utils.AmalUtilsForDecisionsPC;
import globaz.pegasus.utils.PCUserHelper;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitSearch;
import ch.globaz.pegasus.business.models.process.adaptation.DemandePcaPrestation;
import ch.globaz.pegasus.business.models.process.adaptation.DemandePcaPrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.DecisionAdaptationService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class DecisionAdaptationServiceImpl extends PegasusAbstractServiceImpl implements DecisionAdaptationService {

    public void checkAdresse(DemandePcaPrestation demandePcaPrestation) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {

        checkAdresseCourrier(demandePcaPrestation.getSimplePrestationsAccordees().getIdTiersBeneficiaire());

        if (!JadeStringUtil.isBlankOrZero(demandePcaPrestation.getSimplePrestationsAccordees().getMontantPrestation())) {
            checkAdressePaiement(demandePcaPrestation.getIdTierAdressePaiement());
        }

        if ((demandePcaPrestation.getSimplePrestationsAccordeesConjoint() != null)
                && !JadeStringUtil.isBlankOrZero(demandePcaPrestation.getSimplePrestationsAccordeesConjoint()
                        .getIdTiersBeneficiaire())) {

            checkAdresseCourrier(demandePcaPrestation.getSimplePrestationsAccordeesConjoint().getIdTiersBeneficiaire());

            if (!JadeStringUtil.isBlankOrZero(demandePcaPrestation.getSimplePrestationsAccordees()
                    .getMontantPrestation())) {
                checkAdressePaiement(demandePcaPrestation.getIdTierAdressePaiementConjoint());
            }
        }
    }

    public void checkAdresseCourrier(final String idTiers) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        // On envelope ce cheni car derrière ce services on utilise l'ancienne persistance.!!!
        OldPersistence<AdresseTiersDetail> oldPer = new OldPersistence<AdresseTiersDetail>() {
            @Override
            public AdresseTiersDetail action() throws Exception {
                return TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, Boolean.TRUE,
                        JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        AdresseService.CS_TYPE_COURRIER, null);

            }
        };
        AdresseTiersDetail adresse;
        try {
            adresse = oldPer.execute();
        } catch (Exception e) {
            throw new DecisionException("Unabled to checkAdresseCourrier", e);
        }

        if (JadeStringUtil.isEmpty(adresse.getAdresseFormate())) {
            String[] p = new String[1];
            p[0] = PCUserHelper.getDetailAssure(idTiers);
            JadeThread.logError("", "pegasus.process.adaptaion.validation.adrressCourierNull", p);
        }
    }

    private void checkAdressePaiement(final String idTierAdressePaiement)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        OldPersistence<AdresseTiersDetail> oldPer = new OldPersistence<AdresseTiersDetail>() {
            @Override
            public AdresseTiersDetail action() throws Exception {
                return TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTierAdressePaiement,
                        Boolean.TRUE, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        JACalendar.todayJJsMMsAAAA(), null);

            }
        };

        AdresseTiersDetail adresse;
        try {
            adresse = oldPer.execute();
        } catch (Exception e) {
            throw new DecisionException("Unabled to checkAdressePaiement", e);
        }

        if (JadeStringUtil.isEmpty(adresse.getAdresseFormate())) {
            String[] p = new String[1];
            p[0] = PCUserHelper.getDetailAssure(idTierAdressePaiement);
            JadeThread.logError(this.getClass().getName(), "pegasus.process.adaptaion.validation.adrressPaiementNull",
                    p);
        }

    }

    /**
     * cloture la prestations accordees (repracc) si la date de fin est vide
     * 
     * @param pca
     */
    private void closePrestationsDateFin(DemandePcaPrestation pca) {
        // req
        if (JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordees().getDateFinDroit())) {
            pca.getSimplePrestationsAccordees()
                    .setDateFinDroit(pca.getSimplePrestationsAccordees().getDateDebutDroit());
        }
        // con
        if (JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint().getDateFinDroit())) {
            pca.getSimplePrestationsAccordeesConjoint().setDateFinDroit(
                    pca.getSimplePrestationsAccordeesConjoint().getDateDebutDroit());
        }
    }

    private List<SimpleDecisionHeader> createDecisionsAdaptation(DemandePcaPrestation pcaRequerant,
            DemandePcaPrestation pcaConjoint, String date) throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        List<SimpleDecisionHeader> result = new ArrayList<SimpleDecisionHeader>();
        SimpleDecisionHeader simpleDecisionHeader1 = null;
        SimpleDecisionHeader simpleDecisionHeader2 = null;
        try {

            // création systématique de la décision pour le requerant
            simpleDecisionHeader1 = createSimpleDecisionAndSimpleValidation(pcaRequerant, pcaRequerant
                    .getSimplePrestationsAccordees().getIdTiersBeneficiaire(), date);
            result.add(simpleDecisionHeader1);

            if (pcaConjoint != null) {
                // si on est dans un cas de couple couple séparé par maladie

                // vérification qu'on ne soit pas dans la situation d'un couple séparé par maladie ET couple à domicile
                // avec 2 rentes (situation impossible)
                if (!JadeStringUtil.isEmpty(pcaConjoint.getSimplePrestationsAccordeesConjoint()
                        .getIdTiersBeneficiaire())) {
                    throw new AdaptationException("2 PCA was founded the idTiersBeneficiaireConjoint id fille");
                }

                // création de la décision pour le conjoint
                simpleDecisionHeader2 = createSimpleDecisionAndSimpleValidation(pcaConjoint, pcaConjoint
                        .getSimplePrestationsAccordees().getIdTiersBeneficiaire(), date);
            } else if (!JadeStringUtil.isEmpty(pcaRequerant.getSimplePrestationsAccordeesConjoint()
                    .getIdTiersBeneficiaire())) {
                // si on est dans un cas de couple couple avec 2 rente principal (DOM2R)
                // on modifie la pca du requerant en mémoire pour avoir la pca du conjoint. Car la décision pointe sur
                // la même pca

                String idTiersConjont = pcaRequerant.getSimplePrestationsAccordeesConjoint().getIdTiersBeneficiaire();

                simpleDecisionHeader2 = createSimpleDecisionAndSimpleValidation(pcaRequerant, idTiersConjont, date);

                // ON rempli la décision du conjoint seulment pour les DOM2R
                miseAjourDesDecisionLiees(simpleDecisionHeader1, simpleDecisionHeader2);
            }
            if (simpleDecisionHeader2 != null) {
                result.add(simpleDecisionHeader2);
            }

        } catch (DecisionException e) {
            throw new AdaptationException("Unable to create the simpleDecisionHeader ", e);
        }

        return result;
    }

    private SimpleDecisionHeader createSimpleDecisionAndSimpleValidation(final DemandePcaPrestation pca,
            final String idTiersBeneficaire, final String date) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // création du decision header
        SimpleDecisionHeader simpleDecisionHeader = new SimpleDecisionHeader();

        simpleDecisionHeader.setCsEtatDecision(IPCDecision.CS_ETAT_DECISION_VALIDE);
        simpleDecisionHeader.setCsGenreDecision(IPCDecision.CS_GENRE_DECISION);
        simpleDecisionHeader.setCsTypeDecision(IPCDecision.CS_TYPE_ADAPTATION_AC);
        simpleDecisionHeader.setDateDebutDecision(pca.getSimplePCAccordee().getDateDebut());
        simpleDecisionHeader.setDateFinDecision(pca.getSimplePCAccordee().getDateFin());
        // simpleDecisionHeader.setDateDecision(JadeDateUtil.addMonths( JACalendar.todayJJsMMsAAAA(), 1));
        // On fait moins un mois pour retrouvé les décision dans la recap.
//        simpleDecisionHeader.setDateDecision(JadeDateUtil.addMonths("01." + date, -1));
        // PC Reforme - Correction sur date d'adaptation
        if (isMonthOnDate(date)) {
            simpleDecisionHeader.setDateDecision(date);
        } else {
            simpleDecisionHeader.setDateDecision(JadeDateUtil.addMonths("01." + date, -1));
        }
        simpleDecisionHeader.setDatePreparation(JACalendar.todayJJsMMsAAAA());
        simpleDecisionHeader.setDateValidation(JACalendar.todayJJsMMsAAAA());
        // simpleDecisionHeader.setIdDecisionConjoint(pca.getSimpleInformationsComptabiliteConjoint().get)
        simpleDecisionHeader.setIdTiersBeneficiaire(idTiersBeneficaire);
        simpleDecisionHeader.setIdTiersCourrier(idTiersBeneficaire);
        simpleDecisionHeader.setNoDecision(""); // pas de numero de décision car on fait une communication
        simpleDecisionHeader.setPreparationPar(pca.getSimpleDemande().getIdGestionnaire());
        simpleDecisionHeader.setValidationPar(pca.getSimpleDemande().getIdGestionnaire());

        // Fait dans la fonction create
        // simpleDecisionHeader.setNoDecision(Pegasus)
        simpleDecisionHeader = PegasusImplServiceLocator.getSimpleDecisionHeaderService().create(simpleDecisionHeader);

        // création de la décision AC d'adaptation
        SimpleDecisionApresCalcul simpleDecisionApresCalcul = new SimpleDecisionApresCalcul();

        simpleDecisionApresCalcul.setAllocNonActif(false);
        simpleDecisionApresCalcul.setAnnuleEtRemplacePrec(false);
        simpleDecisionApresCalcul.setCodeAmal(EPCCodeAmal.CODE_ADAPTATION.getProperty());
        simpleDecisionApresCalcul.setDateDecisionAmal("");
        simpleDecisionApresCalcul.setCsTypePreparation(IPCDecision.CS_PREP_STANDARD);

        simpleDecisionApresCalcul.setDateProchainPaiement(""); // Utilsé pour les décisions de type courantes
        simpleDecisionApresCalcul.setDiminutionPc(false);
        simpleDecisionApresCalcul.setIdDecisionHeader(simpleDecisionHeader.getIdDecisionHeader());
        simpleDecisionApresCalcul.setIdVersionDroit(pca.getSimpleVersionDroit().getIdVersionDroit());
        simpleDecisionApresCalcul.setIntroduction("");
        simpleDecisionApresCalcul.setIsMostRecent(Boolean.TRUE);
        simpleDecisionApresCalcul.setRemarqueGenerale("");

        simpleDecisionApresCalcul = PegasusImplServiceLocator.getSimpleDecisionApresCalculService().create(
                simpleDecisionApresCalcul);

        // création de la validation de la décision
        SimpleValidationDecision simpleValidationDecision = new SimpleValidationDecision();
        simpleValidationDecision.setIdDecisionHeader(simpleDecisionHeader.getId());
        simpleValidationDecision.setIdPCAccordee(pca.getSimplePCAccordee().getId());

        PegasusImplServiceLocator.getSimpleValidationService().create(simpleValidationDecision);
        //
        // try {
        // PegasusImplServiceLocator.getPrepareAnnonceLapramsService().genereAnnonceLapramsValidation(
        // pca.getSimpleVersionDroit(), simpleDecisionHeader, simpleValidationDecision);
        // } catch (AnnonceException e) {
        // new DecisionException("Error durring create LAPRAMS", e);
        // }

        return simpleDecisionHeader;
    }

    private boolean isMonthOnDate(String date) {
        return date.length() > 7;
    }

    private void filleListPca(DemandePcaPrestationSearch search, List<DemandePcaPrestation> listeNewPca,
            List<DemandePcaPrestation> listeOldPca) throws PropertiesException, JadeNoBusinessLogSessionError {
        Boolean isAi = null;
        for (JadeAbstractModel model : search.getSearchResults()) {
            DemandePcaPrestation pca = (DemandePcaPrestation) model;
            if (isAi == null) {
                isAi = pca.getSimplePCAccordee().getCsTypePC().equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
            } else {
                if (!(isAi.equals(pca.getSimplePCAccordee().getCsTypePC().equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)))) {
                    if (EPCProperties.ADAPTATION_LEVEL_PASAGE_AVS_AI.getValue().equalsIgnoreCase("WARNING")) {
                        JadeThread.logWarn("", "pegasus.process.adaptation.passageAvsAi");
                    } else {
                        JadeThread.logError("", "pegasus.process.adaptation.passageAvsAi");
                    }
                }
            }

            // On prend la nouvelle pca
            if (JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getDateFin()) || isDateDeFinForceRefus(pca)) {
                listeNewPca.add(pca);
            } else {
                listeOldPca.add(pca);
            }
        }
    }

    private boolean isDateDeFinForceRefus(DemandePcaPrestation pca) {
        return !JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getDateFin())
                && pca.getSimplePCAccordee().getIsDateFinForce();
    }

    private DemandePcaPrestationSearch findAllOldVersionPCA(DemandePcaPrestation pca) throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DemandePcaPrestationSearch search = new DemandePcaPrestationSearch();
        search.setWhereKey(DemandePcaPrestationSearch.FOR_PCA_COURANTE_VALIDE);
        search.setForIdDroit(pca.getSimpleVersionDroit().getIdDroit());
        search.setForCsEtatPCA(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        search.setForLessNoVersion(pca.getSimpleVersionDroit().getNoVersion());
        return PegasusImplServiceLocator.getAdaptationService().search(search);
    }

    private void findAndSetIdCompteAnnexe(DemandePcaPrestation pca) throws JadePersistenceException,
            PCAccordeeException {
        String iDCompteAnnexe = null;
        try {
            // On s'assure que le compte annexe est bien setter sinon le creer ou le retroune
            if (JadeStringUtil.isBlankOrZero(pca.getSimpleInformationsComptabilite().getIdCompteAnnexe())) {

                iDCompteAnnexe = CABusinessServiceLocator.getCompteAnnexeService()
                        .getCompteAnnexe(null, pca.getIdTiersBeneficiaire(), IntRole.ROLE_RENTIER, pca.getNss(), true)
                        .getIdCompteAnnexe();

                pca.getSimpleInformationsComptabilite().setIdCompteAnnexe(iDCompteAnnexe);
            }

            if (JadeStringUtil.isBlankOrZero(pca.getSimpleInformationsComptabiliteConjoint().getIdCompteAnnexe())) {
                if (iDCompteAnnexe == null) {
                    iDCompteAnnexe = CABusinessServiceLocator
                            .getCompteAnnexeService()
                            .getCompteAnnexe(null, pca.getIdTiersBeneficiaire(), IntRole.ROLE_RENTIER, pca.getNss(),
                                    true).getIdCompteAnnexe();

                }
                pca.getSimpleInformationsComptabiliteConjoint().setIdCompteAnnexe(iDCompteAnnexe);
            }
        } catch (JadeApplicationException e) {
            throw new PCAccordeeException("Unable to create or get the compteAnnex", e);
        }
    }

    private DemandePcaPrestationSearch findNewPCA(String idDemande) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, AdaptationException {
        DemandePcaPrestationSearch search = new DemandePcaPrestationSearch();
        search.setForCsEtatPCA(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        search.setForIdDemande(idDemande);
        search.setWhereKey(DemandePcaPrestationSearch.WITH_COPIE_PCA);
        PegasusImplServiceLocator.getAdaptationService().search(search);
        return search;
    }

    private void historiserAllOldVersionPCA(DemandePcaPrestation demandePcaPrestation) throws JadePersistenceException,
            JadeApplicationException {
        DemandePcaPrestationSearch search = findAllOldVersionPCA(demandePcaPrestation);
        boolean updateVersionDroit = true;
        if (!("0").equals(demandePcaPrestation.getSimpleVersionDroit().getNoVersion()) && (search.getSize() == 0)) {
            throw new AdaptationException("Any old version(" + search.getForLessNoVersion() + ") pca was founded");
        }

        if (search.getSize() > 2) {
            throw new AdaptationException("Plus de 2 pca à historiser");
        }

        for (JadeAbstractModel model : search.getSearchResults()) {
            DemandePcaPrestation pca = (DemandePcaPrestation) model;

            historiserOldVersionPCA(pca);
            // on update une seule fois la table des version des droit pour une version donnée
            if (updateVersionDroit) {
                PegasusImplServiceLocator.getSimpleVersionDroitService().update(pca.getSimpleVersionDroit());
                updateVersionDroit = false;
            }
        }

    }

    private void historiserOldVersionPCA(DemandePcaPrestation pca) throws JadePersistenceException,
            JadeApplicationException {
        pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        // Date de fin repracc
        closePrestationsDateFin(pca);
        pca.getSimplePCAccordee().setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE);
        pca.getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_HISTORISE);
        updateForValidation(pca);
    }

    private void miseAjourDesDecisionLiees(SimpleDecisionHeader simpleDecisionHeader1,
            SimpleDecisionHeader simpleDecisionHeader2) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AdaptationException {
        if (simpleDecisionHeader2 != null) {
            simpleDecisionHeader1.setIdDecisionConjoint(simpleDecisionHeader2.getId());
            simpleDecisionHeader2.setIdDecisionConjoint(simpleDecisionHeader1.getId());

            try {
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(simpleDecisionHeader1);
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(simpleDecisionHeader2);
            } catch (DecisionException e) {
                throw new AdaptationException("Unable to update the simpleDecisionHeader ", e);
            }
        }
    }

    private List<SimpleDecisionHeader> prepareAndValideDecision(List<DemandePcaPrestation> listeNewPca,
            List<DemandePcaPrestation> listeOldPca, String date) throws JadePersistenceException,
            JadeApplicationException {

        if (listeNewPca.size() > 2) {
            throw new AdaptationException("More than 2 new pac was found");
        } else if (listeNewPca.size() == 0) {
            throw new DecisionException("Any one new PCA founded");
        }

        if (listeOldPca.size() > 2) {
            throw new AdaptationException("More than 2 copy pac was found");
        } else if (listeOldPca.size() == 0) {
            throw new DecisionException("Aucune copy de PCA n'a été trouvée");
        }

        DemandePcaPrestation pcaRequerant = listeNewPca.get(0);
        DemandePcaPrestation pcaConjoint = null;

        // TODO I18n
        // listeOldPca represante les copies
        // On est dans le cas d'un couple séparé par la maladie
        if (listeNewPca.size() == 2) {
            pcaConjoint = listeNewPca.get(1);
            if (listeOldPca.size() == 1) {
                JadeThread.logError("", "pegasus.process.adaptation.passage.coupleSeparer");
            }
        }
        if (listeOldPca.size() == 2) {
            if (listeNewPca.size() == 1) {
                JadeThread.logError("", "pegasus.process.adaptation.passage.coupleSeparer");
            }
        }

        // on s'assure que les pca du requerant et du conjoint soient réellement la pca du requerant et du conjoint
        if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(pcaRequerant.getSimplePCAccordee().getCsRoleBeneficiaire())) {
            pcaRequerant = listeNewPca.get(1);
            pcaConjoint = listeNewPca.get(0);
        }

        List<SimpleDecisionHeader> decisions = createDecisionsAdaptation(pcaRequerant, pcaConjoint, date);

        historiserAllOldVersionPCA(pcaRequerant);

        validerAllNewVersionPCA(listeNewPca, listeOldPca);

        return decisions;
    }

    @Override
    public List<SimpleDecisionHeader> preparerValiderDecision(String idDemande, String date)
            throws JadePersistenceException, JadeApplicationException {
        DemandePcaPrestationSearch search = findNewPCA(idDemande);
        List<DemandePcaPrestation> listeNewPca = new ArrayList<DemandePcaPrestation>();
        List<DemandePcaPrestation> listeOldPca = new ArrayList<DemandePcaPrestation>();
        if (search.getSize() > 0) {
            filleListPca(search, listeNewPca, listeOldPca);
            for (DemandePcaPrestation demandePcaPrestation : listeNewPca) {
                // On vérifie que la personne ai bien une adresse de courrier et une adrese de paiment
                checkAdresse(demandePcaPrestation);
            }
            return prepareAndValideDecision(listeNewPca, listeOldPca, date);
        } else {
            VersionDroitSearch searchDroit = new VersionDroitSearch();
            searchDroit.setForIdDemandePc(idDemande);
            searchDroit.setWhereKey(VersionDroitSearch.CURRENT_VERSION);
            searchDroit = PegasusServiceLocator.getDroitService().searchVersionDroit(searchDroit);
            if (searchDroit.getSize() == 1) {
                VersionDroit droit = (VersionDroit) searchDroit.getSearchResults()[0];
                if (!IPCDroits.CS_CALCULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
                    String[] param = new String[1];
                    param[0] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                            droit.getSimpleVersionDroit().getCsEtatDroit());
                    JadeThread.logError(this.getClass().getName(),
                            "pegasus.decisionAdaptation.preparerValiderDecision.etatDroitNonValide", param);
                }
            }
            throw new AdaptationException("Aucune PCA trouvée valable pour la validation");
        }
    }

    private void updateForValidation(final DemandePcaPrestation pca) throws JadePersistenceException,
            JadeApplicationException {

        PegasusImplServiceLocator.getSimplePCAccordeeService().update(pca.getSimplePCAccordee());
        PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(pca.getSimplePrestationsAccordees());
        CorvusServiceLocator.getSimpleInformationsComptabiliteService().update(pca.getSimpleInformationsComptabilite());

        if (!JadeStringUtil.isIntegerEmpty(pca.getSimplePrestationsAccordeesConjoint().getId())) {

            // Si on à la même infoComptabilite on ne fait pas d'update (Pour géréer les cas de reprise), Si on ne fait
            // pas cela on vas avoir une erreur d'acces concurent
            if (!pca.getSimpleInformationsComptabilite().getIdInfoCompta()
                    .equals(pca.getSimpleInformationsComptabiliteConjoint().getIdInfoCompta())) {
                CorvusServiceLocator.getSimpleInformationsComptabiliteService().update(
                        pca.getSimpleInformationsComptabiliteConjoint());

            }

            PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                    pca.getSimplePrestationsAccordeesConjoint());

        }
    }

    private void validerAllNewVersionPCA(List<DemandePcaPrestation> listeNewPca,
            List<DemandePcaPrestation> listeCopiePca) throws JadePersistenceException, JadeApplicationException {

        for (DemandePcaPrestation pca : listeNewPca) {
            validerNewVersionPCA(pca);
        }

        for (DemandePcaPrestation pca : listeCopiePca) {
            validerNewVersionPCA(pca);
        }

        PegasusImplServiceLocator.getSimpleVersionDroitService().update(listeNewPca.get(0).getSimpleVersionDroit());
    }

    private void validerNewVersionPCA(DemandePcaPrestation pca) throws JadePersistenceException,
            JadeApplicationException {

        // On ne continue pas le traitement si il y a des erreurs, car on risque d'avoir d'autre message d'erreur du a
        // l'encienne percistance
        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            findAndSetIdCompteAnnexe(pca);

            pca.getSimplePCAccordee().setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
            pca.getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_VALIDE);
            pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
            pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
            updateForValidation(pca);
        }
    }
}
