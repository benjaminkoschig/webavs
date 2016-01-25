package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.decision.PFDecisionProcessViewBean;
import globaz.perseus.vb.decision.PFDecisionRefusDemandeViewBean;
import globaz.perseus.vb.decision.PFDecisionRefusFactureViewBean;
import globaz.perseus.vb.decision.PFDecisionViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.constantes.IPFActions;

/**
 * 
 * @author MBO
 * 
 */

public class PFDecisionAction extends PFAbstractDefaultServletAction {

    private static final String DECISION_LIST_URL = "/perseus?userAction=perseus.decision.decision.chercher";
    private static final String DOSSIER_REFUS_FACTURE = "/perseus?userAction=perseus.dossier.dossier.chercher";

    /**
     * 
     * @param aServlet
     */
    public PFDecisionAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String urlPlus = "";
        if (viewBean instanceof PFDecisionViewBean) {
            PFDecisionViewBean vb = (PFDecisionViewBean) viewBean;
            urlPlus = "&idDemande=" + vb.getDecision().getDemande().getId();

            return PFDecisionAction.DECISION_LIST_URL + urlPlus;
        }
        if (viewBean instanceof PFDecisionProcessViewBean) {

            return PFDecisionAction.DECISION_LIST_URL;
        }

        // Mis en commentaire pour ne pas réactualiser la page et afficher l'état du process
        // if (viewBean instanceof PFDecisionRefusFactureViewBean) {
        // PFDecisionRefusFactureViewBean vb = (PFDecisionRefusFactureViewBean) viewBean;
        // urlPlus = "&idDemande=" + vb.getDossier().getDossier().getIdDossier();
        // return PFDecisionAction.DOSSIER_REFUS_FACTURE + urlPlus;
        // }

        // Mis en commentaire pour ne pas réactualiser la page et afficher l'état du process
        // if (viewBean instanceof PFDecisionRefusDemandeViewBean) {
        // PFDecisionRefusDemandeViewBean vb = (PFDecisionRefusDemandeViewBean) viewBean;
        // urlPlus = "&idDossier=" + vb.getDossier().getDossier().getIdDossier();
        // return PFDecisionAction.DOSSIER_REFUS_FACTURE + urlPlus;
        // }

        return super._getDestExecuterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String urlPlus = "";
        if (viewBean instanceof PFDecisionViewBean) {
            PFDecisionViewBean vb = (PFDecisionViewBean) viewBean;
        }

        return super._getDestModifierSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String action = request.getParameter("userAction");

        if (IPFActions.ACTION_IMPRIMER_DOC.equals(action)) {
            PFDecisionProcessViewBean vb = new PFDecisionProcessViewBean();
            vb.setDecisionId(request.getParameter("idDecision"));
            session.setAttribute("viewBean", vb);
        }

        super.actionExecuter(session, request, response, mainDispatcher);
    }

    // @Override
    // protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
    // FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
    //
    // String processLaunchedStr = request.getParameter("process");
    // boolean processSeemsOk = "launched".equals(processLaunchedStr);
    // String validFail = processSeemsOk ? "" : "?_valid=fail";
    // this.servlet.getServletContext().getRequestDispatcher(this.getRelativeURL(request, session) + "_de.jsp")
    // .forward(request, response);
    // }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof PFDecisionViewBean) && "add".equals(request.getParameter("_method"))) {
            PFDecisionViewBean vb = (PFDecisionViewBean) viewBean;
            boolean isNewProject = "true".equals(request.getParameter("isProject"));
            boolean isNewSuppressionVolontaire = "true".equals(request.getParameter("isNewSuppressionVolontaire"));
            vb.setNewProject(isNewProject);
            vb.setNewSuppressionVolontaire(isNewSuppressionVolontaire);
            vb.getDecision().getDemande().setId(request.getParameter("idDemande"));

            return super.beforeAfficher(session, request, response, vb);
        }

        if (viewBean instanceof PFDecisionViewBean) {
            if (request.getParameter("idDecision") != null) {
                PFDecisionViewBean vb = (PFDecisionViewBean) viewBean;
                vb.setId(request.getParameter("idDecision"));
            }
        }

        if (viewBean instanceof PFDecisionRefusFactureViewBean) {
            PFDecisionRefusFactureViewBean vb = (PFDecisionRefusFactureViewBean) viewBean;
            vb.setIdDossier(request.getParameter("idDossier"));
        }

        if (viewBean instanceof PFDecisionRefusDemandeViewBean) {
            PFDecisionRefusDemandeViewBean vb = (PFDecisionRefusDemandeViewBean) viewBean;
            vb.setIdDossier(request.getParameter("idDossier"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }
}
