package ch.globaz.eform.businessimpl.services.sedex;

import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFHandlersFactory;
import ch.globaz.eform.businessimpl.services.sedex.handlers.GFSedexhandler;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.validator.GFDaDossierValidator;
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
public class GFTraitementDemandeTransfereServiceImpl {
    private BSession session;
    private JadeContext context;
    private GFHandlersFactory objectFactory;


    public void setUp(Properties properties) throws Exception {
        String encryptedUser = properties.getProperty("userSedex");
        String encryptedPass = properties.getProperty("passSedex");

        if (encryptedUser == null) {
            LOG.error("GFTraitementDemandeTransfereServiceImpl#setUp - R�ception message demande DA-Dossier: user sedex non renseign�. ");
            throw new IllegalStateException("R�ception message demande DA-Dossier: user sedex non renseign�. ");
        }
        if (encryptedPass == null) {
            LOG.error("GFTraitementDemandeTransfereServiceImpl#setUp - R�ception message demande DA-Dossier: pass sedex non renseign�. ");
            throw new IllegalStateException("R�ception message demande DA-Dossier: pass sedex non renseign�. ");
        }

        String userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);
        String passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFHandlersFactory();

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
                        importMessagesSingle(messageToTreat, result);

                        if (result.hasError()) {
                            sendMail(zipFile, result);
                            errorMailSanded[0] = true;
                            LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - Traitement en erreur pour le fichier {}", messageToTreat.fileLocation);
                            result.getErrors().forEach(error -> LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - {} ", error.getDesignation(session)));
                            //D�clenchement de l'exception pour invalid� l'import sedex
                            throw new JadeApplicationRuntimeException("");
                        } else {
                            LOG.info("GFTraitementDemandeTransfereServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                        }
                    } catch (Exception e) {
                        if (!errorMailSanded[0]) {
                            try {
                                sendMail(zipFile);
                            } catch (Exception e1) {
                                LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                            }
                        }
                        throw new JadeApplicationRuntimeException(e);
                    }
                });
            } else if (message instanceof SimpleSedexMessage) {
                SimpleSedexMessage messageToTreat = (SimpleSedexMessage) message;

                zipFile = new ZipFile(messageToTreat.zipFileLocation);

                try {
                    importMessagesSingle(messageToTreat, result);

                    if (result.hasError()) {
                        sendMail(zipFile, result);
                        errorMailSanded[0] = true;
                        LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - Traitement en erreur pour le fichier {}", messageToTreat.fileLocation);
                        result.getErrors().forEach(error -> LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - {} ", error.getDesignation(session)));
                        //D�clenchement de l'exception pour invalid� l'import sedex
                        throw new JadeApplicationRuntimeException("");
                    } else {
                        LOG.info("GFTraitementDemandeTransfereServiceImpl#importMessages - Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                    }
                } catch (Exception e) {
                    if (!errorMailSanded[0]) {
                        try {
                            sendMail(zipFile);
                        } catch (Exception e1) {
                            LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - Une Erreur s'est produite lors de l'envoie du mail!", e1);
                        }
                    }
                    throw new JadeApplicationRuntimeException(e);
                }
            } else {
                LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessages - Type de message Sedex non g�r�!");
                throw new JadeApplicationRuntimeException("Type de message Sedex non g�r�!");
            }
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "GFTraitementDemandeTransfereServiceImpl#importMessages - Une erreur s'est produite pendant l'importation d'un formulaire P14: " + e1.getMessage());
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
     * @return le contexte initialis�
     * @throws Exception Exception lev�e si le contexte ne peut �tre initialis�
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
     * M�thode de lecture du message sedex en r�ception et traitement
     */
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage, ValidationResult result) throws RuntimeException {
        try {
            GFDaDossierValidator.sedexMessage101(currentSimpleMessage, result);
            if (!result.hasError()) {
                GFSedexhandler handler = objectFactory.getSedexHandler(currentSimpleMessage, session);
                if (handler != null) {
                    handler.save(result);

                    LOG.info("GFTraitementDemandeTransfereServiceImpl#importMessagesSingle - formulaire sauvegard� avec succ�s : {}.", currentSimpleMessage.fileLocation);
                }
            }
        } catch (Exception e) {
            LOG.error("GFTraitementDemandeTransfereServiceImpl#importMessagesSingle - Erreur lors du traitement du message.");
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
        return session.getLabel("MAIL_SUBJECT_DEMANDE_SEDEX");
    }

    private String getMailBody(ZipFile zipFile, ValidationResult validationResult) {
        StringBuilder body = new StringBuilder(String.format(session.getLabel("MAIL_BODY_DEMANDE_SEDEX"), zipFile.getName()));

        if (Objects.nonNull(validationResult)) {
            body.append(session.getLabel("MAIL_BODY_DEMANDE_ERROR_SECTION_SEDEX"));
            validationResult.getErrors().forEach(error -> body.append("\n").append(error.getDesignation(session)));
        }

        return body.toString();
    }

    private String[] getMailsProtocole() {
        try {
            return GFProperties.EMAIL_EFORM.getValue().split(";");
        } catch (PropertiesException e) {
            LOG.error("GFTraitementDemandeTransfereServiceImpl#getMailsProtocole - Erreur � la r�cup�ration de la propri�t� Adresse E-mail !! ", e);
        }
        return null;
    }
}
