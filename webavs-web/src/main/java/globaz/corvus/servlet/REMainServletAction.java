package globaz.corvus.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.widget.FWJadeWidgetServletAction;
import globaz.prestation.servlet.PRDefaultAction;
import java.util.HashMap;

/**
 * Contient le mapping entre une action utilisateur et la classe Java qui va la traiter
 */
public class REMainServletAction {

    private static final HashMap<String, Class<? extends FWDefaultServletAction>> ACTIONS = new HashMap<String, Class<? extends FWDefaultServletAction>>();

    static {
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_2EME_ENVOI_CENTRALE, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_ADAPTATION_MANUELLE, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_CIRCULAIRE, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_ENVOI_ANNONCES_SUB, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_IMPORT_51_53, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_LISTE_ANNONCES_SUB, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_LISTE_ERREURS, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_LISTE_PRST_AUGMENTEES, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_LISTE_RECAP_ADAPTATION, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_MAJ_RECAP_ADAPTATION, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_MISE_A_JOUR_PREST, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_PMT_FICTIF, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ADAPTATION_RENTES_ADAPTEES, REAdaptationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_AFFICHER_LOT_DECISION, RELotAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNONCES, REAnnoncesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNONCES_AUGMENTATION_10, REAnnoncesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNONCES_AUGMENTATION_9, REAnnoncesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNONCES_DIMINUTION_10, REAnnoncesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNONCES_DIMINUTION_9, REAnnoncesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNONCES_PONCTUELLES, REAnnoncePonctuelleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE,
                REAnnulerDiminutionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_AVANCES, REAvanceAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_BASES_DE_CALCUL, REBasesCalculAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_CALCUL_DEMANDE_RENTE, RECalculACORDemandeRenteAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_CALCUL_INTERET_MORATOIRE,
                RECalculInteretMoratoireAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_COMPARAISON_CENTRALE, REComparaisonCentraleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_CONCORDANCE_CENTRALE, REConcordanceCentraleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_COPIES_DEFAUT, RECopiesDefautAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_CREANCIER, RECreancierAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DEBLOQUER_MONTANT_RENTE_ACCORDEE, PRDefaultAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DECISIONS, REDecisionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DECOMPTE, REDecompteAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT,
                REDemandesRassemblementAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT,
                REDemandesRassemblementAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE,
                REDemandeRenteJointPrestationAccordeeAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE,
                REDiminutionRenteAccordeeAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ENVOYER_ANNONCE, REEnvoyerAnnoncesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_EXECUTER_PAIEMENT_MENSUEL,
                REExecuterPaiementMensuelAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_FACTURE_A_RESTITUER, REFactureARestituerAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_ATTESTATION_FISCALE,
                REGenererAttestationFiscaleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_ATTESTATION_FISCALE_UNIQUE, PRDefaultAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_DEMANDE_COMPENSATION,
                REGenererDemandeCompensationAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_LISTES_VERIFICATION, PRDefaultAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_RECAPITULATION_RENTES,
                REGenererRecapitulationRentesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_RECAPITULATION_RENTES_ARC8D,
                REGenererRecapitulationRentesARC8DAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_RENTE_VEUVE_PERDURE,
                REGenererRenteVeuvePerdureAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GENERER_TRANSFERT_DOSSIER,
                REGenererTransfertDossierAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_GLOBAZ_ADMIN, REGlobazAdminAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_HISTORIQUE_RENTES, REHistoriqueRentesAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_HISTORIQUE_RENTES_CALCUL_ACOR,
                REHistoriqueRentesCalculAcorAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_IMPRIMER_DECISION, REImprimerDecisionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_INSCRIPTION_CI, REInscriptionCIAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_INTERET_MORATOIRE, REInteretMoratoireAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_JADE_WIDGET, FWJadeWidgetServletAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_LOTS, RELotAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ORDRES_VERSEMENTS, REOrdresVersementsAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_ORDRES_VERSEMENTS_AJAX, REOrdresVersementsAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PREPARER_DECISION, REDecisionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PREPARER_DECISION_AVEC_AJOURNEMENT,
                REPreparerDecisionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PREPARER_INTERETS_MORATOIRES,
                REPreparationInteretsMoratoiresAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE,
                REPrestationsDuesJointDemandeRenteAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PRESTATIONS_JOINT_DEMANDE_RENTE, PRDefaultAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PREVALIDER_DECISION, REDecisionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_PROCESS_VALIDER_DECISIONS, REProcessDecisionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RASSEMBLEMENT_CI, RERassemblementCIAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RECAPITULATIF_DEMANDE_RENTE,
                RERecapitulatifDemandeRenteAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RECAP_CHARGER, REChargerRecapMensuelleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RECAP_DETAIL, REDetailRecapMensuelleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RECAP_VISU, REVisuRecapMensuelleAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE,
                RERenteAccordeeJointDemandeRenteAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RENTE_LIEE_JOINT_RENTE_ACCORDEE,
                RERenteLieeJointRenteAccordeeAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RENTE_VERSEE_A_TORT, RERenteVerseeATortAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RENTE_VERSEE_A_TORT_AJAX, RERenteVerseeATortAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_REPRISE_DU_DROIT, RERepriseDuDroitAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_RETENUES_SUR_PMT, RERetenuesPaiementAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SAISIE_DEMANDE_RENTE, RESaisieDemandeRenteAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI,
                RESaisieManuelleInscriptionCIAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SAISIE_MANUELLE_RABCPD, RESaisieManuelleRABCPDAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SAISIR_ECHEANCE, RESaisirEcheanceAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SITUATION_FAMILIALE, RESituationFamilialeAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SOLDE_POUR_RESTITUTION, RESoldePourRestitutionAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_TAUX, PRDefaultAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_TERMINER_DEMANDERENTEPREVBIL,
                RETerminerDemandeRentePrevBilAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_VALIDER_GROUPE_DECISIONS,
                REValiderGroupeDecisionsAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_VALIDER_LOT, REValiderLotAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_MODIFICATION_NSS_EX_CONJOINT,
                REModificationNSSExConjointAction.class);
        REMainServletAction.ACTIONS.put(IREActions.ACTION_SAISIE_COMMUNICATION_MUTATION_OAI,
                RECommunicationMutationOaiAction.class);
    }

    /**
     * Retourne la classe qui gère cette action
     * 
     * @param action
     *            l'action
     * @return la classe qui gère cette action
     */
    public static Class<? extends FWDefaultServletAction> getActionClass(final FWAction action) {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();

        return REMainServletAction.ACTIONS.get(key);
    }
}