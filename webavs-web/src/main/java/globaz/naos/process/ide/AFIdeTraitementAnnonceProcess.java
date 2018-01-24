package globaz.naos.process.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.ide.AFIdeAnnonce;
import globaz.naos.db.ide.AFIdeAnnonceManager;
import globaz.naos.exceptions.AFIdeNumberNoMatchException;
import globaz.naos.listes.excel.AFXmlmlIdeTraitementAnnonce;
import globaz.naos.listes.excel.AFXmlmlIdeTraitementAnnonceEntranteActive;
import globaz.naos.listes.excel.AFXmlmlIdeTraitementAnnonceEntrantePassive;
import globaz.naos.properties.AFProperties;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceCallUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonExcelmlUtils;
import idech.admin.bit.xmlns.uid_wse_shared._1.RegisterDeregisterStatus;
import idech.admin.uid.xmlns.uid_wse.ArrayOfUidStructureType;
import idech.admin.uid.xmlns.uid_wse.IPartnerServices;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterBusinessFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterInfrastructureFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterScheduledBusinessFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterScheduledInfrastructureFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterScheduledSecurityFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterSecurityFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterBusinessFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterInfrastructureFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterScheduledBusinessFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterScheduledInfrastructureFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterScheduledSecurityFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterSecurityFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesSubscribeBusinessFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesSubscribeInfrastructureFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesSubscribeSecurityFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesUnsubscribeBusinessFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesUnsubscribeInfrastructureFaultFaultFaultMessage;
import idech.admin.uid.xmlns.uid_wse.IPartnerServicesUnsubscribeSecurityFaultFaultFaultMessage;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import ch.globaz.common.properties.PropertiesException;

public class AFIdeTraitementAnnonceProcess extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = 6232985970478302658L;

    private static final String PATH_SEDEX_FILE_EXEMPLE = "D:\\sedexMessage";
    private static final String MAIL_OBJECT_ANNONCE_IDE = "MAIL_OBJECT_ANNONCE_IDE";

    public static final String CODE_NOGA_INCONNU = "990099";

    // private final static String MAIL_NOTIFICATION_PROPERTY = "naos.ide.webservice.mailnotification.group";

    private String mailObject;
    private String forTypeTraitement;
    private Boolean modeTestSedex;
    private IPartnerServices port = null;

    public Boolean getModeTestSedex() {
        return modeTestSedex;
    }

    public void setModeTestSedex(Boolean modeTestSedex) {
        this.modeTestSedex = modeTestSedex;
    }

    public String getForTypeTraitement() {
        return forTypeTraitement;
    }

    public void setForTypeTraitement(String forTypeTraitement) {
        this.forTypeTraitement = forTypeTraitement;
    }

    public AFIdeTraitementAnnonceProcess() {
        super();
        mailObject = MAIL_OBJECT_ANNONCE_IDE;
        modeTestSedex = Boolean.FALSE;
        forTypeTraitement = "";

    }

    @Override
    protected void _executeCleanUp() {
    }

    private List<AFIdeAnnonce> reloadListAnnonce(List<AFIdeAnnonce> listAnnonceIde) throws Exception {

        if (listAnnonceIde.size() > 0) {

            List<String> listIdAnnonceToReload = new ArrayList<String>();

            for (AFIdeAnnonce ideAnnonce : listAnnonceIde) {
                listIdAnnonceToReload.add(ideAnnonce.getIdeAnnonceIdAnnonce());
            }

            AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
            ideAnnonceManager.setSession(getSession());
            ideAnnonceManager.setInIdAnnonce(listIdAnnonceToReload);
            ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

            listAnnonceIde.clear();

            for (int i = 0; i < ideAnnonceManager.size(); i++) {
                listAnnonceIde.add((AFIdeAnnonce) ideAnnonceManager.getEntity(i));
            }
        }

        return listAnnonceIde;

    }

    private List<AFIdeAnnonce> traiterAnnonceEnAttente(List<AFIdeAnnonce> listAnnonceIde) throws Exception {

        for (AFIdeAnnonce ideAnnonce : listAnnonceIde) {

            try {

                viderErreurProcess();

                boolean isAnnonceMutationToPutInAttente = CodeSystem.TYPE_ANNONCE_IDE_MUTATION
                        .equalsIgnoreCase(ideAnnonce.getIdeAnnonceType());
                isAnnonceMutationToPutInAttente = isAnnonceMutationToPutInAttente
                        && (CodeSystem.STATUT_IDE_PROVISOIRE.equalsIgnoreCase(ideAnnonce.getStatutIde())
                                || CodeSystem.STATUT_IDE_MUTATION.equalsIgnoreCase(ideAnnonce.getStatutIde()));
                boolean isAnnonceToPutInAttente = ideAnnonce.isAnnonceEnAttente();

                if (isAnnonceMutationToPutInAttente) {
                    ideAnnonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ATTENTE);
                    ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
                    ideAnnonce.setMessageErreurForBusinessUser(
                            getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_ANNONCE_MUTATION_EN_ATTENTE"));
                    ideAnnonce.update(getTransaction());
                } else if (isAnnonceToPutInAttente) {
                    ideAnnonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ATTENTE);
                    ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
                    ideAnnonce.setMessageErreurForBusinessUser(FWMessageFormat.format(
                            getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_ANNONCE_EN_ATTENTE"),
                            ideAnnonce.getTypeAnnonceDate()));
                    ideAnnonce.update(getTransaction());
                } else if (CodeSystem.ETAT_ANNONCE_IDE_ATTENTE.equalsIgnoreCase(ideAnnonce.getIdeAnnonceEtat())) {
                    ideAnnonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
                    ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
                    ideAnnonce.setMessageErreurForBusinessUser("");
                    ideAnnonce.update(getTransaction());
                }

            } catch (Exception e) {
                AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e);
                getTransaction().addErrors(e.getMessage());
            } finally {
                handleTransaction(ideAnnonce.getIdeAnnonceIdAnnonce());
            }

        }

        return reloadListAnnonce(listAnnonceIde);

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(false);
        setSendMailOnError(false);

        List<String> listDestinataire = new ArrayList<String>();
        List<String> listAttachedDocumentLocation = new ArrayList<String>();

        try {

            port = IDEServiceCallUtil.initService();

            // Annonces entrantes
            if (JadeStringUtil.isBlankOrZero(forTypeTraitement)
                    || CodeSystem.CATEGORIE_ANNONCE_IDE_RECEPTION.equalsIgnoreCase(forTypeTraitement)) {

                if (modeTestSedex) {
                    genererAnnonceEntrante();
                }
                List<AFIdeAnnonce> listAnnonceEntrante = new ArrayList<AFIdeAnnonce>();
                listAnnonceEntrante = initialiseListAnnonceEntranteInfoAbo();
                List<AFIdeAnnonce> listAnnonceEntranteNotInfoAbo = initialiseListAnnonceEntranteNonInfoAbo();
                listAnnonceEntrante = traiterAnnonceEntrante(listAnnonceEntrante);

                genererExcelResultTraitementAnnonceEntranteActive(listAnnonceEntrante);
                // fusion avec les nonInfoAbo
                listAnnonceEntrante.addAll(listAnnonceEntranteNotInfoAbo);
                genererExcelResultTraitementAnnonceEntrantePassive(listAnnonceEntrante);
            }

            if (JadeStringUtil.isBlankOrZero(forTypeTraitement)
                    || CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI.equalsIgnoreCase(forTypeTraitement)) {

                List<AFIdeAnnonce> listAnnonceSortanteUnitaireCreation = new ArrayList<AFIdeAnnonce>();
                List<AFIdeAnnonce> listAnnonceSortanteUnitaireExceptedCreation = new ArrayList<AFIdeAnnonce>();
                List<AFIdeAnnonce> listAnnonceSortanteEnregistrementMasse = new ArrayList<AFIdeAnnonce>();
                List<AFIdeAnnonce> listAnnonceSortanteDesenregistrementMasse = new ArrayList<AFIdeAnnonce>();

                // Annonces sortantes d'enregistrement masse
                listAnnonceSortanteEnregistrementMasse = initialiseListAnnonceSortanteEnregistrementEnMasse();
                listAnnonceSortanteEnregistrementMasse = traiterAnnonceEnAttente(
                        listAnnonceSortanteEnregistrementMasse);
                listAnnonceSortanteEnregistrementMasse = traiterAnnonceSortanteMasse(port,
                        listAnnonceSortanteEnregistrementMasse);

                // Annonces sortantes unitaire (cas individuel)
                listAnnonceSortanteUnitaireCreation = initialiseListAnnonceSortanteUnitaireCreation();
                listAnnonceSortanteUnitaireCreation = traiterAnnonceSortanteUnitaireCreation(port,
                        listAnnonceSortanteUnitaireCreation);

                listAnnonceSortanteUnitaireExceptedCreation = initialiseListAnnonceSortanteUnitaireExceptedCreation();
                listAnnonceSortanteUnitaireExceptedCreation = traiterAnnonceEnAttente(
                        listAnnonceSortanteUnitaireExceptedCreation);
                listAnnonceSortanteUnitaireExceptedCreation = traiterAnnonceSortanteUnitaireExceptedCreation(port,
                        listAnnonceSortanteUnitaireExceptedCreation);

                // Annonces sortantes de désenregistrement masse
                listAnnonceSortanteDesenregistrementMasse = initialiseListAnnonceSortanteDesenregistrementEnMasse();
                listAnnonceSortanteDesenregistrementMasse = traiterAnnonceEnAttente(
                        listAnnonceSortanteDesenregistrementMasse);
                listAnnonceSortanteDesenregistrementMasse = traiterAnnonceSortanteMasse(port,
                        listAnnonceSortanteDesenregistrementMasse);

                List<AFIdeAnnonce> listAnnonceSortanteForExcel = new ArrayList<AFIdeAnnonce>();
                listAnnonceSortanteForExcel.addAll(listAnnonceSortanteEnregistrementMasse);
                listAnnonceSortanteForExcel.addAll(listAnnonceSortanteUnitaireCreation);
                listAnnonceSortanteForExcel.addAll(listAnnonceSortanteUnitaireExceptedCreation);
                listAnnonceSortanteForExcel.addAll(listAnnonceSortanteDesenregistrementMasse);

                genererExcelResultTraitementAnnonceSortante(listAnnonceSortanteForExcel);
            }

        } catch (Exception e) {
            String messageForUser = AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e);
            getMemoryLog().logMessage(messageForUser, FWMessage.ERREUR, this.getClass().getName());
        } finally {
            try {
                listDestinataire = giveMeListDestinataire(listDestinataire);
                listAttachedDocumentLocation = giveMeListAttachedDocumentsLocation();
            } catch (Exception e2) {
                AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e2);
                getMemoryLog().logMessage(e2.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            } finally {

                JadeSmtpClient.getInstance().sendMail(listDestinataire.toArray(new String[listDestinataire.size()]),
                        getEMailObject(), getSubjectDetail(),
                        listAttachedDocumentLocation.toArray(new String[listAttachedDocumentLocation.size()]));
            }

        }

        return !(isAborted() || isOnError() || getSession().hasErrors());

    }

    private List<AFIdeAnnonce> extractOtherThanInfoAbo(List<AFIdeAnnonce> listAnnonceEntrante) {
        List<AFIdeAnnonce> annonceNotInfoAbo = new ArrayList<AFIdeAnnonce>();
        for (AFIdeAnnonce annonceEntrante : listAnnonceEntrante) {
            if (AFIDEUtil.isAnnoncePassiveNonInfoAbo(annonceEntrante)) {
                listAnnonceEntrante.remove(annonceEntrante);
                annonceNotInfoAbo.add(annonceEntrante);
            }
        }
        return annonceNotInfoAbo;
    }

    private List<String> giveMeListDestinataire(List<String> listDestinataire) throws Exception {

        listDestinataire = AFIDEUtil.giveMeUserGroupMail(AFProperties.MAIL_NOTIFICATION_PROPERTY.getValue());

        String processExecutorMail = getEMailAddress();

        if (!JadeStringUtil.isBlankOrZero(processExecutorMail) && !listDestinataire.contains(processExecutorMail)) {
            listDestinataire.add(processExecutorMail);
        }

        return listDestinataire;

    }

    private List<String> giveMeListAttachedDocumentsLocation() {

        List<JadePublishDocument> listAttachedDocument = getAttachedDocuments();
        List<String> listAttachedDocumentLocation = new ArrayList<String>();

        for (JadePublishDocument aPublishDocument : listAttachedDocument) {
            listAttachedDocumentLocation.add(aPublishDocument.getDocumentLocation());
        }

        return listAttachedDocumentLocation;

    }

    /**
     * <b>For internal use ONLY</b>
     * </br>
     * Génération de test de SEDEX (simulation depuis un folder local) à des fins de testing, simulation et
     * réalisation
     * 
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    private void genererAnnonceEntrante()
            throws JadeDecryptionNotSupportedException, JadeEncrypterNotFoundException, Exception {
        List<String> listPathInfoAboSedexFile = JadeFsFacade.getFolderChildren(PATH_SEDEX_FILE_EXEMPLE);
        AFIdeReceptionMessageInfoAboSedexProcess ideReceptionMessageInfoAboSedexProcess = new AFIdeReceptionMessageInfoAboSedexProcess();
        Properties properties = new Properties();
        properties.setProperty("userSedex", "S7FNHNEbzm4=");
        properties.setProperty("passSedex", "d5UwBcw1VTk=");
        ideReceptionMessageInfoAboSedexProcess.setUp(properties);
        for (String aPathInfoAboSedexFile : listPathInfoAboSedexFile) {
            SimpleSedexMessage sedexMessge = new SimpleSedexMessage();
            sedexMessge.fileLocation = aPathInfoAboSedexFile;
            ideReceptionMessageInfoAboSedexProcess.onReceive(sedexMessge);
        }

    }

    private List<AFIdeAnnonce> initialiseListAnnonceEntranteNonInfoAbo() throws Exception {

        return initialiseListAnnonceEntrante(false);

    }

    private List<AFIdeAnnonce> initialiseListAnnonceEntranteInfoAbo() throws Exception {

        return initialiseListAnnonceEntrante(true);

    }

    private List<AFIdeAnnonce> initialiseListAnnonceEntrante(boolean infoAbo) throws Exception {

        List<AFIdeAnnonce> listAnnonceEntrante = new ArrayList<AFIdeAnnonce>();

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(getSession());
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_RECEPTION);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeATraiter());
        if (infoAbo) {
            ideAnnonceManager.setNotInType(AFIDEUtil.getListTypeAnnonceEntranteAutreInfoAbo());
        } else {
            ideAnnonceManager.setInType(AFIDEUtil.getListTypeAnnonceEntranteAutreInfoAbo());
        }

        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ideAnnonceManager.size(); i++) {
            listAnnonceEntrante.add((AFIdeAnnonce) ideAnnonceManager.getEntity(i));
        }

        return listAnnonceEntrante;

    }

    /**
     * une annonce sortante en masse est une annonces de type (dé)enregistrement, de masse parce que traité en masse
     * dans un seul call du WebService
     */
    private List<AFIdeAnnonce> initialiseListAnnonceSortanteEnregistrementEnMasse() throws Exception {

        List<AFIdeAnnonce> listAnnonceSortanteEnregistrementMasse = new ArrayList<AFIdeAnnonce>();

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(getSession());
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setInType(AFIDEUtil.getListTypeAnnonceIdeSortanteEnregistrement());
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeATraiter());
        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ideAnnonceManager.size(); i++) {
            // On prend la même liste que les cas unitaires pour les avoir dans la liste excel récapitulative
            listAnnonceSortanteEnregistrementMasse.add((AFIdeAnnonce) ideAnnonceManager.getEntity(i));
        }

        return listAnnonceSortanteEnregistrementMasse;

    }

    private List<AFIdeAnnonce> initialiseListAnnonceSortanteDesenregistrementEnMasse() throws Exception {

        List<AFIdeAnnonce> listAnnonceSortanteDesenregistrementMasse = new ArrayList<AFIdeAnnonce>();

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(getSession());
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setInType(AFIDEUtil.getListTypeAnnonceIdeSortanteDesenregistrement());
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeATraiter());
        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ideAnnonceManager.size(); i++) {
            // On prend la même liste que les cas unitaires pour les avoir dans la liste excel récapitulative
            listAnnonceSortanteDesenregistrementMasse.add((AFIdeAnnonce) ideAnnonceManager.getEntity(i));
        }

        return listAnnonceSortanteDesenregistrementMasse;

    }

    private List<AFIdeAnnonce> initialiseListAnnonceSortanteUnitaireCreation() throws Exception {
        List<AFIdeAnnonce> listAnnonceSortanteUnitaire = new ArrayList<AFIdeAnnonce>();

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(getSession());
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setForType(CodeSystem.TYPE_ANNONCE_IDE_CREATION);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeATraiter());
        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ideAnnonceManager.size(); i++) {

            AFIdeAnnonce current = (AFIdeAnnonce) ideAnnonceManager.getEntity(i);

            listAnnonceSortanteUnitaire.add(current);

        }

        return listAnnonceSortanteUnitaire;

    }

    private List<AFIdeAnnonce> initialiseListAnnonceSortanteUnitaireExceptedCreation() throws Exception {
        List<AFIdeAnnonce> listAnnonceSortanteUnitaire = new ArrayList<AFIdeAnnonce>();

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(getSession());
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);

        List<String> listNotInType = AFIDEUtil.getListTypeAnnonceIdeSortanteAIgnorer();
        listNotInType.add(CodeSystem.TYPE_ANNONCE_IDE_CREATION);
        ideAnnonceManager.setNotInType(listNotInType);

        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeATraiter());
        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ideAnnonceManager.size(); i++) {

            AFIdeAnnonce current = (AFIdeAnnonce) ideAnnonceManager.getEntity(i);

            listAnnonceSortanteUnitaire.add(current);

        }

        return listAnnonceSortanteUnitaire;

    }

    /**
     * sortir les annonces sortantes non traitées, en état SUSPENDU
     */
    private List<AFIdeAnnonce> getListAnnonceSuspendu() throws Exception {

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(getSession());
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setNotInType(AFIDEUtil.getListTypeAnnonceIdeSortanteAIgnorer());
        ideAnnonceManager.setInEtat(Arrays.asList(CodeSystem.ETAT_ANNONCE_IDE_SUSPENDU));
        ideAnnonceManager.setNotInStatutIDEAffiliation(AFIDEUtil.getListEtatIDEAffiliationNonAutorise());
        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);
        List<AFIdeAnnonce> listAnnonce = new ArrayList<AFIdeAnnonce>();
        for (int i = 0; i < ideAnnonceManager.size(); i++) {
            // On prend la même liste que les cas de masse pour les avoir dans la liste excel récapitulative
            listAnnonce.add((AFIdeAnnonce) ideAnnonceManager.getEntity(i));
        }
        return listAnnonce;
    }

    private void viderErreurProcess() {

        getMemoryLog().clear();
        getSession().getErrors();
        getSession().getWarnings();
        getTransaction().clearErrorBuffer();
        getTransaction().clearWarningBuffer();

    }

    private List<AFIdeAnnonce> traiterAnnonceSortanteUnitaireCreation(IPartnerServices port,
            List<AFIdeAnnonce> listAnnonceSortanteUnitaire) throws Exception {

        for (AFIdeAnnonce ideAnnonce : listAnnonceSortanteUnitaire) {

            String businessError = "";
            IDEDataBean ideDataBean = new IDEDataBean();

            try {

                viderErreurProcess();

                businessError = AFIDEUtil.checkEnvoiAnnonceCreationMandatory(getSession(),
                        ideAnnonce.getIdeAnnonceIdAffiliation());

                ideAnnonce.setMessageErreurForBusinessUser(businessError);

                if (JadeStringUtil.isBlankOrZero(businessError)) {
                    ideDataBean = AFIDEUtil.transformIdeAnnonceEnIdeDataBean(ideAnnonce, getSession());
                    AFIDEUtil.createHistoriqueDataInAnnonce(getSession(), ideDataBean, ideAnnonce);
                    ideDataBean = IDEServiceCallUtil.createEntiteIde(getSession(), ideDataBean, port);
                }

            } catch (Exception e) {
                AFIDEUtil.handleError(getSession(), e, ideAnnonce);

            } finally {

                try {
                    doUpdateAfterTraitementAnnonceSortante(ideDataBean, ideAnnonce);
                } catch (Exception e2) {
                    AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e2);
                    getTransaction().addErrors(e2.getMessage());
                } finally {
                    handleTransaction(ideAnnonce.getIdeAnnonceIdAnnonce());
                }

            }
        }

        return reloadListAnnonce(listAnnonceSortanteUnitaire);

    }

    /**
     * traiter les annonces sortantes de type Création, Mutation, Radiation, Réactivation
     */
    private List<AFIdeAnnonce> traiterAnnonceSortanteUnitaireExceptedCreation(IPartnerServices port,
            List<AFIdeAnnonce> listAnnonceSortanteUnitaire) throws Exception {

        for (AFIdeAnnonce ideAnnonce : listAnnonceSortanteUnitaire) {

            if (!CodeSystem.ETAT_ANNONCE_IDE_ATTENTE.equalsIgnoreCase(ideAnnonce.getIdeAnnonceEtat())) {

                String businessError = "";
                IDEDataBean ideDataBean = new IDEDataBean();

                try {

                    viderErreurProcess();

                    if (CodeSystem.TYPE_ANNONCE_IDE_MUTATION.equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessError = AFIDEUtil.checkEnvoiAnnonceMutationMandatory(getSession(),
                                ideAnnonce.getIdeAnnonceIdAffiliation());

                        ideAnnonce.setMessageErreurForBusinessUser(businessError);

                        if (JadeStringUtil.isBlankOrZero(businessError)) {
                            ideDataBean = AFIDEUtil.transformIdeAnnonceEnIdeDataBean(ideAnnonce, getSession());
                            AFIDEUtil.createHistoriqueDataInAnnonce(getSession(), ideDataBean, ideAnnonce);
                            ideDataBean = IDEServiceCallUtil.updateEntiteIde(getSession(), ideDataBean, port);
                        }
                    } else if (CodeSystem.TYPE_ANNONCE_IDE_RADIATION.equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessError = AFIDEUtil.checkEnvoiAnnonceRadiationMandatory(getSession(), ideAnnonce,
                                ideAnnonce.getIdeAnnonceIdAffiliation());

                        ideAnnonce.setMessageErreurForBusinessUser(businessError);

                        if (JadeStringUtil.isBlankOrZero(businessError)) {
                            ideDataBean = AFIDEUtil.transformIdeAnnonceEnIdeDataBean(ideAnnonce, getSession());

                            AFIDEUtil.createHistoriqueDataInAnnonce(getSession(), ideDataBean, ideAnnonce);
                            ideDataBean = IDEServiceCallUtil.radiationEntiteIde(getSession(), ideDataBean, port);

                        }
                    } else if (CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION
                            .equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessError = AFIDEUtil.checkEnvoiAnnonceReactivationMandatory(getSession(), ideAnnonce,
                                ideAnnonce.getIdeAnnonceIdAffiliation());

                        ideAnnonce.setMessageErreurForBusinessUser(businessError);

                        if (JadeStringUtil.isBlankOrZero(businessError)) {
                            ideDataBean = AFIDEUtil.transformIdeAnnonceEnIdeDataBean(ideAnnonce, getSession());
                            AFIDEUtil.createHistoriqueDataInAnnonce(getSession(), ideDataBean, ideAnnonce);
                            ideDataBean = IDEServiceCallUtil.reactivateEntiteIde(getSession(), ideDataBean, port);
                        }
                    }

                } catch (Exception e) {
                    AFIDEUtil.handleError(getSession(), e, ideAnnonce);

                } finally {

                    try {
                        doUpdateAfterTraitementAnnonceSortante(ideDataBean, ideAnnonce);
                    } catch (Exception e2) {
                        AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e2);
                        getTransaction().addErrors(e2.getMessage());
                    } finally {
                        handleTransaction(ideAnnonce.getIdeAnnonceIdAnnonce());
                    }

                }
            }

        }

        return reloadListAnnonce(listAnnonceSortanteUnitaire);

    }

    private void doUpdateAfterTraitementAnnonceSortante(IDEDataBean ideDataBean, AFIdeAnnonce ideAnnonce)
            throws Exception {
        viderErreurProcess();

        String etatAnnonce = CodeSystem.ETAT_ANNONCE_IDE_ERREUR;
        if (!ideAnnonce.hasAnnonceErreur()) {
            AFIDEUtil.updateHistoriqueDataInAnnonce(ideDataBean, ideAnnonce);
            etatAnnonce = CodeSystem.ETAT_ANNONCE_IDE_TRAITE;
            updateAffiliationAfterTraitementAnnonceSortante(ideDataBean, ideAnnonce);
        }

        updateAnnonceAfterTraitementAnnonceSortante(ideDataBean, ideAnnonce, etatAnnonce);
    }

    /**
     * Envoi des annonces en masse (annonce dont le paramètre de la méthode est une liste de numéro ide)
     * 
     * @throws IPartnerServicesDeregisterSecurityFaultFaultFaultMessage
     * @throws IPartnerServicesDeregisterInfrastructureFaultFaultFaultMessage
     * @throws IPartnerServicesDeregisterBusinessFaultFaultFaultMessage
     * @throws MalformedURLException
     * @throws IPartnerServicesDeregisterScheduledSecurityFaultFaultFaultMessage
     * @throws IPartnerServicesDeregisterScheduledInfrastructureFaultFaultFaultMessage
     * @throws IPartnerServicesDeregisterScheduledBusinessFaultFaultFaultMessage
     * @throws IPartnerServicesUnsubscribeSecurityFaultFaultFaultMessage
     * @throws IPartnerServicesUnsubscribeInfrastructureFaultFaultFaultMessage
     * @throws IPartnerServicesUnsubscribeBusinessFaultFaultFaultMessage
     * @throws IPartnerServicesRegisterSecurityFaultFaultFaultMessage
     * @throws IPartnerServicesRegisterInfrastructureFaultFaultFaultMessage
     * @throws IPartnerServicesRegisterBusinessFaultFaultFaultMessage
     * @throws IPartnerServicesRegisterScheduledSecurityFaultFaultFaultMessage
     * @throws IPartnerServicesRegisterScheduledInfrastructureFaultFaultFaultMessage
     * @throws IPartnerServicesRegisterScheduledBusinessFaultFaultFaultMessage
     * @throws IPartnerServicesSubscribeSecurityFaultFaultFaultMessage
     * @throws IPartnerServicesSubscribeInfrastructureFaultFaultFaultMessage
     * @throws IPartnerServicesSubscribeBusinessFaultFaultFaultMessage
     */
    private List<AFIdeAnnonce> traiterAnnonceSortanteMasse(IPartnerServices port,
            List<AFIdeAnnonce> listAnnonceSortanteMasse)
            throws Exception, MalformedURLException, IPartnerServicesDeregisterBusinessFaultFaultFaultMessage,
            IPartnerServicesDeregisterInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeregisterSecurityFaultFaultFaultMessage,
            IPartnerServicesDeregisterScheduledBusinessFaultFaultFaultMessage,
            IPartnerServicesDeregisterScheduledInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeregisterScheduledSecurityFaultFaultFaultMessage,
            IPartnerServicesUnsubscribeBusinessFaultFaultFaultMessage,
            IPartnerServicesUnsubscribeInfrastructureFaultFaultFaultMessage,
            IPartnerServicesUnsubscribeSecurityFaultFaultFaultMessage,
            IPartnerServicesRegisterBusinessFaultFaultFaultMessage,
            IPartnerServicesRegisterInfrastructureFaultFaultFaultMessage,
            IPartnerServicesRegisterSecurityFaultFaultFaultMessage,
            IPartnerServicesRegisterScheduledBusinessFaultFaultFaultMessage,
            IPartnerServicesRegisterScheduledInfrastructureFaultFaultFaultMessage,
            IPartnerServicesRegisterScheduledSecurityFaultFaultFaultMessage,
            IPartnerServicesSubscribeBusinessFaultFaultFaultMessage,
            IPartnerServicesSubscribeInfrastructureFaultFaultFaultMessage,
            IPartnerServicesSubscribeSecurityFaultFaultFaultMessage {

        Map<String, ArrayOfUidStructureType> mapTypeAnnonceListCas = new HashMap<String, ArrayOfUidStructureType>();
        Map<String, Map<String, AFIdeAnnonce>> mapTypeAnnonceMapNumIdeAnnonce = new HashMap<String, Map<String, AFIdeAnnonce>>();

        for (AFIdeAnnonce ideAnnonce : listAnnonceSortanteMasse) {

            if (!CodeSystem.ETAT_ANNONCE_IDE_ATTENTE.equalsIgnoreCase(ideAnnonce.getIdeAnnonceEtat())) {

                IDEDataBean ideDataBean = new IDEDataBean();
                viderErreurProcess();

                try {
                    String businessErrorMessage = "";
                    if (CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF
                            .equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessErrorMessage = AFIDEUtil.checkEnvoiAnnonceEnregistrementActifMandatory(getSession(),
                                ideAnnonce, ideAnnonce.getIdeAnnonceIdAffiliation());
                    } else if (CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF
                            .equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessErrorMessage = AFIDEUtil.checkEnvoiAnnonceEnregistrementPassifMandatory(getSession(),
                                ideAnnonce, ideAnnonce.getIdeAnnonceIdAffiliation());
                    } else if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF
                            .equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessErrorMessage = AFIDEUtil.checkEnvoiAnnonceDesenregistrementActifMandatory(getSession(),
                                ideAnnonce, ideAnnonce.getIdeAnnonceIdAffiliation());
                    } else if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF
                            .equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
                        businessErrorMessage = AFIDEUtil.checkEnvoiAnnonceDesenregistrementPassifMandatory(getSession(),
                                ideAnnonce, ideAnnonce.getIdeAnnonceIdAffiliation());
                    }

                    ideAnnonce.setMessageErreurForBusinessUser(businessErrorMessage);
                    if (ideAnnonce.hasAnnonceErreur()) {
                        ideAnnonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ERREUR);
                        ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
                        ideAnnonce.update(getTransaction());
                    }

                } catch (Exception e) {
                    AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e);
                    getTransaction().addErrors(e.getMessage());
                } finally {
                    handleTransaction(ideAnnonce.getIdeAnnonceIdAnnonce());
                }

                if (!ideAnnonce.hasAnnonceErreur()) {

                    /* TODO - Voir quelles données à historiser et s'il faut ajouter les périodes d'annonce */
                    ideDataBean = AFIDEUtil.transformIdeAnnonceEnIdeDataBean(ideAnnonce, getSession());
                    AFIDEUtil.createHistoriqueDataInAnnonce(getSession(), ideDataBean, ideAnnonce);

                    ArrayOfUidStructureType listCasForTypeAnnonce = mapTypeAnnonceListCas
                            .get(ideAnnonce.getIdeAnnonceType());
                    if (listCasForTypeAnnonce == null) {
                        listCasForTypeAnnonce = new ArrayOfUidStructureType();
                        mapTypeAnnonceListCas.put(ideAnnonce.getIdeAnnonceType(), listCasForTypeAnnonce);
                    }
                    mapTypeAnnonceListCas.get(ideAnnonce.getIdeAnnonceType()).getUidStructureType()
                            .add(IDEServiceCallUtil.getStructureForDesenregistrement(ideDataBean));

                    String numeroIde = ideDataBean.getNumeroIDE();
                    if (JadeStringUtil.isBlankOrZero(numeroIde)) {
                        numeroIde = ideDataBean.getNumeroIDERemplacement();
                    }

                    Map<String, AFIdeAnnonce> mapNumIdeAnnonce = mapTypeAnnonceMapNumIdeAnnonce
                            .get(ideAnnonce.getIdeAnnonceType());

                    if (mapNumIdeAnnonce == null) {
                        mapNumIdeAnnonce = new HashMap<String, AFIdeAnnonce>();
                        mapTypeAnnonceMapNumIdeAnnonce.put(ideAnnonce.getIdeAnnonceType(), mapNumIdeAnnonce);
                    }
                    mapNumIdeAnnonce.put(AFIDEUtil.giveMeNumIdeUnformatedWithPrefix(numeroIde), ideAnnonce);

                }

            }
        }

        for (Map.Entry<String, ArrayOfUidStructureType> entry : mapTypeAnnonceListCas.entrySet()) {

            Map<String, Map<String, RegisterDeregisterStatus>> mapRetourWebService = new HashMap<String, Map<String, RegisterDeregisterStatus>>();

            if (CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF.equalsIgnoreCase(entry.getKey())) {
                mapRetourWebService.put(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF,
                        IDEServiceCallUtil.enregistrementActif(getSession(), entry.getValue(), port));
            } else if (CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF.equalsIgnoreCase(entry.getKey())) {
                mapRetourWebService.put(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF,
                        IDEServiceCallUtil.enregistrementPassif(getSession(), entry.getValue(), port));
            } else if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF.equalsIgnoreCase(entry.getKey())) {
                mapRetourWebService.put(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF,
                        IDEServiceCallUtil.desenregistrementActif(getSession(), entry.getValue(), port));
            } else if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF.equalsIgnoreCase(entry.getKey())) {
                mapRetourWebService.put(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF,
                        IDEServiceCallUtil.desenregistrementPassif(getSession(), entry.getValue(), port));
            }

            for (Map.Entry<String, Map<String, RegisterDeregisterStatus>> entryMapRetourWebService : mapRetourWebService
                    .entrySet()) {

                for (Map.Entry<String, RegisterDeregisterStatus> entryMapRetourWebServiceMapNumIdeStatus : entryMapRetourWebService
                        .getValue().entrySet()) {

                    viderErreurProcess();

                    try {

                        doUpdateAfterTraitementAnnonceSortanteMasse(
                                mapTypeAnnonceMapNumIdeAnnonce.get(entryMapRetourWebService.getKey())
                                        .get(entryMapRetourWebServiceMapNumIdeStatus.getKey()),
                                entryMapRetourWebServiceMapNumIdeStatus.getValue());

                    } catch (Exception e) {
                        AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e);
                        getTransaction().addErrors(e.getMessage());
                    } finally {
                        handleTransaction(mapTypeAnnonceMapNumIdeAnnonce.get(entryMapRetourWebService.getKey())
                                .get(entryMapRetourWebServiceMapNumIdeStatus.getKey()).getIdeAnnonceIdAnnonce());
                    }
                }

            }

        }

        return reloadListAnnonce(listAnnonceSortanteMasse);

    }

    private void doUpdateAfterTraitementAnnonceSortanteMasse(AFIdeAnnonce annonceIde,
            RegisterDeregisterStatus registerDeregisterStatus) throws Exception {

        String etatAnnonce = CodeSystem.ETAT_ANNONCE_IDE_TRAITE;
        if (AFIDEUtil.isRegisterDeregisterStatusError(registerDeregisterStatus)) {
            etatAnnonce = CodeSystem.ETAT_ANNONCE_IDE_ERREUR;
        }

        String messageErreur = AFIDEUtil.translateRegisterDeregisterStatus(getSession(), registerDeregisterStatus);
        updateAnnonceAfterTraitementAnnonceSortanteMasse(annonceIde, messageErreur, etatAnnonce);
    }

    private void updateAffiliationWithAnnonceEntrante(List<AFAffiliation> listAffiliation,
            AFIdeAnnonce ideAnnonceEntrante, boolean isAnnoncePassive) throws Exception {

        String numeroIDE = ideAnnonceEntrante.getHistNumeroIde();
        String idCodeNoga = null;
        if (AFIDEUtil.isAnnonceAnnulationDoublon(ideAnnonceEntrante)) {
            numeroIDE = ideAnnonceEntrante.getNumeroIdeRemplacement();
        }

        List<IDEDataBean> listIdeDataBeans;

        if (AFIDEUtil.isAnnonceAnnulationSansRemplacement(ideAnnonceEntrante)) {
            // D0181 Si annonce d'annulation sans IDE de remplacement, mettre à blanc les champs numIde,RaisonSocIde et
            // Status des affiliations
            listIdeDataBeans = new ArrayList<IDEDataBean>();
            listIdeDataBeans.add(new IDEDataBean());
        } else {
            // sinon mettre à jour par rapport à ce qui est dans le registre
            listIdeDataBeans = IDEServiceCallUtil.searchForNumeroIDE(numeroIDE, getSession());
        }
        if (listIdeDataBeans.size() == 1) {

            IDEDataBean ideDataBean = listIdeDataBeans.get(0);

            for (AFAffiliation aAffiliation : listAffiliation) {

                aAffiliation.setNumeroIDE(ideDataBean.getNumeroIDE());
                aAffiliation.setIdeRaisonSociale(ideDataBean.getRaisonSociale());
                aAffiliation.setIdeStatut(ideDataBean.getStatut());

                miseAJourCodeNoga(ideAnnonceEntrante, idCodeNoga, aAffiliation);

                aAffiliation.setRulesByPass(true);
                AFAffiliationUtil.disableExtraProcessingForAffiliation(aAffiliation);
                aAffiliation.update(getTransaction());
                aAffiliation.callExtensionContextsAfterUpdate();
            }
            if (AFIDEUtil.isAnnonceAnnulationDoublon(ideAnnonceEntrante)) {
                // D0181 générer une annonce d'enregistrement Actif pour le numéro ide de remplacement
                AFIDEUtil.generateAnnonceEnregistrementActif(getSession(), listAffiliation.get(0), null);
            }

            // On throw une erreur que lorsque le code NOGA n'a pas été trouvé, si = null ça veut dire que la propriété
            // est à false et donc on remonte pas d'erreur
            if ("0".equals(idCodeNoga)) {
                if (CODE_NOGA_INCONNU.equals(ideDataBean.getNogaCode())) {
                    ideAnnonceEntrante.setErreurNoga(true);
                    ideAnnonceEntrante.setMessageErreurForBusinessUser(
                            FWMessageFormat.format(getSession().getApplication().getLabel("NAOS_CODE_NOGA_INCONNU",
                                    getSession().getIdLangueISO()), ideAnnonceEntrante.getHistNoga()));
                } else {
                    ideAnnonceEntrante.setErreurNoga(true);
                    ideAnnonceEntrante.setMessageErreurForBusinessUser(
                            FWMessageFormat.format(getSession().getApplication().getLabel("NAOS_CODE_NOGA_INDEFINI",
                                    getSession().getIdLangueISO()), ideAnnonceEntrante.getHistNoga()));
                }
            }
        } else {
            throw new Exception(
                    "AFIdeTraitementAnnonceProcess.updateAffiliationWithAnnonceEntrante : unable to update affiliation because no entity IDE founded in register for number "
                            + numeroIDE);
        }

    }

    private void miseAJourCodeNoga(AFIdeAnnonce ideAnnonceEntrante, String idCodeNoga, AFAffiliation aAffiliation)
            throws PropertiesException, Exception {
        // Dans ce cas là la MAJ du code noga se base sur le fichier XML
        if (!AFProperties.NOGA_UPDATE_ANNONCE_IDE.getValue().isEmpty()
                && AFProperties.NOGA_UPDATE_ANNONCE_IDE.getValue() != null
                && AFProperties.NOGA_UPDATE_ANNONCE_IDE.getBooleanValue()) {
            idCodeNoga = getidCodePourCodeNoga(ideAnnonceEntrante.getHistNoga());
            aAffiliation.setCodeNoga(idCodeNoga);
        }
    }

    /***
     * Méthode qui permet de récupérer l'id d'un code noga
     * 
     * @param ideDataBean
     * @throws Exception
     */
    private String getidCodePourCodeNoga(String codeNoga) throws Exception {
        FWParametersSystemCodeManager param = new FWParametersSystemCodeManager();

        param.setSession(getSession());
        param.setForCodeUtilisateur(codeNoga);

        param.find(BManager.SIZE_NOLIMIT);

        if (param.size() == 0) {
            return "0";
        } else {
            return ((FWParametersSystemCode) param.getFirstEntity()).getIdCode();
        }
    }

    private void updateAffiliationAfterTraitementAnnonceSortante(IDEDataBean ideDataBean, AFIdeAnnonce ideAnnonce)
            throws Exception {

        List<String> listIdAffiliationDejaUpdated = new ArrayList<String>();

        AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(ideAnnonce.getIdeAnnonceIdAffiliation(),
                getSession());
        affiliation.setNumeroIDE(ideDataBean.getNumeroIDE());
        affiliation.setIdeRaisonSociale(ideDataBean.getRaisonSociale());
        affiliation.setIdeStatut(ideDataBean.getStatut());
        affiliation.setRulesByPass(true);
        AFAffiliationUtil.disableExtraProcessingForAffiliation(affiliation);
        affiliation.update(getTransaction());
        listIdAffiliationDejaUpdated.add(affiliation.getAffiliationId());

        String listIdAffiliationLiee = ideAnnonce.getIdeAnnonceListIdAffiliationLiee();

        if (!JadeStringUtil.isBlankOrZero(listIdAffiliationLiee)) {

            String[] tabIdAffiliationLiee = listIdAffiliationLiee.split(",");
            for (String aIdAffiliationLiee : tabIdAffiliationLiee) {

                AFAffiliation affLiee = new AFAffiliation();
                affLiee.setSession(getSession());
                affLiee.setAffiliationId(aIdAffiliationLiee.trim());
                affLiee.retrieve(getTransaction());

                affLiee.setNumeroIDE(ideDataBean.getNumeroIDE());
                affLiee.setIdeRaisonSociale(ideDataBean.getRaisonSociale());
                affLiee.setIdeStatut(ideDataBean.getStatut());
                affLiee.setRulesByPass(true);
                AFAffiliationUtil.disableExtraProcessingForAffiliation(affLiee);
                affLiee.update(getTransaction());
                listIdAffiliationDejaUpdated.add(affLiee.getAffiliationId());

            }

        }

        List<AFAffiliation> listAffiliationWithSameIde = AFAffiliationUtil
                .loadAffiliationUsingNumeroIdeForCheckMultiAff(getSession(), ideDataBean.getNumeroIDE(),
                        affiliation.getAffiliationId());
        if (listAffiliationWithSameIde != null) {
            for (AFAffiliation aAff : listAffiliationWithSameIde) {
                if (!listAffiliationWithSameIde.contains(aAff.getAffiliationId())) {

                    aAff.setNumeroIDE(ideDataBean.getNumeroIDE());
                    aAff.setIdeRaisonSociale(ideDataBean.getRaisonSociale());
                    aAff.setIdeStatut(ideDataBean.getStatut());
                    aAff.setRulesByPass(true);
                    AFAffiliationUtil.disableExtraProcessingForAffiliation(aAff);
                    aAff.update(getTransaction());
                    listIdAffiliationDejaUpdated.add(aAff.getAffiliationId());

                }
            }

        }

    }

    private void updateAnnonceAfterTraitementAnnonceSortante(IDEDataBean ideDataBean, AFIdeAnnonce ideAnnonce,
            String etatAnnonce) throws Exception {

        ideAnnonce.setNumeroIde(ideDataBean.getNumeroIDE());
        ideAnnonce.setStatutIde(ideDataBean.getStatut());
        ideAnnonce.setRaisonSociale(ideDataBean.getRaisonSociale());
        ideAnnonce.setIdeAnnonceEtat(etatAnnonce);
        ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
        ideAnnonce.update(getTransaction());
    }

    private void updateAnnonceAfterTraitementAnnonceSortanteMasse(AFIdeAnnonce ideAnnonce, String messageErreur,
            String etatAnnonce) throws Exception {

        ideAnnonce.setMessageErreurForBusinessUser(messageErreur);
        ideAnnonce.setIdeAnnonceEtat(etatAnnonce);
        ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
        ideAnnonce.update(getTransaction());

    }

    private void updateAnnonceAfterTraitementAnnonceEntrante(AFIdeAnnonce ideAnnonce, String etatAnnonce)
            throws Exception {

        ideAnnonce.setIdeAnnonceEtat(etatAnnonce);
        ideAnnonce.setIdeAnnonceDateTraitement(JACalendar.todayJJsMMsAAAA());
        ideAnnonce.update(getTransaction());

    }

    private List<AFIdeAnnonce> traiterAnnonceEntrante(List<AFIdeAnnonce> listAnnonceIde) throws Exception {

        for (AFIdeAnnonce ideAnnonceEntrante : listAnnonceIde) {

            boolean isAnnoncePassive = AFIDEUtil.isAnnoncePassive(ideAnnonceEntrante);
            boolean errorMustFlagAnnonceAsSuccess = false;
            List<AFAffiliation> listAffiliationToUpdate = null;
            try {
                viderErreurProcess();

                ideAnnonceEntrante.setMessageErreurForBusinessUser(
                        AFIDEUtil.checkAnnonceEntranteMandatory(getSession(), ideAnnonceEntrante));

                if (!ideAnnonceEntrante.hasAnnonceErreur()) {

                    ideAnnonceEntrante.setIdeAnnonceIdAffiliation("");
                    ideAnnonceEntrante.setNumeroAffilie("");

                    ideAnnonceEntrante.setIdeAnnonceListIdAffiliationLiee("");
                    ideAnnonceEntrante.setIdeAnnonceListNumeroAffilieLiee("");

                    listAffiliationToUpdate = AFAffiliationUtil.loadAffiliationUsingNumeroIde(getSession(),
                            ideAnnonceEntrante.getHistNumeroIde(), isAnnoncePassive);

                    if (listAffiliationToUpdate != null) {

                        String listIdAffiliationLiee = "";
                        String listNumeroAffilieLiee = "";

                        for (AFAffiliation aAffiliation : listAffiliationToUpdate) {

                            if (JadeStringUtil.isBlankOrZero(ideAnnonceEntrante.getIdeAnnonceIdAffiliation())) {
                                ideAnnonceEntrante.setIdeAnnonceIdAffiliation(aAffiliation.getAffiliationId());
                                ideAnnonceEntrante.setNumeroAffilie(aAffiliation.getAffilieNumero());
                            } else {

                                if (!JadeStringUtil.isBlankOrZero(listIdAffiliationLiee)) {
                                    listIdAffiliationLiee = listIdAffiliationLiee + ",";
                                }
                                listIdAffiliationLiee = listIdAffiliationLiee + aAffiliation.getAffiliationId();

                                if (!JadeStringUtil.isBlankOrZero(listNumeroAffilieLiee)) {
                                    listNumeroAffilieLiee = listNumeroAffilieLiee + ",";
                                }
                                listNumeroAffilieLiee = listNumeroAffilieLiee + aAffiliation.getAffilieNumero();

                            }

                        }

                        ideAnnonceEntrante.setIdeAnnonceListIdAffiliationLiee(listIdAffiliationLiee);
                        ideAnnonceEntrante.setIdeAnnonceListNumeroAffilieLiee(listNumeroAffilieLiee);

                    }

                }

            } catch (AFIdeNumberNoMatchException e) {
                // D0181 si l'annonce entrantes de type ANNULE (avec ou sans le remplacement) renseignent un
                // numéro IDE inconnu dans nos affilié, l'annonce doit
                // générer une erreur spécifique pour pouvoir malgré tout la passer en Traité
                if (AFIDEUtil.isAnnonceAnnulationDoublon(ideAnnonceEntrante)
                        || AFIDEUtil.isAnnonceAnnulationSansRemplacement(ideAnnonceEntrante)) {
                    errorMustFlagAnnonceAsSuccess = true;
                }
                AFIDEUtil.handleError(getSession(), e, ideAnnonceEntrante);
            } catch (Exception e) {
                AFIDEUtil.handleError(getSession(), e, ideAnnonceEntrante);
            } finally {

                try {
                    doUpdateAfterTraitementAnnonceEntrante(ideAnnonceEntrante, isAnnoncePassive,
                            listAffiliationToUpdate, errorMustFlagAnnonceAsSuccess);
                } catch (Exception e2) {
                    AFIDEUtil.logExceptionAndCreateMessageForUser(getSession(), e2);
                    getTransaction().addErrors(e2.getMessage());
                } finally {
                    handleTransaction(ideAnnonceEntrante.getIdeAnnonceIdAnnonce());
                }

            }

        }

        return reloadListAnnonce(listAnnonceIde);

    }

    private void doUpdateAfterTraitementAnnonceEntrante(AFIdeAnnonce ideAnnonceEntrante, boolean isAnnoncePassive,
            List<AFAffiliation> listAffiliationToUpdate, boolean errorMustFlagAnnonceAsSuccess) throws Exception {

        if (!ideAnnonceEntrante.hasAnnonceErreur()
                && (!isAnnoncePassive || (isAnnoncePassive && !ideAnnonceEntrante.getNumeroAffilie().isEmpty()))) {
            updateAffiliationWithAnnonceEntrante(listAffiliationToUpdate, ideAnnonceEntrante, isAnnoncePassive);
        }

        updateAnnonceAfterTraitementAnnonceEntrante(ideAnnonceEntrante, AFIDEUtil
                .giveMeStatusAnnonceApresTraitementAccordingToError(ideAnnonceEntrante, errorMustFlagAnnonceAsSuccess));
    }

    private String logTechnicalErrorMessage(String idAnnonce) {

        StringBuffer technicalError = new StringBuffer("Technical error with annonce " + idAnnonce + "\n");

        technicalError.append("Transaction errors : " + getTransaction().getErrors() + "\n");
        technicalError.append("Transaction warnings : " + getTransaction().getWarnings() + "\n");
        technicalError.append("Session errors : " + getSession().getErrors() + "\n");
        technicalError.append("Session warnings : " + getSession().getWarnings() + "\n");
        technicalError
                .append("Memory Log (only fatal errors make rollbak) : " + getMemoryLog().getMessagesInString() + "\n");
        if (isAborted()) {
            technicalError.append("Aborted" + "\n");
        }

        System.out.println(technicalError.toString());

        return technicalError.toString();

    }

    private void handleTransaction(String idAnnonce) throws Exception {

        if (isOnError() || isAborted() || getTransaction().hasErrors() || getTransaction().hasWarnings()
                || getSession().hasErrors() || getSession().hasWarnings()) {
            logTechnicalErrorMessage(idAnnonce);
            getTransaction().rollback();
        } else {
            getTransaction().commit();
        }

        viderErreurProcess();

    }

    /**
     * créer, remplir, envoyer par mail un xml (mise en forme pour Excel) avec la liste des annonces sortantes traitées
     * et suspendues
     * 
     * @throws Exception
     */
    private void genererExcelResultTraitementAnnonceSortante(List<AFIdeAnnonce> annonceSortieExcel) throws Exception {

        annonceSortieExcel.addAll(getListAnnonceSuspendu());

        AFXmlmlIdeTraitementAnnonce xmlml = new AFXmlmlIdeTraitementAnnonce();

        createDataForExcel(annonceSortieExcel, xmlml);

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + AFApplication.DEFAULT_APPLICATION_NAOS_REP
                + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                + AFXmlmlIdeTraitementAnnonce.XLS_DOC_NAME + ".xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(AFXmlmlIdeTraitementAnnonce.XLS_DOC_NAME + ".xml");

        xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xmlml.getContainer());

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFXmlmlIdeTraitementAnnonce.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(false);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(AFXmlmlIdeTraitementAnnonce.NUMERO_INFOROM);

        this.registerAttachedDocument(docInfoExcel, xlsDocPath);

    }

    private void genererExcelResultTraitementAnnonceEntrantePassive(List<AFIdeAnnonce> listAnnonceEntrante)
            throws Exception {

        AFXmlmlIdeTraitementAnnonceEntrantePassive xmlml = new AFXmlmlIdeTraitementAnnonceEntrantePassive();

        createDataForExcelAnnonceEntrantePassive(xmlml, listAnnonceEntrante);

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + AFApplication.DEFAULT_APPLICATION_NAOS_REP
                + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                + AFXmlmlIdeTraitementAnnonceEntrantePassive.XLS_DOC_NAME + ".xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir() + JadeFilenameUtil
                .addOrReplaceFilenameSuffixUID(AFXmlmlIdeTraitementAnnonceEntrantePassive.XLS_DOC_NAME + ".xml");

        xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xmlml.getContainer());

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFXmlmlIdeTraitementAnnonceEntrantePassive.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(false);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(AFXmlmlIdeTraitementAnnonceEntrantePassive.NUMERO_INFOROM);

        this.registerAttachedDocument(docInfoExcel, xlsDocPath);

    }

    private void genererExcelResultTraitementAnnonceEntranteActive(List<AFIdeAnnonce> listAnnonceEntrante)
            throws Exception {

        AFXmlmlIdeTraitementAnnonceEntranteActive xmlml = new AFXmlmlIdeTraitementAnnonceEntranteActive();

        createDataForExcelAnnonceEntranteActive(xmlml, listAnnonceEntrante);

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + AFApplication.DEFAULT_APPLICATION_NAOS_REP
                + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                + AFXmlmlIdeTraitementAnnonceEntranteActive.XLS_DOC_NAME + ".xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir() + JadeFilenameUtil
                .addOrReplaceFilenameSuffixUID(AFXmlmlIdeTraitementAnnonceEntranteActive.XLS_DOC_NAME + ".xml");

        xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xmlml.getContainer());

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFXmlmlIdeTraitementAnnonceEntranteActive.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(false);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(AFXmlmlIdeTraitementAnnonceEntranteActive.NUMERO_INFOROM);

        this.registerAttachedDocument(docInfoExcel, xlsDocPath);

    }

    private void createDataForExcelAnnonceEntrantePassive(AFXmlmlIdeTraitementAnnonceEntrantePassive xmlml,
            List<AFIdeAnnonce> listAnnonceEntrante) throws Exception {

        // Création des informations sur le document
        xmlml.putData(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_NUM_INFOROM,
                AFXmlmlIdeTraitementAnnonceEntrantePassive.NUMERO_INFOROM);
        xmlml.putData(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_DATE_TRAITEMENT,
                JACalendar.todayJJsMMsAAAA());
        xmlml.putData(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_TITRE_DOC,
                getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_ENTRANTE_PASSIVE_EXCEL_TITRE_DOCUMENT"));

        for (AFIdeAnnonce aIdeAnnonce : listAnnonceEntrante) {

            if (AFIDEUtil.isAnnoncePassive(aIdeAnnonce) || AFIDEUtil.isAnnoncePassiveNonInfoAbo(aIdeAnnonce)) {

                xmlml.createLigne(getSession().getCodeLibelle(aIdeAnnonce.getIdeAnnonceType()),
                        getSession().getCodeLibelle(aIdeAnnonce.getIdeAnnonceEtat()),
                        aIdeAnnonce.getIdeAnnonceDateCreation(),
                        AFIDEUtil.giveMeNumIdeFormatedWithPrefix(aIdeAnnonce.getHistNumeroIde()),
                        getSession().getCodeLibelle(aIdeAnnonce.getHistStatutIde()),
                        AFIDEUtil.giveMeAllNumeroAffilieInAnnonceSeparatedByVirgul(aIdeAnnonce),
                        aIdeAnnonce.getHistRaisonSociale(), aIdeAnnonce.getHistRue(), aIdeAnnonce.getHistNPA(),
                        aIdeAnnonce.getHistLocalite(), aIdeAnnonce.getHistCanton(), aIdeAnnonce.getHistNaissance(),
                        AFIDEUtil.formatNogaRegistre(aIdeAnnonce.getHistNoga(), getSession()),
                        aIdeAnnonce.getMessageErreurForBusinessUser());
            }

        }

    }

    private void createDataForExcelAnnonceEntranteActive(AFXmlmlIdeTraitementAnnonceEntranteActive xmlml,
            List<AFIdeAnnonce> listAnnonceEntrante) throws Exception {

        // Création des informations sur le document
        xmlml.putData(AFXmlmlIdeTraitementAnnonceEntranteActive.DATA_EXCEL_NUM_INFOROM,
                AFXmlmlIdeTraitementAnnonceEntranteActive.NUMERO_INFOROM);
        xmlml.putData(AFXmlmlIdeTraitementAnnonceEntranteActive.DATA_EXCEL_DATE_TRAITEMENT,
                JACalendar.todayJJsMMsAAAA());
        xmlml.putData(AFXmlmlIdeTraitementAnnonceEntranteActive.DATA_EXCEL_TITRE_DOC,
                getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_ENTRANTE_EXCEL_TITRE_DOCUMENT"));

        for (AFIdeAnnonce aIdeAnnonce : listAnnonceEntrante) {

            if (!AFIDEUtil.isAnnoncePassive(aIdeAnnonce)) {

                String numIde = AFIDEUtil.giveMeNumIdeFormatedWithPrefix(aIdeAnnonce.getHistNumeroIde());
                String numIdeRemplacement = AFIDEUtil
                        .giveMeNumIdeFormatedWithPrefix(aIdeAnnonce.getNumeroIdeRemplacement());
                if (!JadeStringUtil.isBlankOrZero(numIdeRemplacement)) {
                    numIde = numIdeRemplacement + " (" + numIde + ")";
                }

                xmlml.createLigne(getSession().getCodeLibelle(aIdeAnnonce.getIdeAnnonceType()),
                        getSession().getCodeLibelle(aIdeAnnonce.getIdeAnnonceEtat()),
                        aIdeAnnonce.getIdeAnnonceDateCreation(), numIde,
                        getSession().getCodeLibelle(aIdeAnnonce.getHistStatutIde()),
                        AFIDEUtil.giveMeAllNumeroAffilieInAnnonceSeparatedByVirgul(aIdeAnnonce),
                        aIdeAnnonce.getHistRaisonSociale(), aIdeAnnonce.getHistRue(), aIdeAnnonce.getHistNPA(),
                        aIdeAnnonce.getHistLocalite(), aIdeAnnonce.getHistCanton(), aIdeAnnonce.getHistNaissance(),
                        AFIDEUtil.formatNogaRegistre(aIdeAnnonce.getHistNoga(), getSession()),
                        aIdeAnnonce.getMessageErreurForBusinessUser());
            }

        }

    }

    private void createDataForExcel(List<AFIdeAnnonce> annonceSortieExcel, AFXmlmlIdeTraitementAnnonce xmlml)
            throws Exception {

        // Création des informations sur le document
        xmlml.putData(AFXmlmlIdeTraitementAnnonce.DATA_EXCEL_NUM_INFOROM, AFXmlmlIdeTraitementAnnonce.NUMERO_INFOROM);
        xmlml.putData(AFXmlmlIdeTraitementAnnonce.DATA_EXCEL_DATE_TRAITEMENT, JACalendar.todayJJsMMsAAAA());
        xmlml.putData(AFXmlmlIdeTraitementAnnonce.DATA_EXCEL_TITRE_DOC,
                getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_SORTANTE_EXCEL_TITRE_DOCUMENT"));

        for (AFIdeAnnonce aIdeAnnonce : annonceSortieExcel) {

            AFAffiliation aff = new AFAffiliation();
            aff = AFAffiliationUtil.getAffiliation(aIdeAnnonce.getIdeAnnonceIdAffiliation(), getSession());

            // D0181 raisonSoc = prenomNom pour personne physique
            String raisonSoc = aIdeAnnonce.getHistRaisonSocialeONLY();
            if (JadeStringUtil.isEmpty(raisonSoc)) {
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(aIdeAnnonce.getIdTiers());
                tiers.retrieve();
                if (tiers.getPersonnePhysique()) {
                    raisonSoc = tiers.getPrenomNom();
                } else {
                    raisonSoc = aIdeAnnonce.getHistRaisonSociale();
                }
            }

            String numIDE = aIdeAnnonce.getNumeroIde();
            if (!aff.isNew() && JadeStringUtil.isEmpty(numIDE)) {
                numIDE = aff.getNumeroIDE();
            }

            String statutIDE = aIdeAnnonce.getStatutIde();
            if (!aff.isNew() && JadeStringUtil.isEmpty(statutIDE)) {
                statutIDE = aff.getIdeStatut();
            }

            xmlml.createLigne(getSession().getCodeLibelle(aIdeAnnonce.getIdeAnnonceType()),
                    getSession().getCodeLibelle(aIdeAnnonce.getIdeAnnonceEtat()),
                    aIdeAnnonce.getIdeAnnonceDateCreation(), AFIDEUtil.giveMeNumIdeFormatedWithPrefix(numIDE),
                    getSession().getCodeLibelle(statutIDE),
                    AFIDEUtil.giveMeAllNumeroAffilieInAnnonceSeparatedByVirgul(aIdeAnnonce), raisonSoc,
                    aIdeAnnonce.getHistRue(), aIdeAnnonce.getHistNPA(), aIdeAnnonce.getHistLocalite(),
                    aIdeAnnonce.getHistCanton(), aIdeAnnonce.getHistNaissance(), aIdeAnnonce.getHistActivite(),
                    aIdeAnnonce.getMessageErreurForBusinessUser());
        }
    }

    @Override
    protected void _validate() throws Exception {

    }

    @Override
    protected String getEMailObject() {

        if (isAborted() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_" + mailObject + "_ERROR");
        } else {
            return getSession().getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_" + mailObject + "_SUCCESS");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}
