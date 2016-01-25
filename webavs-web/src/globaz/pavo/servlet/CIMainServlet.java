package globaz.pavo.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.pavo.stack.rules.NaviRules;
import globaz.pavo.util.CIUtil;

/**
 * Servlet principale de l'application PAVO. Date de création : (14.10.2002 16:23:04)
 * 
 * @author: dgi
 */
public class CIMainServlet extends FWServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur.
     */
    public CIMainServlet() {
        super();
    }

    /**
     * Créé le dispatcher principal de l'application. Date de création : (08.05.2002 14:26:28)
     * 
     * @return une nouvelle instance du dispatcher
     */
    @Override
    public FWController createController(globaz.globall.api.BISession session) {
        // cree un controller de type dispatcher pour l'application
        return new FWDispatcher(session, "pavo", "CI");
    };

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 11:26:46)
     * 
     * @param aStack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    @Override
    protected void customize(globaz.framework.utils.urls.FWUrlsStack aStack) {
        /*
         * FWDefaultRule rule = new FWDefaultRule(aStack); aStack.addRule(rule);
         */
        FWRemoveActionsEndingWith regleSaisieMasseTotauxAfficher = new FWRemoveActionsEndingWith(
                "pavo.compte.compteIndividuel.chercherDetailAnnonce");
        aStack.addRule(regleSaisieMasseTotauxAfficher);
        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        // aStack.setAutoexec(false);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        long time = System.currentTimeMillis();
        // --- Check user action
        // servlet path
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter("userAction");

        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("CI-MenuPrincipal", "menu");

        try {
            menuBB.setNodeActive(false, "annonce_centrale", "CI-MenuPrincipal");
            if (CIUtil.isSpecialist(session)) {
                menuBB.setNodeActive(true, "annonce_centrale", "CI-MenuPrincipal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * if ((action == null) || (action.equals("")) || (action.equals("homePage"))) { // --- Call home page String
         * path = "/" + globaz.pavo.application.CIApplication.APPLICATION_PAVO_REP + "/" +
         * FWDefaultServletAction.getIdLangueIso(session) + "/"; this.getServletContext().getRequestDispatcher(path +
         * "homePage.jsp").forward( request, response); } else if ("back".equals(action)) { FWUrlsStack stack =
         * getUrlStack(session); stack.setAutoexec(true); // supprime l'action back et recherche d'un URL valide dans la
         * pile FWUrl backUrl = stack.pop(); stack.setAutoexec(false); // back backUrl = stack.pop(); if (backUrl !=
         * null) { System.out.println("back to: " + backUrl.toString()); this .getServletContext()
         * .getRequestDispatcher(backUrl.getPageName() + "?" + backUrl.getParamsList()) .forward(request, response); }
         * else { String path = "/" + globaz.pavo.application.CIApplication.APPLICATION_PAVO_REP + "/" +
         * FWDefaultServletAction.getIdLangueIso(session) + "/"; this.getServletContext().getRequestDispatcher(path +
         * "appMain.jsp").forward( request, response); } } else {
         */

        if ("splitting".equals(FWAction.newInstance(action).getPackagePart())) {
            doActionSplitting(session, request, response, mainController);
        } else if ("inscriptions".equals(FWAction.newInstance(action).getPackagePart())) {
            doActionInscriptions(session, request, response, mainController);
        } else if ("compte".equals(FWAction.newInstance(action).getPackagePart())) {
            doActionCompte(session, request, response, mainController);
        } else if ("process".equals(FWAction.newInstance(action).getPackagePart())) {

            doActionProcess(session, request, response, mainController);
        } else if ("comparaison".equals(FWAction.newInstance(action).getPackagePart())) {
            doActionComparaison(session, request, response, mainController);
        } else if ("bta".equals(FWAction.newInstance(action).getPackagePart())) {
            doActionBta(session, request, response, mainController);
        }

        else {
            // --- Call home page
            getServletContext().getRequestDispatcher("/enConstruction.jsp").forward(request, response);
        }
        // }
    }

    public void doActionBta(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        mainController.displayTree();

        String action = FWAction.newInstance(request.getParameter("userAction")).getClassPart();
        FWDefaultServletAction act = null;
        if ("dossierBta".equals(action)) {
            act = new CIActionDossierBta(this);
        } else if ("requerantBta".equals(action)) {
            act = new CIActionRequerantBta(this);
        } else if ("decisionBta".equals(action)) {
            act = new CIActionDecisionBta(this);
        } else if ("repetitionBta".equals(action)) {
            act = new CIActionRepetitionBta(this);
        } else if ("inscriptionRetroBta".equals(action)) {
            act = new CIActionInscriptionRetroBta(this);
        } else if ("inscriptionBta".equals(action)) {
            act = new CIActionInscriptionBta(this);
        } else {
            act = new FWDefaultServletAction(this);
        }

        act.doAction(session, request, response, mainController);
    }

    public void doActionComparaison(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        mainController.displayTree();
        String action = FWAction.newInstance(request.getParameter("userAction")).getClassPart();
        FWDefaultServletAction act = null;
        act = new CIActionComparaison(this);
        act.doAction(session, request, response, mainController);

    }

    public void doActionCompte(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {

        mainController.displayTree();

        String action = FWAction.newInstance(request.getParameter("userAction")).getClassPart();
        FWDefaultServletAction act = null;
        if ("ecriture".equals(action)) {
            act = new CIActionEcriture(this);
        } else if ("periodeSplitting".equals(action)) {
            act = new CIActionPeriodeSplitting(this);
        } else if ("rassemblementOuverture".equals(action)) {
            act = new CIActionRassemblementOuverture(this);
        } else if ("compteIndividuel".equals(action)) {
            act = new CIActionCompteIndividuel(this);
        } else if ("historique".equals(action)) {
            act = new CIActionHistorique(this);

        } else if ("ecrituresSuspens".equals(action)) {
            act = new CIActionEcrituresSuspens(this);
        } else if ("InscriptionsManuelles".equals(action)) {
            act = new CIActionInscriptionsManuelles(this);
        } else if ("ecrituresNonRA".equals(action)) {
            act = new CIActionEcrituresNonRA(this);
        } else if ("exceptions".equals(action)) {
            act = new CIActionException(this);
        } else {
            // pas utilisé pour le moment
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    public void doActionInscriptions(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {

        mainController.displayTree();

        String action = FWAction.newInstance(request.getParameter("userAction")).getClassPart();
        FWDefaultServletAction act = null;
        if ("journal".equals(action)) {
            act = new CIActionJournal(this);
        } else if ("declaration".equals(action)) {
            act = new CIActionDeclaration(this);
        } else {
            // pas utilisé pour le moment
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    public void doActionProcess(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        mainController.displayTree();

        String action = FWAction.newInstance(request.getParameter("userAction")).getClassPart();
        FWDefaultServletAction act = null;

        if ("processServices".equals(action)) {
            // act = new CIActionProcessServices(this);
            act = new FWDefaultServletAction(this);
        } else if ("annonceCentraleProcess".equals(action)) {
            act = new CIActionProcess(this);
            // act = new FWDefaultServletAction(this);

        } else if ("statistiquesCiProcess".equals(action)) {
            act = new CIActionProcess(this);
            // act = new FWDefaultServletAction(this);
        } else if ("detectionDoublEcritureProcess".equals(action)) {
            act = new CIActionProcess(this);
            // act = new FWDefaultServletAction(this);
        } else if ("imprimerExtrait".equals(action)) {
            act = new CIActionProcess(this);
            // act = new FWDefaultServletAction(this);
        } else if ("imprimerInscriptionActif".equals(action)) {
            act = new CIActionProcess(this);
            // act = new FWDefaultServletAction(this);
        } else if ("conversionGenre6".equals(action)) {
            act = new CIActionGenre6(this);
        } else if ("annonceCentraleImpressionRapport".equalsIgnoreCase(action)) {
            act = new CIActionAnnonceCentraleImpressionRapport(this);
        } else {
            // pas utilisé pour le moment
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * Gére les différentes actions concernant le splitting. Date de création : (03.05.2002 08:57:00)
     */
    public void doActionSplitting(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        mainController.displayTree();

        String action = FWAction.newInstance(request.getParameter("userAction")).getClassPart();
        FWDefaultServletAction act = null;
        if ("dossierSplitting".equals(action)) {
            act = new CIActionDossierSplitting(this);
        } else if ("domicileSplitting".equals(action)) {
            act = new CIActionDomicileSplitting(this);
        } else if ("mandatSplitting".equals(action)) {
            act = new CIActionMandatSplitting(this);
        } else if ("revenuSplitting".equals(action)) {
            act = new CIActionRevenuSplitting(this);
        } else {
            // pas utilisé pour le moment
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 07:41:07)
     */
    @Override
    protected void goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        String path = "/" + globaz.pavo.application.CIApplication.APPLICATION_PAVO_REP + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/";

        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("CI-MenuPrincipal", "menu");
        bb.setCurrentMenu("CI-OnlyDetail", "options");
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /**
     * Détermine si le servlet actuel doit utiliser la langue utilisateur pour trouver les pages .jsp (genre
     * <CODE>/FR</CODE>, <CODE>/DE</CODE>, etc.).
     * 
     * @return <code>true</code> si le servlet utilise la langue utilisateur, <code>false</code> sinon.
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /**
     * Initializes the servlet.
     */
    @Override
    public void init() {
    }

}
