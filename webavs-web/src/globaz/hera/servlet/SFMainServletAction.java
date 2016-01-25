/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.servlet;

import globaz.framework.controller.FWAction;
import globaz.jade.log.JadeLogger;
import java.util.HashMap;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * This class contains the mappings between the action strings and their class handlers.
 * </p>
 * 
 * @author vre
 */
public class SFMainServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashMap ACTIONS = new HashMap();

    static {
        ACTIONS.put(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT, SFApercuRelationFamilialeAction.class);
        ACTIONS.put(ISFActions.ACTION_APERCU_RELATION_CONJOINT, SFApercuRelationConjointAction.class);
        ACTIONS.put(ISFActions.ACTION_PERIODE, SFPeriodeAction.class);
        ACTIONS.put(ISFActions.ACTION_ENFANTS, SFApercuEnfantsAction.class);
        ACTIONS.put(ISFActions.ACTION_VUE_GLOBALE, SFVueGlobaleAction.class);

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
    public static Class getActionClass(FWAction action) throws Exception {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();
        Class c = (Class) ACTIONS.get(key);

        if (c == null) {
            JadeLogger.warn(SFMainServletAction.class, "HERA : No Class found to match action : " + key);
        }

        return c;
    }
}
