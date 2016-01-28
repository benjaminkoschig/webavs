package globaz.libra.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.libra.vb.listes.LIListesViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LIListesAction extends LIDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public LIListesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            LIListesViewBean vb = new LIListesViewBean();
            vb.setISession(mainDispatcher.getSession());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}
