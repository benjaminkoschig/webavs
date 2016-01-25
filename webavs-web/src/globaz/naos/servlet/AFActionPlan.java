/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.assurance.AFAssuranceListViewBean;
import globaz.naos.db.plan.AFPlanListViewBean;
import globaz.naos.db.plan.AFPlanSelectViewBean;
import globaz.naos.db.plan.AFPlanViewBean;
import globaz.naos.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Plan (d'Assurance).
 * 
 * @author sau
 */
public class AFActionPlan extends AFDefaultActionChercher {

    public final static String ACTION_SELECTION_PLAN_ASSURANCE = "selectionPlanAssurance";

    /**
     * Constructeur AFActionPlan.
     * 
     * @param servlet
     */
    public AFActionPlan(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Destination après un Echec lors d'un ajout dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestEchec(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        return getActionFullURL() + ".chercher&selectedId=" + request.getParameter("selectedId");
    }

    /**
     * Action utilisée pour ajouter une entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination;

        AFPlanViewBean viewBean = (AFPlanViewBean) session.getAttribute("viewBean");

        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean._ajouterPlan(); // viewBean.getSelectionAssurance(),viewBean.getPlanLibelle());

            if (viewBean.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(viewBean.getErrors().toString());
            } else {
                viewBean.setMsgType(FWViewBeanInterface.OK);
                viewBean.setMessage("Data writed");
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("ERROR Unable to write data");
        }

        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = _getDestAjouterEchec(session, request, response, viewBean);
        } else {
            _destination = _getDestAjouterSucces(session, request, response, viewBean);
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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

        String destination = "";

        AFPlanViewBean vBean = new AFPlanViewBean();

        try {
            vBean = (AFPlanViewBean) mainDispatcher.dispatch(vBean, getAction());
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", vBean);

        // Package Plan
        if (!JadeStringUtil.isEmpty(request.getParameter("selectionPlan"))
                && !JadeStringUtil.isEmpty(request.getParameter("affiliationId"))) {

            destination = getRelativeURL(request, session) + "Select_rc.jsp?affiliationId="
                    + request.getParameter("affiliationId") + "&selectionPlan=1";
        } else {

            String selectedId = request.getParameter("selectedId");
            // destination = getRelativeURL(request, session) + "_rc.jsp";

            // if not selected ID, select the first one
            if (JadeStringUtil.isEmpty(selectedId)) {
                AFPlanListViewBean planlistViewBean = new AFPlanListViewBean();
                try {
                    BISession bSession = CodeSystem.getSession(session);
                    planlistViewBean.setSession((BSession) bSession);
                    planlistViewBean.find();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (planlistViewBean.size() > 0) {
                    selectedId = planlistViewBean.getPlanId(0);
                }
            }
            destination = getRelativeURL(request, session) + "_rc.jsp?selectedId=" + selectedId;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
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

        if (ACTION_SELECTION_PLAN_ASSURANCE.equals(getAction().getActionPart())) {
            selectionPlanAssurance(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Effectue des traitements avant la recherche d'entités.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        try {
            AFPlanListViewBean listViewBean = new AFPlanListViewBean();

            listViewBean = (AFPlanListViewBean) mainDispatcher.dispatch(listViewBean, getAction());
            request.setAttribute("viewBean", listViewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", listViewBean);

            destination = getRelativeURL(request, session);

            if (listViewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = ERROR_PAGE;
            } else if (!JadeStringUtil.isEmpty(request.getParameter("selectionPlan"))
                    && !JadeStringUtil.isEmpty(request.getParameter("affiliationId"))) {
                destination += "Select_rcListe.jsp" + "?affiliationId=" + request.getParameter("affiliationId")
                        + "&selectionPlan=1";
            } else {
                destination += "_rcListe.jsp";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action utilisée pour modifier une entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination;

        AFPlanViewBean vBean = (AFPlanViewBean) session.getAttribute("viewBean");

        try {
            JSPUtils.setBeanProperties(request, vBean);
            vBean._modifierSelectionAssurance(); // vBean.getSelectionAssurance(),
            // vBean.getPlanId());

            if (vBean.hasErrors()) {
                vBean.setMsgType(FWViewBeanInterface.ERROR);
                vBean.setMessage(vBean.getErrors().toString());
            } else {
                vBean.setMsgType(FWViewBeanInterface.OK);
                vBean.setMessage("Data updated");
            }
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage("ERROR Unable to update data");
        }
        session.setAttribute("viewBean", vBean);

        if (vBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = _getDestEchec(session, request, response, vBean);
        } else {
            _destination = _getDestModifierSucces(session, request, response, vBean);
        }
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

        AFAssuranceListViewBean listViewBean = new AFAssuranceListViewBean();
        try {
            BISession bSession = CodeSystem.getSession(session);
            listViewBean.setSession((BSession) bSession);
            listViewBean.find();
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("ERROR Unable to read data");
        }
        request.setAttribute("assuranceListViewBean", listViewBean);

        return viewBean;
    }

    /**
     * Sélection du plan d'assurance a ajouter pour l'affiliation séléctionnée.
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void selectionPlanAssurance(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = "";
        AFPlanSelectViewBean viewBean = new AFPlanSelectViewBean();

        viewBean.setAffiliationId(request.getParameter("affiliationId"));
        viewBean.setISession(dispatcher.getSession());
        request.setAttribute("viewBean", viewBean);

        destination = getRelativeURL(request, session) + "Select_rc.jsp?affiliationId="
                + request.getParameter("affiliationId") + "&selectionPlan=1";

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
