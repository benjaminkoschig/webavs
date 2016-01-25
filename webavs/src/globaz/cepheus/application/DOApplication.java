/*
 * Cr�� le 22 sept. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.application;

import globaz.framework.menu.FWMenuCache;
import globaz.prestation.application.PRAbstractApplication;
import java.util.Properties;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 * 
 *         <p>
 *         Application CEPHEUS
 *         </p>
 */
public class DOApplication extends PRAbstractApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Le r�pertoire racine de l'application */
    public static final String APPLICATION_CEPHEUS_REP = "cepheusRoot";

    /** Le pr�fixe de l'application */
    public static final String APPLICATION_PREFIX = "DO";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /** Le nom de l'application */
    public static final String DEFAULT_APPLICATION_CEPHEUS = "CEPHEUS";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Cr�e une nouvelle instance de la classe DOApplication.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public DOApplication() throws Exception {
        super(DEFAULT_APPLICATION_CEPHEUS);
    }

    /**
     * Cr�e une nouvelle instance de la classe DOApplication.
     * 
     * @param id
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public DOApplication(String id) throws Exception {
        super(id);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /**
     * (non-Javadoc)
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("CEPHEUSMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        // TODO Auto-generated method stub

    }

    /**
     * (non-Javadoc)!
     * 
     * @param arg0
     *            DOCUMENT ME!
     * 
     * @see globaz.globall.db.BApplication#_readProperties(java.util.Properties)
     */
    protected void _readProperties(Properties arg0) {
        // TODO Raccord de m�thode auto-g�n�r�
    }
}
