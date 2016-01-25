package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModulePassageListViewBean;
import globaz.musca.db.facturation.FAModulePassageViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionModulePassage extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionModulePassage(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        FAModulePassageViewBean viewBean = new FAModulePassageViewBean();
        viewBean.setIdPassage(request.getParameter("idPassage"));
        // Sauvegarde de l'id passage passé en paramètre pour le récupérer lors
        // de la création
        session.setAttribute("saveIdPassage", request.getParameter("idPassage"));
        //
        viewBean.setMessage("OK");
        viewBean.setMsgType(FWViewBeanInterface.OK);

        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        } else {
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                    .forward(request, response);
        }
    }

    protected FWViewBeanInterface beforeAdd(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        FAModulePassageViewBean vBean = (FAModulePassageViewBean) viewBean;
        // Récupération de l'id passage concerné (sauvegardé dans l'action
        // chercher)
        vBean.setIdPassage((String) session.getAttribute("saveIdPassage"));

        return vBean;
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        FAModulePassageViewBean vBean = (FAModulePassageViewBean) viewBean;
        // Sauvegarde du module de facturation pour test dans _validate du
        // viewBean
        vBean.setIdPassage((String) session.getAttribute("saveIdPassage"));
        vBean.setIdModuleFacturation(request.getParameter("selectedId2"));

        return vBean;
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAModulePassageListViewBean vBean = (FAModulePassageListViewBean) viewBean;
        try {
            globaz.globall.api.BISession bSession = globaz.musca.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // Positionnement pour l'id plan sélectionné
            vBean.setForIdPassage((String) session.getAttribute("saveIdPassage"));
            // ordre par niveau d'appel
            vBean.orderByNiveauAppel();
            vBean.orderByLibelleLangueSession();

            return vBean;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return vBean;
        }
    }
}
