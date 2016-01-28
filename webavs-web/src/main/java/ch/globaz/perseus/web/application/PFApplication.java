package ch.globaz.perseus.web.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import ch.globaz.jade.process.business.conf.JadeProcessConfManager;
import ch.globaz.perseus.business.constantes.IPFActions;

/**
 * Application des prestations complémentaires famille
 * 
 * @author vyj
 */
public class PFApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_PERSEUS_REP = "perseusRoot";
    public final static String APPLICATION_PREFIX = "PF";
    public final static String DEFAULT_APPLICATION_PERSEUS = "PERSEUS";

    public static final String PROPERTY_GROUPE_PERSEUS_AGENCE = "groupe.agence";
    public static final String PROPERTY_GROUPE_PERSEUS_GESTIONNAIRE = "groupe.perseus.gestionnaire";
    public static final String PROPERTY_PERSEUS_CODESYSTEM_GENREADM_AGENCERI = "perseus.codesystem.genreadm.agenceri";

    /**
     * @throws Exception
     */
    public PFApplication() throws Exception {
        super(PFApplication.DEFAULT_APPLICATION_PERSEUS);
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
        cache.addFile("PERSEUSMenu.xml");
        JadeProcessConfManager.registerFile("PFProcess.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom(IPFActions.ACTION_PCFACCORDEE + ".calculer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPFActions.ACTION_PAIEMENTS + ".activer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPFActions.ACTION_PAIEMENTS + ".desactiver", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPFActions.ACTION_DEMANDE + ".copier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPFActions.ACTION_DOSSIER + ".actionAfficherDossierGed", FWSecureConstants.READ);
        FWAction.registerActionCustom(IPFActions.ACTION_FACTURE + ".actionValider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IPFActions.ACTION_FACTURE + ".actionRestituer", FWSecureConstants.UPDATE);
    }

}
