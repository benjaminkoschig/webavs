package globaz.helios.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.avs.CGPlanComptableAVSManager;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.log.JadeLogger;
import java.util.Hashtable;

/**
 * Application HELIOS
 * 
 * @author Emmanuel Fleury
 * @version 1.0.0
 */
public class CGApplication extends BApplication implements ITreeListable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String ANNONCE_APPLICATION = "annonce";

    public final static String APPLICATION_HELIOS_REP = "heliosRoot";
    public final static String DEFAULT_APPLICATION_HELIOS = "HELIOS";
    // session Hermes
    public final static String KEY_SESSION_HERMES = "sessionHermes";
    private static final String PROPERTY_CG_CHEF_COMPTABLE = "CG_CHEF_COMPTABLE";

    /**
     * L'utilisateur est-il chef comptable comme définit dans les propriétés de l'application helios.
     * 
     * @param session
     * @return True si la session contient le rôle CG_CHEF_COMPTABLE (rCGAdmin)
     */
    public static boolean isUserChefComptable(BSession session) {
        try {
            String[] roles = JadeAdminServiceLocatorProvider.getLocator().getRoleService()
                    .findAllIdRoleForIdUser(session.getUserId());
            String roleChefComptable = session.getApplication().getProperty(CGApplication.PROPERTY_CG_CHEF_COMPTABLE);

            for (int i = 0; i < roles.length; i++) {
                if (roles[i].trim().equals(roleChefComptable.trim())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Constructeur du type CGApplication.
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public CGApplication() throws Exception {
        super(CGApplication.DEFAULT_APPLICATION_HELIOS);
    }

    /**
     * Constructeur du type CGApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public CGApplication(String id) throws Exception {
        super(id);
    }

    /**
     * Déclare les APIs de l'application
     */
    @Override
    protected void _declareAPI() {
        _addAPI(ICGJournal.class, CGJournal.class);
    }

    /**
     * Initialise l'application
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    @Override
    protected void _initializeApplication() throws Exception {
        try {
            FWMenuCache.getInstance().addFile("HELIOSMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "HELIOSMenu.xml non résolu : " + e.toString());
        }
    }

    /**
     * @see BApplication#_initializeCustomActions()
     */
    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("helios.comptes.analyseBudgetaire.modifierAnalyseBudgetaire",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.analyseBudgetaire.supprimerAnalyseBudgetaire",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.analyseBudgetaire.afficherAnalyseBudgetaire",
                FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.comptes.ecriture.listerEcritures", FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.comptes.exerciceComptable.choisirExercice", FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.comptes.ecritureModele.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.process.processImportEcritures.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.print.imprimer.balanceComptes", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.analyseBudgetaire", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.pertesProfits", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.bilan", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.planComptable", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.grandLivre", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.balMvtJournal", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.print.imprimer.listeEcritures", FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.comptes.journal.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.comptes.journal.comptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.journal.exComptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.journal.annuler", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.journal.extourner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.journal.afficherJournal", FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.comptes.analyseBudgetaire.afficherAnalyseBudgetaire",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.classifications.liaisonCompteClasse.ajouterLink",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.classifications.liaisonCompteClasse.supprimerLink",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.comptes.mandat.ajouterMandat", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.comptes.mouvementCompte.listerMouvements", FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.comptes.periodeComptable.boucler", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.periodeComptable.imprimerReleveAVS", FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.comptes.periodeComptable.envoyerAnnonces", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.periodeComptable.envoyerAnnoncesOFAS", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.periodeComptable.importerJournalDebit", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.consolidation.processImportConsolidation.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.consolidation.processExportConsolidation.afficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.consolidation.processResetConsolidation.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.consolidation.processImprimerConsolidationParMois.afficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("helios.consolidation.processImprimerConsolidationParAgence.afficher",
                FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.rente.recapitulation", FWSecureConstants.READ);

        FWAction.registerActionCustom("helios.avs.secteurAVS.ajouterSecteur", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.mapping.mappingComptabiliser.afficherSetIdMandat",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.comptes.mandat.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.classifications.classification.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.classifications.classeCompte.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.classifications.definitionListe.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.libelleStandard.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.comptes.centreCharge.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.consolidation.succursale.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.modeles.modeleEcriture.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.mapping.mappingComptabiliser.chercher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("helios.comptes.analyseBudgetaire.afficherAnalyseBudgetaire",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.classifications.liaisonCompteClasse.ajouterLink",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("helios.classifications.liaisonCompteClasse.supprimerLink",
                FWSecureConstants.UPDATE);

    }

    /**
     * Retourne l'application selon le nom donné. Ce nom doit se trouver dans le fichier de configuration.<br>
     * <tt>applicationExterne.tiers=PYXIS</tt><br>
     * où tiers est le nom à donner en paramètre. Date de création : (04.12.2002 11:25:47)
     * 
     * @param application
     *            le nom de l'application référencé dans le fichier properties
     * @return un object du type {@link globaz.globall.api.BIApplication}
     * @exception si
     *                une exception survient.
     */
    public BIApplication getApplication(String application) throws Exception {
        return GlobazSystem.getApplication(this.getProperty("applicationExterne." + application));
    }

    /**
     * @see globaz.helios.db.interfaces.ITreeListable#getChilds()
     */
    @Override
    public BManager[] getChilds() {
        /* pas de setFor ici, ca ne marche pas ..???... */
        CGMandatManager mandatManager = new CGMandatManager();
        CGPlanComptableAVSManager planAVSManager = new CGPlanComptableAVSManager();

        return new BManager[] { mandatManager, planAVSManager };
    }

    /**
     * @see globaz.helios.db.interfaces.ITreeListable#getLibelle()
     */
    @Override
    public String getLibelle() {
        return CGApplication.DEFAULT_APPLICATION_HELIOS;
    }

    /**
     * Method getProperties.
     * 
     * @return Hashtable
     */
    public Hashtable getProperties() {
        Hashtable table = new Hashtable();
        table.put("application", CGApplication.DEFAULT_APPLICATION_HELIOS);
        return table;
    }

    /**
     * Retourne la session remote de HERMES. Date de création : (13.12.2002 07:35:24)
     * 
     * @param local
     *            la session locale
     * @return la session remote de HERMES
     * @exception si
     *                un erreur survient.
     */
    public BISession getSessionAnnonce(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(CGApplication.KEY_SESSION_HERMES);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(CGApplication.ANNONCE_APPLICATION).newSession(local);
            local.setAttribute(CGApplication.KEY_SESSION_HERMES, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }
}
