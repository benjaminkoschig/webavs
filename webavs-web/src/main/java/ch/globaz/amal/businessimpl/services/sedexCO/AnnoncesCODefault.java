package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.jaxb.JAXBServices;
import java.util.Properties;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import ch.globaz.amal.web.application.AMApplication;

public class AnnoncesCODefault {
    protected String passSedex = "";
    protected String userSedex = "";
    protected JadeContext context;
    protected JAXBServices jaxbs;
    protected BSession session;
    protected Unmarshaller unmarshaller;
    protected Marshaller marshaller;

    /**
     * Retourne un contexte. Si nécessaire il est initialisé
     * 
     * @return le contexte
     * 
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    protected JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(getSession()).getContext();
        }
        return context;
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
            session = (BSession) GlobazSystem.getApplication(AMApplication.DEFAULT_APPLICATION_AMAL).newSession(
                    userSedex, passSedex);
        }

        return session;
    }

    protected void getUserAndPassSedex(Properties properties) {

        // String encryptedUser = properties.getProperty("userSedex");
        // if (encryptedUser == null) {
        // JadeLogger.error(this, "Réception message RP AMAL: user sedex non renseigné. ");
        // throw new IllegalStateException("Réception message RP AMAL: user sedex non renseigné. ");
        // }
        // userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        // String encryptedPass = properties.getProperty("passSedex");
        // if (encryptedPass == null) {
        // JadeLogger.error(this, "Réception message RP AMAL: pass sedex non renseigné. ");
        // throw new IllegalStateException("Réception message RP AMAL: pass sedex non renseigné. ");
        // }
        // passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        userSedex = properties.getProperty("userSedex");
        passSedex = properties.getProperty("passSedex");
    }

    public String getPassSedex() {
        return passSedex;
    }

    public void setPassSedex(String passSedex) {
        this.passSedex = passSedex;
    }

    public String getUserSedex() {
        return userSedex;
    }

    public void setUserSedex(String userSedex) {
        this.userSedex = userSedex;
    }

}
