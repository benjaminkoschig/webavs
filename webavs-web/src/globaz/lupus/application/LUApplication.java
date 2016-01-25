/*
 * Créé le 5 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.application;

import globaz.globall.db.BApplication;
import java.util.Properties;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_LUPUS_REP = "lupusRoot";
    public final static String APPLICATION_PREFIX = "LU";
    public final static String DEFAULT_APPLICATION_LUPUS = "LUPUS";

    /**
     * @param id
     * @throws Exception
     */
    public LUApplication() throws Exception {
        super(DEFAULT_APPLICATION_LUPUS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        // TODO Raccord de méthode auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        // TODO Raccord de méthode auto-généré
    }

    @Override
    protected void _initializeCustomActions() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_readProperties(java.util.Properties)
     */
    protected void _readProperties(Properties properties) {
        // TODO Raccord de méthode auto-généré
    }
}