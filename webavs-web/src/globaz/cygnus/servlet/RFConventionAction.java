// créé le 24 mars 2010
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.conventions.RFConventionListViewBean;
import globaz.cygnus.vb.conventions.RFConventionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
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

/**
 * author fha
 */
public class RFConventionAction extends RFDefaultAction {

    public RFConventionAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            return this.getUserActionURL(request, IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION,
                    FWAction.ACTION_CHERCHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            return this.getUserActionURL(request, IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION,
                    FWAction.ACTION_CHERCHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFConventionViewBean viewBean = new RFConventionViewBean();

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        this.saveViewBean(viewBean, request);

        if ((viewBean.getSession() == null) || viewBean.hasErrors()) {
            // si on rentre dans cygnus et qu'on a pas les droits, la session
            // vaut null
            forward(FWDefaultServletAction.ERROR_PAGE, request, response);
        } else {
            forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        viewBean = new RFConventionViewBean();

        if (!"add".equals(request.getParameter("_method"))) {

            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                RFUtils.setMsgErreurInattendueViewBean(viewBean, "beforeAfficher", "RFConventionAction");
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        RFConventionViewBean outputViewBean = (RFConventionViewBean) viewBean;
        outputViewBean.setIsAjout(Boolean.TRUE);
        return outputViewBean;
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFConventionListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

}
