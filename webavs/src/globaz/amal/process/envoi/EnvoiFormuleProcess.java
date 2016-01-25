/**
 * 
 */
package globaz.amal.process.envoi;

import globaz.amal.process.AMALabstractProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMDocumentModeles;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.envois.AMEnvoiData;
import ch.globaz.amal.business.envois.AMEnvoiDataFactory;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistorique;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Traitement batch pour imprimer les documents topaz ou changement de status des envois
 * 
 * @author dhi
 * 
 */
public class EnvoiFormuleProcess extends AMALabstractProcess {
    // Variables dont nous pourrions avoir besoin

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean changeStatusOnly = false;
    private ArrayList<String> idItem = null;

    private String idJob = null;
    private String newStatus = null;

    private ArrayList<String> noGroupeMergeDone = null;

    private ArrayList<String> noGroupePublishDone = null;
    private ContainerParametres containerParametres = null;

    /**
     * Default constructor
     */
    public EnvoiFormuleProcess() {
        noGroupePublishDone = new ArrayList<String>();
        noGroupeMergeDone = new ArrayList<String>();
    }

    /**
     * Méthode appelée lors d'un changement de status
     * 
     * 
     * @param currentDetail
     * 
     * @return DocumentData renseigné pour TOPAZ ou null
     */
    private DocumentData applyChangeStatus(ComplexControleurEnvoiDetail currentDetail) {

        if (currentDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
            return null;
        } else {
            // ---------------------------------------------------------------------------------
            // Application du changement de status à new Status SENT
            // Provoque la journalisation et la mise à jour du detail famille
            // Les informations pertinentes se trouvent dès lors dans détail famille
            // ---------------------------------------------------------------------------------
            try {
                AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(
                        currentDetail.getIdStatus(), getNewStatus(), true, null);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
            // ---------------------------------------------------------------------------------
            // Récupération du documentdata Lorsque le status a été passé à SENT
            // ---------------------------------------------------------------------------------
            if (getNewStatus().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                if (currentDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())) {
                    return applyChangeStatusWordML(currentDetail);
                } else {
                    return applyChangeStatusTopaz(currentDetail);
                }
            } else {
                return null;
            }
        }

    }

    /**
     * Méthode appelée lors d'un changement de status, document TOPAZ
     * 
     * @param currentDetail
     * @return DocumentData renseigné ou null si problème
     */
    private DocumentData applyChangeStatusTopaz(ComplexControleurEnvoiDetail currentDetail) {

        // ---------------------------------------------------------------------------------
        // Récupération de l'id formule
        // ---------------------------------------------------------------------------------
        String idFormule = null;
        FormuleListSearch formuleListSearch = new FormuleListSearch();
        // en fait, for libelle is for cs document...
        formuleListSearch.setForlibelle(currentDetail.getNumModele());
        formuleListSearch.setDefinedSearchSize(0);
        try {
            formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        if (formuleListSearch.getSize() == 1) {
            FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
            idFormule = formule.getId();
        } else {
            return null;
        }

        // ---------------------------------------------------------------------------------
        // Traitement pour générer les documents
        // ---------------------------------------------------------------------------------
        if (!mergeGroupIsDone(currentDetail)) {
            // Préparation de la map classe-id
            HashMap<Object, Object> mapIds = prepareMapIdForFusion(currentDetail);

            // Préparation de l'EnvoiData
            AMEnvoiData currentEnvoiData = AMEnvoiDataFactory.getAMEnvoiData(mapIds, idFormule);

            // Création du document
            try {
                Object returnDocument = ENServiceLocator.getDocumentMergeService().createDocument(currentEnvoiData);
                if (returnDocument instanceof DocumentData) {
                    return (DocumentData) returnDocument;
                } else {
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Méthode appelé par applychangestatus. On arrive dans cette fonction uniquement pour un status change à SENT
     * 
     * Applique le changement de status à un document non TOPAZ
     * 
     * @param currentDetail
     * 
     * @return toujours null
     */
    private DocumentData applyChangeStatusWordML(ComplexControleurEnvoiDetail currentDetail) {
        // ---------------------------------------------------------------------------------
        // Récupération des propriétés de publications
        // ---------------------------------------------------------------------------------
        try {
            Properties props = AmalServiceLocator.getDetailFamilleService().getGEDPublishProperties(
                    currentDetail.getIdDetailFamille(), currentDetail.getIdStatus());

            JadePublishDocumentInfo publishInfo = AmalServiceLocator.getControleurEnvoiService()
                    .getPublishInfoGEDDocument(currentDetail.getIdStatus());
            String fullFileName = AmalServiceLocator.getControleurEnvoiService()
                    .getInteractivDocumentFullFileNameFromJobServer(currentDetail.getIdStatus());
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
            }
            // on efface le fichier de toute manière (devrait être fait lors de l'étape de publication
            try {
                if (JadeFsFacade.exists(fullFileName)) {
                    JadeFsFacade.delete(fullFileName);
                }
            } catch (Exception ex) {
                JadeThread.logError(toString(),
                        "Error deleting document : " + ex.getLocalizedMessage() + " - " + ex.toString());
            }

        } catch (Exception ex) {
            JadeLogger.error(this, "Error creating index files for id status : " + currentDetail.getIdStatus() + ">> "
                    + ex.toString());
        }
        return null;
    }

    /**
     * Application de la demande d'impression TOPAZ
     * 
     * @param currentDetail
     * @return
     */
    private DocumentData applyPrintRequest(ComplexControleurEnvoiDetail currentDetail) {
        Boolean bError = false;
        String jobError = null;
        try {
            // ---------------------------------------------------------------------------------
            // Check le status du document, si déjà envoyé (sent), ne pas le prendre en compte
            // ---------------------------------------------------------------------------------
            if (currentDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                return null;
            }
            // ---------------------------------------------------------------------------------
            // Récupération de l'id formule
            // ---------------------------------------------------------------------------------
            String idFormule = null;
            FormuleListSearch formuleListSearch = new FormuleListSearch();
            // en fait, for libelle is for cs document...
            formuleListSearch.setForlibelle(currentDetail.getNumModele());
            formuleListSearch.setDefinedSearchSize(0);
            try {
                formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
            } catch (Exception ex) {
                ex.printStackTrace();
                bError = true;
                // On sauvegarde le message d'erreur pour aller l'inscrire dans le job afin d'avoir une trace
                jobError = ex.getMessage();
                JadeBusinessMessage msg = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                        .getName(), "Error(s) while generating document " + currentDetail.getNumModele() + " / Job "
                        + currentDetail.getIdJob() + " / idStatus " + currentDetail.getIdStatus() + " - " + jobError);
                getLogSession().addMessage(msg);
                JadeLogger.error(this, "Error generating document " + currentDetail.getNumModele() + " - Exception : "
                        + ex.toString());
            }
            if ((formuleListSearch.getSize() == 1) && (bError == false)) {
                FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
                idFormule = formule.getId();
            } else {
                bError = true;
                // On sauvegarde le message d'erreur pour aller l'inscrire dans le job afin d'avoir une trace
                jobError = "Formule not found : " + currentDetail.getNumModele();
                JadeBusinessMessage msg = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                        .getName(), "Error(s) while generating document " + currentDetail.getNumModele() + " / Job "
                        + currentDetail.getIdJob() + " / idStatus " + currentDetail.getIdStatus() + " - " + jobError);
                getLogSession().addMessage(msg);
                JadeLogger.error(this, "Error generating document " + currentDetail.getNumModele() + " - " + jobError);
            }
            // ---------------------------------------------------------------------
            // Traitement pour générer les documents
            // ---------------------------------------------------------------------
            Object returnDocument = null;
            if (!mergeGroupIsDone(currentDetail) && (bError == false)) {
                try {
                    // Préparation de la map classe-id
                    HashMap<Object, Object> mapIds = prepareMapIdForFusion(currentDetail);

                    // Préparation de l'EnvoiData
                    AMEnvoiData currentEnvoiData = AMEnvoiDataFactory.getAMEnvoiData(mapIds, idFormule);

                    // Création du document
                    returnDocument = ENServiceLocator.getDocumentMergeService().createDocument(currentEnvoiData);
                    if ((returnDocument instanceof DocumentData) == false) {
                        bError = true;
                    }
                } catch (Exception ex) {
                    bError = true;
                    // On sauvegarde le message d'erreur pour aller l'inscrire dans le job afin d'avoir une trace
                    jobError = ex.getMessage();
                    JadeBusinessMessage msg = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                            .getName(), "Error(s) while generating document " + currentDetail.getNumModele()
                            + " / Job " + currentDetail.getIdJob() + " / idStatus " + currentDetail.getIdStatus()
                            + " - " + jobError);
                    getLogSession().addMessage(msg);
                    JadeLogger.error(this, "Error generating document " + currentDetail.getNumModele()
                            + " - Exception : " + ex.toString());
                }
            }

            // ----------------------------------------------------------------------
            // Mise à jour du status du document dans tous les cas -- PRINTED / ERROR
            // ----------------------------------------------------------------------
            try {
                String statusEnvoi = IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue();
                if (bError) {
                    statusEnvoi = IAMCodeSysteme.AMDocumentStatus.ERROR.getValue();
                }
                AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(
                        currentDetail.getIdStatus(), statusEnvoi, true, jobError);
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }
            // ----------------------------------------------------------------------
            // Renvoi du documentdata
            // ----------------------------------------------------------------------
            if (bError) {
                return null;
            } else {
                return (DocumentData) returnDocument;
            }
        } catch (Exception e) {
            try {
                AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(
                        currentDetail.getIdStatus(), IAMCodeSysteme.AMDocumentStatus.ERROR.getValue(), true,
                        e.getMessage());
            } catch (Exception e2) {
                JadeBusinessMessage msg = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                        .getName(), "Erreur lors du changement de status : IdJob : " + currentDetail.getIdJob()
                        + " / IdStatus : " + currentDetail.getIdStatus() + " (" + e2.getMessage() + ")");
                getLogSession().addMessage(msg);
            }
            JadeBusinessMessage msg = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                    .getName(), e.getMessage());
            getLogSession().addMessage(msg);
            return null;

        }
    }

    /**
     * A appeler à la fin du process pour être sûr que le job ne reste pas en status in progress
     */
    private void ensureJobIsNotStillInProgress() {

        // Contrôle que nous n'ayons plus de status in progress (affichage bloquant) ?
        if (getIdItem().size() > 0) {
            try {
                String idEnvoiStatus = getIdItem().get(0);
                ComplexControleurEnvoiDetail currentDetail = retrieveComplexControleurEnvoiDetail(idEnvoiStatus);
                String currentIdJob = currentDetail.getIdJob();
                SimpleControleurJob currentJob = AmalImplServiceLocator.getSimpleControleurJobService().read(
                        currentIdJob);
                if (currentJob.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())) {
                    List<String> allStatus = new ArrayList<String>();
                    JadeLogger.error(null, "----------------------------------------------------------");
                    JadeLogger.error(null, "Error switching global status : " + currentJob.getIdJob());
                    JadeLogger.error(null, "Current value Job status : " + currentJob.getStatusEnvoi());
                    SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
                    statusSearch.setForIdJob(currentIdJob);
                    statusSearch.setDefinedSearchSize(0);
                    statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
                    statusSearch.setDefinedSearchSize(0);
                    for (int iStatus = 0; iStatus < statusSearch.getSize(); iStatus++) {
                        SimpleControleurEnvoiStatus currentStatus = (SimpleControleurEnvoiStatus) statusSearch
                                .getSearchResults()[iStatus];
                        JadeLogger.error(null,
                                iStatus + " - Current value envoi status : " + currentStatus.getStatusEnvoi() + " - "
                                        + currentStatus.getIdStatus());
                        if (!allStatus.contains(currentStatus.getStatusEnvoi())) {
                            allStatus.add(currentStatus.getStatusEnvoi());
                        }
                    }
                    JadeLogger.error(null, "----------------------------------------------------------");
                    JadeLogger.error(null, "Résumé des status trouvés : ");
                    Iterator<String> iteratorStatus = allStatus.iterator();
                    while (iteratorStatus.hasNext()) {
                        String currentStatus = iteratorStatus.next();
                        JadeLogger.error(null, "Status trouvé : " + currentStatus);
                    }
                    if (allStatus.contains(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())) {
                        // au moins 1 enfant est encore en cours >> ne devrait jamais arriver
                        currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
                        currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                    } else {
                        // Les items en erreurs sont envoyés par e-mail, traiter le status standard
                        if (allStatus.contains(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue())) {
                            currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                        } else if (allStatus.contains(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue())) {
                            currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
                            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                        } else if (allStatus.contains(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue())) {
                            currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
                            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                        } else if (allStatus.contains(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())) {
                            currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
                            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                        } else if (allStatus.contains(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                            currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
                            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                        } else {
                            currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
                            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                        }
                    }
                    JadeThread.commitSession();
                    JadeLogger.error(null, "Status forcé à  : " + currentJob.getStatusEnvoi());
                    JadeLogger.error(null, "----------------------------------------------------------");

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @return the changeStatusOnly
     */
    public boolean getChangeStatusOnly() {
        return changeStatusOnly;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Génération de documents AMAL";
    }

    /**
     * @return the idItem
     */
    public ArrayList<String> getIdItem() {
        return idItem;
    }

    /**
     * Gets all id for current job
     * 
     * @param currentJob
     * 
     * @return arraylist renseignée
     */
    private ArrayList<String> getIdItemFromJob(SimpleControleurJob currentJob) {
        ArrayList<String> allIds = new ArrayList<String>();
        try {
            SimpleControleurEnvoiStatusSearch envoiSearch = new SimpleControleurEnvoiStatusSearch();
            envoiSearch.setForIdJob(currentJob.getId());
            envoiSearch.setDefinedSearchSize(0);
            envoiSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(envoiSearch);
            for (int iEnvoiStatus = 0; iEnvoiStatus < envoiSearch.getSize(); iEnvoiStatus++) {
                SimpleControleurEnvoiStatus currentEnvoi = (SimpleControleurEnvoiStatus) envoiSearch.getSearchResults()[iEnvoiStatus];
                if (currentEnvoi != null) {
                    allIds.add(currentEnvoi.getId());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allIds;
    }

    /**
     * @return the idJob
     */
    public String getIdJob() {
        return idJob;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * @return the newStatus
     */
    public String getNewStatus() {
        return newStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.AbstractJadeJob#jobQueueName()
     */
    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    /**
     * Ajout du detail courant dans les groupes traités si nécessaire et retourne true si le groupe avait déjà été
     * traité
     * 
     * @param currentDetail
     * 
     * @return vrai si le groupe avait déjà été traité
     */
    private boolean mergeGroupIsDone(ComplexControleurEnvoiDetail currentDetail) {

        SimpleControleurEnvoiStatus currentEnvoi = new SimpleControleurEnvoiStatus();
        try {
            currentEnvoi = AmalServiceLocator.getControleurEnvoiService().readSimpleEnvoiStatus(
                    currentDetail.getIdStatus());
            if ((currentEnvoi.getNoGroupe() != null) && !currentEnvoi.getNoGroupe().equals("0")) {
                if (noGroupeMergeDone.contains(currentEnvoi.getNoGroupe())) {
                    // Déjà présent, ne pas traiter
                    return true;
                } else {
                    // Pas encore présent, à ajouter et à traiter
                    noGroupeMergeDone.add(currentEnvoi.getNoGroupe());
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    private void initParamAnnuels() {
        try {
            containerParametres = new ContainerParametres();
            SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
            simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService().search(
                    simpleParametreAnnuelSearch);
            containerParametres
                    .setParametresAnnuelsProvider(new ParametresAnnuelsProvider(simpleParametreAnnuelSearch));
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading parametre annuels --> " + e.getMessage());
        }
    }

    /**
     * Préparation du tableau d'id nécessaire au merge du document topaz
     * 
     * @param currentDetail
     *            information du document courant
     * @return map renseignée
     */
    private HashMap<Object, Object> prepareMapIdForFusion(ComplexControleurEnvoiDetail currentDetail) {
        // Tableau class, id à remplir pour préparer le merge du document topaz
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        try {
            SimpleDetailFamille currentDetailFamille = AmalServiceLocator.getDetailFamilleService().read(
                    currentDetail.getIdDetailFamille());
            if (currentDetailFamille.getAnneeHistorique() != null) {
                // Collecte des id nécessaires
                String anneeHistorique = currentDetailFamille.getAnneeHistorique();
                String idContribuable = currentDetailFamille.getIdContribuable();
                String idDetailFamille = currentDetailFamille.getIdDetailFamille();
                String idFamille = currentDetailFamille.getIdFamille();
                // Renseigne information de revenu lié
                RevenuHistoriqueSearch revenuSearch = new RevenuHistoriqueSearch();
                revenuSearch.setForAnneeHistorique(anneeHistorique);
                revenuSearch.setForRevenuActif(true);
                revenuSearch.setForIdContribuable(idContribuable);
                try {
                    revenuSearch = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService().search(revenuSearch);
                    String revDet = "0";
                    if (revenuSearch.getSize() > 0) {
                        RevenuHistorique revenu = (RevenuHistorique) revenuSearch.getSearchResults()[0];
                        revDet = revenu.getRevenuDeterminantCalcul();
                        map.put(RevenuHistoriqueComplex.class.getName(), revenu.getId());
                    }

                    // Recherche du revenu déterminant pour la demande de recalcul
                    revenuSearch = new RevenuHistoriqueSearch();
                    revenuSearch.setForAnneeHistorique(anneeHistorique);
                    revenuSearch.setForIdContribuable(idContribuable);
                    revenuSearch.setForIsRecalcul(true);
                    revenuSearch = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService().search(revenuSearch);
                    if (revenuSearch.getSize() > 0) {
                        RevenuHistorique revenu = (RevenuHistorique) revenuSearch.getSearchResults()[0];
                        map.put(SimpleRevenuDeterminant.class.getName(), revenu.getIdRevenuDeterminant());
                    }

                    String revDetFormate = JANumberFormatter.fmt(revDet, false, false, false, 0);
                    Integer i_revDet = new Integer(revDetFormate);
                    ArrayList<String> arrayIdEnfants = new ArrayList<String>();

                    int revenuMinSubside = Integer.parseInt(containerParametres.getParametresAnnuelsProvider()
                            .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_REVENU_MIN_SUBSIDE)
                            .getFormatedValueByYear(anneeHistorique, null, false, false, false, 0));
                    int revenuMaxSubside = Integer.parseInt(containerParametres.getParametresAnnuelsProvider()
                            .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_REVENU_MAX_SUBSIDE)
                            .getFormatedValueByYear(anneeHistorique, null, false, false, false, 0));

                    // Si c'est une famille moyenne, on ne peut pas récupérer le subside des parents étant donnée qu'il
                    // n'y en a pas. On va donc aller rechercher celui d'un des enfants
                    if ((i_revDet >= revenuMinSubside) && (i_revDet <= revenuMaxSubside)) {
                        SimpleDetailFamilleSearch detailFamilleSearch = new SimpleDetailFamilleSearch();
                        detailFamilleSearch.setForAnneeHistorique(anneeHistorique);
                        detailFamilleSearch.setForCodeActif(true);
                        detailFamilleSearch.setForIdContribuable(idContribuable);
                        ArrayList<String> noModelDecmstEnfant = new ArrayList<String>();
                        // DECMST8 pour les enfants
                        noModelDecmstEnfant.add(AMDocumentModeles.DECMST8.getValue());
                        detailFamilleSearch.setInNoModeles(noModelDecmstEnfant);
                        detailFamilleSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
                                detailFamilleSearch);

                        for (JadeAbstractModel modelSimpleDetailFamille : detailFamilleSearch.getSearchResults()) {
                            SimpleDetailFamille simpleDetailFamille = (SimpleDetailFamille) modelSimpleDetailFamille;

                            arrayIdEnfants.add(simpleDetailFamille.getIdDetailFamille());
                        }
                    }

                    SimpleDocumentSearch simpleDocumentSearch = new SimpleDocumentSearch();
                    ArrayList<String> listNumModels = new ArrayList<String>();

                    if (arrayIdEnfants.size() == 0) {
                        listNumModels.add(AMDocumentModeles.DECMST1.getValue());
                        listNumModels.add(AMDocumentModeles.DECMST2.getValue());
                        listNumModels.add(AMDocumentModeles.DECMST3.getValue());
                        listNumModels.add(AMDocumentModeles.DECMST5.getValue());
                        simpleDocumentSearch.setInNumModele(listNumModels);
                        simpleDocumentSearch.setForIdDetailFamille(idDetailFamille);
                    } else {
                        listNumModels.add(AMDocumentModeles.DECMST8.getValue());
                        simpleDocumentSearch.setInNumModele(listNumModels);
                        simpleDocumentSearch.setInIdDetailFamille(arrayIdEnfants);
                    }
                    simpleDocumentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(
                            simpleDocumentSearch);

                    if (simpleDocumentSearch.getSize() > 0) {
                        SimpleDocument simpleDocument = (SimpleDocument) simpleDocumentSearch.getSearchResults()[0];
                        map.put(SimpleDocument.class.getName(), simpleDocument.getId());
                    }

                    SimpleSubsideAnneeSearch simpleSubsideAnneeSearch = new SimpleSubsideAnneeSearch();
                    simpleSubsideAnneeSearch.setForAnneeSubside(anneeHistorique);
                    simpleSubsideAnneeSearch.setForLimiteRevenuGOE(revDet);
                    simpleSubsideAnneeSearch = AmalServiceLocator.getSimpleSubsideAnneeService().search(
                            simpleSubsideAnneeSearch);
                    SimpleSubsideAnnee simpleSubsideAnnee = (SimpleSubsideAnnee) simpleSubsideAnneeSearch
                            .getSearchResults()[0];
                    map.put(SimpleSubsideAnnee.class.getName(), simpleSubsideAnnee.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Renseignement de la map
                map.put(Contribuable.class.getName(), idContribuable);
                map.put(SimpleDetailFamille.class.getName(), idDetailFamille);
                map.put(SimpleFamille.class.getName(), idFamille);
                BSession currentSession = BSessionUtil.getSessionFromThreadContext();
                map.put(currentSession.getClass().getName(), currentSession.getUserId());
                map.put(SimpleControleurEnvoiStatus.class.getName(), currentDetail.getIdStatus());
                map.put(SimpleControleurJob.class.getName(), currentDetail.getIdJob());
            }
        } catch (Exception ex) {
            return new HashMap<Object, Object>();
        }
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.amal.process.AMALabstractProcess#process()
     */
    @Override
    protected void process() {
        // try {
        // TopazSystem.getInstance().getDocBuilder().setOpenedDocumentsVisible(true);
        // } catch (Exception e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        // TODO : COMMIT OU ROLLBACK EN FONCTION DES ERREURS

        // -------------------------------------------------------------
        // 1) Check si génération pour un job
        // et renseignement de l'arraylist<idEnvoiStatus> en conséquence
        // le status de tous les détail passe à in progress
        // -------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(getIdJob())) {
            SimpleControleurJob currentJob = retrieveControleurJob(getIdJob());
            if (currentJob != null) {
                setIdItem(getIdItemFromJob(currentJob));
                super.getProgressHelper().setMax(getIdItem().size());
            }
        }
        // ---------------------------------------------------------------------------------
        // 2) Mise à jour du status du document dans tous les cas -- IN PROGRESS
        // ---------------------------------------------------------------------------------
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        for (int iElement = 0; iElement < getIdItem().size(); iElement++) {
            String idEnvoiStatus = getIdItem().get(iElement);
            try {
                AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(idEnvoiStatus,
                        IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue(), false, null);
                JadeThread.commitSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // -------------------------------------------------------------
        // 3) Traitement pour tous les éléments et renseignement du container
        // -------------------------------------------------------------
        if (containerParametres == null) {
            // this.initParamAnnuels(simpleDetailFamille.getAnneeHistorique());
            initParamAnnuels();
        }

        for (int iElement = 0; iElement < getIdItem().size(); iElement++) {
            super.getProgressHelper().setCurrent(iElement + 1);
            String idEnvoiStatus = getIdItem().get(iElement);
            ComplexControleurEnvoiDetail currentDetail = retrieveComplexControleurEnvoiDetail(idEnvoiStatus);
            if (currentDetail != null) {
                if (getChangeStatusOnly()) {
                    // -------------------------------------------------------------
                    // PUBLICATION DOCUMENT PAR DOCUMENT >> DANS GED si status sent
                    // -------------------------------------------------------------
                    DocumentData currentDocumentData = applyChangeStatus(currentDetail);
                    if (currentDocumentData != null) {
                        JadePublishDocumentInfo pubGEDInfos = null;
                        try {
                            pubGEDInfos = AmalServiceLocator.getControleurEnvoiService().getPublishInfoGEDDocument(
                                    idEnvoiStatus);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        // Ajout du document si pas lié à un groupe déjà traité
                        if (!publishGroupIsDone(currentDetail)) {
                            container.addDocument(currentDocumentData, pubGEDInfos);
                        }
                    }
                } else {
                    DocumentData currentDocumentData = applyPrintRequest(currentDetail);
                    if (currentDocumentData != null) {
                        // -------------------------------------------------------------
                        // PUBLICATION DE L'ENSEMBLE DES DOCUMENTS >> IMPRESSION
                        // -------------------------------------------------------------
                        JadePublishDocumentInfo pubPrintInfos = null;
                        if (getIdItem().size() >= 1) {
                            try {
                                if (JadeStringUtil.isEmpty(getIdJob())) {
                                    pubPrintInfos = AmalServiceLocator.getControleurEnvoiService()
                                            .getPublishInfoPrintDocument(currentDetail.getIdStatus(), true);
                                } else {
                                    pubPrintInfos = AmalServiceLocator.getControleurEnvoiService()
                                            .getPublishInfoPrintDocument(currentDetail.getIdStatus(), false);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                pubPrintInfos = null;
                            }
                        }
                        // Ajout du document si pas lié à un groupe déjà traité
                        if (!publishGroupIsDone(currentDetail)) {
                            container.addDocument(currentDocumentData, pubPrintInfos);
                        }
                    }
                }
            } else {
                // ---------------------------------------------------------------------------------
                // Mise à jour du status du document dans tous les cas -- ERROR
                // ---------------------------------------------------------------------------------
                // On sauvegarde le message d'erreur pour aller l'inscrire dans le job afin d'avoir une trace
                JadeBusinessMessage msg = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                        .getName(), "Error(s) while generating document for idStatus " + idEnvoiStatus
                        + " - Status/envoi not complete");
                getLogSession().addMessage(msg);
                try {
                    AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(idEnvoiStatus,
                            IAMCodeSysteme.AMDocumentStatus.ERROR.getValue(), true,
                            msg.getContents(JadeThread.currentLanguage()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // -------------------------------------------------------------
        // 4) set the print publication for job print only, GED is set inside the process
        // -------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(getIdJob())) {
            JadePublishDocumentInfo pubPrintInfos = null;
            try {
                if (!getChangeStatusOnly()) {
                    pubPrintInfos = AmalServiceLocator.getControleurEnvoiService().getPublishInfoPrintJob(getIdJob());
                }
                container.setMergedDocDestination(pubPrintInfos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // -------------------------------------------------------------
        // 5) CREATE THE DOCUMENT
        // -------------------------------------------------------------
        if (container != null) {
            try {
                this.createDocuments(container);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // -------------------------------------------------------------
        // 6) ENSURE JOB IS NOT STILL IN PROGRESS
        // -------------------------------------------------------------
        ensureJobIsNotStillInProgress();

        // -------------------------------------------------------------
        // 7) ENVOI E-MAIL DE COMPLETION SI ERREUR
        // -------------------------------------------------------------
        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            ArrayList<String> emailAdresses = new ArrayList<String>();
            emailAdresses.add(BSessionUtil.getSessionFromThreadContext().getUserEMail());
            try {
                super.sendCompletionMail(emailAdresses);
            } catch (Exception e) {
                JadeThread.logError(this.getClass().getName(), "Error while sending mail ! --> " + e.getMessage());
            }
        }
        if (!JadeStringUtil.isEmpty(getIdJob())) {
            SimpleControleurJob currentJob = retrieveControleurJob(getIdJob());
            if ((currentJob != null) && currentJob.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())) {
                String message = "Les documents ont été exporté avec succès dans le répertoire standard AMAL";
                try {
                    JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                            "Web@Amal >> fin du job", message, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Ajout du detail courant dans les groupes traités si nécessaire et retourne true si le groupe avait déjà été
     * traité
     * 
     * @param currentDetail
     * 
     * @return vrai si le groupe avait déjà été traité
     */
    private boolean publishGroupIsDone(ComplexControleurEnvoiDetail currentDetail) {

        SimpleControleurEnvoiStatus currentEnvoi = new SimpleControleurEnvoiStatus();
        try {
            currentEnvoi = AmalServiceLocator.getControleurEnvoiService().readSimpleEnvoiStatus(
                    currentDetail.getIdStatus());
            if ((currentEnvoi.getNoGroupe() != null) && !currentEnvoi.getNoGroupe().equals("0")) {
                if (noGroupePublishDone.contains(currentEnvoi.getNoGroupe())) {
                    // Déjà présent, ne pas traiter
                    return true;
                } else {
                    // Pas encore présent, à ajouter et à traiter
                    noGroupePublishDone.add(currentEnvoi.getNoGroupe());
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Read the current complexcontroleurenvoidetail
     * 
     * @param idEnvoiDetail
     *            current idEnvoiDetail
     * @return the complexcontroleurenvoidetail if ok, null otherwise
     * 
     */
    private ComplexControleurEnvoiDetail retrieveComplexControleurEnvoiDetail(String idEnvoiDetail) {
        try {
            ComplexControleurEnvoiDetailSearch detailSearch = new ComplexControleurEnvoiDetailSearch();
            detailSearch.setForIdStatus(idEnvoiDetail);
            detailSearch.setDefinedSearchSize(0);
            detailSearch = AmalServiceLocator.getControleurEnvoiService().search(detailSearch);
            if (detailSearch.getSize() > 0) {
                return (ComplexControleurEnvoiDetail) detailSearch.getSearchResults()[0];
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    /**
     * Read the current Job
     * 
     * @param idJob
     *            current idJob
     * @return the job if ok, null otherwise
     */
    private SimpleControleurJob retrieveControleurJob(String idJob) {
        try {
            return AmalImplServiceLocator.getSimpleControleurJobService().read(idJob);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param changeStatusOnly
     *            the changeStatusOnly to set
     */
    public void setChangeStatusOnly(boolean changeStatusOnly) {
        this.changeStatusOnly = changeStatusOnly;
    }

    /**
     * @param idItem
     *            the idItem to set
     */
    public void setIdItem(ArrayList<String> idItem) {
        this.idItem = idItem;
    }

    /**
     * @param idJob
     *            the idJob to set
     */
    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    /**
     * @param newStatus
     *            the newStatus to set
     */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

}
