package globaz.hercule.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.hercule.db.controleEmployeur.CEControlesAttribuesViewBean;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 * @since Créé le 12 févr. 07
 */
public class CEActionControlesAttribues extends FWDefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFListeControlesAttribues.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public CEActionControlesAttribues(FWServlet servlet) {
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = null;

        try {
            CEControlesAttribuesViewBean viewBean = new CEControlesAttribuesViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            session.setAttribute("viewBean", viewBean);
            mainDispatcher.dispatch(viewBean, getAction());

            _destination = getRelativeURL(request, session) + "_de.jsp";

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}