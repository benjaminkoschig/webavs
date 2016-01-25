package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.principale.CPSortieListViewBean;
import globaz.phenix.db.principale.CPSortieViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */
public class CPActionSortie extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionSortie(FWServlet servlet) {
        super(servlet);
    }

    // /**
    // * Insérez la description de la méthode ici.
    // * Date de création : (03.05.2002 08:57:00)
    // */
    // public void actionAfficher(javax.servlet.http.HttpSession session,
    // javax.servlet.http.HttpServletRequest request,
    // javax.servlet.http.HttpServletResponse response,
    // globaz.framework.controller.FWDispatcher mainController) throws
    // javax.servlet.ServletException, java.io.IOException {
    // // --- Get value from request
    // CPSortieViewBean viewBean = new CPSortieViewBean();
    // try {
    // globaz.globall.api.BISession bSession =
    // globaz.phenix.translation.CodeSystem.getSession(session);
    // viewBean.setSession((globaz.globall.db.BSession) bSession);
    // viewBean.setIdTiers((String) session.getAttribute("idTiers"));
    // if ("add".equalsIgnoreCase(request.getParameter("_method"))) {
    // viewBean.setIdAffiliation((String)
    // session.getAttribute("idAffiliation"));
    // // viewBean._initEcran();
    // } else {
    // viewBean.setIdSortie((String) request.getParameter("selectedId"));
    // viewBean.retrieve();
    // FWHelper.afterExecute(viewBean);
    // }
    // } catch (Exception e) {
    // viewBean.setMessage(e.getMessage());
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // }
    // // --- Check view bean
    // session.setAttribute("viewBean", viewBean);
    // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true)
    // servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request,
    // response);
    // else
    // servlet.getServletContext().getRequestDispatcher(getRelativeURLwithoutClassPart(request,
    // session) + "sortie_de.jsp").forward(request, response);
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        CPSortieViewBean viewBean = new CPSortieViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        super.actionChercher(session, request, response, mainController);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        CPSortieListViewBean vBean = (CPSortieListViewBean) viewBean;
        // vBean.setForIdPassage((String) request.getParameter("idPassage"));
        vBean.orderByIdPassageDESC();
        vBean.orderByNoAffilie();
        vBean.orderByAnneeDESC();
        return vBean;
    }

}