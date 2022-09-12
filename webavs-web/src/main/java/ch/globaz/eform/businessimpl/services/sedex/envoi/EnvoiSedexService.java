package ch.globaz.eform.businessimpl.services.sedex.envoi;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import ch.globaz.eform.businessimpl.services.sedex.constant.GFActionSedex;
import ch.globaz.eform.businessimpl.services.sedex.constant.GFMessageTypeSedex;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.utils.GFFileUtils;
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
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
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
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnvoiSedexService {

    @Getter
    @Setter
    private String documentLead;
    private GFEnvoiViewBean viewBean;
    private Sedex000102 sedex000102 = new Sedex000102();

    private Message message;
    private Map<String, String> mapAttachments;

    public EnvoiSedexService(GFEnvoiViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public Message createSedexMessage() {
        Message message = new Message();
        try {
            Sedex000102 sedex0001021 = new Sedex000102();
            //todo sprint 18 lier l'id avec une demande
            String id = "5";
            GFDaDossierModel model = getModel(id);
            message = sedex0001021.createMessage(createHeader(model), createContent());
            updateGFFormulaireStatus(model);
            this.message = message;

        } catch (Exception e) {
            sendMail();
        }
        return message;
    }

    private HeaderType createHeader(GFDaDossierModel model) {
        try {
            HeaderType header = new HeaderType();
            //TODO sprint 18 mapper les 2 données commentées
//        header.setSenderId();
//        header.setRecipientId(getSedexId(model.getCodeCaisse()));
            header.setMessageId(JadeUUIDGenerator.createLongUID().toString());
            header.setReferenceMessageId(Objects.isNull(model) ? "" : model.getMessageId());
            header.setBusinessProcessId(generateBusinessProcessId());
            header.setOurBusinessReferenceId(Objects.isNull(model) ? UUID.randomUUID().toString() : model.getOurBusinessRefId());
            header.setYourBusinessReferenceId(Objects.isNull(model) ? "" : model.getYourBusinessRefId());
            header.setMessageType(GFMessageTypeSedex.TYPE_2021_TRANSFERE.getMessageType());
            header.setSubMessageType(GFMessageTypeSedex.TYPE_2021_TRANSFERE.getSubMessageType());
            header.setSendingApplication(sedex000102.getSendingApplicationType());
            header.setSubject(createHeaderSubject());
            header.setMessageDate(getDocumentDate());
            header.setAction(GFActionSedex.REPONSE.getCode().toString());
            header.getAttachment().addAll(getAttachmentTypeList());
            header.setTestDeliveryFlag(GFMessageTypeSedex.TYPE_2021_TRANSFERE.isTestDeliveryFlag());
            header.setResponseExpected(GFMessageTypeSedex.TYPE_2021_TRANSFERE.isResponseExpected());
            header.setBusinessCaseClosed(GFMessageTypeSedex.TYPE_2021_TRANSFERE.isBusinessCaseClosed());
            header.setBusinessCaseClosed(GFMessageTypeSedex.TYPE_2021_TRANSFERE.isBusinessCaseClosed());
            header.setExtension(getExtensionType());
            return header;

        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void reformatViewBean() {
        viewBean.setNss(viewBean.getNss().replaceAll("\\.", ""));
        viewBean.setCaisseDestinatrice(viewBean.getCaisseDestinatrice().replaceAll("\\D", ""));
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
            if (Objects.isNull(model)) {
                reformatViewBean();
                GFDaDossierModel gfDaDossierModel = new GFDaDossierModel();
                gfDaDossierModel.setMessageId(JadeUUIDGenerator.createLongUID().toString());
                gfDaDossierModel.setNssAffilier(viewBean.getNss());
                gfDaDossierModel.setCodeCaisse(viewBean.getCaisseDestinatrice());
                gfDaDossierModel.setType(GFTypeDADossier.SEND_TYPE.getCodeSystem());
                gfDaDossierModel.setStatus(GFStatusDADossier.SEND.getCodeSystem());
                gfDaDossierModel.setUserGestionnaire(viewBean.getSession().getUserInfo().getVisa());
                gfDaDossierModel.setOurBusinessRefId(UUID.randomUUID().toString());
                GFEFormServiceLocator.getGFDaDossierDBService().create(gfDaDossierModel);
            } else {
                model.setType(GFTypeDADossier.SEND_TYPE.getCodeSystem());
                model.setStatus(GFStatusDADossier.SEND.getCodeSystem());
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

        attachmentTypeList.add(createAttachmentLead(documentLead, getAttachementFileTypeList(new File(documentLead).getName())));
        int order = 2;
        if (tiffFileNameList.size() < 1) {
            for (String fileName : fileNameList) {
                attachmentTypeList.add(createAttachment(fileName, getAttachementFileTypeList(fileName), BigInteger.valueOf(order++)));
            }
        } else {
            Map<String, List<String>> multipleTiffFiles = findMultipleTiffFiles(tiffFileNameList);
            attachmentTypeList = getMultipleAttachmentTypeList(multipleTiffFiles);
        }

        mapAttachments = Stream.concat(Stream.of(new File(documentLead)),                                   // ajoute le document lead
                        GFFileUtils.listFile(GFFileUtils.WORK_PATH + viewBean.getFolderUid()).stream() // liste tous les fichiers
                                .filter(f -> viewBean.getFileNameList().contains(f.getName())))             // filtre selon la liste
                .map(s -> s.getAbsolutePath()).collect(Collectors.toMap(this::getFileNameFromPath,          // crée la map
                Function.identity()));

        return attachmentTypeList;

    }

    String getFileNameFromPath(String path) {
        return new File(path).getName() ;
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
        int order = 1;
        for (String key : multipleTiffFiles.keySet()) {
            attachmentTypeList.add(createAttachment(key, getMultipleAttachementFileTypeList(multipleTiffFiles.get(key)),BigInteger.valueOf(order++)));
        }
        return attachmentTypeList;
    }

    private AttachmentType createAttachment(String fileName, List<AttachmentFileType> attachmentTypeList, BigInteger order) throws DatatypeConfigurationException {
        AttachmentType attachmentType = new AttachmentType();
        attachmentType.setDocumentType(viewBean.getTypeDeFichier());
        attachmentType.setTitle(createAttachmentTitle(attachmentType.getDocumentType()));
        attachmentType.setDocumentDate(getDocumentDate());
        attachmentType.setLeadingDocument(SedexType2021Enum.TYPE_102.isLeadingDocument());
        attachmentType.setSortOrder(new BigInteger(String.valueOf(SedexType2021Enum.TYPE_102.getOrder())));
        attachmentType.setDocumentFormat(FilenameUtils.getExtension(fileName));
        attachmentType.getFile().addAll(attachmentTypeList);
        return attachmentType;
    }

    private AttachmentType createAttachmentLead(String fileName, List<AttachmentFileType> attachmentTypeList) throws DatatypeConfigurationException {
        AttachmentType attachmentLead = createAttachment(fileName, attachmentTypeList, BigInteger.valueOf(1));
        attachmentLead.setLeadingDocument(true);
        attachmentLead.setDocumentType(GFDocumentTypeDossier.getStatusByDocumentType(viewBean.getTypeDeFichier()).getDocumentTypeLead());
        return attachmentLead;
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
        attachmentFileType.setPathFileName("attachements/"+fileName);
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
        contentType.setPensionRecipient(GFMessageTypeSedex.TYPE_2021_TRANSFERE.isPensionRecipient());
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

    public void sendMessage() throws JadeSedexMessageNotSentException, JAXBValidationError, JAXBValidationWarning, JAXBException, IOException, SAXException {
        JAXBServices jaxb = JAXBServices.getInstance();
        String file = jaxb.marshal(message, false, false, new Class[] {});
        JadeSedexService.getInstance().sendSimpleMessage(file, mapAttachments );
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
