package ch.globaz.eform.businessimpl.services.sedex;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFHandlersFactory;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFSedexhandler;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.validator.GFDaDossierValidator;
import ch.globaz.eform.web.application.GFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class GFTraitementReceptionServiceImpl {
    private BSession session;
    private JadeContext context;
    private GFHandlersFactory objectFactory;

    @Setup
    public void setUp(Properties properties) throws Exception {
        String encryptedUser = properties.getProperty("userSedex");
        String encryptedPass = properties.getProperty("passSedex");

        if (encryptedUser == null) {
            LOG.error("GFTraitementReceptionServiceImpl#setUp - Réception message demande DA-Dossier: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message demande DA-Dossier: user sedex non renseigné. ");
        }
        if (encryptedPass == null) {
            LOG.error("GFTraitementReceptionServiceImpl#setUp - Réception message demande DA-Dossier: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message demande DA-Dossier: pass sedex non renseigné. ");
        }

        String userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);
        String passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFHandlersFactory();

        session = (BSession) GlobazSystem.getApplication(GFApplication.APPLICATION_ID).newSession(userSedex, passSedex);
    }

    @OnReceive
    public void importMessages(SedexMessage message) {
        ZipFile zipFile;

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());
            JadeThread.storeTemporaryObject("bsession",session);
            ValidationResult result = new ValidationResult();

            if (message instanceof GroupedSedexMessage) {
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;

                zipFile = new ZipFile(currentGroupedMessage.zipFileLocation);

                currentGroupedMessage.simpleMessages.forEach(messageToTreat -> {
                    traitementSimpleSedexMessage(messageToTreat, zipFile, result);
                });
            } else if (message instanceof SimpleSedexMessage) {
                SimpleSedexMessage messageToTreat = (SimpleSedexMessage) message;

                zipFile = new ZipFile(messageToTreat.zipFileLocation);

                traitementSimpleSedexMessage(messageToTreat, zipFile, result);
            } else {
                LOG.error("GFTraitementReceptionServiceImpl#importMessages - Type de message Sedex non géré!");
                throw new JadeApplicationRuntimeException("Type de message Sedex non géré!");
            }
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "GFTraitementReceptionServiceImpl#importMessages - Une erreur s'est produite pendant l'importation de la demande de dossier: " + e1.getMessage());
            throw new JadeApplicationRuntimeException(e1);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    public JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(session).getContext();
        }
        return context;
    }

    private void traitementSimpleSedexMessage(SimpleSedexMessage messageToTreat, ZipFile zipFile, ValidationResult result) {
        boolean errorMailSanded = false;

        try {
            importMessagesSingle(messageToTreat, zipFile, result);

            if (result.hasError()) {
                sendMail(zipFile, result);
                errorMailSanded = true;
                LOG.error("GFTraitementReceptionServiceImpl#importMessages - Traitement en erreur pour le fichier {}", messageToTreat.fileLocation);
                result.getErrors().forEach(error -> LOG.error("GFTraitementReceptionServiceImpl#importMessages - {} ", error.getDesignation(session)));
                //Déclenchement de l'exception pour invalider l'import sedex
                throw new JadeApplicationRuntimeException("");
            } else {
                LOG.info("GFTraitementReceptionServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
            }
        } catch (Exception e) {
            if (!errorMailSanded) {
                try {
                    sendMail(zipFile);
                } catch (Exception e1) {
                    LOG.error("GFTraitementReceptionServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                }
            }
            throw new JadeApplicationRuntimeException(e);
        }
    }

    /**
     * Initialise un contexte
     *
     * @param session session
     * @return le contexte initialisé
     * @throws Exception Exception levée si le contexte ne peut ętre initialisé
     */
    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(GFApplication.DEFAULT_APPLICATION_ROOT);
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
     * Méthode de lecture du message sedex en réception et traitement
     */
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage, ZipFile zipFile, ValidationResult result) throws RuntimeException {
        try {
            GFDaDossierValidator.sedexMessage102(currentSimpleMessage, result);
            if (!result.hasError()) {
                GFSedexhandler handler = objectFactory.getSedexHandler(currentSimpleMessage, session);
                if (handler != null) {
                    Map<String, Object> extraData = new HashMap<>();
                    extraData.put("zipFile", zipFile);
                    extraData.put("typeTreat", GFTypeDADossier.RECEPTION);

                    handler.setData(extraData);

                    if (result.hasInfo() && result.getInfos().containsKey("yourBusinessReferenceId")) {
                        handler.update(result);
                    } else {
                        handler.create(result);
                    }
                    LOG.info("GFTraitementReceptionServiceImpl#importMessagesSingle - formulaire sauvegardé avec succčs : {}.", currentSimpleMessage.fileLocation);
                }
            }
        } catch (Exception e) {
            LOG.error("GFTraitementReceptionServiceImpl#importMessagesSingle - Erreur lors du traitement du message.", e);
            throw new JadeApplicationRuntimeException(e);
        }
    }



    private void sendMail(ZipFile zipFile) throws Exception {
        sendMail(zipFile, null);
    }

    private void sendMail(ZipFile zipFile, ValidationResult validationResult) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getMailsProtocole(), getMailSubjet(), getMailBody(zipFile, validationResult),
                new String[]{zipFile.getPath()});
    }

    private String getMailSubjet() {
        return session.getLabel("MAIL_SUBJECT_RECEPTION_SEDEX");
    }

    private String getMailBody(ZipFile zipFile, ValidationResult validationResult) {
        StringBuilder body = new StringBuilder(String.format(session.getLabel("MAIL_BODY_RECEPTION_SEDEX"), zipFile.getName()));

        if (Objects.nonNull(validationResult)) {
            body.append(session.getLabel("MAIL_BODY_RECEPTION_ERROR_SECTION_SEDEX"));
            validationResult.getErrors().forEach(error -> body.append("\n").append(error.getDesignation(session)));
        }

        return body.toString();
    }

    private String[] getMailsProtocole() {
        try {
            return GFProperties.EMAIL_DADOSSIER.getValue().split(";");
        } catch (PropertiesException e) {
            LOG.error("GFTraitementReceptionServiceImpl#getMailsProtocole - Erreur ŕ la récupération de la propriété Adresse E-mail !! ", e);
        }
        return null;
    }
}
