/*
 * Créé le 17 juillet 2008
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.process.REEnvoyerAnnoncesViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REEnvoyerAnnoncesAction extends REDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REEnvoyerAnnoncesAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public REEnvoyerAnnoncesAction(FWServlet servlet) {
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
        REEnvoyerAnnoncesViewBean viewBean = (REEnvoyerAnnoncesViewBean) (session.getAttribute("viewBean"));

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }

        // on set la date d'envoi (qui sera vide si ce mois annee comptable n'a
        // pas encore été envoyé, sinon la date
        // d'envoi de cette période
        viewBean.setForDateEnvoi();

        // si on est ok pour réenvoyer ou qu'il ne s'agit pas d'un réenvoi, on
        // execute le process.
        if (viewBean.isOkPourReenvoi() || JAUtil.isDateEmpty(viewBean.getForDateEnvoi())) {
            super.actionExecuter(session, request, response, mainDispatcher);
        } else {
            // sinon on redirige vers la page pour demander confirmation de
            // réenvoi
            viewBean.setDejaEnvoye(true);
            session.setAttribute("viewBean", viewBean);
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        }
    }
}
