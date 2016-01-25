/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliationListViewBean;
import globaz.naos.db.planAffiliation.AFPlanAffiliationViewBean;
import globaz.naos.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité PlanAffiliation.
 * 
 * @author sau
 */
public class AFActionPlanAffiliation extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionPlanAffiliation.
     * 
     * @param servlet
     */
    public AFActionPlanAffiliation(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Effectue des traitements avant recupération de l'entité dans la DB, pour l'afficher.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            AFPlanAffiliationViewBean viewBean = new AFPlanAffiliationViewBean();

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean.setAffiliationId(request.getParameter("affiliationId"));

                String reset = request.getParameter("resetIdTiersFacturation");
                if ((reset != null) && (reset.equals("on"))) {
                    viewBean.setIdTiersFacturation("");
                }
            } else {
                viewBean.setPlanAffiliationId(request.getParameter("selectedId"));
            }

            viewBean = (AFPlanAffiliationViewBean) mainDispatcher.dispatch(viewBean, getAction());

            AFAffiliationListViewBean affiliation = new AFAffiliationListViewBean();

            BISession bSession = CodeSystem.getSession(session);
            affiliation.setSession((BSession) bSession);
            affiliation.setForAffiliationId(viewBean.getAffiliationId());
            affiliation.find();

            if (affiliation.size() == 1) {
                viewBean.setIdTiersAffiliation(affiliation.getIdTiers(0));
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFPlanAffiliationViewBean vBean = new AFPlanAffiliationViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        actionChercher(vBean, session, request, response, mainDispatcher);
    }

    /**
     * Effectue des traitements avant l'ajout d'une nouvelle entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFPlanAffiliationViewBean vBean = (AFPlanAffiliationViewBean) viewBean;

        String reset = request.getParameter("resetIdTiersFacturation");
        if ((reset != null) && (reset.equals("on"))) {
            vBean.setIdTiersFacturation("");
        }

        return vBean;
    }

    /**
     * Effectue des traitements avant la modification d'une entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFPlanAffiliationViewBean vBean = (AFPlanAffiliationViewBean) viewBean;

        String reset = request.getParameter("resetIdTiersFacturation");
        if ((reset != null) && (reset.equals("on"))) {
            vBean.setIdTiersFacturation("");
        }

        return vBean;
    }
}
