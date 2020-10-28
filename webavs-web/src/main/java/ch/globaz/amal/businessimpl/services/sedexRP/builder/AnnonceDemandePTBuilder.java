package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import ch.gdk_cds.xmlns.pv_5205_000801._3.*;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.GregorianCalendar;

/**
 * Fabrique pour les annonces 'Demande de prime tarifaire'
 *
 * @author sco
 */
public class AnnonceDemandePTBuilder extends AnnonceBuilderAbstract {

    /**
     * Constructeur de la fabrique des demandes de prime tarifaire
     * @param simpleAnnonceSedex
     */
    public AnnonceDemandePTBuilder(SimpleAnnonceSedex simpleAnnonceSedex) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
    }

    @Override
    public Object createMessage(Object annonceHeader, Object annonceContent) {
        if (annonceHeader instanceof Message) {
            annonceHeader = ((Message) annonceHeader).getHeader();
        }

        if (annonceContent instanceof Message) {
            annonceContent = ((Message) annonceContent).getContent();
        }

        ObjectFactory of = new ObjectFactory();
        Message message = of.createMessage();

        message.setMinorVersion(IAMSedex.MESSAGE_MINOR_VERSION);
        message.setContent((ContentType) annonceContent);
        message.setHeader((HeaderType) annonceHeader);

        return message;
    }

    @Override
    public Object generateContent() throws AnnonceSedexException, JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        simpleDetailFamille = getSubside();

        ObjectFactory of = new ObjectFactory();
        ContentType contentType = of.createContentType();

        PremiumQueryType premiumQueryType = of.createPremiumQueryType();

        XMLGregorianCalendar xmlCalendarEventDate = null;
        try {
            xmlCalendarEventDate = JAXBUtil.getXmlCalendarTimestamp();
            xmlCalendarEventDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            premiumQueryType.setStatementDate(xmlCalendarEventDate);
        } catch (DatatypeConfigurationException e) {
            throw new AnnonceSedexException("Imposible de définir la date d'émission de la demande de prime tarifaire", e);
        }

        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlCal = factory.newXMLGregorianCalendarDate(Integer.parseInt(simpleDetailFamille.getAnneeHistorique()), DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                    DatatypeConstants.FIELD_UNDEFINED);
            premiumQueryType.setYear(xmlCal);
        } catch (DatatypeConfigurationException e) {
            throw new AnnonceSedexException("Imposible de définir l'année de la demande de prime tarifaire", e);
        }

        premiumQueryType.setSpecificSelectionBoolean(true);
        premiumQueryType.getPerson().add(getPersonType());

        contentType.setPremiumQuery(premiumQueryType);

        return contentType;
    }

    @Override
    public Object generateHeader() throws AnnonceSedexException, JadeSedexDirectoryInitializationException, DatatypeConfigurationException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ObjectFactory of = new ObjectFactory();
        HeaderType header = of.createHeaderType();

        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        header.setRecipientId(AMSedexRPUtil.getSedexIdFromIdTiers(simpleDetailFamille.getNoCaisseMaladie()));
        header.setMessageId(JadeUUIDGenerator.createStringUUID());

        header.setBusinessProcessId(getNextBusinessProcessId());

        header.setMessageType(simpleAnnonceSedex.getMessageType());
        header.setSubMessageType(JadeStringUtil.fillWithZeroes(simpleAnnonceSedex.getMessageSubType(), 6));

        header.setSendingApplication(getSendingApplicationType());

        XMLGregorianCalendar xmlCalendarEventDate = null;
        try {
            xmlCalendarEventDate = JAXBUtil.getXmlCalendarTimestamp();
            xmlCalendarEventDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            header.setMessageDate(xmlCalendarEventDate);
        } catch (DatatypeConfigurationException e) {
            throw new AnnonceSedexException("Imposible de définir la date d'émission de la demande de prime tarifaire", e);
        }


        header.setAction(IAMSedex.MESSAGE_ACTION_DEMANDE);
        header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());

        header.setDeclarationLocalReference(getDeclarationLocalReferenceType());

        return header;
    }


    @Override
    public ByteArrayOutputStream getContentMessage(Object message) throws MalformedURLException, JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        return os;
    }



    @Override
    public ByteArrayOutputStream getHeaderMessage(Object message) throws MalformedURLException, JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});

        return os;
    }

    @Override
    public Object send(Object header, Object content) throws JadeApplicationException, JadePersistenceException {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader((HeaderType) header);
        m.setContent((ContentType) content);

        return m;
    }

    @Override
    public String getDecreeId(Object message) { return simpleAnnonceSedex.getNumeroDecision(); }

    @Override
    public String getBusinessProcessId(Object message) { return ((Message) message).getHeader().getBusinessProcessId().toString(); }

    @Override
    public String getAction(Object message) { return ((Message) message).getHeader().getAction(); }

    @Override
    public String getMessageId(Object message) {
        return ((Message) message).getHeader().getMessageId();
    }

    @Override
    public String getMinorVersion(Object message) {
        return ((Message) message).getMinorVersion().toString();
    }

    @Override
    public String getRecipientId(Object message) {
        return ((Message) message).getHeader().getRecipientId();
    }

    @Override
    public String getSenderId(Object message) {
        return ((Message) message).getHeader().getSenderId();
    }

    @Override
    public Class whichClass() {
        return Message.class;
    }
}
