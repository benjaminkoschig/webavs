package ch.globaz.al.web.servlet;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.al.web.application.ALApplication;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWRemoveActionsEndingWith;
import globaz.fx.common.application.servlet.NaviRules;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;

/**
 * Main Servlet de l'application WEB@AF
 * 
 * @author VYJ
 */
public class ALMainServlet extends FWJadeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Nom du menu WEBAF */
    private static final String MENU_MAIN_AL = "menuWEBAF";

    /** Noeuds du menu désactivés en mode droit "Lecture seule" */
    private static final List<String> nodesDisabledReadingMode = new ArrayList<String>();
    static {
        ALMainServlet.nodesDisabledReadingMode.add("Nouveau");
    }

    private boolean bFPVIsInitialized = false;

    /**
	 * 
	 */
    public ALMainServlet() {
        super(ALApplication.DEFAULT_APPLICATION_WEBAF, ALApplication.APPLICATION_NAME, ALApplication.APPLICATION_PREFIX);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#customize(globaz.framework.utils. urls.FWUrlsStack)
     */
    @Override
    protected void customize(FWUrlsStack stack) {
        NaviRules rule = new NaviRules(stack);
        stack.addRule(rule);
        // supprime les actions interdites de la pile
        List listeInterdits = new ArrayList();
        listeInterdits.add(".lister");
        listeInterdits.add(".choisir");
        listeInterdits.add(".selectionner");
        // on ne reffectue pas d'action qui pourrait modifier des données dans
        // l'historique, ni même un réaffichage (car le viewBean n'est pas
        // réinstancié et du coup l'instance en session ne correspond pas à
        // l'écran chargé)
        listeInterdits.add(".modifier");
        listeInterdits.add(".ajouter");
        listeInterdits.add(".reAfficher");
        listeInterdits.add(".executer");
        listeInterdits.add(".supprimer");

        // Toutes les customs actions qui modifient en DB
        listeInterdits.add("decision.supprimerCopie");
        listeInterdits.add("decompteAdi.supprimerDecompte");
        listeInterdits.add("decompteAdi.calculer");
        listeInterdits.add("decompteAdi.generer");
        listeInterdits.add("dossierMain.retirerBeneficiaire");
        listeInterdits.add("dossierMain.copier");
        listeInterdits.add("droit.toformation");
        listeInterdits.add("droit.retirerBeneficiaire");
        listeInterdits.add("droit.supprimerDroit");
        listeInterdits.add("annonceRafam.synchroniserEnfantAvecUPI");
        listeInterdits.add("annonceRafam.validerAnnonce");
        listeInterdits.add("entetePrestation.supprimerPrestation");
        listeInterdits.add("processusGestion.creerPartiel");
        listeInterdits.add("processusGestion.annulerTraitement");
        listeInterdits.add("revenus.supprimerRevenu");
        listeInterdits.add("saisieAdi.supprimerSaisie");

        // Ajax, widget
        listeInterdits.add("AJAX");
        listeInterdits.add("widget.action.jade.afficher");
        listeInterdits.add("widget.action.jade.download");
        FWRemoveActionsEndingWith remRule = new FWRemoveActionsEndingWith(listeInterdits);
        stack.addRule(remRule);
        initializeFPV();

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
        FWMenuBlackBox bb = (FWMenuBlackBox) httpSession.getAttribute(FWServlet.OBJ_USER_MENU);
        if (bb != null) {
            bb.setCurrentMenu("menuWEBAF", "menu");
            bb.setCurrentMenu("optionsWEBAF", "options");
        }

        BSession objSession = (BSession) httpSession.getAttribute("objSession");
        // Désactivation des noeuds non accessible en lecture seule
        for (String disabledNode : ALMainServlet.nodesDisabledReadingMode) {
            try {
                if (objSession.hasRight(ALApplication.APPLICATION_NAME, FWSecureConstants.UPDATE)) {
                    bb.setNodeActive(true, disabledNode, ALMainServlet.MENU_MAIN_AL);
                } else {
                    bb.setNodeActive(false, disabledNode, ALMainServlet.MENU_MAIN_AL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        StringBuffer path = new StringBuffer("/").append(ALApplication.DEFAULT_APPLICATION_ROOT);
        path.append("/").append("homePage.jsp");
        getServletContext().getRequestDispatcher(path.toString()).forward(request, response);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWServlet#hasLanguageInPagesPath()
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.servlets.FWJadeServlet#initializeActionMapping()
     */
    @Override
    protected void initializeActionMapping() {
        registerActionMapping("al.dossier", ALActionDossier.class);
        registerActionMapping("al.droit", ALActionDroit.class);
        registerActionMapping("al.allocataire", ALActionAllocataire.class);
        registerActionMapping("al.ajax", ALAbstractDefaultAction.class);
        registerActionMapping("al.decision", ALActionDecision.class);
        registerActionMapping("al.decisionFileAttente", ALActionDecision.class);
        registerActionMapping("al.personne", ALActionPersonne.class);
        registerActionMapping("al.prestation", ALActionPrestation.class);
        registerActionMapping("al.generations", ALAbstractDefaultAction.class);
        registerActionMapping("al.echeances", ALAbstractDefaultAction.class);
        registerActionMapping("al.traitement", ALActionTraitement.class);
        registerActionMapping("al.attribut", ALActionAttribut.class);
        registerActionMapping("al.rafam", ALActionAnnonceRafam.class);
        registerActionMapping("al.parametres", ALActionParametres.class);
        registerActionMapping("al.ged", ALActionGed.class);
        registerActionMapping("al.adi", ALActionAdi.class);
        registerActionMapping("al.envois", ALActionEnvois.class);

        registerActionMapping("al.radiationauto", ALActionRadiationAutomatiqueDossiers.class);
        registerActionMapping("al.radiationaffilie", ALActionRadiationAffilie.class);
        // this.registerActionMapping("al.decisionsmasse", ALActionDecisionsMasse.class);
    }

    /**
     * Suppression des entrées de menu lettres au poste de travail pour toutes les caisses exceptées la FPV
     */
    private void initializeFPV() {
        if (bFPVIsInitialized == false) {
            boolean removeMenu = true;

            try {
                String enableEnvoiString = ((ALApplication) GlobazSystem
                        .getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF)).getProperty("enableEnvoi");
                removeMenu = !Boolean.parseBoolean(enableEnvoiString);
            } catch (Exception e1) {
                JadeLogger.info(this, "Properties problem : enableEnvoi >> " + e1.getMessage());
                removeMenu = true;
            }

            if (removeMenu) {
                try {
                    FWMenuBlackBox.ensureNodeDoesntExist("MENU_TRAITEMENT_DOCUMENT_CORRESPONDANCE", "menuWEBAF");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    FWMenuBlackBox.ensureNodeDoesntExist("MENU_RECHERCHE_PARAMETRES_FORMULES", "menuWEBAF");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    FWMenuBlackBox.ensureNodeDoesntExist("MENU_OPTIONS_DOSSIER_ENVOI", "dossier-main");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    FWMenuBlackBox.ensureNodeDoesntExist("MENU_OPTIONS_DOSSIER_ENVOI", "dossier-main-adi");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bFPVIsInitialized = true;
        }
    }

}
