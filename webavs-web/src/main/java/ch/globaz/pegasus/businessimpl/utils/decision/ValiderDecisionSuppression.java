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

    /* D�cision de suppression courantes */
    private DecisionSuppression decisionSuppression = null;

    /* Liste des idPCa old version, cas standard et cas s�par� par la maladie */
    private List<String> listeOldPcaToUpdate = null;

    private List<String> listeOldPcaToUpdateForConjoint = null;
    private PCAccordeeSearch oldPcaSearch = null;
    /* mod�le de recherches des pca pour cette version de droit */
    private PCAccordeeSearch pcaForVersionSearch = null;

    /**
     * Cr�ation des ordres de versement et des prestations de restitution (li�s � une d�cision de suppression)
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
     * Lancement des recherches globales � la classe
     *
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     */
    private void initSearch()
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Recherche des pca cr�e pour cette version de droit, � savoir la courante et les pca deleted
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
     * G�re l'�tat (code syst�me de la prestation accord�es), en fonction du param�tre isOriginal.
     *
     * @param prestation
     *            , la prestation � g�rer
     * @param isOriginal
     *            , isOriginal, d�finit si la prestations accordees doit diminu�, ou valid� (isOriginal->true)
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
     * Mise � jour de la d�cision de suppression, VALIDE
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
     * Mise � jour de la demande
     *
     * @throws DemandeException
     * @throws DossierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void updateDemande() throws DemandeException, DossierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        // Mise � jour demande, set date fin avec dateSupression
        SimpleDemande simpleDemande = decisionSuppression.getVersionDroit().getDemande().getSimpleDemande();

        // if (simpleDemande.getDateFinInitial() == "") {
        simpleDemande.setDateFin(decisionSuppression.getSimpleDecisionSuppression().getDateSuppression());
        // }

        // date debut avec date de depot
        // BZ:8275
        // simpleDemande.setDateDebut(JadeDateUtil.convertDateMonthYear(simpleDemande.getDateDepot()));

        // etat supprim�
        simpleDemande.setCsEtatDemande(IPCDemandes.CS_SUPPRIME);

        simpleDemande = PegasusImplServiceLocator.getSimpleDemandeService().update(simpleDemande);
        // mise � jour
        decisionSuppression.getVersionDroit().getDemande().setSimpleDemande(simpleDemande);
    }

    /**
     * Mise � jour du droit courant, valid�
     *
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void updateDroit(boolean isAnnulation)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Mise � jour des versionde droits
        if (isAnnulation) {
            decisionSuppression.getVersionDroit().getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_ANNULE);
        } else {
            decisionSuppression.getVersionDroit().getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_VALIDE);
        }

        decisionSuppression.getVersionDroit().setSimpleVersionDroit(PegasusImplServiceLocator
                .getSimpleVersionDroitService().update(decisionSuppression.getVersionDroit().getSimpleVersionDroit()));
    }

    /**
     * Mise � jour du droit pr�c�dent, Historis�e
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
        // Mise � jour des versionde droits
        versionOldDroit.setCsEtatDroit(IPCDroits.CS_HISTORISE);

        versionOldDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().update(versionOldDroit);
    }

    /**
     * Mise � jour des anciennes pca
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

        // Iteration sur les pca pour update, historis� et diminu�, avec forcage date de fin
        for (JadeAbstractModel pca : oldPcaSearch.getSearchResults()) {
            PCAccordee pcaToUpdate = ((PCAccordee) pca);
            updateSinglePca(pcaToUpdate, true);
        }
    }

    /**
     * Mise � jour des pca de cette version. Pour les pca deleted (historisation et diminution, avec date de fin
     * forc�e), pour les pca courantes, Validation.
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

        // Iteration sur les pca copi�s (courantes et deleted), Mise � jour pour validation
        // Tri�s par date de dbut et csRoleFamille, ce qui implique que avec deux pca de meme periode, celle
        // du requearant sera toujorus la premiere
        for (int cpt = 0; cpt < pcaForVersionSearch.getSearchResults().length; cpt++) {

            // on r�cup�re la courante
            PCAccordee pcaToUpdate = ((PCAccordee) pcaForVersionSearch.getSearchResults()[cpt]);
            PCAccordee nextPcaToUpdate = null;

            // Si il y a emcore des pc dans le tableau, on r�cup�e la suivante, afin de voir si il y aconcordance de
            // p�riode, donc s�paration par la maladie
            if ((pcaForVersionSearch.getSearchResults().length - 1) > cpt) {
                // on prend la pca suivante
                nextPcaToUpdate = (PCAccordee) pcaForVersionSearch.getSearchResults()[cpt + 1];
            }

            // Si pca supprim�
            if (pcaToUpdate.getSimplePCAccordee().getIsSupprime()) {

                // ******************************* PCA du requ�rant ***************************************
                // ajout de l'id pcaParent � la liste
                listeOldPcaToUpdate.add(pcaToUpdate.getSimplePCAccordee().getIdPcaParent());

                // on force la date de fin prestation, si elle est vide on historise(pcpcacc) et on diminue (repracc)
                if (JadeStringUtil.isBlankOrZero(pcaToUpdate.getSimplePrestationsAccordees().getDateFinDroit())) {
                    updateSinglePca(pcaToUpdate, true);
                }

                // ******************************* PCA du conjoint ***************************************
                // Si la pc suivante a une date de debut egal, cas de la s�paration par la maladie, -->conjoint
                if ((nextPcaToUpdate != null) && nextPcaToUpdate.getSimplePCAccordee().getDateDebut()
                        .equals(pcaToUpdate.getSimplePCAccordee().getDateDebut())) {
                    listeOldPcaToUpdateForConjoint.add(nextPcaToUpdate.getSimplePCAccordee().getIdPcaParent());
                    // on force la date de fin prestation, si elle est vide et on historise(pcpcacc) et on diminue
                    // (repracc)
                    if (JadeStringUtil
                            .isBlankOrZero(nextPcaToUpdate.getSimplePrestationsAccordees().getDateFinDroit())) {
                        updateSinglePca(nextPcaToUpdate, true);

                    }
                    cpt++;// on en traite deux, donc double incr�ment
                }

            }
            // Si pas supprim�, c'est la courante
            else {

                // ******************************* PCA du requ�rant ***************************************
                updateSinglePca(pcaToUpdate, false);
                listeOldPcaToUpdate.add(pcaToUpdate.getSimplePCAccordee().getIdPcaParent());

                // ******************************* PCA du conjoint ***************************************
                if ((nextPcaToUpdate != null) && nextPcaToUpdate.getSimplePCAccordee().getDateDebut()
                        .equals(pcaToUpdate.getSimplePCAccordee().getDateDebut())) {
                    updateSinglePca(nextPcaToUpdate, false);
                    listeOldPcaToUpdateForConjoint.add(nextPcaToUpdate.getSimplePCAccordee().getIdPcaParent());
                    cpt++;// on en traite deux, donc double incr�ment
                }
            }
        }

    }

    /**
     * M�thode qui met a jour la pcaccord�e pass�e en param�tres
     *
     * @param pcaToUpdate
     *            , la pca � mettre � jour
     * @param isOriginal
     *            , d�finit si la pc doit �tre historis� et diminu�, ou valid� (isOriginal->true)
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
        // mise � jour repracc
        if (!JadeStringUtil.isBlankOrZero(simplePcaToSave.getIdPrestationAccordeeConjoint())) {
            pcaToUpdate
                    .setSimplePrestationsAccordeesConjoint(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                            .update(setEtatPrestationAccordee(simplePrestToSaveCon, isOriginal)));
        }

        // ******************************* PCA du requ�rant ***************************************
        // mise � jour repracc
        pcaToUpdate.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                .update(setEtatPrestationAccordee(simplePrestToSaveReq, isOriginal)));

        // Gestion etat pc
        simplePcaToSave.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_VALIDE);

        if (isOriginal) {
            simplePcaToSave.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE);
        }

        // Mise � jour pca
        pcaToUpdate.setSimplePCAccordee(PegasusImplServiceLocator.getSimplePCAccordeeService().update(simplePcaToSave));

    }

    /**
     * Point d'entr�e publique pour la validation des d�cisions de suppression
     *
     * @param decision
     *            , la d�cision de suppression qui va �tre valider
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
     * Point d'entr�e publique pour la validation des d�cisions de suppression
     *
     * @param decision
     *            , la d�cision de suppression qui va �tre valider
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
            // on set la decision de suppression pass� en param�tres
            decisionSuppression = decision;

            // Lancement des recherches
            initSearch();

            // mise � jour des pc copi�s (version courante) et originale (version pr�c�dentes)
            updatePcaAndPrestations();

            // mise � jour des anciennes pca
            updateOldPca(isAnnulation);

            // mise � jour de la demande
            if (!isAnnulation) {
                updateDemande();
            }

            // mise � jour du droit old
            updateOldDroit();

            // mise � jour du droit courant
            updateDroit(isAnnulation);

            // Si date de suppression ant�rieur � date dernier paiement, on cr�e prestations et OV, retro
            // if (!this.decisionSuppression.getSimpleDecisionSuppression().getDateSuppression()
            // .equals(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {

            // Mise en lot de la d�cision
            setTheLot(decisionSuppression, forceCreateLotRestitution);

            // creation des ordres de versements
            createPrestationAndOrdersVersementRestitution();
            // }
            // Mise � jour d�cision
            updateDecisionSuppression();
        }

        return simpleLot.getIdLot();
    }

    /**
     * Permet l'affectation d'un lot � la d�cision.
     * Si l'option de cr�ation de lot est � true, un lot sp�cifique est cr�� (Utilis� pour les d�cisions de restitution)
     * Sinon, si un lot ouvert existe, on le r�cup�re, sinon on cr�e un lot.
     *
     * @param decision Une d�cision
     * @param forceCreateLotRestitution True si on veut forcer la cr�ation du lot
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void setTheLot(DecisionSuppression decision, boolean forceCreateLotRestitution)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Si on veut un lot unique sp�cifique
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
