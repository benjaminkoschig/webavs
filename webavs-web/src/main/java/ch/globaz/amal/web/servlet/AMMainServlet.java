package ch.globaz.amal.web.servlet;

import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.fx.common.application.servlet.NaviRules;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.web.application.AMApplication;

public class AMMainServlet extends FWJadeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AMMainServlet() {
        super(AMApplication.DEFAULT_APPLICATION_AMAL, AMApplication.DEFAULT_APPLICATION_AMAL,
                AMApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {
        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        // supprime les actions interdites de la pile
        List<String> listeInterdits = new ArrayList<String>();
        listeInterdits.add(".afficherAJAX");
        listeInterdits.add(".listerAJAX");
        listeInterdits.add(".supprimerAJAX");
        listeInterdits.add(".modifierAJAX");
        listeInterdits.add("widget.action.jade.afficher");
        listeInterdits.add(".lister");
        listeInterdits.add(".contribuableTaxations.afficher");
        listeInterdits.add(".contribuableRevenu.afficher");
        listeInterdits.add(".contribuableFamille.afficher");
        listeInterdits.add(".download");
        listeInterdits.add(".choisir");
        listeInterdits.add("contribuableComplexAnnonceSedex.afficher");

        FWRemoveActionsEndingWith remRule = new FWRemoveActionsEndingWith(listeInterdits);
        aStack.addRule(remRule);
    }

    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu("amal-menuprincipal", "menu");
            menuBB.setCurrentMenu("amal-optionsempty", "options");
        }
        StringBuffer path = new StringBuffer("/").append(AMApplication.APPLICATION_AMAL_REP);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    @Override
    protected void initializeActionMapping() {
        registerActionMapping("amal.contribuable", AMContribuableServletAction.class);
        registerActionMapping("amal.famille", AMFamilleServletAction.class);
        registerActionMapping("amal.detailfamille", AMDetailFamilleServletAction.class);
        registerActionMapping("amal.formule", AMFormuleServletAction.class);
        registerActionMapping("amal.revenu", AMRevenuServletAction.class);
        registerActionMapping("amal.revenuHistorique", AMRevenuHistoriqueServletAction.class);
        registerActionMapping("amal.envoi", AMFormuleServletAction.class);
        registerActionMapping("amal.subsideannee", AMSubsideAnneeServletAction.class);
        registerActionMapping("amal.primemoyenne", AMPrimeMoyenneServletAction.class);
        registerActionMapping("amal.primeavantageuse", AMPrimeAvantageuseServletAction.class);
        registerActionMapping("amal.parametreapplication", AMParametreApplicationServletAction.class);
        registerActionMapping("amal.parametremodel", AMParametreModelServletAction.class);
        registerActionMapping("amal.parametreannuel", AMParametreAnnuelServletAction.class);
        registerActionMapping("amal.controleurEnvoi", AMControleurEnvoiServletAction.class);
        registerActionMapping("amal.controleurRappel", AMControleurRappelServletAction.class);
        registerActionMapping("amal.ajax", AMAbstractServletAction.class);
        registerActionMapping("amal.reprise", AMRepriseServletAction.class);
        registerActionMapping("amal.copyparametre", AMCopyParametreServletAction.class);
        registerActionMapping("amal.deductionsfiscalesenfants", AMDeductionsFiscalesEnfantsServletAction.class);
        registerActionMapping("amal.ged", AMGedServletAction.class);
        registerActionMapping("amal.process", AMProcessServletAction.class);
        registerActionMapping("amal.documents", AMDocumentsServletAction.class);
        registerActionMapping("amal.caissemaladie", AMCaisseMaladieServletAction.class);
        registerActionMapping("amal.primesassurance", AMPrimesAssuranceServletAction.class);
        registerActionMapping("amal.annoncesassurance", AMPrimesAssuranceServletAction.class);
        registerActionMapping("amal.statistiques", AMStatistiquesServletAction.class);
        registerActionMapping("amal.sedexrp", AMSedexRPServletAction.class);
        registerActionMapping("amal.sedexco", AMSedexCOServletAction.class);
    }

}
