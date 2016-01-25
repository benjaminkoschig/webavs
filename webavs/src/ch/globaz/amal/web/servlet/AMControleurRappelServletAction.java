/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.vb.controleurRappel.AMControleurRappelViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMActions;

/**
 * @author DHI
 * 
 */
public class AMControleurRappelServletAction extends AMAbstractServletAction {

    public AMControleurRappelServletAction(FWServlet aServlet) {
        super(aServlet);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURRAPPEL + ".afficher";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String expandedRappel = request.getParameter("expandedRappel");
        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURRAPPEL + ".afficher";
        if (expandedRappel.length() > 0) {
            destination += "&expandedRappel=" + expandedRappel;
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String selectedRappel = request.getParameter("selectedRappel");
        String selectedLibra = request.getParameter("selectedLibra");

        AMControleurRappelViewBean currentViewBean = (AMControleurRappelViewBean) viewBean;
        currentViewBean.setSelectedRappel(selectedRappel);
        currentViewBean.setSelectedLibra(selectedLibra);

        return super.beforeSupprimer(session, request, response, viewBean);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String generateRappel(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedRappel = request.getParameter("selectedRappel");
        String selectedLibra = request.getParameter("selectedLibra");
        String expandedRappel = request.getParameter("expandedRappel");

        AMControleurRappelViewBean currentViewBean = (AMControleurRappelViewBean) viewBean;
        currentViewBean.setSelectedRappel(selectedRappel);
        currentViewBean.setSelectedLibra(selectedLibra);

        try {
            currentViewBean.generateRappel();
        } catch (Exception e) {
            JadeLogger.info(this, "Problem generating rappels : " + e.toString());
        }

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURRAPPEL + ".afficher";
        if (expandedRappel.length() > 0) {
            destination += "&expandedRappel=" + expandedRappel;
        }

        return destination;
    }

}
