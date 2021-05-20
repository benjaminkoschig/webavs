package globaz.osiris.process;

import ch.globaz.common.document.reference.AbstractReference;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import com.google.common.base.Throwables;
import com.jcraft.jsch.SftpException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.*;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.ebill.CAFichierInscriptionEBill;
import globaz.osiris.db.ebill.CAInscriptionEBill;
import globaz.osiris.db.ebill.enums.CAFichierInscriptionStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CAInscriptionTypeEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.external.IntRole;
import globaz.osiris.process.ebill.CAInscriptionEBillEnum;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CRON permettant le traitement des fichiers d'inscription eBill.
 */
@Slf4j
public class CAProcessImportInscriptionEBill extends BProcess {

    private static final String MAIL_CONTENT = "EBILL_MAIL_INSCRIPTION_CONTENT";
    private static final String MAIL_ERROR_CONTENT = "EBILL_MAIL_INSCRIPTION_ERROR_CONTENT";
    private static final String MAIL_SUBJECT = "EBILL_MAIL_INSCRIPTION_SUBJECT";
    private static final String CSV_EXTENSION = ".csv";
    private static final char SEPARATOR = ';';
    private static final String BOOLEAN_TRUE = "on";
    private final StringBuilder error = new StringBuilder();
    private List<String> filesToSend = new ArrayList<>();
    private boolean isPlusieursTypeAffilie;
    private EBillSftpProcessor serviceFtp;
    private int inscriptionOK = 0;
    private int inscriptionKO = 0;
    private int resiliationOK = 0;
    private int resiliationKO = 0;

    @Override
    protected void _executeCleanUp() {
        //Nothing to do
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Lancement du process d'importation des inscriptions e-Bill.");
            //Pas d'envoi de mail automatique du process, tout est géré manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

            initBsession();
            initServiceFtp();

            isPlusieursTypeAffilie = Boolean.valueOf(CAApplication.getApplicationOsiris().getProperty(CaisseHelperFactory.PLUSIEURS_TYPE_AFFILIE, "false"));
            boolean isActive = CAApplication.getApplicationOsiris().getCAParametres().isEbill(getSession());

            if (isActive) {
                importFiles();
                generationProtocol();
            }

        } catch (Exception e) {
            error.append("Impossible d'exécuter le processus d'importation des inscriptions : ").append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            sendResultMail(error.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
            closeServiceFtp();
        }

        return true;

    }

    /**
     * Méthode permettant de générer le bilan du traitement et l'envoi du mail récapitulatif.
     *
     * @throws Exception : exception envoyée si un problème intervient lors de l'envoi du mail.
     */
    private void generationProtocol() {
        String mailContent;
        if (error.length() != 0) {
            mailContent = getEmailContent() + getEmailErrorContent();
        } else {
            mailContent = getEmailContent();
        }
        sendResultMail(mailContent);
    }

    /**
     * Initialisation de la session.
     *
     * @throws Exception : exception envoyée si un problème intervient lors de l'initialisation de la session.
     */
    private void initBsession() throws Exception {
        LOG.info("Initialisation de la session");
        BSessionUtil.initContext(getSession(), this);
    }

    /**
     * Fermeture de la session.
     */
    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    /**
     * Fermeture du service ftp.
     */
    private void closeServiceFtp() {
        if (serviceFtp != null) {
            serviceFtp.disconnectQuietly();
        }
    }

    /**
     * Initialisation du service ftp.
     */
    private void initServiceFtp() throws PropertiesException {
        if (serviceFtp == null) {
            serviceFtp = new EBillSftpProcessor();
        }
    }

    /**
     * Récupération et traitement des fichiers d'inscription eBill
     */
    private void importFiles() {
        try {
            LOG.info("Importation des fichiers d'inscription...");

            // Nous recherchons tous les fichiers d'inscriptions déposés sur le serveur FTP PostFinance
            List<String> files = serviceFtp.getListFiles(CAProcessImportInscriptionEBill.CSV_EXTENSION);

            for (final String nomFichierDistant : files) {
                importFile(nomFichierDistant);
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de l'importation des fichiers d'inscription", e);
            error.append("Impossible de procéder à l'importation des fichiers d'inscription : ").append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
        }
    }

    /**
     * Récupération et traitement des données dans le fichier d'inscription eBill
     *
     * @param nomFichierDistant : le nom du fichier à traiter.
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     * @throws JadeServiceLocatorException
     */
    private void importFile(String nomFichierDistant) throws PropertiesException {
        // Enregistrement des données de traitement du fichier
        CAFichierInscriptionEBill fichierInscription = saveFichierInscription(nomFichierDistant);
        // Si le fichier n'a pas pu être enregistré en BDD, on ne le traite et le problème sera remonté dans le rapport par mail.
        if (Objects.nonNull(fichierInscription)) {

            String localPath = Jade.getInstance().getPersistenceDir() + serviceFtp.getFolderInName() + nomFichierDistant;
            File localFile = new File(localPath);
            try {

                // Download du fichier CSV
                try (FileOutputStream retrievedFile = new FileOutputStream(localFile)) {
                    serviceFtp.retrieveFile(nomFichierDistant, retrievedFile);
                    serviceFtp.deleteFile(nomFichierDistant);
                }

                // Traitement du fichier CSV
                try (BufferedReader reader = new BufferedReader(new FileReader(localFile))) {
                    List<CAInscriptionEBill> allInscriptions = new ArrayList<>();

                    // TODO : contrôler le nombre de colonne sur la première ligne
                    int i = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // on passe la première ligne.
                        if (i > 0 && !line.isEmpty()) {
                            CAInscriptionEBill inscriptionEBill = extractDataFromFile(line);
                            allInscriptions.add(inscriptionEBill);
                        }
                        i++;
                    }

                    // Enregistre toutes les inscriptions en BD et met à jour le status du fichier d'inscription
                    boolean toutesInscriptionsEnSucces = saveInscriptions(allInscriptions, fichierInscription);
                    updateInscriptionFileStatut(fichierInscription, toutesInscriptionsEnSucces, localFile);
                }

            } catch (FileNotFoundException e) {
                LOG.error("Une erreur s'est produite lors de la récupération du fichier à traiter : " + nomFichierDistant, e);
                error.append("Impossible de récupérer le fichier à traiter :").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            } catch (IOException e) {
                LOG.error("Une erreur s'est produite lors de la lecture du fichier à traiter : " + nomFichierDistant, e);
                error.append("Impossible de lire le fichier à traiter : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            } catch (SftpException e) {
                LOG.error("Une erreur s'est produite lors du téléchargement du fichier à traiter : " + nomFichierDistant, e);
                error.append("Impossible de télécharger le fichier à traiter : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            }
        }
    }

    /**
     * Mise à jour du statut du fichier suite au traitement des inscriptions.
     *
     * @param fichier                    : le fichier à mettre à jour.
     * @param toutesInscriptionsEnSucces : booléen qui indique si la mise à jour du compte annexe s'est bien faite pour toutes les inscriptions
     */
    private void updateInscriptionFileStatut(CAFichierInscriptionEBill fichier, boolean toutesInscriptionsEnSucces, File localFile) {
        String nomFichier = fichier.getNomFichier();
        if (toutesInscriptionsEnSucces) {
            fichier.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.TRAITE.getIndex()));
        } else if (inscriptionOK > 0) {
            fichier.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.TRAITE_ERREUR.getIndex()));
            filesToSend.add(localFile.getAbsolutePath());
        } else {
            fichier.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.NON_TRAITE.getIndex()));
            filesToSend.add(localFile.getAbsolutePath());
        }
        try {
            fichier.update(getTransaction());
        } catch (Exception e) {
            LOG.error("Erreur lors de la mise à jour du fichier : " + nomFichier, e);
            error.append("Impossible de mettre à jour le fichier : ").append(nomFichier).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
        }
    }

    /**
     * Sauvegarde de l'inscription.
     *
     * @param fichier         : le fichier lié à l'inscription.
     * @param eachInscription : l'inscription à sauvegarder.
     * @param result          : booléen qui indique si la mise à jour du compte annexe s'est bien faite.
     * @return vrai si la sauvegarde est en succès.
     */
    private boolean saveInscription(CAFichierInscriptionEBill fichier, CAInscriptionEBill eachInscription, boolean result) {
        eachInscription.setSession(getSession());
        eachInscription.setIdFichier(fichier.getIdFichier());
        if (result) {
            eachInscription.setStatut(CAStatutEBillEnum.NUMERO_STATUT_TRAITE_AUTOMATIQUEMENT);
        } else {
            eachInscription.setStatut(CAStatutEBillEnum.NUMERO_STATUT_A_TRAITER);
        }
        try {
            eachInscription.add(getTransaction());
        } catch (Exception e) {
            LOG.error("Erreur lors de la sauvegarde d'une inscription.", e);
            error.append("Impossible d'enregistrer en base de données l'inscription suivante : ")
                    .append(" numeroAffilie : ").append(eachInscription.getNumeroAffilie())
                    .append(" eBillAccountID : ").append(eachInscription.geteBillAccountID())
                    .append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return true;
    }


    /**
     * Sauvegarde les informations du fichier traité en BDD.
     *
     * @param nomFichierDistant le nom du fichier traité.
     * @return retourne le fichier sauvegardé en BDD au statut non traité.
     */
    private CAFichierInscriptionEBill saveFichierInscription(String nomFichierDistant) {
        CAFichierInscriptionEBill fichier = new CAFichierInscriptionEBill();
        String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
        String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
        fichier.setNomFichier(nameOriginalFileWithoutExt);
        fichier.setDateLecture(JACalendar.todayJJsMMsAAAA());
        fichier.setStatutFichier(String.valueOf(CAFichierInscriptionStatutEBillEnum.NON_TRAITE.getIndex()));
        fichier.setSession(getSession());
        try {
            fichier.add(getTransaction());
        } catch (Exception e) {
            LOG.error("Erreur lors de la sauvegarder du fichier en base de données : " + nomFichierDistant, e);
            error.append("Impossible de sauvegarder en base de données le fichier : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return null;
        }
        return fichier;
    }

    /**
     * Méthode permettant de caster les lignes du fichier csv.
     *
     * @param line : la ligne à caster
     * @return l'inscription lié à la ligne.
     */
    private CAInscriptionEBill extractDataFromFile(String line) {
        CAInscriptionEBill inscriptionEBill = new CAInscriptionEBill();

        String regex = Character.toString(SEPARATOR);
        // Suppression des espaces de fin de ligne
        line = line.trim();

        // Split des données
        String[] datasTemp = line.split(regex);

        // on démarre à 1 pour ne pas caster la première ligne.
        for (int i = 0; i < datasTemp.length; i++) {
            CAInscriptionEBillEnum enumInscriptionEBill = CAInscriptionEBillEnum.fromIndex(String.valueOf(i));
            if (Objects.nonNull(enumInscriptionEBill)) {
                switch (enumInscriptionEBill) {
                    case NUMERO_ADHERENT:
                        inscriptionEBill.seteBillAccountID(datasTemp[i]);
                        break;
                    case PRENOM:
                        inscriptionEBill.setPrenom(datasTemp[i]);
                        break;
                    case NOM:
                        inscriptionEBill.setNom(datasTemp[i]);
                        break;
                    case ENTREPRISE:
                        inscriptionEBill.setEntreprise(datasTemp[i]);
                        break;
                    case ADRESSE_1:
                        inscriptionEBill.setAdresse1(datasTemp[i]);
                        break;
                    case ADRESSE_2:
                        inscriptionEBill.setAdresse2(datasTemp[i]);
                        break;
                    case NPA:
                        inscriptionEBill.setNpa(datasTemp[i]);
                        break;
                    case LOCALITE:
                        inscriptionEBill.setLocalite(datasTemp[i]);
                        break;
                    case NUMERO_TEL:
                        inscriptionEBill.setNumTel(datasTemp[i]);
                        break;
                    case EMAIL:
                        inscriptionEBill.setEmail(datasTemp[i]);
                        break;
                    case NUMERO_AFFILIE:
                        inscriptionEBill.setNumeroAffilie(datasTemp[i]);
                        break;
                    case ROLE_PARITAIRE:
                        inscriptionEBill.setRoleParitaire(StringUtils.equals(BOOLEAN_TRUE, datasTemp[i]));
                        break;
                    case ROLE_PERSONNEL:
                        inscriptionEBill.setRolePersonnel(StringUtils.equals(BOOLEAN_TRUE, datasTemp[i]));
                        break;
                    case NUM_ADHERENT_BVR:
                        inscriptionEBill.setNumAdherentBVR(datasTemp[i]);
                        break;
                    case NUM_REF_BVR:
                        inscriptionEBill.setNumRefBVR(datasTemp[i]);
                        break;
                    case STATUS:
                        inscriptionEBill.setType(datasTemp[i]);
                        break;
                    default:
                        break;
                }
            }
        }
        return inscriptionEBill;
    }

    /**
     * Sauvegardes des inscriptions eBill.
     *
     * @param allInscriptions toutes les inscriptions du fichier.
     * @param fichier         le fichier en cours de traitement.
     * @return vrai si toutes les inscriptions du fichier ont été traités avec succès, faux sinon.
     */
    private boolean saveInscriptions(List<CAInscriptionEBill> allInscriptions, CAFichierInscriptionEBill fichier) {
        boolean toutesInscriptionsSucces = true;
        for (CAInscriptionEBill eachInscription : allInscriptions) {

            final String numeroAdherent = eachInscription.geteBillAccountID();
            final CAInscriptionTypeEBillEnum typeEBillEnum = eachInscription.getType();

            boolean result;
            boolean resultInscription;
            if (StringUtils.isNotEmpty(numeroAdherent)) {
                switch (typeEBillEnum) {
                    case INSCRIPTION:
                        result = this.updateCompteAnnexeTitulariseCasInscription(eachInscription, numeroAdherent);
                        resultInscription = saveInscription(fichier, eachInscription, result);
                        if (result && resultInscription) {
                            inscriptionOK++;
                        } else {
                            inscriptionKO++;
                            toutesInscriptionsSucces = false;
                        }
                        break;
                    case INSCRIPTION_DIRECTE:
                        result = this.updateCompteAnnexeTitulariseCasInscriptionDirect(eachInscription, numeroAdherent);
                        resultInscription = saveInscription(fichier, eachInscription, result);
                        if (result && resultInscription) {
                            inscriptionOK++;
                        } else {
                            inscriptionKO++;
                            toutesInscriptionsSucces = false;
                        }
                        break;
                    case RESILIATION:
                        result = this.updateCompteAnnexeTitulariseCasResiliation(eachInscription, numeroAdherent);
                        resultInscription = saveInscription(fichier, eachInscription, result);
                        if (result && resultInscription) {
                            resiliationOK++;
                        } else {
                            resiliationKO++;
                            toutesInscriptionsSucces = false;
                        }
                        break;
                    default:
                        break;
                }

            } else {
                LOG.warn("Le numéro de compte e-bill n'est pas renseigné.");
                error.append("Impossible de trouver le numéro de compte e-bill.").append("\n");
                toutesInscriptionsSucces = false;
            }

        }
        return toutesInscriptionsSucces;
    }

    /**
     * Mise à jour du compte annexe lors d'une résiliation.
     *
     * @param numeroAdherent : le numéro d'adhérent (id eBill) lié à la résiliation.
     * @return vrai si la mise à jour du compte annexe est en succès.
     */
    private boolean updateCompteAnnexeTitulariseCasResiliation(CAInscriptionEBill eachInscription, final String numeroAdherent) {
        // Récupération du compte annexe.
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForEBillAccountID(numeroAdherent);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération du compte annexe par le numéro d'adhérent : " + numeroAdherent , e);
            eachInscription.setTexteErreurInterne("Erreur lors de la récupération du compte annexe par le numéro d'adhérent : " + numeroAdherent);
            error.append("Impossible de récupérer le compte annexe par le numéro d'adhérent : ").append(numeroAdherent).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }

        if (manager.getSize() == 1) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
            return majCompteAnnexe(eachInscription, StringUtils.EMPTY, StringUtils.EMPTY, compteAnnexe);
        } else {
            LOG.warn("Erreur lors de la récupération d'un compte annexe unique pour le numéro d'adhérent : " + numeroAdherent);
            eachInscription.setTexteErreurInterne("Erreur lors de la récupération d'un compte annexe unique pour le numéro d'adhérent : " + numeroAdherent);
            error.append("Impossible de récupérer un compte annexe unique pour le numéro d'adhérent : ").append(numeroAdherent).append("\n");
            return false;
        }

    }

    /**
     * Mise à jour du compte annexe lors d'une inscription directe.
     *
     * @param inscriptionEBill l'inscription eBill à traiter.
     * @param numeroAdherent   le numéro d'adhérent (id eBill) lié à l'inscription.
     * @return vrai si la mise à jour du compte annexe est en succès.
     */
    private boolean updateCompteAnnexeTitulariseCasInscriptionDirect(CAInscriptionEBill inscriptionEBill,
                                                                     final String numeroAdherent) {
        if (StringUtils.isNotEmpty(inscriptionEBill.getNumRefBVR())) {
            final String idRole = inscriptionEBill.getNumRefBVR().substring(0, 2);

            // Récupération du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());

            if (StringUtils.equals(idRole, AbstractReference.IDENTIFIANT_REF_IDCOMPTEANNEXE)) {
                try {
                    final String idCompteAnnexe = formatterNumeroDepuisRefBVR(inscriptionEBill);
                    manager.setForIdCompteAnnexeIn(idCompteAnnexe);
                    manager.find(BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    LOG.error("Erreur lors de la récupération du compte annexe par l'id du compte annexe depuis la référence de BVR : " + inscriptionEBill.getNumRefBVR(), e);
                    inscriptionEBill.setTexteErreurInterne("Erreur lors de la récupération du compte annexe par l'id du compte annexe depuis la référence de BVR : " + inscriptionEBill.getNumRefBVR());
                    error.append("Impossible de récupérer un compte annexe par l'id du compte annexe depuis la référence du BVR : ").append(inscriptionEBill.getNumRefBVR()).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                    return false;
                }
            } else {

                // Formatage du numéro d'affilié
                String numeroAffilieFormate;
                try {
                    final String numeroAffilie = formatterNumeroDepuisRefBVR(inscriptionEBill);
                    CAApplication application = (CAApplication) GlobazServer.getCurrentSystem().getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
                    IFormatData affilieFormater = application.getAffileFormater();
                    numeroAffilieFormate = affilieFormater.format(numeroAffilie);
                } catch (Exception e) {
                    LOG.error("Erreur lors du formattage du numéro d'affilié à partir du numéro de référence BVR : " + inscriptionEBill.getNumRefBVR());
                    inscriptionEBill.setTexteErreurInterne("Erreur lors du formattage du numéro d'affilié à partir du numéro de référence BVR :  " + inscriptionEBill.getNumRefBVR());
                    error.append("Impossible de formatter le numéro d'affilié à partir du numéro de référence BVR : ").append(inscriptionEBill.getNumRefBVR()).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                    return false;
                }

                manager.setForIdExterneRole(numeroAffilieFormate);
                if (isPlusieursTypeAffilie) {
                    if (inscriptionEBill.getRoleParitaire()) {
                        manager.setForIdRole(IntRole.ROLE_AFFILIE_PARITAIRE);
                    } else if (inscriptionEBill.getRolePersonnel()) {
                        manager.setForIdRole(IntRole.ROLE_AFFILIE_PERSONNEL);
                    }
                } else {
                    manager.setForIdRole(IntRole.ROLE_AFFILIE);
                }
                try {
                    manager.find(BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    String erreurInterne = String.format("Erreur lors de la récupération du compte annexe par le numéro d'affilié : %s", numeroAffilieFormate);
                    LOG.error(erreurInterne, e);
                    inscriptionEBill.setTexteErreurInterne(erreurInterne);
                    error.append("Impossible de récupérer le compte annexe par le numéro d'affilié : ").append(numeroAffilieFormate).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                    return false;
                }
            }

            if (manager.getSize() == 1) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
                return majCompteAnnexe(inscriptionEBill, numeroAdherent, inscriptionEBill.getEmail(), compteAnnexe);
            }
        }
        String erreurInterne = String.format("Erreur lors de la récupération du compte annexe lié au numéro de référence BVR : %s", inscriptionEBill.getNumRefBVR());
        LOG.warn(erreurInterne);
        inscriptionEBill.setTexteErreurInterne(erreurInterne);
        error.append("Impossible de récupérer le compte annexe lié au numéro de référence BVR : " + inscriptionEBill.getNumRefBVR()).append("\n");
        return false;
    }


    /**
     * Méthode permettant de formatter le numéro depuis la référence BVR : on doit supprimer les 0 devant la première valeur.
     *
     * @param inscriptionEBill : le numéro depuis la ref BVR.
     * @return le numéro formatté.
     * @throws CATechnicalException : exception lancée si le formattage échoue.
     */
    private String formatterNumeroDepuisRefBVR(CAInscriptionEBill inscriptionEBill) throws CATechnicalException {
        String numero;
        try {
            numero = Long.toString(new Long(inscriptionEBill.getNumRefBVR().substring(3, 15)));
        } catch (Exception e) {
            throw new CATechnicalException("Une erreur est intervenue lors de la récupération de l'id depuis la référence BVR.", e);
        }
        return numero;
    }

    /**
     * Mise à jour du compte annexe lors d'une inscription.
     *
     * @param inscriptionEBill l'inscription eBill à traiter.
     * @param numeroAdherent   le numéro d'adhérent (id eBill) lié à l'inscription.
     * @return vrai si la mise à jour du compte annexe est en succès.
     */
    private boolean updateCompteAnnexeTitulariseCasInscription(CAInscriptionEBill inscriptionEBill,
                                                               final String numeroAdherent) {
        String numeroAffilie;
        numeroAffilie = inscriptionEBill.getNumeroAffilie();
        if (StringUtils.isNotEmpty(numeroAffilie)) {

            // Récupération du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            manager.setForIdExterneRole(numeroAffilie);
            if (isPlusieursTypeAffilie) {
                if (inscriptionEBill.getRoleParitaire()) {
                    manager.setForIdRole(IntRole.ROLE_AFFILIE_PARITAIRE);
                } else if (inscriptionEBill.getRolePersonnel()) {
                    manager.setForIdRole(IntRole.ROLE_AFFILIE_PERSONNEL);
                }
            } else {
                manager.setForIdRole(IntRole.ROLE_AFFILIE);
            }

            try {
                manager.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                LOG.error("Erreur lors de la récupération du compte annexe par le numéro d'affilié : " + numeroAffilie , e);
                inscriptionEBill.setTexteErreurInterne("Erreur lors de la récupération du compte annexe par le numéro d'affilié : " + numeroAffilie);
                error.append("Impossible de récupérer le compte annexe par le numéro d'affilié : ").append(numeroAffilie).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                return false;
            }

            if (manager.getSize() == 1) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
                return majCompteAnnexe(inscriptionEBill, numeroAdherent, inscriptionEBill.getEmail(), compteAnnexe);
            } else {
                LOG.warn("Erreur lors de la récupération d'un compte annexe unique par le numéro d'affilié : " + numeroAffilie);
                inscriptionEBill.setTexteErreurInterne("Erreur lors de la récupération d'un compte annexe unique par le numéro d'affilié : " + numeroAffilie);
                error.append("Impossible de récupérer un compte annexe unique par le numéro d'affilié : ").append(numeroAffilie).append("\n");
                return false;
            }
        }
        LOG.warn("le numéro d'affilié n'est pas renseigné.");
        return false;
    }

    /**
     * Mise à jour du compte annexe.
     *
     * @param numeroAdherent : le numéro d'adhérent (id eBill) de l'inscription --> vide dans le cas d'une résiliation.
     * @param email          : le mail lié à l'inscription --> vide dans le cadre d'une résiliation.
     * @param compteAnnexe   : le compte annexe à mettre à jour.
     * @return vrai si l'update s'est bien passé.
     */
    private boolean majCompteAnnexe(CAInscriptionEBill inscriptionEBill, final String numeroAdherent,
                                    final String email,
                                    final CACompteAnnexe compteAnnexe) {
        compteAnnexe.seteBillAccountID(numeroAdherent);
        compteAnnexe.seteBillMail(email);
        try {
            compteAnnexe.update();
        } catch (Exception e) {
            String erreurInterne = String.format("Erreur lors de la mise à jour du compte annexe d'id '%s' avec le numéro d'adhérent '%s'. ", compteAnnexe.getIdCompteAnnexe(), numeroAdherent);
            LOG.error(erreurInterne, e);
            inscriptionEBill.setTexteErreurInterne(erreurInterne);
            error.append("Impossible de mettre à jour le compte annexe avec l'id : ").append(compteAnnexe.getIdCompteAnnexe()).append(" avec le numéro d'adhérent : ").append(numeroAdherent).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return true;
    }

    /**
     * Envoi le résultat du traitement par mail.
     *
     * @param mailContent : le contenu du mail envoyé.
     *
     */
    private void sendResultMail(String mailContent) {
        try {
            String[] joinsFilesPathsTab = null;
            if (!filesToSend.isEmpty()) {
                joinsFilesPathsTab = filesToSend.toArray(new String[filesToSend.size()]);
            }
            JadeSmtpClient.getInstance().sendMail(getEMailAddressesEBill(), getEMailObject(), mailContent, joinsFilesPathsTab);
        } catch (Exception e) {
            LOG.error("Erreur lors de l'envoi de l'email : " + mailContent, e);
        }
    }

    /**
     * Retourne le rapport du traitement à afficher dans l'email.
     *
     * @return le contenu du mail
     */
    private String getEmailContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_CONTENT), inscriptionOK, inscriptionKO, resiliationOK, resiliationKO);
    }

    /**
     * Retourne le rapport d'erreur si un fichier n'a pas pu être sauvegardé.
     *
     * @return le contenu "erreur" du mail.
     */
    private String getEmailErrorContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_ERROR_CONTENT), error);
    }

    /**
     * Retourne les adresses mails à laquelle le rapport doit être envoyé.
     *
     * @return l'email renseigné dans la propriété JADE osiris.eBill.email.traitements.
     */
    private String[] getEMailAddressesEBill() {
        String eMailAddress = JadePropertiesService.getInstance().getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_EMAILS);
        eMailAddress = eMailAddress.replaceAll("\\s+", "");
        String[] eMailAddresses = new String[1];
        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && getSession() != null) {
            eMailAddresses[0] = getSession().getUserEMail();
        } else {
            eMailAddresses = eMailAddress.split("[,;:]");
        }
        return eMailAddresses;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel(MAIL_SUBJECT) + " " + JACalendar.todayJJsMMsAAAA();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
