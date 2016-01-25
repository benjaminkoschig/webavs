package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CATransfertSoldesViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les actions de transfert de soldes.
 * 
 * @author DDA
 */
public class CATransfertSoldesAction extends CADefaultServletAction {

    public static final String ACTION_SUITE = "transfertSoldes";
    public static final String AFFICHER_TRANSFERT_SOLDES = "afficherTransfertSoldes";

    /**
     * @param servlet
     */
    public CATransfertSoldesAction(FWServlet servlet) {
        super(servlet);
    }

    private void actionAfficherTransfertSoldes(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            CATransfertSoldesViewBean viewBean = (CATransfertSoldesViewBean) getViewBean(request,
                    request.getParameter("userAction"), true);
            viewBean.setISession(dispatcher.getSession());

            viewBean.setIdSourceCompteAnnexe(getId(request, "idCompteAnnexe"));

            setSessionAttribute(session, VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String actionSuite = getActionSuite(request);

        if (ACTION_SUITE.equals(actionSuite)) {
            if (AFFICHER_TRANSFERT_SOLDES.equals(getAction().getActionPart())) {
                actionAfficherTransfertSoldes(session, request, response, dispatcher);
            }
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = (CATransfertSoldesViewBean) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);

            ((CATransfertSoldesViewBean) viewBean).setUseJournalJournalier(true);
            ((CATransfertSoldesViewBean) viewBean).setSendEmail(true);

            viewBean = mainDispatcher.dispatch(viewBean, action);
            request.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }
}
