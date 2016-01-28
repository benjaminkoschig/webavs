package ch.globaz.perseus.web.servlet;

import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.framework.utils.urls.rules.FWSuppressSameUserActions;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.web.application.PFApplication;

/**
 * Servlet principale de l'application de gestion des prestations familiales
 * 
 * @author vyj
 */
public class PFMainServlet extends FWJadeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PFMainServlet() {
        super(PFApplication.DEFAULT_APPLICATION_PERSEUS, PFApplication.DEFAULT_APPLICATION_PERSEUS,
                PFApplication.APPLICATION_PREFIX);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils.urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        FWRemoveActionsEndingWith removeLister = new FWRemoveActionsEndingWith(".lister");
        FWRemoveActionsEndingWith removeMotifier = new FWRemoveActionsEndingWith(".modifier");
        FWRemoveActionsEndingWith removeSupprimer = new FWRemoveActionsEndingWith(".supprimer");
        FWRemoveActionsEndingWith removeAjax = new FWRemoveActionsEndingWith("AJAX");
        FWRemoveActionsEndingWith removePlanCalcul = new FWRemoveActionsEndingWith(".planCalcul.afficher");
        FWRemoveActionsEndingWith removeLienLocaliteAfficher = new FWRemoveActionsEndingWith(".lienLocalite.afficher");
        FWRemoveActionsEndingWith removeSimpleZoneAfficher = new FWRemoveActionsEndingWith(".simpleZone.afficher");
        // FWRemoveActionsEndingWith removeLoyerAfficher = new FWRemoveActionsEndingWith(".loyer.afficher");
        FWRemoveActionsEndingWith removeOrdreVersementAfficher = new FWRemoveActionsEndingWith(
                "ordreVersement.afficher");
        FWRemoveActionsEndingWith removeWidget = new FWRemoveActionsEndingWith("widget.action.jade.afficher");
        FWRemoveActionsEndingWith removeCalculer = new FWRemoveActionsEndingWith("calcul.calculer");
        aStack.addRule(removeLister);
        aStack.addRule(removeMotifier);
        aStack.addRule(removeSupprimer);
        aStack.addRule(removeAjax);
        aStack.addRule(removePlanCalcul);
        aStack.addRule(removeLienLocaliteAfficher);
        aStack.addRule(removeSimpleZoneAfficher);
        // aStack.addRule(removeLoyerAfficher);
        aStack.addRule(removeOrdreVersementAfficher);
        aStack.addRule(removeWidget);
        aStack.addRule(removeCalculer);
        // Evite de garder 2 fois la même page
        aStack.addRule(new FWSuppressSameUserActions());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu("perseus-menuprincipal", "menu");
            menuBB.setCurrentMenu("perseus-optionsempty", "options");
        }

        globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) httpSession
                .getAttribute("objController");
        globaz.globall.db.BSession bSession = (globaz.globall.db.BSession) controller.getSession();

        if (PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise(bSession)) {
            menuBB.setNodeActive(false, "PF-activerValidationDecision", "perseus-menuprincipal");
            menuBB.setNodeActive(true, "PF-desactiverValidationDecision", "perseus-menuprincipal");
        } else {
            menuBB.setNodeActive(true, "PF-activerValidationDecision", "perseus-menuprincipal");
            menuBB.setNodeActive(false, "PF-desactiverValidationDecision", "perseus-menuprincipal");
        }

        StringBuffer path = new StringBuffer("/").append(PFApplication.APPLICATION_PERSEUS_REP);
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
        registerActionMapping("perseus.dossier", PFDossierAction.class);
        registerActionMapping("perseus.demande", PFDemandeAction.class);
        registerActionMapping("perseus.variablemetier", PFVariableMetierAction.class);
        registerActionMapping("perseus.donneesfinancieres", PFDonneesFinancieresAction.class);
        registerActionMapping("perseus.situationfamille", PFSituationFamilleAction.class);
        registerActionMapping("perseus.parametres", PFParametresAction.class);
        registerActionMapping("perseus.pcfaccordee", PFPCFAccordeeAction.class);
        registerActionMapping("perseus.echeance", PFEcheanceLibreAction.class);
        registerActionMapping("perseus.decision", PFDecisionAction.class);
        registerActionMapping("perseus.lot", PFLotAction.class);
        registerActionMapping("perseus.qd", PFQdAction.class);
        registerActionMapping("perseus.creancier", PFCreancierAction.class);
        registerActionMapping("perseus.paiements", PFPaiementsAction.class);
        registerActionMapping("perseus.rentepont", PFRentePontAction.class);
        registerActionMapping("perseus.statistiques", PFStatistiquesAction.class);
        registerActionMapping("perseus.impotsource", PFImpotSourceAction.class);
        registerActionMapping("perseus.attestationsfiscalesRP", PFAttestationsFiscalesRPAction.class);
        registerActionMapping("perseus.attestationsfiscales", PFAttestationsFiscalesAction.class);
        registerActionMapping("perseus.retenue", PFRetenueAction.class);
        registerActionMapping("perseus.traitements", PFTraitementsAction.class);
        registerActionMapping("perseus.revisiondossier", PFRevisionDossierAction.class);
        registerActionMapping("perseus.informationfacture", PFInformationFactureAction.class);
        registerActionMapping("perseus.process", PFProcessServletAction.class);
    }
}
