/**
 * 
 */
package ch.globaz.amal.businessimpl.services.pyxis;

import junit.framework.Test;
import junit.framework.TestSuite;
import ch.globaz.amal.businessimpl.services.pyxis.repriseTiers.SuiteTestsRepriseTiers;

/**
 * @author dhi
 * 
 */
public class SuiteTestsServicesPyxis {
    /**
     * Initialisation des tests
     * 
     * @return suite de test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.amal.businessimpl.services.pyxis");
        // $JUnit-BEGIN$
        suite.addTest(SuiteTestsRepriseTiers.suite());
        // $JUnit-END$
        return suite;
    }

}
