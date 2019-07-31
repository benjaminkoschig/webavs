package globaz.naos.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.IFWActionHandler;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.stack.rules.AFUrlStackRule;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AFMainServlet extends FWServlet {

    private static final long serialVersionUID = -5413558893025057377L;
    private static AFMainServletAction actionHandlerFactory = new AFMainServletAction();

    /**
     * Constructeur d'AFMainServlet.
     */
    public AFMainServlet() {
        super();
    }

    /**
     * Crée un controller de type dispatcher pour l'application Affiliation.
     *
     * @see globaz.framework.servlets.FWServlet#createController(globaz.globall.api.BISession)
     */
    @Override
    public FWController createController(BISession session) {

        return new FWDispatcher(session, "naos", "AF");
    }

    /**
     * Définis règles gérant la pile d'URLs.
     *
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {

        AFUrlStackRule urlStackRule = new AFUrlStackRule();

        aStack.addRule(urlStackRule);
        aStack.setAutoexec(true);

        boolean showMenuAncienControleEmployeur = false;

        try {
            String propertyShowMenuAncienControleEmployeur = ((AFApplication) GlobazSystem
                    .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS))
                    .getProperty("showMenuAncienControleEmployeur");
            showMenuAncienControleEmployeur = Boolean.parseBoolean(propertyShowMenuAncienControleEmployeur);

        } catch (Exception e) {
            showMenuAncienControleEmployeur = false;
            JadeLogger.info(this, "Properties problem : showMenuAncienControleEmployeur >> " + e.getMessage());

        } finally {
            try {
                if (!showMenuAncienControleEmployeur) {
                    FWMenuBlackBox.ensureNodeDoesntExist("menu_node_ancien_controle_employeur", "AFMenuPrincipal");
                }
                if (!((AFApplication) GlobazSystem
                        .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)).hasContactFpvActive()) {
                    FWMenuBlackBox.ensureNodeDoesntExist("ContactFpv", "AFOptionsAffiliation");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Traitement des actions de l'Affiliation.
     *
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                         FWController mainController) throws ServletException, IOException {

        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter("userAction");

        FWAction act = FWAction.newInstance(action);

        IFWActionHandler actionHandler = null;
        try {
            JadeLogger.debug(this, "NAOS action " + act);

            actionHandler = AFMainServlet.actionHandlerFactory.newInstance(request, this);

        } catch (Exception e) {
            JadeLogger.warn(this, "*********************************************************");
            JadeLogger.warn(this, "* NAOS : WARNING                                         ");
            JadeLogger.warn(this, "* Action mapping FAILED for action " + act);
            JadeLogger.warn(this, "* " + new FWRequestActionAdapter().adapt(request).toString());
            JadeLogger.warn(this, "*********************************************************");
            e.printStackTrace();
        }

        try {

            actionHandler.doAction(session, request, response, mainController);

        } catch (Exception e) {
            JadeLogger.warn(this, "*********************************************************");
            JadeLogger.warn(this, "* NAOS : WARNING                                         ");
            JadeLogger.warn(this, "* Action failed  : action " + act);
            JadeLogger.warn(this, "* " + new FWRequestActionAdapter().adapt(request).toString());
            JadeLogger.warn(this, "*********************************************************");
            e.printStackTrace();
        }
    }

    /**
     * Spécifie la page principale de l'application.
     *
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // initialisation des menus
        setMenuSpecialProperties(session);

        String path = "/" + AFApplication.DEFAULT_APPLICATION_NAOS_REP + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/";

        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /**
     * Détermine si le servlet actuel doit utiliser la langue utilisateur pour trouver les pages .jsp (genre
     * <CODE>/FR</CODE>, <CODE>/DE</CODE>, etc.).
     *
     * @return <code>true</code> si le servlet utilise la langue utilisateur, <code>false</code> sinon. Avec FW V29 Date
     * de création : (18.05.2005 11:00:00)
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /**
     * Initialise les menus de l'application
     *
     * @param session
     * @throws Exception
     */
    private void setMenuSpecialProperties(HttpSession session) throws Exception {
        // déclaration du nouveau menu (05.2006)
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("AFMenuPrincipal", "menu");
        bb.setCurrentMenu("AFMenuVide", "options");

        if (JadeGedFacade.isInstalled()) {
            bb.setNodeActive(true, "naos_af_ged", "AFOptionsAffiliation");
        } else {
            FWMenuBlackBox.ensureNodeDoesntExist("naos_af_ged", "AFOptionsAffiliation");
        }

    }
}
