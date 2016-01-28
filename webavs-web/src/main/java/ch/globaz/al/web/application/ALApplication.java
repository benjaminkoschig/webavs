package ch.globaz.al.web.application;

import globaz.al.helpers.decision.ALDecisionHelper;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.context.JadeThread;
import globaz.jade.editing.JadeEditing;
import ch.globaz.al.businessimpl.ctrlexport.PrestationExportableController;

/**
 * Object application qui défini l'application WEB@AF
 * 
 * @author VYJ
 */
public class ALApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String APPLICATION_NAME = "al";
    public static final String APPLICATION_PREFIX = "AL";
    public static final String DEFAULT_APPLICATION_ROOT = "alRoot";
    public static final String DEFAULT_APPLICATION_WEBAF = "AL";

    /**
     * @throws Exception
     */
    public ALApplication() throws Exception {
        super(ALApplication.DEFAULT_APPLICATION_WEBAF);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {

        try {
            JadeEditing.getInstance().register(ALDecisionHelper.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("menuAL.xml");
        try {
            PrestationExportableController.xmlExportRules = JadeXmlReader
                    .parseFile(PrestationExportableController.EXPORT_RULES_FILENAME);
        } catch (Exception e) {
            JadeThread.logWarn(ALApplication.class.getName(), "al.initapplication.exportation.xmlerror");
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_initializeCustomActions()
     */
    @Override
    protected void _initializeCustomActions() {
        // Ici sont définis les customs et les droits nécessaires pour les
        // exécuter, sinon elles n'apparaissent pas dans le menu principal /
        // options
        FWAction.registerActionCustom("al.dossier.dossierMain.copier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("al.droit.droit.toformation", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("al.adi.decompteAdi.calculer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("al.adi.decompteAdi.generer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("al.rafam.annonceRafam.creer68c", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("al.dossier.dossier.lierDossierLibra", FWSecureConstants.UPDATE);
    }
}
