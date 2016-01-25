package globaz.hercule.servlet;

import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.servlets.widget.FWJadeWidgetServletAction;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.hercule.application.CEApplication;
import globaz.hercule.stack.rules.NaviRules;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CEMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Map<String, Object> ACTION_TO_CLASS = new HashMap();

    private static final Class[] PARAMS_CONSTRUCTOR = new Class[] { FWServlet.class };

    static {
        // TODO rajouter les actions
        ACTION_TO_CLASS.put("hercule", FWDefaultServletAction.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.controleEmployeur", CEActionControleEmployeur.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.reviseur", FWDefaultServletAction.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.saisieRapideReviseur", CEActionSaisieMasseReviseur.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.saisieMasseReviseur", CEActionSaisieMasseReviseur.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.imprimerControle", FWDefaultServletAction.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.imprimerlettrelibre", FWDefaultServletAction.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.lettreProchainControle", CEActionControleEmployeur.class);
        // ACTION_TO_CLASS.put("hercule.controleEmployeur.statOFASControle",AFActionControleEmployeur.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.controlesAttribues", CEActionControlesAttribues.class);
        ACTION_TO_CLASS.put("hercule.controleEmployeur.controlesAEffectuer", CEActionControlesAEffectuer.class);
        ACTION_TO_CLASS.put("hercule.declarationStructuree.saisieMasseReception", CEActionSaisieMasseReception.class);
        ACTION_TO_CLASS.put("hercule.groupement.membre", CEActionGroupementMembre.class);
        ACTION_TO_CLASS.put("widget.action", FWJadeWidgetServletAction.class);
        // ACTION_TO_CLASS.put("hercule.controleEmployeur.controles5PourCent",
        // FWDefaultServletAction.class);
    }

    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, CEApplication.DEFAULT_APPLICATION_HERCULE,
                CEApplication.APPLICATION_HERCULE_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.addRule(new NaviRules(aStack));
        aStack.addRule(new FWRemoveActionsEndingWith("widget.action.jade.lister"));
        aStack.addRule(new FWRemoveActionsEndingWith("hercule.groupement.membre.afficher"));
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        loadSessions();

        // Met à jour le servlet en cours
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());

        // Trouve l'action
        String action = request.getParameter("userAction");
        Class servletClass = (Class) ACTION_TO_CLASS.get(action);

        while (servletClass == null) {
            action = action.substring(0, action.lastIndexOf('.'));
            servletClass = (Class) ACTION_TO_CLASS.get(action);
        }

        // Crée l'action
        FWDefaultServletAction servletAction = null;

        try {
            Constructor constructor = servletClass.getConstructor(PARAMS_CONSTRUCTOR);

            servletAction = (FWDefaultServletAction) constructor.newInstance(new Object[] { this });
        } catch (Exception e) {
            JadeLogger.error(this, "Action incorrecte: " + request.getParameter("userAction"));
            servletAction = new FWDefaultServletAction(this);
        }

        servletAction.doAction(session, request, response, mainController);
    }

    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = "/" + CEApplication.APPLICATION_HERCULE_ROOT + "/";

        // ajouter des choix par défaut pour les menus
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);

        menuBB.setCurrentMenu("CE-MenuPrincipal", "menu");
        menuBB.setCurrentMenu("CE-OptionsDefaut", "options");

        // rediriger vers la page
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    /**
     * Charge une session HERCULE afin de disposer de tout les labels.
     */
    private void loadSessions() {
        try {
            GlobazSystem.getApplication(CEApplication.DEFAULT_APPLICATION_HERCULE);
        } catch (Exception e) {
            // Do nothing
        }
    }

}
