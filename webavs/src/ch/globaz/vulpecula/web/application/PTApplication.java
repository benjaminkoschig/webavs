package ch.globaz.vulpecula.web.application;

import globaz.framework.menu.FWMenuCache;
import globaz.globall.db.BApplication;
import java.util.Properties;
import ch.globaz.jade.process.business.conf.JadeProcessConfManager;
import ch.globaz.vulpecula.application.ApplicationConstants;

/**
 * Application des postes des travail
 * 
 * @author jpa
 */
public class PTApplication extends BApplication {

    public final static String DEFAULT_APPLICATION_VULPECULA = "VULPECULA";
    public static final String PROPERTY_GED_SERVICE_NAME = "ged.service.name";

    /**
     * @throws Exception
     */
    public PTApplication() throws Exception {
        super(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("VULPECULAMenu.xml");

        // initialisation de process
        JadeProcessConfManager.registerFile("PTProcess.xml");
    }

    @Override
    protected void _initializeCustomActions() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_readProperties(java.util.Properties)
     */
    protected void _readProperties(final Properties properties) {
    }
}
