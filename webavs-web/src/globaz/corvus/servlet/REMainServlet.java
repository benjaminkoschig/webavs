/*
 * Créé le 29 dec. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.servlet;

import globaz.corvus.application.REApplication;
import globaz.corvus.stack.RENaviRules;
import globaz.corvus.stack.REPasDeBackSFDepuisDRCI;
import globaz.corvus.utils.REPmtMensuel;
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
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.globall.api.BISession;
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
 * @author bsc
 */
public class REMainServlet extends FWServlet {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
            // reset le cache si nécessaire
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

                Class<?> actionClass = REMainServletAction.getActionClass(act);
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
                JadeLogger.warn(this, "* CORVUS : WARNING                                         ");
                JadeLogger.warn(this, "* Action mapping FAILED for action " + act);
                JadeLogger.warn(this, "*********************************************************");
                e.printStackTrace();
            }
        }
        return hasRight;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param arg0
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     * @see globaz.framework.servlets.FWServlet#createController(globaz.globall.api.BISession)
     */
    @Override
    public FWController createController(BISession arg0) throws Exception {
        return new FWDispatcher(arg0, REApplication.DEFAULT_APPLICATION_CORVUS.toLowerCase(),
                REApplication.APPLICATION_PREFIX);
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
        aStack.addRule(new RENaviRules(aStack));
        aStack.addRule(new REPasDeBackSFDepuisDRCI());
        // retrait de l'action des widget d'autocomplétion de la pile
        aStack.addRule(new FWRemoveActionsEndingWith(IREActions.ACTION_JADE_WIDGET + "." + FWAction.ACTION_LISTER));
        aStack.addRule(new FWRemoveActionsEndingWith(IREActions.ACTION_CALCUL_DEMANDE_RENTE + "."
                + "actionImporterScriptACOR"));
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWController)
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainController
     *            DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {

        String actionString = request.getParameter(FWServlet.USER_ACTION);
        FWAction action = FWAction.newInstance(actionString);
        String userId = mainController.getSession().getUserId();
        String user = JadeAdminServiceLocatorProvider.getLocator().getUserService().findVisaForIdUser(userId);

        boolean hasRight = checkRights(session, request, response, mainController);

        if (hasRight) {
            IFWActionHandler handler = getActionHandler(action);
            request.setAttribute("mainServletPath", request.getServletPath());
            handler.doAction(session, request, response, mainController);
        } else {
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
        Class<?> actionClass = REMainServletAction.getActionClass(action);

        // default action if unknown
        if (actionClass == null) {
            if (JadeLogger.isDebugEnabled()) {
                JadeLogger.debug(this, "No action class mapped with action string: " + action);
            }

            actionClass = FWDefaultServletAction.class;
        }

        try {
            Constructor<?> cons = actionClass.getConstructor(new Class[] { FWServlet.class });

            return (IFWActionHandler) cons.newInstance(new Object[] { this });
        } catch (Exception e) {
            JadeLogger.error(this, "*********************************************************");
            JadeLogger.error(this, "* CORVUS : WARNING");
            JadeLogger.error(this, "* Unable to create instance of action class: " + actionClass.getName());
            JadeLogger.error(this, "*********************************************************");

            throw new ServletException();
        }
    }

    /**
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("corvus-menuprincipal", "menu");
        menuBB.setCurrentMenu("corvus-optionsempty", "options");

        globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) httpSession
                .getAttribute("objController");
        globaz.globall.db.BSession bSession = (globaz.globall.db.BSession) controller.getSession();

        if (!REPmtMensuel.isValidationDecisionAuthorise(bSession)) {
            menuBB.setNodeActive(true, "RE-activerValidationDecision", "corvus-menuprincipal");
            menuBB.setNodeActive(false, "RE-desactiverValidationDecision", "corvus-menuprincipal");
        } else {
            menuBB.setNodeActive(false, "RE-activerValidationDecision", "corvus-menuprincipal");
            menuBB.setNodeActive(true, "RE-desactiverValidationDecision", "corvus-menuprincipal");
        }

        String path = "/" + REApplication.APPLICATION_CORVUS_REP + "/";
        path = path.concat(FWServlet.HOMEPAGE + ".jsp");
        forward(path, request, response);

    }

    /**
     * no languages codes in path
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     * @return false
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }
}
