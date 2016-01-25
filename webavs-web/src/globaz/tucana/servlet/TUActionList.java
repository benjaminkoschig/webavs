package globaz.tucana.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.tucana.application.TUApplication;
import globaz.tucana.vb.list.TUAffiliationProblemListViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe définissant les actions du package list
 * 
 * @author fgo date de création : 19.06.2007
 * @version : version 1.0
 * 
 */
public class TUActionList extends TUActionTucanaDefault {

    /**
     * @param servlet
     */
    public TUActionList(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUAffiliationProblemListViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
            // } else if (viewBean instanceof TUImportationBouclementViewBean) {
            // return "/" + TUApplication.APPLICATION_NAME +
            // "?userAction=tucana.bouclement.bouclement.chercher";
            // } else if (viewBean instanceof TUValidationBouclementViewBean) {
            // return "/" + TUApplication.APPLICATION_NAME +
            // "?userAction=tucana.bouclement.bouclement.chercher";
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
        if (viewBean instanceof TUAffiliationProblemListViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
            // } else if (viewBean instanceof TUImportationBouclementViewBean) {
            // return "/" + TUApplication.APPLICATION_NAME +
            // "?userAction=tucana.bouclement.bouclement.chercher";
            // } else if (viewBean instanceof TUValidationBouclementViewBean) {
            // return "/" + TUApplication.APPLICATION_NAME +
            // "?userAction=tucana.bouclement.bouclement.chercher";
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
        // if ("affiliationProblemList".equals(getAction().getClassPart())){
        // String destination = "";
        // FWAction action = new FWAction(request.getParameter("userAction"),
        // FWSecureConstants.READ);
        //
        // FWViewBeanInterface vBean = (FWViewBeanInterface)
        // FWViewBeanActionFactory.newInstance(action,
        // mainDispatcher.getPrefix());
        //
        // vBean.setISession(mainDispatcher.getSession());
        // vBean = mainDispatcher.dispatch(vBean, getAction());
        // session.setAttribute("viewBean", vBean);
        // destination = getRelativeURL(request, session) +
        // "_de.jsp?_method=add&_valid=error";
        // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
        // response);
        // } else {
        // super.actionAfficher(session, request, response, mainDispatcher);
        // }
    }

}
