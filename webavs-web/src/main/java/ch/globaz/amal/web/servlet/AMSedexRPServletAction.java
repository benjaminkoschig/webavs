package ch.globaz.amal.web.servlet;

import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import globaz.amal.vb.sedexrp.AMSedexrpListViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.decision.PCDecomptViewBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            AMSedexrpListViewBean sedexrpListViewBean = (AMSedexrpListViewBean) viewBean;

            if (sedexrpListViewBean.getSearchModel().getInSDXMessageSubType() == null
                    || sedexrpListViewBean.getSearchModel().getInSDXMessageSubType().isEmpty()) {
                sedexrpListViewBean.getSearchModel().setInSDXMessageSubType(new ArrayList<>(AMMessagesSubTypesAnnonceSedex.getSortedEnumsExeptPT().stream().map(AMMessagesSubTypesAnnonceSedex::getValue).collect(Collectors.toList())));
            }

            viewBean = beforeLister(session, request, response, sedexrpListViewBean);
            viewBean = mainDispatcher.dispatch(sedexrpListViewBean, _action);

            request.setAttribute("viewBean", sedexrpListViewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", sedexrpListViewBean);
            _destination = this.getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception var6) {
            _destination = "/errorPage.jsp";
        }

        this.servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
