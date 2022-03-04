package ch.globaz.eform.web.application;

import globaz.framework.menu.FWMenuCache;
import globaz.globall.db.BApplication;

/**
 * Object application définissant l'application EFORM permettant la gestion des eFormulaires P14.
 *
 * @author JJO
 */
public class GFApplication extends BApplication {

    private static final long serialVersionUID = 1L;
    public static final String APPLICATION_NAME = "EFORM";
    public static final String APPLICATION_PREFIX = "GF";
    public static final String DEFAULT_APPLICATION_ROOT = "gfRoot";
    public static final String DEFAULT_APPLICATION_EFORM = "EFORM";

    /**
     * Constructeur
     *
     * @throws Exception
     */
    public GFApplication() throws Exception {
        super(GFApplication.DEFAULT_APPLICATION_EFORM);
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
//        FWMenuCache cache = FWMenuCache.getInstance();
//        cache.addFile("EFORMMenu.xml");
    }

    /**
     * Enregistrement des actions exécutées par le module EFORM
     *
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeCustomActions() {

        // Définition et enregistrement des Custom actions à exécuter dans le module.
    }
}
