/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.naos.db.couverture.AFCouverture;
import globaz.naos.db.couverture.AFCouvertureViewBean;
import globaz.naos.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Couverture.
 * 
 * @author sau
 */
public class AFActionCouverture extends AFDefaultActionChercher {

    public final static String ACTION_CHERCHER_PARAM_ASSURANCE = "chercherParamAssurance";
    public final static String ACTION_CHERCHER_TAUX_ASSURANCE = "chercherTauxAssurance";

    /**
     * Constructeur d'AFActionCouverture
     * 
     * @param servlet
     */
    public AFActionCouverture(FWServlet servlet) {
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

        AFCouvertureViewBean vBean = new AFCouvertureViewBean();
        vBean.setPlanCaisseId(request.getParameter("planCaisseId"));

        actionChercher(vBean, session, request, response, mainDispatcher);
    }

    /**
     * Action utilisée pour le recherche une descision dans Cot. Pers.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionChercherParamAssurance(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        AFCouverture viewBean = new AFCouverture();
        String couvertureId = request.getParameter("couvertureId");
        try {

            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setSession(bSession);
            viewBean.setCouvertureId(request.getParameter("couvertureId"));
            viewBean.retrieve();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = "/naos?userAction=naos.couverture.couverture.afficher&selectedId=" + couvertureId;
        } else {
            _destination = "/naos?userAction=naos.parametreAssurance.parametreAssurance.chercher&assuranceId="
                    + viewBean.getAssuranceId();
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour le recherche une descision dans Cot. Pers.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionChercherTauxAssurance(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        AFCouverture viewBean = new AFCouverture();
        String couvertureId = request.getParameter("couvertureId");
        try {

            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setSession(bSession);
            viewBean.setCouvertureId(request.getParameter("couvertureId"));
            viewBean.retrieve();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = "/naos?userAction=naos.couverture.couverture.afficher&selectedId=" + couvertureId;
        } else {
            _destination = "/naos?userAction=naos.tauxAssurance.tauxAssurance.chercher&assuranceId="
                    + viewBean.getAssuranceId();
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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

        if (ACTION_CHERCHER_TAUX_ASSURANCE.equals(getAction().getActionPart())) {
            actionChercherTauxAssurance(session, request, response);
        } else if (ACTION_CHERCHER_PARAM_ASSURANCE.equals(getAction().getActionPart())) {
            actionChercherParamAssurance(session, request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
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

        AFCouvertureViewBean vBean = (AFCouvertureViewBean) viewBean;
        vBean.setPlanCaisseId(request.getParameter("planCaisseId"));

        return vBean;
    }
}
