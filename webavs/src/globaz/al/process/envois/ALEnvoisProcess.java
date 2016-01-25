/**
 * 
 */
package globaz.al.process.envois;

import globaz.al.process.ALAbsrtactProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.pyxis.db.tiers.TITiers;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class ALEnvoisProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idJobEnvoi = null;

    private String newStatus = null;

    private String getAllErrorsFromJadeThread() {
        // Lecture des erreurs
        String msgError = "";
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            for (int iMessage = 0; iMessage < JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
                msgError += "\n"
                        + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                                .getContents(JadeThread.currentLanguage());
            }
        }
        return msgError;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Envoi process to publish all AF documents inside the GED system";
        // return JadeI18n.getInstance().getMessage(this.getSession().getUserInfo().getLanguage(),
        // "globaz.al.process.envois.ALEnvoisProcess.description");
    }

    /**
     * récupération du chemin complete du document, vu depuis le serveur de job
     * 
     * @param currentItem
     * @return
     */
    private String getDocumentFullPath(EnvoiItemSimpleModel currentItem) {
        String filePath = "";
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(ALCSEnvoi.SHARED_PATH_FROM_JOB_SERVER);
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Path from job server to the shared directory not found. Exception : " + ex.toString());
        }
        if (searchModel.getSize() == 1) {
            EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel.getSearchResults()[0];
            filePath += currentParametres.getValeurParametre();
        } else {
            JadeThread.logError(toString(), "Path from job server to the shared directory not found.");
        }
        filePath += currentItem.getEnvoiFileName();
        return filePath;
    }

    /**
     * Récupération d'un libelle genre DOC001 - Lettre neutre en fonction d'un id formule
     * 
     * @param idFormule
     * @return
     */
    private String getDocumentLibelle(EnvoiTemplateComplexModel currentTemplate) {

        String returnLibelle = "";

        // Recherche de tous les codes système alenvoidoc
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("ALENVOIDOC");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception ex) {
            JadeLogger.error(this, "Error searching code systeme for formule id : "
                    + currentTemplate.getEnvoiTemplateSimpleModel().getIdFormule() + " >> " + ex.toString());
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return returnLibelle;
        }
        for (Iterator it = cm.getContainer().iterator(); it.hasNext();) {
            FWParametersCode code = (FWParametersCode) it.next();
            if (code.getId().equals(currentTemplate.getFormuleList().getDefinitionformule().getCsDocument())) {
                returnLibelle = code.getLibelle();
                returnLibelle += " ";
                returnLibelle += code.getCodeUtilisateur(currentSession.getIdLangue()).getLibelle();
                break;
            }
        }
        return returnLibelle;
    }

    /**
     * Récupération du type de document pour la ged
     * 
     * @param currentItem
     * @return
     */
    private String getGEDDocumentType(EnvoiItemSimpleModel currentItem) {
        String documentType = "";
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(ALCSEnvoi.GED_DOCUMENT_TYPE);
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
        } catch (Exception ex) {
            JadeThread.logError(toString(), "GED Document Type Exception : " + ex.toString());
        }
        if (searchModel.getSize() == 1) {
            EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel.getSearchResults()[0];
            documentType = currentParametres.getValeurParametre();
        } else {
            JadeThread.logError(toString(), "GED Document Type not found");
        }
        return documentType;
    }

    /**
     * Récupération du type de document number pour la ged
     * 
     * @param currentItem
     * @return
     */
    private String getGEDDocumentTypeNumber(EnvoiItemSimpleModel currentItem) {
        String documentTypeNumber = "";
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(ALCSEnvoi.GED_DOCUMENT_TYPE_NUMBER);
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
        } catch (Exception ex) {
            JadeThread.logError(toString(), "GED Document Type Number Exception : " + ex.toString());
        }
        if (searchModel.getSize() == 1) {
            EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel.getSearchResults()[0];
            documentTypeNumber = currentParametres.getValeurParametre();
        } else {
            JadeThread.logError(toString(), "GED Document Type Number not found");
        }
        return documentTypeNumber;
    }

    /**
     * @return the idJobEnvoi
     */
    public String getIdJobEnvoi() {
        return idJobEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getName().toString();
        // return JadeI18n.getInstance().getMessage(this.getSession().getUserInfo().getLanguage(),
        // "globaz.al.process.envois.ALEnvoisProcess.name");
    }

    /**
     * @return the newStatus
     */
    public String getNewStatus() {
        return newStatus;
    }

    /**
     * Passage des status de tous les éléments à en traitement avant de lancer le job
     * 
     * @param idJobEnvoi
     * @param newStatus
     * @return true if errors occurs
     */
    public Boolean initializeProcess(String idJobEnvoi, String newStatus) {
        // Check des paramètres en entrée
        if (JadeStringUtil.isEmpty(idJobEnvoi)) {
            return true;
        }
        if (JadeStringUtil.isEmpty(newStatus)) {
            return true;
        }
        setIdJobEnvoi(idJobEnvoi);
        setNewStatus(newStatus);
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        setSession(currentSession);
        // Set job status à in progress
        try {
            EnvoiJobSimpleModel currentJob = ALImplServiceLocator.getEnvoiJobSimpleModelService().read(idJobEnvoi);
            currentJob.setJobStatus(ALCSEnvoi.STATUS_ENVOI_IN_PROGRESS);
            currentJob = ALImplServiceLocator.getEnvoiJobSimpleModelService().update(currentJob);
        } catch (Exception ex) {
            JadeLogger.error(this, "Error reading/updating currentJob " + idJobEnvoi + " (rollback will be applied) : "
                    + ex.toString());
            return true;
        }
        // Recherche des enfants du job
        EnvoiItemSimpleModelSearch searchModel = new EnvoiItemSimpleModelSearch();
        searchModel.setForIdJob(idJobEnvoi);
        searchModel.setDefinedSearchSize(0);
        try {
            searchModel = ALImplServiceLocator.getEnvoiItemSimpleModelService().search(searchModel);
            for (int iElement = 0; iElement < searchModel.getSize(); iElement++) {
                EnvoiItemSimpleModel envoiItem = (EnvoiItemSimpleModel) searchModel.getSearchResults()[iElement];
                envoiItem.setEnvoiStatus(ALCSEnvoi.STATUS_ENVOI_IN_PROGRESS);
                envoiItem = ALImplServiceLocator.getEnvoiItemSimpleModelService().update(envoiItem);
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error updating envoi item status (rollback will be applied) : " + ex.toString());
            return true;
        }
        return false;
    }

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.al.process.ALAbsrtactProcess#process()
     */
    @Override
    protected void process() {
        boolean bProcessHasError = false;
        // ---------------------------------------------------------------
        // Lecture de toutes les entités en traitement, pour le job donné
        // ---------------------------------------------------------------
        EnvoiItemSimpleModelSearch allItems = new EnvoiItemSimpleModelSearch();
        allItems.setForIdJob(getIdJobEnvoi());
        allItems.setForEnvoiStatus(ALCSEnvoi.STATUS_ENVOI_IN_PROGRESS);
        allItems.setDefinedSearchSize(0);
        try {
            allItems = ALImplServiceLocator.getEnvoiItemSimpleModelService().search(allItems);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Error searching all items for job : " + ex.getMessage() + "-" + ex.getCause());
            bProcessHasError = true;
        }
        if (!bProcessHasError) {
            String warning = "";
            for (int iCurrentItem = 0; iCurrentItem < allItems.getSize(); iCurrentItem++) {
                Boolean bItemHasError = false;
                // ---------------------------------------------------------------
                // 0) Lecture de toutes les informations nécessaires
                // ---------------------------------------------------------------
                EnvoiItemSimpleModel currentItem = (EnvoiItemSimpleModel) allItems.getSearchResults()[iCurrentItem];
                DossierComplexModel currentDossier = null;
                try {
                    currentDossier = ALServiceLocator.getDossierComplexModelService().read(
                            currentItem.getIdExternalLink());
                } catch (Exception ex) {
                    JadeThread.logError(toString(), "Error reading the dossier " + currentItem.getIdExternalLink()
                            + " for current item : " + ex.getMessage() + "-" + ex.getCause());
                    bItemHasError = true;
                }
                EnvoiTemplateComplexModel currentTemplate = null;
                EnvoiTemplateComplexModelSearch currentTemplateSearch = new EnvoiTemplateComplexModelSearch();
                currentTemplateSearch.setForIdFormule(currentItem.getIdFormule());
                try {
                    currentTemplateSearch = ALServiceLocator.getEnvoiTemplateComplexService().search(
                            currentTemplateSearch);
                } catch (Exception ex) {
                    JadeThread.logError(toString(),
                            "Error reading the template from formule " + currentItem.getIdFormule()
                                    + " for current item : " + ex.getMessage() + "-" + ex.getCause());
                    bItemHasError = true;
                }
                if (currentTemplateSearch.getSize() == 1) {
                    currentTemplate = (EnvoiTemplateComplexModel) currentTemplateSearch.getSearchResults()[0];
                } else {
                    JadeThread.logError(toString(), "Template information for formule " + currentItem.getIdFormule()
                            + " not find for current item");
                    bItemHasError = true;
                }
                if (!bItemHasError) {
                    // ---------------------------------------------------------------
                    // 1) Publication en GED
                    // ---------------------------------------------------------------
                    bItemHasError = processStep1PublishGED(currentItem, currentDossier, currentTemplate);
                    // ---------------------------------------------------------------
                    // 2) Journalisation explicite
                    // ---------------------------------------------------------------
                    bItemHasError = processStep2Journalisation(currentItem, currentDossier, currentTemplate);
                    // ---------------------------------------------------------------
                    // 3) Adaptation du flag du dossier si nécessaire
                    // ---------------------------------------------------------------
                    bItemHasError = processStep3UpdateDossierFlag(currentItem, currentDossier, currentTemplate);
                }
                // ---------------------------------------------------------------
                // 4) Gestion des erreurs (copie de l'erreur dans item)
                // ---------------------------------------------------------------
                bItemHasError = processStep4ManageError(bItemHasError, currentItem, currentDossier, currentTemplate);
                // ---------------------------------------------------------------
                // 5) Mise à jour du status
                // ---------------------------------------------------------------
                bItemHasError = processStep5UpdateStatus(currentItem, currentDossier, currentTemplate);
                // ---------------------------------------------------------------
                // 6) Log errors s'il en reste dans la console
                // ---------------------------------------------------------------
                String addWarning = processStep6ErrorCommit();
                if (addWarning.length() > 0) {
                    addWarning += "\nItem " + currentItem.getId() + " : " + addWarning;
                    warning += addWarning;
                }
            }
            // ---------------------------------------------------------------
            // Travail sur le job en cours (màj status)
            // ---------------------------------------------------------------
            try {
                EnvoiJobSimpleModel currentJob = ALImplServiceLocator.getEnvoiJobSimpleModelService().read(idJobEnvoi);
                currentJob.setJobStatus(ALCSEnvoi.STATUS_ENVOI_SENT);
                currentJob = ALImplServiceLocator.getEnvoiJobSimpleModelService().update(currentJob);
            } catch (Exception ex) {
                JadeLogger.error(this, "Error reading/udpating the job " + idJobEnvoi + " : " + ex.getMessage() + "-"
                        + ex.getCause());
                bProcessHasError = true;
            }
            // ---------------------------------------------------------------
            // Envoi de mail si erreur
            // ---------------------------------------------------------------
            // if (warning.length() > 0) {
            // JadeThread.logWarn(this.toString(), warning);
            // }
            // if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.WARN)) {
            // ArrayList<String> emailAdresses = new ArrayList<String>();
            // emailAdresses.add(BSessionUtil.getSessionFromThreadContext().getUserEMail());
            // try {
            // super.sendCompletionMail(emailAdresses);
            // } catch (Exception e) {
            // JadeThread.logError(this.getClass().getName(), "Error while sending mail ! --> " + e.getMessage());
            // }
            // }

        }
    }

    /**
     * 1ère étape, ajout du document en GED
     * 
     * @param currentItem
     * @param currentDossier
     * @param currentTemplate
     * @return true si une erreur est rencontrée, false sinon
     */
    private boolean processStep1PublishGED(EnvoiItemSimpleModel currentItem, DossierComplexModel currentDossier,
            EnvoiTemplateComplexModel currentTemplate) {

        boolean bError = false;

        // préparation des données de publication pour le document fusionné
        JadePublishDocumentInfo publishInfo = new JadePublishDocumentInfo();

        // GED
        publishInfo.setArchiveDocument(true);
        // MAIL
        publishInfo.setPublishDocument(false);

        publishInfo.setDocumentType(getGEDDocumentType(currentItem));
        publishInfo.setDocumentTypeNumber(getGEDDocumentTypeNumber(currentItem));
        publishInfo.setOwnerId(JadeThread.currentUserId());
        publishInfo.setOwnerEmail(JadeThread.currentUserEmail());
        publishInfo.setDocumentTitle(getDocumentLibelle(currentTemplate));

        String nssAllocataire = currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel();
        String numAffilie = currentDossier.getDossierModel().getNumeroAffilie();

        // FIXME:bz5857
        // TODO : récupération du service futur AL getGedPublishInfo
        // TODO : vérifier la manière de travailler du serveur de job

        publishInfo.setPublishProperty("numero.role.formatte", numAffilie);
        publishInfo.setPublishProperty("numero.affilie.formatte", numAffilie);
        publishInfo.setPublishProperty("numero.avs.formatte", nssAllocataire);
        publishInfo.setPublishProperty("numero.role.non.formatte", JadeStringUtil.removeChar(numAffilie, '.'));
        publishInfo.setPublishProperty("pyxis.tiers.numero.avs.formatte", nssAllocataire);
        publishInfo.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                JadeStringUtil.removeChar(nssAllocataire, '.'));

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        // publishInfo.setPublishProperty("creation.date.dmy", sdf.format(date));
        publishInfo.setDocumentDate(sdf.format(date));

        try {
            publishInfo.setPublishProperty("type.dossier",
                    ALServiceLocator.getGedBusinessService().getTypeSousDossier(currentDossier.getDossierModel()));
        } catch (Exception ex) {
            JadeThread.logError(toString(), "Error reading subtype of dossier : " + ex.getLocalizedMessage() + " - "
                    + ex.toString());
            bError = true;
        }

        // Pas toujours renseigné par la méthode fill ci-dessous
        publishInfo.setPublishProperty(
                "pyxis.tiers.nom.prenom",
                TITiers.getNom(currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                        .getDesignation1(), currentDossier.getAllocataireComplexModel()
                        .getPersonneEtendueComplexModel().getTiers().getDesignation2(), " "));
        try {
            TIBusinessServiceLocator.getDocInfoService().fill(publishInfo,
                    currentDossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), null,
                    null, ALCSTiers.ROLE_AF, currentDossier.getDossierModel().getNumeroAffilie(),
                    JadeStringUtil.removeChar(currentDossier.getDossierModel().getNumeroAffilie(), '.'), null);
        } catch (Exception ex) {
            JadeThread.logError(toString(), "Error filling publish info of dossier : " + ex.getLocalizedMessage()
                    + " - " + ex.toString());
            bError = true;
        }

        // Récupération du path du fichier, vu depuis le serveur de job !
        String fullFileName = getDocumentFullPath(currentItem);
        publishInfo.setCurrentPathName(fullFileName);
        // Préparation pour la publication et publication
        JadePublishDocument currentDocument;
        try {
            currentDocument = new JadePublishDocument(fullFileName, publishInfo);
            JadePublishDocumentMessage currentMessage = new JadePublishDocumentMessage(currentDocument);
            JadePublishServerFacade.publishDocument(currentMessage);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Error publishing document : " + ex.getLocalizedMessage() + " - " + ex.toString());
            bError = true;
        }
        // on efface le fichier de toute manière (devrait être fait lors de l'étape de publication
        try {
            if (JadeFsFacade.exists(fullFileName)) {
                JadeFsFacade.delete(fullFileName);
            }
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Error deleting document : " + ex.getLocalizedMessage() + " - " + ex.toString());
            bError = true;
        }

        return bError;
    }

    /**
     * 2ème étape, journaliser le document au niveau dossier AF
     * 
     * @param currentItem
     * @param currentDossier
     * @param currentTemplate
     * @return true si une erreur est rencontrée, false sinon
     */
    private boolean processStep2Journalisation(EnvoiItemSimpleModel currentItem, DossierComplexModel currentDossier,
            EnvoiTemplateComplexModel currentTemplate) {
        String msgError = getAllErrorsFromJadeThread();
        if (msgError.length() > 0) {
            JadeThread.logClear();
        }

        String dossierId = currentDossier.getId();
        String libelleLibra = getDocumentLibelle(currentTemplate);
        String remarqueLibra = "IdEnvoi : " + currentItem.getIdEnvoi() + " - idJob : " + currentItem.getIdJob();
        String idTiers = currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                .getId();

        try {
            LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(dossierId,
                    libelleLibra, remarqueLibra, idTiers, ILIConstantesExternes.CS_DOMAINE_AF, true);
        } catch (Exception ex) {
            if (msgError.length() > 0) {
                JadeThread.logError(toString(), msgError);
            }
            JadeThread.logError(toString(), "Error inserting item to journalisation " + currentDossier.getId() + " : "
                    + ex.getMessage() + "-" + ex.getCause());
            return true;

        }
        if (msgError.length() > 0) {
            JadeThread.logError(toString(), msgError);
        }

        return false;

    }

    /**
     * 3ème étape, mise à jour du code de l'état du dossier en fonction des paramètres de documents
     * 
     * @param currentItem
     * @param currentDossier
     * @param currentTemplate
     * @return true si une erreur est rencontrée, false sinon
     */
    private boolean processStep3UpdateDossierFlag(EnvoiItemSimpleModel currentItem, DossierComplexModel currentDossier,
            EnvoiTemplateComplexModel currentTemplate) {
        if (!JadeStringUtil.isBlankOrZero(currentTemplate.getEnvoiTemplateSimpleModel().getCodeEtatDossier())) {

            String msgError = getAllErrorsFromJadeThread();
            if (msgError.length() > 0) {
                JadeThread.logClear();
            }

            DossierModel dossierModel = currentDossier.getDossierModel();
            dossierModel.setEtatDossier(currentTemplate.getEnvoiTemplateSimpleModel().getCodeEtatDossier());
            currentDossier.setDossierModel(dossierModel);
            try {
                // dossierModel = ALServiceLocator.getDossierModelService().update(dossierModel);
                currentDossier = ALServiceLocator.getDossierBusinessService().updateDossier(currentDossier, null,
                        ALConstJournalisation.CHANGEMENT_ETAT_DOSSIER_REMARQUE_LETTRE_ENVOI);
            } catch (Exception ex) {
                if (msgError.length() > 0) {
                    JadeThread.logError(toString(), msgError);
                }
                JadeThread.logError(toString(), "Error updating the dossier Model " + currentDossier.getId() + " : "
                        + ex.getMessage() + "-" + ex.getCause());
                return true;
            }
            if (msgError.length() > 0) {
                JadeThread.logError(toString(), msgError);
            }
        }
        return false;
    }

    /**
     * 4ème étape, lectures des erreurs ouvertes et sauvegarde en base
     * 
     * @param bHasError
     * @param currentItem
     * @param currentDossier
     * @param currentTemplate
     * @return true si une erreur est rencontrée, false sinon
     */
    private boolean processStep4ManageError(boolean bHasError, EnvoiItemSimpleModel currentItem,
            DossierComplexModel currentDossier, EnvoiTemplateComplexModel currentTemplate) {
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {

            String msgError = getAllErrorsFromJadeThread();
            JadeThread.logClear();
            JadeLogger.error(this, "Error for item : " + currentItem.getId() + " - " + msgError);

            // Mise à jour du current item
            if (msgError.length() > 254) {
                currentItem.setEnvoiError(msgError.substring(0, 254));
            } else {
                currentItem.setEnvoiError(msgError);
            }

            try {
                currentItem = ALImplServiceLocator.getEnvoiItemSimpleModelService().update(currentItem);
            } catch (Exception ex) {
                JadeThread.logError(toString(), msgError);
                JadeThread.logError(
                        toString(),
                        "Error updating the item envoi " + currentItem.getId() + " : " + ex.getMessage() + "-"
                                + ex.getCause());
                return true;
            }
            JadeThread.logError(toString(), msgError);
        } else {
            try {
                currentItem.setEnvoiError("");
                currentItem = ALImplServiceLocator.getEnvoiItemSimpleModelService().update(currentItem);
            } catch (Exception ex) {
                JadeThread.logError(
                        toString(),
                        "Error updating the item envoi " + currentItem.getId() + " : " + ex.getMessage() + "-"
                                + ex.getCause());
                return true;
            }
        }
        return false;

    }

    /**
     * 5ème étape, mise à jour du status de l'envoi (à SENT normalement)
     * 
     * @param currentItem
     * @param currentDossier
     * @param currentTemplate
     * @return true si une erreur est rencontrée, false sinon
     */
    private boolean processStep5UpdateStatus(EnvoiItemSimpleModel currentItem, DossierComplexModel currentDossier,
            EnvoiTemplateComplexModel currentTemplate) {
        String msgError = getAllErrorsFromJadeThread();
        JadeThread.logClear();

        currentItem.setEnvoiStatus(getNewStatus());
        try {
            currentItem = ALImplServiceLocator.getEnvoiItemSimpleModelService().update(currentItem);
        } catch (Exception ex) {
            if (msgError.length() > 0) {
                JadeThread.logError(toString(), msgError);
            }
            JadeThread.logError(
                    toString(),
                    "Error updating the item envoi " + currentItem.getId() + " : " + ex.getMessage() + "-"
                            + ex.getCause());
            return true;
        }
        if (msgError.length() > 0) {
            JadeThread.logError(toString(), msgError);
        }
        return false;
    }

    /**
     * Enregistrement des changements effectués
     * 
     * @return true si une erreur est rencontrée, false sinon
     */
    private String processStep6ErrorCommit() {
        String msgError = getAllErrorsFromJadeThread();
        JadeThread.logClear();
        if (msgError.length() > 0) {
            JadeLogger.error(this, msgError);
        }
        try {
            JadeThread.commitSession();
        } catch (Exception ex) {
            JadeLogger.error(this, ex.toString() + " - " + ex.getCause());
        }
        JadeThread.logClear();
        return msgError;
    }

    /**
     * @param idJobEnvoi
     *            the idJobEnvoi to set
     */
    public void setIdJobEnvoi(String idJobEnvoi) {
        this.idJobEnvoi = idJobEnvoi;
    }

    /**
     * @param newStatus
     *            the newStatus to set
     */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

}
