/*
 * Créé le 15 juin 05
 */
package globaz.apg.servlet;

import globaz.apg.vb.process.APGenererStatsOFASViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererStatsOFASAction extends APDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APGenererStatsOFASAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APGenererStatsOFASViewBean viewBean = (APGenererStatsOFASViewBean) (session.getAttribute("viewBean"));

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }

        super.actionExecuter(session, request, response, mainDispatcher);
    }
}
