package globaz.aquila.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class COActionImprimer extends CODefaultServletAction {

    public COActionImprimer(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);
            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}