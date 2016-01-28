package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionImprimerLotViewBean;
import globaz.phenix.db.principale.CPDecisionListViewBean;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPEnteteViewBean;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.summary.TIActionSummary;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */
public class CPActionDecision extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionDecision(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * 
     *            HACK : Cr�ation d'un nouveau viewBean (afin d'�viter l'utilisation de l'ancien qui �tend un process)
     *            D�placement de l'execution du process dans le nouveau helper (attention 2 helper ayant le meme nom
     *            pour cette classe, l'un se trouve dans le package helpers.process et l'autre sous helpers.principale)
     */
    private void _actionExecuterRecomptabiliser(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);
            dispatcher.dispatch(viewBean, getAction());
            // CPDecisionRecomptabiliserViewBean viewBean =
            // (CPDecisionRecomptabiliserViewBean) session
            // .getAttribute("viewBean");
            // Cr�e le process qui inserere le fichier dans la BD
            // CPProcessDecisionRecomptabiliser process = new
            // CPProcessDecisionRecomptabiliser();
            // process.setEMailAddress(request.getParameter("eMailAddress"));
            // process.setIdDecision(request.getParameter("idDecision"));
            // process.setSession((BSession) dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.process.decision.recomptabiliser";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(
        // request, response);
        goSendRedirect(_destination, request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * 
     *            HACK : Cr�ation d'un nouveau viewBean (afin d'�viter l'utilisation de l'ancien qui �tend un process)
     *            D�placement de l'execution du process dans le nouveau helper (attention 2 helper ayant le meme nom
     *            pour cette classe, l'un se trouve dans le package helpers.process et l'autre sous helpers.principale)
     */
    private void _actionExecuterReporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);
            // CPDecisionReporterViewBean viewBean =
            // (CPDecisionReporterViewBean) session
            // .getAttribute("viewBean");
            dispatcher.dispatch(viewBean, getAction());
            // Cr�e le process qui inserere le fichier dans la BD
            // CPProcessReporterDecisionPreEncodee process = new
            // CPProcessReporterDecisionPreEncodee();
            // process.setIdPassage(request.getParameter("idPassage"));
            // process.setEMailAddress(request.getParameter("eMailAddress"));
            // process.setSession((BSession) dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.process.decision.reporter";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == false) {
                _destination = _destination + "&process=launched";
                response.sendRedirect(request.getContextPath() + _destination);
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "decisionReporter_de.jsp";
                servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(
        // request, response);
        // this.goSendRedirect(_destination, request, response);
    }

    /**
     * On surcharge la m�thode afin de rediriger sur les pages souhait�es sans surcharger la m�thode actionSupprimer
     */

    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = "";

        if (viewBean instanceof CPDecisionViewBean) {
            CPDecisionViewBean vb = (CPDecisionViewBean) viewBean;

            if (CPDecision.CS_IMPUTATION.equals(vb.getTypeDecision())) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "decisionMiseEnCompte_de.jsp?_valid=fail&_back=sl";
            } else if (vb.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "decisionRemise_de.jsp?_valid=fail&_back=sl";
            } else if (vb.isNonActif()) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "decisionNac_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "decisionInd_de.jsp?_valid=fail&_back=sl";
            }
        } else {
            _destination = super._getDestSupprimerEchec(session, request, response, viewBean);
        }
        return _destination;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00)
     * 
     * Cette action �tait surcharg�e � cause de la redirection particuli�re qui �tait exig�e. Il n'est maintenant plus
     * n�cessaire de la surcharger car la redirection est donn�e en surchargeant les m�thodes _getdestSupprimerSuccess
     * et _getDestSupprimerEchec
     */
    // public void actionSupprimer(javax.servlet.http.HttpSession session,
    // javax.servlet.http.HttpServletRequest request,
    // javax.servlet.http.HttpServletResponse response,
    // globaz.framework.controller.FWDispatcher mainController)
    // throws javax.servlet.ServletException, java.io.IOException {
    // // --- Variables
    // CPDecisionViewBean viewBean = (CPDecisionViewBean) session
    // .getAttribute("viewBean");
    // if (viewBean == null) {
    // viewBean = new CPDecisionViewBean();
    // }
    // viewBean.setMsgType("");
    // try {
    // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
    // viewBean.delete();
    // FWHelper.afterExecute(viewBean);
    // // --- Check view bean
    // session.removeAttribute("viewBean");
    // session.setAttribute("viewBean", viewBean);
    //
    // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
    // if (CPDecision.CS_IMPUTATION.equals(viewBean.getTypeDecision()))
    // servlet
    // .getServletContext()
    // .getRequestDispatcher(
    // getRelativeURLwithoutClassPart(request,
    // session)
    // + "decisionMiseEnCompte_de.jsp?_valid=fail&_back=sl")
    // .forward(request, response);
    // else if (viewBean.getTypeDecision().equalsIgnoreCase(
    // CPDecision.CS_REMISE))
    // servlet
    // .getServletContext()
    // .getRequestDispatcher(
    // getRelativeURLwithoutClassPart(request,
    // session)
    // + "decisionRemise_de.jsp?_valid=fail&_back=sl")
    // .forward(request, response);
    // else if (viewBean.isNonActif())
    // servlet
    // .getServletContext()
    // .getRequestDispatcher(
    // getRelativeURLwithoutClassPart(request,
    // session)
    // + "decisionNac_de.jsp?_valid=fail&_back=sl")
    // .forward(request, response);
    // else
    // servlet
    // .getServletContext()
    // .getRequestDispatcher(
    // getRelativeURLwithoutClassPart(request,
    // session)
    // + "decisionInd_de.jsp?_valid=fail&_back=sl")
    // .forward(request, response);
    // } else
    // servlet.getServletContext().getRequestDispatcher(
    // getActionFullURL() + ".chercher&idTiers="
    // + viewBean.getIdTiers()).forward(request,
    // response);
    // } catch (Exception e) {
    // goSendRedirect(ERROR_PAGE, request, response);
    // }
    // }
    /**
     * On surcharge la m�thode afin de rediriger sur les pages souhait�es sans surcharger la m�thode actionSupprimer
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = "";

        if (viewBean instanceof CPDecisionViewBean) {
            CPDecisionViewBean vb = (CPDecisionViewBean) viewBean;

            _destination = getActionFullURL() + ".chercher&idTiers=" + vb.getIdTiers();
        } else {
            _destination = super._getDestSupprimerSucces(session, request, response, viewBean);
        }
        return _destination;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00) HACK : les forwards sont
     * laiss�s du fait que les pages de destination sont diff�rentes l'execution des m�thodes _initEcran() et retrieve()
     * sont d�sormais execut�e dans le helper (CPRejetsHelper)
     */
    @Override
    public void actionAfficher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPDecisionViewBean viewBean = new CPDecisionViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));

            if ("add".equalsIgnoreCase(request.getParameter("_method"))) {
                viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
                viewBean.setGenreAffilie(request.getParameter("genreAffilie"));
                viewBean.setAnneeDecision(request.getParameter("anneeDecision"));
                viewBean.setTypeDecision(request.getParameter("typeDecision"));
                // Par d�faut, automatique
                // viewBean._initEcran();
            } else {
                viewBean.setIdDecision(request.getParameter("selectedId"));
                // viewBean.retrieve();
            }
            mainController.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        } else if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session)
                                    + "decisionMiseEnCompte_de.jsp?_valid=fail").forward(request, response);
        } else if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "decisionRemise_de.jsp?_valid=fail")
                    .forward(request, response);
        } else if (viewBean.isNonActif()) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "decisionNac_de.jsp?_valid=fail")
                    .forward(request, response);
        } else {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "decisionInd_de.jsp?_valid=fail")
                    .forward(request, response);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00) HACK : Surcharge cette m�thode
     * puisque la redirection en cas d'erreur est diff�rente et qu'un reafficher n'est pas envisageable et donc que le
     * forward est dans ce cas n�cessaire.
     */
    @Override
    public void actionAjouter(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        /*
         * recuperation du bean depuis la session
         */
        CPDecisionViewBean viewBean = (CPDecisionViewBean) session.getAttribute("viewBean");

        /*
         * set automatique des proprietes
         */
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = (CPDecisionViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (CPDecisionViewBean) mainController.dispatch(viewBean, _action);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "decisionMiseEnCompte_de.jsp?_valid=fail&_back=sl")
                        .forward(request, response);
            } else if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "decisionRemise_de.jsp?_valid=fail&_back=sl").forward(request, response);
            } else if (viewBean.isNonActif()) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "decisionNac_de.jsp?_valid=fail&_back=sl").forward(request, response);
            } else {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "decisionInd_de.jsp?_valid=fail&_back=sl").forward(request, response);
            }
        } else if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)
                && !JadeStringUtil.isEmpty(viewBean.getWarningMessage())) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session)
                                    + "decisionRemise_de.jsp?_valid=fail&_back=sl").forward(request, response);
        } else {
            goSendRedirect(getActionFullURL() + ".calculer&idDecision=" + viewBean.getIdDecision(), request, response);
        }
    }

    /**
     * Action qui permet de lancer le calcul des cotisations pour une d�cision Date de cr�ation : (03.05.2002 08:57:00)
     * HACK : Execution du process transf�r� dans le helper (DPDecisionHelper)
     */
    public void actionCalculer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPDecisionViewBean viewBean = (CPDecisionViewBean) session.getAttribute("viewBean");
        // CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            viewBean.setIdDecision(request.getParameter("idDecision"));
            // calcul.setIdDecision((String)
            // request.getParameter("idDecision"));
            // calcul.setISession((globaz.globall.db.BSession) bSession);
            // calcul.setSendMailOnError(true);
            // calcul.setSendCompletionMail(false);
            // // calcul.setEMailAddress("hna@globaz.ch");
            // calcul.executeProcess();
            dispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        if ((viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true)) {
            if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "decisionMiseEnCompte_de.jsp?_valid=fail").forward(request, response);
            } else if (viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session) + "decisionRemise_de.jsp?_valid=fail")
                        .forward(request, response);
            } else if (viewBean.isNonActif()) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session) + "decisionNac_de.jsp?_valid=fail")
                        .forward(request, response);
            } else {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session) + "decisionInd_de.jsp?_valid=fail")
                        .forward(request, response);
            }
        } else {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getActionFullURL() + ".imprimer&idDecision=" + request.getParameter("idDecision"))
                    .forward(request, response);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00) HACK : retrieve execut� dans
     * le helper + appel de la classe parente � la place de la redirection en forward
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        CPEnteteViewBean viewBean = new CPEnteteViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            session.removeAttribute("idSortie");
            if (!JadeStringUtil.isIntegerEmpty(request.getParameter("idTiers"))) {
                session.setAttribute("idTiers", request.getParameter("idTiers"));
                session.setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, request.getParameter("idTiers"));
            } else {
                session.setAttribute("idTiers", request.getParameter("selectedId"));
                session.setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, request.getParameter("selectedId"));
            }
            if (request.getParameter("selectedId2") == null) {
                session.setAttribute("idAffiliation", request.getParameter("idAffiliation"));
            } else {
                session.setAttribute("idAffiliation", request.getParameter("selectedId2"));
            }
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            // viewBean.retrieve();
            if (request.getParameter("doc") != null) {
                // permettre d'afficher dans une autre page le document qui
                // vient d'�tre g�n�r�
                String[] docs = request.getParameterValues("doc");
                for (int i = 0; i < docs.length; i++) {
                    viewBean.addDoc(docs[i]);
                }
            }
            mainController.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        session.removeAttribute("duplicata");
        super.actionChercher(session, request, response, mainController);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // En cr�ation on propose un �cran d'initialisation pour pr�parer
        // l'�cran d'encodage
        // de la d�cision suivant le type et l'ann�e saisis.
        if ("initCreer".equals(getAction().getActionPart())) {
            actionInitCreer(session, request, response, mainDispatcher);
        } else if ("initialiser".equals(getAction().getActionPart())) {
            actionInitialiser(session, request, response, mainDispatcher);
        } else if ("calculer".equals(getAction().getActionPart())) {
            actionCalculer(session, request, response, mainDispatcher);
        } else if ("imprimer".equals(getAction().getActionPart())) {
            actionImprimer(session, request, response, Boolean.FALSE, mainDispatcher);
        } else if ("duplicata".equals(getAction().getActionPart())) {
            actionImprimer(session, request, response, Boolean.TRUE, mainDispatcher);
        } else if ("devalider".equals(getAction().getActionPart())) {
            actionDevalider(session, request, response, mainDispatcher);
        } else if ("imprimerLot".equals(getAction().getActionPart())) {
            actionImprimerLot(session, request, response, mainDispatcher);
        } else if ("dernierDossier".equals(getAction().getActionPart())) {
            actionDernierDossier(session, request, response, mainDispatcher);
            // } else if ("supprimerDirect".equals(getAction().getActionPart()))
            // {
            // actionSupprimerDirect(session, request, response);
        } else if ("reporter".equals(getAction().getActionPart())) {
            actionReporter(session, request, response, mainDispatcher);
        } else if ("executerReporter".equals(getAction().getActionPart())) {
            _actionExecuterReporter(session, request, response, mainDispatcher);
        } else if (getAction().getActionPart().equals("executerRecomptabiliser")) {
            _actionExecuterRecomptabiliser(session, request, response, mainDispatcher);
        } else if ("recomptabiliser".equals(getAction().getActionPart())) {
            actionRecomptabiliser(session, request, response, mainDispatcher);
        }
    }

    /**
     * Action qui permet de lancer le calcul des cotisations pour une d�cision Date de cr�ation : (03.05.2002 08:57:00)
     * 
     * HACK : hasRight supprimer (les droits sont d�j� d�clar� dans CPApplication) Forward non modifi�, car impossible
     * d'apeller la m�thode actionChercher + redirection sp�cifique
     */
    public void actionDernierDossier(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request

        if ((!JadeStringUtil.isNull((String) session.getAttribute("idTiers")))
                || (!JadeStringUtil.isNull((String) session.getAttribute("idAffiliation")))) {

            CPDecisionViewBean viewBean = new CPDecisionViewBean();
            try {
                globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);

                viewBean.setSession((globaz.globall.db.BSession) bSession);
                viewBean.setSession((globaz.globall.db.BSession) bSession);
                viewBean.setIdTiers((String) session.getAttribute("idTiers"));
                viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getActionFullURL() + ".chercher&idTiers=" + viewBean.getIdTiers() + "&selectedId2="
                                    + viewBean.getIdAffiliation()).forward(request, response);
        }
    }

    /**
     * Action qui permet de lancer la d�validation d'une d�cision.
     * 
     * Suppression du hasRight (renseign� dans la d�claration des actions customs dans CPApplication) Execution du
     * process dans le Helper On conserve le forward (redirection sp�cifique � l'action custom)
     */
    public void actionDevalider(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        String _destination = "";
        CPDecisionViewBean viewBean = new CPDecisionViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            viewBean.setIdDecision(request.getParameter("idDecision"));

            dispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        _destination = getActionFullURL() + ".chercher&idTiers=" + viewBean.getIdTiers() + "&selectedId2="
                + viewBean.getIdAffiliation();
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action qui permet de lancer le calcul des cotisations pour une d�cision Date de cr�ation : (03.05.2002 08:57:00)
     * HACK : Rajout des getters/setters pour les variables duplicata et docListe dans CPDecisionViewBean On set
     * directement les valeurs dans le viewbean de mani�re � ne pas le faire directement dans le process L'execution du
     * process se fait maintenant dans le Helper de la classe. Afin de r�cup�rer la valeur de docListe depuis le
     * process, on set la valeur obtenue par le process dans le viewBean (tout ca dans le Helper)
     */
    public void actionImprimer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, Boolean duplicata, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        try {
            BISession bSession = CodeSystem.getSession(session);

            CPDecisionViewBean vb = new CPDecisionViewBean();
            vb.setSession((BSession) bSession);
            vb.setIdTiers((String) session.getAttribute("idTiers"));
            vb.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            vb.setIdDecision(request.getParameter("idDecision"));
            vb.setDuplicata(duplicata);

            dispatcher.dispatch(vb, getAction());

            if (Boolean.FALSE.equals(vb.getDuplicata())) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getActionFullURL().substring(0, getActionFullURL().lastIndexOf('.'))
                                        + ".decisionValider.afficher&_method=upd&selectedId="
                                        + request.getParameter("idDecision") + vb.getDocListe())
                        .forward(request, response);
            } else {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getActionFullURL() + ".chercher&idTiers=" + vb.getIdTiers() + "&selectedId2="
                                        + vb.getIdAffiliation() + vb.getDocListe()).forward(request, response);
            }
        } catch (Exception e) {
            // goSendRedirect(ERROR_PAGE, request, response);
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
    }

    /**
     * HACK: m�thode _init() executer dans le Helper de la classe forward conserv� car redirection sur une page JSP
     */
    private void actionImprimerLot(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination;
        try {
            // nouvelle instance du bean utilis� dans l'en-t�te de la recherche.
            CPDecisionImprimerLotViewBean viewBean = new CPDecisionImprimerLotViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            // viewBean.setIdJournal(request.getParameter("selectedId"));
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // String action = request.getParameter("userAction");
            // appel du controlleur
            mainDispatcher.dispatch(viewBean, getAction());
            // sauve le bean dans la session
            // viewBean._init();
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURLwithoutClassPart(request, session) + "decisionImprimerLot_de.jsp";
        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00)
     * 
     * Modification : actionCustom ajout�e dans CPApplication Suppression du hasRight Retrieve ex�cut� dans le helper de
     * la classe on conserve le forward car la destination est diff�rente
     */
    public void actionInitCreer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // --- Get value from request
        CPDecisionViewBean viewBean = new CPDecisionViewBean();
        try {
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            dispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session)
                                    + "decisionInit_de.jsp?_valid=fail&_back=sl").forward(request, response);
        } else {
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURLwithoutClassPart(request, session) + "decisionInit_de.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00)
     * 
     * Forward laiss� identique (redirection sp�cifique) Les m�thodes retrieve et _controle sont maintenant appell�es
     * dans le Helper
     */
    public void actionInitialiser(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPDecisionViewBean viewBean = new CPDecisionViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // Test si l'on a chang� de tiers (saisie de masse), dans ce cas il
            // faut aller rechercher
            // l'affiliation et contr�ler la saisie du tiers
            if ((request.getParameter("numAffilie")).equalsIgnoreCase(request.getParameter("selection"))) {
                viewBean.setIdTiers((String) session.getAttribute("idTiers"));
                viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            } else {
                viewBean.setIdTiers(request.getParameter("idTiers"));
                viewBean.setIdAffiliation("");
            }
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            dispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session)
                                    + "decisionInit_de.jsp?_valid=fail&_back=sl").forward(request, response);
        } else {
            session.removeAttribute("idAffiliation");
            session.removeAttribute("idTiers");
            session.setAttribute("idTiers", viewBean.loadTiers().getIdTiers());
            session.setAttribute("idAffiliation", viewBean.getIdAffiliation());
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getActionFullURL() + ".afficher&_method=add&_valid=fail&anneeDecision="
                                    + viewBean.getAnneeDecision() + "&genreAffilie=" + viewBean.getGenreAffilie()
                                    + "&selection=" + viewBean.getSelection()).forward(request, response);
        }
    }

    /**
     * Supprime directement � partir du rcListe Date de cr�ation : (03.05.2002 08:57:00)
     * 
     * Il n'y a pas ou plus d'options pour supprimer la d�cision depuis la rcListe Il faut afficher le d�tail d'une
     * d�cision pour pouvoir la supprimer et dans ce cas nous ne passons plus par cette m�thode Elle est d�sormais
     * inutile.
     */
    // public void actionSupprimerDirect(javax.servlet.http.HttpSession session,
    // javax.servlet.http.HttpServletRequest request,
    // javax.servlet.http.HttpServletResponse response)
    // throws javax.servlet.ServletException, java.io.IOException {
    // // --- Variables
    // CPDecisionViewBean viewBean = new CPDecisionViewBean();
    // try {
    // globaz.globall.api.BISession bSession =
    // globaz.phenix.translation.CodeSystem
    // .getSession(session);
    // viewBean.setSession((globaz.globall.db.BSession) bSession);
    // viewBean.setIdTiers((String) session.getAttribute("idTiers"));
    // viewBean.setIdDecision((String) request.getParameter("idDecision"));
    // viewBean.retrieve();
    // viewBean.delete();
    // FWHelper.afterExecute(viewBean);
    // } catch (Exception e) {
    // viewBean.setMessage(e.getMessage());
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // }
    // // --- Check view bean
    // session.removeAttribute("viewBean");
    // session.setAttribute("viewBean", viewBean);
    // servlet.getServletContext().getRequestDispatcher(
    // getActionFullURL() + ".chercher&idTiers="
    // + viewBean.getIdTiers()).forward(request, response);
    // }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"),
                globaz.framework.secure.FWSecureConstants.READ);
        try {
            /*
             * creation automatique du listviewBean
             */
            CPDecisionListViewBean viewBean = new CPDecisionListViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            // Positionnement selon les crit�res de recherche
            viewBean.setForIdTiers((String) session.getAttribute("idTiers"));
            if (JadeStringUtil.isNull(viewBean.getForIdTiers())) {
                viewBean.setForIdTiers("");
            }
            viewBean.setForIdAffiliation((String) session.getAttribute("idAffiliation"));
            if (JadeStringUtil.isNull(viewBean.getForIdAffiliation())) {
                viewBean.setForIdAffiliation("");
            }
            // Ordre d'affichage
            viewBean.setUseTiers(Boolean.FALSE);
            viewBean.orderByAnneeDecision();
            viewBean.orderByEtatDecision();
            viewBean.orderByDateComptabilisation();// PO 8100
            viewBean.orderByIdDecision();
            /*
             * set automatique des properietes du listViewBean depuis la requete
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            /*
             * beforeLister() , puis appelle du dispatcher, puis le bean est mis en request
             */
            viewBean = (CPDecisionListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (CPDecisionListViewBean) mainDispatcher.dispatch(viewBean, _action);
            request.setAttribute("viewBean", viewBean);
            /*
             * destination : remarque : si erreur, on va quand meme sur la liste avec le bean vide en erreur
             */
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 08:57:00)
     * 
     * HACK : Copie de la premi�re partie de la m�thode actionModifier de FWDefaultServletAction On garde la
     * redirection, car elle peut �tre diff�rente selon la situation goSendRedirect modifier en forward sur la page
     * d'erreur
     */
    @Override
    public void actionModifier(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        try {
            // --- Check view bean
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);
            /*
             * recup�ration du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            CPDecisionViewBean viewBean1 = (CPDecisionViewBean) session.getAttribute("viewBean");
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainController.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                if (((CPDecision) viewBean).getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                    servlet.getServletContext()
                            .getRequestDispatcher(
                                    getRelativeURLwithoutClassPart(request, session)
                                            + "decisionMiseEnCompte_de.jsp?_valid=fail&_back=sl")
                            .forward(request, response);
                } else if (((CPDecision) viewBean).getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
                    servlet.getServletContext()
                            .getRequestDispatcher(
                                    getRelativeURLwithoutClassPart(request, session)
                                            + "decisionRemise_de.jsp?_valid=fail&_back=sl").forward(request, response);
                } else if (((CPDecision) viewBean).isNonActif()) {
                    servlet.getServletContext()
                            .getRequestDispatcher(
                                    getRelativeURLwithoutClassPart(request, session)
                                            + "decisionNac_de.jsp?_valid=fail&_back=sl").forward(request, response);
                } else {
                    servlet.getServletContext()
                            .getRequestDispatcher(
                                    getRelativeURLwithoutClassPart(request, session)
                                            + "decisionInd_de.jsp?_valid=fail&_back=sl").forward(request, response);
                }
            } else if (viewBean1.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)
                    && !JadeStringUtil.isEmpty(viewBean1.getWarningMessage())) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "decisionRemise_de.jsp?_valid=fail&_back=sl").forward(request, response);
            } else {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getActionFullURL() + ".calculer&idDecision=" + ((CPDecision) viewBean).getIdDecision())
                        .forward(request, response);
            }
        } catch (Exception e) {
            // goSendRedirect(ERROR_PAGE, request, response);
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     * 
     *             Appel de la nouvelle classe CPDecisionRecomptabiliserViewBean hasRight supprimer car d�j� d�clar�
     *             dans CPApplication pour les actions customs Forward sur page sp�cifique + passage par le dispatcher =
     *             OK
     * 
     */

    private void actionRecomptabiliser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination;

        try {
            // nouvelle instance du bean utilis� dans l'en-t�te de la recherche.
            globaz.phenix.vb.decision.CPDecisionRecomptabiliserViewBean viewBean = new globaz.phenix.vb.decision.CPDecisionRecomptabiliserViewBean();
            BISession bSession = CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            JSPUtils.setBeanProperties(request, viewBean);
            // appel du controlleur
            viewBean = (globaz.phenix.vb.decision.CPDecisionRecomptabiliserViewBean) mainDispatcher.dispatch(viewBean,
                    getAction());
            viewBean.setIdDecision(request.getParameter("idDecision"));
            if ((request.getParameter("wantMajCI") != null) && request.getParameter("wantMajCI").equalsIgnoreCase("no")) {
                viewBean.setWantMajCI(Boolean.FALSE);
            } else {
                viewBean.setWantMajCI(Boolean.TRUE);
            }
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURLwithoutClassPart(request, session) + "decisionNonComptabilisee_de.jsp";
        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     * 
     *             Cette action est appell�e depuis le menu de phenix. Appel de la nouvelle classe
     *             CPDecisionReporterViewBean hasRight supprimer car d�j� d�clar� dans CPApplication pour les actions
     *             customs Forward sur page sp�cifique + passage par le dispatcher = OK
     */
    private void actionReporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination;

        try {
            // nouvelle instance du bean utilis� dans l'en-t�te de la recherche.
            globaz.phenix.vb.decision.CPDecisionReporterViewBean viewBean = new globaz.phenix.vb.decision.CPDecisionReporterViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // appel du controlleur
            viewBean = (globaz.phenix.vb.decision.CPDecisionReporterViewBean) mainDispatcher.dispatch(viewBean,
                    getAction());
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            _destination = getRelativeURLwithoutClassPart(request, session) + "decisionReporter_de.jsp";
        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
