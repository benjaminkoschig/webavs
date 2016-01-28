/*
 * Créé le 28 juin 07
 */

package globaz.corvus.stack;

import globaz.corvus.application.REApplication;
import globaz.corvus.servlet.IREActions;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import java.util.HashSet;

/**
 * @author HPE
 * 
 */

public class REEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {

        REEvaluable.ACTIONS_A_EMPILER.add(FWServlet.BACK);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + "."
                + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RECAPITULATIF_DEMANDE_RENTE + "."
                + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RENTE_LIEE_JOINT_RENTE_ACCORDEE + "."
                + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RENTE_LIEE_JOINT_RENTE_ACCORDEE + "."
                + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + "."
                + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + "."
                + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_BASES_DE_CALCUL + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_BASES_DE_CALCUL + "." + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT + "." + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_INSCRIPTION_CI + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_SAISIE_DEMANDE_RENTE + "." + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_CALCUL_DEMANDE_RENTE + "." + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE + "."
                + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE + "."
                + FWAction.ACTION_AFFICHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RETENUES_SUR_PMT + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_ORDRES_VERSEMENTS + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_INSCRIPTION_CI + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RASSEMBLEMENT_CI + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_CREANCIER + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_FACTURE_A_RESTITUER + "." + FWAction.ACTION_CHERCHER);
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_SAISIE_DEMANDE_RENTE
                + ".afficherInformationsComplementaires");
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_RECAP_VISU + ".charger");
        REEvaluable.ACTIONS_A_EMPILER
                .add(IREActions.ACTION_GENERER_RECAPITULATION_RENTES_ARC8D + ".afficherChargement");
        REEvaluable.ACTIONS_A_EMPILER.add(IREActions.ACTION_CREANCIER + ".actionRepartirLesCreances");
        // corvus.ci.saisieManuelleInscriptionCI.afficher
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REEvaluable.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public REEvaluable(FWUrlsStack stack) {
        super(stack, REApplication.DEFAULT_APPLICATION_CORVUS);
        addValidActions(new String[] { "" });
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationAction(globaz.framework.controller.FWAction)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {

        if ((REEvaluable.ACTIONS_A_EMPILER.contains(action.toString()) || action.toString().endsWith("afficher") || action
                .toString().endsWith("chercher"))
                // supprimer de la pile toutes les useraction afficher des
                // caPage
                && (!action.toString().endsWith("historiqueRentesCalculAcor.afficher"))
                && (!action.toString().endsWith("retenuesPaiement.afficher"))
                && (!action.toString().endsWith("ordresVersements.afficher"))
                && (!action.toString().endsWith("creancier.afficher"))
                && (!action.toString().endsWith("taux.afficher"))
                && (!action.toString().endsWith("calculInteretMoratoire.afficher"))
                && (!action.toString().endsWith("saisieManuelleInscriptionCI.afficher"))
                // && (!action.toString().endsWith("inscriptionCI.afficher"))
                && (!action.toString().endsWith("factureARestituer.afficher"))
                && (!action.toString().endsWith("interetMoratoire.afficher"))) {
            return false;
        } else {
            return true;
        }

    }
}
