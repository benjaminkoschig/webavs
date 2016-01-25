package ch.globaz.orion;

import globaz.draco.process.DSProcessValidation;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.pavo.process.CIDeclaration;
import java.math.BigDecimal;
import ch.globaz.orion.service.EBEbusinessCIAccessImplementation;
import ch.globaz.orion.service.EBEbusinessDSAccessImplementation;

public class EBApplication extends BApplication {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_ID = "ORION";
    public static final String APPLICATION_NAME = "EBUSINESS_APP";
    public static final String APPLICATION_PREFIX = "EB";
    public static final String APPLICATION_ROOT = "orionRoot";
    public static final String MODELS_EXCELML = "model/excelml";
    public static final String PROP_WANT_LINK_DRACO = "wantLinkDraco";
    public static final String PROPERTY_PREVISION_MASSEANNUELLEMAX = "prevision.masseAnnuelleMax";
    public static final String WANT_INSC_PRE_REMPLISSAGE = "inscr.preRempliDan";

    public EBApplication() throws Exception {
        super(EBApplication.APPLICATION_ID);
    }

    @Override
    protected void _declareAPI() {

    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("ORIONMenu.xml");
        // Initialisation de la classe d'accès à EBusiness pour les CI
        CIDeclaration.initEbusinessAccessInstance(new EBEbusinessCIAccessImplementation());
        DSProcessValidation.initEbusinessAccessInstance(new EBEbusinessDSAccessImplementation());
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("orion.dan.danPreRemplissage.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("orion.dan.danPreRemplissageMasse.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("orion.acompte.previsionAcompte.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("orion.swissdec.pucsValidationList.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("orion.swissdec.pucsValidationDetail.accepter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("orion.swissdec.pucsValidationDetail.refuser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("orion.swissdec.pucsValidationDetail.annulerRefus", FWSecureConstants.UPDATE);
    }

    /**
     * 
     * @return la masse qui indieuqe le changement de périodicité anneulle en mensuelle
     */
    public BigDecimal getMasseAnnuelleMaxPourPeriodiciteAnnuelle() {
        try {
            return new BigDecimal(this.getProperty(EBApplication.PROPERTY_PREVISION_MASSEANNUELLEMAX));
        } catch (Exception e) {
            return new BigDecimal("200000");
        }
    }

    /**
     * Renvoie true on désire pré-remplir la DAN après l'inscription
     * 
     * @return true si on désire le lien avec draco
     */
    public boolean wantInscPreRemplissage() {
        return new Boolean(this.getProperty(EBApplication.WANT_INSC_PRE_REMPLISSAGE, "false").trim());

    }

    /**
     * Renvoie true on désire le lien avec draco
     * 
     * @return true si on désire le lien avec draco
     */
    public String wantLinkDraco() {
        return this.getProperty(EBApplication.PROP_WANT_LINK_DRACO, "true").trim();

    }

}
