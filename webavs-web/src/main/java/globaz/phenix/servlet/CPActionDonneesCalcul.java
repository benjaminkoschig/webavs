package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPDonneesCalculListViewBean;
import globaz.phenix.db.principale.CPDonneesCalculViewBean;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionDonneesCalcul extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionDonneesCalcul(FWServlet servlet) {
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
        CPDonneesCalculViewBean viewBean = new CPDonneesCalculViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdDecision(request.getParameter("idDecision"));
            // Sauvegarde identifiant en param�tre pour le r�cup�rer par exemple
            // lors de la cr�ation
            session.setAttribute("idDecision", request.getParameter("idDecision"));
            // transf�r� dans le helper
            // viewBean._chargerEntete();
            mainController.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        super.actionChercher(session, request, response, mainController);
    }

    /*
     * Traitement avant l'action afficher
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPDonneesCalculViewBean vBean = (CPDonneesCalculViewBean) viewBean;
        vBean.setIdDecision((String) session.getAttribute("idDecision"));
        vBean.setIdDonneesCalcul(request.getParameter("selectedId"));
        return vBean;
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPDonneesCalculListViewBean vBean = (CPDonneesCalculListViewBean) viewBean;
        // Positionnement selon les crit�res de recherche
        vBean.setIdDecision((String) session.getAttribute("idDecision"));
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
        CPDonneesCalculViewBean vBean = (CPDonneesCalculViewBean) viewBean;
        // R�cup�ration de l'id d�cision
        vBean.setIdDecision((String) session.getAttribute("idDecision"));
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // transf�r� dans le helper
            // vBean._chargerEntete();
            return vBean;
        } catch (Exception e) {
            e.printStackTrace();
            return vBean;
        }
    }
}
