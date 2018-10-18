package ch.globaz.pegasus.businessimpl.utils.decision;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression.GenerateOvsForSuppression;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression.GeneratePrestation;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.lots.IRELot;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class ValiderDecisionSuppression extends AbstractValiderDecision {

    private static final Logger logger = LoggerFactory.getLogger(ValiderDecisionSuppression.class);

    /* Décision de suppression courantes */
    private DecisionSuppression decisionSuppression = null;

    /* Liste des idPCa old version, cas standard et cas séparé par la maladie */
    private List<String> listeOldPcaToUpdate = null;

    private List<String> listeOldPcaToUpdateForConjoint = null;
    private PCAccordeeSearch oldPcaSearch = null;
    /* modèle de recherches des pca pour cette version de droit */
    private PCAccordeeSearch pcaForVersionSearch = null;

    /**
     * Création des ordres de versement et des prestations de restitution (liés à une décision de suppression)
     *
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     * @throws PmtMensuelException
     * @throws OrdreVersementException
     * @throws PrestationException
     */
    private void createPrestationAndOrdersVersementRestitution()
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            PCAccordeeException, PmtMensuelException, OrdreVersementException, PrestationException {

        String dateSuppression = decisionSuppression.getSimpleDecisionSuppression().getDateSuppression();
        String dateDernierPmt = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        List<PCAccordee> pcas = PersistenceUtil.typeSearch(oldPcaSearch, oldPcaSearch.whichModelClass());
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression(dateSuppression, dateDernierPmt);
        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        BigDecimal montantTotalRestitution = generateOvs.getMontantTotalRestitution();
        List<SimpleJoursAppoint> listeJoursAppoint = new ArrayList<SimpleJoursAppoint>();
        for (PCAccordee pca : pcas) {
            if (pca.getSimplePCAccordee().getHasJoursAppoint()) {
                SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
                search.setForIdPCAccordee(pca.getId());
                search = PegasusImplServiceLocator.getSimpleJoursAppointService().search(search);
                listeJoursAppoint = PersistenceUtil.typeSearch(search, search.whichModelClass());
                JADate dateDebutPCHome = null;
                JADate dateSupression = null;
                try {
                    dateDebutPCHome = new JADate(pca.getSimplePCAccordee().getDateDebut());
                    dateSupression = new JADate(dateSuppression);
                } catch (JAException e) {
                    logger.error(e.getMessage());
                }
                JACalendarGregorian cal = new JACalendarGregorian();
                if (cal.compare(dateSupression, dateDebutPCHome) == JACalendar.COMPARE_FIRSTLOWER) {
                    for (SimpleOrdreVersement simpleOrdreVersement : ovs) {
                        if (simpleOrdreVersement.getMontant().equals(montantTotalRestitution.toString())) {
                            montantTotalRestitution = montantTotalRestitution
                                    .add(new BigDecimal(listeJoursAppoint.get(0).getMontantTotal()));
                            simpleOrdreVersement.setMontant(montantTotalRestitution.toString());
                        }
                    }

                }
            }
        }

        GeneratePrestation generatePrestation = new GeneratePrestation(dateDernierPmt,
                decisionSuppression.getSimpleDecisionSuppression().getDateSuppression());

        SimplePrestation prestation = generatePrestation.generate(pcas, montantTotalRestitution,
                decisionSuppression.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

        prestation.setIdLot(simpleLot.getIdLot());
        prestation = PegasusImplServiceLocator.getSimplePrestationService().create(prestation);

        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setIdPrestation(prestation.getIdPrestation());
        for (SimpleOrdreVersement simpleOrdreVersement : ovs) {
            simpleOrdreVersement.setIdPrestation(prestation.getIdPrestation());
            PegasusImplServiceLocator.getSimpleOrdreVersementService().create(simpleOrdreVersement);
        }
    }

    /**
     * Lancement des recherches globales à la classe
     *
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     */
    private void initSearch()
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Recherche des pca crée pour cette version de droit, à savoir la courante et les pca deleted
        pcaForVersionSearch = new PCAccordeeSearch();
        pcaForVersionSearch.setWhereKey("forValidationDecisionSuppression");
        pcaForVersionSearch.setOrderKey("forDateDebutAsc");
        pcaForVersionSearch
                .setForIdDemande(decisionSuppression.getVersionDroit().getDemande().getSimpleDemande().getIdDemande());
        pcaForVersionSearch
                .setForVersionDroit(decisionSuppression.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());
        pcaForVersionSearch = PegasusServiceLocator.getPCAccordeeService().search(pcaForVersionSearch);
    }

    /**
     * Gère l'état (code système de la prestation accordées), en fonction du paramètre isOriginal.
     *
     * @param prestation
     *            , la prestation à gérer
     * @param isOriginal
     *            , isOriginal, définit si la prestations accordees doit diminué, ou validé (isOriginal->true)
     * @return
     */
    private SimplePrestationsAccordees setEtatPrestationAccordee(SimplePrestationsAccordees prestation,
            Boolean isOriginal) {
        if (isOriginal) {
            prestation.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        } else {
            prestation.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        }

        prestation.setDateFinDroit((JadeDateUtil.addDays("01." + prestation.getDateDebutDroit(), -1)).substring(3));
        return prestation;
    }

    /**
     * Mise à jour de la décision de suppression, VALIDE
     *
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PmtMensuelException
     * @throws JadeNoBusinessLogSessionError
     */
    private void updateDecisionSuppression() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PmtMensuelException, JadeNoBusinessLogSessionError {
        // set spy
        decisionSuppression.getSimpleDecisionSuppression()
                .setSpy(decisionSuppression.getSimpleDecisionSuppression().getSpy());

        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_VALIDE);

        decisionSuppression.getDecisionHeader()
                .setSimpleDecisionHeader(PegasusImplServiceLocator.getSimpleDecisionHeaderService()
                        .updateForValidation(decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()));

        // decision suppression
        decisionSuppression.setSimpleDecisionSuppression(PegasusImplServiceLocator.getSimpleDecisionSuppressionService()
                .update(decisionSuppression.getSimpleDecisionSuppression()));
    }

    /**
     * Mise à jour de la demande
     *
     * @throws DemandeException
     * @throws DossierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void updateDemande() throws DemandeException, DossierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        // Mise à jour demande, set date fin avec dateSupression
        SimpleDemande simpleDemande = decisionSuppression.getVersionDroit().getDemande().getSimpleDemande();

        // if (simpleDemande.getDateFinInitial() == "") {
        simpleDemande.setDateFin(decisionSuppression.getSimpleDecisionSuppression().getDateSuppression());
        // }

        // date debut avec date de depot
        // BZ:8275
        // simpleDemande.setDateDebut(JadeDateUtil.convertDateMonthYear(simpleDemande.getDateDepot()));

        // etat supprimé
        simpleDemande.setCsEtatDemande(IPCDemandes.CS_SUPPRIME);

        simpleDemande = PegasusImplServiceLocator.getSimpleDemandeService().update(simpleDemande);
        // mise à jour
        decisionSuppression.getVersionDroit().getDemande().setSimpleDemande(simpleDemande);
    }

    /**
     * Mise à jour du droit courant, validé
     *
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void updateDroit(boolean isAnnulation)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Mise à jour des versionde droits
        if (isAnnulation) {
            decisionSuppression.getVersionDroit().getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_ANNULE);
        } else {
            decisionSuppression.getVersionDroit().getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_VALIDE);
        }

        decisionSuppression.getVersionDroit().setSimpleVersionDroit(PegasusImplServiceLocator
                .getSimpleVersionDroitService().update(decisionSuppression.getVersionDroit().getSimpleVersionDroit()));
    }

    /**
     * Mise à jour du droit précédent, Historisée
     *
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    private void updateOldDroit() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {
        SimpleVersionDroitSearch serachDroit = new SimpleVersionDroitSearch();
        Integer noVersionToSearch = Integer
                .parseInt(decisionSuppression.getVersionDroit().getSimpleVersionDroit().getNoVersion()) - 1;
        serachDroit.setForNoVersionDroit(noVersionToSearch.toString());
        serachDroit.setForIdDroit(decisionSuppression.getVersionDroit().getSimpleDroit().getId());
        serachDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().search(serachDroit);

        if (serachDroit.getSearchResults().length != 1) {
            throw new DecisionException("zero or more than one version droit found for update droit for suppression");
        }

        SimpleVersionDroit versionOldDroit = (SimpleVersionDroit) serachDroit.getSearchResults()[0];
        // Mise à jour des versionde droits
        versionOldDroit.setCsEtatDroit(IPCDroits.CS_HISTORISE);

        versionOldDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().update(versionOldDroit);
    }

    /**
     * Mise à jour des anciennes pca
     *
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeCloneModelException
     * @throws JadeApplicationException
     */
    private void updateOldPca(boolean isAnnulation)
            throws JadePersistenceException, DecisionException, JadeCloneModelException, JadeApplicationException {
        oldPcaSearch = new PCAccordeeSearch();
        oldPcaSearch.setForIdDroit(decisionSuppression.getVersionDroit().getSimpleDroit().getIdDroit());
        oldPcaSearch.setForDateValable(decisionSuppression.getSimpleDecisionSuppression().getDateSuppression());
        oldPcaSearch.setForNoVersionDroit(decisionSuppression.getVersionDroit().getSimpleVersionDroit().getNoVersion());
        if (isAnnulation) {
            oldPcaSearch.setForIsDeleted(false);
            oldPcaSearch.setWhereKey(PCAccordeeSearch.FOR_PCA_REPLACEC_BY_DECISION_SUPPRESSION_ANNULATION);
        } else {
            oldPcaSearch.setWhereKey(PCAccordeeSearch.FOR_PCA_REPLACEC_BY_DECISION_SUPPRESSION);
        }

        oldPcaSearch.setOrderKey("forDateDebutAsc");
        oldPcaSearch = PegasusServiceLocator.getPCAccordeeService().search(oldPcaSearch);

        // Iteration sur les pca pour update, historisé et diminué, avec forcage date de fin
        for (JadeAbstractModel pca : oldPcaSearch.getSearchResults()) {
            PCAccordee pcaToUpdate = ((PCAccordee) pca);
            updateSinglePca(pcaToUpdate, true);
        }
    }

    /**
     * Mise à jour des pca de cette version. Pour les pca deleted (historisation et diminution, avec date de fin
     * forcée), pour les pca courantes, Validation.
     *
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeCloneModelException
     * @throws JadeApplicationException
     */
    private void updatePcaAndPrestations()
            throws JadePersistenceException, DecisionException, JadeCloneModelException, JadeApplicationException {

        // Liste des pca old a updater (pca original des pc deleted)
        listeOldPcaToUpdate = new ArrayList<String>();
        listeOldPcaToUpdateForConjoint = new ArrayList<String>();

        // Iteration sur les pca copiés (courantes et deleted), Mise à jour pour validation
        // Triés par date de dbut et csRoleFamille, ce qui implique que avec deux pca de meme periode, celle
        // du requearant sera toujorus la premiere
        for (int cpt = 0; cpt < pcaForVersionSearch.getSearchResults().length; cpt++) {

            // on récupère la courante
            PCAccordee pcaToUpdate = ((PCAccordee) pcaForVersionSearch.getSearchResults()[cpt]);
            PCAccordee nextPcaToUpdate = null;

            // Si il y a emcore des pc dans le tableau, on récupèe la suivante, afin de voir si il y aconcordance de
            // période, donc séparation par la maladie
            if ((pcaForVersionSearch.getSearchResults().length - 1) > cpt) {
                // on prend la pca suivante
                nextPcaToUpdate = (PCAccordee) pcaForVersionSearch.getSearchResults()[cpt + 1];
            }

            // Si pca supprimé
            if (pcaToUpdate.getSimplePCAccordee().getIsSupprime()) {

                // ******************************* PCA du requérant ***************************************
                // ajout de l'id pcaParent à la liste
                listeOldPcaToUpdate.add(pcaToUpdate.getSimplePCAccordee().getIdPcaParent());

                // on force la date de fin prestation, si elle est vide on historise(pcpcacc) et on diminue (repracc)
                if (JadeStringUtil.isBlankOrZero(pcaToUpdate.getSimplePrestationsAccordees().getDateFinDroit())) {
                    updateSinglePca(pcaToUpdate, true);
                }

                // ******************************* PCA du conjoint ***************************************
                // Si la pc suivante a une date de debut egal, cas de la séparation par la maladie, -->conjoint
                if ((nextPcaToUpdate != null) && nextPcaToUpdate.getSimplePCAccordee().getDateDebut()
                        .equals(pcaToUpdate.getSimplePCAccordee().getDateDebut())) {
                    listeOldPcaToUpdateForConjoint.add(nextPcaToUpdate.getSimplePCAccordee().getIdPcaParent());
                    // on force la date de fin prestation, si elle est vide et on historise(pcpcacc) et on diminue
                    // (repracc)
                    if (JadeStringUtil
                            .isBlankOrZero(nextPcaToUpdate.getSimplePrestationsAccordees().getDateFinDroit())) {
                        updateSinglePca(nextPcaToUpdate, true);

                    }
                    cpt++;// on en traite deux, donc double incrément
                }

            }
            // Si pas supprimé, c'est la courante
            else {

                // ******************************* PCA du requérant ***************************************
                updateSinglePca(pcaToUpdate, false);
                listeOldPcaToUpdate.add(pcaToUpdate.getSimplePCAccordee().getIdPcaParent());

                // ******************************* PCA du conjoint ***************************************
                if ((nextPcaToUpdate != null) && nextPcaToUpdate.getSimplePCAccordee().getDateDebut()
                        .equals(pcaToUpdate.getSimplePCAccordee().getDateDebut())) {
                    updateSinglePca(nextPcaToUpdate, false);
                    listeOldPcaToUpdateForConjoint.add(nextPcaToUpdate.getSimplePCAccordee().getIdPcaParent());
                    cpt++;// on en traite deux, donc double incrément
                }
            }
        }

    }

    /**
     * Méthode qui met a jour la pcaccordée passée en paramètres
     *
     * @param pcaToUpdate
     *            , la pca à mettre à jour
     * @param isOriginal
     *            , définit si la pc doit être historisé et diminué, ou validé (isOriginal->true)
     * @throws JadePersistenceException
     * @throws JadeCloneModelException
     * @throws JadeApplicationException
     * @throws DecisionException
     */
    private void updateSinglePca(PCAccordee pcaToUpdate, Boolean isOriginal)
            throws JadePersistenceException, JadeCloneModelException, JadeApplicationException, DecisionException {

        SimplePCAccordee simplePcaToSave = pcaToUpdate.getSimplePCAccordee();
        SimplePrestationsAccordees simplePrestToSaveReq = pcaToUpdate.getSimplePrestationsAccordees();
        SimplePrestationsAccordees simplePrestToSaveCon = pcaToUpdate.getSimplePrestationsAccordeesConjoint();

        // ******************************* PCA du conjoint ***************************************
        // mise à jour repracc
        if (!JadeStringUtil.isBlankOrZero(simplePcaToSave.getIdPrestationAccordeeConjoint())) {
            pcaToUpdate
                    .setSimplePrestationsAccordeesConjoint(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                            .update(setEtatPrestationAccordee(simplePrestToSaveCon, isOriginal)));
        }

        // ******************************* PCA du requérant ***************************************
        // mise à jour repracc
        pcaToUpdate.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                .update(setEtatPrestationAccordee(simplePrestToSaveReq, isOriginal)));

        // Gestion etat pc
        simplePcaToSave.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_VALIDE);

        if (isOriginal) {
            simplePcaToSave.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE);
        }

        // Mise à jour pca
        pcaToUpdate.setSimplePCAccordee(PegasusImplServiceLocator.getSimplePCAccordeeService().update(simplePcaToSave));

    }

    /**
     * Point d'entrée publique pour la validation des décisions de suppression
     *
     * @param decision
     *            , la décision de suppression qui va être valider
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeCloneModelException
     * @throws DecisionException
     */
    public String valider(DecisionSuppression decision, boolean forceCreateLotRestitution)
            throws JadePersistenceException, DecisionException, JadeCloneModelException, JadeApplicationException {
        return valider(decision, forceCreateLotRestitution, false);
    }

    /**
     * Point d'entrée publique pour la validation des décisions de suppression
     *
     * @param decision
     *            , la décision de suppression qui va être valider
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeCloneModelException
     * @throws DecisionException
     */
    public String valider(DecisionSuppression decision, boolean forceCreateLotRestitution, boolean isAnnulation)
            throws JadePersistenceException, DecisionException, JadeCloneModelException, JadeApplicationException {

        // this.checkDecision(decision.getDecisionHeader().getSimpleDecisionHeader());

        // DecisionUtils.checkIfDateDecisionCompriseEntreDernierEtProchainPaiement(this.decisionSuppression
        // s .getDecisionHeader().getSimpleDecisionHeader().getDateDecision());

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            // on set la decision de suppression passé en paramètres
            decisionSuppression = decision;

            // Lancement des recherches
            initSearch();

            // mise à jour des pc copiés (version courante) et originale (version précédentes)
            updatePcaAndPrestations();

            // mise à jour des anciennes pca
            updateOldPca(isAnnulation);

            // mise à jour de la demande
            if (!isAnnulation) {
                updateDemande();
            }

            // mise à jour du droit old
            updateOldDroit();

            // mise à jour du droit courant
            updateDroit(isAnnulation);

            // Si date de suppression antérieur à date dernier paiement, on crée prestations et OV, retro
            // if (!this.decisionSuppression.getSimpleDecisionSuppression().getDateSuppression()
            // .equals(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {

            // Mise en lot de la décision
            setTheLot(decisionSuppression, forceCreateLotRestitution);

            // creation des ordres de versements
            createPrestationAndOrdersVersementRestitution();
            // }
            // Mise à jour décision
            updateDecisionSuppression();
        }

        return simpleLot.getIdLot();
    }

    /**
     * Permet l'affectation d'un lot à la décision.
     * Si l'option de création de lot est à true, un lot spécifique est créé (Utilisé pour les décisions de restitution)
     * Sinon, si un lot ouvert existe, on le récupère, sinon on crée un lot.
     *
     * @param decision Une décision
     * @param forceCreateLotRestitution True si on veut forcer la création du lot
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void setTheLot(DecisionSuppression decision, boolean forceCreateLotRestitution)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Si on veut un lot unique spécifique
        if (forceCreateLotRestitution) {
            String monthYear = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_MMsYYYY);
            String libelle = BSessionUtil.getSessionFromThreadContext()
                    .getLabel("PEGASUS_JAVA_NOM_LOT_DECISION_RESTITUTION");
            String numNss = decision.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();

            String description = (" PC-" + libelle + " " + monthYear + "/" + numNss);

            setTheCreatedLot(IRELot.CS_TYP_LOT_DECISION_RESTITUTION, description);
        } else {
            // sinon Set le lot
            setTheOpenLot();
        }
    }
}
