package globaz.apg.servlet;

import globaz.apg.application.APApplication;
import globaz.apg.stack.APNaviRules;
import globaz.apg.stack.APRuleAjoutMultiEcran;
import globaz.apg.util.TypePrestation;
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
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * DOCUMENT ME!
 * 
 * @author scr Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class APMainServlet extends FWServlet {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String PARAM_TYPE_PRESTATION = "typePrestation";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new APMainServlet object.
     */
    public APMainServlet() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private TypePrestation changerTypePrestation(HttpSession session, String typePrestationParam) {
        TypePrestation typePrestation = TypePrestation.typePrestationInstanceFor(typePrestationParam);

        if (typePrestation.isConnu()) {
            // si le type de prestation est connu, on stocke le code systeme
            // dans la session.
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION,
                    typePrestation.toCodeSysteme());
        }

        return typePrestation;
    }

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

                Class<?> actionClass = APMainServletAction.getActionClass(act);
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
                JadeLogger.warn(this, "* APG : WARNING                                         ");
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
        return new FWDispatcher(arg0, APApplication.DEFAULT_APPLICATION_APG.toLowerCase(),
                APApplication.APPLICATION_PREFIX);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aStack
     *            DOCUMENT ME!
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.setAutoexec(true);
        aStack.addRule(new APNaviRules(aStack));
        aStack.addRule(new APRuleAjoutMultiEcran());
    }

    /**
     * DOCUMENT ME!
     * 
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
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws ServletException, IOException {

        if (request.getParameter(APMainServlet.PARAM_TYPE_PRESTATION) != null) {
            // si le type de prestation est présent dans la query string de la
            // requête, changer le type de prestation
            changerTypePrestation(session, request.getParameter(APMainServlet.PARAM_TYPE_PRESTATION));
        }

        request.setAttribute("mainServletPath", request.getServletPath());

        String actionString = request.getParameter(FWServlet.USER_ACTION);
        FWAction action = FWAction.newInstance(actionString);
        String userId = mainController.getSession().getUserId();
        String user = JadeAdminServiceLocatorProvider.getLocator().getUserService().findVisaForIdUser(userId);

        boolean hasRight = checkRights(session, request, response, mainController);

        if (hasRight) {

            FWDefaultServletAction actionMapper = null;
            try {
                if (JadeLogger.isDebugEnabled()) {
                    JadeLogger.debug(this, "APG action " + action);
                }

                Class<?> actionClass = APMainServletAction.getActionClass(action);
                if (actionClass == null) {
                    actionClass = FWDefaultServletAction.class;
                }

                Constructor<?> c = actionClass
                        .getConstructor(new Class[] { globaz.framework.servlets.FWServlet.class });
                actionMapper = (FWDefaultServletAction) c.newInstance(new Object[] { this });
            } catch (Exception e) {
                JadeLogger.warn(this, "*********************************************************");
                JadeLogger.warn(this, "* APG : WARNING                                         ");
                JadeLogger.warn(this, "* Action mapping FAILED for action " + action);
                JadeLogger.warn(this, "*********************************************************");
                e.printStackTrace();
            }

            try {
                actionMapper.doAction(session, request, response, mainController);
            } catch (Exception e) {
                JadeLogger.warn(this, "*********************************************************");
                JadeLogger.warn(this, "* APG : WARNING                                         ");
                JadeLogger.warn(this, "* Action failed  : action " + action);
                JadeLogger.warn(this, "*********************************************************");
                e.printStackTrace();
            }
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

    /**
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = "/" + APApplication.APPLICATION_APG_REP + "/";
        String typePrestationParam = request.getParameter(APMainServlet.PARAM_TYPE_PRESTATION);
        TypePrestation typePrestation = null;

        if (JadeStringUtil.isEmpty(typePrestationParam)) {
            // si le parametre typePrestation n'est pas transmis on charge celui
            // stocké dans la session (s'il existe)
            typePrestation = TypePrestation.typePrestationInstanceForCS((String) PRSessionDataContainerHelper.getData(
                    httpSession, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION));
        } else {
            // sinon on stocke le nouveau dans la session
            typePrestation = changerTypePrestation(httpSession, typePrestationParam);
        }

        if (typePrestation.isConnu()) {
            // si le type de prestation est valide, on redirigie vers la
            // homepage
            path = path.concat(FWServlet.HOMEPAGE + ".jsp");
        } else {
            // sinon on redemande le type de prestation à l'utilisateur
            path = "/" + FWServlet.JSP_CHOOSE_APP;
        }

        forward(path, request, response);

        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("ap-menuprincipalapg", "menu");
        menuBB.setCurrentMenu("ap-optionsempty", "options");

    }

    /**
     * @see globaz.framework.servlets.FWServlet#goProcessPage(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goProcessPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.setAttribute("mainServletPath", request.getServletPath());
        super.goProcessPage(request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }
}
