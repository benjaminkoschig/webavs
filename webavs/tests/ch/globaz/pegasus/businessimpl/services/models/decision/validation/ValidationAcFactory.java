package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexe;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class ValidationAcFactory {

    public static final String DATE_DECISION = "05.04.2013";
    public static final String DATE_DERNIER_PAIEMENT = "04.2013";
    public static final String DATE_PROCHAIN_PAIEMENT = "05.2013";

    public static DecisionApresCalcul generateDecisionAcConjoint(String dateDebut, String dateFin, String montant,
            String dateDecision) {
        DecisionApresCalcul dc = new DecisionApresCalcul();
        dc.setDecisionHeader(ValidationAcFactory.generateDecisionHeader(dateDebut, dateFin, dateDecision));
        dc.setPlanCalcul(ValidationAcFactory.generateSimplePlanCalcule(montant));
        dc.setPcAccordee(ValidationAcFactory.generatePcaConjointCalcule(dateDebut, dateFin));
        dc.setSimpleDecisionApresCalcul(ValidationAcFactory.generateSimpleDecisionAc(dateDecision));
        dc.setSimpleValidationDecision(new SimpleValidationDecision());
        dc.setVersionDroit(new VersionDroit());
        dc.getVersionDroit().setSimpleVersionDroit(dc.getPcAccordee().getSimpleVersionDroit());
        return dc;
    }

    public static DecisionApresCalcul generateDecisionApresCalcul(String dateDebut, String dateFin, String montant) {
        DecisionApresCalcul dc = new DecisionApresCalcul();
        dc.setDecisionHeader(ValidationAcFactory.generateDecisionHeader(dateDebut, dateFin,
                ValidationAcFactory.DATE_DECISION));
        dc.setPlanCalcul(ValidationAcFactory.generateSimplePlanCalcule(montant));
        dc.setPcAccordee(ValidationAcFactory.generatePcaNewDomCalcule(dateDebut, dateFin));
        dc.setSimpleDecisionApresCalcul(ValidationAcFactory.generateSimpleDecisionAc(ValidationAcFactory.DATE_DECISION));
        dc.setSimpleValidationDecision(new SimpleValidationDecision());
        dc.setVersionDroit(new VersionDroit());
        dc.getVersionDroit().setSimpleVersionDroit(dc.getPcAccordee().getSimpleVersionDroit());
        return dc;
    }

    public static DecisionApresCalcul generateDecisionApresCalcul(String dateDebut, String dateFin, String montant,
            String dateDecision) {
        DecisionApresCalcul dc = new DecisionApresCalcul();
        dc.setDecisionHeader(ValidationAcFactory.generateDecisionHeader(dateDebut, dateFin, dateDecision));
        dc.setPlanCalcul(ValidationAcFactory.generateSimplePlanCalcule(montant));
        dc.setPcAccordee(ValidationAcFactory.generatePcaNewDomCalcule(dateDebut, dateFin));
        dc.setSimpleDecisionApresCalcul(ValidationAcFactory.generateSimpleDecisionAc(dateDecision));
        dc.setSimpleValidationDecision(new SimpleValidationDecision());
        dc.setVersionDroit(new VersionDroit());
        dc.getVersionDroit().setSimpleVersionDroit(dc.getPcAccordee().getSimpleVersionDroit());
        // dc.getVersionDroit().setSimpleDroit(generateSimple)
        return dc;
    }

    public static DecisionApresCalcul generateDecisionApresCalculPourConjointCoupleSep(String dateDebut,
            String dateFin, String montant) {
        DecisionApresCalcul dc = new DecisionApresCalcul();
        dc.setDecisionHeader(ValidationAcFactory.generateDecisionHeader(dateDebut, dateFin,
                ValidationAcFactory.DATE_DECISION));
        dc.setPlanCalcul(ValidationAcFactory.generateSimplePlanCalcule(montant));
        dc.setPcAccordee(ValidationAcFactory.generatePcaConjointCalcule(dateDebut, dateFin));
        dc.setSimpleDecisionApresCalcul(ValidationAcFactory.generateSimpleDecisionAc(ValidationAcFactory.DATE_DECISION));
        dc.setSimpleValidationDecision(new SimpleValidationDecision());
        dc.setVersionDroit(new VersionDroit());
        dc.getVersionDroit().setSimpleVersionDroit(dc.getPcAccordee().getSimpleVersionDroit());
        // dc.getVersionDroit().setSimpleDroit(generateSimple)
        return dc;
    }

    private static DecisionHeader generateDecisionHeader(String dateDebut, String dateFin, String dateDecision) {
        DecisionHeader decisionHeader = new DecisionHeader();
        decisionHeader.setSimpleDecisionHeader(ValidationAcFactory.generateSimpleDecisionHeaderOctroi(dateDebut,
                dateFin, dateDecision));
        return decisionHeader;
    }

    public static SimplePCAccordee generateNewSimplePcaConjointCalcule(String dateDebut, String dateFin,
            SimpleVersionDroit simpleVersionDroit) {
        SimplePCAccordee pca = new SimplePCAccordee();
        // pca.setCodeRente(codeRente);
        pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        pca.setCsGenrePC(IPCPCAccordee.CS_GENRE_PC_DOMICILE);
        pca.setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
        pca.setCsTypePC(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
        pca.setDateFin(dateFin);
        pca.setDateDebut(dateDebut);
        pca.setIdPCAccordee("120");
        pca.setIsCalculRetro(true);
        pca.setIdVersionDroit(simpleVersionDroit.getIdVersionDroit());
        return pca;
    }

    public static SimplePCAccordee generateNewSimplePcaRequerantCalcule(String dateDebut, String dateFin,
            SimpleVersionDroit simpleVersionDroit) {
        SimplePCAccordee pca = new SimplePCAccordee();
        // pca.setCodeRente(codeRente);
        pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        pca.setCsGenrePC(IPCPCAccordee.CS_GENRE_PC_DOMICILE);
        pca.setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        pca.setCsTypePC(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
        pca.setDateFin(dateFin);
        pca.setDateDebut(dateDebut);
        pca.setIdPCAccordee("12");
        pca.setIsCalculRetro(true);
        pca.setIdVersionDroit(simpleVersionDroit.getIdVersionDroit());
        return pca;
    }

    public static SimplePrestationsAccordees generatenNewPrestationsAccordeesCalcule(String dateDebut, String dateFin,
            String idTiers) {
        return ValidationAcFactory.generatenNewPrestationsAccordeesCalcule(dateDebut, dateFin, idTiers, null, null);
    }

    public static SimplePrestationsAccordees generatenNewPrestationsAccordeesCalcule(String dateDebut, String dateFin,
            String idTiers, String montant, String sousCodePrestation) {
        SimplePrestationsAccordees presation = new SimplePrestationsAccordees();
        presation.setCodePrestation(PRCodePrestationPC._150.getCodePrestationAsString());
        presation.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        presation.setCsGenre(IREPrestationAccordee.CS_GENRE_PC);
        presation.setDateDebutDroit(dateDebut);
        presation.setIdTiersBeneficiaire(idTiers);
        presation.setDateFinDroit(dateFin);
        presation.setSousCodePrestation(null);
        presation.setMontantPrestation(montant);
        presation.setSousCodePrestation(sousCodePrestation);
        return presation;
    }

    public static SimplePrestationsAccordees generatenReplacedPrestationsAccordees(String dateDebut, String dateFin,
            String idTiers) {
        SimplePrestationsAccordees presation = new SimplePrestationsAccordees();
        presation.setCodePrestation(PRCodePrestationPC._150.getCodePrestationAsString());
        presation.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        presation.setCsGenre(IREPrestationAccordee.CS_GENRE_PC);
        presation.setDateDebutDroit(dateDebut);
        presation.setIdTiersBeneficiaire(idTiers);
        presation.setDateFinDroit(dateFin);
        presation.setSousCodePrestation(null);
        return presation;
    }

    public static PCAccordee generatePcaConjointCalcule(String dateDebut, String dateFin) {
        return ValidationAcFactory.generatePcaConjointCalcule(dateDebut, dateFin, null, null);
    }

    public static PCAccordee generatePcaConjointCalcule(String dateDebut, String dateFin, String montant,
            String sousCodePrestation) {
        PCAccordee pca = new PCAccordee();
        pca.getPersonneEtendue().setId("30");
        pca.setSimpleInformationsComptabilite(ValidationAcFactory.genreateSimpleInfoCompta());
        pca.getSimplePrestationsAccordees().setIdPrestationAccordee(pca.getPersonneEtendue().getTiers().getIdTiers());
        pca.setSimplePrestationsAccordees(ValidationAcFactory.generatenNewPrestationsAccordeesCalcule(dateDebut,
                dateFin, pca.getPersonneEtendue().getId(), montant, sousCodePrestation));
        pca.setSimpleVersionDroit(ValidationAcFactory.generateSimpleVersionDroitInitialCalcule());
        pca.setSimplePCAccordee(ValidationAcFactory.generateNewSimplePcaConjointCalcule(dateDebut, dateFin,
                pca.getSimpleVersionDroit()));
        return pca;
    }

    public static PcaForDecompte generatePcaForDecompte(DecisionApresCalcul dc) {
        PcaForDecompte pca = new PcaForDecompte();
        pca.setMontantPCMensuelle(dc.getPlanCalcul().getMontantPCMensuelle());
        pca.setEtatPC(dc.getPlanCalcul().getEtatPC());
        pca.setSimpleInformationsComptabilite(dc.getPcAccordee().getSimpleInformationsComptabilite());
        pca.setSimpleInformationsComptabiliteConjoint(dc.getPcAccordee().getSimpleInformationsComptabiliteConjoint());
        pca.setSimplePCAccordee(dc.getPcAccordee().getSimplePCAccordee());
        pca.setSimplePrestationsAccordees(dc.getPcAccordee().getSimplePrestationsAccordees());
        pca.setSimplePrestationsAccordeesConjoint(dc.getPcAccordee().getSimplePrestationsAccordeesConjoint());
        return pca;
    }

    public static PcaForDecompte generatePcaForDecompte(String dateDebut, String dateFin, String montant) {
        PcaForDecompte pca = new PcaForDecompte();
        pca.setMontantPCMensuelle(montant);
        pca.setEtatPC(IPCValeursPlanCalcul.STATUS_OCTROI);
        // pca.setSimpleInformationsComptabilite(dc.getPcAccordee().getSimpleInformationsComptabilite());
        // pca.setSimpleInformationsComptabiliteConjoint(dc.getPcAccordee().getSimpleInformationsComptabiliteConjoint());
        pca.setSimplePCAccordee(ValidationAcFactory.generateReplacedSimplePcaRequerant(dateDebut, dateFin));
        pca.setSimplePrestationsAccordees(ValidationAcFactory.generatenReplacedPrestationsAccordees(dateDebut, dateFin,
                null));
        // pca.setSimplePrestationsAccordeesConjoint();
        return pca;
    }

    public static PCAccordee generatePcaNewDomCalcule(String dateDebut, String dateFin) {
        PCAccordee pca = new PCAccordee();
        pca.getPersonneEtendue().setId("30");
        pca.setSimpleInformationsComptabilite(ValidationAcFactory.genreateSimpleInfoCompta());
        pca.setSimplePrestationsAccordees(ValidationAcFactory.generatenNewPrestationsAccordeesCalcule(dateDebut,
                dateFin, pca.getPersonneEtendue().getId()));
        pca.setSimpleVersionDroit(ValidationAcFactory.generateSimpleVersionDroitInitialCalcule());
        pca.setSimplePCAccordee(ValidationAcFactory.generateNewSimplePcaRequerantCalcule(dateDebut, dateFin,
                pca.getSimpleVersionDroit()));
        return pca;
    }

    public static PCAccordee generatePcaRequerantCalcule(String dateDebut, String dateFin) {
        return ValidationAcFactory.generatePcaRequerantCalcule(dateDebut, dateFin, null, null);
    }

    public static PCAccordee generatePcaRequerantCalcule(String dateDebut, String dateFin, String montant,
            String sousCodePrestation) {
        PCAccordee pca = new PCAccordee();
        pca.getPersonneEtendue().setId("10");
        pca.setSimpleInformationsComptabilite(ValidationAcFactory.genreateSimpleInfoCompta());
        pca.setSimplePrestationsAccordees(ValidationAcFactory.generatenNewPrestationsAccordeesCalcule(dateDebut,
                dateFin, pca.getPersonneEtendue().getId(), montant, sousCodePrestation));
        pca.getSimplePrestationsAccordees().setIdPrestationAccordee(pca.getPersonneEtendue().getTiers().getIdTiers());
        pca.setSimpleVersionDroit(ValidationAcFactory.generateSimpleVersionDroitInitialCalcule());
        pca.setSimplePCAccordee(ValidationAcFactory.generateNewSimplePcaRequerantCalcule(dateDebut, dateFin,
                pca.getSimpleVersionDroit()));
        return pca;
    }

    public static SimplePCAccordee generateReplacedSimplePcaRequerant(String dateDebut, String dateFin) {
        SimplePCAccordee pca = new SimplePCAccordee();
        // pca.setCodeRente(codeRente);
        pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        pca.setCsGenrePC(IPCPCAccordee.CS_GENRE_PC_DOMICILE);
        pca.setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        pca.setCsTypePC(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
        pca.setDateFin(dateFin);
        pca.setDateDebut(dateDebut);
        pca.setIdPCAccordee("12");
        pca.setIdVersionDroit("1");
        return pca;
    }

    public static SimpleDecisionApresCalcul generateSimpleDecisionAc(String dateProchainPaiement) {
        SimpleDecisionApresCalcul decisions = new SimpleDecisionApresCalcul();
        decisions.setDateProchainPaiement(dateProchainPaiement);
        decisions.setCsTypePreparation(IPCDecision.CS_PREP_STANDARD);
        return decisions;
    }

    private static SimpleDecisionHeader generateSimpleDecisionHeaderOctroi(String dateDebut, String dateFin,
            String dateDecision) {
        SimpleDecisionHeader header = new SimpleDecisionHeader();
        header.setCsEtatDecision(IPCDecision.CS_ETAT_PRE_VALIDE);
        header.setCsGenreDecision(IPCDecision.CS_GENRE_DECISION);
        header.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        header.setDateDebutDecision(dateDebut);
        header.setDateDecision(dateDecision);
        header.setDateFinDecision(dateFin);
        // header.setDatePreparation(datePreparation);
        // header.setDateValidation(dateValidation);
        return header;
    }

    private static SimplePlanDeCalcul generateSimplePlanCalcule(String montant) {
        SimplePlanDeCalcul planDeCalcul = new SimplePlanDeCalcul();
        planDeCalcul.setMontantPCMensuelle(montant);
        planDeCalcul.setEtatPC(IPCValeursPlanCalcul.STATUS_OCTROI);
        return planDeCalcul;
    }

    public static SimpleVersionDroit generateSimpleVersionDroitCalcule() {
        SimpleVersionDroit droit = new SimpleVersionDroit();
        droit.setCsEtatDroit(IPCDroits.CS_VALIDE);
        droit.setNoVersion("3");
        droit.setIdVersionDroit("30");
        droit.setIdDroit("30");
        return droit;
    }

    public static SimpleVersionDroit generateSimpleVersionDroitInitialCalcule() {
        SimpleVersionDroit droit = new SimpleVersionDroit();
        droit.setCsEtatDroit(IPCDroits.CS_CALCULE);
        droit.setNoVersion("1");
        droit.setIdVersionDroit("10");
        droit.setIdDroit("10");
        return droit;
    }

    public static SimpleVersionDroit generateSimpleVersionDroitValide() {
        SimpleVersionDroit droit = new SimpleVersionDroit();
        droit.setCsEtatDroit(IPCDroits.CS_VALIDE);
        droit.setNoVersion("2");
        droit.setIdVersionDroit("20");
        droit.setIdDroit("20");
        return droit;
    }

    public static SimpleInformationsComptabilite genreateSimpleInfoCompta() {
        SimpleInformationsComptabilite infoCompta = new SimpleInformationsComptabilite();
        return infoCompta;
    }

    public static ValiderDecisionAcData validationCorrectionDroitAcData(String dateDebut, String dateFin,
            List<DecisionApresCalcul> decisions) {
        return ValidationAcFactory.validationCorrectionDroitAcData(dateDebut, dateFin, decisions, true);
    }

    public static ValiderDecisionAcData validationCorrectionDroitAcData(String dateDebut, String dateFin,
            List<DecisionApresCalcul> decisions, boolean isCalculRetro) {
        ValiderDecisionAcData data = null;
        data = ValidationAcFactory.validationInitialAcData(dateDebut, dateFin, decisions);
        data.setSimpleVersionDroitReplaced(ValidationAcFactory.generateSimpleVersionDroitValide());
        data.setSimpleVersionDroitNew(ValidationAcFactory.generateSimpleVersionDroitCalcule());
        for (DecisionApresCalcul decisionApresCalcul : decisions) {
            decisionApresCalcul.getPcAccordee().setSimpleVersionDroit(data.getSimpleVersionDroitNew());
            decisionApresCalcul.getPcAccordee().getSimplePCAccordee().setIsCalculRetro(isCalculRetro);
        }
        return data;
    }

    public static ValiderDecisionAcData validationInitialAcData(String dateDebut, String dateFin,
            List<DecisionApresCalcul> decisions) {
        ValiderDecisionAcData data = new ValiderDecisionAcData();
        data.setPcasNew(new ArrayList<PcaForDecompte>());
        for (DecisionApresCalcul decisionApresCalcul : decisions) {
            PcaForDecompte pca = ValidationAcFactory.generatePcaForDecompte(decisionApresCalcul);
            data.getPcasNew().add(pca);
        }
        data.setAllocationsNoel(new ArrayList<SimpleAllocationNoel>());
        // data.setConjoint(new PersonneEtendueComplexModel());
        // data.setRequerant(new PersonneEtendueComplexModel());
        data.setCreanciers(new ArrayList<CreanceAccordee>());
        data.setDateDebut(dateDebut);
        data.setDateFin(dateFin);
        data.setDateDecision(decisions.get(0).getDecisionHeader().getSimpleDecisionHeader().getDateDecision());
        data.setDateProchainPaiement(ValidationAcFactory.DATE_PROCHAIN_PAIEMENT);
        data.setDecisionApresCalculs(decisions);
        data.setDettes(new ArrayList<DetteCompenseCompteAnnexe>());
        data.setSimpleDemande(decisions.get(0).getVersionDroit().getDemande().getSimpleDemande());
        data.setSimpleVersionDroitNew(decisions.get(0).getPcAccordee().getSimpleVersionDroit());
        data.setHasAllocationNoel(false);
        data.setNbMonthBetween(48);

        data.setPcaCopie(new ArrayList<PCAccordee>());
        data.setCurrentUserId("dma");
        data.setRequerant(decisions.get(0).getPcAccordee().getPersonneEtendue());

        data.setPcasReplaced(new ArrayList<PcaForDecompte>());
        data.setRentuesPayements(new ArrayList<SimpleRetenuePayement>());
        return data;
    }
}
