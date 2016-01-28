/*
 * Créé le 17 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package globaz.hera.test;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author hpe
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class TestHera {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static BSession createSession() throws Exception {
        return (BSession) (GlobazSystem.getApplication("HERA")).newSession("globaz", "glob4az");
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test sur les interfaces d'Hera");

        // $JUnit-BEGIN$

        // suite.addTest(new TestSuite(TestHeraInterfaces0100.class));
        // suite.addTest(new TestSuite(TestHeraInterfaces0101.class));
        // suite.addTest(new TestSuite(TestHeraInterfaces0102.class));
        // suite.addTest(new TestSuite(TestHeraInterfaces0103.class));
        // suite.addTest(new TestSuite(TestHeraInterfaces0104.class));
        // suite.addTest(new TestSuite(TestHeraInterfaces0105.class));

        // $JUnit-END$

        return suite;
    }

}
