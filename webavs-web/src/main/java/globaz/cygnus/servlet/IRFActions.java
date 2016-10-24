/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.servlet;

/**
 * @author jje
 * 
 *         Définition des constantes pour les actions
 */

public interface IRFActions {
    // Uniquement tests CCJU
    public static final String ACTION_ADAPTATIONS_JOURNALIERE = "cygnus.process.adaptationsJournaliere";

    public static final String ACTION_ATTESTATION = "cygnus.attestations.attestation";
    public static final String ACTION_ATTESTATION_JOINT_TIERS = "cygnus.attestations.attestationJointDossierJointTiers";
    public static final String ACTION_ATTESTATION_PIED_DE_PAGE = "cygnus.attestations.attestationPiedDePage";

    public static final String ACTION_AVANCE = "cygnus.avances.avance";

    public static final String ACTION_COMPTABILISER = "cygnus.process.comptabiliser";
    public static final String ACTION_CONTRIBUTIONS_ASSISTANCE_AI = "cygnus.contributions.contributionsAssistanceAI";
    public static final String ACTION_CONVENTION = "cygnus.conventions.convention";
    public static final String ACTION_DECISION_JOINT_TIERS = "cygnus.decisions.decisionJointTiers";
    public static final String ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS = "cygnus.demandes.demandeJointDossierJointTiers";
    public static final String ACTION_DOCUMENTS = "cygnus.documents.documents";

    public static final String ACTION_DOSSIER_JOINT_TIERS = "cygnus.dossiers.dossierJointTiers";

    // action utile uniquement pour cacher la checkbox "forcer paiement" dans une demande
    public static final String ACTION_FORCER_PAIEMENT = "cygnus.demande.forcerPaiement";
    public static final String ACTION_GENERER_DOCUMENT = "cygnus.documents.genererDocument";
    public static final String ACTION_IMPORTER_AVASAD = "cygnus.process.importerAvasad";
    public static final String ACTION_IMPORTER_FINANCEMENT_SOIN = "cygnus.process.importerFinancementSoin";
    public static final String ACTION_IMPORTER_SOIN_A_DOMICILE = "cygnus.process.importerSoinADomicile";

    public static final String ACTION_IMPUTER_SUR_QD = "cygnus.demandes.imputerSurQd";

    public static final String ACTION_JADE_WIDGET = "widget.action.jade";
    public static final String ACTION_LISTE_CONTRIBUTION_ASSISTANCE_AI = "cygnus.process.listeContributionsAssistanceAI";
    public static final String ACTION_LISTE_RECAPITULATIVE_PAIEMENTS = "cygnus.process.listeRecapitulativePaiements";
    public static final String ACTION_LOTS = "cygnus.paiement.lot";
    public static final String ACTION_MAJ_LIMITE_ANNUELLE_QD_ASSURE = "cygnus.qds.majLimiteAnnuelleQdAssure";
    public static final String ACTION_MAJ_LIMITE_ANNUELLE_QD_DROIT_PC = "cygnus.qds.majLimiteAnnuelleQdDroitPC";

    public static final String ACTION_ORDRES_VERSEMENTS = "cygnus.ordresversements.ordresVersements";
    public static final String ACTION_PARAMETRAGE_GRANDE_QD = "cygnus.pots.parametrageGrandeQD";
    public static final String ACTION_PARAMETRAGE_SOINS_RECHERCHE_PERIODE = "cygnus.typeDeSoins.parametrageTypeSoinsRecherchePeriode";
    public static final String ACTION_PARAMETRER_SOINS = "cygnus.typeDeSoins.typeDeSoin";

    public static final String ACTION_PREPARER_DECISIONS = "cygnus.process.preparerDecisions";
    public static final String ACTION_PRESTATION = "cygnus.paiement.prestation";
    public static final String ACTION_PRESTATION_ACCORDEE = "cygnus.prestationsaccordees.prestationsAccordees";

    public static final String ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE = "cygnus.qds.qdJointDossierJointTiersJointDemande";

    public static final String ACTION_RECHERCHE_MONTANTS_CONVENTION = "cygnus.conventions.rechercheMontantsConvention";
    public static final String ACTION_RECHERCHE_MOTIFS_DE_REFUS = "cygnus.motifsDeRefus.rechercheMotifsDeRefus";

    public static final String ACTION_SAISIE_DEMANDE = "cygnus.demandes.saisieDemande";
    public static final String ACTION_SAISIE_DEMANDE_PIED_DE_PAGE = "cygnus.demandes.saisieDemandePiedDePage";

    public static final String ACTION_SAISIE_MONTANTS_CONVENTION = "cygnus.conventions.saisieMontantsConvention";
    public static final String ACTION_SAISIE_MOTIFS_DE_REFUS = "cygnus.motifsDeRefus.saisieMotifsDeRefus";

    public static final String ACTION_SAISIE_QD = "cygnus.qds.saisieQd";
    public static final String ACTION_SAISIE_QD_ASSURE_CHOIX_TYPE_DE_SOIN = "cygnus.qds.saisieQdAssureChoixTypeDeSoin";

    public static final String ACTION_SAISIE_QD_AUGMENTATION = "cygnus.qds.qdSaisieAugmentation";

    public static final String ACTION_SAISIE_QD_CHOIX_GENRE = "cygnus.qds.saisieQdChoixGenre";

    public static final String ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE = "cygnus.qds.qdSaisiePeriodeValiditeQdPrincipale";

    public static final String ACTION_SAISIE_QD_PIED_DE_PAGE = "cygnus.qds.saisieQdPiedDePage";

    public static final String ACTION_SAISIE_QD_SOLDE_CHARGE = "cygnus.qds.qdSaisieSoldeCharge";

    public static final String ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU = "cygnus.qds.qdSaisieSoldeExcedentDeRevenu";

    public static final String ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION = "cygnus.conventions.saisieSoinFournisseurConvention";

    public static final String ACTION_SITUATION_FAMILIALE = "cygnus.dossiers.situationFamiliale";

    public static final String ACTION_STATISTIQUES_PAR_MONTANTS_SASH = "cygnus.process.statistiquesParMontantsSash";

    public static final String ACTION_STATISTIQUES_PAR_NB_CAS = "cygnus.process.statistiquesParNbCas";

    public static final String ACTION_VALIDER_DECISIONS = "cygnus.process.validerDecisions";

}
