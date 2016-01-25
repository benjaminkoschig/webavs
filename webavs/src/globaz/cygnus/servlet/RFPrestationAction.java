/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.servlet;

/**
 * @author FHA
 * 
 */

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.paiement.RFPrestationDetailViewBean;
import globaz.cygnus.vb.paiement.RFPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RFPrestationAction extends RFDefaultAction {

    public RFPrestationAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFPrestationViewBean viewBean = new RFPrestationViewBean();

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);
        viewBean.setIdLot(request.getParameter("idLot"));
        this.saveViewBean(viewBean, request);

        /*
         * choix destination
         */
        String _destination = "";

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {

            RFPrestationDetailViewBean outpoutVb = new RFPrestationDetailViewBean();
            outpoutVb.setSession((BSession) viewBean.getISession());

            JSPUtils.setBeanProperties(request, outpoutVb);
            outpoutVb.setId(request.getParameter("selectedId"));
            return super.beforeAfficher(session, request, response, outpoutVb);

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }

    }

}
