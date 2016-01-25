/*
 * Créé le 30 juil. 07
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.ordresversements.RFOrdresVersementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author FHA
 * 
 */
public class RFOrdresVersementsAction extends PRDefaultAction {

    /**
     * @param servlet
     */
    public RFOrdresVersementsAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        FWViewBeanInterface viewBean = new RFOrdresVersementsViewBean();
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            this.saveViewBean(viewBean, request);
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }

        // viewBean.setSession((BSession) mainDispatcher.getSession());
        viewBean = mainDispatcher.dispatch(viewBean, newAction);

        /*
         * if (session.getAttribute("ERROR_MSG") != null) { viewBean.setMessage((String)
         * session.getAttribute("ERROR_MSG")); viewBean.setMsgType(FWViewBeanInterface.ERROR);
         * session.removeAttribute("ERROR_MSG"); }
         */

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

}
