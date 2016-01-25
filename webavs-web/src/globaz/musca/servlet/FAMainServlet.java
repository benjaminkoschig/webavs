package globaz.musca.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.globall.api.BISession;
import globaz.musca.servlet.print.FAImprimerAction;
import globaz.musca.stack.rules.NaviRules;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (08.05.2002 14:03:28)
 * 
 * @author: Administrator
 */
public class FAMainServlet extends FWServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur TIMainServlet.
     */
    public FAMainServlet() {
        super();
    }

    /**
     * @see globaz.framework.servlets.FWServlet#createController(BISession)
     */
    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, "musca", "FA");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 11:26:46)
     * 
     * @param aStack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    @Override
    protected void customize(globaz.framework.utils.urls.FWUrlsStack aStack) {
        // regle de la pile pour le back
        NaviRules rule = new NaviRules(aStack);
        // aStack.addRule(new FANavigationMultiListes());
        aStack.addRule(rule);

        FWRemoveActionsEndingWith suppLister = new FWRemoveActionsEndingWith(FWAction.ACTION_LISTER);
        aStack.addRule(suppLister);
    }

    /**
     * @see FWServlet.doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
     *      javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
     *      throws javax.servlet.ServletException, java.io.IOException
     */
    @Override
    public void doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter("userAction");
        String actionSuite = FWAction.newInstance(action).getPackagePart();

        if ("facturation".equalsIgnoreCase(actionSuite)) {
            doActionFacturation(session, request, response, mainController);
        } else if ("interets".equalsIgnoreCase(actionSuite)) {
            doActionInteretsMoratoires(session, request, response, mainController);
        } else if ("process".equalsIgnoreCase(actionSuite)) {
            doActionProcess(session, request, response, mainController);
        } else if ("print".equalsIgnoreCase(actionSuite)) {
            FWDefaultServletAction act = new FAImprimerAction(this);
            act.doAction(session, request, response, mainController);
        } else if ("gestionJourFerie".equalsIgnoreCase(actionSuite)) {
            doActionGestionJourFerie(session, request, response, mainController);
        } else {
            getServletContext().getRequestDispatcher("/enConstruction.jsp").forward(request, response);
        }
    }

    /**
     * Actions de l'application Musca - Facturation Date de création
     * 
     * @param session
     * @param request
     * @param response
     * @param mainController
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void doActionFacturation(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        if (actionSuite.equals("planFacturation")) {
            FAActionPlanFacturation act = new FAActionPlanFacturation(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("moduleFacturation")) {
            FAActionModuleFacturation act = new FAActionModuleFacturation(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("processServices")) {
            FAActionProcessServices act = new FAActionProcessServices(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("passageFacturation")) {
            FAActionPassageFacturation act = new FAActionPassageFacturation(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("passage")) {
            FAActionPassage act = new FAActionPassage(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("modulePassage")) {
            FAActionModulePassage act = new FAActionModulePassage(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("enteteFacture")) {
            FAActionEnteteFacture act = new FAActionEnteteFacture(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("moduleImpression")) {
            FAActionModuleImpression act = new FAActionModuleImpression(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("modulePlan")) {
            FAActionModulePlan act = new FAActionModulePlan(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("afact")) {
            FAActionAfact act = new FAActionAfact(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("afactAQuittancer")) {
            FAActionAfactAQuittancer act = new FAActionAfactAQuittancer(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("ordreAttribuer")) {
            FAActionOrdreAttribuer act = new FAActionOrdreAttribuer(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("passageFacturationSousEnsembleAffilies")) {
            FAActionPassageFacturationSousEnsembleAffilies act = new FAActionPassageFacturationSousEnsembleAffilies(
                    this);
            act.doAction(session, request, response, mainController);
        } else {
            FWDefaultServletAction act = new FWDefaultServletAction(this);
            act.doAction(session, request, response, mainController);
        }
    }

    public void doActionGestionJourFerie(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        FWDefaultServletAction action = null;
        if ("modifierSupprimerPeriode".equalsIgnoreCase(actionSuite)) {
            action = new FAActionModifierSupprimerPeriode(this);
        } else {
            action = new FWDefaultServletAction(this);
        }
        action.doAction(session, request, response, mainController);
    }

    public void doActionInteretsMoratoires(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        FWDefaultServletAction action = null;
        if ("gestionInterets".equalsIgnoreCase(actionSuite)) {
            action = new FAActionGestionInterets(this);
        } else if ("changementJournalIM".equals(actionSuite)) {
            action = new FAActionChangementJournalIM(this);
        }
        action.doAction(session, request, response, mainController);
    }

    /**
     * Actions de l'application Musca - Facturation<br/>
     * Traite les actions de processus.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainController
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void doActionProcess(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        if (actionSuite.equals("processServices")) {
            FAActionProcessServices act = new FAActionProcessServices(this);
            act.doAction(session, request, response, mainController);
        } else {
            FWDefaultServletAction act = new FWDefaultServletAction(this);
            act.doAction(session, request, response, mainController);
        }
    }

    /**
     * @see FWServlet.goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
     *      javax.servlet.http.HttpServletResponse response) throws Exception
     */
    @Override
    protected void goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("FA-MenuPrincipal", "menu");
        bb.setCurrentMenu("FA-OptionVide", "options");

        String path = "/" + globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/";
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /**
     * Détermine si le servlet actuel doit utiliser la langue utilisateur pour trouver les pages .jsp (genre
     * <CODE>/FR</CODE>, <CODE>/DE</CODE>, etc.).
     * 
     * @return <code>true</code> si le servlet utilise la langue utilisateur, <code>false</code> sinon. Avec FW V29 Date
     *         de création : (18.05.2005 11:00:00)
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /**
     * Découpe et retourne une chaîne de caractère compris entre un élément
     * 
     * @param str
     * @param pos
     * @return
     */
    public String split(String str, int pos) {
        Vector tmp = new Vector();
        try {
            StringTokenizer st = new StringTokenizer(str, ".");
            while (st.hasMoreTokens()) {
                tmp.add(st.nextToken());
            }
            return (String) tmp.elementAt(pos);
        } catch (Exception e) {
            return null;
        }
    }
}
