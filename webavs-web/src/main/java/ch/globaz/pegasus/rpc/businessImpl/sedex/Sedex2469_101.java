package ch.globaz.pegasus.rpc.businessImpl.sedex;

import globaz.jade.client.util.JadeUUIDGenerator;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ContentType;
import ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.HeaderType;
import ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.Message;
import ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ObjectFactory;
import ch.ech.xmlns.ech_0058._5.SendingApplicationType;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class Sedex2469_101 extends AbstractSedex<Message> implements SedexMessageSender<ContentType> {

    private static final String SUB_MESSAGE_TYPE = "000101";
    private static final int MESSAGE_TYPE = 2469;
    private static final String SUBJECT = MESSAGE_TYPE + "/" + SUB_MESSAGE_TYPE + " - ";
    private static final Logger LOG = LoggerFactory.getLogger(Sedex2469_101.class);

    private HeaderType validationHeader;
    private final String recipientId;
    private final SendingApplicationType applicationType;
    private final ObjectFactory factory = new ObjectFactory();

    private AtomicInteger incrementalFilename = new AtomicInteger();

    public Sedex2469_101(SedexSender sedexSender, InfoCaisse infoCaisse, SendingApplicationType applicationType,
            String directoryName) {
        super(sedexSender, directoryName, infoCaisse, Message.class, "eahv-iv-2469-000101-1-5.xsd");
        recipientId = sedexSender.getRecipientId();
        this.applicationType = applicationType;
    }

    @Override
    public void validate(ContentType content) throws ValidationException {
        if (content == null) {
            throw new CommonTechnicalException("Content is null unable to validate!");
        }

        if (validationHeader == null) {
            validationHeader = createHeader(MESSAGE_TYPE, SUB_MESSAGE_TYPE, "Nom, Prénom");
        }

        Message message = createMessage(validationHeader, content);

        validateByXsd(message);
    }

    @Override
    public File generateMessageFile(ContentType content, String nom, String prenom) throws ValidationException {
        int i = incrementalFilename.incrementAndGet();
        String filename = sedexInfo.getPersistenceDir() + File.separator + MESSAGE_TYPE + FILENAME_SEPARATOR + "101"
                + FILENAME_SEPARATOR + infoCaisse.getNumeroCaisseFormate() + FILENAME_SEPARATOR
                + String.format("%05d", i) + MARSHALLED_XML_EXT;
        HeaderType header = createHeader(MESSAGE_TYPE, SUB_MESSAGE_TYPE, nom + ", " + prenom);
        Message message = createMessage(header, content);
        return marshallCompleteMessage(message, filename);
    }

    @Override
    public void sendMessages() {
        this.sendMessages(MESSAGE_TYPE);
    }

    private Message createMessage(HeaderType header, ContentType content) {
        Message message = factory.createMessage();
        message.setHeader(header);
        message.setContent(content);
        message.setMinorVersion(IMPLEMENTED_MESSAGE_MINOR_VERSION);
        return message;
    }

    private HeaderType createHeader(int messageType, String subMessageType, String nomPrenom) {
        HeaderType header = new HeaderType();

        header.setAction("1");

        header.setMessageDate(toDay());
        header.setMessageType(String.valueOf(messageType));

        header.setRecipientId(recipientId);
        header.setSubMessageType(subMessageType);
        header.setSenderId(sedexInfo.getSedexSenderId());
        header.setTestDeliveryFlag(sedexInfo.getTestDeliveryFlag());

        header.setSubject(SUBJECT + nomPrenom);

        header.setMessageId(JadeUUIDGenerator.createLongUID().toString());

        header.setSendingApplication(applicationType);
        return header;
    }

    public SedexInfo getSedexInfo() {
        return sedexInfo;
    }

}
