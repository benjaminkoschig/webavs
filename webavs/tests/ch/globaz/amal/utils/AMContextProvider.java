/**
 * 
 */
package ch.globaz.amal.utils;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import ch.globaz.amal.web.application.AMApplication;

/**
 * Classe permettant d'initialiser session et contexte
 * 
 * @author DHI
 * 
 */
public class AMContextProvider {

    // Contexte
    private static JadeContext context = null;

    // Session
    private static BSession session = null;

    /**
     * Initialise et retourne un contexte
     * 
     * @return contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte n'a pas pu être initialisé
     */
    public static JadeContext getContext() throws Exception {
        if (AMContextProvider.context == null) {
            AMContextProvider.context = AMContextProvider.initContext(AMContextProvider.getSession()).getContext();
        }
        return AMContextProvider.context;
    }

    /**
     * Initialise et retourne une session
     * 
     * @return session initialisée
     * @throws Exception
     *             Exception levée si la session n'a pas pu être initialisée
     */
    public static BSession getSession() throws Exception {
        if (AMContextProvider.session == null) {
            AMContextProvider.session = (BSession) GlobazSystem.getApplication(AMApplication.DEFAULT_APPLICATION_AMAL)
                    .newSession("globazf", "ssiiadm");
        }
        return AMContextProvider.session;
    }

    /**
     * Initialise un contexte
     * 
     * @param session
     *            Session
     * @return le contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte n'a pas pu être initialisé
     */
    private static JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(AMApplication.DEFAULT_APPLICATION_AMAL);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

}
