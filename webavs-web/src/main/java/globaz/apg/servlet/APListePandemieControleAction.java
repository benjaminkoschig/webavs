package globaz.apg.servlet;

import globaz.apg.vb.process.APListePandemieControleViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class APListePandemieControleAction extends APDefaultProcessAction {
    /**
     * Crée une nouvelle instance de la classe APDefaultProcessAction.
     *
     * @param servlet DOCUMENT ME!
     */
    public APListePandemieControleAction(FWServlet servlet) {
        super(servlet);
    }
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        APListePandemieControleViewBean viewBean = (APListePandemieControleViewBean) (session.getAttribute("viewBean"));

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE)
                    .forward(request, response);
        }

        super.actionExecuter(session, request, response, mainDispatcher);

    }

}
