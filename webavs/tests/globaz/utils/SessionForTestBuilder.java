package globaz.utils;

import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import org.mockito.Mockito;

/**
 * Permet de construire une session pour des cas de testes.<br/>
 * Utilisera la base de donn�es d�fini dans JadeJdbDriver.xml et Jade.xml pour se connecter � cette session.
 * 
 * @author PBA
 */
public class SessionForTestBuilder {

    /**
     * Construit et retourne une session utilisateur avec les param�tres d'entr�.<br/>
     * Si l'ID de l'application est vide, ou si l'application correspondante n'a pas �t� trouv�e, l'application du
     * framework est prise par d�faut.<br/>
     * Si le couplet username/password ne permet pas d'ouvrir la session, un essaie sera fait avec globazf/globazf.<br/>
     * Dans le cas o� la session ne serait toujours pas ouverte, la m�thode retourne <code>null</code>
     * 
     * @param idApplication
     *            un ID d'application de WebAVS
     * @param username
     *            un des noms d'utilisateurs inclue dans la base de donn�es qui sera utilis�e (voir JadeJdbcDriver.xml)
     * @param password
     *            le mot de passe correspondant � l'utilisateur en base de donn�es
     * @return la session ouverte, ou <code>null</code> s'il n'a pas �t� possible de l'ouvrir
     */
    public static BSession getSession(String idApplication, String username, String password) {

        BSession session = null;
        BApplication application = null;

        try {
            application = (BApplication) GlobazSystem.getApplication(idApplication);
        } catch (Exception e) {
            try {
                application = (BApplication) GlobazSystem.getApplication(BApplication.PROPERTY_APPLICATIONNAME);
            } catch (Exception e1) {
                return null;
            }
        }

        try {
            session = (BSession) application.newSession();
        } catch (Exception e) {
            return null;
        }

        try {
            session.connect(username, password);
        } catch (Exception ex) {
            try {
                session.connect("globazf", "globazf");
            } catch (Exception e) {
                return null;
            }
        }
        return session;
    }

    /**
     * Retourne une fausse session ne faisant rien lorsqu'on appelle ses m�thodes
     * 
     * @return un mock
     * @see Mockito
     */
    public static BISession getSessionStub() {
        return Mockito.mock(BISession.class);
    }

    public static JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
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
