package globaz.osiris.process;

import ch.globaz.common.document.reference.AbstractReference;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.jcraft.jsch.SftpException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.*;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
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
import globaz.osiris.process.ebill.EBillMail;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

/**
 * CRON permettant le traitement des fichiers d'inscription eBill.
 */
@Slf4j
public class CAProcessImportInscriptionEBill extends BProcess {

    private static final String MAIL_CONTENT = "EBILL_MAIL_INSCRIPTION_CONTENT";
    private static final String MAIL_ERROR_CONTENT = "EBILL_MAIL_INSCRIPTION_ERROR_CONTENT";
    private static final String MAIL_SUBJECT = "EBILL_MAIL_INSCRIPTION_SUBJECT";
    private static final String COMPTE_ANNEXE_EMAIL_MANQUANTE = "EBILL_COMPTE_ANNEXE_EMAIL_MANQUANTE";
    private static final String COMPTE_ANNEXE_EMAIL_FAILED = "EBILL_COMPTE_ANNEXE_EMAIL_FAILED";
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
            //Pas d'envoi de mail automatique du process, tout est g�r� manuellement
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
            String erreurInterne = getSession().getLabel("INSCR_EBILL_PROCESS_FAILED");
            LOG.error(erreurInterne, e);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            sendResultMail(error.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
            closeServiceFtp();
        }

        return true;

    }

    /**
     * M�thode permettant de g�n�rer le bilan du traitement et l'envoi du mail r�capitulatif.
     *
     * @throws Exception : exception envoy�e si un probl�me intervient lors de l'envoi du mail.
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
     * @throws Exception : exception envoy�e si un probl�me intervient lors de l'initialisation de la session.
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
     * R�cup�ration et traitement des fichiers d'inscription eBill
     */
    private void importFiles() {
        try {
            LOG.info("Importation des fichiers d'inscription...");

            // Nous recherchons tous les fichiers d'inscriptions d�pos�s sur le serveur FTP PostFinance
            List<String> files = serviceFtp.getListFiles(CAProcessImportInscriptionEBill.CSV_EXTENSION);

            for (final String nomFichierDistant : files) {
                importFile(nomFichierDistant);
            }

        } catch (Exception e) {
            String erreurInterne = getSession().getLabel("INSCR_EBILL_FICHIER_IMPORT_FAILED");
            LOG.error(erreurInterne, e);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
        }
    }

    /**
     * R�cup�ration et traitement des donn�es dans le fichier d'inscription eBill
     *
     * @param nomFichierDistant : le nom du fichier � traiter.
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     * @throws JadeServiceLocatorException
     */
    private void importFile(String nomFichierDistant) throws PropertiesException {
        // Enregistrement des donn�es de traitement du fichier
        CAFichierInscriptionEBill fichierInscription = saveFichierInscription(nomFichierDistant);
        // Si le fichier n'a pas pu �tre enregistr� en BDD, on ne le traite et le probl�me sera remont� dans le rapport par mail.
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

                    // TODO : contr�ler le nombre de colonne sur la premi�re ligne
                    int i = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // on passe la premi�re ligne.
                        if (i > 0 && !line.isEmpty()) {
                            CAInscriptionEBill inscriptionEBill = extractDataFromFile(line);
                            allInscriptions.add(inscriptionEBill);
                        }
                        i++;
                    }

                    // Enregistre toutes les inscriptions en BD et met � jour le status du fichier d'inscription
                    boolean toutesInscriptionsEnSucces = saveInscriptions(allInscriptions, fichierInscription);
                    updateInscriptionFileStatut(fichierInscription, toutesInscriptionsEnSucces, localFile);
                }

            } catch (FileNotFoundException e) {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_FICHIER_RECUP_FAILED"), nomFichierDistant);
                LOG.error(erreurInterne, e);
                error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            } catch (IOException e) {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_FICHIER_LECTURE_FAILED"), nomFichierDistant);
                LOG.error(erreurInterne, e);
                error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            } catch (SftpException e) {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_FICHIER_TELECHARGE_FAILED"), nomFichierDistant);
                LOG.error(erreurInterne, e);
                error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            }
        }
    }

    /**
     * Mise � jour du statut du fichier suite au traitement des inscriptions.
     *
     * @param fichier                    : le fichier � mettre � jour.
     * @param toutesInscriptionsEnSucces : bool�en qui indique si la mise � jour du compte annexe s'est bien faite pour toutes les inscriptions
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
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_MISEAJOUR_FAILED"), nomFichier);
            LOG.error(erreurInterne, e);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
        }
    }

    /**
     * Sauvegarde de l'inscription.
     *
     * @param fichier         : le fichier li� � l'inscription.
     * @param eachInscription : l'inscription � sauvegarder.
     * @param result          : bool�en qui indique si la mise � jour du compte annexe s'est bien faite.
     * @return vrai si la sauvegarde est en succ�s.
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
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_ENREGISTRE_FAILED"), eachInscription.getNumeroAffilie(), eachInscription.geteBillAccountID());
            LOG.error(erreurInterne, e);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return true;
    }


    /**
     * Sauvegarde les informations du fichier trait� en BDD.
     *
     * @param nomFichierDistant le nom du fichier trait�.
     * @return retourne le fichier sauvegard� en BDD au statut non trait�.
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
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_FICHIER_ENREGISTRE_FAILED"), nomFichierDistant);
            LOG.error(erreurInterne, e);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return null;
        }
        return fichier;
    }

    /**
     * M�thode permettant de caster les lignes du fichier csv.
     *
     * @param line : la ligne � caster
     * @return l'inscription li� � la ligne.
     */
    private CAInscriptionEBill extractDataFromFile(String line) {
        CAInscriptionEBill inscriptionEBill = new CAInscriptionEBill();

        String regex = Character.toString(SEPARATOR);
        // Suppression des espaces de fin de ligne
        line = line.trim();

        // Split des donn�es
        String[] datasTemp = line.split(regex);

        // on d�marre � 1 pour ne pas caster la premi�re ligne.
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
     * @return vrai si toutes les inscriptions du fichier ont �t� trait�s avec succ�s, faux sinon.
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
                String erreurInterne = getSession().getLabel("INSCR_EBILL_NUMERO_COMPTE_FAILED");
                LOG.warn(erreurInterne);
                error.append(erreurInterne).append("\n");
                toutesInscriptionsSucces = false;
            }

        }
        return toutesInscriptionsSucces;
    }

    /**
     * Mise � jour du compte annexe lors d'une r�siliation.
     *
     * @param numeroAdherent : le num�ro d'adh�rent (id eBill) li� � la r�siliation.
     * @return vrai si la mise � jour du compte annexe est en succ�s.
     */
    private boolean updateCompteAnnexeTitulariseCasResiliation(CAInscriptionEBill eachInscription, final String numeroAdherent) {
        // R�cup�ration du compte annexe.
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForEBillAccountID(numeroAdherent);
        if (isPlusieursTypeAffilie) {
            if (eachInscription.getRoleParitaire() && eachInscription.getRolePersonnel()) {
                Set<String> idsRole = new HashSet();
                idsRole.add(IntRole.ROLE_AFFILIE_PARITAIRE);
                idsRole.add(IntRole.ROLE_AFFILIE_PERSONNEL);
                manager.setForIdRoleIn(Joiner.on(",").join(idsRole));
            } else if (eachInscription.getRoleParitaire()) {
                manager.setForIdRole(IntRole.ROLE_AFFILIE_PARITAIRE);
            } else if (eachInscription.getRolePersonnel()) {
                manager.setForIdRole(IntRole.ROLE_AFFILIE_PERSONNEL);
            }
        } else {
            manager.setForIdRole(IntRole.ROLE_AFFILIE);
        }

        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_RETRIEVE_NUMERO_ADHERENT_FAILED"), numeroAdherent);
            LOG.error(erreurInterne, e);
            eachInscription.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }

        if (eachInscription.getRoleParitaire() && eachInscription.getRolePersonnel() && manager.getSize() == 2) {
            boolean inscriptionParitairePersonelSucces = true;
            for (int i = 0; i < manager.getSize(); i++) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(i);
                if (!majCompteAnnexe(eachInscription, StringUtils.EMPTY, StringUtils.EMPTY, compteAnnexe)) {
                    inscriptionParitairePersonelSucces = false;
                }
            }
            return inscriptionParitairePersonelSucces;
        } else if (manager.getSize() == 1) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
            return majCompteAnnexe(eachInscription, StringUtils.EMPTY, StringUtils.EMPTY, compteAnnexe);
        } else {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UNIQUE_NUMERO_ADHERENT_FAILED"), numeroAdherent);
            LOG.warn(erreurInterne);
            eachInscription.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n");
            return false;
        }

    }

    /**
     * Mise � jour du compte annexe lors d'une inscription directe.
     *
     * @param inscriptionEBill l'inscription eBill � traiter.
     * @param numeroAdherent   le num�ro d'adh�rent (id eBill) li� � l'inscription.
     * @return vrai si la mise � jour du compte annexe est en succ�s.
     */
    private boolean updateCompteAnnexeTitulariseCasInscriptionDirect(CAInscriptionEBill inscriptionEBill,
                                                                     final String numeroAdherent) {
        if (StringUtils.isNotEmpty(inscriptionEBill.getNumRefBVR())) {
            final String idRole = inscriptionEBill.getNumRefBVR().substring(0, 2);

            // R�cup�ration du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());

            if (StringUtils.equals(idRole, AbstractReference.IDENTIFIANT_REF_IDCOMPTEANNEXE)) {
                try {
                    final String idCompteAnnexe = formatterNumeroDepuisRefBVR(inscriptionEBill);
                    manager.setForIdCompteAnnexeIn(idCompteAnnexe);
                    manager.find(BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_RETRIEVE_ID_COMPTE_FAILED"), inscriptionEBill.getNumRefBVR());
                    LOG.error(erreurInterne, e);
                    inscriptionEBill.setTexteErreurInterne(erreurInterne);
                    error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                    return false;
                }
            } else {

                // Formatage du num�ro d'affili�
                String numeroAffilieFormate;
                try {
                    final String numeroAffilie = formatterNumeroDepuisRefBVR(inscriptionEBill);
                    CAApplication application = (CAApplication) GlobazServer.getCurrentSystem().getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
                    IFormatData affilieFormater = application.getAffileFormater();
                    numeroAffilieFormate = affilieFormater.format(numeroAffilie);
                } catch (Exception e) {
                    String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_FORMAT_ID_AFFILIE_FAILED"), inscriptionEBill.getNumRefBVR());
                    LOG.error(erreurInterne, e);
                    inscriptionEBill.setTexteErreurInterne(erreurInterne);
                    error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                    return false;
                }

                manager.setForIdExterneRole(numeroAffilieFormate);
                // On r�cup�re l'id r�le � partir du num�ro BVR. Cas d'une inscription directe on s'appuie sur le num�ro BVR pour r�cup�rer le compte annexe.
                StringBuilder idRoleComplet = new StringBuilder("5170").append(idRole);
                manager.setForIdRole(idRoleComplet.toString());
                try {
                    manager.find(BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_RETRIEVE_ID_AFFILIE_FAILED"), numeroAffilieFormate);
                    LOG.error(erreurInterne, e);
                    inscriptionEBill.setTexteErreurInterne(erreurInterne);
                    error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                    return false;
                }
            }

            if (manager.getSize() == 1) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
                return majCompteAnnexe(inscriptionEBill, numeroAdherent, inscriptionEBill.getEmail(), compteAnnexe);
            } else {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UNIQUE_REF_BVR_FAILED"), inscriptionEBill.getNumRefBVR());
                LOG.warn(erreurInterne);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n");
                return false;
            }
        }

        String erreurInterne = getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_REF_BVR_MISSING_FAILED");
        LOG.warn(erreurInterne);
        inscriptionEBill.setTexteErreurInterne(erreurInterne);
        error.append(erreurInterne).

                append("\n");
        return false;
    }


    /**
     * M�thode permettant de formatter le num�ro depuis la r�f�rence BVR : on doit supprimer les 0 devant la premi�re valeur.
     *
     * @param inscriptionEBill : le num�ro depuis la ref BVR.
     * @return le num�ro formatt�.
     * @throws CATechnicalException : exception lanc�e si le formattage �choue.
     */
    private String formatterNumeroDepuisRefBVR(CAInscriptionEBill inscriptionEBill) throws CATechnicalException {
        String numero;
        try {
            numero = Long.toString(new Long(inscriptionEBill.getNumRefBVR().substring(3, 15)));
        } catch (Exception e) {
            throw new CATechnicalException(getSession().getLabel("INSCR_EBILL_FORMAT_ID_AFFILIE_PARSE_FAILED"), e);
        }
        return numero;
    }

    /**
     * Mise � jour du compte annexe lors d'une inscription.
     *
     * @param inscriptionEBill l'inscription eBill � traiter.
     * @param numeroAdherent   le num�ro d'adh�rent (id eBill) li� � l'inscription.
     * @return vrai si la mise � jour du compte annexe est en succ�s.
     */
    private boolean updateCompteAnnexeTitulariseCasInscription(CAInscriptionEBill inscriptionEBill,
                                                               final String numeroAdherent) {
        String numeroAffilie;
        numeroAffilie = inscriptionEBill.getNumeroAffilie();
        if (StringUtils.isNotEmpty(numeroAffilie)) {

            // R�cup�ration du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            manager.setForIdExterneRole(numeroAffilie);
            if (isPlusieursTypeAffilie) {
                if (inscriptionEBill.getRoleParitaire() && inscriptionEBill.getRolePersonnel()) {
                    Set<String> idsRole = new HashSet();
                    idsRole.add(IntRole.ROLE_AFFILIE_PARITAIRE);
                    idsRole.add(IntRole.ROLE_AFFILIE_PERSONNEL);
                    manager.setForIdRoleIn(Joiner.on(",").join(idsRole));
                } else if (inscriptionEBill.getRoleParitaire()) {
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
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_RETRIEVE_ID_AFFILIE_FAILED"), numeroAffilie);
                LOG.error(erreurInterne, e);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                return false;
            }

            if (inscriptionEBill.getRoleParitaire() && inscriptionEBill.getRolePersonnel() && manager.getSize() == 2) {
                boolean inscriptionParitairePersonelSucces = true;
                for (int i = 0; i < manager.getSize(); i++) {
                    CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(i);
                    if (!majCompteAnnexe(inscriptionEBill, numeroAdherent, inscriptionEBill.getEmail(), compteAnnexe)) {
                        inscriptionParitairePersonelSucces = false;
                    }
                }
                return inscriptionParitairePersonelSucces;
            } else if (manager.getSize() == 1) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
                return majCompteAnnexe(inscriptionEBill, numeroAdherent, inscriptionEBill.getEmail(), compteAnnexe);
            } else {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UNIQUE_ID_AFFILIE_FAILED"), numeroAffilie);
                LOG.warn(erreurInterne);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n");
                return false;
            }
        }
        LOG.warn("le num�ro d'affili� n'est pas renseign�.");
        return false;
    }

    /**
     * Mise � jour du compte annexe.
     *
     * @param numeroAdherent : le num�ro d'adh�rent (id eBill) de l'inscription --> vide dans le cas d'une r�siliation.
     * @param email          : le mail li� � l'inscription --> vide dans le cadre d'une r�siliation.
     * @param compteAnnexe   : le compte annexe � mettre � jour.
     * @return vrai si l'update s'est bien pass�.
     */
    private boolean majCompteAnnexe(CAInscriptionEBill inscriptionEBill, final String numeroAdherent,
                                    final String email,
                                    final CACompteAnnexe compteAnnexe) {
        compteAnnexe.seteBillAccountID(numeroAdherent);
        compteAnnexe.seteBillMail(email);
        try {
            compteAnnexe.update();
        } catch (Exception e) {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UPDATE_ID_COMPTE_ANNEXE_NUM_ADHERENT"), compteAnnexe.getIdCompteAnnexe(), numeroAdherent);
            LOG.error(erreurInterne, e);
            inscriptionEBill.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        if(JadeStringUtil.isEmpty(email)) {
            String erreurInterne = String.format(getSession().getLabel(COMPTE_ANNEXE_EMAIL_MANQUANTE), compteAnnexe.getIdCompteAnnexe(), numeroAdherent);
            LOG.error(erreurInterne);
            inscriptionEBill.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n");
            return false;
        }
        try {
            EBillMail.sendMailConfirmation(email, compteAnnexe.getTiers().getLangueISO());
        } catch (Exception e) {
            String erreurInterne = String.format(getSession().getLabel(COMPTE_ANNEXE_EMAIL_FAILED), compteAnnexe.getIdExterneRole(), numeroAdherent);
            LOG.error(erreurInterne, e);
            inscriptionEBill.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return true;
    }

    /**
     * Envoi le r�sultat du traitement par mail.
     *
     * @param mailContent : le contenu du mail envoy�.
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
     * Retourne le rapport du traitement � afficher dans l'email.
     *
     * @return le contenu du mail
     */
    private String getEmailContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_CONTENT), inscriptionOK, inscriptionKO, resiliationOK, resiliationKO);
    }

    /**
     * Retourne le rapport d'erreur si un fichier n'a pas pu �tre sauvegard�.
     *
     * @return le contenu "erreur" du mail.
     */
    private String getEmailErrorContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_ERROR_CONTENT), error);
    }

    /**
     * Retourne les adresses mails � laquelle le rapport doit �tre envoy�.
     *
     * @return l'email renseign� dans la propri�t� JADE osiris.eBill.email.traitements.
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
