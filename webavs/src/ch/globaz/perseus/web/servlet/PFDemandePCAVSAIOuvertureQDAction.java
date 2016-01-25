package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.vb.demande.PFDemandePCAVSAIOuvertureQDViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PFDemandePCAVSAIOuvertureQDAction extends PFAbstractDefaultServletAction {

    public PFDemandePCAVSAIOuvertureQDAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        viewBean = super.beforeAfficher(session, request, response, viewBean);
        if (viewBean instanceof PFDemandePCAVSAIOuvertureQDViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDemande"))) {
                ((PFDemandePCAVSAIOuvertureQDViewBean) viewBean).setId(request.getParameter("idDemande"));
            }
        }

        return viewBean;
    }

}
