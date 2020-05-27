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
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.helper.ICOEtapeHelper;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

public class COImportMessageELP extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(COImportMessageELP.class);

    private static final String ELP_SCHEMA = "aquila.ch.eschkg";
    private static final String MAIL_SUBJECT = "ELP_MAIL_SUBJECT";
    private static final String BACKUP_FOLDER = "/backup/";
    private static final String XML_EXTENSION = ".xml";
    private static final String ID_ETAPE_SAISIR_CDP = "5";
    private static final String ID_ETAPE_SAISIR_PV_ADB = "34";
    private static final String ID_ETAPE_SAISIR_PV_SAISIE = "36";
    private static final String ID_ETAPE_SAISIR_ADB = "21";

    private COProtocoleELP protocole;
    private String error = "";
    private String backupFolder;


    private void initBsession() throws Exception {
        BSessionUtil.initContext(getSession(), this);
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
     * Permet de d�placer le fichier suite au traitement.
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
        if (StringUtils.isNotEmpty(scElpDto.getDateNotification())) {
            // R�cup�ration du contentieux
            COContentieux contentieux = getContentieux(infos);
            if (Objects.nonNull(contentieux)) {
                // Cr�ation de l'action
                CO040SaisirCDP action = createCdpAction(scElpDto);
                // R�cup�ration de la transition
                COTransition transition = getTransition(contentieux, ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION);
                if (Objects.nonNull(transition)) {
                    action.setTransition(transition);
                    // Ajout des �tapes infos
                    addCDPEtapeInfos(action, scElpDto);
                    // Cr�ation du process
                    executeCOTransitionProcess(contentieux, action, new ArrayList());
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
        manager.setSession(getSession());
        manager.setForNumSection(infos.getNoSection());
        manager.setForNumAffilie(infos.getNoAffilie());
        manager.setForSelectionRole(infos.getGenreAffiliation());
        try {
            manager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
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
     * @param csEtape     : le cs de l'�tape � cr�er.
     * @return la transition s'il en trouve un. null sinon.
     * @throws ElpProcessException Exception lanc�e si un incident intervient dans la r�cup�ration de la transition.
     */
    private COTransition getTransition(COContentieux contentieux, String csEtape) throws ElpProcessException {
        COTransition result = null;
        COEtapeManager etapeManager = new COEtapeManager();
        etapeManager.setSession(getSession());
        etapeManager.setForLibEtape(csEtape);
        try {
            etapeManager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la r�cup�ration de l'id �tape li�e au contentieux", e);
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
                throw new ElpProcessException("Erreur lors de la r�cup�ration de la transition li�e au contentieux", e);
            }
            if (transitionManager.getContainer().size() == 1) {
                result = (COTransition) transitionManager.getFirstEntity();
            } else {
                LOG.error("Erreur lors de la r�cup�ration de de la transition li�e au contentieux : plusieurs transitions existent.");
            }
        } else {
           LOG.error("Erreur lors de la r�cup�ration de l'id �tape li�e au contentieux : plusieurs �tapes existent.");
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
            throw new ElpProcessException("Erreur lors de la r�cup�ration de la configuration des informations de l'�tape", e);
        }
    }

    /**
     * Lance l'ex�cution du process permettant la cr�ation de la nouvelle �tape.
     *
     * @param contentieux     : le contentieux concern�.
     * @param action          : l'action li�e.
     * @param fraisEtInterets : les frais et int�r�ts li�s.
     * @throws ElpProcessException : Exception lanc�e si une erreur intervient lors de l'ex�cution du process.
     */
    private void executeCOTransitionProcess(COContentieux contentieux, COTransitionAction action, List fraisEtInterets) throws ElpProcessException {
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
            throw new ElpProcessException("Erreur lors de la cr�ation de la nouvelle �tape du contentieux", e);
        }
    }

    /**
     * Traitement du fichier PV
     *
     * @param spType
     * @param infos
     * @throws ElpProcessException Exception lanc�e si un incident intervient dans la r�cup�ration du contentieux.
     * @throws COELPException      Exception lanc�e si un incident intervient lors de la r�cup�ration du type de saisie.
     */
    private Boolean traitement(SpType spType, COInfoFileELP infos) throws ElpProcessException, COELPException {
        boolean traitementInSuccess = false;
        // Cr�ation de l'objet spElp
        COSpElpDto spElpDto = getSpElp(spType, infos);
        // R�cup�ration du contentieux
        COContentieux contentieux = getContentieux(infos);
        if (Objects.nonNull(contentieux)) {
            COTransitionAction action;
            COTransition transition;
            if (spElpDto.isADB()) {
                action = createPvAdbAction(spElpDto);
                transition = getTransition(contentieux, ICOEtape.CS_PV_SAISIE_VALANT_ADB_SAISI);
            } else {
                action = createPvSaisieAction(spElpDto);
                transition = getTransition(contentieux, ICOEtape.CS_PV_DE_SAISIE_SAISI);
            }
            if (Objects.nonNull(transition)) {
                action.setTransition(transition);
                // Ajout des �tapes infos
                if (!spElpDto.isADB()) {
                    addPvEtapeInfos(action, spElpDto);
                }
                // Cr�ation du process
                executeCOTransitionProcess(contentieux, action, new ArrayList());
                protocole.addMsgTraite(spElpDto);
                traitementInSuccess = true;
            } else {
                spElpDto.setMotif(COMotifMessageELP.PV_NON_TRAITE);
                protocole.addnonTraite(spElpDto);
            }
        } else {
            spElpDto.setMotif(COMotifMessageELP.REF_INCOMPATIBLE);
            protocole.addMsgIncoherent(spElpDto);
        }
        return traitementInSuccess;
    }

    /**
     * Ajout des informations li�es � l'�tape dans l'action.
     *
     * @param action   : l'action pour laquelle on souhaite ajouter les �tapes infos.
     * @param spElpDto : l'objet spElp contenant les informations du fichier xml.
     * @throws ElpProcessException exception lanc�e si un probl�me intervient durant la r�cup�ration des configs.
     */
    private void addPvEtapeInfos(COTransitionAction action, COSpElpDto spElpDto) throws ElpProcessException {
        List<COEtapeInfoConfig> etapeInfosConfigs;
        try {
            etapeInfosConfigs = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la r�cup�ration de la configuration des informations de l'�tape", e);
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
     * Cr�ation de l'objet SpElp � partir du fichier XML.
     *
     * @param spType : la classe de cast
     * @param infos  : les infos sur le fichier
     * @return l'objet SpElp
     */
    private COSpElpDto getSpElp(SpType spType, COInfoFileELP infos) throws COELPException {
        COSpElpDto result = new COSpElpDto(spType, getSession());
        result.setDateReception(infos.getDate());
        result.setFichier(infos.getFichier());
        return result;
    }

    /**
     * Cr�ation de l'action en fonction des param�tres du fichier xml.
     *
     * @param spElpDto
     * @return l'action PV Saisie pour Acte de d�faut de bien
     */
    private COTransitionAction createPvAdbAction(COSpElpDto spElpDto) {
        CO041SaisirActeDefautBien action = new CO041SaisirActeDefautBien();
        action.setDateExecution(spElpDto.getDateExecution());
        return action;
    }

    /**
     * Cr�� l'action en fonction des param�tres du fichier xml.
     *
     * @param spElpDto
     * @return l'action PV de saisie
     */
    private COTransitionAction createPvSaisieAction(COSpElpDto spElpDto) {
        CO043SaisirPVSaisie action = new CO043SaisirPVSaisie();
        action.setCsTypeSaisie(spElpDto.getCsTypeDeSaisie());
        action.setDateExecutionSaisie(spElpDto.getDateExecution());
        return action;
    }

    /**
     * Traitement du fichier ADB
     *
     * @param rcType
     * @param infos
     */
    private Boolean traitement(RcType rcType, COInfoFileELP infos) throws ElpProcessException {
        boolean traitementInSuccess = false;
        // Cr�ation de l'objet rcElp
        CORcElpDto rcElpDto = getRcElp(rcType, infos);
        // R�cup�ration du contentieux
        COContentieux contentieux = getContentieux(infos);
        if (Objects.nonNull(contentieux)) {
            COTransitionAction action = createAdbAction(rcElpDto);
            COTransition transition = getTransition(contentieux, ICOEtape.CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI);
            if (Objects.nonNull(transition)) {
                action.setTransition(transition);
                // Ajout des �tapes infos
                addAdbEtapeInfos(action, rcElpDto);
                List<Map<String, String>> fraisEtInterets = null;
                try {
                    fraisEtInterets = getFraisEtInterets(rcElpDto, infos.getGenreAffiliation());
                } catch (COELPException e) {
                    LOG.error("Les frais et int�r�ts n'ont pas pu �tre g�r�s automatiquement.", e);
                    rcElpDto.setMotif(COMotifMessageELP.RUBRIQUE_FRAIS_INDEF);
                    protocole.addnonTraite(rcElpDto);
                    return false;
                }
                executeCOTransitionProcess(contentieux, action, fraisEtInterets);
                protocole.addMsgTraite(rcElpDto);
                traitementInSuccess = true;
            } else {
                rcElpDto.setMotif(COMotifMessageELP.ADB_NON_TRAITE);
                protocole.addnonTraite(rcElpDto);
            }
        } else {
            rcElpDto.setMotif(COMotifMessageELP.REF_INCOMPATIBLE);
            protocole.addMsgIncoherent(rcElpDto);
        }
        return traitementInSuccess;
    }

    /**
     * Cr�ation de l'objet RpElp � partir du fichier XML.
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
     * Cr�� l'action en fonction des param�tres du fichier xml.
     *
     * @param rcElpDto
     * @return l'action Acte de d�faut de bien.
     */
    private COTransitionAction createAdbAction(CORcElpDto rcElpDto) {
        CO041SaisirActeDefautBien adbAction = new CO041SaisirActeDefautBien();
        adbAction.setNumeroADB(rcElpDto.getNoAbd());
        return adbAction;
    }

    /**
     * Ajout des informations li�es � l'�tape dans l'action.
     *
     * @param action   : l'action pour laquelle on souhaite ajouter les �tapes infos.
     * @param rcElpDto : l'objet rcElp contenant les informations du fichier xml.
     * @throws ElpProcessException exception lanc�e si un probl�me intervient durant la r�cup�ration des configs.
     */
    private void addAdbEtapeInfos(COTransitionAction action, CORcElpDto rcElpDto) throws ElpProcessException {
        List<COEtapeInfoConfig> etapeInfosConfigs;
        try {
            etapeInfosConfigs = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la r�cup�ration de la configuration des informations de l'�tape", e);
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
     * R�cup�re les frais et les int�r�ts depuis le fichier xml.
     *
     * @param rcElpDto         : le rcElp.
     * @param genreAffiliation : le genre de l'affiliation.
     * @return la liste des frais et int�r�ts s'il y en a.
     */
    private List<Map<String, String>> getFraisEtInterets(CORcElpDto rcElpDto, String genreAffiliation) throws COELPException {
        List<Map<String, String>> fraisEtInterets = new ArrayList<>();
        Map<String, String> mapFraisEtInterets = new HashMap<>();
        if (StringUtils.isNotEmpty(rcElpDto.getInterest())) {
            mapFraisEtInterets.put(COTransitionViewBean.LIBELLE, StringUtils.EMPTY);
            mapFraisEtInterets.put(COTransitionViewBean.MONTANT, rcElpDto.getInterest());
            CAReferenceRubrique ref = new CAReferenceRubrique();
            APIRubrique rubrique;
            ref.setSession(getSession());
            switch (genreAffiliation) {
                case IntRole.ROLE_AFFILIE_PARITAIRE:
                    rubrique = ref.getRubriqueByCodeReference(APIReferenceRubrique.CONTENTIEUX_INTERET_MORATOIRE_PARITAIRE);
                    mapFraisEtInterets.put(COTransitionViewBean.RUB_DESCRIPTION, rubrique.getDescription());
                    mapFraisEtInterets.put(COTransitionViewBean.RUBRIQUE, rubrique.getIdRubrique());
                    break;
                case IntRole.ROLE_AFFILIE_PERSONNEL:
                    rubrique = ref.getRubriqueByCodeReference(APIReferenceRubrique.CONTENTIEUX_INTERET_MORATOIRE_PERSONNEL);
                    mapFraisEtInterets.put(COTransitionViewBean.RUB_DESCRIPTION, rubrique.getDescription());
                    mapFraisEtInterets.put(COTransitionViewBean.RUBRIQUE, rubrique.getIdRubrique());
                    break;
                default:
                    throw new COELPException("Ce type de rubrique n'est pas g�r� automatiquement.");
            }
            fraisEtInterets = new ArrayList<>();
            fraisEtInterets.add(mapFraisEtInterets);
        }
        return fraisEtInterets;
    }

    /**
     * G�n�ration du protocol retour
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

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            //Pas d'envoi de mail automatique du process, tout est g�r� manuellement
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

     private String getEMailAddressELP() {
        String eMailAddress = JadePropertiesService.getInstance().getProperty(COProperties.DESTINATAIRE_PROTOCOLE_MESSAGES_ELP.getProperty());

        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && getSession() != null) {
            return getSession().getUserEMail();
        }
        return eMailAddress;
    }

}
