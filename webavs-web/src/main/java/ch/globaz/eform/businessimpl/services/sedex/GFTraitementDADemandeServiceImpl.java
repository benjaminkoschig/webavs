package ch.globaz.eform.businessimpl.services.sedex;

import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFFormHandler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFFormHandlersFactory;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.validator.GFDaDossierValidator;
import ch.globaz.eform.validator.GFEFormValidator;
import ch.globaz.eform.web.application.GFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
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
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Properties;

@Slf4j
public class GFTraitementDADemandeServiceImpl {
    private BSession session;
    private JadeContext context;
    private GFFormHandlersFactory objectFactory;


    public void setUp(Properties properties) throws Exception {
        String encryptedUser = properties.getProperty("userSedex");
        String encryptedPass = properties.getProperty("passSedex");

        if (encryptedUser == null) {
            LOG.error("GFTraitementDADemandeServiceImpl#setUp - Réception message demande DA-Dossier: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message demande DA-Dossier: user sedex non renseigné. ");
        }
        if (encryptedPass == null) {
            LOG.error("GFTraitementDADemandeServiceImpl#setUp - Réception message demande DA-Dossier: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message demande DA-Dossier: pass sedex non renseigné. ");
        }

        String userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);
        String passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFFormHandlersFactory();

        session = (BSession) GlobazSystem.getApplication(GFApplication.APPLICATION_ID).newSession(userSedex, passSedex);
    }

    @OnReceive
    public void importMessages(SedexMessage message) {
        ZipFile zipFile;
        boolean[] errorMailSanded = {false};

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());
            ValidationResult result = new ValidationResult();


            if (message instanceof GroupedSedexMessage) {
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;

                zipFile = new ZipFile(currentGroupedMessage.zipFileLocation);

                currentGroupedMessage.simpleMessages.forEach(messageToTreat -> {
                    try {
                        importMessagesSingle(messageToTreat, zipFile, result);

                        if (result.hasError()) {
                            sendMail(zipFile, result);
                            errorMailSanded[0] = true;
                            LOG.error("GFTraitementDADemandeServiceImpl#importMessages - Traitement en erreur pour le fichier {}", messageToTreat.fileLocation);
                            result.getErrors().forEach(error -> LOG.error("GFTraitementDADemandeServiceImpl#importMessages - {} ", error.getDesignation(session)));
                            //Déclenchement de l'exception pour invalidé l'import sedex
                            throw new JadeApplicationRuntimeException("");
                        } else {
                            LOG.info("GFTraitementDADemandeServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                        }
                    } catch (Exception e) {
                        if (!errorMailSanded[0]) {
                            try {
                                sendMail(zipFile);
                            } catch (Exception e1) {
                                LOG.error("GFTraitementDADemandeServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                            }
                        }
                        throw new JadeApplicationRuntimeException(e);
                    }
                });
            } else if (message instanceof SimpleSedexMessage) {
                SimpleSedexMessage messageToTreat = (SimpleSedexMessage) message;

                zipFile = new ZipFile(messageToTreat.zipFileLocation);

                try {
                    importMessagesSingle(messageToTreat, zipFile, result);

                    if (result.hasError()) {
                        sendMail(zipFile, result);
                        errorMailSanded[0] = true;
                        LOG.error("GFTraitementDADemandeServiceImpl#importMessages - Traitement en erreur pour le fichier {}", messageToTreat.fileLocation);
                        result.getErrors().forEach(error -> LOG.error("GFTraitementDADemandeServiceImpl#importMessages - {} ", error.getDesignation(session)));
                        //Déclenchement de l'exception pour invalidé l'import sedex
                        throw new JadeApplicationRuntimeException("");
                    } else {
                        LOG.info("GFTraitementDADemandeServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                    }
                } catch (Exception e) {
                    if (!errorMailSanded[0]) {
                        try {
                            sendMail(zipFile);
                        } catch (Exception e1) {
                            LOG.error("GFTraitementDADemandeServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                        }
                    }
                    throw new JadeApplicationRuntimeException(e);
                }
            } else {
                LOG.error("GFTraitementDADemandeServiceImpl#importMessages - Type de message Sedex non géré!");
                throw new JadeApplicationRuntimeException("Type de message Sedex non géré!");
            }
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "GFTraitementDADemandeServiceImpl#importMessages - Une erreur s'est produite pendant l'importation d'un formulaire P14: " + e1.getMessage());
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

    /**
     * Initialise un contexte
     *
     * @param session session
     * @return le contexte initialisé
     * @throws Exception Exception levée si le contexte ne peut être initialisé
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

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     */
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage, ZipFile zipFile, ValidationResult result) throws RuntimeException {
        try {
            GFDaDossierValidator.sedexMessage(currentSimpleMessage, result);
            if (!result.hasError()) {
                GFFormHandler formHandler = objectFactory.getFormHandler(currentSimpleMessage, session);
                if (formHandler != null) {
                    formHandler.setDataFromFile(null, null);
                    formHandler.saveData(result, zipFile);

                    LOG.info("GFTraitementDADemandeServiceImpl#importMessagesSingle - formulaire sauvegardé avec succès : {}.", currentSimpleMessage.fileLocation);
                }
            }
        } catch (Exception e) {
            LOG.error("GFTraitementDADemandeServiceImpl#importMessagesSingle - Erreur lors du traitement du message.");
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
        return session.getLabel("MAIL_SUBJECT_IMPORT_SEDEX");
    }

    private String getMailBody(ZipFile zipFile, ValidationResult validationResult) {
        StringBuilder body = new StringBuilder(String.format(session.getLabel("MAIL_BODY_IMPORT_SEDEX"), zipFile.getName()));

        if (Objects.nonNull(validationResult)) {
            body.append(session.getLabel("MAIL_BODY_IMPORT_ERROR_SECTION_SEDEX"));
            validationResult.getErrors().forEach(error -> body.append("\n").append(error.getDesignation(session)));
        }

        return body.toString();
    }

    private String[] getMailsProtocole() {
        try {
            return GFProperties.EMAIL_EFORM.getValue().split(";");
        } catch (PropertiesException e) {
            LOG.error("GFTraitementDADemandeServiceImpl#getMailsProtocole - Erreur à la récupération de la propriété Adresse E-mail !! ", e);
        }
        return null;
    }
}
