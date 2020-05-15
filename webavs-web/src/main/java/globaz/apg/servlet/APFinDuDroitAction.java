/*
 * Créé le 12 sept. 05
 */
package globaz.apg.servlet;

import globaz.apg.vb.process.APGenererDroitPandemieFinDroitViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <H1>Description</H1>
 *
 */
public class APFinDuDroitAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceAction.
     *
     * @param servlet
     *            DOCUMENT ME!
     */
    public APFinDuDroitAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APGenererDroitPandemieFinDroitViewBean viewBean = new APGenererDroitPandemieFinDroitViewBean();
        String idDroit = request.getParameter("forIdDroit");
        if (!JadeStringUtil.isEmpty(idDroit)) {
            viewBean.setIdDroit(idDroit);
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        forward(getRelativeURL(request, session) + "_de.jsp?", request, response);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        APGenererDroitPandemieFinDroitViewBean viewBean = (APGenererDroitPandemieFinDroitViewBean) (session.getAttribute("viewBean"));

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE)
                    .forward(request, response);
        }

        super.actionExecuter(session, request, response, mainDispatcher);

    }

}
