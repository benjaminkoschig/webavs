package globaz.helios.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.stack.rules.NaviRules;
import globaz.helios.stack.rules.RuleEnteteEcritureMasse;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class pour le servlet principal de l'application Helios Date de création : (14.08.2002 14:11:09)
 * 
 * @author: oca
 */
public class CGMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String USERACTION = "userAction";

    private HashSet actionsAvailableWithoutExerciceComptable = new HashSet();

    /**
     * Commentaire relatif au constructeur CGMainServlet.
     */
    public CGMainServlet() {
        super();

    }

    /**
     * Creation du controller principal
     * 
     * @return globaz.framework.controller.FWController
     */
    @Override
    public FWController createController(BISession session) {
        // cree un controller de type dispatcher pour l'application
        return new FWDispatcher(session, "helios", "CG");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 11:26:46)
     * 
     * @param aStack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        // aStack.setAutoexec(false);
        aStack.addRule(new RuleEnteteEcritureMasse(aStack));
    }

    /**
     * point d'entrée pour le traitement des actions Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("CG-MenuPrincipal", "menu");

        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter(CGMainServlet.USERACTION);
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 1);
        CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

        /*
         * Traitement de l'action si on a bien l'exercice comptable et le mandat en session, ou alors si l'action ne
         * requiert pas ces deux objet en session.
         */
        if ((exerciceComptable != null) || (action == null) || (action.equals(""))
                || actionsAvailableWithoutExerciceComptable.contains(action)) {
            if (actionSuite == null) {
                return;
            } else if (actionSuite.equals("avs")) {
                doActionAVS(session, request, response, mainController);
            } else if (actionSuite.equals("classifications")) {
                doActionClassifications(session, request, response, mainController);
            } else if (actionSuite.equals("comptes")) {
                doActionComptes(session, request, response, mainController);
            } else if (actionSuite.equals("consolidation")) {
                doActionConsolidation(session, request, response, mainController);
            } else if (actionSuite.equals("modeles")) {
                doActionModelesEcritures(session, request, response, mainController);
            } else if (actionSuite.equals("mapping")) {
                CGMappingComptabiliserAction act = new CGMappingComptabiliserAction(this);
                act.doAction(session, request, response, mainController);
            } else if (actionSuite.equals("process")) {
                CGActionProcess act = new CGActionProcess(this);
                act.doAction(session, request, response, mainController);
            } else if (actionSuite.equals("print")) {
                CGActionImprimer act = new CGActionImprimer(this);
                act.doAction(session, request, response, mainController);
            } else if (actionSuite.equals("rente")) {
                doActionRente(session, request, response, mainController);
            } else if (actionSuite.equals("special")) {
                CGActionSpecial act = new CGActionSpecial(this);
                act.doAction(session, request, response, mainController);
            } else if (actionSuite.equals("ecritures")) {
                CGActionGestionEcritures act = new CGActionGestionEcritures(this);
                act.doAction(session, request, response, mainController);
            } else if (actionSuite.equals("parammodeles")) {
                CGActionGestionModeles act = new CGActionGestionModeles(this);
                act.doAction(session, request, response, mainController);
            } else {
                getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE).forward(
                        request, response);
            }
        } else {
            // sinon, on doit d'abbord selectioner mandat/exercice comptable
            session.removeAttribute(CGNeedExerciceComptable.SESSION_DESTINATION);
            session.setAttribute(CGNeedExerciceComptable.SESSION_DESTINATION,
                    request.getServletPath() + "?" + request.getQueryString());

            /*
             * passe aussi l'url de destination pour que l'ecran rc puisse afficher un messge a l'utilisateur dans ce
             * cas.
             */
            getServletContext().getRequestDispatcher(
                    "/helios?userAction=helios.comptes.exerciceComptable.chercher&"
                            + CGNeedExerciceComptable.SESSION_DESTINATION + "=" + request.getRequestURI() + "?"
                            + request.getQueryString()).forward(request, response);
        }
    }

    /**
     * Point d'entrée pour les actions concernant le package AVS Date de création : (03.05.2002 08:57:00)
     */
    public void doActionAVS(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 2);
        FWDefaultServletAction act;
        if (actionSuite.equals("secteurAVS")) {
            act = new CGActionSecteurAVS(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * Point d'entrée pour les actions concernant le package 'classifications' Date de création : (03.05.2002 08:57:00)
     */
    public void doActionClassifications(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 2);
        FWDefaultServletAction act = null;

        if (actionSuite.equals("classeCompte")) {
            act = new CGActionClasseCompte(this);
        } else if (actionSuite.equals("liaisonCompteClasse")) {
            act = new CGActionLiaisonCompteClasse(this);
        } else {
            act = new FWDefaultServletAction(this);
        }

        act.doAction(session, request, response, mainController);
    }

    /**
     * Point d'entrée pour les actions concernant le package 'comptes' Date de création : (03.05.2002 08:57:00)
     */
    public void doActionComptes(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 2);
        FWDefaultServletAction act = null;

        if (actionSuite.equals(CGActionExerciceComptable.EXERCICE_COMPTABLE_PREFIX)) {
            act = new CGActionExerciceComptable(this);
        } else if (actionSuite.equals("mandat")) {
            act = new CGActionMandat(this);
        } else if (actionSuite.equals(CGActionJournal.JOURNAL_PREFIX)) {
            act = new CGActionJournal(this);
        } else if (actionSuite.equals("planComptable")) {
            act = new CGActionPlanComptable(this);
        } else if (actionSuite.equals(CGActionPeriodeComptable.PERIODE_COMPTABLE_PREFIX)) {
            act = new CGActionPeriodeComptable(this);
        } else if (actionSuite.equals("ecriture")) {
            act = new CGActionEcriture(this);
        } else if (actionSuite.equals("mouvementCompte")) {
            act = new CGActionMouvementCompte(this);
        } else if (actionSuite.equals("bilan")) {
            act = new CGActionBilan(this);
        } else if (actionSuite.equals("comptePertesProfits")) {
            act = new CGActionComptePertesProfits(this);
        } else if (actionSuite.equals("centreCharge")) {
            act = new CGActionCentreCharge(this);
        } else if (actionSuite.equals("soldesDesComptes")) {
            act = new CGActionSoldesDesComptes(this);
        } else if (actionSuite.equals("libelleStandard")) {
            act = new CGActionLibelleStandard(this);
        } else if (actionSuite.equals("analyseBudgetaire")) {
            act = new CGActionAnalyseBudgetaire(this);
        } else if (actionSuite.equals("ecritureSeeker")) {
            act = new CGActionEcritureSeeker(this);
        } else if (actionSuite.equals("ecritureModele")) {
            act = new CGActionEcritureModele(this);
        } else if (actionSuite.indexOf("imprimer") > -1) {
            act = new CGActionImprimer(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * Point d'entrée pour les actions concernant le package 'consolidation' Date de création : (03.05.2002 08:57:00)
     */
    public void doActionConsolidation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 2);

        if (actionSuite.equals("succursale")) {
            FWDefaultServletAction act = new FWDefaultServletAction(this);
            act.doAction(session, request, response, mainController);
        } else {
            CGActionProcess act = new CGActionProcess(this);
            act.doAction(session, request, response, mainController);
        }
    }

    /**
     * Point d'entrée pour les actions concernant le package 'modeles.ecritures' Date de création : (03.05.2002
     * 08:57:00)
     */
    public void doActionModelesEcritures(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 2);
        FWDefaultServletAction act = null;

        if (actionSuite.equals("modeleEcriture")) {
            act = new CGActionModeleEcriture(this);
        } else if (actionSuite.equals("ligneModeleEcriture")) {
            act = new CGActionLigneModeleEcriture(this);
        } else {
            act = new FWDefaultServletAction(this);
        }

        act.doAction(session, request, response, mainController);
    }

    /**
     * Point d'entrée pour les actions concernant le package AVS Date de création : (03.05.2002 08:57:00)
     */
    public void doActionRente(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = split(request.getParameter(CGMainServlet.USERACTION), 2);
        FWDefaultServletAction act;
        if (actionSuite.equals("recapitulation")) {
            act = new CGActionRente(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 07:41:07)
     */
    @Override
    protected void goHomePage(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("CG-MenuPrincipal", "menu");
        bb.setCurrentMenu("CG-OnlyDetail", "options");

        String path = "/" + globaz.helios.application.CGApplication.APPLICATION_HELIOS_REP + "/"
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

    /**
     * Pour les initialisation lors de demarage du servlet. <br/>
     * initialisation de la liste des action autorisée sans avoir d'exercice comptable en session
     */
    @Override
    public void init() {
        initActionsAvailableWithoutExerciceComptable();
    }

    /**
     * Dans cette application (Helios), certaines pages requierent un objet de type CGExerciceComptable en session pour
     * être fonctionelles. D'autre n'ont pas besoin ce cet objet. Cette methode permet de lister les pages, ou plutot
     * les actions qui PEUVENT être accéder SANS avoir l'objet CGExerciceComptable en session
     */
    private void initActionsAvailableWithoutExerciceComptable() {
        String[] defaultActions = { FWAction.ACTION_CHERCHER, FWAction.ACTION_LISTER, FWAction.ACTION_AFFICHER,
                FWAction.ACTION_REAFFICHER, FWAction.ACTION_MODIFIER, FWAction.ACTION_SUPPRIMER,
                FWAction.ACTION_AJOUTER, FWAction.ACTION_NOUVEAU, FWAction.ACTION_CHOISIR,
                FWAction.ACTION_SELECTIONNER, FWAction.ACTION_SUIVANT, "precedent", FWAction.ACTION_PRECEDANT };

        for (int i = 0; i < defaultActions.length; i++) {
            // authorize toutes les actions par défaut pour :
            actionsAvailableWithoutExerciceComptable.add("helios.comptes.mandat." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.comptes.exerciceComptable." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.avs.secteurAVS." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.comptes.libelleStandard." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.classifications.classification." + defaultActions[i]);

            actionsAvailableWithoutExerciceComptable.add("helios.mapping.mappingComptabiliser." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.modeles.modeleEcriture." + defaultActions[i]);

            actionsAvailableWithoutExerciceComptable.add("helios.modeles.ligneModeleEcriture." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.modeles.enteteModeleEcriture." + defaultActions[i]);
            actionsAvailableWithoutExerciceComptable.add("helios.modeles.ligneModeleEcritureCollective."
                    + defaultActions[i]);

            actionsAvailableWithoutExerciceComptable.add("helios.consolidation.succursale." + defaultActions[i]);

            actionsAvailableWithoutExerciceComptable.add("helios.parammodeles.gestionModele." + defaultActions[i]);
        }

        actionsAvailableWithoutExerciceComptable.add("helios.comptes.ecritureModele.afficher");
        actionsAvailableWithoutExerciceComptable.add("helios.comptes.ecritureModele.chercher");
        actionsAvailableWithoutExerciceComptable.add("helios.comptes.ecritureModele.executer");

        actionsAvailableWithoutExerciceComptable.add("helios.mapping.mappingComptabiliser.afficherSetIdMandat");

        actionsAvailableWithoutExerciceComptable.add("helios.comptes.mandat.ajouterMandat");
        actionsAvailableWithoutExerciceComptable.add("helios.avs.secteurAVS.ajouterSecteur");

        actionsAvailableWithoutExerciceComptable.add("helios.comptes.ecritureMultiSociete.afficher");

        actionsAvailableWithoutExerciceComptable.add("helios.special.journal.afficher");

        // Récapitulation des rentes
        actionsAvailableWithoutExerciceComptable.add("helios.rente.recapitulation");

        // pour le choix de l'exercice comptable a mettre en session
        actionsAvailableWithoutExerciceComptable.add("helios.comptes.exerciceComptable.choisirExercice");
        // Home page
        actionsAvailableWithoutExerciceComptable.add("homePage");
    }

    /**
     * Découpe et retourne une chaîne de caractère compris entre un élément
     * 
     * @param str
     * @param pos
     * @return
     */
    public String split(String str, int pos) {
        Vector tmp = new Vector();
        try {
            StringTokenizer st = new StringTokenizer(str, ".");
            while (st.hasMoreTokens()) {
                tmp.add(st.nextToken());
            }
            return (String) tmp.elementAt(pos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
