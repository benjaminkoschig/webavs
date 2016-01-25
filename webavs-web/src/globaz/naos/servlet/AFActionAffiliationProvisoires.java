package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.naos.process.AFAffiliationNonProvisoiresProcessViewBean;
import globaz.naos.process.AFAffiliationProvisoiresProcessViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Affiliation Provisoire.
 * 
 * @author ado
 * 
 * 
 * 
 *         21 avr. 04
 * 
 */
public class AFActionAffiliationProvisoires extends FWDefaultServletAction {

    /**
     * Constructeur d'AFActionAffiliationProvisoires.
     * 
     * @param servlet
     */
    public AFActionAffiliationProvisoires(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Effectue les traitements pour en afficher les détails.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());

            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=upd";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour l'execution d'un process.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            FWViewBeanInterface currentBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = null;

            if ("affiliationNonProvisoires".equals(action.getClassPart())) {
                viewBean = new AFAffiliationNonProvisoiresProcessViewBean();
            } else if ("affiliationProvisoires".equals(action.getClassPart())) {
                viewBean = new AFAffiliationProvisoiresProcessViewBean();
            }
            viewBean.setISession(currentBean.getISession());
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            request.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

}
