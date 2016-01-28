/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.beneficiairepc.AFImpressionQuittanceViewBean;
import globaz.naos.process.AFImpressionQuittancesProcess;
import globaz.pyxis.summary.TIActionSummary;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Adhésion.
 * 
 * @author sau
 */
public class AFActionImpressionQuittances extends AFDefaultActionChercher {

    /**
     * Constructeur AFActionImpressionQuittances.
     * 
     * @param servlet
     */
    public AFActionImpressionQuittances(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            getAction().setRight(FWSecureConstants.READ);
            // redirection vers destination
            AFImpressionQuittanceViewBean viewBean = new AFImpressionQuittanceViewBean();
            viewBean.setIsProcessRunning("FALSE");
            if (session.getAttribute("numAffilie") != null) {
                viewBean.setNumAffilie((String) session.getAttribute("numAffilie"));
            }
            viewBean.setISession(mainDispatcher.getSession());
            session.setAttribute("viewBean", viewBean);
            request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, viewBean.getIdTiers());
            _destination = getRelativeURLwithoutClassPart(request, session) + "impression_de.jsp";

        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination = getRelativeURLwithoutClassPart(request, session) + "imprimer_de.jsp";
        AFImpressionQuittanceViewBean viewBean = null;
        if ("imprimerQuittance".equals(getAction().getActionPart())) {
            try {
                viewBean = new AFImpressionQuittanceViewBean();
                viewBean.setISession(dispatcher.getSession());
                JSPUtils.setBeanProperties(request, viewBean);
                viewBean.setIsProcessRunning("FALSE");
                if (viewBean.validate()) {
                    try {
                        AFImpressionQuittancesProcess process = new AFImpressionQuittancesProcess();
                        process.setISession(dispatcher.getSession());
                        process.setViewBean(viewBean);
                        process.start();
                    } catch (Exception ex) {
                        _destination = FWDefaultServletAction.ERROR_PAGE;
                    }
                }
                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
                if (goesToSuccessDest) {
                    _destination = _getDestExecuterSucces(session, request, response, viewBean);
                    viewBean.setIsProcessRunning("TRUE");
                } else {
                    _destination = _getDestExecuterEchec(session, request, response, viewBean);
                }
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            session.setAttribute("viewBean", viewBean);
            response.sendRedirect(request.getContextPath() + _destination);
        }
    }
}