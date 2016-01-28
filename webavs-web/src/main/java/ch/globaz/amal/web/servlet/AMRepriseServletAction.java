/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.vb.reprise.AMRepriseViewBean;
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
public class AMRepriseServletAction extends AMAbstractServletAction {

    public AMRepriseServletAction(FWServlet aServlet) {
        super(aServlet);
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
    public String launchRepriseTiersJob(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        // Get value from formulaire
        // -------------------------------------------------------------------------
        String checkDataBeforeReprise = request.getParameter("checkDataBeforeReprise");
        String isSimulation = request.getParameter("isSimulation");
        String askToStop = request.getParameter("askToStop");
        Boolean checkDataBeforeRepriseBoolean = false;
        Boolean isSimulationBoolean = true;
        Boolean askToStopBoolean = true;
        try {
            checkDataBeforeRepriseBoolean = Boolean.parseBoolean(checkDataBeforeReprise);
        } catch (Exception ex) {
            JadeLogger.error(this, "Error converting boolean checkDataBeforeReprise : " + ex.toString());
        }
        try {
            isSimulationBoolean = Boolean.parseBoolean(isSimulation);
        } catch (Exception ex) {
            JadeLogger.error(this, "Error converting boolean isSimulation : " + ex.toString());
        }
        try {
            askToStopBoolean = Boolean.parseBoolean(askToStop);
        } catch (Exception ex) {
            JadeLogger.error(this, "Error converting boolean askToStop : " + ex.toString());
        }
        // Set view bean variables
        // ---------------------------------------------------------------------------
        AMRepriseViewBean currentViewBean = (AMRepriseViewBean) viewBean;
        currentViewBean.setIsSimulation(isSimulationBoolean);
        currentViewBean.setCheckDataBeforeReprise(checkDataBeforeRepriseBoolean);
        currentViewBean.setAskToStop(askToStopBoolean);
        // Launch Reprise
        // ---------------------------------------------------------------------------
        currentViewBean.launchRepriseTiers();

        // Set destination
        // ---------------------------------------------------------------------------
        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_REPRISE + ".afficher";

        return destination;
    }

}
