package ch.globaz.amal.web.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AMPrimesAssuranceServletAction extends AMAbstractServletAction {

    public AMPrimesAssuranceServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAfficherAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("true".equals(request.getParameter("page"))) {
            // regader que l'on a le bon vieBea
            actionAfficher(session, request, response, mainDispatcher);
        } else {
            super.actionAfficherAJAX(session, request, response, mainDispatcher);
        }
    }
}
