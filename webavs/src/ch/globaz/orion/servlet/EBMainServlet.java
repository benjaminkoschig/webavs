package ch.globaz.orion.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.fx.common.application.servlet.NaviRules;
import globaz.jade.context.JadeThreadContext;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.provider.OwnerProvider;

public class EBMainServlet extends FWJadeServlet {

    private static final long serialVersionUID = 1L;

    public EBMainServlet() {
        super(EBApplication.APPLICATION_ID, EBApplication.APPLICATION_NAME, EBApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack stack) {
        NaviRules rule = new NaviRules(stack);
        stack.addRule(rule);

        // supprime les actions interdites de la pile
        List listeInterdits = new ArrayList();
        listeInterdits.add(".lister");
        listeInterdits.add(".choisir");
        listeInterdits.add(".selectionner");
        listeInterdits.add("orion.pucs.pucsImport.executer");
        listeInterdits.add("AJAX");
        listeInterdits.add("widget.action.jade.afficher");
        listeInterdits.add("widget.action.jade.download");
        listeInterdits.add("orion.swissdec.pucsValidationDetail.refuser");
        listeInterdits.add("orion.swissdec.pucsValidationDetail.accepter");
        FWRemoveActionsEndingWith remRule = new FWRemoveActionsEndingWith(listeInterdits);
        stack.addRule(remRule);

    }

    @Override
    protected void defineParameterInThreadContext(JadeThreadContext context) {
        super.defineParameterInThreadContext(context);
        context.storeTemporaryObject("OWNER", OwnerProvider.getOwner());
    }

    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        StringBuffer path = new StringBuffer("/").append(EBApplication.APPLICATION_ROOT);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    @Override
    protected void initializeActionMapping() {
        registerActionMapping("orion.pucs", EBPucsServletAction.class);
        registerActionMapping("orion.dan", FWDefaultServletAction.class);
        registerActionMapping("orion.acompte", FWDefaultServletAction.class);
        registerActionMapping("orion.partnerWeb", FWDefaultServletAction.class);
        registerActionMapping("orion.swissdec", EBActionValidationSwissDec.class);
    }
}
