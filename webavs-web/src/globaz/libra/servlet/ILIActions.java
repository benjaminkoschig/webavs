/*
 * Créé le 22 juillet 2009
 */

package globaz.libra.servlet;

/**
 * @author hpe
 * 
 *         Définition des constantes pour les actions
 */

public interface ILIActions {

    public static final String ACTION_CHAMPS_RC = "libra.formules.champs";
    public static final String ACTION_DOMAINES = "libra.domaines";
    public static final String ACTION_DOMAINES_DE = "libra.domaines.domaines";
    public static final String ACTION_DOMAINES_RC = "libra.domaines.domaines";
    // Actions générales pour mapping de classes
    public static final String ACTION_DOSSIERS = "libra.dossiers";
    public static final String ACTION_DOSSIERS_DE = "libra.dossiers.dossiers";

    // Actions spécifiques pour affichage d'écrans
    public static final String ACTION_DOSSIERS_RC = "libra.dossiers.dossiersJointTiers";
    public static final String ACTION_ECHEANCES_DE = "libra.journalisations.echeancesDetail";

    public static final String ACTION_ECHEANCES_DOC = "libra.journalisations.documents";
    public static final String ACTION_ECHEANCES_RC = "libra.journalisations.echeances";
    public static final String ACTION_FORMULES = "libra.formules";

    public static final String ACTION_FORMULES_DE = "libra.formules.formulesDetail";
    public static final String ACTION_FORMULES_RC = "libra.formules.formules";

    public static final String ACTION_FORMULES_RC_CS = "libra.formules.formuleCS";
    public static final String ACTION_GROUPES = "libra.groupes";

    public static final String ACTION_GROUPES_DE = "libra.groupes.groupes";
    public static final String ACTION_GROUPES_RC = "libra.groupes.groupes";
    public static final String ACTION_JOURNALISATIONS = "libra.journalisations";

    public static final String ACTION_JOURNALISATIONS_DE = "libra.journalisations.journalisationsDetail";
    public static final String ACTION_JOURNALISATIONS_RC = "libra.journalisations.journalisations";

    public static final String ACTION_LISTES_DE = "libra.listes.listes";
    public static final String ACTION_RAPPEL_DE = "libra.formules.rappel";

    public static final String ACTION_STATISTIQUES_DE = "libra.statistiques.statistiques";
    public static final String ACTION_UTILISATEURS = "libra.utilisateurs";

    public static final String ACTION_UTILISATEURS_DE = "libra.utilisateurs.utilisateurs";

    public static final String ACTION_UTILISATEURS_FX_RC = "libra.utilisateurs.utilisateursFX";

    public static final String ACTION_UTILISATEURS_RC = "libra.utilisateurs.utilisateurs";

}