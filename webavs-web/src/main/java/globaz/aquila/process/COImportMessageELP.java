package globaz.aquila.process;

import aquila.ch.eschkg.Document;
import aquila.ch.eschkg.RcType;
import aquila.ch.eschkg.ScType;
import aquila.ch.eschkg.SpType;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.daemon.suividecompte.AbstractDaemon;
import com.google.common.base.Throwables;
import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COProperties;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.db.access.batch.transition.CO040SaisirCDP;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxManager;
import globaz.aquila.helpers.batch.process.COProcessEffectuerTransition;
import globaz.aquila.print.list.COResultELPExcel;
import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COTypeELPFactory;
import globaz.aquila.process.elp.COInfoFileELP;
import globaz.aquila.process.elp.COProtocoleELP;
import globaz.aquila.process.elp.COScElpDto;
import globaz.aquila.process.exception.ElpProcessException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class COImportMessageELP extends AbstractDaemon {

    private static final Logger LOG = LoggerFactory.getLogger(COImportMessageELP.class);

    private static final String ELP_SCHEMA = "aquila.ch.eschkg";
    private static final String MAIL_SUBJECT = "ELP_MAIL_SUBJECT";
    private static final String BACKUP_FOLDER = "/backup/";
    private static final String XML_EXTENSION = ".xml";
    private static final String ID_ETAPE_SAISIR_CDP = "5";

    private BSession bsession;
    private COProtocoleELP protocole;
    private String error = "";
    private String backupFolder;

    public static void main(final String[] args) {
        try {

            COImportMessageELP importMessageELP = new COImportMessageELP();
            importMessageELP.setPassword("oca");
            importMessageELP.setUsername("oca");
            importMessageELP.run();

            System.exit(0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {

        try {
            initBsession();
            String isActive = JadePropertiesService.getInstance().getProperty(COProperties.RECEPTION_MESSAGES_ELP.getProperty());
            if ("true".equals(isActive)) {
                importFiles();
                generationProtocol();
            }

        } catch (Exception e) {
            error = "erreur fatale : " + Throwables.getStackTraceAsString(e);
            try {
                sendResultMail(null);
            } catch (Exception e1) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e1);
            }
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        bsession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(bsession, this);
        protocole = new COProtocoleELP();
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    /**
     * Traitement des fichiers eLP
     */
    private void importFiles() {
        try {

            String urlFichiersELP = JadePropertiesService.getInstance().getProperty(COProperties.REPERTOIRE_GESTION_MESSAGES_ELP.getProperty());
            List<String> repositoryELP = JadeFsFacade.getFolderChildren(urlFichiersELP);
            backupFolder = new File(urlFichiersELP).getAbsolutePath() + BACKUP_FOLDER;
            for (String nomFichierDistant : repositoryELP) {
                importFile(nomFichierDistant);
            }
        } catch (Exception e) {
            error = "erreur fatale : " + Throwables.getStackTraceAsString(e);
            LOG.error("COImportMessageELP#import : erreur lors de l'importation des fichiers", e);
        }
    }

    /**
     * Copie le fichier eLP en local, effectue le traitement d'importion, sauvegarde le fichier
     * dans le dossier backup, supprime le fichier de base
     *
     * @param nomFichierDistant
     * @throws JAXBException
     */
    private void importFile(String nomFichierDistant) throws JAXBException {
        if (nomFichierDistant.endsWith(XML_EXTENSION)) {
            String tmpLocalWorkFile;
            String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
            String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
            COInfoFileELP infos = COInfoFileELP.getInfosFromFile(nameOriginalFileWithoutExt);
            if (infos != null) {
                try {
                    tmpLocalWorkFile = JadeFsFacade.readFile(nomFichierDistant);
                    File eLPFile = new File(tmpLocalWorkFile);
                    if (eLPFile.isFile()) {
                        boolean traitementInSucces = traitementFichier(getDocument(eLPFile), infos);
                        if (traitementInSucces) {
                            movingFile(nomFichierDistant, tmpLocalWorkFile, nameOriginalFile);
                        }
                    }
                } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
                    LOG.error("COImportMessageELP#importFile : erreur lors de l'importation du fichier " + nameOriginalFile, e);
                    protocole.addMsgIncoherentInattendue(infos, e.getMessage());
                }
            } else {
                protocole.addMsgIncoherentNomFichier(nameOriginalFile);
            }
        }
    }

    /**
     * Return le document XML : unmarshall depuis le fichier
     *
     * @param eLPFile
     * @return
     * @throws JAXBException
     */
    private Document getDocument(File eLPFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ELP_SCHEMA);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Document) unmarshaller.unmarshal(eLPFile);
    }

    /**
     * Traitement fichier selon le type
     *
     * @param doc
     * @param infos
     */
    private boolean traitementFichier(Document doc, COInfoFileELP infos) {
        boolean resultTraitement = Boolean.FALSE;
        if (doc.getSC() != null) {
            try {
                resultTraitement = traitement(doc.getSC(), infos);
            } catch (ElpProcessException e) {
                LOG.error("Une erreur est intervenue lors du traitement du fichier : " + infos.getFichier(), e);
                protocole.addMsgIncoherentInattendue(infos, e.getMessage());
            }
        } else if (doc.getSP() != null) {
            traitement(doc.getSP(), infos);
        } else if (doc.getRC() != null) {
            traitement(doc.getRC(), infos);
        } else {
            protocole.addMsgIncoherentOther(infos, COTypeELPFactory.getCOTypeMessageELPFromXml(doc));
        }
        return resultTraitement;
    }

    /**
     *  Permet de d�placer le fichier suite au traitement.
     *
     * @param nomFichierDistant
     * @param tmpLocalWorkFile
     * @param nameOriginalFile
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private void movingFile(String nomFichierDistant, String tmpLocalWorkFile, String nameOriginalFile) throws JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        if (!JadeFsFacade.exists(backupFolder)) {
            JadeFsFacade.createFolder(backupFolder);
        }
        JadeFsFacade.copyFile(tmpLocalWorkFile, backupFolder + nameOriginalFile);
        JadeFsFacade.delete(nomFichierDistant);
        JadeFsFacade.delete(tmpLocalWorkFile);
    }

    /**
     * Traitement du fichier CDP
     *
     * @param scType : l'objet de parsing du xml pour le type SC
     * @param infos  : les infos sur le fichier xml
     */
    private boolean traitement(ScType scType, COInfoFileELP infos) throws ElpProcessException {
        boolean traitementInSuccess = false;
        // Cr�ation de l'objet scElp
        COScElpDto scElpDto = getScElp(scType, infos);
        if (Objects.nonNull(scElpDto.getDateNotification())) {
            // R�cup�ration du contentieux
            COContentieux contentieux = getContentieux(infos);
            if (Objects.nonNull(contentieux)) {
                // Cr�ation de l'action
                CO040SaisirCDP action = createCdpAction(scElpDto);
                // R�cup�ration de la transition
                COTransition transition = getTransition(contentieux);
                if (Objects.nonNull(transition)) {
                    action.setTransition(transition);
                    // Ajout des �tapes infos
                    addEtapeInfos(action, scElpDto);
                    // Cr�ation du process
                    executeCOTransitionProcess(contentieux, action);
                    protocole.addMsgTraite(scElpDto);
                    traitementInSuccess = true;
                } else {
                    scElpDto.setMotifWrongStep(contentieux.getIdEtape());
                    protocole.addnonTraite(scElpDto);
                }
            } else {
                scElpDto.setMotif(COMotifMessageELP.REF_INCOMPATIBLE);
                protocole.addMsgIncoherent(scElpDto);
            }
        } else {
            scElpDto.setMotif(COMotifMessageELP.CDP_NON_NOTIFIE);
            protocole.addnonTraite(scElpDto);
        }
        return traitementInSuccess;
    }

    /**
     * Cr�ation de l'objet ScElp � partir du fichier XML.
     *
     * @param scType : la classe de cast
     * @param infos  : les infos sur le fichier
     * @return l'objet ScElp
     */
    private COScElpDto getScElp(ScType scType, COInfoFileELP infos) {
        COScElpDto scElpDto = new COScElpDto(scType);
        scElpDto.setDateReception(infos.getDate());
        scElpDto.setFichier(infos.getFichier());
        return scElpDto;
    }

    /**
     * M�thode de r�cup�ration du contentieux li�e au fichier. Si on ne parvient pas � trouver un unique contentieux li� au fichier, on retourne null.
     *
     * @param infos : les infos du fichier.
     * @return le contentieux s'il en trouve un. null sinon.
     * @throws ElpProcessException Exception lanc�e si un incident intervient dans la r�cup�ration du contentieux.
     */
    private COContentieux getContentieux(COInfoFileELP infos) throws ElpProcessException {
        COContentieux result = null;
        COContentieuxManager manager = new COContentieuxManager();
        manager.setSession(bsession);
        manager.setForNumSection(infos.getNoSection());
        manager.setForNumAffilie(infos.getNoAffilie());
        manager.setForSelectionRole(infos.getGenreAffiliation());
        try {
            manager.find(bsession.getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la r�cup�ration du contentieux", e);
        }
        if (manager.getContainer().size() == 1) {
            result = (COContentieux) manager.getFirstEntity();
        }
        return result;
    }

    /**
     * Cr�ation de l'action CDP.
     *
     * @param scElpDto : l'objet SC issue du ficheir xml.
     * @return l'action CDP associ� au fichier xml.
     */
    private CO040SaisirCDP createCdpAction(COScElpDto scElpDto) {
        CO040SaisirCDP action = new CO040SaisirCDP();
        action.setNumPoursuite(scElpDto.getNoPoursuite());
        action.setOpposition(scElpDto.getOpposition());
        action.setDateExecution(scElpDto.getDateNotification());
        return action;
    }

    /**
     * M�thode de r�cup�ration de la transition li�e au contentieux. Si on ne parvient pas � trouver une unique transition li�e au fichier, on retourne null.
     *
     * @param contentieux : le contentieux.
     * @return la transition s'il en trouve un. null sinon.
     * @throws ElpProcessException Exception lanc�e si un incident intervient dans la r�cup�ration de la transition.
     */
    private COTransition getTransition(COContentieux contentieux) throws ElpProcessException {
        COTransition result = null;
        COTransitionManager transitionManager = new COTransitionManager();
        transitionManager.setSession(bsession);
        transitionManager.setForIdEtapeSuivante(ID_ETAPE_SAISIR_CDP);
        transitionManager.setForIdEtape(contentieux.getIdEtape());
        try {
            transitionManager.find(bsession.getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la r�cup�ration de la transition li� au contentieux", e);
        }
        if (transitionManager.getContainer().size() == 1) {
            result = (COTransition) transitionManager.getFirstEntity();
        }
        return result;
    }

    /**
     * Ajout des informations li�es � l'�tape dans l'action.
     *
     * @param action   : l'action pour laquelle on souhaite ajouter les �tapes infos.
     * @param scElpDto : l'objet scElp contenant les informations du fichier xml.
     * @throws ElpProcessException exception lanc�e si un probl�me intervient durant la r�cup�ration des configs.
     */
    private void addEtapeInfos(CO040SaisirCDP action, COScElpDto scElpDto) throws ElpProcessException {
        try {
            List<COEtapeInfoConfig> etapeInfosConfigs = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();
            for (COEtapeInfoConfig eachEtapeInfosConfig : etapeInfosConfigs) {
                String valeur = StringUtils.EMPTY;
                if (Objects.equals(COEtapeInfoConfig.CS_DATE_NOTIFICATION_CDP, eachEtapeInfosConfig.getCsLibelle())) {
                    valeur = scElpDto.getDateNotification();
                } else if (Objects.equals(COEtapeInfoConfig.CS_DATE_RECEPTION_CDP, eachEtapeInfosConfig.getCsLibelle())) {
                    valeur = scElpDto.getDateReception();
                }
                action.addEtapeInfo(eachEtapeInfosConfig, valeur);
            }
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la r�cup�ration de la configuration des informations de l'�tape", e);
        }
    }

    /**
     * Lance l'ex�cution du process permettant la cr�ation de la nouvelle �tape.
     *
     * @param contentieux : le contentieux concern�.
     * @param action : l'action li�e.
     * @throws ElpProcessException : Exception lanc�e si une erreur intervient lors de l'ex�cution du process.
     */
    private void executeCOTransitionProcess(COContentieux contentieux, CO040SaisirCDP action) throws ElpProcessException {
        COProcessEffectuerTransition process = new COProcessEffectuerTransition();
        process.setSession(bsession);
        process.setTransaction(bsession.getCurrentThreadTransaction());
        process.setContentieux(contentieux);
        process.setRefresh(false);
        process.setLibSequence(contentieux.getLibSequence());
        process.setSelectedId(contentieux.getId());
        process.setIdEtapeSuivante(action.getTransition().getIdEtapeSuivante());
        process.setAction(action);
        try {
            process.executeProcess();
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la cr�ation de la nouvelle �tape du contentieux", e);
        }
    }

    /**
     * Traitement du fichier PV
     *
     * @param spType
     * @param infos
     */
    private void traitement(SpType spType, COInfoFileELP infos) {
        // TODO Non trait� : indiquer le motif
        protocole.addnonTraite(spType, infos, COMotifMessageELP.PV_NON_TRAITE);
        // TODO Pas de dossier trouv�
        protocole.addMsgIncoherent(spType, infos, COMotifMessageELP.REF_INCOMPATIBLE);
        // TODO Traitement OK
        protocole.addMsgTraite(spType, infos);
    }

    /**
     * Traitement du fichier ADB
     *
     * @param rcType
     * @param infos
     */
    private void traitement(RcType rcType, COInfoFileELP infos) {
        // TODO Non trait� : indiquer le motif
        protocole.addnonTraite(rcType, infos, COMotifMessageELP.RUBRIQUE_FRAIS_INDEF);
        // TODO Pas de dossier trouv�
        protocole.addMsgIncoherent(rcType, infos, COMotifMessageELP.REF_INCOMPATIBLE);
        // TODO Traitement OK
        protocole.addMsgTraite(rcType, infos);
    }

    /**
     * G�n�ration du protocol retour
     *
     * @throws Exception
     */
    private void generationProtocol() throws Exception {
        COResultELPExcel listResultELP = new COResultELPExcel(bsession, protocole);
        sendResultMail(listResultELP.getOutputFile());
    }

    private void sendResultMail(String filesPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getEMailObject(), error, new String[]{filesPath});
    }

    protected String getEMailObject() {
        return bsession.getLabel(MAIL_SUBJECT) + JACalendar.todayJJsMMsAAAA();
    }

    public final java.lang.String getEMailAddress() {
        String eMailAddress = JadePropertiesService.getInstance().getProperty(COProperties.DESTINATAIRE_PROTOCOLE_MESSAGES_ELP.getProperty());

        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && bsession != null) {
            return bsession.getUserEMail();
        }
        return eMailAddress;
    }

}
