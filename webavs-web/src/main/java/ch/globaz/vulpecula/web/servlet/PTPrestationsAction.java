package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.prestations.PTSaisierapideViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PTPrestationsAction extends PTDefaultServletAction {
    public PTPrestationsAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTSaisierapideViewBean) {
            PTSaisierapideViewBean vb = (PTSaisierapideViewBean) viewBean;
            vb.setIdTravailleur(request.getParameter(PTConstants.ID_TRAVAILLEUR));
            vb.setTypePrestation(request.getParameter(PTConstants.TYPE_PRESTATION));
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }
}
