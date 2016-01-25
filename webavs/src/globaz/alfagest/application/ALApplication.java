package globaz.alfagest.application;

import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.jade.log.JadeLogger;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Application ALFAGEST (Allocations familiales gestion)
 * @deprecated ALFA-Gest n'est plus supporté
 */
@Deprecated
public class ALApplication extends globaz.globall.db.BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // nom de l'application
    public final static String DEFAULT_APPLICATION_ALFAGEST = "ALFAGEST";
    // répertoire racine de l'application
    public final static String DEFAULT_APPLICATION_ALFAGEST_ROOT = "alfagestRoot";

    // application ALFAGEST
    private globaz.globall.api.BIApplication applicationALFAGEST = null;
    // session ALFAGEST
    private globaz.globall.api.BISession sessionALFAGEST = null;

    /**
     * Constructeur par défaut
     * 
     * @exception java.lang.Exception si l'initialisation de l'application a échouée
     */
    public ALApplication() throws Exception {
        super(DEFAULT_APPLICATION_ALFAGEST);
    }

    /**
     * Constructeur avec id de l'application
     * 
     * @param id l'id de l'application
     * @exception java.lang.Exception si l'initialisation de l'application a échouée
     */
    public ALApplication(String id) throws Exception {
        super(id);
    }

    /**
     * Déclare les APIs de l'application
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * Initialise l'application
     * 
     * @exception java.lang.Exception si l'initialisation de l'application a échouée
     */
    @Override
    protected void _initializeApplication() throws Exception {
    }

    /**
     * Lit les propriétés de l'application
     * 
     * @param properties les propriétés lues par le système
     */
    protected void _readProperties(java.util.Properties properties) {
    }

    /**
     * Retourne une session ALFAGEST, ou null si impossible
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationALFAGEST() {
        // si l'application est nouvelle
        if (applicationALFAGEST == null) {
            try {
                applicationALFAGEST = GlobazSystem.getApplication(DEFAULT_APPLICATION_ALFAGEST);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

        return applicationALFAGEST;
    }

    /**
     * Retourne une session ALFAGEST, ou null si impossible
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionOsiris(BISession session) {
        // si la session est nouvelle
        if (sessionALFAGEST == null) {
            try {
                sessionALFAGEST = getApplicationALFAGEST().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

        return sessionALFAGEST;
    }

    @Override
    protected void _initializeCustomActions() {
        // aucune action dans ce module car il n'y a pas de partie affichage

    }
}