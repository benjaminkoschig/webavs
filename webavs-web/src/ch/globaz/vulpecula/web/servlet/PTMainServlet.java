package ch.globaz.vulpecula.web.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.framework.utils.urls.rules.FWSuppressSameUserActions;
import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.web.application.PTActions;

/**
 * Servlet principale de l'application des postes de travail
 * 
 * @author JPA
 */
public class PTMainServlet extends FWJadeServlet {

    private static final String MENU = "vulpecula-menuprincipal";
    private static final String OPTIONS = "vulpecula-optionsempty";

    /** Noeuds du menu désactivés en mode droit "Lecture seule" */
    private static final List<String> NODSE_DISABLED_READING_MODE = new ArrayList<String>();
    static {
        NODSE_DISABLED_READING_MODE.add("RECEPTION_DECOMPTE");
        NODSE_DISABLED_READING_MODE.add("DECOMPTE_VIDE");
    }

    /**
     *
     */
    public PTMainServlet() {
        super(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA, ApplicationConstants.DEFAULT_APPLICATION_VULPECULA,
                ApplicationConstants.APPLICATION_PREFIX);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.
     * urls.FWUrlsStack)
     */
    @Override
    protected void customize(final FWUrlsStack aStack) {
        List<String> removeActions = new ArrayList<String>();
        removeActions.add("AJAX");
        removeActions.add("widget.action.jade.afficher");
        removeActions.add("widget.action.jade.download");
        removeActions.add(".lister");
        removeActions.add(".modifier");
        removeActions.add(".supprimer");
        removeActions.add(".ajouter");
        removeActions.add(".reAfficher");
        removeActions.add(".executer");
        removeActions.add("notitle.afficher");

        FWRemoveActionsEndingWith remRule = new FWRemoveActionsEndingWith(removeActions);
        aStack.addRule(remRule);
        // Evite de garder 2 fois la même page
        aStack.addRule(new FWSuppressSameUserActions());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession
     * , javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(final HttpSession httpSession, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu(MENU, "menu");
            menuBB.setCurrentMenu(OPTIONS, "options");
        }

        BSession objSession = (BSession) httpSession.getAttribute("objSession");
        // Désactivation
        for (String disabledNode : NODSE_DISABLED_READING_MODE) {
            try {
                if (!objSession.hasRight(ApplicationConstants.APPLICATION_ACTION, FWSecureConstants.UPDATE)) {
                    menuBB.setNodeActive(false, disabledNode, PTMainServlet.MENU);
                } else {
                    menuBB.setNodeActive(true, disabledNode, PTMainServlet.MENU);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        StringBuffer path = new StringBuffer("/").append(ApplicationConstants.APPLICATION_VULPECULA_REP);
        path.append("/").append("homePage.jsp");
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWJadeServlet#initializeActionMapping()
     */
    @Override
    protected void initializeActionMapping() {
        registerActionMapping(PTActions.REGISTRE, PTRegistresAction.class);
        registerActionMapping(PTActions.PARAMETRE, PTParametreAction.class);
        registerActionMapping(PTActions.PROCESS, PTProcessAction.class);
        registerActionMapping(PTActions.POSTETRAVAIL, PTPosteTravailAction.class);
        registerActionMapping(PTActions.POSTETRAVAILDETAIL, PTPosteTravailDetailAction.class);
        registerActionMapping(PTActions.POSTETRAVAILVUEGLOBALE, PTPosteTravailvuegeneraleAction.class);
        registerActionMapping(PTActions.DECOMPTE, PTDecompteAction.class);
        registerActionMapping(PTActions.AFFICHER_GED, PTAfficherGedAction.class);
        registerActionMapping(PTActions.DECOMPTEDETAIL, PTDecompteDetailAction.class);
        registerActionMapping(PTActions.DECOMPTENOUVEAU, PTDecompteNouveauAction.class);
        registerActionMapping(PTActions.DECOMPTESALAIRE, PTDecompteSalaireAction.class);
        registerActionMapping(PTActions.DECOMPTERECEPTION, FWDefaultServletAction.class);
        registerActionMapping(PTActions.TAXATION_OFFICE, PTTaxationOfficeAction.class);
        registerActionMapping(PTActions.ABSENCES_JUSTIFIEES, PTAbsencesJustifieesAction.class);
        registerActionMapping(PTActions.CONGE_PAYE, PTCongePayeAction.class);
        registerActionMapping(PTActions.SERVICE_MILITAIRE, PTServiceMilitaireAction.class);
        registerActionMapping(PTActions.LISTES, PTListesAction.class);
        registerActionMapping(PTActions.STATISTIQUES, FWDefaultServletAction.class);
        registerActionMapping(PTActions.ASSOCIATION, PTAssociationAction.class);
        registerActionMapping(PTActions.CAISSE_MALADIE, PTCaissemaladieAction.class);
        registerActionMapping(PTActions.SYNDICAT, PTSyndicatAction.class);
        registerActionMapping(PTActions.PRESTATIONS, PTPrestationsAction.class);
        registerActionMapping(PTActions.IS, FWDefaultServletAction.class);
        registerActionMapping(PTActions.COMPTABILITE, FWDefaultServletAction.class);
        registerActionMapping(PTActions.AF, PTAFAction.class);
    }
}
