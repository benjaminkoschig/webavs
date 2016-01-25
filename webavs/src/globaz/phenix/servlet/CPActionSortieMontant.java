/*
 * Created on 19 déc. 05
 */
package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPSortieMontantListViewBean;
import globaz.phenix.db.principale.CPSortieMontantViewBean;

/**
 * @author mar
 */
public class CPActionSortieMontant extends FWDefaultServletAction {

    public CPActionSortieMontant(FWServlet servlet) {
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
        CPSortieMontantViewBean viewBean = new CPSortieMontantViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdSortie(request.getParameter("idSortie"));
            // Sauvegarde identifiant en paramètre pour le récupérer par exemple
            // lors de la création
            session.setAttribute("idSortie", request.getParameter("idSortie"));
            // chargement de l'entête dans le helper
            // viewBean._chargerEntete();
            session.setAttribute("viewBean", viewBean);
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
        CPSortieMontantListViewBean vBean = (CPSortieMontantListViewBean) viewBean;
        // Positionnement selon les critères de recherche
        vBean.setForIdSortie((String) session.getAttribute("idSortie"));
        return vBean;
    }

    /*
     * Traitement lors d'une création avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPSortieMontantViewBean vBean = (CPSortieMontantViewBean) viewBean;
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            vBean.setSession((globaz.globall.db.BSession) bSession);
            // Récupération de l'id décision
            vBean.setIdSortie((String) session.getAttribute("idSortie"));
            // chargement entete dans helper méthode _init
            // vBean._chargerEntete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vBean;
    }

}
