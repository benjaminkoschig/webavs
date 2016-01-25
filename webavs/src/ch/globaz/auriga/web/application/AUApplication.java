package ch.globaz.auriga.web.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;

public class AUApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_AURIGA_PREFIX = "AU";
    public final static String APPLICATION_AURIGA_REP = "aurigaRoot";
    public final static String DEFAULT_APPLICATION_AURIGA = "AURIGA";

    public AUApplication() throws Exception {
        super(AUApplication.DEFAULT_APPLICATION_AURIGA);
    }

    @Override
    protected void _declareAPI() {

    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("AURIGAMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("auriga.decisioncap.decisionCapSearch.afficherCapSearch", FWSecureConstants.READ);
    }

}
