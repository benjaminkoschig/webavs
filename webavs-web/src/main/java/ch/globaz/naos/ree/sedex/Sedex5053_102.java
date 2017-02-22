package ch.globaz.naos.ree.sedex;

import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.CombinationType;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.ContentType;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.DeclarationLocalReferenceType;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.HeaderType;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.HeaderType.PartialDelivery;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.Message;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.ObjectFactory;
import ree.ch.ech.xmlns.ech_0058._3.SendingApplicationType;
import ch.globaz.naos.ree.domain.pojo.ProcessProperties;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.protocol.SedexTechnicalProtocol;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.SedexInfo;
import ch.globaz.naos.ree.tools.WebAvsVersion;

public class Sedex5053_102 extends AbstractSedex implements SedexMessageSender {

    private static final String SUBJECT = "Livraison annuelle lien - ";
    private static final String XSD_FILE_NAME = "bfs-5053-000102-1-2.xsd";
    private final ProcessProperties properties;
    private final SedexInfo sedexInfo;
    private final Date dateEnvoi;
    private final String ourBusinessReferenceId;

    public Sedex5053_102(ProcessProperties properties, InfoCaisse infoCaisse, SedexInfo sedexInfo) {
        this.properties = properties;
        this.sedexInfo = sedexInfo;
        dateEnvoi = new Date();
        ourBusinessReferenceId = new SimpleDateFormat("yyyyMMdd").format(dateEnvoi)
                + infoCaisse.getNumeroCaisseFormate() + infoCaisse.getNumeroAgenceFormate();
    }

    @Override
    public SedexTechnicalProtocol marshallAndValidate(ProcessProtocolAndMessages processProtocolAndMessages)
            throws JadeSedexMessageNotSentException, MalformedURLException, JAXBValidationError, JAXBValidationWarning,
            JAXBException, SAXException, IOException, JadeSedexDirectoryInitializationException,
            DatatypeConfigurationException {
        Date startDate = new Date();
        List<?> jaxbMessages = processProtocolAndMessages.getBusinessMessages();

        int tailleLot = properties.getTailleLot();
        int nombreLot = calculerNombreLots(jaxbMessages, tailleLot);

        ObjectFactory factory = new ObjectFactory();
        Pagination pagination = null;
        List<File> files = new LinkedList<File>();
        for (int compteurLot = 0; compteurLot < nombreLot; compteurLot++) {
            int startIndex = compteurLot * tailleLot;
            int stopIndex = startIndex + tailleLot;
            // Si c'est le dernier lot et que sa taille < tailleLot
            if (stopIndex > jaxbMessages.size()) {
                stopIndex = jaxbMessages.size();
            }

            List<?> messageJaxbLot = jaxbMessages.subList(startIndex, stopIndex);
            pagination = new Pagination(tailleLot, nombreLot, compteurLot + 1);
            File f = generateMessageFile(5053, "000102", XSD_FILE_NAME, ourBusinessReferenceId, factory, pagination,
                    messageJaxbLot);
            files.add(f);
        }

        return new SedexTechnicalProtocol(startDate, new Date(), nombreLot, files);
    }

    private File generateMessageFile(int messageType, String subMessageType, String xsdFileName,
            String ourBusinessReferenceId, ObjectFactory factory, Pagination pagination, List<?> jaxbMessages)

    throws MalformedURLException, JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException,
            DatatypeConfigurationException, JadeSedexDirectoryInitializationException, JadeSedexMessageNotSentException {

        HeaderType header = factory.createHeaderType();

        String subjectDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        header.setAction("1");
        XMLGregorianCalendar xmlCalendar = JAXBUtil.getXmlCalendarTimestamp();
        xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        header.setEventDate(xmlCalendar);
        header.setMessageDate(xmlCalendar);
        header.setMessageType(messageType);
        header.setSubMessageType(subMessageType);
        header.getRecipientId().add(properties.getRecipientId());
        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        header.setOurBusinessReferenceId(ourBusinessReferenceId);
        header.setTestDeliveryFlag(sedexInfo.getTestDeliveryFlag());
        header.setSubject(SUBJECT + subjectDate);

        // Selon eCH-0058 : A utiliser pour subdiviser les grandes livraisons
        if (!pagination.hasOnlyOneLot()) {
            header.setComment(pagination.getCommentForPartialDelivery(ourBusinessReferenceId));

            PartialDelivery partialDelivery = new PartialDelivery();
            partialDelivery.setUniqueIdBusinessCase(ourBusinessReferenceId + messageType + subMessageType);
            partialDelivery.setTotalNumberOfPackages(new BigInteger(String.valueOf(pagination.getNombreLot())));
            partialDelivery.setNumberOfActualPackage(new BigInteger(String.valueOf(pagination.getLotCourant())));
            header.setPartialDelivery(partialDelivery);
        }

        header.setMessageId(JadeUUIDGenerator.createLongUID().toString());

        DeclarationLocalReferenceType x = new DeclarationLocalReferenceType();
        x.setDepartment(properties.getDepartment());
        x.setEmail(properties.getEmail());
        x.setName(properties.getName());
        x.setOther(properties.getOther());
        x.setPhone(properties.getPhone());
        header.setDeclarationLocalReference(x);

        SendingApplicationType at = new SendingApplicationType();
        at.setManufacturer("Globaz");
        at.setProduct("WebAVS");
        at.setProductVersion(WebAvsVersion.getVersion());
        header.setSendingApplication(at);

        ContentType content = factory.createContentType();

        for (Object masterDataType : jaxbMessages) {
            content.getCombination().add((CombinationType) masterDataType);
        }

        Message message = factory.createMessage();
        message.setHeader(header);
        message.setContent(content);
        message.setMinorVersion(MESSAGE_MINOR_VERSION_0);

        return marshallCompleteMessage(message, xsdFileName);
    }

    @Override
    public void validateUnitMessage(Object object) throws ValidationException, SAXException, JAXBException {
        if (!properties.isValidation()) {
            return;
        }
        throw new RuntimeException("Not implemented yet, don't use me..");
    }

}
