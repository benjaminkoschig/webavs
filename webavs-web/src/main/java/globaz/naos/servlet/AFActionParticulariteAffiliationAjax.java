package globaz.naos.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationAjaxViewBean;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.translation.CodeSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Classe permettant la gestion des actions ajax pour l'entité ParticulariteAffiliation.
 *
 * @author ebko
 */
public class AFActionParticulariteAffiliationAjax extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionParticulariteAffiliationAjax.
     *
     * @param servlet
     */
    public AFActionParticulariteAffiliationAjax(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                      FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFParticulariteAffiliation viewBean = new AFParticulariteAffiliation();
        try {
            BSession bSession = (BSession) CodeSystem.getSession(session);

            viewBean.setId(request.getParameter("id"));
            viewBean.setSession(bSession);
            viewBean.retrieve();
            viewBean.setChampAlphanumerique(JSPUtils.getParameter(request, "champAlphanumerique"));
            viewBean.setDateDebut(JSPUtils.getParameter(request, "dateDebut"));
            viewBean.setDateFin(JSPUtils.getParameter(request, "dateFin"));
            viewBean = (AFParticulariteAffiliation) beforeModifier(session, request, response, viewBean);
            mainDispatcher.dispatch(viewBean, getAction());

            retrieveViewBeanAndForward(session, request, response, mainDispatcher, bSession);
        } catch (Exception e) {
            forwardException(request, response, e);
        }
    }

    @Override
    protected void actionSupprimerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {

            AFParticulariteAffiliation viewBean = new AFParticulariteAffiliation();
            BSession bSession = (BSession) CodeSystem.getSession(session);

            viewBean.setId(request.getParameter("id"));
            viewBean.setSession(bSession);
            viewBean.retrieve();
            viewBean.delete();
            retrieveViewBeanAndForward(session, request, response, mainDispatcher, bSession);
        } catch (Exception e) {
            forwardException(request, response, e);
        }
    }

    private void retrieveViewBeanAndForward(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher, BSession bSession) throws Exception {
        AFParticulariteAffiliationAjaxViewBean resultViewBean = (AFParticulariteAffiliationAjaxViewBean) this.getAJAXViewBean(mainDispatcher, request.getParameter("id"));
        resultViewBean.setSession(bSession);
        resultViewBean.retrieve();
        request.setAttribute(FWServlet.VIEWBEAN, resultViewBean);
        this.servlet.getServletContext().getRequestDispatcher( this.getAJAXAfficherSuccessDestination(session, request)).forward(request, response);
    }

    private void forwardException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        request.setAttribute("exception", e);
        this.servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_AJAX_PAGE).forward(request, response);
    }

}
