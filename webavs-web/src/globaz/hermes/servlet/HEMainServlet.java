package globaz.hermes.servlet;

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
import globaz.hermes.application.HEApplication;
import globaz.hermes.servlet.urlstack.HELimitToRCForNewARC;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (14.10.2002 15:06:03)
 * 
 * @author: Administrator
 */
public class HEMainServlet extends FWServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String CLASS_ANNONCEORPHELINES = "annoncesOrphelines";
    public static String CLASS_INPUTANNONCE = "inputAnnonce";

    public HEMainServlet() {
        super();
    }

    @Override
    public FWController createController(BISession session) {
        return new FWDispatcher(session, "hermes", "HE");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 11:26:46)
     * 
     * @param aStack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        HELimitToRCForNewARC rule = new HELimitToRCForNewARC();
        aStack.addRule(rule);
        FWRemoveActionsEndingWith suppLister = new FWRemoveActionsEndingWith(FWAction.ACTION_LISTER);
        aStack.addRule(suppLister);
        FWSuppressSameUserActions supDouble = new FWSuppressSameUserActions();
        aStack.addRule(supDouble);
        // FWPropagateSearchCriterias copCritere = new
        // FWPropagateSearchCriterias();
        // aStack.addRule(copCritere);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        // //
        // //
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());
        String actionSuite = split(request.getParameter("userAction"), 1);
        if (actionSuite.equals("parametrage") == true) {
            doActionParametrage(session, request, response, mainController);
        } else if (actionSuite.equals("gestion") == true) {
            doActionGestion(session, request, response, mainController);
        } else {
            // --- Call home page
            getServletContext().getRequestDispatcher("/enConstruction.jsp").forward(request, response);
        }
    }

    public void doActionGestion(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws ServletException, IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        FWDefaultServletAction act = null;
        if (actionSuite.equals("inputAnnonce")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.equals("outputAnnonce")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.equals("lot")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("rappel")) {
            act = new HEActionRappel(this);
        } else if (actionSuite.startsWith("extraitAnnonce")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("ciendouble")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("zas")) {
            // http://localhost:8080/webavs/hermes?userAction=hermes.gestion.zas.getzas.executer
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("rassemblement")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("impressionci")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("genererCIPapier")) {
            act = new HEActionGestions(this);
        } else if (actionSuite.startsWith("configurationService")) {
            act = new HEActionGestions(this);
        } else {
            act = new FWDefaultServletAction(this);
        }

        act.doAction(session, request, response, mainController);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    public void doActionParametrage(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        String parametrageActions[] = { "codeapplication", "attenteRetour", "attenteRetourOptimized", "attenteEnvoi",
                "attenteReception", "attenteEnvoiCI", "attenteRetourCI", "libererCI", "supprimerCI", "annulerAnnonce" };
        String actionSuite = split(request.getParameter("userAction"), 2);
        FWDefaultServletAction act = null;
        if (Arrays.asList(parametrageActions).contains(actionSuite)) {
            act = new HEActionParametrage(this);
            act.doAction(session, request, response, mainController);
        } else {
            act = new FWDefaultServletAction(this);
            act.doAction(session, request, response, mainController);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 07:41:07)
     */
    @Override
    protected void goHomePage(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = "/" + HEApplication.DEFAULT_APPLICATION_ROOT + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/";
        // this.getServletContext().getRequestDispatcher(path +
        // "homePage.jsp").forward(request, response);
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("HE-MenuPrincipal", "menu");
        bb.setCurrentMenu("HE-OnlyDetail", "options");
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
            e.printStackTrace();
            return "";
        }
    }
}