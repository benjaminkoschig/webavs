/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.controleurEnvoi;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoi;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author DHI
 * 
 */
public class ControleurEnvoiServiceImpl implements ControleurEnvoiService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#changeStatus(ch.globaz.amal.business
     * . models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurEnvoi changeStatus(String idJob, String newStatus) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, AnnonceException, DocumentException,
            ControleurJobException {
        if (JadeStringUtil.isEmpty(idJob)) {
            throw new ControleurEnvoiException("Unable to change status, the idJob passed is empty");
        }

        if (!newStatus.equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())) {
            // generate the status for each document
            SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
            statusSearch.setForIdJob(idJob);
            statusSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
            for (int iStatus = 0; iStatus < statusSearch.getSize(); iStatus++) {
                SimpleControleurEnvoiStatus status = (SimpleControleurEnvoiStatus) statusSearch.getSearchResults()[iStatus];
                status = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(
                        status.getIdStatus(), newStatus, false, null);
            }
        }

        // change the status of the job
        SimpleControleurJobSearch jobSearch = new SimpleControleurJobSearch();
        jobSearch.setForIdJob(idJob);
        jobSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        jobSearch = AmalImplServiceLocator.getSimpleControleurJobService().search(jobSearch);
        // le status du job est contrôlé lors de la mise à jour
        for (int iJob = 0; iJob < jobSearch.getSize(); iJob++) {
            SimpleControleurJob job = (SimpleControleurJob) jobSearch.getSearchResults()[iJob];
            // Si le nouveau status est à in progress, force à in progress
            // autrement, génération
            if (newStatus.equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())) {
                job.setStatusEnvoi(newStatus);
                job = AmalImplServiceLocator.getSimpleControleurJobService().update(job);
            } else {
                String generatedStatus = generateStatus(job.getIdJob());
                job.setStatusEnvoi(generatedStatus);
                job = AmalImplServiceLocator.getSimpleControleurJobService().update(job);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#count(ch.globaz.amal.business.
     * models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public int count(ComplexControleurAnnonceDetailSearch search) throws JadePersistenceException {
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#count(ch.globaz.amal.business.
     * models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public int count(ComplexControleurEnvoiDetailSearch search) throws JadePersistenceException {
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#count(ch.globaz.amal.business.
     * models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public int count(ComplexControleurEnvoiSearch search) throws JadePersistenceException {
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#create(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoi)
     */
    @Override
    public ComplexControleurEnvoi create(ComplexControleurEnvoi controleurEnvoi) throws JadePersistenceException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#create(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoi)
     */
    @Override
    public ComplexControleurEnvoiDetail create(ComplexControleurEnvoiDetail controleurEnvoi)
            throws JadePersistenceException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#delete(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurEnvoi delete(ComplexControleurEnvoi controleurEnvoi) throws JadePersistenceException {
        if (controleurEnvoi == null) {
            // TODO : create exception for controleurEnvoi *
        }

        return controleurEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#delete(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurEnvoiDetail delete(ComplexControleurEnvoiDetail controleurEnvoi)
            throws JadePersistenceException {
        if (controleurEnvoi == null) {
            // TODO : create exception for controleurEnvoi *
        }
        return controleurEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#delete(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurEnvoi delete(String idJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, ControleurJobException {
        if (JadeStringUtil.isEmpty(idJob)) {
            throw new ControleurEnvoiException("unable to delete the job, the id passed is empty");
        }
        // -----------------------------------------------------------
        // delete the specific document status
        // -----------------------------------------------------------
        SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
        statusSearch.setForIdJob(idJob);
        statusSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
        // le status du document est contrôlé lors de la suppression. Suppression impossible d'un status sent
        for (int iStatus = 0; iStatus < statusSearch.getSize(); iStatus++) {
            SimpleControleurEnvoiStatus status = (SimpleControleurEnvoiStatus) statusSearch.getSearchResults()[iStatus];
            status = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().delete(status);
        }
        // -----------------------------------------------------------
        // Effacement du job uniquement si le nombre d'élément lié est à 0
        // Autrement raffraichissement du status du job
        // -----------------------------------------------------------
        statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
        if (statusSearch.getSize() > 0) {
            String newStatus = AmalServiceLocator.getControleurEnvoiService().generateStatus(idJob);
            SimpleControleurJob currentJob = AmalServiceLocator.getControleurEnvoiService().readSimpleControleurJob(
                    idJob);
            if ((currentJob != null) && (currentJob.getStatusEnvoi() != null)
                    && !newStatus.equals(currentJob.getStatusEnvoi())) {
                currentJob.setStatusEnvoi(newStatus);
                AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
            }
        } else {
            SimpleControleurJobSearch jobSearch = new SimpleControleurJobSearch();
            jobSearch.setForIdJob(idJob);
            jobSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            jobSearch = AmalImplServiceLocator.getSimpleControleurJobService().search(jobSearch);
            // le status du job est contrôlé lors de la suppression
            for (int iJob = 0; iJob < jobSearch.getSize(); iJob++) {
                SimpleControleurJob job = (SimpleControleurJob) jobSearch.getSearchResults()[iJob];
                job = AmalImplServiceLocator.getSimpleControleurJobService().delete(job);
            }
        }
        return null;
    }

    /**
     * Retourne le status global du job selon la règle : 1) généré auto >> 1 à n document généré auto, pour le job
     * manualqueue 2)généré manuel >> 1 à n document généré manuel, pour le job manualedit 3) imprimé/envoyé >> tous les
     * documents avec le même status
     * 
     * @param jobId
     * @param searchedJob
     * @param searchedJobDetail
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    @Override
    public String generateStatus(String idJob) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        ComplexControleurEnvoiSearch searchedJob = new ComplexControleurEnvoiSearch();
        searchedJob.setForIdJob(idJob);
        searchedJob.setDefinedSearchSize(0);
        searchedJob = AmalServiceLocator.getControleurEnvoiService().search(searchedJob);
        ArrayList<String> currentStatus = new ArrayList<String>();
        String csReturn = "";
        String csJobType = "";
        if (searchedJob != null) {
            for (int iIndex = 0; iIndex < searchedJob.getSize(); iIndex++) {
                ComplexControleurEnvoi controleurEnvoi = (ComplexControleurEnvoi) searchedJob.getSearchResults()[iIndex];
                currentStatus.add(controleurEnvoi.getStatusEnvoi());
                csJobType = controleurEnvoi.getTypeJob();
            }
        }

        // Valeur par défaut csReturn
        if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())) {
            csReturn = IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue();
        } else {
            csReturn = IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue();
        }

        boolean bAutoGenerated = currentStatus.contains(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
        boolean bManualGenerated = currentStatus.contains(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
        boolean bSent = currentStatus.contains(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
        boolean bPrinted = currentStatus.contains(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
        boolean bInProgress = currentStatus.contains(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());

        if (bInProgress) {
            csReturn = IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue();
        }
        // check status printed
        else if (bPrinted) {
            // Check si il n'y a pas d'autre status dans le lot
            // Sent est assimilé à imprimé
            if (!(bAutoGenerated || bManualGenerated)) {
                csReturn = IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue();
            } else if (bAutoGenerated) {
                csReturn = IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue();
            } else if (bManualGenerated) {
                csReturn = IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue();
            }
            // Pas de status bPrinted, trouvé
            // check status Sent
        } else if (bSent) {
            // Check si il n'y a pas d'autre status dans le lot
            if (!(bAutoGenerated || bManualGenerated)) {
                csReturn = IAMCodeSysteme.AMDocumentStatus.SENT.getValue();
            } else if (bAutoGenerated) {
                csReturn = IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue();
            } else if (bManualGenerated) {
                csReturn = IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue();
            }
        }
        return csReturn;

    }

    @Override
    public String getInteractivDocumentFullFileName(String idStatusEnvoi) {

        // -------------------------------------------------------------
        // Retrieve the status envoi
        // -------------------------------------------------------------
        SimpleControleurEnvoiStatus envoiStatus = null;
        try {
            envoiStatus = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read(idStatusEnvoi);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        // -------------------------------------------------------------
        // Retrieve the job
        // -------------------------------------------------------------
        SimpleControleurJob currentJob = null;
        try {
            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().read(envoiStatus.getIdJob());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        // -------------------------------------------------------------
        // Retrieve the document
        // -------------------------------------------------------------
        SimpleDocument currentDocument = null;
        try {
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read(envoiStatus.getIdEnvoi());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

        try {
            String csDateComplete = currentJob.getDateJob() + "_"
                    + currentDocument.getLibelleEnvoi().substring(0, 8).trim();
            String idDetailFamille = currentDocument.getIdDetailFamille();
            String csFormule = currentDocument.getNumModele();

            return AmalServiceLocator.getDetailFamilleService().getInteractivDocumentFilePathFromClient(csDateComplete,
                    idDetailFamille, csFormule, csFormule);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public String getInteractivDocumentFullFileNameFromJobServer(String idStatusEnvoi) {
        // -------------------------------------------------------------
        // Retrieve the status envoi
        // -------------------------------------------------------------
        SimpleControleurEnvoiStatus envoiStatus = null;
        try {
            envoiStatus = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read(idStatusEnvoi);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        // -------------------------------------------------------------
        // Retrieve the job
        // -------------------------------------------------------------
        SimpleControleurJob currentJob = null;
        try {
            currentJob = AmalImplServiceLocator.getSimpleControleurJobService().read(envoiStatus.getIdJob());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        // -------------------------------------------------------------
        // Retrieve the document
        // -------------------------------------------------------------
        SimpleDocument currentDocument = null;
        try {
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read(envoiStatus.getIdEnvoi());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

        try {
            String csDateComplete = currentJob.getDateJob() + "_"
                    + currentDocument.getLibelleEnvoi().substring(0, 8).trim();
            String idDetailFamille = currentDocument.getIdDetailFamille();
            String csFormule = currentDocument.getNumModele();

            return AmalServiceLocator.getDetailFamilleService().getInteractivDocumentFilePathFromJobServer(
                    csDateComplete, idDetailFamille, csFormule, csFormule);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public ArrayList<ComplexControleurEnvoiDetail> getListComplexControleurEnvoiDetail(String idJob) {
        ArrayList<ComplexControleurEnvoiDetail> allResults = new ArrayList<ComplexControleurEnvoiDetail>();
        ComplexControleurEnvoiDetailSearch searchModel = new ComplexControleurEnvoiDetailSearch();
        searchModel.setForIdJob(idJob);
        try {
            searchModel = this.search(searchModel);
            for (int iDetail = 0; iDetail < searchModel.getSize(); iDetail++) {
                ComplexControleurEnvoiDetail currentDetail = (ComplexControleurEnvoiDetail) searchModel
                        .getSearchResults()[iDetail];
                allResults.add(currentDetail);
            }
        } catch (Exception ex) {
            JadeLogger.error(this,
                    "Error getting all complexcontroleurenvoidetail for job id " + idJob + " : " + ex.getMessage());
        }
        return allResults;
    }

    @Override
    public JadePublishDocumentInfo getPublishInfoGEDDocument(SimpleControleurEnvoiStatus statusEnvoi) {

        SimpleDetailFamille detailFamille = null;
        SimpleDocument document = null;
        SimpleAnnonce annonce = null;
        String idDetailFamille = null;

        if (statusEnvoi.getTypeEnvoi().equals(IAMCodeSysteme.AMDocumentType.ENVOI.getValue())) {
            try {
                document = AmalImplServiceLocator.getSimpleDocumentService().read(statusEnvoi.getIdEnvoi());
                idDetailFamille = document.getIdDetailFamille();
            } catch (Exception ex) {
                JadeLogger
                        .error(this, "Error getting simpleDocument " + statusEnvoi.getIdEnvoi() + " " + ex.toString());
            }
        } else {
            try {
                annonce = AmalImplServiceLocator.getSimpleAnnonceService().read(statusEnvoi.getIdAnnonce());
                idDetailFamille = annonce.getIdDetailFamille();
            } catch (Exception ex) {
                JadeLogger.error(this, "Error getting simpleAnnonce " + statusEnvoi.getIdEnvoi() + " " + ex.toString());
            }
        }

        try {
            detailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(idDetailFamille);
        } catch (Exception ex) {
            JadeLogger.error(this, "Error getting simple famille " + idDetailFamille + " " + ex.toString());
        }

        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
        if ((detailFamille != null) && (statusEnvoi != null)) {
            try {
                Properties gedProperties = AmalServiceLocator.getDetailFamilleService().getGEDPublishProperties(
                        detailFamille, statusEnvoi);
                pubInfos.setApplicationDomain("AMAL");
                pubInfos.setDocumentDate(gedProperties.getProperty(JadeGedTargetProperties.CREATION_DATE));
                pubInfos.setDocumentType(gedProperties.getProperty(JadeGedTargetProperties.DOCUMENT_TYPE));
                pubInfos.setDocumentTypeNumber(gedProperties.getProperty(JadeGedTargetProperties.DOCUMENT_TYPE_NUMBER));
                pubInfos.setDocumentProperty("gedTypeLamal", "GED_FOR_LAMAL");
                // autres informations
                Iterator<Object> keyIterator = gedProperties.keySet().iterator();
                while (keyIterator.hasNext()) {
                    Object currentKey = keyIterator.next();
                    if (currentKey instanceof String) {
                        pubInfos.setDocumentProperty((String) currentKey,
                                gedProperties.getProperty((String) currentKey));
                    }
                }
                // pour les valeurs nss inexistantes, configurer un NSS bidon
                // TEST PROD 29.05.2012
                try {
                    String NNSS = pubInfos.getDocumentProperty("pyxis.tiers.numero.avs.non.formatte");
                    if (JadeStringUtil.isEmpty(NNSS)) {
                        pubInfos.setDocumentProperty("pyxis.tiers.numero.avs.non.formatte", "0990000000000");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // ged
                pubInfos.setArchiveDocument(true);
                // mail
                pubInfos.setPublishDocument(false);
            } catch (Exception ex) {
                JadeLogger.error(this, "Error getting GED Publish properties " + ex.toString());
            }
        } else {
            // ged
            pubInfos.setArchiveDocument(false);
            // mail
            pubInfos.setPublishDocument(false);
        }

        return pubInfos;
    }

    @Override
    public JadePublishDocumentInfo getPublishInfoGEDDocument(String idStatusEnvoi) {
        SimpleControleurEnvoiStatus simpleStatus = null;
        try {
            simpleStatus = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read(idStatusEnvoi);
        } catch (Exception e) {
            JadeLogger.error(this, "Exception getting the status envoi " + idStatusEnvoi + " >> " + e.toString());
        }
        if (simpleStatus != null) {
            return this.getPublishInfoGEDDocument(simpleStatus);
        } else {
            JadeLogger.error(this, "GED Publish will failed for envoi " + idStatusEnvoi);
            return null;
        }
    }

    @Override
    public JadePublishDocumentInfo getPublishInfoPrintDocument(SimpleControleurEnvoiStatus statusEnvoi,
            boolean bSendEMail) {
        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        String documentTitle = "";
        String documentSubject = "";
        String csCodeUser = "";
        String documentLibelle = "";

        try {
            if (statusEnvoi.getTypeEnvoi().equals(IAMCodeSysteme.AMDocumentType.ENVOI.getValue())) {
                SimpleDocument document = AmalImplServiceLocator.getSimpleDocumentService().read(
                        statusEnvoi.getIdEnvoi());
                SimpleDetailFamille detail = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                        document.getIdDetailFamille());
                SimpleFamille famille = AmalImplServiceLocator.getSimpleFamilleService().read(detail.getIdFamille());
                csCodeUser = currentSession.getCode(document.getNumModele());
                documentLibelle = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().getDocumentLibelle(
                        csCodeUser, currentSession);
                documentTitle = "Envoi " + statusEnvoi.getIdStatus() + " " + famille.getNomPrenom() + " "
                        + documentLibelle;
                documentSubject = "Web@Amal >> " + documentLibelle + " " + famille.getNomPrenom() + " généré";
            } else {
                SimpleAnnonce annonce = AmalImplServiceLocator.getSimpleAnnonceService().read(
                        statusEnvoi.getIdAnnonce());
                SimpleDetailFamille detail = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                        annonce.getIdDetailAnnonce());
                SimpleFamille famille = AmalImplServiceLocator.getSimpleFamilleService().read(detail.getIdFamille());
                documentTitle = "Annonce " + statusEnvoi.getIdStatus() + " " + famille.getNomPrenom();
                documentSubject = "Web@Amal >> Annonce CM pour " + famille.getNomPrenom() + " générée";
            }
        } catch (Exception ex) {
            documentTitle = "Document " + statusEnvoi.getIdStatus();
            documentSubject = "Web@Amal >> Document généré";
        }
        // ged
        pubInfos.setArchiveDocument(false);
        if (bSendEMail) {
            // mail
            pubInfos.setPublishDocument(true);
            // owner e-mail
            pubInfos.setOwnerEmail(currentSession.getUserEMail());
            // destination e-mail
            pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, currentSession.getUserEMail());
        } else {
            // mail
            pubInfos.setPublishDocument(false);
        }
        // DocumentTitle
        pubInfos.setDocumentTitle(documentTitle);
        pubInfos.setDocumentSubject(documentSubject);
        pubInfos.setApplicationDomain("AMAL");
        pubInfos.setDuplex(true);
        pubInfos.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
        return pubInfos;
    }

    @Override
    public JadePublishDocumentInfo getPublishInfoPrintDocument(String idStatusEnvoi, boolean bSendEMail) {
        SimpleControleurEnvoiStatus simpleStatus = null;
        try {
            simpleStatus = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read(idStatusEnvoi);
        } catch (Exception e) {
            JadeLogger.error(this, "Exception getting the status envoi " + idStatusEnvoi + " >> " + e.toString());
        }
        if (simpleStatus != null) {
            return this.getPublishInfoPrintDocument(simpleStatus, bSendEMail);
        } else {
            return null;
        }
    }

    @Override
    public JadePublishDocumentInfo getPublishInfoPrintJob(SimpleControleurJob job) {
        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        // ged
        pubInfos.setArchiveDocument(false);
        // mail
        pubInfos.setPublishDocument(true);
        // owner e-mail
        pubInfos.setOwnerEmail(currentSession.getUserEMail());
        // destination e-mail
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, currentSession.getUserEMail());
        // DocumentTitle
        pubInfos.setDocumentTitle(job.getDescriptionJob());
        pubInfos.setDocumentSubject("Web@Amal >> Documents du job " + job.getIdJob() + " générés");
        pubInfos.setApplicationDomain("AMAL");
        pubInfos.setDocumentProperty("jobType", job.getTypeJob());
        // pubInfos.setDuplex(true);
        // pubInfos.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_FIRST);
        return pubInfos;
    }

    @Override
    public JadePublishDocumentInfo getPublishInfoPrintJob(String idJob) {
        SimpleControleurJob simpleJob = null;
        try {
            simpleJob = AmalImplServiceLocator.getSimpleControleurJobService().read(idJob);
        } catch (Exception e) {
            JadeLogger.error(this, "Exception getting the job " + idJob + " >> " + e.toString());
        }
        if (simpleJob != null) {
            return this.getPublishInfoPrintJob(simpleJob);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#read(java.lang.String)
     */
    @Override
    public ComplexControleurEnvoi read(String idControleurEnvoi) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#readSimpleControleurJob
     */
    @Override
    public SimpleControleurJob readSimpleControleurJob(String idControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException {
        return AmalImplServiceLocator.getSimpleControleurJobService().read(idControleurJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#readSimpleEnvoiStatus
     */
    @Override
    public SimpleControleurEnvoiStatus readSimpleEnvoiStatus(String idControleurEnvoiStatus)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read(idControleurEnvoiStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#search(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurAnnonceDetailSearch search(ComplexControleurAnnonceDetailSearch controleurAnnonceSearch)
            throws JadePersistenceException {
        if (controleurAnnonceSearch == null) {
            // TODO : create exception for controleurEnvoi *
        }
        return (ComplexControleurAnnonceDetailSearch) JadePersistenceManager.search(controleurAnnonceSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#search(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurEnvoiDetailSearch search(ComplexControleurEnvoiDetailSearch controleurEnvoiSearch)
            throws JadePersistenceException {
        if (controleurEnvoiSearch == null) {
            // TODO : create exception for controleurEnvoi *
        }
        return (ComplexControleurEnvoiDetailSearch) JadePersistenceManager.search(controleurEnvoiSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#search(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public ComplexControleurEnvoiSearch search(ComplexControleurEnvoiSearch controleurEnvoiSearch)
            throws JadePersistenceException {
        if (controleurEnvoiSearch == null) {
            // TODO : create exception for controleurEnvoi *
        }
        return (ComplexControleurEnvoiSearch) JadePersistenceManager.search(controleurEnvoiSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#search(ch.globaz.amal.business
     * .models.controleurEnvoi.ComplexControleurEnvoiSearch)
     */
    @Override
    public SimpleControleurEnvoiStatusSearch search(SimpleControleurEnvoiStatusSearch simpleControleurEnvoiSearch)
            throws JadePersistenceException, ControleurEnvoiException, JadeApplicationServiceNotAvailableException {
        return AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(simpleControleurEnvoiSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService#search(ch.globaz.amal.business
     * .models.controleurEnvoi.SimpleControleurJobSearch)
     */
    @Override
    public SimpleControleurJobSearch search(SimpleControleurJobSearch simpleControleurJobSearch)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, ControleurJobException {
        return AmalImplServiceLocator.getSimpleControleurJobService().search(simpleControleurJobSearch);
    }

}
