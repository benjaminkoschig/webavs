package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPRemarqueDecisionListViewBean;
import globaz.phenix.db.principale.CPRemarqueDecisionViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionRemarqueDecision extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionRemarqueDecision(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        CPRemarqueDecisionViewBean viewBean = new CPRemarqueDecisionViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdDecision(request.getParameter("idDecision"));
            // Sauvegarde identifiant en paramètre pour le récupérer par exemple
            // lors de la création
            session.setAttribute("idDecision", request.getParameter("idDecision"));
            mainController.dispatch(viewBean, getAction());
            // viewBean._chargerEntete();
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        super.actionChercher(session, request, response, mainController);
    }

    // protected void _actionLister(
    // HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWDispatcher mainDispatcher)
    // throws javax.servlet.ServletException, java.io.IOException {
    // String destination = "";
    // try {
    // FWViewBeanInterface viewBean =
    // FWListViewBeanActionFactory.newInstance(getAction(),
    // mainDispatcher.getPrefix());
    // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
    // viewBean = beforeLister(session, request, response, viewBean);
    // FWAction action = getAction();
    // //action.setRight(FWSecureConstants.READ);
    // // viewBean = mainDispatcher.dispatch(viewBean, action);
    // session.setAttribute("viewBean", viewBean);
    // mainDispatcher.dispatch(viewBean, getAction());
    //
    // destination = getRelativeURL(request,session) + "_rcListe.jsp";
    // } catch (Exception e) {
    // destination = ERROR_PAGE;
    // }
    //
    // servlet.getServletContext().getRequestDispatcher(destination).forward(
    // request,
    // response);
    // }
    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPRemarqueDecisionViewBean vBean = (CPRemarqueDecisionViewBean) viewBean;
        // Positionnement selon les critères de recherche
        vBean.setIdTiers((String) session.getAttribute("idTiers"));
        vBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
        return vBean;
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPRemarqueDecisionListViewBean vBean = (CPRemarqueDecisionListViewBean) viewBean;
        // Positionnement selon les critères de recherche
        vBean.setForIdDecision((String) session.getAttribute("idDecision"));
        return vBean;
    }

    /*
     * Traitement lors d'une création avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPRemarqueDecisionViewBean vBean = (CPRemarqueDecisionViewBean) viewBean;
        // Récupération de l'id décision
        vBean.setIdDecision((String) session.getAttribute("idDecision"));
        return vBean;
    }
}
