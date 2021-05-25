/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import ch.globaz.pegasus.business.models.decision.*;
import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.AmalUtilsForDecisionsPC;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.EPCCodeAmal;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.DecisionApresCalculService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionHeaderChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.decision.AnnexesDecisionHandler;
import ch.globaz.pegasus.businessimpl.utils.decision.CopiesDecisionHandler;
import ch.globaz.pegasus.businessimpl.utils.plancalcul.PCPlanCalculHandlerOO;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author SCE 29 juil. 2010
 */
public class DecisionApresCalculServiceImpl extends PegasusAbstractServiceImpl implements DecisionApresCalculService {

    public final static String DEMANDE_DU = "{date_demande}";

    @Override
    public DocumentData buildPlanCalculDocumentData(String idDecisionApresCalcul, boolean isWithMemmbreFamilles, boolean isRetenu,String dateAdaptation)
            throws Exception {
        if (idDecisionApresCalcul == null) {
            throw new DecisionException("Unable to buil plan de calcul, the id Decision passed is null!");
        }

        // DAC oo
        DecisionApresCalculOO dacOO;
        dacOO = readForOO(idDecisionApresCalcul);
        dacOO.setDateAdaptation(dateAdaptation);
        if (dacOO == null) {
            throw new DecisionException(
                    "Unable to retrieve DecisionOO, the decision is null! Check the id Passed for decision is right!");

        }
        DocumentData data = new DocumentData();
        // Textes babel
        Map<Langues, CTDocumentImpl> documentsBabel;
        documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForTypeDecision(
                IPCCatalogueTextes.BABEL_DOC_NAME_PLAN_CALCUL);
        Langues langueTiers = LanguageResolver.resolveISOCode(dacOO.getDecisionHeader().getPersonneEtendue().getTiers()
                .getLangue());
        ICTDocument babelDoc = documentsBabel.get(langueTiers);
        // Tupleroot
        byte[] bytePlanCalcul;
        if (isRetenu) {
            bytePlanCalcul = dacOO.getPlanCalcul().getResultatCalcul();
        } else {
            bytePlanCalcul = dacOO.getPlanCalculNonRetenu().getResultatCalcul();
        }
        if (bytePlanCalcul == null) {
            return null;
        }
        String byteArrayToString = new String(bytePlanCalcul);
        TupleDonneeRapport tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                byteArrayToString);

        return new PCPlanCalculHandlerOO().build(babelDoc, dacOO, data, tupleRoot, isWithMemmbreFamilles, isRetenu);

    }

    // /**
    // * Verification des dates de la decision après le dernier paiement, et avant le prochain paiement
    // *
    // * @param decision
    // * @throws PmtMensuelException
    // * @throws JadeApplicationServiceNotAvailableException
    // * @throws DecisionException
    // */
    // private void checkDateDecision(DecisionApresCalcul decision) throws PmtMensuelException,
    // JadeApplicationServiceNotAvailableException, DecisionException {
    // // vérifie que la date de décision soit après le dernier paiement
    // String dateDernierPmt = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
    // String dateProchainPmt = "01." + PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
    // if (JadeDateUtil.isDateBefore(decision.getDecisionHeader().getSimpleDecisionHeader().getDateDecision(),
    // dateDernierPmt)) {
    // JadeThread.logError(decision.getClass().getName(),
    // "pegasus.simpleDecisionHeader.dateDecision.integrity.dateDernierPaiement");
    // }
    // if (!JadeDateUtil.isDateBefore(decision.getDecisionHeader().getSimpleDecisionHeader().getDateDecision(),
    // dateProchainPmt)) {
    // JadeThread.logError(decision.getClass().getName(),
    // "pegasus.simpleDecisionHeader.dateDecision.integrity.dateProchainPaiement");
    // }
    // }

    /**
     * Copie d'une décision
     * 
     * @param decToCopy
     * @return
     */
    private DecisionApresCalcul copyDecision(DecisionApresCalcul decToCopy) {

        DecisionApresCalcul dec = new DecisionApresCalcul();
        dec.setId(decToCopy.getId());
        dec.getDecisionHeader().setSimpleDecisionHeader(decToCopy.getDecisionHeader().getSimpleDecisionHeader());
        dec.setPcAccordee(decToCopy.getPcAccordee());
        dec.setSimpleDecisionApresCalcul(decToCopy.getSimpleDecisionApresCalcul());
        dec.setSimpleValidationDecision(decToCopy.getSimpleValidationDecision());
        dec.setSpy(decToCopy.getSpy());
        dec.setVersionDroit(decToCopy.getVersionDroit());

        return dec;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. DecisionApresCalculService
     * #count(ch.globaz.pegasus.business.models.decision .SimpleDecisionRefusSearch)
     */
    @Override
    public int count(DecisionApresCalculSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count DecisionAprescalcul, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /**
     * Methode calculant le nombre de décision pour une version de droit
     * 
     * @param idVersionDroit
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    private int countForVersionDroit(String idVersionDroit) throws DecisionException, JadePersistenceException {
        if (idVersionDroit == null) {
            throw new DecisionException(
                    "Unable to count DecisionAprescalcul for VersionDroit, the idVersion droit passed is null!");
        }

        DecisionApresCalculSearch decisionApresCalculSearch = new DecisionApresCalculSearch();
        decisionApresCalculSearch.setWhereKey("forSpecificVersionDroit");
        decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
        return JadePersistenceManager.count(decisionApresCalculSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. DecisionApresCalculService
     * #create(ch.globaz.pegasus.business.models.decision.DecisionApresCalcul)
     */
    @Override
    public DecisionApresCalcul create(DecisionApresCalcul decision) throws DecisionException, JadePersistenceException {
        return null;
    }

    @Override
    public void createCourantDecision(DecisionApresCalcul decision, PCAccordeeSearch pcaSearchForDac,
            Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal) throws Exception {

        if (decision == null) {
            throw new DecisionException("Unable to create DecisionApresCalcul, type COURANT, the model passed is null!");
        }

        try {
            // Recherche si decision existantes
            if (countForVersionDroit(getIdVersionDroit(decision)) > 0) {
                PegasusImplServiceLocator.getCleanDecisionService().deleteDecisionsForVersion(
                        getIdVersionDroit(decision));
            }
            // Check de la date de décision
            SimpleDecisionHeaderChecker.checkForCoherenceDateDecision(decision.getDecisionHeader()
                    .getSimpleDecisionHeader());

            // depot de la decision en param dans local
            DecisionApresCalcul decisionACreer = copyDecision(decision);

            // on set la pcaccordee
            PCAccordee pca = ((PCAccordee) pcaSearchForDac.getSearchResults()[0]);
            // oin set isMostRecent
            decisionACreer.getSimpleDecisionApresCalcul().setIsMostRecent(Boolean.TRUE);
            decisionACreer.setPcAccordee(pca);

            // plan calcul
            SimplePlanDeCalculSearch plancalculsearch = new SimplePlanDeCalculSearch();
            plancalculsearch.setForIdPCAccordee(pca.getSimplePCAccordee().getIdPCAccordee());
            plancalculsearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(plancalculsearch);

            decisionACreer.setPlanCalcul((SimplePlanDeCalcul) plancalculsearch.getSearchResults()[0]);

            // on set la date de debut avec la date du prochain paiement
            decisionACreer.getDecisionHeader().getSimpleDecisionHeader()
                    .setDateDebutDecision(PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());

            // on set type prep
            decisionACreer.getSimpleDecisionApresCalcul().setCsTypePreparation(IPCDecision.CS_PREP_COURANT);

            // Si amalUtil défini, on gère, sinon code undefined, cas des adaptataions
            if (listeAmal == null) {
                // on set code amal undefined
                decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(EPCCodeAmal.CODE_UNDEFINED.getProperty());
            } else {
                // TODO a corriger!!!!!!!!!!!
                AmalUtilsForDecisionsPC amalUtil = new AmalUtilsForDecisionsPC();
                AmalUtilsForDecisionsPC.setListeAmal(listeAmal);
                AmalUtilsForDecisionsPC.checkAndGenerateWarningCoherenceWithAmal(decision.getVersionDroit().getId(),
                        BSessionUtil.getSessionFromThreadContext());
                // On set date de decision amal
                decisionACreer.getSimpleDecisionApresCalcul().setDateDecisionAmal(
                        AmalUtilsForDecisionsPC.getDateDecisionAmalForTiersByPeriod(pca.getSimplePCAccordee()
                                .getDateDebut(), pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire()));
                // on set code amal
                decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(getCodeAmal(decisionACreer, amalUtil));

            }
            dealDecisionCreation(decisionACreer, true);
        } catch (PmtMensuelException e) {
            throw new DecisionException("Paiement mensuel service not available - ", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDecisionsWithConjoints(DecisionApresCalcul decision, Boolean isPcaCourante) throws Exception {

        // Id decision conjoint, et insertion decision conjoint
        String idCon = createSingleDAC(decision, true, isPcaCourante).getDecisionHeader().getId();
        // Set idConjojt pour requerant et insertion
        decision.getDecisionHeader().getSimpleDecisionHeader().setIdDecisionConjoint(idCon);

        // on vide la liste des annexes et copies pour que les elements ne soient pas a double
        decision.getDecisionHeader().setListeAnnexes(new ArrayList<SimpleAnnexesDecision>());
        decision.getDecisionHeader().setListeCopies(new ArrayList<CopiesDecision>());

        String idReq = createSingleDAC(decision, false, isPcaCourante).getDecisionHeader().getId();
        // conjoint
        // chargement et update conjoint avec idRequerant
        SimpleDecisionHeader headerConjoint = PegasusImplServiceLocator.getSimpleDecisionHeaderService().read(idCon);
        headerConjoint.setIdDecisionConjoint(idReq);
        PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(headerConjoint);

    }

    @Override
    public void createRetroDecision(DecisionApresCalcul decision, PCAccordeeSearch pcaSearchForDac,
            Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal) throws Exception {
        if (decision == null) {
            throw new DecisionException("Unable to create DecisionApresCalcul, type RETRO, the model passed is null!");
        }

        try {
            // Check de la date de décision
            SimpleDecisionHeaderChecker.checkForCoherenceDateDecision(decision.getDecisionHeader()
                    .getSimpleDecisionHeader());

            // Recherche de la decision courante déjà créer
            DecisionApresCalculSearch currentDecision = new DecisionApresCalculSearch();
            currentDecision
                    .setForIdVersionDroit(decision.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());
            currentDecision.setForCsTypePreparation(IPCDecision.CS_PREP_COURANT);
            currentDecision = PegasusServiceLocator.getDecisionApresCalculService().search(currentDecision);
            // Recup date de debut decision courante
            DecisionApresCalcul current = (DecisionApresCalcul) currentDecision.getSearchResults()[0];
            String beginCurrentDecisionDate = "01."
                    + current.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision();

            // Iteration sur les pca
            for (JadeAbstractModel model : pcaSearchForDac.getSearchResults()) {
                PCAccordee pca = (PCAccordee) model;
                // reprise de la decision passé en param
                DecisionApresCalcul decisionACreer = copyDecision(decision);
                // on set la pcaccordee
                decisionACreer.setPcAccordee(pca);
                // plan calcul
                SimplePlanDeCalculSearch plancalculsearch = new SimplePlanDeCalculSearch();
                plancalculsearch.setForIdPCAccordee(pca.getSimplePCAccordee().getIdPCAccordee());
                plancalculsearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(plancalculsearch);

                decisionACreer.setPlanCalcul((SimplePlanDeCalcul) plancalculsearch.getSearchResults()[0]);

                // on set le date debut d'après les pca
                decisionACreer.getDecisionHeader().getSimpleDecisionHeader()
                        .setDateDebutDecision(pca.getSimplePCAccordee().getDateDebut());

                // On ne définit pas de date de fin si la date de fin est forcé.
                if (!pca.getSimplePCAccordee().getIsDateFinForce()) {
                    if (JadeStringUtil.isEmpty(pca.getSimplePCAccordee().getDateFin())) {
                        // Set date fin avec date prochain paiement
                        decisionACreer.getDecisionHeader().getSimpleDecisionHeader()
                                .setDateFinDecision(JadeDateUtil.addMonths(beginCurrentDecisionDate, -1).substring(3));
                    } else {
                        decisionACreer.getDecisionHeader().getSimpleDecisionHeader()
                                .setDateFinDecision(pca.getSimplePCAccordee().getDateFin());
                    }
                }
                // on set type pre
                decisionACreer.getSimpleDecisionApresCalcul().setCsTypePreparation(IPCDecision.CS_PREP_RETRO);

                // Si amalUtil défini, on gère, sinon code undefined, cas des adaptataions
                if (listeAmal == null) {
                    // on set code amal undefined
                    decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(EPCCodeAmal.CODE_UNDEFINED.getProperty());
                } else {
                    // TODO a corriger!!!!!!!!!!!
                    AmalUtilsForDecisionsPC amalUtil = new AmalUtilsForDecisionsPC();
                    AmalUtilsForDecisionsPC.setListeAmal(listeAmal);
                    AmalUtilsForDecisionsPC.checkAndGenerateWarningCoherenceWithAmal(
                            decision.getVersionDroit().getId(), BSessionUtil.getSessionFromThreadContext());
                    // On set date de decision amal
                    decisionACreer.getSimpleDecisionApresCalcul().setDateDecisionAmal(
                            AmalUtilsForDecisionsPC.getDateDecisionAmalForTiersByPeriod(pca.getSimplePCAccordee()
                                    .getDateDebut(), pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire()));
                    // on set code amal
                    decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(getCodeAmal(decisionACreer, amalUtil));

                }
                dealDecisionCreation(decisionACreer, false);

            }
        } catch (DecisionException e) {
            throw new DecisionException("Unable to create RetroDecision", e);
        }

    }

    /**
     * Création d'un décision après calcul
     * 
     * @param decision
     * @return
     * @throws Exception
     * @throws CatalogueTexteException
     */
    private DecisionApresCalcul createSingleDAC(DecisionApresCalcul decision, Boolean forConjoint, Boolean isPcaCourante)
            throws CatalogueTexteException, Exception {
        if (decision == null) {
            throw new DecisionException("Unable to create DecisionApresCalcul, the model passed is null!");
        }
        // récupération de la langue du tiers
        Langues langueTiers = LanguageResolver.resolveISOCode(decision.getPcAccordee().getPersonneEtendue().getTiers()
                .getLangue());
        // creation header decision
        try {

            // set planCalcul
            decision.setPlanCalcul(PegasusServiceLocator.getPCAccordeeService().findSimplePlanCalculeRetenu(
                    decision.getPcAccordee().getSimplePCAccordee().getId()));
            // Set tiers - requérant
            if (!forConjoint) {
                decision.getDecisionHeader().getSimpleDecisionHeader()
                        .setIdTiersBeneficiaire(getIdTiers(decision, IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
                // Set tiers courrier
                decision.getDecisionHeader().getSimpleDecisionHeader()
                        .setIdTiersCourrier(getIdTiers(decision, IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
            } else {
                decision.getDecisionHeader().getSimpleDecisionHeader()
                        .setIdTiersBeneficiaire(getIdTiers(decision, IPCDroits.CS_ROLE_FAMILLE_CONJOINT));
                // Set tiers courrier
                decision.getDecisionHeader().getSimpleDecisionHeader()
                        .setIdTiersCourrier(getIdTiers(decision, IPCDroits.CS_ROLE_FAMILLE_CONJOINT));

            }

            // Set etat - enregistré
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
            // Set type
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(getEtatDecision(decision));

            // Set annexes
            decision.getDecisionHeader().setListeAnnexes(
                    AnnexesDecisionHandler.getAnnexesList(decision.getVersionDroit(), isPcaCourante,
                            getEtatDecision(decision).equals(IPCDecision.CS_TYPE_REFUS_AC), decision.getPcAccordee()
                                    .getSimplePCAccordee().getDateDebut(), langueTiers));

            // Set copies
            decision.getDecisionHeader().setListeCopies(CopiesDecisionHandler.getCopiesList(decision));

            // on vide
            // creation header
            decision.setDecisionHeader(PegasusServiceLocator.getDecisionHeaderService().create(
                    decision.getDecisionHeader()));
            decision.getSimpleDecisionApresCalcul().setIdDecisionHeader(
                    decision.getDecisionHeader().getSimpleDecisionHeader().getId());

            // set id version droit
            decision.getSimpleDecisionApresCalcul().setIdVersionDroit(
                    decision.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

            if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                // Set type preparation
                // decision.getSimpleDecisionApresCalcul().setCsTypePreparation(IPCDecision.CS_GENRE_DECISION);
                // Set texte intro, par défaut texte genre décision
                decision.getSimpleDecisionApresCalcul().setIntroduction(
                        PRStringUtils.replaceString(BabelServiceLocator.getPCCatalogueTexteService()
                                .getTextIntroForDAC(langueTiers), DecisionApresCalculServiceImpl.DEMANDE_DU, decision
                                .getVersionDroit().getSimpleVersionDroit().getDateAnnonce()));

                // creation decision aprescalcul
                decision.setSimpleDecisionApresCalcul(PegasusImplServiceLocator.getSimpleDecisionApresCalculService()
                        .create(decision.getSimpleDecisionApresCalcul()));

                // Creation validation
                SimpleValidationDecision validation = new SimpleValidationDecision();
                validation.setIdDecisionHeader(decision.getDecisionHeader().getSimpleDecisionHeader()
                        .getIdDecisionHeader());
                validation.setIdPCAccordee(decision.getPcAccordee().getSimplePCAccordee().getIdPCAccordee());
                decision.setSimpleValidationDecision(PegasusImplServiceLocator.getSimpleValidationService().create(
                        validation));
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e);
        }

        return decision;
    }

    @Override
    public void createStandardDecision(DecisionApresCalcul decision) throws Exception {

        if (decision == null) {
            throw new DecisionException(
                    "Unable to create DecisionApresCalcul, type STANDARD, the model passed is null!");
        }

        PCAccordeeSearch pcAccordeeSearchDac = new PCAccordeeSearch();
        pcAccordeeSearchDac.setForVersionDroit(decision.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());
        pcAccordeeSearchDac.setOrderKey("byDateDebut");
        pcAccordeeSearchDac.setWhereKey("forCreateDAC");
        pcAccordeeSearchDac = PegasusServiceLocator.getPCAccordeeService().search(pcAccordeeSearchDac);

        this.createStandardDecision(decision, pcAccordeeSearchDac, null);
    }

    /**
     * Création des décsion de type STANDARD
     * 
     * @throws Exception
     */
    @Override
    public void createStandardDecision(DecisionApresCalcul decision, PCAccordeeSearch pcSearchForDac,
            Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal) throws Exception {

        if (decision == null) {
            throw new DecisionException(
                    "Unable to create DecisionApresCalcul, type STANDARD, the model passed is null!");
        }

        try {

            // Recherche si decisionApresCalculExistantes
            if (countForVersionDroit(getIdVersionDroit(decision)) > 0) {
                PegasusImplServiceLocator.getCleanDecisionService().deleteDecisionsForVersion(
                        getIdVersionDroit(decision));
            }

            // Check de l'integrité métier de la date de décision
            SimpleDecisionHeaderChecker.checkForCoherenceDateDecision(decision.getDecisionHeader()
                    .getSimpleDecisionHeader());
            String date = "";
            // Iteration sur les pca
            for (JadeAbstractModel pca : pcSearchForDac.getSearchResults()) {
                PCAccordee pcAccordee = (PCAccordee) pca;
                // reprise de la decision passé en param
                DecisionApresCalcul decisionACreer = copyDecision(decision);
                // Gestion most recente

                if ("".equals(date)) {
                    date = pcAccordee.getSimplePCAccordee().getDateDebut();
                    decisionACreer.getSimpleDecisionApresCalcul().setIsMostRecent(Boolean.TRUE);
                } else if (date.equals(pcAccordee.getSimplePCAccordee().getDateDebut())) {
                    decisionACreer.getSimpleDecisionApresCalcul().setIsMostRecent(Boolean.TRUE);
                } else {
                    decisionACreer.getSimpleDecisionApresCalcul().setIsMostRecent(Boolean.FALSE);
                }

                // on set la pcaccordee
                decisionACreer.setPcAccordee(pcAccordee);

                // plan calcul
                SimplePlanDeCalculSearch plancalculsearch = new SimplePlanDeCalculSearch();
                plancalculsearch.setForIdPCAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee());
                plancalculsearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(plancalculsearch);
                for(JadeAbstractModel model : plancalculsearch.getSearchResults()){
                    SimplePlanDeCalcul planDeCalcul = (SimplePlanDeCalcul)model;
                    if(planDeCalcul.getIsPlanRetenu()){
                        decisionACreer.setPlanCalcul(planDeCalcul);
                    }
                }
                // on set les dates debut et fin d'après les pca
                decisionACreer.getDecisionHeader().getSimpleDecisionHeader()
                        .setDateDebutDecision(pcAccordee.getSimplePCAccordee().getDateDebut());

                if (!pcAccordee.getSimplePCAccordee().getIsDateFinForce()) {
                    decisionACreer.getDecisionHeader().getSimpleDecisionHeader()
                            .setDateFinDecision(pcAccordee.getSimplePCAccordee().getDateFin());
                }
                // on set le type de preparation
                decisionACreer.getSimpleDecisionApresCalcul().setCsTypePreparation(IPCDecision.CS_PREP_STANDARD);

                // Si amalUtil défini, on gère, sinon code undefined, cas des adaptataions
                // et cas egalement lorsque la propriété n'est pas activé, cette valeur n'aura pas d'importance car elle
                // ne sera pas gere dans les écrans

                if (!PCproperties.getBoolean(EPCProperties.CHECK_AMAL_FOR_DECISION_ENABLE)) {
                    // on set code amal undefined
                    decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(EPCCodeAmal.CODE_STANDARD.getProperty());
                } else if (listeAmal == null) {
                    decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(EPCCodeAmal.CODE_UNDEFINED.getProperty());
                } else {
                    // TODO a adapter proprement!!!!!!!
                    AmalUtilsForDecisionsPC amalUtil = new AmalUtilsForDecisionsPC();
                    AmalUtilsForDecisionsPC.setListeAmal(listeAmal);
                    AmalUtilsForDecisionsPC.checkAndGenerateWarningCoherenceWithAmal(
                            decision.getVersionDroit().getId(), BSessionUtil.getSessionFromThreadContext());

                    // On set date de decision amal, si pas inconnu ou susbide pour période
                    if (!AmalUtilsForDecisionsPC.getIsInconnuAmal()
                            && AmalUtilsForDecisionsPC.hasSubsideByTiersForPeriod(pcAccordee.getSimplePCAccordee()
                                    .getDateDebut(), pcAccordee.getSimplePrestationsAccordees()
                                    .getIdTiersBeneficiaire())) {
                        decisionACreer.getSimpleDecisionApresCalcul().setDateDecisionAmal(
                                AmalUtilsForDecisionsPC.getDateDecisionAmalForTiersByPeriod(pcAccordee
                                        .getSimplePCAccordee().getDateDebut(), pcAccordee
                                        .getSimplePrestationsAccordees().getIdTiersBeneficiaire()));
                        // on set code amal
                        decisionACreer.getSimpleDecisionApresCalcul()
                                .setCodeAmal(getCodeAmal(decisionACreer, amalUtil));
                    } else {
                        decisionACreer.getSimpleDecisionApresCalcul().setCodeAmal(
                                EPCCodeAmal.CODE_UNDEFINED.getProperty());
                    }

                }
                boolean isPcaCourante = JadeStringUtil.isEmpty(pcAccordee.getSimplePCAccordee().getDateFin());

                // aiguille la creation de la decision
                dealDecisionCreation(decisionACreer, isPcaCourante);

            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e);
        }

    }

    private void dealDecisionCreation(DecisionApresCalcul decision, Boolean isPcaCourante)
            throws CatalogueTexteException, Exception {

        // raz id decision conjoint
        decision.getDecisionHeader().getSimpleDecisionHeader().setIdDecisionConjoint("");

        if (JadeStringUtil.isBlankOrZero(decision.getPcAccordee().getSimplePrestationsAccordeesConjoint().getId())) {
            createSingleDAC(decision, false, isPcaCourante);
        } else {
            // Creations des décision, conjoint et requérant
            createDecisionsWithConjoints(decision, isPcaCourante);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. DecisionApresCalculService
     * #delete(ch.globaz.pegasus.business.models.decision.DecisionApresCalcul)
     */
    @Override
    public void delete(ForDeleteDecisionSearch search) throws JadePersistenceException, DecisionException {
        if (search == null) {
            throw new DecisionException("unable to delete decisionApresCalcul, the model passed is null");
        }

        if (search.getSize() > 0) {
            List<String> idsDecisionHeader = new ArrayList<String>();

            for (JadeAbstractModel model : search.getSearchResults()) {
                ForDeleteDecision decision = (ForDeleteDecision) model;
                idsDecisionHeader.add(decision.getIdDecisionHeader());
                if (!(IPCDecision.CS_TYPE_ADAPTATION_AC.equals(decision.getCsTypeDecision())
                        || IPCDecision.CS_TYPE_OCTROI_AC.equals(decision.getCsTypeDecision())
                        || IPCDecision.CS_TYPE_PARTIEL_AC.equals(decision.getCsTypeDecision()) || IPCDecision.CS_TYPE_REFUS_AC
                            .equals(decision.getCsTypeDecision()))) {

                    throw new DecisionException(
                            "Unable to delete the decision apresCalcul because the idDecsionHeader("
                                    + decision.getIdDecisionHeader()
                                    + ") is not a decsionAprsCalcule("
                                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                            decision.getCsTypeDecision()) + ")");
                }

            }

            try {
                // Suppression copies
                PegasusImplServiceLocator.getSimpleCopiesDecisionsService().deleteForDecision(idsDecisionHeader);

                // Suppression annexes
                PegasusImplServiceLocator.getSimpleAnnexesDecisionService().deleteForDecision(idsDecisionHeader);

                // Delete simple dac
                PegasusImplServiceLocator.getSimpleDecisionApresCalculService().delete(idsDecisionHeader);
                // delete header
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().delete(idsDecisionHeader);

                PegasusImplServiceLocator.getSimpleValidationService().delete(idsDecisionHeader);

            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DecisionException("Service not available - " + e.getMessage());
            }
        }

    }

    private String getCodeAmal(DecisionApresCalcul dec, AmalUtilsForDecisionsPC amalUtil) throws DecisionException {
        String periode = dec.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision();
        String idTiers = dec.getPcAccordee().getSimplePrestationsAccordees().getIdTiersBeneficiaire();

        // check etat decision, pour définir le code amal
        if (getEtatDecision(dec).equals(IPCDecision.CS_TYPE_OCTROI_AC)) {
            if (AmalUtilsForDecisionsPC.getIsInconnuAmal()
                    || !AmalUtilsForDecisionsPC.hasSubsideByTiersForPeriod(periode, idTiers)) {
                return EPCCodeAmal.CODE_F.getProperty();
            }
            // Si subside P
            else if (AmalUtilsForDecisionsPC.getIfTypeDemandeByTiersForPeriodIsTypeP(periode, idTiers)) {
                return EPCCodeAmal.CODE_A.getProperty();
            }
            // code F
            else {
                return EPCCodeAmal.CODE_F.getProperty();
            }
        }
        // Octroi partiel
        else if (getEtatDecision(dec).equals(IPCDecision.CS_TYPE_PARTIEL_AC)) {
            if (AmalUtilsForDecisionsPC.getIsInconnuAmal()
                    || !AmalUtilsForDecisionsPC.hasSubsideByTiersForPeriod(periode, idTiers)) {
                return EPCCodeAmal.CODE_H.getProperty();
            }
            // Si subside P
            else if (AmalUtilsForDecisionsPC.getIfTypeDemandeByTiersForPeriodIsTypeP(periode, idTiers)) {
                return EPCCodeAmal.CODE_I.getProperty();
            }
            // code F
            else {
                return EPCCodeAmal.CODE_H.getProperty();
            }
        }
        // refus
        else {
            if (AmalUtilsForDecisionsPC.getIsInconnuAmal()
                    || !AmalUtilsForDecisionsPC.hasSubsideByTiersForPeriod(periode, idTiers)) {
                return EPCCodeAmal.CODE_K.getProperty();
            }
            // Si subside P
            else if (AmalUtilsForDecisionsPC.getIfTypeDemandeByTiersForPeriodIsTypeP(periode, idTiers)) {
                return EPCCodeAmal.CODE_C.getProperty();
            }
            // code F
            else {
                return EPCCodeAmal.CODE_J.getProperty();
            }
        }
    }

    /**
     * Retourne l'état de la décision refus, octroi ou partiel
     * 
     * @return, contante enum de EtatDecision
     * @throws DecisionException
     */
    private String getEtatDecision(DecisionApresCalcul dac) throws DecisionException {

        if (dac.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            return IPCDecision.CS_TYPE_OCTROI_AC;
        } else if (dac.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
            return IPCDecision.CS_TYPE_PARTIEL_AC;
        } else {
            return IPCDecision.CS_TYPE_REFUS_AC;
        }

    }

    /**
     * Retourne l'id du tiers demandé concerné par la décision et correspondant au rôle passé en paramètre
     * 
     * @param decision
     * @return id du tiers (requérant ou conjoint)
     */
    private String getIdTiers(DecisionApresCalcul decision, String membre) {
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decision.getPcAccordee().getPersonneEtendue().getPersonneEtendue().getIdTiers();
        } else if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
            return decision.getPcAccordee().getPersonneEtendueConjoint().getPersonneEtendue().getIdTiers();
        } else {
            return null;
        }
    }

    /**
     * Retourne l'id de la version du droit de la décision passé en paramètre
     * 
     * @param dec
     * @return id version du droit concerné
     */
    private String getIdVersionDroit(DecisionApresCalcul dec) {
        return dec.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit();
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. DecisionApresCalculService#read(java.lang.String)
     */
    @Override
    public DecisionApresCalcul read(String idDecision) throws JadePersistenceException, DecisionException {
        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to find DecisionApresCalcul, the id passed is null");
        }

        DecisionApresCalcul decision = new DecisionApresCalcul();
        decision.setId(idDecision);
        return (DecisionApresCalcul) JadePersistenceManager.read(decision);

    }

    /**
     * Chargement de l'entité decisioNApresCalculOO pour documents
     */
    @Override
    public DecisionApresCalculOO readForOO(String idDecision) throws JadePersistenceException, DecisionException {

        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to find DecisionApresCalculOO, the id passed is null");
        }

        DecisionApresCalculOO decisionOO = new DecisionApresCalculOO();
        decisionOO.setId(idDecision);
        decisionOO = (DecisionApresCalculOO) JadePersistenceManager.read(decisionOO);

        // Annexes
        SimpleAnnexesDecisionSearch annexeSearch = new SimpleAnnexesDecisionSearch();
        annexeSearch.setForIdDecisionHeader(decisionOO.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionHeader());
        annexeSearch = (SimpleAnnexesDecisionSearch) JadePersistenceManager.search(annexeSearch);
        ArrayList<SimpleAnnexesDecision> listeAnnexes = new ArrayList<SimpleAnnexesDecision>();

        for (JadeAbstractModel annexes : annexeSearch.getSearchResults()) {
            listeAnnexes.add((SimpleAnnexesDecision) annexes);
        }
        decisionOO.getDecisionHeader().setListeAnnexes(listeAnnexes);

        // Copies
        CopiesDecisionSearch copiesSearch = new CopiesDecisionSearch();
        copiesSearch.setForIdDecisionHeader(decisionOO.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionHeader());
        copiesSearch = (CopiesDecisionSearch) JadePersistenceManager.search(copiesSearch);
        ArrayList<CopiesDecision> listeCopies = new ArrayList<CopiesDecision>();

        for (JadeAbstractModel copies : copiesSearch.getSearchResults()) {
            listeCopies.add((CopiesDecision) copies);
        }
        decisionOO.getDecisionHeader().setListeCopies(listeCopies);
        return decisionOO;

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. DecisionApresCalculService
     * #search(ch.globaz.pegasus.business.models.decision .DecisionApresCalculSearch)
     */
    @Override
    public DecisionApresCalculSearch search(DecisionApresCalculSearch decisionApresCalculSearch)
            throws JadePersistenceException, DecisionException {
        if (decisionApresCalculSearch == null) {
            throw new DecisionException("Unable to search decisionAprescalcul, the search model passed is null!");
        }
        return (DecisionApresCalculSearch) JadePersistenceManager.search(decisionApresCalculSearch);
    }

    @Override
    public DecisionApresCalculSearch searchForDecisionCourant(DecisionApresCalculSearch decisionApresCalculSearch)
            throws JadePersistenceException, DecisionException {
        if (decisionApresCalculSearch == null) {
            throw new DecisionException("Unable to search decisionAprescalcul, the search model passed is null!");
        }
        decisionApresCalculSearch = (DecisionApresCalculSearch) JadePersistenceManager
                .search(decisionApresCalculSearch);

        return decisionApresCalculSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. DecisionApresCalculService
     * #update(ch.globaz.pegasus.business.models.decision.DecisionApresCalcul)
     */
    @Override
    public DecisionApresCalcul update(DecisionApresCalcul decision) throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to update Decision apres calcul, the model passed is null!");
        }
        try {
            // simpleDAC
            decision.setSimpleDecisionApresCalcul(PegasusImplServiceLocator.getSimpleDecisionApresCalculService()
                    .update(decision.getSimpleDecisionApresCalcul()));
            decision.getDecisionHeader().setSimpleDecisionHeader(
                    PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(
                            decision.getDecisionHeader().getSimpleDecisionHeader()));

            // ************************ ANNEXES **********************
            SimpleAnnexesDecisionSearch annexesDecisionSearch = new SimpleAnnexesDecisionSearch();
            annexesDecisionSearch.setForIdDecisionHeader(decision.getDecisionHeader().getSimpleDecisionHeader()
                    .getIdDecisionHeader());
            annexesDecisionSearch = PegasusServiceLocator.getSimpleAnnexesDecisionsService().search(
                    annexesDecisionSearch);
            // si annexes deja presentes, suppression par lots
            if (annexesDecisionSearch.getSearchResults().length != 0) {
                PegasusServiceLocator.getSimpleAnnexesDecisionsService().deleteByLots(
                        annexesDecisionSearch.getSearchResults());
            }
            // ajout des nouveau objets
            for (SimpleAnnexesDecision annexe : decision.getDecisionHeader().getListeAnnexes()) {
                annexe.setIdDecisionHeader(decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader());
                PegasusServiceLocator.getSimpleAnnexesDecisionsService().create(annexe);
            }

            // ************************ COPIES ************************
            CopiesDecisionSearch copiesDecisionSearch = new CopiesDecisionSearch();
            copiesDecisionSearch.setForIdDecisionHeader(decision.getDecisionHeader().getSimpleDecisionHeader()
                    .getIdDecisionHeader());
            copiesDecisionSearch = PegasusServiceLocator.getCopiesDecisionsService().search(copiesDecisionSearch);
            // si annexes deja presentes, suppression par lots
            if (copiesDecisionSearch.getSearchResults().length != 0) {
                PegasusServiceLocator.getCopiesDecisionsService().deleteByLots(copiesDecisionSearch.getSearchResults());

            }

            // ajout des nouveau objets
            for (CopiesDecision copie : decision.getDecisionHeader().getListeCopies()) {
                copie.getSimpleCopiesDecision().setIdDecisionHeader(
                        decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader());
                PegasusServiceLocator.getCopiesDecisionsService().create(copie);
            }

            try {
                updateProvisoire(decision);
            } catch (Exception e) {
                throw new DecisionException("Erreur lors de la mise à jour de l'état provisoire : "+ e.toString(), e);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.toString(), e);
        }
        return decision;
    }

    private void updateProvisoire(DecisionApresCalcul decision) throws Exception {
        boolean decisionProvisoire = decision.getDecisionHeader().getSimpleDecisionHeader().getDecisionProvisoire();
        boolean pcaProvisoire = decision.getPcAccordee().getSimplePCAccordee().getIsProvisoire();
        if(decisionProvisoire != pcaProvisoire){
            decision.getPcAccordee().getSimplePCAccordee().setIsProvisoire(decisionProvisoire);
            PegasusImplServiceLocator.getSimplePCAccordeeService().update(decision.getPcAccordee().getSimplePCAccordee());
            // recherche d'autre décision pour la même pca (conjoint)
            DecisionApresCalculSearch search = new DecisionApresCalculSearch();
            search.setForIdPcAccordee(decision.getPcAccordee().getId());
            search = (DecisionApresCalculSearch) JadePersistenceManager.search(search);
            if (search.getSize() > 1) {
                for (JadeAbstractModel decisionResult : search.getSearchResults()) {
                    DecisionApresCalcul decisionPca = (DecisionApresCalcul) decisionResult;
                    if(!decisionPca.getDecisionHeader().getId().equals(decision.getDecisionHeader().getId())){
                        decisionPca.getDecisionHeader().getSimpleDecisionHeader().setDecisionProvisoire(decisionProvisoire);
                        decisionPca.getSimpleDecisionApresCalcul().setIntroduction(getLabelRemarqueForProvisoire(decisionPca, decisionProvisoire));
                        PegasusImplServiceLocator.getSimpleDecisionApresCalculService().update(decisionPca.getSimpleDecisionApresCalcul());
                        PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(decisionPca.getDecisionHeader().getSimpleDecisionHeader());
                    }
                }
            }
        }
    }

    private String getLabelRemarqueForProvisoire(DecisionApresCalcul decision, boolean provisoire) throws Exception {
        Map<Langues, CTDocumentImpl>  documentsBabel = BabelServiceLocator.getPCCatalogueTexteService()
                .searchForTypeDecision(IPCCatalogueTextes.BABEL_DOC_NAME_APRES_CALCUL);
        CTDocumentImpl document = documentsBabel.get(LanguageResolver.resolveISOCode(decision.getDecisionHeader().getPersonneEtendue().getTiers().getLangue()));
        String text;
        if(provisoire) {
            text = PRStringUtils.replaceString(document.getTextes(2).getTexte(40).getDescription(),
                    DecisionApresCalculServiceImpl.DEMANDE_DU, decision
                            .getVersionDroit().getSimpleVersionDroit().getDateAnnonce());
        } else {
            text = PRStringUtils.replaceString(document.getTextes(2).getTexte(10).getDescription(),
                    DecisionApresCalculServiceImpl.DEMANDE_DU, decision
                            .getVersionDroit().getSimpleVersionDroit().getDateAnnonce());
        }
        return text;
    }

    /**
     * Mise à jour de la décision dans l'état prévalider
     */
    @Override
    public DecisionApresCalcul updateForPrevalidation(DecisionApresCalcul decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // set spy
        decision.getSimpleDecisionApresCalcul().setSpy(decision.getSimpleDecisionApresCalcul().getSpy());

        // header pour prévalidation
        decision.getDecisionHeader().setSimpleDecisionHeader(
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().updateForPrevalidation(
                        decision.getDecisionHeader().getSimpleDecisionHeader()));
        // decision apresCalcul
        decision.setSimpleDecisionApresCalcul(PegasusImplServiceLocator.getSimpleDecisionApresCalculService().update(
                decision.getSimpleDecisionApresCalcul()));

        return decision;
    }
}
