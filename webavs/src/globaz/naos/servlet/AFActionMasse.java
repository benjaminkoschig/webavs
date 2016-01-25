/*
 * Created on 13-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.cotisation.AFCotisationListViewBean;
import globaz.naos.db.masse.AFMasseViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Masse.
 * 
 * @author sau
 */
public class AFActionMasse extends AFDefaultActionChercher {

    public final static String ACTION_SELECT_COTISATION = "selectCotisation";

    public final static String ACTION_SELECT_COTISATION_LISTER = "selectCotisationLister";
    public final static String ACTION_SELECT_COTISATION_MODIFIER = "selectCotisationModifier";
    public final static String CLASS_COTISATION_MASSE = "cotisationMasse";

    /**
     * Constructeur d'AFActionMasse.
     * 
     * @param servlet
     */
    public AFActionMasse(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFMasseViewBean vBean = new AFMasseViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        actionChercher(vBean, session, request, response, mainDispatcher);
    }

    /**
     * Traitement des actions non standard.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));

        if (_action.getClassPart().equalsIgnoreCase(CLASS_COTISATION_MASSE)
                && _action.getActionPart().equalsIgnoreCase(ACTION_SELECT_COTISATION)) {

            actionSelectionCotisation(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Selection de la Cotisation pour la Masse.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void actionSelectionCotisation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination;

        AFMasseViewBean viewBean = (AFMasseViewBean) session.getAttribute("viewBean");

        AFCotisationListViewBean listViewBean = new AFCotisationListViewBean();
        listViewBean.setForAffiliationId(viewBean.getAffiliation().getAffiliationId());

        FWAction action = FWAction.newInstance("naos.cotisation.cotisation.lister");
        try {
            listViewBean = (AFCotisationListViewBean) mainDispatcher.dispatch(listViewBean, action);
        } catch (Exception e) {
            listViewBean.setMsgType(FWViewBeanInterface.ERROR);
            listViewBean.setMessage(e.getMessage());
        }

        if (listViewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        }

        request.setAttribute("viewBean", listViewBean);
        session.removeAttribute("listViewBean");
        session.setAttribute("listViewBean", listViewBean);

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Effectue des traitements avant recupération de l'entité dans la DB, pour l'afficher.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFMasseViewBean vBean = (AFMasseViewBean) viewBean;
        return vBean;
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFMasseViewBean vBean = (AFMasseViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));
        if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) {
            vBean.setCotisationId(request.getParameter("cotisationId"));
        }

        return vBean;
    }
}
