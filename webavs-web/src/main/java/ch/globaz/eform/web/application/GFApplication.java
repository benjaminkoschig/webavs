package ch.globaz.eform.web.application;

import ch.globaz.eform.web.servlet.GFDemandeServletAction;
import ch.globaz.eform.web.servlet.GFEnvoiServletAction;
import ch.globaz.eform.web.servlet.GFFormulaireServletAction;
import ch.globaz.eform.web.servlet.GFSuiviServletAction;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;

/**
 * Object application définissant l'application EFORM permettant la gestion des eFormulaires P14.
 *
 * @author JJO
 */
public class GFApplication extends BApplication {

    private static final long serialVersionUID = 1L;

    public static final String APPLICATION_ID = "EFORM";
    public static final String APPLICATION_NAME = "EFORM";
    public static final String APPLICATION_PREFIX = "GF";
    public static final String DEFAULT_APPLICATION_ROOT = "eformRoot";
    public static final String EFORM_HOST_FILE_SERVER = "EFormHostService";
    public static final String DA_DOSSIER_HOST_FILE_SERVER = "DaDossierHostService";
    public static final String DA_DOSSIER_SHARE_FILE = "DaDossierShareFile";

    /**
     * Constructeur
     *
     * @throws Exception
     */
    public GFApplication() throws Exception {
        super(GFApplication.APPLICATION_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("EFORMMenu.xml");
    }

    /**
     * Enregistrement des actions exécutées par le module EFORM
     *
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeCustomActions() {
        // Définition et enregistrement des Customs actions à exécuter dans le module.
        FWAction.registerActionCustom(GFFormulaireServletAction.PATH_EFORM + "." + GFFormulaireServletAction.ACTION_TELECHARGER, FWSecureConstants.READ);
        FWAction.registerActionCustom(GFFormulaireServletAction.PATH_EFORM + "." + GFFormulaireServletAction.ACTION_CHANGE_STATUT, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(GFEnvoiServletAction.ACTION_PATH + "." + GFEnvoiServletAction.ACTION_UPLOAD, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(GFEnvoiServletAction.ACTION_PATH + "." + GFEnvoiServletAction.ACTION_REMOVEFILE, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(GFEnvoiServletAction.ACTION_PATH + "." + GFEnvoiServletAction.ACTION_ENVOYER, FWSecureConstants.ADD);
        FWAction.registerActionCustom(GFDemandeServletAction.ACTION_PATH + "." + GFDemandeServletAction.ACTION_ENVOYER, FWSecureConstants.ADD);
        FWAction.registerActionCustom(GFSuiviServletAction.ACTION_PATH + "." + GFSuiviServletAction.ACTION_STATUT, FWSecureConstants.UPDATE);
    }
}
