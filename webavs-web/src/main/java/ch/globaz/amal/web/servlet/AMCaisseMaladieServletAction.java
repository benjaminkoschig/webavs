package ch.globaz.amal.web.servlet;

import globaz.amal.process.annonce.AnnonceCMProcess;
import globaz.amal.process.annonce.AnnonceSedexProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMActions;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOEnvoiMessage5222_000201_1;

public class AMCaisseMaladieServletAction extends AMAbstractServletAction {

    public AMCaisseMaladieServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    // public String launchAnnonceProcess(HttpSession session, HttpServletRequest request, HttpServletResponse response,
    // FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
    // String destination = "";
    //
    // String noGroupe = request.getParameter("selectedGroupe");
    // String noCaisse = request.getParameter("selectedCaisse");
    // String anneeHistorique = request.getParameter("selectedAnnee");
    // String typeAdmin = request.getParameter("searchModel.forTypeAdmin");
    // String orderBy = request.getParameter("searchModel.orderBy");
    //
    // List<String> selectedIdCaisses = null;
    // if (!JadeStringUtil.isEmpty(noCaisse)) {
    // String[] selectedCaisses = noCaisse.split(";");
    // if (selectedCaisses.length > 0) {
    // selectedIdCaisses = new ArrayList<String>();
    // for (int iCaisse = 0; iCaisse < selectedCaisses.length; iCaisse++) {
    // selectedIdCaisses.add(selectedCaisses[iCaisse]);
    // }
    // }
    // }
    //
    // // ---------------------------------------------------------------------
    // // process preparation - afin d'éviter une double action sur les boutons
    // // ---------------------------------------------------------------------
    // try {
    // AmalServiceLocator.getAnnonceService().createAnnoncesSimulationJobs(noGroupe, selectedIdCaisses);
    // JadeThread.commitSession();
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // JadeLogger.error(this, "Error Initializing job for annonce process : " + ex.getMessage());
    // }
    // AnnonceCMProcess process = new AnnonceCMProcess();
    // process.setIdTiersCM(selectedIdCaisses);
    // process.setIdTiersGroupe(noGroupe);
    // process.setSession(BSessionUtil.getSessionFromThreadContext());
    // process.setIsSimulation(false);
    // process.setAnneeHistorique(anneeHistorique);
    // try {
    // // -----------------------------------
    // // Launch the process
    // // -----------------------------------
    // BProcessLauncher.start(process, false);
    // } catch (Exception e) {
    // // TODO : restore status job annonce (suppression ?)
    // e.printStackTrace();
    // JadeLogger.error(this, "Error Launching Process AnnonceCMProcess : " + e.getMessage());
    // }
    //
    // destination = "/amal?userAction=" + IAMActions.ACTION_CAISSEMALADIE + ".chercher&searchModel.forTypeAdmin="
    // + typeAdmin + "&searchModel.orderBy=" + orderBy;
    //
    // return destination;
    // }

    public String launchSEDEXRPProcess(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        String destination = "";

        String noGroupe = request.getParameter("selectedGroupe");
        String noCaisse = request.getParameter("selectedCaisse");
        String anneeHistorique = request.getParameter("selectedAnnee");
        String typeMessage = request.getParameter("selectedTypeMessage");
        String typeAdmin = request.getParameter("searchModel.forTypeAdmin");
        String orderBy = request.getParameter("searchModel.orderBy");
        String periodeInsuranceQueryFrom = request.getParameter("periodeInsuranceQueryFrom");
        String periodeInsuranceQueryTo = request.getParameter("periodeInsuranceQueryTo");
        String periodeDecreeInventoryFrom = request.getParameter("periodeDecreeInventoryFrom");
        String periodeDecreeInventoryTo = request.getParameter("periodeDecreeInventoryTo");
        String typeSimu = request.getParameter("simulationSedex");

        boolean isSimulation = false;
        if (!JadeStringUtil.isBlankOrZero(typeSimu)) {
            isSimulation = true;
        }

        ArrayList<String> selectedIdCaisses = null;
        if (!JadeStringUtil.isEmpty(noCaisse)) {
            String[] selectedCaisses = noCaisse.split(";");
            if (selectedCaisses.length > 0) {
                selectedIdCaisses = new ArrayList<String>();
                for (int iCaisse = 0; iCaisse < selectedCaisses.length; iCaisse++) {
                    selectedIdCaisses.add(selectedCaisses[iCaisse]);
                }
            }
        }

        // ---------------------------------------------------------------------
        // process preparation - afin d'éviter une double action sur les boutons
        // ---------------------------------------------------------------------
        try {
            AmalServiceLocator.getAnnonceService().createAnnoncesSimulationJobs(noGroupe, selectedIdCaisses);
            JadeThread.commitSession();
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "Error Initializing job for annonce process : " + ex.getMessage());
        }

        AnnonceSedexProcess process = new AnnonceSedexProcess();
        process.setSession(BSessionUtil.getSessionFromThreadContext());
        process.setAnneeHistorique(anneeHistorique);
        process.setNoGroupe(noGroupe);
        process.setSelectedIdCaisses(selectedIdCaisses);
        process.setTypeMessage(typeMessage);
        process.setPeriodeDecreeInventoryFrom(periodeDecreeInventoryFrom);
        process.setPeriodeDecreeInventoryTo(periodeDecreeInventoryTo);
        process.setPeriodeInsuranceQueryFrom(periodeInsuranceQueryFrom);
        process.setPeriodeInsuranceQueryTo(periodeInsuranceQueryTo);
        process.setIsSimulation(isSimulation);

        try {
            // -----------------------------------
            // Launch the process
            // -----------------------------------
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            // TODO : restore status job annonce (suppression ?)
            e.printStackTrace();
            JadeLogger.error(this, "Error Launching Process AnnonceCMProcess : " + e.getMessage());
        }

        destination = "/amal?userAction=" + IAMActions.ACTION_CAISSEMALADIE + ".chercher&searchModel.forTypeAdmin="
                + typeAdmin + "&searchModel.orderBy=" + orderBy;

        return destination;

    }

    public String launchSimulationProcess(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String destination = "";

        String noGroupe = request.getParameter("selectedGroupe");
        String noCaisse = request.getParameter("selectedCaisse");
        String anneeHistorique = request.getParameter("selectedAnnee");
        String typeAdmin = request.getParameter("searchModel.forTypeAdmin");
        String orderBy = request.getParameter("searchModel.orderBy");
        String typeSimu = request.getParameter("simulationSedex");

        // Si c'est une simulation de message, on lance la méthode adéquate
        if (!typeSimu.equals("null") && !typeSimu.equals("undefined") && !JadeStringUtil.isBlankOrZero(typeSimu)) {
            return launchSEDEXRPProcess(session, request, response, mainDispatcher, viewBean);
        }

        List<String> selectedIdCaisses = null;
        if (!JadeStringUtil.isEmpty(noCaisse)) {
            String[] selectedCaisses = noCaisse.split(";");
            if (selectedCaisses.length > 0) {
                selectedIdCaisses = new ArrayList<String>();
                for (int iCaisse = 0; iCaisse < selectedCaisses.length; iCaisse++) {
                    selectedIdCaisses.add(selectedCaisses[iCaisse]);
                }
            }
        }

        // ---------------------------------------------------------------------
        // process preparation - afin d'éviter une double action sur les boutons
        // ---------------------------------------------------------------------
        try {
            AmalServiceLocator.getAnnonceService().createAnnoncesSimulationJobs(noGroupe, selectedIdCaisses);
            JadeThread.commitSession();
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "Error Initializing job for annonce process : " + ex.getMessage());
        }
        AnnonceCMProcess process = new AnnonceCMProcess();
        process.setIdTiersCM(selectedIdCaisses);
        process.setIdTiersGroupe(noGroupe);
        process.setSession(BSessionUtil.getSessionFromThreadContext());
        process.setIsSimulation(true);
        process.setAnneeHistorique(anneeHistorique);
        try {
            // -----------------------------------
            // Launch the process
            // -----------------------------------
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            // TODO : restore status job annonce (suppression ?)
            e.printStackTrace();
            JadeLogger.error(this, "Error Launching Process AnnonceCMProcess : " + e.getMessage());
        }

        destination = "/amal?userAction=" + IAMActions.ACTION_CAISSEMALADIE + ".chercher&searchModel.forTypeAdmin="
                + typeAdmin + "&searchModel.orderBy=" + orderBy;

        return destination;
    }

    public String launchSEDEXCOProcess(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        String destination = "";

        String noGroupe = request.getParameter("selectedGroupe");
        String noCaisse = request.getParameter("selectedCaisse");
        String anneeHistorique = request.getParameter("selectedAnnee");
        String typeMessage = request.getParameter("selectedTypeMessage");
        String typeAdmin = request.getParameter("searchModel.forTypeAdmin");
        String orderBy = request.getParameter("searchModel.orderBy");
        String typeSimu = request.getParameter("simulationSedex");

        boolean isSimulation = false;
        if (!JadeStringUtil.isBlankOrZero(typeSimu)) {
            isSimulation = true;
        }

        ArrayList<String> selectedIdCaisses = null;
        if (!JadeStringUtil.isEmpty(noCaisse)) {
            String[] selectedCaisses = noCaisse.split(";");
            if (selectedCaisses.length > 0) {
                selectedIdCaisses = new ArrayList<String>();
                for (int iCaisse = 0; iCaisse < selectedCaisses.length; iCaisse++) {
                    selectedIdCaisses.add(selectedCaisses[iCaisse]);
                }
            }
        }

        // ---------------------------------------------------------------------
        // process preparation - afin d'éviter une double action sur les boutons
        // ---------------------------------------------------------------------
        try {
            AmalServiceLocator.getAnnonceService().createAnnoncesSimulationJobs(noGroupe, selectedIdCaisses);
            JadeThread.commitSession();
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "Error Initializing job for annonce process : " + ex.getMessage());
        }

        AnnoncesCOEnvoiMessage5222_000201_1 process = new AnnoncesCOEnvoiMessage5222_000201_1();
        process.setSession(BSessionUtil.getSessionFromThreadContext());
        process.setNoGroupeCaisse(noGroupe);
        process.setSelectedIdCaisses(selectedIdCaisses);
        // process.setAnneeHistorique(anneeHistorique);
        // process.setNoGroupe(noGroupe);
        // process.setSelectedIdCaisses(selectedIdCaisses);
        // process.setTypeMessage(typeMessage);
        // process.setIsSimulation(isSimulation);

        try {
            // -----------------------------------
            // Launch the process
            // -----------------------------------
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            // TODO : restore status job annonce (suppression ?)
            e.printStackTrace();
            JadeLogger.error(this, "Error Launching Process AnnonceCMProcess : " + e.getMessage());
        }

        destination = "/amal?userAction=" + IAMActions.ACTION_CAISSEMALADIE + ".chercher&searchModel.forTypeAdmin="
                + typeAdmin + "&searchModel.orderBy=" + orderBy;

        return destination;

    }
}
