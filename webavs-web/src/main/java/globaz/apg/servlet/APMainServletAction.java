/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.corvus.servlet.IREActions;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.widget.FWJadeWidgetServletAction;
import globaz.prestation.servlet.PRDefaultAction;
import java.util.HashMap;

/**
 * DOCUMENT ME!
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class APMainServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashMap ACTIONS = new HashMap();

    static {
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_DROIT_LAPG, APLAPGAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_SAISIE_CARTE_APG, APDroitAPGPAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_SAISIE_CARTE_AMAT, APDroitMatPAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_RECAPITUALATIF_DROIT_APG, PRDefaultAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_RECAPITUALATIF_DROIT_MAT, PRDefaultAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_PERE_MAT, APPereMatAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ENFANT_MAT, APEnfantMatAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_SITUATION_PROFESSIONNELLE,
                APSituationProfessionnelleAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ENFANT_APG, APEnfantAPGAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_PRESTATIONS, APPrestationAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_REPARTITION_PAIEMENTS, APRepartitionPaiementsAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_CONTROLE_PRESTATIONS_APG, APDefaultProcessAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_LOTS, PRDefaultAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_COMPENSATIONS, APGenererCompensationsAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_LOT, APDefaultProcessAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT,
                APPrestationJointLotTiersDroitAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ANNONCEAPG, APAnnonceAPGAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ATTESTATIONS_FISCALES, APDefaultProcessAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ANNONCEREVISION2005, APAnnonceAPGAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ANNONCEREVIVION1999, APAnnonceAPGAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ANNONCESEDEX, APAnnonceAPGAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ENVOYER_ANNONCE, APEnvoyerAnnoncesAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_STATS_OFAS, APGenererStatsOFASAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_ENVOYER_CI, APDefaultProcessAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_INSCRIRE_CI, APEnvoyerCIAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_COTISATION_JOINT_REPARTITION, APCotisationAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_DECOMPTES, APGenererDecomptesAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_LISTE_RECAPITULATION_ANNONCE, APDefaultProcessAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_COMPENSATIONS_LOT, APFactureJointCompensationAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_FACTURES_LOT, APFactureACompenserAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_CALCUL_ACOR, APCalculACORAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_COMM_DEC_AMAT, APGenererDecisionAMATAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_INFO_COMPL, APInfoComplAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_ATTESTATIONS_APG, APGenererAttestationsAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_LISTE_CONTROLE, APListeControleAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_GENERER_DECISION_REFUS, APGenererDecisionRefusAction.class);
        APMainServletAction.ACTIONS.put(IREActions.ACTION_JADE_WIDGET, FWJadeWidgetServletAction.class);

        APMainServletAction.ACTIONS.put(IAPActions.ACTION_LISTE_PRESTATION_VERSEE, APListePrestationVerseeAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_LISTE_PRESTATION_CIAB, APListePrestationCIABAction.class);
        APMainServletAction.ACTIONS.put(IAPActions.ACTION_LISTE_TAXATIONS, APListeTaxationsDefinitivesAction.class);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut action class
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut action class
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static Class getActionClass(FWAction action) throws Exception {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();
        Class c = (Class) APMainServletAction.ACTIONS.get(key);

        // if (c == null) {
        // JadeLogger.warn(APMainServletAction.class,
        // "APG : No Class found to match action : " + key);
        // }

        return c;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APMainServletAction.
     */
    public APMainServletAction() {
    }
}
