package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModuleImpressionListViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionModuleImpression extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionModuleImpression(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    public void actionImprimer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                .forward(request, response);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAModuleImpressionListViewBean vBean = (FAModuleImpressionListViewBean) viewBean;
        try {
            globaz.globall.api.BISession bSession = globaz.musca.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // Positionnement selon la zone "A partir de " dans la langue de
            // l'utilisateur
            String langue = vBean.getSession().getIdLangueISO();
            if (langue.equals("AL")) {
                vBean.setFromLibelleDe(request.getParameter("fromLibelle"));
                vBean.orderByLibelleDe();
            }
            if (langue.equals("FR")) {
                vBean.setFromLibelleFr(request.getParameter("fromLibelle"));
                vBean.orderByLibelleFr();
            }
            if (langue.equals("IT")) {
                vBean.setFromLibelleIt(request.getParameter("fromLibelle"));
                vBean.orderByLibelleIt();
            }
            return vBean;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return vBean;
        }
    }
}
