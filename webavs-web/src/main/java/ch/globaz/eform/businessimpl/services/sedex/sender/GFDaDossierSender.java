package ch.globaz.eform.businessimpl.services.sedex.sender;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.businessimpl.services.sedex.generator.GFIdentifiantGeneratorDefault;
import ch.globaz.eform.businessimpl.services.sedex.generator.GFIdentifiantSedexGenerator;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.AttachmentFileType;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.ContactInformationType;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import eform.ch.ech.xmlns.ech_0044_f._4.DatePartiallyKnownType;
import eform.ch.ech.xmlns.ech_0058._4.SendingApplicationType;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.sedex.JadeSedexService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class GFDaDossierSender<T> {

    protected final Pattern page = Pattern.compile("(.*)(_[pP][1-9]+)$");
    protected final Pattern multi = Pattern.compile("(.*)(_[pP][1-9]+)$");

    protected Map<GFDaDossierElementSender, String> elements = new HashMap<>();
    protected Map<String, String> attachments = new HashMap<>();
    protected GFAttachment leadingAttachment;
    protected GFIdentifiantSedexGenerator identifiantGenerator = new GFIdentifiantGeneratorDefault();

    protected abstract String getDocumentType();
    protected abstract String getDocumentTypeLead();

    private DatePartiallyKnownType getDate(String stringDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = simpleDateFormat.parse(stringDate);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);


            DatePartiallyKnownType datePartiallyKnownType = new DatePartiallyKnownType();
            datePartiallyKnownType.setYearMonth(xmlGregorianCalendar);
            datePartiallyKnownType.setYearMonthDay(xmlGregorianCalendar);
            datePartiallyKnownType.setYear(xmlGregorianCalendar);
            return datePartiallyKnownType;
        } catch (ParseException | DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSender() {
        //génération des id si nécessaires
        if (elements.containsKey(GFDaDossierHeaderElementSender.MESSAGE_ID)) {
            elements.put(GFDaDossierHeaderElementSender.MESSAGE_ID, identifiantGenerator.generateMessageId());
        }
        if (elements.containsKey(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID)) {
            elements.put(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID, identifiantGenerator.generateBusinessReferenceId());
        }
    }

    private Map<String, String> prepareAttachment() {
        return this.attachments.entrySet().stream()
                .peek(entry -> convertToPdfA(Paths.get(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Path convertToPdfA(Path pdfSource) {
        /*
        Path pathPdfConverted = Paths.get( Jade.getInstance().getPersistenceDir() + File.separator + "converted" + File.separator + pdfSource.getFileName());

        PdfStandardsConverter converter = new PdfStandardsConverter(pdfSource.toAbsolutePath().toString());

        converter.toPdfA3B(pathPdfConverted.toAbsolutePath().toString());

        try {
            Files.delete(pdfSource);
            Files.move(pathPdfConverted, pdfSource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */

        return pdfSource;
    }

    //Todo péréniser pour tout WebAVS (Tache technique)
    protected SendingApplicationType getSendingApplication() {
        SendingApplicationType sendingApplication = new SendingApplicationType();

        sendingApplication.setManufacturer("Globaz SA");
        sendingApplication.setProduct("WebAVS");
        sendingApplication.setProductVersion("1.29.0");

        return sendingApplication;
    }

    protected List<AttachmentFileType> createAttachmentFileType(List<Path> attachments) {
        BigInteger internalOrder = BigInteger.ONE;
        return attachments.stream()
                .map(attachment -> {
                    AttachmentFileType attachmentFileType = new AttachmentFileType();
                    attachmentFileType.setPathFileName("attachments/" + attachment.getFileName().toString());
                    attachmentFileType.setInternalSortOrder(internalOrder);
                    internalOrder.add(BigInteger.ONE);
                    return attachmentFileType;
                }).collect(Collectors.toList());
    }

    protected NaturalPersonsOASIDIType createInsuredPerson() {
        NaturalPersonsOASIDIType naturalPersonsOASIDIType = new NaturalPersonsOASIDIType();
        naturalPersonsOASIDIType.setOfficialName(elements.get(GFDaDossierInsuredPersonElementSender.OFFICIAL_NAME));
        naturalPersonsOASIDIType.setFirstName(elements.get(GFDaDossierInsuredPersonElementSender.FIRST_NAME));
        naturalPersonsOASIDIType.setDateOfBirth(getDate(elements.get(GFDaDossierInsuredPersonElementSender.DATE_OF_BIRT)));
        naturalPersonsOASIDIType.setVn(Long.valueOf(NSSUtils.unFormatNss(elements.get(GFDaDossierInsuredPersonElementSender.VN))));
        return naturalPersonsOASIDIType;
    }

    protected ContactInformationType createContactInformation() {
        ContactInformationType contactInformationType = new ContactInformationType();

        contactInformationType.setDepartment(elements.get(GFDaDossierContactInformationElementSender.DEPARTEMENT));
        contactInformationType.setName(elements.get(GFDaDossierContactInformationElementSender.NAME));
        contactInformationType.setEmail(elements.get(GFDaDossierContactInformationElementSender.EMAIL));
        contactInformationType.setPhone(elements.get(GFDaDossierContactInformationElementSender.PHONE));
        return contactInformationType;

    }

    protected abstract T createMessage();

    public String generateMessage() throws Exception{
        T message = createMessage();
        JAXBServices jaxb = JAXBServices.getInstance();
        return jaxb.marshal(message, false, false);
    }

    public void setIdentifiantGenerator(GFIdentifiantSedexGenerator identifiantGenerator) {
        this.identifiantGenerator = identifiantGenerator;
    }

    public void setElement(GFDaDossierElementSender element, Object value){
        elements.put(element, value.toString());
    }

    public void setElements(Map<GFDaDossierElementSender, Object> elements){
        elements.forEach((key, value) -> this.elements.put(key, value.toString()));
    }

    public void addAttachment(String fileName, String path) {
        attachments.put(fileName, path);
    }

    public void addAttachment(Path attachment) {
        attachments.put(attachment.getFileName().toString(), attachment.toAbsolutePath().toString());
    }

    public void addAttachments(Map<String, String> attachments) {
        this.attachments.putAll(attachments);
    }

    public void addAttachments(List<Path> attachments) {
        attachments.forEach(attachment -> this.attachments.put(attachment.getFileName().toString(), attachment.toAbsolutePath().toString()));
    }

    public void setLeadingAttachment(String fileName, String path) {
        this.leadingAttachment = new GFAttachment(fileName, path);
    }

    public void setLeadingAttachment(Path path) {
        this.leadingAttachment = new GFAttachment(path.getFileName().toString(), path.toAbsolutePath().toString());
    }

    public GFIdentifiantSedexGenerator getIdentifiantGenerator() {
        return identifiantGenerator;
    }

    public void send() {
        try {
            JadeSedexService.getInstance().sendSimpleMessage(generateMessage(), prepareAttachment());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
