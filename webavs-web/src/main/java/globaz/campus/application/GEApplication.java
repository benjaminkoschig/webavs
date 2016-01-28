package globaz.campus.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import java.util.Properties;

public class GEApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Prefix de l'application */
    public static final String APPLICATION_CAMPUS_PREFIX = "GE";
    /** Racine de l'application */
    public static final String APPLICATION_CAMPUS_ROOT = "campusRoot";
    /** Id de l'application */
    public static final String DEFAULT_APPLICATION_CAMPUS = "CAMPUS";
    // Nouveau format EPFL
    public static final String FORMAT_EPFL = "nouveauFormatEPFL";
    // Properties
    public static final String PROPERTY_PLAN_CAISSE_ETUDIANT = "idplanCaisseEtudiant";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialize l'application campus
     * 
     * @throws Exception
     *             Si l'initialisation de l'application a échoué
     */
    public GEApplication() throws Exception {
        super(GEApplication.DEFAULT_APPLICATION_CAMPUS);
    }

    /**
     * Crée une nouvelle instance de la classe GEApplication.
     * 
     * @param id
     * @throws Exception
     */
    public GEApplication(String id) throws Exception {
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
        cache.addFile("CAMPUSMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("campus.process.processChargementLot.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("campus.process.processValidationAnnonces.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("campus.process.processComptabilisationCI.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("campus.process.processTraitementImpayes.afficher", FWSecureConstants.UPDATE);

    }

    protected void _readProperties(Properties properties) {
    }

    public String getIdPlanCaisseEtudiant() throws Exception {
        return this.getProperty(GEApplication.PROPERTY_PLAN_CAISSE_ETUDIANT);
    }

    public boolean isNouveauFormatEPFL() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(GEApplication.FORMAT_EPFL))) {
            return true;
        } else {
            return false;
        }
    }
}
