package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.controller.FWScenarios;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIAnnonceSuspens;
import globaz.pavo.db.compte.CIAnnonceSuspensViewBean;
import globaz.pavo.db.compte.CICalculeMasseSalarialeViewBean;
import globaz.pavo.db.compte.CICompteIndividuelAttestationViewBean;
import globaz.pavo.db.compte.CICompteIndividuelCertificatAssuranceViewBean;
import globaz.pavo.db.compte.CICompteIndividuelExtournerViewBean;
import globaz.pavo.db.compte.CICompteIndividuelImprimerViewBean;
import globaz.pavo.db.compte.CICompteIndividuelRechercheViewBean;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action concernant les journeaux. Date de création : (07.11.2002 17:43:11)
 * 
 * @author: ema
 */
public class CIActionCompteIndividuel extends FWDefaultServletAction {

    /**
     * Commentaire relatif au constructeur CIActionJournal.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionCompteIndividuel(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    protected void _actionAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CICompteIndividuelCertificatAssuranceViewBean viewBean = new CICompteIndividuelCertificatAssuranceViewBean();
            // viewBean.setFromIdJournal(request.getParameter("selectedId"));
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // viewBean.retrieve();
            // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean.setSession((BSession) mainDispatcher.getSession());
            // viewBean.setSession((BSession)mainDispatcher.getSession());

            viewBean = (CICompteIndividuelCertificatAssuranceViewBean) mainDispatcher.dispatch(viewBean, action);
            viewBean.retrieve();
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURL(request, session) + "CertificatAssurance_de.jsp";
            // _destination = getAction().getPackagePart() +
            // "/journalImprimer_de.jsp";
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    protected void _actionAfficherAttest(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CICompteIndividuelAttestationViewBean viewBean = new CICompteIndividuelAttestationViewBean();
            // viewBean.setFromIdJournal(request.getParameter("selectedId"));
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // viewBean.retrieve();
            // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur

            viewBean.setSession((BSession) mainDispatcher.getSession());

            viewBean = (CICompteIndividuelAttestationViewBean) mainDispatcher.dispatch(viewBean, action);
            viewBean.retrieve();
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURL(request, session) + "Attestation_de.jsp";
            // _destination = getAction().getPackagePart() +
            // "/journalImprimer_de.jsp";
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionAfficherExtourneCI(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CICompteIndividuelExtournerViewBean viewBean = new CICompteIndividuelExtournerViewBean();
            // viewBean.setFromIdJournal(request.getParameter("selectedId"));
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // viewBean.retrieve();
            // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CICompteIndividuelExtournerViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURL(request, session) + "Extourner_de.jsp";
            // _destination = getAction().getPackagePart() +
            // "/journalImprimer_de.jsp";
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAnnonce(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher) {
        String _destination = null;
        try {
            CIAnnonceSuspensViewBean viewBean = new CIAnnonceSuspensViewBean();
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CIAnnonceSuspensViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            CIAnnonceSuspens sus = new CIAnnonceSuspens();
            sus.setAnnonceSuspensId(viewBean.getAnnonceSuspensId());
            sus.setSession((BSession) viewBean.getISession());
            sus.retrieve();
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {

            } else {
                _destination = "/pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId="
                        + sus.getIdCIRA();
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

    }

    private void _actionAnnonceDetail(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) {
        String _destination = null;
        try {
            CIAnnonceSuspensViewBean viewBean = new CIAnnonceSuspensViewBean();
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CIAnnonceSuspensViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            CIAnnonceSuspens sus = new CIAnnonceSuspens();
            sus.setAnnonceSuspensId(viewBean.getAnnonceSuspensId());
            sus.setSession((BSession) viewBean.getISession());
            sus.retrieve();
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {

            } else {
                _destination = "/hermes?userAction=hermes.parametrage.attenteReception.afficher&selectedId="
                        + sus.getIdAnnonce();
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

    }

    private void _actionCalculeMasseSalariale(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CICalculeMasseSalarialeViewBean viewBean = new CICalculeMasseSalarialeViewBean();
            viewBean.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // viewBean.retrieve();
            // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CICalculeMasseSalarialeViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURL(request, session) + "CalculeMasseSalariale_de.jsp";
            // _destination = getAction().getPackagePart() +
            // "/journalImprimer_de.jsp";
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionExecuterEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        servlet.getServletContext()
                .getRequestDispatcher(
                        getRelativeURLwithoutClassPart(request, session)
                                + "compteIndividuelExtourner_de.jsp?_valid=fail&_back=sl").forward(request, response);
    }

    private void _actionExtournerCI(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CICompteIndividuelExtournerViewBean viewBean = new CICompteIndividuelExtournerViewBean();
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // appel du controlleur
            viewBean = (CICompteIndividuelExtournerViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getActionFullURL() + ".reAfficherEchec";
            } else {
                _destination = getAction().getApplicationPart().toString()
                        + "?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId="
                        + viewBean.getCompteIndividuelId();
            }
            // Pas de réexecution en cas de back => on conserve le forward
            goSendRedirect(_destination, request, response);

        } catch (Exception ex) {
            JadeLogger.error(this, ex);
        }
    }

    private void _actionImpression(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CICompteIndividuelImprimerViewBean viewBean = new CICompteIndividuelImprimerViewBean();
            JSPUtils.setBeanProperties(request, viewBean);

            if (!JadeStringUtil.isBlank(viewBean.getRassemblementEcritureId())) {
                CIRassemblementOuverture rass = new CIRassemblementOuverture();
                rass.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                rass.setRassemblementOuvertureId(viewBean.getRassemblementEcritureId());
                rass.retrieve();
                viewBean.setCompteIndividuelId(rass.getCompteIndividuelId());
                CICompteIndividuelViewBean viewBeanFk = new CICompteIndividuelViewBean();
                viewBeanFk.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                viewBeanFk.setCompteIndividuelId(viewBean.getCompteIndividuelId());
                viewBeanFk = (CICompteIndividuelViewBean) mainDispatcher.dispatch(viewBeanFk, action);
                session.setAttribute("viewBeanFK", viewBeanFk);
            }
            // appel du controlleur
            viewBean = (CICompteIndividuelImprimerViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURL(request, session) + "Imprimer_de.jsp";
            // _destination = getAction().getPackagePart() +
            // "/journalImprimer_de.jsp";
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=upd&_back=sl&selectedId="
                + ((CICompteIndividuelViewBean) viewBean).getCompteIndividuelId() + "&compteIndividuelId="
                + ((CICompteIndividuelViewBean) viewBean).getCompteIndividuelId();
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher";
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        /*
         * pas de bean par defaut pour l'action chercher
         */

        /*
         * redirection vers destination
         */
        String _destination = "";
        CICompteIndividuelRechercheViewBean viewBean = new CICompteIndividuelRechercheViewBean();
        FWAction action = getAction();
        try {
            viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            viewBean = (CICompteIndividuelRechercheViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                FWAction.newInstance(request.getParameter("userAction")), null);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("imprimer")) {
            // chercher le panneau de lancement de l'impression
            _actionImpression(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("extournerAfficher")) {
            _actionAfficherExtourneCI(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("extourner")) {
            _actionExtournerCI(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("chercherEcritureAnnonce")) {
            _actionAnnonce(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("chercherDetailAnnonce")) {
            _actionAnnonceDetail(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("compareMasseSalariale")) {
            _actionCalculeMasseSalariale(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("reAfficherEchec")) {
            _actionExecuterEchec(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("certificatAfficher")) {
            _actionAfficher(session, request, response, dispatcher);
        }

        if (getAction().getActionPart().equals("attestationAfficher")) {
            _actionAfficherAttest(session, request, response, dispatcher);
        }

    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Suppression de la plausibilité obligeant la saisie d'un critère
            // de recherche PO 4066
            // /**
            // * check des critères
            // */
            // boolean criteresOk = _criteresOk(request);
            //
            // if (criteresOk) {
            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            // } else {
            // BSession ses = (BSession)mainDispatcher.getSession();
            //
            // viewBean.setMessage(ses.getLabel("CRITERES_INSUFFISANTS"));
            // viewBean.setMsgType(FWViewBeanInterface.ERROR);
            //
            // }
            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        CICompteIndividuelViewBean vBean = (CICompteIndividuelViewBean) viewBean;
        vBean.setMainSelectedId(request.getParameter("mainSelectedId"));
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(20);
        return viewBean;
    }

}
