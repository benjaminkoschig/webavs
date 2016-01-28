package globaz.ij.servlet;

/**
 * @author DVH
 */
public interface IIJActions {

    public static final String ACTION_ANNONCE = "ij.annonces.annonce";
    public static final String ACTION_ANNONCE3EMEREVISION = "ij.annonces.annonce3EmeRevision";
    public static final String ACTION_ANNONCE4EMEREVISION = "ij.annonces.annonce4EmeRevision";
    public static final String ACTION_ANNULER_CORRIGER_DEPUIS_PRONONCE = "ij.prononces.annulerCorrigerDepuisPrononce";
    public static final String ACTION_BASE_INDEMNISATION = "ij.basesindemnisation.baseIndemnisation";
    public static final String ACTION_BASE_INDEMNISATION_AIT_AA = "ij.basesindemnisation.baseIndemnisationAitAa";
    public static final String ACTION_BASE_INDEMNISATION_CONTROLE_ABSENCES = "ij.controleAbsences.baseIndemnisationAjax";
    public static final String ACTION_CALCUL_DECOMPTE = "ij.acor.calculACORDecompte";
    public static final String ACTION_CALCUL_DECOMPTE_AIT_AA = "ij.prestations.calculDecompteAitAa";
    public static final String ACTION_CALCUL_IJ = "ij.acor.calculACORIJ";
    public static final String ACTION_COMPENSATIONS_LOT = "ij.lots.factureJointCompensation";
    public static final String ACTION_CORRIGER_DEPUIS_PRONONCE = "ij.prononces.corrigerDepuisPrononce";
    public static final String ACTION_COTISATIONS = "ij.prestations.cotisation";
    public static final String ACTION_DOSSIER_CONTROLE_ABSENCES = "ij.controleAbsences.dossierControleAbsencesAjax";
    public static final String ACTION_ENVOYER_ANNONCES = "ij.process.envoyerAnnonces";
    public static final String ACTION_ENVOYER_CI = "ij.process.envoyerCI";
    public static final String ACTION_FACTURES_LOT = "ij.lots.factureACompenser";
    public static final String ACTION_FORMULAIRE_INDEMNISATION = "ij.basesindemnisation.formulaireIndemnisation";
    public static final String ACTION_GENERER_ATTESTATIONS_IJ = "ij.process.genererAttestations";
    public static final String ACTION_GENERER_COMPENSATIONS = "ij.process.genererCompensations";
    public static final String ACTION_GENERER_DECISION = "ij.process.genererDecision";
    public static final String ACTION_GENERER_DECOMPTES = "ij.process.genererDecomptes";
    public static final String ACTION_GENERER_FORMULAIRE = "ij.process.genererFormulaire";
    public static final String ACTION_GENERER_FORMULAIRES = "ij.process.genererFormulaires";
    public static final String ACTION_GENERER_LOT = "ij.process.genererLot";
    /**
     * Action d'admnisitration GLOBAZ.
     */
    public static final String ACTION_GLOBAZ_ADMIN = "ij.globaz.administrator";
    public static final String ACTION_GRANDE_IJ = "ij.prononces.grandeIJ";
    public static final String ACTION_IJ_CALCULEE_JOINT_GRANDE_PETITE = "ij.prestations.iJCalculeeJointGrandePetite";
    public static final String ACTION_IJ_CALCULEES = "ij.prestations.iJCalculeeJointIndemnite";
    public static final String ACTION_INFO_COMPL = "ij.prononces.infoCompl";
    public static final String ACTION_INSCRIRE_CI = "ij.process.inscrireCI";
    public static final String ACTION_LISTE_CONTROLE = "ij.process.listeControle";
    public static final String ACTION_LISTE_FORMULAIRES_NON_RECUS = "ij.process.listeFormulairesNonRecus";
    public static final String ACTION_LISTE_RECAPITULATION_ANNONCE = "ij.process.recapitulationAnnonce";
    public static final String ACTION_LOTS = "ij.lots.lot";
    public static final String ACTION_MESURE_JOINT_AGENT_EXECUTION = "ij.prononces.mesureJointAgentExecution";
    public static final String ACTION_PERIODE_CONTROLE_ABSENCES = "ij.controleAbsences.periodeControleAbsencesAjax";
    public static final String ACTION_PETITE_IJ = "ij.prononces.petiteIJ";
    public static final String ACTION_PETITE_IJ_JOINT_REVENU = "ij.prononces.petiteIJJointRevenu";
    public static final String ACTION_PETITE_IJ_P = "ij.prononces.petiteIJP";
    public static final String ACTION_PRESTATION_JOINT_LOT_PRONONCE = "ij.prestations.prestationJointLotPrononce";
    public static final String ACTION_PRONONCE = "ij.prononces.prononce";
    public static final String ACTION_PRONONCE_CONTROLE_ABSENCE = "ij.controleAbsences.prononceAjax";
    public static final String ACTION_PRONONCE_JOINT_DEMANDE = "ij.prononces.prononceJointDemande";
    public static final String ACTION_RECAPITULATIF_PRONONCE = "ij.prononces.recapitulatifPrononce";
    public static final String ACTION_REPARTITION_PAIEMENTS = "ij.prestations.repartitionJointPrestation";
    public static final String ACTION_REQUERANT = "ij.prononces.requerant";
    public static final String ACTION_SAISIE_ABSENCE_CONTROLE_ABSENCES = "ij.controleAbsences.saisieAbsenceAjax";
    public static final String ACTION_SAISIE_PRONONCE = "ij.prononces.saisiePrononce";
    public static final String ACTION_SAISIE_PRONONCE_AIT = "ij.prononces.saisiePrononceAit";
    public static final String ACTION_SAISIE_PRONONCE_ALLOC_ASSIST = "ij.prononces.saisiePrononceAllocAssistance";
    public static final String ACTION_SAISIR_ECHEANCE = "ij.prononces.saisirEcheance";
    public static final String ACTION_SAISIR_NO_DECISION = "ij.prononces.saisirNoDecision";
    public static final String ACTION_SAISIR_TAUX_IS = "ij.prononces.saisirTauxIS";
    public static final String ACTION_SITUATION_PROFESSIONNELLE = "ij.prononces.situationProfessionnelle";
    public static final String ACTION_TERMINER_PRONONCE = "ij.prononces.terminerPrononce";
    public static final String ACTION_VALIDER_DECISION = "ij.process.validerDecision";
}
