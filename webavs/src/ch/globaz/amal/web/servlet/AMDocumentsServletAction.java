/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.process.envoi.EnvoiFormuleProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMActions;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMDocumentsServletAction extends AMAbstractServletAction {

    /**
     * @param aServlet
     */
    public AMDocumentsServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /**
     * ACTION DE CHANGEMENT DE STATUS D'UN JOB OU ITEM SELECTIONNE
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String changeJobStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");
        String newStatus = request.getParameter("newStatus");
        String forJobType = request.getParameter("searchModel.forTypeJob");
        String forUserJob = request.getParameter("searchModel.forUserJob");

        try {
            // -----------------------------------
            // Traitement uniquement si new status renseigné
            // -----------------------------------
            if ((newStatus != null) && (newStatus.length() > 0)) {
                // -----------------------------------
                // INPUT VALUES
                // -----------------------------------
                ArrayList<String> allIds = null;
                String idJobForProcess = null;
                if ((selectedStatus != null) && (selectedStatus.length() > 0)) {
                    allIds = new ArrayList<String>();
                    String[] itemsToChange = selectedStatus.split(",");
                    for (int iItem = 0; iItem < itemsToChange.length; iItem++) {
                        String currentStatus = itemsToChange[iItem];
                        allIds.add(currentStatus);
                    }
                } else if ((selectedJob != null) && (selectedJob.length() > 0)) {
                    idJobForProcess = selectedJob;
                }
                // -----------------------------------
                // process preparation
                // -----------------------------------
                EnvoiFormuleProcess process = new EnvoiFormuleProcess();
                process.setIdItem(allIds);
                process.setIdJob(idJobForProcess);
                process.setChangeStatusOnly(true);
                process.setNewStatus(newStatus);
                process.setSession(BSessionUtil.getSessionFromThreadContext());

                String savedStatus = null;

                try {
                    // AVANT TRAITEMENT METTRE LE STATUS A IN PROGRESS (ITEM VS JOB)
                    // -----------------------------------
                    savedStatus = setPrintStatusInProgress(allIds, idJobForProcess);
                    // Launch the process
                    // -----------------------------------
                    BProcessLauncher.start(process, false);
                } catch (Exception e) {
                    JadeLogger.error(this, "Error Launching Process processEnvoiStatus : " + e.getMessage());
                    // Restore l'ancien status en cas de mmmmm
                    // -----------------------------------
                    restorePrintStatusSavedProgress(allIds, idJobForProcess, savedStatus);
                }
            }
        } catch (Exception e) {
            JadeLogger.info(this, "Problem changing status : " + e.toString());
        }
        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_DOCUMENTS + ".chercher&searchModel.forTypeJob="
                + forJobType + "&searchModel.forUserJob=" + forUserJob;
        return destination;
    }

    /**
     * ACTION DE SUPPRESSION D'UN JOB / ITEM SELECTIONNE
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String deleteJob(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");
        String forJobType = request.getParameter("searchModel.forTypeJob");
        String forUserJob = request.getParameter("searchModel.forUserJob");

        try {
            if ((selectedStatus != null) && (selectedStatus.length() > 0)) {
                String[] itemsToRemove = selectedStatus.split(",");
                for (int iItem = 0; iItem < itemsToRemove.length; iItem++) {
                    String currentStatus = itemsToRemove[iItem];
                    AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().delete(currentStatus);
                }
            } else if ((selectedJob != null) && (selectedJob.length() > 0)) {
                AmalServiceLocator.getControleurEnvoiService().delete(selectedJob);
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Exception when deleting a job : " + ex.getMessage());
        }

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_DOCUMENTS + ".chercher&searchModel.forTypeJob="
                + forJobType + "&searchModel.forUserJob=" + forUserJob;
        return destination;
    }

    /**
     * ACTION DE DEMARRER LE PROCESS D'IMPRESSION D'UN JOB / ITEM SELECTIONNE
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String launchPrintProcess(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");
        String forJobType = request.getParameter("searchModel.forTypeJob");
        String forUserJob = request.getParameter("searchModel.forUserJob");
        // -----------------------------------
        // INPUT VALUES
        // -----------------------------------
        ArrayList<String> allIds = null;
        String idJobForProcess = null;
        if ((selectedStatus != null) && (selectedStatus.length() > 0)) {
            allIds = new ArrayList<String>();
            String[] itemsToChange = selectedStatus.split(",");
            for (int iItem = 0; iItem < itemsToChange.length; iItem++) {
                String currentStatus = itemsToChange[iItem];
                allIds.add(currentStatus);
            }
        } else if ((selectedJob != null) && (selectedJob.length() > 0)) {
            idJobForProcess = selectedJob;
        }

        // -----------------------------------
        // process preparation
        // -----------------------------------
        EnvoiFormuleProcess process = new EnvoiFormuleProcess();
        process.setIdItem(allIds);
        process.setIdJob(idJobForProcess);
        process.setChangeStatusOnly(false);
        process.setSession(BSessionUtil.getSessionFromThreadContext());

        String savedStatus = null;

        try {
            // AVANT TRAITEMENT METTRE LE STATUS A IN PROGRESS (ITEM VS JOB)
            // -----------------------------------
            savedStatus = setPrintStatusInProgress(allIds, idJobForProcess);
            // Launch the process
            // -----------------------------------
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.error(this, "Error Launching Process processEnvoiStatus : " + e.getMessage());
            restorePrintStatusSavedProgress(allIds, idJobForProcess, savedStatus);
        }

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_DOCUMENTS + ".chercher&searchModel.forTypeJob="
                + forJobType + "&searchModel.forUserJob=" + forUserJob;
        return destination;
    }

    /**
     * ACTION DE CHANGEMENT DE LA DATE D'UN JOB
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String newJobDate(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");
        String forJobType = request.getParameter("searchModel.forTypeJob");
        String forUserJob = request.getParameter("searchModel.forUserJob");
        String newDate = request.getParameter("newDate");

        if (JadeDateUtil.isGlobazDate(newDate)) {
            try {
                if (!JadeStringUtil.isBlankOrZero(selectedJob)) {
                    // UPDATE DU JOB
                    SimpleControleurJob currentJob = AmalImplServiceLocator.getSimpleControleurJobService().read(
                            selectedJob);
                    currentJob.setDateJob(newDate);
                    currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                    // RECHERCHE DES DOCUMENTS VIA STATUS
                    SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
                    statusSearch.setForIdJob(selectedJob);
                    statusSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
                    for (int iCurrentStatus = 0; iCurrentStatus < statusSearch.getSize(); iCurrentStatus++) {
                        SimpleControleurEnvoiStatus currentStatus = (SimpleControleurEnvoiStatus) statusSearch
                                .getSearchResults()[iCurrentStatus];
                        // UPDATE DU DOCUMENT
                        SimpleDocument currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read(
                                currentStatus.getIdEnvoi());
                        currentDocument.setDateEnvoi(newDate);
                        currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
                    }
                }
            } catch (Exception ex) {
                JadeLogger.error(this, "Exception when updating the date : " + ex.getMessage());
            }
        }

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_DOCUMENTS + ".chercher&searchModel.forTypeJob="
                + forJobType + "&searchModel.forUserJob=" + forUserJob;
        return destination;
    }

    /**
     * Si échec au démarrage du process, restore de l'ancienne valeur du status
     * 
     * @param allIds
     * @param idJobForProcess
     * @param savedStatus
     */
    private void restorePrintStatusSavedProgress(ArrayList<String> allIds, String idJobForProcess, String savedStatus) {
        // RESTORE ANCIEN STATUS EN CAS DE MMMM
        // -----------------------------------
        if (idJobForProcess != null) {
            try {
                SimpleControleurJob job = AmalImplServiceLocator.getSimpleControleurJobService().read(idJobForProcess);
                job.setStatusEnvoi(savedStatus);
                job = AmalImplServiceLocator.getSimpleControleurJobService().update(job);
                JadeThread.commitSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                for (int iDetail = 0; iDetail < allIds.size(); iDetail++) {
                    AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(allIds.get(iDetail),
                            savedStatus, true, null);
                }
                JadeThread.commitSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Avant traitement, application du status à in progress (refresh UI)
     * 
     * @param allIds
     * @param idJobForProcess
     * @return Ancienne valeur du status
     */
    private String setPrintStatusInProgress(ArrayList<String> allIds, String idJobForProcess) {
        String savedStatus = null;
        if (idJobForProcess != null) {
            try {
                SimpleControleurJob job = AmalImplServiceLocator.getSimpleControleurJobService().read(idJobForProcess);
                savedStatus = job.getStatusEnvoi();
                job.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
                job = AmalImplServiceLocator.getSimpleControleurJobService().update(job);
                JadeThread.commitSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                for (int iDetail = 0; iDetail < allIds.size(); iDetail++) {
                    if (iDetail == 0) {
                        SimpleControleurEnvoiStatus envoiItem = AmalImplServiceLocator
                                .getSimpleControleurEnvoiStatusService().read(allIds.get(iDetail));
                        savedStatus = envoiItem.getStatusEnvoi();
                    }
                    AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().changeStatus(allIds.get(iDetail),
                            IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue(), true, null);
                }
                JadeThread.commitSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return savedStatus;
    }

}
