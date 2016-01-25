/*
 * Créé en juillet 2009
 */

package globaz.libra.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.libra.servlet.ILIActions;
import java.util.Properties;

/**
 * @author hpe
 */

public class LIApplication extends BApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_LIBRA_REP = "libraRoot";
    public final static String APPLICATION_PREFIX = "LI";
    public final static String DEFAULT_APPLICATION_LIBRA = "LIBRA";

    // ~Constructors----------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIApplication.
     * 
     * @throws Exception
     */
    public LIApplication() throws Exception {
        super(DEFAULT_APPLICATION_LIBRA);
    }

    /**
     * Crée une nouvelle instance de la classe LIApplication.
     * 
     * @param id
     * 
     * @throws Exception
     */
    public LIApplication(String id) throws Exception {
        super(id);
    }

    // ~Methods---------------------------------------------------------------------

    @Override
    protected void _declareAPI() {
    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("LIBRAMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("libra.journalisations.documents.afficherExecuter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("libra.journalisations.echeances.executerAction", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(ILIActions.ACTION_JOURNALISATIONS_RC + ".actionAfficherDossierGed",
                FWSecureConstants.READ);

    }

    protected void _readProperties(Properties properties) {
    }

}
