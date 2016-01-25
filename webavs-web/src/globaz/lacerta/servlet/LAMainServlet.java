package globaz.lacerta.servlet;

import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.lacerta.application.LAApplication;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet principal de l'application du fichier central.
 * 
 * @author jpa, 07.04.2007
 */
public class LAMainServlet extends FWServlet {

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
        return new FWDispatcher(session, LAApplication.DEFAULT_APPLICATION_LACERTA.toLowerCase(),
                LAApplication.APPLICATION_LACERTA_PREFIX);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("LAMenuPrincipal", "menu");
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());
        String actionSuite = split(request.getParameter("userAction"), 1);
        if (actionSuite.equals("fichier") == true) {
            doActionFichier(session, request, response, mainController);
        } else if (actionSuite.equals("annonce") == true) {
            doActionAnnonce(session, request, response, mainController);
        } else {
            getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE).forward(request,
                    response);
        }
    }

    public void doActionAnnonce(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        LAActionAnnonce act = new LAActionAnnonce(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionFichier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        LAActionFichier act = new LAActionFichier(this);
        act.doAction(session, request, response, mainController);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = "/" + LAApplication.APPLICATION_LACERTA_ROOT + "/"
                + FWDefaultServletAction.getIdLangueIso(httpSession) + "/";

        // ajouter des choix par défaut pour les menus
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);

        menuBB.setCurrentMenu("LAMenuPrincipal", "menu");
        menuBB.setCurrentMenu("LAOptionsDefaut", "options");

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
