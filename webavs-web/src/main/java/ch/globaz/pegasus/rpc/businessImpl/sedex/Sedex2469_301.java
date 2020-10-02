package ch.globaz.pegasus.rpc.businessImpl.sedex;

import globaz.jade.client.util.JadeUUIDGenerator;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ContentType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.HeaderType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.Message;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ObjectFactory;
import rpc.ch.ech.xmlns.ech_0058._5.SendingApplicationType;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class Sedex2469_301 extends AbstractSedex<Message> implements SedexMessageSender<ContentType> {
    public static final int MESSAGE_TYPE = 2469;

    private static final Logger LOG = LoggerFactory.getLogger(Sedex2469_301.class);

    private static final String SUB_MESSAGE_TYPE = "000301";
    private static final String SUBJECT = "2469/000301 - ";
    private AtomicInteger incrementalFilename = new AtomicInteger();
    private final HeaderType validationHeader;
    private final String recipientId;
    private final SendingApplicationType applicationType;
    private final ObjectFactory factory = new ObjectFactory();
    private static final String RPC_XSD_301_FILENAME = "eahv-iv-2469-000301-2-1.xsd";

    public Sedex2469_301(SedexSender sedexSender, InfoCaisse infoCaisse, SendingApplicationType applicationType,
            String directoryName) {
        super(sedexSender, directoryName, infoCaisse, Message.class, RPC_XSD_301_FILENAME);
        recipientId = sedexSender.getRecipientId();
        this.applicationType = applicationType;
        validationHeader = createHeader(MESSAGE_TYPE, SUB_MESSAGE_TYPE, "Nom, Prénom");
    }

    @Override
    public void validate(ContentType contentType) throws ValidationException {
        if (contentType == null) {
            throw new CommonTechnicalException("ContentType is null unable to validate!");
        }

        LOG.info("Validation unitaire pour la decision id [{}]", contentType.getDecisionId());

        Message message = createMessage(validationHeader, contentType);
        validateByXsd(message);
    }

    @Override
    public File generateMessageFile(ContentType content, String nom, String prenom) throws ValidationException {
        int i = incrementalFilename.incrementAndGet();
        String filename = sedexInfo.getPersistenceDir() + File.separator + MESSAGE_TYPE + FILENAME_SEPARATOR + "301"
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

    @Override
    public SedexInfo getSedexInfo() {
        return sedexInfo;
    }
}