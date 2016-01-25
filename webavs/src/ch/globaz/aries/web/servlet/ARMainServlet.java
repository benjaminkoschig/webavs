package ch.globaz.aries.web.servlet;

import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.aries.web.application.ARApplication;
import ch.globaz.aries.web.servlet.urlstack.ARUrlStackRuleActionPushedOnStack;

/**
 * Main servlet de ARIES
 * 
 * @author FWI
 */
public class ARMainServlet extends FWJadeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ARMainServlet() {
        super(ARApplication.DEFAULT_APPLICATION_ARIES, ARApplication.DEFAULT_APPLICATION_ARIES,
                ARApplication.APPLICATION_ARIES_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {

        List<String> actionPushedOnStack = new ArrayList<String>();
        actionPushedOnStack.add("aries.decisioncgas.decisionCgasSearch.afficherCgasSearch");
        actionPushedOnStack.add("aries.renouvellementdecisionmasse.renouvellementDecisionMasse.afficher");
        actionPushedOnStack.add("aries.sortiecgas.sortieCgas.afficher");

        ARUrlStackRuleActionPushedOnStack urlStackRuleActionPushedOnStack = new ARUrlStackRuleActionPushedOnStack(
                actionPushedOnStack);

        FWRemoveActionsEndingWith removeWidget = new FWRemoveActionsEndingWith("widget.action.jade.afficher");
        FWRemoveActionsEndingWith removeWidgetDownload = new FWRemoveActionsEndingWith("widget.action.jade.download");

        aStack.addRule(urlStackRuleActionPushedOnStack);
        aStack.addRule(removeWidget);
        aStack.addRule(removeWidgetDownload);

    }

    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu("aries-menuprincipal", "menu");
            menuBB.setCurrentMenu("aries-optionsempty", "options");
        }
        StringBuffer path = new StringBuffer("/").append(ARApplication.APPLICATION_ARIES_REP);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    @Override
    protected void initializeActionMapping() {
        registerActionMapping("aries.decisioncgas", ARDefaultAction.class);
        registerActionMapping("aries.renouvellementdecisionmasse", ARDefaultAction.class);
        registerActionMapping("aries.sortiecgas", ARDefaultAction.class);
    }
}
