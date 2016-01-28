package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.process.IJListeControleViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJListeControleAction extends IJDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererFormulaireAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJListeControleAction(FWServlet servlet) {
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
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            IJListeControleViewBean gcViewBean = (IJListeControleViewBean) viewBean;

            session.setAttribute("viewBean", gcViewBean);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
