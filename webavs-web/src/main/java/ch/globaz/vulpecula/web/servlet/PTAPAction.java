package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.ap.PTFacturationAPAjaxViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PTAPAction extends PTAbstractDefaultServletAction {
    public PTAPAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTFacturationAPAjaxViewBean) {
            PTFacturationAPAjaxViewBean vb = (PTFacturationAPAjaxViewBean) viewBean;
            vb.setIdEmployeur(request.getParameter(PTConstants.ID_EMPLOYEUR));
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

}
