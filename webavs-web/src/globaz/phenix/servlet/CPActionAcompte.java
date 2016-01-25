package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.phenix.vb.acompte.CPAcompteCreationAnnuelleViewBean;
import globaz.phenix.vb.acompte.CPAcompteSuppressionViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */
public class CPActionAcompte extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionAcompte(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    private void _actionExecuterCreation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            CPAcompteCreationAnnuelleViewBean viewBean = (CPAcompteCreationAnnuelleViewBean) session
                    .getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);

            dispatcher.dispatch(viewBean, getAction());

            _destination = "/phenix?userAction=phenix.process.acompte.creation";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == false) {
                _destination = _destination + "&process=launched";
                response.sendRedirect(request.getContextPath() + _destination);
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "acompteCreationAnnuelle_de.jsp";
                servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        // this.goSendRedirect(_destination, request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    private void _actionExecuterSuppression(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPAcompteSuppressionViewBean viewBean = (CPAcompteSuppressionViewBean) session.getAttribute("viewBean");
            // set les propriétés contenuent dans la request dans le viewBean
            JSPUtils.setBeanProperties(request, viewBean);
            // Appel le controller
            dispatcher.dispatch(viewBean, getAction());
            _destination = "/phenix?userAction=phenix.process.acompte.suppression";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        goSendRedirect(_destination, request, response);

    }

    private void actionCreation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CPAcompteCreationAnnuelleViewBean viewBean = new CPAcompteCreationAnnuelleViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // appel du controlleur
            viewBean = (CPAcompteCreationAnnuelleViewBean) mainDispatcher.dispatch(viewBean, getAction());
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURLwithoutClassPart(request, session) + "acompteCreationAnnuelle_de.jsp";
        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if ("creation".equals(getAction().getActionPart())) {
            actionCreation(session, request, response, mainDispatcher);
        } else if ("executerCreation".equals(getAction().getActionPart())) {
            _actionExecuterCreation(session, request, response, mainDispatcher);
        } else if ("suppression".equals(getAction().getActionPart())) {
            actionSuppression(session, request, response, mainDispatcher);
        } else if ("executerSuppression".equals(getAction().getActionPart())) {
            _actionExecuterSuppression(session, request, response, mainDispatcher);
        }
    }

    private void actionSuppression(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination;
        CPAcompteSuppressionViewBean viewBean;
        if ("fail".equals(request.getParameter("_valid"))) {
            viewBean = (CPAcompteSuppressionViewBean) session.getAttribute("viewBean");
        } else {
            viewBean = new CPAcompteSuppressionViewBean();
        }
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // appel du controlleur
            if (!"fail".equals(request.getParameter("_valid"))) {
                viewBean = (CPAcompteSuppressionViewBean) mainDispatcher.dispatch(viewBean, getAction());
            }
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURLwithoutClassPart(request, session) + "acompteSuppression_de.jsp";
        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
