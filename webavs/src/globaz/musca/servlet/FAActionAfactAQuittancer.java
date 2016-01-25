package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassageViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionAfactAQuittancer extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionAfactAQuittancer(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        FAPassageViewBean viewBean = new FAPassageViewBean();
        try {
            viewBean.setSession((BSession) dispatcher.getSession());
            String passageId = request.getParameter("selectedId");
            viewBean.setIdPassage(passageId);
            viewBean.retrieve();
        } catch (Exception e) {
            _destination = ERROR_PAGE;
            JadeLogger.error(this, e);
        }

        // GESTION DES DROITS
        viewBean = (FAPassageViewBean) dispatcher.dispatch(viewBean, getAction());

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + "afactAQuittancer_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
