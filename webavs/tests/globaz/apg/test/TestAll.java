/*
 * Créé le 5 août 05
 */
package globaz.apg.test;

import globaz.apg.application.APApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;

/**
 * <H1>Suite de tests incluant TOUT les tests</H1>
 * 
 * @author dvh
 */
public class TestAll {

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
        return (BSession) ((APApplication) GlobazSystem.getApplication("apg")).newSession("adminuser", "adminuser");
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    // public static Test suite() {
    // TestSuite suite = new TestSuite("Test for globaz.apg.test");
    //
    // // $JUnit-BEGIN$
    // suite.addTest(TestToutesPrestationsAPG2005.suite());
    // suite.addTest(TestToutesPrestationsAPG99.suite());
    // suite.addTest(new TestSuite(TestCalculPrestationMaternite.class));
    // suite.addTest(new TestSuite(TestRepartitionPaiementToutesPrestations.class));
    // suite.addTest(TestACOR2_1_15_IO.suite());
    // suite.addTest(new TestSuite(TestDroitAcquis.class));
    //
    // // $JUnit-END$
    // return suite;
    // }
}
