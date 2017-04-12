package ch.globaz.amal.web.servlet;

import globaz.amal.vb.sedexco.AMSedexcocomparaisonViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMActions;

public class AMSedexCOServletAction extends AMAbstractServletAction {

    public AMSedexCOServletAction(FWServlet aServlet) {
        super(aServlet);
        // TODO Auto-generated constructor stub
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

}
