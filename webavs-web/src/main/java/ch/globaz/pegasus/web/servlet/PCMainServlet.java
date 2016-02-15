package ch.globaz.pegasus.web.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.framework.utils.urls.rules.FWSuppressSameUserActions;
import globaz.jade.log.JadeLogger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCMenu;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.web.application.PCApplication;

public class PCMainServlet extends FWJadeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PCMainServlet() {
        super(PCApplication.DEFAULT_APPLICATION_PEGASUS, PCApplication.DEFAULT_APPLICATION_PEGASUS,
                PCApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {

        // ************* gestion des actions à exclure du stacktrace pour bon fonctionnement bouton applicatif retour
        // action standards
        FWRemoveActionsEndingWith removeLister = new FWRemoveActionsEndingWith(".lister");
        FWRemoveActionsEndingWith removeMotifier = new FWRemoveActionsEndingWith(".modifier");
        FWRemoveActionsEndingWith removeSupprimer = new FWRemoveActionsEndingWith(".supprimer");
        // Ajax, widget
        FWRemoveActionsEndingWith removeAjax = new FWRemoveActionsEndingWith("AJAX");
        FWRemoveActionsEndingWith removeWidget = new FWRemoveActionsEndingWith("widget.action.jade.afficher");
        FWRemoveActionsEndingWith removeWidgetDownload = new FWRemoveActionsEndingWith("widget.action.jade.download");
        // CAPage action afficher
        FWRemoveActionsEndingWith removeDetailVariablesMetiers = new FWRemoveActionsEndingWith(
                "variableMetier.afficher");
        FWRemoveActionsEndingWith removeDetailMonnaiesEtrangeres = new FWRemoveActionsEndingWith(
                "monnaieEtrangere.afficher");
        FWRemoveActionsEndingWith removeDetailTabelleAfc = new FWRemoveActionsEndingWith(
                "pegasus.parametre.conversionRente.afficher");
        FWRemoveActionsEndingWith removeLienLocaliteAfficher = new FWRemoveActionsEndingWith(".lienLocalite.afficher");
        FWRemoveActionsEndingWith removeSimpleZoneAfficher = new FWRemoveActionsEndingWith(
                "pegasus.parametre.zoneLocalite.afficher");
        FWRemoveActionsEndingWith removeZoneForfait = new FWRemoveActionsEndingWith(
                "pegasus.parametre.zoneForfaits.afficher");
        FWRemoveActionsEndingWith removeForfaitPrimesAmal = new FWRemoveActionsEndingWith(
                "pegasus.parametre.forfaitPrimesAssuranceMaladie.afficher");

        // Process
        // exclusion process calculer
        FWRemoveActionsEndingWith removeCalculer = new FWRemoveActionsEndingWith("calculer");
        // Exclusion validation DAC
        FWRemoveActionsEndingWith removeProcessValider = new FWRemoveActionsEndingWith("validationDecisions.valider");
        // Exclusion ecran valider DAC
        FWRemoveActionsEndingWith removeScreenValider = new FWRemoveActionsEndingWith("validationDecisions.afficher");
        // communication OCC
        FWRemoveActionsEndingWith removeScreenGenererOCC = new FWRemoveActionsEndingWith(
                "pegasus.annonce.genererCommunicationOCC.executer");
        // communication LAPRAMS
        FWRemoveActionsEndingWith removeScreenGenererLAPRAMS = new FWRemoveActionsEndingWith(
                "pegasus.annonce.genererAnnonceLaprams.executer");
        // comptabilisation des lots
        FWRemoveActionsEndingWith removeScreenComptabiliserLots = new FWRemoveActionsEndingWith(
                "pegasus.lot.comptabiliser.executer");
        // Decsision detail supp et detail dac, custom action detail qui apellent
        FWRemoveActionsEndingWith detailDecisionsDac = new FWRemoveActionsEndingWith("decisionApresCalcul.afficher");
        FWRemoveActionsEndingWith detailDecisionsSup = new FWRemoveActionsEndingWith("decisionSuppression.afficher");
        FWRemoveActionsEndingWith detailDecisionsRefus = new FWRemoveActionsEndingWith("decisionRefus.afficher");
        // plan calcul affichage - nouvel onglet
        FWRemoveActionsEndingWith removePlanCalcul = new FWRemoveActionsEndingWith(".planCalcul.afficher");

        // blocage/deblocage
        FWRemoveActionsEndingWith blocagePC = new FWRemoveActionsEndingWith(".pcAccordeeDetail.actionBloquerPC");
        FWRemoveActionsEndingWith deBlocagePC = new FWRemoveActionsEndingWith(".pcAccordeeDetail.actionDebloquerPC");

        FWRemoveActionsEndingWith listeCommunePolitque = new FWRemoveActionsEndingWith(
                ".listeRepartitionCommunePolitique.executer");

        aStack.addRule(removeLister);
        aStack.addRule(removeMotifier);
        aStack.addRule(removeSupprimer);
        aStack.addRule(removeAjax);
        aStack.addRule(removePlanCalcul);
        aStack.addRule(removeLienLocaliteAfficher);
        aStack.addRule(removeSimpleZoneAfficher);

        aStack.addRule(removeDetailVariablesMetiers);
        aStack.addRule(removeDetailMonnaiesEtrangeres);
        aStack.addRule(removeDetailTabelleAfc);

        aStack.addRule(removeZoneForfait);
        aStack.addRule(removeForfaitPrimesAmal);
        aStack.addRule(detailDecisionsDac);
        aStack.addRule(detailDecisionsSup);
        aStack.addRule(detailDecisionsRefus);

        aStack.addRule(removeWidgetDownload);
        aStack.addRule(removeScreenGenererOCC);
        aStack.addRule(removeScreenGenererLAPRAMS);
        aStack.addRule(removeScreenComptabiliserLots);

        aStack.addRule(blocagePC);
        aStack.addRule(deBlocagePC);

        aStack.addRule(removeWidget);
        aStack.addRule(removeCalculer);
        aStack.addRule(removeProcessValider);
        aStack.addRule(removeScreenValider);

        aStack.addRule(listeCommunePolitque);
        // Evite de garder 2 fois la même page
        aStack.addRule(new FWSuppressSameUserActions());
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession ,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        FWMenuBlackBox menuBB = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (menuBB != null) {
            menuBB.setCurrentMenu("pegasus-menuprincipal", "menu");
            menuBB.setCurrentMenu("pegasus-optionsempty", "options");
        }

        // Gestions paramètres et noeuds du menu
        setMenuParametersAndNodes(menuBB);

        StringBuffer path = new StringBuffer("/").append(PCApplication.APPLICATION_PEGASUS_REP);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    @Override
    protected void initializeActionMapping() {
        registerActionMapping("pegasus.dossier", PCDossierServletAction.class);
        registerActionMapping("pegasus.demande", PCDemandeServletAction.class);
        registerActionMapping("pegasus.home", PCHomeServletAction.class);

        registerActionMapping("pegasus.dettecomptatcompense", PCDetteComptatCompenseServletAction.class);

        registerActionMapping("pegasus.droit", PCDroitServletAction.class);
        registerActionMapping("pegasus.fortuneparticuliere", PCDroitServletAction.class);
        registerActionMapping("pegasus.revenusdepenses", PCDroitServletAction.class);
        registerActionMapping("pegasus.fortuneusuelle", PCDroitServletAction.class);
        registerActionMapping("pegasus.dessaisissement", PCDroitServletAction.class);
        registerActionMapping("pegasus.variablemetier", PCVariableMetierServletAction.class);
        registerActionMapping("pegasus.monnaieetrangere", PCMonnaieEtrangereServletAction.class);
        registerActionMapping("pegasus.pcaccordee", PCPCAccordeeServletAction.class);
        registerActionMapping("pegasus.renteijapi", PCDroitServletAction.class);
        registerActionMapping("pegasus.situationFamiliale", PCSituationFamilialeAction.class);
        registerActionMapping("pegasus.habitat", PCDroitServletAction.class);
        registerActionMapping("pegasus.decision", PCDecisionServletAction.class);
        // PCRenteijapiServletAction.class);
        registerActionMapping("pegasus.lot", PCLotServletAction.class);

        registerActionMapping("pegasus.parametre", PCParametreServletAction.class);

        registerActionMapping("pegasus.creancier", PCCreancierServletAction.class);

        // test pour jstl
        registerActionMapping("pegasus.testjstl", PCTestJstlServletAction.class);
        // this.registerActionMapping("pegasus.downloadDocument", PCDownloadDocumentServletAction.class);
        registerActionMapping("pegasus.traitementdemasse", PCTraitementDeMasseServletAction.class);
        registerActionMapping("pegasus.process", PCProcessServletAction.class);
        registerActionMapping("pegasus.retenues", PCAccordeeRetenuesServletAction.class);
        registerActionMapping("pegasus.annonce", PCAnnonceServletAction.class);
        registerActionMapping("pegasus.transfertdossier", PCTransfertServletAction.class);
        registerActionMapping("pegasus.demanderenseignement", PCDemandeRenseignementServletAction.class);
        registerActionMapping("pegasus.avance", PCAvanceServletAction.class);
        registerActionMapping("pegasus.blocage", PCBlocageServletAction.class);
        registerActionMapping("pegasus.liste", FWDefaultServletAction.class);
    }

    /**
     * Méthode servant à gérer les différents paramètres des menus de l'application
     * 
     * @param menuBB
     * @throws Exception
     */
    private void setMenuParametersAndNodes(FWMenuBlackBox menuBB) throws Exception {

        // Gestion entrée allocation de noel d'apres properties.
        // Si définis a false, dans les properties, on supprimer le noeud des process

        try {
            // si value null (propriétés inexistantes) ou false, on force à false
            if (!PCproperties.getBoolean(EPCProperties.ALLOCATION_NOEL)) {
                FWMenuBlackBox.ensureNodeDoesntExist(EPCMenu.ALLOCATION_NOEL_NODE_ID.getProperty(),
                        EPCMenu.PEGASUS_MENU_PRINCIPAL.getProperty());
            }
        } catch (PropertiesException ex) {
            // Si exception, propritè pas présente, on loggue l'info, et on désactive le menu
            JadeLogger.warn(this, "the properties 'pegasus-allocation.noel' doesn't exist, so the menu was disabled!");
            FWMenuBlackBox.ensureNodeDoesntExist(EPCMenu.ALLOCATION_NOEL_NODE_ID.getProperty(),
                    EPCMenu.PEGASUS_MENU_PRINCIPAL.getProperty());
        }

        // Gestion commune politique
        try {
            if (!CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue()) {
                FWMenuBlackBox.ensureNodeDoesntExist(EPCMenu.LISTE_REPARTITION_COMMUNE_POLITIQUE.getProperty(),
                        EPCMenu.PEGASUS_MENU_PRINCIPAL.getProperty());
            }
        } catch (PropertiesException ex) {
            // Si exception, propritè pas présente, on loggue l'info, et on désactive le menu
            JadeLogger.warn(this,
                    "the properties 'liste.ajouter.commune.politique' doesn't exist, so the menu was disabled!");
            FWMenuBlackBox.ensureNodeDoesntExist(EPCMenu.LISTE_REPARTITION_COMMUNE_POLITIQUE.getProperty(),
                    EPCMenu.PEGASUS_MENU_PRINCIPAL.getProperty());
        }
    }
}
