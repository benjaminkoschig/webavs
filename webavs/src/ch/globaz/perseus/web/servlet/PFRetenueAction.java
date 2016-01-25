package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.retenue.PFRetenueViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PFRetenueAction extends PFAbstractDefaultServletAction {

    public PFRetenueAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFRetenueViewBean) {
            ((PFRetenueViewBean) viewBean).setId(request.getParameter("idPcfAccordee"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
