package globaz.hera.servlet;

import globaz.framework.bean.FWViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWMessageFormat;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.hera.application.SFApplication;
import globaz.hera.stack.SFNaviRules;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
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
 * @author scr
 */
public class SFMainServlet extends FWServlet {

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
        return new FWDispatcher(arg0, SFApplication.DEFAULT_APPLICATION_SF.toLowerCase(),
                SFApplication.APPLICATION_PREFIX);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aStack
     *            DOCUMENT ME!
     * 
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
        aStack.addRule(new SFNaviRules(aStack));
    }

    /**
     * DOCUMENT ME!
     * 
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
        request.setAttribute("mainServletPath", request.getServletPath());

        // ===================================================================================
        // Contrôle des droits

        boolean hasRight = false;
        FWAction action = new FWRequestActionAdapter().adapt(request);
        String userId = mainController.getSession().getUserId();
        String user = JadeAdminServiceLocatorProvider.getLocator().getUserService().findVisaForIdUser(userId);
        try {
            String right = action.getRight();
            if (right == null) {
                // pour les actions custom, on utilise le droit UPDATE
                right = FWSecureConstants.UPDATE;
            }
            hasRight = mainController.getSession().hasRight(action.getElement(), right);
        } catch (java.rmi.RemoteException e) {
            hasRight = false;
        }
        if (!hasRight) {
            String msg = FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]", user,
                    action.getRight(), action);
            FWViewBean viewBean = new FWViewBean();
            viewBean.setMessage(msg);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            // redirection sur page d'erreur
            String viewName = FWDefaultServletAction.ERROR_PAGE;
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            /*
             * Redirect
             */
            request.getRequestDispatcher(viewName).forward(request, response);
        } else {
            // ===================================================================================
            // reset le cache si necessaire
            if ((action.getActionPart().equals(FWAction.ACTION_AJOUTER))
                    || (action.getActionPart().equals(FWAction.ACTION_MODIFIER))
                    || (action.getActionPart().equals(FWAction.ACTION_SUPPRIMER))) {
                String classPart = action.getClassPart();
                if ((classPart.equals("userGroup")) || (classPart.equals("roleUser"))
                        || (classPart.equals("roleGroup")) || (classPart.equals("attribution"))) {
                    JadeAdminServiceLocatorProvider.getLocator().reset();
                }
            }

            String actionName = request.getParameter(USER_ACTION);
            FWAction act = FWAction.newInstance(actionName);

            // String actionSuite = act.getPackagePart();
            // String actionPart = act.getActionPart();
            FWDefaultServletAction actionMapper = null;

            try {
                if (JadeLogger.isDebugEnabled()) {
                    JadeLogger.debug(this, "APG action " + act);
                }

                Class actionClass = SFMainServletAction.getActionClass(act);

                if (actionClass == null) {
                    actionClass = FWDefaultServletAction.class;
                }

                Constructor c = actionClass.getConstructor(new Class[] { globaz.framework.servlets.FWServlet.class });
                actionMapper = (FWDefaultServletAction) c.newInstance(new Object[] { this });
            } catch (Exception e) {
                JadeLogger.warn(this, "*********************************************************");
                JadeLogger.warn(this, "* HERA : WARNING                                         ");
                JadeLogger.warn(this, "* Action mapping FAILED for action " + act);
                JadeLogger.warn(this, "*********************************************************");
                e.printStackTrace();
            }

            try {
                // java.util.Enumeration enum = request.getParameterNames();
                // System.out.println("-------------------------------------------------->>>");
                // while (enum.hasMoreElements()) {
                // String key = (String)enum.nextElement();
                // System.out.println(key + " = " + request.getParameter(key));
                // }
                // System.out.println("--------------------------------------------------<<<");
                //

                actionMapper.doAction(session, request, response, mainController);
            } catch (Exception e) {
                JadeLogger.warn(this, "*********************************************************");
                JadeLogger.warn(this, "* HERA : WARNING                                         ");
                JadeLogger.warn(this, "* Action failed  : action " + act);
                JadeLogger.warn(this, "*********************************************************");
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
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
        String path = "/" + SFApplication.APPLICATION_SF_REP + "/";
        path = path.concat(HOMEPAGE + ".jsp");

        forward(path, request, response);

        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("sf-menuprincipal", "menu");
        menuBB.setCurrentMenu("sf-optionsempty", "options");
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
