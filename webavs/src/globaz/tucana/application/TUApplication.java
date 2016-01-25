package globaz.tucana.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import java.util.Properties;

/**
 * Class applicative de l'application tucana
 * 
 * @author FGo
 * @version 1.0
 */
public class TUApplication extends BApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** définition de la locatité de l'agence */
    public static final String APPLICATION_LOCALITE = "localite";
    public static final String APPLICATION_LOCALITE_CORRESPONDANCE = "localiteCorrespondance";
    /** Nom de l'application */
    public static final String APPLICATION_NAME = "tucana";
    /** Prefix de l'application */
    public static final String APPLICATION_PREFIX = "TU";
    /** Version de l'application */
    public static final String APPLICATION_VERSION = "0.000.001";
    /** code système de l'agence */
    public static final String CS_AGENCE = "csAgence";
    /** Root de l'application */
    public static final String DEFAULT_APPLICATION_ROOT = "tucanaRoot";
    /** Id de l'application */
    public static final String DEFAULT_APPLICATION_TUCANA = "TUCANA";
    /** code système de l'agence */
    public static final String DEFAULT_CANTON = "defaultCanton";

    /**
     * Constructor for AIApplication.
     * 
     * @throws Exception
     *             Exception
     */
    public TUApplication() throws Exception {
        super(TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        // Surcharge les actions de redirection de PYXIS...
        // MainServlet.getMap().put("pyxis.tiers.tiers", AIActionTiersAI.class);

        // Charge les fichiers properties autre que le standard
        loadLabels("TUCANAMessages.properties");
        loadLabels("TUCANATitles.properties");
        loadLabels("TUCANACodeSystem.properties");

        // Load le menu cache
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("menuTucana.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("tucana.journal", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.administration.importationBouclement.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.administration.passageSuppression.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.administration.validationBouclement.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.transfert.export.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.transfert.importation.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.parametrage.groupeCategorie.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("tucana.bouclement.detailInsertionACM.afficher", FWSecureConstants.UPDATE);
    }

    /**
     * @see globaz.globall.db.BApplication#_readProperties(Properties)
     */
    protected void _readProperties(Properties properties) {
    }

}