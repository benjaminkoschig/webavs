package ch.globaz.auriga.web.servlet;

import globaz.auriga.vb.decisioncap.AUDecisionCapSearchAjaxViewBean;
import globaz.auriga.vb.decisioncap.AUDecisionCapSearchViewBean;
import globaz.auriga.vb.decisioncap.AUDecisionCapViewBean;
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
 * @author bjo
 * 
 */
public class AUDefaultAction extends AUAbstractDefaultServletAction {

    public final static String ACTION_AFFICHER_CAPSEARCH = "afficherCapSearch";

    /**
     * @param aServlet
     */
    public AUDefaultAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * Permet de définir le comportement des actions customs (non-Javadoc)
     * 
     * @see ch.globaz.auriga.web.servlet.AUAbstractDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String actionPart = getAction().getActionPart();

        if (actionPart.equals(AUDefaultAction.ACTION_AFFICHER_CAPSEARCH)) {
            afficherCapSearchAction(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }

    }

    private void afficherCapSearchAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = null;
        try {
            String affiliationId = null;
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            AUDecisionCapSearchViewBean viewBean = (AUDecisionCapSearchViewBean) FWViewBeanActionFactory.newInstance(
                    action, dispatcher.getPrefix());

            // Récupération de l'affiliationPrincipale en session
            if (!JadeStringUtil.isBlank((String) session.getAttribute("affiliationPrincipale"))) {
                affiliationId = (String) session.getAttribute("affiliationPrincipale");
                viewBean.setIdAffilie(affiliationId);
            }

            // Copie des propriétés
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Traitement
            viewBean = (AUDecisionCapSearchViewBean) dispatcher.dispatch(viewBean, action);
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

        // set l'id affilie au viewBean
        if (viewBean.getClass().isAssignableFrom(AUDecisionCapViewBean.class)) {
            AUDecisionCapViewBean decisionCapViewBean = (AUDecisionCapViewBean) viewBean;
            decisionCapViewBean.setIdAffilie(request.getParameter("idAffilie"));
            decisionCapViewBean.setIdDecisionCapRectifiee(request.getParameter("idDecisionCapRectifiee"));
            decisionCapViewBean.setOptionDefinitive(Boolean.valueOf(request.getParameter("optionDefinitive")));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (!JadeStringUtil.isBlankOrZero(request.getParameter("searchModel.forIdAffiliation"))) {
            if (viewBean.getClass().isAssignableFrom(AUDecisionCapSearchAjaxViewBean.class)) {
                session.setAttribute("affiliationPrincipale", request.getParameter("searchModel.forIdAffiliation"));
            }
        }

        return super.beforeLister(session, request, response, viewBean);
    }
}
