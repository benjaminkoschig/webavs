package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModulePlanListViewBean;
import globaz.musca.db.facturation.FAModulePlanViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionModulePlan extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionModulePlan(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action chercher de la gestion des modules d'un plan de facturation
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        FAModulePlanViewBean viewBean = new FAModulePlanViewBean();
        viewBean.setIdPlanFacturation(request.getParameter("selectedId"));
        // Savegarde de l'id plan passé en paramètre pour le récupérer lors de
        // la création
        session.setAttribute("saveIdPlan", request.getParameter("selectedId"));
        //
        viewBean.setMessage("OK");
        viewBean.setMsgType(FWViewBeanInterface.OK);

        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        // Dispatch
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                .forward(request, response);
    }

    protected FWViewBeanInterface beforeAdd(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        FAModulePlanViewBean vBean = (FAModulePlanViewBean) viewBean;
        // Récupération de l'id plan concerné (sauvegardé dans l'action
        // chercher)
        vBean.setIdPlanFacturation((String) session.getAttribute("saveIdPlan"));

        return vBean;
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        FAModulePlanViewBean vBean = (FAModulePlanViewBean) viewBean;
        // Récupération de l'id plan concerné (sauvegardé dans l'action
        // chercher)
        vBean.setIdPlanFacturation((String) session.getAttribute("saveIdPlan"));
        vBean.setIdModuleFacturation(request.getParameter("selectedId2"));

        return vBean;
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAModulePlanListViewBean vBean = (FAModulePlanListViewBean) viewBean;
        // Positionnement pour l'id plan sélectionné
        vBean.setForIdPlanFacturation((String) session.getAttribute("saveIdPlan"));
        try {
            globaz.globall.api.BISession bSession = globaz.musca.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // Traitement de la zone A partir de selon la langue de
            // l'utilisateur
            vBean.orderByNiveauAppel();
            vBean.orderByLibelleLangueSession();
            return vBean;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return vBean;
        }
    }
}
