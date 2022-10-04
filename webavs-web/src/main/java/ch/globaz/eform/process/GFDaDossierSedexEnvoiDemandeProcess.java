package ch.globaz.eform.process;


import ch.globaz.common.util.NSSUtils;
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
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.eform.web.application.GFApplication;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.eform.itext.GFDemandeDossier;
import globaz.eform.itext.GFDocumentPojo;
import globaz.eform.translation.CodeSystem;
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

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Process qui effectue un envoi SEDEX d'une demande de dossier Da Dossier
 */
@Slf4j
public class GFDaDossierSedexEnvoiDemandeProcess extends BProcess {

    private BSession bsession;
    @Setter
    @Getter
    private GFDaDossierModel model;

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        LOG.info("Lancement du process d'envoi d'une demande Da Dossier");

        initBsession();
        this.setSendMailOnError(true);
        this.setSendCompletionMail(false);

        envoyerDemande(model, getSession());

        closeBsession();
        LOG.info("Fin du process d'information.");
        return true;
    }

    public void envoyerDemande(GFDaDossierModel model, BSession session) throws Exception {
        TIPersonneAvsManager mgr = new TIPersonneAvsManager();
        mgr.setISession(session);
        mgr.setForNumAvsActuel(NSSUtils.formatNss(model.getNssAffilier()));
        mgr.setForIncludeInactif(true);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.size() == 0) {
            throw new IllegalArgumentException("Tier avec le NSS '" + model.getNssAffilier() + "' non trouvé!");
        }

        TITiersViewBean tiers = (TITiersViewBean) mgr.getFirstEntity();

        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_COMP);
        search.setForCodeAdministration(model.getCodeCaisse());

        String sedexId = ((AdministrationComplexModel) TIBusinessServiceLocator.getAdministrationService()
                .find(search).getSearchResults()[0])
                .getAdmin()
                .getSedexId();

        GFDocumentPojo documentPojo = GFDocumentPojo.builder()
                .nom(tiers.getDesignation1())
                .prenom(tiers.getDesignation2())
                .nss(NSSUtils.formatNss(model.getNssAffilier()))
                .dateNaissance(tiers.getDateNaissance())
                .build();

        //Préparation du document leading
        String leadingDocumentFileName = session.getLabel("DOCUMENT_LEAD_2021_101_FILENAME");
        GFDemandeDossier documentDemande = new GFDemandeDossier(session, GFApplication.DEFAULT_APPLICATION_ROOT, leadingDocumentFileName);
        documentDemande.setSession(session);
        documentDemande.setDocumentPojo(documentPojo);

        GFDaDossierSender sender = GFSenderFactory.getSedexSender(GFMessageTypeSedex.TYPE_2021_DEMANDE);

        Map<GFDaDossierElementSender, String> dataMessageSedex = new HashMap<>();

        //Attribution des identifiants
        model.setMessageId(sender.getIdentifiantGenerator().generateMessageId());
        model.setOurBusinessRefId(sender.getIdentifiantGenerator().generateBusinessReferenceId());

        //Définition des informations complémentaire en vue de la persistence de la demande
        model.setOriginalType(GFTypeDADossier.RECEPTION.getCodeSystem());
        model.setType(GFTypeDADossier.RECEPTION.getCodeSystem());
        model.setStatus(GFStatusDADossier.WAITING.getCodeSystem());
        model.setUserGestionnaire(session.getUserInfo().getVisa());

        dataMessageSedex.put(GFDaDossierHeaderElementSender.MESSAGE_ID, model.getMessageId());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID, model.getOurBusinessRefId());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.SENDER_ID, GFProperties.SENDER_ID.getValue());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.RECIPIENT_ID, sedexId);
        dataMessageSedex.put(GFDaDossierHeaderElementSender.SUBJECT,
                session.getLabel("SUBJECT_2021_101_SEDEX") + " – " + tiers.getDesignation2() + ", " + tiers.getDesignation1());
        dataMessageSedex.put(GFDaDossierHeaderElementSender.TEST_DELIVERY_FLAG, GFProperties.DA_DOSSIER_MODE_TEST.getValue());

        dataMessageSedex.put(GFDaDossierAttachmentElementSender.TITLE, session.getLabel("DOCUMENT_LEAD_2021_101_SEDEX"));

        dataMessageSedex.put(GFDaDossierContactInformationElementSender.DEPARTEMENT, GFProperties.GESTIONNAIRE_USER_DEPARTEMENT.getValue());
        dataMessageSedex.put(GFDaDossierContactInformationElementSender.NAME, session.getUserFullName());
        dataMessageSedex.put(GFDaDossierContactInformationElementSender.EMAIL, session.getUserInfo().getEmail());
        dataMessageSedex.put(GFDaDossierContactInformationElementSender.PHONE, GFProperties.GESTIONNAIRE_USER_TELEPHONE.getValue());

        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.OFFICIAL_NAME, tiers.getDesignation1());
        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.FIRST_NAME, tiers.getDesignation2());
        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.DATE_OF_BIRT, tiers.getDateNaissance());
        dataMessageSedex.put(GFDaDossierInsuredPersonElementSender.VN, NSSUtils.formatNss(model.getNssAffilier()));

        sender.setElements(dataMessageSedex);

        try {
            documentDemande.executeProcess();

            JadePublishDocument getDocument = (JadePublishDocument)documentDemande.getAttachedDocuments().get(0);
            sender.setLeadingAttachment(Paths.get(getDocument.getDocumentLocation()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        sender.send();

        GFEFormServiceLocator.getGFDaDossierDBService().create(model);
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
     * @throws Exception : lance une exception si un problème intervient lors de l'initialisation du contexte
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
