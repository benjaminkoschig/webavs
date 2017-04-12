package ch.globaz.amal.web.servlet;

import globaz.amal.vb.sedexco.AMSedexcoViewBean;
import globaz.amal.vb.sedexco.AMSedexcocomparaisonViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMActions;

public class AMSedexCOServletAction extends AMAbstractServletAction {

    public AMSedexCOServletAction(FWServlet aServlet) {
        super(aServlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = new AMSedexcoViewBean();
        String selectedId = request.getParameter("selectedId");
        ((AMSedexcoViewBean) viewBean).setId(selectedId);
        session.setAttribute("viewBean", viewBean);
        super.actionExecuter(session, request, response, mainDispatcher);
    }

    public String launchGenerationComparaison(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        AMSedexcocomparaisonViewBean listesViewBean = new AMSedexcocomparaisonViewBean();

        String email = request.getParameter("email");
        String annee = request.getParameter("annee");
        String idTiersCM = request.getParameter("idTiersCM");

        listesViewBean.setEmail(email);
        listesViewBean.setIdTiersCM(idTiersCM);
        listesViewBean.setAnnee(annee);
        listesViewBean.launchGenerationComparaison();

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_SEDEX_CO_COMPARAISON + ".afficher";

        return destination;
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");
        String id = request.getParameter("selectedId");

        AMSedexcoViewBean viewBean = new AMSedexcoViewBean();
        viewBean.setId(id);
        if ((action != null) && (action.indexOf("amal.sedexco.sedexco.imprimerListe") > -1)) {
            dispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                    .forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    // @Override
    // protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
    // FWDispatcher dispatcher) throws ServletException, IOException {
    // String destination = FWDefaultServletAction.ERROR_PAGE;
    // FWViewBeanInterface viewBean;
    //
    // try {
    // viewBean = this.loadViewBean(session);
    //
    // if (viewBean != null) {
    //
    // JSPUtils.setBeanProperties(request, viewBean);
    // }
    // FWAction action = FWAction.newInstance(request.getParameter("userAction"));
    // dispatcher.dispatch(viewBean, action);
    //
    // Method methode = this.getClass().getMethod(getAction().getActionPart(), AMAbstractServletAction.PARAMS);
    //
    // destination = (String) methode.invoke(this,
    // new Object[] { session, request, response, dispatcher, viewBean });
    // } catch (Exception e) {
    // JadeLogger.error("Error in AMActionCustom", e);
    // }
    //
    // // desactive le forward pour le cas ou la reponse a deja ete flushee
    // if (!JadeStringUtil.isBlank(destination)) {
    // goSendRedirect(destination, request, response);
    // }
    // }

}
