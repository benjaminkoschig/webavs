package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.lot.PCOrdreVersementViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCLotServletAction extends PCAbstractServletAction {

    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public PCLotServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAfficherAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = null;

        try {
            String selectedId = request.getParameter("idEntity");
            destination = getAJAXAfficherSuccessDestination(session, request);
            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            FWAJAXViewBeanInterface viewBean = getAJAXViewBean(mainDispatcher, selectedId);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * initialisation du viewBean appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = (FWAJAXViewBeanInterface) beforeAfficher(session, request, response, viewBean);
            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            request.setAttribute("exception", e);
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCOrdreVersementViewBean) {
            PCOrdreVersementViewBean viewBean1 = (PCOrdreVersementViewBean) viewBean;
            /*
             * viewBean1.getOrdreVersement().getSimpleOrdreVersement()
             * .setIdOrdreVersement(JadeStringUtil.toNotNullString(request.getParameter("idOrdreVersement")));
             */

        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
