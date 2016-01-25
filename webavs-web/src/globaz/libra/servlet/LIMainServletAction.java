/*
 * Créé le 22 juillet 2009
 */

package globaz.libra.servlet;

import globaz.framework.controller.FWAction;
import globaz.jade.log.JadeLogger;
import java.util.HashMap;

/**
 * <h1>Description</h1>
 * 
 * @author hpe
 */
public class LIMainServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashMap ACTIONS = new HashMap();

    static {

        ACTIONS.put(ILIActions.ACTION_DOSSIERS, LIDossiersAction.class);
        ACTIONS.put(ILIActions.ACTION_JOURNALISATIONS, LIJournalisationsAction.class);
        ACTIONS.put(ILIActions.ACTION_DOMAINES, LIDomainesAction.class);
        ACTIONS.put(ILIActions.ACTION_GROUPES, LIGroupesAction.class);
        ACTIONS.put(ILIActions.ACTION_UTILISATEURS, LIUtilisateursAction.class);
        ACTIONS.put(ILIActions.ACTION_FORMULES, LIFormulesAction.class);

        ACTIONS.put(ILIActions.ACTION_LISTES_DE, LIListesAction.class);

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
            key = action.getApplicationPart() + "." + action.getPackagePart();
            c = (Class) ACTIONS.get(key);

            if (c == null) {
                JadeLogger.warn(LIMainServletAction.class, "LIBRA : No Class found to match action : " + key);
            }
        }

        return c;
    }
}
