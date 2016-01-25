package globaz.lyra.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.lyra.vb.echeances.LYEcheanceViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LYEcheancesAction extends LYDefaultAction {

    public LYEcheancesAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        LYEcheanceViewBean viewBean = new LYEcheanceViewBean();

        viewBean.setCsDomaineApplicatifParDefaut(request.getParameter("forDomaineApplicatif"));

        beforeNouveau(session, request, response, viewBean);

        this.saveViewBean(viewBean, request);
        this.saveViewBean(null, session);

        mainDispatcher.dispatch(viewBean, getAction());

        forward(getRelativeURL(request, session) + "_de.jsp", request, response);
    }
}
