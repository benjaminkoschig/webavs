package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPDonneesCalculListViewBean;
import globaz.phenix.db.principale.CPDonneesCalculViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
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
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
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
            // Sauvegarde identifiant en paramètre pour le récupérer par exemple
            // lors de la création
            session.setAttribute("idDecision", request.getParameter("idDecision"));
            // transféré dans le helper
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
        // Positionnement selon les critères de recherche
        vBean.setIdDecision((String) session.getAttribute("idDecision"));
        vBean.setForIdDecision((String) session.getAttribute("idDecision"));
        return vBean;
    }

    /*
     * Traitement lors d'une création avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPDonneesCalculViewBean vBean = (CPDonneesCalculViewBean) viewBean;
        // Récupération de l'id décision
        vBean.setIdDecision((String) session.getAttribute("idDecision"));
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // transféré dans le helper
            // vBean._chargerEntete();
            return vBean;
        } catch (Exception e) {
            e.printStackTrace();
            return vBean;
        }
    }
}
