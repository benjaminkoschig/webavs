package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPCotisationListViewBean;
import globaz.phenix.db.principale.CPCotisationViewBean;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionCotisation extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionCotisation(FWServlet servlet) {
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
        CPCotisationViewBean viewBean = new CPCotisationViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdDecision(request.getParameter("idDecision"));
            // Sauvegarde identifiant en param�tre pour le r�cup�rer par exemple
            // lors de la cr�ation
            session.setAttribute("idDecision", request.getParameter("idDecision"));
            session.setAttribute("viewBean", viewBean);
            // viewBean._chargerEntete() transf�r� dans le helper
            mainController.dispatch(viewBean, getAction());
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
        CPCotisationListViewBean vBean = (CPCotisationListViewBean) viewBean;
        // Positionnement selon les crit�res de recherche
        vBean.setForIdDecision((String) session.getAttribute("idDecision"));
        return vBean;
    }

    /*
     * Traitement lors d'une cr�ation avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPCotisationViewBean vBean = (CPCotisationViewBean) viewBean;
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // R�cup�ration de l'id d�cision
            vBean.setIdDecision((String) session.getAttribute("idDecision"));
            // execut� dans le helper m�thode _init surcharg�e
            // vBean._chargerEntete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vBean;
    }
}
