package globaz.osiris.process;

import ch.globaz.common.document.reference.AbstractReference;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.BooleanUtils;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.jcraft.jsch.SftpException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.ebill.CAFichierInscriptionEBill;
import globaz.osiris.db.ebill.CAInscriptionEBill;
import globaz.osiris.db.ebill.enums.CAFichierInscriptionStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CAInscriptionTypeEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.external.IntRole;
import globaz.osiris.parser.IntReferenceBVRParser;
import globaz.osiris.process.ebill.CAInscriptionEBillEnum;
import globaz.osiris.process.ebill.EBillMail;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
    private static final String SEPARATOR = ";";
    private static final String BOOLEAN_TRUE = "on";
    private final StringBuilder error = new StringBuilder();
    private final List<String> filesToSend = new ArrayList<>();
    private boolean isPlusieursTypeAffilie;
    private int inscriptionOK = 0;
    private int inscriptionKO = 0;
    private int resiliationOK = 0;
    private int resiliationKO = 0;
    private int lenIdExterneRole;
    private int posIdExterneRole;

    @Override
    protected void _executeCleanUp() {
        //Nothing to do
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Lancement du process d'importation des inscriptions e-Bill.");
            //Pas d'envoi de mail automatique du process, tout est g?r? manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

            initBsession();
            EBillSftpProcessor.getInstance();
            initIdReferenceParameter();

            isPlusieursTypeAffilie = Boolean.parseBoolean(CAApplication.getApplicationOsiris().getProperty(CaisseHelperFactory.PLUSIEURS_TYPE_AFFILIE, "false"));
            boolean eBillMuscaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillMuscaActifEtDansListeCaisses(getSession());

            if (eBillMuscaActif) {
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
            EBillSftpProcessor.closeServiceFtp();
        }

        return true;

    }

    /**
     * M?thode permettant de g?n?rer le bilan du traitement et l'envoi du mail r?capitulatif.
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
     * @throws Exception : exception envoy?e si un probl?me intervient lors de l'initialisation de la session.
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
     * Initialisation des param?tres de Id r?f?rence dans le Num?ro de r?f?rence BVR
     */
    private void initIdReferenceParameter() {
        lenIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                IntReferenceBVRParser.LEN_ID_EXTERNE_ROLE));;
        posIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                IntReferenceBVRParser.POS_ID_EXTERNE_ROLE));
    }

    /**
     * R?cup?ration et traitement des fichiers d'inscription eBill
     */
    private void importFiles() {
        try {
            LOG.info("Importation des fichiers d'inscription...");

            // Nous recherchons tous les fichiers d'inscriptions d?pos?s sur le serveur FTP PostFinance
            List<String> files = EBillSftpProcessor.getInstance().getListFiles(CAProcessImportInscriptionEBill.CSV_EXTENSION);

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
     * R?cup?ration et traitement des donn?es dans le fichier d'inscription eBill
     *
     * @param nomFichierDistant : le nom du fichier ? traiter
     */
    private void importFile(String nomFichierDistant) throws PropertiesException {
        // Enregistrement des donn?es de traitement du fichier
        CAFichierInscriptionEBill fichierInscription = saveFichierInscription(nomFichierDistant);
        // Si le fichier n'a pas pu ?tre enregistr? en BDD, on ne le traite pas et le probl?me sera remont? dans le rapport par mail.
        if (Objects.nonNull(fichierInscription)) {

            String localPath = Jade.getInstance().getPersistenceDir() + EBillSftpProcessor.getFolderInName() + nomFichierDistant;
            File localFile = new File(localPath);
            try {

                // Download du fichier CSV
                try (FileOutputStream retrievedFile = new FileOutputStream(localFile)) {
                    EBillSftpProcessor.getInstance().retrieveFile(nomFichierDistant, retrievedFile);
                    EBillSftpProcessor.getInstance().deleteFile(nomFichierDistant);
                }

                // Traitement du fichier CSV
                try (BufferedReader reader = new BufferedReader(new FileReader(localFile))) {
                    boolean toutesInscriptionsEnSucces;
                    String line = reader.readLine();

                    List<CAInscriptionEBill> allInscriptions = new ArrayList<>();
                    if ( CAProperties.EBILL_BASCULE.getBooleanValue()) {
                        //Chargement de l'ordre des colonnes
                        String[] csvColonnes = line.split(SEPARATOR);

                        Map<Integer, CAInscriptionEBillEnum> csvColonneOrder = new HashMap<>();
                        int[] index = {0};

                        Arrays.stream(csvColonnes).forEach(colonne -> {
                            try (CAInscriptionEBillEnum inscription = CAInscriptionEBillEnum.getInscriptionEBill(colonne)) {
                                if (!inscription.isIgnored()) {
                                    csvColonneOrder.put(index[0], inscription);
                                }
                                index[0]++;
                            } catch (Exception e) {
                                //ne peux pas ?tre d?clench? en th?orie
                                LOG.error("Une erreur imprevue est survenu", e);
                            }
                        });

                        while (!StringUtils.isEmpty(line = reader.readLine())) {
                            CAInscriptionEBill inscriptionEBill = extractDataFromFile(line, csvColonneOrder);

                            if (CAProperties.EBILL_BILLER_ID.getValue().equals(inscriptionEBill.getBillerId())) {
                                allInscriptions.add(inscriptionEBill);
                            }

                        }
                        // Enregistre toutes les inscriptions en BD et met ? jour le status du fichier d'inscription
                        toutesInscriptionsEnSucces = saveInscriptions(allInscriptions, fichierInscription);
                    } else {
                        while (!StringUtils.isEmpty(line = reader.readLine())) {
                            allInscriptions.add(extractDataFromFileV1(line));
                        }

                        // Enregistre toutes les inscriptions en BD et met ? jour le status du fichier d'inscription
                        toutesInscriptionsEnSucces = saveInscriptions(allInscriptions, fichierInscription);
                    }


                    updateInscriptionFileStatut(fichierInscription, toutesInscriptionsEnSucces, localFile);
                } catch (IllegalArgumentException impossible) {
                    //ne peux pas ?tre d?clench? en th?orie
                    LOG.error("error get method", impossible);
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
     * Mise ? jour du statut du fichier suite au traitement des inscriptions.
     *
     * @param fichier                    : le fichier ? mettre ? jour.
     * @param toutesInscriptionsEnSucces : bool?en qui indique si la mise ? jour du compte annexe s'est bien faite pour toutes les inscriptions
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
     * Sauvegarde de l'inscription V1.
     *
     * @param fichier         : le fichier li? ? l'inscription.
     * @param eachInscription : l'inscription ? sauvegarder.
     * @param result          : bool?en qui indique si la mise ? jour du compte annexe s'est bien faite.
     * @return vrai si la sauvegarde est en succ?s.
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
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_ENREGISTRE_FAILED"), eachInscription.getNumeroAffilie(), eachInscription.getEBillAccountID());
            LOG.error(erreurInterne, e);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return true;
    }

    /**
     * Sauvegarde les informations du fichier trait? en BDD.
     *
     * @param nomFichierDistant le nom du fichier trait?.
     * @return retourne le fichier sauvegard? en BDD au statut non trait?.
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
     * M?thode permettant de caster les lignes du fichier csv.
     *
     * @param line : la ligne ? caster
     * @return l'inscription li? ? la ligne.
     */
    private CAInscriptionEBill extractDataFromFile(String line, Map<Integer, CAInscriptionEBillEnum> orderColonnes) {
        CAInscriptionEBill inscriptionEBill = new CAInscriptionEBill();

        // Suppression des espaces de fin de ligne
        line = line.trim();

        // Split des donn?es
        String[] datasTemp = line.split(SEPARATOR, -1);

        orderColonnes.forEach((index, col) -> {
            switch (col) {
                case SUBSCRIPTION_TYPE:
                    inscriptionEBill.setType(datasTemp[index]);
                    break;
                case BILLER_ID:
                    inscriptionEBill.setBillerId(datasTemp[index]);
                    break;
                case RECIPIENT_ID:
                    inscriptionEBill.setEBillAccountID(datasTemp[index]);
                    break;
                case RECIPIENT_TYPE:
                    inscriptionEBill.setEBillAccountType(datasTemp[index]);
                    break;
                case GIVEN_NAME:
                    inscriptionEBill.setPrenom(datasTemp[index]);
                    break;
                case FAMILY_NAME:
                    inscriptionEBill.setNom(datasTemp[index]);
                    break;
                case COMPANY_NAME:
                    inscriptionEBill.setEntreprise(datasTemp[index]);
                    break;
                case ADRESSE:
                    inscriptionEBill.setAdresse1(datasTemp[index]);
                    break;
                case ZIP:
                    if (JadeNumericUtil.isNumeric(datasTemp[index])) {
                        inscriptionEBill.setNpa(Integer.parseInt(datasTemp[index]));
                    }
                    break;
                case CITY:
                    inscriptionEBill.setLocalite(datasTemp[index]);
                    break;
                case COUNTRY:
                    inscriptionEBill.setPays(datasTemp[index]);
                    break;
                case EMAIL:
                    inscriptionEBill.setEmail(datasTemp[index]);
                    break;
                case CREDIT_ACCOUNT:
                    inscriptionEBill.setNumAdherentBVR(datasTemp[index]);
                    break;
                case CREDITOR_REFERENCE:
                    inscriptionEBill.setNumRefBVR(datasTemp[index]);
                    break;
                case CUSTOMER_NBR:
                    inscriptionEBill.setNumeroAffilie(datasTemp[index]);
                    break;
                case PHONE:
                    inscriptionEBill.setNumTel(datasTemp[index]);
                    break;
                case PARITAIRE:
                    inscriptionEBill.setRoleParitaire(BooleanUtils.translateBoolean(datasTemp[index]));
                    break;
                case PERSONNEL:
                    inscriptionEBill.setRolePersonnel(BooleanUtils.translateBoolean(datasTemp[index]));
                    break;
            }
        });
        return inscriptionEBill;
    }

    //TODO a supprim? apr?s l'activation de la version 2
    /**
     * M?thode permettant de caster les lignes du fichier csv V1.
     *
     * @param line : la ligne ? caster
     * @return l'inscription li? ? la ligne.
     */
    private CAInscriptionEBill extractDataFromFileV1(String line) {
        CAInscriptionEBill inscriptionEBill = new CAInscriptionEBill();

        // Suppression des espaces de fin de ligne
        line = line.trim();

        // Split des donn?es
        String[] datasTemp = line.split(SEPARATOR);

        // on d?marre ? 1 pour ne pas caster la premi?re ligne.
        for (int i = 0; i < datasTemp.length; i++) {
            CAInscriptionEBillEnum enumInscriptionEBill = CAInscriptionEBillEnum.fromIndex(i);
            if (Objects.nonNull(enumInscriptionEBill)) {
                switch (enumInscriptionEBill) {
                    case RECIPIENT_ID:
                        inscriptionEBill.setEBillAccountID(datasTemp[i]);
                        break;
                    case GIVEN_NAME:
                        inscriptionEBill.setPrenom(datasTemp[i]);
                        break;
                    case FAMILY_NAME:
                        inscriptionEBill.setNom(datasTemp[i]);
                        break;
                    case COMPANY_NAME:
                        inscriptionEBill.setEntreprise(datasTemp[i]);
                        break;
                    case ADRESSE:
                        inscriptionEBill.setAdresse1(datasTemp[i]);
                        break;
                    case ADRESSE_2:
                        inscriptionEBill.setAdresse2(datasTemp[i]);
                        break;
                    case ZIP:
                        if (JadeNumericUtil.isNumeric(datasTemp[i])) {
                            inscriptionEBill.setNpa(Integer.parseInt(datasTemp[i]));
                        }
                        break;
                    case CITY:
                        inscriptionEBill.setLocalite(datasTemp[i]);
                        break;
                    case PHONE:
                        inscriptionEBill.setNumTel(datasTemp[i]);
                        break;
                    case EMAIL:
                        inscriptionEBill.setEmail(datasTemp[i]);
                        break;
                    case CUSTOMER_NBR:
                        inscriptionEBill.setNumeroAffilie(datasTemp[i]);
                        break;
                    case PARITAIRE:
                        inscriptionEBill.setRoleParitaire(StringUtils.equals(BOOLEAN_TRUE, datasTemp[i]));
                        break;
                    case PERSONNEL:
                        inscriptionEBill.setRolePersonnel(StringUtils.equals(BOOLEAN_TRUE, datasTemp[i]));
                        break;
                    case CREDIT_ACCOUNT:
                        inscriptionEBill.setNumAdherentBVR(datasTemp[i]);
                        break;
                    case CREDITOR_REFERENCE:
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
     * Sauvegardes des inscriptions eBill V1.
     *
     * @param allInscriptions toutes les inscriptions du fichier.
     * @param fichier         le fichier en cours de traitement.
     * @return vrai si toutes les inscriptions du fichier ont ?t? trait?s avec succ?s, faux sinon.
     */
    private boolean saveInscriptions(List<CAInscriptionEBill> allInscriptions, CAFichierInscriptionEBill fichier) {
        boolean toutesInscriptionsSucces = true;
        for (CAInscriptionEBill eachInscription : allInscriptions) {
            final CAInscriptionTypeEBillEnum typeEBillEnum = eachInscription.getType();

            boolean result;
            boolean resultInscription;
            if (StringUtils.isNotEmpty(eachInscription.getEBillAccountID())) {
                switch (typeEBillEnum) {
                    case INSCRIPTION:
                        result = updateCompteAnnexeTitulariseCasInscription(eachInscription);
                        resultInscription = saveInscription(fichier, eachInscription, result);
                        if (result && resultInscription) {
                            inscriptionOK++;
                        } else {
                            inscriptionKO++;
                            toutesInscriptionsSucces = false;
                        }
                        break;
                    case INSCRIPTION_DIRECTE:
                        result = updateCompteAnnexeTitulariseCasInscriptionDirect(eachInscription);
                        resultInscription = saveInscription(fichier, eachInscription, result);
                        if (result && resultInscription) {
                            inscriptionOK++;
                        } else {
                            inscriptionKO++;
                            toutesInscriptionsSucces = false;
                        }
                        break;
                    case RESILIATION:
                        result = updateCompteAnnexeTitulariseCasResiliation(eachInscription);
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
     * Mise ? jour du compte annexe lors d'une r?siliation V1.
     *
     * @param eachInscription l'inscription eBill ? traiter.
     * @return vrai si la mise ? jour du compte annexe est en succ?s.
     */
    private boolean updateCompteAnnexeTitulariseCasResiliation(CAInscriptionEBill eachInscription) {
        // R?cup?ration du compte annexe.
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForEBillAccountID(eachInscription.getEBillAccountID());

        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_RETRIEVE_NUMERO_ADHERENT_FAILED"), eachInscription.getEBillAccountID());
            LOG.error(erreurInterne, e);
            eachInscription.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }

        if (manager.getSize() > 0) {
            boolean inscriptionParitairePersonelSucces = true;
            for (int i = 0; i < manager.getSize(); i++) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(i);
                if (!majCompteAnnexe(eachInscription, compteAnnexe)) {
                    inscriptionParitairePersonelSucces = false;
                }
            }
            return inscriptionParitairePersonelSucces;
        } else {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UNIQUE_NUMERO_ADHERENT_FAILED"), eachInscription.getEBillAccountID());
            LOG.warn(erreurInterne);
            eachInscription.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n");
            return false;
        }

    }

    /**
     * Mise ? jour du compte annexe lors d'une inscription directe V1.
     *
     * @param inscriptionEBill l'inscription eBill ? traiter.
     * @return vrai si la mise ? jour du compte annexe est en succ?s.
     */
    private boolean updateCompteAnnexeTitulariseCasInscriptionDirect(CAInscriptionEBill inscriptionEBill) {
        if (StringUtils.isNotEmpty(inscriptionEBill.getNumRefBVR())) {
            String idRole = inscriptionEBill.getNumRefBVR().substring(0, 2);

            // R?cup?ration du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());

            if (StringUtils.equals(idRole, AbstractReference.IDENTIFIANT_REF_IDCOMPTEANNEXE)) {
                try {
                    final String idCompteAnnexe = extractReferenceDepuisReferenceBVR(inscriptionEBill, idRole);
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
                // Formatage du id role
                idRole = "5170" + idRole;

                // Formatage du num?ro d'affili?
                String numeroAffilieFormate;
                try {
                    final String numeroAffilie = extractReferenceDepuisReferenceBVR(inscriptionEBill, idRole);
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
                // On r?cup?re l'id r?le ? partir du num?ro BVR. Cas d'une inscription directe on s'appuie sur le num?ro BVR pour r?cup?rer le compte annexe.
                manager.setForIdRole(idRole);
                if (executeFindManager(inscriptionEBill, manager, numeroAffilieFormate)) return false;
            }

            if (manager.getSize() == 1) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
                return majCompteAnnexe(inscriptionEBill, compteAnnexe);
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

    private boolean executeFindManager(CAInscriptionEBill inscriptionEBill, CACompteAnnexeManager manager, String numeroAffilieFormate) {
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_RETRIEVE_ID_AFFILIE_FAILED"), numeroAffilieFormate);
            LOG.error(erreurInterne, e);
            inscriptionEBill.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return true;
        }
        return false;
    }

    /**
     * M?thode permettant de formatter le num?ro depuis la r?f?rence BVR : on doit supprimer les 0 devant la premi?re valeur.
     *
     * @param inscriptionEBill : le num?ro de r?f?rence depuis la r?f?rence BVR.
     * @return Le id r?f?rence extraite.
     */
    private String extractReferenceDepuisReferenceBVR(CAInscriptionEBill inscriptionEBill, String idRole) {
        String idExterneRole = Long.toString(Long.parseLong(inscriptionEBill.getNumRefBVR().substring(posIdExterneRole - 1, posIdExterneRole + lenIdExterneRole - 1)));
        if (idRole.equals(IntRole.ROLE_AFFILIE_PERSONNEL)
                || idRole.equals(IntRole.ROLE_AFFILIE_PARITAIRE)
                || idRole.equals(IntRole.ROLE_AFFILIE)) {
            String format = CAApplication.getApplicationOsiris().getCAParametres().getFormatAdminNumAffilie();
            if (!JadeStringUtil.isBlank(format)) {
                format = JadeStringUtil.removeChar(format, '.');
                format = JadeStringUtil.removeChar(format, '-');
                if (idExterneRole.length() < format.length()) {
                    idExterneRole = JadeStringUtil.fillWithZeroes(idExterneRole, format.length());

                }
            }
        }

        return idExterneRole;
    }

    /**
     * Mise ? jour du compte annexe lors d'une inscription V1.
     *
     * @param inscriptionEBill l'inscription eBill ? traiter.
     * @return vrai si la mise ? jour du compte annexe est en succ?s.
     */
    private boolean updateCompteAnnexeTitulariseCasInscription(CAInscriptionEBill inscriptionEBill) {
        String numeroAffilie = inscriptionEBill.getNumeroAffilie();
        if (StringUtils.isNotEmpty(numeroAffilie)) {
            // R?cup?ration du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            manager.setForIdExterneRole(numeroAffilie);
            if (isPlusieursTypeAffilie) {
                if (inscriptionEBill.getRoleParitaire() && inscriptionEBill.getRolePersonnel()) {
                    HashSet<String> idsRole = new HashSet<>();
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

            if (executeFindManager(inscriptionEBill, manager, numeroAffilie)) return false;

            if (inscriptionEBill.getRoleParitaire() && inscriptionEBill.getRolePersonnel() && manager.getSize() == 2) {
                boolean inscriptionParitairePersonelSucces = true;
                for (int i = 0; i < manager.getSize(); i++) {
                    CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(i);
                    if (!majCompteAnnexe(inscriptionEBill, compteAnnexe)) {
                        inscriptionParitairePersonelSucces = false;
                    }
                }
                return inscriptionParitairePersonelSucces;
            } else if (manager.getSize() == 1) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(0);
                return majCompteAnnexe(inscriptionEBill, compteAnnexe);
            } else {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UNIQUE_ID_AFFILIE_FAILED"), numeroAffilie);
                LOG.warn(erreurInterne);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n");
                return false;
            }
        }
        LOG.warn("le num?ro d'affili? n'est pas renseign?.");
        return false;
    }

    /**
     * Mise ? jour du compte annexe pour une inscription.
     *
     * @param compteAnnexe   : le compte annexe ? mettre ? jour.
     * @return vrai si l'update s'est bien pass?.
     */
    private boolean majCompteAnnexe(CAInscriptionEBill inscriptionEBill, final CACompteAnnexe compteAnnexe) {
        if (inscriptionEBill.getType().estResiliation()) {
            compteAnnexe.setEBillAccountID(null);
            compteAnnexe.setEBillMail(null);
            compteAnnexe.setEBillDateInscription(null);
        } else {
            if (!StringUtils.isEmpty(compteAnnexe.getEBillAccountID())) {
                String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_ALREADY_REGISTERED"), compteAnnexe.getIdCompteAnnexe(), inscriptionEBill.getEBillAccountID());
                LOG.error(erreurInterne);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n");
                return false;
            }

            compteAnnexe.setEBillAccountID(inscriptionEBill.getEBillAccountID());
            compteAnnexe.setEBillMail(inscriptionEBill.getEmail());
            compteAnnexe.setEBillDateInscription(JadeDateUtil.getGlobazFormattedDate(new Date()));
        }

        try {
            compteAnnexe.update();
        } catch (Exception e) {
            String erreurInterne = String.format(getSession().getLabel("INSCR_EBILL_COMPTE_ANNEXE_UPDATE_ID_COMPTE_ANNEXE_NUM_ADHERENT"), compteAnnexe.getIdCompteAnnexe(), inscriptionEBill.getEBillAccountID());
            LOG.error(erreurInterne, e);
            inscriptionEBill.setTexteErreurInterne(erreurInterne);
            error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return sendMail(inscriptionEBill, compteAnnexe);
    }

    private boolean sendMail(CAInscriptionEBill inscriptionEBill, CACompteAnnexe compteAnnexe) {
        // envoie un mail pour les inscriptions seulement
        if(!inscriptionEBill.getType().estResiliation()) {
            if (JadeStringUtil.isEmpty(inscriptionEBill.getEmail()) || JadeStringUtil.isEmpty(inscriptionEBill.getEBillAccountID())) {
                String erreurInterne = String.format(getSession().getLabel(COMPTE_ANNEXE_EMAIL_MANQUANTE), compteAnnexe.getIdCompteAnnexe(), inscriptionEBill.getEBillAccountID());
                LOG.error(erreurInterne);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n");
                return false;
            }
            try {
                EBillMail.sendMailConfirmation(inscriptionEBill.getEmail(), compteAnnexe.getTiers().getLangueISO());
            } catch (Exception e) {
                String erreurInterne = String.format(getSession().getLabel(COMPTE_ANNEXE_EMAIL_FAILED), compteAnnexe.getIdExterneRole(), inscriptionEBill.getEBillAccountID());
                LOG.error(erreurInterne, e);
                inscriptionEBill.setTexteErreurInterne(erreurInterne);
                error.append(erreurInterne).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                return false;
            }
        }
        return true;
    }

    /**
     * Envoi le r?sultat du traitement par mail.
     *
     * @param mailContent : le contenu du mail envoy?.
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
     * Retourne le rapport du traitement ? afficher dans l'email.
     *
     * @return le contenu du mail
     */
    private String getEmailContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_CONTENT), inscriptionOK, inscriptionKO, resiliationOK, resiliationKO);
    }

    /**
     * Retourne le rapport d'erreur si un fichier n'a pas pu ?tre sauvegard?.
     *
     * @return le contenu "erreur" du mail.
     */
    private String getEmailErrorContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_ERROR_CONTENT), error);
    }

    /**
     * Retourne les adresses mails ? laquelle le rapport doit ?tre envoy?.
     *
     * @return l'email renseign? dans la propri?t? JADE osiris.eBill.email.traitements.
     */
    private String[] getEMailAddressesEBill() {
        String eMailAddress = JadePropertiesService.getInstance().getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_EMAILS);
        eMailAddress = eMailAddress.replaceAll("\\s+", "");
        String[] eMailAddresses = new String[1];
        if (eMailAddress.length() == 0 && getSession() != null) {
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
