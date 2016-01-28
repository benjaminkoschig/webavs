package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGAnalyseBudgetaireViewBean;
import globaz.jade.log.JadeLogger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la saisie des données d'analyse budgétaire.
 * 
 * @author DDA
 * 
 */
public class CGActionAnalyseBudgetaire extends CGDefaultServletAction {

    public static final String ACTION_AFFICHER_ANALYSE_BUDGETAIRE = "afficherAnalyseBudgetaire";
    public static final String ACTION_MODIFIER_ANALYSE_BUDGETAIRE = "modifierAnalyseBudgetaire";
    public static final String ACTION_SUPPRIMER_ANALYSE_BUDGETAIRE = "supprimerAnalyseBudgetaire";

    public CGActionAnalyseBudgetaire(FWServlet servlet) {
        super(servlet);
    }

    protected void actionAfficherAnalyseBudgetaire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            String selectedId = request.getParameter("selectedId");

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            ((CGAnalyseBudgetaireViewBean) viewBean).setIdCompte(selectedId);

            JSPUtils.setBeanProperties(request, viewBean);

            if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (ACTION_MODIFIER_ANALYSE_BUDGETAIRE.equals(getAction().getActionPart())) {
            actionModifierAnalyseBudgetaire(session, request, response, mainDispatcher);
        } else if (ACTION_AFFICHER_ANALYSE_BUDGETAIRE.equals(getAction().getActionPart())) {
            actionAfficherAnalyseBudgetaire(session, request, response, mainDispatcher);
        } else if (ACTION_SUPPRIMER_ANALYSE_BUDGETAIRE.equals(getAction().getActionPart())) {
            actionSupprimerAnalyseBudgetaire(session, request, response, mainDispatcher);
        }
    }

    protected void actionModifierAnalyseBudgetaire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";

        try {
            CGAnalyseBudgetaireViewBean viewBean = (CGAnalyseBudgetaireViewBean) session.getAttribute(VIEWBEAN);

            for (int i = 0; i < viewBean.getPeriodeSize(); i++) {
                String element = "idSolde_" + viewBean.geIdSoldePeriode(i);
                String budgetPeriode = request.getParameter(element);
                FWCurrency budget = new FWCurrency(budgetPeriode);
                viewBean.setMontantBudgetePeriode(i, budget.toString());
            }

            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            viewBean = (CGAnalyseBudgetaireViewBean) beforeModifier(session, request, response, viewBean);
            viewBean = (CGAnalyseBudgetaireViewBean) mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                destination = "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                        + action.getPackagePart() + ".planComptable.chercher";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    protected void actionSupprimerAnalyseBudgetaire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";

        try {
            CGAnalyseBudgetaireViewBean viewBean = (CGAnalyseBudgetaireViewBean) session.getAttribute(VIEWBEAN);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (CGAnalyseBudgetaireViewBean) beforeSupprimer(session, request, response, viewBean);
            viewBean = (CGAnalyseBudgetaireViewBean) mainDispatcher.dispatch(viewBean, action);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                destination = "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                        + action.getPackagePart() + ".planComptable.chercher";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
