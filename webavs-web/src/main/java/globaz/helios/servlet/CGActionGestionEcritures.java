package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la saisie d'écritures.
 * 
 * @author DDA
 * 
 */
public class CGActionGestionEcritures extends CGDefaultServletAction {

    private static final String USER_ACTION_CHERCHER_ECRITURES = "helios.comptes.ecriture.chercher";

    public CGActionGestionEcritures(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=add&_back=sl&_valid=new&idJournal="
                + request.getParameter("idJournal") + "&forceNew=false";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(USER_ACTION_CHERCHER_ECRITURES);
        return action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher&selectedId="
                + request.getParameter("idJournal");
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(USER_ACTION_CHERCHER_ECRITURES);
        return "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher";
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = null;

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                // Modification pour réinitialiser les champs
                // "Libellé du comtpe" après ajout d'une écriture.
                if (request.getParameter("forceNew") != null
                        && !(new Boolean(request.getParameter("forceNew")).booleanValue())) {
                    viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
                } else {
                    viewBean = new CGGestionEcritureViewBean();
                }

                action.changeActionPart(FWAction.ACTION_NOUVEAU);
                ((CGGestionEcritureViewBean) viewBean).setIdJournal(request.getParameter("idJournal"));
            } else {
                viewBean = new CGGestionEcritureViewBean();

                JSPUtils.setBeanProperties(request, viewBean);
            }

            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            String action = request.getParameter("userAction");

            FWViewBeanInterface viewBean = new CGGestionEcritureViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            ((CGGestionEcritureViewBean) viewBean).setEcritures(getEcrituresFromRequest(request, false));
            ((CGGestionEcritureViewBean) viewBean).setShowRows(((CGGestionEcritureViewBean) viewBean).getEcritures()
                    .size());

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(action));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
                goSendRedirectWithoutParameters(destination, request, response);
                return;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            JSPUtils.setBeanProperties(request, viewBean);
            ((CGGestionEcritureViewBean) viewBean).setEcritures(getEcrituresFromRequest(request, true));
            ((CGGestionEcritureViewBean) viewBean).setShowRows(((CGGestionEcritureViewBean) viewBean).getEcritures()
                    .size());

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirectWithoutParameters(destination, request, response);
    }

    private ArrayList getEcrituresFromRequest(HttpServletRequest request, boolean acceptMontantAZero) throws Exception {
        ArrayList ecritures = new ArrayList();

        int countParam = 0;
        while (!JadeStringUtil.isIntegerEmpty(request.getParameter("idc" + countParam))
                || !JadeStringUtil.isBlank(request.getParameter("idext" + countParam))) {
            String idCompte = request.getParameter("idc" + countParam);

            if (validUserMontantEntries(request, countParam) || acceptMontantAZero) {
                CGEcritureViewBean ecriture = new CGEcritureViewBean();

                ecriture.setIdCompte(idCompte);
                ecriture.setIdEcriture(request.getParameter("ide" + countParam));
                ecriture.setIdExterneCompte(request.getParameter("idext" + countParam));
                ecriture.setIdCentreCharge(request.getParameter("idcc" + countParam));
                ecriture.setLibelle(request.getParameter("l" + countParam));
                ecriture.setLibelleCompte(request.getParameter("rubriqueDescription" + countParam));
                ecriture.setMontantEtrangerAffiche(new Boolean(request.getParameter("montantEtrangerAffiche")));
                ecriture.setCentreChargeAffiche(new Boolean(request.getParameter("centreChargeAffiche")));

                if (hasMontantDebit(request, countParam)) {
                    ecriture.setCodeDebitCredit(CodeSystem.CS_DEBIT);
                    ecriture.setMontant(JANumberFormatter.deQuote(request.getParameter("md" + countParam)));
                } else {
                    ecriture.setCodeDebitCredit(CodeSystem.CS_CREDIT);
                    ecriture.setMontant(JANumberFormatter.deQuote(request.getParameter("mc" + countParam)));
                }

                if (hasMontantMonnaieEtrangere(request, countParam)) {
                    ecriture.setMontantMonnaie(JANumberFormatter.deQuote(request.getParameter("me" + countParam)));
                }

                if (hasCours(request, countParam)) {
                    ecriture.setCoursMonnaie(request.getParameter("c" + countParam));
                }

                ecritures.add(ecriture);
            }

            countParam++;
        }

        return ecritures;
    }

    private boolean hasCours(HttpServletRequest request, int countParam) {
        return ((!JadeStringUtil.isIntegerEmpty(JANumberFormatter.deQuote(request.getParameter("c" + countParam)))));
    }

    private boolean hasMontantCredit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("mc" + countParam)));
    }

    private boolean hasMontantDebit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("md" + countParam)));
    }

    private boolean hasMontantMonnaieEtrangere(HttpServletRequest request, int countParam) {
        return ((!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("me" + countParam)))));
    }

    private boolean validUserMontantEntries(HttpServletRequest request, int countParam) {
        if (hasMontantDebit(request, countParam)) {
            return true;
        } else if (hasMontantCredit(request, countParam)) {
            return true;
        } else if (hasMontantMonnaieEtrangere(request, countParam) && hasCours(request, countParam)) {
            return true;
        } else {
            return false;
        }
    }
}
