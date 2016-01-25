/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.stack;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.servlet.IRFActions;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import java.util.HashSet;

/**
 * 
 * @author jje
 */
public class RFEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {
        RFEvaluable.ACTIONS_A_EMPILER.add(FWServlet.BACK);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_DOSSIER_JOINT_TIERS + "." + FWAction.ACTION_CHERCHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_DOSSIER_JOINT_TIERS + "." + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS + "."
                + FWAction.ACTION_CHERCHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS + "."
                + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_DEMANDE + "." + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE + "."
                + FWAction.ACTION_CHERCHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE + "."
                + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_ASSURE_CHOIX_TYPE_DE_SOIN + "."
                + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_CHOIX_GENRE + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE + "."
                + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_ATTESTATION_JOINT_TIERS + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_PARAMETRER_SOINS + "." + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_SOLDE_CHARGE + "." + FWAction.ACTION_CHERCHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_SOLDE_CHARGE + "." + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_AUGMENTATION + "." + FWAction.ACTION_CHERCHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_QD_AUGMENTATION + "." + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION + "."
                + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_CONVENTION + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_CONVENTION + "." + FWAction.ACTION_CHERCHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION + "."
                + FWAction.ACTION_AFFICHER);

        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_GENERER_DOCUMENT + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_DOCUMENTS + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_VALIDER_DECISIONS + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_PREPARER_DECISIONS + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_PRESTATION + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_PRESTATION_ACCORDEE + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_LOTS + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_SITUATION_FAMILIALE + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_ATTESTATION + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_IMPUTER_SUR_QD + "." + FWAction.ACTION_AFFICHER);
        RFEvaluable.ACTIONS_A_EMPILER.add(IRFActions.ACTION_DECISION_JOINT_TIERS + "." + FWAction.ACTION_AFFICHER);

    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWUrlsStack stackRefCopy;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFEvaluable
     * 
     * @param stack
     */
    public RFEvaluable(FWUrlsStack stack) {
        super(stack, RFApplication.DEFAULT_APPLICATION_CYGNUS);
        addValidActions(new String[] { "afficher" });
        stackRefCopy = stack;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param aSFion
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationASFion(globaz.framework.controller.FWASFion)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {
        if (stackRefCopy.size() <= 1) {
            return false;
        } else {

            if ((RFEvaluable.ACTIONS_A_EMPILER.contains(action.toString()) || action.toString().endsWith("chercher") || action
                    .toString().endsWith("preparation.afficher"))
                    // supprimer de la pile toutes les useraction suivantes
                    && (!action.toString().endsWith("reAfficher"))
                    && (!action.toString().endsWith(IRFActions.ACTION_CONTRIBUTIONS_ASSISTANCE_AI + ".afficher"))) {
                return false;
            } else {
                return true;
            }
        }
    }
}
