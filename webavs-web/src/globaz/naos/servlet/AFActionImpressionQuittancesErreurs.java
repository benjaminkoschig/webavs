/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.beneficiairepc.AFQuittanceViewBean;
import globaz.naos.process.AFImpressionQuittancesErreursProcess;
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
public class AFActionImpressionQuittancesErreurs extends AFDefaultActionChercher {

    /**
     * Constructeur AFActionImpressionQuittances.
     * 
     * @param servlet
     */
    public AFActionImpressionQuittancesErreurs(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            // redirection vers destination
            AFQuittanceViewBean viewBean = new AFQuittanceViewBean();
            viewBean.setIsProcessRunning("FALSE");
            viewBean.setISession(mainDispatcher.getSession());
            session.setAttribute("viewBean", viewBean);
            _destination = getRelativeURLwithoutClassPart(request, session) + "impressionErreurs_de.jsp";
        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination = getRelativeURLwithoutClassPart(request, session) + "imprimer_de.jsp";
        AFQuittanceViewBean viewBean = null;
        if ("imprimerErreurs".equals(getAction().getActionPart())) {
            try {
                viewBean = new AFQuittanceViewBean();
                viewBean.setISession(dispatcher.getSession());
                JSPUtils.setBeanProperties(request, viewBean);
                viewBean.setIsProcessRunning("FALSE");
                AFImpressionQuittancesErreursProcess process = new AFImpressionQuittancesErreursProcess();
                process.setEMailAddress(request.getParameter("adresseEmail"));

                process.setIdJournalQuittance(viewBean.getIdJournalQuittance());
                process.setSession((BSession) dispatcher.getSession());
                process.executeProcess();

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