package globaz.al.process.rafam;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.log.JadeLogger;
import ch.globaz.al.web.application.ALApplication;

public abstract class AbstractRafamSedex implements Runnable {

    private JadeContext context;
    private String passSedex;
    private BSession session;
    private String userSedex;

    /**
     * Retourne un contexte. Si nécessaire il est initialisé
     * 
     * @return le contexte
     * 
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    public JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(getSession()).getContext();
        }
        return context;
    }

    public String getPassSedex() {
        return passSedex;
    }

    /**
     * Retourne une session. Si nécessaire elle est initialisée
     * 
     * @return la session
     * 
     * @throws Exception
     *             Exception levée si la session ne peut être initialisée
     */
    public BSession getSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(ALApplication.APPLICATION_NAME).newSession(userSedex,
                    passSedex);
        }

        return session;
    }

    public String getUserSedex() {
        return userSedex;
    }

    /**
     * Initialise un contexte
     * 
     * @param session
     *            session
     * @return le contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    protected JadeThreadContext initContext(BSession session) throws Exception {
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

    public void setPassSedex(String passSedex) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        if (passSedex == null) {
            JadeLogger.error(this, "Envoi d'un message RAFam: user sedex non renseigné. ");
            throw new IllegalStateException("Envoi d'un message RAFam: user sedex non renseigné. ");
        }
        this.passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(passSedex);
    }

    public void setUserSedex(String userSedex) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        if (userSedex == null) {
            JadeLogger.error(this, "Envoi d'un message RAFam: pass sedex non renseigné. ");
            throw new IllegalStateException("Envoi d'un message RAFam: pass sedex non renseigné. ");
        }
        this.userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(userSedex);
    }
}
