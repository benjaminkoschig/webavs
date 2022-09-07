package ch.globaz.eform.businessimpl.services.sedex.envoi;

import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.AttachmentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.ContentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.ExtensionType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.HeaderType;

import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.AttachmentFileType;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.ContactInformationType;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import eform.ch.ech.xmlns.ech_0044_f._4.DatePartiallyKnownType;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnvoiSedexService {

    private GFEnvoiViewBean viewBean;
    private Sedex000102 sedex000102 = new Sedex000102();

    public EnvoiSedexService(GFEnvoiViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public void createSedexMessage() throws DatatypeConfigurationException {
        Sedex000102 sedex0001021 = new Sedex000102();
        Message message = sedex0001021.createMessage(createHeader(), createContent());
    }

    private HeaderType createHeader() throws DatatypeConfigurationException {
        HeaderType header = new HeaderType();
//        header.setSenderId();  info de la demande en db
//        header.setRecipientId(); info de la demande en db
//        header.setMessageId(); info de la demande en db
//        header.setReferenceMessageId(); info de la demande en db
//        header.setBusinessProcessId(); a créer
//        header.setOurBusinessReferenceId(); a créer
//        header.setYourBusinessReferenceId(); info de la demande en db
        header.setMessageType(SedexType2021Enum.TYPE_102.getMessageType());
        header.setSubMessageType(SedexType2021Enum.TYPE_102.getSubMessageType());
        header.setSendingApplication(sedex000102.getSendingApplicationType());
//        header.setSubject(); info de la demande en db
        header.setMessageDate(getDocumentDate());
        header.setAction(SedexType2021Enum.TYPE_102.getAction());
        header.getAttachment().addAll(getAttachmentTypeList());
        header.setTestDeliveryFlag(SedexType2021Enum.TYPE_102.isTestDeliveryFlag());
        header.setResponseExpected(SedexType2021Enum.TYPE_102.isResponseExpected());
        header.setBusinessCaseClosed(SedexType2021Enum.TYPE_102.isBusinessCaseClosed());
        header.setExtension(getExtensionType());
        return header;
    }

    private List<AttachmentType> getAttachmentTypeList() throws DatatypeConfigurationException {
        List<String> fileNameList = viewBean.getFileNameList();
        List<String> tiffFileNameList = getTiffFilesNameList(fileNameList);
        List<AttachmentType> attachmentTypeList = new ArrayList<>();

        if (tiffFileNameList.size() < 1) {
            for (String fileName : fileNameList) {
                attachmentTypeList.add(createAttachment(fileName,getAttachementFileTypeList(fileName)));
            }
        } else {
            Map<String, List<String>> multipleTiffFiles = findMultipleTiffFiles(tiffFileNameList);
            attachmentTypeList = getMultipleAttachmentTypeList(multipleTiffFiles);
        }
        return attachmentTypeList;

    }

    private List<String> getTiffFilesNameList(List<String> fileName) {
        return fileName.stream().filter(e -> FilenameUtils.getExtension(e).equals("tiff")).collect(Collectors.toList());
    }

    private Map<String, List<String>> findMultipleTiffFiles(List<String> tiffFileNameList) {
        // les fichiers tiff multiple ont un numero de page avant l'extension par exemple : test_p1.tiff , test_p2.tiff
        Map<String, List<String>> multiTiffFilesMap = new HashMap<>();
        for (String fileName : tiffFileNameList) {
            String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
            String fileNameWithoutPage = fileNameWithoutExtension.substring(0, fileNameWithoutExtension.length() - 3);
            if (fileNameWithoutExtension.charAt(fileNameWithoutExtension.length() - 2) == 'p' && multiTiffFilesMap.containsKey(fileNameWithoutPage)) {
                List<String> test = multiTiffFilesMap.get(fileNameWithoutPage);
                test.add(fileName);
                multiTiffFilesMap.put(fileNameWithoutPage, test);
            } else {
                List<String> tempArrayList = new ArrayList<>();
                tempArrayList.add(fileName);
                multiTiffFilesMap.put(fileNameWithoutPage, tempArrayList);

            }
        }
        return multiTiffFilesMap;
    }

    private List<AttachmentType> getMultipleAttachmentTypeList(Map<String, List<String>> multipleTiffFiles) throws DatatypeConfigurationException {
        List<AttachmentType> attachmentTypeList = new LinkedList<>();

        for (String key : multipleTiffFiles.keySet()) {
           attachmentTypeList.add(createAttachment(key,getMultipleAttachementFileTypeList(multipleTiffFiles.get(key))));
        }
        return attachmentTypeList;
    }
    private AttachmentType createAttachment(String fileName, List<AttachmentFileType> attachmentTypeList) throws DatatypeConfigurationException {
        AttachmentType attachmentType = new AttachmentType();
        attachmentType.setTitle("titre de test");
        attachmentType.setDocumentDate(getDocumentDate());
        attachmentType.setLeadingDocument(SedexType2021Enum.TYPE_102.isLeadingDocument());
        attachmentType.setSortOrder(new BigInteger(String.valueOf(SedexType2021Enum.TYPE_102.getOrder())));
        attachmentType.setDocumentFormat(FilenameUtils.getExtension(fileName));
        attachmentType.setDocumentType("01.10.11");
        attachmentType.getFile().addAll(attachmentTypeList);
        return attachmentType;
    }


    private XMLGregorianCalendar getDocumentDate() throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        return xmlGregorianCalendar;
    }

    private List<AttachmentFileType> getAttachementFileTypeList(String fileName) {
        List<AttachmentFileType> attachmentFileTypesList = new ArrayList<>();
       attachmentFileTypesList.add(getAttachmentFileType(fileName,1));
       return attachmentFileTypesList;

    }

    private List<AttachmentFileType> getMultipleAttachementFileTypeList(List<String> fileNameList) {
//        int sortOrder = 1;
        List<AttachmentFileType> attachmentFileTypesList = new ArrayList<>();
        int sortOrder = 1;

        for (String fileName : fileNameList) {
            attachmentFileTypesList.add(getAttachmentFileType(fileName,sortOrder));
            sortOrder++;
        }
        return attachmentFileTypesList;
    }

    private AttachmentFileType getAttachmentFileType(String fileName, int sortOrder){
        AttachmentFileType attachmentFileType = new AttachmentFileType();
        attachmentFileType.setPathFileName(fileName);
        attachmentFileType.setInternalSortOrder(BigInteger.valueOf(sortOrder));
        return  attachmentFileType;
    }

    public ExtensionType getExtensionType() {
        ExtensionType extensionType = new ExtensionType();
        extensionType.setContactInformation(getContactInformationType());
        return extensionType;
    }

    private ContactInformationType getContactInformationType() {
        ContactInformationType contactInformationType = new ContactInformationType();
        contactInformationType.setDepartment(viewBean.getNomDepartement());
        contactInformationType.setName(viewBean.getNomGestionnaire());
        contactInformationType.setEmail(viewBean.getEmailGestionnaire());
        contactInformationType.setPhone(viewBean.getTelephoneGestionnaire());
        return contactInformationType;

    }

    private ContentType createContent() {
        ContentType contentType = new ContentType();
        contentType.setPensionRecipient(SedexType2021Enum.TYPE_102.isPensionRecipient());
        contentType.setInsuredPerson(getNaturalPersonsOASIDIType());
        return contentType;
    }

    private NaturalPersonsOASIDIType getNaturalPersonsOASIDIType() {
        NaturalPersonsOASIDIType naturalPersonsOASIDIType = new NaturalPersonsOASIDIType();
        naturalPersonsOASIDIType.setOfficialName(viewBean.getNomAssure());
        naturalPersonsOASIDIType.setFirstName(viewBean.getPrenomAssure());
        naturalPersonsOASIDIType.setDateOfBirth(getDateOfBirth(viewBean.getDateNaissance()));
        naturalPersonsOASIDIType.setVn(Long.valueOf(StringUtils.replace(viewBean.getNss(), ".", "")));
        return naturalPersonsOASIDIType;
    }

    private DatePartiallyKnownType getDateOfBirth(String dateDeNaissance) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD.MM.YYYY");
            Date date = simpleDateFormat.parse(dateDeNaissance);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);


            DatePartiallyKnownType datePartiallyKnownType = new DatePartiallyKnownType();
            datePartiallyKnownType.setYearMonth(xmlGregorianCalendar);
            datePartiallyKnownType.setYearMonthDay(xmlGregorianCalendar);
            datePartiallyKnownType.setYear(xmlGregorianCalendar);

            return datePartiallyKnownType;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
