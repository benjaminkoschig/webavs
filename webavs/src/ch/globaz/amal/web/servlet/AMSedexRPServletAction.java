package ch.globaz.amal.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.decision.PCDecomptViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AMSedexRPServletAction extends AMAbstractServletAction {

    public AMSedexRPServletAction(FWServlet aServlet) {
        super(aServlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCDecomptViewBean) {
            ((PCDecomptViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            ((PCDecomptViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            ((PCDecomptViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
            ((PCDecomptViewBean) viewBean).setIdDemande(request.getParameter("idDemandePc"));
            if (JadeStringUtil.isEmpty(((PCDecomptViewBean) viewBean).getIdDemande())) {
                ((PCDecomptViewBean) viewBean).setIdDemande(request.getParameter("idDemande"));
            }
            ((PCDecomptViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
