package globaz.ij.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.ij.vb.process.IJListeFormulairesNonRecusViewBean;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 * 
 *         Action afficher pour liste des formulaires non reçus
 */
public class IJFormulaireAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFormulaireAction.
     * 
     */

    public IJFormulaireAction(FWServlet servlet) {
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

        IJListeFormulairesNonRecusViewBean viewBean = new IJListeFormulairesNonRecusViewBean();
        saveViewBean(viewBean, session);
        viewBean.setISession(mainDispatcher.getSession());
        forward(getRelativeURL(request, session) + "_de.jsp", request, response);
    }

}
