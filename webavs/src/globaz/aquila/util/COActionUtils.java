package globaz.aquila.util;

import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWActionFactory;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Utilitaires pour les actions.
 * 
 * @author Pascal Lovy, 01-dec-2004
 */
public final class COActionUtils {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cré et retourne une action en fonction de la classe du viewBean et du type d'action demandée.
     * 
     * @param viewBean
     *            Le viewBean pour lequel l'action doit être créé
     * @param prefix
     *            Le prefix de tous les viewBean
     * @param actionString
     *            Le type d'action comme défini dans FWAction
     * @return L'objet FWAction
     */
    public static FWAction createAction(FWViewBeanInterface viewBean, String prefix, String actionString) {
        // exemple className=^globaz.aquila.db.tutorial.COPersonneViewBean$
        String className = "^" + viewBean.getClass().getName() + "$";

        // exemple className=aquila.db.tutorial.COPersonneViewBean$
        className = JadeStringUtil.change(className, "^" + FWActionFactory.BASE + ".", "^");

        // exemple className=aquila.tutorial.COPersonneViewBean$
        className = JadeStringUtil.change(className, "." + FWViewBeanActionFactory.PATH_DB + ".", ".");

        // exemple className=aquila.tutorial.PersonneViewBean$
        className = JadeStringUtil.change(className, "." + prefix, ".");

        // exemple className=aquila.tutorial.personneViewBean$
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(0, lastDotIndex + 1)
                + className.substring(lastDotIndex + 1, lastDotIndex + 2).toLowerCase()
                + className.substring(lastDotIndex + 2);

        // exemple className=aquila.tutorial.personne
        className = JadeStringUtil.change(className, FWViewBeanActionFactory.SUFFIX_VIEWBEAN + "$", "^");

        // Si quelque chose ne s'est pas passé comme prévu, on nettoie
        // quand-même le nom de l'action
        className = JadeStringUtil.removeChar(className, '^');
        className = JadeStringUtil.removeChar(className, '$');
        className = JadeStringUtil.change(className, ".access.", ".");

        return FWAction.newInstance(className + "." + actionString);
    }

    /**
     * Obtient le message pour l'exception de transition donnée.
     * 
     * @param session
     * @param transitionException
     * @return le message de l'exception de transition
     */
    public static String getMessage(BSession session, COTransitionException transitionException) {
        String message = "ERROR - COTransitionException";

        if (transitionException != null) {
            if (!JadeStringUtil.isBlank(transitionException.getMessageId())) {
                message = session.getLabel(transitionException.getMessageId());
            } else {
                message = transitionException.toString();
            }
        }

        if (transitionException.getArgs() != null) {
            return COStringUtils.formatMessage(message, transitionException.getArgs());
        }

        return message;
    }

    /**
     * Obtient le message pour l'exception de transition donnée.
     * 
     * @param session
     * @param messageId
     *            transitionException
     * @return le message pour l'exception de transition donnée.
     */
    public static String getMessage(BSession session, String messageId) {
        return COActionUtils.getMessage(session, messageId, null);
    }

    /**
     * Obtient le message pour l'exception de transition donnée.
     * 
     * @param session
     * @param messageId
     *            transitionException
     * @param args
     * @return le message pour l'exception de transition donnée.
     */
    public static String getMessage(BSession session, String messageId, Object[] args) {
        String message = session.getLabel(messageId);

        if (args != null) {
            return COStringUtils.formatMessage(message, args);
        }

        return message;
    }
}
