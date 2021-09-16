package globaz.ij.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.IFWActionHandler;
import globaz.framework.servlets.widget.FWJadeWidgetServletAction;
import globaz.prestation.servlet.PRDefaultAction;
import java.util.HashMap;

/**
 * <p>
 * This class contains the mappings between the action strings and their class handlers.
 * </p>
 * 
 * @author VRE
 */
public class IJMainServletAction {

    private static final HashMap<String, Class<? extends IFWActionHandler>> ACTIONS = new HashMap<String, Class<? extends IFWActionHandler>>();

    static {
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_LOTS, PRDefaultAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_PRONONCE_JOINT_DEMANDE, IJPrononceJointDemandeAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_PRONONCE, IJPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_CORRIGER_DEPUIS_PRONONCE,
                IJCorrigerDepuisPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_ANNULER_CORRIGER_DEPUIS_PRONONCE,
                IJAnnulerCorrigerDepuisPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_TERMINER_PRONONCE, IJPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_INFO_COMPL, IJInfoComplAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_REQUERANT, IJRequerantAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_BASE_INDEMNISATION, IJBaseIndemnisationAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA,
                IJBaseIndemnisationAitAaAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE,
                IJPrestationJointLotPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_LOT, IJDefaultProcessAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIE_PRONONCE, IJSaisiePrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIE_PRONONCE_AIT, IJSaisiePrononceAitAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST,
                IJSaisiePrononceAllocAssistanceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION,
                IJMesureJointAgentExecutionAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_PETITE_IJ_JOINT_REVENU, IJPetiteIJJointRevenuAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_FPI_JOINT_REVENU, IJFpiJointRevenuAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SITUATION_PROFESSIONNELLE,
                IJSituationProfessionnelleAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_CALCUL_IJ, IJCalculACORIJAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_CALCUL_DECOMPTE, IJCalculACORDecompteAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_CALCUL_DECOMPTE_AIT_AA, IJCalculDecompteAitAaAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_IJ_CALCULEES, IJIJCalculeeJointIndemniteAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_COMPENSATIONS, IJGenererCompensationsAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_ENVOYER_CI, IJEnvoyerCIAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_INSCRIRE_CI, IJEnvoyerCIAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_COMPENSATIONS_LOT, IJFactureJointCompensationAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_FACTURES_LOT, IJFactureACompenserAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_DECOMPTES, IJGenererDecomptesAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_DECISION, IJGenererDecisionAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_ENVOYER_ANNONCES, IJEnvoyerAnnoncesAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_REPARTITION_PAIEMENTS,
                IJRepartitionJointPrestationAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_COTISATIONS, IJCotisationAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_ANNONCE, IJAnnonceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_ANNONCE3EMEREVISION, IJAnnonceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_ANNONCE4EMEREVISION, IJAnnonceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_FORMULAIRE_INDEMNISATION,
                IJFormulaireIndemnisationAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_FORMULAIRE, IJGenererFormulaireAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_FORMULAIRES, IJGenererFormulairesAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_IJ_CALCULEE_JOINT_GRANDE_PETITE, IJCalculeeAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_LISTE_FORMULAIRES_NON_RECUS, IJFormulaireAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_RECAPITULATIF_PRONONCE, IJRecapitulatifPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_LISTE_CONTROLE, IJListeControleAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_LISTE_RECAPITULATION_ANNONCE, IJDefaultProcessAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GENERER_ATTESTATIONS_IJ, IJGenererAttestationsAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIR_ECHEANCE, IJPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIR_TAUX_IS, IJPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIR_NO_DECISION, IJPrononceAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_VALIDER_DECISION, IJValiderDecisionAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_GLOBAZ_ADMIN, IJGlobazAdminAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_DOSSIER_CONTROLE_ABSENCES,
                IJDossierControleAbsencesAjaxAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_PERIODE_CONTROLE_ABSENCES,
                IJPeriodeControleAbsencesAjaxAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_BASE_INDEMNISATION_CONTROLE_ABSENCES,
                IJBaseIndemnisationAjaxAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_PRONONCE_CONTROLE_ABSENCE, IJPrononceAjaxAction.class);
        IJMainServletAction.ACTIONS.put(IIJActions.ACTION_SAISIE_ABSENCE_CONTROLE_ABSENCES,
                IJSaisieAbsenceAjaxAction.class);
        IJMainServletAction.ACTIONS.put("widget.action.jade", FWJadeWidgetServletAction.class);
    }

    /**
     * returns the class of the handler for this action string.
     * 
     * @param action
     *            the action string
     * @return the action class
     */
    public static Class<? extends IFWActionHandler> getActionClass(FWAction action) {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();
        return IJMainServletAction.ACTIONS.get(key);
    }
}