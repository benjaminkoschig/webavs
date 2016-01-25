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
     * Permet de récupérer l'action instanciée en fonction de son application et de son package
     * 
     * @param servlet
     *            Servlet attachée au flux d'exécution
     * @param action
     *            L'action définissant l'action à instancier
     * @return L'action instanciée
     * 
     * @throws SecurityException
     *             Levée si l'accès au constructor est formellement interdit par des règles de sécurités
     * @throws NoSuchMethodException
     *             Levée si le constructor recherché n'existe pas
     * @throws IllegalArgumentException
     *             Levée dans le cas où les arguments passés en paramètres ne sont pas correctes
     * @throws InstantiationException
     *             Levée si la class que l'on tente d'instancier est abstraite
     * @throws IllegalAccessException
     *             Levée si le constructor est inaccessible (private ou protected dans un autre package)
     * @throws InvocationTargetException
     *             Levée si lors de l'exécution du constructeur, une exception est levée
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
