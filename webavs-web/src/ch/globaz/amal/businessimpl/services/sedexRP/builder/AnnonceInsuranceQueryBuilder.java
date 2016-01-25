package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.gdk_cds.xmlns.pv_5202_000401._3.ContentType;
import ch.gdk_cds.xmlns.pv_5202_000401._3.HeaderType;
import ch.gdk_cds.xmlns.pv_5202_000401._3.InsuranceQueryType;
import ch.gdk_cds.xmlns.pv_5202_000401._3.Message;
import ch.gdk_cds.xmlns.pv_5202_000401._3.ObjectFactory;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;

/**
 * Fabrique pour les annonces 'Demande du rapport d'assurance'
 * 
 * @author cbu
 * @version XSD _1
 * 
 */
public class AnnonceInsuranceQueryBuilder extends AnnonceBuilderAbstract {
    private AnnonceInfosContainer annonceInfos = null;

    public AnnonceInsuranceQueryBuilder(SimpleAnnonceSedex simpleAnnonceSedex) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
    }

    public AnnonceInsuranceQueryBuilder(SimpleAnnonceSedex simpleAnnonceSedex, AnnonceInfosContainer annonceInfos) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
        this.annonceInfos = annonceInfos;
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

        message.setMinorVersion(new BigInteger("0"));
        message.setContent((ContentType) annonceContent);
        message.setHeader((HeaderType) annonceHeader);

        return message;
    }

    @Override
    public Object generateContent() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {

        ObjectFactory of = new ObjectFactory();
        ContentType contentType = of.createContentType();
        InsuranceQueryType insuranceQueryType = of.createInsuranceQueryType();

        insuranceQueryType.setPerson(getPersonType());
        insuranceQueryType.setBeginMonth(toXmlDate(annonceInfos.getPeriodeFrom(), true));
        insuranceQueryType.setEndMonth(toXmlDate(annonceInfos.getPeriodeTo(), true));

        contentType.setInsuranceQuery(insuranceQueryType);

        return contentType;
    }

    /**
     * Génère l'en-tête de l'annonce
     * 
     * @return l'en-tête initialisée
     * 
     * @throws JadeSedexDirectoryInitializationException
     *             Exception levée si dépôt Sedex ne peut être trouvé
     * @throws DatatypeConfigurationException
     *             Exception levée si l'en-tête ne peut être initialisée
     * @throws ALAnnonceRafamException
     *             Exception levée si une erreur métier se produit
     */
    @Override
    public Object generateHeader() throws JadeSedexDirectoryInitializationException, DatatypeConfigurationException {
        // try {
        String messageType = simpleAnnonceSedex.getMessageType();
        String messageSubType = JadeStringUtil.fillWithZeroes(simpleAnnonceSedex.getMessageSubType(), 6);
        String recipientId = annonceInfos.getRecipientId();

        ObjectFactory of = new ObjectFactory();
        HeaderType header = of.createHeaderType();

        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        header.setRecipientId(recipientId);
        header.setMessageId(JadeUUIDGenerator.createStringUUID());
        header.setBusinessProcessId(new BigInteger("1"));
        header.setMessageType(messageType);
        header.setSubMessageType(messageSubType);

        header.setSendingApplication(getSendingApplicationType());

        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar nowDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        header.setMessageDate(nowDateTime);
        header.setAction(IAMSedex.MESSAGE_ACTION_DEMANDE);
        header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());

        header.setDeclarationLocalReference(getDeclarationLocalReferenceType());

        return header;
    }

    @Override
    public String getAction(Object message) {
        return ((Message) message).getHeader().getAction();
    }

    @Override
    public String getBusinessProcessId(Object message) {
        return ((Message) message).getHeader().getBusinessProcessId().toString();
    }

    @Override
    public ByteArrayOutputStream getContentMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return os;
    }

    @Override
    public String getDecreeId(Object message) {
        // On utilise le message Id comme decreeId
        return getMessageId(message);
    }

    @Override
    public ByteArrayOutputStream getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return os;
    }

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
    public Object send(Object header, Object content) throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class whichClass() {
        return Message.class;
    }
}
