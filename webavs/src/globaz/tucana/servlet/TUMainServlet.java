package globaz.tucana.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.framework.utils.urls.rules.FWSuppressSameUserActions;
import globaz.fx.common.application.servlet.NaviRules;
import globaz.globall.api.BISession;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.stack.rules.TUChoisirSelectRule;
import globaz.tucana.stack.rules.TUGererSupprimerRule;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Main servlet de l'application
 * 
 * <pre>
 * web@tucana
 * </pre>
 * 
 * @author fgo
 * @version 1.0
 */
public class TUMainServlet extends FWServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for MainServlet.
     */
    public TUMainServlet() {
        super();
    }

    /**
     * @see globaz.framework.servlets.FWServlet#createController(BISession)
     */
    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, TUApplication.APPLICATION_NAME, TUApplication.APPLICATION_PREFIX);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils. urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        // supprime les actions interdites de la pile
        List listeInterdits = new ArrayList();
        listeInterdits.add(".lister");
        listeInterdits.add(".choisir");
        listeInterdits.add(".selectionner");
        listeInterdits.add(".ajouter");
        FWRemoveActionsEndingWith remRule = new FWRemoveActionsEndingWith(listeInterdits);
        aStack.addRule(remRule);
        // Règle en test
        aStack.addRule(new TUChoisirSelectRule());
        aStack.addRule(new TUGererSupprimerRule());
        aStack.addRule(new FWSuppressSameUserActions());
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(HttpSession, HttpServletRequest, HttpServletResponse,
     *      FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());

        try {
            TUActionTucanaDefault action = TUActionProvider.getAction(this,
                    FWAction.newInstance(request.getParameter("userAction")));
            if (action != null) {
                action.doAction(session, request, response, mainController);
            } else {
                // --- Call home page
                getServletContext().getRequestDispatcher("/enConstruction.jsp").forward(request, response);
            }
        } catch (SecurityException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (IllegalArgumentException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (ServletException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (IOException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (NoSuchMethodException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (InstantiationException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (IllegalAccessException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        } catch (InvocationTargetException e) {
            JadeCodingUtil.assertNotAccessible(getClass(), "doAction", e.getMessage());
            throw new ServletException(e);
        }
    }

    /**
     * @see globaz.framework.servlets.FWServlet#goHomePage(HttpSession, HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("menuWebTucana", "menu");
        bb.setCurrentMenu("optionsTucanaDetail", "options");

        StringBuffer path = new StringBuffer("/").append(TUApplication.DEFAULT_APPLICATION_ROOT);
        path.append("/").append(getLanguageTree((BISession) httpSession.getAttribute(FWServlet.OBJ_SESSION)));
        // path.append("tucana.bouclement.bouclement.chercher");
        // path.append("bouclement/bouclement_rc.jsp");
        path.append("homePage.jsp?userAction=tucana.bouclement.bouclement.chercher");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

}