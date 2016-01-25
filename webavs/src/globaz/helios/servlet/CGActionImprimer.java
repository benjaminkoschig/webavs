package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.print.CGImprimerAnalyseBudgetaireViewBean;
import globaz.helios.db.print.CGImprimerBalMvtJournalViewBean;
import globaz.helios.db.print.CGImprimerBalanceComptesViewBean;
import globaz.helios.db.print.CGImprimerBilanViewBean;
import globaz.helios.db.print.CGImprimerGrandLivreViewBean;
import globaz.helios.db.print.CGImprimerListeEcrituresViewBean;
import globaz.helios.db.print.CGImprimerPertesProfitsViewBean;
import globaz.helios.db.print.CGImprimerPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des écrans d'impression.
 * 
 * @author DDA
 * 
 */
public class CGActionImprimer extends CGDefaultServletAction {

    private static final String IMPRIMER_RELEVE_AVS = "imprimerReleveAVS";

    public CGActionImprimer(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    private void actionAfficherImprimerAnalyseBudgetaire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerAnalyseBudgetaireViewBean viewBean = new CGImprimerAnalyseBudgetaireViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setInclurePeriodePrecedente(new Boolean(true));

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "AnalyseBudgetaire_de.jsp";

        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerBalanceComptes(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerBalanceComptesViewBean viewBean = new CGImprimerBalanceComptesViewBean();
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setInclurePeriodePrecedente(new Boolean(true));

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "BalanceComptes_de.jsp";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerBalMvtJournal(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerBalMvtJournalViewBean viewBean = new CGImprimerBalMvtJournalViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "BalMvtJournal_de.jsp";

        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerBilan(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerBilanViewBean viewBean = new CGImprimerBilanViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "Bilan_de.jsp";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerGrandLivre(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerGrandLivreViewBean viewBean = new CGImprimerGrandLivreViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "GrandLivre_de.jsp";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerListeEcritures(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerListeEcrituresViewBean viewBean = new CGImprimerListeEcrituresViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "ListeEcritures_de.jsp";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerPertesProfits(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerPertesProfitsViewBean viewBean = new CGImprimerPertesProfitsViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setInclurePeriodePrecedente(new Boolean(true));

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "PertesProfits_de.jsp";

        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimerPlanComptable(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGImprimerPlanComptableViewBean viewBean = new CGImprimerPlanComptableViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setIdExerciceComptable(request.getParameter("selectedId"));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "PlanComptable_de.jsp";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("balanceComptes".equals(getAction().getActionPart())) {
            actionAfficherImprimerBalanceComptes(session, request, response, mainDispatcher);
        } else if ("analyseBudgetaire".equals(getAction().getActionPart())) {
            actionAfficherImprimerAnalyseBudgetaire(session, request, response, mainDispatcher);
        } else if ("pertesProfits".equals(getAction().getActionPart())) {
            actionAfficherImprimerPertesProfits(session, request, response, mainDispatcher);
        } else if ("bilan".equals(getAction().getActionPart())) {
            actionAfficherImprimerBilan(session, request, response, mainDispatcher);
        } else if ("planComptable".equals(getAction().getActionPart())) {
            actionAfficherImprimerPlanComptable(session, request, response, mainDispatcher);
        } else if ("grandLivre".equals(getAction().getActionPart())) {
            actionAfficherImprimerGrandLivre(session, request, response, mainDispatcher);
        } else if ("balMvtJournal".equals(getAction().getActionPart())) {
            actionAfficherImprimerBalMvtJournal(session, request, response, mainDispatcher);
        } else if ("listeEcritures".equals(getAction().getActionPart())) {
            actionAfficherImprimerListeEcritures(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination;

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            JSPUtils.setBeanProperties(request, viewBean);

            ((BProcess) viewBean).setControleTransaction(true);
            ((BProcess) viewBean).setSendCompletionMail(true);
            ((BProcess) viewBean).setSendMailOnError(true);

            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));
            request.setAttribute(VIEWBEAN, viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirectWithoutParameters(destination, request, response);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String relativeUrl = getRelativeURL(request, session);
        StringBuffer tmp = new StringBuffer(relativeUrl.substring(relativeUrl.lastIndexOf("/") + 1,
                relativeUrl.length()));

        String myDestination = relativeUrl.substring(0, relativeUrl.lastIndexOf("/")) + "/";

        if (relativeUrl.indexOf("/print/") == -1) {
            if (relativeUrl.indexOf(IMPRIMER_RELEVE_AVS) > -1) {
                myDestination += CGActionPeriodeComptable.PERIODE_COMPTABLE_PREFIX;
            } else {
                myDestination += CGActionExerciceComptable.EXERCICE_COMPTABLE_PREFIX;
            }
            tmp.setCharAt(0, Character.toUpperCase(tmp.charAt(0)));
        }

        myDestination += (tmp.toString() + "_de.jsp");

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            viewBean.setISession(mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, VIEWBEAN, viewBean);
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }
}
