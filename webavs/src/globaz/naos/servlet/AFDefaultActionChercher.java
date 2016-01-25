/*
 * Created on 11-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.AFAbstractViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion de l'action <code>chercher</code> par défaut.
 * 
 * @author sau
 */
public class AFDefaultActionChercher extends FWDefaultServletAction {
    public final static String ACTION_RELOAD = "reload";

    /**
     * Constructeur d'AFDefaultActionChercher
     * 
     * @param servlet
     */
    public AFDefaultActionChercher(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action par défault utilisé pour la recherche.
     * 
     * @param vBean
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionChercher(FWViewBeanInterface vBean, HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String destination = "";
        FWAction action = getAction();
        try {
            // action.changeActionPart(FWAction.ACTION_AFFICHER);
            // vBean = mainDispatcher.dispatch(vBean, action);
            // session.removeAttribute("viewBean");
            vBean.setISession(mainDispatcher.getSession());
            session.setAttribute("viewBean", vBean);
            // action.changeActionPart(FWAction.ACTION_CHERCHER);
            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Traitement des actions non standard.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (ACTION_RELOAD.equals(getAction().getActionPart())) {
            // reafraichir les menus deroulants de la page (en particulier le
            // menu des plans de caisse)
            AFAbstractViewBean viewBean = (AFAbstractViewBean) session.getAttribute("viewBean");

            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }
}
