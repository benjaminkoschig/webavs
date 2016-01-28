/*
 * Créé le 30 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.servlet;

import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DSActionInscriptions extends FWDefaultServletAction {
    public static void main(String[] args) {
    }

    /**
     * @param servlet
     */
    public DSActionInscriptions(FWServlet servlet) {
        super(servlet);
        // TODO Raccord de constructeur auto-généré
    }

    protected void _actionAjouter15(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).createLeer();
            request.removeAttribute("viewBean");
            request.setAttribute("viewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionFindNext(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).findNext();
            request.removeAttribute("viewBean");
            request.setAttribute("viewBean", viewBean);
            if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionFindPrevious(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).findPrev();
            request.removeAttribute("viewBean");
            request.setAttribute("viewBean", viewBean);
            if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionReAfficherPerso(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String processLaunchedStr = request.getParameter("process");
        boolean processSeemsOk = "launched".equals(processLaunchedStr);
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                .forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Raccord de méthode auto-généré
        return getActionFullURL() + ".reAfficherPerso";
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("suivantPerso")) {
            // suivant pour les journaux
            _actionFindNext(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("precedantPerso")) {
            // précedent pour les journaux
            _actionFindPrevious(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("ajouter15")) {
            _actionAjouter15(session, request, response, dispatcher);

        } else if (getAction().getActionPart().equals("reAfficherPerso")) {
            _actionReAfficherPerso(session, request, response, dispatcher);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Raccord de méthode auto-généré
        String noAvs = request.getParameter("fromNumeroAvs");
        if (noAvs != null) {
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).setFromNumeroAvs(noAvs);
        }
        String aTraiter = request.getParameter("aTraiterStr");
        if (aTraiter != null) {
            if ("True".equals(aTraiter)) {
                ((DSInscriptionsIndividuellesListeViewBean) viewBean).setATraiter(new Boolean(true));
            } else {
                ((DSInscriptionsIndividuellesListeViewBean) viewBean).setATraiter(new Boolean(false));
            }
        }
        String tri = request.getParameter("tri");

        if (!JadeStringUtil.isBlank(tri)) {
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).setTri(tri);
        }
        String fromNomPrenom = request.getParameter("fromNomPrenom");
        if (!JadeStringUtil.isBlankOrZero(fromNomPrenom)) {
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).setFromNomPrenom(fromNomPrenom);
        }
        String forMontantSigne = request.getParameter("forMontantSigne");
        if (!JadeStringUtil.isBlankOrZero(forMontantSigne)) {
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).setForMontantSigne(forMontantSigne);
        }
        String forMontantSigneValue = request.getParameter("forMontantSigneValue");
        if (!JadeStringUtil.isBlankOrZero(forMontantSigneValue)) {
            ((DSInscriptionsIndividuellesListeViewBean) viewBean).setForMontantSigneValue(forMontantSigneValue);
        }
        String forAvertissement = request.getParameter("forAvertissementStr");
        if (forAvertissement != null) {
            if ("True".equals(forAvertissement)) {
                ((DSInscriptionsIndividuellesListeViewBean) viewBean).setForAvertissement(new Boolean(true));
            } else {
                ((DSInscriptionsIndividuellesListeViewBean) viewBean).setForAvertissement(new Boolean(false));
            }
        }
        return viewBean;
    }

}
