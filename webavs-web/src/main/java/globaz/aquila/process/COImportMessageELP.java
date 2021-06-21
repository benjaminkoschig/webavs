package globaz.aquila.process;

import aquila.ch.eschkg.*;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import com.google.common.base.Throwables;
import globaz.af.gui.JJ14ActeurListeNumeroAvs;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.application.COProperties;
import globaz.aquila.db.access.batch.*;
import globaz.aquila.db.access.batch.transition.CO040SaisirCDP;
import globaz.aquila.db.access.batch.transition.CO041SaisirActeDefautBien;
import globaz.aquila.db.access.batch.transition.CO043SaisirPVSaisie;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxManager;
import globaz.aquila.db.batch.COTransitionViewBean;
import globaz.aquila.helpers.batch.process.COProcessEffectuerTransition;
import globaz.aquila.print.list.COResultELPExcel;
import globaz.aquila.print.list.elp.COELPException;
import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COTypeELPFactory;
import globaz.aquila.process.elp.*;
import globaz.aquila.process.exception.ElpProcessException;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.external.IntRole;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class COImportMessageELP extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(COImportMessageELP.class);

    private static final String ELP_SCHEMA = "aquila.ch.eschkg";
    private static final String MAIL_SUBJECT = "ELP_MAIL_SUBJECT";
    private static final String BACKUP_FOLDER = "/backup/";
    private static final String ERROR_FOLDER = "/error/";
    private static final String XML_EXTENSION = ".xml";
    private static final String XSD_FOLDER = "/xsd/aquila/";
    private static final String XSD_FILE_NAME = "eSchKG_2.2.01.xsd";

    private COProtocoleELP protocole;
    private String error = "";
    private String backupFolder;
    private String errorFolder;
    private Schema xsdSchema;

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            LOG.info("Lancement du process eLP-Box.");
            //Pas d'envoi de mail automatique du process, tout est géré manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

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

        return true;
    }

    private void initBsession() throws Exception {
        LOG.info("Initialisation de la session");
        BSessionUtil.initContext(getSession(), this);
        protocole = new COProtocoleELP();
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    private void clearErrorsWarning() {
        getSession().getErrors();
        getTransaction().clearErrorBuffer();
        getTransaction().clearWarningBuffer();
        getSession().getCurrentThreadTransaction().clearErrorBuffer();
        getSession().getCurrentThreadTransaction().clearWarningBuffer();
        getMemoryLog().clear();
        JadeThread.logClear();
    }

    /**
     * Traitement des fichiers eLP
     */
    private void importFiles() {
        try {
            LOG.info("Importation des fichiers.");
            String urlFichiersELP = JadePropertiesService.getInstance().getProperty(COProperties.REPERTOIRE_GESTION_MESSAGES_ELP.getProperty());
            List<String> repositoryELP = JadeFsFacade.getFolderChildren(urlFichiersELP);
            backupFolder = new File(urlFichiersELP).getAbsolutePath() + BACKUP_FOLDER;
            errorFolder = new File(urlFichiersELP).getAbsolutePath() + ERROR_FOLDER;
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
    private void importFile(String nomFichierDistant) throws Exception {
        if (nomFichierDistant.endsWith(XML_EXTENSION)) {
            String tmpLocalWorkFile;
            String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
            String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
            COInfoFileELP infos = COInfoFileELP.getInfosFromFile(nameOriginalFileWithoutExt);
            if (infos != null) {
                try {
                    tmpLocalWorkFile = JadeFsFacade.readFile(nomFichierDistant);
                    File eLPFile = new File(tmpLocalWorkFile);
                    boolean traitementInSucces = false;
                    if (eLPFile.isFile()) {
                        if(validateXml(eLPFile, infos)) {
                            traitementInSucces = traitementFichier(getDocument(eLPFile), infos);
                        }
                        movingFile(nomFichierDistant, tmpLocalWorkFile, nameOriginalFile, traitementInSucces);
                        if (getSession().hasErrors()) {
                            LOG.error("Une erreur est intervenue durant le traitement du fichier {} : {} ", nomFichierDistant, getSession().getErrors());
                            clearErrorsWarning();
                        }
                    }
                } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
                    LOG.error("COImportMessageELP#importFile : erreur lors de l'importation du fichier {}", nameOriginalFile, e);
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
        LOG.info("Traitement du fichier {}", infos.getFichier());
        boolean resultTraitement = Boolean.FALSE;
        try {
            if (doc.getSC() != null) {
                resultTraitement = traitement(doc.getSC(), infos);
            } else if (doc.getSP() != null) {
                resultTraitement = traitement(doc.getSP(), infos);
            } else if (doc.getRC() != null) {
                resultTraitement = traitement(doc.getRC(), infos);
            } else {
                protocole.addMsgIncoherentOther(infos, COTypeELPFactory.getCOTypeMessageELPFromXml(doc));
            }
        } catch (ElpProcessException | COELPException e) {
            LOG.error("Une erreur est intervenue lors du traitement du fichier : " + infos.getFichier(), e);
            protocole.addMsgIncoherentInattendue(infos, e.getMessage());
        }
        return resultTraitement;
    }

    /**
     * Permet de déplacer le fichier suite au traitement.
     *
     * @param nomFichierDistant
     * @param tmpLocalWorkFile
     * @param nameOriginalFile
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private void movingFile(String nomFichierDistant, String tmpLocalWorkFile, String nameOriginalFile, boolean traitementInSucces) throws JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        LOG.info("Déplacement du fichier après traitement.");
        String folder = traitementInSucces ? backupFolder : errorFolder;
        if (!JadeFsFacade.exists(folder)) {
            JadeFsFacade.createFolder(folder);
        }
        JadeFsFacade.copyFile(tmpLocalWorkFile, folder + nameOriginalFile);
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
        LOG.info("Traitement SC du fichier {}", infos.getFichier());
        boolean traitementInSuccess = false;
        // Création de l'objet scElp
        COScElpDto scElpDto = getScElp(scType, infos);
        switch (scElpDto.getNumeroStatut()) {
            case ElpStatut.CDP_SANS_OPPOSITION:
            case ElpStatut.CDP_AVEC_OPPOSITION:
                traitementInSuccess = createEtapeCDP(infos, scElpDto);
                break;
            case ElpStatut.CDP_NON_DELIVRE:
                scElpDto.setMotif(COMotifMessageELP.CDP_NON_DELIVRE);
                protocole.addnonTraite(scElpDto);
                break;
            case ElpStatut.PAIEMENT_COMPLET:
                scElpDto.setMotif(COMotifMessageELP.CDP_PAIEMENT_COMPLET);
                protocole.addnonTraite(scElpDto);
                break;
            default:
                scElpDto.setMotif(COMotifMessageELP.CDP_NON_TRAITE);
                protocole.addnonTraite(scElpDto);
                break;
        }
        return traitementInSuccess;
    }

    /**
     * Méthode de création de l'étape dans WebAVS.
     * Retourne vrai si le traitement a été exécuté avec succès.
     *
     * @param infos    : les infos du fichier
     * @param scElpDto : le contenu du fichier
     * @return vrai si le traitement a été exécuté avec succès.
     * @throws ElpProcessException
     */
    private boolean createEtapeCDP(COInfoFileELP infos, COScElpDto scElpDto) throws ElpProcessException {
        LOG.info("Création de l'étape Commandement de payer pour le fichier {}", infos.getFichier());
        boolean traitementInSuccess = false;
        if (StringUtils.isNotEmpty(scElpDto.getDateNotification())) {
            // Récupération du contentieux
            COContentieux contentieux = getContentieux(infos);
            if (Objects.nonNull(contentieux)) {
                // Création de l'action
                CO040SaisirCDP action = createCdpAction(scElpDto);
                // Récupération de la transition
                COTransition transition = getTransition(contentieux, ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION);
                if (Objects.nonNull(transition)) {
                    action.setTransition(transition);
                    // Ajout des étapes infos
                    addCDPEtapeInfos(action, scElpDto);
                    // Création du process
                    traitementInSuccess = executeCOTransitionProcess(contentieux, action, new ArrayList<>());
                    if (traitementInSuccess) {
                        protocole.addMsgTraite(scElpDto);
                    } else {
                        LOG.warn("La nouvelle étape du contentieux n'a pas été créée.");
                        if (getSession().hasErrors()) {
                            StringBuilder errors = filterErrors(getSession().getErrors().toString());
                            scElpDto.setMotifAdditional(errors.toString());
                        }
                        scElpDto.setMotif(COMotifMessageELP.CDP_NON_TRAITE);
                        protocole.addnonTraite(scElpDto);
                    }
                } else {
                    LOG.warn("La transition n'a pas pu être récupérée.");
                    scElpDto.setMotifWrongStep(getSession(), contentieux.getIdSequence(), contentieux.getIdEtape());
                    protocole.addnonTraite(scElpDto);
                }
            } else {
                LOG.warn("Le contentieux n'a pas pu être récupéré.");
                scElpDto.setMotif(COMotifMessageELP.REF_INCOMPATIBLE);
                protocole.addMsgIncoherent(scElpDto);
            }
        } else {
            LOG.warn("La date de notification est vide.");
            scElpDto.setMotif(COMotifMessageELP.CDP_NON_NOTIFIE);
            protocole.addnonTraite(scElpDto);
        }
        return traitementInSuccess;
    }

    /**
     * Création de l'objet ScElp à partir du fichier XML.
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
     * Méthode de récupération du contentieux liée au fichier. Si on ne parvient pas à trouver un unique contentieux lié au fichier, on retourne null.
     *
     * @param infos : les infos du fichier.
     * @return le contentieux s'il en trouve un. null sinon.
     * @throws ElpProcessException Exception lancée si un incident intervient dans la récupération du contentieux.
     */
    private COContentieux getContentieux(COInfoFileELP infos) throws ElpProcessException {
        COContentieux result = null;
        COContentieuxManager manager = new COContentieuxManager();
        manager.setSession(getSession());
        manager.setForNumSection(infos.getNoSection());
        manager.setForNumAffilie(infos.getNoAffilie());
        manager.setForSelectionRole(infos.getGenreAffiliation());
        try {
            manager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la récupération du contentieux", e);
        }
        if (manager.getContainer().size() == 1) {
            result = (COContentieux) manager.getFirstEntity();
        }
        return result;
    }

    /**
     * Création de l'action CDP.
     *
     * @param scElpDto : l'objet SC issue du ficheir xml.
     * @return l'action CDP associé au fichier xml.
     */
    private CO040SaisirCDP createCdpAction(COScElpDto scElpDto) {
        CO040SaisirCDP action = new CO040SaisirCDP();
        action.setNumPoursuite(scElpDto.getNoPoursuite());
        action.setOpposition(scElpDto.getOpposition());
        action.setDateExecution(scElpDto.getDateNotification());
        return action;
    }

    /**
     * Méthode de récupération de la transition liée au contentieux. Si on ne parvient pas à trouver une unique transition liée au fichier, on retourne null.
     *
     * @param contentieux : le contentieux.
     * @param csEtape     : le cs de l'étape à créer.
     * @return la transition s'il en trouve un. null sinon.
     * @throws ElpProcessException Exception lancée si un incident intervient dans la récupération de la transition.
     */
    private COTransition getTransition(COContentieux contentieux, String csEtape) throws ElpProcessException {
        COTransition result = null;
        COEtapeManager etapeManager = new COEtapeManager();
        etapeManager.setSession(getSession());
        etapeManager.setForIdSequence(contentieux.getIdSequence());
        etapeManager.setForLibEtape(csEtape);
        try {
            etapeManager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la récupération de l'id étape liée au contentieux", e);
        }
        if (etapeManager.getContainer().size() == 1) {
            COEtape etape = (COEtape) etapeManager.getFirstEntity();
            COTransitionManager transitionManager = new COTransitionManager();
            transitionManager.setSession(getSession());
            transitionManager.setForIdEtapeSuivante(etape.getIdEtape());
            transitionManager.setForIdEtape(contentieux.getIdEtape());
            try {
                transitionManager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new ElpProcessException("Erreur lors de la récupération de la transition liée au contentieux", e);
            }
            if (transitionManager.getContainer().size() == 1) {
                result = (COTransition) transitionManager.getFirstEntity();
            } else {
                LOG.warn("Erreur lors de la récupération de de la transition liée au contentieux.");
            }
        } else {
            LOG.warn("Erreur lors de la récupération de l'étape à créer.");
        }
        return result;
    }

    /**
     * Ajout des informations liées à l'étape dans l'action.
     *
     * @param action   : l'action pour laquelle on souhaite ajouter les étapes infos.
     * @param scElpDto : l'objet scElp contenant les informations du fichier xml.
     * @throws ElpProcessException exception lancée si un problème intervient durant la récupération des configs.
     */
    private void addCDPEtapeInfos(CO040SaisirCDP action, COScElpDto scElpDto) throws ElpProcessException {
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
            throw new ElpProcessException("Erreur lors de la récupération de la configuration des informations de l'étape", e);
        }
    }

    /**
     * Lance l'exécution du process permettant la création de la nouvelle étape.
     *
     * @param contentieux     : le contentieux concerné.
     * @param action          : l'action liée.
     * @param fraisEtInterets : les frais et intérêts liés.
     * @throws ElpProcessException : Exception lancée si une erreur intervient lors de l'exécution du process.
     *                             return true si le process s'est exécuté en succès.
     */
    private boolean executeCOTransitionProcess(COContentieux contentieux, COTransitionAction action, List fraisEtInterets) throws ElpProcessException {
        COProcessEffectuerTransition process = new COProcessEffectuerTransition();
        process.setSession(getSession());
        process.setTransaction(getSession().getCurrentThreadTransaction());
        process.setContentieux(contentieux);
        process.setRefresh(false);
        process.setLibSequence(contentieux.getLibSequence());
        process.setSelectedId(contentieux.getId());
        process.setFraisEtInterets(fraisEtInterets);
        process.setIdEtapeSuivante(action.getTransition().getIdEtapeSuivante());
        process.setAction(action);
        try {
            process.executeProcess();
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la création de la nouvelle étape du contentieux", e);
        }
        return process.getReturnCode() == 0;
    }

    /**
     * On filtre les erreurs pour ne pas afficher plusieurs fois le même message.
     * @param errors : les erreurs de la session
     * @return le message d'erreurs à afficher.
     */
    private StringBuilder filterErrors(String errors) {
        String[] allErrors = errors.split("\n");
        Set<String> filterErrors = new HashSet<>(Arrays.asList(allErrors));
        StringBuilder resultat = new StringBuilder();
        for (String eachError : filterErrors) {
            resultat.append("\n");
            resultat.append(eachError);
        }
        return resultat;
    }

    /**
     * Traitement du fichier PV
     *
     * @param spType
     * @param infos
     * @throws ElpProcessException Exception lancée si un incident intervient dans la récupération du contentieux.
     * @throws COELPException      Exception lancée si un incident intervient lors de la récupération du type de saisie.
     */
    private Boolean traitement(SpType spType, COInfoFileELP infos) throws ElpProcessException, COELPException {
        LOG.info("Traitement SP du fichier {}", infos.getFichier());
        boolean traitementInSuccess = false;
        // Création de l'objet spElp
        List<COSpElpDto> spElpDtoList = getSpElp(spType, infos);
        for (COSpElpDto spElpDto : spElpDtoList) {
            switch (spElpDto.getNumeroStatut()) {
                case ElpStatut.PV_SAISIE_MOB:
                case ElpStatut.PV_SAISIE_SAL:
                case ElpStatut.PV_SAISIE_SAL_MOB:
                case ElpStatut.PV_SAISIE_ADB:
                    traitementInSuccess = createEtapePVSaisie(infos, spElpDto);
                    if (!traitementInSuccess) {
                        return false;
                    }
                    break;
                case ElpStatut.PV_NON_LIEU:
                    spElpDto.setMotif(COMotifMessageELP.PV_NON_LIEU);
                    protocole.addnonTraite(spElpDto);
                    break;
                case ElpStatut.SOLDE_OP:
                    spElpDto.setMotif(COMotifMessageELP.PV_SOLDE_OP);
                    protocole.addnonTraite(spElpDto);
                    break;
                case ElpStatut.COMMINATION_FAILLITE:
                    spElpDto.setMotif(COMotifMessageELP.PV_COMMINATION_FAILLITE);
                    protocole.addnonTraite(spElpDto);
                    break;
                default:
                    spElpDto.setMotif(COMotifMessageELP.PV_NON_TRAITE);
                    protocole.addnonTraite(spElpDto);
                    break;
            }
        }
        return traitementInSuccess;
    }

    /**
     * Méthode de création de l'étape dans WebAVS.
     * Retourne vrai si le traitement a été exécuté avec succès.
     *
     * @param infos    : les infos du fichier
     * @param spElpDto : le contenu du fichier
     * @return vrai si le traitement a été exécuté avec succès.
     * @throws ElpProcessException
     */
    private boolean createEtapePVSaisie(COInfoFileELP infos, COSpElpDto spElpDto) throws ElpProcessException {
        LOG.info("Création de l'étape PV de saisie du fichier {}", infos.getFichier());
        boolean traitementInSuccess = false;
        // Récupération du contentieux
        COContentieux contentieux = getContentieux(infos);
        if (Objects.nonNull(contentieux)) {
            COTransitionAction action;
            COTransition transition;
            if (StringUtils.equals(ElpStatut.PV_SAISIE_ADB, spElpDto.getNumeroStatut())) {
                action = createPvAdbAction(spElpDto, contentieux);
                transition = getTransition(contentieux, ICOEtape.CS_PV_SAISIE_VALANT_ADB_SAISI);
            } else {
                action = createPvSaisieAction(spElpDto, contentieux);
                transition = getTransition(contentieux, ICOEtape.CS_PV_DE_SAISIE_SAISI);
            }
            if (Objects.nonNull(transition)) {
                action.setTransition(transition);
                List<Map<String, String>> fraisEtInterets;
                if (StringUtils.equals(ElpStatut.PV_SAISIE_ADB, spElpDto.getNumeroStatut())) {
                    try {
                        fraisEtInterets = getFraisEtInterets(spElpDto, infos.getGenreAffiliation());
                    } catch (COELPException e) {
                        LOG.error("Les frais et intérêts n'ont pas pu être gérés automatiquement.", e);
                        spElpDto.setMotif(COMotifMessageELP.RUBRIQUE_FRAIS_INDEF);
                        protocole.addnonTraite(spElpDto);
                        return false;
                    }
                } else {
                    fraisEtInterets = new ArrayList<>();
                    addPvEtapeInfos(action, spElpDto);
                }
                // Création du process
                traitementInSuccess = executeCOTransitionProcess(contentieux, action, fraisEtInterets);
                if (traitementInSuccess) {
                    protocole.addMsgTraite(spElpDto);
                } else {
                    LOG.warn("La nouvelle étape du contentieux n'a pas été créée.");
                    if (getSession().hasErrors()) {
                        StringBuilder errors = filterErrors(getSession().getErrors().toString());
                        spElpDto.setMotifAdditional(errors.toString());
                    }
                    spElpDto.setMotif(COMotifMessageELP.PV_NON_TRAITE);
                    protocole.addnonTraite(spElpDto);
                }
            } else {
                LOG.warn("La transition n'a pas pu être récupérée");
                spElpDto.setMotif(COMotifMessageELP.PV_NON_TRAITE);
                protocole.addnonTraite(spElpDto);
            }
        } else {
            LOG.warn("Le contentieux n'a pas pu être récupéré.");
            spElpDto.setMotif(COMotifMessageELP.REF_INCOMPATIBLE);
            protocole.addMsgIncoherent(spElpDto);
        }
        return traitementInSuccess;
    }

    /**
     * Ajout des informations liées à l'étape dans l'action.
     *
     * @param action   : l'action pour laquelle on souhaite ajouter les étapes infos.
     * @param spElpDto : l'objet spElp contenant les informations du fichier xml.
     * @throws ElpProcessException exception lancée si un problème intervient durant la récupération des configs.
     */
    private void addPvEtapeInfos(COTransitionAction action, COSpElpDto spElpDto) throws ElpProcessException {
        List<COEtapeInfoConfig> etapeInfosConfigs;
        try {
            etapeInfosConfigs = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la récupération de la configuration des informations de l'étape", e);
        }
        for (COEtapeInfoConfig eachEtapeInfosConfig : etapeInfosConfigs) {
            String valeur = StringUtils.EMPTY;
            switch (eachEtapeInfosConfig.getCsLibelle()) {
                case COEtapeInfoConfig.CS_TYPE_SAISIE:
                    valeur = spElpDto.getCsTypeDeSaisie();
                    break;
                case COEtapeInfoConfig.CS_DATE_EXECUTION_SAISIE:
                    valeur = spElpDto.getDateExecution();
                    break;
                case COEtapeInfoConfig.CS_DATE_RECEPTION_PV_SAISIE:
                    valeur = spElpDto.getDateReception();
                    break;
                case COEtapeInfoConfig.CS_DELAI_VENTE:
                    valeur = spElpDto.getDelaiVente();
                    break;
                default:
                    break;
            }
            action.addEtapeInfo(eachEtapeInfosConfig, valeur);
        }
    }

    /**
     * Création de la liste des objets SpElp à partir du fichier XML.
     *
     * @param spType : la classe de cast
     * @param infos  : les infos sur le fichier
     * @return la liste d'objet SpElp
     */
    private List<COSpElpDto> getSpElp(SpType spType, COInfoFileELP infos) throws COELPException {
        List<COSpElpDto> spElpDtoList = new ArrayList<>();
        Set<String> typesDeSaisie = getTypeSaisie(spType);
        if (typesDeSaisie.isEmpty()) {
            COSpElpDto result = new COSpElpDto(spType, getSession());
            result.setDateReception(infos.getDate());
            result.setFichier(infos.getFichier());
            spElpDtoList.add(result);
        } else {
            for (String typeSaisie : typesDeSaisie) {
                COSpElpDto result = new COSpElpDto(spType, typeSaisie, getSession());
                result.setDateReception(infos.getDate());
                result.setFichier(infos.getFichier());
                spElpDtoList.add(result);
            }
        }
        return spElpDtoList;
    }

    /**
     * Méthode permettant de récupérer tous les types de saisie contenus dans
     *
     * @param spType : la classe de cast
     * @return la liste des codes systèmes du type de saisie.
     */
    private Set<String> getTypeSaisie(SpType spType) {
        Set<String> result = new HashSet<>();
        if(Objects.nonNull(spType.getOutcome())) {
            SpType.Outcome.Seizure seizure = spType.getOutcome().getSeizure();
            if (Objects.nonNull(seizure)) {
                SpType.Outcome.Seizure.Deed deed = seizure.getDeed();
                if (Objects.nonNull(deed)) {
                    SpType.Outcome.Seizure.Deed.Seized seized = deed.getSeized();
                    for (JAXBElement eachPart : seized.getContent()) {
                        String localpart = eachPart.getName().getLocalPart();
                        String codeSytem = CODeedTypeSaisie.getCodeSystemFromCodeXml(localpart.substring(0, 2));
                        result.add(codeSytem);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Création de l'action en fonction des paramètres du fichier xml.
     *
     * @param spElpDto
     * @param contentieux
     * @return l'action PV Saisie pour Acte de défaut de bien
     */
    private COTransitionAction createPvAdbAction(COSpElpDto spElpDto, COContentieux contentieux) {
        CO041SaisirActeDefautBien action = new CO041SaisirActeDefautBien();
        contentieux.setDateExecution(spElpDto.getDateExecution());
        action.setDateExecution(contentieux.getDateExecution());
        return action;
    }

    /**
     * Créé l'action en fonction des paramètres du fichier xml.
     *
     * @param spElpDto
     * @param contentieux
     * @return l'action PV de saisie
     */
    private COTransitionAction createPvSaisieAction(COSpElpDto spElpDto, COContentieux contentieux) {
        CO043SaisirPVSaisie action = new CO043SaisirPVSaisie();
        action.setCsTypeSaisie(spElpDto.getCsTypeDeSaisie());
        action.setDateExecutionSaisie(spElpDto.getDateExecution());
        contentieux.setDateExecution(spElpDto.getDateExecution());
        action.setDateExecution(contentieux.getDateExecution());
        return action;
    }

    /**
     * Traitement du fichier ADB
     *
     * @param rcType
     * @param infos
     */
    private Boolean traitement(RcType rcType, COInfoFileELP infos) throws ElpProcessException {
        LOG.info("Traitement RC du fichier {}", infos.getFichier());
        boolean traitementInSuccess = false;
        // Création de l'objet rcElp
        CORcElpDto rcElpDto = getRcElp(rcType, infos);
        switch (rcElpDto.getNumeroStatut()) {
            case ElpStatut.ADB:
                traitementInSuccess = createEtapeADB(infos, rcElpDto);
                break;
            case ElpStatut.REGLEMENT_COMPLET_DETTE:
                rcElpDto.setMotif(COMotifMessageELP.ADB_REGLEMENT_DETTES);
                protocole.addnonTraite(rcElpDto);
                break;
            case ElpStatut.DEBUT_PROCEDURE_FAILLITE:
                rcElpDto.setMotif(COMotifMessageELP.ADB_PROCEDURE_FAILLITE);
                protocole.addnonTraite(rcElpDto);
                break;
            case ElpStatut.SURSIS:
                rcElpDto.setMotif(COMotifMessageELP.ADB_SURSIS);
                protocole.addnonTraite(rcElpDto);
                break;
            default:
                rcElpDto.setMotif(COMotifMessageELP.ADB_NON_TRAITE);
                protocole.addnonTraite(rcElpDto);
                break;
        }
        return traitementInSuccess;
    }

    /**
     * Méthode de création de l'étape dans WebAVS.
     * Retourne vrai si le traitement a été exécuté avec succès.
     *
     * @param infos    : les infos du fichier
     * @param rcElpDto : le contenu du fichier
     * @return vrai si le traitement a été exécuté avec succès.
     * @throws ElpProcessException
     */
    private boolean createEtapeADB(COInfoFileELP infos, CORcElpDto rcElpDto) throws ElpProcessException {
        LOG.info("Création de l'étape Acte de défaut de bien du fichier {}", infos.getFichier());
        boolean traitementInSuccess = false;
        // Récupération du contentieux
        COContentieux contentieux = getContentieux(infos);
        if (Objects.nonNull(contentieux)) {
            COTransitionAction action = createAdbAction(rcElpDto, contentieux);
            COTransition transition = getTransition(contentieux, ICOEtape.CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI);
            if (Objects.nonNull(transition)) {
                action.setTransition(transition);
                // Ajout des étapes infos
                addAdbEtapeInfos(action, rcElpDto);
                List<Map<String, String>> fraisEtInterets;
                try {
                    fraisEtInterets = getFraisEtInterets(rcElpDto, infos.getGenreAffiliation());
                } catch (COELPException e) {
                    LOG.error("Les frais et intérêts n'ont pas pu être gérés automatiquement.", e);
                    rcElpDto.setMotif(COMotifMessageELP.RUBRIQUE_FRAIS_INDEF);
                    protocole.addnonTraite(rcElpDto);
                    return false;
                }
                traitementInSuccess = executeCOTransitionProcess(contentieux, action, fraisEtInterets);
                if (traitementInSuccess) {
                    protocole.addMsgTraite(rcElpDto);
                } else {
                    LOG.warn("La nouvelle étape du contentieux n'a pas été créée.");
                    if (getSession().hasErrors()) {
                        StringBuilder errors = filterErrors(getSession().getErrors().toString());
                        rcElpDto.setMotifAdditional(errors.toString());
                    }
                    rcElpDto.setMotif(COMotifMessageELP.ADB_NON_TRAITE);
                    protocole.addnonTraite(rcElpDto);
                }
            } else {
                LOG.warn("La transition n'a pas pu être récupérée.");
                rcElpDto.setMotif(COMotifMessageELP.ADB_NON_TRAITE);
                protocole.addnonTraite(rcElpDto);
            }
        } else {
            LOG.warn("Le contentieux n'a pas pu être récupéré.");
            rcElpDto.setMotif(COMotifMessageELP.REF_INCOMPATIBLE);
            protocole.addMsgIncoherent(rcElpDto);
        }
        return traitementInSuccess;
    }

    /**
     * Création de l'objet RpElp à partir du fichier XML.
     *
     * @param rcType : la classe de cast
     * @param infos  : les infos sur le fichier
     * @return l'objet RcElp
     */
    private CORcElpDto getRcElp(RcType rcType, COInfoFileELP infos) {
        CORcElpDto result = new CORcElpDto(rcType);
        result.setDateReception(infos.getDate());
        result.setFichier(infos.getFichier());
        return result;
    }

    /**
     * Créé l'action en fonction des paramètres du fichier xml.
     *
     * @param rcElpDto
     * @param contentieux
     * @return l'action Acte de défaut de bien.
     */
    private COTransitionAction createAdbAction(CORcElpDto rcElpDto, COContentieux contentieux) {
        CO041SaisirActeDefautBien adbAction = new CO041SaisirActeDefautBien();
        adbAction.setNumeroADB(rcElpDto.getNoAbd());
        contentieux.setDateExecution(rcElpDto.getDateEtablissement());
        adbAction.setDateExecution(contentieux.getDateExecution());
        return adbAction;
    }

    /**
     * Ajout des informations liées à l'étape dans l'action.
     *
     * @param action   : l'action pour laquelle on souhaite ajouter les étapes infos.
     * @param rcElpDto : l'objet rcElp contenant les informations du fichier xml.
     * @throws ElpProcessException exception lancée si un problème intervient durant la récupération des configs.
     */
    private void addAdbEtapeInfos(COTransitionAction action, CORcElpDto rcElpDto) throws ElpProcessException {
        List<COEtapeInfoConfig> etapeInfosConfigs;
        try {
            etapeInfosConfigs = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la récupération de la configuration des informations de l'étape", e);
        }
        for (COEtapeInfoConfig eachEtapeInfosConfig : etapeInfosConfigs) {
            String valeur = StringUtils.EMPTY;
            switch (eachEtapeInfosConfig.getCsLibelle()) {
                case COEtapeInfoConfig.CS_NUMERO_ADB:
                    valeur = rcElpDto.getNoAbd();
                    break;
                case COEtapeInfoConfig.CS_DATE_ETABLISSEMENT_ADB:
                    valeur = rcElpDto.getDateEtablissement();
                    break;
                default:
                    break;
            }
            action.addEtapeInfo(eachEtapeInfosConfig, valeur);
        }
    }

    /**
     * Récupère les frais et les intérêts depuis le fichier xml.
     *
     * @param elpDto         : le dto de type RC ou SP -> type 206 ou 303.
     * @param genreAffiliation : le genre de l'affiliation.
     * @return la liste des frais et intérêts s'il y en a.
     */
    private List<Map<String, String>> getFraisEtInterets(COAbstractELP elpDto, String genreAffiliation) throws COELPException {
        List<Map<String, String>> fraisEtInterets = new ArrayList<>();
        Map<String, String> mapFraisEtInterets = new HashMap<>();
        if (StringUtils.isNotEmpty(elpDto.getInterest()) && Float.parseFloat(elpDto.getInterest()) != 0) {
            mapFraisEtInterets.put(COTransitionViewBean.LIBELLE, StringUtils.EMPTY);
            mapFraisEtInterets.put(COTransitionViewBean.MONTANT, elpDto.getInterest());
            CAReferenceRubrique ref = new CAReferenceRubrique();
            APIRubrique rubrique;
            ref.setSession(getSession());
            switch (genreAffiliation) {
                case IntRole.ROLE_AFFILIE_PARITAIRE:
                    rubrique = ref.getRubriqueByCodeReference(APIReferenceRubrique.CONTENTIEUX_INTERET_MORATOIRE_PARITAIRE);
                    mapFraisEtInterets.put(COTransitionViewBean.RUB_DESCRIPTION, rubrique.getDescription());
                    mapFraisEtInterets.put(COTransitionViewBean.RUBRIQUE, rubrique.getIdExterne());
                    break;
                case IntRole.ROLE_AFFILIE_PERSONNEL:
                    rubrique = ref.getRubriqueByCodeReference(APIReferenceRubrique.CONTENTIEUX_INTERET_MORATOIRE_PERSONNEL);
                    mapFraisEtInterets.put(COTransitionViewBean.RUB_DESCRIPTION, rubrique.getDescription());
                    mapFraisEtInterets.put(COTransitionViewBean.RUBRIQUE, rubrique.getIdExterne());
                    break;
                default:
                    throw new COELPException("Ce type de rubrique n'est pas géré automatiquement.");
            }
            fraisEtInterets = new ArrayList<>();
            fraisEtInterets.add(mapFraisEtInterets);
        }
        return fraisEtInterets;
    }

    /**
     * Génération du protocol retour
     *
     * @throws Exception
     */
    private void generationProtocol() throws Exception {
        COResultELPExcel listResultELP = new COResultELPExcel(getSession(), protocole);
        sendResultMail(listResultELP.getOutputFile());
    }

    private void sendResultMail(String filesPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getEMailAddressELP(), getEMailObject(), error, new String[]{filesPath});
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel(MAIL_SUBJECT) + JACalendar.todayJJsMMsAAAA();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        //Nothing to do
    }


    private String getEMailAddressELP() {
        String eMailAddress = JadePropertiesService.getInstance().getProperty(COProperties.DESTINATAIRE_PROTOCOLE_MESSAGES_ELP.getProperty());

        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && getSession() != null) {
            return getSession().getUserEMail();
        }
        return eMailAddress;
    }

    private boolean validateXml(File xmlFile, COInfoFileELP infos) {
        try {
            loadXsdSchema();
            Validator validator = xsdSchema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (SAXException | IOException e) {
            protocole.addMsgIncoherentInattendue(infos, "le fichier xml fourni n'est pas valide.");
            LOG.error("e fichier xml fourni n'est pas valide.", e);
            return false;
        }
    }

    private void loadXsdSchema(){
        if(Objects.isNull(xsdSchema)) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = getClass().getResource(XSD_FOLDER + XSD_FILE_NAME);
            if (Objects.nonNull(url)) {
                File xsdFile = new File(url.getFile());
                try {
                    xsdSchema = factory.newSchema(xsdFile);
                } catch (SAXException e) {
                    LOG.info("Impossible de charger le schema xsd: {}", XSD_FILE_NAME);
                }
            }
        }
    }
}
