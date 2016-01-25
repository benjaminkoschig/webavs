package globaz.phenix.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.servlet.communications.CPActionApercuCommunicationFiscaleRetour;
import globaz.phenix.servlet.communications.CPActionCommunicationFiscaleAffichage;
import globaz.phenix.servlet.communications.CPActionJournalRetour;
import globaz.phenix.servlet.communications.CPActionParametrePlausibilite;
import globaz.phenix.servlet.communications.CPActionReglePlausibilite;
import globaz.phenix.servlet.communications.CPActionRejets;
import globaz.phenix.servlet.communications.CPActionTraitementAnomalies;
import globaz.phenix.servlet.communications.CPActionValidationJournalRetour;
import globaz.phenix.stack.rules.NaviRules;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.Constante;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (08.05.2002 14:03:28)
 * 
 * @author: Administrator
 */
public class CPMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String PARAM_DOC = "doc";
    public static final String PARAM_WANT_SAVE_DLG = "wantOpenSaveDialog";

    /**
     * Commentaire relatif au constructeur TIMainServlet.
     */
    public CPMainServlet() {
        super();
    }

    private void _showDocument(String filename, boolean wantDlg, HttpServletResponse response) throws IOException {

        if (filename == null) {
            // ne devrait jamais arriver...
            response.getOutputStream().write("No document found.".getBytes("UTF-8"));
            return;
        }
        String path = Jade.getInstance().getHomeDir();
        // Article intéressant sur
        // http://www.javaworld.com/javaworld/javatips/jw-javatip94.html
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");

        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");

        // seulement si on veut ouvrir le doc dans acrobat lui-même, ou
        // enregistrer le fichier
        if (wantDlg) {
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path + "/persistence/" + filename));
        BufferedOutputStream bos = new BufferedOutputStream(out); // out
        try {
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } finally {
            bos.close();
            bis.close();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    public boolean checkOptionAllowed(String selectedId, HttpSession session, HttpServletRequest request) {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            String option = action.getActionPart();
            String action1 = action.getClassPart();
            // options à monitorer : calculer, imprimer, devalider,
            // supprimer,modifier
            if (!JadeStringUtil.isEmpty(selectedId)
                    && (Arrays.asList(Constante.OPTIONS_CHECK).contains(option) || "decisionValider"
                            .equalsIgnoreCase(action1))) { // on vérifie
                if (("decision".equalsIgnoreCase(action1) || "decisionValider".equalsIgnoreCase(action1))
                        && !"chercher".equalsIgnoreCase(option)) {
                    CPDecision decision = new CPDecision();
                    decision.setSession(CodeSystem.getSession(session));
                    decision.setIdDecision(selectedId);
                    decision.retrieve();
                    if (!decision.hasErrors()) {
                        if (CPDecision.CS_CALCUL.equalsIgnoreCase(decision.getDernierEtat())) {
                            // option autorisées : supprimer,calculer,imprimer,
                            // valider
                            // option interdite : dévalider, duplicata
                            // option autorisées calcul et modifier:
                            if (option.equalsIgnoreCase(Constante.OPTION_DEVALIDER)
                                    || option.equalsIgnoreCase(Constante.OPTION_DUPLICATA)) {
                                return false;
                            } else {
                                return true;
                            }
                        } else if (decision.getDernierEtat().equalsIgnoreCase(CPDecision.CS_CREATION)) {
                            // option autorisées calcul et modifier:
                            if (option.equalsIgnoreCase(Constante.OPTION_MODIFIER)
                                    || option.equalsIgnoreCase(Constante.OPTION_CALCULER)
                                    || option.equalsIgnoreCase(Constante.OPTION_SUPPRIMER)
                                    || option.equalsIgnoreCase(Constante.OPTION_SUPPRIMERDIRECT)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if (CPDecision.CS_FACTURATION.equalsIgnoreCase(decision.getDernierEtat())
                                || CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(decision.getDernierEtat())) {
                            return (option.equalsIgnoreCase(Constante.OPTION_DUPLICATA));
                        } else if (decision.getDernierEtat().equalsIgnoreCase(CPDecision.CS_REPRISE)) {
                            // option autorisées : aucune
                            return false;
                        } else if (CPDecision.CS_SORTIE.equalsIgnoreCase(decision.getDernierEtat())) {
                            // option autorisées : aucune
                            return false;
                        } else if (CPDecision.CS_VALIDATION.equalsIgnoreCase(decision.getDernierEtat())) {
                            // option autorisées : dévalidation
                            if (option.equalsIgnoreCase(Constante.OPTION_DEVALIDER)
                                    || option.equalsIgnoreCase(Constante.OPTION_DUPLICATA)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else { // decision en erreur, par précautions, on interdit
                        return false;
                    }
                } else { // c'est une option qui ne nécessite pas de
                    // vérification (ie:lister)
                    return true;
                }
            } else { // c'est une option qui ne nécessite pas de vérification
                // (ie:lister)
                return true;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * creation du controller principal
     * 
     * @param session
     *            la session
     * @return globaz.framework.controller.FWController
     */
    @Override
    public FWController createController(BISession session) {
        // cree un controller de type dispatcher pour l'application
        return new FWDispatcher(session, "phenix", "CP");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 11:26:46)
     * 
     * @param aStack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        // regle de la pile pour le back
        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        aStack.addRule(new FWRemoveActionsEndingWith("phenix.principale.decision.imprimer"));
        // aStack.addRule(new CPDecisionValiderRule());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        // request.setAttribute("mainServletPath","pyxis");
        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter("userAction");
        String actionSuite = FWAction.newInstance(action).getPackagePart();

        /*
         * Affichage du document dans l'écran pour validation
         */
        if ("phenix.document.decision.generer".equals(action)) {
            String wantOpenSaveDialog = request.getParameter(CPMainServlet.PARAM_WANT_SAVE_DLG);
            String doc = request.getParameter(CPMainServlet.PARAM_DOC);
            boolean wantDlg = false;
            if ("true".equals(wantOpenSaveDialog)) {
                wantDlg = true;
            }
            _showDocument(doc, wantDlg, response);
            return;
        } else if (actionSuite.equals("divers")) {
            doActionDivers(session, request, response, mainController);
        } else if (actionSuite.equals("principale")) {
            doActionPrincipale(session, request, response, mainController);
        } else if (actionSuite.equals("listes")) {
            CPActionDefault act = new CPActionDefault(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("communications")) {
            doActionCommunications(session, request, response, mainController);
        } else if (actionSuite.equals("itext")) {
            doActionCommunications(session, request, response, mainController);
        } else if (actionSuite.equals("process")) {
            doActionProcess(session, request, response, mainController);
        } else if (actionSuite.equals("suivis")) {
            doActionSuivis(session, request, response, mainController);
        } else {
            // CPProcessusRetourCommunicationFiscale.main();
            getServletContext().getRequestDispatcher("/enConstruction.jsp").forward(request, response);
        }
    }

    public void doActionCommunications(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws ServletException, IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        if (actionSuite.equals("reglePlausibilite")) {
            CPActionReglePlausibilite act = new CPActionReglePlausibilite(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("parametrePlausibilite")) {
            CPActionParametrePlausibilite act = new CPActionParametrePlausibilite(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("journalRetour")) {
            CPActionJournalRetour act = new CPActionJournalRetour(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("apercuCommunicationFiscaleRetour")) {
            CPActionApercuCommunicationFiscaleRetour act = new CPActionApercuCommunicationFiscaleRetour(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("communicationFiscaleAffichage")) {
            CPActionCommunicationFiscaleAffichage act = new CPActionCommunicationFiscaleAffichage(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("validationJournalRetour")) {
            CPActionValidationJournalRetour act = new CPActionValidationJournalRetour(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("devalidationJournalRetour")) {
            CPActionValidationJournalRetour act = new CPActionValidationJournalRetour(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("traitementAnomalies")) {
            CPActionTraitementAnomalies act = new CPActionTraitementAnomalies(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("rejets")) {
            CPActionRejets act = new CPActionRejets(this);
            act.doAction(session, request, response, mainController);
        } else {
            // Action par défaut
            CPActionDefault act = new CPActionDefault(this);
            act.doAction(session, request, response, mainController);
        }
    }

    public void doActionDivers(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        mainController.displayTree();
        // --- Check user action
        String actionSuite = split(request.getParameter("userAction"), 2);
        if (actionSuite.equals("periodeFiscale")) {
            CPActionPeriodeFiscale act = new CPActionPeriodeFiscale(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableNonActif")) {
            CPActionTableNonActif act = new CPActionTableNonActif(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableIndependant")) {
            CPActionTableIndependant act = new CPActionTableIndependant(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableRentier")) {
            CPActionTableRentier act = new CPActionTableRentier(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableEtrangerSansActivite")) {
            CPActionTableEtrangerSansActivite act = new CPActionTableEtrangerSansActivite(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableEtrangerAvecActivite")) {
            CPActionTableEtrangerAvecActivite act = new CPActionTableEtrangerAvecActivite(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableAFI")) {
            CPActionTableAFI act = new CPActionTableAFI(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("tableFortune")) {
            CPActionTableFortune act = new CPActionTableFortune(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("parametreCanton")) {
            CPActionParametreCanton act = new CPActionParametreCanton(this);
            act.doAction(session, request, response, mainController);
        } else {
            // Action par défaut
            CPActionDefault act = new CPActionDefault(this);
            act.doAction(session, request, response, mainController);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    public void doActionPrincipale(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        mainController.displayTree();
        String actionSuite = split(request.getParameter("userAction"), 2);
        // --- Check user action
        // * Sale mais nécessaire * (ado)
        // Il faut interdire certaines options
        // en fonction de l'état. (cf doc points ouverts)
        // impossible de faire ça à l'écran, on le fait donc ici, dans la main
        // servlet
        String idDecision = request.getParameter("idDecision");
        if (JadeStringUtil.isEmpty(idDecision)) {
            idDecision = request.getParameter("selectedId");
        }
        if (!checkOptionAllowed(idDecision, session, request)) {
            CPActionCancel act = new CPActionCancel(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("decision")) {
            CPActionDecision act = new CPActionDecision(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("decisionValider")) {
            CPActionDecisionValider act = new CPActionDecisionValider(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("cotisation") == true) {
            CPActionCotisation act = new CPActionCotisation(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("remarqueDecision")) {
            CPActionRemarqueDecision act = new CPActionRemarqueDecision(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("lienTypeDecRemarque")) {
            CPActionLienTypeDecRemarque act = new CPActionLienTypeDecRemarque(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("commentaireRemarqueType")) {
            CPActionCommentaireRemarqueType act = new CPActionCommentaireRemarqueType(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("donneesCalcul")) {
            CPActionDonneesCalcul act = new CPActionDonneesCalcul(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("decisionImprimerLot")) {
            CPActionDecisionImprimerLot act = new CPActionDecisionImprimerLot(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("autreDossier")) {
            CPActionAutreDossier act = new CPActionAutreDossier(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("sortie")) {
            CPActionSortie act = new CPActionSortie(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("sortieMontant")) {
            CPActionSortieMontant act = new CPActionSortieMontant(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("decisionReprise")) {
            CPActionDecisionReprise act = new CPActionDecisionReprise(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("decisionSuivi")) {
            CPActionDecisionSuivi act = new CPActionDecisionSuivi(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("commentaireCommunication")) {
            CPActionCommentaireCommunication act = new CPActionCommentaireCommunication(this);
            act.doAction(session, request, response, mainController);
        } else {
            // Action par défaut
            CPActionDefault act = new CPActionDefault(this);
            act.doAction(session, request, response, mainController);
        }
    }

    public void doActionProcess(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws ServletException, IOException {
        String actionSuite = split(request.getParameter("userAction"), 2);
        if (actionSuite.equals("acompte")) {
            CPActionAcompte act = new CPActionAcompte(this);
            act.doAction(session, request, response, mainController);
        } else if (actionSuite.equals("decision")) {
            CPActionDecision act = new CPActionDecision(this);
            act.doAction(session, request, response, mainController);
        } else {
            // Action par défaut
            CPActionDefault act = new CPActionDefault(this);
            act.doAction(session, request, response, mainController);
        }
    }

    private void doActionSuivis(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        CPActionSuivis act = new CPActionSuivis(this);
        act.doAction(session, request, response, mainController);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 07:41:07)
     */
    @Override
    protected void goHomePage(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("CP-MenuPrincipal", "menu");
        bb.setCurrentMenu("CP-OnlyDetail", "options");

        String path = "/" + globaz.phenix.application.CPApplication.APPLICATION_PHENIX_REP + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/";
        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    // *******************************************************************
    // * Méthode _doActioBack n'est appelée null part... vraiment utile? *
    // *******************************************************************
    // TODO: voir l'utilité de cette méthode
    // private boolean _doActionBack (HttpSession session, HttpServletRequest
    // request, HttpServletResponse response, FWController mainController)
    // throws ServletException, IOException {
    // String action = request.getParameter ("userAction");
    //
    // if ((action != null) && ("back".equals(action))) {
    // //int size = getUrlStack(session).size();
    // FWUrl currentUrl = getUrlStack(session).pop(); // remove current url
    // FWUrl backUrl = getUrlStack(session).pop();
    // String curentPage = "";
    // String backPage = "";
    // if (currentUrl != null) {
    // curentPage = currentUrl.getPageName();
    // }
    // if (backUrl != null) {
    // backPage = backUrl.getPageName();
    // }
    // if (!JadeStringUtil.isEmpty(backPage)){
    //
    // if (backPage.equals(curentPage)) {
    // this.getServletContext().getRequestDispatcher ("/" +
    // backUrl.getPageName() + "?" + backUrl.getParamsList()).forward (request,
    // response);
    // return true;
    // } else if (session.getAttribute("globazContext") != null) {
    // //Hashtable parentSession = (Hashtable)
    // session.getAttribute("globazContext");
    // FWDefaultServletAction._restoreContext(request);
    // this.getServletContext().getRequestDispatcher ("/" +
    // backUrl.getPageName() + "?" + backUrl.getParamsList()).forward (request,
    // response);
    // return true;
    // }
    // }
    //
    // // sinon on va a la home page
    // String languePage = FWDefaultServletAction.getIdLangueIso(session);
    // String tmp = "";
    // tmp = "/" +
    // globaz.phenix.application.CPApplication.APPLICATION_PHENIX_REP + "/" +
    // languePage;
    // this.getServletContext().getRequestDispatcher
    // (tmp+FWDefaultServletAction.MAIN_PAGE).forward (request, response);
    // return true;
    // }
    // return false;
    // }
    /**
     * Détermine si le servlet actuel doit utiliser la langue utilisateur pour trouver les pages .jsp (genre
     * <CODE>/FR</CODE>, <CODE>/DE</CODE>, etc.).
     * 
     * @return <code>true</code> si le servlet utilise la langue utilisateur, <code>false</code> sinon. Avec FW V29 Date
     *         de création : (18.05.2005 11:00:00)
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }

    /* Découpe et retourne une chaîne de caractère compris entre un élément */
    public String split(String str, int pos) {
        Vector<String> tmp = new Vector<String>();
        try {
            StringTokenizer st = new StringTokenizer(str, ".");
            while (st.hasMoreTokens()) {
                tmp.add(st.nextToken());
            }
            return tmp.elementAt(pos);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return null;
        }
    }
}
