/*
 * Créé le 21 décembre 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.paiement.RFLotViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author fha
 */
public class RFLotAction extends RFDefaultAction {

    public RFLotAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFLotViewBean viewBean = new RFLotViewBean();

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        this.saveViewBean(viewBean, request);

        if ((viewBean.getSession() == null) || viewBean.hasErrors()) {
            // si on rentre dans cygnus et qu'on a pas les droits, la session
            // vaut null
            forward(FWDefaultServletAction.ERROR_PAGE, request, response);
        } else {
            forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
        }

    }

}
