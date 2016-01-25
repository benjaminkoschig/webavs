/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.properties.REProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.beans.PRPeriode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSearch;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSuppressionWithPcaSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.ValidationDecisionService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionHeaderChecker;
import ch.globaz.pegasus.businessimpl.checkers.decision.ValidationDecisionChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcChecker;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcData;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcLoader;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcPersister;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcTreat;
import ch.globaz.pegasus.businessimpl.utils.decision.ValiderDecisionSuppression;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author SCE 13 sept. 2010
 */
public class ValidationDecisionServiceImpl extends PegasusAbstractServiceImpl implements ValidationDecisionService {

    @Override
    public void checkAdresses(DecisionApresCalcul decisionApresCalcul) throws JadePersistenceException,
            DecisionException {
        AdresseTiersDetail detailTiers;
        try {

            detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                    decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire(),
                    Boolean.TRUE, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    JACalendar.todayJJsMMsAAAA(), null);
            if ((detailTiers == null) || (detailTiers.getFields() == null)) {
                JadeThread.logError(this.getClass().getName(), "pegasus.decisionApresCalcule.adressePaiment.mandatory");
            }

            detailTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdTiersCourrier(),
                    Boolean.TRUE, JACalendar.todayJJsMMsAAAA(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER, null);

            if ((detailTiers == null) || (detailTiers.getFields() == null)) {
                JadeThread.logError(this.getClass().getName(), "pegasus.decisionApresCalcule.adresse.mandatory");
            }
        } catch (JadeApplicationException e) {
            throw new DecisionException("Service for check adresse is not available");
        }

    }

    @Override
    public int count(ValidationDecisionSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    private void createAnnonceLaprams(DecisionApresCalcul decisionApresCalcul, DecisionApresCalcul dcAvant,
            PcaForDecompte pcaReplacedHome) throws AnnonceException, DecisionException {
        // générer communication en bd
        try {
            PegasusImplServiceLocator.getPrepareAnnonceLapramsService().genereAnnonceLapramsValidation(
                    decisionApresCalcul, dcAvant, pcaReplacedHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Could not find service GenerationAnnonceLapramsService!");
        }

    }

    @Override
    public void createAnnoncesLaprams(List<DecisionApresCalcul> list, List<PcaForDecompte> pcasReplaced)
            throws AnnonceException, DecisionException {

        sortPcaByDateAsc(pcasReplaced);
        sortDecisionByDate(list);

        PcaForDecompte pcaReplacedLast = null;

        for (int i = 0; i < list.size(); i++) {
            DecisionApresCalcul decisionApresCalcul = list.get(i);
            PcaForDecompte pcaReplacedHome = resovlePcaHomeReplaced(pcasReplaced, decisionApresCalcul);

            if (mustCreateAnnonce(pcaReplacedLast, pcaReplacedHome)) {
                DecisionApresCalcul dcAvant = resolvePrecedanteDecsion(list, i, decisionApresCalcul);
                createAnnonceLaprams(decisionApresCalcul, dcAvant, pcaReplacedHome);
            }

            pcaReplacedLast = pcaReplacedHome;
        }
    }

    private boolean mustCreateAnnonce(PcaForDecompte pcaReplacedLast, PcaForDecompte pcaReplacedHome) {
        return ((pcaReplacedLast == null) || (pcaReplacedHome == null))
                || ((pcaReplacedLast != null) && (pcaReplacedHome != null) && !pcaReplacedHome.equals(pcaReplacedLast));
    }

    private PcaForDecompte resovlePcaHomeReplaced(List<PcaForDecompte> pcasReplaced,
            DecisionApresCalcul decisionApresCalcul) {
        PcaForDecompte pcaReplacedHome = null;
        Periode periodeDecision = new Periode(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getDateDebutDecision(), decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getDateFinDecision());

        for (PcaForDecompte pca : pcasReplaced) {
            Periode priodePca = new Periode(pca.getSimplePCAccordee().getDateDebut(), pca.getSimplePCAccordee()
                    .getDateFin());
            if (periodeDecision.comparerChevauchement(priodePca).equals(
                    ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT)
                    && (pca != null)) {
                if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(pca.getSimplePCAccordee().getCsGenrePC())
                        && pca.getSimplePrestationsAccordees()
                                .getIdTiersBeneficiaire()
                                .equals(decisionApresCalcul.getPcAccordee().getSimplePrestationsAccordees()
                                        .getIdTiersBeneficiaire())) {
                    pcaReplacedHome = pca;
                }
            }
        }
        return pcaReplacedHome;
    }

    private void sortDecisionByDate(List<DecisionApresCalcul> list) {
        Collections.sort(list, new Comparator<DecisionApresCalcul>() {
            @Override
            public int compare(DecisionApresCalcul o1, DecisionApresCalcul o2) {
                PRPeriode priode1 = new PRPeriode(o1.getDecisionHeader().getSimpleDecisionHeader()
                        .getDateDebutDecision(), o1.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision());

                PRPeriode priode2 = new PRPeriode(o2.getDecisionHeader().getSimpleDecisionHeader()
                        .getDateDebutDecision(), o2.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision());
                if (priode2.equals(priode1)) {
                    Integer a = Integer.parseInt(o1.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire());
                    Integer b = Integer.parseInt(o2.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire());
                    return a.compareTo(b);
                } else {
                    return priode1.compareTo(priode2) * -1;
                }
            }
        });
    }

    private void sortPcaByDateAsc(List<PcaForDecompte> pcasReplaced) {
        Collections.sort(pcasReplaced, new Comparator<PcaForDecompte>() {
            @Override
            public int compare(PcaForDecompte o1, PcaForDecompte o2) {
                PRPeriode priode1 = new PRPeriode(o1.getSimplePCAccordee().getDateDebut(), o1.getSimplePCAccordee()
                        .getDateFin());

                PRPeriode priode2 = new PRPeriode(o2.getSimplePCAccordee().getDateDebut(), o2.getSimplePCAccordee()
                        .getDateFin());

                if (priode2.equals(priode1)) {
                    Integer a = Integer.parseInt(o1.getSimplePCAccordee().getCsRoleBeneficiaire());
                    Integer b = Integer.parseInt(o2.getSimplePCAccordee().getCsRoleBeneficiaire());
                    return a.compareTo(b);
                } else {
                    return priode1.compareTo(priode2) * -1;
                }
            }
        });
    }

    private DecisionApresCalcul resolvePrecedanteDecsion(List<DecisionApresCalcul> list, int i,
            DecisionApresCalcul decisionApresCalcul) {
        DecisionApresCalcul dcAvant = null;
        if (i > 0) {
            if (!isSameBeneficiaire(decisionApresCalcul, list.get(i - 1))) {
                if (i > 1) {
                    dcAvant = list.get(i - 2);
                }
            } else {
                dcAvant = list.get(i - 1);
            }
        }
        return dcAvant;
    }

    private boolean isSameBeneficiaire(DecisionApresCalcul decisionApresCalcul, DecisionApresCalcul dcAvant) {
        return decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire()
                .equals(dcAvant.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire());
    }

    private void createCommunicationOCC(List<DecisionApresCalcul> list, List<PcaForDecompte> listAnciennePca)
            throws PrestationException, DecisionException {
        try {
            PegasusImplServiceLocator.getGenerationCommunicationOCCService().genereCommunicationOCCValidation(list,
                    listAnciennePca);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Could not find service GenerationCommunicationOCCService!");
        }
        // }

    }

    @Override
    public boolean isPaymentDoneBetweenTheValidation(DecisionApresCalcul decisionApresCalcul)
            throws JadeApplicationServiceNotAvailableException, DecisionException {
        return ValidationDecisionChecker.isPaymentDoneBetweenTheValidation(decisionApresCalcul);
    }

    @Override
    public DecisionApresCalcul preValidDecisionApresCalcul(DecisionApresCalcul decisionApresCalcul)
            throws JadePersistenceException, DecisionException, JadeApplicationServiceNotAvailableException {

        checkAdresses(decisionApresCalcul);

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            // MIse à jour de l'entité
            decisionApresCalcul = PegasusImplServiceLocator.getDecisionApresCalculService().updateForPrevalidation(
                    decisionApresCalcul);
        }

        return decisionApresCalcul;
    }

    @Override
    public DecisionRefus preValidDecisionRefus(DecisionRefus decisionRefus) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException {

        // Set etat cs header --> preValider
        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_PRE_VALIDE);

        // MIse à jour de l'entité
        return PegasusImplServiceLocator.getDecisionRefusService().updateForPrevalidation(decisionRefus);

    }

    @Override
    public ValidationDecisionSearch search(ValidationDecisionSearch validationDecisionSearch)
            throws JadePersistenceException, DecisionException, JadeApplicationServiceNotAvailableException {

        if (validationDecisionSearch == null) {
            throw new DecisionException("Unable to search validationDecisionSearch, the model passed is null!");
        }
        return (ValidationDecisionSearch) JadePersistenceManager.search(validationDecisionSearch);
    }

    @Override
    public ValidationDecisionSuppressionWithPcaSearch search(
            ValidationDecisionSuppressionWithPcaSearch validationDecisionSearch) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException {

        if (validationDecisionSearch == null) {
            throw new DecisionException("Unable to search validationDecisionSearch, the model passed is null!");
        }
        return (ValidationDecisionSuppressionWithPcaSearch) JadePersistenceManager.search(validationDecisionSearch);
    }

    @Override
    public DecisionRefus validDecisionRefus(DecisionRefus decisionRefus) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException, DemandeException, DossierException {
        // Set etat decision
        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_VALIDE);

        return PegasusImplServiceLocator.getDecisionRefusService().updateForValidation(decisionRefus);

    }

    private ValiderDecisionAcData valideDecisionAc(String idVersionDroit, boolean forceCreationLotRestitution)
            throws JadePersistenceException, JadeNoBusinessLogSessionError, JadeApplicationException, DecisionException {
        ValiderDecisionAcLoader loader = new ValiderDecisionAcLoader();
        ValiderDecisionAcData data = loader.load(idVersionDroit);
        ValiderDecisionAcChecker.check(data);

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
            data = treat.treat();

            // Recherche lot ou création
            SimpleLot simpleLot = null;

            if (forceCreationLotRestitution) {
                String monthYear = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_MMsYYYY);
                String libelle = BSessionUtil.getSessionFromThreadContext().getLabel(
                        "PEGASUS_JAVA_NOM_LOT_DECISION_RESTITUTION");
                String numNss = data.getRequerant().getPersonneEtendue().getNumAvsActuel();

                String description = (" PC-" + libelle + " " + monthYear + "/" + numNss);

                simpleLot = PegasusServiceLocator.getLotService().createLot(IRELot.CS_TYP_LOT_DECISION_RESTITUTION,
                        description);
            } else {
                simpleLot = PegasusServiceLocator.getLotService().findCurrentDecisionLotOrCreate();
            }

            // On renseinge l'idlot
            data.getSimplePrestation().setIdLot(simpleLot.getIdLot());

            // Persistance des données
            ValiderDecisionAcPersister persister = new ValiderDecisionAcPersister(data);
            persister.persist();
        }
        return data;
    }

    @Override
    public void validerDecisionSuppression(DecisionSuppression decisionSuppression, boolean isComptabilisationAuto)
            throws JadePersistenceException, DecisionException, JadeCloneModelException, JadeApplicationException {

        if (!PegasusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise()) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "JSP_PC_VALIDATION_DECISIONS_D_LOCK_LOT"));
        }

        // Check de l'integrité métier de la date de décision
        SimpleDecisionHeaderChecker.checkForCoherenceDateDecision(decisionSuppression.getDecisionHeader()
                .getSimpleDecisionHeader());

        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            ValiderDecisionSuppression validerDecisionSuppression = new ValiderDecisionSuppression();
            String idLot = validerDecisionSuppression.valider(decisionSuppression, isComptabilisationAuto);

            if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
                PegasusImplServiceLocator.getPrepareAnnonceLapramsService().genereAnnonceLapramsSuppression(
                        decisionSuppression);
            }

            if (EPCProperties.GESTION_COMMUNICATION_OCC.getBooleanValue()) {
                PegasusImplServiceLocator.getGenerationCommunicationOCCService().genereCommunicationOCCSuppression(
                        decisionSuppression);
            }

            // Comptabilisation
            if (isComptabilisationAuto) {
                String idOrganeExecution = REProperties.ORGANE_EXECUTION_PAIEMENT.getValue();
                String dateComptable = decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                        .getDateDecision();
                String dateEcheancePaiement = dateComptable;
                // lancement process de comptabilisation
                try {
                    PegasusServiceLocator.getLotService().comptabiliserLot(idLot, idOrganeExecution, "1", null,
                            dateComptable, dateEcheancePaiement);
                } catch (JAException e) {
                    throw new DecisionException("Unabled to comptabilise lot decision restitution", e);
                }
            }
        }

    }

    @Override
    public ValiderDecisionAcData validerToutesLesDesionsionApresCalcule(String idVersionDroit,
            boolean forceCreationLotRestitution) throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        if (idVersionDroit == null) {
            throw new DecisionException("Unable to valider decisionApresCalule, the idVersionDroit passed is null!");
        }

        if (!PegasusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise()) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "JSP_PC_VALIDATION_DECISIONS_D_LOCK_LOT"));
        }

        ValiderDecisionAcData data = valideDecisionAc(idVersionDroit, forceCreationLotRestitution);

        if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
            createAnnoncesLaprams(data.getDecisionsApresCalcul(), data.getPcasReplaced());
        }

        if (EPCProperties.GESTION_COMMUNICATION_OCC.getBooleanValue()) {
            createCommunicationOCC(data.getDecisionsApresCalcul(), data.getPcasReplaced());
        }

        return data;
    }
}
