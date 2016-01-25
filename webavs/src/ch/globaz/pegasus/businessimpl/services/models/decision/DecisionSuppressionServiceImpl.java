/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecisionSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.DecisionSuppressionService;
import ch.globaz.pegasus.businessimpl.checkers.decision.DecisionSuppressionChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class DecisionSuppressionServiceImpl extends PegasusAbstractServiceImpl implements DecisionSuppressionService {

    /**
     * cloture la prestations accordees (repracc) si la date de fin est vide
     * 
     * @param pca
     * @throws JadeCloneModelException
     */
    private void copiePlanCalcul(String idPcaOriginal, String idPcaToSet) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeCloneModelException {
        // Recherche du plan de calcul a copié
        SimplePlanDeCalculSearch planSearch = new SimplePlanDeCalculSearch();
        planSearch.setForIdPCAccordee(idPcaOriginal);
        planSearch.setForIsPlanRetenu(Boolean.TRUE);
        planSearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(planSearch);

        SimplePlanDeCalcul planDeCalcul = new SimplePlanDeCalcul();

        // Si aucun résultat ou plus de un, problème, on set le plan a null
        if (planSearch.getSearchResults().length != 1) {
            planDeCalcul = null;
        } else {
            planDeCalcul = (SimplePlanDeCalcul) JadePersistenceUtil.clone(planSearch.getSearchResults()[0]);
            planDeCalcul.setIsPlanCalculAccessible(Boolean.FALSE);
            planDeCalcul.setIdPCAccordee(idPcaToSet);
            // Blob vide, pas besoin
            planDeCalcul.setResultatCalcul(new String().getBytes());

            PegasusImplServiceLocator.getSimplePlanDeCalculService().create(planDeCalcul);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #count(ch.globaz.pegasus.business.models.decision .SimpleDecisionSuppressionSearch)
     */
    @Override
    public int count(DecisionSuppressionSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count decisionsSuppressions, the search model passed is null!");
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
        // decisionApresCalculSearch.setWhereKey("forSpecificVersionDroit");
        decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
        return JadePersistenceManager.count(decisionApresCalculSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #create(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppression)
     */
    @Override
    public DecisionSuppression create(DecisionSuppression decision) throws JadePersistenceException,
            JadeApplicationException, JadeCloneModelException {
        if (decision == null) {
            throw new DecisionException("Unable to count, the search model passed is null!");
        }

        DecisionSuppressionChecker.checkForCreate(decision);

        try {

            // creation des pca pour les decisions de suppressions, 1 pca en standard, 2 pour le cas de la separation
            // par la maladie
            ArrayList<String> idPcaCourantesCrees = createPcasForDecSup(decision);
            // Set tiers
            decision.getDecisionHeader().getSimpleDecisionHeader().setIdTiersBeneficiaire(getIdTiers(decision));

            // Set tiers courrier
            decision.getDecisionHeader().getSimpleDecisionHeader().setIdTiersBeneficiaire(getIdTiers(decision));
            // Set adresse Tiers courrier
            decision.getDecisionHeader().getSimpleDecisionHeader().setIdTiersCourrier(getIdTiers(decision));
            // Set etat - enregistré
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
            // Set type
            decision.getDecisionHeader().getSimpleDecisionHeader()
                    .setCsTypeDecision(IPCDecision.CS_TYPE_SUPPRESSION_SC);
            // set genre, par défaut cs Decision
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsGenreDecision(IPCDecision.CS_GENRE_DECISION);
            // creation header
            decision.setDecisionHeader(PegasusServiceLocator.getDecisionHeaderService().create(
                    decision.getDecisionHeader()));

            // set version droit
            decision.getSimpleDecisionSuppression().setIdVersionDroit(
                    decision.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

            // Creation validation, autant que de pca crée
            for (String idPca : idPcaCourantesCrees) {
                SimpleValidationDecision validation = new SimpleValidationDecision();
                validation.setIdDecisionHeader(decision.getDecisionHeader().getSimpleDecisionHeader()
                        .getIdDecisionHeader());
                validation.setIdPCAccordee(idPca);
                validation = PegasusImplServiceLocator.getSimpleValidationService().create(validation);

            }

            // creation decision suppression
            decision.setSimpleDecisionSuppression(PegasusImplServiceLocator.getSimpleDecisionSuppressionService()
                    .create(decision.getSimpleDecisionSuppression()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return decision;
    }

    private ArrayList<String> createPcasForDecSup(DecisionSuppression decision) throws JadePersistenceException,
            JadeApplicationException, JadeCloneModelException {

        ArrayList<String> idPcaCourantes = new ArrayList<String>(2);

        boolean couranteTraite = false;

        // recherche des pca de la version précédente du droit, mais comprises et postérieur à la date de suppression
        PCAccordeeSearch pcaSearch = new PCAccordeeSearch();
        pcaSearch.setForIdDroit(decision.getVersionDroit().getSimpleDroit().getIdDroit());
        pcaSearch.setForDateValable(decision.getSimpleDecisionSuppression().getDateSuppression());
        pcaSearch.setWhereKey("fromDateForDecsup");
        pcaSearch.setOrderKey("forDateDebutAsc");
        pcaSearch = PegasusServiceLocator.getPCAccordeeService().search(pcaSearch);

        if (pcaSearch.getSearchResults().length == 0) {
            throw new DecisionException("pegasus.decision.suppression.nopcaforperiod");
        }

        String datePmt = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        if (JadeDateUtil.isDateMonthYearAfter(decision.getSimpleDecisionSuppression().getDateSuppression(), datePmt)) {
            throw new DecisionException("pegasus.decision.suppression.future");

        }

        // itération sur les pca retournés
        for (int cpt = 0; cpt < pcaSearch.getSearchResults().length; cpt++) {

            PCAccordee pcaToDeal = (PCAccordee) pcaSearch.getSearchResults()[cpt];
            PCAccordee nextPcaToDeal = null;
            // Si il y a emcore des pc dans le tableau
            if ((pcaSearch.getSearchResults().length - 1) > cpt) {
                // on prend la pca suivante
                nextPcaToDeal = (PCAccordee) pcaSearch.getSearchResults()[cpt + 1];
            }

            // Date de suppression
            String dateSuppressionDecision = decision.getSimpleDecisionSuppression().getDateSuppression();
            // Date de fin de la pca courante
            String dateFinPcaCourante = pcaToDeal.getSimplePCAccordee().getDateFin();

            // si la date de fin de la pca est avant la date de suppression, est vide, ou est egal à la date de
            // suppression,
            // et que le courant n'est pas traité
            if (!couranteTraite
                    && (JadeDateUtil.isDateBefore("01." + dateSuppressionDecision, "01." + dateFinPcaCourante)
                            || JadeStringUtil.isBlankOrZero(dateFinPcaCourante) || dateSuppressionDecision
                                .equals(dateFinPcaCourante))) {

                pcaToDeal.getSimplePCAccordee().setIdVersionDroit(
                        decision.getVersionDroit().getSimpleVersionDroit().getId());
                // set date fin dac avec pca
                pcaToDeal.getSimplePCAccordee()
                        .setDateFin(decision.getSimpleDecisionSuppression().getDateSuppression());
                // on va rechercher le plan de calcul

                // on sauva la pc
                saveCopiePca(pcaToDeal, true);
                decision.getDecisionHeader().getSimpleDecisionHeader()
                        .setDateDebutDecision(pcaToDeal.getSimplePCAccordee().getDateDebut());
                decision.getDecisionHeader().getSimpleDecisionHeader()
                        .setDateFinDecision(pcaToDeal.getSimplePCAccordee().getDateFin());
                couranteTraite = true;
                // isCoupleSepareParMaladie = false;
                idPcaCourantes.add(pcaToDeal.getId());
                // Si la pc suivante a une date de debut egal, cas de la séparation par la maladie
                if ((nextPcaToDeal != null)
                        && nextPcaToDeal.getSimplePCAccordee().getDateDebut()
                                .equals(pcaToDeal.getSimplePCAccordee().getDateDebut())) {
                    nextPcaToDeal.getSimplePCAccordee().setIdVersionDroit(
                            decision.getVersionDroit().getSimpleVersionDroit().getId());
                    // set date fin dac avec pca
                    nextPcaToDeal.getSimplePCAccordee().setDateFin(
                            decision.getSimpleDecisionSuppression().getDateSuppression());
                    // on sauva la pc
                    saveCopiePca(nextPcaToDeal, true);
                    idPcaCourantes.add(nextPcaToDeal.getSimplePCAccordee().getIdPCAccordee());
                    // double increment, on en traite deux
                    cpt++;

                }

                // Si date de fin vide
            } else {

                pcaToDeal.getSimplePCAccordee().setIdVersionDroit(
                        decision.getVersionDroit().getSimpleVersionDroit().getId());
                saveCopiePca(pcaToDeal, false);
                // Si la pc suivante a une date de debut egal, cas de la séparation par la maladie
                if ((nextPcaToDeal != null)
                        && nextPcaToDeal.getSimplePCAccordee().getDateDebut()
                                .equals(pcaToDeal.getSimplePCAccordee().getDateDebut())) {
                    nextPcaToDeal.getSimplePCAccordee().setIdVersionDroit(
                            decision.getVersionDroit().getSimpleVersionDroit().getId());
                    saveCopiePca(nextPcaToDeal, false);
                    cpt++;
                }

            }

        }
        return idPcaCourantes;
    }

    /**
     * Mise à jour de la prestations accordees originelles (REPRACC)
     * On définit l'état, on définit la date de fin comme antérieur à la date de début, histoire d'viter des paiement
     * non désirés
     * 
     */
    private void dealPrestationsAccordee(SimplePrestationsAccordees prestationToSave, SimplePCAccordee pca,
            Boolean isCourante) throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {

        // prestations accorddées à créé, date de debut et de fin comme pca
        prestationToSave.setDateDebutDroit(pca.getDateDebut());

        if (!isCourante) {
            pca.setIsSupprime(Boolean.TRUE);
        }

        pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        prestationToSave.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        prestationToSave.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        prestationToSave.setDateFinDroit(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                "01." + pca.getDateDebut(), -1)));
        // on remet l'id à vide pour la création de la nouvelle entitée, et in remet la date de fin originelle (celle de
        // la pca)
        prestationToSave.setDateFinDroit(pca.getDateFin());
        prestationToSave.setId("");

    }

    /**
     * Mise à jour de la prestations accordees originelles (REPRACC)
     * On définit l'état, on définit la date de fin comme antérieur à la date de début, histoire d'viter des paiement
     * non désirés
     * 
     */
    private void dealPrestationsAccordeeOld(SimplePrestationsAccordees prestationToSave, SimplePCAccordee pca,
            Boolean isCourante) throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {

        // prestations accorddées à créé, date de debut et de fin comme pca
        prestationToSave.setDateDebutDroit(pca.getDateDebut());

        if (isCourante) {
            pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
            prestationToSave.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        } else {
            pca.setIsSupprime(Boolean.TRUE);
            pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE);
            prestationToSave.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        }

        // on update la prestations originelles avec date de fin avant la date de debut
        prestationToSave.setDateFinDroit(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                "01." + pca.getDateDebut(), -1)));
        PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(prestationToSave);

        // on remet l'id à vide pour la création de la nouvelle entitée, et in remet la date de fin originelle (celle de
        // la pca)
        prestationToSave.setDateFinDroit(pca.getDateFin());
        prestationToSave.setId("");

    }

    @Override
    public DecisionSuppression delete(DecisionSuppression decision) throws DecisionException, JadePersistenceException {
        if (decision == null) {
            throw new DecisionException("unable to delete decisionApresCalcul, the model passed is null");
        }

        try {
            // Suppression copies
            PegasusImplServiceLocator.getSimpleCopiesDecisionsService().deleteForDecision(
                    decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader());

            // Suppression annexes
            PegasusImplServiceLocator.getSimpleAnnexesDecisionService().deleteForDecision(
                    decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader());
            // Delete simple decsup
            PegasusImplServiceLocator.getSimpleDecisionSuppressionService().delete(
                    decision.getSimpleDecisionSuppression());
            // delete header
            PegasusImplServiceLocator.getSimpleDecisionHeaderService().delete(
                    decision.getDecisionHeader().getSimpleDecisionHeader());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }
        return decision;
    }

    @Override
    public int delete(DecisionSuppressionSearch decisionSearch) throws DecisionException, JadePersistenceException {

        decisionSearch = search(decisionSearch);
        for (JadeAbstractModel absDecision : decisionSearch.getSearchResults()) {
            DecisionSuppression decision = (DecisionSuppression) absDecision;
            deleteCascade(decision);
        }

        return 0;
    }

    private void deleteCascade(DecisionSuppression decision) throws DecisionException, JadePersistenceException {

        try {
            // Suppression copies
            PegasusImplServiceLocator.getSimpleCopiesDecisionsService().deleteForDecision(
                    decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader());

            // Suppression annexes
            PegasusImplServiceLocator.getSimpleAnnexesDecisionService().deleteForDecision(
                    decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader());

            // delete validation
            SimpleValidationDecisionSearch validationSearch = new SimpleValidationDecisionSearch();
            validationSearch.setForIdDecisionHeader(decision.getDecisionHeader().getSimpleDecisionHeader()
                    .getIdDecisionHeader());

            // Suppression
            PegasusImplServiceLocator.getSimpleValidationService().delete(validationSearch);

            // delete header
            PegasusImplServiceLocator.getSimpleDecisionHeaderService().delete(
                    decision.getDecisionHeader().getSimpleDecisionHeader());

            // return this.delete(decision);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not found!", e);
        }
    }

    /**
     * Remise à zéro des id et spy de l'information compta pour persister
     * 
     * @param infoCompta
     */
    private void generateNewInfoComptaForPersist(SimpleInformationsComptabilite infoCompta) {
        infoCompta.setId("");// = pcaToSave.getSimpleInformationsComptabilite();
        infoCompta.setSpy("");// = pcaToSave.getSimpleInformationsComptabilite();
    }

    /**
     * @param decision
     * @return
     */
    private String getIdTiers(DecisionSuppression decision) {
        return decision.getVersionDroit().getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                .getTiers().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService#read(java.lang.String)
     */
    @Override
    public DecisionSuppression read(String idDecision) throws JadePersistenceException, DecisionException {
        if (idDecision == null) {
            throw new DecisionException("Unable to read decisionSupression, the id passed is null");
        }
        DecisionSuppression decision = new DecisionSuppression();
        decision.setId(idDecision);
        return (DecisionSuppression) JadePersistenceManager.read(decision);
    }

    /**
     * Sauvegarde des pca copiés
     * 
     * @param pcaToSave
     * @param isCourante
     *            , définit si on a affaire à léa pca courante. En effet, une seule pca sera traité dans ce cas, les
     *            autres seront supprimée
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeCloneModelException
     */
    private void saveCopiePca(PCAccordee pcaToSave, Boolean isCourante) throws JadePersistenceException,
            JadeApplicationException, JadeCloneModelException {

        SimplePCAccordee simplePcaToSave = pcaToSave.getSimplePCAccordee();
        String idPcaOriginal = pcaToSave.getSimplePCAccordee().getId();

        // Set id parent, si la pca a elle meme un pcaParent, on set la valeur du parent originel
        if (!JadeStringUtil.isBlankOrZero(pcaToSave.getSimplePCAccordee().getIdPcaParent())) {
            simplePcaToSave.setIdPcaParent(pcaToSave.getSimplePCAccordee().getIdPcaParent());
        } else {
            simplePcaToSave.setIdPcaParent(pcaToSave.getId());
        }

        SimplePrestationsAccordees simplePrestToSaveReq = pcaToSave.getSimplePrestationsAccordees();
        SimplePrestationsAccordees simplePrestToSaveCon = pcaToSave.getSimplePrestationsAccordeesConjoint();
        SimpleInformationsComptabilite simpleInfoCompta = pcaToSave.getSimpleInformationsComptabilite();
        SimpleInformationsComptabilite simpleInfoComptaCon = pcaToSave.getSimpleInformationsComptabiliteConjoint();

        // SI conjoint
        if (!JadeStringUtil.isBlankOrZero(simplePcaToSave.getIdPrestationAccordeeConjoint())) {

            // SAuvegarde infoCompta -->REINCOM
            generateNewInfoComptaForPersist(simpleInfoComptaCon);
            generateNewInfoComptaForPersist(simpleInfoCompta);

            simpleInfoCompta = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(simpleInfoCompta);
            simpleInfoComptaCon = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(
                    simpleInfoComptaCon);

            // Sauvegarde des prestations-->REPRACC --> Conjoint
            simplePrestToSaveCon.setIdInfoCompta(simpleInfoComptaCon.getIdInfoCompta());
            dealPrestationsAccordee(simplePrestToSaveCon, simplePcaToSave, isCourante);
            simplePrestToSaveCon = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(
                    simplePrestToSaveCon);
            // --> Requerant
            simplePrestToSaveReq.setIdInfoCompta(simpleInfoCompta.getIdInfoCompta());
            dealPrestationsAccordee(simplePrestToSaveReq, simplePcaToSave, isCourante);
            simplePrestToSaveReq = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(
                    simplePrestToSaveReq);

            // sauvegarde de la pca, on set les nouveau id prestations accordees
            simplePcaToSave.setIdPrestationAccordeeConjoint(simplePrestToSaveCon.getIdPrestationAccordee());
            simplePcaToSave.setIdPrestationAccordee(simplePrestToSaveReq.getIdPrestationAccordee());

        } else {
            // Sauvegarde infoCompta -->REINCOM
            generateNewInfoComptaForPersist(simpleInfoCompta);
            simpleInfoCompta = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(simpleInfoCompta);

            // simplePrestToSaveReq.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            simplePrestToSaveReq.setIdInfoCompta(simpleInfoCompta.getIdInfoCompta());
            dealPrestationsAccordee(simplePrestToSaveReq, simplePcaToSave, isCourante);

            simplePrestToSaveReq = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(
                    simplePrestToSaveReq);
            // sauvegarde de la pca, on set les nouveau id prestations accordees
            simplePcaToSave.setIdPrestationAccordee(simplePrestToSaveReq.getIdPrestationAccordee());
        }

        // Save pca
        // simplePcaToSave.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        simplePcaToSave = PegasusImplServiceLocator.getSimplePCAccordeeService().create(simplePcaToSave);

        // on set le plan de calcul pour la courante
        if (isCourante) {
            copiePlanCalcul(idPcaOriginal, simplePcaToSave.getIdPCAccordee());

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #search(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppressionSearch)
     */
    @Override
    public DecisionSuppressionSearch search(DecisionSuppressionSearch decisionSearch) throws JadePersistenceException,
            DecisionException {
        if (decisionSearch == null) {
            throw new DecisionException("Unable to search decision, the search model passed is null!");
        }
        return (DecisionSuppressionSearch) JadePersistenceManager.search(decisionSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision. SimpleDecisionSuppressionService
     * #update(ch.globaz.pegasus.business.models. decision.SimpleDecisionSuppression)
     */
    @Override
    public DecisionSuppression update(DecisionSuppression decision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException, PmtMensuelException, JadeNoBusinessLogSessionError {
        if (decision == null) {
            throw new DecisionException("Unable to update decisionSupression, the model passed is null");
        }

        // set state enregistre
        // decision.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);

        // header
        decision.getDecisionHeader().setSimpleDecisionHeader(
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(
                        decision.getDecisionHeader().getSimpleDecisionHeader()));
        decision.setDecisionHeader(PegasusImplServiceLocator.getDecisionHeaderService().updateAnnexesCopies(
                decision.getDecisionHeader()));
        // decision suppression
        decision.setSimpleDecisionSuppression(PegasusImplServiceLocator.getSimpleDecisionSuppressionService().update(
                decision.getSimpleDecisionSuppression()));

        return decision;
    }

}
