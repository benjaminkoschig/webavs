package ch.globaz.eform.process;


import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.NSSUtils;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.businessimpl.services.sedex.constant.GFMessageTypeSedex;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierAttachmentElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierContactInformationElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierHeaderElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierInsuredPersonElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFSenderFactory;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.web.application.GFApplication;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.eform.itext.GFDocumentPojo;
import globaz.eform.itext.GFEnvoiDossier;
import globaz.eform.translation.CodeSystem;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Process qui effectue un envoi SEDEX des documents Da Dossier
 */
@Slf4j
@Setter
@Getter
public class GFDaDossierSedexEnvoiReponseProcess extends BProcess {

    private BSession bsession;

    private GFDaDossierModel model;
    private GFDocumentTypeDossier documentType;
    private List<String> attachments;

    @Override
    protected void _executeCleanUp() {
        EFormFileService fileService = new EFormFileService(GFApplication.DA_DOSSIER_SHARE_FILE);
        String partageDir = model.getMessageId() + File.separator;
        fileService.removeFolder(partageDir);
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        LOG.info("Lancement du process d'envoi du dossier Da Dossier");

        initBsession();
        this.setSendMailOnError(true);
        this.setSendCompletionMail(false);
        this.setEMailAddress(GFProperties.EMAIL_DADOSSIER.getValue());

        List<Path> attachmentsPath;

        //Transf?re des fichiers joint dans le dossier work
        EFormFileService fileService = new EFormFileService(GFApplication.DA_DOSSIER_SHARE_FILE);

        String partageDir = model.getMessageId() + File.separator;

        if (fileService.exist(partageDir)) {
            attachmentsPath = attachments.stream()
                    .map(filename -> fileService.retrieve(partageDir, filename).toPath())
                    .collect(Collectors.toList());
        } else {
            attachmentsPath = new ArrayList<>();
        }

        try {
            ValidationResult validation = envoyerReponse(model, documentType, attachmentsPath, getSession());

            if (validation.hasError()) {
                validation.getErrors().forEach(error -> {
                    String errorMsg = error.getDesignation(getSession());
                    getMemoryLog().logMessage(errorMsg, FWMessage.ERREUR, this.getClass().getName());
                    LOG.error(errorMsg);
                });
                sendMail(validation);
                return false;
            }

            //suppression du sous dossier de partage
            //TODO ? faire, supprim? par manque de temps
        } finally {
            closeBsession();
        }

        LOG.info("Fin du process d'information.");
        return true;
    }

    private ValidationResult envoyerReponse(GFDaDossierModel model, GFDocumentTypeDossier documentType, List<Path> attachments, BSession session) throws Exception {
        TIPersonneAvsManager mgr = new TIPersonneAvsManager();
        mgr.setISession(session);
        mgr.setForNumAvsActuel(NSSUtils.formatNss(model.getNssAffilier()));
        mgr.setForIncludeInactif(true);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.size() == 0) {
            throw new IllegalArgumentException("Tier avec le NSS '" + model.getNssAffilier() + "' non trouv?!");
        }

        TITiersViewBean tiers = (TITiersViewBean) mgr.getFirstEntity();

        GFDocumentPojo documentPojo = GFDocumentPojo.builder()
                .nom(tiers.getDesignation1())
                .prenom(tiers.getDesignation2())
                .nss(NSSUtils.formatNss(model.getNssAffilier()))
                .dateNaissance(tiers.getDateNaissance())
                .build();

        //Pr?paration du document leading
        String leadingDocumentFileName = session.getLabel("DOCUMENT_LEAD_2021_102_FILENAME");
        GFEnvoiDossier documentEnvoie = new GFEnvoiDossier(session, GFApplication.DEFAULT_APPLICATION_ROOT, leadingDocumentFileName);
        documentEnvoie.setSession(session);
        documentEnvoie.setDocumentPojo(documentPojo);

        GFDaDossierSender sender = GFSenderFactory.getSedexSender(GFMessageTypeSedex.TYPE_2021_TRANSFERE);

        Map<GFDaDossierElementSender, String> dataMessageSedex = new HashMap<>();

        //Attribution des identifiants
        if (StringUtils.isBlank(model.getMessageId())) {
            model.setMessageId(sender.getIdentifiantGenerator().generateMessageId());
        }

        //D?finition des informations compl?mentaire en vue de la persistence de la demande

        //compl?ment des informations sur la caisse destinatrice et le type original si n?cessaire (nb: envoie sans sollicitation)
        if (StringUtils.isBlank(model.getId())) {
            model.setOriginalType(GFTypeDADossier.SEND_TYPE.getCodeSystem());
        }
        model.setType(GFTypeDADossier.SEND_TYPE.getCodeSystem());
        model.setStatus(GFStatusDADossier.SEND.getCodeSystem());
        model.setUserGestionnaire(session.getUserInfo().getVisa());

        if (StringUtils.isBlank(model.getSedexIdCaisse())) {
            AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
            search.setForIdTiersAdministration(model.getIdTierAdministration());

            model.setSedexIdCaisse(((AdministrationComplexModel) TIBusinessServiceLocator.getAdministrationService()
                    .find(search).getSearchResults()[0])
                    .getAdmin()
                    .getSedexId());
        }

        dataMessageSedex.put(GFDaDossierHeaderElementSender.MESSAGE_ID, model.getMessageId());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID, model.getOurBusinessRefId());
        if (!StringUtils.isEmpty(model.getYourBusinessRefId())) {
            dataMessageSedex.put(GFDaDossierHeaderElementSender.YOUR_BUSINESS_REFERENCE_ID, model.getYourBusinessRefId());
        }
        dataMessageSedex.put(GFDaDossierHeaderElementSender.SENDER_ID, GFProperties.SENDER_ID.getValue());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.RECIPIENT_ID, model.getSedexIdCaisse());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.SUBJECT,
                session.getLabel("SUBJECT_2021_102_SEDEX") + " ? " + tiers.getDesignation2() + ", " + tiers.getDesignation1());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.TEST_DELIVERY_FLAG, GFProperties.DA_DOSSIER_MODE_TEST.getValue());

        dataMessageSedex.put(GFDaDossierAttachmentElementSender.DOCUMENT_TYPE, documentType.toString());
        dataMessageSedex.put(GFDaDossierAttachmentElementSender.TITLE, session.getLabel("DOCUMENT_LEAD_2021_102_SEDEX"));

        dataMessageSedex.put(GFDaDossierContactInformationElementSender.DEPARTEMENT, GFProperties.GESTIONNAIRE_USER_DEPARTEMENT.getValue());
        dataMessageSedex.put(GFDaDossierContactInformationElementSender.NAME, session.getUserFullName());
        dataMessageSedex.put(GFDaDossierContactInformationElementSender.EMAIL, session.getUserInfo().getEmail());
        dataMessageSedex.put(GFDaDossierContactInformationElementSender.PHONE, GFProperties.GESTIONNAIRE_USER_TELEPHONE.getValue());

        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.OFFICIAL_NAME, tiers.getDesignation1());
        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.FIRST_NAME, tiers.getDesignation2());
        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.DATE_OF_BIRT, tiers.getDateNaissance());
        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.VN, NSSUtils.formatNss(model.getNssAffilier()));

        try {
            documentEnvoie.executeProcess();

            JadePublishDocument getDocument = (JadePublishDocument)documentEnvoie.getAttachedDocuments().get(0);
            sender.setLeadingAttachment(Paths.get(getDocument.getDocumentLocation()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        sender.setElements(dataMessageSedex);
        sender.addAttachments(attachments);

        //pr?validation du model
        ValidationResult validation = model.validating();

        if (!validation.hasError()) {
            sender.send();

            if (StringUtils.isBlank(model.getId())) {
                GFEFormServiceLocator.getGFDaDossierDBService().create(model);
            } else {
                GFEFormServiceLocator.getGFDaDossierDBService().update(model);
            }
        }

        return validation;
    }

    private void sendMail(ValidationResult validation) throws PropertiesException {
        String email = GFProperties.EMAIL_DADOSSIER.getValue();

        String subject = getSession().getLabel("MAIL_SUBJECT_ENVOI_RECEPTION_ERROR_SEDEX");
        String body = getMailBody(validation);

        ProcessMailUtils.sendMail(Collections.singletonList(email), subject, body, Collections.EMPTY_LIST);
    }

    private String getMailBody(ValidationResult validationResult) {
        StringBuilder body = new StringBuilder(getSession().getLabel("MAIL_BODY_ENVOI_RECEPTION_MESSAGE_ERROR_SEDEX"));

        body.append(System.getProperty("line.separator"))
                .append(getSession().getLabel("MAIL_BODY_ENVOI_RECEPTION_ERROR_SECTION_SEDEX"))
                .append(System.getProperty("line.separator"));

        if (Objects.nonNull(validationResult)) {
            validationResult.getErrors().forEach(error -> body.append("    ").append(error.getDesignation(getSession())).append(System.getProperty("line.separator")));
        }

        return body.toString();
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Initialisation de la session
     *
     * @throws Exception : lance une exception si un probl?me intervient lors de l'initialisation du contexte
     */
    private void initBsession() throws Exception {
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    /**
     *  Fermeture de la session
     */
    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
