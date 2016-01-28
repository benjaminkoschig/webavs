package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPLienTypeDecRemarqueListViewBean;
import globaz.phenix.db.principale.CPLienTypeDecRemarqueViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionLienTypeDecRemarque extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionLienTypeDecRemarque(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        CPLienTypeDecRemarqueViewBean viewBean = new CPLienTypeDecRemarqueViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdRemarqueType(request.getParameter("idRemarqueType"));
            // Sauvegarde identifiant en paramètre pour le récupérer par exemple
            // lors de la création
            session.setAttribute("idRemarqueType", request.getParameter("idRemarqueType"));
            mainController.dispatch(viewBean, getAction());
            // transferer dans le helper
            // viewBean._chargerEntete();
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        super.actionChercher(session, request, response, mainController);
        // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true)
        // servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(
        // request,
        // response);
        // else
        // servlet
        // .getServletContext()
        // .getRequestDispatcher(getRelativeURL(request,session)+"_rc.jsp")
        // .forward(request, response);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPLienTypeDecRemarqueListViewBean vBean = (CPLienTypeDecRemarqueListViewBean) viewBean;
        // Positionnement selon les critères de recherche
        vBean.setForIdRemarqueType((String) session.getAttribute("idRemarqueType"));
        return vBean;
    }

    /*
     * Traitement lors d'une création avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPLienTypeDecRemarqueViewBean vBean = (CPLienTypeDecRemarqueViewBean) viewBean;
        // Récupération de l'id remarque type
        vBean.setIdRemarqueType((String) session.getAttribute("idRemarqueType"));
        return vBean;
    }
}
