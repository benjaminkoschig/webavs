package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.modeles.CGGestionModeleViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la saisie d'écritures dans un modèle.
 * 
 * @author DDA
 * 
 */
public class CGActionGestionModeles extends CGDefaultServletAction {

    private static final String USER_ACTION_CHERCHER_MODELES = "helios.modeles.ligneModeleEcriture.chercher";

    public CGActionGestionModeles(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=add&_back=sl&_valid=new";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(USER_ACTION_CHERCHER_MODELES);
        return "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher&selectedId="
                + ((CGGestionModeleViewBean) viewBean).getIdModeleEcriture();
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(USER_ACTION_CHERCHER_MODELES);
        return "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher&selectedId="
                + ((CGGestionModeleViewBean) viewBean).getIdModeleEcriture();
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = new CGGestionModeleViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

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

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);

            JSPUtils.setBeanProperties(request, viewBean);
            ((CGGestionModeleViewBean) viewBean).setLignes(getLignesFromRequest(request, false));
            ((CGGestionModeleViewBean) viewBean).setShowRows(((CGGestionModeleViewBean) viewBean).getLignes().size());

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(action));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            JSPUtils.setBeanProperties(request, viewBean);
            ((CGGestionModeleViewBean) viewBean).setLignes(getLignesFromRequest(request, true));
            ((CGGestionModeleViewBean) viewBean).setShowRows(((CGGestionModeleViewBean) viewBean).getLignes().size());

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute(VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    private ArrayList getLignesFromRequest(HttpServletRequest request, boolean acceptMontantAZero) throws Exception {
        ArrayList ecritures = new ArrayList();

        int countParam = 0;
        while (!JadeStringUtil.isIntegerEmpty(request.getParameter("idc" + countParam))) {
            String idCompte = request.getParameter("idc" + countParam);

            CGLigneModeleEcriture ecriture = new CGLigneModeleEcriture();

            ecriture.setIdCompte(idCompte);
            ecriture.setIdExterneCompte(request.getParameter("idext" + countParam));
            ecriture.setIdLigneModeleEcriture(request.getParameter("ide" + countParam));
            ecriture.setIdCentreCharge(request.getParameter("idcc" + countParam));
            ecriture.setLibelle(request.getParameter("l" + countParam));

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

            countParam++;
        }

        return ecritures;
    }

    private boolean hasCours(HttpServletRequest request, int countParam) {
        return ((!JadeStringUtil.isIntegerEmpty(JANumberFormatter.deQuote(request.getParameter("c" + countParam)))));
    }

    private boolean hasMontantDebit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isBlank(JANumberFormatter.deQuote(request.getParameter("md" + countParam)));
    }

    private boolean hasMontantMonnaieEtrangere(HttpServletRequest request, int countParam) {
        return ((!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("me" + countParam)))));
    }
}
