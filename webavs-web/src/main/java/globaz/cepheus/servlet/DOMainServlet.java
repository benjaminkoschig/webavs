/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.servlet;

import globaz.cepheus.application.DOApplication;
import globaz.cepheus.stack.DONaviRules;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DOCUMENT ME!
 * 
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOMainServlet extends FWServlet {

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
     * Creates a new DOMainServlet object.
     */
    public DOMainServlet() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        return new FWDispatcher(arg0, DOApplication.DEFAULT_APPLICATION_CEPHEUS.toLowerCase(),
                DOApplication.APPLICATION_PREFIX);
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
        aStack.addRule(new DONaviRules(aStack));
        // aStack.addRule(new DORuleAjoutMultiEcran());
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
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws ServletException, IOException {

        request.setAttribute("mainServletPath", request.getServletPath());

        String action = request.getParameter(USER_ACTION);
        FWAction act = FWAction.newInstance(action);

        // String actionSuite = act.getPackagePart();
        // String actionPart = act.getActionPart();
        FWDefaultServletAction actionMapper = null;

        try {
            if (JadeLogger.isDebugEnabled()) {
                JadeLogger.debug(this, "CEPHEUS action " + act);
            }

            Class actionClass = DOMainServletAction.getActionClass(act);

            if (actionClass == null) {
                actionClass = FWDefaultServletAction.class;
            }

            Constructor c = actionClass.getConstructor(new Class[] { globaz.framework.servlets.FWServlet.class });
            actionMapper = (FWDefaultServletAction) c.newInstance(new Object[] { this });
        } catch (Exception e) {
            JadeLogger.warn(this, "*********************************************************");
            JadeLogger.warn(this, "* CEPHEUS : WARNING                                         ");
            JadeLogger.warn(this, "* Action mapping FAILED for action " + act);
            JadeLogger.warn(this, "*********************************************************");
            e.printStackTrace();
        }

        try {
            actionMapper.doAction(session, request, response, mainController);
        } catch (Exception e) {
            JadeLogger.warn(this, "*********************************************************");
            JadeLogger.warn(this, "* CEPHEUS : WARNING                                         ");
            JadeLogger.warn(this, "* Action failed  : action " + act);
            JadeLogger.warn(this, "*********************************************************");
            e.printStackTrace();
        }
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
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

}
