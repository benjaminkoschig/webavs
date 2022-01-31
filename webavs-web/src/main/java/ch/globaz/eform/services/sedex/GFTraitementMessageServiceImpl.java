package ch.globaz.eform.services.sedex;

import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.eform.services.sedex.handlers.GFFormHandler;
import ch.globaz.eform.services.sedex.handlers.GFFormHandlersFactory;
import ch.globaz.eform.web.application.GFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;

import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Properties;

@Slf4j
public class GFTraitementMessageServiceImpl {
    private String passSedex;
    private String userSedex;
    private BSession session;
    private JadeContext context;

    private String eFormBackupFolder;

    private GFFormHandlersFactory objectFactory;

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
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
            JadeLogger.error(this, "Réception message RP AMAL: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: user sedex non renseigné. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "Réception message RP AMAL: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: pass sedex non renseigné. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        objectFactory = new GFFormHandlersFactory();
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
                        LOG.error("Traitement NOK pour le fichier {}", messageToTreat.fileLocation);
                    }
                }
            } else if (message instanceof SimpleSedexMessage) {
                SimpleSedexMessage messageToTreat = (SimpleSedexMessage) message;
                boolean importSuccess = importMessagesSingle(messageToTreat);
                if(importSuccess){
                    LOG.info("Traitement OK pour le fichier {}", messageToTreat.fileLocation);
                }else{
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
     * Méthode de lecture du message sedex en réception, et traitement
     *
     * @return Retourne si le trait
     */
    private boolean importMessagesSingle(SimpleSedexMessage currentSimpleMessage) {
        boolean traitementSucceed = false;
        try {
            GFFormHandler formHandler = objectFactory.getFormHandler(currentSimpleMessage, currentSimpleMessage.getFileLocation());
            if(formHandler != null){
                //traitementSucceed = formHandler.saveDataInDb(session);
            }
        }catch (JAXBValidationError | JAXBValidationWarning | JAXBException | IOException | SAXException | InstantiationException | IllegalAccessException  e){
            LOG.error("Erreur lors du traitement du message.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return traitementSucceed;
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
     * Retourne une session. Si nécessaire elle est initialisée
     *
     * @return la session
     *
     * @throws Exception
     *             Exception levée si la session ne peut être initialisée
     */
    public BSession getSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(GFApplication.DEFAULT_APPLICATION_EFORM)
                    .newSession(userSedex, passSedex);
        }

        return session;
    }

}
