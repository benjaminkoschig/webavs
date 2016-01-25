package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.communications.CPCommentaireCommunicationListViewBean;
import globaz.phenix.db.communications.CPCommentaireCommunicationViewBean;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionCommentaireCommunication extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionCommentaireCommunication(FWServlet servlet) {
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
        CPCommentaireCommunicationViewBean viewBean = new CPCommentaireCommunicationViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdCommunicationRetour(request.getParameter("idRetour"));
            // Sauvegarde identifiant en param�tre pour le r�cup�rer par exemple
            // lors de la cr�ation
            session.setAttribute("idRetour", request.getParameter("idRetour"));
            viewBean._chargerEntete();
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        } else {
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                    .forward(request, response);
        }
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPCommentaireCommunicationListViewBean vBean = (CPCommentaireCommunicationListViewBean) viewBean;
        // Positionnement selon les crit�res de recherche
        vBean.setForIdCommunicationRetour((String) session.getAttribute("idRetour"));
        return vBean;
    }

    /*
     * Traitement lors d'une cr�ation avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPCommentaireCommunicationViewBean vBean = (CPCommentaireCommunicationViewBean) viewBean;
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // R�cup�ration de l'id d�cision
            vBean.setIdCommunicationRetour((String) session.getAttribute("idRetour"));
            // vBean._chargerEntete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vBean;
    }
}
