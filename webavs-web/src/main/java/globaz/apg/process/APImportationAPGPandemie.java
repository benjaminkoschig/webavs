/*
 * Créé le 25 mars 2020
 */
package globaz.apg.process;

import apg.pandemie.*;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.mail.CommonFilesUtils;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.pegasus.business.vo.donneeFinanciere.IbanCheckResultVO;
import ch.globaz.pegasus.businessimpl.services.process.allocationsNoel.AdressePaiementPrimeNoelService;
import ch.globaz.pyxis.business.model.*;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.importation.beneficiaires.APBeneficiaries;
import globaz.apg.db.importation.beneficiaires.APBeneficiary;
import globaz.apg.db.droits.*;
import globaz.apg.db.importation.turnover.*;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.db.droits.APImportationAPGPandemieHistorique;
import globaz.apg.properties.APProperties;
import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.commons.nss.NSUtil;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.*;
import globaz.globall.util.*;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.external.IntRole;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAVSUtils;
import globaz.prestation.tools.PRSession;
import globaz.prestation.utils.PRDateUtils;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.tiers.*;
import globaz.osiris.api.APIOperation;
import globaz.pyxis.util.TIIbanFormater;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static ch.globaz.pyxis.business.services.AdresseService.*;

/**
 * <H1>Importation des demandes APG Pandémie</H1>
 *
 * @author mpe
 */
public class APImportationAPGPandemie extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(APImportationAPGPandemie.class);

    private static final String AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_OK = "AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_OK";
    private static final String AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_TECHNIQUE = "AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_TECHNIQUE";
    private static final String AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_METIER = "AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_METIER";
    private static final String BACKUP_FOLDER = "/backup/";
    private static final String ERRORS_FOLDER = "/errors/";
    private static final String XML_EXTENSION = ".xml";
    private static final String ZIP_EXTENSION = ".zip";
    private static final int MAX_TREATMENT = 40;
    private static final String DATE_RETRO_DROIT_VAGUE_2 = "17.09.2020";
    private static final String ANNEE_PRISE_COMPTE_SALAIRE = "2019";

    private static String userGestionnaire = "";

    private BSession bsession;
    private BabelContainer conteneurCatalogues = null;
    private LinkedList<String> errors = new LinkedList();
    private LinkedList<String> infos = new LinkedList();
    private LinkedList<String> errorsCreateBeneficiaries = new LinkedList();
    private String backupFolder;
    private String errorsFolder;
    private String storageFolder;
    private String urlFichiersDemandesPandemie;
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String adresseEmailAssure;
    private String sexeCI;
    private List<String> listIDAff = new ArrayList<>();
    private List<APPeriodeAPG> periodes = new ArrayList<>();
    private boolean isProcessErrorMetier = false;
    private boolean isProcessErrorTechnique = false;
    private int nbTraites = 0;

    private Boolean isImportGestionnaire = false;
    private String senderId = "";
    private String referenceData = "";
    private boolean isAssimEmployeur = false;
    private boolean isEmploye = false;
    private List<String> nssTraites;

    // Pour les tests - A Supprimer une fois que la recup du NSS sera implémentée
    String state = APIOperation.ETAT_TRAITE;
    private String NOM_CATALOGUE = "Decompte_Pandemie";

    @Override
    public boolean _executeProcess() {
        try {

            initBsession();
            // Pour ne pas envoyer de double mail, un mail d'erreur de validation est déjà généré
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);
            LOG.info("ImportAPGPandemie - Start Import - Demandes APG Covid19");
            importFiles();
        } catch (Exception e) {
            setReturnCode(-1);
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            try {
                sendResultMailToCaisse(null, null);
            } catch (Exception e1) {
                throw new TechnicalException("Problème à l'envoi du mail", e1);
            }
            throw new TechnicalException("Erreur dans le process d'import", e);
        } finally {
            closeBsession();
        }
        LOG.info("ImportAPGPandemie - END PROCESS");
        return true;
    }

    private void initDataClasse() {
        listIDAff = new ArrayList<>();
        periodes = new ArrayList<>();
        dateDebutDroit = "";
        dateFinDroit = "";
        adresseEmailAssure = "";
        sexeCI = "";
        nssTraites = new ArrayList<>();
        errors = new LinkedList();
        infos = new LinkedList();
        isProcessErrorMetier = false;
        isProcessErrorTechnique = false;
    }

    private void initBsession() throws Exception {
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    /**
     * Traitement des fichiers APG - Pandémie
     */
    private void importFiles() throws Exception {
        if (!APProperties.GESTIONNAIRE_USER.getValue().isEmpty()) {
            try {
                JadeUser user = bsession.getApplication()._getSecurityManager().getUserForVisa(bsession, APProperties.GESTIONNAIRE_USER.getValue());
                if (Objects.nonNull(user.getVisa())) {
                    userGestionnaire = user.getVisa();
                }
            } catch (FWSecurityLoginException e) {
                LOG.warn("APImportationAPGPandemie#importFiles - Erreur à la récupéreration le nom du gestionnaire :", e);
                userGestionnaire = "";
            }
        }
        try {
            urlFichiersDemandesPandemie = APProperties.DEMANDES_APG_PANDEMIE_FOLDER.getValue();
            storageFolder = APProperties.STORAGE_APG_PANDEMIE_FOLDER.getValue();

            if(!JadeStringUtil.isBlankOrZero(urlFichiersDemandesPandemie)){
                List<String> repositoryELP = JadeFsFacade.getFolderChildren(urlFichiersDemandesPandemie);
                backupFolder = new File(urlFichiersDemandesPandemie).getAbsolutePath() + BACKUP_FOLDER;
                errorsFolder = new File(urlFichiersDemandesPandemie).getAbsolutePath() + ERRORS_FOLDER;
                for (String nomFichierDistant : repositoryELP) {
                    if(MAX_TREATMENT > nbTraites) {
                        importFile(nomFichierDistant);
                    }
                }
                LOG.info("Nombre de dossiers traités : "+nbTraites);
            }else{
                LOG.warn("Properties APG Pandemie undefined");
                throw new Exception("APImportationAPGPandemie#import : Propriétés non définies !");
            }
        } catch (Exception e) {
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            LOG.error("APImportationAPGPandemie#import : erreur lors de l'importation des fichiers", e);
            throw new Exception("APImportationAPGPandemie#import : erreur lors de l'importation des fichiers"+e.getMessage());
        }
    }

    /**
     * Copie le fichier XML en local, effectue le traitement d'importion, sauvegarde le fichier
     * dans le dossier backup, supprime le fichier de base
     *
     * @param nomFichierZipDistant
     * @throws JAXBException
     */
    private void importFile(String nomFichierZipDistant) throws IOException {
        if(nomFichierZipDistant.endsWith(ZIP_EXTENSION)){
            try {
                //initBsession();
                JadeThread.logClear();
                isImportGestionnaire = false;
                initDataClasse();
                LOG.info("ImportAPGPandemie - Start treat " + nomFichierZipDistant);
                ZipFile zipFile = new ZipFile(nomFichierZipDistant);
                String destTmpXMLPath = "";
                Message message;
                String nameOriginalZipFile = FilenameUtils.getName(nomFichierZipDistant);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                boolean isTraitementSuccess = false;
                boolean xmlInZip = false;
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (!entry.isDirectory() && entry.getName().endsWith(XML_EXTENSION)) {
                        xmlInZip = true;
                        destTmpXMLPath = urlFichiersDemandesPandemie + entry.getName();
                        // Unzip du fichier XML temporairement
                        unzipfile(zipFile, entry, destTmpXMLPath);
                        // Parsing du fichier XML
                        message = getMessageFromFile(destTmpXMLPath);
                        if (message != null) {
                            isTraitementSuccess = traiterMessage(message);
                            if (isAssimEmployeur) {
                                adresseEmailAssure = message.getContent().getActivity().getCompanies().getCompany().get(0).getEmail();
                            } else {
                            adresseEmailAssure = message.getContent().getInsuredAddress().getEmail();
                            }

                            if (isTraitementSuccess) {
                                for(String nss : nssTraites) {
                                savingFileInDb(nss, destTmpXMLPath, state);
                                infos.add("Traitement du fichier suivant réussi : " + nameOriginalZipFile);
                                    infos.add("Assuré(s) concerné(s) : " + nss);
                                }
                            }
                        } else {
                            errors.add("Fichier zip suivant ne contient aucun fichier XML pouvant être traité : " + nameOriginalZipFile);
                        }
                    }
                }
                if (!xmlInZip) {
                    errors.add("Le fichier zip suivant ne contient aucun fichier XML : " + nameOriginalZipFile);
                }
                // List des fichiers a ajouter au mail
                List<String> filesToSend = new ArrayList<>();
                // Reset des erreurs de la transaction
                zipFile.close();
                if (isTraitementSuccess) {
                    LOG.info("ImportAPGPandemie - Moving message " + nomFichierZipDistant);
                    String movedZipPath = movingFile(nomFichierZipDistant, nameOriginalZipFile, isTraitementSuccess);
                    filesToSend.add(movedZipPath);
                    try {
                        sendResultMailToCaisse(filesToSend, nameOriginalZipFile);
                    } catch (Exception e1) {
                        LOG.error("Error in sending mail...", e1.getStackTrace());
                        LOG.error("Error in sending mail..." + ExceptionUtils.getStackTrace(e1));
                        throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e1);
                    }
                } else {
                    LOG.info("ImportAPGPandemie - Moving message " + nomFichierZipDistant);
                    String movedZipPath = movingFile(nomFichierZipDistant, nameOriginalZipFile, isTraitementSuccess);
                    filesToSend.add(movedZipPath);
                    try {
                        sendResultMailToCaisse(filesToSend, nameOriginalZipFile);
                        //Vague 2 - FERCIAM ne veut plus envoyer de message à l'ayant droit
                        //if (isProcessErrorMetier) {
                        //sendResultMailAssure(filesToSend, adresseEmailAssure, (nssTraites.size()>1 ? "" : nssTraites.toString()));
                        //}
                    } catch (Exception e) {
                        LOG.error("Error in sending mail...", e.getStackTrace());
                        LOG.error("Error in sending mail..." + ExceptionUtils.getStackTrace(e));
                        throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
                    }
                }
            }catch(Exception e){
                LOG.error("Error in sending mail...", e.getStackTrace());
                LOG.error("Error in sending mail..." + ExceptionUtils.getStackTrace(e));
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            }finally {
                nbTraites++;
                //closeBsession();
            }
        }
    }

    private void savingFileInDb(String nss, String pathFile, String state) throws Exception {
        BTransaction transaction = null;
        FileInputStream fileToStore = null;
        try {
            transaction = (BTransaction) bsession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            if(transaction.hasErrors()) {
                LOG.error("Des erreurs ont été trouvés dans la transaction. : ", transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            fileToStore = new FileInputStream(pathFile);
            APImportationAPGPandemieHistorique importData = new APImportationAPGPandemieHistorique();
            importData.setSession(bsession);
            importData.setEtatDemande(state);
            importData.setNss(nss);
            importData.setXmlFile(fileToStore);
            importData.add();
            if (!hasError(bsession, transaction)) {
                transaction.commit();
            }else {
                transaction.rollback();
            }
        } catch (FileNotFoundException e) {
            LOG.error("Fichier non trouvé : " + pathFile, e);
        } catch (Exception e) {
            errors.add("Erreur lors de l'update HistoriqueAPGPandemie "+e.getMessage());
            LOG.error("Erreur lors de l'update HistoriqueAPGPandemie : ", e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            try {
                if (!(Objects.isNull(fileToStore))) {
                    fileToStore.close();
                }
                if (transaction != null) {
                    transaction.closeTransaction();
                }

            } catch (IOException e) {
                LOG.error("Impossible de cloture le fichier", e);
            }
        }
    }

    private boolean traiterMessage(Message message) {
        boolean traitementInSuccess;
        try {
            // Vérification s'il s'agit d'un import Gestionnaire
            isImportGestionnaire = isGestionnaireBySenderId(Objects.nonNull(message.getHeader()) ? message.getHeader().getSenderId() : null);
            senderId = Objects.nonNull(message.getHeader()) ? message.getHeader().getSenderId() : "";
            // Vague 2
            referenceData = Objects.nonNull(message.getContent()) ? message.getContent().getReferenceData() : "";

            traitementInSuccess = processCreationDroitGlobal(message.getContent());
        } catch (Exception e) {
            errors.add("Impossible de traiter le message : "+message.getHeader().getMessageId());
            LOG.error("APImportationAPGPandemie#traiterMessage : erreur lors du traitement du message " + message.getHeader().getMessageId(), e);
            traitementInSuccess = false;
        }
        return traitementInSuccess;
    }

    private boolean isGestionnaireBySenderId(String senderId) {
        if (Objects.nonNull(senderId)){
            String endChar = senderId.substring(senderId.length()-2, senderId.length());

            if (Objects.equals(endChar, "-G"))  {
                return true;
            }
        }
        return false;
    }

    private void unzipfile(ZipFile zipFile, ZipEntry entry, String destPath) {
        InputStream inputStream;
        FileOutputStream outputStream = null;
        try {
            inputStream = zipFile.getInputStream(entry);
            outputStream = new FileOutputStream(destPath);
            int data = inputStream.read();
            while (data != -1) {
                outputStream.write(data);
                data = inputStream.read();
            }
        }catch (IOException e) {
            errors.add("Impossible de dézipper le fichier : " + zipFile.getName() + "("+e.getMessage()+")");
            LOG.error("APImportationAPGPandemie#import : erreur lors de l'importation des fichiers", e);
        } finally {
            try {
                if (!(Objects.isNull(outputStream))) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOG.error("Erreur à la fermeture du fichier", e);
            }
        }
    }

    private Message getMessageFromFile(String destPath) {
        try {
            LOG.info("ImportAPGPandemie - Reading file...");
            String tmpLocalWorkFile = JadeFsFacade.readFile(destPath);
            File demandeApgPandemieFile = new File(tmpLocalWorkFile);
            if (demandeApgPandemieFile.isFile()) {
                return getMessage(demandeApgPandemieFile);
            }
        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException | JAXBException e) {
            errors.add("Impossible de parser le fichier XML : " + destPath+ "("+e.getMessage()+")");
            LOG.error("APImportationAPGPandemie#importFile : erreur lors de l'importation du fichier " + destPath, e);
        }
        return null;
    }

    /**
     * Return le document XML : unmarshall depuis le fichier
     *
     * @param demandeApgPandemeFile
     * @return
     * @throws JAXBException
     */
    private Message getMessage(File demandeApgPandemeFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Message) unmarshaller.unmarshal(demandeApgPandemeFile);
    }

    /**
     * Permet de déplacer le fichier suite au traitement.
     *
     * @param nomFichierZipDistant
     * @param nameOriginalZipFile
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private String movingFile(String nomFichierZipDistant, String nameOriginalZipFile, Boolean processSuccess) {
        String storageFolderTemp = "";
        String storageFileTempTrace = "";
        String storageFileTempStorage = "";
        String fileToSend = "";
        try{
            if (!JadeFsFacade.exists(backupFolder)) {
                JadeFsFacade.createFolder(backupFolder);
            }
            if (!JadeFsFacade.exists(errorsFolder)) {
                JadeFsFacade.createFolder(errorsFolder);
            }

            if (processSuccess) {
                // copy du zip en backup
                JadeFsFacade.copyFile(nomFichierZipDistant, backupFolder + nameOriginalZipFile);
                storageFileTempTrace = backupFolder + nameOriginalZipFile;
            } else {
                // copy du zip en backup
                JadeFsFacade.copyFile(nomFichierZipDistant, errorsFolder + nameOriginalZipFile);
                storageFileTempTrace = errorsFolder + nameOriginalZipFile;
            }
            LOG.info("ImportAPGPandemie - Copy to "+storageFileTempTrace);


            if (!(Objects.isNull(storageFolder) || (storageFolder.isEmpty()))) {
                // Création du répertoire de storage
                for (String nss : nssTraites) {
                storageFolderTemp = CommonFilesUtils.createPathFiles(nss, storageFolder);
                }

                // copy du zip dans le storage
                if (!storageFolderTemp.isEmpty()) {
                    JadeFsFacade.copyFile(nomFichierZipDistant, storageFolderTemp + nameOriginalZipFile);
                    storageFileTempStorage = storageFolderTemp + nameOriginalZipFile;
                    LOG.info("ImportAPGPandemie - Copy to "+storageFileTempStorage);
                }
            }
            // On ne supprime le fichier uniquement si celui-ci a pu être copié dans un des deux répertoires
            // Et on retourne un des deux emplacements du fichier copiés
            if(JadeFsFacade.exists(storageFileTempTrace)){
                JadeFsFacade.delete(nomFichierZipDistant);
                fileToSend = storageFileTempTrace;
            } else if(JadeFsFacade.exists(storageFileTempStorage)){
                JadeFsFacade.delete(nomFichierZipDistant);
                fileToSend = storageFileTempStorage;
            }else{
                LOG.warn("APImportationAPGPandemie#movingFile : Fichier non supprimé car celui-ci n'a pas pu être copié dans un des deux emplacements suivants :\n-"
                        + storageFileTempTrace +"\n-"+storageFileTempTrace);
            }

        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
            LOG.error("APImportationAPGPandemie#movingFile : erreur lors de l'importation du fichier " + nameOriginalZipFile, e);
        }
        return fileToSend;
    }

    private void sendResultMailToCaisse(List<String> filesToJoin, String nameOriginalZipFile) throws Exception {
        StringBuilder corpsCaisse = new StringBuilder();
        if (Objects.nonNull(nameOriginalZipFile)) {
            corpsCaisse.append("Fichier traité : " + nameOriginalZipFile + "\n\n");
        }
        for(String info : infos){
            corpsCaisse.append(info + "\n");
        }
        if(!errors.isEmpty()){
            corpsCaisse.append("Les informations suivantes sont à vérifier ET sont bloquantes ! (droit non créé dans WebAVS) : \n");
        }
        for(String error : errors){
            corpsCaisse.append(error + "\n");
        }
        for (String errorCreationBeneficiaire : errorsCreateBeneficiaries) {
            corpsCaisse.append(errorCreationBeneficiaire + "\n");
        }
        LOG.info("ImportAPGPandemie - Envoi mail à l'adresse de la caisse "+getListEMailAddressTechnique().toString());
        ProcessMailUtils.sendMail(getListEMailAddressTechnique(), getEMailObjectCaisse(), corpsCaisse.toString(), filesToJoin);
    }

    /**
     * Process d'envoi des mails
     *
     * @param filesToJoin
     * @param emailTiers
     * @throws Exception
     */
    private void sendResultMailAssure(List<String> filesToJoin, String emailTiers, String nss) throws Exception {
        StringBuilder corpsAssure = new StringBuilder();

        // TODO à supprimer lorsque le Catalogue de texte sera mis en place
        // Correction pour la FPV

        if(CommonProperties.KEY_NO_CAISSE.getValue().equals("110")) {
            corpsAssure.append("Madame, Monsieur, \n" +
                    "\n" +
                    "Nous avons constaté que vous avez rempli une demande de prestation APG Coronavirus par le biais de notre formulaire en ligne.\n" +
                    "\n" +
                    "Le système n'a pas pu traiter votre demande du fait que vous n'avez pas d'affiliation avec notre Caisse ou en raison d'une saisie erronée de données. \n" +
                    "\n" +
                    "En cas d'inscription existante auprès de notre Caisse, vous voudrez bien réitérer votre demande en contrôlant soigneusement votre numéro AVS (756.xxxx.xxxx.xx) ainsi que votre numéro d'affilié que vous trouverez sur nos décomptes de cotisations.\n" +
                    "\n" +
                    "Si vous êtes salarié(e), il est possible que vous n'ayez pas encore été annoncé(e) à notre Caisse par votre employeur. Vous voudrez bien contrôler cette information avec ce dernier. \n" +
                    "\n" +
                    "Nous restons à votre disposition pour tout renseignement complémentaire, et nous vous prions d'agréer, Madame, Monsieur, nos salutations distinguées.\n");
            corpsAssure.append("******************************************************************************\n\n\n");
            corpsAssure.append("Sehr geehrte Damen und Herren\n" +
                    "\n" +
                    "Wir haben festgestellt, dass Sie einen Coronavirus EO-Anmeldung dank unseres Online-Formular ausgefüllt haben.\n" +
                    "\n" +
                    "Das System konnte Ihre Anfrage nicht bearbeiten, weil Sie kein Mitglied unserer Kasse sind oder wegen falsche vorliegende Daten.\n" +
                    "\n" +
                    "Wenn Sie noch bei unserer Kasse registriert sind, laden wir Sie ein, Ihre Anmeldung zu wiederholen, indem Sie Ihre AHV-Nummer (756.xxxx.xxxx.xx) und Ihre Mitgliedsnummer, die Sie auf Ihren Beitragsnachweisen finden, sorgfältig überprüfen.\n" +
                    "\n" +
                    "Wenn Sie Arbeitnehmer-innen sind, ist es möglich, dass Sie von Ihrem Arbeitgeber noch nicht bei unserer Kasse angemeldet wurden. In diesem Fall bitten wir Sie, diese Angaben bei Ihrem Arbeitgeber zu überprüfen.\n" +
                    "\n" +
                    "Bei allfälligen Fragen stehen wir Ihnen selbstverständlich gerne zur Verfügung.\n" +
                    "\n" +
                    "Freundliche Grüssen\n");

        } else {
            corpsAssure.append("Madame, Monsieur, \n" +
                    "\n" +
                    "Votre demande d'allocation pour perte de gain en cas de coronavirus a reçu toute notre attention. Toutefois nous regrettons de ne pouvoir y répondre favorablement, vous n'êtes pas inscrit auprès de notre caisse et nous ne sommes donc pas compétent pour entrer en matière.\n" +
                    "\n" +
                    "Nous restons à votre disposition pour tout renseignement complémentaire, et nous vous prions d'agréer, Madame, Monsieur, nos salutations distinguées\n\n\n");
            corpsAssure.append("******************************************************************************\n\n\n");
            corpsAssure.append("Sehr geehrte Frau, Sehr geehrter Herr \n" +
                    "\n" +
                    "Wir bestätigen Ihnen hiermit den Erhalt Ihrer Anmeldung für die Corona Erwerbsersatzentschädigung.\n" +
                    "Sie sind nicht bei unserer AHV Kasse angeschlossen und wir sind demnach nicht zuständig für die Bearbeitung von Ihrem Gesuch.\n" +
                    "\n"+
                    "Für allfällige weitere Auskünfte stehen wir zu Ihrer Verfügung.\n"+
                    "Freundliche Grüsse\n");
        }


        LOG.info("ImportAPGPandemie - Envoi mail à l'adresse de l'assuré "+getListEMailAddressAssure(emailTiers).toString());
        ProcessMailUtils.sendMail(getListEMailAddressAssure(emailTiers), getEMailObjectAssure(nss), corpsAssure.toString(), filesToJoin);
    }

    /**
     * Récupère les textes du catalogue de texte
     *
     * @param niveau
     * @param position
     * @return resString
     * @throws Exception
     */
    public String getTexte(int niveau, int position) throws IndexOutOfBoundsException, TechnicalException {

        StringBuilder resString = new StringBuilder();
        try {
            resString.append(conteneurCatalogues.getTexte(getCatalogueTexte(), niveau, position));
        } catch (Exception e) {
            throw new TechnicalException("Erreur à l'import du catalogue de texte", e);
        }


        if (resString.toString().isEmpty()) {
            throw new IndexOutOfBoundsException("pas de textes au niveau " + niveau);
        }
        return resString.toString();
    }

    /**
     * Initialisation du catalogue de texte pour l'envoi des mails.
     *
     * @throws TechnicalException
     */
    private void initializeCatalogueTexte() throws TechnicalException {

        conteneurCatalogues = new BabelContainer();
        conteneurCatalogues.setSession(bsession);
        conteneurCatalogues.addCatalogueText(getCatalogueTexte());
        try {
            conteneurCatalogues.load();
        } catch (Exception e) {
            LOG.error("ImportAPGPandemie - Impossible de charger le catalogue de texte, veuillez le créer dans le module des allocations pour perte de gains ! (non bloquant)");
        }
    }

    /**
     * Get paramétrage du catalogue de texte
     *
     * @return catalogueTexteApg
     */
    private CatalogueText getCatalogueTexte(){
        CatalogueText catalogueTexteApg = new CatalogueText();
        // TODO Gestion de la langue du tiers à implémenter
        catalogueTexteApg.setCodeIsoLangue("fr");
        catalogueTexteApg.setCsDomaine(IAPCatalogueTexte.CS_APG);
        catalogueTexteApg.setCsTypeDocument(IAPCatalogueTexte.CS_DECOMPTE_APG);
        catalogueTexteApg.setNomCatalogue(NOM_CATALOGUE);

        return catalogueTexteApg;
    }

    protected String getEMailObjectCaisse() {
        if(isProcessErrorMetier){
            return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_METIER)+ " " + nssTraites.toString();
        }else if(isProcessErrorTechnique){
            return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_TECHNIQUE) + " " + nssTraites.toString();
        }
        return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_OK) + " " + nssTraites.toString();
    }

    protected String getEMailObjectAssure(String nss) {
        return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_METIER)+ " " + nss;
    }

    public final List<String> getListEMailAddressAssure(String emailTiers) {
        List<String> listEmailAddress = new ArrayList<>();
        listEmailAddress.add(emailTiers);
        return listEmailAddress;
    }

    public final List<String> getListEMailAddressTechnique() {
        List<String> listEmailAddress = new ArrayList<>();
        try {
            listEmailAddress.add(APProperties.EMAIL_APG_PANDEMIE.getValue());
        } catch (PropertiesException e) {
            LOG.error("ImportAPGPandemie - Erreur à la récupération de la propriété Adresse E-mail !! ", e);
        }
        return listEmailAddress;
    }

    private boolean processCreationDroitGlobal(Content content) throws Exception {
        LOG.info("ImportAPGPandemie - Treating message...");
        boolean isTraitementSuccess = true;
        BTransaction transaction = null;
        CIEcriture ecriture = null;
        InsuredPerson assure = content.getInsuredPerson();
        InsuredAddress adresseAssure = content.getInsuredAddress();
        Activity activiteProfessionnelle = content.getActivity();

        // Vague 2 - Creation des bénéficiaires.
        APBeneficiaries beneficiaires = createBeneficiariesObject(content.getBeneficiaries());

        boolean isIndependant = isIndependant(activiteProfessionnelle);
        // Vague 2
        isAssimEmployeur = isAssimileEmployeur(activiteProfessionnelle);
        isEmploye = isEmploye(activiteProfessionnelle);
        // TODO Charger la var
        boolean isCaisseCompetente = false;
        ActivityCessation activiteArret = content.getActivityCessation();

        initDatesDroit(activiteArret);
        try {
            transaction = (BTransaction) bsession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            // Test si la caisse est compétente -> Si non, on ne créé rien
            PRTiersWrapper tiers = null;
            String nss = "";
            // Concerne autant indépendant APG que indépendant quarantaine
            if(isIndependant){
                nss = assure.getVn();
                nssTraites.add(nss);
                tiers = getTiersByNss(assure.getVn());
                if(tiers != null){
                    if(isCaisseCompetentePourIndependant(bsession, dateDebutDroit, dateFinDroit, tiers.getIdTiers(), nss)){
                        isCaisseCompetente = true;
                    }
                }else {
                    infos.add("Indépendant inexistant: " + nss);
                }
            }else if (isEmploye){ // + SubMessageType = 103 (103 = Quarantaine, 104 = APG)
                nss = assure.getVn();
                nssTraites.add(nss);
                tiers = getTiersByNss(assure.getVn());
                if(!isAffiliationRenseigneExist(activiteProfessionnelle, nss)) {
                    ecriture = retrieveEcrituresAssure(bsession, NSUtil.unFormatAVS(nss), transaction);
                    if (ecriture != null) {
                        isCaisseCompetente = true;
                    }else{
                        infos.add("Aucun CI trouvé pour l'assuré: "+nss);
                    }
                }else{
                    isCaisseCompetente = true;
                }
                if(isCaisseCompetente && tiers == null){
                    tiers = creationTiers(assure, adresseAssure);
                }
            }
            FamilyMembers membresFamilleAssure = new FamilyMembers();
            if ((isIndependant || isEmploye) && isCaisseCompetente && tiers != null) {
                // Vague 2 - Création pour l'indépendant (APG / quarantaine) ou l'employé (quarantaine)
                membresFamilleAssure = content.getFamilyMembers();
                isTraitementSuccess = createDroitForTier(nss, tiers, content, adresseAssure, membresFamilleAssure, activiteArret, activiteProfessionnelle, activiteProfessionnelle.getSalary(), true, content.getInsuredAddress().getEmail(), transaction);
            } else if (isAssimEmployeur){
                // Vague 2 - Création pour les bénéficiaires
                // Pour le moment, les bénéficiaires n'ont pas de membres de famille associés
                InsuredAddress adresseBeneficiaire = new InsuredAddress();
                InsuredPerson assureBeneficiaire = new InsuredPerson();

                for (Beneficiary beneficiaire : content.getBeneficiaries().getBeneficiary()) {
                    nss = beneficiaire.getInsuredPerson().getVn();
                    nssTraites.add(nss);
                    PRTiersWrapper beneficiaireTiers = getTiersByNss(nss);
                    assureBeneficiaire = beneficiaire.getInsuredPerson();
                    adresseBeneficiaire = beneficiaire.getInsuredAddress();

                    boolean continueProcessBeneficiaire = false;

                    if(!isAffiliationRenseigneExist(activiteProfessionnelle, nss)) {
                        ecriture = retrieveEcrituresAssure(bsession, NSUtil.unFormatAVS(nss), transaction);
                        if (ecriture != null) {
                            continueProcessBeneficiaire = true;
                        }else{
                            infos.add("Aucun CI trouvé pour l'assuré: " + nss);
                        }
                    }else{
                        continueProcessBeneficiaire = true;
                    }
                    if(continueProcessBeneficiaire && beneficiaireTiers == null){
                        beneficiaireTiers = creationTiers(assureBeneficiaire, adresseBeneficiaire);
                    }

                    if (continueProcessBeneficiaire && beneficiaireTiers != null) {
                        isTraitementSuccess = createDroitForTier(nss, beneficiaireTiers, content, adresseBeneficiaire, membresFamilleAssure, activiteArret, activiteProfessionnelle
                                , beneficiaire.getSalary(), false, beneficiaire.getInsuredAddress().getEmail(), transaction);
                    } else{
                        // Vague 2 - Ajout d'une erreur si le Beneficiaire ne peut pas être traité
                        errorsCreateBeneficiaries.add("Caisse non compétente pour le bénéficiaire :" + nss + "\n");
                    }
                }
            } else{
                transaction.rollback();
                adresseEmailAssure = adresseAssure.getEmail();
                LOG.warn("ImportAPGPandemie - Caisse non compétente pour l'assuré : "+nss);
                isTraitementSuccess = false;
                isProcessErrorMetier = true;
            }

            // Vague 2 - Création des turnOvers
            if(Objects.nonNull(activiteArret.getActivityLimitationCessation())) {
                createTurnovers(activiteArret.getActivityLimitationCessation().getTurnovers(), transaction);
            }

        } catch (Exception e) {
            isTraitementSuccess = false;
            if (transaction != null) {
                transaction.rollback();
            }
            errors.add("Erreur dans la création du droit "+e.getMessage());
            LOG.error("APImportationAPGPandemie#processCreationDroitGlobal : erreur lors de la création du droit ", e);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return isTraitementSuccess;
    }

    private boolean createDroitForTier(String nss, PRTiersWrapper tiers, Content content, InsuredAddress adresseAssure, FamilyMembers membresFamilleAssure, ActivityCessation activiteArret, Activity activiteProfessionnelle, Salary salary, boolean isIndependant, String email, BTransaction transaction) throws Exception {
        boolean isTraitementSuccess = true;
        PaymentContact adressePaiement = content.getPaymentContact();

                // Création du contact avec l'email récupéré du XML
        createContact(tiers, email);

                // Création rôle APG du tiers
                LOG.info("ImportAPGPandemie - Creating APG Role...");
                creationRoleApgTiers(tiers.getIdTiers());

                // Création de l'adresse APG-Pandémie du tiers
        // Vague 2
        String npaFormat = getNpaFormat(getZipCode(adresseAssure));

                if(isDonneesAdresseValide(adresseAssure, npaFormat)) {
                    LOG.info("ImportAPGPandemie - Creating Adresses...");
                    createAdresseApgPandemie(tiers, adresseAssure, adressePaiement, npaFormat);
                }else{
                    LOG.warn("ImportAPGPandemie - Adresse non créée (données non remplies)");
                    infos.add("Adresse non créée : Les données récupérées ne sont pas complètes / correctes");
                }

                // Création de la demande du droit
                LOG.info("ImportAPGPandemie - Creating Demande APG Pandemie...");
                PRDemande demande = creationDemande(tiers.getIdTiers());

                // Création du droit
                LOG.info("ImportAPGPandemie - Creating Droit APG Pandemie...");
                APDroitPandemie newDroit = creationDroit(demande, content, transaction, npaFormat);
                // Création des périodes du droit
                LOG.info("ImportAPGPandemie - Creating Periodes APG Pandemie...");
                createPeriodes(transaction, newDroit.getIdDroit());

                // Création droit pandémie
                LOG.info("ImportAPGPandemie - Creating Data Pandemie...");
        APDroitPanSituation newDroitPademie = creationDroitPandemie(newDroit, membresFamilleAssure, isIndependant, adressePaiement, activiteProfessionnelle, salary, activiteArret, transaction);



                // Création de la situation professionnelle pour indépendant
                LOG.info("ImportAPGPandemie - Creating Situation Professionnelle Pandemie...");
        if(isEmploye){
            creerSituationProfQuarantaine(transaction, newDroit, salary);
                }else{
            // Si le tiers possédait déjà un droit pandémie, on créé la même situation professionnelle qu'il y avait de ce dernier droit
            boolean situationProfessionnelleCreeSucess = creerSituationProfPanSelonDroitPrecedent(getSession(), transaction, newDroit, tiers.getIdTiers());
            // Si aucune situation professionnelle n'a été créé (pas de droit pandémie, ou problème de création
            if(!situationProfessionnelleCreeSucess) {
                if (isIndependant) {
                    creerSituationProfIndependant(transaction, newDroit, tiers.getIdTiers());
                } else if (isAssimEmployeur) {
                    creerSituationProfAssimileEmployeur(transaction, nss, newDroit, salary);
                }
            }
                }
                if (!hasError(bsession, transaction)) {
                    transaction.commit();
                    LOG.info("ImportAPGPandemie - Traitement in success...");
                }else{
                    transaction.rollback();
                    isProcessErrorTechnique = true;
                    errors.add("Un problème est survenu lors de la création du droit pour cet assuré : "+nss);
                    LOG.error("APImportationAPGPandemie#processCreationDroitGlobal : erreur lors de la création du droit\nSession errors : "
                            +bsession.getErrors()+"\nTransactions errors : "+transaction.getErrors());
                    isTraitementSuccess = false;
                }


        return isTraitementSuccess;
    }

    /**
     *
     *
     * @param adresseAssure
     * @return
     */
    private String getZipCode(InsuredAddress adresseAssure) {
        String npaTrouve = "";

        if (Objects.isNull(adresseAssure.getZipCode())) {
            String town = adresseAssure.getTown().trim();
            Matcher matcher = Pattern.compile("^\\d+").matcher(town); // on cherche une suite de chiffres
            if (matcher.find()) { // si on trouve
                npaTrouve = matcher.group(0); // on récupère ce qui a été trouvé
            }
            else {
                LOG.info("NPA non trouvé dans InsuredAdress.town");
            }
        } else return adresseAssure.getZipCode();

        return npaTrouve;
    }

    private APTurnovers createTurnovers(Turnovers turnoversXml, BTransaction transaction) throws Exception {
        APTurnovers turnovers = new APTurnovers(referenceData);

        if (Objects.nonNull(turnoversXml)) {
            if (Objects.nonNull(turnoversXml.getMonthlyTurnovers())){
                for(MonthlyTurnover monthlyTurnoverXml : turnoversXml.getMonthlyTurnovers().getMonthlyTurnover()) {
                    APTurnover monthlyTurnover = new APTurnover();
                    monthlyTurnover.setSession(bsession);
                    monthlyTurnover.setAmount(String.valueOf(monthlyTurnoverXml.getAmount()));
                    monthlyTurnover.setUnit(monthlyTurnoverXml.getUnit());
                    monthlyTurnover.setMonth(monthlyTurnoverXml.getMonth());
                    monthlyTurnover.setReferenceData(referenceData);
                    try {
                        monthlyTurnover.add(transaction);
                    } catch (Exception e) {
                        infos.add("Un problème a été rencontré lors de mise en DB des Turnovers ");
                        LOG.error("APImportationAPGPandemie#createTurnovers : Erreur rencontré lors de la création des turnovers Mensuels pour l'assuré", e);
                    }
                    turnovers.addTurnover(monthlyTurnover);
                }
            }

            if (Objects.nonNull(turnoversXml.getAnnualTurnovers())){
                for(AnnualTurnover annualTurnoverXml : turnoversXml.getAnnualTurnovers().getAnnualTurnover()) {
                    APTurnover annualTurnover = new APTurnover();
                    annualTurnover.setSession(bsession);
                    annualTurnover.setAmount(String.valueOf(annualTurnoverXml.getAmount()));
                    annualTurnover.setUnit(annualTurnoverXml.getUnit());
                    annualTurnover.setYear(String.valueOf(annualTurnoverXml.getYear()));
                    annualTurnover.setReferenceData(referenceData);
                    try {
                        annualTurnover.add(transaction);
                    } catch (Exception e) {
                        infos.add("Un problème a été rencontré lors de mise en DB des Turnovers ");
                        LOG.error("APImportationAPGPandemie#createTurnovers : Erreur rencontré lors de la création des turnovers Annuels pour l'assuré", e);
                    }
                    turnovers.addTurnover(annualTurnover);
                }
            }
        }

        if (!hasError(bsession, transaction)) {
            transaction.commit();
            LOG.info("ImportAPGPandemie - Création des TurnOvers - Traitement in success...");
        }

        return turnovers;
    }

    /**
     * Mise en object des Beneficiares contenu dans le message.
     *
     * @param beneficiariesXml
     * @return
     */
    private APBeneficiaries createBeneficiariesObject(Beneficiaries beneficiariesXml) {
        // Creation de l'objet et lien avec demande.
        APBeneficiaries beneficiaries = new APBeneficiaries(referenceData);

        if (Objects.nonNull(beneficiariesXml)){
            for(Beneficiary beneficiaryXml : beneficiariesXml.getBeneficiary()) {
                APBeneficiary beneficiary = new APBeneficiary();
                beneficiary.setNumAVS(beneficiaryXml.getInsuredPerson().getVn());
                beneficiary.setName(beneficiaryXml.getInsuredPerson().getOfficialName());
                beneficiary.setFirstName(beneficiaryXml.getInsuredPerson().getFirstName());
                beneficiary.setDateOfBirth(beneficiaryXml.getInsuredPerson().getDateOfBirth());
                beneficiary.setEmail(beneficiaryXml.getInsuredAddress().getEmail());
                beneficiary.setNameStreetWithNumber(beneficiaryXml.getInsuredAddress().getStreetWithNr());
                beneficiary.setTown(beneficiaryXml.getInsuredAddress().getTown());
                beneficiary.setZipCode(beneficiaryXml.getInsuredAddress().getZipCode());
                beneficiary.setPhoneNumber(beneficiaryXml.getInsuredAddress().getPhone());
                beneficiary.setAmountLastAnnualIncome(beneficiaryXml.getLastAnnualIncome().getAmount());
                beneficiary.setUnitLastAnnualIncome(beneficiaryXml.getLastAnnualIncome().getUnit());
                beneficiary.setYearLastAnnualIncome(beneficiaryXml.getLastAnnualIncome().getYear());
                beneficiary.setAmountLastIncome(beneficiaryXml.getSalary().getLastIncome());
                beneficiary.setPositionType(beneficiaryXml.getEmploymentInfo().getPositionType());
                beneficiary.setAssimilablePosition(beneficiaryXml.getEmploymentInfo().isAssimilablePosition());
                beneficiary.setRhtIndemnity(beneficiaryXml.getEmploymentInfo().isRHTIndemnity());
                beneficiary.setHasLossOfProfit(beneficiaryXml.getEmploymentInfo().isHasLossOfProfit());
                beneficiary.setReduceWorkHours(beneficiaryXml.getEmploymentInfo().isReducedWorkHours());

                beneficiaries.addBeneficiaries(beneficiary);
            }
        }
        return beneficiaries;
    }

    private String getNpaFormat(String npa) {
        if(!Objects.isNull(npa)){
            String npaTrim = StringUtils.trim(npa);
            if(StringUtils.isNumeric(npaTrim)){
                return npaTrim;
            }else{
                infos.add("NPA incorrect ! Celui-ci doit être dans un format numérique uniquement ! Valeur: "+npa);
            }
        }
        return "";
    }

    private boolean isDonneesAdresseValide(InsuredAddress adresseAssure, String npa) {
        if(JadeStringUtil.isBlankOrZero(npa)){
            return false;
        }
        return !JadeStringUtil.isBlankOrZero(adresseAssure.getStreetWithNr()) || !JadeStringUtil.isBlankOrZero(adresseAssure.getTown());
    }

    private boolean isAffiliationRenseigneExist(Activity activiteProfessionnelle, String nss) throws Exception {
        try {
            boolean affiliationExist = false;
            if (!Objects.isNull(activiteProfessionnelle.getCompanies().getCompany())) {
                for (Company company : activiteProfessionnelle.getCompanies().getCompany()) {
                    if (!JadeStringUtil.isBlankOrZero(company.getAffiliateID())) {
                        AFAffiliation affiliation = findAffiliationByNum(company.getAffiliateID());
                        if (affiliation != null && !JadeStringUtil.isBlankOrZero(affiliation.getAffiliationId())) {
                            affiliationExist = true;
                            if (!listIDAff.contains(affiliation.getAffiliationId())) {
                                listIDAff.add(affiliation.getAffiliationId());
                            }
                        }
                    }
                }
            }
            return affiliationExist;
        }catch(Exception e){
            errors.add("Une erreur est survenue lors de la recherche de l'affilié pour le tiers suivant : "+nss);
            LOG.error("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la récupération de l'affiliaton " + nss, e);
            throw new Exception("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la récupération de l'affiliation " + nss);
        }
    }

    private void createContact(PRTiersWrapper tiers, String email) throws Exception {
        try {
            if(!JadeStringUtil.isBlankOrZero(email)) {
                // Création du contact
                TIContact contact = new TIContact();
                contact.setSession(bsession);
                contact.setNom(tiers.getNom());
                contact.setPrenom(tiers.getPrenom());
                contact.add(bsession.getCurrentThreadTransaction());

                // Création du moyen de communication
                TIMoyenCommunication moyenCommunication = new TIMoyenCommunication();
                moyenCommunication.setSession(bsession);
                moyenCommunication.setMoyen(email);
                moyenCommunication.setTypeCommunication(TIMoyenCommunication.EMAIL);
                moyenCommunication.setIdContact(contact.getIdContact());
                moyenCommunication.setIdApplication(APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getValue());
                moyenCommunication.add();

                // Lien du contact avec le tiers
                TIAvoirContact avoirContact = new TIAvoirContact();
                avoirContact.setSession(bsession);
                avoirContact.setIdTiers(tiers.getIdTiers());
                avoirContact.setIdContact(contact.getIdContact());
                avoirContact.add();
            }

        } catch (Exception e) {
            errors.add("Une erreur est survenue lors de la création du contact pour l'id tiers : "+tiers.getNSS()+" - "+email);
            LOG.error("APImportationAPGPandemie#createContact : Une erreur est survenue lors de la création du contact pour l'id tiers " + tiers.getNSS(), e);
            throw new Exception("APImportationAPGPandemie#createContact : Une erreur est survenue lors de la création du contact pour l'id tiers " + tiers.getNSS());
        }


    }

    private PRTiersWrapper creationTiers(InsuredPerson assure, InsuredAddress adresseAssure) throws Exception {
        String sexePy = getSexePy(sexeCI);
        // les noms et prenoms doivent être renseignés pour insérer un nouveau tiers
        if (JadeStringUtil.isEmpty(assure.getOfficialName()) || JadeStringUtil.isEmpty(assure.getFirstName())) {
            bsession.addError(bsession.getLabel("APAbstractDroitPHelper.ERREUR_NOM_OU_PRENOM_INVALIDE"));
            return null;
        }

        // date naissance obligatoire pour inserer
        if (JAUtil.isDateEmpty(tranformGregDateToGlobDate(assure.getDateOfBirth()))) {
            bsession.addError(bsession.getLabel("DATE_NAISSANCE_INCORRECTE"));
            return null;
        }

        // recherche du canton si le npa est renseigné
        String canton = "";
        if (!JadeStringUtil.isIntegerEmpty(adresseAssure.getZipCode())) {
            try {
                canton = PRTiersHelper.getCanton(bsession, adresseAssure.getZipCode());

                if (canton == null) {
                    // canton non trouvé
                    canton = "";
                }
            } catch (Exception e1) {
                bsession.addError(bsession.getLabel("CANTON_INTROUVABLE"));

            }
        }

        // insertion du tiers
        // si son numero AVS est suisse on lui met suisse comme pays, sinon on
        // lui met un pays bidon qu'on pourrait
        // interpreter comme "etranger"
        PRAVSUtils avsUtils = PRAVSUtils.getInstance(assure.getVn());

        String idTiers = PRTiersHelper.addTiers(bsession, assure.getVn(), assure.getOfficialName(),
                assure.getFirstName(), sexePy, tranformGregDateToGlobDate(assure.getDateOfBirth()),
                "",
                avsUtils.isSuisse(assure.getVn()) ? TIPays.CS_SUISSE : PRTiersHelper.ID_PAYS_BIDON, canton, "",
                String.valueOf(EtatCivil.CELIBATAIRE.getCodeSysteme()));

        return PRTiersHelper.getTiersParId(bsession, idTiers);
    }

    private String getSexePy(String sexe) {
        if(CICompteIndividuel.CS_FEMME.equalsIgnoreCase(sexe)){
            return ITIPersonne.CS_FEMME;
        }else{
            return ITIPersonne.CS_HOMME;
        }
    }

    private void initPeriodesPanFromXml(ActivityCessation activityCessation){
        try {
            XMLGregorianCalendar dateDebutXml = null;
            XMLGregorianCalendar dateFinXml = null;
            int nbJours = 0;
            if (Objects.nonNull(activityCessation.getChildCareCessation()) && activityCessation.getChildCareCessation().isIsSelected()) {
                for (ChildCarePeriod periode : activityCessation.getChildCareCessation().getChildCarePeriods().getChildCarePeriod()) {
                    dateDebutXml = periode.getFrom();
                    dateFinXml = periode.getTo();
                    nbJours = periode.getNumbersOfDays();
                    periodes.add(buildPeriode(dateDebutXml, dateFinXml, nbJours));
                }
            } else {
                if (Objects.nonNull(activityCessation.getQuarantineCessation()) && activityCessation.getQuarantineCessation().isIsSelected()) {
                    // Vague 2
                    if (activityCessation.getQuarantineCessation().isIsQuarantineDueToTravel()) {
                        dateDebutXml = activityCessation.getQuarantineCessation().getQuarantineTravel().getFrom();
                        dateFinXml = activityCessation.getQuarantineCessation().getQuarantineTravel().getTo();
                    } else {
                    dateDebutXml = activityCessation.getQuarantineCessation().getQuarantinePeriod().getFrom();
                    dateFinXml = activityCessation.getQuarantineCessation().getQuarantinePeriod().getTo();
                    }
                } else if (Objects.nonNull(activityCessation.getIndependantWorkClosureCessation()) && activityCessation.getIndependantWorkClosureCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getFrom();
                    dateFinXml = activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getTo();
                } else if (Objects.nonNull(activityCessation.getIndependantEventCessation()) && activityCessation.getIndependantEventCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getFrom();
                    dateFinXml = activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getTo();
                } else if (Objects.nonNull(activityCessation.getIndependantLossOfIncomeCessation()) && activityCessation.getIndependantLossOfIncomeCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getFrom();
                    dateFinXml = activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getTo();

                    // Vague 2
                } else if (Objects.nonNull(activityCessation.getActivityLimitationCessation()) && activityCessation.getActivityLimitationCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getFrom();
                    dateFinXml = activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getTo();
                } else if (Objects.nonNull(activityCessation.getEventCessation()) && activityCessation.getEventCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getEventCessation().getEventPeriod().getFrom();
                    dateFinXml = activityCessation.getEventCessation().getEventPeriod().getTo();
                } else if (Objects.nonNull(activityCessation.getWorkClosureCessation()) && activityCessation.getWorkClosureCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getWorkClosureCessation().getWorkClosurePeriod().getFrom();
                    dateFinXml = activityCessation.getWorkClosureCessation().getWorkClosurePeriod().getTo();
                }else if (Objects.nonNull(activityCessation.getVulnerabilityCessation()) && Objects.nonNull(activityCessation.getVulnerabilityCessation().getPeriod())) {
                    dateDebutXml = activityCessation.getVulnerabilityCessation().getPeriod().getFrom();
                    dateFinXml = activityCessation.getVulnerabilityCessation().getPeriod().getTo();
                }
                periodes.add(buildPeriode(dateDebutXml, dateFinXml, nbJours));
            }
        }catch (Exception e){
            errors.add("Erreur lors de la récupération des périodes: Vérifier les données du XML ("+e.getMessage()+")");
            LOG.error("Erreur lors de la récupération des périodes: "+e.getMessage());
        }
    }

    private APPeriodeAPG buildPeriode(XMLGregorianCalendar from, XMLGregorianCalendar to, int numbersOfDays) throws Exception {
        APPeriodeAPG periodePan = new APPeriodeAPG();
        if(!Objects.isNull(from)){
            periodePan.setDateDebutPeriode(tranformGregDateToGlobDate(from));
        }else{
            throw new Exception("Date de début du droit obligatoire (vérifier les données de la demande)");
        }
        if(!Objects.isNull(numbersOfDays) || numbersOfDays > 0){
            periodePan.setNbrJours(String.valueOf(numbersOfDays));
        }
        if(!Objects.isNull(to)){
            periodePan.setDateFinPeriode(tranformGregDateToGlobDate(to));
        }else{
            periodePan.setNbrJours("0");
        }
        return periodePan;
    }

    private void createPeriodes(BTransaction transaction, String idDroit){
        try {
            for (APPeriodeAPG periode : periodes) {
                periode.setSession(bsession);
                periode.setIdDroit(idDroit);
                // Pour autoriser la date de fin vide
                periode.wantCallValidate(false);
                periode.add(transaction);
            }
        }catch(Exception e){
            errors.add("Erreur lors de l'enregistrement des périodes pour le droit "+idDroit);
            LOG.error("Erreur lors de la récupération des périodes pour le droit "+idDroit+" - Exception: "+e.getMessage());
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Période");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans la création des périodes
                JadeThread.logClear();
            }
        }
    }

    private void initDatesDroit(ActivityCessation activityCessation) throws Exception {
        initPeriodesPanFromXml(activityCessation);
        List<String> datesDeDebut = new ArrayList<>();
        List<String> datesDeFin = new ArrayList<>();
        boolean hasDateFinVide = false;
        for(APPeriodeAPG periode : periodes){
            if(!JadeStringUtil.isBlankOrZero(periode.getDateDebutPeriode())){
                datesDeDebut.add(periode.getDateDebutPeriode());
            }
            if(!JadeStringUtil.isBlankOrZero(periode.getDateFinPeriode())){
                datesDeFin.add(periode.getDateFinPeriode());
            }else{
                hasDateFinVide = true;
            }
        }
        if(!datesDeDebut.isEmpty()){
            dateDebutDroit = PRDateUtils.sortDate(datesDeDebut, PRDateUtils.DateOrder.NEWER_TO_OLDER).get(0);
        }
        if(!hasDateFinVide && !datesDeFin.isEmpty()){
            dateFinDroit = PRDateUtils.sortDate(datesDeFin, PRDateUtils.DateOrder.OLDER_TO_NEWER).get(0);
        }
    }

    private String transformCatEntrepriseToCode(String xmlEnumCatEnt){
        if(!Objects.isNull(xmlEnumCatEnt)) {
            switch (xmlEnumCatEnt) {
                case "MAGASINS":
                    return "52026001";
                case "RESTAURANTS":
                    return "52026002";
                case "BARS":
                    return "52026003";
                case "DIVERTISSEMENT":
                    return "52026004";
                case "MASSAGE":
                    return "52026005";
                case "AUTRE":
                    return "52026006";
                default:
                    return "";
            }
        }
        return "";
    }

    private String transformMotifGardToCode(String xmlEnumMotifG){
        if(!Objects.isNull(xmlEnumMotifG)) {
            switch (xmlEnumMotifG) {
                case "ECOLE":
                    return "52027001";
                case "CRECHE":
                    return "52027002";
                case "RISQUE":
                    return "52027003";
                default:
                    return "";
            }
        }
        return "";
    }

    private String transformMotifGardHandicapToCode(String xmlEnumMotifG){
        if(!Objects.isNull(xmlEnumMotifG)) {
            switch (xmlEnumMotifG) {
                case "ECOLE":
                    return "52030001";
                case "HANDICAPSPECIALE":
                    return "52030002";
                case "HANDICAPINSTITUTION":
                    return "52030003";
                default:
                    return "";
            }
        }
        return "";
    }

    private String transformMotifGardeVague2ToCode(String xmlEnumMotifG) {
        if(!Objects.isNull(xmlEnumMotifG)) {
            switch (xmlEnumMotifG) {
                case "FERMETURE_GARDE_ENFANT":
                    return "52040001";
                case "QUARANTAINE_ENFANT":
                    return "52040002";
                default:
                    return "";
            }
        }
        return "";
    }

    private String transformGroupeRisqueToCode(String xmlEnumGroupeR){
        if(!Objects.isNull(xmlEnumGroupeR)) {
            switch (xmlEnumGroupeR) {
                case "MALADIE":
                    return "52028001";
                case "AGE_SUP_65":
                    return "52028002";
                default:
                    return "";
            }
        }
        return "";
    }

    private String transformCauseMaladieToCode(String xmlEnumCauseMal){
        if(!Objects.isNull(xmlEnumCauseMal)) {
            switch (xmlEnumCauseMal) {
                case "HYPERTENSION":
                    return "52029001";
                case "DIABETE":
                    return "52029002";
                case "CARDIO":
                    return "52029003";
                case "RESPIRATOIRE":
                    return "52029004";
                case "FAIBLESSE":
                    return "52029005";
                case "CANCER":
                    return "52029006";
                default:
                    return "";
            }
        }
        return "";
    }

    private boolean isMaladie(String csGroupeRisque){
        if(StringUtils.equals("52028001", csGroupeRisque)){
            return true;
        }
        return false;
    }

    private APDroitPanSituation creationDroitPandemie(APDroitPandemie droitPandemie, FamilyMembers membresFamilleAssure, boolean isIndependant, PaymentContact paymentContact,
                                                      Activity activity, Salary salary, ActivityCessation activityCessation, BTransaction transaction) throws Exception {
        APDroitPanSituation droitPanSituation = new APDroitPanSituation();
        try {
            droitPanSituation.setSession(bsession);
            droitPanSituation.setReferenceData(referenceData);
            // Activity
            droitPanSituation.setActiviteSalarie(!isIndependant);
            if (Objects.nonNull(activity.getActivityCategory())) {
            droitPanSituation.setCategorieEntreprise(Objects.isNull(activity.getActivityCategory().getActivityType())?
                    "": transformCatEntrepriseToCode(activity.getActivityCategory().getActivityType()));
            droitPanSituation.setCategorieEntrepriseLibelle(activity.getActivityCategory().getActivityOtherDetail());
            }
            droitPanSituation.setCopieDecompteEmployeur(salary.isSendDecompteToEmployer());
            // ActivityCessation
            if(Objects.nonNull(activityCessation.getChildCareCessation()) && activityCessation.getChildCareCessation().isIsSelected()) {
                // Création de la situation Familiale
                creationSituationFamiliale(membresFamilleAssure, droitPandemie.getId(), transaction);
                // Vague 2
                    droitPanSituation.setMotifGarde(Objects.isNull(activityCessation.getChildCareCessation().getChildCareCause()) ?
                        "" : transformMotifGardeVague2ToCode(activityCessation.getChildCareCessation().getChildCareCause()));

                // Vague 1
//                if (activityCessation.getChildCareCessation().isChildCareWithHandicap()) {
//                    droitPanSituation.setMotifGardeHandicap(Objects.isNull(activityCessation.getChildCareCessation().getChildCareCause()) ?
//                            "" : transformMotifGardHandicapToCode(activityCessation.getChildCareCessation().getChildCareCause()));
//                } else {
//                    droitPanSituation.setMotifGarde(Objects.isNull(activityCessation.getChildCareCessation().getChildCareCause()) ?
//                            "" : transformMotifGardToCode(activityCessation.getChildCareCessation().getChildCareCause()));
//                }

                if(Objects.nonNull(activityCessation.getChildCareCessation().getChildCarePersonRiskProof())){
                    droitPanSituation.setGroupeRisque(transformGroupeRisqueToCode(activityCessation.getChildCareCessation().getChildCarePersonRiskProof().getChildCarePersonType()));
                    if(isMaladie(droitPanSituation.getGroupeRisque())) {
                        droitPanSituation.setCauseMaladie(transformCauseMaladieToCode(activityCessation.getChildCareCessation().getChildCarePersonRiskProof().getChildCarePersonDiseaseCause()));
                    }
                }
            }
            if(Objects.nonNull(activityCessation.getQuarantineCessation()) && activityCessation.getQuarantineCessation().isIsSelected()) {
                droitPanSituation.setQuarantaineOrdonnee(activityCessation.getQuarantineCessation().isPrescribedQuarantine());
                droitPanSituation.setQuarantaineOrdonneePar(activityCessation.getQuarantineCessation().getPrescribedBy());
            }
            if(Objects.nonNull(activityCessation.getIndependantWorkClosureCessation()) && activityCessation.getIndependantWorkClosureCessation().isIsSelected()) {
                droitPanSituation.setDateFermetureEtablissementDebut(Objects.isNull(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getFrom()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getFrom()));
                droitPanSituation.setDateFermetureEtablissementFin(Objects.isNull(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getTo()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getTo()));
            }
            if(Objects.nonNull(activityCessation.getIndependantEventCessation()) && activityCessation.getIndependantEventCessation().isIsSelected()) {
                droitPanSituation.setDateDebutManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getEventDate()));
                droitPanSituation.setDateDebutManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getFrom()));
                droitPanSituation.setDateFinManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getTo()));
            }

            // Vague 2
             if (Objects.nonNull(activityCessation.getActivityLimitationCessation()) && activityCessation.getActivityLimitationCessation().isIsSelected()) {
                 droitPanSituation.setDateDebutActiviteLimitee(Objects.isNull(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getFrom()) ?
                         "" : tranformGregDateToGlobDate(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getFrom()));
                 droitPanSituation.setDateFinActiviteLimitee(Objects.isNull(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getTo()) ?
                         "" : tranformGregDateToGlobDate(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getTo()));
                 droitPanSituation.setStartDateActiviteLimitee(Objects.isNull(activityCessation.getActivityLimitationCessation().getActivityDateStart()) ?
                         "" : tranformGregDateToGlobDate(activityCessation.getActivityLimitationCessation().getActivityDateStart()));
                 droitPanSituation.setLossValueActiviteLimitee(Objects.isNull(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getLossValue()) ?
                         "" : String.valueOf(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getLossValue()));
                 droitPanSituation.setUnitActiviteLimitee(Objects.isNull(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getUnit()) ?
                         "" : String.valueOf(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getUnit()));
                 droitPanSituation.setReasonActiviteLimitee(Objects.isNull(activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getReason()) ?
                         "" : activityCessation.getActivityLimitationCessation().getLossOfIncomeTurnoverPeriod().getReason());
             }
            if (Objects.nonNull(activityCessation.getEventCessation()) && activityCessation.getEventCessation().isIsSelected()) {
                // Deprecated
//                droitPanSituation.setDateDebutManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getEventCessation().getEventPeriod().getEventDate()));
                droitPanSituation.setDateDebutManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getEventCessation().getEventPeriod().getFrom()));
                droitPanSituation.setDateFinManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getEventCessation().getEventPeriod().getTo()));
            }
            if (Objects.nonNull(activityCessation.getWorkClosureCessation()) && activityCessation.getWorkClosureCessation().isIsSelected()) {
                droitPanSituation.setDateFermetureEtablissementDebut(Objects.isNull(activityCessation.getWorkClosureCessation().getWorkClosurePeriod().getFrom()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getWorkClosureCessation().getWorkClosurePeriod().getFrom()));
                droitPanSituation.setDateFermetureEtablissementFin(Objects.isNull(activityCessation.getWorkClosureCessation().getWorkClosurePeriod().getTo()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getWorkClosureCessation().getWorkClosurePeriod().getTo()));
            }
            if (Objects.nonNull(activityCessation.getVulnerabilityCessation()) && Objects.nonNull(activityCessation.getVulnerabilityCessation().getPeriod())) {
                droitPanSituation.setDateDebutVulnerability(Objects.isNull(activityCessation.getVulnerabilityCessation().getPeriod().getFrom()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getVulnerabilityCessation().getPeriod().getFrom()));
                droitPanSituation.setDateFinVulnerability(Objects.isNull(activityCessation.getVulnerabilityCessation().getPeriod().getTo()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getVulnerabilityCessation().getPeriod().getTo()));
                droitPanSituation.setReasonVulnerability(activityCessation.getVulnerabilityCessation().getReason());
                droitPanSituation.setPartialActivityPercentVulnerability(Objects.isNull(activityCessation.getVulnerabilityCessation().getPartialActivityPercent()) ?
                        "" : String.valueOf(activityCessation.getVulnerabilityCessation().getPartialActivityPercent()));
            }


            droitPanSituation.setIdDroit(droitPandemie.getIdDroit());
            if(paymentContact != null) {
                droitPanSituation.setPaiementEmployeur(paymentContact.isDirectPayment());
            }

            if (Objects.nonNull(activity.getTeleworking()) && activity.getTeleworking().isIsSelected()){
                droitPanSituation.setTeletravail(true);
                droitPanSituation.setUnitPerte(Objects.isNull(activity.getTeleworking().getUnit()) ? "" : activity.getTeleworking().getUnit());
                droitPanSituation.setValeurPerte(Objects.isNull(activity.getTeleworking().getLossValue()) ? "" : String.valueOf(activity.getTeleworking().getLossValue()));
            }

            if (Objects.nonNull(activityCessation.getIndependantLossOfIncomeCessation()) && activityCessation.getIndependantLossOfIncomeCessation().isIsSelected()) {
                droitPanSituation.setDateDebutPerteGains(Objects.isNull(activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getFrom()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getFrom()));
                droitPanSituation.setDateFinPerteGains(Objects.isNull(activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getTo()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getTo()));
            }

            if (Objects.nonNull(senderId)) {
                droitPanSituation.setSenderId(senderId);
            }

            droitPanSituation.add(transaction);
        } catch (Exception e) {
            errors.add("Erreur dans l'enregistrement des données spécifiques du formulaire ("+e.getMessage()+")");
            LOG.error("APImportationAPGPandemie#creationDroitPandemie : erreur lors de la création de la demande du droit Pandémie", e);
            throw new Exception("APImportationAPGPandemie#creationDroitPandemie : erreur lors de la création de la demande du droit Pandémie", e);
        }
        return droitPanSituation;
    }

    private void creationSituationFamiliale(FamilyMembers membresFamilleAssure, String idDroit, BTransaction transaction) {
        try {
            for(Child child : membresFamilleAssure.getChildren().getChild()){
                APSituationFamilialePan enfant = new APSituationFamilialePan();
                enfant.setNoAVS(child.getVn());
                enfant.setNom(child.getOfficialName());
                enfant.setPrenom(child.getFirstName());
                enfant.setDateNaissance(tranformGregDateToGlobDate(child.getDateOfBirth()));
                enfant.setIdDroit(idDroit);
                enfant.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
                enfant.setSession(bsession);
                enfant.add(transaction);
            }
            if(isDonneesConjointValides(membresFamilleAssure.getPartner())) {
                APSituationFamilialePan conjoint = new APSituationFamilialePan();
                conjoint.setNoAVS(membresFamilleAssure.getPartner().getVn());
                conjoint.setNom(membresFamilleAssure.getPartner().getOfficialName());
                conjoint.setPrenom(membresFamilleAssure.getPartner().getFirstName());
                conjoint.setDateNaissance(tranformGregDateToGlobDate(membresFamilleAssure.getPartner().getDateOfBirth()));
                conjoint.setIdDroit(idDroit);
                conjoint.setType(IAPDroitMaternite.CS_TYPE_CONJOINT);
                conjoint.setSession(bsession);
                conjoint.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Impossible de créer la situation professionnelle ");
            LOG.error("APImportationAPGPandemie#creationSituationProfessionnelle : erreur lors de la création de la situation professionnelle ", e);
        };
    }

    private boolean isDonneesConjointValides(Partner partner) {
        return (!JadeStringUtil.isBlankOrZero(partner.getVn())
                || !JadeStringUtil.isBlankOrZero(partner.getFirstName())
                || !JadeStringUtil.isBlankOrZero(partner.getOfficialName())
                || !JadeStringUtil.isBlankOrZero(tranformGregDateToGlobDate(partner.getDateOfBirth())));
    }

    private boolean isIndependant(Activity activity) {
        return "INDEPENDANT".equalsIgnoreCase(activity.getEmploymentType());
    }

    private boolean isAssimileEmployeur(Activity activity) {
        return "ASSIM_EMPLOYEUR".equalsIgnoreCase(activity.getEmploymentType());
    }

    private boolean isEmploye(Activity activity) {
        return "EMPLOYE".equalsIgnoreCase(activity.getEmploymentType());
    }

    private PRDemande creationDemande(String idTiers) throws Exception {
        PRDemande retValue = null;
        String typeDemande = IPRDemande.CS_TYPE_PANDEMIE;

        try {
            PRDemandeManager mgr = new PRDemandeManager();
            mgr.setSession(bsession);
            mgr.setForIdTiers(JadeStringUtil.isEmpty(idTiers) ? PRDemande.ID_TIERS_DEMANDE_BIDON : idTiers);
            mgr.setForTypeDemande(typeDemande);
            mgr.find();

            if (mgr.isEmpty()) {
                retValue = new PRDemande();
                retValue.setIdTiers(idTiers);
                retValue.setEtat(IPRDemande.CS_ETAT_OUVERT);
                retValue.setTypeDemande(typeDemande);
                retValue.setSession(bsession);
                retValue.add();
            } else if (!mgr.isEmpty()) {
                retValue = (PRDemande) mgr.get(0);
            }
        }catch (Exception e){
            errors.add("Erreur dans la création de la demande du droit (idTiers : "+idTiers+")");
            LOG.error("APImportationAPGPandemie#creationDemande : erreur lors de la création de la demande du droit ", e.getStackTrace());
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Demande");
            }
            throw new Exception("APImportationAPGPandemie#creationDemande : erreur lors de la création de la demande du droit ", e);
        }
        return retValue;
    }

    private PRTiersWrapper getTiersByNss(String nss) throws Exception {
        try {
            return PRTiersHelper.getTiers(bsession, nss);
        } catch (Exception e) {
            errors.add("Impossible de récupérer le tiers : "+nss);
            LOG.error("APImportationAPGPandemie#getTiersByNss : erreur lors de la récupération du tiers " + nss, e);
            throw new Exception("APImportationAPGPandemie#getTiersByNss : erreur lors de la récupération du tiers ");
        }
    }

    private APDroitPandemie creationDroit(PRDemande demande, Content content, BTransaction transaction, String npa) throws Exception {
        APDroitPandemie droitPandemie = new APDroitPandemie();
        droitPandemie.setSession(bsession);
        try {
            droitPandemie.setIdDemande(demande.getIdDemande());
            droitPandemie.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitPandemie.setGenreService(getCSGenreService(content));
            droitPandemie.setIdCaisse(creationIdCaisse());

            Date date = new Date();
            droitPandemie.setDateDepot(date.getSwissValue());
            droitPandemie.setDateReception(date.getSwissValue());
            droitPandemie.setDateDebutDroit(getDateDebutDroit());
            droitPandemie.setDateFinDroit(getDateFinDroit());
            droitPandemie.setIsSoumisImpotSource(content.getActivity().getSalary().isWithholdingTax());
            droitPandemie.setNpa(npa);
            droitPandemie.setPays("100");
            // On met l'id du Gestionnaire, s'il s'agit d'une demande faite par un gestionnaire
            if (isImportGestionnaire && !userGestionnaire.isEmpty()) {
                droitPandemie.setIdGestionnaire(userGestionnaire);
            }
            droitPandemie.setNoRevision(IAPDroitAPG.CS_REVISION_STANDARD);
            droitPandemie.add(transaction);
        }catch (Exception e){
            errors.add("Impossible de créer le droit APG Pandémie : "+ e.getMessage());
            LOG.error("APImportationAPGPandemie#creationDroit : erreur lors de la création du droit APG Pandémie ", e);
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Droit");
            }
            throw new Exception("APImportationAPGPandemie#creationDroit : erreur lors de la création du droit APG Pandémie ", e);
        }
        return droitPandemie;
    }

    private String getCSGenreService(Content content) {
        String cause = content.getActivityCessation().getActivityCause();
        String employementType = content.getActivity().getEmploymentType();

        // Vague 1 - N'est plus utilisé
        // Le type garde peut être pour enfant avec Handicap. S'il s'agit d'une garde et qu'il a un handicap, alors on retourne le type garde handicap
        if (Objects.equals(cause, "GARDE") && Objects.nonNull(content.getActivityCessation().getChildCareCessation())
                && Objects.nonNull(content.getActivityCessation().getChildCareCessation().isChildCareWithHandicap())
                && content.getActivityCessation().getChildCareCessation().isChildCareWithHandicap()) {
            return APGenreServiceAPG.GardeParentaleHandicap.getCodeSysteme();
        }

        switch (cause){
            // Vague 1 - N'est plus utilisé
            case "GARDE": return APGenreServiceAPG.GardeParentale.getCodeSysteme();
            case "PERTEGAIN": return APGenreServiceAPG.IndependantPerteGains.getCodeSysteme();
            // Vague 2
            case "QUARANTAINE": return APGenreServiceAPG.Quarantaine_17_09_20.getCodeSysteme();
            case "GARDE_ENFANT_MOINS_12_ANS": return APGenreServiceAPG.GardeParentale_17_09_20.getCodeSysteme();
            case "GARDE_ENFANT_HANDICAP": return APGenreServiceAPG.GardeParentaleHandicap_17_09_20.getCodeSysteme();

            case "LIMITATION_ACTIVITE":
                if ("ASSIM_EMPLOYEUR".equalsIgnoreCase(employementType)) {
                    return APGenreServiceAPG.DirigeantSalarieLimitationActivite.getCodeSysteme();
                } else {
                    return APGenreServiceAPG.IndependantLimitationActivite.getCodeSysteme();
                }
            case "FERMETURE":
                if ("ASSIM_EMPLOYEUR".equalsIgnoreCase(employementType)) {
                    return APGenreServiceAPG.DirigeantSalarieFermeture.getCodeSysteme();
                } else {
                    return APGenreServiceAPG.IndependantFermeture.getCodeSysteme();
                }
            case "MANIFESTATION":
                if ("ASSIM_EMPLOYEUR".equalsIgnoreCase(employementType)) {
                    return APGenreServiceAPG.DirigeantSalarieManifestationAnnulee.getCodeSysteme();
                } else {
                    return APGenreServiceAPG.IndependantManifestationAnnulee.getCodeSysteme();
                }
            case "VULNERABLE":
                if ("INDEPENDANT".equalsIgnoreCase(employementType)) {
                    return APGenreServiceAPG.IndependantPersonneVulnerable.getCodeSysteme();
                } else {
                    return APGenreServiceAPG.SalariePersonneVulnerable.getCodeSysteme();
                }
            default: return "";
        }
    }

    private String creationIdCaisse() {
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            return noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            errors.add("Impossible de récupérer les propriétés n° caisse et n° agence");
            LOG.error("APImportationAPGPandemie#creationIdCaisse : A fatal exception was thrown when accessing to the CommonProperties " + exception);
        }
        return null;
    }

    private String tranformGregDateToGlobDate(XMLGregorianCalendar date){
        if (Objects.nonNull(date)){
            return JadeDateUtil.getGlobazFormattedDate(date.toGregorianCalendar().getTime());
        } else {
            return "";
        }

    }

    private void creationRoleApgTiers(String idTiers) throws Exception {
        TIRoleManager roleManager = new TIRoleManager();
        roleManager.setSession(bsession);
        roleManager.setForIdTiers(idTiers);
        BStatement statement = null;
        BTransaction trans = (BTransaction) bsession.newTransaction();
        trans.openTransaction();
        try {
            statement = roleManager.cursorOpen(bsession.getCurrentThreadTransaction());
            TIRole role = null;
            boolean isRolePresent = false;

            while ((role = (TIRole) roleManager.cursorReadNext(statement)) != null) {
                if (IntRole.ROLE_APG.equals(role.getRole())) {
                    isRolePresent = true;
                }
            }

            if (!isRolePresent) {
                // on ajoute le rôle APG au Tier si il ne l'a pas deja
                ITIRole newRole = (ITIRole) bsession.getAPIFor(ITIRole.class);
                newRole.setIdTiers(idTiers);
                newRole.setISession(PRSession.connectSession(bsession, TIApplication.DEFAULT_APPLICATION_PYXIS));
                newRole.setRole(IntRole.ROLE_APG);
                newRole.add(bsession.getCurrentThreadTransaction());
            }
            trans.commit();
        } catch (Exception e) {
            errors.add("Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
            LOG.error("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
            if (trans != null) {
                trans.rollback();
            }
            if(isJadeThreadError()){
                addJadeThreadErrorToListInfos("Rôle");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout du rôle
                JadeThread.logClear();
            }
            throw new Exception("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
        } finally {
            try {
                if (statement != null) {
                    try {
                        roleManager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                trans.closeTransaction();
            }
        }
    }

    private void createAdresseApgPandemie(PRTiersWrapper tiers, InsuredAddress adresseAssure, PaymentContact adressePaiement, String npa) {
        try {
            String domainePandemie = APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getValue();
            if (!JadeStringUtil.isBlankOrZero(domainePandemie)) {
                AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(tiers.getIdTiers(), false, new Date().getSwissValue(), domainePandemie,
                        CS_TYPE_COURRIER, "");
                if(adresse.getFields() == null) {
                    PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
                    searchTiers.setForIdTiers(tiers.getIdTiers());
                    PersonneEtendueSearchComplexModel personneEtendueSearch = TIBusinessServiceLocator
                            .getPersonneEtendueService().find(searchTiers);

                    if (personneEtendueSearch.getNbOfResultMatchingQuery() == 1) {
                        PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) personneEtendueSearch
                                .getSearchResults()[0];
                        AdresseComplexModel adresseDomicile = createAdresseCourrier(personneEtendueComplexModel, adresseAssure, domainePandemie, npa);
                        if (adresseDomicile != null && !adresseDomicile.isNew()) {
                            createAdressePaiement(adresseDomicile, tiers.getIdTiers(), adressePaiement, domainePandemie);
                        }
                    }
                }
            }else{
                infos.add("Aucune adresse créée car le domaine pandémie n'a pas été défini dans la propriété suivante: "+APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getPropertyName());
            }
        }catch (Exception e){
            infos.add("Un problème a été rencontré lors de la création des adresses pour l'assuré suivant"+tiers.getNSS());
            LOG.error("APImportationAPGPandemie#createAdresseApgPandemie : Erreur rencontré lors de la création adresses pour l'assuré", e);
        }
    }

    private AdresseComplexModel createAdresseCourrier(PersonneEtendueComplexModel personneEtendueComplexModel, InsuredAddress adresseAssure, String domainePandemie, String npa) throws JadeApplicationException, JadePersistenceException {
        personneEtendueComplexModel.getTiers();
        AdresseComplexModel adresseComplexModel = new AdresseComplexModel();
        adresseComplexModel.setTiers(personneEtendueComplexModel);
        adresseComplexModel.getAvoirAdresse().setDateDebutRelation(Date.now().getSwissValue());
        adresseComplexModel.getTiers().setId(personneEtendueComplexModel.getTiers().getId());
        adresseComplexModel.getLocalite().setNumPostal(npa);
        adresseComplexModel.getAdresse().setRue(adresseAssure.getStreetWithNr());

        // Bug Fix - Cette correction permet de corriger un bug lorsque la LigneAdresse est laissé à null
        // Le risque de réutiliser une adresse identique avec un complément d'adresse qui n'a rien à voir
        // une correction sera effectuée sur Pyxis par la suite.
        // Mise à chaine vide des champs ligneAdresse
        adresseComplexModel.getAdresse().setLigneAdresse1("");
        adresseComplexModel.getAdresse().setLigneAdresse2("");
        adresseComplexModel.getAdresse().setLigneAdresse3("");
        adresseComplexModel.getAdresse().setLigneAdresse4("");
        AdresseComplexModel adresse =  TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, domainePandemie,
                CS_TYPE_COURRIER, false);
        if(!isJadeThreadError()){
            return adresse;
        }else{
            addJadeThreadErrorToListInfos("Adresse Courrier");
            // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'adresse
            JadeThread.logClear();
            return null;
        }
    }

    private boolean isJadeThreadError(){
        if(JadeThread.logMessages() != null) {
            return JadeThread.logMessages().length > 0;
        }
        return false;
    }

    /**
     * Ajout des erreurs blocantes
     * @param methodeSource
     */
    private void addJadeThreadErrorToListError(String methodeSource) {
        JadeBusinessMessage[] messages = JadeThread.logMessages();
        LOG.error("APImportationAPGPandemie#"+methodeSource+": ");
        errors.add("Concerne: "+methodeSource+"\n");
        for (int i = 0; (messages != null) && (i < messages.length); i++) {
            LOG.error("-->Erreur: "+messages[i].getContents(null));
            errors.add("-->Erreur: "+messages[i].getContents(null));
        }
        errors.add("\n");
    }

    /**
     * Ajout des infos / erreurs non bloquantes
     * @param methodeSource
     */
    private void addJadeThreadErrorToListInfos(String methodeSource) {
        JadeBusinessMessage[] messages = JadeThread.logMessages();
        LOG.error("APImportationAPGPandemie#"+methodeSource+": ");
        infos.add("Concerne: "+methodeSource +" (éléments non bloquants)");
        for (int i = 0; (messages != null) && (i < messages.length); i++) {
            LOG.warn("-->Infos: "+messages[i].getContents(null));
            infos.add("-->Infos: "+messages[i].getContents(null));
        }
        infos.add("\n");
    }

    private void createAdressePaiement(AdresseComplexModel adresseComplexModel, String idTiers, PaymentContact adressePaiementXml, String domainePandemie) throws Exception {
        TIAdressePaiement adressePaiement = new TIAdressePaiement();
        adressePaiement.setIdTiersAdresse(idTiers);
        adressePaiement.setIdAdresse(adresseComplexModel.getAdresse().getId());

        if (!Objects.isNull(adressePaiementXml.getBankAccount().getIban())) {
            String iban = unformatIban(adressePaiementXml.getBankAccount().getIban());
            if(checkChIban(iban).getIsValidChIban()) {
                adressePaiement.setIdTiersBanque(retrieveBanque(iban).getTiersBanque().getId());
                adressePaiement.setCode(IConstantes.CS_ADRESSE_PAIEMENT_IBAN_OK);
                adressePaiement.setNumCompteBancaire(adressePaiementXml.getBankAccount().getIban());

                adressePaiement.setIdMonnaie(AdressePaiementPrimeNoelService.CS_CODE_MONNAIE_FRANC_SUISSE);
                adressePaiement.setIdPays(AdressePaiementPrimeNoelService.CS_CODE_PAYS_SUISSE);
                adressePaiement.setSession(BSessionUtil.getSessionFromThreadContext());
                adressePaiement.add();

                TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
                avoirPaiement.setIdApplication(domainePandemie);
                avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
                avoirPaiement.setDateDebutRelation(Date.now().getSwissValue());
                avoirPaiement.setIdTiers(idTiers);
                avoirPaiement.setSession(BSessionUtil.getSessionFromThreadContext());
                avoirPaiement.add();
            }else {
                infos.add("Paiement adresse non créée : IBAN non valide : " + iban);
            }
        }
        if(isJadeThreadError()){
            addJadeThreadErrorToListInfos("Adresse Paiement");
            // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'adresse
            JadeThread.logClear();
        }
    }

    private String unformatIban(String iban) {
        TIIbanFormater ibanFormatter = new TIIbanFormater();
        return  ibanFormatter.unformat(iban);
    }

    private BanqueComplexModel retrieveBanque(String iban) throws JadeApplicationException, JadePersistenceException {
        BanqueComplexModel banque = new BanqueComplexModel();
        String noClearing = iban.substring(4, 9);

        BanqueSearchComplexModel banqueSearchModel = new BanqueSearchComplexModel();
        banqueSearchModel.setForClearing(noClearing);
        banqueSearchModel.setDefinedSearchSize(1);
        banqueSearchModel = TIBusinessServiceLocator.getBanqueService().find(banqueSearchModel);

        if (banqueSearchModel.getSize() == 1) {
            banque = (BanqueComplexModel) banqueSearchModel.getSearchResults()[0];
        }
        return banque;
    }

    public IbanCheckResultVO checkChIban(String chIban) throws JadeApplicationException {

        IbanCheckResultVO result = new IbanCheckResultVO();

        TIIbanFormater ibanFormatter = new TIIbanFormater();

        chIban = ibanFormatter.unformat(chIban);

        if ((!JadeStringUtil.isEmpty(chIban)) && chIban.startsWith("CH")) {

            result.setIsCheckable(Boolean.TRUE);

            // si l'iban est checkable, on le format meme si il n'est pas valide. ceci pour faciliter la correction
            result.setFormattedIban(ibanFormatter.format(chIban));

            if (TIBusinessServiceLocator.getIBANService().checkIBANforCH(chIban)) {
                result.setIsValidChIban(Boolean.TRUE);
            }
        }

        return result;
    }

    private void creerSituationProfQuarantaine(BTransaction transaction, APDroitPandemie droitPandemie, Salary salary) {
        try {
            boolean isVersementEmployeur = salary.isHasReceiveSalaryDuringInterruption();
            String salaireMensuel = String.valueOf(salary.getLastIncome());
            for(String idAffiliation : listIDAff){
                AFAffiliation affiliation = findAffiliationById(idAffiliation);
                if(affiliation != null) {
                    // creation de la situation prof avec le dernier employeur d'après les CI
                    APEmployeur emp = new APEmployeur();
                    emp.setSession(bsession);
                    emp.setIdTiers(affiliation.getIdTiers());
                    emp.setIdAffilie(affiliation.getAffiliationId());
                    emp.add(transaction);

                    APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                    situationProfessionnelle.setSession(bsession);
                    situationProfessionnelle.setIdDroit(droitPandemie.getIdDroit());
                    situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                    situationProfessionnelle.setIsIndependant(Boolean.FALSE);
                    situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur);
                    situationProfessionnelle.setSalaireMensuel(salaireMensuel);

                    // Vague 2 - Si le salarié est payé sur 13 mois
                    // On ajoute son 13eme mois sans une autre rémunération annuelle
                    if (salary.isHasThirteenthMonth()) {
                        situationProfessionnelle.setAutreRemuneration(salaireMensuel);
                        situationProfessionnelle.setPeriodiciteAutreRemun(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
                    }

                    salaireMensuel = "";
                    situationProfessionnelle.wantCallValidate(false);
                    situationProfessionnelle.add(transaction);
                }
            }
        }catch (Exception e){
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }
    }

    private void creerSituationProfAssimileEmployeur(BTransaction transaction, String nss, APDroitPandemie droitPandemie, Salary salary) {
        try {
            boolean isVersementEmployeur = salary.isHasReceiveSalaryDuringInterruption();
            String salaireMensuel = String.valueOf(salary.getLastIncome());
            for(String idAffiliation : listIDAff){
                AFAffiliation affiliation = findAffiliationById(idAffiliation);
                if(affiliation != null) {
                    // creation de la situation prof avec le dernier employeur d'après les CI
                    APEmployeur emp = new APEmployeur();
                    emp.setSession(bsession);
                    emp.setIdTiers(affiliation.getIdTiers());
                    emp.setIdAffilie(affiliation.getAffiliationId());
                    emp.add(transaction);

                    APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                    situationProfessionnelle.setSession(bsession);
                    situationProfessionnelle.setIdDroit(droitPandemie.getIdDroit());
                    situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                    situationProfessionnelle.setIsIndependant(Boolean.FALSE);
                    situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur);
                    situationProfessionnelle.setSalaireMensuel(salaireMensuel);

                    // Vague 2 - Si le salarié est payé sur 13 mois
                    // On ajoute son 13eme mois sans une autre rémunération annuelle
                    if (salary.isHasThirteenthMonth()) {
                        situationProfessionnelle.setAutreRemuneration(salaireMensuel);
                        situationProfessionnelle.setPeriodiciteAutreRemun(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
                    }

                    CIEcriture ecriture = retrieveEcrituresAssureAF(getSession(), nss, affiliation.getAffilieNumero(), transaction);
                    if(ecriture != null){
                        situationProfessionnelle.setAutreSalaire(ecriture.getMontant());
                        situationProfessionnelle.setPeriodiciteAutreSalaire(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
                    }

                    salaireMensuel = "";
                    situationProfessionnelle.wantCallValidate(false);
                    situationProfessionnelle.add(transaction);
                }
            }
        }catch (Exception e){
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }

    }

    private AFAffiliation findAffiliationById(String affiliateID) throws Exception {
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(bsession);
        manager.setForAffiliationId(affiliateID);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            return (AFAffiliation) manager.getFirstEntity();
        }
        return null;
    }

    private AFAffiliation findAffiliationByNum(String numAff) throws Exception {
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(bsession);
        manager.setForAffilieNumero(numAff);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            return (AFAffiliation) manager.getFirstEntity();
        }
        return null;
    }

    /**
     * Si le tiers possède une affiliation personnelle en cours durant la période du droit, on creer une situation prof.
     * avec cette affiliation
     */
    private APSituationProfessionnelle creerSituationProfIndependant(final BTransaction transaction,
                                                                     final APDroitLAPG droit, final String idTiers) {
        APSituationProfessionnelle situationProfessionnelle = null;
        try {
            String masseAnnuel = "0";
            // on cherche les affiliations pour ce tiers
            final IAFAffiliation affiliation = (IAFAffiliation) bsession.getAPIFor(IAFAffiliation.class);
            affiliation.setISession(PRSession.connectSession(bsession, AFApplication.DEFAULT_APPLICATION_NAOS));
            final Hashtable<Object, Object> param = new Hashtable<Object, Object>();
            param.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
            final IAFAffiliation[] affiliations = affiliation.findAffiliation(param);

            if (affiliations.length > 0) {
                for (int i = 0; i < affiliations.length; i++) {
                    final IAFAffiliation aff = affiliations[i];

                    // InfoRom531 : On ne reprend que les indépendants et indépendant + employeur.
                    if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                            || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                        final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                                .compareDateFirstGreaterOrEqual(bsession, droit.getDateDebutDroit(), aff.getDateDebut());
                        final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                                .compareDateFirstLowerOrEqual(bsession, droit.getDateDebutDroit(), aff.getDateFin());
                        // si l'affiliation est en cours
                        if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                                || globaz.jade.client.util.JadeStringUtil.isEmpty(aff.getDateFin()))) {

                            // creation de l'employeur
                            final APEmployeur emp = new APEmployeur();
                            emp.setSession(bsession);
                            emp.setIdTiers(aff.getIdTiers());
                            emp.setIdAffilie(aff.getAffiliationId());
                            emp.add(transaction);

                            // retrouver la masse annuelle dans les cotisations
                            // pers.

                            // on cherche la decision
                            final CPDecisionManager decision = new CPDecisionManager(); //(CPDecision) bsession.getAPIFor(CPDecision.class);
                            BSession sessionPhenix = new BSession("PHENIX");
                            bsession.connectSession(sessionPhenix);

                            decision.setSession(sessionPhenix);
                            decision.setForIdAffiliation(aff.getAffiliationId());
                            decision.setForIsActive(Boolean.TRUE);
                            decision.setForAnneeDecision(ANNEE_PRISE_COMPTE_SALAIRE);
                            decision.find(BManager.SIZE_NOLIMIT);

                            // on cherche les données calculées en fonction de la
                            // decision
                            if ((decision != null) && (decision.size() > 0)) {

                                final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bsession
                                        .getAPIFor(ICPDonneesCalcul.class);
                                donneesCalcul.setISession(PRSession.connectSession(bsession, "PHENIX"));

                                final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, ((CPDecision) decision.getEntity(0)).getIdDecision());
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);

                                final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                                if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                                    masseAnnuel = donneesCalculs[0].getMontant();
                                }
                            }

                            // creation de la situation prof.
                            situationProfessionnelle = new APSituationProfessionnelle();
                            situationProfessionnelle.setSession(bsession);
                            situationProfessionnelle.setIdDroit(droit.getIdDroit());
                            situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                            situationProfessionnelle.setIsIndependant(Boolean.TRUE);
                            situationProfessionnelle.setIsVersementEmployeur(Boolean.TRUE);
                            // on set la masse annuelle
                            situationProfessionnelle.setRevenuIndependant(masseAnnuel);
                            situationProfessionnelle.wantCallValidate(false);
                            situationProfessionnelle.add(transaction);
                        }
                    }
                }
            }
        }catch (Exception e){
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour un indépendant (IDTiers : "+idTiers+")");
            LOG.error("APImportationAPGPandemie#creerSituationProfSiIndependant : Erreur rencontré lors de la création de la situation professionnelle pour un indépendant ", e);

            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Indépendant");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
                return null;
            }
        }
        return situationProfessionnelle;
    }

    private String findLastIDDroitPandemie(BSession session, final BTransaction transaction, final String idTiers, boolean lastDroit){
        try {
            List<String> etat = new ArrayList<>();
            etat.add(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
            APDroitPanJointTiersManager manager = new APDroitPanJointTiersManager();
            manager.setSession(session);
            manager.setForIdTiers(idTiers);
            if(!lastDroit) {
                manager.setToDateDebutDroit(DATE_RETRO_DROIT_VAGUE_2);
                manager.setToDateFinDroit(DATE_RETRO_DROIT_VAGUE_2);
            }
            manager.setForEtatDroitIn(etat);
            manager.setOrderByDroitDesc(true);
            manager.find(transaction, BManager.SIZE_NOLIMIT);
            List<APDroitPanJointTiers> droitsPandemie = manager.getContainer();
            if (droitsPandemie.size() > 0) {
                return droitsPandemie.get(0).getIdDroit();
            }
        }catch(Exception e){
            errors.add("Erreur rencontré lors de la recherche du dernier droit du tiers (IDTiers : "+idTiers+")");
            LOG.error("APImportationAPGPandemie#findLastIDDroitPandemie : Erreur rencontré lors de la création de la situation professionnelle, lors de la recherche du dernier droit (IDTiers : "+idTiers+")", e);

            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Indépendant");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
                return null;
            }
        }
        return null;
    }

    private List<APSituationProfessionnelle> findSituationProfessionnelleByIDDroit(BSession session, final BTransaction transaction, String idDroit) {
        List<APSituationProfessionnelle> listSituationPro = new ArrayList<>();
        try {
            if (!JadeStringUtil.isBlankOrZero(idDroit)) {
                APSituationProfessionnelleManager managerSitu = new APSituationProfessionnelleManager();
                managerSitu.setSession(session);
                managerSitu.setForIdDroit(idDroit);
                managerSitu.find(transaction, BManager.SIZE_NOLIMIT);

                for (int idSitPro = 0; idSitPro < managerSitu.size(); ++idSitPro) {
                    APSituationProfessionnelle sitPro = (APSituationProfessionnelle) managerSitu.get(idSitPro);
                    listSituationPro.add(sitPro);
                }
            }
        }catch(Exception e){
            errors.add("Erreur rencontré lors de la recherche des situations professionnelles du droit (IDDroit : "+idDroit+")");
            LOG.error("APImportationAPGPandemie#findLastIDDroitPandemie : Erreur rencontré lors de la création de la situation professionnelle, lors de la recherche des situations professionnelles du droit", e);

            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Indépendant");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
                return null;
            }
        }
        return listSituationPro;
    }

    private boolean creerSituationProfPanSelonDroitPrecedent(BSession session, final BTransaction transaction, final APDroitLAPG droit, final String idTiers) {
        // Récupération du dernier droit
        boolean lastDroit = false;
        if((IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE.equals(droit.getGenreService()))) {
            lastDroit = true;
        }
        String idLastDroit = findLastIDDroitPandemie(session, transaction, idTiers, lastDroit);

        // Récupération de(s) dernière(s) situation(s) prof
        List<APSituationProfessionnelle> listLastSituationPro = findSituationProfessionnelleByIDDroit(session, transaction, idLastDroit);
        // Création des situations professionnelles
        List<APSituationProfessionnelle> listLastSituationProCrees = creerCopieSituationProf(session, transaction, listLastSituationPro, droit);
        // Permet de savoir si au moins une situation professionnelle a été créée
        return listLastSituationProCrees.size() > 0;
    }

    private CIEcriture retrieveEcrituresAssureAF(BSession session, String nssAssure, String numAffilie, BTransaction transaction) throws Exception {
        try {
            CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
            ciManager.setSession(session);
            ciManager.setForNumeroAvs(nssAssure);
            ciManager.orderByAvs(true);
            ciManager.find(transaction);
            // pour tous les ci trouvés
            for (int i = 0; i < ciManager.size(); i++) {
                CICompteIndividuel compte = (CICompteIndividuel) ciManager.getEntity(i);
                // Recherche d'une écriture comptable pour l'année 2019 pour les CI
                CIEcritureManager ecritureSource = new CIEcritureManager();
                ecritureSource.setSession(session);
                ecritureSource.setForCompteIndividuelId(compte.getId());
                ecritureSource.setForAffilie(numAffilie);
                ecritureSource.setForAnnee(ANNEE_PRISE_COMPTE_SALAIRE);
                ecritureSource.setForIdTypeCompte(CIEcriture.CS_CI);
                ecritureSource.setOrderBy("KBNANN DESC");
                ecritureSource.find(transaction, BManager.SIZE_NOLIMIT);
                if (!ecritureSource.isEmpty()) {
                    return (CIEcriture) ecritureSource.getEntity(0);
                }
            }
        }catch (Exception e){
            errors.add("Une erreur est survenue lors de la recherche des écritures comptable pour le tiers : "+nssAssure);
            LOG.error("APImportationAPGPandemie#retrieveEcrituresAssure : erreur lors de la récupération des écritures comptable pour le tiers : " + nssAssure, e);
            throw new Exception("APImportationAPGPandemie#retrieveEcrituresAssure : erreur lors de la récupération des écritures comptable pour le tiers : " + nssAssure);
        }
        return null;
    }
    /**
     * Si le tiers possède une affiliation personnelle en cours durant la période du droit, on creer une situation prof.
     * avec cette affiliation
     */
    private void creerSituationProfAssimEmpl(final BTransaction transaction, final APDroitLAPG droit, final String idTiers) {
        try {
            APSituationProfessionnelle situationProfessionnelle = null;
            String masseAnnuel = "0";
            // on cherche les affiliations pour ce tiers
            final IAFAffiliation affiliation = (IAFAffiliation) bsession.getAPIFor(IAFAffiliation.class);
            affiliation.setISession(PRSession.connectSession(bsession, AFApplication.DEFAULT_APPLICATION_NAOS));
            final Hashtable<Object, Object> param = new Hashtable<Object, Object>();
            param.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
            final IAFAffiliation[] affiliations = affiliation.findAffiliation(param);

            if (affiliations.length > 0) {
                for (int i = 0; i < affiliations.length; i++) {
                    final IAFAffiliation aff = affiliations[i];

                    // InfoRom531 : On ne reprend que les indépendants et indépendant + employeur.
                    if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                            || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                        final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                                .compareDateFirstGreaterOrEqual(bsession, droit.getDateDebutDroit(), aff.getDateDebut());
                        final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                                .compareDateFirstLowerOrEqual(bsession, droit.getDateDebutDroit(), aff.getDateFin());
                        // si l'affiliation est en cours
                        if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                                || globaz.jade.client.util.JadeStringUtil.isEmpty(aff.getDateFin()))) {

                            // creation de l'employeur
                            final APEmployeur emp = new APEmployeur();
                            emp.setSession(bsession);
                            emp.setIdTiers(aff.getIdTiers());
                            emp.setIdAffilie(aff.getAffiliationId());
                            emp.add(transaction);

                            // retrouver la masse annuelle dans les cotisations
                            // pers.

                            // on cherche la decision
                            final CPDecisionManager decision = new CPDecisionManager(); //(CPDecision) bsession.getAPIFor(CPDecision.class);
                            BSession sessionPhenix = new BSession("PHENIX");
                            bsession.connectSession(sessionPhenix);

                            decision.setSession(sessionPhenix);
                            decision.setForIdAffiliation(aff.getAffiliationId());
                            decision.setForIsActive(Boolean.TRUE);
                            decision.setForAnneeDecision(Integer.toString(new JADate(droit.getDateDebutDroit()).getYear() - 1));
                            decision.find(BManager.SIZE_NOLIMIT);

                            // on cherche les données calculées en fonction de la
                            // decision
                            if ((decision != null) && (decision.size() > 0)) {

                                final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bsession
                                        .getAPIFor(ICPDonneesCalcul.class);
                                donneesCalcul.setISession(PRSession.connectSession(bsession, "PHENIX"));

                                final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, ((CPDecision) decision.getEntity(0)).getIdDecision());
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);

                                final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                                if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                                    masseAnnuel = donneesCalculs[0].getMontant();
                                }
                            }

                            // creation de la situation prof.
                            situationProfessionnelle = new APSituationProfessionnelle();
                            situationProfessionnelle.setSession(bsession);
                            situationProfessionnelle.setIdDroit(droit.getIdDroit());
                            situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                            situationProfessionnelle.setIsIndependant(Boolean.TRUE);
                            situationProfessionnelle.setIsVersementEmployeur(Boolean.TRUE);
                            // on set la masse annuelle
                            situationProfessionnelle.setRevenuIndependant(masseAnnuel);
                            situationProfessionnelle.wantCallValidate(false);
                            situationProfessionnelle.add(transaction);
                        }
                    }
                }
            }
        }catch (Exception e){
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour un indépendant (IDTiers : "+idTiers+")");
            LOG.error("APImportationAPGPandemie#creerSituationProfSiIndependant : Erreur rencontré lors de la création de la situation professionnelle pour un indépendant ", e);

            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Indépendant");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }
    }

    private List<APSituationProfessionnelle> creerCopieSituationProf(BSession session, BTransaction transaction, List<APSituationProfessionnelle> listLastSituationPro, APDroitLAPG droit) {
        List<APSituationProfessionnelle> situProCrees = new ArrayList<>();
        try {
            for (APSituationProfessionnelle sitPro : listLastSituationPro) {
                // creation de la situation prof.
                APSituationProfessionnelle newSituationProfessionnelle = new APSituationProfessionnelle();
                newSituationProfessionnelle.setSession(session);
                newSituationProfessionnelle.setIdDroit(droit.getIdDroit());
                newSituationProfessionnelle.setIdEmployeur(sitPro.getIdEmployeur());
                newSituationProfessionnelle.setIsIndependant(sitPro.getIsIndependant());
                newSituationProfessionnelle.setIsVersementEmployeur(sitPro.getIsVersementEmployeur());
                // on set la masse annuelle
                newSituationProfessionnelle.setRevenuIndependant(sitPro.getRevenuIndependant());
                newSituationProfessionnelle.wantCallValidate(false);
                newSituationProfessionnelle.add(transaction);
                situProCrees.add(newSituationProfessionnelle);
            }
        }catch (Exception e){
            errors.add("Erreur rencontré lors de la création de la situation professionnelle selon droit précédent ");
            LOG.error("APImportationAPGPandemie#creerSituationProfSiIndependant : Erreur rencontré lors de la création de la situation professionnelle selon droit précédent ", e);

            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Indépendant");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }
        return situProCrees;
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    private boolean hasError(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly() || !errors.isEmpty();
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    private CIEcriture retrieveEcrituresAssure(BSession session, String nssAssure, BTransaction transaction) throws Exception {
        try {
            CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
            ciManager.setSession(session);
            ciManager.setForNumeroAvs(nssAssure);
            ciManager.orderByAvs(true);
            ciManager.find(transaction);
            // pour tous les ci trouvés
            for (int i = 0; i < ciManager.size(); i++) {
                CICompteIndividuel compte = (CICompteIndividuel) ciManager.getEntity(i);
                // Recherche d'une écriture comptable pour l'année 2018 et 2019 pour les CI
                CIEcritureManager ecritureSource = new CIEcritureManager();
                ecritureSource.setForCompteIndividuelId(compte.getId());
                ecritureSource.setFromAnnee("2018");
                ecritureSource.setUntilAnnee("2019");
                ecritureSource.setForIdTypeCompte(CIEcriture.CS_CI);
                ecritureSource.setSession(session);
                // Mois début
                ecritureSource.setOrderBy("KBNANN DESC");
                ecritureSource.find(transaction, BManager.SIZE_NOLIMIT);
                if (!ecritureSource.isEmpty()) {
                    sexeCI = compte.getSexe();
                    CIEcriture ecriture = (CIEcriture) ecritureSource.getEntity(0);
                    if (!listIDAff.contains(ecriture.getEmployeur())) {
                        listIDAff.add(ecriture.getEmployeur());
                    }
                    return ecriture;
                }
            }
            return null;
        }catch (Exception e){
            errors.add("Une erreur est survenue lors de la recherche des écritures comptable pour le tiers : "+nssAssure);
            LOG.error("APImportationAPGPandemie#retrieveEcrituresAssure : erreur lors de la récupération des écritures comptable pour le tiers : " + nssAssure, e);
            throw new Exception("APImportationAPGPandemie#retrieveEcrituresAssure : erreur lors de la récupération des écritures comptable pour le tiers : " + nssAssure);
        }
    }

    private boolean isCaisseCompetentePourIndependant(final BSession session, final String dateDebut,
                                                      final String dateFin, final String idTiers, final String nss) throws Exception {
        try {
            boolean isCompetente = false;
            List<String> listAffNonPriseEnCompte = new ArrayList<>();
            // on cherche les affiliations pour ce tiers
            final IAFAffiliation affiliation = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
            affiliation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
            final Hashtable<Object, Object> param = new Hashtable<Object, Object>();
            param.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
            final IAFAffiliation[] affiliations = affiliation.findAffiliation(param);

            if (affiliations.length > 0) {
                for (int i = 0; i < affiliations.length; i++) {
                    final IAFAffiliation aff = affiliations[i];

                    // InfoRom531 : On ne reprend que les indépendants et indépendant + employeur.
                    if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                            || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                        // Test si date début du droit doit est plus grande ou égal à date début affiliation
                        final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                                .compareDateFirstGreaterOrEqual(session, dateDebut, aff.getDateDebut());
                        final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                                .compareDateFirstLowerOrEqual(session, dateFin, aff.getDateFin());
                        // si l'affiliation est en cours
                        if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                                || globaz.jade.client.util.JadeStringUtil.isEmpty(aff.getDateFin()))) {
                            listIDAff.add(aff.getAffiliationId());
                            isCompetente = true;
                        } else {
                            listAffNonPriseEnCompte.add("Affiliation non prise en compte (vérifier période): " + aff.getAffilieNumero());
                        }
                    } else {
                        listAffNonPriseEnCompte.add("Affiliation non indépendant / employeur: " + aff.getAffilieNumero());
                    }
                }
                if(!isCompetente){
                    infos.addAll(listAffNonPriseEnCompte);
                }
            } else {
                infos.add("Affiliation inexistante: " + nss + " - (IDTiers :" + idTiers + ")");
            }
            return isCompetente;
        }catch (Exception e){
            errors.add("Une erreur est survenue lors de la recherche de l'affilié pour le tiers suivant : "+nss);
            LOG.error("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la récupération de l'affiliaton " + nss, e);
            throw new Exception("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la récupération de l'affiliation " + nss);
        }
    }

    @Override
    protected void _executeCleanUp() {

    }


    @Override
    protected String getEMailObject() {
        return null;
    }


    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
