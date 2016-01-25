package globaz.tucana.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.common.JadeCodingUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

/**
 * @author fgo
 * 
 *         Provider des actions pour l'application Tucana
 */
public abstract class TUActionProvider {
    private static Hashtable actionsTable = new Hashtable();

    static {
        actionsTable.put("tucana.administration", TUActionAdministration.class);
        actionsTable.put("tucana.bouclement", TUActionBouclement.class);
        actionsTable.put("tucana.communs", TUActionCommuns.class);
        actionsTable.put("tucana.journal", TUActionJournal.class);
        actionsTable.put("tucana.transfert", TUActionTransfert.class);
        actionsTable.put("tucana.parametrage", TUActionParametrage.class);
        actionsTable.put("tucana.list", TUActionList.class);
    }

    /**
     * Permet de r�cup�rer l'action instanci�e en fonction de son application et de son package
     * 
     * @param servlet
     *            Servlet attach�e au flux d'ex�cution
     * @param action
     *            L'action d�finissant l'action � instancier
     * @return L'action instanci�e
     * 
     * @throws SecurityException
     *             Lev�e si l'acc�s au constructor est formellement interdit par des r�gles de s�curit�s
     * @throws NoSuchMethodException
     *             Lev�e si le constructor recherch� n'existe pas
     * @throws IllegalArgumentException
     *             Lev�e dans le cas o� les arguments pass�s en param�tres ne sont pas correctes
     * @throws InstantiationException
     *             Lev�e si la class que l'on tente d'instancier est abstraite
     * @throws IllegalAccessException
     *             Lev�e si le constructor est inaccessible (private ou protected dans un autre package)
     * @throws InvocationTargetException
     *             Lev�e si lors de l'ex�cution du constructeur, une exception est lev�e
     */
    /**
	 */
    public static TUActionTucanaDefault getAction(FWServlet servlet, FWAction action) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        TUActionTucanaDefault newAction = null;
        String key = action.getApplicationPart() + "." + action.getPackagePart();
        Class actionClass = (Class) actionsTable.get(key);
        if (actionClass == null) {
            JadeCodingUtil.assertNotAccessible(TUActionProvider.class, "getAction", "No actionclass defined for '"
                    + key + "' !");
        } else {
            Constructor c = actionClass.getConstructor(new Class[] { FWServlet.class });
            newAction = (TUActionTucanaDefault) c.newInstance(new Object[] { servlet });
        }
        return newAction;
    }

}
