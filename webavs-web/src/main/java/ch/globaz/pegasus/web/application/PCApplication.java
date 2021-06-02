package ch.globaz.pegasus.web.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.prestation.application.PRAbstractApplication;
import ch.globaz.jade.process.business.conf.JadeProcessConfManager;
import ch.globaz.pegasus.business.constantes.IPCActions;

/**
 * @author bsc
 */
public class PCApplication extends PRAbstractApplication {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_PEGASUS_REP = "pegasusRoot";
    public final static String APPLICATION_PREFIX = "PC";
    public final static String DEFAULT_APPLICATION_PEGASUS = "PEGASUS";

    public static final String PROPERTY_GROUPE_PEGASUS_GESTIONNAIRE = "groupe.pegasus.gestionnaire";

    // public static final String PROPERTY_PEGASUS_REVISION_MONTHS_BETWEEN = "pegasus.revision.monthsBetween";

    // ~Constructors----------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe PCApplication.
     * 
     * @throws Exception
     */
    public PCApplication() throws Exception {
        super(PCApplication.DEFAULT_APPLICATION_PEGASUS);
    }

    public PCApplication(String id) throws Exception {
        super(id);
    }

    // ~Methods---------------------------------------------------------------------

    @Override
    protected void _declareAPI() {
    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("PEGASUSMenu.xml");
        JadeProcessConfManager.registerFile("PCProcess.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        // FWAction.registerActionCustom(IPCActions.ACTION_DROIT_JOINT_DEMANDES_JOINT_DOSSIERS_JOINT_TIERS
        // + ".creerDroitInitial", FWSecureConstants.UPDATE);
        //
        //
        // FWAction.registerActionCustom(IPCActions.ACTION_DROIT_SAISIE_MARCHANDISE_STOCK_AJAX
        // + ".recharger", FWSecureConstants.UPDATE);
        // FWAction.registerActionCustom(IPCActions.ACTION_DROIT_SAISIE_MARCHANDISE_STOCK_AJAX
        // + ".sauvegarder", FWSecureConstants.UPDATE);
        // FWAction.registerActionCustom(IPCActions.ACTION_DROIT_SAISIE_MARCHANDISE_STOCK_AJAX
        // + ".ajouterNouveau", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IPCActions.ACTION_DROIT + ".synchroniser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPCActions.ACTION_CALCUL_DROIT + ".calculer", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IPCActions.ACTION_DROIT + ".modifierDateAnnonce", FWSecureConstants.UPDATE);
        // pegasus.downloadDocument.downloadDocument.download
        FWAction.registerActionCustom(IPCActions.ACTION_DOWNLOAD + ".download", FWSecureConstants.READ);
        // préparer decisions
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_AC_PREPARATION, FWSecureConstants.ADD);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_SUP_PREPARATION, FWSecureConstants.ADD);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_REF_PREPARATION, FWSecureConstants.ADD);
        // prévalider décision
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_AC_PREVALIDATION, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_SUP_PREVALIDATION, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_REF_PREVALIDATION, FWSecureConstants.UPDATE);
        // validation decision
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_AC_VALIDATION, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_SUP_VALIDATION, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_REF_VALIDATION, FWSecureConstants.UPDATE);
        // imprimer sup
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_SUP_IMPRIMER, FWSecureConstants.UPDATE);

        // Afficher décision ac-re-sup
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_APRES_CALCUL_DETAIL, FWSecureConstants.READ);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_REFUS_DETAIL, FWSecureConstants.READ);
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_SUPPRESSION_DETAIL, FWSecureConstants.READ);
        // Dévalidation après calcul
        FWAction.registerActionCustom(IPCActions.ACTION_DECISION_DEVALIDER, FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IPCActions.ACTION_RESTITUTION_PC+".comptabiliser", FWSecureConstants.UPDATE);
    }
}
