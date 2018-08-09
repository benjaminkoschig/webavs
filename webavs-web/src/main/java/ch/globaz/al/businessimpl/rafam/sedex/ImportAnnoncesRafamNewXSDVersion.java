package ch.globaz.al.businessimpl.rafam.sedex;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.datatype.XMLGregorianCalendar;
import al.ch.ech.xmlns.ech_0104_69._4.HeaderType;
import al.ch.ech.xmlns.ech_0104_69._4.Message;
import al.ch.ech.xmlns.ech_0104_69._4.NoticeType;
import al.ch.ech.xmlns.ech_0104_69._4.ReceiptType;
import al.ch.ech.xmlns.ech_0104_69._4.RegisterStatusRecordType;
import al.ch.ech.xmlns.ech_0104_69._4.UPISynchronizationRecordType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.AllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.BeneficiaryType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildAllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildType;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamImportProtocolFields;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamNotFoundException;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.sedex.AnnonceRafamSedexService;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessageEmployeurDelegueHandler;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessageNoticeNewXSDVersionHandler;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessageReceiptNewXSDVersionHandler;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessageRegisterNewXSDVersionStatusRecordDelegueHandler;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessageRegisterStatusRecordDelegueHandler;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessageUPISynchronizationNewXSDVersionRecordHandler;
import ch.globaz.al.businessimpl.rafam.sedex.handler.MessagesEmployeursDeleguesContainer;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALRafamUtils;
import ch.globaz.jaxbUtils.MarshallerEmployeurDelegueSingleton;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererDefaultStringAdapter;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;

/**
 * Gère l'importation des annonces RAFam à leur retour de la centrale
 *
 * @author jts
 *
 */
public class ImportAnnoncesRafamNewXSDVersion {

    private static final String APPLICATION_NAME = "AL";
    private JadeContext context;
    private String passSedex;
    private BSession session;
    private String userSedex;

    private void addErrorProtocole(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole, Object messageRafam,
            String messageErreur) throws JadeApplicationServiceNotAvailableException {

        HashMap<RafamImportProtocolFields, String> erreur = new HashMap<RafamImportProtocolFields, String>();

        String nssEnfant = "";
        String nssAllocataire = "";
        String recordNumber = "";
        List<String> errList = new ArrayList<String>();

        if (messageRafam instanceof NoticeType) {
            errList = ALServiceLocator.getAnnoncesRafamNewXSDVersionErrorBusinessService()
                    .getErrorsFromList(((NoticeType) messageRafam).getError());
            nssEnfant = Long.toString(((NoticeType) messageRafam).getChild().getVn());
            nssAllocataire = Long.toString(((NoticeType) messageRafam).getBeneficiary().getVn());
            recordNumber = ((NoticeType) messageRafam).getRecordNumber().toString();

        } else if (messageRafam instanceof ReceiptType) {
            errList = ALServiceLocator.getAnnoncesRafamNewXSDVersionErrorBusinessService()
                    .getErrorsFromList(((ReceiptType) messageRafam).getError());
            if (((ReceiptType) messageRafam).getChild().getVn() != null) {
                nssEnfant = Long.toString(((ReceiptType) messageRafam).getChild().getVn());
            }
            if (((ReceiptType) messageRafam).getBeneficiary().getVn() != null) {
                nssAllocataire = Long.toString(((ReceiptType) messageRafam).getBeneficiary().getVn());
            }
            recordNumber = ((ReceiptType) messageRafam).getRecordNumber().toString();

        } else if (messageRafam instanceof RegisterStatusRecordType) {
            errList = ALServiceLocator.getAnnoncesRafamNewXSDVersionErrorBusinessService()
                    .getErrorsFromList(((RegisterStatusRecordType) messageRafam).getError());
            if (((RegisterStatusRecordType) messageRafam).getChild().getVn() != null) {
                nssEnfant = Long.toString(((RegisterStatusRecordType) messageRafam).getChild().getVn());
            }
            if (((RegisterStatusRecordType) messageRafam).getBeneficiary().getVn() != null) {
                nssAllocataire = Long.toString(((RegisterStatusRecordType) messageRafam).getBeneficiary().getVn());
            }
            recordNumber = ((RegisterStatusRecordType) messageRafam).getRecordNumber().toString();

        } else if (messageRafam instanceof UPISynchronizationRecordType) {
            errList = ALServiceLocator.getAnnoncesRafamNewXSDVersionErrorBusinessService()
                    .getErrorsFromList(((UPISynchronizationRecordType) messageRafam).getError());
            if (((UPISynchronizationRecordType) messageRafam).getChild().getVn() != null) {
                nssEnfant = Long.toString(((UPISynchronizationRecordType) messageRafam).getChild().getVn());
            }
            if (((UPISynchronizationRecordType) messageRafam).getBeneficiary().getVn() != null) {
                nssAllocataire = Long.toString(((UPISynchronizationRecordType) messageRafam).getBeneficiary().getVn());
            }
            recordNumber = ((UPISynchronizationRecordType) messageRafam).getRecordNumber().toString();
        }

        String errString = "";
        for (String error : errList) {
            errString += error + ", ";
        }

        erreur.put(RafamImportProtocolFields.ERRORS, errString);
        erreur.put(RafamImportProtocolFields.MSG_ERR, messageErreur);
        erreur.put(RafamImportProtocolFields.NSS_ALLOCATAIRE, nssAllocataire);
        erreur.put(RafamImportProtocolFields.NSS_ENFANT, nssEnfant);
        erreur.put(RafamImportProtocolFields.RECORD_NUMBER, recordNumber);
        protocole.add(erreur);
    }

    // TODO v2: gérer le format CSV, pas indispensable car les CAF webAF/alfagest => xml
    private void createFichiersRetoursDelegue(HeaderType header, RafamTypeAnnonce typeAnnonces) throws Exception {

        StringBuffer errors = new StringBuffer();
        boolean isUPI = false;
        MessagesEmployeursDeleguesContainer medc = MessagesEmployeursDeleguesContainer.getInstance();
        Map<Integer, ArrayList<Object>> annonceEmpl = medc.getMapAnnoncesEmployeursDelegue();
        if (annonceEmpl.size() > 0) {
            HashMap<String, BeneficiaryType> beneficiariesToReturn = new HashMap<String, BeneficiaryType>();
            HashMap<String, ChildType> childrenToReturn = new HashMap<String, ChildType>();
            HashMap<String, AllowanceType> allowanceToReturn = new HashMap<String, AllowanceType>();

            ChildAllowanceType childAllowances = new ChildAllowanceType();

            for (Map.Entry<Integer, ArrayList<Object>> en : annonceEmpl.entrySet()) {

                int idEmployeurDelegue = en.getKey();
                // c'est la premiere annonce qui détermine le header, dans le cas de 69a c'est les même infos de toute
                // façon

                fillHeaderFichierEmployeur(header, childAllowances,
                        (AnnonceRafamDelegueComplexModel) en.getValue().get(0));

                for (Object annonce : en.getValue()) {
                    try {
                        if (RafamTypeAnnonce._69B_SYNCHRO_UPI.equals(
                                ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel().getTypeAnnonce())) {
                            isUPI = true;
                        }
                        String cleBeneficiaire = ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel()
                                .getNssAllocataire();
                        String cleEnfant = cleBeneficiaire + "-"
                                + ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel().getNssEnfant();

                        String cleAllowance = cleEnfant + "-"
                                + ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel().getRecordNumber();
                        // String cleAllowance = ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel()
                        // .getInternalOfficeReference();

                        BeneficiaryType beneficaryToSave = null;
                        ChildType childToSave = null;
                        AllowanceType allowanceToSave = null;

                        if (!beneficiariesToReturn.containsKey(cleBeneficiaire)) {

                            beneficaryToSave = ALRafamUtils
                                    .toBeneficiaryAfDelegueFormat((AnnonceRafamDelegueComplexModel) annonce);

                            beneficiariesToReturn.put(cleBeneficiaire, beneficaryToSave);
                            childAllowances.getBeneficiary().add(beneficaryToSave);
                        } else {
                            beneficaryToSave = beneficiariesToReturn.get(cleBeneficiaire);
                        }

                        if (!childrenToReturn.containsKey(cleEnfant)) {
                            childToSave = ALRafamUtils
                                    .toChildAfDelegueFormat((AnnonceRafamDelegueComplexModel) annonce);
                            childrenToReturn.put(cleEnfant, childToSave);
                            beneficaryToSave.getChild().add(childToSave);

                        } else {
                            childToSave = childrenToReturn.get(cleEnfant);
                        }

                        if (!allowanceToReturn.containsKey(cleAllowance)) {
                            allowanceToSave = ALRafamUtils
                                    .toAllowanceAfDelegueFormat((AnnonceRafamDelegueComplexModel) annonce);
                            childToSave.getAllowance().add(allowanceToSave);
                            allowanceToReturn.put(cleAllowance, allowanceToSave);
                        } else {
                            allowanceToSave = allowanceToReturn.get(cleAllowance);
                        }

                    } catch (Exception e) {
                        JadeLogger.error(this,
                                "Une erreur s'est produite pendant la création des fichiers d'annonces de l'employeur délégué n°"
                                        + idEmployeurDelegue + ", annonce n° "
                                        + ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel()
                                                .getRecordNumber()
                                        + "," + e.getMessage());

                        errors.append(
                                "Une erreur s'est produite pendant la création des fichiers d'annonces de l'employeur délégué n°"
                                        + idEmployeurDelegue + ", annonce n° "
                                        + ((AnnonceRafamDelegueComplexModel) annonce).getAnnonceRafamModel()
                                                .getRecordNumber())
                                .append(e.getMessage()).append("\n");

                    }
                }

                // envoi messages d'erreur éventuels lors de création fichier retour af-delégué
                if (errors.length() > 0) {
                    try {
                        JadeLogger.error(this, errors.toString());

                        ALRafamUtils.sendMail(
                                new StringBuffer("Af-delegue :Erreurs survenues lors de génération du fichier retour"),
                                errors, new String[0]);
                    } catch (Exception e) {
                        // Exception levée si le mail n'a pas pu être envoyé. On ne peut rien faire de plus mais
                        // le message
                        // d'erreur est enregistré dans le log
                    }
                }

                // TODO v2: .xml ou csv selon properties
                // envoi fichier xml retour
                String uriFilesRetour = JadePropertiesService.getInstance()
                        .getProperty("al.rafam.delegue." + idEmployeurDelegue + ".filesRetour.uri");

                String suffixTypeAnnonce = "";
                switch (typeAnnonces) {
                    case _69B_SYNCHRO_UPI:
                        suffixTypeAnnonce = "_upi";
                        break;

                    case _69C_REGISTER_STATUS:
                        suffixTypeAnnonce = "_register";
                        break;

                    case _69A_RECEIPT:

                    case _69D_NOTICE:

                }

                String nowDate = JadeDateUtil.getYMDDate(new Date()).concat(JadeDateUtil.getSqlTime(new Date()));

                nowDate = JadeStringUtil.removeChar(nowDate, ':');

                String urlForReturn = uriFilesRetour + idEmployeurDelegue + "_" + nowDate + suffixTypeAnnonce + ".xml";

                String nomFichierTemp = Jade.getInstance().getPersistenceDir() + "tmp_" + idEmployeurDelegue + "_"
                        + nowDate + ".xml";

                File fichierARetourner = new File(nomFichierTemp);

                MarshallerEmployeurDelegueSingleton.marshal(childAllowances, fichierARetourner);
                JadeFsFacade.copyFile(fichierARetourner.getAbsolutePath(), urlForReturn);
                if (!fichierARetourner.delete()) {
                    JadeLogger.info(this,
                            "Le fichier " + fichierARetourner.getAbsolutePath() + " n'a pas pu être supprimé");
                }

                // création + envoi protocole retour au responsable Rafam de la CAF
                String file = ALServiceLocator.getAnnoncesRafamDelegueProtocoleService()
                        .createProtocole(Integer.toString(idEmployeurDelegue));

                StringBuffer description = new StringBuffer("Protocole de retour des Annonces RAFam délégué ("
                        + typeAnnonces + ") employeur n°" + idEmployeurDelegue);
                ALRafamUtils.sendMail(description, description, new String[] { file });
                // TODO v2: déléguer cette tâche au module délégué qui devra checker si de nouveaux fichiers sont
                // présents
                // et envoyer un mail le cas échéant
                // envoi mail employeur délégué indiquant fichier retour + protcole
                String uriProtocole = JadePropertiesService.getInstance().getProperty(
                        "al.rafam.delegue." + Integer.toString(idEmployeurDelegue) + ".protocoleRetour.uri");

                String destinationFinale = uriProtocole.concat(file.substring(file.lastIndexOf("/") + 1));
                JadeFsFacade.copyFile(file, destinationFinale);

                description = new StringBuffer(
                        "Protocole et fichiers de retour (" + typeAnnonces + ") de vos annonces disponibles");
                JadeSmtpClient.getInstance().sendMail(childAllowances.getMailResponsiblePerson(),
                        description.toString(), description.toString(), new String[0]);

                JadeLogger.info(this, "protocole RAFam délégué envoyé");

            }

            // une fois qu'on a génère les fichiers de retour on vide le conteneur des annonces déléguées car sinon
            // elles seront regénérées dans le nouveau fichier de retour
            medc.clear();

        }

    }

    private void fillHeaderFichierEmployeur(HeaderType header, ChildAllowanceType childAllowances,
            AnnonceRafamDelegueComplexModel annonce) throws JadeApplicationException {

        childAllowances.setEventDate(header.getEventDate());
        childAllowances.setMessageDate(header.getMessageDate());

        if (annonce.getComplementDelegueModel() != null) {

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageCompanyName())) {
                childAllowances.setCompanyName(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            } else {
                childAllowances.setCompanyName(annonce.getComplementDelegueModel().getMessageCompanyName());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageFakId())) {
                childAllowances.setFakID("000000");
            } else {
                childAllowances.setFakID(annonce.getComplementDelegueModel().getMessageFakId());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageFakName())) {
                childAllowances.setFakName(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            } else {
                childAllowances.setFakName(annonce.getComplementDelegueModel().getMessageFakName());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageMailResponsiblePerson())) {

                String email = JadePropertiesService.getInstance().getProperty("al.rafam.delegue."
                        + annonce.getAnnonceRafamModel().getRecordNumber().substring(0, 2) + "defaultContactMail");
                childAllowances.setMailResponsiblePerson(email);
            } else {
                childAllowances.setMailResponsiblePerson(
                        annonce.getComplementDelegueModel().getMessageMailResponsiblePerson());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageNameResponsiblePerson())) {
                childAllowances.setNameResponsiblePerson(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            } else {
                childAllowances.setNameResponsiblePerson(
                        annonce.getComplementDelegueModel().getMessageNameResponsiblePerson());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageTelResponsiblePerson())) {
                childAllowances.setTelResponsiblePerson(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            } else {
                childAllowances
                        .setTelResponsiblePerson(annonce.getComplementDelegueModel().getMessageTelResponsiblePerson());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getMessageId())) {
                childAllowances.setMessageID("000000000000");
            } else {
                childAllowances.setMessageID(annonce.getComplementDelegueModel().getMessageId());
            }

            if (JadeStringUtil.isBlankOrZero(annonce.getComplementDelegueModel().getBusinessId())) {
                childAllowances.setBusinessID("000000000000");
            } else {
                childAllowances.setBusinessID(annonce.getComplementDelegueModel().getBusinessId());
            }

        } else {

            childAllowances.setBusinessID("000000000000");
            childAllowances.setCompanyName(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            childAllowances.setFakID("000000");
            childAllowances.setFakName(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            String email = JadePropertiesService.getInstance().getProperty("al.rafam.delegue."
                    + annonce.getAnnonceRafamModel().getRecordNumber().substring(0, 2) + "defaultContactMail");
            childAllowances.setMailResponsiblePerson(email);
            childAllowances.setMessageID("000000000000");
            childAllowances.setNameResponsiblePerson(ALConstRafam.MESSAGE_ED_DATA_BLANK);
            childAllowances.setReferenceMessageID("000000000000");
            childAllowances.setTelResponsiblePerson(ALConstRafam.MESSAGE_ED_DATA_BLANK);
        }

    }

    /**
     * Retourne un contexte. Si nécessaire il est initialisé
     *
     * @return le contexte
     *
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    public JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(getSession()).getContext();
        }
        return context;
    }

    private String[] getMailsProtocole() {
        String contactTechnique = JadePropertiesService.getInstance()
                .getProperty(ALConstRafam.RAFAM_CONTACT_TECHNIQUE_EMAIL);

        if (JadeStringUtil.isBlank(contactTechnique)) {
            return new String[] { JadePropertiesService.getInstance().getProperty(ALConstRafam.RAFAM_CONTACT_EMAIL),
                    JadeThread.currentUserEmail() };
        } else {
            return new String[] { JadePropertiesService.getInstance().getProperty(ALConstRafam.RAFAM_CONTACT_EMAIL),
                    contactTechnique };
        }
    }

    /**
     * Retourne une session. Si nécessaire elle est initialisée
     *
     * @return la session
     *
     * @throws Exception
     *             Exception levée si la session ne peut être initialisée
     */
    public BSession getSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(ImportAnnoncesRafamNewXSDVersion.APPLICATION_NAME)
                    .newSession(userSedex, passSedex);
        }

        return session;
    }

    /**
     * Charge les annonces contenues dans le message et exécute, pour chaque type de message, le traitement approprié.
     * Une fois les annonces de la caisse traitées, la méthode permettant de préparer le fichier des annonces des
     * employeurs délégués est appelée
     *
     * @param messageCentrale
     *            Le message contenant les annonces à importer
     * @throws Exception
     */
    public void importMessage(SedexMessage messageCentrale) throws Exception {

        try {
            JadeLogger.info(this, "Start import RAFam Sedex message " + messageCentrale.getFileLocation());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            ArrayList<HashMap<RafamImportProtocolFields, String>> protocole = new ArrayList<HashMap<RafamImportProtocolFields, String>>();
            String recipientId = "?";
            Message message = (Message) messageCentrale;

            // /////////////////////////////////////////////////////////////////////////////////////////////////////
            // Traitement du message
            // /////////////////////////////////////////////////////////////////////////////////////////////////////

            if (message != null) {

                List<String> recipientIdMessage = message.getHeader().getRecipientId();
                if (isRecipientIdValid(messageCentrale, recipientIdMessage)) {

                    recipientId = recipientIdMessage.get(0);
                    processNotices(protocole, message);
                    processReceipts(protocole, message);
                    processRegisterStatusRecords(protocole, message, message.getHeader().getMessageDate());
                    processUPISynchronizationRecords(protocole, message);

                    processDelegues(message);
                }
            }

            // /////////////////////////////////////////////////////////////////////////////////////////////////////
            // Envoi d'éventuels messages d'erreur
            // /////////////////////////////////////////////////////////////////////////////////////////////////////

            if (protocole.size() > 0) {
                try {
                    String file = ALServiceLocator.getAnnonceRafamImportProtocoleService().createProtocole(protocole);
                    String description = "Protocole d'importation des annonces RAFam (recipientId " + recipientId + ")";
                    JadeSmtpClient.getInstance().sendMail(getMailsProtocole(), description, description,
                            new String[] { file });
                    JadeLogger.info(this, description + " envoyé");
                } catch (Exception e) {
                    JadeLogger.error(this,
                            "Une erreur s'est produite pendant la préparation du protocole d'importation des annonces RAFam"
                                    + e.getMessage());
                }
            }
            JadeLogger.info(this, "Finish import RAFam Sedex message " + messageCentrale.getFileLocation());
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant l'importation d'un message RAFam : " + e1.getMessage());
            throw e1;
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Initialise un contexte
     *
     * @param session
     *            session
     * @return le contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(ImportAnnoncesRafamNewXSDVersion.APPLICATION_NAME);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    /**
     * Vérifie le paramètre indiquant si les annonces d'état du registre doivent être importées
     *
     * @return <code>true</code> si les annonces doivent être importées
     */
    private boolean isImportRegisterStatusDelegateEnabled() {
        return "1".equals(JadePropertiesService.getInstance()
                .getProperty(ALConstRafam.IMPORT_REGISTER_STATUS_DELEGATE_IS_ENABLED));
    }

    /**
     * Vérifie le paramètre indiquant si les annonces d'état du registre doivent être importées
     *
     * @return <code>true</code> si les annonces doivent être importées
     */
    private boolean isImportRegisterStatusEnabled() {
        return "1".equals(
                JadePropertiesService.getInstance().getProperty(ALConstRafam.IMPORT_REGISTER_STATUS_IS_ENABLED));
    }

    private boolean isRecipientIdValid(SedexMessage messageCentrale, List<String> recipientIdMessage)
            throws JadeSedexDirectoryInitializationException {

        boolean isValid = false;

        if ((recipientIdMessage == null) || (recipientIdMessage.size() == 0)) {
            JadeLogger.error(this,
                    new StringBuffer("Le message sedex RAFam")
                            .append(((SimpleSedexMessage) messageCentrale).fileLocation)
                            .append(" ne contient pas de recipientId et n'a pas pu être traité").toString());
        } else if (recipientIdMessage.size() > 1) {
            JadeLogger.error(this,
                    new StringBuffer("Le message sedex RAFam")
                            .append(((SimpleSedexMessage) messageCentrale).fileLocation)
                            .append(" contient plusieurs recipientId et n'a pas pu être traité").toString());

        } else if (!recipientIdMessage.get(0)
                .equals(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId())) {
            JadeLogger.error(this,
                    new StringBuffer("Le recipientId du message sedex RAFam")
                            .append(((SimpleSedexMessage) messageCentrale).fileLocation)
                            .append(" ne correspond pas à la caisse et n'a pas pu être traité").toString());
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void logException(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole, Object message,
            Exception e, String recordNumber) {
        StringBuffer cause = new StringBuffer();
        if (e.getCause() != null) {
            cause = cause.append(", cause : ").append(e.getCause().getMessage());
        }

        try {
            addErrorProtocole(protocole, message, new StringBuffer(e.getMessage()).append(cause).toString());
        } catch (JadeApplicationServiceNotAvailableException e1) {
            // DO NOTHING : l'erreur est dans tous les cas logguée
        }

        JadeLogger.error(this, "Une erreur s'est produite pendant l'importation de l'annonce recordNumber "
                + recordNumber + " : " + e.getMessage() + cause.toString());
    }

    private void processDelegues(Message m) {
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // Génération fichiers employeurs délégués
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        try {

            if (m.getContent().getReceipt().size() > 0) {
                createFichiersRetoursDelegue(m.getHeader(), RafamTypeAnnonce._69A_RECEIPT);
            } else if (m.getContent().getNotice().size() > 0) {
                createFichiersRetoursDelegue(m.getHeader(), RafamTypeAnnonce._69D_NOTICE);
            } else if (m.getContent().getUPISynchronizationRecord().size() > 0) {
                createFichiersRetoursDelegue(m.getHeader(), RafamTypeAnnonce._69B_SYNCHRO_UPI);
            } else if (m.getContent().getRegisterStatus() != null) {
                createFichiersRetoursDelegue(m.getHeader(), RafamTypeAnnonce._69C_REGISTER_STATUS);
            }

        } catch (Exception e) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant la création des fichiers d'annonces des employeurs délégué "
                            + e.getMessage());
        }

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // envoi mail après importation des annonces déléguées initiales selon registre
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // Envoi d'un mail indiquant que le module af-delegue, arrêté lors de l'installation de la v 1-11-0
        // peut être redémarré
        if (MessageRegisterStatusRecordDelegueHandler.canSendMailReactivationModuleAfDelegue) {

            try {
                StringBuffer mailContent = new StringBuffer(
                        "Les annonces des employeurs provenant de la CdC ont été importées dans votre base de données \n");

                mailContent.append("Veuillez svp à présent redémarrer le/les batch/s af-delegue");
                ALRafamUtils.sendMail(new StringBuffer("Af-delegue : annonces en base de données"), mailContent,
                        new String[0]);
            } catch (Exception e) {
                JadeLogger.error(this,
                        "Une erreur s'est produite pendant l'envoi du mail demandant la ré-activation du module af-delegue"
                                + e.getMessage());
            } finally {
                MessageRegisterStatusRecordDelegueHandler.canSendMailReactivationModuleAfDelegue = false;
            }
        }
    }

    /**
     * Traite les annonces de type notice (69d) contenues dans le message passé en paramètre. Pour chaque annonce, un
     * handler est instancié et exécuté.
     *
     * @param errors
     *            buffer destiné à recevoir d'éventuels message d'erreurs
     * @param message
     *            message sedex
     *
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler
     */
    protected void processNotices(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole, Message message) {

        for (NoticeType notice : message.getContent().getNotice()) {
            try {

                AnnonceRafamModel annonce = new MessageNoticeNewXSDVersionHandler(notice).traiterMessage(null);
                AnnonceRafamSedexService serviceAnnonceRafamSedex = ALImplServiceLocator.getAnnonceRafamSedexService();

                if (serviceAnnonceRafamSedex.isAnnonceEmployeurDelegue(notice.getRecordNumber().toString())) {
                    MessageEmployeurDelegueHandler afDelegueHandler = new MessageEmployeurDelegueHandler(annonce);
                    afDelegueHandler.traiterMessage(null);
                }

                if (JadeThread.logMessages() != null) {
                    addErrorProtocole(protocole, notice, new JadeBusinessMessageRendererDefaultStringAdapter()
                            .render(JadeThread.logMessages(), JadeThread.currentLanguage()));
                    JadeThread.rollbackSession();
                    JadeThread.logClear();
                } else {
                    JadeThread.commitSession();
                }

            } catch (ALAnnonceRafamNotFoundException e) {

                try {
                    // si l'annonce est annulée à la centrale il n'est pas nécessaire de logguer l'erreur
                    if (!RafamReturnCode.ANNULEE
                            .equals(RafamReturnCode.getRafamReturnCode(String.valueOf(notice.getReturnCode())))) {
                        logException(protocole, notice, e, notice.getRecordNumber().toString());
                    }
                } catch (JadeApplicationException e1) {
                    logException(protocole, notice, e, notice.getRecordNumber().toString());
                }

            } catch (Exception e) {
                logException(protocole, notice, e, notice.getRecordNumber().toString());
            }
        }
    }

    /**
     * Traite les annonces de type reçu (69a) contenues dans le message passé en paramètre. Pour chaque annonce, un
     * handler est instancié et exécuté.
     *
     * @param errors
     *            buffer destiné à recevoir d'éventuels message d'erreurs
     * @param message
     *            message sedex
     *
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler
     */
    protected void processReceipts(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole, Message message) {

        for (ReceiptType receipt : message.getContent().getReceipt()) {
            try {

                AnnonceRafamModel annonce = new MessageReceiptNewXSDVersionHandler(receipt).traiterMessage(null);

                AnnonceRafamSedexService serviceAnnonceRafam = ALImplServiceLocator.getAnnonceRafamSedexService();

                // TODO rajouter premier paramètre (receipt.getInternalOfficeReference())
                if (serviceAnnonceRafam.isAnnonceEmployeurDelegue(null, receipt.getRecordNumber().toString())) {
                    MessageEmployeurDelegueHandler afDelegueHandler = new MessageEmployeurDelegueHandler(annonce);
                    afDelegueHandler.traiterMessage(null);
                }

                if (JadeThread.logMessages() != null) {
                    addErrorProtocole(protocole, receipt, new JadeBusinessMessageRendererDefaultStringAdapter()
                            .render(JadeThread.logMessages(), JadeThread.currentLanguage()));
                    JadeThread.rollbackSession();
                    JadeThread.logClear();
                } else {
                    JadeThread.commitSession();
                }

            } catch (ALAnnonceRafamNotFoundException e) {

                try {
                    // si l'annonce est annulée à la centrale il n'est pas nécessaire de logguer l'erreur
                    if (!RafamReturnCode.ANNULEE
                            .equals(RafamReturnCode.getRafamReturnCode(String.valueOf(receipt.getReturnCode())))) {
                        logException(protocole, receipt, e, receipt.getRecordNumber().toString());
                    }
                } catch (JadeApplicationException e1) {
                    logException(protocole, receipt, e, receipt.getRecordNumber().toString());
                }

            } catch (Exception e) {
                logException(protocole, receipt, e, receipt.getRecordNumber().toString());
            }
        }
    }

    /**
     * Traite les annonces d'état du registre (69c) contenues dans le message passé en paramètre. Pour chaque annonce,
     * un handler est instancié et exécuté.
     *
     * @param errors
     *            buffer destiné à recevoir d'éventuels message d'erreurs
     * @param message
     *            message sedex
     *
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler
     */
    protected void processRegisterStatusRecords(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole,
            Message message, XMLGregorianCalendar messageDate) {

        if ((isImportRegisterStatusEnabled() || isImportRegisterStatusDelegateEnabled())
                && (message.getContent().getRegisterStatus() != null)) {
            MessageRegisterNewXSDVersionStatusRecordDelegueHandler afDelegueHandler = new MessageRegisterNewXSDVersionStatusRecordDelegueHandler();
            try {

                afDelegueHandler.setAreAnnoncesDelegueInDb(
                        ALServiceLocator.getAnnonceRafamDelegueBusinessService().isAnnoncesInDb());
            } catch (Exception e) {
                // si on arrive pas à tester si il y a déjà des annonces délégués, on part du principe que c'est
                // il ne faut pas faire d'import initial
                afDelegueHandler.setAreAnnoncesDelegueInDb(true);
            }

            for (RegisterStatusRecordType register : message.getContent().getRegisterStatus()
                    .getRegisterStatusRecord()) {

                AnnonceRafamSedexService serviceAnnonceRafamSedex = null;

                try {

                    serviceAnnonceRafamSedex = ALImplServiceLocator.getAnnonceRafamSedexService();

                    if (isImportRegisterStatusDelegateEnabled() && serviceAnnonceRafamSedex
                            .isAnnonceEmployeurDelegue(register.getRecordNumber().toString())) {
                        afDelegueHandler.setMessage(register);
                        afDelegueHandler.traiterMessage(null);

                    } else if (isImportRegisterStatusEnabled() && !serviceAnnonceRafamSedex
                            .isAnnonceEmployeurDelegue(register.getRecordNumber().toString())) {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("messageDate", messageDate);
                        afDelegueHandler.setMessage(register);
                        afDelegueHandler.traiterMessage(params);
                    }

                    if (JadeThread.logMessages() != null) {
                        addErrorProtocole(protocole, register, new JadeBusinessMessageRendererDefaultStringAdapter()
                                .render(JadeThread.logMessages(), JadeThread.currentLanguage()));
                        JadeThread.rollbackSession();
                        JadeThread.logClear();

                    } else {
                        JadeThread.commitSession();
                    }

                } catch (ALAnnonceRafamNotFoundException e) {
                    // si l'annonce est annulée à la centrale il n'est pas nécessaire de logguer l'erreur
                    if ((register.getCanceled() != null) && (0 == register.getCanceled())) {
                        logException(protocole, register, e, register.getRecordNumber().toString());
                    }
                } catch (Exception e) {
                    logException(protocole, register, e, register.getRecordNumber().toString());
                }
            }
        }
    }

    /**
     * Traite les annonces de synchronisation UPI (69b) contenues dans le message passé en paramètre. Pour chaque
     * annonce, un handler est instancié et exécuté.
     *
     * @param errors
     *            buffer destiné à recevoir d'éventuels message d'erreurs
     * @param m
     *            message sedex
     *
     * @see ch.globaz.al.businessimpl.rafam.sedex.handler.MessageHandler
     */
    protected void processUPISynchronizationRecords(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole,
            Message m) {

        for (UPISynchronizationRecordType synchro : m.getContent().getUPISynchronizationRecord()) {
            try {

                AnnonceRafamModel annonce = new MessageUPISynchronizationNewXSDVersionRecordHandler(synchro)
                        .traiterMessage(null);

                AnnonceRafamSedexService serviceAnnonceRafam = ALImplServiceLocator.getAnnonceRafamSedexService();

                if (serviceAnnonceRafam.isAnnonceEmployeurDelegue(synchro.getRecordNumber().toString())) {
                    MessageEmployeurDelegueHandler afDelegueHandler = new MessageEmployeurDelegueHandler(annonce);
                    afDelegueHandler.traiterMessage(null);
                }

                if (JadeThread.logMessages() != null) {
                    addErrorProtocole(protocole, synchro, new JadeBusinessMessageRendererDefaultStringAdapter()
                            .render(JadeThread.logMessages(), JadeThread.currentLanguage()));
                    JadeThread.rollbackSession();
                    JadeThread.logClear();

                } else {
                    JadeThread.commitSession();
                }

            } catch (ALAnnonceRafamNotFoundException e) {

                try {
                    // si l'annonce est annulée à la centrale il n'est pas nécessaire de logguer l'erreur
                    if (!RafamReturnCode.ANNULEE
                            .equals(RafamReturnCode.getRafamReturnCode(String.valueOf(synchro.getReturnCode())))) {
                        logException(protocole, synchro, e, synchro.getRecordNumber().toString());
                    }
                } catch (JadeApplicationException e1) {
                    logException(protocole, synchro, e, synchro.getRecordNumber().toString());
                }

            } catch (Exception e) {
                logException(protocole, synchro, e, synchro.getRecordNumber().toString());
            }
        }
    }

    /**
     * Envoie un e-mail à l'adresse de l'utilisateur connecté
     *
     * @param mailContent
     *            contenu du message
     * @param recipientId
     *            recipient ID contenu dans le message sedex
     * @throws Exception
     *             Exception levée si l'e-mail n'a pas pu être envoyée
     */
    protected void sendMailError(StringBuffer mailContent, String recipientId) throws Exception {

        StringBuffer title = new StringBuffer("Erreur pendant l'importation des annonces RAFam (");
        title.append(recipientId);
        try {
            title.append(", ").append(ALServiceLocator.getParametersServices().getNomCaisse());
        } catch (Exception e) {
            // DO NOTHING, pas un problème si le nom de la caisse n'a pas pu être récupéré (cf : recipientId)
        }

        JadeSmtpClient.getInstance().sendMail(JadeThread.currentUserEmail(), title.toString(), mailContent.toString(),
                new String[0]);

    }

    @Setup
    public void setUp(Properties properties)
            throws JadeDecryptionNotSupportedException, JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "Réception message RAFam: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message RAFam: user sedex non renseigné. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "Réception message RAFam: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message RAFam: pass sedex non renseigné. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);
    }

    /**
     * Charge le message sedex
     *
     * @param message
     *            Le message à charger
     * @return l'objet message
     *
     * @throws JadeApplicationException
     *             Exception levée si le message ne peut être traité
     */
    private Message unmarshalMessage(SedexMessage message) throws JadeApplicationException {

        if (!(message instanceof SimpleSedexMessage)) {
            throw new ALRafamSedexException("Le message sedex " + message.getFileLocation()
                    + " n'est pas un message SimpleSedexMessage et n'a pas été traité");
        } else {

            Object brutMessage = null;
            try {
                JAXBServices jaxbService = JAXBServices.getInstance();
                brutMessage = jaxbService.unmarshal(((SimpleSedexMessage) message).fileLocation, false, false,
                        (Class<?>[]) null);

                if (brutMessage instanceof Message) {
                    return (Message) brutMessage;
                } else {
                    throw new ALRafamSedexException("Le message sedex " + message.getFileLocation()
                            + " n'est pas un message ch.ech.xmlns.ech_0104_69._3.Message et n'a pas été traité");
                }
            } catch (Exception e) {
                throw new ALRafamSedexException("Une erreur s'est produite pendant la lecture du message sedex "
                        + message.getFileLocation() + " : " + e.getMessage());
            }
        }
    }
}