package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.musca.db.facturation.FAPlanFacturationListViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionPlanFacturation extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionPlanFacturation(FWServlet servlet) {
        super(servlet);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAPlanFacturationListViewBean vBean = (FAPlanFacturationListViewBean) viewBean;
        vBean.setForIdTypeFacturation(request.getParameter("forIdTypeFacturation"));
        try {
            globaz.globall.api.BISession bSession = globaz.musca.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // Positionnement selon la zone "A partir de" selon la langue de
            // l'utilisateur
            vBean.orderByLibelleLangueSession();
            return vBean;
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
            return vBean;
        }
    }
}
