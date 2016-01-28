package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCFratrieServletAction extends PCAbstractServletAction {

    public PCFratrieServletAction(FWServlet aServlet) {
        super(aServlet);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String appPart = getAction().getApplicationPart();

        return "/" + appPart + "?userAction=" + appPart + ".droit.droit.chercher&idDroit="
                + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion") + "&idDemandePc="
                + request.getParameter("idDemandePc") + "&idDossier=" + request.getParameter("idDossier");

    }
}
