package globaz.lacerta.application;

import globaz.framework.menu.FWMenuCache;
import globaz.globall.db.BApplication;
import java.util.Properties;

public class LAApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Prefix de l'application */
    public static final String APPLICATION_LACERTA_PREFIX = "LA";
    /** Racine de l'application */
    public static final String APPLICATION_LACERTA_ROOT = "lacertaRoot";
    /** Id de l'application */
    public static final String DEFAULT_APPLICATION_LACERTA = "LACERTA";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialize l'application campus
     * 
     * @throws Exception
     *             Si l'initialisation de l'application a échoué
     */
    public LAApplication() throws Exception {
        super(DEFAULT_APPLICATION_LACERTA);
    }

    /**
     * Crée une nouvelle instance de la classe GEApplication.
     * 
     * @param id
     * @throws Exception
     */
    public LAApplication(String id) throws Exception {
        this();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _declareAPI() {
    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("LACERTAMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        // TODO Auto-generated method stub

    }

    protected void _readProperties(Properties properties) {
    }
}
