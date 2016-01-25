package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.dettecomptatcompense.PCDetteComptatCompenseViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCDetteComptatCompenseServletAction extends PCAbstractServletAction {
    public PCDetteComptatCompenseServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCDetteComptatCompenseViewBean) {
            ((PCDetteComptatCompenseViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            ((PCDetteComptatCompenseViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            ((PCDetteComptatCompenseViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
            ((PCDetteComptatCompenseViewBean) viewBean).setIdDemande(request.getParameter("idDemande"));

            if (JadeStringUtil.isEmpty(((PCDetteComptatCompenseViewBean) viewBean).getIdDemande())) {
                ((PCDetteComptatCompenseViewBean) viewBean).setIdDemande(request.getParameter("idDemandePc"));
            }

            ((PCDetteComptatCompenseViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
        }

        return viewBean;
    }
}
