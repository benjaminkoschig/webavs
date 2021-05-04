package globaz.osiris.process;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import com.google.common.base.Throwables;
import com.jcraft.jsch.SftpException;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
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
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.ebill.CAFichierTraitementEBill;
import globaz.osiris.db.ebill.CATraitementEBill;
import globaz.osiris.db.ebill.enums.CAFichierTraitementStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CATraitementCodeErreurEBillEnum;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osiris.ch.ebill.response.protocol.ProtocolBillType;
import osiris.ch.ebill.response.protocol.ProtocolEnvelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CRON permettant le traitement des fichiers de traitement eBill.
 */
public class CAProcessImportTraitementEBill extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CAProcessImportTraitementEBill.class);
    private static final String MAIL_CONTENT = "EBILL_MAIL_TRAITEMENT_CONTENT";
    private static final String MAIL_ERROR_CONTENT = "EBILL_MAIL_TRAITEMENT_ERROR_CONTENT";
    private static final String MAIL_SUBJECT = "EBILL_MAIL_TRAITEMENT_SUBJECT";
    private static final String XML_EXTENSION = ".xml";
    private final StringBuilder error = new StringBuilder();
    List<String> filesToSend = new ArrayList<>();
    private EBillSftpProcessor serviceFtp;

    private int nbElements = 0;
    private int nbElementsTraites = 0;
    private int nbElementsRejetes = 0;
    private int nbElementsEnErreurs = 0;

    private int traitementOK = 0;
    private int traitementKO = 0;

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
            LOG.info("Lancement du process d'importation des traitements e-Bill.");
            //Pas d'envoi de mail automatique du process, tout est g�r� manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

            initBsession();
            initServiceFtp();

            boolean isActive = CAApplication.getApplicationOsiris().getCAParametres().isEbill(getSession());

            if (isActive) {
                importFiles();
                generationProtocol();
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de l'execution du processus d'importation des traitements : ", e);
            error.append("Impossible d'ex�cuter le processus d'importation des traitements : ").append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
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
     * R�cup�ration et traitement des fichiers de traitement eBill
     */
    private void importFiles() {
        try {
            LOG.info("Importation des fichiers de traitement...");

            // Nous recherchons tous les fichiers de traitements d�pos�s sur le serveur FTP PostFinance
            List<String> files = serviceFtp.getListFiles(CAProcessImportTraitementEBill.XML_EXTENSION);

            for (final String nomFichierDistant : files) {
                importFile(nomFichierDistant);
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de l'importation des fichiers de traitement.", e);
            error.append("Impossible de proc�der � l'importation des fichiers de traitement : ").append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
        }
    }

    /**
     * R�cup�ration et traitement des donn�es dans le fichier de traitement eBill
     *
     * @param nomFichierDistant : le nom du fichier � traiter.
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     * @throws JadeServiceLocatorException
     */
    private void importFile(String nomFichierDistant) throws PropertiesException {
        // Enregistrement des donn�es de traitement du fichier
        CAFichierTraitementEBill fichier = saveFichierTraitement(nomFichierDistant);
        // Si le fichier n'a pas pu �tre enregistr� en BDD, on ne le traite et le probl�me sera remont� dans le rapport par mail.
        if (Objects.nonNull(fichier)) {

            String localPath = Jade.getInstance().getPersistenceDir() + serviceFtp.getFolderInName() + nomFichierDistant;
            File localFile = new File(localPath);
            try {

                // Download du fichier XML
                try (FileOutputStream retrievedFile = new FileOutputStream(localFile)) {
                    serviceFtp.retrieveFile(nomFichierDistant, retrievedFile);
                    serviceFtp.deleteFile(nomFichierDistant);
                }

                // Traitement du fichier XML
                try (BufferedReader reader = new BufferedReader(new FileReader(localFile))) {
                    List<CATraitementEBill> allTraitements = extractDataFromFile(reader);

                    // Enregistre tous les traitements en BD et met � jour le status du fichier de traitement
                    boolean tousLesTraitementsEnSucces = saveTraitements(allTraitements, fichier);
                    updateTraitementFileStatut(fichier, tousLesTraitementsEnSucces, localFile);
                }

            } catch (JAXBException e) {
                LOG.error("Une erreur s'est produite lors du mapping du fichier � traiter : " + nomFichierDistant, e);
                error.append("Impossible de proc�der au mapping du fichier � traiter : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
                filesToSend.add(localFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                LOG.error("Une erreur s'est produite lors de la r�cup�ration du fichier � traiter : " + nomFichierDistant, e);
                error.append("Impossible de r�cup�rer le fichier � traiter : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            } catch (IOException e) {
                LOG.error("Une erreur s'est produite lors de la lecture du fichier � traiter : " + nomFichierDistant, e);
                error.append("Impossible de lire le fichier � traiter : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            } catch (SftpException e) {
                LOG.error("Une erreur s'est produite lors du t�l�chargement du fichier � traiter : " + nomFichierDistant, e);
                error.append("Impossible de t�l�charger le fichier � traiter : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            }
        }
    }

    /**
     * Mise � jour du statut du fichier suite au traitements.
     *
     * @param fichier                    : le fichier � mettre � jour.
     * @param tousLesTraitementsEnSucces : bool�en qui indique si tous les traitements on trouv� et mis � jour une section par le bias de l'id de transaction
     */
    private void updateTraitementFileStatut(CAFichierTraitementEBill fichier, boolean tousLesTraitementsEnSucces, File localFile) {
        String nomFichier = fichier.getNomFichier();
        if (tousLesTraitementsEnSucces) {
            fichier.setStatutFichier(String.valueOf(CAFichierTraitementStatutEBillEnum.TRAITE.getIndex()));
        } else {
            fichier.setStatutFichier(String.valueOf(CAFichierTraitementStatutEBillEnum.A_TRAITE.getIndex()));
            filesToSend.add(localFile.getAbsolutePath()); // Seul les fichiers � traiter sont ajout� dans l'email en fin de processus
        }
        fichier.setNbElements(String.valueOf(nbElements));
        fichier.setNbElementsTraites(String.valueOf(nbElementsTraites));
        fichier.setNbElementsEnErreurs(String.valueOf(nbElementsEnErreurs));
        fichier.setNbElementsRejetes(String.valueOf(nbElementsRejetes));
        try {
            fichier.update(getTransaction());
        } catch (Exception e) {
            LOG.error("Erreur lors de la mise � jour du fichier : " + nomFichier, e);
            error.append("Impossible de mettre � jour le fichier : ").append(nomFichier).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
        }
    }

    /**
     * Sauvegarde du traitement.
     *
     * @param fichier        : le fichier li� au traitement.
     * @param eachTraitement : le traitement � sauvegarder.
     * @param updatedSection : bool�en qui indique si la mise � jour de la secion du compte annexe s'est bien faite.
     * @return vrai si la sauvegarde est en succ�s.
     */
    private boolean saveTraitement(CAFichierTraitementEBill fichier, CATraitementEBill eachTraitement, boolean updatedSection) {
         try {
            eachTraitement.setSession(getSession());
            eachTraitement.setIdFichier(fichier.getIdFichier());
            if (updatedSection) {
                eachTraitement.setStatut(CAStatutEBillEnum.NUMERO_STATUT_TRAITE_AUTOMATIQUEMENT);
            } else {
                eachTraitement.setStatut(CAStatutEBillEnum.NUMERO_STATUT_A_TRAITER);
            }
            eachTraitement.add(getTransaction());
        } catch (Exception e) {
            LOG.error("Erreur lors de la sauvegarde du traitement.", e);
            error.append("Impossible d'enregistrer en base de donn�es le traitement suivant :")
                    .append(" ").append("numeroAffilie : ").append(eachTraitement.getNumeroAffilie())
                    .append(" ").append("eBillAccountID : ").append(eachTraitement.geteBillAccountID())
                    .append(" ").append("transactionID : ").append(eachTraitement.getTransactionID())
                    .append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return false;
        }
        return true;
    }


    /**
     * Sauvegarde les informations du fichier trait� en BDD.
     *
     * @param nomFichierDistant le nom du fichier trait�.
     * @return le fichier sauvegard� en BDD au statut non trait�.
     */
    private CAFichierTraitementEBill saveFichierTraitement(String nomFichierDistant) {
        CAFichierTraitementEBill fichier = new CAFichierTraitementEBill();
        String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
        String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
        fichier.setNomFichier(nameOriginalFileWithoutExt);
        fichier.setDateLecture(JACalendar.todayJJsMMsAAAA());
        fichier.setStatutFichier(String.valueOf(CAFichierTraitementStatutEBillEnum.A_TRAITE.getIndex()));
        fichier.setSession(getSession());
        try {
            fichier.add(getTransaction());
        } catch (Exception e) {
            LOG.error("Erreur lors de la sauvegarder du fichier en base de donn�es : " + nomFichierDistant, e);
            error.append("Impossible de sauvegarder en base de donn�es le fichier : ").append(nomFichierDistant).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return null;
        }
        return fichier;
    }

    /**
     * M�thode permettant de unmarshall le fichier xml de traitement avec le XSD eBill.
     *
     * @param bufferedReader : le fichier � lire
     * @return l'inscription li� � la ligne.
     */
    private List<CATraitementEBill> extractDataFromFile(BufferedReader bufferedReader) throws JAXBException {
        List<CATraitementEBill> allTraitements = new ArrayList<CATraitementEBill>();

        JAXBContext jc = JAXBContext.newInstance(ProtocolEnvelope.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ProtocolEnvelope protocolEnvelope = (ProtocolEnvelope) unmarshaller.unmarshal(bufferedReader);

        //Map les traitements rejet�s
        for (ProtocolBillType protocolBillType : protocolEnvelope.getBody().getRejectedBills().getBill()){
            CATraitementEBill traitementEBill = new CATraitementEBill();
            traitementEBill.setEtat(CATraitementEtatEBillEnum.REJECTED_OR_PENDING.getNumeroEtat());
            allTraitements.add(mappingTraitement(traitementEBill, protocolBillType));
            nbElementsRejetes++;
        }

        for (ProtocolEnvelope.Body.DeliveryDate deliveryDate : protocolEnvelope.getBody().getDeliveryDate()) {
            //Map les traitements en erreurs
            for (ProtocolBillType protocolBillType : deliveryDate.getNOKResult().getBill()) {
                CATraitementEBill traitementEBill = new CATraitementEBill();
                traitementEBill.setEtat(CATraitementEtatEBillEnum.EN_ERREUR.getNumeroEtat());
                allTraitements.add(mappingTraitement(traitementEBill, protocolBillType));
                nbElementsEnErreurs++;
            }
            //Map les traitements en succ�s
            for (ProtocolEnvelope.Body.DeliveryDate.OKResult.Bill protocolBillType : deliveryDate.getOKResult().getBill()) {
                CATraitementEBill traitementEBill = new CATraitementEBill();
                traitementEBill.setEtat(CATraitementEtatEBillEnum.TRAITE.getNumeroEtat());
                traitementEBill.setTransactionID(protocolBillType.getTransactionID());
                allTraitements.add(traitementEBill);
                nbElementsTraites++;
            }
        }

        nbElements = allTraitements.size();
        return allTraitements;
    }

    private CATraitementEBill mappingTraitement(CATraitementEBill traitementEBill, ProtocolBillType protocolBillType) {
        traitementEBill.setTransactionID(protocolBillType.getTransactionID());
        traitementEBill.seteBillAccountID(protocolBillType.getEBillAccountID());
        traitementEBill.setNumRefBVR(protocolBillType.getESRReference());
        traitementEBill.setMontantTotal(protocolBillType.getTotalAmount());
        String codeErreurSansPrefixe = StringUtils.stripStart(protocolBillType.getReasonCode(),"0");
        CATraitementCodeErreurEBillEnum codeErreurEBill = CATraitementCodeErreurEBillEnum.parValeur(codeErreurSansPrefixe);
        traitementEBill.setCodeErreur(codeErreurEBill != null ? codeErreurEBill.getNumeroCodeErreur() : codeErreurSansPrefixe);
        traitementEBill.setTexteErreur(protocolBillType.getReasonText());
        traitementEBill.setDateTraitement(protocolBillType.getDate());
        return traitementEBill;
    }

    /**
     * Sauvegardes des traitements eBill.
     *
     * @param allTraitements tous les traitements du fichier.
     * @param fichier         le fichier en cours de traitement.
     * @return vrai si tous les traitements du fichier ont �t� trait�s avec succ�s, faux sinon.
     */
    private boolean saveTraitements(List<CATraitementEBill> allTraitements, CAFichierTraitementEBill fichier) {
        boolean tousLesTraitementsSucces = true;
        for (CATraitementEBill traitementEBill : allTraitements) {

            String transactionId = traitementEBill.getTransactionID();
            if (StringUtils.isNotEmpty(transactionId)) {

                boolean updatedTraitement = false;
                CASection updatedSection = updateAndReturnSection(traitementEBill, transactionId);
                if (updatedSection != null) {
                    CACompteAnnexe compteAnnexe = getCompteAnnexe(traitementEBill, updatedSection.getIdCompteAnnexe());
                    if (compteAnnexe != null) {
                        traitementEBill.seteBillAccountID(compteAnnexe.geteBillAccountID());
                        traitementEBill.setNumeroAffilie(compteAnnexe.getIdExterneRole());
                        traitementEBill.setNom(compteAnnexe.getDescription());
                        updatedTraitement = true;
                    }
                }

                boolean savedTraitement = saveTraitement(fichier, traitementEBill, updatedTraitement);
                if (updatedTraitement && savedTraitement) {
                    traitementOK++;
                } else {
                    traitementKO++;
                    tousLesTraitementsSucces = false;
                }

            } else {
                LOG.warn("L'id de transaction n'est pas renseign�.");
                traitementEBill.setTexteErreurInterne("L'id de transaction n'est pas renseign�.");
                error.append("Impossible de r�cup�rer l'id de transaction.").append("\n");
                tousLesTraitementsSucces = false;
            }

        }
        return tousLesTraitementsSucces;
    }

    private CACompteAnnexe getCompteAnnexe(CATraitementEBill traitementEBill, String idCompteAnnexe) {
        try {
            // R�cup�ration du compte annexe.
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            manager.setForIdCompteAnnexeIn(idCompteAnnexe);
            manager.find(CACompteAnnexeManager.SIZE_NOLIMIT);
            if (manager.getSize() == 1) {
                return (CACompteAnnexe) manager.get(0);
            } else {
                LOG.warn("Un compte annexe unique n'a pas pu �tre trouv� avec l'id de compte annexe : " + idCompteAnnexe );
                traitementEBill.setTexteErreurInterne("Un compte annexe unique n'a pas pu �tre trouv� avec l'id de compte annexe : " + idCompteAnnexe );
                error.append("Impossible de r�cup�rer un compte annexe unique avec l'id de compte annexe : ").append(idCompteAnnexe).append("\n");
                return null;
            }
        } catch (Exception e) {
            LOG.error("Erreur lors de la r�cup�ration du compte annexe avec l'id : "+ idCompteAnnexe, e);
            traitementEBill.setTexteErreurInterne("Erreur lors de la r�cup�ration du compte annexe avec l'id : "+ idCompteAnnexe);
            error.append("Impossible de r�cup�rer le compte annexe avec l'id : ").append(idCompteAnnexe).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return null;
        }
    }

    /**
     * Mise � jour du statut eBill de la section
     * recherche avec un id de transaction et retourne la section.
     *
     * @param traitementEBill le traitement eBill.
     * @param transactionId   l'id de transaction li� au traitement.
     * @return la section si la mise � jour est en succ�s sinon null.
     */
    private CASection updateAndReturnSection(final CATraitementEBill traitementEBill, final String transactionId) {
        try {
            // R�cup�ration de la section
            CASectionManager manager = new CASectionManager();

            manager.setSession(getSession());
            manager.setForEBillTransactionID(transactionId);
            manager.find(globaz.globall.db.BManager.SIZE_NOLIMIT);

            // Met � jour l'�tat eBill de la section et le message d'erreur
            if (manager.size() == 1) {
                CASection section = (CASection) manager.get(0);
                section.seteBillEtat(traitementEBill.getEtat().getNumeroEtat());
                section.seteBillErreur(concatErreurInterneEtExterne(traitementEBill));
                section.update();
                return section;
            } else {
                LOG.warn("Une section unique n'a pas pu �tre trouv� avec l'id de transaction: " + transactionId);
                traitementEBill.setTexteErreurInterne("Une section unique n'a pas pu �tre trouv� avec l'id de transaction: " + transactionId);
                error.append("Impossible de r�cup�rer une section unique avec l'id de transaction : ").append(transactionId).append("\n");
                return null;
            }

         } catch (Exception e) {
            LOG.error("Erreur lors de la r�cup�ration de la section avec l'id de transaction : " + transactionId, e);
            traitementEBill.setTexteErreurInterne("Erreur lors de la r�cup�ration de la section avec l'id de transaction : " + transactionId);
            error.append("Impossible de r�cup�rer la section avec l'id de transaction : ").append(transactionId).append("\n").append(Throwables.getStackTraceAsString(e)).append("\n");
            return null;
        }
    }

    private String concatErreurInterneEtExterne(CATraitementEBill traitementEBill) {
        StringBuilder str = new StringBuilder();
        boolean erreurPresente = false;
        if (traitementEBill.getCodeErreurDescription() != null) {
            str.append(traitementEBill.getCodeErreurDescription());
            erreurPresente = true;
        }
        if (traitementEBill.getTexteErreur() != null) {
            str.append(erreurPresente ? " : " : "").append(traitementEBill.getTexteErreur());
            erreurPresente = true;
        }
        if (traitementEBill.getTexteErreurInterne() != null) {
            str.append(erreurPresente ? " : " : "").append(traitementEBill.getTexteErreurInterne());
        }

        return str.toString();
    }

    /**
     * Envoi le r�sultat du traitement par mail.
     *
     * @param mailContent : le contenu du mail envoy�.
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
     * Retourne le rapport du traitement � afficher dans l'email.
     *
     * @return le contenu du mail
     */
    private String getEmailContent() {
        return FWMessageFormat.format(getSession().getLabel(MAIL_CONTENT), traitementOK, traitementKO);
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
        eMailAddress = eMailAddress.replaceAll("\\s+","");
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
