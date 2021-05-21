package globaz.apg.process;

import apg.amatapat.*;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.apg.db.droits.*;
import globaz.apg.properties.APProperties;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.osiris.api.APIOperation;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class APImportationAPGAmatApatProcess extends APAbstractImportationAPGProcess {

    private static final Logger LOG = LoggerFactory.getLogger(APImportationAPGAmatApatProcess.class);

    private static final String AMAT_TYPE = "AMAT";
    private static final String APAT_TYPE = "APAT";

    private final List<String> fichiersTraites = new ArrayList<>();
    private final List<String> fichiersNonTraites = new ArrayList<>();

    @Override
    protected void _executeCleanUp() {

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
     * Méthode permettant de générer le bilan du traitement et l'envoi du mail récapitulatif.
     *
     * @throws Exception : exception envoyée si un problème intervient lors de l'envoi du mail.
     */
    private void generationProtocol() {

        StringBuilder corpsCaisse = new StringBuilder();

        corpsCaisse.append(buildBlocMessage(fichiersTraites, "Fichiers traités : ", "; "));

        corpsCaisse.append(buildBlocMessage(fichiersNonTraites, "Fichiers non traités :", "; "));

        corpsCaisse.append(buildBlocMessage(infos, "Infos :", "\n"));

        corpsCaisse.append(buildBlocMessage(errors, "Erreurs :", "\n"));

        List<String> mails = getListEMailAddressTechnique(APProperties.EMAIL_AMAT_APAT);
        LOG.info("Envoi mail à l'adresse de la caisse " + mails.toString());
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
    private String buildBlocMessage(List<String> liste, String texte, String separator) {
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
        return "Importation AMAT-APAT : " + nbTraites + " fichier(s) traité(s)";
    }


    /**
     * Traitement des fichiers AMAT
     */
    private void importFiles() throws Exception {
        try {
            demandeFolder = APProperties.DEMANDES_AMAT_APAT_FOLDER.getValue();
            storageFolder = APProperties.STORAGE_AMAT_APAT_FOLDER.getValue();

            if (!JadeStringUtil.isBlankOrZero(demandeFolder) && !JadeStringUtil.isBlankOrZero(storageFolder)) {
                List<String> repositoryDemandesAmatApat = JadeFsFacade.getFolderChildren(demandeFolder);
                backupFolder = new File(storageFolder).getAbsolutePath() + BACKUP_FOLDER;
                errorsFolder = new File(storageFolder).getAbsolutePath() + ERRORS_FOLDER;
                for (String nomFichierDistant : repositoryDemandesAmatApat) {
                    if (MAX_TREATMENT > nbTraites) {
                        importFile(nomFichierDistant);
                    }
                }
                LOG.info("Nombre de dossiers traités : " + nbTraites);
            } else {
                LOG.warn("Les propriétés AMAT APAT ne sont pas définis.");
                errors.add("Import impossible : les propriétés AMAT-APAT ne sont pas définis.");
            }
        } catch (Exception e) {
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            LOG.error("Erreur lors de l'importation des fichiers", e);
            throw new Exception("Erreur lors de l'importation des fichiers", e);
        }
    }

    /**
     * Copie le fichier XML en local, effectue le traitement d'importion, sauvegarde le fichier
     * dans le dossier backup, supprime le fichier de base
     *
     * @param nomFichier
     * @throws IOException
     */
    private void importFile(String nomFichier) throws IOException {
        if (nomFichier.endsWith(XML_EXTENSION)) {
            try {
                LOG.info("Démarrage de l'import du fichier : " + nomFichier);
                String nameOriginalFile = FilenameUtils.getName(nomFichier);
                String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
                boolean isTraitementSuccess = false;
                // Parsing du fichier XML
                Message message = getMessageFromFile(nomFichier);
                if (message != null) {
                    Content content = message.getContent();
                    isTraitementSuccess = creationDroitGlobal(content);
                    if (isTraitementSuccess) {
                        fichiersTraites.add(nameOriginalFile);
                    } else {
                        fichiersNonTraites.add(nameOriginalFile);
                    }
                    // TODO : si traitement en succès --> sauvegarde du fichier
                    if (isTraitementSuccess) {
                        for (String nss : nssTraites) {
                            savingFileInDb(nss, nomFichier, APIOperation.ETAT_TRAITE, content.getAmatApatType());
                            infos.add("Traitement du fichier suivant réussi : " + nameOriginalFile);
                            infos.add("Assuré(s) concerné(s) : " + nss);
                        }
                    }
                } else {
                    errors.add("Le fichier XML ne peut pas être traité : " + nameOriginalFileWithoutExt);
                }

                // on déplace les fichiers traités.
                LOG.info("Déplacer le fichier : " + nameOriginalFileWithoutExt);
                movingFile(nomFichier, nameOriginalFile, isTraitementSuccess);
            } catch (Exception e) {
                LOG.error("Erreur lors du traitement du fichier. ", e);
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            } finally {
                nbTraites++;
            }
        }

    }

    private void savingFileInDb(String nss, String pathFile, String state, String apgType) throws Exception {
        BTransaction transaction = null;
        FileInputStream fileToStore = null;
        try {
            transaction = (BTransaction) bsession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            if (transaction.hasErrors()) {
                LOG.error("Des erreurs ont été trouvés dans la transaction. : ", transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            fileToStore = new FileInputStream(pathFile);
            APImportationAPGHistorique importData = new APImportationAPGHistorique();
            importData.setSession(bsession);
            importData.setEtatDemande(state);
            importData.setTypeDemande(apgType);
            importData.setNss(nss);
            importData.setXmlFile(fileToStore);
            importData.add(transaction);
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

    private boolean creationDroitGlobal(Content content) throws Exception {
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
            handler = new APImportationAmat(errors, infos);
            isWomen = true;
        } else if(StringUtils.equals(APAT_TYPE, content.getAmatApatType())) {
            handler = new APImportationApat(errors, infos);
        } else {
            handler = null;
        }

        String nssTiers = assure.getVn();
        if(handler != null) {
            PRTiersWrapper tiers = getTiersByNss(nssTiers);
            // TODO : identifier quand il faut créer un tiers.
            if (Objects.isNull(tiers)) {
                tiers = handler.createTiers(assure, npaFormat, bsession, isWomen);
            }
            if (tiers != null) {
                handler.createRoleApgTiers(tiers.getIdTiers(), bsession);
                handler.createContact(tiers, assure.getEmail(), bsession);
                PRDemande demande = handler.createDemande(tiers.getIdTiers(), bsession);
                APDroitLAPG droit = handler.createDroit(content, npaFormat, demande, transaction, bsession);
                handler.createSituationFamiliale(content.getFamilyMembers(), droit.getIdDroit(), transaction, bsession);
                handler.createSituationProfessionnel(content, droit.getIdDroit(), transaction, bsession);
                nssTraites.add(tiers.getNSS());
            } else {
                errors.add("Une erreur s'est produite lors de la création du tiers : " + nssTiers);
                LOG.error("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la création du tiers : " + nssTiers);
            }
        }

        if (!hasError(bsession, transaction) && handler != null) {
            transaction.commit();
            LOG.info("Traitement en succès...");
            return true;
        }else{
            transaction.rollback();
            errors.add("Un problème est survenu lors de la création du droit pour cet assuré : "+ assure.getVn());
            LOG.error("Erreur lors de la création du droit\nSession errors : "
                    +bsession.getErrors()+"\nTransactions errors : "+transaction.getErrors());
            return false;
        }

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

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
