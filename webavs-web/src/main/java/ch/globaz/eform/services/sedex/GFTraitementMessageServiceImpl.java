package ch.globaz.eform.services.sedex;

import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.services.sedex.handlers.GFFormHandler;
import ch.globaz.eform.services.sedex.handlers.GFFormHandlersFactory;
import ch.globaz.eform.services.sedex.model.GFSedexModel;
import ch.globaz.eform.web.application.GFApplication;
import globaz.apg.properties.APProperties;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.*;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.smtp.JadeSmtpClient;

import globaz.jade.sedex.message.*;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;

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
     * @param properties
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    @Setup
    public void setUp(Properties properties)
            throws JadeDecryptionNotSupportedException, JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");

        if (encryptedUser == null) {
            JadeLogger.error(this, "R�ception message RP AMAL: user sedex non renseign�. ");
            throw new IllegalStateException("R�ception message RP AMAL: user sedex non renseign�. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "R�ception message RP AMAL: pass sedex non renseign�. ");
            throw new IllegalStateException("R�ception message RP AMAL: pass sedex non renseign�. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFFormHandlersFactory();

        currentSedexFolder = properties.getProperty("currentSedexFolder");
    }

    @OnReceive
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException {

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;
                Iterator<SimpleSedexMessage> messagesIterator = currentGroupedMessage.iterator();
                while (messagesIterator.hasNext()) {
                    SimpleSedexMessage messageToTreat = messagesIterator.next();
                    boolean importSuccess = importMessagesSingle(messageToTreat);
                    if(importSuccess){
                        LOG.info("Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                    }else{
                        sendMail(messageToTreat);
                        LOG.error("Traitement NOK pour le fichier {}", messageToTreat.fileLocation);
                    }
                }
            } else if (message instanceof SimpleSedexMessage) {
                SimpleSedexMessage messageToTreat = (SimpleSedexMessage) message;
                boolean importSuccess = importMessagesSingle(messageToTreat);

                if(importSuccess){
                    LOG.info("Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                }else{
                    sendMail(messageToTreat);
                    LOG.error("Traitement NOK pour le fichier {}", messageToTreat.fileLocation);
                }
            } else {
                LOG.error("Traitement NOK pour le fichier.");
            }
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant l'importation d'un formulaire P14: " + e1.getMessage());
            throw new JadeApplicationRuntimeException(e1);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * M�thode de lecture du message sedex en r�ception, et traitement
     *
     * @return Retourne si le trait
     */
    private boolean importMessagesSingle(SimpleSedexMessage currentSimpleMessage) {
        boolean traitementSucceed = false;
        try {
            GFFormHandler formHandler = objectFactory.getFormHandler(currentSimpleMessage, currentSimpleMessage.getFileLocation(), currentSedexFolder);
            if(formHandler != null){
                traitementSucceed = formHandler.setDataFromFile(currentSimpleMessage, currentSimpleMessage.getFileLocation(), currentSedexFolder);

                if(traitementSucceed) {
                    boolean saveSucceed;
                    saveSucceed = formHandler.saveDataInDb(session);
                    if(saveSucceed){
                        LOG.info("formulaire sauvegard� : {}.", currentSimpleMessage.fileLocation);
                    } else {
                        LOG.error("formulaire non sauvegard� : {}", currentSimpleMessage.fileLocation);
                    }
                } else {
                    LOG.error("Le formulaire n'a pas pu �tre trait� : {}", currentSimpleMessage.fileLocation);
                }
            }
        }catch (JAXBValidationError | JAXBValidationWarning | JAXBException | IOException | SAXException | InstantiationException | IllegalAccessException  e){
            LOG.error("Erreur lors du traitement du message.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return traitementSucceed;
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
            context = initContext(getSession()).getContext();
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

    /**
     * Retourne une session. Si n�cessaire elle est initialis�e
     *
     * @return la session
     *
     * @throws Exception
     *             Exception lev�e si la session ne peut �tre initialis�e
     */
    public BSession getSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(GFApplication.DEFAULT_APPLICATION_EFORM)
                    .newSession(userSedex, passSedex);
        }
        return session;
    }

    private void sendMail(SimpleSedexMessage message) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getMailsProtocole(), getMailSubjet(), getMailBody(message),
                new String[] { message.getFileLocation() });
    }

    private String getMailSubjet(){
        return session.getLabel("MAIL_SUBLECT_IMPORT_SEDEX");
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
