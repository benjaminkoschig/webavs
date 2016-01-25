package globaz.cygnus.servlet;

import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesListViewBean;
import globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RFPrestationsAccordeesAction extends RFDefaultAction {

    public RFPrestationsAccordeesAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFPrestationsAccordeesViewBean viewBean = new RFPrestationsAccordeesViewBean();

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

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
        RFPrestationsAccordeesViewBean outputViewBean = (RFPrestationsAccordeesViewBean) viewBean;
        outputViewBean = (RFPrestationsAccordeesViewBean) session.getAttribute("viewBean");
        try {
            JSPUtils.setBeanProperties(request, outputViewBean);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outputViewBean;
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFPrestationsAccordeesListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }
}
