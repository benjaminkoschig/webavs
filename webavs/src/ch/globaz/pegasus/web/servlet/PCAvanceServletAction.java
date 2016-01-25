package ch.globaz.pegasus.web.servlet;

import globaz.corvus.api.avances.IREAvances;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.avance.PCAvanceViewBean;
import globaz.pegasus.vb.avance.PCExecuterAvancesViewBean;
import globaz.pegasus.vb.avance.PCListeAvancesViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.constantes.IPCActions;

public class PCAvanceServletAction extends PCAbstractServletAction {

    private String destination = "";

    /**
     * Constucteur avec servlet en paramètre
     * 
     * @param aServlet
     */
    public PCAvanceServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return dealErrors(request);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCAvanceViewBean) {
            String idDemande = ((PCAvanceViewBean) viewBean).getIdDemande();
            String idTiersreq = ((PCAvanceViewBean) viewBean).getIdTiersRequerant();

            destination = "/pegasus?userAction=pegasus.avance.listeAvances.afficher&idDemande=" + idDemande
                    + "&idTiers=" + idTiersreq;

        }

        return destination;
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        /*
         * _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja rempli par l'utilisateur
         */
        return getActionFullURL() + ".reAfficher";
        // return getActionFullURL() + ".afficher" + "&_valid=fail";
        // return getRelativeURL(request,session)+"_de.jsp?_valid=fail";
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // _valid=fail : revient en mode edition
        // return getRelativeURL(request,session)+"_de.jsp?_valid=fail";
        return getActionFullURL() + ".reAfficher&process=ko";
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // on appelle reafficher avec ok
        return getActionFullURL() + ".reAfficher&process=ok";
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return dealErrors(request);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCAvanceViewBean) {
            String idDemande = ((PCAvanceViewBean) viewBean).getIdDemande();
            String idTiersreq = ((PCAvanceViewBean) viewBean).getIdTiersDemande();

            destination = "/pegasus?userAction=pegasus.avance.listeAvances.afficher&idDemande=" + idDemande
                    + "&idTiers=" + idTiersreq;

        }

        return destination;
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCAvanceViewBean) {
            String idDemande = ((PCAvanceViewBean) viewBean).getIdDemande();
            String idTiersreq = ((PCAvanceViewBean) viewBean).getIdTiersDemande();

            destination = "/pegasus?userAction=pegasus.avance.listeAvances.afficher&idDemande=" + idDemande
                    + "&idTiers=" + idTiersreq;

        }
        return destination;
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // est en erreur
            boolean error = false;

            // si erreur
            error = BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().getErrors().length() > 0;

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) || error;
            if (goesToSuccessDest && !error) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        // user action
        String action = request.getParameter("userAction");

        if (IPCActions.ACTION_AVANCE_DETAIL_EXECUTE.equals(action)) {

            // type de traitement
            String typeTraitement = request.getParameter("csTypeAvance");
            // mail
            String mail = request.getParameter("email");

            // liste
            if ((IREAvances.CS_TYPE_LISTES).equals(typeTraitement)) {
                // paramètres obligatoire
                if (JadeStringUtil.isBlankOrZero(mail)) {
                    // erreur on reaffiche
                    // TODO voir erreur
                    actionReAfficher(session, request, response, mainDispatcher);
                } else {
                    FWViewBeanInterface viewBean = new PCExecuterAvancesViewBean();
                    ((PCExecuterAvancesViewBean) viewBean).setEmail(mail);
                    ((PCExecuterAvancesViewBean) viewBean).setTypeTraitement(typeTraitement);
                    session.setAttribute("viewBean", viewBean);
                    super.actionExecuter(session, request, response, mainDispatcher);
                }
            }
            // paiement unique
            else if ((IREAvances.CS_TYPE_ACOMPTES_UNIQUE).equals(typeTraitement)) {
                if (JadeStringUtil.isBlankOrZero(mail)) {
                    // erreur on reaffiche
                    // TODO voir erreur
                    actionReAfficher(session, request, response, mainDispatcher);
                } else {
                    FWViewBeanInterface viewBean = new PCExecuterAvancesViewBean();
                    ((PCExecuterAvancesViewBean) viewBean).setEmail(mail);
                    ((PCExecuterAvancesViewBean) viewBean).setTypeTraitement(typeTraitement);
                    session.setAttribute("viewBean", viewBean);
                    super.actionExecuter(session, request, response, mainDispatcher);
                }
            } else {

            }

        }
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);

            // est en erreur
            boolean error = false;

            // si erreur
            error = BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().getErrors().length() > 0;
            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest && !error) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // Si process
        String process = request.getParameter("process");
        if (process != null) {
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp" + "?process=" + process)
                    .forward(request, response);
        }
        // si pas process, action "standard"
        else {
            boolean processSeemsOk = "launched".equals(process);
            String validFail = processSeemsOk ? "" : "?_valid=fail";
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp" + validFail)
                    .forward(request, response);
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // ecran de detail des avances
        if (viewBean instanceof PCAvanceViewBean) {
            // on set les paramètres
            String idDemande = request.getParameter("idDemande");
            String idAvance = request.getParameter("idAvance");
            String idTiers = request.getParameter("idTiers");

            String method = request.getParameter("_method");

            // Si methode add, on initialise la préparation
            if ((method != null) && request.getParameter("_method").equals("add")) {
                ((PCAvanceViewBean) viewBean).setIdDemande(idDemande);
                ((PCAvanceViewBean) viewBean).setIdTiersDemande(idTiers);
                ((PCAvanceViewBean) viewBean).initForNew();
            } else {
                ((PCAvanceViewBean) viewBean).setIdDemande(idDemande);
                ((PCAvanceViewBean) viewBean).setIdTiersDemande(idTiers);
                ((PCAvanceViewBean) viewBean).setIdAvance(idAvance);
            }
        }

        if (viewBean instanceof PCListeAvancesViewBean) {
            // idedemande pc depuis menuxml
            String idDemande = request.getParameter("idDemandePc");

            if (idDemande == null) {
                idDemande = request.getParameter("idDemande");
            }
            String idTiers = request.getParameter("idTiers");

            if (idDemande != null) {
                ((PCListeAvancesViewBean) viewBean).setIdDemande(idDemande);
            }
            if (idTiers != null) {
                ((PCListeAvancesViewBean) viewBean).setIdTiers(idTiers);
            }

        }

        return viewBean;
    }

    /**
     * On gère ici les
     * 
     * @param request
     * @return
     */
    private String dealErrors(HttpServletRequest request) {

        StringBuffer err = null;
        err = BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().getErrors();

        StringBuilder destination = new StringBuilder(getActionFullURL() + ".reAfficher");
        destination.append("&_method=").append(request.getParameter("_method")).append("&valid=_fail");
        destination.append("&errorMsg=" + err);

        return destination.toString();

    }

}
