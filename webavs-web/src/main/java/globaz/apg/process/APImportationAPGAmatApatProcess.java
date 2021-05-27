package globaz.apg.process;

import apg.amatapat.AddressType;
import apg.amatapat.Content;
import apg.amatapat.InsuredPerson;
import apg.amatapat.Message;
import ch.globaz.common.mail.CommonFilesUtils;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APImportationAPGHistorique;
import globaz.apg.eformulaire.APImportationAmat;
import globaz.apg.eformulaire.APImportationApat;
import globaz.apg.eformulaire.IAPImportationAmatApat;
import globaz.apg.properties.APProperties;
import globaz.globall.db.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.osiris.api.APIOperation;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
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
import java.util.*;

@Slf4j
public class APImportationAPGAmatApatProcess extends BProcess {

    protected static final String BACKUP_FOLDER = "/backup/";
    protected static final String ERRORS_FOLDER = "/errors/";
    protected static final String XML_EXTENSION = ".xml";
    protected static final int MAX_TREATMENT = 40;
    private static final String AMAT_TYPE = "AMAT";
    private static final String APAT_TYPE = "APAT";

    protected BSession bsession;
    protected LinkedList<String> errors = new LinkedList<>();
    protected LinkedList<String> infos = new LinkedList<>();
    protected LinkedList<String> errorsCreateBeneficiaries = new LinkedList<>();
    protected String backupFolder;
    protected String errorsFolder;
    protected String storageFolder;
    protected String demandeFolder;
    private final List<String> fichiersTraites = new ArrayList<>();
    private final List<String> fichiersNonTraites = new ArrayList<>();

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
            generationProtocol();
        } catch (Exception e) {
            setReturnCode(-1);
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            try {
                generationProtocol();
            } catch (Exception e1) {
                throw new TechnicalException("Problème à l'envoi du mail", e1);
            }
            throw new TechnicalException("Erreur dans le process d'import", e);
        } finally {
            closeBsession();
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
                    if (MAX_TREATMENT > fichiersTraites.size()) {
                        importFile(nomFichierDistant);
                    }
                }
                LOG.info("Nombre de dossiers traités : {}", fichiersTraites.size());
            } else {
                LOG.warn("Les propriétés AMAT APAT ne sont pas définis.");
                errors.add("Import impossible : les propriétés AMAT-APAT ne sont pas définis.");
            }
        } catch (Exception e) {
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
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
                if (message != null) {
                    Content content = message.getContent();
                    nss = content.getInsuredPerson().getVn();
                    isTraitementSuccess = createDroitGlobal(content, nss);
                    if (isTraitementSuccess) {
                        fichiersTraites.add(nameOriginalFile);
                        savingFileInDb(nss, nomFichier, content.getAmatApatType());
                        infos.add("Traitement du fichier suivant réussi : " + nameOriginalFile);
                        infos.add("Assuré(s) concerné(s) : " + nss);
                    } else {
                        fichiersNonTraites.add(nameOriginalFile);
                    }
                } else {
                    errors.add("Le fichier XML ne peut pas être traité : " + nameOriginalFileWithoutExt);
                }

                // on déplace les fichiers traités.
                LOG.info("Déplacer le fichier : {}", nameOriginalFileWithoutExt);
                movingFile(nomFichier, nameOriginalFile, nss, isTraitementSuccess);
            } catch (Exception e) {
                // TODO: ajuster message dans l'erreur
                errors.add("Erreur lors du traitement du fichier.");
                LOG.error("Erreur lors du traitement du fichier. ", e);
            }
        }
    }

    private boolean createDroitGlobal(Content content, String nssTiers) throws Exception {
        BTransaction transaction = (BTransaction) bsession.newTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        InsuredPerson assure = content.getInsuredPerson();
        AddressType adresseAssure = content.getInsuredAddress();

        String npaFormat = formatNPA(getZipCode(adresseAssure));
        IAPImportationAmatApat handler;
        boolean isWomen = false;
        if (StringUtils.equals(AMAT_TYPE, content.getAmatApatType())) {
            handler = new APImportationAmat(errors, infos, bsession);
            isWomen = true;
        } else if(StringUtils.equals(APAT_TYPE, content.getAmatApatType())) {
            handler = new APImportationApat(errors, infos, bsession);
        } else {
            handler = null;
        }

        if(handler != null) {
            PRTiersWrapper tiers = getTiersByNss(nssTiers);
            if (Objects.isNull(tiers)) {
                tiers = handler.createTiers(assure, npaFormat, isWomen);
            }
            if (tiers != null) {
                handler.createRoleApgTiers(tiers.getIdTiers());
                handler.createContact(tiers, assure.getEmail());
                PRDemande demande = handler.createDemande(tiers.getIdTiers());
                APDroitLAPG droit = handler.createDroit(content, npaFormat, demande, transaction);
                handler.createSituationFamiliale(content.getFamilyMembers(), droit.getIdDroit(), transaction);
                handler.createSituationProfessionnel(content, droit.getIdDroit(), transaction);
            } else {
                errors.add("Une erreur s'est produite lors de la création du tiers : " + nssTiers);
                LOG.error("ImportAPGAmatApat#createTiers - Une erreur s'est produite dans la création du tiers : {}", nssTiers);
            }
        }

        if (!hasError(bsession, transaction) && handler != null) {
            transaction.commit();
            LOG.info("Traitement en succès...");
            transaction.closeTransaction();
            return true;
        }else {
            transaction.rollback();
            errors.add("Un problème est survenu lors de la création du droit pour cet assuré : " + assure.getVn());
            LOG.error("Erreur lors de la création du droit\nSession errors : {} \nTransactions errors {}: ", bsession.getErrors(), transaction.getErrors());
            transaction.closeTransaction();
            return false;
        }
    }

    private void savingFileInDb(String nss, String pathFile, String apgType) throws Exception {
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
            if (!hasError(bsession, transaction)) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } catch (FileNotFoundException e) {
            LOG.error("Fichier non trouvé : " + pathFile, e);
        } catch (Exception e) {
            errors.add("Erreur lors de l'update Historique importation APG " + e.getMessage());
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
    private void movingFile(String nomFichierDistant, String nameOriginalFile, String nss, boolean processSuccess) {
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

            if (processSuccess) {
                // Création du répertoire de storage
                storageFolderTemp = CommonFilesUtils.createPathFiles(nss, backupFolder);

                // Copie du fichier dans le storage
                if (!storageFolderTemp.isEmpty()) {
                    JadeFsFacade.copyFile(nomFichierDistant, storageFolderTemp + nameOriginalFile);
                    storageFileTempStorage = storageFolderTemp + nameOriginalFile;
                    LOG.info("Copie dans {}", storageFileTempStorage);
                }

            } else {
                // copie du fichier en erreur
                JadeFsFacade.copyFile(nomFichierDistant, errorsFolder + nameOriginalFile);
                storageFileTempTrace = errorsFolder + nameOriginalFile;
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

        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
            LOG.error("Erreur lors du déplacement du fichier {}", nameOriginalFile, e);
        }
    }


    /**
     * Méthode permettant de générer le bilan du traitement et l'envoi du mail récapitulatif.
     *
     */
    private void generationProtocol() {

        StringBuilder corpsCaisse = new StringBuilder();

        corpsCaisse.append(buildBlocMessage(fichiersTraites, "Fichiers traités : ", "; "));

        corpsCaisse.append(buildBlocMessage(fichiersNonTraites, "Fichiers non traités :", "; "));

        corpsCaisse.append(buildBlocMessage(infos, "Infos :", "\n"));

        corpsCaisse.append(buildBlocMessage(errors, "Erreurs :", "\n"));

        List<String> mails = getListEMailAddressTechnique();
        LOG.info("Envoi mail à l'adresse de la caisse {}", mails);
        ProcessMailUtils.sendMail(mails, getEMailObjectCaisse(), corpsCaisse.toString(), new ArrayList<>());

    }

    /**
     * Construit un bloc du mail.
     *
     * @param liste     la liste des infos
     * @param texte     le texte à saisir
     * @param separator le seprateur
     * @return un bloc du mail.
     */
    private String buildBlocMessage(Collection<String> liste, String texte, String separator) {
        StringBuilder bloc = new StringBuilder();
        if (!liste.isEmpty()) {
            bloc.append(texte).append(liste.size()).append("\n");
            for (String each : liste) {
                bloc.append(each).append(separator);
            }
            bloc.append("\n");
        }
        return bloc.toString();
    }

    /**
     * Récupérer l'objet du mail.
     *
     * @return l'objet du mail
     */
    private String getEMailObjectCaisse() {
        return "Importation AMAT-APAT : " + fichiersTraites.size() + " fichier(s) traité(s)";
    }


    /**
     * @param adresseAssure
     * @return
     */
    private String getZipCode(AddressType adresseAssure) {
        String zipCodeTown = adresseAssure.getZipCodeTown();

        String[] zipCodeTownSplitted = StringUtils.split(zipCodeTown, ",");

        return zipCodeTownSplitted[0].trim();
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
            errors.add("Impossible de parser le fichier XML : " + destPath + "(" + e.getMessage() + ")");
            LOG.error("Erreur lors de l'importation du fichier " + destPath, e);
        }
        return null;
    }

    /**
     * Méthode permettant de récupérer les adresses email à qui on souhaite envoyer l'email.
     * @return la liste des adresses email.
     */
    protected final List<String> getListEMailAddressTechnique() {
        List<String> listEmailAddress = new ArrayList<>();
        try {
            String[] addresses = APProperties.EMAIL_AMAT_APAT.getValue().split(";");
            listEmailAddress = Arrays.asList(addresses);
        } catch (PropertiesException e) {
            LOG.error("ImportAPG-AMAT-APAT - Erreur à la récupération de la propriété Adresse E-mail !! ", e);
        }
        return listEmailAddress;
    }


    /**
     * Méthode permettant de récupérer le tiers à partir du NSS.
     *
     * @param nss : le nss
     * @return le tiers
     * @throws Exception
     */
    protected PRTiersWrapper getTiersByNss(String nss) {
        try {
            return PRTiersHelper.getTiers(bsession, nss);
        } catch (Exception e) {
            errors.add("Impossible de récupérer le tiers : "+nss);
            LOG.error("Erreur lors de la récupération du tiers {}", nss, e);
            return null;
        }
    }

    /**
     * Formattage du NPA
     *
     * @param npa le npa à formatter
     * @return le npa formatté.
     */
    private String formatNPA(String npa) {
        if(StringUtils.isNotEmpty(npa)){
            String npaTrim = StringUtils.trim(npa);
            if(StringUtils.isNumeric(npaTrim)){
                return npaTrim;
            }else{
                infos.add("NPA incorrect ! Celui-ci doit être dans un format numérique uniquement ! Valeur: "+npa);
            }
        }
        return "";
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    private boolean hasError(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly() || !errors.isEmpty();
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
