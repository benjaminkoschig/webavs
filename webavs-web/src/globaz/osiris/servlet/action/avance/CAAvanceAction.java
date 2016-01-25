package globaz.osiris.servlet.action.avance;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.osiris.db.avance.CAAvanceListViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la saisie des avances.
 * 
 * @author DDA
 */
public class CAAvanceAction extends CADefaultServletAction {

    public CAAvanceAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String destination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CAAvanceListViewBean viewBean = new CAAvanceListViewBean();
            viewBean.setISession(mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (CAAvanceListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (CAAvanceListViewBean) mainDispatcher.dispatch(viewBean, action);
            request.setAttribute(VBL_ELEMENT, viewBean);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
