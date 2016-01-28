package ch.globaz.al.utils;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import ch.globaz.al.web.application.ALApplication;

/**
 * Classe permettant d'initialiser session et contexte
 * 
 * @author jts
 * 
 */
public abstract class ContextProvider {
    /**
     * contexte
     */
    private static JadeContext context = null;
    /**
     * Session
     */
    private static BSession session = null;

    /**
     * Initialise et retourne un contexte
     * 
     * @return contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte n'a pas pu être initialisé
     */
    public static JadeContext getContext() throws Exception {
        if (ContextProvider.context == null) {
            ContextProvider.context = ContextProvider.initContext(ContextProvider.getSession()).getContext();
        }
        return ContextProvider.context;
    }

    /**
     * Initialise et retourne une session
     * 
     * @return session initialisée
     * @throws Exception
     *             Exception levée si la session n'a pas pu être initialisée
     */
    public static BSession getSession() throws Exception {
        if (ContextProvider.session == null) {
            ContextProvider.session = (BSession) GlobazSystem.getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF)
                    .newSession("alfagest", "glob4az");
        }
        return ContextProvider.session;
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
        ctxtImpl.setApplicationId(ALApplication.APPLICATION_NAME);
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
