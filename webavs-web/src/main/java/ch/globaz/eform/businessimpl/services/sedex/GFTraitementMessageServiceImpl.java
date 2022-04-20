package ch.globaz.eform.businessimpl.services.sedex;

import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFFormHandler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFFormHandlersFactory;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.validator.GFEFormValidator;
import ch.globaz.eform.web.application.GFApplication;
import globaz.common.util.CommonBlobUtils;
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

    private BSession session;
    private JadeContext context;

    private GFFormHandlersFactory objectFactory;

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     *
     * @param properties Propriété du service Sedex
     */
    @Setup
    public void setUp(Properties properties)
            throws Exception {

        String encryptedUser = properties.getProperty("userSedex");
        String encryptedPass = properties.getProperty("passSedex");

        if (encryptedUser == null) {
            LOG.error("GFTraitementMessageServiceImpl#setUp - Réception message RP AMAL: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: user sedex non renseigné. ");
        }
        if (encryptedPass == null) {
            LOG.error("GFTraitementMessageServiceImpl#setUp - Réception message RP AMAL: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: pass sedex non renseigné. ");
        }

        String userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);
        String passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFFormHandlersFactory();

        session = (BSession) GlobazSystem.getApplication(GFApplication.APPLICATION_ID).newSession(userSedex, passSedex);
    }

    @OnReceive
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException {
        String[] defaultUserGestionnaire = {""};
        String[] zipPath = {null};
        String[] zipName = {null};
        byte[][] zipByte = {null};

        //Lecture dans les propriétés du gestionnaire par défaut
        if (!GFProperties.GESTIONNAIRE_USER_DEFAULT.getValue().isEmpty()) {
            try {
                JadeUser user = session.getApplication()._getSecurityManager().getUserForVisa(session, GFProperties.GESTIONNAIRE_USER_DEFAULT.getValue());
                if (Objects.nonNull(user.getVisa())) {
                    defaultUserGestionnaire[0] = user.getVisa();
                }
            } catch (FWSecurityLoginException e) {
                LOG.warn("GFTraitementMessageServiceImpl#importMessages - GFTraitementMessageServiceImpl#importMessages - Erreur à la récupération du gestionnaire par défaut :", e);
            } catch (Exception e) {
                LOG.warn("GFTraitementMessageServiceImpl#importMessages - GFTraitementMessageServiceImpl#importMessages - Erreur inconnue à la récupération du gestionnaire par défaut :", e);
            }
        }

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;

                // recupération du ZIP
                File zipFile = new File(currentGroupedMessage.zipFileLocation);
                zipPath[0] = currentGroupedMessage.zipFileLocation;
                zipName[0] = zipFile.getName();
                zipByte[0] = CommonBlobUtils.fileToByteArray(currentGroupedMessage.zipFileLocation);

                currentGroupedMessage.simpleMessages.forEach(messageToTreat -> {
                    try {
                        importMessagesSingle(messageToTreat, defaultUserGestionnaire[0], zipName[0], zipByte[0]);
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

                // recupération du ZIP
                File zipFile = new File(messageToTreat.zipFileLocation);
                zipPath[0] = messageToTreat.zipFileLocation;
                zipName[0] = zipFile.getName();
                zipByte[0] = CommonBlobUtils.fileToByteArray(messageToTreat.zipFileLocation);

                try {
                    importMessagesSingle(messageToTreat, defaultUserGestionnaire[0], zipName[0], zipByte[0]);
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
                LOG.error("GFTraitementMessageServiceImpl#importMessages - Type de message Sedex non géré!");
                throw new JadeApplicationRuntimeException("Type de message Sedex non géré!");
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
     * Méthode de lecture du message sedex en réception, et traitement
     */
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage, String userGestionnaire, String zipName, byte[] zipByte) throws RuntimeException {
        try {
            ValidationResult result = GFEFormValidator.sedexMessage(currentSimpleMessage);
            if (result.hasError()) {
                throw new Exception("Le message sedex comporte des erreurs");
            }
            GFFormHandler formHandler = objectFactory.getFormHandler(currentSimpleMessage, session);
            if(formHandler != null){
                formHandler.setDataFromFile(userGestionnaire, zipName, zipByte);
                formHandler.saveDataInDb();

                LOG.info("GFTraitementMessageServiceImpl#importMessagesSingle - formulaire sauvegardé avec succès : {}.", currentSimpleMessage.fileLocation);
            }
        } catch (Exception e){
            LOG.error("GFTraitementMessageServiceImpl#importMessagesSingle - Erreur lors du traitement du message.");
            throw new JadeApplicationRuntimeException(e);
        }
    }

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
            context = initContext(session).getContext();
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
            LOG.error("GFTraitementMessageServiceImpl#getMailsProtocole - Erreur à la récupération de la propriété Adresse E-mail !! ", e);
        }
        return null;
    }
}
