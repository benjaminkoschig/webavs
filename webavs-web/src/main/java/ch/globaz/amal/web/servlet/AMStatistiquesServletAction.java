package ch.globaz.amal.web.servlet;

import globaz.amal.vb.statistiques.AMPublipostageViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMActions;

public class AMStatistiquesServletAction extends AMAbstractServletAction {

    public AMStatistiquesServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    public String launchListePublipostage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        AMPublipostageViewBean listesViewBean = new AMPublipostageViewBean();
        String yearSubside = request.getParameter("yearSubside");
        String fields = request.getParameter("hidden_wantedFields");
        String recordsSize = request.getParameter("sizeRecords");
        String wantedNpa = request.getParameter("wantedNpa");
        String inTypeDemande = request.getParameter("hidden_inTypeDemande");
        String inNumeroContribuable = request.getParameter("hidden_inNumeroContribuable");
        String wantOnlyContribuablePrincipal = request.getParameter("wantOnlyContribuablePrincipal");
       
        String wantOnlyCasAvecCarteCulture = request.getParameter("wantOnlyCasAvecCarteCulture");
        String wantOnlySubsidesActifs = request.getParameter("wantOnlySubsidesActifs");
        boolean isContribuable = false;
        if ("yes".equals(wantOnlyContribuablePrincipal)) {
            isContribuable = true;
        }

        boolean isCodeActif = true;
        if (!"yes".equals(wantOnlySubsidesActifs)) {
            isCodeActif = false;
        }

        boolean isCarteCulture = false;
        if ("yes".equals(wantOnlyCasAvecCarteCulture)) {
            isCarteCulture = true;
        }

        int iRecordsSize;
        try {
            iRecordsSize = Integer.parseInt(recordsSize);
        } catch (Exception e) {
            iRecordsSize = 0;
        }
        listesViewBean.setYearSubside(yearSubside);
        listesViewBean.setWantedFields(fields);
        listesViewBean.setWantedNpa(wantedNpa);
        listesViewBean.setInTypeDemande(inTypeDemande);
        listesViewBean.setInNumeroContribuable(inNumeroContribuable);
        listesViewBean.setIsContribuable(isContribuable);
        listesViewBean.setIsCarteCulture(isCarteCulture);
        listesViewBean.setCodeActif(isCodeActif);
        listesViewBean.setRecordsSize(iRecordsSize);
        listesViewBean.launchListePublipostage();

        String destination = "";
        destination = "/amal?userAction=" + IAMActions.ACTION_PUBLIPOSTAGE + ".afficher";

        return destination;
    }
}
