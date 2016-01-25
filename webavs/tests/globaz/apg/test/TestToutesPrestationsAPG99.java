/*
 * Créé le 5 août 05
 */
package globaz.apg.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <H1>Suite de tests pour le calcul des prestations apg 99</H1>
 * 
 * @author dvh
 */
public class TestToutesPrestationsAPG99 {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for globaz.apg.test");

        // // $JUnit-BEGIN$
        // suite.addTest(new TestSuite(TestRevenuMoyenDeterminant99.class));
        // suite.addTest(new TestSuite(TestCalculPrestationAPGRevision99ServiceNormal.class));

        // $JUnit-END$
        return suite;
    }
}
