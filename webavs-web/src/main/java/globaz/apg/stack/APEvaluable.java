/*
 * Créé le 6 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.stack;

import globaz.apg.application.APApplication;
import globaz.apg.servlet.IAPActions;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {
        ACTIONS_A_EMPILER.add(FWServlet.BACK);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_DROIT_LAPG + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_SAISIE_CARTE_AMAT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_SAISIE_CARTE_APG + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ENFANT_MAT + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ENFANT_APG + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ENFANT_APG + ".actionAfficherEnfantDeListe");
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_PERE_MAT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_SITUATION_PROFESSIONNELLE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_RECAPITUALATIF_DROIT_APG + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_RECAPITUALATIF_DROIT_MAT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + ".actionAjouterDansLot");
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_PRESTATIONS + ".actionPreparerPrestationPourRepartitions");
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_COTISATION_JOINT_REPARTITION + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_LOTS + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_LOTS + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ANNONCEAPG + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_GENERER_COMPENSATIONS + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ANNONCEAPG + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_CONTROLE_PRESTATIONS_APG + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ENVOYER_ANNONCE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add("apg.process.genererAnnonces." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_GENERER_LOT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ENVOYER_CI + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_LISTE_RECAPITULATION_ANNONCE + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_GENERER_DECOMPTES + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_COMPENSATIONS_LOT + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_TEXTE_JOINT_CATALOGUE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_TEXTE_JOINT_CATALOGUE_SAISIE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_REPARTITION_PAIEMENTS + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_GENERER_COMM_DEC_AMAT + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_DROIT_LAPG + "." + "actionRecapitulatif");
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_DROIT_LAPG + "." + "actionEnvoyerMail");
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ANNONCEAPG + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_ANNONCEAPG + ".choisirType");
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_GENERER_DROIT_PAN_MENSUEL + "." + FWAction.ACTION_AFFICHER);
        ACTIONS_A_EMPILER.add(IAPActions.ACTION_PANDEMIE_FIN_DU_DROIT + "." + FWAction.ACTION_AFFICHER);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEvaluable.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public APEvaluable(FWUrlsStack stack) {
        super(stack, APApplication.DEFAULT_APPLICATION_APG);
        addValidActions(new String[] { "actionAfficherEnfantDeListe", "actionAjouterDansLot",
                "actionPreparerPrestationPourRepartitions", "actionRecapitulatif", "choisirType" });
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationAction(globaz.framework.controller.FWAction)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {

        if ((ACTIONS_A_EMPILER.contains(action.toString()) || action.toString().endsWith("afficher") || action
                .toString().endsWith("chercher"))
                // supprimer de la pile toutes les useraction afficher des
                // caPage
                && (!action.toString().endsWith("situationProfessionnelle.afficher"))
                && (!action.toString().endsWith("enfantAPG.afficher"))
                && (!action.toString().endsWith("enfantMat.afficher"))
                && (!action.toString().endsWith("repartitionPaiements.afficher"))
                && (!action.toString().endsWith("cotisationJointRepartition.afficher"))) {
            return false;
        } else {
            return true;
        }

    }
}
