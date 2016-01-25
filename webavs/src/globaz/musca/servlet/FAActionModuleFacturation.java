package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModuleFacturationListViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionModuleFacturation extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionModuleFacturation(FWServlet servlet) {
        super(servlet);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAModuleFacturationListViewBean vBean = (FAModuleFacturationListViewBean) viewBean;
        try {
            globaz.globall.api.BISession bSession = globaz.musca.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            vBean.orderByNiveauAppel();
            vBean.orderByLibelleLangueSession();
            /*
             * String langue = vBean.getSession().getIdLangueISO(); if (langue.equals("AL")) {
             * vBean.setFromLibelleDe(request.getParameter("fromLibelle")); vBean.orderByLibelleDe(); } if
             * (langue.equals("FR")) { vBean.setFromLibelleFr(request.getParameter("fromLibelle"));
             * vBean.orderByLibelleFr(); } if (langue.equals("IT")) {
             * vBean.setFromLibelleIt(request.getParameter("fromLibelle")); vBean.orderByLibelleIt(); }
             */
            return vBean;
        } catch (Exception e) {

            JadeLogger.error(this, e);
            return vBean;
        }
    }
}
