package ch.globaz.eform.businessimpl.services.sedex.sender.dadossier;

import ch.globaz.eform.businessimpl.services.sedex.constant.GFActionSedex;
import ch.globaz.eform.businessimpl.services.sedex.constant.GFMessageTypeSedex;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierAttachmentElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierHeaderElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierSender;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.AttachmentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.ContentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.ExtensionType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.Message;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GF2021000101Sender extends GFDaDossierSender<Message> {
    @Override
    protected Message createMessage(){
        Message message = new Message();

        try {
            message.setHeader(createHeader());
            message.setContent(createContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    private HeaderType createHeader() {
        HeaderType header = new HeaderType();

        header.setSenderId(elements.get(GFDaDossierHeaderElementSender.SENDER_ID));
        header.setRecipientId(elements.get(GFDaDossierHeaderElementSender.RECIPIENT_ID));
        header.setMessageId(elements.get(GFDaDossierHeaderElementSender.MESSAGE_ID));
        header.setOurBusinessReferenceId(elements.get(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID));
        header.setMessageType(GFMessageTypeSedex.TYPE_2021_DEMANDE.getMessageType());
        header.setSubMessageType(GFMessageTypeSedex.TYPE_2021_DEMANDE.getSubMessageType());
        header.setSubject(elements.get(GFDaDossierHeaderElementSender.SUBJECT));
        header.setComment("");
        header.setMessageDate(getDocumentDate());
        header.setAction(GFActionSedex.DEMANDE.getCode().toString());
        header.setTestDeliveryFlag(Boolean.parseBoolean(elements.get(GFDaDossierHeaderElementSender.TEST_DELIVERY_FLAG)));
        header.setResponseExpected(true);
        header.setBusinessCaseClosed(false);
        header.setSendingApplication(getSendingApplication());
        header.getAttachment().addAll(createAttachments());
        header.setExtension(createExtention());

        return header;
    }

    private ExtensionType createExtention() {
       ExtensionType extensionType = new ExtensionType();
        extensionType.setContactInformation(createContactInformation());
        return extensionType;
    }

    private ContentType createContent() {
        ContentType content = new ContentType();

        content.setInsuredPerson(createInsuredPerson());

        return content;
    }

    private List<AttachmentType> createAttachments() {
        //Il ne peut avoir qu'un seul document de type PDF
        if (this.attachments.size() != 1) { throw new IllegalArgumentException("Lettre de cession manquante!"); }
        if (!this.attachments.entrySet().stream().allMatch(entry -> StringUtils.endsWith(entry.getKey(), "pdf")))  { throw new IllegalArgumentException("Type de document non conforme!"); }

        List<AttachmentType> attachments = new ArrayList<>();

        this.attachments.forEach((filaName, path) -> {
            Path file = Paths.get(path);

            AttachmentType attachmentType = new AttachmentType();

            attachmentType.setTitle(elements.get(GFDaDossierAttachmentElementSender.TITLE));
            attachmentType.setDocumentType("01.01.01.01");
            attachmentType.setDocumentDate(getDocumentDate());
            attachmentType.setLeadingDocument(true);
            attachmentType.setSortOrder(BigInteger.ONE);
            attachmentType.setDocumentFormat(getDocumentFormat(file));
            attachmentType.getFile().addAll(createAttachmentFileType(Collections.singletonList(file)));

            attachments.add(attachmentType);
        });

        return attachments;
    }

    private String getDocumentFormat(Path file) {
        String extension = FilenameUtils.getExtension(file.getFileName().toString());
        if (StringUtils.equalsIgnoreCase("pdf", extension)) {
            return "application/pdf";
        } else if (StringUtils.equalsIgnoreCase("tif", extension)) {
            return "image/tiff";
        } else {
            return "";
        }
    }

    private XMLGregorianCalendar getDocumentDate() {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
