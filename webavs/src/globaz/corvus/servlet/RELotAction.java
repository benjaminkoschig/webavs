package globaz.corvus.servlet;

import globaz.corvus.vb.lots.RELotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.servlet.PRDefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RELotAction extends PRDefaultAction {

    public RELotAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    public void afficherLotDepuisDecision(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String _destination = "";

        RELotViewBean vb = new RELotViewBean();

        try {
            JSPUtils.setBeanProperties(request, vb);

            vb.setSession((BSession) mainDispatcher.getSession());
            vb.retrieve();

            if (vb.isNew()) {
                throw new Exception(((BSession) mainDispatcher.getSession()).getLabel("ERREUR_LOT_NON_TROUVE"));
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);

            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}
