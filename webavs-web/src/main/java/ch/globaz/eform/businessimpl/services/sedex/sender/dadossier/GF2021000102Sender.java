package ch.globaz.eform.businessimpl.services.sedex.sender.dadossier;

import ch.globaz.eform.businessimpl.services.sedex.constant.GFActionSedex;
import ch.globaz.eform.businessimpl.services.sedex.constant.GFMessageTypeSedex;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierAttachmentElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierHeaderElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierSender;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.AttachmentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.ContentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.ExtensionType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class GF2021000102Sender extends GFDaDossierSender<Message> {
    @Override
    protected String getDocumentType() {
        return GFDocumentTypeDossier.valueOf(elements.get(GFDaDossierAttachmentElementSender.DOCUMENT_TYPE))
                .getDocumentType();
    }

    @Override
    protected String getDocumentTypeLead() {
        return GFDocumentTypeDossier.valueOf(elements.get(GFDaDossierAttachmentElementSender.DOCUMENT_TYPE))
                .getDocumentTypeLead();
    }

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
        header.setMessageType(GFMessageTypeSedex.TYPE_2021_TRANSFERE.getMessageType());
        header.setSubMessageType(GFMessageTypeSedex.TYPE_2021_TRANSFERE.getSubMessageType());
        header.setSubject(elements.get(GFDaDossierHeaderElementSender.SUBJECT));
        header.setComment("");
        header.setMessageDate(getDocumentDate());
        header.setAction(GFActionSedex.REPONSE.getCode().toString());
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
        //Le docuement principal est forcéemnt un
        if (this.leadingAttachment == null) { throw new IllegalArgumentException("Lettre de transfère manquante!"); }
        if (!this.attachments.entrySet().stream()
                .allMatch(entry -> StringUtils.endsWith(entry.getKey(), "pdf") || StringUtils.endsWith(entry.getKey(), "tiff")))  { throw new IllegalArgumentException("Type de document attaché non conforme!"); }

        List<AttachmentType> attachments = new ArrayList<>();

        Path leadingFile = Paths.get(this.leadingAttachment.getPath());

        AttachmentType leadingAttachmentType = new AttachmentType();
        leadingAttachmentType.setTitle(elements.get(GFDaDossierAttachmentElementSender.TITLE));
        leadingAttachmentType.setDocumentType(getDocumentTypeLead());
        leadingAttachmentType.setDocumentDate(getDocumentDate());
        leadingAttachmentType.setLeadingDocument(true);
        leadingAttachmentType.setSortOrder(BigInteger.ONE);
        leadingAttachmentType.setDocumentFormat(getDocumentFormat(leadingFile));
        leadingAttachmentType.getFile().addAll(createAttachmentFileType(Collections.singletonList(leadingFile)));
        attachments.add(leadingAttachmentType);

        Map<String, List<Path>> attachmentsGroup = new HashMap<>();

        this.attachments.forEach((fileName, path) -> {
            String extension = FilenameUtils.getExtension(fileName);
            if ("tiff".equalsIgnoreCase(extension)) {
                String extractName = extractName(fileName) + "." + extension;
                if (!attachmentsGroup.containsKey(extractName)) {
                    attachmentsGroup.put(extractName, new ArrayList<>());
                }

                attachmentsGroup.get(extractName).add(Paths.get(path));
            } else {
                attachmentsGroup.put(fileName, Collections.singletonList(Paths.get(path)));
            }
        });

        BigInteger counter = BigInteger.valueOf(2);
        attachmentsGroup.forEach((key, value) -> {
            AttachmentType attachmentType = new AttachmentType();
            attachmentType.setTitle(key);
            attachmentType.setDocumentType(getDocumentType());
            attachmentType.setDocumentDate(getDocumentDate());
            attachmentType.setLeadingDocument(false);
            attachmentType.setSortOrder(counter);
            attachmentType.setDocumentFormat(getDocumentFormat(key));
            attachmentType.getFile().addAll(createAttachmentFileType(value));
            attachments.add(attachmentType);
            counter.add(BigInteger.ONE);
        });

        return attachments;
    }

    private String extractName(String fileName) {
        String fullName = FilenameUtils.removeExtension(fileName);
        Matcher mPage = page.matcher(fullName);
        Matcher mMulti;
        if (mPage.find()) {
            mMulti = multi.matcher(mPage.group(1));
        } else {
            mMulti = multi.matcher(fullName);
        }
        String extractName;
        if (mMulti.find()) {
            extractName = mMulti.group(1);
        } else {
            extractName = fullName;
        }

        return extractName;
    }

    private String getDocumentFormat(Path file) {
        return getDocumentFormat(file.getFileName().toString());
    }
    private String getDocumentFormat(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
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
