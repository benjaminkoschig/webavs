package globaz.tucana.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.tucana.application.TUApplication;
import globaz.tucana.vb.journal.TUJournalRecapF1V1ViewBean;
import globaz.tucana.vb.journal.TUJournalRecapF1V2ViewBean;
import globaz.tucana.vb.journal.TUJournalRecapF2ViewBean;
import globaz.tucana.vb.journal.TUJournalRecapF3ViewBean;
import globaz.tucana.vb.journal.TUJournalRecapF4ViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Définition de l'action pour le package journal
 * 
 * @author fgo date de création : 3 juil. 06
 * @version : version 1.0
 */
public class TUActionJournal extends TUActionTucanaDefault {

    /**
     * @param servlet
     */
    public TUActionJournal(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUJournalRecapF1V2ViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else {
            return super._getDestExecuterEchec(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUJournalRecapF1V1ViewBean) {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            if ("generer".equals(action.getActionPart())) {
                // Redirection sur file xml
                request.setAttribute("journal", viewBean);
                return getRelativeURL(request, session) + "_xsl.jsp";
            } else {
                // Redirection, bouclement_rc
                return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
            }

        } else if (viewBean instanceof TUJournalRecapF2ViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else if (viewBean instanceof TUJournalRecapF3ViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else if (viewBean instanceof TUJournalRecapF4ViewBean) {
            // Redirection, bouclement_rc
            request.setAttribute("journal", viewBean);
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else if (viewBean instanceof TUJournalRecapF1V2ViewBean) {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            if ("generer".equals(action.getActionPart())) {
                // Redirection sur file xml
                request.setAttribute("journal", viewBean);
                return getRelativeURL(request, session) + "_xsl.jsp";
            } else {
                // Redirection, bouclement_rc
                return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
            }
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAfficher(session, request, response, mainDispatcher);
        // String destination = "";
        // FWAction action = new FWAction(request.getParameter("userAction"),
        // FWSecureConstants.READ);
        //
        // FWViewBeanInterface vBean =
        // FWViewBeanActionFactory.newInstance(action,
        // mainDispatcher.getPrefix());
        //
        // vBean.setISession(mainDispatcher.getSession());
        // // vBean = mainDispatcher.dispatch(vBean, getAction());
        // session.setAttribute("viewBean", vBean);
        // destination = getRelativeURL(request, session) +
        // "_de.jsp?_method=add&_valid=error";
        // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
        // response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#goSendRedirect(java .lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (url.indexOf("tucanaRoot") > -1) {
            servlet.getServletContext().getRequestDispatcher(url).forward(request, response);
        } else {
            super.goSendRedirect(url, request, response);
        }
    }

}
