package globaz.ij.servlet;

import globaz.framework.bean.FWViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.IFWActionHandler;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWMessageFormat;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.ij.application.IJApplication;
import globaz.ij.stack.IJNaviRules;
import globaz.ij.stack.IJRuleAjoutMultiEcran;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author VRE
 */
public class IJMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Contrôle les droits en fonction de l'action. Si l'utilisateur n'a pas les droits, la méthode renvoie
     * <code>false</code>
     * 
     * @param session
     *            la session http
     * @param request
     *            la requête http
     * @param response
     *            la reponse http
     * @param mainController
     *            le controller principale
     * @return <code>false</code> si l'utilisateur n'a pas les droits sur cette action sinon revoie <code>true</code>
     * @throws ServletException
     * @throws IOException
     */
    private boolean checkRights(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {

        boolean hasRight = false;
        FWAction action = new FWRequestActionAdapter().adapt(request);
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
        if (hasRight) {
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

            String actionName = request.getParameter(FWServlet.USER_ACTION);
            FWAction act = FWAction.newInstance(actionName);

            try {
                if (JadeLogger.isDebugEnabled()) {
                    JadeLogger.debug(this, this.getClass().getSimpleName() + " action " + act);
                }

                Class<?> actionClass = IJMainServletAction.getActionClass(act);
                if (actionClass == null) {
                    actionClass = FWDefaultServletAction.class;
                }

                Constructor<?> c = actionClass
                        .getConstructor(new Class[] { globaz.framework.servlets.FWServlet.class });
                @SuppressWarnings("unused")
                // LGA : volontaire car on veut tester l'instanciation de l'actionMapper
                FWDefaultServletAction actionMapper = (FWDefaultServletAction) c.newInstance(new Object[] { this });
            } catch (Exception e) {
                JadeLogger.warn(this, "*********************************************************");
                JadeLogger.warn(this, "* IJ : WARNING                                         ");
                JadeLogger.warn(this, "* Action mapping FAILED for action " + act);
                JadeLogger.warn(this, "*********************************************************");
                e.printStackTrace();
            }
        }
        return hasRight;
    }

    @Override
    public FWController createController(BISession arg0) throws Exception {
        return new FWDispatcher(arg0, IJApplication.DEFAULT_APPLICATION_IJ.toLowerCase(),
                IJApplication.APPLICATION_PREFIX);
    }

    /**
     * Register the instances responsible of the url stack management.
     * 
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     * @param aStack
     *            the stack to manage
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
        aStack.addRule(new IJNaviRules(aStack));
        aStack.addRule(new IJRuleAjoutMultiEcran());
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter(FWServlet.USER_ACTION));
        String userId = mainController.getSession().getUserId();
        String user = JadeAdminServiceLocatorProvider.getLocator().getUserService().findVisaForIdUser(userId);

        boolean hasRight = checkRights(session, request, response, mainController);

        if (hasRight) {
            IFWActionHandler handler = getActionHandler(action);
            request.setAttribute("mainServletPath", request.getServletPath());
            handler.doAction(session, request, response, mainController);
        }

        else {
            String msg = FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]", user,
                    action.getRight(), action);
            FWViewBean viewBean = new FWViewBean();
            viewBean.setMessage(msg);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            String viewName = FWDefaultServletAction.ERROR_PAGE;
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.getRequestDispatcher(viewName).forward(request, response);
        }
    }

    private IFWActionHandler getActionHandler(FWAction action) throws ServletException {
        Class<? extends IFWActionHandler> actionClass = IJMainServletAction.getActionClass(action);

        // default action if unknown
        if (actionClass == null) {
            if (JadeLogger.isDebugEnabled()) {
                JadeLogger.debug(this, "No action class mapped with action string: " + action);
            }

            actionClass = FWDefaultServletAction.class;
        }

        try {
            Constructor<? extends IFWActionHandler> cons = actionClass.getConstructor(new Class[] { FWServlet.class });

            return cons.newInstance(new Object[] { this });
        } catch (Exception e) {
            JadeLogger.error(this, "*********************************************************");
            JadeLogger.error(this, "* IJAI : WARNING");
            JadeLogger.error(this, "* Unable to create instance of action class: " + actionClass.getName());
            JadeLogger.error(this, "*********************************************************");

            throw new ServletException();
        }
    }

    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = "/" + IJApplication.APPLICATION_IJ_REP + "/";

        path = path.concat(FWServlet.HOMEPAGE + ".jsp");

        forward(path, request, response);

        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("ij-menuprincipal", "menu");
        menuBB.setCurrentMenu("ij-optionsempty", "options");
    }

    /**
     * no languages codes in path
     * 
     * @return false
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }
}
