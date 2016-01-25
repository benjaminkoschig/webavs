/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.servlets.widget.FWJadeWidgetServletAction;
import java.util.HashMap;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * This class contains the mappings between the action strings and their class handlers.
 * </p>
 * 
 * @author jje
 */
public class RFMainServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashMap<String, Class<?>> ACTIONS = new HashMap<String, Class<?>>();

    static {
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_DOSSIER_JOINT_TIERS, RFDossierJointTiersAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_STATISTIQUES_PAR_MONTANTS_SASH,
                RFStatistiquesParMontantsSashAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_STATISTIQUES_PAR_NB_CAS, RFStatistiquesParNbCasAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS,
                RFDemandeJointDossierJointTiersAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_DEMANDE, RFSaisieDemandeAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE,
                RFSaisieDemandePiedDePageAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_IMPUTER_SUR_QD, RFImputerSurQdAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE,
                RFQdJointDossierJointTiersJointDemandeAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD, RFSaisieQdAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_CHOIX_GENRE, RFSaisieQdChoixGenreAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE, RFSaisieQdPiedDePageAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_AUGMENTATION, RFQdSaisieAugmentationAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_SOLDE_CHARGE, RFQdSaisieSoldeChargeAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU,
                RFQdSaisieSoldeExcedentDeRevenuAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE,
                RFQdSaisiePeriodeValiditeQdPrincipaleAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_QD_ASSURE_CHOIX_TYPE_DE_SOIN,
                RFSaisieQdAssureChoixTypeDeSoinAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_MAJ_LIMITE_ANNUELLE_QD_ASSURE,
                RFSaisieQdPiedDePageAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_MAJ_LIMITE_ANNUELLE_QD_DROIT_PC,
                RFSaisieQdPiedDePageAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_AVANCE, RFAvanceAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_CONVENTION, RFConventionAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION,
                RFRechercheMontantsConventionAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_MONTANTS_CONVENTION,
                RFSaisieMontantsConventionAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION,
                RFSaisieSoinFournisseurConventionAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_DECISION_JOINT_TIERS, RFDecisionJointTiersAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_ATTESTATION, RFAttestationAction.class);
        RFMainServletAction.ACTIONS
                .put(IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE, RFAttestationPiedDePageAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_ATTESTATION_JOINT_TIERS,
                RFAttestationJointDossierJointTiersAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_PARAMETRER_SOINS, RFTypeDeSoinAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_PARAMETRAGE_SOINS_RECHERCHE_PERIODE,
                RFParametrageTypeSoinsRecherchePeriodeAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_PARAMETRAGE_GRANDE_QD, RFParametrageGrandeQDAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_DOCUMENTS, RFDocumentsAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SITUATION_FAMILIALE, RFSituationFamilialeAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS,
                RFRechercheMotifsDeRefusAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_SAISIE_MOTIFS_DE_REFUS, RFSaisieMotifsDeRefusAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_PREPARER_DECISIONS, RFPreparerDecisionAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_VALIDER_DECISIONS, RFValiderDecisionAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_LOTS, RFLotAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_ORDRES_VERSEMENTS, RFOrdresVersementsAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_PRESTATION_ACCORDEE, RFPrestationsAccordeesAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_PRESTATION, RFPrestationAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_JADE_WIDGET, FWJadeWidgetServletAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_COMPTABILISER, RFComptabiliserAction.class);

        RFMainServletAction.ACTIONS
                .put(IRFActions.ACTION_ADAPTATIONS_JOURNALIERE, RFAdaptationsJournaliereAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_IMPORTER_FINANCEMENT_SOIN,
                RFImporterFinancementSoinAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_LISTE_RECAPITULATIVE_PAIEMENTS,
                RFListeRecapitulativePaiementsAction.class);

        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_CONTRIBUTIONS_ASSISTANCE_AI,
                RFContributionsAssistanceAIAction.class);
        RFMainServletAction.ACTIONS.put(IRFActions.ACTION_LISTE_CONTRIBUTION_ASSISTANCE_AI,
                RFListeContributionsAssistanceAIAction.class);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * returns the class of the handler for this action string.
     * 
     * @param action
     *            the action string
     * 
     * @return la valeur courante de l'attribut action class
     */
    public static Class<?> getActionClass(FWAction action) {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();

        return RFMainServletAction.ACTIONS.get(key);
    }
}
