package ch.globaz.eform.services.sedex;

import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.services.sedex.handlers.GFFormHandler;
import ch.globaz.eform.services.sedex.handlers.GFFormHandlersFactory;
import ch.globaz.eform.web.application.GFApplication;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class GFTraitementMessageServiceImpl {

    private String passSedex;
    private String userSedex;
    private BSession session;
    private JadeContext context;
    private String currentSedexFolder;

    private GFFormHandlersFactory objectFactory;

    /**
     * Pr�paration des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     *
     * @param properties Propri�t� du service Sedex
     */
    @Setup
    public void setUp(Properties properties)
            throws Exception {

        String encryptedUser = properties.getProperty("userSedex");

        if (encryptedUser == null) {
            LOG.error("GFTraitementMessageServiceImpl#setUp - R�ception message RP AMAL: user sedex non renseign�. ");
            throw new IllegalStateException("R�ception message RP AMAL: user sedex non renseign�. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            LOG.error("GFTraitementMessageServiceImpl#setUp - R�ception message RP AMAL: pass sedex non renseign�. ");
            throw new IllegalStateException("R�ception message RP AMAL: pass sedex non renseign�. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFFormHandlersFactory();

        currentSedexFolder = properties.getProperty("currentSedexFolder");

        session = (BSession) GlobazSystem.getApplication(GFApplication.DEFAULT_APPLICATION_EFORM)
                .newSession(userSedex, passSedex);
    }

    @OnReceive
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException {
        String[] defaultUserGestionnaire = {""};
        String[] zipPath = {null};

        //Lecture dans les propri�t�s du gestionnaire par d�faut
        if (!GFProperties.GESTIONNAIRE_USER_DEFAULT.getValue().isEmpty()) {
            try {
                JadeUser user = session.getApplication()._getSecurityManager().getUserForVisa(session, GFProperties.GESTIONNAIRE_USER_DEFAULT.getValue());
                if (Objects.nonNull(user.getVisa())) {
                    defaultUserGestionnaire[0] = user.getVisa();
                }
            } catch (FWSecurityLoginException e) {
                LOG.warn("GFTraitementMessageServiceImpl#importMessages - GFTraitementMessageServiceImpl#importMessages - Erreur � la r�cup�ration du gestionnaire par d�faut :", e);
            } catch (Exception e) {
                LOG.warn("GFTraitementMessageServiceImpl#importMessages - GFTraitementMessageServiceImpl#importMessages - Erreur inconnue � la r�cup�ration du gestionnaire par d�faut :", e);
            }
        }

        // recup�ration du ZIP
        File currentFolder = new File(currentSedexFolder);
        File[] listFiles = currentFolder.listFiles();
        if (Objects.nonNull(listFiles) && listFiles.length > 0)  {
            zipPath[0] = listFiles[0].getAbsolutePath();
        }

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;

                currentGroupedMessage.simpleMessages.forEach(messageToTreat -> {
                    try {
                        importMessagesSingle(messageToTreat, defaultUserGestionnaire[0], zipPath[0]);
                        LOG.info("GFTraitementMessageServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                    } catch (Exception e) {
                        try {
                            sendMail(messageToTreat, zipPath[0]);
                        } catch (Exception e1) {
                            LOG.error("GFTraitementMessageServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                        }
                        throw new JadeApplicationRuntimeException(e);
                    }
                });
            } else if (message instanceof SimpleSedexMessage) {
                SimpleSedexMessage messageToTreat = (SimpleSedexMessage) message;
                try {
                    importMessagesSingle(messageToTreat, defaultUserGestionnaire[0], zipPath[0]);
                    LOG.info("GFTraitementMessageServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                } catch (Exception e) {
                    try {
                        sendMail(messageToTreat, zipPath[0]);
                    } catch (Exception e1) {
                        LOG.error("GFTraitementMessageServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                    }
                    throw new JadeApplicationRuntimeException(e);
                }
            } else {
                LOG.error("GFTraitementMessageServiceImpl#importMessages - Type de message Sedex non g�r�!");
                throw new JadeApplicationRuntimeException("Type de message Sedex non g�r�!");
            }
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "GFTraitementMessageServiceImpl#importMessages - Une erreur s'est produite pendant l'importation d'un formulaire P14: " + e1.getMessage());
            throw new JadeApplicationRuntimeException(e1);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * M�thode de lecture du message sedex en r�ception, et traitement
     */
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage, String userGestionnaire, String zipPath) throws RuntimeException {
        try {
            GFFormHandler formHandler = objectFactory.getFormHandler(currentSimpleMessage, session);
            if(formHandler != null){
                formHandler.setDataFromFile(userGestionnaire, zipPath);
                formHandler.saveDataInDb();

                LOG.info("GFTraitementMessageServiceImpl#importMessagesSingle - formulaire sauvegard� avec succ�s : {}.", currentSimpleMessage.fileLocation);
            }
        } catch (Exception e){
            LOG.error("GFTraitementMessageServiceImpl#importMessagesSingle - Erreur lors du traitement du message.");
            throw new JadeApplicationRuntimeException(e);
        }
    }

    /**
     * Retourne un contexte. Si n�cessaire il est initialis�
     *
     * @return le contexte
     *
     * @throws Exception
     *             Exception lev�e si le contexte ne peut �tre initialis�
     */
    public JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(session).getContext();
        }
        return context;
    }


    /**
     * Initialise un contexte
     *
     * @param session
     *            session
     * @return le contexte initialis�
     * @throws Exception
     *             Exception lev�e si le contexte ne peut �tre initialis�
     */
    private JadeThreadContext initContext(BSession session) throws Exception {
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

    private void sendMail(SimpleSedexMessage message, String zipPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getMailsProtocole(), getMailSubjet(), getMailBody(message),
                new String[] { zipPath });
    }

    private String getMailSubjet(){
        return session.getLabel("MAIL_SUBJECT_IMPORT_SEDEX");
    }

    private String getMailBody(SimpleSedexMessage message){
        return session.getLabel("MAIL_BODY_IMPORT_SEDEX") + message.getFileLocation();
    }

    private String[] getMailsProtocole() {
        try {
            return GFProperties.EMAIL_EFORM.getValue().split(";");
        } catch (PropertiesException e) {
            LOG.error("GFTraitementMessageServiceImpl#getMailsProtocole - Erreur � la r�cup�ration de la propri�t� Adresse E-mail !! ", e);
        }
        return null;
    }
}
