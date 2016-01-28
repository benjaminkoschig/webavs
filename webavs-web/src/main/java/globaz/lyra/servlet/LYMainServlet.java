package globaz.lyra.servlet;

import globaz.corvus.servlet.RERentePourEnfantAction;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.lyra.application.LYApplication;
import globaz.lyra.stack.LYNaviRules;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author PBA
 */
public class LYMainServlet extends FWJadeServlet {

    private static final long serialVersionUID = 3449537699083832597L;

    public LYMainServlet() {
        super(LYApplication.DEFAULT_APPLICATION_LYRA, LYApplication.DEFAULT_APPLICATION_LYRA,
                LYApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
        aStack.addRule(new LYNaviRules(aStack));
    }

    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu("ly-menuprincipal", "menu");
            menuBB.setCurrentMenu("ly-optionsempty", "options");
        }

        StringBuilder path = new StringBuilder();
        path.append("/").append(LYApplication.APPLICATION_LYRA_REP);
        path.append("/").append(FWServlet.HOMEPAGE + ".jsp");

        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    @Override
    protected void initializeActionMapping() {
        registerActionMapping(ILYActions.ACTION_ECHEANCES, LYEcheancesAction.class);
        registerActionMapping(ILYActions.ACTION_HISTORIQUE, LYDefaultAction.class);
        registerActionMapping(ILYActions.ACTION_PROCESS, LYProcessAction.class);
        registerActionMapping("", RERentePourEnfantAction.class);
    }
}
