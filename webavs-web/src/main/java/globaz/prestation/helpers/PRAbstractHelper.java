/*
 * Créé le 21 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.helpers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class PRAbstractHelper extends FWHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Class[] PARAMS = new Class[] { FWViewBeanInterface.class, FWAction.class, BSession.class };

    /**
     */
    public static final String SET_SESSION = "setSession";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * recherche une méthode PUBLIQUE de cette classe qui porte le nom de la partie action de l'instance 'action' et
     * prend les mêmes paramètres que cette méthode sauf pour BSession a la place de BISession et l'invoke.
     * 
     * <p>
     * le tout est fait dans un bloc try...catch, il est donc possible de lancer une exception dans ces méthodes. Cette
     * méthode retourne ensuite le viewBean retourné par ladite méthode ou celui transis en argument si la méthode n'a
     * pas de return value.
     * </p>
     * 
     * <p>
     * si la partie action contient la chaine SET_ACTION, la session est simplement settee dans le viewBean et celui-ci
     * est retourné.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ClassCastException
     *             si session n'est pas une instance de BSession
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    protected FWViewBeanInterface deleguerExecute(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws ClassCastException {
        if (SET_SESSION.equals(action.getActionPart())) {
            viewBean.setISession(session);

            return viewBean;
        } else {
            try {
                Method method = getClass().getMethod(action.getActionPart(), PARAMS);
                Object retValue = method.invoke(this, new Object[] { viewBean, action, (BSession) session });

                if ((retValue != null) && (retValue instanceof FWViewBeanInterface)) {
                    return (FWViewBeanInterface) retValue;
                }
            } catch (InvocationTargetException ie) {
                ie.printStackTrace();
                if (ie.getTargetException() != null) {
                    ((BSession) session).addError(ie.getTargetException().getMessage());
                } else {
                    JadeLogger.error(ie, ie.getMessage());
                    ((BSession) session).addError(ie.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                JadeLogger.error(e, e.getMessage());
                ((BSession) session).addError(e.getMessage());
            }

            return viewBean;
        }
    }
}
