package globaz.leo.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.jade.log.JadeLogger;
import java.util.Hashtable;

/**
 * @author ald
 * @since Créé le 18 mars 05
 */
public class LEMainServletAction {
    public static String CLASS_EDITER_FORMULE = "editerFormule";
    private static Hashtable hActionsTable = new Hashtable();

    static {
        hActionsTable.put("leo.parametrage.formule", FWDefaultServletAction.class);
        hActionsTable.put("leo.envoi.envoi", LEEnvoiServletAction.class);
        hActionsTable.put("leo.envoi.editerFormule", LEEnvoiServletAction.class);
        hActionsTable.put("leo.process.etapesSuivantes", LEProcessServletAction.class);
        hActionsTable.put("leo.process.listeFormulesEnAttente", LEProcessServletAction.class);
        hActionsTable.put("leo.parametrage.rappel", LEFormuleServletAction.class);
        hActionsTable.put("leo.parametrage.formule", LEFormuleServletAction.class);
        hActionsTable.put("leo.parametrage.selectFormule", FWDefaultServletAction.class);
    }

    public static Class getActionClass(FWAction action) throws Exception {
        //
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();

        Class c = (Class) hActionsTable.get(key);
        if (c == null) {
            JadeLogger.error(LEMainServletAction.class, "LEO : No Class found to match action : " + key);
        }
        return c;
    }
}
