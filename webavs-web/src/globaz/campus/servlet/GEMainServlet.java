package globaz.campus.servlet;

import globaz.campus.application.GEApplication;
import globaz.campus.stack.rules.GECampusEntryRule;
import globaz.campus.stack.rules.NaviRules;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.framework.utils.urls.rules.FWSuppressSameUserActions;
import globaz.globall.api.BISession;
import globaz.templates.FWRemoveOldestStackUrl;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet principal de l'application aquila.
 * 
 * @author acr, 01-oct-2007
 */
public class GEMainServlet extends FWServlet {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.framework.servlets.FWServlet#createController(globaz.globall.api.BISession)
     */
    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, GEApplication.DEFAULT_APPLICATION_CAMPUS.toLowerCase(),
                GEApplication.APPLICATION_CAMPUS_PREFIX);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        FWRemoveActionsEndingWith suppLister = new FWRemoveActionsEndingWith(FWAction.ACTION_LISTER);
        aStack.addRule(suppLister);
        FWSuppressSameUserActions supDouble = new FWSuppressSameUserActions();
        aStack.addRule(supDouble);
        FWRemoveOldestStackUrl oldStack = new FWRemoveOldestStackUrl(aStack);
        aStack.addRule(new GECampusEntryRule());
        // aStack.addRule(new GENaviRules(aStack));
        // aStack.addRule(new GEAjoutModifAnnonces());
        // aStack.addRule(new FWDefaultRule(aStack,
        // GEApplication.DEFAULT_APPLICATION_CAMPUS));
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("GEMenuPrincipal", "menu");
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());
        String actionSuite = split(request.getParameter("userAction"), 1);
        if (actionSuite.equals("lots") == true) {
            doActionLots(session, request, response, mainController);
        } else if (actionSuite.equals("annonces") == true) {
            doActionAnnonces(session, request, response, mainController);
        } else if (actionSuite.equals("etudiants") == true) {
            doActionEtudiants(session, request, response, mainController);
        } else if (actionSuite.equals("process") == true) {
            doActionProcess(session, request, response, mainController);
        } else {
            getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE).forward(request,
                    response);
        }
    }

    public void doActionAnnonces(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        if (actionSuite.equals("imputations") == true) {
            GEActionImputations act = new GEActionImputations(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("annonces") == true) {
            GEActionAnnonces act = new GEActionAnnonces(this);
            act.doAction(session, request, response, mainController);
        } else {
            FWDefaultServletAction act = new FWDefaultServletAction(this);
            act.doAction(session, request, response, mainController);
        }
    }

    public void doActionEtudiants(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        GEActionEtudiants act = new GEActionEtudiants(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionLots(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionProcess(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);
        act.doAction(session, request, response, mainController);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = "/" + GEApplication.APPLICATION_CAMPUS_ROOT + "/"
                + FWDefaultServletAction.getIdLangueIso(httpSession) + "/";

        // ajouter des choix par défaut pour les menus
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);

        menuBB.setCurrentMenu("GEMenuPrincipal", "menu");
        menuBB.setCurrentMenu("GEOptionsDefaut", "options");

        // rediriger vers la page
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /* Découpe et retourne une chaîne de caractère compris entre un élément */
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
