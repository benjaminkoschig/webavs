/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.stack;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.ij.application.IJApplication;
import globaz.ij.servlet.IIJActions;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_LOTS + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_LOTS + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_REQUERANT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_SAISIE_PRONONCE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_SITUATION_PROFESSIONNELLE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_PETITE_IJ_P + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_ENVOYER_CI + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_BASE_INDEMNISATION + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_BASE_INDEMNISATION + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_CALCUL_DECOMPTE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_CALCUL_IJ + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_COTISATIONS + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_IJ_CALCULEES + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_RECAPITULATIF_PRONONCE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_REPARTITION_PAIEMENTS + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_PETITE_IJ_JOINT_REVENU + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE + ".actionAjouterDansLot");
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_GENERER_DECOMPTES + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_GENERER_COMPENSATIONS + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_IJ_CALCULEE_JOINT_GRANDE_PETITE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_FORMULAIRE_INDEMNISATION + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_FORMULAIRE_INDEMNISATION + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_GENERER_FORMULAIRE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_ANNONCE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_SAISIR_ECHEANCE + "." + "saisirEcheance");
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_SAISIR_NO_DECISION + "." + "saisirNoDecision");
        ACTIONS_A_EMPILER.add(IIJActions.ACTION_TERMINER_PRONONCE + "." + "afficherDateFin");

    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJEvaluable.
     * 
     * @param stack
     */
    public IJEvaluable(FWUrlsStack stack) {
        super(stack, IJApplication.DEFAULT_APPLICATION_IJ);

        addValidActions(new String[] { "afficherDateFin", "actionRecapitulatif", "saisirEcheance", "saisirNoDecision",
                "afficherDateFin" });

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationAction(globaz.framework.controller.FWAction)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {

        if ((ACTIONS_A_EMPILER.contains(action.toString()) || action.toString().endsWith("afficher")
                || action.toString().endsWith("chercher") || action.toString().endsWith("saisirEcheance"))
                // supprimer de la pile toutes les useraction afficher des
                // caPage
                && (!action.toString().endsWith("cotisation.afficher"))
                && (!action.toString().endsWith("repartitionJointPrestation.afficher"))
                && (!action.toString().endsWith("prestationJointLotPrononce.afficher"))
                && (!action.toString().endsWith("situationProfessionnelle.afficher"))
                && (!action.toString().endsWith("mesureJointAgentExecution.afficher"))) {
            return false;
        } else {
            return true;
        }
    }
}
