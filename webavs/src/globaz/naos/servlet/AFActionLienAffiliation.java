/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.affiliation.AFAffiliationListViewBean;
import globaz.naos.db.lienAffiliation.AFLienAffiliationViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité LienAffiliation.
 * 
 * @author sau
 */
public class AFActionLienAffiliation extends AFDefaultActionChercher {

    public final static String ACTION_SELECT_AFFILIE = "selectAffilie";

    public final static String ACTION_SELECT_AFFILIE_LISTER = "selectAffilieLister";
    public final static String ACTION_SELECT_AFFILIE_MODIFIER = "selectAffilieModifier";
    public final static String CLASS_AFFILIE_LIEN_AFFILIATION = "affilieLienAffiliation";

    /**
     * Constructeur d'AFActionLienAffiliation.
     * 
     * @param servlet
     */
    public AFActionLienAffiliation(FWServlet servlet) {
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

        AFLienAffiliationViewBean vBean = new AFLienAffiliationViewBean();
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

        FWAction action = getAction();

        if (action.getClassPart().equalsIgnoreCase(CLASS_AFFILIE_LIEN_AFFILIATION)
                && action.getActionPart().equalsIgnoreCase(ACTION_SELECT_AFFILIE)) {

            actionSelectionAffilieChercher(session, request, response);

        } else if (action.getClassPart().equalsIgnoreCase(CLASS_AFFILIE_LIEN_AFFILIATION)
                && action.getActionPart().equalsIgnoreCase(ACTION_SELECT_AFFILIE_LISTER)) {

            actionSelectionAffilieLister(session, request, response, dispatcher);

        } else if (action.getClassPart().equalsIgnoreCase(CLASS_AFFILIE_LIEN_AFFILIATION)
                && action.getActionPart().equalsIgnoreCase(ACTION_SELECT_AFFILIE_MODIFIER)) {

            actionSelectionAffilieModifier(session, request, response, dispatcher);

        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Action utilisée pour afficher les critères de recherche pour la selection d'Affilié pour la Lien.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void actionSelectionAffilieChercher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

        String lienAffiliationId = request.getParameter("selectedId");
        String affiliationId = request.getParameter("affiliationId");
        String typeLien = request.getParameter("typeLien");
        String dateDebut = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");

        String _destination = getRelativeURLwithoutClassPart(request, session)
                + "affilieLienAffiliation_rc.jsp?lienAffiliationId=" + lienAffiliationId + "&affiliationId="
                + affiliationId + "&typeLien=" + typeLien + "&dateDebut=" + dateDebut + "&dateFin=" + dateFin;

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour lister les affilies. pour le lien.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void actionSelectionAffilieLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination;

        String lienAffiliationId = request.getParameter("lienAffiliationId");
        String affiliationId = request.getParameter("affiliationId");
        String typeLien = request.getParameter("typeLien");
        String dateDebut = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");

        AFAffiliationListViewBean listViewBean = new AFAffiliationListViewBean();

        FWAction _action = FWAction.newInstance("naos.affiliation.affiliation.lister");
        try {
            JSPUtils.setBeanProperties(request, listViewBean);
            listViewBean = (AFAffiliationListViewBean) mainDispatcher.dispatch(listViewBean, _action);
        } catch (Exception e) {
            listViewBean.setMsgType(FWViewBeanInterface.ERROR);
            listViewBean.setMessage(e.getMessage());
        }

        request.setAttribute("viewBean", listViewBean);
        session.removeAttribute("listViewBean");
        session.setAttribute("listViewBean", listViewBean);

        if (listViewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "affilieLienAffiliation_rcListe.jsp?lienAffiliationId=" + lienAffiliationId + "&affiliationId="
                    + affiliationId + "&typeLien=" + typeLien + "&dateDebut=" + dateDebut + "&dateFin=" + dateFin;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour modifier l'affilie du lien.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void actionSelectionAffilieModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination;

        String selectedId = request.getParameter("lienAffiliationId");
        String affiliationId = request.getParameter("affiliationId");
        String aff_AffiliationId = request.getParameter("aff_AffiliationId");
        String typeLien = request.getParameter("typeLien");
        String dateDebut = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");

        AFLienAffiliationViewBean viewBean = new AFLienAffiliationViewBean();
        viewBean.setLienAffiliationId(selectedId);

        FWAction _action = FWAction.newInstance("naos.lienAffiliation.lienAffiliation.afficher");
        try {
            viewBean = (AFLienAffiliationViewBean) mainDispatcher.dispatch(viewBean, _action);
            viewBean.setAff_AffiliationId(aff_AffiliationId);
            viewBean.setAffiliationId(affiliationId);
            viewBean.setTypeLien(typeLien);
            viewBean.setDateDebut(dateDebut);
            viewBean.setDateFin(dateFin);
            if (!viewBean.isNew()) {
                viewBean.update();
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.isNew()) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "lienAffiliation_de.jsp?_method=add";
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + "lienAffiliation_de.jsp?selectedId="
                    + selectedId;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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

        AFLienAffiliationViewBean vBean = (AFLienAffiliationViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        return vBean;
    }
}
