/*
 * Créé le 6 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.jade.log.JadeLogger;
import java.util.Hashtable;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUMainServletAction {
    private static Hashtable hActionsTable = new Hashtable();

    static {
        hActionsTable.put("lupus.journalisation.journal", LUJournalServletAction.class);
        hActionsTable.put("lupus.journalisation.icone", FWDefaultServletAction.class);
    }

    public static Class getActionClass(FWAction action) throws Exception {
        //
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();

        Class c = (Class) hActionsTable.get(key);
        if (c == null) {
            JadeLogger.error(LUMainServletAction.class, "LUPUS : No Class found to match action : " + key);
        }
        return c;
    }
}
