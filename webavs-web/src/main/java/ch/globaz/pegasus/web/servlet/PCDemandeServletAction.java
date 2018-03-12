package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pegasus.vb.demande.PCDemandeDetailViewBean;
import globaz.pegasus.vb.demande.PCFratrieViewBean;
import globaz.pegasus.vb.demande.PCImprimerBillagViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDemandeServletAction extends PCAbstractServletAction {

    private static final String BACK_TO_LIST_DEMANDE = "/pegasus?userAction=pegasus.demande.demande.chercher";
    private String idDossier = null;

    public PCDemandeServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // if ((this.privateAction != null) && (this.privateAction.isWellFormed())) {
        // return "/" + this.privateAction.getApplicationPart() + "?userAction="
        // + this.privateAction.getApplicationPart() + "." + this.privateAction.getPackagePart() + "."
        // + this.privateAction.getClassPart();
        // }

        return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + getAction().getPackagePart() + "." + "demandeDetail" + ".reAfficher";

        // return this._getDestEchec(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCFratrieViewBean) {
            String appPart = getAction().getApplicationPart();
            return "/" + appPart + "?userAction=" + appPart + ".droit.droit.chercher&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDemandePc=" + request.getParameter("idDemandePc") + "&idDossier="
                    + request.getParameter("idDossier");
        } else {
            PCDemandeDetailViewBean vb = (PCDemandeDetailViewBean) viewBean;
            vb.getDemande().getDossier().setId(idDossier);
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }

    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String parametersStr = "&idDossier=" + request.getParameter("idDossier");
        return PCDemandeServletAction.BACK_TO_LIST_DEMANDE.concat(parametersStr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        PCDemandeDetailViewBean vb = (PCDemandeDetailViewBean) viewBean;
        vb.getDemande().getDossier().setId(idDossier);
        return super._getDestModifierEchec(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        PCDemandeDetailViewBean vb = (PCDemandeDetailViewBean) viewBean;
        vb.getDemande().getDossier().setId(idDossier);
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String idDemandePc = request.getParameter("idDemandePc");
        String idDossier = null;
        DemandeSearch demandeSearch = new DemandeSearch();
        demandeSearch.setForIdDemande(idDemandePc);
        try {
            if (!JadeStringUtil.isBlankOrZero(idDemandePc)) {
                demandeSearch = PegasusServiceLocator.getDemandeService().search(demandeSearch);
                if (demandeSearch.getSize() == 1) {
                    Demande demande = ((Demande) demandeSearch.getSearchResults()[0]);
                    idDossier = demande.getDossier().getDossier().getIdDossier();
                    session.setAttribute("idDossier", idDossier);
                }
            }
        } catch (Exception e) {
            JadeLogger.error("Error in PCDemandeServletAction actionChercher", e);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String processLaunchedStr = request.getParameter("process");
        boolean processSeemsOk = "launched".equals(processLaunchedStr);
        String validFail = processSeemsOk ? "" : "?_valid=fail&_method=add";
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp" + validFail)
                .forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // FIXME Petite correction d'architecture à effectuer, on va plutôt
        // implémenter un helper pour la demande et surcharger la méthode _init
        // de ce helper. (Et éviter les e.printStackTrace())

        if ((viewBean instanceof PCImprimerBillagViewBean)) {
            ((PCImprimerBillagViewBean) viewBean).setIdDemandePc(request.getParameter("idDemandePc"));

        }

        if ((viewBean instanceof PCDemandeDetailViewBean)) {
            PCDemandeDetailViewBean vb = (PCDemandeDetailViewBean) viewBean;

            idDossier = request.getParameter("idDossier");

            try {
                vb.getDemande().setDossier(PegasusServiceLocator.getDossierService().read(idDossier));
                vb.getDemande().getSimpleDemande().setIdDossier(idDossier);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (viewBean instanceof PCFratrieViewBean) {
            PCFratrieViewBean vb = ((PCFratrieViewBean) viewBean);
            vb.setIdDemandePc(request.getParameter("idDemandePc"));
            vb.setIdTiers(request.getParameter("idTiers"));
        }

        return viewBean;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#doAction(javax.servlet .http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        putIdeDossier(request);
        super.doAction(session, request, response, mainController);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWDefaultServletAction# goSendRedirectWithoutParameters(java.lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!JadeStringUtil.isEmpty(idDossier)) {
            url = url + "&idDossier=" + idDossier;
        }
        super.goSendRedirect(url, request, response);
    }

    private void putIdeDossier(HttpServletRequest request) {
        idDossier = request.getParameter("idDossier");
    }

}
