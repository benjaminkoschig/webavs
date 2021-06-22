package globaz.apg.process;

import apg.amatapat.AddressType;
import apg.amatapat.Content;
import apg.amatapat.InsuredPerson;
import apg.amatapat.Message;
import ch.globaz.common.mail.CommonFilesUtils;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APImportationAPGHistorique;
import globaz.apg.eformulaire.*;
import globaz.apg.properties.APProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.api.APIOperation;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.tiers.TITiersViewBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class APImportationAPGAmatApatProcess extends BProcess {

    protected static final String BACKUP_FOLDER = "/backup/";
    protected static final String ERRORS_FOLDER = "/errors/";
    protected static final String XML_EXTENSION = ".xml";
    protected static final int MAX_TREATMENT = 40;
    private static final String AMAT_TYPE = "AMAT";

    protected BSession bsession;
    protected LinkedList<String> errorsCreateBeneficiaries = new LinkedList<>();
    protected String backupFolder;
    protected String errorsFolder;
    protected String storageFolder;
    protected String demandeFolder;
    protected APImportationStatusReport report = new APImportationStatusReport();

    @Override
    protected void _executeCleanUp() {
        // cleanup done previously in the process
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Start Import - Import des demandes APG AMAT ou APAT.");
            initBsession();
            // Pour ne pas envoyer de double mail, un mail d'erreur de validation est déjà généré
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);
            importFiles();
        } catch (Exception e) {
            setReturnCode(-1);
            APImportationStatusFile statusFile = report.addFile(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
            statusFile.getErrors().add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            throw new TechnicalException("Erreur dans le process d'import", e);
        } finally {
            closeBsession();
        }
        try {
            report.generationProtocols();
        } catch (Exception e1) {
            throw new TechnicalException("Problème à l'envoi du mail", e1);
        }
        LOG.info("Fin du process d'importation.");
        return true;
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

    /**
     * Traitement des fichiers AMAT
     */
    private void importFiles() {
        try {
            demandeFolder = APProperties.DEMANDES_AMAT_APAT_FOLDER.getValue();
            storageFolder = APProperties.STORAGE_AMAT_APAT_FOLDER.getValue();

            if (!JadeStringUtil.isBlankOrZero(demandeFolder) && !JadeStringUtil.isBlankOrZero(storageFolder)) {
                List<String> repositoryDemandesAmatApat = JadeFsFacade.getFolderChildren(demandeFolder);
                backupFolder = new File(storageFolder).getAbsolutePath() + BACKUP_FOLDER;
                errorsFolder = new File(storageFolder).getAbsolutePath() + ERRORS_FOLDER;
                for (String nomFichierDistant : repositoryDemandesAmatApat) {
                    if (MAX_TREATMENT > report.getNbFichierTraites()) {
                        importFile(nomFichierDistant);
                    }
                }
                LOG.info("Nombre de dossiers traités : {}",  report.getNbFichierTraites());
            } else {
                LOG.warn("Les propriétés AMAT APAT ne sont pas définis.");
                APImportationStatusFile statusFile = report.addFile(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY);
                statusFile.getErrors().add("Import impossible : les propriétés AMAT-APAT ne sont pas définis.");
            }
        } catch (Exception e) {
            APImportationStatusFile statusFile = report.addFile(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY);
            statusFile.getErrors().add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            LOG.error("Erreur lors de l'importation des fichiers", e);
        }
    }

    /**
     * Copie le fichier XML en local, effectue le traitement d'importion, sauvegarde le fichier
     * dans le dossier backup, supprime le fichier de base
     *
     * @param nomFichier: nopm du fichier à importer
     */
    private void importFile(String nomFichier)  {
        if (nomFichier.endsWith(XML_EXTENSION)) {
            try {
                LOG.info("Démarrage de l'import du fichier : {}", nomFichier);
                String nameOriginalFile = FilenameUtils.getName(nomFichier);
                String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
                boolean isTraitementSuccess = false;
                String nss = "";
                // Parsing du fichier XML
                Message message = getMessageFromFile(nomFichier);
                APImportationStatusFile fileStatus;
                if (message != null) {
                    Content content = message.getContent();
                    nss = content.getInsuredPerson().getVn();

                    boolean isWomen = StringUtils.equals(AMAT_TYPE, content.getAmatApatType());
                    fileStatus = report.addFile(nameOriginalFile, nss, isWomen ? IPRDemande.CS_TYPE_MATERNITE : IPRDemande.CS_TYPE_PATERNITE);

                    isTraitementSuccess = traiterMessage(content, nss, fileStatus, isWomen);
                    if (isTraitementSuccess) {
                        fileStatus.setSucceed(true);
                        savingFileInDb(nss, nomFichier, content.getAmatApatType(), fileStatus);
                    } else {
                        fileStatus.setSucceed(false);
                    }
                } else {
                    fileStatus = report.addFile(nameOriginalFile, StringUtils.EMPTY, StringUtils.EMPTY);
                    fileStatus.getErrors().add("Le fichier XML ne peut pas être traité : " + nameOriginalFileWithoutExt);
                }

                // on déplace les fichiers traités.
                LOG.info("Déplacer le fichier : {}", nameOriginalFileWithoutExt);
                String fullPath = movingFile(nomFichier, nameOriginalFile, nss, isTraitementSuccess);
                fileStatus.setFileFullPath(fullPath);
            } catch (Exception e) {
                APImportationStatusFile fileStatus = report.addFile(nomFichier, StringUtils.EMPTY, StringUtils.EMPTY);
                fileStatus.getErrors().add("Erreur lors du traitement du fichier : " + nomFichier);
                LOG.error("Erreur lors du traitement du fichier. ", e);
            }
        }
    }

    private boolean traiterMessage(Content content, String nssTiers, APImportationStatusFile fileStatus, boolean isWomen) throws Exception {
        BTransaction transaction = (BTransaction) bsession.newTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        InsuredPerson assure = content.getInsuredPerson();
        AddressType adresseAssure = content.getInsuredAddress();
        String npaFormat = formatNPA(getZipCode(adresseAssure), fileStatus);
        IAPImportationAmatApat handler = getHandler(content, fileStatus, nssTiers);

        PRTiersWrapper tiers = getTiersByNss(nssTiers, fileStatus);
        if (Objects.isNull(tiers)) {
            tiers = handler.createTiers(assure, npaFormat, isWomen);
        }
        fileStatus.setNom(tiers.getNom());
        fileStatus.setPrenom(tiers.getPrenom());
        fileStatus.setNumeroAffilie(getNumeroAffilie(tiers));
        if (tiers.getSexe().equals(TITiersViewBean.CS_FEMME) && !isWomen) {
            fileStatus.getErrors().add("Demande de droit paternité pour une personne de sexe féminin.");
            LOG.error("ImportAPGAmatApat#CreateDroitGlobal - Une erreur s'est produite dans la création du droit : demande de droit paternité pour une personne de sexe féminin");
        } else if (tiers.getSexe().equals(TITiersViewBean.CS_HOMME) && isWomen) {
            fileStatus.getErrors().add("Demande de droit maternité pour une personne de sexe masculin.");
            LOG.error("ImportAPGAmatApat#CreateDroitGlobal - Une erreur s'est produite dans la création du droit : demande de droit maternité pour une personne de sexe masculin");
        } else {
            String idTiers = tiers.getIdTiers();
            String domaine = isWomen ? IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE : IPRConstantesExternes.TIERS_CS_DOMAINE_PATERNITE;
            if (StringUtils.isEmpty(PRTiersHelper.getAdresseGeneriqueFormatee(bsession, idTiers, "", "", domaine))) {
                handler.createAdresses(tiers, content.getInsuredAddress(), content.getPaymentContact(), npaFormat);
            } else {
                fileStatus.getInformations().add("Une adresse a été trouvée pour le tiers dans WebAVS et l'adresse du fichier ne sera pas utilisée.");
            }
            handler.createRoleApgTiers(tiers.getIdTiers());
            handler.createContact(tiers, assure.getEmail());
            PRDemande demande = handler.createDemande(tiers.getIdTiers());
            APDroitLAPG droit = handler.createDroit(content, npaFormat, demande, transaction);
            handler.createSituationFamiliale(content.getFamilyMembers(), droit.getIdDroit(), transaction);
            handler.createSituationProfessionnel(content, droit, transaction);
        }

        if (hasError(bsession, transaction, fileStatus)) {
            transaction.rollback();
            fileStatus.getErrors().add("Un problème est survenu lors de la création du droit pour cet assuré .");
            LOG.error("Erreur lors de la création du droit\nSession errors : {} \nTransactions errors {}: ", bsession.getErrors(), transaction.getErrors());
            transaction.closeTransaction();
            return false;
        } else {
            transaction.commit();
            LOG.info("Traitement en succès...");
            transaction.closeTransaction();
            return true;
        }
    }

    private String getNumeroAffilie(PRTiersWrapper tiers) throws Exception {
        AFAffiliationManager manager = new AFAffiliationManager();
        String numeroAffilie = "N° d'affiliation non reconnu ou non défini";
        manager.setSession(bsession);
        manager.setForIdTiers(tiers.getIdTiers());
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            return ((AFAffiliation) manager.getContainer().get(0)).getAffilieNumero();
        }
        return numeroAffilie;
    }

    private IAPImportationAmatApat getHandler(Content content, APImportationStatusFile fileStatus, String nssTiers){
        if (StringUtils.equals(AMAT_TYPE, content.getAmatApatType())) {
            return new APImportationAmat(fileStatus, bsession, nssTiers);
        }
        return new APImportationApat(fileStatus, bsession, nssTiers);
    }

    private void savingFileInDb(String nss, String pathFile, String apgType, APImportationStatusFile fileStatus) throws Exception {
        BTransaction transaction = null;

        try (FileInputStream fileToStore = new FileInputStream(pathFile)) {
            transaction = (BTransaction) bsession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            if (transaction.hasErrors()) {
                LOG.error("Des erreurs ont été trouvés dans la transaction. : {}", transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            APImportationAPGHistorique importData = new APImportationAPGHistorique();
            importData.setSession(bsession);
            importData.setEtatDemande(APIOperation.ETAT_TRAITE);
            importData.setTypeDemande(apgType);
            importData.setNss(nss);
            importData.setXmlFile(fileToStore);
            importData.add();
            if (hasError(bsession, transaction, fileStatus)) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (FileNotFoundException e) {
            LOG.error("Fichier non trouvé : " + pathFile, e);
        } catch (Exception e) {
            fileStatus.getErrors().add("Erreur lors de l'update Historique importation APG " + e.getMessage());
            LOG.error("Erreur lors de l'update Historique importation APG : ", e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }

            } catch (IOException e) {
                LOG.error("Impossible de cloture le fichier", e);
            }
        }
    }
    /**
     * Permet de déplacer le fichier suite au traitement.
     *
     * @param nomFichierDistant: Nom du fichier distant à déplacer
     * @param nameOriginalFile: nom du fichier original
     * @param nss: nss du cas traité dans le fichier
     * @param processSuccess: définit si le processus d'importation à réussi avec succes
     */
    private String movingFile(String nomFichierDistant, String nameOriginalFile, String nss, boolean processSuccess) {
        String storageFolderTemp;
        String storageFileTempTrace = "";
        String storageFileTempStorage = "";

         try{
            if (!JadeFsFacade.exists(backupFolder)) {
                JadeFsFacade.createFolder(backupFolder);
            }
            if (!JadeFsFacade.exists(errorsFolder)) {
                JadeFsFacade.createFolder(errorsFolder);
            }
            String fileFullPath;
            if (processSuccess) {
                // Création du répertoire de storage
                storageFolderTemp = CommonFilesUtils.createPathFiles(nss, backupFolder);

                // Copie du fichier dans le storage
                if (!storageFolderTemp.isEmpty()) {
                    JadeFsFacade.copyFile(nomFichierDistant, storageFolderTemp + nameOriginalFile);
                    storageFileTempStorage = storageFolderTemp + nameOriginalFile;
                    LOG.info("Copie dans {}", storageFileTempStorage);
                }
                fileFullPath = storageFileTempStorage;
            } else {
                // copie du fichier en erreur
                JadeFsFacade.copyFile(nomFichierDistant, errorsFolder + nameOriginalFile);
                storageFileTempTrace = errorsFolder + nameOriginalFile;
                fileFullPath = storageFileTempTrace;
            }
            LOG.info("Copie dans {}", storageFileTempTrace);

            // On ne supprime le fichier que si celui-ci a pu être copié dans un des deux répertoires
            // Et on retourne un des deux emplacements du fichier copiés
            if(JadeFsFacade.exists(storageFileTempTrace)){
                JadeFsFacade.delete(nomFichierDistant);
            } else if(JadeFsFacade.exists(storageFileTempStorage)){
                JadeFsFacade.delete(nomFichierDistant);
            }else{
                LOG.warn("Fichier non supprimé car celui-ci n'a pas pu être copié dans un des deux emplacements suivants :\n-{}\n-{}",
                        storageFileTempTrace, storageFileTempTrace);
            }
            return fileFullPath;

        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
            LOG.error("Erreur lors du déplacement du fichier {}", nameOriginalFile, e);
             return StringUtils.EMPTY;
        }
    }

    private Message getMessageFromFile(String destPath) {
        try {
            LOG.info("Lecture du fichier.");
            String tmpLocalWorkFile = JadeFsFacade.readFile(destPath);
            File fichier = new File(tmpLocalWorkFile);
            if (fichier.isFile()) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (Message) unmarshaller.unmarshal(fichier);
            }
        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException | JAXBException e) {
            APImportationStatusFile statusFile = report.addFile(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY);
            statusFile.getErrors().add("Impossible de parser le fichier XML : " + destPath + "(" + e.getMessage() + ")");
            LOG.error("Erreur lors de l'importation du fichier " + destPath, e);
        }
        return null;
    }

    /**
     *
     * @param session: la session en cours
     * @param transaction: la transaction en cours
     * @param fileStatus: Le status du traitement du fichier
     * @return True si le traitement à une erreur.
     */
    private boolean hasError(BSession session, BTransaction transaction, APImportationStatusFile fileStatus) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly() || !fileStatus.getErrors().isEmpty();
    }

    /**
     * Méthode permettant de récupérer le tiers à partir du NSS.
     *
     * @param nss : le nss
     * @param fileStatus : Le status du traitement du fichier
     * @return le tiers
     */
    private PRTiersWrapper getTiersByNss(String nss, APImportationStatusFile fileStatus) {
        try {
            return PRTiersHelper.getTiers(bsession, nss);
        } catch (Exception e) {
            fileStatus.getInformations().add("Impossible de récupérer le tiers.");
            LOG.error("Erreur lors de la récupération du tiers {}", nss, e);
            return null;
        }
    }

    /**
     * @param adresseAssure : adresse de l'assuré
     * @return le zip code correctement formatté
     */
    private String getZipCode(AddressType adresseAssure) {
        String zipCodeTown = adresseAssure.getZipCodeTown();

        String[] zipCodeTownSplitted = StringUtils.split(zipCodeTown, ",");

        return zipCodeTownSplitted[0].trim();
    }

    /**
     * Formattage du NPA
     *
     * @param npa le npa à formatter
     * @return le npa formatté.
     */
    private String formatNPA(String npa, APImportationStatusFile fileStatus) {
        if(StringUtils.isNotEmpty(npa)){
            String npaTrim = StringUtils.trim(npa);
            if(StringUtils.isNumeric(npaTrim)){
                return npaTrim;
            }else{
                fileStatus.getInformations().add("NPA incorrect ! Celui-ci doit être dans un format numérique uniquement ! Valeur: "+npa);
            }
        }
        return "";
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
