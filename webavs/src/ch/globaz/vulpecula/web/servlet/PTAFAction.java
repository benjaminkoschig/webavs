package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.af.PTDroitsAjaxViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PTAFAction extends PTDefaultServletAction {

    public PTAFAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDroitsAjaxViewBean) {
            PTDroitsAjaxViewBean vb = (PTDroitsAjaxViewBean) viewBean;
            String idTiers = request.getParameter(PTConstants.ID_TIERS);
            vb.setIdTiers(idTiers);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }
}
