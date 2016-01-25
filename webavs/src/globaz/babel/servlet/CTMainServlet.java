package globaz.babel.servlet;

import globaz.babel.application.CTApplication;
import globaz.babel.stack.CTNaviRules;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.IFWActionHandler;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTMainServlet extends FWServlet {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param arg0
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.servlets.FWServlet#createController(globaz.globall.api.BISession)
     */
    @Override
    public FWController createController(BISession arg0) throws Exception {
        return new FWDispatcher(arg0, CTApplication.DEFAULT_APPLICATION_BABEL.toLowerCase(),
                CTApplication.APPLICATION_PREFIX);
    }

    /**
     * Register the instances responsible of the url stack management.
     * 
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     * 
     * @param aStack
     *            the stack to manage
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
        aStack.addRule(new CTNaviRules(aStack));
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWController)
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainController
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        FWAction act = FWAction.newInstance(request.getParameter(USER_ACTION));
        IFWActionHandler handler = getActionHandler(act);

        request.setAttribute("mainServletPath", request.getServletPath());
        handler.doAction(session, request, response, mainController);
    }

    private IFWActionHandler getActionHandler(FWAction action) throws ServletException {
        Class actionClass = CTMainServletAction.getActionClass(action);

        // default action if unknown
        if (actionClass == null) {
            if (JadeLogger.isDebugEnabled()) {
                JadeLogger.debug(this, "No action class mapped with action string: " + action);
            }

            actionClass = FWDefaultServletAction.class;
        }

        try {
            Constructor cons = actionClass.getConstructor(new Class[] { FWServlet.class });

            return (IFWActionHandler) cons.newInstance(new Object[] { this });
        } catch (Exception e) {
            JadeLogger.error(this, "*********************************************************");
            JadeLogger.error(this, "* ERROR");
            JadeLogger.error(this, "* Unable to create instance of action class: " + actionClass.getName());
            JadeLogger.error(this, "*********************************************************");

            throw new ServletException();
        }
    }

    /**
     * @param httpSession
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        FWMenuBlackBox bb = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("CT-MenuPrincipal", "menu");
        bb.setCurrentMenu("CT-OnlyDetail", "options");
        String path = "/" + CTApplication.APPLICATION_BABEL_REP + "/" + HOMEPAGE + ".jsp";

        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * no languages codes in path
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     * 
     * @return false
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }
}
