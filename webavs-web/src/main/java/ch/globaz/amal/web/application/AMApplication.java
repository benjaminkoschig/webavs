package ch.globaz.amal.web.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import ch.globaz.amal.business.constantes.IAMActions;
import ch.globaz.jade.process.business.conf.JadeProcessConfManager;

public class AMApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_AMAL_REP = "amalRoot";
    public final static String APPLICATION_PREFIX = "AM";
    public final static String DEFAULT_APPLICATION_AMAL = "AMAL";

    public final static String PROPERTY_NAMESPACE_XSD_1 = "xsd.namespace.1";
    public final static String PROPERTY_NAMESPACE_XSD_2 = "xsd.namespace.2";
    public final static String PROPERTY_NAMESPACE_XSD_3 = "xsd.namespace.3";
    public final static String PROPERTY_NAMESPACE_XSD_URL_1 = "xsd.namespace.url.1";
    public final static String PROPERTY_NAMESPACE_XSD_URL_2 = "xsd.namespace.url.2";
    public final static String PROPERTY_NAMESPACE_XSD_URL_3 = "xsd.namespace.url.3";

    public AMApplication() throws Exception {
        super(AMApplication.DEFAULT_APPLICATION_AMAL);
    }

    @Override
    protected void _declareAPI() {

    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("AMALMenu.xml");
        JadeProcessConfManager.registerFile("AMProcess.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom(IAMActions.ACTION_CONTRIBUABLE + ".transferer", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAMActions.ACTION_CONTRIBUABLE + ".fusionner", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAMActions.ACTION_PARAMETRES_COPY_PARAMETRES + ".copyParams",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAMActions.ACTION_SEDEX_CO + ".imprimerListe", FWSecureConstants.READ);
    }
}
