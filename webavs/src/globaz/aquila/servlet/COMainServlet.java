package globaz.aquila.servlet;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.rules.FWDefaultRule;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet principal de l'application aquila.
 * 
 * @author Pascal Lovy, 01-oct-2004
 */
public class COMainServlet extends FWServlet {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Map<String, Class<?>> ACTION_TO_CLASS = new HashMap<String, Class<?>>();
    private static final Class[] PARAMS_CONSTRUCTOR = new Class[] { FWServlet.class };

    private static final long serialVersionUID = -497579734877243493L;

    static {
        COMainServlet.ACTION_TO_CLASS.put("aquila", CODefaultServletAction.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch", COActionBatch.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch.etape", COActionEtape.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch.paramTaxes", COActionParamTaxes.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch.sequence", FWDefaultServletAction.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch.parametreTaxe", FWDefaultServletAction.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch.trancheTaxe", FWDefaultServletAction.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.batch.transitionEdit", COActionTransitionEdit.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.poursuite", COActionPoursuite.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.poursuite.historique", COActionHistorique.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.irrecouvrables", COActionIrrecouvrables.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.irrecouvrables.recouvrementCotisations",
                COActionRecouvrementCotisations.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.irrecouvrables.comptabilisationRecouvrement",
                COActionCompabilisationRecouvrement.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process", COActionProcessContentieux.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process.processDumpConfigEtapes", FWDefaultServletAction.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process.processCreerARD", COACtionProcessCreerARD.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process.processMuterDelai", COActionProcessMuterDelai.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process.executerJournal", COActionProcess.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process.annulerJournal", COActionProcess.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.process.validerJournal", COActionProcess.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.administrateurs", COActionAdministrateur.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.plaintes", COActionPlaintePenale.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.infos", FWDefaultServletAction.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.print", COActionImprimer.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.ard", COActionARD.class);
        COMainServlet.ACTION_TO_CLASS.put("aquila.suiviprocedure", COActionSuiviProcedure.class);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.servlets.FWServlet#createController(globaz.globall.api.BISession)
     */
    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, ICOApplication.DEFAULT_APPLICATION_AQUILA.toLowerCase(),
                COApplication.APPLICATION_AQUILA_PREFIX);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        aStack.addRule(new FWDefaultRule(aStack, ICOApplication.DEFAULT_APPLICATION_AQUILA));
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        loadSessions();

        // Met à jour le servlet en cours
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());

        // Trouve l'action
        String action = request.getParameter("userAction");
        Class servletClass = COMainServlet.ACTION_TO_CLASS.get(action);

        while (servletClass == null) {
            action = action.substring(0, action.lastIndexOf('.'));
            servletClass = COMainServlet.ACTION_TO_CLASS.get(action);
        }

        // Crée l'action
        FWDefaultServletAction servletAction = null;

        try {
            Constructor constructor = servletClass.getConstructor(COMainServlet.PARAMS_CONSTRUCTOR);

            servletAction = (FWDefaultServletAction) constructor.newInstance(new Object[] { this });
        } catch (Exception e) {
            JadeLogger.error(this, "Action incorrecte: " + request.getParameter("userAction"));
            servletAction = new CODefaultServletAction(this);
        }

        servletAction.doAction(session, request, response, mainController);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = "/" + COApplication.APPLICATION_AQUILA_ROOT + "/"
                + FWDefaultServletAction.getIdLangueIso(httpSession) + "/";

        // ajouter des choix par défaut pour les menus
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);

        menuBB.setCurrentMenu("CO-MenuPrincipal", "menu");
        menuBB.setCurrentMenu("CO-OptionsDefaut", "options");

        // rediriger vers la page
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /**
     * Charge une session OSIRIS et AQUILA afin de disposer de tout les labels.
     */
    private void loadSessions() {
        try {
            GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        } catch (Exception e) {
            // Do nothing
        }
    }

}
