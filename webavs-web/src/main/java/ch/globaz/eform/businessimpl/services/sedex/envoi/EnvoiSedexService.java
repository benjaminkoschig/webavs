package ch.globaz.eform.businessimpl.services.sedex.envoi;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
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
import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnvoiSedexService {

    private GFEnvoiViewBean viewBean;
    private Sedex000102 sedex000102 = new Sedex000102();

    public EnvoiSedexService(GFEnvoiViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public void createSedexMessage() {
        try {
            Sedex000102 sedex0001021 = new Sedex000102();
            String id = "1";
            GFDaDossierModel model = getModel(id);
            Message message = sedex0001021.createMessage(createHeader(model), createContent());
            updateGFFormulaireStatus(model);

        } catch (Exception e) {
            sendMail();
        }
    }
    private HeaderType createHeader(GFDaDossierModel model) {
        //todo sprint 18 lier l'id avec une demande
        try {
//            String id = "2";
//            GFDaDossierModel model = getModel(id);
            HeaderType header = new HeaderType();
            //TODO sprint 18 mapper les 3 données commentées
//        header.setSenderId();
//        header.setRecipientId(getSedexId(model.getCodeCaisse())); a faire sprint 2022.18
//        header.setMessageId(); info généré soit par sm-client soit par nous a vérifier
            header.setReferenceMessageId(Objects.isNull(model) ? "" : model.getMessageId());
            header.setBusinessProcessId(generateBusinessProcessId());
            header.setOurBusinessReferenceId(Objects.isNull(model) ? UUID.randomUUID().toString() : model.getOurBusinessRefId());
            header.setYourBusinessReferenceId(Objects.isNull(model) ? "" : model.getYourBusinessRefId());
            header.setMessageType(SedexType2021Enum.TYPE_102.getMessageType());
            header.setSubMessageType(SedexType2021Enum.TYPE_102.getSubMessageType());
            header.setSendingApplication(sedex000102.getSendingApplicationType());
            header.setSubject(createHeaderSubject());
            header.setMessageDate(getDocumentDate());
            header.setAction(SedexType2021Enum.TYPE_102.getAction());
            header.getAttachment().addAll(getAttachmentTypeList());
            header.setTestDeliveryFlag(SedexType2021Enum.TYPE_102.isTestDeliveryFlag());
            header.setResponseExpected(SedexType2021Enum.TYPE_102.isResponseExpected());
            header.setBusinessCaseClosed(SedexType2021Enum.TYPE_102.isBusinessCaseClosed());
            header.setBusinessCaseClosed(SedexType2021Enum.TYPE_102.isBusinessCaseClosed());
            header.setExtension(getExtensionType());

            return header;

        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    private GFDaDossierModel getModel(String id) {
        try {
            GFDaDossierSearch search = new GFDaDossierSearch();
            search.setById(id);
            GFEFormServiceLocator.getGFDaDossierDBService().search(search);
            GFDaDossierModel model = Arrays.stream(search.getSearchResults())
                    .map(o -> (GFDaDossierModel) o)
                    .findFirst()
                    .orElse(null);
            return model;
        } catch (JadePersistenceException | JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateBusinessProcessId() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private void updateGFFormulaireStatus(GFDaDossierModel model) {

        try {
            if(Objects.isNull(model)){
                GFDaDossierModel gfDaDossierModel = new GFDaDossierModel();
                gfDaDossierModel.setType("Envoi");
                gfDaDossierModel.setStatus("Envoyé");
                gfDaDossierModel.setCodeCaisse(viewBean.getCaisseDestinatrice());
                gfDaDossierModel.setNssAffilier(viewBean.getNss());
                gfDaDossierModel.setOurBusinessRefId(UUID.randomUUID().toString());

                // ajout du status dans la table correspondante
                GFEFormServiceLocator.getGFDaDossierDBService().create(gfDaDossierModel);
            }else{
//                update du status
                model.setType("Envoi");  // en dur ou il y a des numeros ?
                model.setStatus("Envoyé");
                GFEFormServiceLocator.getGFDaDossierDBService().update(model);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    private List<AttachmentType> getAttachmentTypeList() throws DatatypeConfigurationException {
        List<String> fileNameList = viewBean.getFileNameList();
        List<String> tiffFileNameList = getTiffFilesNameList(fileNameList);
        List<AttachmentType> attachmentTypeList = new ArrayList<>();

        if (tiffFileNameList.size() < 1) {
            for (String fileName : fileNameList) {
                attachmentTypeList.add(createAttachment(fileName, getAttachementFileTypeList(fileName)));
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
            attachmentTypeList.add(createAttachment(key, getMultipleAttachementFileTypeList(multipleTiffFiles.get(key))));
        }
        return attachmentTypeList;
    }

    private AttachmentType createAttachment(String fileName, List<AttachmentFileType> attachmentTypeList) throws DatatypeConfigurationException {
        AttachmentType attachmentType = new AttachmentType();
        attachmentType.setDocumentType("01.10.11");
        attachmentType.setTitle(createAttachmentTitle(attachmentType.getDocumentType()));
        attachmentType.setDocumentDate(getDocumentDate());
        attachmentType.setLeadingDocument(SedexType2021Enum.TYPE_102.isLeadingDocument());
        attachmentType.setSortOrder(new BigInteger(String.valueOf(SedexType2021Enum.TYPE_102.getOrder())));
        attachmentType.setDocumentFormat(FilenameUtils.getExtension(fileName));
        attachmentType.getFile().addAll(attachmentTypeList);
        return attachmentType;
    }

    private String createAttachmentTitle(String documentType) {
        return documentType + "-" + viewBean.getNomAssure() + " ," + viewBean.getPrenomAssure();
    }

    private String createHeaderSubject() {
        return "demande de dossier CC " + "-" + viewBean.getNomAssure() + " ," + viewBean.getPrenomAssure();
    }

    private XMLGregorianCalendar getDocumentDate() throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        return xmlGregorianCalendar;
    }

    private List<AttachmentFileType> getAttachementFileTypeList(String fileName) {
        List<AttachmentFileType> attachmentFileTypesList = new ArrayList<>();
        attachmentFileTypesList.add(getAttachmentFileType(fileName, 1));
        return attachmentFileTypesList;

    }

    private List<AttachmentFileType> getMultipleAttachementFileTypeList(List<String> fileNameList) {
        List<AttachmentFileType> attachmentFileTypesList = new ArrayList<>();
        int sortOrder = 1;

        for (String fileName : fileNameList) {
            attachmentFileTypesList.add(getAttachmentFileType(fileName, sortOrder));
            sortOrder++;
        }
        return attachmentFileTypesList;
    }

    private AttachmentFileType getAttachmentFileType(String fileName, int sortOrder) {
        AttachmentFileType attachmentFileType = new AttachmentFileType();
        attachmentFileType.setPathFileName(fileName);
        attachmentFileType.setInternalSortOrder(BigInteger.valueOf(sortOrder));
        return attachmentFileType;
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

    private void sendMail() {
        try {
            String[] files = null;
            JadeSmtpClient.getInstance().sendMail(viewBean.getEmailGestionnaire(), "erreur creation message sedex", "l'envoi n'a pas pu être effectué à cause d'une erreur de la construction du message ", files);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
