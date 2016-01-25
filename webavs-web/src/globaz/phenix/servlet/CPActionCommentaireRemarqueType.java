package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPCommentaireRemarqueTypeListViewBean;
import globaz.phenix.db.principale.CPCommentaireRemarqueTypeViewBean;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionCommentaireRemarqueType extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionCommentaireRemarqueType(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        CPCommentaireRemarqueTypeViewBean viewBean = new CPCommentaireRemarqueTypeViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdRemarqueType(request.getParameter("idRemarqueType"));
            // Sauvegarde identifiant en param�tre pour le r�cup�rer par exemple
            // lors de la cr�ation
            session.setAttribute("idRemarqueType", request.getParameter("idRemarqueType"));
            // Dispatcher
            mainController.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        super.actionChercher(session, request, response, mainController);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPCommentaireRemarqueTypeListViewBean vBean = (CPCommentaireRemarqueTypeListViewBean) viewBean;
        // Positionnement selon les crit�res de recherche
        vBean.setForIdRemarqueType((String) session.getAttribute("idRemarqueType"));
        return vBean;
    }

    /*
     * Traitement lors d'une cr�ation avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPCommentaireRemarqueTypeViewBean vBean = (CPCommentaireRemarqueTypeViewBean) viewBean;
        // R�cup�ration de l'id remarque type
        vBean.setIdRemarqueType((String) session.getAttribute("idRemarqueType"));
        return vBean;
    }
}
