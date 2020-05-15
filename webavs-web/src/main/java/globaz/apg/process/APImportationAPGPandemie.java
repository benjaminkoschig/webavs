/*
 * Cr�� le 25 mars 2020
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
import globaz.apg.db.droits.*;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static ch.globaz.pyxis.business.services.AdresseService.*;

/**
 * <H1>Importation des demandes APG Pand�mie</H1>
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

    private static String userGestionnaire = "";

    private BSession bsession;
    private BabelContainer conteneurCatalogues = null;
    private LinkedList<String> errors = new LinkedList();
    private LinkedList<String> infos = new LinkedList();
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

    // Pour les tests - A Supprimer une fois que la recup du NSS sera impl�ment�e
    String nss;
    String state = APIOperation.ETAT_TRAITE;
    private String NOM_CATALOGUE = "Decompte_Pandemie";

    @Override
    public boolean _executeProcess() {
        try {

            initBsession();
            // Pour ne pas envoyer de double mail, un mail d'erreur de validation est d�j� g�n�r�
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
                throw new TechnicalException("Probl�me � l'envoi du mail", e1);
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
        nss = "";
        errors = new LinkedList();
        infos = new LinkedList();
        isProcessErrorMetier = false;
        isProcessErrorTechnique = false;
    }

    private void initBsession() throws Exception {
//        bsession = (BSession) GlobazServer.getCurrentSystem()
//                .getApplication(APApplication.DEFAULT_APPLICATION_APG)
//                .newSession(getUsername(), getPassword());
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    /**
     * Traitement des fichiers APG - Pand�mie
     */
    private void importFiles() throws Exception {
        if (!APProperties.GESTIONNAIRE_USER.getValue().isEmpty()) {
            try {
                JadeUser user = bsession.getApplication()._getSecurityManager().getUserForVisa(bsession, APProperties.GESTIONNAIRE_USER.getValue());
                if (Objects.nonNull(user.getVisa())) {
                    userGestionnaire = user.getVisa();
                }
            } catch (FWSecurityLoginException e) {
                LOG.warn("APImportationAPGPandemie#importFiles - Erreur � la r�cup�reration le nom du gestionnaire :", e);
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
                LOG.info("Nombre de dossiers trait�s : "+nbTraites);
            }else{
                LOG.warn("Properties APG Pandemie undefined");
                throw new Exception("APImportationAPGPandemie#import : Propri�t�s non d�finies !");
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
                            adresseEmailAssure = message.getContent().getInsuredAddress().getEmail();
                            if (isTraitementSuccess) {
                                savingFileInDb(nss, destTmpXMLPath, state);
                                infos.add("Traitement du fichier suivant r�ussi : " + nameOriginalZipFile);
                                infos.add("Assur� concern� : " + message.getContent().getInsuredPerson().getVn());
                            }
                        } else {
                            errors.add("Fichier zip suivant ne contient aucun fichier XML pouvant �tre trait� : " + nameOriginalZipFile);
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
                        if (isProcessErrorMetier) {
                            sendResultMailAssure(filesToSend, adresseEmailAssure);
                        }
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
                LOG.error("Des erreurs ont �t� trouv�s dans la transaction. : ", transaction.getErrors());
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
            LOG.error("Fichier non trouv� : " + pathFile, e);
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
            // V�rification s'il s'agit d'un import Gestionnaire
            isImportGestionnaire = isGestionnaireBySenderId(Objects.nonNull(message.getHeader()) ? message.getHeader().getSenderId() : null);
            senderId = Objects.nonNull(message.getHeader()) ? message.getHeader().getSenderId() : "";

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
            errors.add("Impossible de d�zipper le fichier : " + zipFile.getName() + "("+e.getMessage()+")");
            LOG.error("APImportationAPGPandemie#import : erreur lors de l'importation des fichiers", e);
        } finally {
            try {
                if (!(Objects.isNull(outputStream))) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOG.error("Erreur � la fermeture du fichier", e);
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
     * Permet de d�placer le fichier suite au traitement.
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
                // Cr�ation du r�pertoire de storage
                storageFolderTemp = CommonFilesUtils.createPathFiles(nss, storageFolder);

                // copy du zip dans le storage
                if (!storageFolderTemp.isEmpty()) {
                    JadeFsFacade.copyFile(nomFichierZipDistant, storageFolderTemp + nameOriginalZipFile);
                    storageFileTempStorage = storageFolderTemp + nameOriginalZipFile;
                    LOG.info("ImportAPGPandemie - Copy to "+storageFileTempStorage);
                }
            }
            // On ne supprime le fichier uniquement si celui-ci a pu �tre copi� dans un des deux r�pertoires
            // Et on retourne un des deux emplacements du fichier copi�s
            if(JadeFsFacade.exists(storageFileTempTrace)){
                JadeFsFacade.delete(nomFichierZipDistant);
                fileToSend = storageFileTempTrace;
            } else if(JadeFsFacade.exists(storageFileTempStorage)){
                JadeFsFacade.delete(nomFichierZipDistant);
                fileToSend = storageFileTempStorage;
            }else{
                LOG.warn("APImportationAPGPandemie#movingFile : Fichier non supprim� car celui-ci n'a pas pu �tre copi� dans un des deux emplacements suivants :\n-"
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
            corpsCaisse.append("Fichier trait� : " + nameOriginalZipFile + "\n\n");
        }
        for(String info : infos){
            corpsCaisse.append(info + "\n");
        }
        if(!errors.isEmpty()){
            corpsCaisse.append("Les informations suivantes sont � v�rifier ET sont bloquantes ! (droit non cr�� dans WebAVS) : \n");
        }
        for(String error : errors){
            corpsCaisse.append(error + "\n");
        }
        LOG.info("ImportAPGPandemie - Envoi mail � l'adresse de la caisse "+getListEMailAddressTechnique().toString());
        ProcessMailUtils.sendMail(getListEMailAddressTechnique(), getEMailObjectCaisse(), corpsCaisse.toString(), filesToJoin);
    }

    /**
     * Process d'envoi des mails
     *
     * @param filesToJoin
     * @param emailTiers
     * @throws Exception
     */
    private void sendResultMailAssure(List<String> filesToJoin, String emailTiers) throws Exception {
        StringBuilder corpsAssure = new StringBuilder();

        // TODO � supprimer lorsque le Catalogue de texte sera mis en place
        // Correction pour la FPV

        if(CommonProperties.KEY_NO_CAISSE.getValue().equals("110")) {
            corpsAssure.append("Madame, Monsieur, \n" +
                    "\n" +
                    "Nous avons constat� que vous avez rempli une demande de prestation APG Coronavirus par le biais de notre formulaire en ligne.\n" +
                    "\n" +
                    "Le syst�me n'a pas pu traiter votre demande du fait que vous n'avez pas d'affiliation avec notre Caisse ou en raison d'une saisie erron�e de donn�es. \n" +
                    "\n" +
                    "En cas d'inscription existante aupr�s de notre Caisse, vous voudrez bien r�it�rer votre demande en contr�lant soigneusement votre num�ro AVS (756.xxxx.xxxx.xx) ainsi que votre num�ro d'affili� que vous trouverez sur nos d�comptes de cotisations.\n" +
                    "\n" +
                    "Si vous �tes salari�(e), il est possible que vous n'ayez pas encore �t� annonc�(e) � notre Caisse par votre employeur. Vous voudrez bien contr�ler cette information avec ce dernier. \n" +
                    "\n" +
                    "Nous restons � votre disposition pour tout renseignement compl�mentaire, et nous vous prions d'agr�er, Madame, Monsieur, nos salutations distingu�es.\n");
            corpsAssure.append("******************************************************************************\n\n\n");
            corpsAssure.append("Sehr geehrte Damen und Herren\n" +
                    "\n" +
                    "Wir haben festgestellt, dass Sie einen Coronavirus EO-Anmeldung dank unseres Online-Formular ausgef�llt haben.\n" +
                    "\n" +
                    "Das System konnte Ihre Anfrage nicht bearbeiten, weil Sie kein Mitglied unserer Kasse sind oder wegen falsche vorliegende Daten.\n" +
                    "\n" +
                    "Wenn Sie noch bei unserer Kasse registriert sind, laden wir Sie ein, Ihre Anmeldung zu wiederholen, indem Sie Ihre AHV-Nummer (756.xxxx.xxxx.xx) und Ihre Mitgliedsnummer, die Sie auf Ihren Beitragsnachweisen finden, sorgf�ltig �berpr�fen.\n" +
                    "\n" +
                    "Wenn Sie Arbeitnehmer-innen sind, ist es m�glich, dass Sie von Ihrem Arbeitgeber noch nicht bei unserer Kasse angemeldet wurden. In diesem Fall bitten wir Sie, diese Angaben bei Ihrem Arbeitgeber zu �berpr�fen.\n" +
                    "\n" +
                    "Bei allf�lligen Fragen stehen wir Ihnen selbstverst�ndlich gerne zur Verf�gung.\n" +
                    "\n" +
                    "Freundliche Gr�ssen\n");

        } else {
            corpsAssure.append("Madame, Monsieur, \n" +
                    "\n" +
                    "Votre demande d'allocation pour perte de gain en cas de coronavirus a re�u toute notre attention. Toutefois nous regrettons de ne pouvoir y r�pondre favorablement, vous n'�tes pas inscrit aupr�s de notre caisse et nous ne sommes donc pas comp�tent pour entrer en mati�re.\n" +
                    "\n" +
                    "Nous restons � votre disposition pour tout renseignement compl�mentaire, et nous vous prions d'agr�er, Madame, Monsieur, nos salutations distingu�es\n\n\n");
            corpsAssure.append("******************************************************************************\n\n\n");
            corpsAssure.append("Sehr geehrte Frau, Sehr geehrter Herr \n" +
                    "\n" +
                    "Wir best�tigen Ihnen hiermit den Erhalt Ihrer Anmeldung f�r die Corona Erwerbsersatzentsch�digung.\n" +
                    "Sie sind nicht bei unserer AHV Kasse angeschlossen und wir sind demnach nicht zust�ndig f�r die Bearbeitung von Ihrem Gesuch.\n" +
                    "\n"+
                    "F�r allf�llige weitere Ausk�nfte stehen wir zu Ihrer Verf�gung.\n"+
                    "Freundliche Gr�sse\n");
        }


        LOG.info("ImportAPGPandemie - Envoi mail � l'adresse de l'assur� "+getListEMailAddressAssure(emailTiers).toString());
        ProcessMailUtils.sendMail(getListEMailAddressAssure(emailTiers), getEMailObjectAssure(), corpsAssure.toString(), filesToJoin);
    }

    /**
     * R�cup�re les textes du catalogue de texte
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
            throw new TechnicalException("Erreur � l'import du catalogue de texte", e);
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
            LOG.error("ImportAPGPandemie - Impossible de charger le catalogue de texte, veuillez le cr�er dans le module des allocations pour perte de gains ! (non bloquant)");
        }
    }

    /**
     * Get param�trage du catalogue de texte
     *
     * @return catalogueTexteApg
     */
    private CatalogueText getCatalogueTexte(){
        CatalogueText catalogueTexteApg = new CatalogueText();
        // TODO Gestion de la langue du tiers � impl�menter
        catalogueTexteApg.setCodeIsoLangue("fr");
        catalogueTexteApg.setCsDomaine(IAPCatalogueTexte.CS_APG);
        catalogueTexteApg.setCsTypeDocument(IAPCatalogueTexte.CS_DECOMPTE_APG);
        catalogueTexteApg.setNomCatalogue(NOM_CATALOGUE);

        return catalogueTexteApg;
    }

    protected String getEMailObjectCaisse() {
        if(isProcessErrorMetier){
            return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_METIER)+ " " + nss;
        }else if(isProcessErrorTechnique){
            return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_KO_TECHNIQUE) + " " + nss;
        }
        return bsession.getLabel(AGP_PANDEMIE_IMPORT_MAIL_SUBJECT_OK) + " " + nss;
    }

    protected String getEMailObjectAssure() {
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
            LOG.error("ImportAPGPandemie - Erreur � la r�cup�ration de la propri�t� Adresse E-mail !! ", e);
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
        FamilyMembers membresFamilleAssure = content.getFamilyMembers();
        Activity activiteProfessionnelle = content.getActivity();
        boolean isIndependant = isIndependant(activiteProfessionnelle);
        boolean isCaisseCompetente = false;
        ActivityCessation activiteArret = content.getActivityCessation();
        PaymentContact adressePaiement = content.getPaymentContact();
        initDatesDroit(activiteArret);
        try {
            transaction = (BTransaction) bsession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            // Test si la caisse est comp�tente -> Si non, on ne cr�� rien
            nss = assure.getVn();
            PRTiersWrapper tiers = getTiersByNss(assure.getVn());
            if(isIndependant){
                if(tiers != null){
                    if(isCaisseCompetentePourIndependant(bsession, dateDebutDroit, dateFinDroit, tiers.getIdTiers())){
                        isCaisseCompetente = true;
                    }
                }else {
                    infos.add("Ind�pendant inexistant: " + nss);
                }
            }else{
                if(!isAffiliationRenseigneExist(activiteProfessionnelle)) {
                    ecriture = retrieveEcrituresAssure(bsession, NSUtil.unFormatAVS(nss), transaction);
                    if (ecriture != null) {
                        isCaisseCompetente = true;
                    }else{
                        infos.add("Aucun CI trouv� pour l'assur�: "+nss);
                    }
                }else{
                    isCaisseCompetente = true;
                }
                if(isCaisseCompetente && tiers == null){
                    tiers = creationTiers(assure, adresseAssure);
                }
            }
            if(isCaisseCompetente && tiers != null){

                // Cr�ation du contact avec l'email r�cup�r� du XML
                createContact(tiers, content.getInsuredAddress().getEmail());

                // Cr�ation r�le APG du tiers
                LOG.info("ImportAPGPandemie - Creating APG Role...");
                creationRoleApgTiers(tiers.getIdTiers());

                // Cr�ation de l'adresse APG-Pand�mie du tiers
                String npaFormat = getNpaFormat(adresseAssure.getZipCode());
                if(isDonneesAdresseValide(adresseAssure, npaFormat)) {
                    LOG.info("ImportAPGPandemie - Creating Adresses...");
                    createAdresseApgPandemie(tiers, adresseAssure, adressePaiement, npaFormat);
                }else{
                    LOG.warn("ImportAPGPandemie - Adresse non cr��e (donn�es non remplies)");
                    infos.add("Adresse non cr��e : Les donn�es r�cup�r�es ne sont pas compl�tes / correctes");
                }

                // Cr�ation de la demande du droit
                LOG.info("ImportAPGPandemie - Creating Demande APG Pandemie...");
                PRDemande demande = creationDemande(tiers.getIdTiers());

                // Cr�ation du droit
                LOG.info("ImportAPGPandemie - Creating Droit APG Pandemie...");
                APDroitPandemie newDroit = creationDroit(demande, content, transaction, npaFormat);
                // Cr�ation des p�riodes du droit
                LOG.info("ImportAPGPandemie - Creating Periodes APG Pandemie...");
                createPeriodes(transaction, newDroit.getIdDroit());

                // Cr�ation droit pand�mie
                LOG.info("ImportAPGPandemie - Creating Data Pandemie...");
                APDroitPanSituation newDroitPademie = creationDroitPandemie(newDroit, membresFamilleAssure, isIndependant, adressePaiement, activiteProfessionnelle, activiteArret, transaction);

                // Cr�ation de la situation professionnelle pour ind�pendant
                LOG.info("ImportAPGPandemie - Creating Situation Professionnelle Pandemie...");
                if(isIndependant(activiteProfessionnelle)){
                    creerSituationProfIndependant(transaction, newDroit, tiers.getIdTiers());
                }else{
                    creerSituationProf(transaction, newDroit, activiteProfessionnelle);
                }
                if (!hasError(bsession, transaction)) {
                    transaction.commit();
                    LOG.info("ImportAPGPandemie - Traitement in success...");
                }else{
                    transaction.rollback();
                    isProcessErrorTechnique = true;
                    errors.add("Un probl�me est survenu lors de la cr�ation du droit pour cet assur� : "+nss);
                    LOG.error("APImportationAPGPandemie#processCreationDroitGlobal : erreur lors de la cr�ation du droit\nSession errors : "
                            +bsession.getErrors()+"\nTransactions errors : "+transaction.getErrors());
                    isTraitementSuccess = false;
                }
            }else{
                transaction.rollback();
                adresseEmailAssure = adresseAssure.getEmail();
                LOG.warn("ImportAPGPandemie - Caisse non comp�tente pour l'assur� : "+nss);
                isTraitementSuccess = false;
                isProcessErrorMetier = true;
            }
        } catch (Exception e) {
            isTraitementSuccess = false;
            if (transaction != null) {
                transaction.rollback();
            }
            errors.add("Erreur dans la cr�ation du droit "+e.getMessage());
            LOG.error("APImportationAPGPandemie#processCreationDroitGlobal : erreur lors de la cr�ation du droit ", e);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return isTraitementSuccess;
    }

    private String getNpaFormat(String npa) {
        if(!Objects.isNull(npa)){
            String npaTrim = StringUtils.trim(npa);
            if(StringUtils.isNumeric(npaTrim)){
                return npaTrim;
            }else{
                infos.add("NPA incorrect ! Celui-ci doit �tre dans un format num�rique uniquement ! Valeur: "+npa);
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

    private boolean isAffiliationRenseigneExist(Activity activiteProfessionnelle) throws Exception {
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
            errors.add("Une erreur est survenue lors de la recherche de l'affili� pour le tiers suivant : "+nss);
            LOG.error("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la r�cup�ration de l'affiliaton " + nss, e);
            throw new Exception("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la r�cup�ration de l'affiliation " + nss);
        }
    }

    private void createContact(PRTiersWrapper tiers, String email) throws Exception {
        try {
            if(!JadeStringUtil.isBlankOrZero(email)) {
                // Cr�ation du contact
                TIContact contact = new TIContact();
                contact.setSession(bsession);
                contact.setNom(tiers.getNom());
                contact.setPrenom(tiers.getPrenom());
                contact.add(bsession.getCurrentThreadTransaction());

                // Cr�ation du moyen de communication
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
            errors.add("Une erreur est survenue lors de la cr�ation du contact pour l'id tiers : "+nss+" - "+email);
            LOG.error("APImportationAPGPandemie#createContact : Une erreur est survenue lors de la cr�ation du contact pour l'id tiers " + nss, e);
            throw new Exception("APImportationAPGPandemie#createContact : Une erreur est survenue lors de la cr�ation du contact pour l'id tiers " + nss);
        }


    }

    private PRTiersWrapper creationTiers(InsuredPerson assure, InsuredAddress adresseAssure) throws Exception {
        String sexePy = getSexePy(sexeCI);
        // les noms et prenoms doivent �tre renseign�s pour ins�rer un nouveau tiers
        if (JadeStringUtil.isEmpty(assure.getOfficialName()) || JadeStringUtil.isEmpty(assure.getFirstName())) {
            bsession.addError(bsession.getLabel("APAbstractDroitPHelper.ERREUR_NOM_OU_PRENOM_INVALIDE"));
            return null;
        }

        // date naissance obligatoire pour inserer
        if (JAUtil.isDateEmpty(tranformGregDateToGlobDate(assure.getDateOfBirth()))) {
            bsession.addError(bsession.getLabel("DATE_NAISSANCE_INCORRECTE"));
            return null;
        }

        // recherche du canton si le npa est renseign�
        String canton = "";
        if (!JadeStringUtil.isIntegerEmpty(adresseAssure.getZipCode())) {
            try {
                canton = PRTiersHelper.getCanton(bsession, adresseAssure.getZipCode());

                if (canton == null) {
                    // canton non trouv�
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
            if (activityCessation.getChildCareCessation().isIsSelected()) {
                for (ChildCarePeriod periode : activityCessation.getChildCareCessation().getChildCarePeriods().getChildCarePeriod()) {
                    dateDebutXml = periode.getFrom();
                    dateFinXml = periode.getTo();
                    nbJours = periode.getNumbersOfDays();
                    periodes.add(buildPeriode(dateDebutXml, dateFinXml, nbJours));
                }
            } else {
                if (activityCessation.getQuarantineCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getQuarantineCessation().getQuarantinePeriod().getFrom();
                    dateFinXml = activityCessation.getQuarantineCessation().getQuarantinePeriod().getTo();
                } else if (activityCessation.getIndependantWorkClosureCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getFrom();
                    dateFinXml = activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getTo();
                } else if (activityCessation.getIndependantEventCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getFrom();
                    dateFinXml = activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getTo();
                } else if (activityCessation.getIndependantLossOfIncomeCessation().isIsSelected()) {
                    dateDebutXml = activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getFrom();
                    dateFinXml = activityCessation.getIndependantLossOfIncomeCessation().getIndependantLossOfIncomePeriod().getTo();
                }
                periodes.add(buildPeriode(dateDebutXml, dateFinXml, nbJours));
            }
        }catch (Exception e){
            errors.add("Erreur lors de la r�cup�ration des p�riodes: V�rifier les donn�es du XML ("+e.getMessage()+")");
            LOG.error("Erreur lors de la r�cup�ration des p�riodes: "+e.getMessage());
        }
    }

    private APPeriodeAPG buildPeriode(XMLGregorianCalendar from, XMLGregorianCalendar to, int numbersOfDays) throws Exception {
        APPeriodeAPG periodePan = new APPeriodeAPG();
        if(!Objects.isNull(from)){
            periodePan.setDateDebutPeriode(tranformGregDateToGlobDate(from));
        }else{
            throw new Exception("Date de d�but du droit obligatoire (v�rifier les donn�es de la demande)");
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
            errors.add("Erreur lors de l'enregistrement des p�riodes pour le droit "+idDroit);
            LOG.error("Erreur lors de la r�cup�ration des p�riodes pour le droit "+idDroit+" - Exception: "+e.getMessage());
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("P�riode");
                // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans la cr�ation des p�riodes
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
                                                      Activity activity, ActivityCessation activityCessation, BTransaction transaction) throws Exception {
        APDroitPanSituation droitPanSituation = new APDroitPanSituation();
        try {
            droitPanSituation.setSession(bsession);
            // Activity
            droitPanSituation.setActiviteSalarie(!isIndependant);
            droitPanSituation.setCategorieEntreprise(Objects.isNull(activity.getActivityCategory().getActivityType())?
                    "": transformCatEntrepriseToCode(activity.getActivityCategory().getActivityType()));
            droitPanSituation.setCategorieEntrepriseLibelle(activity.getActivityCategory().getActivityOtherDetail());
            droitPanSituation.setCopieDecompteEmployeur(activity.getSalary().isSendDecompteToEmployer());
            // ActivityCessation
            if(activityCessation.getChildCareCessation().isIsSelected()) {
                // Cr�ation de la situation Familiale
                creationSituationFamiliale(membresFamilleAssure, droitPandemie.getId(), transaction);
                if (activityCessation.getChildCareCessation().isChildCareWithHandicap()) {
                    droitPanSituation.setMotifGardeHandicap(Objects.isNull(activityCessation.getChildCareCessation().getChildCareCause()) ?
                            "" : transformMotifGardHandicapToCode(activityCessation.getChildCareCessation().getChildCareCause()));
                } else {
                    droitPanSituation.setMotifGarde(Objects.isNull(activityCessation.getChildCareCessation().getChildCareCause()) ?
                            "" : transformMotifGardToCode(activityCessation.getChildCareCessation().getChildCareCause()));
                }
                if(Objects.nonNull(activityCessation.getChildCareCessation().getChildCarePersonRiskProof())){
                    droitPanSituation.setGroupeRisque(transformGroupeRisqueToCode(activityCessation.getChildCareCessation().getChildCarePersonRiskProof().getChildCarePersonType()));
                    if(isMaladie(droitPanSituation.getGroupeRisque())) {
                        droitPanSituation.setCauseMaladie(transformCauseMaladieToCode(activityCessation.getChildCareCessation().getChildCarePersonRiskProof().getChildCarePersonDiseaseCause()));
                    }
                }
            }
            if(activityCessation.getQuarantineCessation().isIsSelected()) {
                droitPanSituation.setQuarantaineOrdonnee(activityCessation.getQuarantineCessation().isPrescribedQuarantine());
                droitPanSituation.setQuarantaineOrdonneePar(activityCessation.getQuarantineCessation().getPrescribedBy());
            }
            if(activityCessation.getIndependantWorkClosureCessation().isIsSelected()) {
                droitPanSituation.setDateFermetureEtablissementDebut(Objects.isNull(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getFrom()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getFrom()));
                droitPanSituation.setDateFermetureEtablissementFin(Objects.isNull(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getTo()) ?
                        "" : tranformGregDateToGlobDate(activityCessation.getIndependantWorkClosureCessation().getIndependantWorkClosurePeriod().getTo()));
            }
            if(activityCessation.getIndependantEventCessation().isIsSelected()) {
                droitPanSituation.setDateDebutManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getEventDate()));
                droitPanSituation.setDateDebutManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getFrom()));
                droitPanSituation.setDateFinManifestationAnnulee(tranformGregDateToGlobDate(activityCessation.getIndependantEventCessation().getIndependantEventPeriod().getTo()));
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
            errors.add("Erreur dans l'enregistrement des donn�es sp�cifiques du formulaire ("+e.getMessage()+")");
            LOG.error("APImportationAPGPandemie#creationDroitPandemie : erreur lors de la cr�ation de la demande du droit Pand�mie", e);
            throw new Exception("APImportationAPGPandemie#creationDroitPandemie : erreur lors de la cr�ation de la demande du droit Pand�mie", e);
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
            errors.add("Impossible de cr�er la situation professionnelle ");
            LOG.error("APImportationAPGPandemie#creationSituationProfessionnelle : erreur lors de la cr�ation de la situation professionnelle ", e);
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
            errors.add("Erreur dans la cr�ation de la demande du droit (idTiers : "+idTiers+")");
            LOG.error("APImportationAPGPandemie#creationDemande : erreur lors de la cr�ation de la demande du droit ", e.getStackTrace());
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Demande");
            }
            throw new Exception("APImportationAPGPandemie#creationDemande : erreur lors de la cr�ation de la demande du droit ", e);
        }
        return retValue;
    }

    private PRTiersWrapper getTiersByNss(String nss) throws Exception {
        try {
            return PRTiersHelper.getTiers(bsession, nss);
        } catch (Exception e) {
            errors.add("Impossible de r�cup�rer le tiers : "+nss);
            LOG.error("APImportationAPGPandemie#getTiersByNss : erreur lors de la r�cup�ration du tiers " + nss, e);
            throw new Exception("APImportationAPGPandemie#getTiersByNss : erreur lors de la r�cup�ration du tiers ");
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
            errors.add("Impossible de cr�er le droit APG Pand�mie : "+ e.getMessage());
            LOG.error("APImportationAPGPandemie#creationDroit : erreur lors de la cr�ation du droit APG Pand�mie ", e);
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Droit");
            }
            throw new Exception("APImportationAPGPandemie#creationDroit : erreur lors de la cr�ation du droit APG Pand�mie ", e);
        }
        return droitPandemie;
    }

    private String getCSGenreService(Content content) {
        String cause = content.getActivityCessation().getActivityCause();

        // Le type garde peut �tre pour enfant avec Handicap. S'il s'agit d'une garde et qu'il a un handicap, alors on retourne le type garde handicap
        if (Objects.equals(cause, "GARDE") && Objects.nonNull(content.getActivityCessation().getChildCareCessation())
                && Objects.nonNull(content.getActivityCessation().getChildCareCessation().isChildCareWithHandicap())
                && content.getActivityCessation().getChildCareCessation().isChildCareWithHandicap()) {
            return APGenreServiceAPG.GardeParentaleHandicap.getCodeSysteme();
        }

        switch (cause){
            case "GARDE": return APGenreServiceAPG.GardeParentale.getCodeSysteme();
            case "QUARANTAINE": return APGenreServiceAPG.Quarantaine.getCodeSysteme();
            case "FERMETURE": return APGenreServiceAPG.IndependantPandemie.getCodeSysteme();
            case "MANIFESTATION": return APGenreServiceAPG.IndependantManifAnnulee.getCodeSysteme();
            case "PERTEGAIN": return APGenreServiceAPG.IndependantPerteGains.getCodeSysteme();
            default: return "";
        }
    }

    private String creationIdCaisse() {
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            return noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            errors.add("Impossible de r�cup�rer les propri�t�s n� caisse et n� agence");
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
                // on ajoute le r�le APG au Tier si il ne l'a pas deja
                ITIRole newRole = (ITIRole) bsession.getAPIFor(ITIRole.class);
                newRole.setIdTiers(idTiers);
                newRole.setISession(PRSession.connectSession(bsession, TIApplication.DEFAULT_APPLICATION_PYXIS));
                newRole.setRole(IntRole.ROLE_APG);
                newRole.add(bsession.getCurrentThreadTransaction());
            }
            trans.commit();
        } catch (Exception e) {
            errors.add("Une erreur s'est produite dans la cr�ation du r�le du tiers : "+idTiers);
            LOG.error("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la cr�ation du r�le du tiers : "+idTiers);
            if (trans != null) {
                trans.rollback();
            }
            if(isJadeThreadError()){
                addJadeThreadErrorToListInfos("R�le");
                // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans l'ajout du r�le
                JadeThread.logClear();
            }
            throw new Exception("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la cr�ation du r�le du tiers : "+idTiers);
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
                infos.add("Aucune adresse cr��e car le domaine pand�mie n'a pas �t� d�fini dans la propri�t� suivante: "+APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getPropertyName());
            }
        }catch (Exception e){
            infos.add("Un probl�me a �t� rencontr� lors de la cr�ation des adresses pour l'assur� suivant"+nss);
            LOG.error("APImportationAPGPandemie#createAdresseApgPandemie : Erreur rencontr� lors de la cr�ation adresses pour l'assur�", e);
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

        // Bug Fix - Cette correction permet de corriger un bug lorsque la LigneAdresse est laiss� � null
        // Le risque de r�utiliser une adresse identique avec un compl�ment d'adresse qui n'a rien � voir
        // une correction sera effectu�e sur Pyxis par la suite.
        // Mise � chaine vide des champs ligneAdresse
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
            // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans l'adresse
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
        infos.add("Concerne: "+methodeSource +" (�l�ments non bloquants)");
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
                infos.add("Paiement adresse non cr��e : IBAN non valide : " + iban);
            }
        }
        if(isJadeThreadError()){
            addJadeThreadErrorToListInfos("Adresse Paiement");
            // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans l'adresse
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

    private void creerSituationProf(BTransaction transaction, APDroitPandemie droitPandemie, Activity activity) {
        try {
            boolean isVersementEmployeur = activity.getSalary().isHasReceiveSalaryDuringInterruption();
            String salaireMensuel = String.valueOf(activity.getSalary().getLastIncome());
            for(String idAffiliation : listIDAff){
                AFAffiliation affiliation = findAffiliationById(idAffiliation);
                if(affiliation != null) {
                    // creation de la situation prof avec le dernier employeur d'apr�s les CI
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
                    salaireMensuel = "";
                    situationProfessionnelle.wantCallValidate(false);
                    situationProfessionnelle.add(transaction);
                }
            }
        }catch (Exception e){
            errors.add("Erreur rencontr� lors de la cr�ation de la situation professionnelle pour l'assur� ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontr� lors de la cr�ation de la situation professionnelle pour l'assur� ", e);
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Employ�");
                // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans l'ajout de la situation prof
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
     * Si le tiers poss�de une affiliation personnelle en cours durant la p�riode du droit, on creer une situation prof.
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

                    // InfoRom531 : On ne reprend que les ind�pendants et ind�pendant + employeur.
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
                            decision.setForAnneeDecision(Integer.toString(new JADate(droit.getDateDebutDroit()).getYear()-1));
                            decision.find(BManager.SIZE_NOLIMIT);

                            // on cherche les donn�es calcul�es en fonction de la
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
            errors.add("Erreur rencontr� lors de la cr�ation de la situation professionnelle pour un ind�pendant (IDTiers : "+idTiers+")");
            LOG.error("APImportationAPGPandemie#creerSituationProfSiIndependant : Erreur rencontr� lors de la cr�ation de la situation professionnelle pour un ind�pendant ", e);

            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Situation Professionnelle Ind�pendant");
                // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans l'ajout de la situation prof
                JadeThread.logClear();
                return null;
            }
        }
        return situationProfessionnelle;
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
            // pour tous les ci trouv�s
            for (int i = 0; i < ciManager.size(); i++) {
                CICompteIndividuel compte = (CICompteIndividuel) ciManager.getEntity(i);
                // Recherche d'une �criture comptable pour l'ann�e 2018 et 2019 pour les CI
                CIEcritureManager ecritureSource = new CIEcritureManager();
                ecritureSource.setForCompteIndividuelId(compte.getId());
                ecritureSource.setFromAnnee("2018");
                ecritureSource.setUntilAnnee("2019");
                ecritureSource.setForIdTypeCompte(CIEcriture.CS_CI);
                ecritureSource.setSession(session);
                // Mois d�but
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
            errors.add("Une erreur est survenue lors de la recherche des �critures comptable pour le tiers : "+nss);
            LOG.error("APImportationAPGPandemie#retrieveEcrituresAssure : erreur lors de la r�cup�ration des �critures comptable pour le tiers : " + nss, e);
            throw new Exception("APImportationAPGPandemie#retrieveEcrituresAssure : erreur lors de la r�cup�ration des �critures comptable pour le tiers : " + nss);
        }
    }

    private boolean isCaisseCompetentePourIndependant(final BSession session, final String dateDebut,
                                                      final String dateFin, final String idTiers) throws Exception {
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

                    // InfoRom531 : On ne reprend que les ind�pendants et ind�pendant + employeur.
                    if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                            || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                        // Test si date d�but du droit doit est plus grande ou �gal � date d�but affiliation
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
                            listAffNonPriseEnCompte.add("Affiliation non prise en compte (v�rifier p�riode): " + aff.getAffilieNumero());
                        }
                    } else {
                        listAffNonPriseEnCompte.add("Affiliation non ind�pendant / employeur: " + aff.getAffilieNumero());
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
            errors.add("Une erreur est survenue lors de la recherche de l'affili� pour le tiers suivant : "+nss);
            LOG.error("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la r�cup�ration de l'affiliaton " + nss, e);
            throw new Exception("APImportationAPGPandemie#isCaisseCompetentePourIndependant : erreur lors de la r�cup�ration de l'affiliation " + nss);
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
