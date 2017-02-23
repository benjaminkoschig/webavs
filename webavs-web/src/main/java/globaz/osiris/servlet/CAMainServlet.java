package globaz.osiris.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.hermes.utils.FTPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeInitProperties;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.servlet.action.avance.CAAvanceAction;
import globaz.osiris.servlet.action.bvrftp.CABvrFtpAction;
import globaz.osiris.servlet.action.compte.CAApercuComptes;
import globaz.osiris.servlet.action.compte.CAApercuParSectionAction;
import globaz.osiris.servlet.action.compte.CACompteCourantAction;
import globaz.osiris.servlet.action.compte.CAComptesAnnexesAction;
import globaz.osiris.servlet.action.compte.CAExtournerOperationAction;
import globaz.osiris.servlet.action.compte.CAExtournerSectionAction;
import globaz.osiris.servlet.action.compte.CAHistoriqueCompteAnnexeAction;
import globaz.osiris.servlet.action.compte.CAJournalAction;
import globaz.osiris.servlet.action.compte.CAJournalOperationAction;
import globaz.osiris.servlet.action.compte.CAReferenceRubriqueAction;
import globaz.osiris.servlet.action.compte.CARoleAction;
import globaz.osiris.servlet.action.compte.CARubriqueAction;
import globaz.osiris.servlet.action.compte.CASectionAction;
import globaz.osiris.servlet.action.compte.CATransfertSoldesAction;
import globaz.osiris.servlet.action.compte.CATypeOperationAction;
import globaz.osiris.servlet.action.compte.CATypeSectionAction;
import globaz.osiris.servlet.action.contentieux.CAAnnulerEtapeContentieuxAction;
import globaz.osiris.servlet.action.contentieux.CACalculTaxeAction;
import globaz.osiris.servlet.action.contentieux.CAEtapeAction;
import globaz.osiris.servlet.action.contentieux.CAGestionEtapeAction;
import globaz.osiris.servlet.action.contentieux.CAOperationContentieuxAction;
import globaz.osiris.servlet.action.contentieux.CASequenceContentieuxAction;
import globaz.osiris.servlet.action.historique.CAHistoriqueBulletinSoldeAction;
import globaz.osiris.servlet.action.interets.CADetailInteretMoratoireAction;
import globaz.osiris.servlet.action.interets.CAGenreInteretAction;
import globaz.osiris.servlet.action.interets.CAInteretMoratoireAction;
import globaz.osiris.servlet.action.interets.CAPlanCalculInteretAction;
import globaz.osiris.servlet.action.interets.CARubriqueSoumiseInteretAction;
import globaz.osiris.servlet.action.lettrage.CAActionLettrage;
import globaz.osiris.servlet.action.message.CAApercuJournalisationAction;
import globaz.osiris.servlet.action.message.CAApercuMessageAction;
import globaz.osiris.servlet.action.ordres.CAApercuOrdreAction;
import globaz.osiris.servlet.action.ordres.CAApercuOrdreRecouvrementAction;
import globaz.osiris.servlet.action.ordres.CAApercuOrdreVersementAction;
import globaz.osiris.servlet.action.ordres.CAOrdresGroupesAction;
import globaz.osiris.servlet.action.print.CAImprimerAction;
import globaz.osiris.servlet.action.process.CAProcessAction;
import globaz.osiris.servlet.action.recouvrement.CARecouvrementAction;
import globaz.osiris.servlet.action.retours.CARetoursAction;
import globaz.osiris.servlet.action.services.CAListDirectoryAction;
import globaz.osiris.servlet.action.services.CARechercheMontantOperationAction;
import globaz.osiris.servlet.action.services.CARechercheMontantSectionAction;
import globaz.osiris.stack.rules.NaviRules;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Nom du menu CA */
    private static final String MENU_MAIN_AL = "CA-MenuPrincipal";

    /** Noeuds du menu désactivés en mode droit "Lecture seule" */
    private static final List<String> nodesDisabledReadingMode = new ArrayList<String>();
    static {
        CAMainServlet.nodesDisabledReadingMode.add("retours_comptabilisation");
    }

    /**
     * Constructor for CAMainSrvlt.
     */
    public CAMainServlet() {
        super();
    }

    /**
     * @see globaz.framework.servlets.FWServlet#createController(BISession)
     */
    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, "osiris", "CA");
    }

    /**
     * @see globaz.framework.servlets.FWServlet#customize(FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack aStack) {
        // FWDefaultRule rule = new FWDefaultRule(aStack,
        // CAApplication.DEFAULT_APPLICATION_OSIRIS);

        // exe BVR
        FWRemoveActionsEndingWith removeBVRExec = new FWRemoveActionsEndingWith("osiris.process.bvr.executer");

        NaviRules rule = new NaviRules(aStack);
        aStack.addRule(rule);
        aStack.addRule(removeBVRExec);
    }

    /**
     * @see globaz.framework.servlets.FWServlet#doAction(HttpSession, HttpServletRequest, HttpServletResponse,
     *      FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        setMenuParameters(session);

        request.setAttribute("mainServletPath", request.getServletPath());
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String actionSuite = action.getPackagePart();
        String actionClass = action.getClassPart();

        String _action = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();

        if ("osiris.lettrage.plages".equals(_action)) {
            new CAActionLettrage().doAction(session, request, response, mainController);
        } else if ("osiris.lettrage.main".equals(_action)) {
            new CAActionLettrage().doAction(session, request, response, mainController);
        } else if (actionSuite == null) {
            return;
        } else if (actionSuite.equals("comptes")) {
            doActionComptes(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("contentieux")) {
            doActionContentieux(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("ordres")) {
            doActionOrdres(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("process")) {
            doActionProcess(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("services")) {
            doActionServices(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("message")) {
            doActionMessage(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("print")) {
            doActionPrint(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("recouvrement")) {
            doActionRecouvrement(session, request, response, mainController, action);
        } else if (actionSuite.equals("interets")) {
            doActionInteretsMoratoires(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("bvrftp")) {
            doActionBvrFtp(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("avance")) {
            doActionAvance(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("retours")) {
            doActionRetours(session, request, response, mainController, actionClass);
        } else if (actionSuite.equals("historique")) {
            doActionHistorique(session, request, response, mainController, actionClass);
        } else {
            FWDefaultServletAction act = new FWDefaultServletAction(this);
            act.doAction(session, request, response, mainController);
        }

        if (JadeLogger.isDebugEnabled()) {
            JadeLogger.debug(this, "UserAction processed : " + action.toString());
        }
    }

    public void doActionAvance(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        new CAAvanceAction(this).doAction(session, request, response, mainController);
    }

    public void doActionBvrFtp(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        new CABvrFtpAction(this).doAction(session, request, response, mainController);
    }

    public void doActionComptes(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act;
        if (actionSuite.equals("apercuComptes")) {
            act = new CAApercuComptes(this);
        } else if (actionSuite.equals("apercuJournal")) {
            act = new CAJournalAction(this);
        } else if (actionSuite.equals("apercuCompteResume")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.equals("apercuComptesAnnexes")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.equals("apercuComptesCourants")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.equals("apercuCompteur")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.equals("apercuListeComptesCourants")) {
            act = new CACompteCourantAction(this);
        } else if (actionSuite.equals("apercuParSection")) {
            act = new CAApercuParSectionAction(this);
        } else if (actionSuite.equals("apercuSectionDetaille")) {
            act = new CASectionAction(this);
        } else if (actionSuite.equals("historiqueCompteAnnexe")) {
            act = new CAHistoriqueCompteAnnexeAction(this);
        } else if (actionSuite.equals("compteCourant")) {
            act = new CACompteCourantAction(this);
        } else if (actionSuite.equals("comptesAnnexes")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.equals("detailComptesAnnexes")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.indexOf("journalOperation") != -1) {
            act = new CAJournalOperationAction(this);
        } else if (actionSuite.equals("rechercheCompteAnnexe")) {
            act = new CAComptesAnnexesAction(this);
        } else if (actionSuite.equals("rechercheCompteCourant")) {
            act = new CACompteCourantAction(this);
        } else if (actionSuite.equals("rechercheJournal")) {
            act = new CAJournalAction(this);
        } else if (actionSuite.equals("rechercheRubrique")) {
            act = new CARubriqueAction(this);
        } else if (actionSuite.equals("referenceRubrique")) {
            act = new CAReferenceRubriqueAction(this);
        } else if (actionSuite.equals("role")) {
            act = new CARoleAction(this);
        } else if (actionSuite.equals("rubrique")) {
            act = new CARubriqueAction(this);
        } else if (actionSuite.equals("typeOperation")) {
            act = new CATypeOperationAction(this);
        } else if (actionSuite.equals("typeSection")) {
            act = new CATypeSectionAction(this);
        } else if (actionSuite.equals("extournerOperation")) {
            act = new CAExtournerOperationAction(this);
        } else if (actionSuite.equals("extournerSection")) {
            act = new CAExtournerSectionAction(this);
        } else if (actionSuite.equals(CATransfertSoldesAction.ACTION_SUITE)) {
            act = new CATransfertSoldesAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }

        act.doAction(session, request, response, mainController);
    }

    public void doActionContentieux(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController, String actionSuite)
            throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);
        if (actionSuite.equals("annulerEtapeContentieux")) {
            act = new CAAnnulerEtapeContentieuxAction(this);
        } else if (actionSuite.equals("calculTaxe")) {
            act = new CACalculTaxeAction(this);
        } else if (actionSuite.equals("operationContentieux")) {
            act = new CAOperationContentieuxAction(this);
        } else if (actionSuite.equals("etape")) {
            act = new CAEtapeAction(this);
        } else if (actionSuite.equals("gestionEtape")) {
            act = new CAGestionEtapeAction(this);
        } else if (actionSuite.equals("sequenceContentieux")) {
            act = new CASequenceContentieuxAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainController
     * @param actionSuite
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void doActionHistorique(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWController mainController, String actionSuite)
            throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new CAHistoriqueBulletinSoldeAction(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionInteretsMoratoires(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWController mainController, String actionSuite)
            throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);

        if (actionSuite.equals("interetMoratoire") || actionSuite.equals("apercuDecisionsInteretsMoratoires")
                || actionSuite.equals("gestionInterets")) {
            act = new CAInteretMoratoireAction(this);
        } else if (actionSuite.equals("detailInteretMoratoire")) {
            act = new CADetailInteretMoratoireAction(this);
        } else if (actionSuite.equals("planCalculInteret")) {
            act = new CAPlanCalculInteretAction(this);
        } else if (actionSuite.equals("genreInteret")) {
            act = new CAGenreInteretAction(this);
        } else if (actionSuite.equals("rubriqueSoumiseInteret")) {
            act = new CARubriqueSoumiseInteretAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    public void doActionMessage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);
        if (actionSuite.equals("apercuJournalisation")) {
            act = new CAApercuJournalisationAction(this);
        } else if (actionSuite.equals("apercuMessage")) {
            act = new CAApercuMessageAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    public void doActionOrdres(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);
        if (actionSuite.equals("apercuOrdresRecouvrement")) {
            act = new CAApercuOrdreRecouvrementAction(this);
        } else if (actionSuite.equals("apercuOrdresVersement")) {
            act = new CAApercuOrdreVersementAction(this);
        } else if (actionSuite.equals("apercuOrdres")) {
            act = new CAApercuOrdreAction(this);
        } else if (actionSuite.equals("ordresGroupes")) {
            act = new CAOrdresGroupesAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    public void doActionPrint(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new CAImprimerAction(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionProcess(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new CAProcessAction(this);
        act.doAction(session, request, response, mainController);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainController
     * @param action
     */
    private void doActionRecouvrement(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController, FWAction action) throws ServletException, IOException {
        CARecouvrementAction actionRecouvrements = new CARecouvrementAction(this);
        actionRecouvrements.doAction(session, request, response, mainController);
    }

    public void doActionRetours(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new CARetoursAction(this);
        act.doAction(session, request, response, mainController);
    }

    public void doActionServices(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController,
            String actionSuite) throws javax.servlet.ServletException, java.io.IOException {
        FWDefaultServletAction act = new FWDefaultServletAction(this);
        if (actionSuite.equals("listDirectory")) {
            act = new CAListDirectoryAction(this);
        } else if (actionSuite.equals("rechercheMontantOperation")) {
            act = new CARechercheMontantOperationAction(this);
        } else if (actionSuite.equals("rechercheMontantSection")) {
            act = new CARechercheMontantSectionAction(this);
        } else {
            act = new FWDefaultServletAction(this);
        }
        act.doAction(session, request, response, mainController);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 07:41:07)
     */
    @Override
    protected void goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        bb.setCurrentMenu("CA-MenuPrincipal", "menu");
        bb.setCurrentMenu("CA-OnlyDetail", "options");

        BSession objSession = (BSession) session.getAttribute("objSession");
        // Désactivation des noeuds non accessible en lecture seule
        for (String disabledNode : CAMainServlet.nodesDisabledReadingMode) {
            try {
                if (objSession.hasRight(CAApplication.DEFAULT_OSIRIS_NAME, FWSecureConstants.UPDATE)) {
                    bb.setNodeActive(true, disabledNode, CAMainServlet.MENU_MAIN_AL);
                } else {
                    bb.setNodeActive(false, disabledNode, CAMainServlet.MENU_MAIN_AL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String path = "/" + CAApplication.DEFAULT_OSIRIS_ROOT + "/" + FWDefaultServletAction.getIdLangueIso(session)
                + "/";
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
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() {
        JadeInitProperties.setLogToSystemOut(true);
        super.init();
    }

    /**
     * @param session
     */
    private void setMenuParameters(HttpSession session) {
        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("CA-MenuPrincipal", "menu");
        try {
            BIApplication osiApp = GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            String gedFolderType = osiApp.getProperty("ged.folder.type", "");
            String gedServiceName = osiApp.getProperty("ged.servicename.id", "");

            // Doit être placé avant les menuBB.setActionParameter()
            setMenuSpecialProperties(menuBB);

            /*
             * menuBB.setActionParameter("serviceNameId", gedServiceName ,"detail_section_ged", "CA-DetailSection");
             * menuBB.setActionParameter("gedFolderType", gedFolderType,"detail_section_ged", "CA-DetailSection");
             */
            menuBB.setActionParameter("serviceNameId", gedServiceName, "detail_section_gauche_ged",
                    "CA-DetailSectionGauche");
            menuBB.setActionParameter("gedFolderType", gedFolderType, "detail_section_gauche_ged",
                    "CA-DetailSectionGauche");
        } catch (Exception e) {
            // On ne fait rien de concret, on se contente de mettre un warning
            // l'utilisateur peut utiliser tout le reste de l'appli quand même!
            JadeLogger.warn(this, e);
        }
    }

    /**
     * 1. Set OSIRIS default menu. <br/>
     * 2. Set menu properties from OSIRIS.properties <br/>
     * <b>Note : </b> Will be deprecated.
     * 
     * @param session
     * @throws ServletException
     * @throws Exception
     */
    private void setMenuSpecialProperties(FWMenuBlackBox bb) throws Exception {
        try {
            CAApplication application = (CAApplication) GlobazServer.getCurrentSystem().getApplication(
                    CAApplication.DEFAULT_APPLICATION_OSIRIS);

            if (JadeStringUtil.isBlank(application.getProperty(FTPUtils.FTP_HOST))
                    || JadeStringUtil.isBlank(application.getProperty(FTPUtils.FTP_PORT))
                    || JadeStringUtil.isBlank(application.getProperty(FTPUtils.FTP_LOGIN))) {
                FWMenuBlackBox.ensureNodeDoesntExist("connection_postfinance", "CA-BVR");
                FWMenuBlackBox.ensureNodeDoesntExist("mot_de_passe_postfinance", "CA-BVR");
            }

            FWMenuBlackBox.ensureNodeDoesntExist("paiement_etranger", "CA-MenuPrincipal");
            FWMenuBlackBox.ensureNodeDoesntExist("journal_paiement_etranger", "CA-JournalOperation");

            if (!application.getCAParametres().isRappelSurPlan()) {
                FWMenuBlackBox.ensureNodeDoesntExist("IMPRESSION_RAPPEL", "CA-PlanRecouvrement");
            }

            if (application.getCAParametres().isContentieuxAquila()) {
                FWMenuBlackBox.ensureNodeDoesntExist("contentieux", "CA-MenuPrincipal");
                FWMenuBlackBox.ensureNodeDoesntExist("contentieux_etapes", "CA-MenuPrincipal");
                FWMenuBlackBox.ensureNodeDoesntExist("contentieux_sequences", "CA-MenuPrincipal");
                // FWMenuBlackBox.ensureNodeDoesntExist("catalogue_texte",
                // "CA-MenuPrincipal");
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_gauche_contentieux_osiris",
                        "CA-DetailSectionGauche");
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_contentieux_osiris", "CA-DetailSection");
                FWMenuBlackBox.ensureNodeDoesntExist("dossiers_etape", "CA-MenuPrincipal");

                if (bb != null) {
                    bb.setNodeActive(true, "detail_section_gauche_creer_ard", "CA-DetailSectionGauche");
                    bb.setNodeActive(true, "detail_section_gauche_contentieux_aquila", "CA-DetailSectionGauche");
                    bb.setNodeActive(true, "detail_section_creer_ard", "CA-DetailSection");
                    bb.setNodeActive(true, "detail_section_contentieux_aquila", "CA-DetailSection");
                }
            } else {
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_gauche_creer_ard", "CA-DetailSectionGauche");
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_gauche_contentieux_aquila",
                        "CA-DetailSectionGauche");
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_creer_ard", "CA-DetailSection");
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_contentieux_aquila", "CA-DetailSection");
            }

            if (JadeGedFacade.isInstalled()) {
                if (bb != null) {
                    bb.setNodeActive(true, "detail_section_gauche_ged", "CA-DetailSectionGauche");
                    // bb.setNodeActive(true, "detail_section_ged",
                    // "CA-DetailSection");
                }
            } else {
                FWMenuBlackBox.ensureNodeDoesntExist("detail_section_gauche_ged", "CA-DetailSectionGauche");
                // FWMenuBlackBox.ensureNodeDoesntExist("detail_section_ged",
                // "CA-DetailSection");
            }
        } catch (IllegalArgumentException e) {
            throw new ServletException(e);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
