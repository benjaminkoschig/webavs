package ch.globaz.al.businessimpl.services.rafam.sedex;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import al.ch.ech.xmlns.ech_00058._5.SendingApplicationType;
import al.ch.ech.xmlns.ech_0104_68._4.ContentType;
import al.ch.ech.xmlns.ech_0104_68._4.HeaderType;
import al.ch.ech.xmlns.ech_0104_68._4.Message;
import al.ch.ech.xmlns.ech_0104_68._4.ObjectFactory;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService;
import ch.globaz.al.business.services.rafam.sedex.ExportAnnoncesNewXSDVersionRafamService;
import ch.globaz.al.businessimpl.rafam.sedex.builder.MessageBuilderFactoryNewXSDVersion;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.smtp.JadeSmtpClient;

/**
 * Implémentation du service d'envoi des annonces RAFam
 *
 *
 * @author jts
 *
 */
public class ExportAnnoncesNewXSDVersionRafamServiceImpl extends ALAbstractBusinessServiceImpl
        implements ExportAnnoncesNewXSDVersionRafamService {
    private static final String XSD_FOLDER = "/xsd/al/";
    private static final String XSD_FILE_NAME = "eCH-0104-68-4-1.xsd";
    private final static String MARSHALLED_XML = "marshalled.xml";

    /**
     * Initialise l'en-tête du fichier d'annonce
     *
     * @return l'en-tête initialisée
     *
     * @throws JadeSedexDirectoryInitializationException
     *                                                       Exception levée si dépôt Sedex ne peut être trouvé
     * @throws DatatypeConfigurationException
     *                                                       Exception levée si l'en-tête ne peut être initialisée
     * @throws ALAnnonceRafamException
     *                                                       Exception levée si une erreur métier se produit
     */
    private HeaderType getHeader(boolean isDelegue)
            throws JadeSedexDirectoryInitializationException, DatatypeConfigurationException, ALAnnonceRafamException {

        String messageType = JadePropertiesService.getInstance().getProperty(ALConstRafam.MESSAGE_TYPE);
        if (JadeStringUtil.isBlank(messageType)) {
            throw new ALAnnonceRafamException(
                    "ExportAnnoncesRafamServiceImpl#getHeader : message type is null or empty");
        }

        String recipientId = JadePropertiesService.getInstance().getProperty(ALConstRafam.RECIPIENT_ID);
        if (JadeStringUtil.isBlank(recipientId)) {
            throw new ALAnnonceRafamException(
                    "ExportAnnoncesRafamServiceImpl#getHeader : recipientId is null or empty");
        }

        ObjectFactory of = new ObjectFactory();
        HeaderType header = of.createHeaderType();

        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        header.setMessageId(JadeUUIDGenerator.createStringUUID());
        header.setMessageType(messageType);
        header.setSubMessageType(ALConstRafam.SUB_MESSAGE_TYPE_0);
        header.setAction(ALConstRafam.ACTION_1);
        header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());
        header.getRecipientId().add(recipientId);
        if (isDelegue) {
            header.setSubject(ALConstRafam.SUBJECT_ANNONCE_RAFAM_DELEGUE);
        } else {
            header.setSubject(ALConstRafam.SUBJECT_ANNONCE_RAFAM);
        }

        SendingApplicationType sat = new SendingApplicationType();
        sat.setManufacturer(ALConstRafam.MANUFACTURER);
        sat.setProduct(ALConstRafam.PRODUCT_NAME);
        sat.setProductVersion(ALConstRafam.PRODUCT_VERSION);
        header.setSendingApplication(sat);

        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar nowDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        XMLGregorianCalendar nowDate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);

        header.setMessageDate(nowDateTime);
        header.setEventDate(nowDate);

        return header;
    }

    /**
     * Récupère les messages contenu dans le log passé en paramètre
     *
     * @param logMessages
     *                        Le message de log
     * @return StringBuffer contenant les messages
     */
    private StringBuffer getMessages(JadeBusinessMessage[] logMessages) {
        StringBuffer sb = new StringBuffer();

        if (logMessages != null) {

            for (int i = 0; i < logMessages.length; i++) {
                sb.append(
                        JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), logMessages[i].getMessageId()))
                        .append("; ");
            }
        }

        return sb;
    }

    /**
     * Vérifie le paramètre indiquant si les annonces doivent être exportées
     *
     * @return <code>true</code> si les annonces doivent être exportées
     */
    private boolean isExportEnabled() {
        return "1".equals(JadePropertiesService.getInstance().getProperty(ALConstRafam.EXPORT_IS_ENABLED));
    }

    /**
     * Restaure l'état des annonces contenues dans la liste passée en paramètre. Cette méthode est utilisée si une
     * erreur se produit pendant la génération du fichier XML afin de pouvoir retraiter ces annonces à l'envoi suivant.
     *
     *
     * @param updatedAnnonces
     *                            les annonces à restaurer
     *
     * @throws JadeApplicationException
     *                                      Exception levée par la couche métier lorsqu'elle n'a pu effectuer
     *                                      l'opération souhaitée
     * @throws JadePersistenceException
     *                                      Exception levée lorsque le chargement ou la mise à jour en DB par la couche
     *                                      de persistence n'a pu se
     *                                      faire
     *
     * @throws Exception
     *                                      Exception levée si une erreur se produit à l'enregistrement des données
     */
    protected void restoreEtatAnnonces(ArrayList<AnnonceRafamModel> updatedAnnonces)
            throws JadeApplicationException, JadePersistenceException, Exception {

        StringBuffer ids = new StringBuffer();

        for (AnnonceRafamModel annonce : updatedAnnonces) {
            ALImplServiceLocator.getAnnonceRafamBusinessService().setEtat(annonce, RafamEtatAnnonce.A_TRANSMETTRE);
            ids.append(annonce.getId()).append(", ");
        }

        if (JadeThread.logMessages() != null) {
            try {
                JadeThread.rollbackSession();
            } catch (Exception e) {
                throw e;
            }
            JadeThread.logClear();
            throw new ALRafamSedexException(
                    "ExportAnnoncesRafamServiceImpl#restoreEtatAnnonces : l'état des annonces suivantes n'a pas pu être restauré : "
                            + ids.toString());
        } else {
            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Envoie un e-mail contenant la chaîne du paramètre <code>mailContent</code>
     *
     * @param mailContent
     *                        Contenu du message
     * @param recipientId
     *                        Recipient Id (affiché dans le sujet du message)
     * @throws Exception
     *                       Exception levée si une erreur se produit à l'envoi de l'e-mail
     */
    protected void sendMailError(StringBuffer mailContent, String recipientId) throws Exception {

        StringBuffer title = new StringBuffer("Erreur pendant l'exportation des annonces RAFam (");
        title.append(recipientId);
        try {
            title.append(", ").append(ALServiceLocator.getParametersServices().getNomCaisse());
        } catch (Exception e) {
            // DO NOTHING, pas un problème si le nom de la caisse n'a pas pu être récupéré (cf : recipientId)
        }

        JadeSmtpClient.getInstance().sendMail(JadeThread.currentUserEmail(), title.toString(), mailContent.toString(),
                new String[0]);

    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.sedex.ExportAnnoncesRafamService#sendMessage()
     */
    @Override
    public void sendMessage() throws JadeApplicationException, JadePersistenceException,
            JadeSedexDirectoryInitializationException, DatatypeConfigurationException {

        if (isExportEnabled()) {

            StringBuffer errors = new StringBuffer();
            ArrayList<AnnonceRafamModel> updatedAnnonces = new ArrayList<AnnonceRafamModel>();

            AnnonceRafamComplexSearchModel annonces = ALImplServiceLocator.getAnnoncesRafamSearchService()
                    .loadAnnoncesToSend();

            AnnonceRafamBusinessService s = ALImplServiceLocator.getAnnonceRafamBusinessService();

            if (annonces.getSize() > 0) {

                ObjectFactory of = new ObjectFactory();
                Message message = of.createMessage();
                message.setMinorVersion(Integer.valueOf(ALConstRafam.MINOR_VERSION_XSD_4_1));
                message.setHeader(getHeader(false));
                ContentType content = of.createContentType();

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // Préparation du message
                // /////////////////////////////////////////////////////////////////////////////////////////////////////

                for (int i = 0; i < annonces.getSize(); i++) {

                    AnnonceRafamComplexModel annonce = ((AnnonceRafamComplexModel) annonces.getSearchResults()[i]);

                    try {
                        Object messageDroit = MessageBuilderFactoryNewXSDVersion.getMessageBuilder(annonce).build();

                        if (JadeThread.logMessages() != null) {
                            errors.append("Erreur pendant la préparation d'une annonce (ID " + annonce.getId())
                                    .append(", ").append(getMessages(JadeThread.logMessages())).append(")\n");
                            JadeLogger.error(this, annonce.getId() + " non exportée");
                            JadeThread.rollbackSession();
                            JadeThread.logClear();
                        } else {
                            JadeThread.commitSession();

                            if (messageDroit != null) {
                                updatedAnnonces.add(annonce.getAnnonceRafamModel());
                                content.getNewBenefitOrBenefitMutationOrBenefitCancellation().add(messageDroit);
                            }
                        }
                    } catch (JadeApplicationException e) {
                        try {
                            JadeThread.rollbackSession();
                            annonce.setAnnonceRafamModel(
                                    s.setError(annonce.getAnnonceRafamModel(), "al.rafam.sedex.preparation.error"));
                            JadeThread.commitSession();
                        } catch (Exception e2) {
                            JadeLogger.error(this,
                                    "Une erreur s'est produite pendant la mise à jour de l'annonce RAFam "
                                            + annonce.getId() + " : " + e2.getMessage());
                        }

                        errors.append("Erreur pendant la préparation d'une annonce (ID " + annonce.getId() + ") : "
                                + e.getMessage()).append("\n");
                        JadeLogger.error(this, "Erreur pendant la préparation d'une annonce (ID " + annonce.getId()
                                + ") : " + e.getMessage());
                    } catch (JadePersistenceException e) {

                        try {
                            JadeThread.rollbackSession();
                            annonce.setAnnonceRafamModel(
                                    s.setError(annonce.getAnnonceRafamModel(), "al.rafam.sedex.preparation.error"));
                            JadeThread.commitSession();
                        } catch (Exception e2) {
                            JadeLogger.error(this,
                                    "Une erreur s'est produite pendant la mise à jour de l'annonce RAFam "
                                            + annonce.getId() + " : " + e2.getMessage());
                        }

                        errors.append("Erreur pendant la préparation d'une annonce (ID " + annonce.getId() + ") : "
                                + e.getMessage()).append("\n");
                        JadeLogger.error(this, "Erreur pendant la préparation d'une annonce (ID " + annonce.getId()
                                + ") : " + e.getMessage());
                    } catch (Exception e) {
                        JadeLogger.error(this, e.getMessage());
                    }

                    JadeThread.logClear();
                }

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // Envoi du mail contenant la liste des annonces qui n'ont pas pu être traitées
                // /////////////////////////////////////////////////////////////////////////////////////////////////////

                if (errors.length() > 0) {
                    try {
                        sendMailError(errors, message.getHeader().getRecipientId().get(0));
                    } catch (Exception e) {
                        JadeLogger.error(this, "Erreur pendant l'envoi d'un e-mail : " + e.getMessage());
                        // Exception levée si le mail n'a pas pu être envoyé. On ne peux rien faire de plus mais les
                        // messages
                        // d'erreur sont enregistrés dans le log
                    }
                }

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // Envoi du message
                // /////////////////////////////////////////////////////////////////////////////////////////////////////

                try {
                    if (content.getNewBenefitOrBenefitMutationOrBenefitCancellation().size() > 0) {
                        message.setContent(content);

                        // JAXBServices jaxb = JAXBServices.getInstance();
                        // Class<?>[] addClasses = new Class[] { Message.class, InfoType.class,
                        // al.ch.ech.xmlns.ech_0104._4.DeliveryOfficeType.class,
                        // al.ch.ech.xmlns.ech_0007._6.CantonAbbreviationType.class,
                        // al.ch.ech.xmlns.ech_00044_f._4.PersonIdentificationType.class };
                        // String file = jaxb.marshal(message, true, false, addClasses);
                        // JadeSedexService.getInstance().sendSimpleMessage(file, null);
                        File xmlMarshaled = marshallCompleteMessage(message);
                        JadeSedexService.getInstance().sendSimpleMessage(xmlMarshaled.getAbsolutePath(), null);
                    }
                } catch (Exception e) {

                    errors = new StringBuffer();
                    try {
                        restoreEtatAnnonces(updatedAnnonces);
                    } catch (Exception e1) {
                        errors = errors.append(e.getMessage());
                        JadeLogger.error(this, errors.toString());
                    }

                    errors = errors.append(e.getMessage());
                    JadeLogger.error(this, errors.toString());

                    if (e instanceof JAXBValidationError) {
                        errors.append("\n");
                        for (ValidationEvent event : ((JAXBValidationError) e).getEvents()) {
                            errors.append(" - ").append(event.getMessage()).append("\n");
                        }
                    }

                    try {
                        sendMailError(errors, message.getHeader().getRecipientId().get(0));
                    } catch (Exception e2) {
                        JadeLogger.error(this, "Erreur pendant l'envoi d'un e-mail : " + e.getMessage());
                        // Exception levée si le mail n'a pas pu être envoyé. On ne peux rien faire de plus mais les
                        // messages
                        // d'erreur sont enregistrés dans le log
                    }
                }
            }
        }
    }

    @Override
    public void sendMessageDelegue() throws DatatypeConfigurationException, JadeApplicationException,
            JadePersistenceException, JadeSedexDirectoryInitializationException {

        if (isExportEnabled()) {

            StringBuffer errors = new StringBuffer();
            ArrayList<AnnonceRafamModel> updatedAnnonces = new ArrayList<AnnonceRafamModel>();

            AnnonceRafamSearchModel annonces = new AnnonceRafamSearchModel();
            annonces.setForDelegated(new Boolean(true));
            annonces.setForEtatAnnonce(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
            annonces.setWhereKey("afDelegue");
            annonces.setDefinedSearchSize(0);
            annonces = ALServiceLocator.getAnnonceRafamModelService().search(annonces);

            AnnonceRafamBusinessService s = ALImplServiceLocator.getAnnonceRafamBusinessService();

            if (annonces.getSize() > 0) {

                ObjectFactory of = new ObjectFactory();
                Message message = of.createMessage();
                message.setMinorVersion(Integer.valueOf(ALConstRafam.MINOR_VERSION_XSD_4_1));
                message.setHeader(getHeader(true));
                ContentType content = of.createContentType();

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // Préparation du message
                // /////////////////////////////////////////////////////////////////////////////////////////////////////

                for (int i = 0; i < annonces.getSize(); i++) {

                    AnnonceRafamModel annonceSimple = ((AnnonceRafamModel) annonces.getSearchResults()[i]);
                    AnnonceRafamComplexModel annonceComplex = new AnnonceRafamComplexModel();
                    annonceComplex.setAnnonceRafamModel(annonceSimple);

                    try {
                        Object messageDroit = MessageBuilderFactoryNewXSDVersion.getMessageBuilder(annonceComplex)
                                .build();

                        if (JadeThread.logMessages() != null) {
                            errors.append("Erreur pendant la préparation d'une annonce (ID " + annonceSimple.getId())
                                    .append(", ").append(getMessages(JadeThread.logMessages())).append(")\n");
                            JadeLogger.error(this, annonceSimple.getId() + " non exportée");
                            JadeThread.rollbackSession();
                            JadeThread.logClear();
                        } else {
                            JadeThread.commitSession();
                            if (messageDroit != null) {
                                updatedAnnonces.add(annonceSimple);
                                content.getNewBenefitOrBenefitMutationOrBenefitCancellation().add(messageDroit);
                            }
                        }
                    } catch (JadeApplicationException e) {
                        try {
                            JadeThread.rollbackSession();
                            annonceComplex.setAnnonceRafamModel(
                                    s.setError(annonceSimple, "al.rafam.sedex.preparation.error"));
                            JadeThread.commitSession();
                        } catch (Exception e2) {
                            JadeLogger.error(this,
                                    "Une erreur s'est produite pendant la mise à jour de l'annonce RAFam "
                                            + annonceSimple.getId() + " : " + e2.getMessage());
                        }

                        errors.append("Erreur pendant la préparation d'une annonce (ID " + annonceSimple.getId()
                                + ") : " + e.getMessage()).append("\n");
                        JadeLogger.error(this, "Erreur pendant la préparation d'une annonce (ID "
                                + annonceSimple.getId() + ") : " + e.getMessage());
                    } catch (JadePersistenceException e) {

                        try {
                            JadeThread.rollbackSession();
                            annonceComplex.setAnnonceRafamModel(
                                    s.setError(annonceSimple, "al.rafam.sedex.preparation.error"));
                            JadeThread.commitSession();
                        } catch (Exception e2) {
                            JadeLogger.error(this,
                                    "Une erreur s'est produite pendant la mise à jour de l'annonce RAFam "
                                            + annonceSimple.getId() + " : " + e2.getMessage());
                        }

                        errors.append("Erreur pendant la préparation d'une annonce (ID " + annonceSimple.getId()
                                + ") : " + e.getMessage()).append("\n");
                        JadeLogger.error(this, "Erreur pendant la préparation d'une annonce (ID "
                                + annonceSimple.getId() + ") : " + e.getMessage());
                    } catch (Exception e) {
                        JadeLogger.error(this, e.getMessage());
                    }

                    JadeThread.logClear();
                }

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // Envoi du mail contenant la liste des annonces qui n'ont pas pu être traitées
                // /////////////////////////////////////////////////////////////////////////////////////////////////////

                if (errors.length() > 0) {
                    try {
                        sendMailError(errors, message.getHeader().getRecipientId().get(0));
                    } catch (Exception e) {
                        JadeLogger.error(this, "Erreur pendant l'envoi d'un e-mail : " + e.getMessage());
                        // Exception levée si le mail n'a pas pu être envoyé. On ne peux rien faire de plus mais les
                        // messages
                        // d'erreur sont enregistrés dans le log
                    }
                }

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // Envoi du message
                // /////////////////////////////////////////////////////////////////////////////////////////////////////

                try {
                    if (content.getNewBenefitOrBenefitMutationOrBenefitCancellation().size() > 0) {
                        message.setContent(content);

                        // JAXBServices jaxb = JAXBServices.getInstance();
                        // Class<?>[] addClasses = new Class[] { /*
                        // * Message.class, ReportType.class,
                        // * ch.ech.xmlns.ech_0104._2.DeliveryOffice.class,
                        // * ch.ech.xmlns.ech_0044._1.NamedPersonIdType.class,
                        // * ch.ech.xmlns.ech_0007._4.CantonAbbreviationType.class
                        // */ };
                        // String file = jaxb.marshal(message, false, false, addClasses);
                        File xmlMarshaled = marshallCompleteMessage(message);
                        JadeSedexService.getInstance().sendSimpleMessage(xmlMarshaled.getAbsolutePath(), null);
                    }
                } catch (Exception e) {

                    errors = new StringBuffer();
                    try {
                        restoreEtatAnnonces(updatedAnnonces);
                    } catch (Exception e1) {
                        errors = errors.append(e.getMessage());
                        JadeLogger.error(this, errors.toString());
                    }

                    errors = errors.append(e.getMessage());
                    JadeLogger.error(this, errors.toString());

                    if (e instanceof JAXBValidationError) {
                        errors.append("\n");
                        for (ValidationEvent event : ((JAXBValidationError) e).getEvents()) {
                            errors.append(" - ").append(event.getMessage()).append("\n");
                        }
                    }

                    try {
                        sendMailError(errors, message.getHeader().getRecipientId().get(0));
                    } catch (Exception e2) {
                        JadeLogger.error(this, "Erreur pendant l'envoi d'un e-mail : " + e.getMessage());
                        // Exception levée si le mail n'a pas pu être envoyé. On ne peux rien faire de plus mais les
                        // messages
                        // d'erreur sont enregistrés dans le log
                    }
                }
            }
        }
    }

    private File marshallCompleteMessage(Message message) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL url = getClass().getResource(XSD_FOLDER + XSD_FILE_NAME);

        Schema schema = sf.newSchema(url);
        JAXBContext jc = JAXBContext.newInstance(Message.class);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);

        String filename = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addFilenameSuffixUID(MARSHALLED_XML);

        File f = new File(filename);
        if (!f.createNewFile()) {
            throw new Exception("Echec lors de la génération du fichier d'échange");
        }

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    // logger.warn("JAXB validation error : " + event.getMessage(), this);
                    JadeLogger.error(this, "JAXB validation error : " + event.getMessage());
                    return false;
                }
            });
            marshaller.marshal(message, f);

        } catch (JAXBException exception) {
            // logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            JadeLogger.error(this, "JAXB validation has thrown a JAXBException : " + exception.toString());
            throw exception;
        }
        return f;
    }
}