package globaz.aquila.util;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;

/**
 * Utilitaire pour la GED
 */
public final class COGedUtils {

    /**
     * Retourne le dossier utilisé pour la ged du contentieux
     * 
     * @param application
     * @return
     */
    public final static String getFolder(BApplication application) {
        try {
            if (application == null) {
                application = (COApplication) GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            }
            return application.getProperty("ged.folder.type", "");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le service utilisé pour la ged du contentieux
     * 
     * @param application
     * @return
     */
    public final static String getService(BApplication application) {
        try {
            if (application == null) {
                application = (COApplication) GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            }
            return application.getProperty("ged.servicename.id", "");
        } catch (Exception e) {
            return "";
        }
    }

}
