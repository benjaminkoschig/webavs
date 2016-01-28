package ch.globaz.auriga.web.servlet;

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
import ch.globaz.auriga.web.application.AUApplication;
import ch.globaz.auriga.web.servlet.urlstack.AUUrlStackRuleActionPushedOnStack;

/**
 * Main servlet de SITAX
 * 
 * @author FWI
 */
public class AUMainServlet extends FWJadeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AUMainServlet() {
        super(AUApplication.DEFAULT_APPLICATION_AURIGA, AUApplication.DEFAULT_APPLICATION_AURIGA,
                AUApplication.APPLICATION_AURIGA_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {

        List<String> actionPushedOnStack = new ArrayList<String>();
        actionPushedOnStack.add("auriga.decisioncap.decisionCapSearch.afficherCapSearch");
        actionPushedOnStack.add("auriga.renouvellementdecisionmasse.renouvellementDecisionMasse.afficher");
        actionPushedOnStack.add("auriga.sortiecap.sortieCap.afficher");

        AUUrlStackRuleActionPushedOnStack urlStackRuleActionPushedOnStack = new AUUrlStackRuleActionPushedOnStack(
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
            menuBB.setCurrentMenu("auriga-menuprincipal", "menu");
            menuBB.setCurrentMenu("auriga-optionsempty", "options");
        }
        StringBuffer path = new StringBuffer("/").append(AUApplication.APPLICATION_AURIGA_REP);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    @Override
    protected void initializeActionMapping() {
        registerActionMapping("auriga.decisioncap", AUDefaultAction.class);
        registerActionMapping("auriga.renouvellementdecisionmasse", AUDefaultAction.class);
        registerActionMapping("auriga.sortiecap", AUDefaultAction.class);
    }
}
