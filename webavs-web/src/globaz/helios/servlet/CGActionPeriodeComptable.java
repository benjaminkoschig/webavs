package globaz.helios.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGPeriodeComptableBouclementViewBean;
import globaz.helios.db.comptes.CGPeriodeComptableEnvoyerAnnoncesViewBean;
import globaz.helios.db.comptes.CGPeriodeComptableImportJournalDebitViewBean;
import globaz.helios.db.comptes.CGPeriodeComptableImprimerReleveAVSViewBean;
import globaz.helios.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des périodes comptables.
 * 
 * @author DDA
 * 
 */
public class CGActionPeriodeComptable extends CGActionNeedExerciceComptable {

    public static final String PERIODE_COMPTABLE_PREFIX = "periodeComptable";

    public CGActionPeriodeComptable(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    private void actionAfficherBouclerPeriode(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;

        try {
            CGPeriodeComptableBouclementViewBean viewBean = new CGPeriodeComptableBouclementViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdPeriodeComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "Bouclement_de.jsp";
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherEnvoyerAnnonces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;

        try {
            CGPeriodeComptableEnvoyerAnnoncesViewBean viewBean = new CGPeriodeComptableEnvoyerAnnoncesViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdPeriodeComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "EnvoyerAnnonces_de.jsp";
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionAfficherEnvoyerAnnoncesOFAS(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;

        try {
            CGPeriodeComptableEnvoyerAnnoncesViewBean viewBean = new CGPeriodeComptableEnvoyerAnnoncesViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdPeriodeComptable(request.getParameter("selectedId"));

            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "EnvoyerAnnoncesOFAS_de.jsp";
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws IOException
     * @throws ServletException
     */
    private void actionAfficherImporterJournalDebit(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = null;

        try {
            CGPeriodeComptableImportJournalDebitViewBean viewBean = new CGPeriodeComptableImportJournalDebitViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdPeriodeComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "ImportJournalDebit_de.jsp";
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionAfficherImprimerReleveAVS(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;

        try {
            CGPeriodeComptableImprimerReleveAVSViewBean viewBean = new CGPeriodeComptableImprimerReleveAVSViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setIdPeriodeComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "ImprimerReleveAVS_de.jsp";
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("boucler".equals(getAction().getActionPart())) {
            actionAfficherBouclerPeriode(session, request, response, mainDispatcher);
        } else if ("imprimerReleveAVS".equals(getAction().getActionPart())) {
            actionAfficherImprimerReleveAVS(session, request, response, mainDispatcher);
        } else if ("envoyerAnnonces".equals(getAction().getActionPart())) {
            actionAfficherEnvoyerAnnonces(session, request, response, mainDispatcher);
        } else if ("envoyerAnnoncesOFAS".equals(getAction().getActionPart())) {
            actionAfficherEnvoyerAnnoncesOFAS(session, request, response, mainDispatcher);
        } else if ("importerJournalDebit".equals(getAction().getActionPart())) {
            actionAfficherImporterJournalDebit(session, request, response, mainDispatcher);
        }

    }
}
