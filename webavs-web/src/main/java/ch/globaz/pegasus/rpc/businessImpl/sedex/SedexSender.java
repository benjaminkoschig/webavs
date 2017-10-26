package ch.globaz.pegasus.rpc.businessImpl.sedex;

import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;
import ch.ech.xmlns.ech_0090._1.EnvelopeType;
import ch.ech.xmlns.ech_0090._1.ObjectFactory;
import ch.globaz.common.exceptions.CommonTechnicalException;

public class SedexSender {
    private final JadeSedexService jadeSedexService;
    private final String recipientId;
    private final String persistenceDir;
    private final String sedexSenderId;
    private final boolean deliveryFlag;

    public SedexSender(JadeSedexService jadeSedexService, String recipientId, String persistenceDir) {
        this.jadeSedexService = jadeSedexService;
        this.recipientId = recipientId;
        this.persistenceDir = persistenceDir;
        try {
            sedexSenderId = jadeSedexService.getSedexDirectory().getLocalEntry().getId();
            deliveryFlag = jadeSedexService.getTestDeliveryFlag();
        } catch (Exception e) {
            throw new CommonTechnicalException("Unabled to initialise sedex", e);
        }
    }

    public SedexSender(String recipientId, String persistenceDir) {
        this.recipientId = recipientId;
        this.persistenceDir = persistenceDir;
        sedexSenderId = "";
        deliveryFlag = true;
        jadeSedexService = null;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public SedexInfo buildSedexInfos(String directoryName) {
        String patFile = createPathIfNotExist(directoryName);
        return new SedexInfo(sedexSenderId, deliveryFlag, patFile);
    }

    public String createPathIfNotExist(String dir) {
        File f = new File(persistenceDir, dir + JadeUUIDGenerator.createStringUUID());
        if (!f.exists()) {
            f.mkdir();
        }
        return f.getAbsolutePath();
    }

    /**
     * Réalise l'envoi des fichier XML marshallés
     * 
     * @param files
     * @throws JadeSedexMessageNotSentException
     */
    public void sendMessages(SedexInfo sedexInfo, int msgType) {
        File[] files = new File(sedexInfo.getPersistenceDir() + File.separator).listFiles();
        List<SimpleSedexMessage> list = new ArrayList<SimpleSedexMessage>();
        for (File file : files) {
            SimpleSedexMessage ssm = new SimpleSedexMessage();
            ssm.fileLocation = file.getAbsolutePath();
            // reuse filename (extention will be re-added by FW)
            ssm.increment = StringUtils.substring(file.getName(), 0, -4);
            list.add(ssm);
        }
        if (!list.isEmpty()) {
            try {
                jadeSedexService.sendGroupedMessage(generateEnveloppe(recipientId, msgType), list);
            } catch (Exception e) {
                throw new CommonTechnicalException(e);
            }
        }
        deletePathRPC(sedexInfo.getPersistenceDir());
    }

    private String generateEnveloppe(String recipientId, int msgType) throws JAXBValidationError,
            JAXBValidationWarning, JAXBException, SAXException, IOException, DatatypeConfigurationException,
            JadeSedexDirectoryInitializationException {
        String envelopeMessageId = JadeUUIDGenerator.createLongUID().toString();
        ObjectFactory of0090 = new ObjectFactory();
        EnvelopeType enveloppe = of0090.createEnvelopeType();
        enveloppe.setEventDate(JAXBUtil.getXmlCalendarTimestamp());
        enveloppe.setMessageDate(JAXBUtil.getXmlCalendarTimestamp());
        enveloppe.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        enveloppe.setMessageId(envelopeMessageId);
        enveloppe.setMessageType(msgType);
        enveloppe.setVersion("1.0");
        enveloppe.getRecipientId().add(recipientId);
        JAXBElement<EnvelopeType> element = of0090.createEnvelope(enveloppe);
        return JAXBServices.getInstance().marshal(element, true, true, new Class<?>[] {});
    }

    private void deletePathRPC(String path) {
        if (path != null && !path.isEmpty()) {
            File f = new File(path);
            FileUtils.deleteQuietly(f);
        }
    }
}
