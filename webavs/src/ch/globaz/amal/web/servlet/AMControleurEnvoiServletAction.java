/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.vb.controleurEnvoi.AMControleurEnvoiViewBean;
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
public class AMControleurEnvoiServletAction extends AMAbstractServletAction {

    public AMControleurEnvoiServletAction(FWServlet aServlet) {
        super(aServlet);
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
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURENVOI + ".afficher";
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

        String expandedJob = request.getParameter("expandedJob");
        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURENVOI + ".afficher";
        if (expandedJob.length() > 0) {
            destination += "&expandedJob=" + expandedJob;
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

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");

        AMControleurEnvoiViewBean currentViewBean = (AMControleurEnvoiViewBean) viewBean;
        currentViewBean.setSelectedJob(selectedJob);
        currentViewBean.setSelectedStatus(selectedStatus);

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
    public String changeJobStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");
        String expandedJob = request.getParameter("expandedJob");
        String newStatus = request.getParameter("newStatus");

        AMControleurEnvoiViewBean currentViewBean = (AMControleurEnvoiViewBean) viewBean;
        currentViewBean.setSelectedJob(selectedJob);
        currentViewBean.setSelectedStatus(selectedStatus);
        currentViewBean.setApplyNewStatus(newStatus);

        try {
            // si newStatus à 0, réaffichage de la page uniquement.
            if ((newStatus != null) && (newStatus.length() > 0)) {
                currentViewBean.changeStatus();
            }
        } catch (Exception e) {
            JadeLogger.info(this, "Problem changing status : " + e.toString());
        }

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURENVOI + ".afficher";
        if (expandedJob.length() > 0) {
            destination += "&expandedJob=" + expandedJob;
        }

        return destination;
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
    public String launchPrintProcess(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedJob = request.getParameter("selectedJob");
        String selectedStatus = request.getParameter("selectedStatus");
        String expandedJob = request.getParameter("expandedJob");

        AMControleurEnvoiViewBean currentViewBean = (AMControleurEnvoiViewBean) viewBean;
        currentViewBean.setSelectedJob(selectedJob);
        currentViewBean.setSelectedStatus(selectedStatus);

        try {
            currentViewBean.launchPrintProcess();
        } catch (Exception e) {
            JadeLogger.info(this, "Problem launching process : " + e.toString());
        }

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_CONTROLEURENVOI + ".afficher";
        if (expandedJob.length() > 0) {
            destination += "&expandedJob=" + expandedJob;
        }

        return destination;
    }

}
