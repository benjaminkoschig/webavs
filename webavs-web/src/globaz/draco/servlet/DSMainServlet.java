package globaz.draco.servlet;

import globaz.draco.stack.rules.NaviRules;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.globall.api.BISession;
import globaz.globall.util.JATime;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet principal de l'application DRACO. Date de création : (29.11.2002 13:00:20)
 * 
 * @author: Sébastien Chappatte
 */
public class DSMainServlet extends FWServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur DSMainServlet.
     */
    public DSMainServlet() {
        super();
    }

    /**
     * Créer le dispatcher principal de l'application Date de création : (29.11.2002 13:00:40)
     * 
     * @return une nouvelle instance du dispatcher
     */
    @Override
    public FWController createController(BISession session) {
        // Crée un controller de type dispatcher pour l'application
        return new FWDispatcher(session, "draco", "DS");
    }

    /**
     * Méthode permettant de personnaliser une action Date de création : (06.11.2002 11:26:46)
     * 
     * @param aStack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        // pour la saisie de montants
        FWRemoveActionsEndingWith regleSaisieMasseTotauxAfficher = new FWRemoveActionsEndingWith(
                "draco.declaration.saisieMasseTotaux.afficher");
        aStack.addRule(regleSaisieMasseTotauxAfficher);
        FWRemoveActionsEndingWith regleSaisieMasseTotauxValider = new FWRemoveActionsEndingWith(
                "draco.declaration.saisieMasseTotaux.valider");
        aStack.addRule(regleSaisieMasseTotauxValider);
        // pour la saisie des dates de réception
        FWRemoveActionsEndingWith regleSaisieMasseDateAjouter = new FWRemoveActionsEndingWith(
                "draco.declaration.saisieMasseDateReception.afficher");
        aStack.addRule(regleSaisieMasseDateAjouter);
        FWRemoveActionsEndingWith regleSaisieMasseDateValider = new FWRemoveActionsEndingWith(
                "draco.declaration.saisieMasseDateReception.ajouter");
        aStack.addRule(regleSaisieMasseDateValider);

        // pour la saisie des dates de réception automatique
        FWRemoveActionsEndingWith regleSaisieAutomatiqueAjouter = new FWRemoveActionsEndingWith(
                "draco.declaration.saisieMasseAutomatique.afficher");
        aStack.addRule(regleSaisieAutomatiqueAjouter);
        FWRemoveActionsEndingWith regleSaisieAutomatiqueValider = new FWRemoveActionsEndingWith(
                "draco.declaration.saisieMasseAutomatique.ajouter");
        aStack.addRule(regleSaisieAutomatiqueValider);

        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
    }

    /**
     * point d'entrée pour le traitement des actions Date de création : (29.11.2002 13:15:00)
     */
    @Override
    public void doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("DS-MenuPrincipal", "menu");
        request.removeAttribute("mainServletPath");
        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter("userAction");
        if (action.equals("draco.ping")) {
            // OCA - 14.11.2011 - PO5912
            // voir également inscriptionsIndividuellesListe_de.jsp
            // qui demande le ping par ajax depuis la function javascript init ()
            response.getOutputStream().write((new JATime(System.currentTimeMillis()).toStr(":")).getBytes("UTF-8"));

        } else {
            String actionSuite = split(request.getParameter("userAction"), 1);
            if (actionSuite.equals("declaration") == true) {
                doActionDeclaration(session, request, response, mainController);
            } else if (actionSuite.equals("preimpression") == true) {
                doActionPreImpression(session, request, response, mainController);
            } else if (actionSuite.equals("inscriptions")) {
                doActionInscription(session, request, response, mainController);
            } else if (actionSuite.equals("listes")) {
                doActionListes(session, request, response, mainController);
            } else {
                getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE).forward(
                        request, response);
            }
        }
    }

    /**
     * Point d'entrée pour les actions concernant le package Declaration Date de création : (10.12.2002 08:57:00)
     */
    public void doActionDeclaration(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        FWDefaultServletAction act = null;

        if (actionSuite.equals("ligneDeclaration")) {
            act = new DSActionLigneDeclaration(this);
            // modif jmc 27.06.2005, utilisation des actions standards du FW
            /*
             * } else if (actionSuite.equals("validation")) { act = new DSActionValidation(this); } else if
             * (actionSuite.equals("annulerValidation")) { act = new DSActionAnnulerValidation(this);
             */
        } else if (actionSuite.equals("declaration") || actionSuite.equals("imprimerDeclaration")) {
            act = new DSActionDeclaration(this);
        } else if (actionSuite.equals("saisieMasseDateReception")) {
            act = new DSActionSaisieMasseDateReception(this);
        } else if (actionSuite.equals("saisieMasseAutomatique")) {
            act = new DSActionSaisieMasseAutomatique(this);
        } else if (actionSuite.equals("saisieMasseTotaux")) {
            act = new DSActionSaisieMasseTotaux(this);
        } else if (actionSuite.equals("attestationFiscaleLtn") || actionSuite.equals("attestationFiscaleLtnGen")) {
            act = new DSActionAttestationFiscaleLtn(this);
        } else if (actionSuite.equals("decompteImpotLtn")) {
            act = new DSActionDecompteImpotLtn(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    public void doActionInscription(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        FWDefaultServletAction act = null;
        act = new DSActionInscriptions(this);
        act.doAction(session, request, response, mainController);

    }

    public void doActionListes(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = null;
        act = new FWDefaultServletAction(this);
        act.doAction(session, request, response, mainController);

    }

    /**
     * Point d'entrée pour les actions concernant le package PreImpression Date de création : (10.12.2002 08:57:00)
     */
    public void doActionPreImpression(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        FWDefaultServletAction act = null;
        act = new DSActionPreImpression(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionSaisieMasseDeclaration(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWController mainController) throws ServletException, IOException {
        FWDefaultServletAction act = null;
        act = new DSActionSaisieMasseDateReception(this);
        act.doAction(session, request, response, mainController);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 07:41:07)
     */
    @Override
    protected void goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("DS-MenuPrincipal", "menu");
        bb.setCurrentMenu("DS-OnlyDetail", "options");
        String path = "/" + globaz.draco.application.DSApplication.DEFAULT_APPLICATION_ROOT + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/";
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /**
     * Détermine si le servlet actuel doit utiliser la langue utilisateur pour trouver les pages .jsp (genre
     * <CODE>/FR</CODE>, <CODE>/DE</CODE>, etc.).
     * 
     * @return <code>true</code> si le servlet utilise la langue utilisateur, <code>false</code> sinon.
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /* Découpe et retourne une chaîne de caractère compris entre un élément */
    public String split(String str, int pos) {
        Vector tmp = new Vector();
        try {
            StringTokenizer st = new StringTokenizer(str, ".");
            while (st.hasMoreTokens()) {
                tmp.add(st.nextToken());
            }
            return (String) tmp.elementAt(pos);
        } catch (Exception e) {
            return null;
        }
    }
}
