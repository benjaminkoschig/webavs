package ch.globaz.aries.web.servlet;

import globaz.aries.vb.decisioncgas.ARDecisionCgasSearchAjaxViewBean;
import globaz.aries.vb.decisioncgas.ARDecisionCgasSearchViewBean;
import globaz.aries.vb.decisioncgas.ARDecisionCgasViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmo
 * 
 */
public class ARDefaultAction extends ARAbstractDefaultServletAction {

    public final static String ACTION_AFFICHER_CGASSEARCH = "afficherCgasSearch";

    /**
     * @param aServlet
     */
    public ARDefaultAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * Permet de définir le comportement des actions customs (non-Javadoc)
     * 
     * @see ch.globaz.aries.web.servlet.ARAbstractDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String actionPart = getAction().getActionPart();

        if (ARDefaultAction.ACTION_AFFICHER_CGASSEARCH.equals(actionPart)) {
            afficherCgasSearchAction(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }

    }

    private void afficherCgasSearchAction(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = null;
        try {
            String affiliationId = null;
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            ARDecisionCgasSearchViewBean viewBean = (ARDecisionCgasSearchViewBean) FWViewBeanActionFactory.newInstance(
                    action, dispatcher.getPrefix());

            // Récupération de l'affiliationPrincipale en session
            if (!JadeStringUtil.isBlank((String) session.getAttribute("affiliationPrincipale"))) {
                affiliationId = (String) session.getAttribute("affiliationPrincipale");
                viewBean.setIdAffilie(affiliationId);
            }

            // Copie des propriétés
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Traitement
            viewBean = (ARDecisionCgasSearchViewBean) dispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);
            request.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean.getClass().isAssignableFrom(ARDecisionCgasViewBean.class)) {
            ARDecisionCgasViewBean decisionCgasViewBean = (ARDecisionCgasViewBean) viewBean;
            decisionCgasViewBean.setIdAffiliation(request.getParameter("idAffiliation"));
            decisionCgasViewBean.setIdDecisionCgasRectifiee(request.getParameter("idDecisionCgasRectifiee"));
            decisionCgasViewBean.setOptionDefinitive(Boolean.valueOf(request.getParameter("optionDefinitive")));
        }

        return super.beforeAfficher(session, request, response, viewBean);

    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean.getClass().isAssignableFrom(ARDecisionCgasSearchAjaxViewBean.class)) {
            if (!JadeStringUtil.isBlankOrZero(request.getParameter("searchModel.forIdAffiliation"))) {
                session.setAttribute("affiliationPrincipale", request.getParameter("searchModel.forIdAffiliation"));
            }
        }

        return super.beforeLister(session, request, response, viewBean);

    }
}
