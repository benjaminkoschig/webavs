package globaz.lynx.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.helios.application.CGApplication;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.lynx.application.LXApplication;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.lynx.servlet.canevas.LXCanevasAction;
import globaz.lynx.servlet.escompte.LXEscompteAction;
import globaz.lynx.servlet.extourne.LXExtourneAction;
import globaz.lynx.servlet.facture.LXFactureAction;
import globaz.lynx.servlet.fournisseur.LXFournisseurAction;
import globaz.lynx.servlet.impression.LXImpressionAction;
import globaz.lynx.servlet.journal.LXJournalAction;
import globaz.lynx.servlet.notedecredit.LXNoteDeCreditAction;
import globaz.lynx.servlet.ordregroupe.LXOrdreGroupeAction;
import globaz.lynx.servlet.paiement.LXPaiementAction;
import globaz.lynx.servlet.recherche.LXRechercheDetailAction;
import globaz.lynx.servlet.societedebitrice.LXSocieteDebitriceAction;
import globaz.lynx.servlet.stack.rules.LXNaviRules;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see FWServlet#createController(BISession)
     */
    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase(), "LX");
    }

    /**
     * @see FWServlet#customize(FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack stack) {
        LXNaviRules rule = new LXNaviRules(stack);
        stack.addRule(rule);

    }

    /**
     * @see FWServlet#doAction(HttpSession, HttpServletRequest, HttpServletResponse, FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        loadSessions();
        setMenuParameters(session);

        request.setAttribute("mainServletPath", request.getServletPath());

        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String actionSuite = action.getPackagePart();
        // String actionClass = action.getClassPart();
        FWDefaultServletAction act = null;

        if (actionSuite == null) {
            return;
        } else if (actionSuite.equals("fournisseur")) {
            act = new LXFournisseurAction(this);
        } else if (actionSuite.equals("societesdebitrice")) {
            act = new LXSocieteDebitriceAction(this);
        } else if (actionSuite.equals("journal")) {
            act = new LXJournalAction(this);
        } else if (actionSuite.equals("ordregroupe")) {
            act = new LXOrdreGroupeAction(this);
        } else if (actionSuite.equals("facture")) {
            act = new LXFactureAction(this);
        } else if (actionSuite.equals("paiement")) {
            act = new LXPaiementAction(this);
        } else if (actionSuite.equals("escompte")) {
            act = new LXEscompteAction(this);
        } else if (actionSuite.equals("notedecredit")) {
            act = new LXNoteDeCreditAction(this);
        } else if (actionSuite.equals("impression")) {
            act = new LXImpressionAction(this);
        } else if (actionSuite.equals("recherche")) {
            act = new LXRechercheDetailAction(this);
        } else if (actionSuite.equals("extourne")) {
            act = new LXExtourneAction(this);
        } else if (actionSuite.equals("canevas")) {
            act = new LXCanevasAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }

        act.doAction(session, request, response, mainController);
    }

    /**
     * @see FWServlet#goHomePage(HttpSession, HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("LX-MenuPrincipal", "menu");
        bb.setCurrentMenu("LX-OnlyDetail", "options");

        if (JadeGedFacade.isInstalled()) {
            bb.setNodeActive(true, "lynx_ged", "LX-Fournisseur");
        } else {
            FWMenuBlackBox.ensureNodeDoesntExist("lynx_ged", "LX-Fournisseur");
        }

        String path = "/" + LXApplication.DEFAULT_LYNX_ROOT + "/" + FWDefaultServletAction.getIdLangueIso(session)
                + "/";
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /**
     * @see FWServlet#hasLanguageInPagesPath()
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
            GlobazSystem.getApplication(LXApplication.DEFAULT_APPLICATION_LYNX);
            GlobazSystem.getApplication(CGApplication.DEFAULT_APPLICATION_HELIOS);
        } catch (Exception e) {
            // Do nothing
        }
    }

    /**
     * Initialise les paramètres pour le point de menu GED du fournisseur.
     * 
     * @param menuBB
     */
    private void setMenuGedParameters(FWMenuBlackBox menuBB) {
        if (JadeGedFacade.isInstalled()) {
            try {
                BIApplication lynxApplication = GlobazSystem.getApplication(LXApplication.DEFAULT_APPLICATION_LYNX);
                String gedFolderType = lynxApplication.getProperty("ged.folder.type", "");
                String gedServiceName = lynxApplication.getProperty("ged.servicename.id", "");

                menuBB.setActionParameter("serviceNameId", gedServiceName, "lynx_ged", "LX-Fournisseur");
                menuBB.setActionParameter("gedFolderType", gedFolderType, "lynx_ged", "LX-Fournisseur");
                menuBB.setActionParameter("idRole", LXTiersService.ROLE_FOURNISSEUR, "lynx_ged", "LX-Fournisseur");

            } catch (Exception e) {
                JadeLogger.error(LXMainServlet.class.getName() + "_setMenuGedParameters()", e.toString());
            }
        }
    }

    /**
     * Set menu to lynx menu.
     * 
     * @param session
     */
    private void setMenuParameters(HttpSession session) {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("LX-MenuPrincipal", "menu");

        setMenuGedParameters(menuBB);
    }
}
