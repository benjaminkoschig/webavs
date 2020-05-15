/*
 */
package globaz.apg.servlet;

import globaz.apg.vb.process.APGenererDroitPandemieMensuelViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 */
public class APGenererDroitPandemieMensuelAction extends APDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererDroitPandemieMensuelAction.
     *
     * @param servlet
     *            DOCUMENT ME!
     */
    public APGenererDroitPandemieMensuelAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        APGenererDroitPandemieMensuelViewBean viewBean = (APGenererDroitPandemieMensuelViewBean) (session.getAttribute("viewBean"));

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE)
                    .forward(request, response);
        }

        super.actionExecuter(session, request, response, mainDispatcher);

    }


}
