package globaz.lyra.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.lyra.servlet.ILYActions;

/**
 * @author PBA
 */
public class LYApplication extends BApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_LYRA_REP = "lyraRoot";
    public final static String APPLICATION_PREFIX = "LY";
    public final static String DEFAULT_APPLICATION_LYRA = "LYRA";

    public LYApplication() throws Exception {
        super(LYApplication.DEFAULT_APPLICATION_LYRA);
    }

    public LYApplication(String id) throws Exception {
        super(id);
    }

    @Override
    protected void _declareAPI() {
    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("LYRAMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom(ILYActions.ACTION_HISTORIQUE + ".supprimerHistorique", FWSecureConstants.REMOVE);
        FWAction.registerActionCustom(ILYActions.ACTION_PREPARATION + ".reAfficher", FWSecureConstants.READ);
    }
}
