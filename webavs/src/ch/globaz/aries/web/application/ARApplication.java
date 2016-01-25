package ch.globaz.aries.web.application;

import globaz.framework.menu.FWMenuCache;
import globaz.globall.db.BApplication;

public class ARApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_ARIES_PREFIX = "AR";
    public final static String APPLICATION_ARIES_REP = "ariesRoot";
    public final static String DEFAULT_APPLICATION_ARIES = "ARIES";

    public ARApplication() throws Exception {
        super(ARApplication.DEFAULT_APPLICATION_ARIES);
    }

    @Override
    protected void _declareAPI() {

    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("ARIESMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {

    }

}
