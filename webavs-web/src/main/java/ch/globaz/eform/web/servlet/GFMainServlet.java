package ch.globaz.eform.web.servlet;

import ch.globaz.eform.web.application.GFApplication;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.framework.utils.urls.rules.FWSuppressSameUserActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class GFMainServlet extends FWJadeServlet {

    private static final long serialVersionUID = 1L;

    public final static String ACTION_DOSSIER_GED = "ged";

    /**
     * Noeuds du menu d?sactiv?s en mode droit "Lecture seule"
     */
    private static final List<String> nodesDisabledReadingMode = new ArrayList<>();

    static {
        GFMainServlet.nodesDisabledReadingMode.add("Nouveau");
    }

    /**
     *
     */
    public GFMainServlet() {
        super(GFApplication.APPLICATION_ID, GFApplication.APPLICATION_NAME, GFApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void initializeActionMapping() {
        //Mapping des actions P14
        registerActionMapping("eform.formulaire", GFFormulaireServletAction.class);
        registerActionMapping("eform.statistique", GFStatistiqueServletAction.class);
        //Mapping des actions Da-Dossier
        registerActionMapping("eform.envoi", GFEnvoiServletAction.class);
        registerActionMapping("eform.demande", GFDemandeServletAction.class);
        registerActionMapping("eform.suivi", GFSuiviServletAction.class);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {
        // ************* gestion des actions ? exclure du stacktrace pour bon fonctionnement bouton applicatif retour
        // action standards
        ///FWRemoveActionsEndingWith removeLister = new FWRemoveActionsEndingWith(".lister");

        aStack.addRule(new FWRemoveActionsEndingWith(".lister"));
        aStack.addRule(new FWRemoveActionsEndingWith(".modifier"));
        aStack.addRule(new FWRemoveActionsEndingWith(".telecharger"));
        aStack.addRule(new FWRemoveActionsEndingWith(".ged"));

        // Evite de garder 2 fois la m?me page
        aStack.addRule(new FWSuppressSameUserActions());
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession ,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu("eform-menuprincipal", "menu");
            menuBB.setCurrentMenu("eform-optionsempty", "options");
        }

        StringBuffer path = new StringBuffer("/").append(GFApplication.DEFAULT_APPLICATION_ROOT);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }
}
